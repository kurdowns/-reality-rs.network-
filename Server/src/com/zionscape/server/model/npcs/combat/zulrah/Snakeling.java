package com.zionscape.server.model.npcs.combat.zulrah;

import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCCombat;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.util.Misc;

public class Snakeling extends NPCCombat {

	private int attackStyle = -1;

	public Snakeling(NPC npc) {
		super(npc);
	}

	@Override
	public void attackPlayer(Player player) {
		if (attackStyle == -1) {
			attackStyle = Misc.random(1, 2);
		}
		getNpc().attackType = attackStyle;

		if (getNpc().attackType == 1) {
			getNpc().projectileId = 1044;
		} else {
			getNpc().projectileId = 1046;
		}
	}

	@Override
	public void onDeath() {
		super.onDeath();

		NPC npc = (NPC) getNpc().getAttribute("zulrah");
		if (npc == null) {
			return;
		}

		Zulrah zulrah = (Zulrah) npc.getCombat();
		zulrah.removeSnake(getNpc());
	}

	@Override
	public int damageDealtByPlayer(Player player, int damage) {
		return super.damageDealtByPlayer(player, damage);
	}

	@Override
	public int getDamage() {
		return 15;
	}

	@Override
	public int getDistanceRequired() {
		return super.getDistanceRequired();
	}


	@Override
	public int getAttackDelay() {
		return super.getAttackDelay();
	}

	@Override
	public int getAttackAnimation() {
		return 2407;
	}

	@Override
	public boolean overrideProtectionPrayers() {
		return false;
	}

}