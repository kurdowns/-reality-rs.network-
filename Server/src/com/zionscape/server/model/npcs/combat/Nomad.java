package com.zionscape.server.model.npcs.combat;

import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCCombat;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.util.Misc;

public class Nomad extends NPCCombat {

	private boolean magicBurst;
	private boolean fastAttack;

	public Nomad(NPC npc) {
		super(npc);
	}

	@Override
	public void attackPlayer(Player player) {


		if (player.getTeleportLocation() != null || !player.getLocation().isWithinDistance(getNpc().getLocation())) {
			getNpc().underAttack = false;
			getNpc().underAttackBy = 0;
			return;
		}

		if (magicBurst) {
			magicBurst = false;
			return;
		}

		// eating under 75% health
		if (!fastAttack && getNpc().getHP() <= (getNpc().getDefinition().getHealth() * 0.75) && Misc.random(14) == 0) {
			getNpc().startAnimation(12700);
			getNpc().incrementHP(25);
			getNpc().setSkipCombatTurn(true);
			return;
		}

		// fast attacking under 25 hp
		if (!fastAttack && getNpc().getHP() <= (getNpc().getDefinition().getHealth() * 0.25)) {
			fastAttack = true;
		}

		if (!fastAttack && (Misc.random(6) == 0 || getNpc().getLocation().getDistance(player.getLocation()) >= 2)) {
			magicBurst = true;
			getNpc().startAnimation(12699);
			//getNpc().setSkipCombatTurn(true);
			getNpc().hitDelayTimer += 8;
			getNpc().attackTimer += 6;
			getNpc().attackType = 2;
			return;
		}

		getNpc().attackType = 0;
	}

	@Override
	public int getDamage() {
		if (fastAttack) {
			return 25;
		}

		return magicBurst ? 30 : 20;
	}

	@Override
	public int getAttackAnimation() {
		if (fastAttack) {
			return 12696;
		}
		if (magicBurst) {
			return 12697;
		}
		return 12693;
	}

	@Override
	public int getAttackDelay() {
		if (fastAttack) {
			return 1;
		}
		return 4;
	}

	@Override
	public int getHitDelay() {
		return super.getHitDelay();
	}

	@Override
	public boolean overrideProtectionPrayers() {
		return true;
	}

}
