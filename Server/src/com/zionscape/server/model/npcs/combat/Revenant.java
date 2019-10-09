package com.zionscape.server.model.npcs.combat;

import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCCombat;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.util.Misc;

public class Revenant extends NPCCombat {

	private long lastCombatSwitch = 0;

	public Revenant(NPC npc) {
		super(npc);
	}

	@Override
	public void attackPlayer(Player player) {

		// healing
		// eating under 75% health
		if (getNpc().getHP() <= (getNpc().maxHP * 0.75) && Misc.random(20) == 0) {
			getNpc().setHP((int) (getNpc().maxHP * 0.10) + getNpc().getHP());
			getNpc().setSkipCombatTurn(true);
			player.sendMessage("The revenant heals itself!");
			return;
		}

		// attack with the best attack type
		if (System.currentTimeMillis() - lastCombatSwitch > 3 * 1000) {

			// check for active protection prayers
			boolean[] protectionPrayers = new boolean[3];

			if (player.prayerActive[18] || player.curseActive[9]) {
				protectionPrayers[0] = true;
			}

			if (player.prayerActive[17] || player.curseActive[8]) {
				protectionPrayers[1] = true;
			}

			if (player.prayerActive[16] || player.curseActive[7]) {
				protectionPrayers[2] = true;
			}

			// get player bonuses
			int[] playerDefenceBonuses = new int[3];

			playerDefenceBonuses[0] = player.getCombat().bestMeleeDef();
			playerDefenceBonuses[1] = player.playerBonus[9];
			playerDefenceBonuses[2] = player.playerBonus[10];

			int bonus = 0;
			int worstAttackType = 0;
			for (int i = 0; i < 3; i++) {
				if (protectionPrayers[i]) {
					continue;
				}
				if (playerDefenceBonuses[i] < bonus) {
					worstAttackType = i;
					bonus = playerDefenceBonuses[i];
				}
			}

			if (getNpc().attackType != worstAttackType) {
				getNpc().attackType = worstAttackType;
				lastCombatSwitch = System.currentTimeMillis();
			}
		}

	}

}