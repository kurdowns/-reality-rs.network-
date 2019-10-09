package com.zionscape.server.model.content.tradingpost.item.container.impl;

import java.text.NumberFormat;
import com.zionscape.server.model.content.tradingpost.item.TradingPostItem;
import com.zionscape.server.model.content.tradingpost.item.container.TradingPostItemContainer;
import com.zionscape.server.model.players.Player;

/**
 * The trading history container
 * 
 * @author 2012
 *
 */
public class TradingPostHistoryContainer extends TradingPostItemContainer {

	/**
	 * The scroll
	 */
	private static final int CONTAINER_SCROLL_ID = 74_513;

	/**
	 * The container
	 */
	private static final int CONTAINER_ID = 74_794;

	/**
	 * The purchase type
	 */
	private static final int PURCHASE_TYPE = 74_516;

	/**
	 * The name
	 */
	private static final int ITEM_NAME_BAR_ID = 74_517;

	/**
	 * The amount line id
	 */
	private static final int AMOUNT_LINE_ID = 74_518;

	/**
	 * The seller id
	 */
	private static final int SELLER_ID = 74_519;

	/**
	 * The price id
	 */
	private static final int PRICE_ID = 74520;

	/**
	 * The sold line
	 */
	private int soldLine;

	/**
	 * The item name line
	 */
	private int itemNameLine;

	/**
	 * The amount line
	 */
	private int amountLine;

	/**
	 * The seller line
	 */
	private int sellerLine;

	/**
	 * The price line
	 */
	private int priceLine;

	/**
	 * Represents a trading post history container
	 * 
	 * @param player the player
	 */
	public TradingPostHistoryContainer(Player player) {
		super(player);
	}

	/**
	 * Represents the trading post global history container
	 */
	public TradingPostHistoryContainer() {
		super(null);
	}

	@Override
	public int capacity() {
		return 30;
	}

	@Override
	public TradingPostItemContainer refreshItems() {
		refresh(getPlayer());
		return this;
	}

	/**
	 * Refreshes the container
	 * 
	 * @param player the player
	 */
	public void refresh(Player player) {
		if (player == null) {
			return;
		}
		/*
		 * The valid size
		 */
		int validSize = getValidItems().size();
		/*
		 * The scroll
		 */
		int scroll = validSize * 35;
		/*
		 * Fix scroll
		 */
		if (scroll < 183) {
			scroll = 183;
		}
		/*
		 * Send scroll
		 */
		player.getPA().sendScrollbarHeight(CONTAINER_SCROLL_ID, scroll);
		/*
		 * Loops through the items
		 */
		for (int i = 0; i < capacity(); i++) {
			/*
			 * The item
			 */
			TradingPostItem item = getItems()[i];
			/*
			 * Set the lines
			 */
			soldLine = PURCHASE_TYPE + (i * 7);
			itemNameLine = ITEM_NAME_BAR_ID + (i * 7);
			amountLine = AMOUNT_LINE_ID + (i * 7);
			sellerLine = SELLER_ID + (i * 7);
			priceLine = PRICE_ID + (i * 7);
			/*
			 * Invalid item
			 */
			if (item == null || item.getId() < 1) {
				player.sendMessage("hide_interface:" + (soldLine - 2) + ":true");
				player.sendMessage("hide_interface:" + (soldLine - 1) + ":true");
				player.sendMessage("hide_interface:" + (soldLine) + ":true");
				player.sendMessage("hide_interface:" + itemNameLine + ":true");
				player.sendMessage("hide_interface:" + sellerLine + ":true");
				player.sendMessage("hide_interface:" + priceLine + ":true");
				player.sendMessage("hide_interface:" + amountLine + ":true");
				continue;
			} else {
				player.sendMessage("hide_interface:" + (soldLine - 2) + ":false");
				player.sendMessage("hide_interface:" + (soldLine - 1) + ":false");
				player.sendMessage("hide_interface:" + (soldLine) + ":false");
				player.sendMessage("hide_interface:" + itemNameLine + ":false");
				player.sendMessage("hide_interface:" + sellerLine + ":false");
				player.sendMessage("hide_interface:" + priceLine + ":false");
				player.sendMessage("hide_interface:" + amountLine + ":false");
			}
			/*
			 * Send the lines
			 */
			player.getPA().sendFrame126("@or3@" + (item.isBought() ? "Bought" : "Sold"), soldLine);
			player.getPA().sendFrame126("@or1@" + item.getDefinition().getName(), itemNameLine);
			player.getPA().sendFrame126("@whi@X " + item.getAmount(), amountLine);
			player.getPA().sendFrame126("@or2@" + item.getSeller(), sellerLine);
			player.getPA().sendFrame126(
					"@or1@" + NumberFormat.getInstance().format(item.getPrice() * item.getAmount())
							+ " @red@BM\\n@whi@" + NumberFormat.getInstance().format(item.getPrice())
							+ " @red@BM each",
					priceLine);
		}
		/*
		 * Send container
		 */
		player.getPA().sendItemContainer(this, CONTAINER_ID);
	}
}
