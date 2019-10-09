package com.zionscape.server.model;

public final class DynamicMapObject extends MapObject {

	public DynamicMapObject(int id, Location location, int type, int orientation) {
		super(id, location, type, orientation, EntityType.DYNAMIC_OBJECT);
	}

}