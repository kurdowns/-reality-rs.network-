package com.zionscape.server.model.npcs.combat;

import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCCombat;
import com.zionscape.server.model.players.Player;

/**
 * @author Scott Perretta .
 */
public class Strykewyrm extends NPCCombat {

	//NPC ID, GFX, MAX HIT,
	private final static int[][] WYRM_DATA = {
			//{9462, 2704}, {9464, 2730}, {9466, 2682} old code, jungle one was bugged
			{9462, 2704}, {9464, 2730}, {9466, 2730}
	};

	public Strykewyrm(NPC npc) {
		super(npc);
	}

	@Override
	public void attackPlayer(Player player) {
		for (int i = 0; i < WYRM_DATA.length; i++) {
			if (getNpc().type == WYRM_DATA[i][0]) {
				getNpc().attackType = 2;
				getNpc().projectileId = WYRM_DATA[i][1];
			}
		}
	}

	@Override
	public int getAttackAnimation() {
		return 12791;
	}

	@Override
	public boolean overrideProtectionPrayers() {
		return false;
	}
}
