package com.zionscape.server.model.content.tradingpost.item.container.impl;

import com.zionscape.server.model.content.tradingpost.TradingPostManager;
import com.zionscape.server.model.content.tradingpost.item.container.TradingPostItemContainer;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.world.shops.Shop;

/**
 * Trading post shop
 * 
 * @author 2012
 *
 */
public class TradingPostShopContainer extends TradingPostItemContainer {

	/**
	 * The container id
	 */
	private static final int CONTAINER_ID = 69_381;

	/**
	 * The container scroll id
	 */
	private static final int CONTAINER_SCROLL_ID = 69_050;

	/**
	 * The name
	 */
	private static final int NAME_BAR_ID = 69_054;

	/**
	 * The price
	 */
	private static final int PRICE_BAR_ID = 69_055;

	/**
	 * The percentage bar
	 */
	private static final int PERCENTAGE_BAR_ID = 69_056;

	/**
	 * The shop owner
	 */
	private String owner;

	/**
	 * Represents a trading post shop
	 * 
	 * @param player the player
	 */
	public TradingPostShopContainer(Player player) {
		super(player);
		setOwner(player.getUsername());
	}

	@Override
	public int capacity() {
		return 30;
	}

	@Override
	public TradingPostItemContainer refreshItems() {
		/*
		 * Trim to size
		 */
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
		 * Sort items
		 */
		sortItems();
		/*
		 * Send scroll
		 */
		getPlayer().getPA().sendScrollbarHeight(CONTAINER_SCROLL_ID, scroll);
		/*
		 * Send display
		 */
		sendItemDisplay(NAME_BAR_ID, PRICE_BAR_ID, PERCENTAGE_BAR_ID);
		/*
		 * Send inventory TODO: null
		 */
		getPlayer().getPA().sendItemContainer(null,
				Shop.INVENTORY_INTERFACE_ID);
		/*
		 * Send shop main container
		 */
		getPlayer().getPA().sendItemContainer(this, CONTAINER_ID);
		/*
		 * Set to global
		 */
		TradingPostManager.tradingPosts.put(validSize > 0 ? this : null, getPlayer().getUsername());
		return this;
	}

	/**
	 * Gets the owner
	 *
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Sets the owner
	 * 
	 * @param owner the owner
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
}
