package com.zionscape.server.world.shops;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zionscape.server.Config;
import com.zionscape.server.Server;
import com.zionscape.server.model.content.grandexchange.GrandExchange;
import com.zionscape.server.model.content.minigames.quests.QuestHandler;
import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.commands.impl.Interface;
import com.zionscape.server.util.CollectionUtil;
import com.zionscape.server.util.DatabaseUtil;
import com.zionscape.server.world.shops.impl.ItemCurrency;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.*;

public final class Shops {

	public static final int UNTRADABLES_SHOP = 999;
	public static final int SKILLCAPE_SHOP_ID = 9;
	public static final int MASTER_SKILLCAPE_SHOP_ID = 14;
	private static final int MAX_SHOP_ITEMS = 40;
	private static final int REPLENISH_TIME = 300000;
	private static final int REMOVAL_OF_GENERAL_STORE_ITEMS = 3 * 60 * 1000;
	private static final Map<Integer, Shop> shops = new HashMap<>();

	/**
	 *
	 */
	public static void process() {

		long timeStamp = System.currentTimeMillis();

		shops.entrySet().stream().forEach(x -> {

			boolean updated = false;
			Shop shop = x.getValue();

			if (shop.isGeneralStore()) {
				Iterator<ShopItem> itr = shop.getItems().iterator();
				while (itr.hasNext()) {
					ShopItem shopItem = itr.next();

					if (!shopItem.isPlayerItem()) {
						continue;
					}

					ShopItem.GeneralStoreShopItem i = (ShopItem.GeneralStoreShopItem) shopItem;
					if (timeStamp - i.getPlacedAt() > REMOVAL_OF_GENERAL_STORE_ITEMS) {
						itr.remove();
						updated = true;
					}
				}
			}

			for (ShopItem shopItem : shop.getItems()) {
				if (shopItem.isPlayerItem()) {
					continue;
				}
				if (shopItem.getAmount() >= shopItem.getMaxStock()) {
					continue;
				}
				if (timeStamp - shopItem.getLastReplenished() < (shop.getReplenishTime() > 0 ? shop.getReplenishTime() : REPLENISH_TIME)) {
					continue;
				}
				shopItem.setLastReplenished(System.currentTimeMillis());
				shopItem.getItem().setAmount(shopItem.getAmount() + 1);
				updated = true;
			}

			if (updated) {
				for (Player plr : PlayerHandler.players) {
					if (plr == null || !plr.attributeExists("shop") || plr.getAttribute("shop") != shop) {
						continue;
					}
					sendInventory(plr, shop);
				}
			}

		});
	}

	/**
	 *
	 */
	public static void load() throws Exception {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		File directory = new File("./config/shops/");

		if (directory == null) {
			throw new RuntimeException("shops directory does not exist");
		}

		for (File f : directory.listFiles()) {

			// skip logs directory
			if (f.isDirectory()) {
				continue;
			}

			JsonShop jsonShop = gson.fromJson(FileUtils.readFileToString(f), JsonShop.class);
			//FileUtils.writeStringToFile(new File("./Data/shops/" + jsonShop.getId() + " " + jsonShop.getName().toLowerCase() + ".json"), gson.toJson(jsonShop));

			String args[] = jsonShop.getCurrency().split(":");

			Currency currency;
			if (args.length > 1) {
				Class<?> clazz = Class.forName("com.zionscape.server.world.shops.impl." + args[0]);
				Constructor<?> constructor = clazz.getConstructor(String.class);
				currency = (Currency) constructor.newInstance(args[1]);
			} else {
				currency = (Currency) Class.forName("com.zionscape.server.world.shops.impl." + args[0]).newInstance();
			}

			Shop shop = new Shop(jsonShop.getId(), jsonShop.getName(), currency, jsonShop.generalStore(), jsonShop.buysItems(), jsonShop.getReplenishTime(), jsonShop.isBuyBackFullPrice());
			for (JsonItem item : jsonShop.getItems()) {
				shop.getItems().add(new ShopItem(new Item(item.getId(), item.getAmount()), item.getAmount(), item.getPrice()));
			}
			shops.put(jsonShop.getId(), shop);
		}

		System.out.println("loaded " + shops.size() + " shops.");
	}

	/**
	 * @param player
	 * @param id
	 */
	public static void open(Player player, int id) {
		if ((id == 11 || id == 12) && !player.ironman) {
			player.sendMessage("This is for ironmen only.");
			return;
		}

		if (GrandExchange.usingGrandExchange(player)) {
			return;
		}

		if(id == 77 || id == 78 || id == 15) {
			player.sendMessage("You have " + player.donatorPoint + " points to spend.");
		}

		// 11 is the ironman store
		if (player.ironman && CollectionUtil.getIndexOfValue(Config.ENABLED_IRON_MAN_SHOP_IDS, id) == -1) {
			player.sendMessage("You cannot do this on an ironman account.");
			return;
		}

		if (!shops.containsKey(id) && id != UNTRADABLES_SHOP) {
			throw new RuntimeException("no shop with the id " + id + " exists");
		}

		Shop shop;
		if(id == UNTRADABLES_SHOP) {
			shop = new Shop(UNTRADABLES_SHOP, "Untradables Shop", new ItemCurrency(Integer.toString(995)), false, false, 0, false);
			for(Item item : player.getData().shopUntradables) {

				int price = -1;
				for(int i = 0; i < Config.UNTRADABLE_SHOP_ITEMS.length; i++) {
					if(Config.UNTRADABLE_SHOP_ITEMS[i][0] == item.getId()) {
						price = Config.UNTRADABLE_SHOP_ITEMS[i][1];
					}
				}

				if(price == -1) {
					continue;
				}

				shop.getItems().add(new ShopItem(item, 0, price, true));
			}
		} else {
			shop = shops.get(id);
		}

		player.setAttribute("shop", shop);

		player.getItems().resetItems(3823);
		Shops.sendInventory(player, shop);
		player.getPA().sendFrame126(shop.getName(), 3901);
		player.getPA().sendFrame248(3824, 3822);
	}

	/**
	 * @param player
	 */
	public static void close(Player player) {
		player.removeAttribute("shop");
		player.getPA().closeAllWindows();
	}

	/**
	 * @param player
	 * @param shop
	 */
	private static void sendInventory(Player player, Shop shop) {

		List<ShopItem> items = new ArrayList<>();

		if (shop.getId() == MASTER_SKILLCAPE_SHOP_ID) {
			for (ShopItem item : shop.getItems()) {
				if (player.xp[getMastercapeSkillIndex(item.getId())] >= 200_000_000) {
					items.add(item);
				}
			}
		} else if (shop.getId() == SKILLCAPE_SHOP_ID) {
			for (ShopItem item : shop.getItems()) {
				if (item.getId() == 9813) { // quest cape
					if (player.questPoints >= QuestHandler.MAX_QUEST_POINTS) {
						items.add(item);
					}
					continue;
				}
				if (player.xp[getSkillcapeSkillIndex(item.getId())] >= 13_034_431) {
					items.add(item);
				}
			}
		} else {
			items.addAll(shop.getItems());
		}

		player.getOutStream().createFrameVarSizeWord(53);
		player.getOutStream().writeWord(3900);
		player.getOutStream().writeWord(items.size());

		items.stream().forEach(x -> {
			if (x.getItem().getAmount() > 254 || x.getItem().getAmount() < 0) {
				player.getOutStream().writeByte(255);
				player.getOutStream().writeDWord_v2(x.getItem().getAmount());
			} else {
				player.getOutStream().writeByte(x.getItem().getAmount());
			}
			player.getOutStream().writeWordBigEndianA(x.getItem().getId() + 1);

		});

		player.getOutStream().endFrameVarSizeWord();
	}


	/**
	 * @param player
	 * @param itemId
	 * @param amount
	 * @param slot
	 */

	public static void buyItem(Player player, int itemId, int amount, int slot) {
		if (!player.attributeExists("shop")) {
			return;
		}
		Shop shop = (Shop)player.getAttribute("shop");
		if (shop == null) {
			return;
		}

		if (player.openInterfaceId != 3824) {
			return;
		}

		Optional<ShopItem> itemOptional = shop.getItems().stream().filter(x -> x.getItem().getId() == itemId).findFirst();
		if (!itemOptional.isPresent()) {
			return;
		}
		ShopItem shopItem = itemOptional.get();

		if (!shopItem.hasInfinityStock() && amount > shopItem.getAmount()) {

			if (shopItem.getAmount() == 0) {
				player.sendMessage("The shop has ran out of stock.");
				return;
			}

			amount = shopItem.getAmount();
		}

		int freeSlots = player.getItems().freeInventorySlots();
		if (ItemUtility.isStackable(itemId)) {
			if (!player.getItems().playerHasItem(itemId) && freeSlots == 0) {
				player.sendMessage("You don't have enough inventory space.");
				return;
			}
		} else {
			if (freeSlots == 0) {
				player.sendMessage("You don't have enough inventory space.");
				return;
			}
			if (amount > player.getItems().freeInventorySlots()) {
				amount = player.getItems().freeInventorySlots();
			}
		}

		int itemCost = shopItem.getPrice() > 0 ? shopItem.getPrice() : shop.getCurrency().buyValue(itemId);
		int currencyAmount = shop.getCurrency().has(player);

		int maxPurchaseAmount = (int) Math.floor(Integer.MAX_VALUE / itemCost);
		if (amount > maxPurchaseAmount) {
			amount = maxPurchaseAmount;
		}

		int totalCost = amount * itemCost;
		if (totalCost > currencyAmount) {
			player.sendMessage("You don't have enough " + shop.getCurrency().getName() + ".");

			if (currencyAmount == 0) {
				return;
			}

			amount = currencyAmount / itemCost;

			if (amount == 0) {
				return;
			}

			totalCost = amount * itemCost;
		}

		// reset from refilling straight away
		shopItem.setLastReplenished(System.currentTimeMillis());

		if (!shopItem.hasInfinityStock()) {


			if (shopItem.isPlayerItem()) {
				shopItem.getItem().setAmount(shopItem.getAmount() - amount);

				if (shopItem.getAmount() == 0 && shopItem.isPlayerItem()) {
					shop.getItems().removeIf(x -> x == shopItem);
				}
			}

			for (Player plr : PlayerHandler.players) {
				if (plr == null || !plr.attributeExists("shop") || plr.getAttribute("shop") != shop) {
					continue;
			}
				sendInventory(plr, shop);
			}
		}

		if(shop.getId() == UNTRADABLES_SHOP) {
			updateUntradables(player, shop);
		}

		shop.getCurrency().remove(player, totalCost);

		player.getItems().addItem(itemId, amount);
		if (shop.getId() == SKILLCAPE_SHOP_ID) {
			player.getItems().addItem(getSkillcapeHood(itemId), 1);
		}

		if (shop.getId() == 99) {
			player.getItems().addItem(itemId + 1, amount);
		}

		addLog(player, shop.getId(), itemId, amount, LogType.BOUGHT);

		player.getItems().resetItems(3823);
	}

	/**
	 * @param player
	 * @param itemId
	 * @param amount
	 * @param slot
	 */


	public static void sellItem(Player player, int itemId, int amount, int slot) {
		if (!player.getItems().playerHasItem(itemId)) {
			return;
		}
		if (itemId == 995) {
			player.sendMessage("You can't sell this item.");
			return;
		}
		if (player.openInterfaceId != 3824) {
			return;
		}
		for (int i : Config.UNTRADABLE_ITEM_IDS) {
			if (i == itemId) {
				player.sendMessage("You can't sell this item.");
				return;
			}
		}
		if (!player.attributeExists("shop")) {
			return;
		}
		Shop shop = (Shop)player.getAttribute("shop");
		if (shop == null) {
			return;
		}

		if (!shop.buysItems()) {
			player.sendMessage("You can't sell this item to this shop.");
			return;
		}

		Optional<ShopItem> itemOptional = shop.getItems().stream().filter(x -> x.getItem().getId() == itemId).findFirst();
		if (!shop.isGeneralStore() && !itemOptional.isPresent()) {
			player.sendMessage("You can't sell this item to this shop.");
			return;
		}

		ShopItem shopItem = null;
		if (itemOptional.isPresent()) {
			shopItem = itemOptional.get();
		}

		int playerAmount = player.getItems().getItemAmount(itemId);
		if (amount > playerAmount) {
			amount = playerAmount;
		}

		if (shop.getCurrency() instanceof ItemCurrency) {
			if (player.getItems().freeInventorySlots() == 0 && shop.getCurrency().has(player) == 0) {
				player.sendMessage("You do not have enough inventory space.");
				return;
			}
		}

		if (shop.isGeneralStore()) {
			if (itemOptional.isPresent()) {
				if (shopItem instanceof ShopItem.GeneralStoreShopItem) {
					shopItem.getItem().setAmount(shopItem.getAmount() + amount);
				}
			} else {
				if (shop.getItems().size() >= MAX_SHOP_ITEMS) {
					player.sendMessage("The shop is currently full.");
					return;
				}
				shop.getItems().add(new ShopItem.GeneralStoreShopItem(new Item(itemId, amount), System.currentTimeMillis()));
			}
		}

		for (Player plr : PlayerHandler.players) {
			if (plr == null || !plr.attributeExists("shop") || plr.getAttribute("shop") != shop) {
				continue;
			}
			sendInventory(plr, shop);
		}

		int value;
		if (itemOptional.isPresent() && shopItem.getPrice() > 0) {
			if (shop.buyBackFullPrice()) {
				value = shopItem.getPrice();
			} else {
				value = (int) Math.floor(shopItem.getPrice() * 0.75);
			}
		} else {
			value = shop.getCurrency().sellValue(itemId, shop.buyBackFullPrice());
		}

		long totalValue = (long) value * (long) amount;
		if (totalValue > Integer.MAX_VALUE) {
			totalValue = Integer.MAX_VALUE;
		}

		addLog(player, shop.getId(), itemId, amount, LogType.SOLD);

		shop.getCurrency().add(player, (int) totalValue);
		player.getItems().deleteItem(itemId, amount);
		player.getItems().resetItems(3823);
	}

	/**
	 * @param player
	 * @param itemId
	 */
	public static void buyItemPrice(Player player, int itemId) {
		if (!player.attributeExists("shop")) {
			return;
		}
		Shop shop = (Shop)player.getAttribute("shop");
		if (shop == null) {
			return;
		}

		Optional<ShopItem> itemOptional = shop.getItems().stream().filter(x -> x.getItem().getId() == itemId).findFirst();
		if (!itemOptional.isPresent()) {
			return;
		}

		ShopItem item = itemOptional.get();

		int value;
		if (item.getPrice() > 0) {
			value = item.getPrice();
		} else {
			value = shop.getCurrency().buyValue(itemId);
		}

		player.sendMessage(ItemUtility.getName(itemId) + ": currently costs " + NumberFormat.getInstance().format(value) + " " + shop.getCurrency().getName() + ".");
	}

	/**
	 * @param player
	 * @param itemId
	 */
	public static void sellItemPrice(Player player, int itemId) {
		if (!player.attributeExists("shop")) {
			return;
		}

		if (itemId == 995) {
			player.sendMessage("You can't sell this item.");
			return;
		}

		for (int i : Config.UNTRADABLE_ITEM_IDS) {
			if (i == itemId) {
				player.sendMessage("You can't sell this item.");
				return;
			}
		}

		Shop shop = (Shop)player.getAttribute("shop");
		if (shop == null) {
			return;
		}
		if (!shop.buysItems()) {
			player.sendMessage("You cannot sell items to this shop.");
			return;
		}

		Optional<ShopItem> itemOptional = shop.getItems().stream().filter(x -> x.getItem().getId() == itemId).findFirst();
		if (!shop.isGeneralStore() && !itemOptional.isPresent()) {
			player.sendMessage("You cannot sell this item to this shop.");
			return;
		}

		int sellValue = 0;
		if (itemOptional.isPresent()) {
			int price = itemOptional.get().getPrice();
			if (price > 0) {
				if (shop.buyBackFullPrice()) {
					sellValue = price;
				} else {
					sellValue = (int) Math.floor(price * 0.75);
				}
			}
		}
		if (sellValue <= 0) {
			sellValue = shop.getCurrency().sellValue(itemId, shop.buyBackFullPrice());
		}

		player.sendMessage(ItemUtility.getName(itemId) + ": shop will buy for " + NumberFormat.getInstance().format(sellValue) + " " + shop.getCurrency().getName() + ".");
	}


	private static int getSkillcapeHood(int itemId) {
		switch (itemId) {
			case 12169:
			case 12170:
				return 12171;

			case 9747:
			case 9748:
				return 9749;
			case 9750:
			case 9751:
				return 9752;
			case 9753:
			case 9754:
				return 9755;
			case 9756:
			case 9757:
				return 9758;
			case 9759:
			case 9760:
				return 9761;
			case 9762:
			case 9763:
				return 9764;
			case 9768:
			case 9769:
				return 9770;
			case 9807:
			case 9808:
				return 9809;
			case 9786:
			case 9787:
				return 9788;
			case 9777:
			case 9778:
				return 9779;
			case 9804:
			case 9805:
				return 9806;
			case 9783:
			case 9784:
				return 9785;
			case 9801:
			case 9802:
				return 9803;
			case 9798:
			case 9799:
				return 9800;
			case 9792:
			case 9793:
				return 9794;
			case 9795:
			case 9796:
				return 9797;
			case 9765:
			case 9766:
				return 9767;
			case 9771:
			case 9772:
				return 9773;
			case 9774:
			case 9775:
				return 9776;
			case 9780:
			case 9781:
				return 9782;
			case 9810:
			case 9811:
				return 9812;
			case 9948:
			case 9949:
				return 9950;

			case 9813: // SKILLCAPE
				return 9814;

		}
		return 0;
	}

	private static int getMastercapeSkillIndex(int itemId) {
		return itemId - 7692;
	}

	private static int getSkillcapeSkillIndex(int itemId) {
		switch (itemId) {
			case 12169:
			case 12170:
				return PlayerConstants.SUMMONING;

			case 9747:
			case 9748:
				return 0;
			case 9750:
			case 9751:
				return 2;
			case 9753:
			case 9754:
				return 1;
			case 9756:
			case 9757:
				return 4;
			case 9759:
			case 9760:
				return 5;
			case 9762:
			case 9763:
				return 6;
			case 9768:
			case 9769:
				return 3;
			case 9807:
			case 9808:
				return 8;
			case 9786:
			case 9787:
				return 18;
			case 9777:
			case 9778:
				return 17;
			case 9804:
			case 9805:
				return 11;
			case 9783:
			case 9784:
				return 9;
			case 9801:
			case 9802:
				return 7;
			case 9798:
			case 9799:
				return 10;
			case 9792:
			case 9793:
				return 14;
			case 9795:
			case 9796:
				return 13;
			case 9765:
			case 9766:
				return 20;
			case 9771:
			case 9772:
				return 16;
			case 9774:
			case 9775:
				return 15;
			case 9780:
			case 9781:
				return 12;
			case 9810:
			case 9811:
				return 19;
			case 9948:
			case 9949:
				return PlayerConstants.HUNTER;

		}
		return 0;
	}

	private enum LogType {
		BOUGHT, SOLD
	}

	private static void addLog(Player player, int shopId, int item, int amount, LogType type) {
		Server.submitWork(() -> {
			try (Connection connection = DatabaseUtil.getConnection()) {
				try (PreparedStatement ps = connection.prepareStatement("INSERT INTO shop_logs (shop_id, player_id, username, item_id, amount, type) VALUES(?, ?, ?, ?, ?, ?)")) {
					ps.setInt(1, shopId);
					ps.setInt(2, player.getDatabaseId());
					ps.setString(3, player.username);
					ps.setInt(4, item);
					ps.setInt(5, amount);
					ps.setString(6, type.equals(LogType.BOUGHT) ? "BOUGHT" : "SOLD");
					ps.execute();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

	public static void updateUntradables(Player player, Shop shop) {
		player.getData().shopUntradables.clear();

		for(ShopItem item : shop.getItems()) {
			player.getData().shopUntradables.add(new Item(item.getId(), item.getAmount()));
		}
	}

}