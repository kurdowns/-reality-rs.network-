package com.zionscape.server.model.content;

import com.zionscape.server.model.players.Player;

/**
 * @author Scott Perretta
 */
public class GreeGree {

	private static final int TRANSFORM_GFX = 359;

	private static final int[][] GREE_GREE_DATA = {
			{4031, 132}, {4029, 1465}, {4027, 1461}, {4026, 1460}, {4024, 1456}
	};

	public static boolean isGreeGree(int item) {
		for (int i = 0; i < GREE_GREE_DATA.length; i++) {
			if (item == GREE_GREE_DATA[i][0]) {
				return true;
			}
		}
		return false;
	}

	public static boolean isGreeGree(Player player) {
		for (int i = 0; i < GREE_GREE_DATA.length; i++) {
			if (player.equipment[player.playerWeapon] == GREE_GREE_DATA[i][0]) {
				return true;
			}
		}
		return false;
	}

	public static void transform(Player player) {
		if (player.equipment[Player.playerWeapon] < 4024 || player.equipment[Player.playerWeapon] > 4031) {
			if (player.isNpc) {
				player.isNpc = false;
				player.npcId2 = -1;
				player.gfx0(TRANSFORM_GFX);
				player.updateRequired = true;
				player.appearanceUpdateRequired = true;
				return;
			}
			return;
		}
		if (player.inWild) {
			player.sendMessage("You cannot transform whilst in the wild!");
			return;
		}
		for (int i = 0; i < GREE_GREE_DATA.length; i++) {
			if (GREE_GREE_DATA[i][0] == player.equipment[Player.playerWeapon]) {
				player.isNpc = true;
				player.npcId2 = GREE_GREE_DATA[i][1];
				player.gfx0(TRANSFORM_GFX);
				player.updateRequired = true;
				player.appearanceUpdateRequired = true;
				return;
			}
		}
	}

}
