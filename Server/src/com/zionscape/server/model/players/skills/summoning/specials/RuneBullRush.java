package com.zionscape.server.model.players.skills.summoning.specials;

import com.zionscape.server.model.Entity;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.skills.summoning.FamiliarSpecial;
import com.zionscape.server.model.players.skills.summoning.Summoning;
import com.zionscape.server.util.Misc;

public class RuneBullRush extends FamiliarSpecial {

	private boolean resetAttackType = false;

	public RuneBullRush(Player player) {
		super(player);
	}

	@Override
	public boolean execute() {

		// player must be in combat
		if (getPlayer().playerIndex == 0 && getPlayer().npcIndex == 0) {
			getPlayer().sendMessage("You must be in combat to use this special.");
			return false;
		}

		if (!getNPC().underAttack) {
			Summoning.familiarStartCombat(getPlayer());
		}

		// player animation and gfx
		getPlayer().gfx100(1316);
		getPlayer().startAnimation(7660);

		// npc animation and gfx
		getNPC().startAnimation(8026);
		getNPC().gfx0(1496);

		getNPC().doSpecial = true;
		getNPC().secondHit = true;
		return true;
	}

	@Override
	public void onFamiliarDamageGiven(Entity target) {

		if (resetAttackType) {
			getNPC().attackType = 0;
			resetAttackType = false;
			super.onFamiliarDamageGiven(target);
			return;
		}

		getNPC().attackType = 2;
		getNPC().overrideDamage = Misc.random(1, 19);

		resetAttackType = true;
	}

}
