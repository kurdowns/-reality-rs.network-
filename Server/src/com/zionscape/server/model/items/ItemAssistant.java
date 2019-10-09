package com.zionscape.server.model.items;

import com.zionscape.server.Config;
import com.zionscape.server.ServerEvents;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.Food;
import com.zionscape.server.model.content.GreeGree;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.content.minigames.DuelArena;
import com.zionscape.server.model.content.minigames.castlewars.CastleWars;
import com.zionscape.server.model.content.minigames.quests.QuestHandler;
import com.zionscape.server.model.content.minigames.zombies.Zombies;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.npcs.combat.zulrah.Zulrah;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.combat.Animations;
import com.zionscape.server.model.players.combat.CombatHelper;
import com.zionscape.server.util.CollectionUtil;
import com.zionscape.server.util.Misc;
import com.zionscape.server.world.ItemDrops;

import java.text.DecimalFormat;
import java.util.*;

public class ItemAssistant {

	/**
	 * Bonuses
	 */
	public final String[] BONUS_NAMES = {"Stab", "Slash", "Crush", "Magic", "Range", "Stab", "Slash", "Crush",
			"Magic", "Range", "Strength", "Prayer"};
	public int[] extreme_Donator = {};
	public int[] super_Donator = {};
	public int[] Donator = {15441, 15442, 15443, 15444};
	public boolean updateInventory = false;
	/**
	 * Items
	 */
	public int[][] brokenBarrows = {{4708, 4860}, {4710, 4866}, {4712, 4872}, {4714, 4878}, {4716, 4884},
			{4720, 4896}, {4718, 4890}, {4720, 4896}, {4722, 4902}, {4732, 4932}, {4734, 4938},
			{4736, 4944}, {4738, 4950}, {4724, 4908}, {4726, 4914}, {4728, 4920}, {4730, 4926},
			{4745, 4956}, {4747, 4926}, {4749, 4968}, {4751, 4974}, {4753, 4980}, {4755, 4986},
			{4757, 4992}, {4759, 4998}};
	private Player c;

	public ItemAssistant(Player client) {
		this.c = client;
	}

	public static int getItemValue(int itemID) {
		Optional<ItemDefinition> item = ItemDefinition.get(itemID);
		if (item.isPresent()) {
			return item.get().getValue();
		}
		return 0;
	}

	public static int getUntradePrice(int item) {
		switch (item) {
			case 2518:
			case 2524:
			case 2526:
				return 100000;
			case 2520:
			case 2522:
				return 150000;
		}
		return 0;
	}

	public void updateInventory() { // omfg!
		updateInventory = false;
		this.resetItems(3214);
	}

	public void keepItemsOnDeath(final int amount, boolean showItems) {
		final ArrayList<ItemKeptOnDeath> items = new ArrayList<>();

		for (int i = 0; i < c.inventory.length; i++) {
			if (c.inventory[i] > 0) {
				int id = c.inventory[i] - 1;

				if (id == Blowpipe.BLOWPIPE) {
					id = Blowpipe.EMPTY_BLOWPIPE;

					if (c.getData().blowpipeAmmo > 0) {
						items.add(new ItemKeptOnDeath(c.getData().blowpipeAmmoId, c.getData().blowpipeAmmo, getItemValue(c.getData().blowpipeAmmoId)));

						if (!showItems) {
							c.getData().blowpipeAmmo = 0;
							c.getData().blowpipeAmmoId = 0;
						}
					}
					if (c.getData().blowpipeCharges > 0) {
						items.add(new ItemKeptOnDeath(Blowpipe.ZULRAH_SCALES, c.getData().blowpipeCharges, getItemValue(Blowpipe.ZULRAH_SCALES)));

						if (!showItems) {
							c.getData().blowpipeCharges = 0;
						}
					}
				}

				if (id == SerpentineHelmet.HELM) {
					id = SerpentineHelmet.UNCHARGED_HELM;

					if (c.getData().serpentineHelmetCharges > 0) {
						items.add(new ItemKeptOnDeath(Blowpipe.ZULRAH_SCALES, c.getData().serpentineHelmetCharges, getItemValue(Blowpipe.ZULRAH_SCALES)));

						if (!showItems) {
							c.getData().serpentineHelmetCharges = 0;
						}
					}
				}

				if(id == TridentOfTheSeas.CHARGED_TRIDENT) {
					id = TridentOfTheSeas.UNCHARGED_TRIDENT;

					if (!showItems) {
						c.getData().tridentSeaCharges = 0;
					}
				}
				if(id == TridentOfTheSwamp.CHARGED_TRIDENT) {
					id = TridentOfTheSwamp.UNCHARGED_TRIDENT;

					if (!showItems) {
						c.getData().tridentSwampCharges = 0;
					}
				}

				items.add(new ItemKeptOnDeath(id, c.inventoryN[i], getItemValue(id)));
			}
		}
		for (int i = 0; i < c.equipment.length; i++) {
			if (c.equipment[i] > 0) {
				int id = c.equipment[i];

				if (id == Blowpipe.BLOWPIPE) {
					id = Blowpipe.EMPTY_BLOWPIPE;

					if (c.getData().blowpipeAmmo > 0) {
						items.add(new ItemKeptOnDeath(c.getData().blowpipeAmmoId, c.getData().blowpipeAmmo, getItemValue(c.getData().blowpipeAmmoId)));

						if (!showItems) {
							c.getData().blowpipeAmmo = 0;
							c.getData().blowpipeAmmoId = 0;
						}
					}
					if (c.getData().blowpipeCharges > 0) {
						items.add(new ItemKeptOnDeath(Blowpipe.ZULRAH_SCALES, c.getData().blowpipeCharges, getItemValue(Blowpipe.ZULRAH_SCALES)));

						if (!showItems) {
							c.getData().blowpipeCharges = 0;
						}
					}
				}

				if (id == SerpentineHelmet.HELM) {
					id = SerpentineHelmet.UNCHARGED_HELM;

					if (c.getData().serpentineHelmetCharges > 0) {
						items.add(new ItemKeptOnDeath(Blowpipe.ZULRAH_SCALES, c.getData().serpentineHelmetCharges, getItemValue(Blowpipe.ZULRAH_SCALES)));

						if (!showItems) {
							c.getData().serpentineHelmetCharges = 0;
						}
					}
				}

				if(id == TridentOfTheSeas.CHARGED_TRIDENT) {
					id = TridentOfTheSeas.UNCHARGED_TRIDENT;

					if (!showItems) {
						c.getData().tridentSeaCharges = 0;
					}
				}
				if(id == TridentOfTheSwamp.CHARGED_TRIDENT) {
					id = TridentOfTheSwamp.UNCHARGED_TRIDENT;

					if (!showItems) {
						c.getData().tridentSwampCharges = 0;
					}
				}

				items.add(new ItemKeptOnDeath(id, c.playerEquipmentN[i], getItemValue(id)));
			}
		}

		if (c.getFamiliar() != null && c.getFamiliar().getInventoryItems().size() > 0) {
			for (Item item : c.getFamiliar().getInventoryItems()) {
				int id = item.getId();
				items.add(new ItemKeptOnDeath(id, item.getAmount(), getItemValue(id)));
			}
		}

		Player o = null;
		if (c.killerId > -1) {
			o = PlayerHandler.players[c.killerId];
		}

		Collections.sort(items, (ItemKeptOnDeath o1, ItemKeptOnDeath o2) -> {
			if (o1.getCost() < o2.getCost()) {
				return 1;
			} else if (o1.getCost() > o2.getCost()) {
				return -1;
			}
			return 0;
		});

		if (!showItems) {
			deleteAllItems();
			if (c.getFamiliar() != null && c.getFamiliar().getInventoryItems().size() > 0) {
				c.getFamiliar().getInventoryItems().clear();
			}
		}

		int cashDrop = 0;

		List<Item> keptList = new ArrayList<>();
		List<Item> tradableList = new ArrayList<>();
		List<Item> untradableShopList = new ArrayList<>();
		List<Item> notKeptList = new ArrayList<>();

		int count = 0;
		for (ItemKeptOnDeath item : items) {

			if (ItemUtility.getName(item.getId()).toLowerCase().contains("clue scroll")) {
				continue;
			}

			if(c.attributeExists("died_to_player") && isUntradableShopItem(item.getId())) {
				untradableShopList.add(new Item(item.getId(), item.getAmount()));

				for(int index = 0; index < Config.UNTRADABLE_SHOP_ITEMS.length; index++) {
					if(Config.UNTRADABLE_SHOP_ITEMS[index][0] == item.getId()) {
						cashDrop += Config.UNTRADABLE_SHOP_ITEMS[index][1] * 0.80;
					}
				}

				continue;
			}

			if (!tradeable(item.getId())) {
				tradableList.add(new Item(item.getId(), item.getAmount()));

				if (showItems) {
					keptList.add(new Item(item.getId(), item.getAmount()));
				}

				continue;
			}

			if (count < amount) {
				if (item.getAmount() <= 1) {
					keptList.add(new Item(item.getId(), item.getAmount()));
					count++;
				} else {
					int amountLeft = amount - count;

					if (amountLeft >= item.getAmount()) {
						keptList.add(new Item(item.getId(), item.getAmount()));
						count += item.getAmount();
					} else {
						int leftOver = item.getAmount() - amountLeft;

						keptList.add(new Item(item.getId(), amountLeft));
						notKeptList.add(new Item(item.getId(), leftOver));

						count += amountLeft;
					}
				}
			} else {
				notKeptList.add(new Item(item.getId(), item.getAmount()));
			}
		}

		if (showItems) {
			for (int i = 0; i < 8; i++) {
				if (i > keptList.size() - 1) {
					c.getPA().sendFrame34a(10494, -1, i, 0);
				} else {
					Item item = keptList.get(i);
					c.getPA().sendFrame34a(10494, item.getId(), i, item.getAmount());
				}
			}
			for (int i = 0; i < 40; i++) {
				if (i > notKeptList.size() - 1) {
					c.getPA().sendFrame34a(10600, -1, i, 0);
				} else {
					Item item = notKeptList.get(i);
					c.getPA().sendFrame34a(10600, item.getId(), i, item.getAmount());
				}
			}
			return;
		}

		keptList.stream().forEach(x -> c.getItems().addItem(x.getId(), x.getAmount()));

		if (tradableList.size() > 0) {
			tradableList.stream().forEach(x -> c.getItems().addItem(x.getId(), x.getAmount()));
			//c.sendMessage("Your untradeables have been put in your inventory.");
		}

		if(untradableShopList.size() >= 0) {
			for(Item item : untradableShopList) {
				int id = item.getId();
				if(ItemUtility.isNote(id)) {
					id = ItemUtility.getUnotedId(id);
				}

				boolean found = false;
				for(Item i : c.getData().shopUntradables) {
					if(i.getId() == id) {
						i.incrementAmount(item.getAmount());
						found = true;
					}
				}

				if(!found) {
					c.getData().shopUntradables.add(item);
				}
			}

			c.sendMessage("Your untradable's have been put in the Grim Reaper at South of home.");
		}

		Location location = c.getLocation();

		if (Zulrah.inArea(c)) {
			location = Location.create(Config.RESPAWN_X, Config.RESPAWN_Y, 0);
		}

		for (Item item : notKeptList) {
			List<GroundItem> drops = ItemDrops.createGroundItem(o, item.getId(), location.getX(), location.getY(), location.getZ(), item.getAmount(), c.killerId, true);
			if (o != c) {
				drops.forEach(x -> x.pkLoot = true);
			}
		}

		if (o != null) {
			List<GroundItem> drops = ItemDrops.createGroundItem(o, 526, location.getX(), location.getY(), location.getZ(), 1, c.killerId, true);
			if (o != c) {
				drops.forEach(x -> x.pkLoot = true);
			}
			if (cashDrop > 0) {
				drops = ItemDrops.createGroundItem(o, 995, location.getX(), location.getY(), location.getZ(), cashDrop, c.killerId, true);
				if (o != c) {
					drops.forEach(x -> x.pkLoot = true);
				}
			}
		}
	}

	private boolean isUntradableShopItem(int id) {
		for(int i = 0; i < Config.UNTRADABLE_SHOP_ITEMS.length; i++) {
			if(Config.UNTRADABLE_SHOP_ITEMS[i][0] == id) {
				return true;
			}
		}
		return false;
	}

	public int amountOfItemsKeptOnDeath() {

		int count = 0;

		if (!c.isSkulled) { // what items to
			count += 3;
		}
		if (c.prayerActive[10]) {
			count += 1;
		}
		if (c.curseActive[0]) {
			count += 1;
		}

		return count;
	}

	public int getCarriedWealth() {
		int toReturn = 0;
		for (int i = 0; i < c.equipment.length; i++) {
			if (c.equipment[i] > 0) {
				toReturn += ItemUtility.getValue(c.equipment[i]) * c.playerEquipmentN[i];
			}
		}
		for (int i = 0; i < c.inventory.length; i++) {
			if (c.inventory[i] > 0) {
				toReturn += ItemUtility.getValue(c.inventory[i]) * c.inventoryN[i];
			}
		}
		return toReturn;
	}

	public void addMoneyToInventory(int amount) {
		c.setAttribute("withdraw_from_pouch", true);
		c.getItems().addItem(995, amount);
		c.removeAttribute("withdraw_from_pouch");
	}

	public void addDirectlyToBank(int itemId, int amount) {
		c.getBank().depositItem(itemId, amount, -1, true, false);
	}

	public boolean playerHasItemGlobally(int id) {
		for (int i = 0; i < c.inventory.length; i++) {
			if (c.inventory[i] > 0 && c.inventory[i] - 1 == id) {
				return true;
			}
		}
		for (List<Item> items : c.getBank().getTabs()) {

			for (Item item : items) {
				if (item.getId() == id) {
					return true;
				}
			}

		}
		return false;
	}

	public boolean hasClueScroll() {
		for (int i = 0; i < c.inventory.length; i++) {
			if (c.inventory[i] > 0 && ItemUtility.getName(c.inventory[i] - 1).toLowerCase().contains("clue scroll")) {
				return true;
			}
		}
		for (List<Item> items : c.getBank().getTabs()) {

			for (Item item : items) {
				if (ItemUtility.getName(item.getId()).toLowerCase().contains("clue scroll")) {
					return true;
				}
			}

		}
		return false;
	}

	/*
	 * Amount of item
	 */
	public int amountOfItem(int itemID) {
		int i1 = 0;
		for (int i = 0; i < 28; i++) {
			if (c.inventory[i] == (itemID + 1)) {

				i1 += c.inventoryN[i];


			}
		}
		return i1;
	}

	/*
	 * public int[][] BROKEN_BARROWS = {{4708,4860},{4710,4866},{4712,4872},{4714,4878},{4716,4884},
	 * {4720,4896},{4718,4890},{4720,4896},{4722,4902},{4732,4932},{4734,4938},{4736,4944},{4738,4950},
	 * {4724,4908},{4726,4914},{4728,4920},{4730,4926},{4745,4956},{4747,4926},{4749,4968},{4751,4794},
	 * {4753,4980},{4755,4986},{4757,4992},{4759,4998}};
	 */

	public boolean hasInventoryItems() {
		for (int i = 0; i < c.inventory.length; i++) {
			if (c.inventory[i] > 0) {
				return true;
			}
		}
		return false;
	}

	public boolean isWearingItems() {
		for (int i = 0; i < c.equipment.length; i++) {
			if (c.equipment[i] > 0) {
				return true;
			}

		}
		return false;
	}

	public void resetItems(int WriteFrame) {
		if (c.getAwaitingUpdate()) {
			c.setAwaitingUpdate(false);
		}
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(WriteFrame);
			c.getOutStream().writeWord(c.inventory.length);
			for (int i = 0; i < c.inventory.length; i++) {
				if (c.inventoryN[i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(c.inventoryN[i]);
				} else {
					c.getOutStream().writeByte(c.inventoryN[i]);
				}
				c.getOutStream().writeWordBigEndianA(c.inventory[i]);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	public int getItemCount(int itemID) {
		int count = 0;
		for (int j = 0; j < c.inventory.length; j++) {
			if (c.inventory[j] == itemID + 1) {
				count += c.inventoryN[j];
			}
		}
		return count;
	}

	public void writeBonus() {
		int offset = 0;
		String send = "";
		for (int i = 0; i < c.playerBonus.length; i++) {

			int additional = 0;

			if (i == 4) { // RANGE ATTACK
				if (c.equipment[PlayerConstants.WEAPON] == Blowpipe.BLOWPIPE) {
					if (c.getData().blowpipeAmmoId > 0) {
						Optional<ItemDefinition> definition = ItemDefinition.get(c.getData().blowpipeAmmoId);
						if (definition.isPresent()) {
							ItemDefinition def = definition.get();
							additional += def.getBonuses()[4];
						}
					}
				}
			}

			if ((c.playerBonus[i] + additional) >= 0) {
				send = BONUS_NAMES[i] + ": +" + (c.playerBonus[i] + additional);
			} else {
				send = BONUS_NAMES[i] + ": -" + Math.abs(c.playerBonus[i] + additional);
			}
			if (i == 10) {
				offset = 1;
			}
			c.getPA().sendFrame126(send, 1675 + i + offset);
		}
	}

	public int getTotalCount(int itemID) {
		int count = 0;
		for (int j = 0; j < c.inventory.length; j++) {
			if (ItemUtility.isNote(itemID + 1)) {
				if (itemID + 2 == c.inventory[j]) {
					count += c.inventoryN[j];
				}
			}
			if (!ItemUtility.isNote(itemID + 1)) {
				if (itemID + 1 == c.inventory[j]) {
					count += c.inventoryN[j];
				}
			}
		}

		count += c.getBank().playerHasItem(itemID);
		return count;
	}

	/**
	 * delete all items
	 */
	public void deleteAllItems() {
		for (int i1 = 0; i1 < c.equipment.length; i1++) {
			this.deleteEquipment(c.equipment[i1], i1);
		}
		for (int i = 0; i < c.inventory.length; i++) {
			this.deleteItem(c.inventory[i] - 1, this.getItemSlot(c.inventory[i] - 1), c.inventoryN[i]);
		}
	}

	/**
	 * PVP Gear
	 */
	public boolean pvpGear() {
		for (int i = 0; i < c.inventory.length; i++) {
			if (c.inventory[i] - 1 == 13889 || c.inventory[i] - 1 == 13901 || c.inventory[i] - 1 == 13895
					|| c.inventory[i] - 1 == 13892 || c.inventory[i] - 1 == 13886 || c.inventory[i] - 1 == 13898
					|| c.inventory[i] - 1 == 13904) {
				return true;
			}
		}
		return false;
	}

	public boolean specialCase(int itemId) {
		switch (itemId) {
			case 2518:
			case 2520:
			case 2522:
			case 2524:
			case 2526:
			case 13901:
			case 13895:
			case 13889:
				return true;
		}
		return false;
	}

	public void handleSpecialPickup(int itemId) {
		// c.sendMessage("My " + getItemName(itemId) +
		// " has been recovered. I should talk to the void knights to get it back.");
		// c.getItems().addToVoidList(itemId);
	}

	public void addToVoidList(int itemId) {
		switch (itemId) {
			case 2518:
				c.voidStatus[0]++;
				break;
			case 2520:
				c.voidStatus[1]++;
				break;
			case 2522:
				c.voidStatus[2]++;
				break;
			case 2524:
				c.voidStatus[3]++;
				break;
			case 2526:
				c.voidStatus[4]++;
				break;
		}
	}

	public boolean tradeable(int itemId) {


		for (int j = 0; j < Config.UNTRADABLE_ITEM_IDS.length; j++) {
			if (itemId == Config.UNTRADABLE_ITEM_IDS[j]) {
				return false;
			}
		}
		return true;
	}

	public void addOrBank(int item, int amount) {

		boolean stackable = ItemUtility.isStackable(item);

		// no space required
		if (stackable && playerHasItem(item)) {
			addItem(item, amount);
			return;
		}

		if (stackable) {
			// we only require one space
			if (freeInventorySlots() > 0) {
				addItem(item, amount);
			} else {
				c.getBank().depositItem(item, amount, -1, true, false);
				c.sendMessage("You do not have enough inventory space, the item/s have been put in your bank.");
			}
			return;
		}

		int freeSlots = freeInventorySlots();

		if (amount > freeSlots) {
			int amountToDrop = amount - freeSlots;
			amount -= amountToDrop;

			if (amount > 0) {
				addItem(item, amount);
			}

			if (amountToDrop > 0) {
				c.getBank().depositItem(item, amountToDrop, -1, true, false);
			}

			c.sendMessage("You do not have enough inventory space, the item/s have been put in your bank.");
		} else {
			addItem(item, amount);
		}
	}

	public void addOrDrop(int item, int amount) {

		boolean stackable = ItemUtility.isStackable(item);

		// no space required
		if (stackable && playerHasItem(item)) {
			addItem(item, amount);
			return;
		}

		if (stackable) {
			// we only require one space
			if (freeInventorySlots() > 0) {
				addItem(item, amount);
			} else {
				ItemDrops.createGroundItem(c, item, c.absX, c.absY, amount, c.playerId, false);
				c.sendMessage("You do not have enough inventory space, the item/s has been dropped under you.");
			}
			return;
		}

		int freeSlots = freeInventorySlots();

		if (amount > freeSlots) {
			int amountToDrop = amount - freeSlots;
			amount -= amountToDrop;

			if (amount > 0) {
				addItem(item, amount);
			}

			if (amountToDrop > 0) {
				ItemDrops.createGroundItem(c, item, c.absX, c.absY, amountToDrop, c.playerId, false);
			}

			c.sendMessage("You do not have enough inventory space, the item/s has been dropped under you.");
		} else {
			addItem(item, amount);
		}
	}

	public void addItem(Item item) {
		addItem(item.getId(), item.getAmount());
	}
	
	/**
	 * Add Item
	 */
	public boolean addItem(int item, int amount) {

		if (item >= 8844 && item <= 8850 || item == 20072) {
			Achievements.progressMade(c, Achievements.Types.EARN_EVERY_DEFENDER, item);
		}

		if (item == 995 && amount > 1_000_000_000) {
			Achievements.progressMade(c, Achievements.Types.EARN_1B);
		}

		if (item == 995 && amount >= 2_147_000_000) {
			Achievements.progressMade(c, Achievements.Types.EARN_MAX_CASH);
		}

		if (amount < 1) {
			amount = 1;
		}
		if (item <= 0) {
			return false;
		}

		if (item == 995 && !c.attributeExists("withdraw_from_pouch")) {
			c.getData().moneyPouchAmount += amount;
			sendMoneyPouch();
			c.getPA().sendMoneyPouchUpdate(amount);
			return true;
		}

		if ((((this.freeInventorySlots() >= 1) || this.playerHasItem(item, 1)) && ItemUtility.isStackable(item))
				|| ((this.freeInventorySlots() > 0) && !ItemUtility.isStackable(item))) {
			for (int i = 0; i < c.inventory.length; i++) {
				if ((c.inventory[i] == (item + 1)) && ItemUtility.isStackable(item) && (c.inventory[i] > 0)) {
					c.inventory[i] = item + 1;
					if (((c.inventoryN[i] + amount) < Config.MAXITEM_AMOUNT) && ((c.inventoryN[i] + amount) > -1)) {
						c.inventoryN[i] += amount;

						if (item == 995 && c.inventoryN[i] > 1_000_000_000) {
							Achievements.progressMade(c, Achievements.Types.EARN_1B);
						}

					} else {
						c.inventoryN[i] = Config.MAXITEM_AMOUNT;

						if (item == 995 && amount >= Config.MAXITEM_AMOUNT) {
							Achievements.progressMade(c, Achievements.Types.EARN_MAX_CASH);
						}
					}
					if (c.getOutStream() != null && c != null) {
						c.getOutStream().createFrameVarSizeWord(34);
						c.getOutStream().writeWord(3214);
						c.getOutStream().writeByte(i);
						c.getOutStream().writeWord(c.inventory[i]);
						if (c.inventoryN[i] > 254) {
							c.getOutStream().writeByte(255);
							c.getOutStream().writeDWord(c.inventoryN[i]);
						} else {
							c.getOutStream().writeByte(c.inventoryN[i]);
						}
						c.getOutStream().endFrameVarSizeWord();
						c.flushOutStream();
					}
					i = 30;
					return true;
				}
			}
			for (int i = 0; i < c.inventory.length; i++) {
				if (c.inventory[i] <= 0) {
					c.inventory[i] = item + 1;
					if ((amount < Config.MAXITEM_AMOUNT) && (amount > -1)) {
						c.inventoryN[i] = 1;
						if (amount > 1) {
							c.getItems().addItem(item, amount - 1);
							return true;
						}
					} else {
						c.inventoryN[i] = Config.MAXITEM_AMOUNT;
					}
						/*
						 * if(c.getOutStream() != null && c != null ) { c.getOutStream().createFrameVarSizeWord(34);
						 * c.getOutStream().writeWord(3214); c.getOutStream().writeByte(i);
						 * c.getOutStream().writeWord(c.inventory[i]); if (c.inventoryN[i] > 254) {
						 * c.getOutStream().writeByte(255); c.getOutStream().writeDWord(c.inventoryN[i]); } else {
						 * c.getOutStream().writeByte(c.inventoryN[i]); } c.getOutStream().endFrameVarSizeWord();
						 * c.flushOutStream(); }
						 */
					this.resetItems(3214);
					i = 30;
					return true;
				}
			}
			return false;
		} else {
			this.resetItems(3214);
			c.sendMessage("Not enough space in your inventory.");
			return false;
		}
	}

	private DecimalFormat df = new DecimalFormat("#.##");

	public void sendMoneyPouch() {
		c.getPA().sendFrame126(Long.toString(c.getData().moneyPouchAmount), 251);

		String amount = Long.toString(c.getData().moneyPouchAmount);
		if (c.getData().moneyPouchAmount > 10_000_000_000L) {
			double d = Math.round(c.getData().moneyPouchAmount / (double) 1_000_000_000);
			amount = df.format(d) + "B";
		} else if (c.getData().moneyPouchAmount > 10_000_000) {
			double d = Math.round(c.getData().moneyPouchAmount / (double) 1_000_000);
			amount = df.format(d) + "M";
		} else if (c.getData().moneyPouchAmount > 1_000) {
			double d = Math.round(c.getData().moneyPouchAmount / (double) 1_000);
			amount = df.format(d) + "K";
		}

		c.getPA().sendFrame126(amount, 252);
	}

	public void resetBonus() {
		for (int i = 0; i < c.playerBonus.length; i++) {
			c.playerBonus[i] = 0;
		}
	}

	public void getBonus() {
		for (int i = 0; i < c.equipment.length; i++) {
			if (c.equipment[i] > -1) {
				for (int k = 0; k < c.playerBonus.length; k++) {
					c.playerBonus[k] += ItemUtility.getBonus(c.equipment[i], k);
				}
			}
		}
	}

	/**
	 * Wear Item
	 */
	public void sendWeapon(int weapon, String weaponName) {
		CombatHelper.sendWeaponInterface(c, weapon, weaponName);
	}

	/**
	 * Weapon Requirements
	 */
	public boolean checkRequirements(int itemId) {

		Optional<ItemDefinition> definitionOptional = ItemDefinition.get(itemId);
		if (!definitionOptional.isPresent()) {
			c.sendMessage("This item does not have a definition in the database.");
			return false;
		}

		ItemDefinition definition = definitionOptional.get();
		if (definition.getWearRequirements() == null) {
			return true;
		}

		for (Map.Entry<Integer, Integer> entry : definition.getWearRequirements().entrySet()) {

			if (entry.getKey() > c.level.length - 1) {
				continue;
			}

			if (c.getActualLevel(entry.getKey()) < entry.getValue()) {
				c.sendMessage("You require level " + entry.getValue() + " " + PlayerConstants.SKILL_NAMES[entry.getKey()] + " to wear this item.");
				return false;
			}
		}

		return true;
	}

	/**
	 * Weapons special bar, adds the spec bars to weapons that require them and removes the spec bars from weapons which
	 * don't require them
	 */
	public void addSpecialBar(int weapon) {
		switch (weapon) {


			case 25040: // blowpipe
			case 13879:
			case 13883: // A vicious throwing axe.
				c.getPA().hideShowLayer(7649, true);
				specialAmount(weapon, c.specAmount, 7661);
				break;

			case 4151: // whip
			case 21371:
			case 15441:
			case 15442:
			case 15443:
			case 15444:
			case 14661:
				c.getPA().hideShowLayer(12323, true);
				this.specialAmount(weapon, c.specAmount, 12335);
				break;
			case 859: // magic bows
			case 861:
			case 20173:
			case 11235:
			case 14481:
			case 14482:
			case 15015:
				c.getPA().hideShowLayer(7549, true);
				this.specialAmount(weapon, c.specAmount, 7561);
				break;
			case 15241:
				c.getPA().hideShowLayer(7549, true);
				this.specialAmount(weapon, c.specAmount, 7561);
				break;
			case 4587: // dscimmy
				c.getPA().hideShowLayer(7599, true);
				this.specialAmount(weapon, c.specAmount, 7611);
				break;
			case 3204: // d hally
				c.getPA().hideShowLayer(8493, true);
				this.specialAmount(weapon, c.specAmount, 8505);
				break;
			case 1377: // d battleaxe
				c.getPA().hideShowLayer(7499, true);
				this.specialAmount(weapon, c.specAmount, 7511);
				break;
			case 4153: // gmaul
				c.getPA().hideShowLayer(7474, true);
				this.specialAmount(weapon, c.specAmount, 7486);
				break;
			case 14484: // d claws
			case 3101:
				c.getPA().hideShowLayer(7800, true);
				this.specialAmount(weapon, c.specAmount, 7812);
				break;
			case 1249: // dspear
			case 13905:
				c.getPA().hideShowLayer(7674, true);
				this.specialAmount(weapon, c.specAmount, 7686);
				break;
			case 13899:
			case 11779:
			case 1305:
				c.getPA().hideShowLayer(7599, true);
				this.specialAmount(weapon, c.specAmount, 7611);
				break;

			case 7158: // d2h
			case 11694: // Godswords and Saradomin sword
			case 11698:
			case 11700:
			case 11730:
			case 11696:
			case 15486:
				c.getPA().hideShowLayer(7699, true);
				this.specialAmount(weapon, c.specAmount, 7711);
				break;
			case 1215:// dragon dagger
			case 1231:
			case 5680:
			case 5698:
			case 13901:
			case 15001:
			case 10887:
			case 19784:
			case 19780:// KORASI!
				c.getPA().hideShowLayer(7574, true);
				this.specialAmount(weapon, c.specAmount, 7586);
				break;

			case 25053: // dragon warhammer
			case 13902: // warhammer
				c.getPA().hideShowLayer(7474, true);
				this.specialAmount(weapon, c.specAmount, 7486);
				break;
			case 1434: // dragon mace
			case 13904:
				c.getPA().hideShowLayer(7624, true);
				this.specialAmount(weapon, c.specAmount, 7686);
				break;
			default:
				c.getPA().hideShowLayer(7624, false); // mace interface
				c.getPA().hideShowLayer(7474, false); // hammer, gmaul
				c.getPA().hideShowLayer(7499, false); // axe
				c.getPA().hideShowLayer(7549, false); // bow interface
				c.getPA().hideShowLayer(7574, false); // sword interface
				c.getPA().hideShowLayer(7599, false); // scimmy sword interface, for most swords
				c.getPA().hideShowLayer(8493, false);
				c.getPA().hideShowLayer(12323, false); // whip interface
				break;
		}
	}

	/**
	 * Specials bar filling amount
	 */
	public void specialAmount(int weapon, double specAmount, int barId) {
		c.specBarId = barId;
		c.getPA().hideShowLayer(barId, false);
		c.getPA().sendFrame87(300, (int) Math.round(specAmount * 100));
		this.sendWeapon(weapon, this.getItemName(weapon));
		this.updateSpecialBar();
	}

	/**
	 * Special attack text and what to highlight or blackout
	 */
	@SuppressWarnings("static-access")
	public void updateSpecialBar() {
		if (c.usingSpecial && c.equipment[c.playerWeapon] != 15050 && c.usingSpecial || c.equipment[c.playerWeapon] != 15486 && c.usingSpecial) {
			c.getPA().sendFrame126("@yel@ Special Attack (" + (int) Math.round(c.specAmount * 10) + "%)", c.specBarId);
		} else {
			c.getPA().sendFrame126("@bla@ Special Attack (" + (int) Math.round(c.specAmount * 10) + "%)", c.specBarId);
		}
	}

	private static int[] ZOMBIE_ITEMS = new int[] { 13462, 13442, 13429, 13458, 13460, 13459, 13444, 13481, 13488, 13445, 13427, 13457, 13456, 13455, 13450, 13451, 13452, 13453, 14486, 19784, 13461, 13405, 13430, 13472, 526, 13478, 13479};

	/**
	 * Wear Item
	 */
	@SuppressWarnings({"static-access", "unused"})
	public boolean wearItem(int wearID, int slot) {

		if(!Zombies.inGameArea(c.getLocation()) && CollectionUtil.getIndexOfValue(ZOMBIE_ITEMS, wearID) > -1) {
			deleteItem(wearID, 1);
			return false;
		}


		int originalBoost = Food.getHpBoost(c);
		int targetSlot = 3;
		boolean canWearItem = true;
		if (c.inventory[slot] == (wearID + 1)) {

			if (!checkRequirements(wearID)) {
				return false;
			}

			// targetSlot = proccesEquipment(wearID);
			// if(Server.itemHandler.ItemList[wearID] != null)
			targetSlot = ItemUtility.getWearIndex(wearID);

			if (targetSlot == -1) {
				c.sendMessage("This item cannot be worn.");
				return false;
			}

			if (targetSlot == c.playerArrows && c.equipment[c.playerWeapon] == 25040) {
				c.sendMessage("You cannot wield any arrows while using the Toxic blowpipe.");
				return false;
			}

			// Completionist cape
			if(wearID == 20769 || wearID == 20770) {
				if(!Achievements.completedAllAchievements(c)) {
					c.sendMessage("You must complete all achievements to wear this.");
					return false;
				}
				if(c.questPoints < QuestHandler.MAX_QUEST_POINTS) {
					c.sendMessage("You must complete all quests to wear this.");
					return false;
				}
				for(int i = 0; i < c.xp.length; i++) {
					if(i == PlayerConstants.CONSTRUCTION || i == PlayerConstants.DUNGEONEERING) {
						continue;
					}
					if(c.getPA().getActualLevel(i) < 99) {
						c.sendMessage("You require level 99 in all skills to wear this.");
						return false;
					}
				}
			}

			if (wearID == 25040) {
				int ammo = c.equipment[c.playerArrows];
				if (ammo != 1) {
					boolean hasSpace = c.getItems().freeInventorySlots() > 0 || c.getItems().playerHasItem(ammo);
					if (!hasSpace) {
						c.sendMessage("You do not have enough space in your inventory.");
						return false;
					}
					int amount = c.playerEquipmentN[c.playerArrows];
					c.getItems().deleteEquipment(ammo, c.playerArrows);
					c.getItems().addItem(ammo, amount);
					c.sendMessage("Your ammunition has been unequipped.");
				}
			}

			for (int i : extreme_Donator) {
				if (i == wearID && c.isMember < 3) {
					c.sendMessage("You can't wear this item, unless you're extreme donator.");
					return false;
				}
			}

			if (wearID >= 7692 && wearID <= 7716) {
				int index = wearID - 7692;
				if (c.xp[index] < 200_000_000) {
					c.sendMessage("This cape requires 200m XP in " + PlayerConstants.SKILL_NAMES[index] + " to wear.");
					return false;
				}
			}

			if (wearID == 1052 && !Achievements.completedAllAchievements(c)) {
				c.sendMessage("You must complete all achievements to wear this.");
				return false;
			}

			if (wearID == 18739 && !c.getData().isHadMostCastleWarsCaptures()) {
				c.sendMessage("You must of achieved most captures in a game of Castlewars to wear this.");
				return false;
			}
			if (wearID == 18740 && !c.getData().isHadMostCastleWarsKills()) {
				c.sendMessage("You must of achieved most kills in a game of Castlewars to wear this.");
				return false;
			}
			if (wearID == 18741 && c.getData().getCastleWarsGamesPlayed() < 500) {
				c.sendMessage("You must of played 500 games of Castlewars to wear this.");
				return false;
			}
			if (wearID == 18742 && c.getData().getCastleWarsGamesPlayed() < 1000) {
				c.sendMessage("You must of played 1000 games of Castlewars to wear this.");
				return false;
			}
			if (wearID == 18743 && c.getData().getCastleWarsGamesPlayed() < 5000) {
				c.sendMessage("You must of played 5000 games of Castlewars to wear this.");
				return false;
			}

			if (c.inCastleWars() && (targetSlot == PlayerConstants.HAT || targetSlot == PlayerConstants.CAPE)) {
				c.sendMessage("You cannot equip this type of item whilst in Castlewars.");
				return false;
			}

			if (wearID >= 2412 && wearID <= 2417) {
				if (c.mageState != 5) {
					c.sendMessage("You must have defeated all forms of Kolodion before you can wear this.");
					return false;
				}
			}

			if (c.getDuel() != null
					&& (c.getDuel().getStage().equals(DuelArena.Stage.STAKING) || c.getDuel().getStage()
					.equals(DuelArena.Stage.CONFIRMING_STAKE))) {
				return false;
			}

			if (c.getDuel() != null) {

				if (c.getDuel().getRules()[PlayerConstants.DUEL_DDS_WHIP] && targetSlot == 3) {
					if (wearID != 1215 && wearID != 1231 && wearID != 5680 && wearID != 5698 && wearID != 4151) {
						c.sendMessage("You can't use that weapon in dds + whip only stakes.");
						return false;
					}
				}

				if (c.getDuel().getRules()[PlayerConstants.DUEL_SAME_WEAPON] && targetSlot == 3) {
					c.sendMessage("You're not allowed to switch weapons in this duel.");
					return false;
				}

				if (c.getDuel().getRules()[PlayerConstants.DUEL_DDS_WHIP]) {
					for (int item : Config.FUN_WEAPONS) {
						if (item == wearID) {
							c.sendMessage("Fun weapons have been disabled in this duel.");
							return true;
						}
					}
				}

				if (c.getDuel().getRules()[PlayerConstants.DUEL_HELMET] && targetSlot == 0) {
					c.sendMessage("Wearing hats has been disabled in this duel!");
					return false;
				}
				if (c.getDuel().getRules()[PlayerConstants.DUEL_CAPE] && targetSlot == 1) {
					c.sendMessage("Wearing capes has been disabled in this duel!");
					return false;
				}
				if (c.getDuel().getRules()[PlayerConstants.DUEL_AMMY] && targetSlot == 2) {
					c.sendMessage("Wearing amulets has been disabled in this duel!");
					return false;
				}
				if (c.getDuel().getRules()[PlayerConstants.DUEL_WEAPON] && targetSlot == 3) {
					c.sendMessage("Wielding weapons has been disabled in this duel!");
					return false;
				}
				if (c.getDuel().getRules()[PlayerConstants.DUEL_BODY] && targetSlot == 4) {
					c.sendMessage("Wearing bodies has been disabled in this duel!");
					return false;
				}
				if ((c.getDuel().getRules()[PlayerConstants.DUEL_SHIELD] && targetSlot == 5)
						|| (c.getDuel().getRules()[PlayerConstants.DUEL_SHIELD] && ItemUtility.isTwoHanded(wearID))) {
					c.sendMessage("Wearing shield has been disabled in this duel!");
					return false;
				}
				if (c.getDuel().getRules()[PlayerConstants.DUEL_LEGS] && targetSlot == 7) {
					c.sendMessage("Wearing legs has been disabled in this duel!");
					return false;
				}
				if (c.getDuel().getRules()[PlayerConstants.DUEL_GLOVES] && targetSlot == 9) {
					c.sendMessage("Wearing gloves has been disabled in this duel!");
					return false;
				}
				if (c.getDuel().getRules()[PlayerConstants.DUEL_BOOTS] && targetSlot == 10) {
					c.sendMessage("Wearing boots has been disabled in this duel!");
					return false;
				}
				if (c.getDuel().getRules()[PlayerConstants.DUEL_RINGS] && targetSlot == 12 && wearID != 13659 && wearID != 13566) {
					c.sendMessage("Wearing rings has been disabled in this duel!");
					return false;
				}
				if (c.getDuel().getRules()[PlayerConstants.DUEL_ARROWS] && targetSlot == 13) {
					c.sendMessage("Wearing arrows has been disabled in this duel!");
					return false;
				}
			}

			if (wearID == 20767) {
				for (int i = 0; i < c.level.length; i++) {

					if (i == PlayerConstants.DUNGEONEERING || i == PlayerConstants.CONSTRUCTION) {
						continue;
					}

					if (c.getPA().getLevelForXP(c.xp[i]) < 99) {
						c.sendMessage("You require level 99 in all skills to wear this cape.");
						return false;
					}
				}
			}
            /*
             * if (wearID > 15750 && wearID < 18500) { c.sendMessage("You cannot wear this item"); return false; }
			 */
			/*
			 * for (int i : Config.ZOMBIE_ITEMS) { if (i == wearID) { if (!c.inZombieGame() &&
			 * !c.username.equalsIgnoreCase("tylers pur3")) { c.sendMessage("You can not wear this item."); return
			 * false; } } }
			 */
			for (int i : super_Donator) {
				if (i == wearID && c.isMember < 2) {
					c.sendMessage("You can't wear this item, unless you're super donator.");
					return false;
				}
			}
			for (int i : Donator) {
				if (i == wearID && c.isMember < 1) {
					c.sendMessage("You can't wear this item, unless you're donator.");
					return false;
				}
			}

			// legends cape
			if (wearID == 1052) {
				if (!Achievements.completedAllAchievements(c)) {
					c.sendMessage("You must complete all achievements to wear this item.");
					canWearItem = false;
				}
			}

			if (ServerEvents.onWearItem(c, wearID, slot, targetSlot)) {
				return false;
			}

			if (!canWearItem) {
				return false;
			}
			int wearAmount = c.inventoryN[slot];
			if (wearAmount < 1) {
				return false;
			}
			if (targetSlot == c.playerWeapon) {
				c.getPA().resetAutocast();
				c.autocastId = 0;
				c.getPA().sendFrame36(108, 0);

				if (getItemName(wearID).startsWith("Dragon")) {
					Achievements.progressMade(c, Achievements.Types.WIELD_DRAGON_WEAPON);
				}

				if (wearID == 4151) {
					Achievements.progressMade(c, Achievements.Types.WIELD_A_WHIP);
				}

			}
			if (slot >= 0 && wearID >= 0) {
				int toEquip = c.inventory[slot];
				int toEquipN = c.inventoryN[slot];
				int toRemove = c.equipment[targetSlot];
				int toRemoveN = c.playerEquipmentN[targetSlot];
				if (toEquip == toRemove + 1 && ItemUtility.isStackable(toRemove)) {
					this.deleteItem(toRemove, this.getItemSlot(toRemove), toEquipN);
					c.playerEquipmentN[targetSlot] += toEquipN;
				} else if (targetSlot != 5 && targetSlot != 3) {

					// fixes making multiple stacks
					if (ItemUtility.isStackable(toRemove) && playerHasItem(toRemove)) {
						for (int i = 0; i < c.inventory.length; i++) {
							if (c.inventory[i] == toRemove + 1) {
								c.inventoryN[i] += toRemoveN;

								c.inventory[slot] = 0;
								c.inventoryN[slot] = 0;
								break;
							}
						}
					} else {
						c.inventory[slot] = toRemove + 1;
						c.inventoryN[slot] = toRemoveN;
					}

					c.equipment[targetSlot] = toEquip - 1;
					c.playerEquipmentN[targetSlot] = toEquipN;
				} else if (targetSlot == 5) {
					boolean wearing2h = ItemUtility.isTwoHanded(c.equipment[c.playerWeapon]);
					boolean wearingShield = c.equipment[c.playerShield] > 0;
					if (wearing2h) {
						toRemove = c.equipment[c.playerWeapon];
						toRemoveN = c.playerEquipmentN[c.playerWeapon];
						c.equipment[c.playerWeapon] = -1;
						c.playerEquipmentN[c.playerWeapon] = 0;
						this.updateSlot(c.playerWeapon);
					}
					c.inventory[slot] = toRemove + 1;
					c.inventoryN[slot] = toRemoveN;
					c.equipment[targetSlot] = toEquip - 1;
					c.playerEquipmentN[targetSlot] = toEquipN;
				} else if (targetSlot == 3) {
					boolean is2h = ItemUtility.isTwoHanded(wearID);
					boolean wearingShield = c.equipment[c.playerShield] > 0;
					boolean wearingWeapon = c.equipment[c.playerWeapon] > 0;
					if (is2h) {
						if (wearingShield && wearingWeapon) {
							if (this.freeInventorySlots() > 0) {
								c.inventory[slot] = toRemove + 1;
								c.inventoryN[slot] = toRemoveN;
								c.equipment[targetSlot] = toEquip - 1;
								c.playerEquipmentN[targetSlot] = toEquipN;
								this.removeItem(c.equipment[c.playerShield], c.playerShield);
							} else {
								c.sendMessage("You do not have enough inventory space to do this.");
								return false;
							}
						} else if (wearingShield && !wearingWeapon) {

							if(c.equipment[targetSlot] == TridentOfTheSeas.CHARGED_TRIDENT || c.equipment[targetSlot] == TridentOfTheSwamp.CHARGED_TRIDENT) {
								c.getPA().resetAutocast();
							}


							c.inventory[slot] = c.equipment[c.playerShield] + 1;
							c.inventoryN[slot] = c.playerEquipmentN[c.playerShield];
							c.equipment[targetSlot] = toEquip - 1;
							c.playerEquipmentN[targetSlot] = toEquipN;
							c.equipment[c.playerShield] = -1;
							c.playerEquipmentN[c.playerShield] = 0;
							this.updateSlot(c.playerShield);
						} else {
							c.inventory[slot] = toRemove + 1;
							c.inventoryN[slot] = toRemoveN;
							c.equipment[targetSlot] = toEquip - 1;
							c.playerEquipmentN[targetSlot] = toEquipN;
						}
					} else {
						c.inventory[slot] = toRemove + 1;
						c.inventoryN[slot] = toRemoveN;
						c.equipment[targetSlot] = toEquip - 1;
						c.playerEquipmentN[targetSlot] = toEquipN;
					}
				}
				c.setAwaitingUpdate(true);
			}
			if (targetSlot == 3) {
				CombatHelper.switchWeapons(c);
				c.usingSpecial = false;
				c.doJavelinSpecial = false;
				this.addSpecialBar(wearID);
			}

			int boost = Food.getHpBoost(c);
			if (originalBoost != boost) {
				int max = c.getPA().getActualLevel(PlayerConstants.HITPOINTS) + boost;
				if (c.level[PlayerConstants.HITPOINTS] > max) {
					c.level[PlayerConstants.HITPOINTS] = max;
					c.getPA().refreshSkill(PlayerConstants.HITPOINTS);
				}
			}

			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(1688);
				c.getOutStream().writeByte(targetSlot);
				c.getOutStream().writeWord(wearID + 1);
				if (c.playerEquipmentN[targetSlot] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord(c.playerEquipmentN[targetSlot]);
				} else {
					c.getOutStream().writeByte(c.playerEquipmentN[targetSlot]);
				}
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
			}
			this.sendWeapon(c.equipment[c.playerWeapon], this.getItemName(c.equipment[c.playerWeapon]));
			this.resetBonus();
			this.getBonus();
			this.writeBonus();

			Animations.setMovementAnimationIds(c);
			c.getPA().requestUpdates();
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("static-access")
	public void wearItem(int wearID, int wearAmount, int targetSlot) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(targetSlot);
			c.getOutStream().writeWord(wearID + 1);
			if (wearAmount > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(wearAmount);
			} else {
				c.getOutStream().writeByte(wearAmount);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
			c.equipment[targetSlot] = wearID;
			c.playerEquipmentN[targetSlot] = wearAmount;
			if (targetSlot == 3) {
				CombatHelper.switchWeapons(c);
			}
			c.getItems().sendWeapon(c.equipment[c.playerWeapon],
					c.getItems().getItemName(c.equipment[c.playerWeapon]));
			c.getItems().resetBonus();
			c.getItems().getBonus();
			c.getItems().writeBonus();
			Animations.setMovementAnimationIds(c);
			c.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
		}
	}

	public void updateSlot(int slot) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(slot);
			c.getOutStream().writeWord(c.equipment[slot] + 1);
			if (c.playerEquipmentN[slot] > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(c.playerEquipmentN[slot]);
			} else {
				c.getOutStream().writeByte(c.playerEquipmentN[slot]);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	/**
	 * Remove Item
	 */
	@SuppressWarnings("static-access")
	public void removeItem(int wearID, int slot) {

		if (CastleWars.onRemoveItem(c, wearID, slot)) {
			return;
		}

		if(c.equipment[slot] == TridentOfTheSeas.CHARGED_TRIDENT || c.equipment[slot] == TridentOfTheSwamp.CHARGED_TRIDENT) {
			c.getPA().resetAutocast();
		}

		if ((c.equipment[slot] == 4335 || c.equipment[slot] == 4355)
				&& (c.getPA().waitingYellow() || c.getPA().waitingRed() || c.inTrinityWar())) {
			c.sendMessage("You cannot unequip your capes!");
			return;
		}
		if (c.getDuel() != null) {
			if (c.getDuel().getRules()[PlayerConstants.DUEL_SAME_WEAPON] && slot == 3) {
				c.sendMessage("You're not allowed to remove weapons in this duel.");
				return;
			}
			if (c.getDuel().getRules()[PlayerConstants.DUEL_DDS_WHIP] && slot == 3) {
				if (wearID != 1215 && wearID != 1231 && wearID != 5680 && wearID != 5698 && wearID != 4151) {
					c.sendMessage("You can't use that weapon in dds + whip only stakes.");
					return;
				}
			}
			if (c.getDuel().getRules()[PlayerConstants.DUEL_DDS_WHIP] & slot == 3
					&& c.getDuel().getStage() == DuelArena.Stage.FIGHTING) {
				c.sendMessage("You cannot switch weapons in this duel!");
				return;
			}
		}
		if (c.getOutStream() != null && c != null) {
			if (c.equipment[slot] > -1) {
				if (this.addItem(c.equipment[slot], c.playerEquipmentN[slot])) {

					int originalBoost = Food.getHpBoost(c);

					c.equipment[slot] = -1;
					c.playerEquipmentN[slot] = 0;

					int boost = Food.getHpBoost(c);
					if (originalBoost != boost) {
						int max = c.getPA().getActualLevel(PlayerConstants.HITPOINTS) + boost;
						if (c.level[PlayerConstants.HITPOINTS] > max) {
							c.level[PlayerConstants.HITPOINTS] = max;
							c.getPA().refreshSkill(PlayerConstants.HITPOINTS);
						}
					}


					this.sendWeapon(c.equipment[c.playerWeapon],
							this.getItemName(c.equipment[c.playerWeapon]));
					this.resetBonus();
					this.getBonus();
					this.writeBonus();
					if (slot == 3) {
						CombatHelper.switchWeapons(c);
					}
					Animations.setMovementAnimationIds(c);
					c.getOutStream().createFrame(34);
					c.getOutStream().writeWord(6);
					c.getOutStream().writeWord(1688);
					c.getOutStream().writeByte(slot);
					c.getOutStream().writeWord(0);
					c.getOutStream().writeByte(0);
					c.flushOutStream();
					c.updateRequired = true;
					c.setAppearanceUpdateRequired(true);

					if (slot == 3 && GreeGree.isGreeGree(wearID)) {
						GreeGree.transform(c);
					}
				}
			}
		}
	}

	public boolean isStackable(int itemID) {
		return ItemUtility.isStackable(itemID);
	}

	/**
	 * Update Equip tab
	 */
	public void setEquipment(int wearID, int amount, int targetSlot) {
		c.getOutStream().createFrameVarSizeWord(34);
		c.getOutStream().writeWord(1688);
		c.getOutStream().writeByte(targetSlot);
		c.getOutStream().writeWord(wearID + 1);
		if (amount > 254) {
			c.getOutStream().writeByte(255);
			c.getOutStream().writeDWord(amount);
		} else {
			c.getOutStream().writeByte(amount);
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
		c.equipment[targetSlot] = wearID;
		c.playerEquipmentN[targetSlot] = amount;
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	/**
	 * Move Items
	 */
	public void moveItems(int from, int to, int moveWindow) {
		if (moveWindow == 3724) {
			int tempI;
			int tempN;
			tempI = c.inventory[from];
			tempN = c.inventoryN[from];
			c.inventory[from] = c.inventory[to];
			c.inventoryN[from] = c.inventoryN[to];
			c.inventory[to] = tempI;
			c.inventoryN[to] = tempN;
		}
		if (moveWindow == 18579) {
			int tempI;
			int tempN;
			tempI = c.inventory[from];
			tempN = c.inventoryN[from];
			c.inventory[from] = c.inventory[to];
			c.inventoryN[from] = c.inventoryN[to];
			c.inventory[to] = tempI;
			c.inventoryN[to] = tempN;
			// updateInventory = true;
			this.resetItems(3214);
		}
		if (moveWindow == 3724) {
			// updateInventory = true;
			this.resetItems(3214);
		}

		if (moveWindow == 3214) {
			int tempI;
			int tempN;
			tempI = c.inventory[from];
			tempN = c.inventoryN[from];
			c.inventory[from] = c.inventory[to];
			c.inventoryN[from] = c.inventoryN[to];
			c.inventory[to] = tempI;
			c.inventoryN[to] = tempN;

			this.resetItems(3214);
		}

	}

	/**
	 * delete Item
	 */
	@SuppressWarnings("static-access")
	public void deleteEquipment(int i, int j) {
		if (PlayerHandler.players[c.playerId] == null) {
			return;
		}
		if (i < 0) {
			return;
		}
		c.equipment[j] = -1;
		c.playerEquipmentN[j] = c.playerEquipmentN[j] - 1;
		c.getOutStream().createFrame(34);
		c.getOutStream().writeWord(6);
		c.getOutStream().writeWord(1688);
		c.getOutStream().writeByte(j);
		c.getOutStream().writeWord(0);
		c.getOutStream().writeByte(0);
		this.getBonus();
		if (j == c.playerWeapon) {
			this.sendWeapon(-1, "Unarmed");
		}
		this.resetBonus();
		this.getBonus();
		this.writeBonus();
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	public void deleteItem(int id, int slot, int amount) {
		if (id <= 0 || slot < 0) {
			return;
		}

		int amountDeleted = 0;

		if (c.inventory[slot] == (id + 1)) {
			if (c.inventoryN[slot] > amount) {
				c.inventoryN[slot] -= amount;
				amountDeleted += amount;
			} else {
				amountDeleted += c.inventoryN[slot];
				c.inventoryN[slot] = 0;
				c.inventory[slot] = 0;
			}
			// updateInventory = true; //see? it just sets updateinventory to true and then approx 300ms later the
			// processmethod checks
			// whether it is set to true
			this.resetItems(3214);
		}

		if (id == 995 && amountDeleted < amount) {
			c.getData().moneyPouchAmount -= (amount - amountDeleted);

			// this should never happen but we had better make sure anyway
			if (c.getData().moneyPouchAmount < 0) {
				c.getData().moneyPouchAmount = 0;
			}

			c.getItems().sendMoneyPouch();
			c.getPA().sendMoneyPouchUpdate(amount);
		}

	}

	public void deleteItem(int id, int amount) {
		int amountDeleted = 0;
		for (int i = 0; i < c.inventory.length; i++) {
			if (c.inventory[i] == (id + 1)) {
				if (c.inventoryN[i] > amount) {
					c.inventoryN[i] -= amount;
					amountDeleted += amount;
				} else {
					amountDeleted += c.inventoryN[i];
					c.inventory[i] = 0;
					c.inventoryN[i] = 0;
				}
			}
			if (amountDeleted >= amount) {
				break;
			}
		}

		if (id == 995 && amountDeleted < amount) {
			c.getData().moneyPouchAmount -= (amount - amountDeleted);

			// this should never happen but we had better make sure anyway
			if (c.getData().moneyPouchAmount < 0) {
				c.getData().moneyPouchAmount = 0;
			}

			c.getItems().sendMoneyPouch();
			c.getPA().sendMoneyPouchUpdate(-amount);
		}

		// updateInventory = true;
		this.resetItems(3214);
	}

	/**
	 * Delete Arrows
	 */
	public void deleteArrow() {
		if (c.equipment[PlayerConstants.WEAPON] == Blowpipe.BLOWPIPE) {
			// avas
			if (!wearingAvas()) {
				c.getData().blowpipeAmmo--;
			} else {
				if (Misc.random(5) == 0) {
					c.getData().blowpipeAmmo--;
				}
			}

			c.getData().blowpipeCharges--;

			if (c.getData().blowpipeAmmo == 0) {
				c.getData().blowpipeAmmoId = 0;
			}

			return;
		}

		if (wearingAvas() && Misc.random(5) != 1 && c.equipment[c.playerArrows] != 4740) {
			return;
		}
		if (c.playerEquipmentN[c.playerArrows] == 1) {
			c.getItems().deleteEquipment(c.equipment[c.playerArrows], c.playerArrows);
		}
		if (c.playerEquipmentN[c.playerArrows] != 0) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(c.playerArrows);
			c.getOutStream().writeWord(c.equipment[c.playerArrows] + 1);
			if (c.playerEquipmentN[c.playerArrows] - 1 > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(c.playerEquipmentN[c.playerArrows] - 1);
			} else {
				c.getOutStream().writeByte(c.playerEquipmentN[c.playerArrows] - 1);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
			c.playerEquipmentN[c.playerArrows] -= 1;
		}
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	public boolean wearingAvas() {
		return c.equipment[c.playerCape] == 25639 || c.equipment[c.playerCape] == 9757 || c.equipment[c.playerCape] == 9756 || c.equipment[c.playerCape] == 10499;
	}

	@SuppressWarnings("static-access")
	public void deleteEquipment() {

		if (wearingAvas() && Misc.random(5) != 1) {
			return;
		}

		if (c.playerEquipmentN[c.playerWeapon] == 1) {
			c.getItems().deleteEquipment(c.equipment[c.playerWeapon], c.playerWeapon);
		}
		if (c.playerEquipmentN[c.playerWeapon] != 0) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(c.playerWeapon);
			c.getOutStream().writeWord(c.equipment[c.playerWeapon] + 1);
			if (c.playerEquipmentN[c.playerWeapon] - 1 > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(c.playerEquipmentN[c.playerWeapon] - 1);
			} else {
				c.getOutStream().writeByte(c.playerEquipmentN[c.playerWeapon] - 1);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
			c.playerEquipmentN[c.playerWeapon] -= 1;
		}
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	/**
	 * Dropping Arrows
	 */
	@SuppressWarnings("static-access")
	public void dropArrowNpc() {
		if (wearingAvas()) {
			return;
		}

		if (Zulrah.inArea(c)) {
			return;
		}

		int enemyX = NPCHandler.npcs[c.oldNpcIndex].getX();
		int enemyY = NPCHandler.npcs[c.oldNpcIndex].getY();
		int enemyZ = NPCHandler.npcs[c.oldNpcIndex].heightLevel;
		if (Misc.random(10) >= 4) {
			int amount = ItemDrops.itemAmount(c.username, c.rangeItemUsed, enemyX, enemyY, enemyZ);

			if (amount == 0) {
				ItemDrops.createGroundItem(c, c.rangeItemUsed, enemyX, enemyY, 1, c.getId(), false);
			} else {
				ItemDrops.removeGroundItem(c, c.rangeItemUsed, enemyX, enemyY, false);
				ItemDrops.createGroundItem(c, c.rangeItemUsed, enemyX, enemyY, amount + 1, c.getId(), false);
			}
		}
	}

	public void dropArrowPlayer() {
	}

	public void removeAllItems() {
		for (int i = 0; i < c.inventory.length; i++) {
			c.inventory[i] = 0;
		}
		for (int i = 0; i < c.inventoryN.length; i++) {
			c.inventoryN[i] = 0;
		}
		// updateInventory = true;
		this.resetItems(3214);
	}

	public int freeInventorySlots() {
		int freeS = 0;
		for (int i = 0; i < c.inventory.length; i++) {
			if (c.inventory[i] <= 0) {
				freeS++;
			}
		}
		return freeS;
	}

	public int findItem(int id, int[] items, int[] amounts) {
		for (int i = 0; i < c.inventory.length; i++) {
			if (((items[i] - 1) == id) && (amounts[i] > 0)) {
				return i;
			}
		}
		return -1;
	}

	/*
	 * public String getItemName(int ItemID) { for (int i = 0; i < Config.ITEM_LIMIT; i++) { if
	 * (Server.itemHandler.ItemList[i] != null) { if (Server.itemHandler.ItemList[i].itemId == ItemID) { return
	 * Server.itemHandler.ItemList[i].itemName; } } } return "Unarmed"; }
	 */
	public String getItemName(int ItemID) {
		if (ItemID == -1) {
			return "Unarmed";
		}
		return ItemUtility.getName(ItemID);
	}

	public int getItemId(String itemName) {
		Optional<Map.Entry<Integer, ItemDefinition>> optional = ItemDefinition.getDefinitions().entrySet().stream().filter(x -> x.getValue().getName().equalsIgnoreCase(itemName)).findFirst();
		return optional.isPresent() ? optional.get().getKey() : -1;
	}

	public int getItemSlot(int ItemID) {
		for (int i = 0; i < c.inventory.length; i++) {
			if ((c.inventory[i] - 1) == ItemID) {
				return i;
			}
		}
		return -1;
	}

	public int getItemAmount(int ItemID) {
		// we use a long as the moneypouch could roll over an int
		long itemCount = 0;

		if (ItemID == 995) {
			itemCount += c.getData().moneyPouchAmount;
		}

		for (int i = 0; i < c.inventory.length; i++) {
			if ((c.inventory[i] - 1) == ItemID) {
				itemCount += c.inventoryN[i];
			}
		}

		if (itemCount > Integer.MAX_VALUE) {
			itemCount = Integer.MAX_VALUE;
		}

		return (int) itemCount;
	}

	public boolean playerHasItem(int itemID) {
		itemID++;
		for (int i = 0; i < c.inventory.length; i++) {
			if (c.inventory[i] == itemID) {
				return true;
			}
		}
		return false;
	}

	public boolean playerHasItem(int itemID, int amt) {
		itemID++;
		long found = 0;

		if (itemID == 996) {
			found += c.getData().moneyPouchAmount;
		}

		for (int i = 0; i < c.inventory.length; i++) {
			if (c.inventory[i] == itemID) {
				found += c.inventoryN[i];
			}
		}

		return found >= amt;
	}

	/**
	 * Drop Item
	 */
	public void createGroundItem(int itemID, int itemX, int itemY, int itemAmount) {
		if (c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(itemY - 8 * c.getLastKnownRegion().getRegionY());
			c.getOutStream().writeByteC(itemX - 8 * c.getLastKnownRegion().getRegionX());
			c.getOutStream().createFrame(44);
			c.getOutStream().writeWordBigEndianA(itemID);
			c.getOutStream().writeWord(itemAmount);
			c.getOutStream().writeByte(0);
			c.flushOutStream();
		}
	}

	/**
	 * Pickup Item
	 */
	public void removeGroundItem(int itemID, int itemX, int itemY, int Amount) {
		if (c == null) {
			return;
		}
		c.getOutStream().createFrame(85);
		c.getOutStream().writeByteC(itemY - 8 * c.getLastKnownRegion().getRegionY());
		c.getOutStream().writeByteC(itemX - 8 * c.getLastKnownRegion().getRegionX());
		c.getOutStream().createFrame(156);
		c.getOutStream().writeByteS(0);
		c.getOutStream().writeWord(itemID);
		c.flushOutStream();
	}

	public boolean ownsCape() {
		if (c.getItems().playerHasItem(2412, 1) || c.getItems().playerHasItem(2413, 1)
				|| c.getItems().playerHasItem(2414, 1)) {
			return true;
		}
		if (c.equipment[c.playerCape] == 2413 || c.equipment[c.playerCape] == 2414
				|| c.equipment[c.playerCape] == 2415) {
			return true;
		}
		return false;
	}

	boolean hasFury() {
		return this.playerHasItem(6585, 1) && this.playerHasItem(19333, 1);
	}

	public void makeFury() {
		if (this.hasFury()) {
			this.deleteItem(6585, 1);
			this.deleteItem(19333, 1);
			this.addItem(19335, 1);
			c.sendMessage("You combine the kit and amulet to make a decorative amulet");
		}
	}

	public boolean hasAllShards() {
		return this.playerHasItem(11712, 1) && this.playerHasItem(11712, 1) && this.playerHasItem(11714, 1);
	}

	boolean hasBlessed() {
		return this.playerHasItem(13734, 1) && this.playerHasItem(13754, 1);
	}

	public void makeBlessed() {
		if (this.hasBlessed()) {
			this.deleteItem(13734, 1);
			this.deleteItem(13754, 1);
			this.addItem(13736, 1);
			c.sendMessage("You pour the elixir over the shield - It takes on a different hue!");
		}
	}

	boolean hasSpectral() {
		return this.playerHasItem(13736, 1) && this.playerHasItem(13752, 1);
	}

	public void makeSpectral() {
		if (this.hasSpectral()) {
			this.deleteItem(13736, 1);
			this.deleteItem(13752, 1);
			this.addItem(13744, 1);
			c.sendMessage("You combine the spectral sigil and blessed spirit shield into one");
		}
	}

	boolean hasDivine() {
		return this.playerHasItem(13736, 1) && this.playerHasItem(13748, 1);
	}

	public void makeDivine() {
		if (this.hasDivine()) {
			this.deleteItem(13736, 1);
			this.deleteItem(13748, 1);
			this.addItem(13740, 1);
			c.sendMessage("You combine the divine sigil and blessed spirit shield into one");
		}
	}

	boolean hasArcane() {
		return this.playerHasItem(13736, 1) && this.playerHasItem(13746, 1);
	}

	public void makeArcane() {
		if (this.hasArcane()) {
			this.deleteItem(13736, 1);
			this.deleteItem(13746, 1);
			this.addItem(13738, 1);
			c.sendMessage("You combine the arcane sigil and blessed spirit shield into one");
		}
	}

	boolean hasElysian() {
		return this.playerHasItem(13736, 1) && this.playerHasItem(13750, 1);
	}

	public void makeElysian() {
		if (this.hasElysian()) {
			this.deleteItem(13736, 1);
			this.deleteItem(13750, 1);
			this.addItem(13742, 1);
			c.sendMessage("You combine the elysian sigil and blessed spirit shield into one");
		}
	}

	public void makeFullSlayerHelm() {
		this.deleteItem(15488, 1);
		this.deleteItem(15490, 1);
		this.deleteItem(13263, 1);
		this.addItem(15492, 1);
		c.sendMessage("You combine the hexcrest, focus sight, and slayer helmet to make a new helmet");
	}

	public boolean hasHelmParts() {
		return this.playerHasItem(15488, 1) && this.playerHasItem(15490, 1) && this.playerHasItem(13263, 1);
	}

	public void makeBlade() {
		this.deleteItem(11710, 1);
		this.deleteItem(11712, 1);
		this.deleteItem(11714, 1);
		this.addItem(11690, 1);
		c.sendMessage("You combine the shards to make a blade.");
	}

	@SuppressWarnings("unused")
	public void makeGodsword(int i) {
		int godsword = i - 8;
		if (this.playerHasItem(11690) && this.playerHasItem(i)) {
			this.deleteItem(11690, 1);
			this.deleteItem(i, 1);
			this.addItem(i - 8, 1);
			c.sendMessage("You combine the hilt and the blade to make a godsword.");
		}
	}

	public boolean isHilt(int i) {
		return i >= 11702 && i <= 11708 && i % 2 == 0;
	}

}