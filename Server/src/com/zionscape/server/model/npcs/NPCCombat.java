package com.zionscape.server.model.npcs;

import com.zionscape.server.model.players.Player;

public abstract class NPCCombat {

	private NPC npc;

	public NPCCombat(NPC npc) {
		this.npc = npc;
	}

	public void attackPlayer(Player player) {

	}

	public void attackedByPlayer(Player player) {

	}

	public void onDeath() {

	}

	public void process() {

	}

	public void npcSpawned() {

	}

	public int getDamage() {
		return npc.getDefinition().getMaxDamage();
	}

	public int damageDealtByPlayer(Player player, int damage) {
		return damage;
	}

	public int getAttackAnimation() {
		return npc.getDefinition().getAttackAnimation();
	}

	public int getHitDelay() {
		return 2;
	}

	public int getAttackDelay() {
		return 5;
	}

	public NPC getNpc() {
		return npc;
	}

	public int getDistanceRequired() {
		return 1;
	}

	public boolean overrideProtectionPrayers() {
		return false;
	}

	@Override
	public String toString() {
		return String.format("index: %d id: %d", npc.id, npc.type);
	}

}