package com.zionscape.server.model.players.skills;

import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.util.Misc;

/**
 * Thieving.java
 *
 * @author Sanity
 */
public class Thieving {

	private static final int[] SANDWHICH_BUTTONS = new int[]{63013, 63014, 63015, 63009, 63010, 63011, 63012};
	// npc, level, exp, coin amount
	public int[][] npcThieving = {
			{1, 1, 8, 200, 1}, // men
			{18, 25, 26, 500, 1},
			{9, 40, 47, 1000, 2},
			{26, 55, 85, 1500, 3},
			{20, 70, 152, 2000, 4}, // paladin
			{21, 80, 273, 3000, 5}}; // hero
	private Player c;

	public Thieving(Player c) {
		this.c = c;
	}

	public static void sandwhichLadyEvent(Player player) {
		player.getPA().sendFrame126(" ", 16131);
		player.getPA().showInterface(16135);
		int randomMessage = Misc.random(6);

		player.setAttribute("sandwhich_lady", randomMessage);

		if (randomMessage == 0) {
			player.getPA().sendFrame126("Please select the pie.", 16145);
		} else if (randomMessage == 1) {
			player.getPA().sendFrame126("Please select the purple drink.", 16145);
		} else if (randomMessage == 2) {
			player.getPA().sendFrame126("Please select the chocolate.", 16145);
		} else if (randomMessage == 3) {
			player.getPA().sendFrame126("Please select the bagel.", 16145);
		} else if (randomMessage == 4) {
			player.getPA().sendFrame126("Please select the triangle sandwich.", 16145);
		} else if (randomMessage == 5) {
			player.getPA().sendFrame126("Please select the square sandwich.", 16145);
		} else if (randomMessage == 6) {
			player.getPA().sendFrame126("Please select the bread.", 16145);
		}
	}

	public static boolean onClickingButtons(Player player, int button) {

		for (int i = 0; i < SANDWHICH_BUTTONS.length; i++) {
			if (button == SANDWHICH_BUTTONS[i]) {

				if (!player.attributeExists("sandwhich_lady")) {
					return true;
				}

				int index = (int) player.getAttribute("sandwhich_lady");
				if (index != i) {
					player.sendMessage("Incorrect answer!");
					return true;
				}

				player.setAttribute("gem_cut", 0);
				player.setAttribute("leather_crafted", 0);
				player.setAttribute("thieved_stall", 0);
				player.removeAttribute("sandwhich_lady");
				player.sendMessage("Correct answer!");

				player.getPA().closeAllWindows();

				return true;
			}
		}

		return false;
	}

	public void stealFromNPC(int id) {
		if (System.currentTimeMillis() - c.lastThieve < 2000) {
			return;
		}
		for (int j = 0; j < npcThieving.length; j++) {
			if (npcThieving[j][0] == id) {
				if (c.level[PlayerConstants.THIEVING] >= npcThieving[j][1]) {
					if (Misc.random(c.level[PlayerConstants.THIEVING] + 2 - npcThieving[j][1]) != 1) {
						c.getPA().addSkillXP(npcThieving[j][2], PlayerConstants.THIEVING);
						c.getItems().addItem(995, npcThieving[j][3]);
						c.startAnimation(881);
						c.lastThieve = System.currentTimeMillis();
						c.sendMessage("You thieve some money...");

						if (j == 0) {
							Achievements.progressMade(c, Achievements.Types.PICKPOCKET_A_MAN);
						}

						if (j == 4) {
							Achievements.progressMade(c, Achievements.Types.PICKPOCKET_250_PALADINS);
						}

						if (j == 5) {
							Achievements.progressMade(c, Achievements.Types.PICKPOCKET_500_HEROES);
						}

						if(SkillUtils.checkSkillRequirement(c, PlayerConstants.THIEVING) && SkillUtils.givePet(c.getActualLevel(PlayerConstants.THIEVING), 10)) {
							c.getItems().addOrBank(25621, 1);
							c.sendMessage("You have been given a Giant squirrel pet.");
						}

						break;
					} else {
						c.setHitDiff(npcThieving[j][4]);
						c.setHitUpdateRequired(true);
						c.level[3] -= npcThieving[j][4];
						c.getPA().refreshSkill(3);
						c.lastThieve = System.currentTimeMillis() + 2000;
						c.sendMessage("You fail to thieve the NPC.");
						break;
					}
				} else {
					c.sendMessage("You need a thieving level of " + npcThieving[j][1] + " to thieve from this NPC.");
				}
			}
		}
	}

	public void stealFromStall(int id, int xp, int level) {

		if (c.attributeExists("sandwhich_lady")) {
			sandwhichLadyEvent(c);
			return;
		}

		if (System.currentTimeMillis() - c.lastThieve < 2500) {
			return;
		}
		if (Misc.random(100) == 0) {
			return;
		}
		if (c.level[PlayerConstants.THIEVING] >= level) {
			if (c.getItems().addItem(id, 1)) {

				if (!c.attributeExists("thieved_stall")) {
					c.setAttribute("thieved_stall", 0);
				}

				int thievedStall = (int) c.getAttribute("thieved_stall");
				if (thievedStall + 1 > 115) {
					sandwhichLadyEvent(c);
					return;
				}

				c.setAttribute("thieved_stall", thievedStall + 1);

				c.startAnimation(832);
				c.getPA().addSkillXP(xp, PlayerConstants.THIEVING);
				c.lastThieve = System.currentTimeMillis();
				c.sendMessage("You steal a " + ItemUtility.getName(id) + ".");

				Achievements.progressMade(c, Achievements.Types.LOOT_STALL_350_TIMES);

			}
		} else {
			c.sendMessage("You must have a thieving level of " + level + " to thieve from this stall.");
		}
	}


}