package com.zionscape.server.model;

public final class StaticMapObject extends MapObject {

	public StaticMapObject(int id, Location location, int type, int orientation) {
		super(id, location, type, orientation, EntityType.STATIC_OBJECT);
	}

}