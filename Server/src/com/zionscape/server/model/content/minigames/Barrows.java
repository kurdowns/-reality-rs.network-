package com.zionscape.server.model.content.minigames;

import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.util.Misc;

public class Barrows {

	public static final int[][] COFFIN_AND_BROTHERS = {{6823, 2030}, {6772, 2029}, {6822, 2028}, {6773, 2027},
			{6771, 2026}, {6821, 2025}};
	public static final int[] BARROW_LOOT = {4708, 4710, 4712, 4714, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730,
			4732, 4734, 4736, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4749, 13028, 21736, 21744, 21752, 21760};
	public static final int[] NOTED_LOOT = {1080, 1114, 1148, 1164, 1202, 1214, 1276, 1304, 1320, 1334, 437};
	public static final int[] MISC_LOOT = {560, 565, 562, 4740, 995};
	private static final int BARROWS_LOOT_CHANCE = 11;// higher # = Lesser Chance
	private static int[] loot = new int[2];
	private static int[] lootN = new int[2];

	/**
	 * Picking the random coffin
	 */
	public static int getRandomCoffin() {
		return Misc.random(COFFIN_AND_BROTHERS.length - 1);
	}

	/**
	 * Selects the coffin and shows the interface if coffin id matches random coffin
	 */
	public static boolean selectCoffin(Player c, int coffinId) {
		if (c.randomCoffin == 0) {
			c.randomCoffin = Barrows.getRandomCoffin();
		}
		if (COFFIN_AND_BROTHERS[c.randomCoffin][0] == coffinId) {
			c.getDH().sendDialogues(1, -1);
			return true;
		}
		return false;
	}

	@SuppressWarnings("unused")
	public static void BarrowsLoot(Player c) {
		int slots = c.getItems().freeInventorySlots();
		int chance = Misc.random(BARROWS_LOOT_CHANCE);
		loot[0] = MISC_LOOT[Misc.random(MISC_LOOT.length - 1)];
		loot[1] = NOTED_LOOT[Misc.random(NOTED_LOOT.length - 1)];
		lootN[0] = Misc.random(1000);
		lootN[1] = Misc.random(3);
		if (chance == 0) {
			loot[0] = MISC_LOOT[Misc.random(MISC_LOOT.length - 1)];
			loot[1] = BARROW_LOOT[Misc.random(BARROW_LOOT.length - 1)];
			lootN[1] = 1;
		} else if (chance == 2) {
			loot[0] = BARROW_LOOT[Misc.random(BARROW_LOOT.length - 1)];
			lootN[0] = 1;
			loot[1] = BARROW_LOOT[Misc.random(BARROW_LOOT.length - 1)];
			lootN[1] = 1;
		} else if (chance == 4) {
			loot[0] = NOTED_LOOT[Misc.random(NOTED_LOOT.length - 1)];
			lootN[0] = 1;
			loot[1] = BARROW_LOOT[Misc.random(BARROW_LOOT.length - 1)];
			lootN[1] = 1;
		}
		if (loot[0] > 0) {
			c.getItems().addItem(loot[0], lootN[0]);
		}
		if (loot[1] > 0) {
			c.getItems().addItem(loot[1], lootN[1]);
		}

		Achievements.progressMade(c, Achievements.Types.COMPLETE_BARROWS_50_TIMES);
		Achievements.progressMade(c, Achievements.Types.COMPLETE_350_BARROW_RUNS);
		// c.getPA().sendFrame35(150);
	}

}