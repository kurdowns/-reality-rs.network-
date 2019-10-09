package com.zionscape.server.model.players;

import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.content.grandexchange.GrandExchange;
import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.util.Misc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Bank {

	private final Player player;
	private final List<List<Item>> tabs = new ArrayList<>();
	private int tabSelected = 0;
	private boolean withdrawAsNote = false;
	private boolean searching = false;

	public Bank(Player player) {
		this.player = player;
		tabs.add(new ArrayList<>());
	}

	public void clearBank() {
		for(List<Item> l : tabs) {
			l.clear();
		}
		tabs.clear();
	}

	public static final int getMaxBankSize(Player player) {
		return 600 + (player.isMember * 200);
	}

	public void addItem(int tab, int slot, int itemId, int amount) {
		if (tab > tabs.size() - 1) {
			tabs.add(new ArrayList<>());
		}

		tabs.get(tab).add(slot, new Item(itemId, amount));
	}

	public boolean clickingButton(int button) {
		if (!player.isBanking || player.storingFamiliarItems) {
			return false;
		}

		if (button == 85252) { // Deposit inventory
			for (int i = 0; i < player.inventory.length; i++) {
				if (player.inventory[i] <= 0) {
					continue;
				}
				depositItem(player.inventory[i] - 1, player.inventoryN[i], i);
			}
			return true;
		}

		if (button == 86000) { // Deposit equipment

			if (player.getItems().freeInventorySlots() == 0) {
				player.sendMessage("You must have one free inventory space to do this.");
				return true;
			}

			for (int i = 0; i < player.equipment.length; i++) {
				if (player.equipment[i] <= 0) {
					continue;
				}
				if(depositItem(player.equipment[i], player.playerEquipmentN[i], -1, true, false)) {
					player.getItems().setEquipment(-1, 0, i);
					player.getPA().resetAnimation();
					player.getItems().resetBonus();
					player.getItems().getBonus();
					player.getItems().writeBonus();
				}
			}

			return true;
		}

		if (button == 86004) { // Deposit equipment

			if(player.getFamiliar() == null) {
				return true;
			}

			if(player.storingFamiliarItems) {
				return true;
			}

			Iterator<Item> familiarInventory = player.getFamiliar().getInventoryItems().iterator();
			while(familiarInventory.hasNext()) {
				Item item = familiarInventory.next();

				if(depositItem(item.getId(), item.getAmount(), -1, true, false)) {
					familiarInventory.remove();
				}

			}

			return true;
		}

		if (button == 85244) {
			searching = !searching;
		}

		if (button == 85248) {
			withdrawAsNote = !withdrawAsNote;
			return true;
		}

		if (button >= 86008 && button <= 86016) {

			int selected = button - 86008;

			if (selected > tabs.size() - 1) {
				player.sendMessage("Drag an item on to the tab to create a new tab.");
				return true;
			}

			tabSelected = selected;

			sendTabItems(selected);
			player.getPA().setScrollPos(5385, 0);
			player.getPA().sendFrame126(Integer.toString(tabSelected), 27002); // The current tab?
			player.getPA().sendFrame126("1", 27000); // Tells client to update???
			return true;
		}

		return false;
	}

	public void interfaceAction(int id, int action) {
		if (!(id >= 22025 && id <= 22032)) {
			return;
		}

		int selected = id - 22024;

		// tab dont exist check
		if (selected > tabs.size() - 1) {
			player.sendMessage("Drag an item on to the tab to create a new tab.");
			return;
		}

		// view tab
		if (action == 0) {
			tabSelected = selected;

			sendTabItems(selected);
			player.getPA().setScrollPos(5385, 0);
			player.getPA().sendFrame126(Integer.toString(tabSelected), 27002); // The current tab?
			player.getPA().sendFrame126("1", 27000); // Tells client to update???
		}

		// collapse
		if (action == 1) {
			for (Item item : tabs.get(selected)) {
				tabs.get(0).add(item);
			}

			tabs.remove(selected);

			player.getPA().sendFrame126(Integer.toString(tabs.size() > 0 ? tabs.size() - 1 : 0), 27001); // Bank tab
			for (int i = 1; i < 9; i++) {
				drawTabHeaderItem(i);
				sendTabItems(i);
			}

			if (tabSelected == selected) {
				tabSelected = 0;
				player.getPA().sendFrame126(Integer.toString(tabSelected), 27002); // The current tab?
			}

			player.getPA().sendFrame126("1", 27000); // Tells client to update???
		}
	}

	public boolean depositItem(int itemId, int amount, int slot) {
		return depositItem(itemId, amount, slot, false, true);
	}

	public boolean depositItem(int itemId, int amount, int slot, boolean byPass, boolean deleteItems) {

		if (!byPass) {

			// Make sure its a valid deposit request
			if (slot < 0 || slot > player.inventory.length - 1 || amount < 1 || !player.isBanking
					|| player.storingFamiliarItems) {
				return false;
			}

			// Make sure the player has the item
			if (itemId != player.inventory[slot] - 1) {
				return false;
			}

			// Make sure the player has the given amount of the item
			if (ItemUtility.isStackable(itemId) && amount > player.inventoryN[slot]) {
				amount = player.inventoryN[slot];
			} else {
				int tempAmount = player.getItems().getItemAmount(itemId);
				if (amount > tempAmount) {
					amount = tempAmount;
				}
			}
		}

		int itemIdToStore = itemId;

		// We store all items as there unoted id
		if (ItemUtility.isNote(itemId)) {
			itemIdToStore = ItemUtility.getUnotedId(itemId);
		}

		// Check if we already have the item in the bank
		int hasItemAtTab = -1;
		int hasItemAtSlot = -1;
		loop:
		for (int a = 0; a < tabs.size(); a++) {
			for (int b = 0; b < tabs.get(a).size(); b++) {
				if (tabs.get(a).get(b).getId() == itemIdToStore) {
					hasItemAtSlot = b;
					hasItemAtTab = a;
					break loop;
				}
			}
		}

		// Add the item to the bank
		if (hasItemAtSlot > -1) {
			Item item = tabs.get(hasItemAtTab).get(hasItemAtSlot);

			// Check if the player already has a max stack of the given item
			if (item.getAmount() == Integer.MAX_VALUE) {
				player.sendMessage("The bank has reached max capacity for this item.");
				return false;
			}

			// Reduce the amount the player can deposit if the total amount exceeds max value
			long longAmount = (long) item.getAmount() + (long) amount;
			if (longAmount > Integer.MAX_VALUE) {
				amount = Integer.MAX_VALUE - item.getAmount();
			}

			tabs.get(hasItemAtTab).get(hasItemAtSlot)
					.setAmount(tabs.get(hasItemAtTab).get(hasItemAtSlot).getAmount() + amount);
		} else {
			// check if the players bank is already full
			if (getTotalAmountOfItems() + 1 > getMaxBankSize(player)) {
				player.sendMessage("Your bank is full.");
				return false;
			}

			// this is for checking for blank spots upon withdrawing an item
			boolean depositedItem = false;
			for (Item item : tabs.get(tabSelected)) {
				if (item.getId() == -1) {
					item.setId(itemIdToStore);
					item.setAmount(amount);
					depositedItem = true;
					break;
				}
			}

			if (!depositedItem)
				tabs.get(tabSelected).add(new Item(itemIdToStore, amount));
		}

		// Delete the item from the inventory
		if (deleteItems) {
			player.getItems().deleteItem(itemId, amount);
		}

		// if the item is already in the bank but on a different tab make that tab the active one
		/*
		 * if (hasItemAtTab > -1 && tabSelected != hasItemAtTab) { tabSelected = hasItemAtTab;
		 * player.getPA().sendFrame126(Integer.toString(tabSelected), 27002); // The current tab?
		 * player.getPA().sendFrame126("1", 27000); // Tells client to update??? }
		 */

		// send bank items, update the amount of items in the bank counter and resend the players inventory
		sendTabItems(tabSelected);
		player.getPA().sendFrame126(Integer.toString(getTotalAmountOfItems()), 22033); // Send total Bank Items
		player.getItems().resetItems(5064);

		return true;
	}

	private void drawTabHeaderItem(int tabId) {
		if (tabId > tabs.size() - 1) {
			player.getPA().sendFrame34(-1, 0, 22034 + tabId, 1);
		} else {
			Item item = tabs.get(tabId).get(0);

			player.sendFrame34(22034 + tabId, item.getId(), 0, ItemUtility.isStackable(item.getId()) ? item.getAmount() : 1);
		}
	}

	private int getTabHeaderInterfaceToTabId(int interfaceId) {
		switch (interfaceId) {
			case 34176:
				return 0;
			case 36224:
				return 1;
			case 39552:
				return 2;
			case 42880:
				return 3;
			case 46208:
				return 4;
			case 49536:
				return 5;
			case 52864:
				return 6;
			case 56192:
				return 7;
			case 59520:
				return 8;
		}
		return -1;
	}

	public List<List<Item>> getTabs() {
		return tabs;
	}

	private int getTotalAmountOfItems() {
		int count = 0;
		for (List<Item> items : tabs) {
			count += items.stream().filter(x -> x.getId() != -1).count();
		}
		return count;
	}

	private boolean isTabHeaderInterface(int interfaceId) {
		switch (interfaceId) {
			case 34176:
			case 36224:
			case 39552:
			case 42880:
			case 46208:
			case 49536:
			case 52864:
			case 56192:
			case 59520:
				return true;
		}
		return false;
	}

	public void moveItems(int from, int to, int window, int windowTo, boolean insert) {

		// Moving items in inventory
		if (window == 5064) {
			if (from < 0 || to < 0) {
				return;
			}

			// cant move non existing items
			if (player.inventory[from] <= 0) {
				return;
			}

			Item fromItem = new Item(player.inventory[from], player.inventoryN[from]);
			Item toItem = new Item(player.inventory[to], player.inventoryN[to]);

			player.inventory[to] = fromItem.getId();
			player.inventoryN[to] = fromItem.getAmount();

			player.inventory[from] = toItem.getId();
			player.inventoryN[from] = toItem.getAmount();

			player.getItems().resetItems(5064);
			player.getItems().resetItems(3214);
			return;
		}

		// stop players moving items round whilst in searching mode
		if (searching) {
			return;
		}

		// check the window doesnt exceed the amount of tabs
		int tab = window - 15292;
		if (tab < 0 || tab > tabs.size()) {
			return;
		}

		// current items on the selected tab
		List<Item> currentTab = tabs.get(tab);

		// check to make sure we're moving a valid item
		if (currentTab == null || from > currentTab.size() - 1 || currentTab.get(from) == null) {
			return;
		}

		// Moving items from one tab to another
		if (isTabHeaderInterface(windowTo)) {

			int newTabId = getTabHeaderInterfaceToTabId(windowTo);

			// make sure its a valid tab
			if (newTabId == -1) {
				return;
			}

			// Need to know if a new tab has been created or not
			boolean newTab = false;

			// does the tab already exist if not create it
			if (newTabId > tabs.size() - 1 && tabs.size() < 9) {
				tabs.add(new ArrayList<>());
				newTab = true;
			}

			// move the item to the new tab
			tabs.get(newTabId).add(currentTab.get(from));

			// delete the item from the current tab
			currentTab.remove(from);

			// has a tab been removed
			boolean tabRemoved = false;

			// remove old tab
			if (currentTab.size() == 0) {
				tabs.remove(currentTab);
				tabRemoved = true;
			}

			// Draw the new
			if (newTab) {
				Achievements.progressMade(player, Achievements.Types.MAKE_A_BANK_TAB);

				for (int i = 1; i < 9; i++) {
					drawTabHeaderItem(i);
				}
			}

			// Check if we need to redraw current tab header item
			if (tabSelected > 0 && from == 0) {
				for (int i = 1; i < 9; i++) {
					drawTabHeaderItem(i);
				}
				tabSelected = tabSelected - 1;
			}

			if (tabRemoved && newTabId > 0) {
				newTabId--;
			}

			// Move the client to the new tab
			// tabSelected = newTabId;
			for (int i = 0; i < 9; i++) {
				sendTabItems(i);
			}
			player.getPA().sendFrame126(Integer.toString(tabs.size() > 0 ? tabs.size() - 1 : 0), 27001); // Bank tab
			// count
			player.getPA().sendFrame126(Integer.toString(tabSelected), 27002); // The current tab?
			player.getPA().sendFrame126("1", 27000); // Tells client to update???
		} else if (tabSelected == 0 && windowTo > 0 && window != windowTo) { // moving items between tabs in the main tab
			List<Item> fromTab = tabs.get(window - 15292);
			List<Item> toTab = tabs.get(windowTo - 15292);

			// check both tabs exist
			if (fromTab == null || toTab == null) {
				return;
			}

			Item fromItem = fromTab.get(from);


			if (to > toTab.size() - 1) {
				int index = toTab.size();
				toTab.add(index, new Item(-1, 0));
			}

			Item toItem = toTab.get(to);

			fromTab.set(from, toItem);
			toTab.set(to, fromItem);

			// Move the client to the new tab
			// tabSelected = newTabId;
			for (int i = 0; i < 9; i++) {
				sendTabItems(i);
			}
			player.getPA().sendFrame126("1", 27000); // Tells client to update???

		} else {
			Item item = currentTab.get(from);

			if (insert) {
				Item[] items = new Item[currentTab.size()];

				for (int i = 0; i < items.length; i++) {
					items[i] = currentTab.get(i);
				}

				if (to > from) {
					for (int slot = from; slot < to; slot++) {
						swap(items, slot, slot + 1);
					}
				} else if (from > to) {
					for (int slot = from; slot > to; slot--) {
						swap(items, slot, slot - 1);
					}
				}

				currentTab.clear();
				for (int i = 0; i < items.length; i++) {
					currentTab.add(items[i]);
				}
			} else {
				if (to < currentTab.size()) {
					currentTab.set(from, currentTab.get(to));
					currentTab.set(to, item);
				}
			}
		}
	}

	private void swap(Item[] items, int from, int to) {
		Item item = items[from];
		items[from] = items[to];
		items[to] = item;
	}

	public void openBank() {

		if (GrandExchange.usingGrandExchange(player)) {
			return;
		}

		if (player.hardcoreIronman) {
			player.sendMessage("You cannot do this as an hardcore ironman.");
			return;
		}

		searching = false;
		//tabSelected = 0;

		// remove any -1 items
		for (List<Item> items : tabs) {
			Iterator<Item> itr = items.iterator();
			while (itr.hasNext()) {
				Item item = itr.next();

				if (item.getId() == -1) {
					itr.remove();
				}
			}
		}

		sendTabItems(tabSelected);

		// draws the tab header items
		// send the items
		for (int i = 1; i < 9; i++) {
			drawTabHeaderItem(i);
			sendTabItems(i);
		}

		// send all the info to the player
		player.getPA().sendFrame36(304, 0);
		player.getPA().sendFrame36(0, 0);
		player.getPA().sendFrame36(115, withdrawAsNote ? 1 : 0);
		player.getPA().sendFrame126(Integer.toString(getTotalAmountOfItems()), 22033); // Send total Bank Items
		player.getPA().sendFrame126(Integer.toString(getMaxBankSize(player)), 22034); // Send max Bank Items - 496
		player.getPA().sendFrame126(Integer.toString(tabs.size() > 0 ? tabs.size() - 1 : 0), 27001); // Bank tab count
		player.getPA().sendFrame126(Integer.toString(tabSelected), 27002); // The current tab?
		player.getPA().sendFrame126("1", 27000); // Tells client to update???

		player.getPA().sendFrame248(5292, 5063);
		player.getItems().resetItems(5064);

		player.isBanking = true;
	}

	public int playerHasItem(int itemId) {

		// We store all items as there unoted id
		if (ItemUtility.isNote(itemId)) {
			itemId = ItemUtility.getUnotedId(itemId);
		}

		for (List<Item> items : tabs) {
			for (Item item : items) {
				if (item.getId() == itemId) {
					return item.getAmount();
				}
			}
		}

		return -1;
	}

	private void sendTabItems(int tab) {
		player.getPA().sendFrame126(Integer.toString(getTotalAmountOfItems()), 22033); // Send total Bank Items

		// empty tab
		if (tab > tabs.size() - 1) {
			player.getOutStream().createFrame(72);
			player.getOutStream().writeWordBigEndian(15292 + tab);
			return;
		}

		List<Item> items = tabs.get(tab);

		player.getOutStream().createFrameVarSizeWord(53);
		player.getOutStream().writeWord(15292 + tab);
		player.getOutStream().writeWord(items.size());

		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);

			if (item.getAmount() > 254) {
				player.getOutStream().writeByte(255);
				player.getOutStream().writeDWord_v2(item.getAmount());
			} else {
				player.getOutStream().writeByte(item.getAmount());
			}

			player.getOutStream().writeWordBigEndianA(item.getId() + 1);
		}

		player.getOutStream().endFrameVarSizeWord();
		player.flushOutStream();

		player.getPA().sendFrame126("1", 27000); // Tells client to update???
	}

	public void withdrawItem(int itemId, int amount, int slot, int window) {

		// make sure the requst is valid
		if (player.storingFamiliarItems || slot < 0 || !player.isBanking) {
			return;
		}

		slot = -1;
		int tabIndex = -1;
		Item item = null;
		for (int index = 0; index < tabs.size(); index++) {
			item = tabs.get(index).stream().filter(x -> x.getId() == itemId).findFirst().orElse(null);
			if (item != null) {
				tabIndex = index;

				slot = tabs.get(index).indexOf(item);
				break;
			}
		}

		// item doesnt exist
		if (item == null || tabIndex == -1 || slot == -1) {
			return;
		}

		// check item amount
		if (amount > item.getAmount()) {
			amount = item.getAmount();
		}

		// is item stackable
		boolean stackable = (withdrawAsNote && ItemUtility.isNote(ItemUtility.getNotedId(itemId)) || ItemUtility.isStackable(itemId));

		// inventory free space checking
		int freeSlots = player.getItems().freeInventorySlots();
		int requiredSlots = 0;


		// we don't want to roll over integer max value
		if (stackable && player.getItems().playerHasItem(itemId)) {
			int playerHas = player.getItems().getItemAmount(itemId);

			if ((long) amount + (long) playerHas > Integer.MAX_VALUE) {
				//its rolling over oh nooo
				amount = Integer.MAX_VALUE - playerHas;

				player.sendMessage("You cannot store more than " + Misc.formatNumber(Integer.MAX_VALUE) + " of any item in your inventory.");

				if (amount > 0) {
					withdrawItem(itemId, amount, slot, -1);
				}
				return;
			}
		}

		if (stackable && !player.getItems().playerHasItem(itemId)) {
			requiredSlots = 1;
		} else if (!stackable) {
			requiredSlots = amount;
		}

		if (requiredSlots > 0 && freeSlots == 0) {
			player.sendMessage("You do not have enough free inventory space.");
			return;
		}

		if (!stackable && requiredSlots > 0 && amount > freeSlots) {
			amount = freeSlots;
			player.sendMessage("You do not have enough free inventory space.");
		}

		if (amount == 0) {
			return;
		}

		// remove the item from the bank
		if (amount >= item.getAmount()) {
			tabs.get(tabIndex).get(slot).setId(-1);

			// check if any items are left on the tab
			int itemCount = 0;
			for (Item item1 : tabs.get(tabIndex)) {
				if (item1.getId() > -1) {
					itemCount++;
				}
			}

			// delete the tab
			if (tabIndex > 0 && itemCount == 0) {
				tabs.remove(tabIndex);

				tabIndex -= 1;

				tabSelected = tabIndex;

				for (int i = 1; i < 9; i++) {
					drawTabHeaderItem(i);
					sendTabItems(i);
				}
				player.getPA().sendFrame126(Integer.toString(tabs.size() > 0 ? tabs.size() - 1 : 0), 27001); // Bank tab
				// count
				player.getPA().sendFrame126(Integer.toString(tabIndex), 27002); // The current tab?
				player.getPA().sendFrame126("1", 27000); // Tells client to update???
			}
		} else {
			tabs.get(tabIndex).get(slot).setAmount(item.getAmount() - amount);
		}

		// Add the item
		if (withdrawAsNote && ItemUtility.isNote(ItemUtility.getNotedId(itemId))) {
			player.getItems().addItem(ItemUtility.getNotedId(itemId), amount);
		} else {
			player.getItems().addItem(itemId, amount);
		}

		// send bank items, update the amount of items in the bank counter and resend the players inventory
		sendTabItems(tabIndex);
		player.getPA().sendFrame126(Integer.toString(getTotalAmountOfItems()), 22033); // Send total Bank Items
		player.getItems().resetItems(5064);
	}

}
