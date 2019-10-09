package com.zionscape.server.model.items;

public class ItemList {

	public int itemId;
	public String itemName;
	public String itemDescription = "";
	public double ShopValue;
	public double LowAlch;
	public double HighAlch;
	public int[] Bonuses = new int[12];
	public int targetSlot = 3;

	public ItemList(int _itemId) {
		itemId = _itemId;
	}
}
