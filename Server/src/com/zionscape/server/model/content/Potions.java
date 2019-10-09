package com.zionscape.server.model.content;

import com.zionscape.server.gamecycle.GameCycleTask;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.content.minigames.clanwars.Constants;
import com.zionscape.server.model.content.minigames.clanwars.War;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;

/**
 * @author Sanity
 */
public class Potions {

	private Player c;

	public Potions(Player c) {
		this.c = c;
	}

	private static int getOverloadBoost(Player player, int skill) {
		double boost = 1;
		switch (skill) {
		case 0:
		case 1:
		case 2:
			boost = 5 + (player.getPA().getActualLevel(skill) * .22);
			break;
		case 4:
			boost = 3 + (player.getPA().getActualLevel(skill) * .22);
			break;
		case 6:
			boost = 7;
			break;
		}
		return (int) boost;
	}

	public static void doOverloadBoost(Player player) {
		int[] toIncrease = { 0, 1, 2, 4, 6 };
		int boost;
		for (int skill : toIncrease) {
			boost = getOverloadBoost(player, skill);

			player.level[skill] += boost;

			if (player.level[skill] > player.getPA().getActualLevel(skill) + boost) {
				player.level[skill] = player.getPA().getActualLevel(skill) + boost;
			}

			player.getPA().refreshSkill(skill);
		}
	}

	public static void doOverload(Player player, int itemId, int replaceItem, int slot) {
		player.startAnimation(829);

		if (replaceItem == -1) {
			player.inventory[slot] = 0;
			player.inventoryN[slot] = 0;
			player.getItems().resetItems(3214);
		}

		if (replaceItem > -1) {
			player.inventory[slot] = (short) (replaceItem + 1);
			player.getItems().resetItems(3214);
		}

		player.getData().lastOverloadPotion = System.currentTimeMillis();
		doOverloadBoost(player);
		startOverloadPotionEvent(player);
		player.setOverloaded(true);

		GameCycleTaskHandler.addEvent(player, new GameCycleTask() {

			int damageDelt = 0;

			@Override
			public void execute(GameCycleTaskContainer container) {

				player.dealDamage(10);
				player.handleHitMask(10, 0, 2, 0);
				player.getPA().refreshSkill(PlayerConstants.HITPOINTS);
				damageDelt += 10;

				if (damageDelt >= 50) {
					container.stop();
				}

			}
		}, 5).setName("overload-damage" + player.username);
	}

	public static void startOverloadPotionEvent(Player player) {
		GameCycleTaskHandler.stopEvents("overload-restore" + player.username);

		GameCycleTaskHandler.addEvent(player, new GameCycleTask() {

			@Override
			public void execute(GameCycleTaskContainer container) {
				// do the effect
				doOverloadBoost(container.getPlayer());

				// check if the effect has expired
				if (System.currentTimeMillis() - player.getData().lastOverloadPotion > 5 * 60 * 1000) {

					int[] toIncrease = { 0, 1, 2, 4, 6 };

					for (int i = 0; i < toIncrease.length; i++) {
						player.level[toIncrease[i]] = player.getPA().getActualLevel(toIncrease[i]);
						player.getPA().refreshSkill(toIncrease[i]);
					}
					player.setOverloaded(false);
					player.sendMessage("Your Overload Potion effect has now worn off.");
					container.stop();
				}

			}
		}, 25).setName("overload-restore" + player.username);
	}

	public static void drinkAntiFire(Player c, int replaceItem, int slot, int time) {
		c.startAnimation(829);

		if (replaceItem == -1) {
			c.inventory[slot] = 0;
			c.inventoryN[slot] = 0;
			c.getItems().resetItems(3214);
		}

		if (replaceItem > -1) {
			c.inventory[slot] = replaceItem + 1;
			c.getItems().resetItems(3214);
		}
		c.antiFirePot = time / 600;
	}

	public static void drinkSuperAntiFire(Player c, int replaceItem, int slot, int time) {
		c.startAnimation(829);
		c.inventory[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);
		c.antiFirePot = time / 1200;
	}

	public void handlePotion(int itemId, int slot) {
		// Check item is real
		if (!c.getItems().playerHasItem(itemId, 1)) {
			return;
		}

		if (c.getDuel() != null) {
			if (c.getDuel().getRules()[PlayerConstants.DUEL_DRINKS]) {
				c.sendMessage("You may not drink potions in this duel.");
				return;
			}
		}

		if (c.getAttribute("clan_war") != null) {
			War war = (War) c.getAttribute("clan_war");
			if (war.getCombatRules()[Constants.POTIONS_RULE]) {
				c.sendMessage("You may may not drink potions in this clan war.");
				return;
			}
		}

		if (System.currentTimeMillis() - c.potDelay >= 1500) {
			c.potDelay = System.currentTimeMillis();
			c.foodDelay = c.potDelay;
			c.getCombat().resetPlayerAttack();
			c.attackTimer++;
			c.sendMessage("You drink some of your " + ItemUtility.getName(itemId) + ".");
			String item = ItemUtility.getName(itemId);
			if (item.endsWith("(4)")) {
				c.sendMessage("You have 3 doses of potion left.");
			} else if (item.endsWith("(3)")) {
				c.sendMessage("You have 2 doses of potion left.");
			} else if (item.endsWith("(2)")) {
				c.sendMessage("You have 1 dose of potion left.");
			} else if (item.endsWith("(1)")) {
				c.sendMessage("You have finished your potion.");
			}
			switch (itemId) {

			case 25339:
			case 25341:
			case 25343:
			case 25345:
			case 25347:
			case 25349:
				int give = itemId + 2;
				if (itemId == 25349) {
					give = -1;
				}

				drinkAntiFire(c, give, slot, 360000);
				break;

			case 2452:
				drinkAntiFire(c, 2454, slot, 360000);
				break;
			case 2454:
				drinkAntiFire(c, 2456, slot, 360000);
				break;
			case 2456:
				drinkAntiFire(c, 2458, slot, 360000);
				break;
			case 2458:
				drinkAntiFire(c, 229, slot, 360000);
				break;

			case 15304:
				drinkSuperAntiFire(c, 15305, slot, 360000);
				break;
			case 15305:
				drinkSuperAntiFire(c, 15306, slot, 360000);
				break;
			case 15306:
				drinkSuperAntiFire(c, 15307, slot, 360000);
				break;
			case 15307:
				drinkSuperAntiFire(c, 229, slot, 360000);
				break;

			case 25399:
			case 25401:
			case 25403:
			case 25405:
			case 25407:
			case 25409:
				give = itemId + 2;
				if (itemId == 25409) {
					give = -1;
				}

				drinkStatPotion(itemId, give, slot, 6, false);
				break;

			case 3040: // (4) magic pots
				drinkStatPotion(itemId, 3042, slot, 6, false);
				break;
			case 3042: // (3)
				drinkStatPotion(itemId, 3044, slot, 6, false);
				break;
			case 3044: // (2)
				drinkStatPotion(itemId, 3046, slot, 6, false);
				break;
			case 3046: // (1)
				drinkStatPotion(itemId, 229, slot, 6, false);
				break;

			case 25327:
			case 25329:
			case 25331:
			case 25333:
			case 25335:
			case 25337:

				give = itemId + 2;
				if (itemId == 25337) {
					give = -1;
				}

				this.doTheBrew(itemId, give, slot);
				break;

			case 6685: // brews
				this.doTheBrew(itemId, 6687, slot);
				break;
			case 6687:
				this.doTheBrew(itemId, 6689, slot);
				break;
			case 6689:
				this.doTheBrew(itemId, 6691, slot);
				break;
			case 6691:
				this.doTheBrew(itemId, 229, slot);
				break;

			case 25471:
			case 25472:
			case 25473:
			case 25474:
			case 25475:
			case 25476:
				give = itemId + 1;
				if (itemId == 25476) {
					give = -1;
				}
				this.drinkExt(itemId, give, slot, 0);
				break;

			case 15308:
				this.drinkExt(itemId, 15309, slot, 0);
				break;
			case 15309:
				this.drinkExt(itemId, 15310, slot, 0);
				break;
			case 15310:
				this.drinkExt(itemId, 15311, slot, 0);
				break;
			case 15311:
				this.drinkExt(itemId, 229, slot, 0);
				break;

			case 25477:
			case 25478:
			case 25479:
			case 25480:
			case 25481:
			case 25482:
				give = itemId + 1;
				if (itemId == 25482) {
					give = -1;
				}
				this.drinkExt(itemId, give, slot, 2);
				break;

			case 15312:
				this.drinkExt(itemId, 15313, slot, 2);
				break;
			case 15313:
				this.drinkExt(itemId, 15314, slot, 2);
				break;
			case 15314:
				this.drinkExt(itemId, 15315, slot, 2);
				break;
			case 15315:
				this.drinkExt(itemId, 229, slot, 2);
				break;

			case 25483:
			case 25484:
			case 25485:
			case 25486:
			case 25487:
			case 25488:
				give = itemId + 1;
				if (itemId == 25277) {
					give = -1;
				}
				this.drinkExt(itemId, give, slot, 1);
				break;

			case 15316:
				this.drinkExt(itemId, 15317, slot, 1);
				break;
			case 15317:
				this.drinkExt(itemId, 15318, slot, 1);
				break;
			case 15318:
				this.drinkExt(itemId, 15319, slot, 1);
				break;
			case 15319:
				this.drinkExt(itemId, 229, slot, 1);
				break;

			case 25494:
			case 25493:
			case 25492:
			case 25491:
			case 25490:
			case 25489:
				give = itemId + 1;
				if (itemId == 25494) {
					give = -1;
				}
				this.drinkExt(itemId, give, slot, 6);
				break;

			case 15320:
				this.drinkExt(itemId, 15321, slot, 6);
				break;
			case 15321:
				this.drinkExt(itemId, 15322, slot, 6);
				break;
			case 15322:
				this.drinkExt(itemId, 15323, slot, 6);
				break;
			case 15323:
				this.drinkExt(itemId, 229, slot, 6);
				break;

			case 25500:
			case 25499:
			case 25498:
			case 25497:
			case 25496:
			case 25495:
				give = itemId + 1;
				if (itemId == 25500) {
					give = -1;
				}
				this.drinkExt(itemId, give, slot, 4);
				break;

			case 15324:
				this.drinkExt(itemId, 15325, slot, 4);
				break;
			case 15325:
				this.drinkExt(itemId, 15326, slot, 4);
				break;
			case 15326:
				this.drinkExt(itemId, 15327, slot, 4);
				break;
			case 15327:
				this.drinkExt(itemId, 229, slot, 4);
				break;

			case 25231:
			case 25233:
			case 25235:
			case 25237:
			case 25239:
			case 25241:
				give = itemId + 2;
				if (itemId == 25241) {
					give = -1;
				}

				this.drinkStatPotion(itemId, give, slot, 0, true); // sup attack
				break;

			case 2436:
				this.drinkStatPotion(itemId, 145, slot, 0, true); // sup attack
				break;
			case 145:
				this.drinkStatPotion(itemId, 147, slot, 0, true);
				break;
			case 147:
				this.drinkStatPotion(itemId, 149, slot, 0, true);
				break;
			case 149:
				this.drinkStatPotion(itemId, 229, slot, 0, true);
				break;

			case 25255:
			case 25257:
			case 25259:
			case 25261:
			case 25263:
			case 25265:
				give = itemId + 2;
				if (itemId == 25265) {
					give = -1;
				}

				this.drinkStatPotion(itemId, give, slot, 2, true); // sup str
				break;

			case 2440:
				this.drinkStatPotion(itemId, 157, slot, 2, true); // sup str
				break;
			case 157:
				this.drinkStatPotion(itemId, 159, slot, 2, true);
				break;
			case 159:
				this.drinkStatPotion(itemId, 161, slot, 2, true);
				break;
			case 161:
				this.drinkStatPotion(itemId, 229, slot, 2, true);
				break;

			case 25279:
			case 25281:
			case 25283:
			case 25285:
			case 25287:
			case 25289:
				give = itemId + 2;
				if (itemId == 25289) {
					give = -1;
				}

				this.drinkStatPotion(itemId, give, slot, 4, false); // range pot
				break;

			case 2444:
				this.drinkStatPotion(itemId, 169, slot, 4, false); // range pot
				break;
			case 169:
				this.drinkStatPotion(itemId, 171, slot, 4, false);
				break;
			case 171:
				this.drinkStatPotion(itemId, 173, slot, 4, false);
				break;
			case 173:
				this.drinkStatPotion(itemId, 229, slot, 4, false);
				break;
			case 2432:
				this.drinkStatPotion(itemId, 133, slot, 1, false); // def pot
				break;
			case 133:
				this.drinkStatPotion(itemId, 135, slot, 1, false);
				break;
			case 135:
				this.drinkStatPotion(itemId, 137, slot, 1, false);
			case 137:
				this.drinkStatPotion(itemId, 229, slot, 1, false);
				break;
			case 113:
				this.drinkStatPotion(itemId, 115, slot, 2, false); // str pot
				break;
			case 115:
				this.drinkStatPotion(itemId, 117, slot, 2, false);
				break;
			case 117:
				this.drinkStatPotion(itemId, 119, slot, 2, false);
				break;
			case 119:
				this.drinkStatPotion(itemId, 229, slot, 2, false);
				break;
			case 2428:
				this.drinkStatPotion(itemId, 121, slot, 0, false); // attack pot
				break;
			case 121:
				this.drinkStatPotion(itemId, 123, slot, 0, false);
				break;
			case 123:
				this.drinkStatPotion(itemId, 125, slot, 0, false);
				break;
			case 125:
				this.drinkStatPotion(itemId, 229, slot, 0, false);
				break;

			case 25507:
			case 25508:
			case 25509:
			case 25510:
			case 25511:
			case 25512:
				give = itemId + 1;
				if (itemId == 25512) {
					give = -1;
				}
				doOverload(c, itemId, give, slot);
				break;

			case 15332:
				doOverload(c, itemId, 15333, slot);
				break;
			case 15333:
				doOverload(c, itemId, 15334, slot);
				break;
			case 15334:
				doOverload(c, itemId, 15335, slot);
				break;
			case 15335:
				doOverload(c, itemId, 229, slot);
				break;

			case 25267:
			case 25269:
			case 25271:
			case 25273:
			case 25275:
			case 25277:
				give = itemId + 1;
				if (itemId == 25277) {
					give = -1;
				}
				this.drinkStatPotion(itemId, give, slot, 1, true); // super def pot
				break;

			case 2442:
				this.drinkStatPotion(itemId, 163, slot, 1, true); // super def pot
				break;
			case 163:
				this.drinkStatPotion(itemId, 165, slot, 1, true);
				break;
			case 165:
				this.drinkStatPotion(itemId, 167, slot, 1, true);
				break;
			case 167:
				this.drinkStatPotion(itemId, 229, slot, 1, true);
				break;

			case 25375:
			case 25377:
			case 25379:
			case 25381:
			case 25383:
			case 25385:
				give = itemId + 2;
				if (itemId == 25385) {
					give = -1;
				}
				this.drinkPrayerPot(itemId, give, slot, true); // sup restore
				break;

			case 3024:
				this.drinkPrayerPot(itemId, 3026, slot, true); // sup restore
				break;
			case 3026:
				this.drinkPrayerPot(itemId, 3028, slot, true);
				break;
			case 3028:
				this.drinkPrayerPot(itemId, 3030, slot, true);
				break;
			case 3030:
				this.drinkPrayerPot(itemId, 229, slot, true);
				break;
			case 10925:
				this.drinkPrayerPot(itemId, 10927, slot, true); // sanfew serums
				this.curePoison(300000);
				break;
			case 10927:
				this.drinkPrayerPot(itemId, 10929, slot, true);
				this.curePoison(300000);
				break;
			case 10929:
				this.drinkPrayerPot(itemId, 10931, slot, true);
				this.curePoison(300000);
				break;
			case 10931:
				this.drinkPrayerPot(itemId, 229, slot, true);
				this.curePoison(300000);
				break;

			case 12140: // summoning pots
				drinkSummoningPot(12142, slot);
				break;
			case 12142:
				drinkSummoningPot(12144, slot);
				break;
			case 12144:
				drinkSummoningPot(12146, slot);
				break;
			case 12146:
				drinkSummoningPot(229, slot);
				break;

			case 25219:
			case 25221:
			case 25223:
			case 25225:
			case 25227:
			case 25229:
				give = itemId + 2;
				if (itemId == 25229) {
					give = -1;
				}
				this.drinkPrayerPot(itemId, give, slot, false); // pray pot
				break;

			case 2434:
				this.drinkPrayerPot(itemId, 139, slot, false); // pray pot
				break;
			case 139:
				this.drinkPrayerPot(itemId, 141, slot, false);
				break;
			case 141:
				this.drinkPrayerPot(itemId, 143, slot, false);
				break;
			case 143:
				this.drinkPrayerPot(itemId, 229, slot, false);
				break;

			case 25291:
			case 25293:
			case 25295:
			case 25297:
			case 25299:
			case 25301:
				give = itemId + 1;
				if (itemId == 25301) {
					give = -1;
				}
				this.drinkAntiPoison(itemId, give, slot, 30000);
				break;

			case 2446:
				this.drinkAntiPoison(itemId, 175, slot, 30000); // anti poisons
				break;
			case 175:
				this.drinkAntiPoison(itemId, 177, slot, 30000);
				break;
			case 177:
				this.drinkAntiPoison(itemId, 179, slot, 30000);
				break;
			case 179:
				this.drinkAntiPoison(itemId, 229, slot, 30000);
				break;
			case 2448:
				this.drinkAntiPoison(itemId, 181, slot, 300000); // anti poisons
				break;
			case 181:
				this.drinkAntiPoison(itemId, 183, slot, 300000);
				break;
			case 183:
				this.drinkAntiPoison(itemId, 185, slot, 300000);
				break;
			case 185:
				this.drinkAntiPoison(itemId, 229, slot, 300000);
				break;
			}
		}
	}

	public void drinkAntiPoison(int itemId, int replaceItem, int slot, long delay) {
		c.startAnimation(829);

		if (replaceItem == -1) {
			c.inventory[slot] = 0;
			c.inventoryN[slot] = 0;
			c.getItems().resetItems(3214);
		}

		if (replaceItem > -1) {
			c.inventory[slot] = replaceItem + 1;
			c.getItems().resetItems(3214);
		}
		this.curePoison(delay);
	}

	public void curePoison(long delay) {
		c.poisonDamage = 0;
		c.poisonImmune = delay;
		c.lastPoisonSip = System.currentTimeMillis();
	}

	public void drinkStatPotion(int itemId, int replaceItem, int slot, int stat, boolean sup) {
		c.startAnimation(829);

		if (replaceItem == -1) {
			c.inventory[slot] = 0;
			c.inventoryN[slot] = 0;
			c.getItems().resetItems(3214);
		}

		if (replaceItem > -1) {
			c.inventory[slot] = replaceItem + 1;
			c.getItems().resetItems(3214);
		}
		this.enchanceStat(stat, sup);
	}

	public void drinkExt(int itemId, int replaceItem, int slot, int skill) {
		c.startAnimation(829);

		if (replaceItem == -1) {
			c.inventory[slot] = 0;
			c.inventoryN[slot] = 0;
			c.getItems().resetItems(3214);
		}

		if (replaceItem > -1) {
			c.inventory[slot] = replaceItem + 1; // potssss
			c.getItems().resetItems(3214); // show items
		}
		c.level[skill] += this.boostExt(skill);

		c.getPA().refreshSkill(skill);
	}

	public int boostExt(int skill) {
		int backtraced = 0;
		if (skill == 0) {
			backtraced = (int) ((c.getPA().getLevelForXP(c.xp[0]) * .22) + 6);
			c.extAtt = true;
		}
		if (skill == 1) {
			backtraced = (int) ((c.getPA().getLevelForXP(c.xp[1]) * .22) + 6);
			c.extDef = true;
		}
		if (skill == 2) {
			backtraced = (int) ((c.getPA().getLevelForXP(c.xp[2]) * .22) + 6);
			c.extStr = true;
		}
		if (skill == 4) {
			backtraced = (int) (c.getPA().getLevelForXP(c.xp[4]) * .15) + 3;
			c.extRange = true;
		}
		if (skill == 6) {
			backtraced = (int) (c.getPA().getLevelForXP(c.xp[6]) * .15) + 3;
			c.extMage = true;
		}
		if (c.level[skill] + backtraced > c.getPA().getLevelForXP(c.xp[skill]) + backtraced + 1) {
			return c.getPA().getLevelForXP(c.xp[skill]) + backtraced - c.level[skill];
		}
		return backtraced;
	}

	public void drinkPrayerPot(int itemId, int replaceItem, int slot, boolean rest) {
		c.startAnimation(829);

		if (replaceItem == -1) {
			c.inventory[slot] = 0;
			c.inventoryN[slot] = 0;
			c.getItems().resetItems(3214);
		}

		if (replaceItem > -1) {
			c.inventory[slot] = replaceItem + 1;
			c.getItems().resetItems(3214);
		}
		c.level[5] += c.getPA().getLevelForXP(c.xp[5]) * .33;
		if (rest) {
			c.level[5] += 1;
		}
		if (c.level[5] > c.getPA().getLevelForXP(c.xp[5])) {
			c.level[5] = c.getPA().getLevelForXP(c.xp[5]);
		}
		c.getPA().refreshSkill(5);
		if (rest) {
			this.restoreStats();
		}
	}

	public void drinkSummoningPot(int replaceItem, int slot) {
		c.startAnimation(829);
		c.inventory[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);

		int actualLevel = c.getPA().getActualLevel(PlayerConstants.SUMMONING);

		c.level[PlayerConstants.SUMMONING] += 7 + (int) Math.round(actualLevel * 0.25);
		if (c.level[PlayerConstants.SUMMONING] > actualLevel) {
			c.level[PlayerConstants.SUMMONING] = actualLevel;
		}
		c.getPA().refreshSkill(PlayerConstants.SUMMONING);
	}

	public void restoreStats() {

		for (int skill = 0; skill < c.level.length; skill++) {
			if (skill == PlayerConstants.HITPOINTS || skill == PlayerConstants.PRAYER) {
				continue;
			}

			int actualLevel = c.getPA().getActualLevel(skill);

			if (c.level[skill] < actualLevel) {
				c.level[skill] += actualLevel * .33;
				if (c.level[skill] > actualLevel) {
					c.level[skill] = actualLevel;
				}
				c.getPA().refreshSkill(skill);
			}
		}
	}

	public void doTheBrew(int itemId, int replaceItem, int slot) {
		c.startAnimation(829);

		if (replaceItem == -1) {
			c.inventory[slot] = 0;
			c.inventoryN[slot] = 0;
			c.getItems().resetItems(3214);
		}

		if (replaceItem > -1) {
			c.inventory[slot] = replaceItem + 1;
			c.getItems().resetItems(3214);
		}
		int[] toDecrease = { 0, 2, 4, 6 };
		// int[] toIncrease = {1, 3};
		if (!c.isOverloaded()) {
			for (int tD : toDecrease) {
				c.level[tD] -= this.getBrewStat(tD, .10);
				if (c.level[tD] < 0) {
					c.level[tD] = 1;
				}
				c.getPA().refreshSkill(tD);
			}

			c.level[1] += this.getBrewStat(1, .20);
			if (c.level[1] > (c.getPA().getLevelForXP(c.xp[1]) * 1.2 + 1)) {
				c.level[1] = (int) (c.getPA().getLevelForXP(c.xp[1]) * 1.2);
			}
			c.getPA().refreshSkill(1);
		}
		int boost = Food.getHpBoost(c);

		c.level[3] += this.getBrewStat(3, .15);
		if (c.level[3] > (c.getPA().getLevelForXP(c.xp[3]) * 1.17 + 1 + boost)) {
			c.level[3] = (int) (c.getPA().getLevelForXP(c.xp[3]) * 1.17 + boost);
		}
		c.getPA().refreshSkill(3);
	}

	public void enchanceStat(int skillID, boolean sup) {
		c.level[skillID] += this.getBoostedStat(skillID, sup);
		c.getPA().refreshSkill(skillID);
	}

	public int getBrewStat(int skill, double amount) {
		return (int) (c.getPA().getLevelForXP(c.xp[skill]) * amount);
	}

	public int getBoostedStat(int skill, boolean sup) {
		int increaseBy = 0;
		if (sup) {
			increaseBy = (int) (c.getPA().getLevelForXP(c.xp[skill]) * .20);
		} else {
			increaseBy = (int) (c.getPA().getLevelForXP(c.xp[skill]) * .13) + 1;
		}
		if (c.level[skill] + increaseBy > c.getPA().getLevelForXP(c.xp[skill]) + increaseBy + 1) {
			return c.getPA().getLevelForXP(c.xp[skill]) + increaseBy - c.level[skill];
		}
		return increaseBy;
	}

	public boolean isPotion(int itemId) {
		String name = c.getItems().getItemName(itemId);
		return name.contains("(6)") || name.contains("(5)") || name.contains("(4)") || name.contains("(3)")
				|| name.contains("(2)") || name.contains("(1)");
	}

}