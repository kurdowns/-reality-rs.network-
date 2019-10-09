package com.zionscape.server.model.players.skills.mining;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;

/**
 * @author stuart
 */
public enum PickAxe {

	BRONZE(1265, 1, 1, 625, 12), // bronze
	IRON(1267, 1, 2, 626, 12), // iron
	STEEL(1269, 6, 3, 627, 10), // steel
	MITHRIL(1273, 21, 4, 629, 8), // mithril
	ADAMANT(1271, 31, 5, 628, 6), // adamant
	RUNE(1275, 41, 6, 624, 5), // rune
	DRAGON(15259, 61, 8, 12188, 5), // dragon
	INFERNO_ADZE(13661, 41, 8, 10249, 5); // inferno adze

	private final int id;
	private final int level;
	private final int bonus;
	private final int animation;
	private final int animationCycles;

	PickAxe(int id, int level, int bonus, int animation, int animationCycles) {
		this.id = id;
		this.level = level;
		this.bonus = bonus;
		this.animation = animation;
		this.animationCycles = animationCycles;
	}

	/**
	 * Get the best axe for the player if he has one
	 *
	 * @param player the player
	 * @return the axe instance
	 */
	public static PickAxe get(Player player) {
		int miningLevel = player.getPA().getActualLevel(PlayerConstants.MINING);
		PickAxe axe = null;
		PickAxe tempAxe = get(player.equipment[PlayerConstants.WEAPON]);
		if (tempAxe != null && miningLevel >= tempAxe.getLevel()) {
			axe = tempAxe;
		}
		for (int itemId : player.inventory) {
			if (itemId <= 0) {
				continue;
			}
			tempAxe = get(itemId - 1);
			if (tempAxe != null && miningLevel >= tempAxe.getLevel()) {
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
	private static PickAxe get(int id) {
		for (PickAxe axe : PickAxe.values()) {
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

	public int getAnimationCycles() {
		return animationCycles;
	}

}