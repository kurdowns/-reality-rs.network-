package com.zionscape.server.model.content.tradingpost.item.container;

import java.text.NumberFormat;
import java.util.ArrayList;
import com.zionscape.server.model.content.tradingpost.item.TradingPostItem;
import com.zionscape.server.model.content.tradingpost.item.container.impl.TradingPostHistoryContainer;
import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.players.Player;

/**
 * Represents a container which contains Trading Post items.
 *
 * @author 2012
 */
public abstract class TradingPostItemContainer {

	/**
	 * Player who owns the item container.
	 */
	private Player player;

	/**
	 * The items located in the container.
	 */
	private TradingPostItem[] items = new TradingPostItem[capacity()];

	/**
	 * ItemContainer constructor to create a new instance and to define the player.
	 *
	 * @param player Player who owns the item container.
	 */
	public TradingPostItemContainer(Player player) {
		this.player = player;
		for (int i = 0; i < capacity(); i++) {
			items[i] = new TradingPostItem(-1, 0);
		}
	}

	/**
	 * ItemContainer constructor to create a new instance and to define the player.
	 *
	 * @param player Player who owns the item container.
	 */
	public TradingPostItemContainer(Player player, int capacity) {
		this.player = player;
		items = new TradingPostItem[capacity];
		for (int i = 0; i < capacity; i++) {
			items[i] = new TradingPostItem(-1, 0);
		}
	}

	/**
	 * Sending the items
	 * 
	 * @param nameLineId the name line id
	 * @param priceLineId the price line id
	 * @param percentageLineId the percentage line id
	 */
	public void sendItemDisplay(int nameLineId, int priceLineId, int percentageLineId) {
		/*
		 * The sold
		 */
		int sold = 0;
		/*
		 * The total
		 */
		int total = 0;
		/*
		 * The name line
		 */
		int nameLine = 0;
		/*
		 * The price line
		 */
		int priceLine = 0;
		/*
		 * The percentage line
		 */
		int percentageLine = 0;
		/*
		 * Loops through the items
		 */
		for (int i = 0; i < capacity(); i++) {
			/*
			 * The item
			 */
			TradingPostItem item = getItems()[i];
			/*
			 * The name line
			 */
			nameLine = nameLineId + (i * 11);
			/*
			 * The price line
			 */
			priceLine = priceLineId + (i * 11);
			/*
			 * The percentage line
			 */
			percentageLine = percentageLineId + (i * 11);
			/*
			 * Invalid item
			 */
			if (item == null || item.getId() < 1) {
				getPlayer().sendMessage("hide_interface:" + (nameLine - 3) + ":true");
				getPlayer().sendMessage("hide_interface:" + nameLine + ":true");
				getPlayer().sendMessage("hide_interface:" + priceLine + ":true");
				getPlayer().sendMessage("hide_interface:" + percentageLine + ":true");
				continue;
			} else {
				getPlayer().sendMessage("hide_interface:" + (nameLine - 3) + ":false");
				getPlayer().sendMessage("hide_interface:" + nameLine + ":false");
				getPlayer().sendMessage("hide_interface:" + priceLine + ":false");
				getPlayer().sendMessage("hide_interface:" + percentageLine + ":false");
			}
			/*
			 * The name
			 */
			getPlayer().getPA().sendFrame126("@or1@" + item.getDefinition().getName(), nameLine);
			/*
			 * The price
			 */
			getPlayer().getPA().sendFrame126(
					"@whi@" + NumberFormat.getInstance().format(item.getPrice()) + " @red@BM",
					priceLine);
			/*
			 * The percentage
			 */
			int percentage = 0;
			/*
			 * The sold
			 */
			sold = item.getSold();
			/*
			 * The total
			 */
			total = item.getTotalToSell();
			/*
			 * Set percentage
			 */
			if (sold > 0 && total > 0) {
				percentage = (sold * 100) / total;
			}
			/*
			 * Send percentage bar
			 */
			getPlayer().sendMessage("percentage:" + percentageLine + ":" + percentage + "");
		}
	}

	/**
	 * The amount of items the container can hold, such as 28 for inventory.
	 */
	public abstract int capacity();

	/**
	 * The refresh method to send the container's interface on addition or deletion of an item.
	 */
	public abstract TradingPostItemContainer refreshItems();

	/**
	 * Gets the owner's player instance.
	 *
	 * @return player.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Sets the player viewing the container, used for containers such as Shops.
	 */
	public TradingPostItemContainer setPlayer(Player player) {
		this.player = player;
		return this;
	}

	/**
	 * Gets the items in the container.
	 *
	 * @return items.
	 */
	public TradingPostItem[] getItems() {
		return items;
	}

	/**
	 * Sets all the items in the container.
	 *
	 * @param items The item array to which set the container to hold.
	 */
	public TradingPostItemContainer setItems(TradingPostItem[] items) {
		this.items = items;
		return this;
	}

	/**
	 * Gets the valid items in the container,
	 *
	 * @return items in a list format.
	 */
	public ArrayList<TradingPostItem> getValidItems() {
		ArrayList<TradingPostItem> items = new ArrayList<TradingPostItem>();
		for (TradingPostItem item : this.items) {
			if (item != null && item.getId() > 0) {
				if (item.getAmount() > 0) {
					items.add(item);
				}
			}
		}
		return items;
	}

	/**
	 * Sets the item in said slot.
	 *
	 * @param slot Slot to set item for.
	 * @param item Item that will occupy the slot.
	 */
	public TradingPostItemContainer setItem(int slot, TradingPostItem item) {
		items[slot] = item;
		return this;
	}

	/**
	 * Gets the amount of free slots the container has.
	 *
	 * @return Total amount of free slots in container.
	 */
	public int getFreeSlots() {
		int space = 0;
		for (Item item : items) {
			if (item.getId() == -1) {
				space++;
			}
		}
		return space;
	}

	/**
	 * Checks if the container is out of available slots.
	 *
	 * @return No free slot available.
	 */
	public boolean isFull() {
		return getEmptySlot() == -1;
	}

	/**
	 * Checks if the container is currently empty.
	 *
	 * @return
	 */
	public boolean isEmpty() {
		return getFreeSlots() == capacity();
	}

	/**
	 * Checks if container contains a certain item id.
	 *
	 * @param id The item id to check for in container.
	 * @return Container contains item with the specified id.
	 */
	public boolean contains(int id) {
		for (TradingPostItem items : this.items) {
			if (items.getId() == id) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if container contains a certain item id.
	 *
	 * @param item The item id to check for in container.
	 * @return Container contains item with the specified id.
	 */
	public boolean contains(Item item) {
		return getAmount(item.getId()) >= item.getAmount();
	}

	/**
	 * Checks if this container has a set of certain items.
	 *
	 * @param item the item to check in this container for.
	 * @return true if this container has the item.
	 */
	public boolean contains(TradingPostItem[] item) {
		if (item.length == 0) {
			return false;
		}
		for (Item nextItem : item) {
			if (nextItem == null) {
				continue;
			}
			if (!contains(nextItem.getId())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Gets the next empty slot for an item to equip.
	 *
	 * @return The next empty slot index.
	 */
	public int getEmptySlot() {
		for (int i = 0; i < capacity(); i++) {
			if (items[i].getId() <= 0 || items[i].getAmount() <= 0) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gets the first slot found for an item with said id.
	 *
	 * @param id The id to loop through items to find.
	 * @return The slot index the item is located in.
	 */
	public int getSlot(int id) {
		for (int i = 0; i < capacity(); i++) {
			if (items[i].getId() == id) {
				if (items[i].getAmount() > 0) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * Gets the total amount of items in the container with the specified id.
	 *
	 * @param id The id of the item to search for.
	 * @return The total amount of items in the container with said id.
	 */
	public int getAmount(int id) {
		int totalAmount = 0;
		for (Item item : items) {
			if (item.getId() == id) {
				totalAmount += item.getAmount();
			}
		}
		return totalAmount;
	}

	/**
	 * Gets the total amount of items in the container in the specified slot
	 *
	 * @param id The slot of the item to search for.
	 * @return The total amount of items in the container with said slot.
	 */
	public int getAmountForSlot(int slot) {
		return items[slot].getAmount();
	}

	/**
	 * Resets items in the container.
	 *
	 * @return The ItemContainer instance.
	 */
	public TradingPostItemContainer resetItems() {
		for (int i = 0; i < capacity(); i++) {
			items[i] = new TradingPostItem(-1, 0);
		}
		return this;
	}

	/**
	 * Gets an item by their slot index.
	 *
	 * @param slot Slot to check for item.
	 * @return Item in said slot.
	 */
	public Item forSlot(int slot) {
		return items[slot];
	}

	/**
	 * Adds an item to the item container.
	 *
	 * @param item The item to add.
	 * @return The ItemContainer instance.
	 */
	public TradingPostItemContainer add(TradingPostItem item) {
		return add(item, true);
	}

	/**
	 * Gets the slot by price
	 * 
	 * @param item the item
	 * @return the slot
	 */
	private int getSlotByPrice(TradingPostItem item) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				continue;
			}
			if (items[i].getId() < 1 || items[i].getAmount() < 1) {
				continue;
			}
			if (items[i].getPrice() == item.getPrice() && items[i].getId() == item.getId()) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Adds an item to the item container.
	 *
	 * @param item The item to add.
	 * @param refresh If <code>true</code> the item container interface will be refreshed.
	 * @return The ItemContainer instance.
	 */
	public TradingPostItemContainer addToSearch(ArrayList<TradingPostItem> list) {
		for (TradingPostItem item : list) {
			if (item == null) {
				continue;
			}
			add(item, false);
		}
		refreshItems();
		return this;
	}

	/**
	 * Adds an item to the item container.
	 *
	 * @param item The item to add.
	 * @param refresh If <code>true</code> the item container interface will be refreshed.
	 * @return The ItemContainer instance.
	 */
	public TradingPostItemContainer add(TradingPostItem item, boolean refresh) {
		if (item.getId() <= 0) {
			return this;
		}

		boolean history = this instanceof TradingPostHistoryContainer;

		int existing = getSlotByPrice(item);

		if (existing != -1 && !history) {
			items[existing].setAmount(items[existing].getAmount() + item.getAmount());
			items[existing].setTotalToSell(items[existing].getTotalToSell() + item.getAmount());
			refreshItems();
			return this;
		}

		int amount = item.getAmount();
		while (amount > 0) {
			existing = getSlotByPrice(item);
			int slot = history ? getEmptySlot() : (existing != -1 ? existing : getEmptySlot());
			if (slot == -1) {
				getPlayer().sendMessage("You couldn't hold all those items.");
				if (refresh) {
					refreshItems();
				}
				return this;
			} else {
				items[slot] = item;
			}
			amount--;
		}
		if (refresh) {
			refreshItems();
		}
		return this;
	}

	public TradingPostItemContainer addToHistory(TradingPostItem item) {
		if (item.getId() <= 0) {
			return this;
		}
		int slot = getEmptySlot();
		if (slot == -1) {
			return this;
		} else {
			items[slot] = item;
		}
		return this;
	}

	/**
	 * Deletes an item from the item container.
	 *
	 * @param item The item to delete.
	 * @return The ItemContainer instance.
	 */
	public TradingPostItemContainer delete(Item item) {
		return delete(item.getId(), item.getAmount());
	}

	/**
	 * Deletes an item from the item container.
	 *
	 * @param id The id of the item to delete.
	 * @param amount The amount of the item to delete.
	 * @return The ItemContainer instance.
	 */
	public TradingPostItemContainer delete(int id, int amount) {
		return delete(id, amount, true);
	}

	/**
	 * Deletes an item from the item container.
	 *
	 * @param id The id of the item to delete.
	 * @param amount The amount of the item to delete.
	 * @param refresh If <code>true</code> the item container interface will refresh.
	 * @return The ItemContainer instance.
	 */
	public TradingPostItemContainer delete(int id, int amount, boolean refresh) {
		return delete(new TradingPostItem(id, amount), getSlot(id), refresh);
	}

	/**
	 * Deletes an item from the item container.
	 *
	 * @param item The item to delete.
	 * @param slot The slot of the item to delete.
	 * @param refresh If <code>true</code> the item container interface will refresh.
	 * @return The ItemContainer instance.
	 */
	public TradingPostItemContainer delete(TradingPostItem item, int slot, boolean refresh) {
		return delete(item, slot, refresh, null);
	}

	/**
	 * Deletes an item from the item container.
	 *
	 * @param item The item to delete.
	 * @param slot The slot of the item to delete.
	 * @param refresh If <code>true</code> the item container interface will refresh.
	 * @param toContainer To check if other container has enough space to continue deleting said
	 *        amount from this container.
	 * @return The ItemContainer instance.
	 */
	public TradingPostItemContainer delete(TradingPostItem item, int slot, boolean refresh,
			TradingPostItemContainer toContainer) {
		if (item.getId() <= 0 || slot < 0) {
			return this;
		}
		if (item.getAmount() > getAmount(item.getId())) {
			item.setAmount(getAmount(item.getId()));
		}
		items[slot].setAmount(items[slot].getAmount() - item.getAmount());
		if (items[slot].getAmount() < 1) {
			items[slot].setAmount(0);
			items[slot].setId(-1);
		}
		if (refresh) {
			refreshItems();
		}
		return this;
	}

	/**
	 * Gets an item id by its index.
	 *
	 * @param index the index.
	 * @return the item id on this index.
	 */
	public TradingPostItem getById(int id) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				continue;
			}
			if (items[i].getId() == id) {
				return items[i];
			}
		}
		return null;
	}

	/**
	 * Sorting items
	 * 
	 * @return sorted items
	 */
	public TradingPostItemContainer sortItems() {
		for (int k = 0; k < capacity(); k++) {
			if (getItems()[k] == null) {
				continue;
			}
			for (int i = 0; i < (capacity() - 1); i++) {
				if (getItems()[i] == null || getItems()[i].getId() <= 0) {
					swap((i + 1), i);
				}
			}
		}
		return this;
	}

	/**
	 * Swaps two item slots.
	 *
	 * @param fromSlot From slot.
	 * @param toSlot To slot.
	 */
	public TradingPostItemContainer swap(int fromSlot, int toSlot) {
		TradingPostItem temporaryItem = getItems()[fromSlot];
		if (temporaryItem == null || temporaryItem.getId() <= 0) {
			return this;
		}
		set(fromSlot, getItems()[toSlot]);
		set(toSlot, temporaryItem);
		return this;
	}

	/**
	 * Sets the item in slot
	 * 
	 * @param slot the slot
	 * @param item the item
	 */
	public void set(int slot, TradingPostItem item) {
		items[slot] = item;
	}

	/**
	 * Gets the item from slot
	 * 
	 * @param slot the slot
	 * @return the item
	 */
	public TradingPostItem get(int slot) {
		return items[slot];
	}
}
