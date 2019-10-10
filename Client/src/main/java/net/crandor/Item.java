package net.crandor;

final class Item extends Entity {

	@Override
	public final Model getRotatedModel() {
		ItemDefinition itemDef = ItemDefinition.forID(ID);
		return itemDef.getInventoryModel(itemCount);
	}

	public int ID;
	public int x;
	public int y;
	public int itemCount;

}
