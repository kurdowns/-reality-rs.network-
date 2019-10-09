package com.zionscape.server.model.content.tradingpost;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Objects;
import com.zionscape.server.model.content.tradingpost.item.TradingPostItem;
import com.zionscape.server.model.content.tradingpost.item.collection.TradingPostItemCollection;
import com.zionscape.server.model.content.tradingpost.item.container.impl.TradingPostCollectionContainer;
import com.zionscape.server.model.content.tradingpost.item.container.impl.TradingPostHistoryContainer;
import com.zionscape.server.model.content.tradingpost.item.container.impl.TradingPostSearchContainer;
import com.zionscape.server.model.content.tradingpost.item.container.impl.TradingPostShopContainer;
import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.items.ItemDefinition;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.util.Misc;
import com.zionscape.server.world.shops.Shop;

/**
 * Handling everything to do with the trading post
 * 
 * @author 2012
 *
 */
public class TradingPostManager {

	/**
	 * The location where the files are saved
	 */
	public static final String SAVE_FILE = "./data/saves/trading_post/";

	/**
	 * The currency used in trading post
	 */
	public static final int CURRENCY = 13_307;

	/**
	 * The main interface id
	 */
	public static final int MAIN_INTERFACE_ID = 66_000;

	/**
	 * My shop interface id
	 */
	public static final int MY_SHOP_INTERFACE_ID = 69_000;

	/**
	 * History interface id
	 */
	private static final int HISTORY_INTERFACE_ID = 74_500;

	/**
	 * The collect interface id
	 */
	private static final int COLLECT_INTERFACE_ID = 76_000;

	/**
	 * The flashing side bar id
	 */
	private static final int FLASHING_SIDE_BAR_ID = 75_000;

	/**
	 * My shop item display container id
	 */
	private static final int MY_SHOP_ITEM_DISPLAY_CONTAINER = 69_005;

	/**
	 * The order submit string id
	 */
	private static final int ORDER_SUBMIT_STRING = 69_009;

	/**
	 * The display amount string id
	 */
	private static final int MY_SHOP_DISPLAY_AMOUNT = 69_010;

	/**
	 * The instruction string id
	 */
	private static final int INSTRUCTION_STRING = 69_014;

	/**
	 * The sidebar string id
	 */
	private static final int SIDEBAR_STRING_ID = 66_013;

	/**
	 * The item name string id
	 */
	private static final int MY_SHOP_ITEM_NAME = 69_011;

	/**
	 * The display price string id
	 */
	public static final int MY_SHOP_DISPLAY_PRICE = 69_049;

	/**
	 * The feature string id
	 */
	private static final int FEATURE_STRING_ID = 66_448;

	/**
	 * The trading post manager npc id
	 */
	public static final int TRADING_POST_MANAGER = 6_027;

	/**
	 * The maximum side bar entries
	 */
	private static final int MAXIMUM_SIDE_BAR_ENTRIES = 100;

	/**
	 * The maximum slots for a donator in my shop
	 */
	private static final int DONATOR_MY_SHOP_MAX_SLOTS = 30;

	/**
	 * The max slots for regular player
	 */
	private static final int MY_SHOP_MAX_SLOTS = 20;

	/**
	 * The cost to feature a shop
	 */
	private static final Item FEATURE_COST = new Item(13307, 50_000);

	/**
	 * The listing time in minutes
	 */
	private static final int FEATURE_LISTING_TIME = 15;

	/**
	 * The instruction types
	 */
	private static final String[] INSTRUCTIONS = {"Select @or1@Sale @whi@to sell an item",
			"Select an @or1@Item @whi@from\\n@whi@your inventory to sell"};

	/**
	 * The trading posts shops
	 */
	public static HashMap<TradingPostShopContainer, String> tradingPosts =
			new HashMap<TradingPostShopContainer, String>();

	/**
	 * The featured shops
	 */
	public static ArrayList<TradingPostShopContainer> featuredShops =
			new ArrayList<TradingPostShopContainer>();

	/**
	 * The trading post shop
	 */
	private TradingPostShopContainer tradingPostShop;

	/**
	 * The trading post search
	 */
	private TradingPostSearchContainer tradingPostSearch;

	/**
	 * The trading post collection
	 */
	private TradingPostCollectionContainer collection;

	/**
	 * The trading post personal history
	 */
	private TradingPostHistoryContainer personalHistory;

	/**
	 * The trading post global history
	 */
	public static TradingPostHistoryContainer globalHistory = new TradingPostHistoryContainer();

	/**
	 * The offering item
	 */
	private TradingPostItem item;

	/**
	 * Represents the trading post
	 * 
	 * @param player the player
	 */
	public TradingPostManager(Player player) {
		setTradingPostShop(new TradingPostShopContainer(player));
		setTradingPostSearch(new TradingPostSearchContainer(player));
		setCollection(new TradingPostCollectionContainer(player));
		setPersonalHistory(new TradingPostHistoryContainer(player));
	}

	/**
	 * Opening the trading post
	 * 
	 * @param player the player
	 */
	public static void open(Player player) {
		/*
		 * Clear sidebar
		 */
		clearMainSidebar(player);
		/*
		 * The iterator for trading post
		 */
		Iterator<Entry<TradingPostShopContainer, String>> it = tradingPosts.entrySet().iterator();
		/*
		 * Sending the sidebar entries
		 */
		while (it.hasNext()) {
			Entry<TradingPostShopContainer, String> pair = it.next();
			/*
			 * Invalid
			 */
			if (pair == null) {
				continue;
			}
			/*
			 * Ignore own shop
			 */
			if (pair.getValue().equals(player.getUsername())) {
				continue;
			}
			/*
			 * Send name
			 */
			player.getPA().sendFrame126(pair.getValue() + "", SIDEBAR_STRING_ID);
		}
		/*
		 * Features
		 */
		sendFeatures(player);
		player.getTradingPost().getTradingPostSearch().resetItems().refreshItems();
		player.getPA().showInterface(MAIN_INTERFACE_ID);
	}

	/**
	 * Sending features
	 * 
	 * @param player the player
	 */
	private static void sendFeatures(Player player) {
		for (int i = 0; i < 4; i++) {
			if (i >= featuredShops.size()) {
				player.getPA().sendFrame126("Empty", FEATURE_STRING_ID + (i * 4));
			} else {
				player.getPA().sendFrame126(featuredShops.get(i).getOwner(),
						FEATURE_STRING_ID + (i * 4));
			}
		}
	}

	/**
	 * Opening a shop
	 * 
	 * @param player the player
	 * @param shop the shop
	 */
	private static void openShop(Player player, TradingPostShopContainer shop) {
		/*
		 * Reset offering item
		 */
		player.getTradingPost().setItem(null);
		/*
		 * Reset search
		 */
		player.getTradingPost().getTradingPostSearch().resetItems();
		/*
		 * Clear interface
		 */
		player.getTradingPost().getTradingPostSearch().addToSearch(shop.getValidItems());
		/*
		 * Show main interface
		 */
		player.getPA().showInterface(MAIN_INTERFACE_ID);
	}

	/**
	 * Opening own shop
	 * 
	 * @param player the player
	 */
	public static void openMyShop(Player player) {
		/*
		 * Reset offering item
		 */
		player.getTradingPost().setItem(null);
		/*
		 * Clear interface
		 */
		player.getTradingPost().getTradingPostShop().refreshItems();
		/*
		 * Send offering item
		 */
		sendDisplayOfferingItem(player);
		/*
		 * Show main interface
		 */
		player.getPA().showInterface(MY_SHOP_INTERFACE_ID);
	}

	/**
	 * Opens the history
	 * 
	 * @param player the player
	 */
	public static void openHistory(Player player) {
		player.getTradingPost().getPersonalHistory().refreshItems();
		player.getPA().showInterface(HISTORY_INTERFACE_ID);
	}

	/**
	 * Opens the collection interface
	 * 
	 * @param player the player
	 */
	public static void openCollection(Player player) {
		/*
		 * Resets collection
		 */
		player.getTradingPost().getCollection().resetItems();
		/*
		 * The coins
		 */
		int coins = TradingPostItemCollection.getCollectionAmount(player);
		/*
		 * Sets the coin
		 */
		player.getTradingPost().getCollection().setCoin(new Item(coins > 0 ? CURRENCY : -1, coins));
		/*
		 * Refresh
		 */
		player.getTradingPost().getCollection().refreshItems();
		/*
		 * Send interface
		 */
		player.getPA().showInterface(COLLECT_INTERFACE_ID);
	}

	/**
	 * Using the main button in my shop
	 * 
	 * @param player the player
	 */
	private static void sendMainButton(Player player) {
		/*
		 * The max slots
		 */
		int maxSlots = getMaximumSlots(player);
		/*
		 * No more slots
		 */
		if (player.getTradingPost().getTradingPostShop().getValidItems().size() == maxSlots) {
			player.sendMessage(
					"You have reached the maximum amount of slots available in your Trading Post shop.");
			// if (!player.getDonatorRights().isDonator()) {
			// player.sendMessage("Upgrade your account to Donator for 10 more available slots.");
			// }
		}
		/*
		 * No item selected
		 */
		if (player.getTradingPost().getItem() == null) {
			player.getPA().sendFrame126(INSTRUCTIONS[1], INSTRUCTION_STRING);
			// TODO: Null
			player.getPA().sendItemContainer(null,
					Shop.INVENTORY_INTERFACE_ID);
			player.getPA().sendFrame248(MY_SHOP_INTERFACE_ID, FLASHING_SIDE_BAR_ID);
		} else {
			/*
			 * The item
			 */
			TradingPostItem item = player.getTradingPost().getItem();
			/*
			 * Doesn't have item
			 */
			if (!player.getItems().playerHasItem(item.getId())) {
				return;
			}
			/*
			 * Sets price
			 */
			item.setPrice(
					item.getPrice() < 1 ? item.getDefinition().getGeValue() : item.getPrice());
			/*
			 * No price
			 */
			if (item.getPrice() == 0) {
				player.sendMessage("You must enter a price first.");
				return;
			}
			/*
			 * Delete
			 */
			player.getItems().deleteItem(item.getId(), item.getAmount());
			/*
			 * Increase total to sell
			 */
			item.setTotalToSell(item.getTotalToSell() + item.getAmount());
			/*
			 * Reset offering item
			 */
			player.getTradingPost().setItem(null);
			/*
			 * Display offering item
			 */
			sendDisplayOfferingItem(player);
			/*
			 * Reset back to just shop
			 */
			player.getPA().showInterface(MY_SHOP_INTERFACE_ID);
			/*
			 * Add to shop
			 */
			player.getTradingPost().getTradingPostShop().add(item);
		}
	}

	/**
	 * Sending an item to sell
	 * 
	 * @param player the player
	 * @param item the item
	 * @param slot the item slot
	 */
	public static void sendItemSale(Player player, Item item, int slot) {
		/*
		 * At the wrong interface id
		 */
		if (player.openInterfaceId != MY_SHOP_INTERFACE_ID) {
			return;
		}
		/*
		 * The existing amount
		 */
		int existingAmount = player.inventoryN[slot];
		/*
		 * Fixes amount
		 */
		if (existingAmount < item.getAmount()) {
			item.setAmount(existingAmount);
		}
		/*
		 * Invalid item
		 */
		if (item.getId() <= 0 || item.getAmount() <= 0) {
			return;
		}
		/*
		 * The item definition
		 */
		ItemDefinition def = item.getDefinition();
		/*
		 * Invalid definition
		 */
		if (def == null) {
			return;
		}
		/*
		 * Can't sell this item
		 */
		if (!def.isGeTradable()) {
			player.sendMessage("You cannot sell this item.");
			return;
		}
		/*
		 * Doesn't have item
		 */
		if (player.inventory[slot] != item.getId() || player.inventoryN[slot] != item.getAmount()) {
			return;
		}
		/*
		 * Sets the amount
		 */
		item.setAmount(player.inventoryN[slot]);
		/*
		 * Set offering item
		 */
		TradingPostItem tradingPostItem = new TradingPostItem(item.getId(), item.getAmount());
		/*
		 * Sets price
		 */
		tradingPostItem.setPrice(item.getDefinition().getGeValue());
		/*
		 * Set seller
		 */
		tradingPostItem.setSeller(player.getUsername());
		/*
		 * Sets dealing item
		 */
		player.getTradingPost().setItem(tradingPostItem);
		/*
		 * Displays offering item
		 */
		sendDisplayOfferingItem(player);
	}

	/**
	 * Sending offering item to display
	 * 
	 * @param player the player
	 */
	private static void sendDisplayOfferingItem(Player player) {
		/*
		 * The item
		 */
		if (player.getTradingPost().getItem() == null) {
			player.getPA().sendInterfaceItems(MY_SHOP_ITEM_DISPLAY_CONTAINER,
					Arrays.asList(new Item(-1)));
			player.getPA().sendFrame126("", MY_SHOP_DISPLAY_AMOUNT);
			player.getPA().sendFrame126("", MY_SHOP_DISPLAY_PRICE);
			player.getPA().sendFrame126("", MY_SHOP_ITEM_NAME);
			player.getPA().sendFrame126(INSTRUCTIONS[0], INSTRUCTION_STRING);
			player.getPA().sendFrame126("@or1@Sell", ORDER_SUBMIT_STRING);
			return;
		}
		/*
		 * The item
		 */
		TradingPostItem item = player.getTradingPost().getItem();
		/*
		 * Display item
		 */
		player.getPA().sendInterfaceItems(MY_SHOP_ITEM_DISPLAY_CONTAINER,
				Arrays.asList(item.clone()));
		player.getPA().sendFrame126("@or1@" + item.getAmount() + "", MY_SHOP_DISPLAY_AMOUNT);
		player.getPA().sendFrame126(
				"@or1@" + NumberFormat.getInstance().format(item.getPrice()) + " BM",
				MY_SHOP_DISPLAY_PRICE);
		player.getPA().sendFrame126("@or1@" + Misc.ucFirst(item.getDefinition().getName()),
				MY_SHOP_ITEM_NAME);
	}

	/**
	 * Sends the item search list
	 * 
	 * @param player the player
	 * @param input the input
	 */
	public static void sendItemSearchList(Player player, int input) {
		/*
		 * Trading post empty
		 */
		if (tradingPosts.size() == 0) {
			player.sendMessage("The Trading Post is empty. Be the first to sell something!");
			return;
		}
		/*
		 * The found posts
		 */
		ArrayList<TradingPostItem> found = new ArrayList<TradingPostItem>();
		/*
		 * Clear
		 */
		player.getTradingPost().getTradingPostSearch().resetItems();
		/*
		 * The iterator for trading post
		 */
		Iterator<Entry<TradingPostShopContainer, String>> it = tradingPosts.entrySet().iterator();
		/*
		 * Checking for matches
		 */
		while (it.hasNext() && found.size() < 10) {
			/*
			 * The next entry
			 */
			Entry<TradingPostShopContainer, String> pair = it.next();
			/*
			 * The shop
			 */
			TradingPostShopContainer shop = (TradingPostShopContainer) pair.getKey();
			/*
			 * Invalid shop
			 */
			if (shop == null) {
				continue;
			}
			/*
			 * Search for items
			 */
			for (TradingPostItem item : shop.getValidItems()) {
				/*
				 * Invalid item
				 */
				if (item.getDefinition().getName().equals("null")) {
					continue;
				}
				/*
				 * Found
				 */
				if (item.getId() == input) {
					found.add(item);
				}
			}
		}
		/*
		 * Sort the list
		 */
		Collections.sort(found, new Comparator<TradingPostItem>() {

			@Override
			public int compare(TradingPostItem o1, TradingPostItem o2) {

				double type1 = o1.getPrice();
				double type2 = o2.getPrice();

				if (type1 == type2) {
					return 0;
				} else if (type1 > type2) {
					return 1;
				} else {
					return -1;
				}
			}
		});
		/*
		 * Add to container
		 */
		player.sendMessage(
				"Found " + found.size() + " results for " + ItemDefinition.forId(input).getName());
		player.getTradingPost().getTradingPostSearch().addToSearch(found);
	}

	/**
	 * Cancelling a sale post
	 * 
	 * @param player the player
	 * @param index the index
	 */
	public static void cancelPost(Player player, int index) {
		/*
		 * Nothing
		 */
		if (player.getTradingPost().getTradingPostShop().isEmpty()) {
			return;
		}
		/*
		 * Null index
		 */
		if (player.getTradingPost().getTradingPostShop().get(index) == null) {
			return;
		}
		/*
		 * The item
		 */
		final TradingPostItem item = player.getTradingPost().getTradingPostShop().get(index);
		/*
		 * Invalid item
		 */
		if (item == null) {
			return;
		}
		/*
		 * The item to add
		 */
		final Item add = new Item(item.getId(), item.getAmount());
		/*
		 * Delete first
		 */
		player.getTradingPost().getTradingPostShop().delete(item, index, true);
		/*
		 * No space
		 */
		if (player.getItems().freeInventorySlots() < add.getAmount()
				&& !add.getDefinition().isStackable()) {
			player.getItems().addDirectlyToBank(add.getId(), add.getAmount());
			player.sendMessage(
					"The item has been sent to your bank as you don't have any inventory space.");
		} else {
			player.getItems().addItem(add.getId(), add.getAmount());
		}
	}

	/**
	 * Sending item value
	 * 
	 * @param player the player
	 * @param index the index
	 */
	public static void sendItemValue(Player player, int index) {
		/*
		 * Nothing
		 */
		if (player.getTradingPost().getTradingPostSearch().isEmpty()) {
			return;
		}
		/*
		 * Null index
		 */
		if (player.getTradingPost().getTradingPostSearch().get(index) == null) {
			return;
		}
		/*
		 * The item
		 */
		TradingPostItem item = player.getTradingPost().getTradingPostSearch().get(index);
		/*
		 * Invalid item
		 */
		if (item == null) {
			return;
		}
		player.sendMessage(
				"@blu@" + item.getSeller() + " @bla@sells @whi@" + item.getDefinition().getName()
						+ " @bla@for @red@" + NumberFormat.getInstance().format(item.getPrice()) + " "
						+ ItemDefinition.forId(CURRENCY).getName() + ".");
	}

	/**
	 * Buying the item
	 * 
	 * @param player the player
	 * @param slot the slot
	 * @param id the id
	 * @param amount the amount
	 */
	public static void purchaseItem(Player player, int slot, int id, int amount) {
		/*
		 * The definition
		 */
		ItemDefinition def = ItemDefinition.forId(id);
		/*
		 * Invalid definition
		 */
		if (def == null) {
			return;
		}
		/*
		 * Nothing
		 */
		if (player.getTradingPost().getTradingPostSearch().isEmpty()) {
			return;
		}
		/*
		 * Null index
		 */
		if (player.getTradingPost().getTradingPostSearch().get(slot) == null) {
			return;
		}
		/*
		 * The item
		 */
		TradingPostItem item = player.getTradingPost().getTradingPostSearch().get(slot);
		/*
		 * Invalid item
		 */
		if (item == null) {
			return;
		}
		/*
		 * Wrong id
		 */
		if (item.getId() != id) {
			return;
		}
		/*
		 * The item name
		 */
		String itemName = item.getDefinition().getName();
		/*
		 * String price name
		 */
		String currencyName = ItemDefinition.forId(CURRENCY).getName();
		/*
		 * The possible amount
		 */
		int purchase = (int) Math.floor(player.getItems().getItemAmount(CURRENCY) / item.getPrice());
		/*
		 * Fix amount
		 */
		if (purchase > amount) {
			purchase = amount;
		}
		/*
		 * Fix buying more than available
		 */
		if (item.getAmount() < purchase) {
			purchase = item.getAmount();
		}
		/*
		 * Not enough
		 */
		if (purchase == 0) {
			player.sendMessage(
					"You don't have enough " + currencyName + " to purchase " + amount + " " + itemName);
			return;
		}
		/*
		 * Stackable
		 */
		if (def.isStackable()) {
			/*
			 * Doesn't exist
			 */
			if (!player.getItems().playerHasItem(id) && player.getItems().freeInventorySlots() == 0) {
				player.sendMessage("You don't have any inventory space to purchase the " + itemName);
				return;
			}
		} else {
			/*
			 * The free slots
			 */
			int spaces = player.getItems().freeInventorySlots();
			/*
			 * No space
			 */
			if (spaces == 0) {
				player.sendMessage("You have no inventory space to purchase the item.");
				return;
			}
			/*
			 * Purchase
			 */
			if (purchase > spaces) {
				purchase = spaces;
				player.sendMessage("You was only able to purchase " + spaces
						+ " due to your available spaces in inventory.");
			}
		}
		/*
		 * The cost
		 */
		Item cost = new Item(CURRENCY, purchase * item.getPrice());
		/*
		 * Doesn't have enough
		 */
		if (!player.getItems().playerHasItem(cost.getId(), cost.getAmount())) {
			return;
		}
		/*
		 * The delete
		 */
		player.getItems().deleteItem(cost.getId(), cost.getAmount());
		/*
		 * The purchased item
		 */
		TradingPostItem purchasedItem = new TradingPostItem(item.getId(), purchase);
		/*
		 * Remove from shop
		 */
		removeItemFromShop(purchasedItem);
		/*
		 * Set seller
		 */
		purchasedItem.setSeller(item.getSeller());
		purchasedItem.setPrice(item.getPrice());
		/*
		 * Sold
		 */
		item.setSold(item.getSold() + purchase);
		/*
		 * Remove from search
		 */
		player.getTradingPost().getTradingPostSearch().delete(purchasedItem, slot, true);
		/*
		 * Add to inventory
		 */
		player.getItems().addItem(id, purchase);
		/*
		 * The seller
		 */
		Player seller = PlayerHandler.getPlayer(item.getSeller());
		/*
		 * Seller is online
		 */
		if (seller != null) {
			/*
			 * Notify the player
			 */
			seller.sendMessage("@gre@[Trading Post] @blu@"
					+ player.getUsername() + "@bla@ has purchased your @whi@" + itemName);
		}
		/*
		 * Add to collection
		 */
		TradingPostItemCollection.addToCollection(item.getPrice() * purchase, item.getSeller());
		/*
		 * History
		 */
		player.getTradingPost().getPersonalHistory().addToHistory(purchasedItem);
		/*
		 * Global history
		 */
		globalHistory.addToHistory(purchasedItem);
	}

	/**
	 * Buying shop feature
	 * 
	 * @param player the player
	 */
	public static void purchaseShopFeature(Player player) {
		/*
		 * The feature shops max
		 */
		if (featuredShops.size() == 4) {
			player.sendMessage("There are no feature shops slots currently available.");
			return;
		}
		/*
		 * Already featured
		 */
		if (featuredShops.contains(player.getTradingPost().getTradingPostShop())) {
			player.sendMessage("Your shop is alread being featured.");
			return;
		}
		/*
		 * Cost
		 */
		if (!player.getItems().playerHasItem(FEATURE_COST.getId(), FEATURE_COST.getAmount())) {
			player.sendMessage("It currently costs " + FEATURE_COST.getAmount()
					+ " Blood money to feature your shop for 15 minutes.");
			return;
		}
		/*
		 * No items in shop
		 */
		if (player.getTradingPost().getTradingPostShop().getValidItems().size() == 0) {
			player.sendMessage(
					"You need to have some items being sold in your Trading Post to feature your shop.");
			return;
		}
		/*
		 * The slot
		 */
		// final int slot = featuredShops.size();
		/*
		 * Delete
		 */
		player.getItems().deleteItem(FEATURE_COST.getId(), FEATURE_COST.getAmount());
		player.getTradingPost().getTradingPostShop().setOwner(player.getUsername());
		featuredShops.add(player.getTradingPost().getTradingPostShop());
		player.sendMessage(
				"Your shop has been featured on the main page and will be removed in 15 minutes.");
		/*
		 * Shop removal
		 */
		// TaskManager.submit(new Task(100 * FEATURE_LISTING_TIME) {
		//
		// @Override
		// protected void execute() {
		// featuredShops.remove(slot);
		// stop();
		// }
		// });
	}

	/**
	 * Increasing amount
	 * 
	 * @param player the player
	 * @param amount the amount
	 */
	private static void increaseOfferingItemAmount(Player player, int amount) {
		/*
		 * The item
		 */
		if (player.getTradingPost().getItem() == null) {
			return;
		}
		/*
		 * The item
		 */
		TradingPostItem item = player.getTradingPost().getItem();
		/*
		 * The existing amount
		 */
		int existing = player.getItems().getItemAmount(item.getId());
		/*
		 * Fixes amount
		 */
		if (amount > existing) {
			amount = existing;
		}
		/*
		 * Increases
		 */
		if (item.getAmount() + amount < existing) {
			item.incrementAmount(amount);
		} else {
			item.setAmount(amount);
		}
		player.getPA().sendFrame126("@or1@" + item.getAmount() + "", MY_SHOP_DISPLAY_AMOUNT);
	}

	/**
	 * Changing price for item
	 * 
	 * @param player the player
	 * @param price the price
	 */
	private static void changeOfferingItemPrice(Player player, double price) {
		/*
		 * The item
		 */
		if (player.getTradingPost().getItem() == null) {
			return;
		}
		/*
		 * The item
		 */
		TradingPostItem item = player.getTradingPost().getItem();
		/*
		 * Set the price
		 */
		if (price == 0) {
			item.setPrice(item.getDefinition().getGeValue());
		} else {
			item.setPrice((int) (item.getPrice() * price));
		}
		/*
		 * Send info
		 */
		player.getPA().sendFrame126(
				"@or1@" + NumberFormat.getInstance().format(item.getPrice()) + " BM",
				MY_SHOP_DISPLAY_PRICE);
	}

	/**
	 * Removes item from shop
	 * 
	 * @param item the item
	 */
	private static void removeItemFromShop(TradingPostItem item) {
		/*
		 * The iterator for trading post
		 */
		Iterator<Entry<TradingPostShopContainer, String>> it = tradingPosts.entrySet().iterator();
		/*
		 * Sending the sidebar entries
		 */
		while (it.hasNext()) {
			Entry<TradingPostShopContainer, String> pair = it.next();
			/*
			 * Ignore own shop
			 */
			if (pair.getValue().equals(item.getSeller())) {
				continue;
			}
			/*
			 * The shop
			 */
			TradingPostShopContainer shop = pair.getKey();
			if (shop == null) {
				break;
			}
			/*
			 * Deletes item
			 */
			shop.delete(item);
		}
	}

	/**
	 * Collection of shops in array list
	 * 
	 * @return the shops
	 */
	private static ArrayList<TradingPostShopContainer> shopsToList() {
		ArrayList<TradingPostShopContainer> shops = new ArrayList<TradingPostShopContainer>();
		tradingPosts.keySet().stream().filter(Objects::nonNull).forEach(shop -> shops.add(shop));
		return shops;
	}

	/**
	 * The maximum slots
	 * 
	 * @param player the player
	 * @return the slots
	 */
	public static int getMaximumSlots(Player player) {
		return MY_SHOP_MAX_SLOTS;
	}

	/**
	 * Clearing the sidebar
	 * 
	 * @param player the player
	 */
	private static void clearMainSidebar(Player player) {
		for (int i = SIDEBAR_STRING_ID; i < (SIDEBAR_STRING_ID + MAXIMUM_SIDE_BAR_ENTRIES); i++) {
			player.getPA().sendFrame126("", i);
		}
	}

	/**
	 * Handles buttons
	 * 
	 * @param player the player
	 * @param button the button
	 */
	public static boolean handleButton(Player player, int button) {
		if (button >= 69_051 && button <= 69_051 + (11 * 30)) {
			int index = button - 69_051;
			if (index > 0) {
				index /= 11;
			}
			cancelPost(player, index);
			return true;
		} else if (button >= 66_114 && button <= 66_114 + (11 * 30)) {
			int index = button - 66_114;
			if (index > 0) {
				index /= 11;
			}
			sendItemValue(player, index);
			return true;
		} else if (button >= 66_445 && button <= 66457) {
			int index = button - 66_445;
			if (index > 0) {
				index /= 4;
			}
			if (index >= featuredShops.size()) {
				player.sendMessage(
						"Currently unavailable. Setup your shop and purchase a feature to be displayed here.");
				return true;
			}
			openShop(player, featuredShops.get(index));
			return true;
		} else if (button >= 66_013 && button <= 66_112) {
			int index = button - 66_013;
			ArrayList<TradingPostShopContainer> shops = shopsToList();
			if (index >= shops.size()) {
				return true;
			}
			TradingPostShopContainer shop = shops.get(index);
			if (shop == null) {
				return true;
			}
			openShop(player, shop);
			return true;
		}
		switch (button) {
			case 66_461:
				openMyShop(player);
				return true;
			case 66_466:
				openHistory(player);
				return true;
			case 66_471:
				openCollection(player);
				return true;
			case 69_006:
				sendMainButton(player);
				return true;
			case 69_015:
				open(player);
				return true;
			case 69_016:
				purchaseShopFeature(player);
				return true;
			case 69_017:
				increaseOfferingItemAmount(player, 1);
				return true;
			case 69_021:
				increaseOfferingItemAmount(player, 5);
				return true;
			case 69_025:
				increaseOfferingItemAmount(player, 10);
				return true;
			case 69_029:
				increaseOfferingItemAmount(player, Integer.MAX_VALUE);
				return true;
			case 69_033:
				changeOfferingItemPrice(player, 1.10);
				return true;
			case 69_037:
				changeOfferingItemPrice(player, 0.90);
				return true;
			case 69_041:
				changeOfferingItemPrice(player, 0);
				return true;
			case 69_045:
				// player.setEnterSyntax(new TradingPostChangePriceEnterSyntax());
				// player.getPA().sendEnterAmountPrompt("Enter price:");
				return true;
			case 74_505:
				player.getTradingPost().getGlobalHistory().refreshItems();
				return true;
			case 74_508:
				globalHistory.refresh(player);
				return true;
		}
		return false;
	}

	/**
	 * Gets the tradingPostShop
	 *
	 * @return the tradingPostShop
	 */
	public TradingPostShopContainer getTradingPostShop() {
		return tradingPostShop;
	}

	/**
	 * Sets the tradingPostShop
	 * 
	 * @param tradingPostShop the tradingPostShop
	 */
	public void setTradingPostShop(TradingPostShopContainer tradingPostShop) {
		this.tradingPostShop = tradingPostShop;
	}

	/**
	 * Gets the tradingPostSearch
	 *
	 * @return the tradingPostSearch
	 */
	public TradingPostSearchContainer getTradingPostSearch() {
		return tradingPostSearch;
	}

	/**
	 * Sets the tradingPostSearch
	 * 
	 * @param tradingPostSearch the tradingPostSearch
	 */
	public void setTradingPostSearch(TradingPostSearchContainer tradingPostSearch) {
		this.tradingPostSearch = tradingPostSearch;
	}

	/**
	 * Gets the collection
	 *
	 * @return the collection
	 */
	public TradingPostCollectionContainer getCollection() {
		return collection;
	}

	/**
	 * Sets the collection
	 * 
	 * @param collection the collection
	 */
	public void setCollection(TradingPostCollectionContainer collection) {
		this.collection = collection;
	}

	/**
	 * Gets the personalHistory
	 *
	 * @return the personalHistory
	 */
	public TradingPostHistoryContainer getPersonalHistory() {
		return personalHistory;
	}

	/**
	 * Sets the personalHistory
	 * 
	 * @param personalHistory the personalHistory
	 */
	public void setPersonalHistory(TradingPostHistoryContainer personalHistory) {
		this.personalHistory = personalHistory;
	}

	/**
	 * Gets the globalHistory
	 *
	 * @return the globalHistory
	 */
	public TradingPostHistoryContainer getGlobalHistory() {
		return globalHistory;
	}

	/**
	 * Gets the item
	 *
	 * @return the item
	 */
	public TradingPostItem getItem() {
		return item;
	}

	/**
	 * Sets the item
	 * 
	 * @param item the item
	 */
	public void setItem(TradingPostItem item) {
		this.item = item;
	}
}
