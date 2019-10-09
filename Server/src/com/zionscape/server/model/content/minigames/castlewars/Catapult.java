package com.zionscape.server.model.content.minigames.castlewars;

import com.zionscape.server.model.players.Player;

public class Catapult {

	private int fireCooldown = 0;
	private boolean onFire = false;
	private int burningTicks = Constants.CATAPULT_ON_FIRE_DURATION;
	private int targetX;
	private int targetY;
	private Player controller;

	public int getTargetX() {
		return targetX;
	}

	public void setTargetX(int targetX) {
		this.targetX = targetX;
	}

	public int getTargetY() {
		return targetY;
	}

	public void setTargetY(int targetY) {
		this.targetY = targetY;
	}

	public void offsetTarget(int x, int y) {
		targetX += x;
		targetY += y;
	}

	public void incrementX() {
		targetX += 3;
	}

	public void decrementX() {
		targetX -= 3;
	}

	public void incrementY() {
		targetY += 3;
	}

	public void decrementY() {
		targetY -= 3;
	}

	public Player getController() {
		return controller;
	}

	public void setController(Player controller) {
		this.controller = controller;
	}

	public int getBurningTicks() {
		return burningTicks;
	}

	public void setBurningTicks(int burningTicks) {
		this.burningTicks = burningTicks;
	}

	public boolean isOnFire() {
		return onFire;
	}

	public void setOnFire(boolean onFire) {
		this.onFire = onFire;
	}

	public int getFireCooldown() {
		return fireCooldown;
	}

	public void setFireCooldown(int fireCooldown) {
		this.fireCooldown = fireCooldown;
	}
}