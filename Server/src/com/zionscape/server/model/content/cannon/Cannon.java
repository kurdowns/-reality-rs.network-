package com.zionscape.server.model.content.cannon;

import com.zionscape.server.model.objects.GameObject;

import java.util.ArrayList;
import java.util.List;

public final class Cannon {

	private int rotation = 1;
	private GameObject object;
	private int objectId;
	private int balls;
	private boolean firing;
	private List<Integer> firedAt = new ArrayList<>();

	public Cannon(GameObject object) {
		this.object = object;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public GameObject getObject() {
		return object;
	}

	public void setObject(GameObject object) {
		this.object = object;
	}

	public int getBalls() {
		return balls;
	}

	public void setBalls(int balls) {
		this.balls = balls;
	}

	public boolean isFiring() {
		return firing;
	}

	public void setFiring(boolean firing) {
		this.firing = firing;
	}

	public List<Integer> getFiredAt() {
		return firedAt;
	}

	public int getObjectId() {
		return objectId;
	}

	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}

}