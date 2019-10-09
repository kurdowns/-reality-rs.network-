package com.zionscape.server.model.npcs.combat;

import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCCombat;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.util.Misc;

public class Glacor extends NPCCombat {

	private boolean doingSpecial = false;

	public Glacor(NPC npc) {
		super(npc);
	}

	@Override
	public void attackPlayer(Player player) {
		int distance = (int) player.getLocation().getDistance(getNpc().getLocation());

		// special
		if (!doingSpecial && Misc.random(6) == 0) {
			doingSpecial = true;

			if (getNpc().attackType <= 1) {
				getNpc().attackType = 2;
			} else {
				getNpc().attackType = 1;
			}
			return;
		}

		if (distance <= 2) {
			getNpc().attackType = 0;
			getNpc().projectileId = 0;
		} else {
			if (Misc.random(10) < 5) {
				getNpc().attackType = 1;
				getNpc().projectileId = 2314;
			} else {
				getNpc().attackType = 2;
				getNpc().projectileId = 2315;
				getNpc().endGfx = 899;
			}
		}

	}

	@Override
	public int getDamage() {

		if (doingSpecial) {
			doingSpecial = false;
			return 30;
		}

		if (getNpc().attackType == 0) {
			return 30;
		}

		return super.getDamage();
	}

	@Override
	public int getAttackAnimation() {
		if (doingSpecial) {
			return 9957;
		}
		if (getNpc().attackType == 0) {
			return 9955;
		}
		if (getNpc().attackType == 1) {
			return 9950;
		}
		if (getNpc().attackType == 2) {
			return 9951;
		}
		return -1;
	}

	@Override
	public boolean overrideProtectionPrayers() {
		return true;
	}
}
