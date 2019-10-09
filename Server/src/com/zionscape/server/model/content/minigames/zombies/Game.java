package com.zionscape.server.model.content.minigames.zombies;

import com.google.common.collect.Lists;
import com.zionscape.server.gamecycle.GameCycleTask;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.objects.GameObject;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.util.pathfinding.CollisionMap;

import java.util.List;
import java.util.Optional;


public class Game {

	private final Type type;
	private final List<Player> players;
	private final int height;
	private boolean eastGateUnlocked;
	private final List<NPC> zombies = Lists.newArrayList();
	private final List<NPC> shopNpcs = Lists.newArrayList();
	private int wave = 1;
	private final CollisionMap collisionMap;
	private List<GameObject> gameObjects = Lists.newArrayList();
	private Optional<GameCycleTask> spawnTask = Optional.empty();

	private boolean instantKill = false;
	private boolean doublePoints = false;
	private boolean heal = false;
	private int perkTicks;

	public Game(Type type, List<Player> players, int height) {
		this.type = type;
		this.players = players;
		this.height = height;
		this.collisionMap = new CollisionMap(Location.create(4098, 3970), Location.create(4156, 4027), 0);
	}

	public List<Player> getPlayers() {
		return players;
	}

	public Type getType() {
		return type;
	}

	public int getActualHeight() {
		return height;
	}

	public int getHeight() {
		return height * 4;
	}

	public boolean isEastGateUnlocked() {
		return eastGateUnlocked;
	}

	public void setEastGateUnlocked(boolean eastGateUnlocked) {
		this.eastGateUnlocked = eastGateUnlocked;
	}

	public List<NPC> getZombies() {
		return zombies;
	}

	public int getWave() {
		return wave;
	}

	public void setWave(int wave) {
		this.wave = wave;
	}

	public CollisionMap getCollisionMap() {
		return collisionMap;
	}

	public List<GameObject> getGameObjects() {
		return gameObjects;
	}

	public Optional<GameCycleTask> getSpawnTask() {
		return spawnTask;
	}

	public void setSpawnTask(Optional<GameCycleTask> spawnTask) {
		this.spawnTask = spawnTask;
	}

	public List<NPC> getShopNpcs() {
		return shopNpcs;
	}

	public boolean instantKill() {
		return instantKill;
	}

	public void setInstantKill(boolean instantKill) {
		this.instantKill = instantKill;
	}

	public boolean doublePoints() {
		return doublePoints;
	}

	public void setDoublePoints(boolean doublePoints) {
		this.doublePoints = doublePoints;
	}

	public boolean heal() {
		return heal;
	}

	public void setHeal(boolean heal) {
		this.heal = heal;
	}

	public int getPerkTicks() {
		return perkTicks;
	}

	public void setPerkTicks(int perkTicks) {
		this.perkTicks = perkTicks;
	}

}