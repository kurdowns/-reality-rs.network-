package com.zionscape.server.model.npcs;

import com.zionscape.server.model.Location;

public class NPCSpawn {

	private int type;
	private Location location;
	private boolean randomWalk = false;
	private String comment;

	private NPCSpawn() {

	}

	public NPCSpawn(int type, Location location) {
		this.type = type;
		this.location = location;
	}

	public NPCSpawn(int type, Location location, boolean randomWalk) {
		this.type = type;
		this.location = location;
		this.randomWalk = randomWalk;
	}

	public NPCSpawn(int type, Location location, boolean randomWalk, String comment) {
		this.type = type;
		this.location = location;
		this.randomWalk = randomWalk;
		this.comment = comment;
	}

	public int getType() {
		return type;
	}

	public Location getLocation() {
		return location;
	}

	public boolean isRandomWalk() {
		return randomWalk;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}