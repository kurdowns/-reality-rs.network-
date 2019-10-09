package com.zionscape.server.world.doors;

public class DoubleDoor {

	private final Door firstDoor;
	private final Door secondDoor;

	public DoubleDoor(Door firstDoor, Door secondDoor) {
		this.firstDoor = firstDoor;
		this.secondDoor = secondDoor;
	}

	public Door getFirstDoor() {
		return firstDoor;
	}

	public Door getSecondDoor() {
		return secondDoor;
	}

}
