package com.zionscape.server.model;

import java.util.HashMap;
import java.util.Map;

public abstract class Entity {

	private Location lastKnownRegion = Location.create(0, 0, 0);
	private Location location = Location.create(0, 0, 0);
	public int absX = 0, absY = 0, heightLevel = 0;

	private final Sprites sprites = new Sprites();
	private final Map<String, Object> attributes = new HashMap<>();
	public boolean validateWalkingQueue = true;
	public int javelinDamage;
	public boolean doJavelinSpecial = false;
	private boolean eventHandled = false;

	public abstract boolean isDead();

	public abstract EntityType getEntityType();

	public void setAttribute(String key, Object object) {
		attributes.put(key, object);
	}

	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	public void removeAttribute(String key) {
		attributes.remove(key);
	}

	public boolean attributeExists(String key) {
		return attributes.containsKey(key);
	}

	public boolean isEventHandled() {
		return eventHandled;
	}

	public void setEventHandled(boolean eventHandled) {
		this.eventHandled = eventHandled;
	}

	public enum EntityType {
		STATIC_OBJECT,
		DYNAMIC_OBJECT,
		PLAYER,
		NPC
	}

	public Sprites getSprites() {
		return sprites;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;

		absX = location.getX();
		absY = location.getY();
		heightLevel = location.getZ();
	}

	public Location getLastKnownRegion() {
		return lastKnownRegion;
	}

	public void setLastKnownRegion(Location lastKnownRegion) {
		this.lastKnownRegion = lastKnownRegion;
	}

	public int getX() {
		return location.getX();
	}

	public int getY() {
		return location.getY();
	}

}