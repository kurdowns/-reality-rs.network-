package com.zionscape.server.model.content.tradingpost.item.container.impl;

import java.util.ArrayList;
import java.util.Arrays;
import com.zionscape.server.model.content.tradingpost.TradingPostManager;
import com.zionscape.server.model.content.tradingpost.item.TradingPostItem;
import com.zionscape.server.model.content.tradingpost.item.collection.TradingPostItemCollection;
import com.zionscape.server.model.content.tradingpost.item.container.TradingPostItemContainer;
import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.net.PacketBuilder;

/**
 * Represents the trading post collection container
 * 
 * @author 2012
 *
 */
public class TradingPostCollectionContainer extends TradingPostItemContainer {


	/**
	 * The scroll
	 */
	private static final int CONTAINER_SCROLL_ID = 76_014;

	/**
	 * The container
	 */
	public static final int CONTAINER_ID = 76_015;

	/**
	 * The coin container
	 */
	public static final int COIN_CONTAINER_ID = 76_005;

	/**
	 * Represents trading post collection container
	 * 
	 * @param player the player
	 */
	public TradingPostCollectionContainer(Player player) {
		super(player);
	}

	/**
	 * The coins
	 */
	private Item coin;

	@Override
	public int capacity() {
		return 60;
	}

	@Override
	public TradingPostItemContainer refreshItems() {
		/*
		 * The items
		 */
		ArrayList<TradingPostItem> items = getValidItems();
		/*
		 * The valid size
		 */
		int validSize = items.size();
		/*
		 * The scroll
		 */
		int scroll = ((1 + validSize) / 10) * 90;
		/*
		 * Fix scroll
		 */
		if (scroll < 87) {
			scroll = 87;
		}
		/*
		 * Sort items
		 */
		sortItems();
		items.trimToSize();
		/*
		 * Send scroll
		 */
		getPlayer().getPA().sendScrollbarHeight(CONTAINER_SCROLL_ID, scroll);
		/*
		 * Send coins
		 */
		getPlayer().getPA().sendItemContainerList(Arrays.asList(getCoin()),
				COIN_CONTAINER_ID);
		/*
		 * Send items
		 */
		sendItemContainerList(items);
		return this;
	}

	/**
	 * Sending item container
	 * 
	 * @param container the container id
	 */
	private void sendItemContainerList(ArrayList<TradingPostItem> container) {
		PacketBuilder out = new PacketBuilder(53);
		out.putInt(CONTAINER_ID);
		out.putShort(container.size());
		for (Item item : container) {
			if (item == null || item.getId() <= 0 || item.getAmount() <= 0) {
				out.putInt(-1);
				continue;
			}
			out.putInt(item.getAmount());
			out.putShort(item.getId() + 1);
		}
		getPlayer().getSession().write(out);
	}

	/**
	 * Withdrawing the coins
	 * 
	 * @param player the player
	 * @param toBank to send to bank
	 */
	public static void collectCoins(Player player, boolean toBank) {
		/*
		 * No coins to collect
		 */
		if (player.getTradingPost().getCollection().getCoin() == null) {
			return;
		}
		/*
		 * The collection amount
		 */
		int amount = TradingPostItemCollection.getCollectionAmount(player);
		/*
		 * Resets coins
		 */
		player.getTradingPost().getCollection().setCoin(null);
		/*
		 * The coins
		 */
		Item coins = new Item(TradingPostManager.CURRENCY, amount);
		/*
		 * Remove
		 */
		TradingPostItemCollection.withdrawCollection(player);
		/*
		 * Add to bank
		 */
		if (toBank) {
			player.getItems().addDirectlyToBank(coins.getId(), coins.getAmount());
			player.sendMessage("The Trading Post coins have been sent to your bank.");
		} else {
			player.getItems().addItem(coins.getId(), coins.getAmount());
			player.sendMessage("You withdraw all the coins from the Trading Post collection.");
		}
		/*
		 * Refresh
		 */
		player.getPA().sendItemContainerList(
				Arrays.asList(player.getTradingPost().getCollection().getCoin()), COIN_CONTAINER_ID);
	}

	/**
	 * Gets the coin
	 *
	 * @return the coin
	 */
	public Item getCoin() {
		return coin;
	}

	/**
	 * Sets the coin
	 * 
	 * @param coin the coin
	 */
	public void setCoin(Item coin) {
		this.coin = coin;
	}
}
