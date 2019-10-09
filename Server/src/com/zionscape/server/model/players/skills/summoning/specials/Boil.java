package com.zionscape.server.model.players.skills.summoning.specials;

import com.zionscape.server.model.Entity;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.skills.summoning.FamiliarSpecial;
import com.zionscape.server.model.players.skills.summoning.Summoning;

public class Boil extends FamiliarSpecial {

	private boolean resetAttackType = false;

	public Boil(Player player) {
		super(player);
	}

	@Override
	public void onFamiliarAttack(Entity target) {

		getNPC().projectileId = 1376;

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
		getNPC().startAnimation(7883);
		getNPC().gfx100(1375);

		getNPC().doSpecial = true;
		getNPC().secondHit = true;
		return true;
	}

	@Override
	public void onFamiliarDamageGiven(Entity target) {

		if (resetAttackType) {
			getNPC().projectileId = 0;
			getNPC().attackType = 0;

			resetAttackType = false;
			super.onFamiliarDamageGiven(target);
			return;
		}

		int damage = 0;
		int armour = 0;

		if (target instanceof Player) {
			Player plr = (Player) target;
			armour = plr.getCombat().bestMeleeDef();
		}

		if (target instanceof NPC) {
			armour = ((NPC) target).defence;
		}

		if (armour > 10) {
			damage = armour / 10;
		}

		if (damage < 1) {
			damage = 1;
		}

		if (damage > 30) {
			damage = 30;
		}

		getNPC().attackType = 2;
		getNPC().overrideDamage = damage;

		resetAttackType = true;
	}

}