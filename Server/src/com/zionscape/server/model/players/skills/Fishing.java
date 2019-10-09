package com.zionscape.server.model.players.skills;

import com.zionscape.server.Server;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.tick.Tick;
import com.zionscape.server.util.Misc;

/*
 * Fishing handler V1.
 * Auther: Eric W.
 * Email: EricW.Gabber93@hotmail.com
 * Created: 24/5/2011
 */

public class Fishing {

	private static final int fishingXP = 5;
	private static final int[][] data = {
			// {FID, ,lvl ,Tool ,Bait ,raw ,XP ,Anim ,raw2 ,lvl2 ,XP2
			{1, 1, 303, -1, 317, 10, 621, 321, 15, 30}, // SHRIMP + ANCHOVIES
			{2, 5, 309, 313, 327, 20, 622, 345, 10, 30}, // SARDINE + HERRING
			{4, 20, 309, 314, 335, 50, 622, 331, 30, 70}, // TROUT
			{5, 23, 305, -1, 341, 45, 619, 363, 46, 100}, // BASS + COD
			{6, 25, 309, 313, 349, 60, 622, -1, -1, -1}, // PIKE
			{7, 35, 311, -1, 359, 100, 618, 371, 50, 80}, // TUNA + SWORDIE
			{8, 40, 301, -1, 377, 90, 619, -1, -1, -1}, // LOBSTER
			{9, 62, 305, -1, 7944, 110, 619, -1, -1, -1}, // MONKFISH
			{10, 76, 311, -1, 383, 185, 618, -1, -1, -1}, // SHARK
			{11, 79, 305, -1, 395, 40, 619, -1, -1, -1}, // SEA TURTLE
			{12, 81, 305, -1, 389, 220, 621, -1, -1, -1}, // MANTA RAY
			{13, 99, 305, -1, 15270, 380, 621, -1, -1, -1},//rocktail
			// {0 ,1 ,2 ,3 ,4 ,5 ,6 ,7 ,8 ,9}
	};

	private static void attemptdata(final Player c, int npcId) {


		if(c.isBusy()) {
			return;
		}

		if (c.getItems().freeInventorySlots() == 0) {
			c.sendMessage("You haven't got enough inventory space to continue fishing!");
			c.getPA().sendStatement("You haven't got enough inventory space to continue fishing!");
			Fishing.resetFishing(c);
			return;
		}
		if (c.playerIsFishing) {
			return;
		}
		for (int i = 0; i < data.length; i++) {
			if (npcId == data[i][0]) {
				if (data[i][4] < 0) {
					Fishing.resetFishing(c);
				}
				/*
				 * Fishing level checking
				 */
				if (c.level[PlayerConstants.FISHING] < data[i][1]) {
					c.sendMessage("You need a fishing level of atleast " + data[i][1] + ".");
					return;
				}
                /*
                 * tool checking
				 */
				if (!c.getItems().playerHasItem(data[i][2])) {
					c.sendMessage("You need an " + c.getItems().getItemName(data[i][2]) + " to be able to fish here.");
					return;
				}
                /*
				 * bait checking
				 */
				if (data[i][3] > 0) {
					if (!c.getItems().playerHasItem(data[i][3])) {
						c.sendMessage("You haven't got any " + c.getItems().getItemName(data[i][3]) + "!");
						return;
					}
				}
				/*
				 * sets the configs.
				 */
				c.fishingProp[0] = data[i][6]; // ANIM
				c.fishingProp[1] = data[i][4]; // FISH
				c.fishingProp[2] = data[i][5]; // XP
				c.fishingProp[3] = data[i][3]; // BAIT
				c.fishingProp[4] = data[i][2]; // EQUIP
				c.fishingProp[5] = data[i][7]; // sFish
				c.fishingProp[6] = data[i][8]; // sLvl
				c.fishingProp[7] = data[i][4]; // FISH
				c.fishingProp[8] = data[i][9]; // sXP

				if (data[i][7] == -1) {
					c.fishingProp[9] = 5;
				} else {
					c.fishingProp[9] = Misc.random(2) == 0 ? 7 : 5;
				}

				c.sendMessage("You start fishing.");
				c.startAnimation(c.fishingProp[0]);
				c.stopPlayerSkill = true;
				if (c.playerIsFishing) {
					return;
				}

				c.setBusy(true);
				final Location location = c.getLocation();
				c.playerIsFishing = true;
				Server.getTickManager().submit(new Tick(Fishing.getTimer(npcId, c.level[PlayerConstants.FISHING]) * 2) {

					@Override
					public void execute() {
						if (c.isDisconnected()) {
							this.stop();
							return;
						}

						if (!c.getLocation().equals(location)) {
							Fishing.resetFishing(c);
							this.stop();
							return;
						}

						if (!c.playerIsFishing) {
							Fishing.resetFishing(c);
							this.stop();
							return;
						}
						if (c.fishingProp[2] == -1) {
							Fishing.resetFishing(c);
							this.stop();
							return;
						}
						if (c.fishingProp[5] > 0) {
							if (c.level[PlayerConstants.FISHING] >= c.fishingProp[6]) {
								c.fishingProp[1] = c.fishingProp[c.fishingProp[9]];
							}
						}
						if (!c.getItems().playerHasItem(c.fishingProp[4])) {
							c.sendMessage("You need a " + c.getItems().getItemName(c.fishingProp[4]) + " to fish here.");
							Fishing.resetFishing(c);
							this.stop();
							return;
						}
						if (c.getItems().freeInventorySlots() == 0) {
							Fishing.resetFishing(c);
							c.sendMessage("You don't have enough inventory space to continue fishing.");
							c.getPA().sendStatement("You don't have enough inventory space to continue fishing.");
							this.stop();
							return;
						}
						if (!c.stopPlayerSkill) {
							Fishing.resetFishing(c);
							this.stop();
							return;
						}
						if (c.fishingProp[3] > 0) {
							c.getItems().deleteItem(c.fishingProp[3], c.getItems().getItemSlot(c.fishingProp[3]), 1);
							if (!c.getItems().playerHasItem(c.fishingProp[3])) {
								c.sendMessage("You haven't got any " + c.getItems().getItemName(c.fishingProp[3])
										+ " left!");
								c.sendMessage("You need " + c.getItems().getItemName(c.fishingProp[3])
										+ " to fish here.");
								Fishing.resetFishing(c);
								this.stop();
								return;
							}
						}

						double xpMulti = 1;
						int skillItemCount = SkillItems.getFishingItemsWorn(c);

						if(skillItemCount > 0) {
							if(skillItemCount >= 4) {
								xpMulti += 0.10;
							} else {
								xpMulti += skillItemCount * 0.02;
							}
						}

						if (c.fishingProp[1] > 0) {
							c.sendMessage("You catch a " + c.getItems().getItemName(c.fishingProp[1]) + ".");
							if(skillItemCount >= 4 && Misc.random(0, 5) == 0) {
								c.getItems().addItem(ItemUtility.getNotedId(c.fishingProp[1]), 1);
							} else {
								c.getItems().addItem(c.fishingProp[1], 1);
							}
						}
						// player.sendMessage("Test: "+player.fishingProp[2]);
						c.getPA().addSkillXP((int)Math.round(xpMulti * (double)(c.fishingProp[c.fishingProp[9] == 7 ? 8 : 2])), PlayerConstants.FISHING);
						c.startAnimation(c.fishingProp[0]);

						if(SkillUtils.givePet(c.getActualLevel(PlayerConstants.FISHING), c.fishingProp[1], skillItemCount >= 4 ? 0.05 : 0)) {
							c.getItems().addOrBank(25083, 1);
							c.sendMessage("You have been given a Heron pet.");
						}

						if (c.fishingProp[1] == 377) {
							Achievements.progressMade(c, Achievements.Types.FISH_500_LOBSTERS);
						}
						if (c.fishingProp[1] == 389) {
							Achievements.progressMade(c, Achievements.Types.FISH_2000_MANTA_RAY);
						}
						if (c.fishingProp[1] == 317) {
							Achievements.progressMade(c, Achievements.Types.FISH_200_SHRIMP);
						}
						if (c.fishingProp[1] == 359) {
							Achievements.progressMade(c, Achievements.Types.FISH_500_TUNA);
						}
					}

					@Override
					public void stop() {
						super.stop();
						c.setBusy(false);
					}
				});
			}
		}
	}

	public static void fishingNPC(Player c, int i, int l) {
		switch (i) {
			case 1:
				switch (l) {
					case 1174: // manta
						Fishing.attemptdata(c, 12);
						break;
					case 319:
					case 329:
					case 323:
					case 325:
					case 326:
					case 327:
					case 330:
					case 332:
					case 316: // NET + BAIT
						Fishing.attemptdata(c, 1);
						break;
					case 334:
					case 313: // NET + HARPOON
						Fishing.attemptdata(c, 13);
						break;
					case 322: // NET + HARPOON
						Fishing.attemptdata(c, 5);
						break;
					case 309: // LURE
					case 310:
					case 311:
					case 314:
					case 315:
					case 317:
					case 318:
					case 328:
					case 331:
						Fishing.attemptdata(c, 4);
						break;
					case 312:
					case 321:
					case 324: // CAGE + HARPOON
						Fishing.attemptdata(c, 8);
						break;
				}
				break;
			case 2:
				switch (l) {
					case 326:
					case 327:
					case 330:
					case 332:
					case 316: // BAIT + NET
						Fishing.attemptdata(c, 2);
						break;
					case 319:
					case 323:
					case 325: // BAIT + NET
						Fishing.attemptdata(c, 9);
						break;
					case 310:
					case 311:
					case 314:
					case 315:
					case 317:
					case 318:
					case 328:
					case 329:
					case 331:
					case 309: // BAIT + LURE
						Fishing.attemptdata(c, 6);
						break;
					case 312:
					case 321:
					case 324:// SWORDIES+TUNA-CAGE+HARPOON
						Fishing.attemptdata(c, 7);
						break;
					case 313:
					case 322:
					case 334: // NET+HARPOON
						Fishing.attemptdata(c, 10);
						break;
				}
				break;
		}
	}

	private static int getTimer(int npcId, int level) {
		int delay = Misc.random(1);// Misc.random((9900 - (level * 100)));
		switch (npcId) {
			case 1:
				return delay += 2;
			case 2:
				return delay += 2;
			case 3:
				return delay += 2;
			case 4:
				return delay += 2;
			case 5:
				return delay += 2;
			case 6:
				return delay += 3;
			case 7:
				return delay += 3;
			case 8:
				return delay += 3;
			case 9:
				return delay += 4;
			case 10:
				return delay += 4;
			case 11:
				return delay += 4;
			case 12:
				return delay += 5;
			case 13:
				return delay += 5;
			default:
				return 1;
		}
	}

	public static void resetFishing(Player c) {
		c.startAnimation(65535);
		c.getPA().removeAllWindows();
		c.playerIsFishing = false;
		for (int i = 0; i < 9; i++) {
			c.fishingProp[i] = -1;
		}
	}
}