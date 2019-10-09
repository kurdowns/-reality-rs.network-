package com.zionscape.server.model.content.minigames;

import com.zionscape.server.model.players.Player;

/**
 * @author Scott Perretta
 */
public class MagicArena {

	/**
	 * Saradomin staff item id.
	 */
	public static final int SARADOMIN = 2415;

	/**
	 * Guthix staff item id.
	 */
	public static final int GUTHIX = 2416;

	/**
	 * Zamorak staff item id.
	 */
	public static final int ZAMORAK = 2417;

	/**
	 * Checks to see what staff you can buy from the staff shop.
	 */
	public static int getBuyableStaff(Player player) {
		for (int i = SARADOMIN; i <= ZAMORAK; i++) {
			if (player.equipment[player.playerCape] == i - 3) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Checks to see what staff you can buy from the staff shop.
	 */
	public static boolean canWieldStaff(Player player, int staff) {
		for (int i = SARADOMIN; i <= ZAMORAK; i++) {
			if (player.equipment[player.playerCape] == i - 3 && staff == i) {
				return true;
			}
		}
		player.sendMessage("You must wield the correct cape that corresponds with the staff.");
		return false;
	}

	//c.inMageArena()
	public static void onPlayerLoggedOut(Player player) {
		if (player.inMageArena()) {
			player.absX = 2539;
			player.absY = 4715;
			player.heightLevel = 0;
		}
	}

}
