package com.zionscape.server.model.players.skills.summoning;

import com.zionscape.server.model.Entity;
import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.players.Player;

public abstract class FamiliarSpecial {

	private final Player player;

	public FamiliarSpecial(Player player) {
		this.player = player;
	}

	public abstract boolean execute();

	public boolean onItemOnItem(Item item, int slot, Item otherItem, int otherSlot) {
		return false;
	}

	public void onFamiliarDamageGiven(Entity target) {
		getNPC().doSpecial = false;
	}

	public void onFamiliarAttack(Entity target) {

	}

	public Player getPlayer() {
		return player;
	}

	public NPC getNPC() {
		return player.getFamiliar().getNpc();
	}

}