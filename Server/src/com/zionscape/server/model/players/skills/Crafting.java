package com.zionscape.server.model.players.skills;

import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;

public class Crafting {

	public int hideType = 0, makeId = 0, amount = 0, craftType = 0, exp = 0, index = 0;
	public int[][] gems = { { 1623, 1607, 1, 50 }, { 1621, 1605, 27, 68 }, { 1619, 1603, 34, 85 },
			{ 1617, 1601, 43, 108 }, { 1631, 1615, 55, 138 }, { 6571, 6573, 67, 168 } };
	public int[] vambs = { 1065, 2487, 2489, 2491 };
	public int[] chaps = { 1099, 2493, 2495, 2497 };
	public int[] bodys = { 1135, 2499, 2501, 2503 };
	public int[][] buttons = { { 34185, 1, 1 }, { 34184, 1, 5 }, { 34183, 1, 10 }, { 34182, 1, 27 }, { 34189, 2, 1 },
			{ 34188, 2, 5 }, { 34187, 2, 10 }, { 34186, 2, 27 }, { 34193, 3, 1 }, { 34192, 3, 5 }, { 34191, 3, 10 },
			{ 34190, 3, 27 } };
	public int[][] expsAndLevels = { { 1745, 62, 57 }, { 2505, 66, 70 }, { 2507, 73, 78 }, { 2509, 79, 86 } };
	Player c;

	public Crafting(Player c) {
		this.c = c;
	}

	public static boolean itemOnItem(Player player, int firstItemId, int firstItemSlot, int secondItemId,
			int secondItemSlot) {

		if (firstItemId == 1785 && secondItemId == 25618) {

			if (player.getActualLevel(PlayerConstants.CRAFTING) < 89) {
				player.sendMessage("This requires level 89+ crafting.");
				return true;
			}

			player.getItems().deleteItem(secondItemId, 1);
			player.getItems().addItem(25615, 1);
			player.getPA().addSkillXP(177, PlayerConstants.CRAFTING);

			player.startAnimation(890);

			player.sendMessage("You craft the Robust glass into a Potion flask.");
			return true;
		}

		return false;
	}

	public void checkRequirements() {
		for (int j = 0; j < expsAndLevels.length; j++) {
			if (expsAndLevels[j][0] == hideType) {
				if (c.level[PlayerConstants.CRAFTING] >= expsAndLevels[j][1]) {
					if (c.getItems().playerHasItem(hideType, 1)) {
						c.getPA().closeAllWindows();
						exp = expsAndLevels[j][2];
						index = j;
						this.craftHides(hideType);
					}
				} else {
					c.sendMessage("You need a crafting level of " + expsAndLevels[j][1] + " to craft this.");
				}
			}
		}
	}

	public void craftHides(int id) {

		if (c.attributeExists("sandwhich_lady")) {
			Thieving.sandwhichLadyEvent(c);
			return;
		}

		for (int j = 0; j < amount; j++) {
			if (!c.getItems().playerHasItem(id, craftType)) {
				break;
			}
			c.getItems().deleteItem(id, craftType);
			if (this.getItemToAdd() <= 0) {
				break;
			}
			c.getItems().addItem(this.getItemToAdd(), 1);
			c.getPA().addSkillXP(exp, PlayerConstants.CRAFTING);

			if (getItemToAdd() == 2503) {
				Achievements.progressMade(c, Achievements.Types.CRAFT_500_BLACK_BODIES);
			}

			if (!c.attributeExists("leather_crafted")) {
				c.setAttribute("leather_crafted", 0);
			}

			int thievedStall = (int) c.getAttribute("leather_crafted");
			if (thievedStall + 1 > 75) {
				Thieving.sandwhichLadyEvent(c);
				return;
			}

			c.setAttribute("leather_crafted", thievedStall + 1);

		}
		this.resetCrafting();
	}

	private int h, iter = 0;

	public void cutGem(int id) {
		if (c.attributeExists("sandwhich_lady")) {
			Thieving.sandwhichLadyEvent(c);
			return;
		}
		for (int j = 0; j < gems.length; j++) {
			if (gems[j][0] == id) {
				if (c.level[PlayerConstants.CRAFTING] >= gems[j][2]) {
					int amount = c.getItems().getItemAmount(id);
					h = j;
					System.out.println(h + " - " + j);
					GameCycleTaskHandler.addEvent(c, container -> {
						container.setStopUponWalking(true);
						c.startAnimation(890, 1);
						c.getItems().deleteItem(id, c.getItems().getItemSlot(id), 1);
						c.getItems().addItem(gems[h][1], 1);
						c.getPA().addSkillXP(gems[h][3], PlayerConstants.CRAFTING);

						if (gems[h][0] == 1623) {
							Achievements.progressMade(c, Achievements.Types.CRAFT_100_UNCUT_SAPPHIRE);
						}
						if (gems[h][0] == 1617) {
							Achievements.progressMade(c, Achievements.Types.CRAFT_500_DIAMONDS);
						}

						if (!c.attributeExists("gem_cut")) {
							c.setAttribute("gem_cut", 0);
						}

						int thievedStall = (int) c.getAttribute("gem_cut");
						if (thievedStall + 1 > 75) {
							Thieving.sandwhichLadyEvent(c);
							iter = 0;
							container.stop();
							return;
						}

						c.setAttribute("gem_cut", thievedStall + 1);
						if (++iter == amount) {
							iter = 0;
							container.stop();
							return;
						}
					}, 2);
				}
			}
		}
	}

	public int getItemToAdd() {
		if (craftType == 1) {
			return vambs[index];
		} else if (craftType == 2) {
			return chaps[index];
		} else if (craftType == 3) {
			return bodys[index];
		}
		return -1;
	}

	public void handleChisel(int id1, int id2) {

		long lastGemCut = 0;
		if (c.attributeExists("last_gem_cut")) {
			lastGemCut = (long) c.getAttribute("last_gem_cut");
		}

		if (System.currentTimeMillis() - lastGemCut < 600) {
			return;
		}

		c.setAttribute("last_gem_cut", System.currentTimeMillis());

		if (id1 == 1755) {
			this.cutGem(id2);
		} else {
			this.cutGem(id1);
		}
	}

	public void handleCraftingClick(int clickId) {
		for (int j = 0; j < buttons.length; j++) {
			if (buttons[j][0] == clickId) {
				craftType = buttons[j][1];
				amount = buttons[j][2];
				this.checkRequirements();
				break;
			}
		}
	}

	public void handleLeather(int item1, int item2) {
		if (item1 == 1733) {
			this.openLeather(item2);
		} else {
			this.openLeather(item1);
		}
	}

	public void openLeather(int item) {
		if (item == 1745) {
			c.getPA().sendFrame164(8880); // green dhide
			c.getPA().sendFrame126("What would you like to make?", 8879);
			c.getPA().sendFrame246(8884, 250, 1099); // middle
			c.getPA().sendFrame246(8883, 250, 1065); // left picture
			c.getPA().sendFrame246(8885, 250, 1135); // right pic
			c.getPA().sendFrame126("Vambs", 8889);
			c.getPA().sendFrame126("Chaps", 8893);
			c.getPA().sendFrame126("Body", 8897);
			hideType = item;
		} else if (item == 2505) {
			c.getPA().sendFrame164(8880); // blue
			c.getPA().sendFrame126("What would you like to make?", 8879);
			c.getPA().sendFrame246(8884, 250, 2493); // middle
			c.getPA().sendFrame246(8883, 250, 2487); // left picture
			c.getPA().sendFrame246(8885, 250, 2499); // right pic
			c.getPA().sendFrame126("Vambs", 8889);
			c.getPA().sendFrame126("Chaps", 8893);
			c.getPA().sendFrame126("Body", 8897);
			hideType = item;
		} else if (item == 2507) {
			c.getPA().sendFrame164(8880);
			c.getPA().sendFrame126("What would you like to make?", 8879);
			c.getPA().sendFrame246(8884, 250, 2495); // middle
			c.getPA().sendFrame246(8883, 250, 2489); // left picture
			c.getPA().sendFrame246(8885, 250, 2501); // right pic
			c.getPA().sendFrame126("Vambs", 8889);
			c.getPA().sendFrame126("Chaps", 8893);
			c.getPA().sendFrame126("Body", 8897);
			hideType = item;
		} else if (item == 2509) {
			c.getPA().sendFrame164(8880);
			c.getPA().sendFrame126("What would you like to make?", 8879);
			c.getPA().sendFrame246(8884, 250, 2497); // middle
			c.getPA().sendFrame246(8883, 250, 2491); // left picture
			c.getPA().sendFrame246(8885, 250, 2503); // right pic
			c.getPA().sendFrame126("Vambs", 8889);
			c.getPA().sendFrame126("Chaps", 8893);
			c.getPA().sendFrame126("Body", 8897);
			hideType = item;
		}
		c.craftingLeather = true;
	}

	public void resetCrafting() {
		hideType = 0;
		makeId = 0;
		amount = 0;
		c.craftingLeather = false;
		craftType = 0;
	}
}