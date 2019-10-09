package com.zionscape.server.model.players.skills.hunter;

import com.zionscape.server.model.Location;

public class Trap {

	private final long placedAt;
	private final int playerId;
	private final Location location;
	private final Type type;
	private int objectId;
	private boolean failed = false;
	private boolean captured = false;
	private int capturedType = -1;
	private int bait = -1;

	public Trap(int playerId, Location location, Type type, int objectId) {
		this.playerId = playerId;
		this.location = location;
		this.type = type;
		this.placedAt = System.currentTimeMillis();
		this.objectId = objectId;
	}

	public int getPlayerId() {
		return playerId;
	}

	public Location getLocation() {
		return location;
	}

	public Type getType() {
		return type;
	}

	public long getPlacedAt() {
		return placedAt;
	}

	public int getObjectId() {
		return objectId;
	}

	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}

	public boolean isFailed() {
		return failed;
	}

	public void setFailed(boolean failed) {
		this.failed = failed;
	}

	public boolean isCaptured() {
		return captured;
	}

	public void setCaptured(boolean captured) {
		this.captured = captured;
	}

	public int getCapturedType() {
		return capturedType;
	}

	public void setCapturedType(int capturedType) {
		this.capturedType = capturedType;
	}

	@Override
	public String toString() {
		return "Trap{" +
				"playerId=" + playerId +
				", objectId=" + objectId +
				", location=" + location +
				'}';
	}

	public int getBait() {
		return bait;
	}

	public void setBait(int bait) {
		this.bait = bait;
	}
}
