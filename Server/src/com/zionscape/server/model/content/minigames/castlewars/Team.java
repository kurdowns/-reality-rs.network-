package com.zionscape.server.model.content.minigames.castlewars;

import com.zionscape.server.model.players.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Team {

	private final List<Player> players = new ArrayList<>();
	private int score;
	private FlagStatus flagStatus = FlagStatus.SAFE;
	private int doorHealth = 100;
	private boolean sidedoorLocked = true;
	private boolean rock1Collapsed = true;
	private boolean rock2Collapsed = true;
	private boolean catapultOperational = true;
	private boolean updateInterfaceConfig = true;
	private int interfaceConfig;
	private List<Barricade> barricades = new ArrayList<>();
	private Map<Player, Long> respawnAreaTimes = new HashMap<>();
	private Player flagTaker;
	private Catapult catapult = new Catapult();

	public List<Barricade> getBarricades() {
		return barricades;
	}

	public void setBarricades(List<Barricade> barricades) {
		this.barricades = barricades;
	}

	public Catapult getCatapult() {
		return catapult;
	}

	public int getDoorHealth() {
		return doorHealth;
	}

	public void setDoorHealth(int doorHealth) {
		this.doorHealth = doorHealth;
		updateInterfaceConfig = true;
	}

	public FlagStatus getFlagStatus() {
		return flagStatus;
	}

	public void setFlagStatus(FlagStatus flagStatus) {
		this.flagStatus = flagStatus;
		updateInterfaceConfig = true;
	}

	public Player getFlagTaker() {
		return flagTaker;
	}

	public void setFlagTaker(Player flagTaker) {
		this.flagTaker = flagTaker;
	}

	public int getInterfaceConfig(Team other) {
		//if (updateInterfaceConfig) {
		interfaceConfig = 0;
		interfaceConfig += other.getDoorHealth(); // health 0 - 100
		interfaceConfig += other.isSidedoorLocked() ? 0 : 128; // door locked(128) or unlocked(0)
		interfaceConfig += other.isRock1Collapsed() ? 0 : 256; // rock 1 cleared(256) or collapsed(0)
		interfaceConfig += other.isRock2Collapsed() ? 0 : 512; // rock 2 cleared(512) or collapsed(0)
		interfaceConfig += other.isCatapultOperational() ? 0 : 1024; // catapult destroyed(1024) or operational(0)
		interfaceConfig += getFlagStatus().getStatus() * 2097152;
		interfaceConfig += getScore() * 16777216;

		//    updateInterfaceConfig = false;
		//}

		return interfaceConfig;
	}

	public void setInterfaceConfig(int interfaceConfig) {
		this.interfaceConfig = interfaceConfig;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public Map<Player, Long> getRespawnAreaTimes() {
		return respawnAreaTimes;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
		updateInterfaceConfig = true;
	}

	public boolean isCatapultOperational() {
		return catapultOperational;
	}

	public void setCatapultOperational(boolean catapultOperational) {
		this.catapultOperational = catapultOperational;
		updateInterfaceConfig = true;
	}

	public boolean isRock1Collapsed() {
		return rock1Collapsed;
	}

	public void setRock1Collapsed(boolean rock1Collapsed) {
		this.rock1Collapsed = rock1Collapsed;
		updateInterfaceConfig = true;
	}

	public boolean isRock2Collapsed() {
		return rock2Collapsed;
	}

	public void setRock2Collapsed(boolean rock2Collapsed) {
		this.rock2Collapsed = rock2Collapsed;
		updateInterfaceConfig = true;
	}

	public boolean isSidedoorLocked() {
		return sidedoorLocked;
	}

	public void setSidedoorLocked(boolean sidedoorLocked) {
		this.sidedoorLocked = sidedoorLocked;
		updateInterfaceConfig = true;
	}

	public void setUpdateInterfaceConfig(boolean updateInterfaceConfig) {
		this.updateInterfaceConfig = updateInterfaceConfig;
	}

}