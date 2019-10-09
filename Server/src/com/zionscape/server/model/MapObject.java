package com.zionscape.server.model;

import com.google.common.base.MoreObjects;

public class MapObject extends Entity {

	private final int id;
	private final Location location;
	private final int type;
	private final int orientation;
	private final EntityType entityType;

	public MapObject(int id, Location location, int type, int orientation, EntityType entityType) {
		this.id = id;
		this.location = location;
		this.type = type;
		this.orientation = orientation;
		this.entityType = entityType;
	}

	public int getId() {
		return id;
	}

	public int getType() {
		return type;
	}

	public int getOrientation() {
		return orientation;
	}

	@Override
	public boolean isDead() {
		return false;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public EntityType getEntityType() {
		return entityType;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(StaticMapObject.class).add("id", id).add("location", location).add("type", type).add("orientation", orientation).toString();
	}

}
