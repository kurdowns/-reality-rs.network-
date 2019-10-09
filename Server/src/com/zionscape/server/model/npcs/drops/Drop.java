package com.zionscape.server.model.npcs.drops;

import com.zionscape.server.util.Misc;

public final class Drop {

	private int itemId;
	private int[] itemIds;
	private int fixedAmount;
	private int minAmount;
	private int maxAmount;
	private Rate rate;

	protected Drop(int itemId) {
		this.itemId = itemId;
	}

	protected Drop(int[] itemIds) {
		this.itemIds = itemIds;
	}

	public int getItemId() {
		return itemId;
	}

	public Drop setAmount(int amount) {
		this.fixedAmount = amount;
		return this;
	}

	public Drop setAmount(int min, int max) {
		minAmount = min;
		maxAmount = max;
		return this;
	}

	public int getDropAmount() {
		return fixedAmount > 0 ? fixedAmount : Misc.random(minAmount, maxAmount);
	}

	public Rate getRate() {
		return rate;
	}

	public void setRate(Rate rate) {
		this.rate = rate;
	}

	public int[] getItemIds() {
		return itemIds;
	}

	public int getFixedAmount() {
		return fixedAmount;
	}

	public int getMinAmount() {
		return minAmount;
	}

	public int getMaxAmount() {
		return maxAmount;
	}


}