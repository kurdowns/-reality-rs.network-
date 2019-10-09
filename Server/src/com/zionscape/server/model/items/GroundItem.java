package com.zionscape.server.model.items;

import com.zionscape.server.model.Location;

public class GroundItem {

	private final Location location;
	public int itemId;
	public int itemAmount;
	public int itemController;
	public int hideTicks;
	public int removeTicks;
	public String ownerName;
	public boolean playerDrop = false;
	public boolean pkLoot = false;
	private boolean respawns;
	private int respawnTimer = -1;

	public GroundItem(int id, int x, int y, int z, int amount, int controller, int hideTicks, String name, boolean playerDrop) {
		this.itemId = id;
		this.location = Location.create(x, y, z);
		this.itemAmount = amount;
		this.itemController = controller;
		this.hideTicks = hideTicks;
		this.ownerName = name;
		this.playerDrop = playerDrop;
	}

	public GroundItem(int id, int amount, int x, int y, int z, boolean respawns) {
		this.itemId = id;
		this.location = Location.create(x, y, z);
		this.itemAmount = amount;
		this.itemController = -1;
		this.ownerName = "";
		this.respawns = respawns;
	}

	public int getItemId() {
		return this.itemId;
	}

	public Location getLocation() {
		return location;
	}

	public int getItemAmount() {
		return this.itemAmount;
	}

	public int getItemController() {
		return this.itemController;
	}

	public String getName() {
		return this.ownerName;
	}

	public int getRespawnTimer() {
		return respawnTimer;
	}

	public void setRespawnTimer(int respawnTimer) {
		this.respawnTimer = respawnTimer;
	}

	public boolean respawns() {
		return respawns;
	}

	public void setRespawns(boolean respawns) {
		this.respawns = respawns;
	}
}