package com.zionscape.server.model.players.skills.summoning;

import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.npcs.NPC;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class PlayerFamiliar {

	private Familiar type;
	private NPC npc;
	private int ticksLeft;
	private boolean calledByPlayer = false;
	private long lastCalled;
	private boolean beingRespawned;
	private List<Item> inventoryItems = new ArrayList<>();
	private int energyDrainTicks;
	private int specialMovesEnergy = 60;
	private int specialBarRestoreTicks = 0;
	private int cachedAmountOfScrolls = 0;
	private FamiliarSpecial special = null;
	private long lastSpecial;
	private boolean canAttack = true;

	private int npcId;

	public PlayerFamiliar(int npcId) {
		this.npcId = npcId;
	}

	public PlayerFamiliar(NPC npc, Familiar type) {
		this.npc = npc;
		this.type = type;
		this.ticksLeft = type.getMinutes() * 100;
	}

	public void decrementTicksLeft() {
		ticksLeft--;
	}

	public int getCachedAmountOfScrolls() {
		return cachedAmountOfScrolls;
	}

	public void setCachedAmountOfScrolls(int cachedAmountOfScrolls) {
		this.cachedAmountOfScrolls = cachedAmountOfScrolls;
	}

	public int getEnergyDrainTicks() {
		return energyDrainTicks;
	}

	public void setEnergyDrainTicks(int energyDrainTicks) {
		this.energyDrainTicks = energyDrainTicks;
	}

	public List<Item> getInventoryItems() {
		return inventoryItems;
	}

	public void setInventoryItems(List<Item> inventoryItems) {
		this.inventoryItems = inventoryItems;
	}

	public long getLastCalled() {
		return lastCalled;
	}

	public void setLastCalled(long lastCalled) {
		this.lastCalled = lastCalled;
	}

	public long getLastSpecial() {
		return lastSpecial;
	}

	public void setLastSpecial(long lastSpecial) {
		this.lastSpecial = lastSpecial;
	}

	public NPC getNpc() {
		return npc;
	}

	public void setNpc(NPC npc) {
		this.npc = npc;
	}

	public int getNpcId() {
		return npcId;
	}

	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	public FamiliarSpecial getSpecial() {
		return special;
	}

	public void setSpecial(FamiliarSpecial special) {
		this.special = special;
	}

	public int getSpecialBarRestoreTicks() {
		return specialBarRestoreTicks;
	}

	public void setSpecialBarRestoreTicks(int specialBarRestoreTicks) {
		this.specialBarRestoreTicks = specialBarRestoreTicks;
	}

	public int getSpecialMovesEnergy() {
		return specialMovesEnergy;
	}

	public void setSpecialMovesEnergy(int specialMovesEnergy) {
		this.specialMovesEnergy = specialMovesEnergy;
	}

	public int getTicksLeft() {
		return ticksLeft;
	}

	public void setTicksLeft(int ticksLeft) {
		this.ticksLeft = ticksLeft;
	}

	public Familiar getType() {
		return type;
	}

	public void setType(Familiar type) {
		this.type = type;
	}

	public boolean isBeingRespawned() {
		return beingRespawned;
	}

	public void setBeingRespawned(boolean beingRespawned) {
		this.beingRespawned = beingRespawned;
	}

	public boolean isCalledByPlayer() {
		return calledByPlayer;
	}

	public void setCalledByPlayer(boolean calledByPlayer) {
		this.calledByPlayer = calledByPlayer;
	}

}