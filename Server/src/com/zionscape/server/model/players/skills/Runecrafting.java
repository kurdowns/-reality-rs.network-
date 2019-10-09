package com.zionscape.server.model.players.skills;

import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.content.pets.Pets;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;

/*
 *Runecrafting
 */

public class Runecrafting {

	private static final int ESSENCE_ITEM_ID = 1436/*7936*/;

	private Player c;

	public Runecrafting(Player client) {
		this.c = client;
	}

	/*
	 * Main
	 */
	public void runecraft(int level, int exp, int rune, int a2, int a3, int a4, int a5, int a6, int a7, int a8, int a9, int a10) {
		int amount = c.getItems().amountOfItem(ESSENCE_ITEM_ID);
		if (amount <= 0) {
			c.sendMessage("You do not have any Rune essence to craft.");
			return;
		}

		if (c.level[PlayerConstants.RUNECRAFTING] < level) {
			c.sendMessage("You need a Runecrafting level of " + level + " in order to do this.");
			return;
		}

		if (c.level[PlayerConstants.RUNECRAFTING] >= level) {
			if (c.level[PlayerConstants.RUNECRAFTING] >= a2 && c.level[PlayerConstants.RUNECRAFTING] < a3) {
				amount *= 2;
			}
			if (c.level[PlayerConstants.RUNECRAFTING] >= a3 && c.level[PlayerConstants.RUNECRAFTING] < a4) {
				amount *= 3;
			}
			if (c.level[PlayerConstants.RUNECRAFTING] >= a4 && c.level[PlayerConstants.RUNECRAFTING] < a5) {
				amount *= 4;
			}
			if (c.level[PlayerConstants.RUNECRAFTING] >= a5 && c.level[PlayerConstants.RUNECRAFTING] < a6) {
				amount *= 5;
			}
			if (c.level[PlayerConstants.RUNECRAFTING] >= a6 && c.level[PlayerConstants.RUNECRAFTING] < a7) {
				amount *= 6;
			}
			if (c.level[PlayerConstants.RUNECRAFTING] >= a7 && c.level[PlayerConstants.RUNECRAFTING] < a8) {
				amount *= 7;
			}
			if (c.level[PlayerConstants.RUNECRAFTING] >= a8 && c.level[PlayerConstants.RUNECRAFTING] < a9) {
				amount *= 8;
			}
			if (c.level[PlayerConstants.RUNECRAFTING] >= a9 && c.level[PlayerConstants.RUNECRAFTING] < a10) {
				amount *= 9;
			}
			if (c.level[PlayerConstants.RUNECRAFTING] >= a10) {
				amount *= 10;
			}
		}
		int petId = -1;

		switch (rune) {
			case 565: // blood
				petId = 25635;
				break;
			case 560: // death
				petId = 25634;
				break;
			case 563: // law
				petId = 25627;
				break;
			case 561: // nat
				petId = 25630;
				break;
			case 562: // chaos
				petId = 25623;
				break;
			case 564: // cosmic
				petId = 25628;
				break;
			case 559: // body
				petId = 25625;
				break;
			case 554: // fire
				petId = 25622;
				break;
			case 557: // earth
				petId = 25626;
				break;
			case 555: // water
				petId = 25625;
				break;
			case 558: // mind
				petId = 25625;
				break;
			case 556: // air
				petId = 25623;
				break;
		}

		Pets.changeRiftGuardianColor(c, petId);

		if(SkillUtils.checkSkillRequirement(c, PlayerConstants.RUNECRAFTING) && SkillUtils.givePet(c.getActualLevel(PlayerConstants.RUNECRAFTING), rune)) {
			if(petId > -1) {
				c.getItems().addOrBank(petId, 1);
				c.sendMessage("You have been given a Rift guardian pet.");
			}
		}

		c.getItems().deleteItem(ESSENCE_ITEM_ID, amount);
		c.getItems().addItem(rune, amount);
		c.getPA().addSkillXP(exp * amount, 20);
		c.sendMessage("You bind the temple's power into " + c.getItems().getItemName(rune).toLowerCase() + "s.");
		c.startAnimation(791);
		c.getPA().highGFX(186);

		for (int i = 0; i < amount; i++) {
			Achievements.progressMade(c, Achievements.Types.USE_50_ESS);
			Achievements.progressMade(c, Achievements.Types.USE_1000_RUNE_ESS);
			Achievements.progressMade(c, Achievements.Types.USE_2500_ESS);
		}

		c.getPA().refreshSkill(20);
	}

	/*
	 * Altars
	 */

	public void altars(int object) {
		switch (object) {
			case 2478: // air
				this.runecraft(1, 5, 556, 11, 22, 33, 44, 55, 66, 77, 88, 99);
				break;
			case 2479: // mind
				this.runecraft(2, 6, 558, 14, 28, 42, 56, 70, 84, 98, 150, 150);
				break;
			case 2480: // water
				this.runecraft(5, 7, 555, 19, 38, 57, 76, 95, 150, 150, 150, 150);
				break;
			case 2481: // earth
				this.runecraft(9, 8, 557, 26, 52, 78, 150, 150, 150, 150, 150, 150);
				break;
			case 2482: // fire
				this.runecraft(14, 9, 554, 35, 70, 150, 150, 150, 150, 150, 150, 150);
				break;
			case 2483: // body
				this.runecraft(20, 10, 559, 46, 92, 150, 150, 150, 150, 150, 150, 150);
				break;
			case 2484: // cosmic
				this.runecraft(27, 12, 564, 59, 150, 150, 150, 150, 150, 150, 150, 150);
				break;
			case 2487: // chaos
				this.runecraft(35, 15, 562, 74, 150, 150, 150, 150, 150, 150, 150, 150);
				break;
			case 2486: // nat
				this.runecraft(44, 20, 561, 91, 150, 150, 150, 150, 150, 150, 150, 150);
				break;
			case 2485: // law
				this.runecraft(54, 29, 563, 150, 150, 150, 150, 150, 150, 150, 150, 150);
				break;
			case 2488: // death
				this.runecraft(65, 36, 560, 150, 150, 150, 150, 150, 150, 150, 150, 150);
				break;
			case 30624:
			case 2489: // bloods
				this.runecraft(77, 45, 565, 150, 150, 150, 150, 150, 150, 150, 150, 150);
				break;
			case 7139:
				c.getPA().movePlayer(2841, 4829, 0);
				break;
			case 7140:
				c.getPA().movePlayer(2793, 4828, 0);
				break;
			case 7137:
				c.getPA().movePlayer(2727, 4833, 0);
				break;
			case 7130:
				c.getPA().movePlayer(2655, 4830, 0);
				break;
			case 7129:
				c.getPA().movePlayer(2574, 4849, 0);
				break;
			case 7131:
				c.getPA().movePlayer(2524, 4833, 0);
				break;
			case 7132:
				c.getPA().movePlayer(2137, 4833, 0);
				break;
			case 7134:
				c.getPA().movePlayer(2266, 4842, 0);
				break;
			case 7133:
				c.getPA().movePlayer(2400, 4835, 0);
				break;
			case 7135:
				c.getPA().movePlayer(2464, 4818, 0);
				break;
			case 7136:
				c.getPA().movePlayer(2208, 4830, 0);
				break;
			case 7141:
				c.getPA().movePlayer(2467, 4895, 1);
				break;
			case 2465:
			case 2466:
			case 2467:
			case 2468:
			case 2469:
			case 2470:
			case 2471:
			case 2472:
			case 2473:
			case 2474:
			case 2475:
			case 2476:
			case 2477:
				c.getPA().movePlayer(3087, 3490, 0);
				break;
		}// You just need to add the blood altar :)
	}
}