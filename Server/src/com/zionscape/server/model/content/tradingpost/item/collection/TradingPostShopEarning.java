package com.zionscape.server.model.content.tradingpost.item.collection;

/**
 * Represents a shop earning
 * 
 * @author 2012
 */
public class TradingPostShopEarning {

	/**
	 * The amount
	 */
	private long amount;

	/**
	 * The username
	 */
	private String username;

	/**
	 * The shop earning
	 * 
	 * @param amount the amount
	 * @param username the username
	 */
	public TradingPostShopEarning(long amount, String username) {
		this.setAmount(amount);
		this.setUsername(username);
	}

	/**
	 * Gets the amount
	 *
	 * @return the amount
	 */
	public long getAmount() {
		return amount;
	}

	/**
	 * Sets the amount
	 * 
	 * @param amount the amount
	 */
	public void setAmount(long amount) {
		this.amount = amount;
	}

	/**
	 * Gets the username
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username
	 * 
	 * @param username the username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
}
