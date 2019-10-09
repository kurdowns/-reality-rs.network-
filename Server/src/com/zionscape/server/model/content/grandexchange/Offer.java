package com.zionscape.server.model.content.grandexchange;


public class Offer {

	private long createdAt;
	private int type;
	private int playerId;
	private int slot;
	private int itemId;
	private int quantity;
	private int price;
	private int received = 0;
	private boolean aborted;
	private int amountOffered;
	private int collectItemAmount;
	private int collectCoinAmount;
	private boolean requiresUpdate = true;

	public Offer(int type, int playerId, int slot, int itemId, int quantity, int price) {
		this.createdAt = System.currentTimeMillis();
		this.type = type;
		this.playerId = playerId;
		this.slot = slot;
		this.itemId = itemId;
		this.quantity = quantity;
		this.price = price;

		if (type == Type.BUY) {
			amountOffered = getTotalCost();
		} else if (type == Type.SELL) {
			amountOffered = quantity;
		}

	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public int getTotalCost() {
		return quantity * price;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getReceived() {
		return received;
	}

	public void setReceived(int received) {
		this.received = received;
	}

	public boolean isAborted() {
		return aborted;
	}

	public void setAborted(boolean aborted) {
		this.aborted = aborted;
	}

	public int getCollectItemAmount() {
		return collectItemAmount;
	}

	public void setCollectItemAmount(int collectItemAmount) {
		this.collectItemAmount = collectItemAmount;
	}

	public int getCollectCoinAmount() {
		return collectCoinAmount;
	}

	public void setCollectCoinAmount(int collectCoinAmount) {
		this.collectCoinAmount = collectCoinAmount;
	}

	public boolean isComplete() {
		return aborted || getReceived() == getQuantity();
	}

	public int getAmountOffered() {
		return amountOffered;
	}

	public void setAmountOffered(int amountOffered) {
		this.amountOffered = amountOffered;
	}

	public boolean requiresUpdate() {
		return requiresUpdate;
	}

	public void setRequiresUpdate(boolean requiresUpdate) {
		this.requiresUpdate = requiresUpdate;
	}
}

