package com.zionscape.server.model.players.skills.woodcutting;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;

public enum Axe {

	BRONZE(1351, 1, 1, 879),
	IRON(1349, 1, 2, 877),
	STEEL(1353, 6, 3, 875),
	MITHRIL(1355, 21, 4, 871),
	ADAMANT(1357, 31, 5, 869),
	RUNE(1359, 41, 6, 867),
	DRAGON(6739, 61, 8, 2846),
	INFERNO(13661, 61, 8, 10251);

	private final int id;
	private final int level;
	private final int bonus;
	private final int animation;

	Axe(int id, int level, int bonus, int animation) {
		this.id = id;
		this.level = level;
		this.bonus = bonus;
		this.animation = animation;
	}

	/**
	 * Get the best axe for the player if he has one
	 *
	 * @param player the player
	 * @return the axe instance
	 */
	public static Axe get(Player player) {
		int woodcuttingLevel = player.getPA().getActualLevel(PlayerConstants.WOODCUTTING);
		Axe axe = null;
		Axe tempAxe = get(player.equipment[PlayerConstants.WEAPON]);
		if (tempAxe != null && woodcuttingLevel >= tempAxe.getLevel()) {
			axe = tempAxe;
		}
		for (int itemId : player.inventory) {
			if (itemId <= 0) {
				continue;
			}
			tempAxe = get(itemId - 1);
			if (tempAxe != null && woodcuttingLevel >= tempAxe.getLevel()) {
				if (axe == null || tempAxe.getLevel() >= axe.getLevel()) {
					axe = tempAxe;
				}
			}
		}
		return axe;
	}

	/**
	 * Get an Axe by its id
	 *
	 * @param id The id of the axe
	 * @return the axe instance
	 */
	private static Axe get(int id) {
		for (Axe axe : Axe.values()) {
			if (id == axe.getId()) {
				return axe;
			}
		}
		return null;
	}

	public int getId() {
		return id;
	}

	public int getLevel() {
		return level;
	}

	public int getBonus() {
		return bonus;
	}

	public int getAnimation() {
		return animation;
	}


}
