package com.zionscape.server.world.shops;

import com.zionscape.server.model.items.Item;

public class ShopItem {

	private final Item item;
	private final int maxStock;
	private final int price;
	private final boolean playerItem;
	private long lastReplenished = System.currentTimeMillis();

	public ShopItem(Item item, int maxStock, int price) {
		this.item = item;
		this.maxStock = maxStock;
		this.playerItem = false;
		this.price = price;
	}

	public ShopItem(Item item, int maxStock, int price, boolean playerItem) {
		this.item = item;
		this.maxStock = maxStock;
		this.playerItem = playerItem;
		this.price = price;
	}

	public Item getItem() {
		return item;
	}

	public int getMaxStock() {
		return maxStock;
	}

	public int getId() {
		return item.getId();
	}

	public int getAmount() {
		return item.getAmount();
	}

	public boolean hasInfinityStock() {
		return maxStock == -2;
	}

	public boolean isPlayerItem() {
		return playerItem;
	}

	public long getLastReplenished() {
		return lastReplenished;
	}

	public void setLastReplenished(long lastReplenished) {
		this.lastReplenished = lastReplenished;
	}

	public int getPrice() {
		return price;
	}

	public static final class GeneralStoreShopItem extends ShopItem {
		private long placedAt;

		public GeneralStoreShopItem(Item item, long placedAt) {
			super(item, -1, -1, true);
			this.placedAt = placedAt;
		}

		public long getPlacedAt() {
			return placedAt;
		}

		public void setPlacedAt(long placedAt) {
			this.placedAt = placedAt;
		}

	}

}