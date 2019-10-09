package com.zionscape.server.model.content.tradingpost.item;

import com.zionscape.server.model.items.Item;

/**
 * Represents a trading post item
 * 
 * @author 2012
 *
 */
public class TradingPostItem extends Item {

	/**
	 * The selling price
	 */
	private int price;

	/**
	 * The total price
	 */
	private int totalToSell;

	/**
	 * How many have been sold
	 */
	private int sold;

	/**
	 * The user selling the item
	 */
	private String seller;

	/**
	 * Bought or sold
	 */
	private boolean bought;

	/**
	 * Represents a trading post item
	 * 
	 * @param id the id
	 * @param amount the amount
	 */
	public TradingPostItem(int id, int amount) {
		super(id, amount);
		setPrice(getDefinition().getGeValue());
		setTotalToSell(0);
	}

	/**
	 * Gets the price
	 *
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * Sets the price
	 * 
	 * @param price the price
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * Gets the totalToSell
	 *
	 * @return the totalToSell
	 */
	public int getTotalToSell() {
		return totalToSell;
	}

	/**
	 * Sets the totalToSell
	 * 
	 * @param totalToSell the totalToSell
	 */
	public void setTotalToSell(int totalToSell) {
		this.totalToSell = totalToSell;
	}

	/**
	 * Gets the sold
	 *
	 * @return the sold
	 */
	public int getSold() {
		return sold;
	}

	/**
	 * Sets the sold
	 * 
	 * @param sold the sold
	 */
	public void setSold(int sold) {
		this.sold = sold;
	}

	/**
	 * Gets the seller
	 *
	 * @return the seller
	 */
	public String getSeller() {
		return seller;
	}

	/**
	 * Sets the seller
	 * 
	 * @param seller the seller
	 */
	public void setSeller(String seller) {
		this.seller = seller;
	}

	/**
	 * Gets the bought
	 *
	 * @return the bought
	 */
	public boolean isBought() {
		return bought;
	}

	/**
	 * Sets the bought
	 * 
	 * @param bought the bought
	 */
	public void setBought(boolean bought) {
		this.bought = bought;
	}
}