package com.zionscape.server.model.content.tradingpost.item.container.impl;

import com.zionscape.server.model.content.tradingpost.item.container.TradingPostItemContainer;
import com.zionscape.server.model.players.Player;

/**
 * The trading post search container
 * 
 * @author 2012
 *
 */
public class TradingPostSearchContainer extends TradingPostItemContainer {

	/**
	 * The scroll
	 */
	private static final int CONTAINER_SCROLL_ID = 66_113;

	/**
	 * The container
	 */
	public static final int CONTAINER_ID = 66_444;

	/**
	 * The name
	 */
	private static final int NAME_BAR_ID = 66_117;

	/**
	 * The price
	 */
	private static final int PRICE_BAR_ID = 66_118;

	/**
	 * The percentage bar
	 */
	private static final int PERCENTAGE_BAR_ID = 66_119;

	/**
	 * Represents a trading post search container
	 * 
	 * @param player the player
	 */
	public TradingPostSearchContainer(Player player) {
		super(player);
	}

	@Override
	public int capacity() {
		return 30;
	}

	@Override
	public TradingPostItemContainer refreshItems() {
		/*
		 * The valid size
		 */
		int validSize = getValidItems().size();
		/*
		 * The scroll
		 */
		int scroll = ((1 + validSize) / 3) + 150;
		/*
		 * Fix scroll
		 */
		if (scroll < 147) {
			scroll = 147;
		}
		/*
		 * Send scroll
		 */
		getPlayer().getPA().sendScrollbarHeight(CONTAINER_SCROLL_ID, scroll);
		/*
		 * Send display
		 */
		sendItemDisplay(NAME_BAR_ID, PRICE_BAR_ID, PERCENTAGE_BAR_ID);
		/*
		 * Send container
		 */
		getPlayer().getPA().sendItemContainer(this, CONTAINER_ID);
		return this;
	}
}
