package com.zionscape.server.model.players.skills.summoning.specials;

import com.zionscape.server.model.Entity;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.skills.summoning.FamiliarSpecial;
import com.zionscape.server.model.players.skills.summoning.Summoning;

public class IronWithin extends FamiliarSpecial {

	private int hits = 0;

	public IronWithin(Player player) {
		super(player);
	}

	@Override
	public boolean execute() {

		// player must be in combat
		if (getPlayer().playerIndex == 0 && getPlayer().npcIndex == 0) {
			getPlayer().sendMessage("You must be in combat to use this special.");
			return false;
		}

		getPlayer().gfx100(1316);
		getPlayer().startAnimation(7660);

		final NPC npc = getPlayer().getFamiliar().getNpc();
		npc.gfx100(1540);

		npc.doSpecial = true;
		npc.secondHit = true;
		npc.attackTimer = 0;

		if (!npc.underAttack) {
			Summoning.familiarStartCombat(getPlayer());
		}

		return true;
	}

	@Override
	public void onFamiliarDamageGiven(Entity target) {
		hits++;

		if (hits >= 2) {
			hits = 0;
			getNPC().attackTimer = 0;

			super.onFamiliarDamageGiven(target);
		}
	}

}