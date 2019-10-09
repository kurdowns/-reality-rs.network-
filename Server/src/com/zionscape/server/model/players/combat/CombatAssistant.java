package com.zionscape.server.model.players.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.zionscape.server.Config;
import com.zionscape.server.Server;
import com.zionscape.server.cache.Collision;
import com.zionscape.server.gamecycle.GameCycleTask;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.Food;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.content.minigames.DuelArena;
import com.zionscape.server.model.content.minigames.PestControl;
import com.zionscape.server.model.content.minigames.castlewars.CastleWars;
import com.zionscape.server.model.content.minigames.clanwars.ClanWars;
import com.zionscape.server.model.content.minigames.clanwars.Constants;
import com.zionscape.server.model.content.minigames.clanwars.War;
import com.zionscape.server.model.content.minigames.zombies.Game;
import com.zionscape.server.model.content.minigames.zombies.Zombies;
import com.zionscape.server.model.items.Blowpipe;
import com.zionscape.server.model.items.ItemDefinition;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.items.SerpentineHelmet;
import com.zionscape.server.model.items.TridentOfTheSeas;
import com.zionscape.server.model.items.TridentOfTheSwamp;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCAnimations;
import com.zionscape.server.model.npcs.NPCConfig;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.npcs.NpcDefinition;
import com.zionscape.server.model.npcs.combat.Nex;
import com.zionscape.server.model.players.Areas;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.combat.CombatHelper.CombatStyle;
import com.zionscape.server.model.players.skills.slayer.Slayer;
import com.zionscape.server.model.players.skills.slayer.Tasks;
import com.zionscape.server.model.players.skills.summoning.Summoning;
import com.zionscape.server.tick.Tick;
import com.zionscape.server.util.Misc;
import com.zionscape.server.world.ItemDrops;

public class CombatAssistant {

	public int aPrayer = 0;
	public boolean pItem = false;
	public int toDrain = 0;
	private Player c;

	public CombatAssistant(Player Player) {
		this.c = Player;
	}

	public void activateCurse(int i) {

		if (c.getDuel() != null && c.getDuel().getStage() == DuelArena.Stage.FIGHTING) {
			if (c.getDuel().getRules()[PlayerConstants.DUEL_PRAYER]) {
				c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
				c.sendMessage("Prayer has been disabled in this duel!");
				return;
			}
		}

		if (c.getAttribute("clan_war") != null) {
			War war = (War) c.getAttribute("clan_war");
			if (war.getCombatRules()[Constants.PRAYER_RULE]) {
				c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
				c.sendMessage("You may may not use prayers in this clan war.");
				return;
			}
		}

		if (c.playerPrayerBook != 1) {
			for (int p = 0; p < c.PRAYER.length; p++) { // reset prayer glows
				c.prayerActive[p] = false;
				c.getPA().sendFrame36(c.PRAYER_GLOW[p], 0);
			}
			for (int p = 0; p < c.CURSE_ID.length; p++) { // reset prayer glows
				c.curseActive[p] = false;
				c.getPA().sendFrame36(c.CURSE_GLOW[p], 0);
			}
			c.sendMessage("You are not on the right spellbook to activate this Curse");
			return;
		}
		if (c.level[3] <= 0 && i == 0) {
			if (!c.curseActive[0]) {
				c.getPA().sendFrame36(c.CURSE_GLOW[10], 0);
				c.sendMessage("You can't activate this Curse when you are dead!");
			}
			return;
		}
		if (i == 19 && c.level[1] < 30) {
			c.getPA().sendFrame36(c.CURSE_GLOW[i], 0);
			c.getPA().sendFrame126("You need a @blu@Defence level of 30 to use " + c.CURSE_NAME[i] + ".", 357);
			c.getPA().sendFrame126("Click here to continue", 358);
			c.getPA().sendFrame164(356);
			return;
		}
		int[] saps = { 1, 2, 3, 4 };
		int[] leeches = { 10, 11, 12, 13, 14, 15, 16 };
		int[] overheads = { 7, 8, 9, 17, 18 };
		if (c.level[5] > 0 || !Config.PRAYER_POINTS_REQUIRED) {
			if (c.getPA().getLevelForXP(c.xp[5]) >= c.CURSE_LEVEL_REQUIRED[i] || !Config.PRAYER_LEVEL_REQUIRED) {
				boolean headIcon = false;
				if (!c.curseActive[i]) {
					this.curseActivateGFX(i);
				}
				switch (i) {
				case 0:
					c.lastProtItem = System.currentTimeMillis();
					break;
				case 1:
				case 2:
				case 3:
				case 4: // Saps
					if (c.curseActive[i] == false) {
						for (int j = 0; j < leeches.length; j++) {
							if (leeches[j] != i) {
								c.curseActive[leeches[j]] = false;
								c.getPA().sendFrame36(c.CURSE_GLOW[leeches[j]], 0);
							}
						}
						if (c.curseActive[19] = true) {
							c.curseActive[19] = false;
							c.getPA().sendFrame36(c.CURSE_GLOW[19], 0);
						}
					}
					break;
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
				case 16: // Saps
					if (c.curseActive[i] == false) {
						for (int j = 0; j < saps.length; j++) {
							if (saps[j] != i) {
								c.curseActive[saps[j]] = false;
								c.getPA().sendFrame36(c.CURSE_GLOW[saps[j]], 0);
							}
						}
						if (c.curseActive[19] = true) {
							c.curseActive[19] = false;
							c.getPA().sendFrame36(c.CURSE_GLOW[19], 0);
						}
					}
					break;
				case 19:
					if (c.curseActive[i] == false) {
						for (int j = 0; j < leeches.length; j++) {
							if (leeches[j] != i) {
								c.curseActive[leeches[j]] = false;
								c.getPA().sendFrame36(c.CURSE_GLOW[leeches[j]], 0);
							}
						}
						for (int j = 0; j < saps.length; j++) {
							if (saps[j] != i) {
								c.curseActive[saps[j]] = false;
								c.getPA().sendFrame36(c.CURSE_GLOW[saps[j]], 0);
							}
						}
					}
					break;
				case 7:
				case 8:
				case 9:
					if (System.currentTimeMillis() - c.stopPrayerDelay < 10000) {
						c.sendMessage("You have been injured and can't use this prayer!");
						c.getPA().sendFrame36(c.CURSE_GLOW[7], 0);
						c.getPA().sendFrame36(c.CURSE_GLOW[8], 0);
						c.getPA().sendFrame36(c.CURSE_GLOW[9], 0);
						return;
					}
				case 17:
				case 18:
					headIcon = true;
					for (int j = 0; j < overheads.length; j++) {
						if (i != overheads[j]) {
							c.curseActive[overheads[j]] = false;
							c.getPA().sendFrame36(c.CURSE_GLOW[overheads[j]], 0);
						}
					}
					break;
				}
				if (i == 7) {
					c.protMageDelay = System.currentTimeMillis();
				} else if (i == 8) {
					c.protRangeDelay = System.currentTimeMillis();
				} else if (i == 9) {
					c.protMeleeDelay = System.currentTimeMillis();
				}
				if (!headIcon) {
					if (c.curseActive[i] == false) {
						c.curseActive[i] = true;
						c.getPA().sendFrame36(c.CURSE_GLOW[i], 1);
					} else {
						c.curseActive[i] = false;
						c.getPA().sendFrame36(c.CURSE_GLOW[i], 0);
					}
				} else {
					if (c.curseActive[i] == false) {
						c.curseActive[i] = true;
						c.getPA().sendFrame36(c.CURSE_GLOW[i], 1);
						c.headIcon = c.CURSE_HEAD_ICONS[i];
						c.getPA().requestUpdates();
					} else {
						c.curseActive[i] = false;
						c.getPA().sendFrame36(c.CURSE_GLOW[i], 0);
						c.headIcon = -1;
						c.getPA().requestUpdates();
					}
				}
			} else {
				c.getPA().sendFrame36(c.CURSE_GLOW[i], 0);
				c.getPA().sendFrame126("You need a @blu@Prayer level of " + c.CURSE_LEVEL_REQUIRED[i] + " to use "
						+ c.CURSE_NAME[i] + ".", 357);
				c.getPA().sendFrame126("Click here to continue", 358);
				c.getPA().sendFrame164(356);
			}
		} else {
			c.getPA().sendFrame36(c.CURSE_GLOW[i], 0);
			c.sendMessage("You have run out of prayer points!");
		}
	}

	public void activatePrayer(int i) {

		if (i == 26 && c.getActualLevel(PlayerConstants.DEFENCE) < 70) {
			c.sendMessage("This prayer requires level 70 defence.");
			return;
		}

		if (c.getDuel() != null && c.getDuel().getStage() == DuelArena.Stage.FIGHTING) {
			if (c.getDuel().getRules()[PlayerConstants.DUEL_PRAYER]) {
				c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
				c.sendMessage("Prayer has been disabled in this duel!");
				return;
			}
		}

		if (c.getAttribute("clan_war") != null) {
			War war = (War) c.getAttribute("clan_war");
			if (war.getCombatRules()[Constants.PRAYER_RULE]) {
				c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
				c.sendMessage("You may may not use prayers in this clan war.");
				return;
			}
		}

		if (c.playerPrayerBook != 0) {
			for (int p = 0; p < c.PRAYER.length; p++) { // reset prayer glows
				c.prayerActive[p] = false;
				c.getPA().sendFrame36(c.PRAYER_GLOW[p], 0);
			}
			for (int p = 0; p < c.CURSE_ID.length; p++) { // reset prayer glows
				c.curseActive[p] = false;
				c.getPA().sendFrame36(c.CURSE_GLOW[p], 0);
			}
			c.sendMessage("You aren't on the right spellbook to activate this Prayer");
			return;
		}
		if (c.level[3] <= 0 && i == 10) {
			if (!c.prayerActive[10]) {
				c.getPA().sendFrame36(c.PRAYER_GLOW[10], 0);
				c.sendMessage("You can't activate this Prayer when you are dead!");
			}
			return;
		}
		if (i == 24 && c.getPA().getLevelForXP(c.xp[1]) < 65) {
			c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
			c.sendMessage("You may not use this prayer yet.");
			return;
		}
		if (i == 25 && c.getPA().getLevelForXP(c.xp[1]) < 70) {
			c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
			c.sendMessage("You may not use this prayer yet.");
			return;
		}
		int[] defPray = { 0, 5, 13, 24, 25, 26, 27 };
		int[] strPray = { 1, 6, 14, 24, 25 };
		int[] atkPray = { 2, 7, 15, 24, 25 };
		int[] rangePray = { 3, 11, 19, 26 };
		int[] magePray = { 4, 12, 20, 27 };
		if (c.level[5] > 0 || !Config.PRAYER_POINTS_REQUIRED) {
			if (c.getPA().getLevelForXP(c.xp[5]) >= c.PRAYER_LEVEL_REQUIRED[i] || !Config.PRAYER_LEVEL_REQUIRED) {
				boolean headIcon = false;
				switch (i) {
				case 0:
				case 5:
				case 13:
					if (c.prayerActive[i] == false) {
						for (int j = 0; j < defPray.length; j++) {
							if (defPray[j] != i) {
								c.prayerActive[defPray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[defPray[j]], 0);
							}
						}
					}
					break;
				case 1:
				case 6:
				case 14:
					if (c.prayerActive[i] == false) {
						for (int j = 0; j < strPray.length; j++) {
							if (strPray[j] != i) {
								c.prayerActive[strPray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[strPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++) {
							if (rangePray[j] != i) {
								c.prayerActive[rangePray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++) {
							if (magePray[j] != i) {
								c.prayerActive[magePray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[magePray[j]], 0);
							}
						}
					}
					break;
				case 2:
				case 7:
				case 15:
					if (c.prayerActive[i] == false) {
						for (int j = 0; j < atkPray.length; j++) {
							if (atkPray[j] != i) {
								c.prayerActive[atkPray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[atkPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++) {
							if (rangePray[j] != i) {
								c.prayerActive[rangePray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++) {
							if (magePray[j] != i) {
								c.prayerActive[magePray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[magePray[j]], 0);
							}
						}
					}
					break;
				case 3:// range prays
				case 11:
				case 19:
					if (c.prayerActive[i] == false) {
						for (int j = 0; j < atkPray.length; j++) {
							if (atkPray[j] != i) {
								c.prayerActive[atkPray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[atkPray[j]], 0);
							}
						}
						for (int j = 0; j < strPray.length; j++) {
							if (strPray[j] != i) {
								c.prayerActive[strPray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[strPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++) {
							if (rangePray[j] != i) {
								c.prayerActive[rangePray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++) {
							if (magePray[j] != i) {
								c.prayerActive[magePray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[magePray[j]], 0);
							}
						}
					}
					break;
				case 4:
				case 12:
				case 20:
					if (c.prayerActive[i] == false) {
						for (int j = 0; j < atkPray.length; j++) {
							if (atkPray[j] != i) {
								c.prayerActive[atkPray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[atkPray[j]], 0);
							}
						}
						for (int j = 0; j < strPray.length; j++) {
							if (strPray[j] != i) {
								c.prayerActive[strPray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[strPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++) {
							if (rangePray[j] != i) {
								c.prayerActive[rangePray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++) {
							if (magePray[j] != i) {
								c.prayerActive[magePray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[magePray[j]], 0);
							}
						}
					}
					break;
				case 10:
					c.lastProtItem = System.currentTimeMillis();
					break;
				case 16:
				case 17:
				case 18:
					if (System.currentTimeMillis() - c.stopPrayerDelay < 10000) {
						c.sendMessage("You have been injured and can't use this prayer!");
						c.getPA().sendFrame36(c.PRAYER_GLOW[16], 0);
						c.getPA().sendFrame36(c.PRAYER_GLOW[17], 0);
						c.getPA().sendFrame36(c.PRAYER_GLOW[18], 0);
						return;
					}
					if (i == 16) {
						c.protMageDelay = System.currentTimeMillis();
					} else if (i == 17) {
						c.protRangeDelay = System.currentTimeMillis();
					} else if (i == 18) {
						c.protMeleeDelay = System.currentTimeMillis();
					}
				case 21:
				case 22:
				case 23:
					headIcon = true;
					for (int p = 16; p < 25; p++) {
						if (i != p && p != 19 && p != 20 && p != 24 && p != 25) {
							c.prayerActive[p] = false;
							c.getPA().sendFrame36(c.PRAYER_GLOW[p], 0);
						}
					}
					break;
				case 24:
				case 25:
					if (c.prayerActive[i] == false) {
						for (int j = 0; j < atkPray.length; j++) {
							if (atkPray[j] != i) {
								c.prayerActive[atkPray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[atkPray[j]], 0);
							}
						}
						for (int j = 0; j < strPray.length; j++) {
							if (strPray[j] != i) {
								c.prayerActive[strPray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[strPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++) {
							if (rangePray[j] != i) {
								c.prayerActive[rangePray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++) {
							if (magePray[j] != i) {
								c.prayerActive[magePray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[magePray[j]], 0);
							}
						}
						for (int j = 0; j < defPray.length; j++) {
							if (defPray[j] != i) {
								c.prayerActive[defPray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[defPray[j]], 0);
							}
						}
					}
					break;
				case 26: // rigour
					for (int j = 0; j < defPray.length; j++) {
						if (defPray[j] != i) {
							c.prayerActive[defPray[j]] = false;
							c.getPA().sendFrame36(c.PRAYER_GLOW[defPray[j]], 0);
						}
					}
					for (int j = 0; j < rangePray.length; j++) {
						if (rangePray[j] != i) {
							c.prayerActive[rangePray[j]] = false;
							c.getPA().sendFrame36(c.PRAYER_GLOW[rangePray[j]], 0);
						}
					}
					break;
				case 27: // augory
					for (int j = 0; j < defPray.length; j++) {
						if (defPray[j] != i) {
							c.prayerActive[defPray[j]] = false;
							c.getPA().sendFrame36(c.PRAYER_GLOW[defPray[j]], 0);
						}
					}
					for (int j = 0; j < magePray.length; j++) {
						if (magePray[j] != i) {
							c.prayerActive[magePray[j]] = false;
							c.getPA().sendFrame36(c.PRAYER_GLOW[magePray[j]], 0);
						}
					}
					break;
				}
				if (!headIcon) {
					if (c.prayerActive[i] == false) {
						c.prayerActive[i] = true;
						c.getPA().sendFrame36(c.PRAYER_GLOW[i], 1);
					} else {
						c.prayerActive[i] = false;
						c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
					}
				} else {
					if (c.prayerActive[i] == false) {
						c.prayerActive[i] = true;
						c.getPA().sendFrame36(c.PRAYER_GLOW[i], 1);
						c.headIcon = c.PRAYER_HEAD_ICONS[i];
						c.getPA().requestUpdates();
					} else {
						c.prayerActive[i] = false;
						c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
						c.headIcon = -1;
						c.getPA().requestUpdates();
					}
				}
			} else {
				c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
				c.getPA().sendFrame126("You need a @blu@Prayer level of " + c.PRAYER_LEVEL_REQUIRED[i] + " to use "
						+ c.PRAYER_NAME[i] + ".", 357);
				c.getPA().sendFrame126("Click here to continue", 358);
				c.getPA().sendFrame164(356);
			}
		} else {
			c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
			c.sendMessage("You have run out of prayer points!");
		}
	}

	public void activateSpecial(int weapon, int i, boolean npc) {
		if (npc) {
			if (NPCHandler.npcs[i] == null && c.npcIndex > 0) {
				return;
			}
		} else {
			if (PlayerHandler.players[i] == null && c.playerIndex > 0) {
				return;
			}
		} // done

		Achievements.progressMade(c, Achievements.Types.PERFORM_SPECIAL_ATTACK);

		c.doubleHit = false;
		c.specEffect = 0;
		c.projectileStage = 0;
		c.specMaxHitIncrease = 2;
		if (c.npcIndex > 0) {
			c.oldNpcIndex = i;
		} else if (c.playerIndex > 0) {
			c.oldPlayerIndex = i;
			PlayerHandler.players[i].underAttackBy = c.playerId;
			PlayerHandler.players[i].logoutDelay = System.currentTimeMillis();
			PlayerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
			PlayerHandler.players[i].killerId = c.playerId;
		}
		switch (weapon) {
		case 1305: // dragon long
			c.gfx100(2117);
			c.startAnimation(12033);
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			c.specAccuracy = 1.10;
			c.specDamage = 0.50;
			break;
		case 1215: // dragon daggers
		case 1231:
		case 5680:
		case 5698:
			c.gfx100(252);
			c.startAnimation(1062);
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			c.doubleHit = true;
			c.specAccuracy = 1.00;
			c.specDamage = 0.20;
			// c.specDamage = 1.05;
			break;
		case 10887:
			c.startAnimation(5870);
			c.specDamage = 1;
			c.specAccuracy = 2.0;
			c.gfx0(1027);
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			break;
		case 11730:
			c.gfx100(1194);
			c.startAnimation(11993);
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			c.doubleHit = true;
			c.ssSpec = true;
			c.specAccuracy = 1.30;
			break;
		case 13899:
		case 13901: // Vesta LongSword
			c.startAnimation(10502);
			if (c.rights == 3 && c.overpower) {
				c.specDamage = 1.25;
				c.specAccuracy = 1000;
			} else {
				c.specDamage = 1.24;
			}
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase() + 1);
			break;

		case 25053: // d hammer
			c.startAnimation(1378);
			c.gfx0(1292);
			c.specAccuracy = 2;
			c.specDamage = 1.5;
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase() + 1);

			if (!npc) {
				Player target = PlayerHandler.players[c.playerIndex];

				// reduce opponents defence by 30%
				if (target.level[PlayerConstants.DEFENCE] > 0) {
					int amount = (int) Math.round(target.level[PlayerConstants.DEFENCE] * 0.30);

					if (amount < 1) {
						amount = 1;
					}

					if (amount > target.level[PlayerConstants.DEFENCE]) {
						amount = target.level[PlayerConstants.DEFENCE];
					}

					target.level[PlayerConstants.DEFENCE] -= amount;
					target.getPA().refreshSkill(PlayerConstants.DEFENCE);
				}
			} else {
				NPC target = NPCHandler.npcs[i];
				if (target.attributeExists("d_hammer")) {
					c.sendMessage("The NPC's defence is already effected by this special attack.");
				} else {
					target.setAttribute("d_hammer", true);
					if (target.defence > 0) {
						int drain = (int) Math.ceil(target.defence * 0.30);
						if (drain > 0) {
							target.defence -= drain;
						}
					}
					c.sendMessage("The special attack lowers the NPC's defence by 30%.");
				}
			}
			break;
		case 13902: // Statius
		case 13904:
			c.startAnimation(10505);
			c.gfx0(1840);
			if (c.rights == 3 && c.overpower) {
				c.specDamage = 1.25;
				c.specAccuracy = 1000;
			} else {
				c.specDamage = 1.25;
				// c.specAccuracy = 1.5;
			}
			c.specEffect = 10;
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase() + 1);
			break;
		case 21371: // Vine Whip
			c.startAnimation(1658);
			applyVineDamage(i);
			c.specAccuracy = 1.25;
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			break;
		case 4151: // whip
		case 15441:
		case 15442:
		case 15443:
		case 15444:
		case 14661:
			if (NPCHandler.npcs[i] != null) {
				NPCHandler.npcs[i].gfx100(341);
			}
			c.specAccuracy = 1.10;
			c.startAnimation(1658);
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			break;
		case 13883: // Morrigan thrown axe
			c.usingRangeWeapon = true;
			c.rangeItemUsed = c.equipment[Player.playerWeapon];
			c.getItems().deleteEquipment();
			c.lastWeaponUsed = weapon;
			c.startAnimation(10501);
			c.gfx0(1836);
			c.specAccuracy = 2.0;
			c.specDamage = 1.20;
			c.hitDelay = 3;
			c.projectileStage = 1;
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			if (c.getCombatStyle() == CombatStyle.RAPID) {
				c.attackTimer--;
			}
			if (c.playerIndex > 0) {
				this.fireProjectilePlayer();
			} else if (c.npcIndex > 0) {
				this.fireProjectileNpc();
			}
			break;
		case 13879:
			c.doJavelinSpecial = true;
			c.usingRangeWeapon = true;
			c.rangeItemUsed = c.equipment[Player.playerWeapon];
			c.getItems().deleteEquipment();
			c.lastWeaponUsed = weapon;
			c.startAnimation(10504);
			c.gfx0(1838);
			c.specAccuracy = 2.0;
			c.specDamage = 1;
			c.hitDelay = 3;
			c.projectileStage = 1;
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			if (c.getCombatStyle() == CombatStyle.RAPID) {
				c.attackTimer--;
			}
			if (c.playerIndex > 0) {
				this.fireProjectilePlayer();
			} else if (c.npcIndex > 0) {
				this.fireProjectileNpc();
			}
			break;
		case 13905:
			c.startAnimation(10499);
			c.gfx0(1835);
			c.specAccuracy = 1.5;
			c.specDamage = 1.0;
			c.specEffect = 20;
			break;
		case 14484:
			c.startAnimation(10961);
			c.gfx0(1950);
			c.specAccuracy = 2.8;
			c.specDamage = 1.40;
			c.clawDamage = 0;
			if (c.playerIndex > 0) {
				Player o = PlayerHandler.players[c.playerIndex];
				if (Misc.random(this.calculateMeleeAttack()) > Misc.random(o.getCombat().calculateMeleeDefence(c))) {
					c.clawDamage = Misc.random(this.calculateMeleeMaxHit());
				}
				c.clawIndex = c.playerIndex;
				c.clawType = 1;
			} else if (c.npcIndex > 0) {
				NPC n = NPCHandler.npcs[c.npcIndex];
				if (Misc.random(this.calculateMeleeAttack()) > Misc.random(n.defence)) {
					c.clawDamage = Misc.random(this.calculateMeleeMaxHit());
				}
				c.clawIndex = c.npcIndex;
				c.clawType = 2;
			}
			c.doubleHit = true;
			c.usingClaws = true;
			c.specEffect = 5;
			c.specDamage = 1.40;
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			break;
		case 11694: // ags
			c.startAnimation(11989);
			c.specDamage = 1.50;
			c.specAccuracy = 1.75;
			c.gfx0(2113);
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			break;
		case 7158: // D2h
			c.startAnimation(3157);
			c.specDamage = 1.50;
			c.specAccuracy = 4.0;
			c.gfx0(559);
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			break;
		case 19784:
		case 19780:// korasi
			c.korasiSpec = true;
			c.specAccuracy = 1.40;
			c.specDamage = 1.25;
			c.startAnimation(14788);
			if (c.playerIndex > 0) {
				Player o = PlayerHandler.players[i];
				if (o != null)
					o.gfx100(1729);
			} else if (c.npcIndex > 0) {
				NPC npc2 = NPCHandler.npcs[i];
				if (npc2 != null)
					npc2.gfx100(1729);
			}
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			break;
		case 15241:
			c.usingBow = true;
			c.hcSpec = true;
			c.projectileStage = 1;
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			c.startAnimation(12152);
			c.gfx100(this.getRangeStartGFX());
			if (c.playerIndex > 0) {
				this.fireProjectilePlayer();
			} else if (c.npcIndex > 0) {
				this.fireProjectileNpc();
			}
			c.specAccuracy = 1.8;
			c.specDamage = 1.40;
			break;
		case 11700: // zgs
			if (NPCHandler.npcs[i] != null) {
				NPCHandler.npcs[i].gfx0(2104);
			}
			/*
			 * if(Server.playerHandler.players[i] != null) {
			 * Server.playerHandler.players[i].gfx0(2111); }
			 */
			if (c.rights == 3 && c.overpower) {
				c.specDamage = 1.5;
				c.specAccuracy = 1000;
			} else {
				c.specAccuracy = 5;
				c.specDamage = 0.1;
			}
			c.startAnimation(7070);
			c.gfx0(1221);
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			c.specEffect = 2;
			break;
		case 11696: // bgs
			c.startAnimation(11991);
			c.gfx0(2114);
			if (c.rights == 3 && c.overpower) {
				c.specDamage = 1.5;
				c.specAccuracy = 1000;
			} else {
				c.specDamage = 1.10;
				c.specAccuracy = 1.5;
			}
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			c.specEffect = 3;
			break;
		case 11698:// sgs
			c.startAnimation(7071);
			c.gfx0(2109);
			if (c.rights == 3 && c.overpower) {
				c.specDamage = 1.5;
				c.specAccuracy = 1000;
			} else {
				c.specAccuracy = 1.25;
			}
			c.specEffect = 4;
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			break;
		case 1249:
			c.startAnimation(1064);
			c.gfx100(253);
			if (c.playerIndex > 0) {

				// disable this special in a duel with no movement rule
				if (c.getDuel() != null && c.getDuel().getRules()[PlayerConstants.DUEL_MOVEMENT]) {
					return;
				}

				Player o = PlayerHandler.players[i];
				o.getPA().getSpeared(c.absX, c.absY);
			}
			break;
		case 3204: // d hally
			c.gfx100(282);
			c.startAnimation(1203);
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			if (NPCHandler.npcs[i] != null && c.npcIndex > 0) {
				if (!c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(), 1)) {
					c.doubleHit = true;
				}
			}
			if (PlayerHandler.players[i] != null && c.playerIndex > 0) {
				if (!c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[i].getX(),
						PlayerHandler.players[i].getY(), 1)) {
					c.doubleHit = true;
					c.delayedDamage2 = CombatHelper.getMeleeDamage(c, PlayerHandler.players[c.playerIndex]);
				}
			}
			break;
		case 4153: // maul
			if (c.respawnTimer <= 0) {
				c.startAnimation(1667);
				c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
				/*
				 * if (c.playerIndex > 0) gmaulPlayer(i); else gmaulNpc(i);
				 */
				c.gfx100(340);
			}
			break;
		case 4587: // dscimmy
			c.gfx100(2118);
			c.specEffect = 1;
			c.startAnimation(12031);
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			break;
		case 1434: // mace
			c.startAnimation(1060);
			c.gfx100(251);
			c.specMaxHitIncrease = 3;
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase()) + 1;
			c.specDamage = 0.50;
			c.specAccuracy = 1.15;
			break;
		case 859: // magic long
			c.usingBow = true;
			c.bowSpecShot = 3;
			c.rangeItemUsed = c.equipment[c.playerArrows];
			c.getItems().deleteArrow();
			c.lastWeaponUsed = weapon;
			c.startAnimation(426);
			c.gfx100(250);
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			c.projectileStage = 1;
			if (c.getCombatStyle() == CombatStyle.RAPID) {
				c.attackTimer--;
			}
			break;
		case Blowpipe.BLOWPIPE:
			c.usingBow = true;
			c.rangeItemUsed = c.equipment[c.playerArrows];
			c.specDamage = 1.5;
			c.specAccuracy = 2;
			c.getItems().deleteArrow();
			c.lastWeaponUsed = weapon;
			c.startAnimation(5061);
			c.gfx100(1043);
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			c.projectileStage = 1;
			if (c.getCombatStyle() == CombatStyle.RAPID) {
				c.attackTimer--;
			}
			c.setAttribute("zulrah_special", true);
			if (c.playerIndex > 0) {
				this.fireProjectilePlayer();
			} else if (c.npcIndex > 0) {
				this.fireProjectileNpc();
			}
			c.removeAttribute("zulrah_special");
			break;
		case 861: // magic short
			c.usingBow = true;
			c.bowSpecShot = 1;
			c.rangeItemUsed = c.equipment[c.playerArrows];
			c.getItems().deleteArrow();
			c.lastWeaponUsed = weapon;
			c.startAnimation(1074);
			c.hitDelay = 1;
			c.projectileStage = 1;
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			if (c.getCombatStyle() == CombatStyle.RAPID) {
				c.attackTimer--;
			}
			if (c.playerIndex > 0) {
				this.fireProjectilePlayer();
			} else if (c.npcIndex > 0) {
				this.fireProjectileNpc();
			}
			break;
		case 11235: // dark bow
			c.usingBow = true;
			c.dbowSpec = true;
			c.specEffect = 6;
			c.rangeItemUsed = c.equipment[c.playerArrows];
			c.getItems().deleteArrow();
			c.getItems().deleteArrow();
			c.lastWeaponUsed = weapon;
			c.hitDelay = 3;
			c.startAnimation(426);
			c.projectileStage = 1;
			c.gfx100(this.getRangeStartGFX());
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			if (c.getCombatStyle() == CombatStyle.RAPID) {
				c.attackTimer--;
			}
			if (c.playerIndex > 0) {
				this.fireProjectilePlayer();
			} else if (c.npcIndex > 0) {
				this.fireProjectileNpc();
			}
			c.specAccuracy = 1.75;
			c.specDamage = 1.50;
			break;
		case 20173: // zaryte bow spec
			c.usingBow = true;
			c.dbowSpec = true;
			c.specEffect = 6;
			c.lastWeaponUsed = weapon;
			c.hitDelay = 3;
			c.startAnimation(426);
			c.projectileStage = 1;
			c.gfx100(this.getRangeStartGFX());
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			if (c.getCombatStyle() == CombatStyle.RAPID) {
				c.attackTimer--;
			}
			if (c.playerIndex > 0) {
				this.fireProjectilePlayer();
			} else if (c.npcIndex > 0) {
				this.fireProjectileNpc();
			}
			c.specAccuracy = 1.5;
			c.specDamage = 1.0;
			break;
		}
		if (!c.usingSpecial) {
			c.doubleHit = false;
			return;
		}
		// this is spec damage
		if (c.playerIndex > 0) {
			c.delayedDamage = CombatHelper.getMeleeDamage(c, PlayerHandler.players[c.playerIndex]);
			c.delayedDamage2 = CombatHelper.getMeleeDamage(c, PlayerHandler.players[c.playerIndex]);
		} else {
			c.delayedDamage = Misc.random(c.getCombat().calculateMeleeMaxHit());
			c.delayedDamage2 = Misc.random(c.getCombat().calculateMeleeMaxHit());
		}

		if (c.korasiSpec) {
			int maxHit = c.getCombat().calculateMeleeMaxHit();

			c.delayedDamage = Misc.random((int) (maxHit * 0.5), (int) (maxHit * 1.5));
		}

		c.usingSpecial = false;
		c.getItems().updateSpecialBar();
	}

	public void addCharge() {
		if (c.equipment[c.playerShield] != 11283) {
			return;
		}
		c.dfsCount++;
		if (c.dfsCount > 40) {
			c.dfsCount = 40;
		}
	}

	public void appendMultiBarrage(int playerId, boolean splashed) {
		if (PlayerHandler.players[playerId] != null) {
			Player c2 = PlayerHandler.players[playerId];
			if (c2.isDead || c2.respawnTimer > 0) {
				return;
			}
			if (this.checkMultiBarrageReqs(playerId)) {
				c.barrageCount++;
				if (Misc.random(this.mageAtk()) > Misc.random(this.mageDef()) && !c.magicFailed) {
					if (this.getEndGfxHeight() == 100) { // end GFX
						c2.gfx100(Player.MAGIC_SPELLS[c.oldSpellId][5]);
					} else {
						c2.gfx0(Player.MAGIC_SPELLS[c.oldSpellId][5]);
					}
					int maxDamage = Player.MAGIC_SPELLS[c.oldSpellId][6];
					int damage = Misc.random(maxDamage); // search op barrage
					if (c.equipment[c.playerAmulet] == 18335 && c.equipment[Player.playerWeapon] != 15040) {
						maxDamage = (int) (Player.MAGIC_SPELLS[c.oldSpellId][6] * 1.35);
					}
					if (c.equipment[c.playerAmulet] != 18335 && c.equipment[Player.playerWeapon] == 15040) {
						maxDamage = (int) (Player.MAGIC_SPELLS[c.oldSpellId][6] * 1.20);
					}
					if (c.equipment[c.playerAmulet] == 18335 && c.equipment[Player.playerWeapon] == 15040) {
						maxDamage = (int) (Player.MAGIC_SPELLS[c.oldSpellId][6] * 1.35);
					}

					damage = Misc.random(maxDamage);
					if (c2.prayerActive[12] || c2.curseActive[7]) {
						damage *= (int) .60;
					}
					if (c2.level[3] - damage < 0) {
						damage = c2.level[3];
					}
					c.getPA().addSkillXP(Player.MAGIC_SPELLS[c.oldSpellId][7] + damage, 6);
					c.getPA().addSkillXP(Player.MAGIC_SPELLS[c.oldSpellId][7] + damage / 3, 3);
					// Server.playerHandler.players[playerId].setHitDiff(damage);
					// Server.playerHandler.players[playerId].setHitUpdateRequired(true);
					PlayerHandler.players[playerId].handleHitMask(damage, maxDamage, 3, playerId);
					// Server.playerHandler.players[playerId].level[3] -=
					// damage;
					PlayerHandler.players[playerId].dealDamage(damage);
					PlayerHandler.players[playerId].damageTaken[c.playerId] += damage;
					c2.getPA().refreshSkill(3);
					c.totalPlayerDamageDealt += damage;
					this.multiSpellEffect(playerId, damage);
				} else {
					c2.gfx100(85);
				}
			}
		}
	}

	public void appendMultiBarrageNPC(int npcId, boolean splashed) {
		if (NPCHandler.npcs[npcId] != null) {
			NPC n = (NPC) NPCHandler.npcs[npcId];
			if (n.isDead || n.type == 3782) {
				return;
			}

			if (this.checkMultiBarrageReqsNPC(npcId)) {
				c.barrageCount++;
				NPCHandler.npcs[npcId].underAttackBy = c.playerId;
				NPCHandler.npcs[npcId].underAttack = true;
				if (Misc.random(this.mageAtk()) > Misc.random(this.mageDef()) && !c.magicFailed) {
					if (this.getEndGfxHeight() == 100) { // end GFX
						n.gfx100(Player.MAGIC_SPELLS[c.oldSpellId][5]);
					} else {
						n.gfx0(Player.MAGIC_SPELLS[c.oldSpellId][5]);
					}
					int damage; // search op barrage
					int maxDamage = Player.MAGIC_SPELLS[c.oldSpellId][6];
					if (c.equipment[c.playerAmulet] == 18335 && c.equipment[Player.playerWeapon] != 15040) {
						maxDamage = (int) (Player.MAGIC_SPELLS[c.oldSpellId][6] * 1.35);
					}
					if (c.equipment[c.playerAmulet] != 18335 && c.equipment[Player.playerWeapon] == 15040) {
						maxDamage = (int) (Player.MAGIC_SPELLS[c.oldSpellId][6] * 1.20);
					}
					if (c.equipment[c.playerAmulet] == 18335 && c.equipment[Player.playerWeapon] == 15040) {
						maxDamage = (int) (Player.MAGIC_SPELLS[c.oldSpellId][6] * 1.35);
					}
					damage = (int) Misc.random(maxDamage);
					if (NPCHandler.npcs[npcId].getHP() - damage < 0) {
						damage = NPCHandler.npcs[npcId].getHP();
					}

					NPCHandler.npcs[npcId].dealdamage(c.getDatabaseId(), damage);

					c.getPA().addSkillXP(Player.MAGIC_SPELLS[c.oldSpellId][7] + damage, 6);
					// c.made += (c.MAGIC_SPELLS[c.oldSpellId][7] +
					// damage*Config.MAGIC_EXP_RATE*c.getPA().expMultiplier()) -
					// ;
					c.getPA().addSkillXP(Player.MAGIC_SPELLS[c.oldSpellId][7] + damage / 3, 3);
					NPCHandler.npcs[npcId].handleHitMask(damage, maxDamage, 3, npcId);
					// NPCHandler.npcs[npcId].dealDamage(damage);
					c.totalPlayerDamageDealt += damage;
					this.multiSpellEffectNPC(npcId, damage);
				} else {

					// disable splash on zulrah
					if (n.type < 2042 && n.type > 2045) {

						n.gfx100(85);
					}
				}
			}
		}
	}

	public void appendVengeance(int otherPlayer, int damage) {
		if (damage <= 0) {
			return;
		}
		Player o = PlayerHandler.players[otherPlayer];
		o.forcedText = "Taste Vengeance!";
		o.forcedChatUpdateRequired = true;
		o.updateRequired = true;
		o.vengOn = false;
		if ((o.level[3] - damage) > 0) {
			damage = (int) (damage * 0.75);
			if (damage > c.level[3]) {
				damage = c.level[3];
			}

			c.handleHitMask(damage, 100, 2, 0);

			// c.setHitDiff2(damage);
			// c.setHitUpdateRequired2(true);
			c.level[3] -= damage;
			c.getPA().refreshSkill(3);
		}
		c.updateRequired = true;
	}

	public void appendVengeanceNpc(int i, int damage) {
		if (damage <= 0) {
			return;
		}
		NPC o = NPCHandler.npcs[i];
		c.forcedText = "Taste Vengeance!";
		c.forcedChatUpdateRequired = true;
		c.updateRequired = true;
		c.vengOn = false;
		if ((o.getHP() - damage) > 0) {
			damage = (int) (damage * 0.75);
			NPCHandler.npcs[i].dealdamage(c.getDatabaseId(), damage);
			NPCHandler.npcs[i].hitUpdateRequired = true;
		}
		c.updateRequired = true;
	}

	public void applyAttackLeech(int index) {
		/*
		 * if (c.attackleech == false || c.defenceleech == false || c.strengthleech ==
		 * false || c.magicleech == false || c.rangedleech == false) return;
		 */
		if (PlayerHandler.players[index] != null) {
			Player c2 = PlayerHandler.players[index];
			c2.getPA().refreshSkill(0);
			c2.getPA().refreshSkill(1);
			c2.getPA().refreshSkill(2);
			c2.getPA().refreshSkill(3);
			c2.getPA().refreshSkill(4);
			c2.getPA().refreshSkill(5);
			c2.sendMessage("your stats wer drained...");
			c.startAnimation(4004);
			int r = Misc.random(3);
			switch (r) {
			case 1:
				c.gfx100(910);
				break;
			case 2:
				c.gfx100(911);
				break;
			case 3:
				c.gfx100(912);
				break;
			}
			if (c.attackleech) {
				c2.level[0] -= Misc.random(1);
				// c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 80,
				// 91, 43, 31, - c.oldPlayerIndex - 1, 0);
				if (c2.level[5] <= 0) {
					c2.level[5] = 0;
					c2.getCombat().resetPrayers();
				}
			}
			if (c.defenceleech) {
				c2.level[1] -= Misc.random(1);
				// c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 80,
				// 91, 43, 31, - c.oldPlayerIndex - 1, 0);
				if (c2.level[5] <= 0) {
					c2.level[5] = 0;
					c2.getCombat().resetPrayers();
				}
			}
			if (c.strengthleech) {
				c2.level[2] -= Misc.random(1);
				// c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 80,
				// 91, 43, 31, - c.oldPlayerIndex - 1, 0);
				if (c2.level[5] <= 0) {
					c2.level[5] = 0;
					c2.getCombat().resetPrayers();
				}
			}
			if (c.magicleech) {
				c2.level[6] -= Misc.random(1);
				// c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 80,
				// 91, 43, 31, - c.oldPlayerIndex - 1, 0);
				if (c2.level[5] <= 0) {
					c2.level[5] = 0;
					c2.getCombat().resetPrayers();
				}
			}
			if (c.rangedleech) {
				c2.level[4] -= Misc.random(1);
				// c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 80,
				// 91, 43, 31, - c.oldPlayerIndex - 1, 0);
				if (c2.level[5] <= 0) {
					c2.level[5] = 0;
					c2.getCombat().resetPrayers();
				}
			}
		}
	}

	public void applyLeechAttack(int index, int damage) {
		try {
			/*
			 * if(!c.username.equalsIgnoreCase("nightmare")) return;
			 */
			if (!c.curseActive[10]) {
				return;
			}
			if (c.level[0] > (c.getPA().getLevelForXP(c.xp[0]) + (c.getPA().getLevelForXP(c.xp[0]) / 15))) {
				return;
			}
			if (PlayerHandler.players[index] != null) {
				final Player c2 = PlayerHandler.players[index];
				final int pX = c.getX();
				final int pY = c.getY();
				final int oX = c2.getX();
				final int oY = c2.getY();
				final int offX = (pY - oY) * -1;
				final int offY = (pX - oX) * -1;
				if (damage > 0 && (c2.level[0] < (c2.getPA().getLevelForXP(c2.xp[0])
						+ (c2.getPA().getLevelForXP(c2.xp[0]) / 20)))) {
					int deduct = c.getPA().getLevelForXP(c.xp[0]) / (Misc.random(15) + 10);
					c2.level[0] -= deduct;
					c.level[0] += 1;
					c.leechAttackTimer += Misc.random(15) + 5;
					if (c2.level[0] <= 0) {
						c2.level[0] = 0;
					}
				}
				c.getPA().refreshSkill(0);
				c2.getPA().refreshSkill(0);
				c.leechAttackDelay = 16;
				Server.getTickManager().submit(new Tick(1) {

					@Override
					public void execute() {
						if (c == null || c2 == null || c.isDisconnected() || c2.isDisconnected()) {
							this.stop();
							return;
						}
						if (c.leechAttackDelay > 0) {
							c.leechAttackDelay--;
						}
						if (c.leechAttackDelay == 7) {
							c.startAnimation(12575);
						}
						if (c.leechAttackDelay == 6) {
							c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2231, 31, 31,
									-c.oldPlayerIndex - 1, 0);
						}
						// c.sendMessage("Started the event");
						if (c.leechAttackDelay == 4) {
							c2.gfx0(2233);
						}
						// c.sendMessage("Started anim & gfx");
						if (c.leechAttackDelay == 3) {
							c2.gfx0(2232);
						}
						if (c.leechAttackDelay == 0) {
							this.stop();
						}
					}
				});
			}
		} catch (Exception e) {
			System.out.println("Failed curse line 4771");
			e.printStackTrace();
		}
	}

	public void applyLeechDefence(int index, int damage) {
		/*
		 * if(!c.username.equalsIgnoreCase("nightmare")) return;
		 */
		try {
			if (!c.curseActive[13]) {
				return;
			}
			if (c.level[1] > (c.getPA().getLevelForXP(c.xp[1]) + (c.getPA().getLevelForXP(c.xp[1]) / 15))) {
				return;
			}
			if (PlayerHandler.players[index] != null) {
				final Player c2 = PlayerHandler.players[index];
				final int pX = c.getX();
				final int pY = c.getY();
				final int oX = c2.getX();
				final int oY = c2.getY();
				final int offX = (pY - oY) * -1;
				final int offY = (pX - oX) * -1;
				if (damage > 0 && (c2.level[1] < (c2.getPA().getLevelForXP(c2.xp[1])
						+ (c2.getPA().getLevelForXP(c2.xp[1]) / 20)))) {
					int deduct = c.getPA().getLevelForXP(c.xp[1]) / (Misc.random(15) + 10);
					c2.level[1] -= deduct;
					c.level[1] += 1;
					if (c2.level[1] <= 0) {
						c2.level[1] = 0;
					}
				}
				c.getPA().refreshSkill(1);
				c2.getPA().refreshSkill(1);
				c.leechDefenceDelay = 16;
				Server.getTickManager().submit(new Tick(1) {

					@Override
					public void execute() {
						if (c == null || c2 == null || c.isDisconnected() || c2.isDisconnected()) {
							this.stop();
							return;
						}
						if (c.leechDefenceDelay > 0) {
							c.leechDefenceDelay--;
						}
						if (c.leechDefenceDelay == 7) {
							c.startAnimation(12575);
						}
						if (c.leechDefenceDelay == 6) {
							c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2244, 31, 31,
									-c.oldPlayerIndex - 1, 0);
						}
						// c.sendMessage("Started the event");
						if (c.leechDefenceDelay == 4) {
							c2.gfx0(2245);
						}
						// c.sendMessage("Started anim & gfx");
						if (c.leechDefenceDelay == 3) {
							c2.gfx0(2246);
						}
						if (c.leechDefenceDelay == 0) {
							this.stop();
						}
					}
				});
			}
		} catch (Exception e) {
			System.out.println("Failed curse line 4830");
			e.printStackTrace();
		}
	}

	public void applyLeechMagic(int index, int damage) {
		try {
			if (!c.curseActive[12]) {
				return;
			}
			if (c.level[6] > (c.getPA().getLevelForXP(c.xp[6]) + (c.getPA().getLevelForXP(c.xp[6]) / 15))) {
				return;
			}
			if (PlayerHandler.players[index] != null) {
				final Player c2 = PlayerHandler.players[index];
				final int pX = c.getX();
				final int pY = c.getY();
				final int oX = c2.getX();
				final int oY = c2.getY();
				final int offX = (pY - oY) * -1;
				final int offY = (pX - oX) * -1;
				if (damage > 0 && (c2.level[6] < (c2.getPA().getLevelForXP(c2.xp[6])
						+ (c2.getPA().getLevelForXP(c2.xp[6]) / 20)))) {
					int deduct = c.getPA().getLevelForXP(c.xp[6]) / (Misc.random(15) + 10);
					c2.level[6] -= deduct;
					c.level[6] += 1;
					c.leechAttackTimer += Misc.random(15) + 5;
					if (c2.level[6] <= 0) {
						c2.level[6] = 0;
					}
				}
				c.getPA().refreshSkill(6);
				c2.getPA().refreshSkill(6);
				c.leechMagicDelay = 16;
				Server.getTickManager().submit(new Tick(1) {

					@Override
					public void execute() {
						if (c == null || c2 == null || c.isDisconnected() || c2.isDisconnected()) {
							this.stop();
							return;
						}
						if (c.leechMagicDelay > 0) {
							c.leechMagicDelay--;
						}
						if (c.leechMagicDelay == 7) {
							c.startAnimation(12575);
						}
						if (c.leechMagicDelay == 6) {
							c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2240, 31, 31,
									-c.oldPlayerIndex - 1, 0);
						}
						// c.sendMessage("Started the event");
						if (c.leechMagicDelay == 4) {
							c2.gfx0(2241);
						}
						// c.sendMessage("Started anim & gfx");
						if (c.leechMagicDelay == 3) {
							c2.gfx0(2242);
						}
						if (c.leechMagicDelay == 0) {
							this.stop();
						}
					}
				});
			}
		} catch (Exception e) {
			System.out.println("Failed curse line 5007");
			e.printStackTrace();
		}
	}

	public void applyLeechSpecial(int index, int damage) {
		try {
			/*
			 * if(!c.username.equalsIgnoreCase("nightmare")) return;
			 */
			if (!c.curseActive[16]) {
				return;
			}
			if (c.specAmount > 9.0) {
				c.sendMessage("Your special attack is already too full to take more!");
				return;
			}
			if (PlayerHandler.players[index] != null) {
				final Player c2 = PlayerHandler.players[index];
				final int pX = c.getX();
				final int pY = c.getY();
				final int oX = c2.getX();
				final int oY = c2.getY();
				final int offX = (pY - oY) * -1;
				final int offY = (pX - oX) * -1;
				if (damage > 0) {
					double deduct = 1.0;
					if (c2.specAmount >= 1.0) {
						c2.specAmount -= deduct;
						c.specAmount += deduct;
					} else {
						c.sendMessage(c2.username + " doesn't have enough special attack energy!");
						return;
					}
				}
				c.leechSpecialDelay = 16;
				Server.getTickManager().submit(new Tick(1) {

					@Override
					public void execute() {
						if (c == null || c2 == null || c.isDisconnected() || c2.isDisconnected()) {
							this.stop();
							return;
						}
						if (c.leechSpecialDelay > 0) {
							c.leechSpecialDelay--;
						}
						if (c.leechSpecialDelay == 7) {
							c.startAnimation(12575);
						}
						if (c.leechSpecialDelay == 6) {
							c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2256, 31, 31,
									-c.oldPlayerIndex - 1, 0);
						}
						// c.sendMessage("Started the event");
						if (c.leechSpecialDelay == 4) {
							c2.gfx0(2257);
						}
						// c.sendMessage("Started anim & gfx");
						if (c.leechSpecialDelay == 3) {
							c2.gfx0(2258);
						}
						if (c.leechSpecialDelay == 0) {
							this.stop();
						}
					}
				});
			}
		} catch (Exception e) {
			System.out.println("Failed curse line 4948");
			e.printStackTrace();
		}
	}

	public void applyLeechStrength(int index, int damage) {
		/*
		 * if(!c.username.equalsIgnoreCase("nightmare")) return;
		 */
		try {
			if (!c.curseActive[14]) {
				return;
			}
			if (c.level[2] > (c.getPA().getLevelForXP(c.xp[2]) + (c.getPA().getLevelForXP(c.xp[2]) / 15))) {
				return;
			}
			if (PlayerHandler.players[index] != null) {
				final Player c2 = PlayerHandler.players[index];
				final int pX = c.getX();
				final int pY = c.getY();
				final int oX = c2.getX();
				final int oY = c2.getY();
				final int offX = (pY - oY) * -1;
				final int offY = (pX - oX) * -1;
				if (damage > 0 && (c2.level[2] < (c2.getPA().getLevelForXP(c2.xp[2])
						+ (c2.getPA().getLevelForXP(c2.xp[2]) / 20)))) {
					int deduct = c.getPA().getLevelForXP(c.xp[2]) / (Misc.random(15) + 10);
					c2.level[2] -= deduct;
					c.level[2] += 1;
					if (c2.level[2] <= 0) {
						c2.level[2] = 0;
					}
				}
				c.getPA().refreshSkill(2);
				c2.getPA().refreshSkill(2);
				c.leechDefenceDelay = 16;
				Server.getTickManager().submit(new Tick(1) {

					@Override
					public void execute() {
						if (c == null || c2 == null || c.isDisconnected() || c2.isDisconnected()) {
							this.stop();
							return;
						}
						if (c.leechDefenceDelay > 0) {
							c.leechDefenceDelay--;
						}
						if (c.leechDefenceDelay == 7) {
							c.startAnimation(12575);
						}
						if (c.leechDefenceDelay == 6) {
							c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2248, 31, 31,
									-c.oldPlayerIndex - 1, 0);
						}
						// c.sendMessage("Started the event");
						if (c.leechDefenceDelay == 4) {
							c2.gfx0(2249);
						}
						// c.sendMessage("Started anim & gfx");
						if (c.leechDefenceDelay == 3) {
							c2.gfx0(2250);
						}
						if (c.leechDefenceDelay == 0) {
							this.stop();
						}
					}
				});
			}
		} catch (Exception e) {
			System.out.println("Failed curse line 4889");
			e.printStackTrace();
		}
	}

	public void applyMageDamage(int damage) {
		int i = c.playerIndex;
		c.previousDamage = damage;
		Player o = PlayerHandler.players[i];
		if (o == null) {
			return;
		}
		if (o.vengOn && damage > 0) {
			this.appendVengeance(i, damage);
		}
		int xp = damage * 4;
		if (c.getCombatStyle() == CombatStyle.CONTROLLED) {
			c.getPA().addSkillXP(xp / 3, 0);
			c.getPA().addSkillXP(xp / 3, 1);
			c.getPA().addSkillXP(xp / 3, 2);
			c.getPA().addSkillXP(xp / 3, 3);
		} else {
			c.getPA().addSkillXP(xp, 6);
			c.getPA().addSkillXP(xp / 3, 3);
		}
		PlayerHandler.players[i].logoutDelay = System.currentTimeMillis();
		PlayerHandler.players[i].underAttackBy = c.playerId;
		PlayerHandler.players[i].killerId = c.playerId;
		PlayerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
		if (c.killedBy != PlayerHandler.players[i].playerId) {
			c.totalPlayerDamageDealt = 0;
		}
		c.killedBy = PlayerHandler.players[i].playerId;
		this.applySmite(i, damage);
		PlayerHandler.players[i].dealDamage(damage);
		PlayerHandler.players[i].damageTaken[c.playerId] += damage;
		c.totalPlayerDamageDealt += damage;
		PlayerHandler.players[i].updateRequired = true;
		o.getPA().refreshSkill(3);
		PlayerHandler.players[i].handleHitMask(damage, i, i, i);
	}

	public void applyNpcMeleeDamage(int i, int damageMask, int damage) {
		if (NPCHandler.npcs[i] == null) {
			return;
		}

		NPC npc = NPCHandler.npcs[i];

		int maxHit = this.calculateMeleeMaxHit();

		NpcDefinition definition = NPCHandler.getNPCDefinition(npc.type);
		if (definition.getWeakness().equals(Weaknesses.MELEE)) {
			maxHit *= 1.15;
		}

		c.previousDamage = damage;
		boolean fullVeracsEffect = c.getPA().fullVeracs() && Misc.random(3) == 1;
		if (NPCHandler.npcs[i].getHP() - damage < 0) {
			damage = NPCHandler.npcs[i].getHP();
		}
		/*
		 * if (c.soulSplitDelay <= 0) { //npc soulsplit? applySoulSplit(i, damage); }
		 */
		if (c.vengOn) {
			this.appendVengeanceNpc(i, damage);
		}
		if (!fullVeracsEffect) {
			if (Misc.random(NPCHandler.npcs[i].defence) > 10 + Misc.random(this.calculateMeleeAttack())) {
				damage = 0;
			} else if (NPCHandler.npcs[i].type == 2882 || NPCHandler.npcs[i].type == 2883) {
				damage = 0;
			}
		}

		boolean guthansEffect = false;
		if (c.getPA().fullGuthans()) {
			if (Misc.random(3) == 1) {
				guthansEffect = true;
			}
		}

		if (npc.getCombat() != null && damage > 0) {
			damage = npc.getCombat().damageDealtByPlayer(c, damage);
		}

		// slayer melee
		if (Slayer.isFightingSlaverNPC(c, npc, Tasks.GARGOYLE)) {
			if (!c.getItems().playerHasItem(4162)) {
				if (damage >= npc.getHP()) {
					damage = 0;
				}
			}
		}
		if (Slayer.isFightingSlaverNPC(c, npc, Tasks.TUROTH) || Slayer.isFightingSlaverNPC(c, npc, Tasks.KURASK)) {
			if (c.equipment[PlayerConstants.WEAPON] != 13290 && c.equipment[PlayerConstants.WEAPON] != 4158) {
				damage = 0;
			}
		}
		if (Slayer.isFightingSlaverNPC(c, npc, Tasks.ROCKSLUG) && npc.getHP() <= 25) {
			if (!c.getItems().playerHasItem(4161)) {
				damage = 0;
			} else {
				c.getItems().deleteItem(4161, 1);
				damage = npc.getHP();
			}
		}

		// glacor damage
		if (npc.type == 14301) {
			damage = (int) (damage * 0.10);
		}

		if (Zombies.inGameArea(c.getLocation())) {
			Game game = (Game) c.getAttribute("zombie_game");
			if (game != null && game.instantKill()) {
				damage = npc.getHP();
			}
		}

		if (damage > 0) {

			if (c.soulSplitDelay <= 0) {
				this.applySoulSplitNPC(i, damage);
			}

			if (c.inPestControl) {
				PestControl.dealtNpcDamage(c, damage);
			}

			int xp = damage * 4;
			if (c.getCombatStyle() == CombatStyle.CONTROLLED) {
				c.getPA().addSkillXP(xp / 3, 0);
				c.getPA().addSkillXP(xp / 3, 1);
				c.getPA().addSkillXP(xp / 3, 2);
				c.getPA().addSkillXP(xp / 3, 3);
			} else {
				c.getPA().addSkillXP(xp, c.getCombatStyle().getSkill());
				c.getPA().addSkillXP(xp / 3, 3);
			}
		}

		if (damage > 50) {
			Achievements.progressMade(c, Achievements.Types.HIT_500_PLUS);
		}

		if (damage > 0) {
			if (NPCHandler.npcs[i].type >= 6142 && NPCHandler.npcs[i].type <= 6145) {
				c.pcDamage += damage;
			}
		}
		if (damage > 0 && guthansEffect) {
			c.level[3] += damage;
			if (c.level[3] > c.getPA().getLevelForXP(c.xp[3])) {
				c.level[3] = c.getPA().getLevelForXP(c.xp[3]);
			}
			c.getPA().refreshSkill(3);
			NPCHandler.npcs[i].gfx0(398);
		}
		NPCHandler.npcs[i].underAttack = true;
		// NPCHandler.npcs[i].killerId = c.playerId;
		c.killingNpcIndex = c.npcIndex;
		switch (c.specEffect) {
		case 4:
			/**
			 * he Saradomin Godsword has a Special attack called Healing Blade. When used,
			 * the player's max hit is increased by 10%, and half (rounded down) of the
			 * damage dealt by the sword is restored to the player's hitpoints, and 1/4 of
			 * the damage is restored to the player's Prayer. This drains 50% of the special
			 * attack bar. Note that the sword's special attack must hit to have the effect.
			 * There is a minimum restoration of 10 hitpoints and 5 Prayer points, even with
			 * a hit of 1.
			 */

			if (damage > 0) {

				int heal = damage / 2;
				int prayer = heal / 4;

				if (heal < 10) {
					heal = 10;
				}
				if (prayer < 5) {
					prayer = 5;
				}

				int actualHitpointLevel = c.getPA().getActualLevel(PlayerConstants.HITPOINTS);
				if (c.level[PlayerConstants.HITPOINTS] < actualHitpointLevel) {
					if ((c.level[PlayerConstants.HITPOINTS] + heal) > actualHitpointLevel) {
						c.level[PlayerConstants.HITPOINTS] = actualHitpointLevel;
					} else {
						c.level[PlayerConstants.HITPOINTS] += heal;
					}
					c.getPA().refreshSkill(PlayerConstants.HITPOINTS);
				}

				int actualPrayerLevel = c.getPA().getActualLevel(PlayerConstants.PRAYER);
				if (c.level[PlayerConstants.PRAYER] < actualPrayerLevel) {
					if ((c.level[PlayerConstants.PRAYER] + prayer) > actualPrayerLevel) {
						c.level[PlayerConstants.PRAYER] = actualPrayerLevel;
					} else {
						c.level[PlayerConstants.PRAYER] += prayer;
					}
					c.getPA().refreshSkill(PlayerConstants.PRAYER);
				}
			}
			break;
		case 5:
			c.clawDelay = 2;
			// c.clawDamage = Misc.random(calculateMeleeMaxHit());
			break;
		case 6:
			c.bowDelay = 2;
			break;
		}
		if (c.curseActive[18]) {// brk ownt
			if (NPCHandler.npcs[c.oldNpcIndex] != null && c.oldNpcIndex > 0) {
				try {
					final int nX = NPCHandler.npcs[c.oldNpcIndex].getX();
					final int nY = NPCHandler.npcs[c.oldNpcIndex].getY();
					final int pX = c.getX();
					final int pY = c.getY();
					final int offX = (pY - nY) * -1;
					final int offY = (pX - nX) * -1;
					if (c.curseActive[18] && !c.prayerActive[23] && c.level[3] <= 99) {
						int heal = (damage * 4) / 10;
						if (c.level[3] + heal >= c.getPA().getLevelForXP(c.xp[3])) {
							c.level[3] = c.getPA().getLevelForXP(c.xp[3]);
						} else {
							c.level[3] += heal;
						}
						c.getPA().refreshSkill(3);
					}
					c.getPA().createPlayersProjectile2(c.getX(), c.getY(), offX, offY, 50, 50, 2263, 9, 9,
							c.oldNpcIndex + 1, 24, 0);
					Server.getTickManager().submit(new Tick(1) {

						@Override
						public void execute() {
							if (!c.isDisconnected()) {
								if (NPCHandler.npcs[c.oldNpcIndex] != null) {
									NPCHandler.npcs[c.oldNpcIndex].gfx0(2264);
								}
							}
							this.stop();
						}
					});
					Server.getTickManager().submit(new Tick(2) {

						@Override
						public void execute() {
							if (!c.isDisconnected()) {
								if (c != null && NPCHandler.npcs[c.oldNpcIndex] != null) {
									c.getPA().createPlayersProjectile2(nX, nY, offX, offY, 50, 50, 2263, 9, 9,
											-c.playerId - 1, 24, 0);
								}
							}
							this.stop();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		int damageType = 1;
		if (c.korasiSpec) {
			damageType = 3;
			NPCHandler.npcs[i].gfx100(1730);
		}
		switch (damageMask) {
		case 1:
			NPCHandler.npcs[i].dealdamage(c.getDatabaseId(), damage);
			NPCHandler.npcs[i].handleHitMask(damage, maxHit, damageType, i);
			c.totalPlayerDamageDealt += damage;
			break;
		case 2:
			NPCHandler.npcs[i].dealdamage(c.getDatabaseId(), damage);
			NPCHandler.npcs[i].handleHitMask(damage, maxHit, damageType, i);
			c.totalPlayerDamageDealt += damage;
			c.doubleHit = false;
			break;
		}
		c.korasiSpec = false;
	}

	public void applyPlayerMeleeDamage(int i, int damageMask, int damage) {
		int maxDamage = this.calculateMeleeMaxHit();
		c.previousDamage = damage;
		Player o = PlayerHandler.players[i];
		if (o == null) {
			return;
		}
		boolean veracsEffect = false;
		boolean guthansEffect = false;
		if (c.getPA().fullVeracs()) {
			if (Misc.random(4) == 1) {
				veracsEffect = true;
			}
		}
		if (c.getPA().fullGuthans()) {
			if (Misc.random(4) == 1) {
				guthansEffect = true;
			}
		}
		if (!c.usingClaws) {
			if (damageMask == 1) {
				damage = c.delayedDamage;
				c.delayedDamage = 0;
			} else {
				damage = c.delayedDamage2;
				c.delayedDamage2 = 0;
			}
		}
		if (c.equipment[Player.playerWeapon] == 5698) {
			if (o.poisonDamage <= 0 && Misc.random(3) == 1) {
				o.getPA().appendPoison(13);
			}
			c.bonusAttack += damage / 3;
		}
		if (o.equipment[Player.playerWeapon] == 15486 && damage >= 1 && o.SolProtect >= 1) {
			damage = (int) damage / 2;
			o.gfx0(2320);
		}
		if (c.korasiSpec && (c.equipment[Player.playerWeapon] == 19780 || c.equipment[Player.playerWeapon] == 19784)) {
			if (o.prayerActive[16] && System.currentTimeMillis() - o.protMageDelay > 1500) { // if
				// prayer
				// active
				// reduce
				// damage
				// by
				// half
				damage = (int) damage * 60 / 100;
			}
			if (o.curseActive[7] && System.currentTimeMillis() - o.protMageDelay > 1100) { // if
				// prayer
				// active
				// reduce
				// damage
				// by
				// half
				damage = (int) damage * 40 / 100;
				if (Misc.random(3) == 0) {
					o.gfx0(2228);
					o.startAnimation(12573);
					this.deflectDamage(damage, i);
				}
			}
		} else {
			if (o.prayerActive[18] && System.currentTimeMillis() - o.protMeleeDelay > 1500 && !veracsEffect) { // if
				// prayer
				// active
				// reduce
				// damage
				// by
				// 40%
				damage = (int) damage * 60 / 100;
			}
			if (o.curseActive[9] && System.currentTimeMillis() - o.protMeleeDelay > 1100 && !veracsEffect) { // if
				// prayer
				// active
				// reduce
				// damage
				// by
				// half
				damage = (int) damage * 40 / 100;
				if (Misc.random(3) == 0) {
					o.gfx0(2230);
					o.startAnimation(12573);
					this.deflectDamage(damage, i);
				}
			}
		}
		if (o.equipment[o.playerShield] == 13740) {
			o.level[5] -= damage * 0.15;
			if (o.level[5] >= 1) {
				damage = (int) damage * 70 / 100;
			}
			o.getPA().refreshSkill(5);
			if (o.level[5] <= 0) {
				o.level[5] = 0;
			}
		}
		if (o.equipment[o.playerShield] == 13742) {
			int random = Misc.random(9);
			if (random >= 0 && random <= 6) {
				damage = (int) damage * 75 / 100;
			}
		}
		if (o.equipment[o.playerShield] == 15042 && (damage >= 15)) {
			damage -= (int) damage * 15 / 100;
		}
		// what is this
		if (c.maxNextHit) {
			damage = this.calculateMeleeMaxHit();
		}
		if (o.equipment[Player.playerWeapon] == 15001 && damage >= 1 && o.SolProtect >= 1) {
			damage = (int) damage / 2;
		}
		if (damage > 0 && guthansEffect) {
			c.level[3] += damage;
			if (c.level[3] > c.getPA().getLevelForXP(c.xp[3])) {
				c.level[3] = c.getPA().getLevelForXP(c.xp[3]);
			}
			c.getPA().refreshSkill(3);
			o.gfx0(398);
		}
		if (c.ssSpec && damageMask == 2) {
			damage = 5 + Misc.random(11);
			c.ssSpec = false;
		}
		if (PlayerHandler.players[i].level[3] - damage < 0) {
			damage = PlayerHandler.players[i].level[3];
		}
		if (o.vengOn && damage > 0) {
			this.appendVengeance(i, damage);
		}
		if (damage > 0) {
			this.applyRecoil(damage, i);
		}
		switch (c.specEffect) {
		case 20:
			o.getCombat().resetPlayerAttack();
			o.vestaDelay = 8;
			break;
		case 5:
			c.clawDelay = 2;
			// c.clawDamage = Misc.random(calculateMeleeMaxHit());
			break;
		case 1: // dragon scimmy special
			if (o.prayerActive[16] || o.prayerActive[17] || o.prayerActive[18] || o.curseActive[7] || o.curseActive[8]
					|| o.curseActive[9]) {
				o.headIcon = -1;
				o.getPA().sendFrame36(c.PRAYER_GLOW[16], 0);
				o.getPA().sendFrame36(c.PRAYER_GLOW[17], 0);
				o.getPA().sendFrame36(c.PRAYER_GLOW[18], 0);
				o.getPA().sendFrame36(c.CURSE_GLOW[7], 0);
				o.getPA().sendFrame36(c.CURSE_GLOW[8], 0);
				o.getPA().sendFrame36(c.CURSE_GLOW[9], 0);
			}
			o.sendMessage("You have been injured!");
			o.stopPrayerDelay = System.currentTimeMillis();
			o.prayerActive[16] = false;
			o.prayerActive[17] = false;
			o.prayerActive[18] = false;
			o.curseActive[7] = false;
			o.curseActive[8] = false;
			o.curseActive[9] = false;
			o.getPA().requestUpdates();
			break;
		case 10:
			if (damage > 0) {
				o.level[1] -= damage / 3;
				o.sendMessage("You feel weakened by the might of Statius.");
				if (o.level[1] < 1) {
					o.level[1] = 1;
				}
				o.getPA().refreshSkill(1);
			}
			break;
		case 2:
			if (damage > 0) {
				if (o.freezeTimer <= 0) {
					o.freezeTimer = 30;
				}
				o.gfx0(2111);
				o.sendMessage("You have been frozen.");
				o.frozenBy = c.playerId;
				o.stopMovement();
				c.sendMessage("You freeze your enemy.");
			}
			break;
		case 3:
			if (damage > 0) {
				o.level[1] -= damage;
				o.sendMessage("You feel weak.");
				if (o.level[1] < 1) {
					o.level[1] = 1;
				}
				o.getPA().refreshSkill(1);
			}
			break;
		case 4: // sgs
			/**
			 * he Saradomin Godsword has a Special attack called Healing Blade. When used,
			 * the player's max hit is increased by 10%, and half (rounded down) of the
			 * damage dealt by the sword is restored to the player's hitpoints, and 1/4 of
			 * the damage is restored to the player's Prayer. This drains 50% of the special
			 * attack bar. Note that the sword's special attack must hit to have the effect.
			 * There is a minimum restoration of 10 hitpoints and 5 Prayer points, even with
			 * a hit of 1.
			 */

			if (damage > 0) {

				int heal = damage / 2;
				int prayer = heal / 4;

				if (heal < 10) {
					heal = 10;
				}
				if (prayer < 5) {
					prayer = 5;
				}

				int actualHitpointLevel = c.getPA().getActualLevel(PlayerConstants.HITPOINTS);
				if (c.level[PlayerConstants.HITPOINTS] < actualHitpointLevel) {
					if ((c.level[PlayerConstants.HITPOINTS] + heal) > actualHitpointLevel) {
						c.level[PlayerConstants.HITPOINTS] = actualHitpointLevel;
					} else {
						c.level[PlayerConstants.HITPOINTS] += heal;
					}
					c.getPA().refreshSkill(PlayerConstants.HITPOINTS);
				}

				int actualPrayerLevel = c.getPA().getActualLevel(PlayerConstants.PRAYER);
				if (c.level[PlayerConstants.PRAYER] < actualPrayerLevel) {
					if ((c.level[PlayerConstants.PRAYER] + prayer) > actualPrayerLevel) {
						c.level[PlayerConstants.PRAYER] = actualPrayerLevel;
					} else {
						c.level[PlayerConstants.PRAYER] += prayer;
					}
					c.getPA().refreshSkill(PlayerConstants.PRAYER);
				}
			}
			break;
		}
		c.specEffect = 0;
		if (damage > 0) {
			int xp = damage * 4;
			if (c.getCombatStyle() == CombatStyle.CONTROLLED) {
				c.getPA().addSkillXP(xp / 3, 0);
				c.getPA().addSkillXP(xp / 3, 1);
				c.getPA().addSkillXP(xp / 3, 2);
				c.getPA().addSkillXP(xp / 3, 3);
			} else {
				c.getPA().addSkillXP(xp / 3, 3);
				c.getPA().addSkillXP(xp, c.getCombatStyle().getSkill());
			}
		}
		PlayerHandler.players[i].logoutDelay = System.currentTimeMillis();
		PlayerHandler.players[i].underAttackBy = c.playerId;
		PlayerHandler.players[i].killerId = c.playerId;
		PlayerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
		if (c.killedBy != PlayerHandler.players[i].playerId) {
			c.totalPlayerDamageDealt = 0;
		}
		c.killedBy = PlayerHandler.players[i].playerId;
		this.applySmite(i, damage);
		this.applyRedemption(i, damage);
		if (c.soulSplitDelay <= 0) {
			this.applySoulSplit(i, damage);
		}
		if (c.leechAttackDelay <= 0) {
			this.applyLeechAttack(i, damage);
		}
		if (c.leechSpecialDelay <= 0) {
			this.applyLeechSpecial(i, damage);
		}
		if (c.leechDefenceDelay <= 0) {
			this.applyLeechDefence(i, damage);
		}
		if (c.leechStrengthDelay <= 0) {
			this.applyLeechStrength(i, damage);
		}
		// applyDivineEffect(i, damage);
		if (c.leechRangeDelay <= 0) {
			this.applyRangeLeech(i, damage);
		}
		if (c.leechMagicDelay <= 0) {
			this.applyLeechMagic(i, damage);
		}
		int damageType = 1;
		if (c.korasiSpec) {
			damageType = 3;
			PlayerHandler.players[i].gfx100(1730);
		}

		if (damage > 50) {
			Achievements.progressMade(c, Achievements.Types.HIT_500_PLUS);
		}

		PlayerHandler.players[i].handleHitMask(damage, maxDamage, damageType, i);
		switch (damageMask) {
		case 1:
			PlayerHandler.players[i].dealDamage(damage);
			PlayerHandler.players[i].damageTaken[c.playerId] += damage;
			c.totalPlayerDamageDealt += damage;
			PlayerHandler.players[i].updateRequired = true;
			o.getPA().refreshSkill(3);
			break;
		case 2:
			PlayerHandler.players[i].dealDamage(damage);
			PlayerHandler.players[i].damageTaken[c.playerId] += damage;
			c.totalPlayerDamageDealt += damage;
			PlayerHandler.players[i].updateRequired = true;
			c.doubleHit = false;
			o.getPA().refreshSkill(3);
			break;
		}
		c.korasiSpec = false;
	}

	public void applyRangeLeech(int index, int damage) {
		try {
			if (!c.curseActive[11]) {
				return;
			}
			if (c.level[4] > (c.getPA().getLevelForXP(c.xp[4]) + (c.getPA().getLevelForXP(c.xp[4]) / 15))) {
				return;
			}
			if (PlayerHandler.players[index] != null) {
				final Player c2 = PlayerHandler.players[index];
				final int pX = c.getX();
				final int pY = c.getY();
				final int oX = c2.getX();
				final int oY = c2.getY();
				final int offX = (pY - oY) * -1;
				final int offY = (pX - oX) * -1;
				if (damage > 0 && (c2.level[4] < (c2.getPA().getLevelForXP(c2.xp[4])
						+ (c2.getPA().getLevelForXP(c2.xp[4]) / 20)))) {
					int deduct = c.getPA().getLevelForXP(c.xp[4]) / (Misc.random(15) + 10);
					c2.level[4] -= deduct;
					c.level[4] += 1;
					// c.leechRangeTimer += Misc.random(15)+5;
					if (c2.level[4] <= 0) {
						c2.level[4] = 0;
					}
				}
				c.getPA().refreshSkill(4);
				c2.getPA().refreshSkill(4);
				c.leechRangeDelay = 16;
				Server.getTickManager().submit(new Tick(1) {

					@Override
					public void execute() {
						if (c == null || c2 == null || c.isDisconnected() || c2.isDisconnected()) {
							this.stop();
							return;
						}
						if (c.leechRangeDelay > 0) {
							c.leechRangeDelay--;
						}
						if (c.leechRangeDelay == 7) {
							c.startAnimation(12575);
						}
						if (c.leechRangeDelay == 6) {
							c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 45, 2236, 31, 31,
									-c.oldPlayerIndex - 1, 0);
						}
						// c.sendMessage("Started the event");
						if (c.leechRangeDelay == 4) {
							c2.gfx0(2237);
						}
						// c.sendMessage("Started anim & gfx");
						if (c.leechRangeDelay == 3) {
							c2.gfx0(2238);
						}
						if (c.leechRangeDelay == 0) {
							this.stop();
						}
					}
				});
			}
		} catch (Exception e) {
			System.out.println("Failed curse line 5067");
			e.printStackTrace();
		}
	}

	public void applyRecoil(int damage, int i) {
		if (damage > 0 && PlayerHandler.players[i].equipment[c.playerRing] == 2550) {
			int recDamage = (int) Math.round(damage * 0.10);

			c.dealDamage(recDamage);
			c.handleHitMask(recDamage, 100, 1, 0);
		}
	}

	/**
	 * Attack Npcs
	 */

	public void applyRedemption(int i, int damage) {
		if (PlayerHandler.players[i] == null || damage == 0) {
			return;
		}
		Player o = PlayerHandler.players[i];
		if (o.prayerActive[22]) {
			if (o.level[3] - damage < (o.getPA().getLevelForXP(o.xp[3]) * 10 / 100)) {
				o.gfx0(436);
				o.level[3] += o.getPA().getLevelForXP(o.xp[5]) * 25 / 100;
				o.getPA().refreshSkill(3);
				o.level[5] = 0;
				o.getPA().refreshSkill(5);
				o.getCombat().resetPrayers();
			}
		}
	}

	public void applySmite(int index, int damage) {
		if (!c.prayerActive[23]) {
			return;
		}
		if (damage <= 0) {
			return;
		}
		if (PlayerHandler.players[index] != null) {
			Player c2 = PlayerHandler.players[index];
			c2.level[5] -= (int) (damage / 4);
			if (c2.level[5] <= 0) {
				c2.level[5] = 0;
				c2.getCombat().resetPrayers();
			}
			c2.getPA().refreshSkill(5);
		}
	}

	public void applySoulSplitNPC(int index, int damage) {
		if (!c.curseActive[18]) {
			return;
		}
		if (NPCHandler.npcs[index] != null) {
			// c.sendMessage("here 1");
			final NPC c2 = NPCHandler.npcs[index];
			final int pX = c.getX();
			final int pY = c.getY();
			final int oX = c2.getX();
			final int oY = c2.getY();
			int offX = (pY - oY) * -1;
			int offY = (pX - oX) * -1;
			if (damage > 0) {
				int boost = Food.getHpBoost(c);
				int maxHitpoints = c.getActualLevel(PlayerConstants.HITPOINTS) + boost;
				if (c.level[PlayerConstants.HITPOINTS] < maxHitpoints) {
					int increment = (damage * 4) / 10;

					c.level[PlayerConstants.HITPOINTS] += increment;

					if (c.level[PlayerConstants.HITPOINTS] > maxHitpoints) {
						c.level[PlayerConstants.HITPOINTS] = maxHitpoints;
					}

					c.getPA().refreshSkill(3);
				}
			}
			c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 70, 2263, 31, 31, -c.oldPlayerIndex - 1, 0);
			c.soulSplitDelay = 4;
			// c.sendMessage("here 3");
			Server.getTickManager().submit(new Tick(1) {

				@Override
				public void execute() {
					// c.sendMessage("here 4");
					if (c == null || c2 == null || c.isDisconnected()) {
						this.stop();
						return;
					}
					if (c.soulSplitDelay > 0) {
						c.soulSplitDelay--;
					}
					// c.sendMessage("first");
					if (c.soulSplitDelay == 3) {
						c2.gfx0(2264);
					}
					// c.sendMessage("second");
					if (c.soulSplitDelay == 2) {
						int offX2 = (oY - pY) * -1;
						int offY2 = (oX - pX) * -1;
						c.getPA().createPlayersProjectile(oX, oY, offX2, offY2, 50, 70, 2263, 31, 31, -c.playerId - 1,
								0);
					}
					// c.sendMessage("projectile");
					if (c.soulSplitDelay == 0) {
						this.stop();
					}
				}
			});
		}
	}

	public void applySoulSplit(int index, int damage) {
		if (!c.curseActive[18]) {
			return;
		}
		if (PlayerHandler.players[index] != null) {
			// c.sendMessage("here 1");
			final Player c2 = PlayerHandler.players[index];
			final int pX = c.getX();
			final int pY = c.getY();
			final int oX = c2.getX();
			final int oY = c2.getY();
			int offX = (pY - oY) * -1;
			int offY = (pX - oX) * -1;
			if (damage > 0) {
				c2.level[5] -= (int) (damage / 5);
				if (c2.level[5] <= 0) {
					c2.level[5] = 0;
					c2.getCombat().resetPrayers();
				}

				int boost = Food.getHpBoost(c);
				int maxHitpoints = c.getActualLevel(PlayerConstants.HITPOINTS) + boost;
				if (c.level[PlayerConstants.HITPOINTS] < maxHitpoints) {
					int increment = (damage * 4) / 10;

					c.level[PlayerConstants.HITPOINTS] += increment;

					if (c.level[PlayerConstants.HITPOINTS] > maxHitpoints) {
						c.level[PlayerConstants.HITPOINTS] = maxHitpoints;
					}

					c.getPA().refreshSkill(3);
				}

				c2.getPA().refreshSkill(5);
			}
			c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 70, 2263, 31, 31, -c.oldPlayerIndex - 1, 0);
			c.soulSplitDelay = 4;
			// c.sendMessage("here 3");
			Server.getTickManager().submit(new Tick(1) {

				@Override
				public void execute() {
					// c.sendMessage("here 4");
					if (c == null || c2 == null || c.isDisconnected() || c2.isDisconnected()) {
						this.stop();
						return;
					}
					if (c.soulSplitDelay > 0) {
						c.soulSplitDelay--;
					}
					// c.sendMessage("first");
					if (c.soulSplitDelay == 3) {
						c2.gfx0(2264);
					}
					// c.sendMessage("second");
					if (c.soulSplitDelay == 2) {
						int offX2 = (oY - pY) * -1;
						int offY2 = (oX - pX) * -1;
						c.getPA().createPlayersProjectile(oX, oY, offX2, offY2, 50, 70, 2263, 31, 31, -c.playerId - 1,
								0);
					}
					// c.sendMessage("projectile");
					if (c.soulSplitDelay == 0) {
						this.stop();
					}
				}
			});
		}
	}

	public boolean armaNpc(int i) {
		switch (NPCHandler.npcs[i].type) {
		case 2558:
		case 2559:
		case 2560:
		case 2561:
			return true;
		}
		return false;
	}

	public void attackNpc(int i) {
		if (i > NPCHandler.npcs.length) {
			return;
		}
		NPC npc = NPCHandler.npcs[i];
		if (npc == null) {
			return;
		}
		if (npc.isDead || npc.maxHP <= 0) {
			c.usingMagic = false;
			c.faceUpdate(0);
			c.npcIndex = 0;
			return;
		}

		if (npc.damageImmune) {
			c.sendMessage("This NPC is immune to your damage.");
			c.getCombat().resetPlayerAttack();
			return;
		}
		if (c.respawnTimer > 0) {
			c.npcIndex = 0;
			return;
		}
		if (c.equipment[Player.playerWeapon] == 861 && c.equipment[c.playerArrows] < 1) {
			c.sendMessage("You cannot range without having arrows!");
			this.resetPlayerAttack();
			return;
		}
		if (c.absX == npc.absX && c.absY == npc.absY) {
			c.getPA().walkTo2(-1, 0);
		}
		if (npc.underAttackBy > 0 && npc.underAttackBy != c.playerId && !Areas.inMulti(npc) && npc.type != 5666
				|| npc.type == 3782) {
			c.npcIndex = 0;
			c.sendMessage("This monster is already in combat.");
			return;
		}
		if ((c.underAttackBy > 0 || c.underAttackByNpcID > 0) && c.underAttackByNpcID != i && !Areas.inMulti(c)) {
			this.resetPlayerAttack();
			c.sendMessage("I am already under attack.");
			return;
		}
		if (npc.spawnedBy != c.playerId && npc.spawnedBy > 0) {
			this.resetPlayerAttack();
			c.sendMessage("This monster was not spawned for you.");
			return;
		}
		if (c.autocasting) {
			c.spellId = c.autocastId;
			c.usingMagic = true;
		}
		if (c.spellId > 0) {
			c.usingMagic = true;
		}
		if (!c.usingMagic && c.inMageArena()) {
			c.sendMessage("You can only use autocast magic attacks in this arena!");
			return;
		}

		boolean usingBow = false;
		boolean usingOtherRangeWeapons = false;

		if (!c.usingMagic) {
			for (int bowId : c.BOWS) {
				if (c.equipment[Player.playerWeapon] == bowId) {
					usingBow = true;
					break;
				}
			}
			for (int otherRangeId : c.OTHER_RANGE_WEAPONS) {
				if (c.equipment[Player.playerWeapon] == otherRangeId) {
					usingOtherRangeWeapons = true;
					break;
				}
			}
		}

		/*
		 * if (usingBow || c.usingMagic || usingOtherRangeWeapons || this.usingHally())
		 * { c.followId2 = -1; c.stopMovement(); } if (usingBow || c.usingMagic &&
		 * c.goodDistance(c.getX(), c.getY(), npc.getX(), npc.getY(), 8)) {
		 * c.stopMovement(); }
		 */

		c.specEffect = 0;
		c.followId2 = i;
		c.followId = 0;
		if (c.attackTimer > 0) {
			return;
		}
		c.strBonus = c.playerBonus[10];
		// if (usingBow || c.usingMagic || usingOtherRangeWeapons) {
		if (!Collision.canProjectileMove(c.absX, c.absY, npc.absX, npc.absY, c.heightLevel, 1, 1)) {
			if (npc.type == 14205 && Zombies.inGameArea(npc.getLocation()) && npc.absX == 4132
					&& (npc.absY == 3993 || npc.absY == 3994)) {
				if (!((c.absX + 1) == npc.absX
						&& (c.absY == npc.absY || c.absY - 1 == npc.absY || c.absY + 1 == npc.absY))) {
					return;
				}
			}
		}
		// }

		if (NPCHandler.npcs[i].getCombat() != null) {
			NPCHandler.npcs[i].getCombat().attackedByPlayer(c);
		}

		if (c.attributeExists("cancel_attacking_npc")) {
			c.removeAttribute("cancel_attacking_npc");
			this.resetPlayerAttack();
			return;
		}

		boolean usingCross = usingCrossbow(c.equipment[Player.playerWeapon]);
		c.bonusAttack = 0;
		c.rangeItemUsed = 0;
		c.projectileStage = 0;
		c.attackTimer = this.getAttackDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
		c.specAccuracy = 1.0;
		c.specDamage = 1.0;
		if (this.armaNpc(i) && !usingCross && !usingBow && !c.usingMagic && !this.usingCrystalBow()
				&& !usingOtherRangeWeapons) {
			this.resetPlayerAttack();
			return;
		}

		int offsetX = NPCConfig.offset(npc);
		int offsetY = NPCConfig.offset(npc);

		if (!Location.isWithinDistance(c.getLocation(), NPCHandler.npcs[i].getLocation().transform(offsetX, offsetY, 0),
				0, CombatHelper.getRequiredDistance(c))) {
			c.attackTimer = 2;
			return;
		}

		/*
		 * if ((!c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[i].getX(),
		 * NPCHandler.npcs[i].getY(), 2) && this.usingHally() && !usingOtherRangeWeapons
		 * && !usingBow && !c.usingMagic) || (!c.goodDistance(c.getX(), c.getY(),
		 * NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(), 4) &&
		 * usingOtherRangeWeapons && !usingBow && !c.usingMagic) ||
		 * (!c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[i].getX(),
		 * NPCHandler.npcs[i].getY(), 1) && !usingOtherRangeWeapons &&
		 * !this.usingHally() && !usingBow && !c.usingMagic) ||
		 * (!c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[i].getX(),
		 * NPCHandler.npcs[i].getY(), 8) && (usingBow || c.usingMagic))) { c.attackTimer
		 * = 2; return; }
		 */

		if (c.equipment[PlayerConstants.WEAPON] == TridentOfTheSeas.CHARGED_TRIDENT) {
			if (c.getData().tridentSeaCharges <= 0) {
				c.sendMessage("Your staff has ran out of charges.");
				c.stopMovement();
				resetPlayerAttack();
				return;
			}
		}

		if (c.equipment[PlayerConstants.WEAPON] == TridentOfTheSwamp.CHARGED_TRIDENT) {
			if (c.getData().tridentSwampCharges <= 0) {
				c.sendMessage("Your staff has ran out of charges.");
				c.stopMovement();
				resetPlayerAttack();
				return;
			}
		}

		if (usingBow || usingCross) {
			if (c.equipment[PlayerConstants.WEAPON] != Blowpipe.BLOWPIPE
					&& !(c.equipment[Player.playerWeapon] >= 4212 && c.equipment[Player.playerWeapon] <= 4223)
					&& c.equipment[c.playerArrows] == -1 && c.equipment[Player.playerWeapon] != 20173) {
				c.sendMessage("You do not have any ammo for this weapon!");
				c.stopMovement();
				resetPlayerAttack();
				return;
			}

			if (c.equipment[PlayerConstants.WEAPON] == Blowpipe.BLOWPIPE) {
				if (c.getData().blowpipeAmmo == 0) {
					c.sendMessage("You do not have any ammo for this weapon!");
					c.stopMovement();
					resetPlayerAttack();
					return;
				}
				if (c.getData().blowpipeCharges == 0) {
					c.sendMessage("You do not have any Toxic blowpipe charges.");
					c.stopMovement();
					resetPlayerAttack();
					return;
				}
			}

			if (!CombatHelper.correctBowAndArrow(c)) {
				c.sendMessage("You can't use " + c.getItems().getItemName(c.equipment[c.playerArrows]).toLowerCase()
						+ "s with a " + c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase() + ".");
				c.stopMovement();
				resetPlayerAttack();
				return;
			}
		}

		if (usingBow || c.usingMagic || usingOtherRangeWeapons
				|| (c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(), 2)
						&& this.usingHally())) {
			c.stopMovement();
		}
		if (!this.checkMagicReqs(c.spellId)) {
			c.stopMovement();
			c.npcIndex = 0;
			return;
		}
		c.faceUpdate(i);
		NPCHandler.npcs[i].underAttackBy = c.playerId;
		NPCHandler.npcs[i].lastDamageTaken = System.currentTimeMillis();
		if (c.usingSpecial && !c.usingMagic) {
			if (this.checkSpecAmount(c.equipment[Player.playerWeapon])) {
				c.lastWeaponUsed = c.equipment[Player.playerWeapon];
				c.lastArrowUsed = c.equipment[c.playerArrows];
				this.activateSpecial(c.equipment[Player.playerWeapon], i, true);
				return;
			} else {
				c.sendMessage("You don't have the required special energy to use this attack.");
				c.usingSpecial = false;
				c.getItems().updateSpecialBar();
				c.npcIndex = 0;
				return;
			}
		}
		if (usingBow || c.usingMagic || usingOtherRangeWeapons) {
			c.mageFollow = true;
		} else {
			c.mageFollow = false;
		}
		c.specMaxHitIncrease = 0;
		if (!c.usingMagic) {
			c.startAnimation(Animations.getAttackAnimation(c));
		} else {
			c.startAnimation(Player.MAGIC_SPELLS[c.spellId][2]);
		}
		c.lastWeaponUsed = c.equipment[Player.playerWeapon];
		c.lastArrowUsed = c.equipment[c.playerArrows];
		if (!usingBow && !c.usingMagic && !usingOtherRangeWeapons) { // melee
			// hit
			// delay
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			c.projectileStage = 0;
			c.oldNpcIndex = i;
		}
		if (usingBow && !usingOtherRangeWeapons && !c.usingMagic || usingCross) { // range hit delay
			if (usingCross) {
				c.usingBow = true;
			}
			if (c.getCombatStyle() == CombatStyle.RAPID) {
				c.attackTimer--;
			}
			c.lastArrowUsed = c.equipment[c.playerArrows];
			c.lastWeaponUsed = c.equipment[Player.playerWeapon];
			c.gfx100(this.getRangeStartGFX());
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			c.projectileStage = 1;
			c.oldNpcIndex = i;
			if (c.equipment[Player.playerWeapon] >= 20173 && c.equipment[Player.playerWeapon] <= 20174) {
				c.rangeItemUsed = c.equipment[Player.playerWeapon];
				c.lastArrowUsed = 0;
			}
			if (c.equipment[Player.playerWeapon] >= 4212 && c.equipment[Player.playerWeapon] <= 4223) {
				c.rangeItemUsed = c.equipment[Player.playerWeapon];
				c.crystalBowArrowCount++;
				c.lastArrowUsed = 0;
			} else {
				c.rangeItemUsed = c.equipment[c.playerArrows];
				c.getItems().deleteArrow();
			}
			this.fireProjectileNpc();
		}
		if (usingOtherRangeWeapons && !c.usingMagic && !usingBow) { // knives,
			// darts,
			// etc
			// hit
			// delay
			c.rangeItemUsed = c.equipment[Player.playerWeapon];
			c.getItems().deleteEquipment();
			c.gfx100(this.getRangeStartGFX());
			c.lastArrowUsed = 0;
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			c.projectileStage = 1;
			c.oldNpcIndex = i;
			if (c.getCombatStyle() == CombatStyle.RAPID) {
				c.attackTimer--;
			}
			this.fireProjectileNpc();
		}
		if (c.usingMagic) { // magic hit delay
			int pX = c.getX();
			int pY = c.getY();
			int nX = NPCHandler.npcs[i].getX();
			int nY = NPCHandler.npcs[i].getY();
			int offX = (pY - nY) * -1;
			int offY = (pX - nX) * -1;
			c.castingMagic = true;
			c.projectileStage = 2;
			if (Player.MAGIC_SPELLS[c.spellId][3] > 0) {
				if (this.getStartGfxHeight() == 100) {
					c.gfx100(Player.MAGIC_SPELLS[c.spellId][3]);
				} else {
					c.gfx0(Player.MAGIC_SPELLS[c.spellId][3]);
				}
			}
			if (Player.MAGIC_SPELLS[c.spellId][4] > 0) {
				c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 78, Player.MAGIC_SPELLS[c.spellId][4],
						this.getStartHeight(), this.getEndHeight(), i + 1, 50);
			}
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			c.oldNpcIndex = i;
			c.oldSpellId = c.spellId;
			c.spellId = 0;
			if (!c.autocasting) {
				c.npcIndex = 0;

				// stop attacking npc when using manual casts
				resetPlayerAttack();
			}
		}
		if (usingBow && Config.CRYSTAL_BOW_DEGRADES) { // crystal bow
			// degrading
			if (c.equipment[Player.playerWeapon] == 4212) { // new
				// crystal
				// bow
				// becomes
				// full
				// bow
				// on
				// the
				// first
				// shot
				c.getItems().wearItem(4214, 1, 3);
			}
			if (c.crystalBowArrowCount >= 250) {
				switch (c.equipment[Player.playerWeapon]) {
				case 4223: // 1/10 bow
					c.getItems().wearItem(-1, 1, 3);
					c.sendMessage("Your crystal bow has fully degraded.");
					if (!c.getItems().addItem(4207, 1)) {
						ItemDrops.createGroundItem(c, 4207, c.getX(), c.getY(), 1, c.getId(), false);
					}
					c.crystalBowArrowCount = 0;
					break;
				default:
					c.getItems().wearItem(++c.equipment[Player.playerWeapon], 1, 3);
					c.sendMessage("Your crystal bow degrades.");
					c.crystalBowArrowCount = 0;
					break;
				}
			}
		}
	}

	public void attackPlayer(int i) {
		if (i < 0 || i > PlayerHandler.players.length) {
			return;
		}
		if (PlayerHandler.players[i] == null) {
			return;
		}
		Player c2 = PlayerHandler.players[i];
		c.logoutDelay = System.currentTimeMillis();
		if (c.vestaDelay > 0) {
			this.resetPlayerAttack();
			c.sendMessage("The shield your opponent put up is too hard to break through!");
			return;
		}

		if (c.getCastleWarsState() != null) {
			if (!CastleWars.onAttack(c, c2)) {
				this.resetPlayerAttack();
				return;
			}
		}

		if (c.tradeTimer > 0) {
			this.resetPlayerAttack();
			c.sendMessage("You have to wait 15 minutes on new accounts before you can attack people!");
			return;
		}
		if (c2.tradeTimer > 0 /* && !clanBypass */) {
			this.resetPlayerAttack();
			c.sendMessage("You have to wait 15 minutes on new accounts before you can attack people!");
			return;
		}
		if (PlayerHandler.players[i].level[3] <= 0) {
			this.resetPlayerAttack();
			return;
		}
		if (c.equipment[Player.playerWeapon] == 861 && c.equipment[c.playerArrows] < 1) {
			c.sendMessage("You cannot range without having arrows!");
			this.resetPlayerAttack();
			return;
		}
		if (c.equipment[Player.playerWeapon] == 11235 && c.equipment[c.playerArrows] < 1) {
			c.sendMessage("You cannot range without having arrows!");
			this.resetPlayerAttack();
			return;
		}
		/*
		 * if(c.clan != null && c2.clan != null && c.clan == c2.clan && c.inClanArena()
		 * && c2.inClanArena()) { resetPlayerAttack();
		 * c.sendMessage("You cannot attack your own clan mates in clan wars!"); return;
		 * }
		 */
		if (c.equipment[c.playerCape] == c2.equipment[c.playerCape] && c.inTrinityWar()) {
			this.resetPlayerAttack();
			c.sendMessage("You cannot attack your own clan mates in clan wars!");
			return;
		}
		if (c.respawnTimer > 0 || PlayerHandler.players[i].respawnTimer > 0) {
			this.resetPlayerAttack();
			return;
		}
		if (c2.isNotFighting == true && (c.isNotFighting == true)) {
			this.resetPlayerAttack();
			c2.sendMessage("This staff member is impervious to your attacks!");
			return;
		}
		if (!c.getCombat().checkReqs() /* && !clanBypass */) {
			return;
		}
		if (c2.inDeathChamber()) {
			return;
		}
		if (c.rights != 3 && c.getAttribute("clan_war") == null && c.getCastleWarsState() == null && c.getDuel() == null
				&& c.getPA().getWearingAmount() < 4) {
			c.sendMessage("You must be wearing at least 4 items to attack someone.");
			this.resetPlayerAttack();
			return;
		}
		boolean sameSpot = c.absX == PlayerHandler.players[i].getX() && c.absY == PlayerHandler.players[i].getY();
		if (!c.goodDistance(PlayerHandler.players[i].getX(), PlayerHandler.players[i].getY(), c.getX(), c.getY(), 25)
				&& !sameSpot) {
			this.resetPlayerAttack();
			return;
		}
		if (PlayerHandler.players[i].respawnTimer > 0) {
			PlayerHandler.players[i].playerIndex = 0;
			this.resetPlayerAttack();
			return;
		}
		if (PlayerHandler.players[i].heightLevel != c.heightLevel) {
			this.resetPlayerAttack();
			return;
		}
		if (PlayerHandler.players[i].teleTimer > 0 || c.teleTimer > 0) {
			this.resetPlayerAttack();
			return;
		}

		c.followId = i;
		c.followId2 = 0;
		if (c.attackTimer > 0) {
			return;
		}
		c.strBonus = c.playerBonus[10];
		c.usingBow = false;
		c.specEffect = 0;
		c.usingRangeWeapon = false;
		c.rangeItemUsed = 0;
		boolean usingBow = false;
		boolean usingOtherRangeWeapons = false;
		boolean usingCross = usingCrossbow(c.equipment[Player.playerWeapon]);

		c.projectileStage = 0;
		if (c.absX == PlayerHandler.players[i].absX && c.absY == PlayerHandler.players[i].absY) {
			if (c.freezeTimer > 0) {
				this.resetPlayerAttack();
				return;
			}
			c.followId = i;
			c.attackTimer = 0;
			return;
		}

		if (!c.usingMagic) {
			for (int bowId : c.BOWS) {
				if (c.equipment[Player.playerWeapon] == bowId) {
					usingBow = true;
					break;
				}
			}
			for (int otherRangeId : c.OTHER_RANGE_WEAPONS) {
				if (c.equipment[Player.playerWeapon] == otherRangeId) {
					usingOtherRangeWeapons = true;
					break;
				}
			}
		}

		c.usingBow = usingBow;
		c.usingRangeWeapon = usingOtherRangeWeapons;

		if (c.autocasting && c.autocastId != -1) {
			c.spellId = c.autocastId;
			c.usingMagic = true;
		}

		if (!c.usingMagic && c.inMageArena()) {
			c.sendMessage("You can only use autocast magic attacks in this arena!");
			return;
		}

		if (c.getDuel() != null && c.getDuel().getStage() == DuelArena.Stage.FIGHTING) {
			if (c.getDuel().getRules()[PlayerConstants.DUEL_RANGE] && (usingBow || usingOtherRangeWeapons)) {
				c.sendMessage("Range has been disabled in this duel!");
				return;
			}
			if (c.getDuel().getRules()[PlayerConstants.DUEL_MELEE]
					&& (!usingBow && !usingOtherRangeWeapons && !c.usingMagic)) {
				c.sendMessage("Melee has been disabled in this duel!");
				return;
			}
			if (c.getDuel().getRules()[PlayerConstants.DUEL_MAGE] && c.usingMagic) {
				c.sendMessage("Magic has been disabled in this duel!");
				resetPlayerAttack();
				return;
			}
		}
		if (c.getAttribute("clan_war") != null) {
			War war = (War) c.getAttribute("clan_war");
			if (war.getCombatRules()[Constants.RANGE_RULE] && (usingBow || usingOtherRangeWeapons)) {
				c.sendMessage("You may may not use range in this clan war.");
				return;
			}
			if (war.getCombatRules()[Constants.MELEE_RULE] && (!usingBow && !usingOtherRangeWeapons && !c.usingMagic)) {
				c.sendMessage("You may may not use melee in this clan war.");
				return;
			}
			if (war.getCombatRules()[Constants.MAGIC_RULE] && c.usingMagic) {
				c.sendMessage("You may may not use magic in this clan war.");
				return;
			}
		}

		// if (usingBow || c.usingMagic || usingOtherRangeWeapons) {
		if (!Collision.canProjectileMove(c.absX, c.absY, PlayerHandler.players[i].absX, PlayerHandler.players[i].absY,
				c.heightLevel, 1, 1)) {
			return;
		}
		// }
		c.attackTimer = this.getAttackDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
		/*
		 * if ((!c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[i].getX(),
		 * PlayerHandler.players[i].getY(), 4) && usingOtherRangeWeapons && !usingBow &&
		 * !c.usingMagic) || (!c.goodDistance(c.getX(), c.getY(),
		 * PlayerHandler.players[i].getX(), PlayerHandler.players[i].getY(), 2) &&
		 * !usingOtherRangeWeapons && this.usingHally() && !usingBow && !c.usingMagic)
		 * || (!c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[i].getX(),
		 * PlayerHandler.players[i].getY(), this.getRequiredDistance()) &&
		 * !usingOtherRangeWeapons && !this.usingHally() && !usingBow && !c.usingMagic)
		 * || (!c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[i].getX(),
		 * PlayerHandler.players[i].getY(), 10) && (usingBow || c.usingMagic))) { //
		 * c.sendMessage("Setting attack timer to 1"); c.attackTimer = 1; if (!usingBow
		 * && !c.usingMagic && !usingOtherRangeWeapons && c.freezeTimer > 0) {
		 * this.resetPlayerAttack(); } return; }
		 */

		if (!c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[i].getX(), PlayerHandler.players[i].getY(),
				CombatHelper.getRequiredDistance(c) + (PlayerHandler.players[i].isMoving() ? 2 : 0))) {
			c.attackTimer = 1;
			if (!usingBow && !c.usingMagic && !usingOtherRangeWeapons && c.freezeTimer > 0) {
				this.resetPlayerAttack();
			}
			return;
		}

		if (usingBow || usingCross) {
			if (c.equipment[PlayerConstants.WEAPON] != Blowpipe.BLOWPIPE
					&& !(c.equipment[Player.playerWeapon] >= 4212 && c.equipment[Player.playerWeapon] <= 4223)
					&& c.equipment[c.playerArrows] == -1 && c.equipment[Player.playerWeapon] != 20173) {
				c.sendMessage("You do not have any ammo for this weapon!");
				c.stopMovement();
				resetPlayerAttack();
				return;
			}
			if (c.equipment[PlayerConstants.WEAPON] == Blowpipe.BLOWPIPE && c.getData().blowpipeAmmo == 0) {
				if (c.getData().blowpipeAmmo == 0) {
					c.sendMessage("You do not have any ammo for this weapon!");
					c.stopMovement();
					resetPlayerAttack();
					return;
				}
				if (c.getData().blowpipeCharges == 0) {
					c.sendMessage("You do not have any Toxic blowpipe charges.");
					c.stopMovement();
					resetPlayerAttack();
					return;
				}
			}
			if (!CombatHelper.correctBowAndArrow(c)) {
				c.sendMessage("You can't use " + c.getItems().getItemName(c.equipment[c.playerArrows]).toLowerCase()
						+ "s with a " + c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase() + ".");
				c.stopMovement();
				resetPlayerAttack();
				return;
			}
		}
		if (usingBow || c.usingMagic || usingOtherRangeWeapons || this.usingHally()) {
			c.stopMovement();
		}
		if (!this.checkMagicReqs(c.spellId)) {// THIS IS YOUR ISSUE
			c.stopMovement();
			this.resetPlayerAttack();
			return;
		}
		c.faceUpdate(i + 32768);
		if (!ClanWars.inArena(c) && c.getDuel() == null && c.getCastleWarsState() == null) {
			if (!c.attackedPlayers.contains(c.playerIndex)
					&& !PlayerHandler.players[c.playerIndex].attackedPlayers.contains(c.playerId)) {
				c.attackedPlayers.add(c.playerIndex);
				c.isSkulled = true;
				c.skullTimer = Config.SKULL_TIMER;
				c.headIconPk = 0;
				c.getPA().requestUpdates();
			}
		}
		c.specAccuracy = 1.0;
		c.specDamage = 1.0;
		c.delayedDamage = c.delayedDamage2 = 0;
		if (c.usingSpecial && !c.usingMagic) {

			if (c.getDuel() != null && c.getDuel().getStage() == DuelArena.Stage.FIGHTING) {
				if (c.getDuel().getRules()[PlayerConstants.DUEL_SPECIAL]) {
					c.sendMessage("Special attacks have been disabled during this duel!");
					c.usingSpecial = false;
					c.getItems().updateSpecialBar();
					resetPlayerAttack();
					return;
				}
			}

			if (this.checkSpecAmount(c.equipment[Player.playerWeapon])) {
				c.lastArrowUsed = c.equipment[c.playerArrows];
				this.activateSpecial(c.equipment[Player.playerWeapon], i, true);
				c.followId = c.playerIndex;
				return;
			} else {
				c.sendMessage("You don't have the required special energy to use this attack.");
				c.usingSpecial = false;
				c.getItems().updateSpecialBar();
				c.playerIndex = 0;
				return;
			}
		}
		if (!c.usingMagic) {
			c.startAnimation(Animations.getAttackAnimation(c));
			c.mageFollow = false;
		} else {
			c.startAnimation(Player.MAGIC_SPELLS[c.spellId][2]);
			c.mageFollow = true;
			c.followId = c.playerIndex;
		}
		PlayerHandler.players[i].underAttackBy = c.playerId;
		PlayerHandler.players[i].logoutDelay = System.currentTimeMillis();
		PlayerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
		PlayerHandler.players[i].killerId = c.playerId;
		c.lastArrowUsed = 0;
		c.rangeItemUsed = 0;
		if (!usingBow && !c.usingMagic && !usingOtherRangeWeapons) { // melee
			// hit
			// delay
			c.followId = PlayerHandler.players[c.playerIndex].playerId;
			c.getPA().follow();
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			c.delayedDamage = CombatHelper.getMeleeDamage(c, PlayerHandler.players[c.playerIndex]);
			c.projectileStage = 0;
			c.oldPlayerIndex = i;
		}
		if (usingBow && !usingOtherRangeWeapons && !c.usingMagic || usingCross) { // range
			// hit
			// delay
			if (c.equipment[Player.playerWeapon] >= 4212 && c.equipment[Player.playerWeapon] <= 4223) {
				c.rangeItemUsed = c.equipment[Player.playerWeapon];
				c.crystalBowArrowCount++;
			} else {
				c.rangeItemUsed = c.equipment[c.playerArrows];
				c.getItems().deleteArrow();
			}
			if (c.getCombatStyle() == CombatStyle.RAPID) {
				c.attackTimer--;
			}
			if (usingCross) {
				c.usingBow = true;
			}
			c.usingBow = true;
			c.followId = PlayerHandler.players[c.playerIndex].playerId;
			c.getPA().follow();
			c.lastWeaponUsed = c.equipment[Player.playerWeapon];
			c.lastArrowUsed = c.equipment[c.playerArrows];
			c.gfx100(this.getRangeStartGFX());
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			c.projectileStage = 1;
			c.oldPlayerIndex = i;
			this.fireProjectilePlayer();
		}
		if (usingOtherRangeWeapons) { // knives, darts, etc hit delay
			c.rangeItemUsed = c.equipment[Player.playerWeapon];
			c.getItems().deleteEquipment();
			c.usingRangeWeapon = true;
			c.followId = PlayerHandler.players[c.playerIndex].playerId;
			c.getPA().follow();
			c.gfx100(this.getRangeStartGFX());
			if (c.getCombatStyle() == CombatStyle.RAPID) {
				c.attackTimer--;
			}
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			c.projectileStage = 1;
			c.oldPlayerIndex = i;
			this.fireProjectilePlayer();
		}
		if (c.usingMagic) { // magic hit delay
			int pX = c.getX();
			int pY = c.getY();
			int nX = PlayerHandler.players[i].getX();
			int nY = PlayerHandler.players[i].getY();
			int offX = (pY - nY) * -1;
			int offY = (pX - nX) * -1;
			c.castingMagic = true;
			c.projectileStage = 2;
			int spellGFX = Player.MAGIC_SPELLS[c.spellId][3];

			if (Player.MAGIC_SPELLS[c.spellId][4] > 0) {
				c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 78, Player.MAGIC_SPELLS[c.spellId][4],
						this.getStartHeight(), this.getEndHeight(), -i - 1, this.getStartDelay());
			}

			if (c.autocastId > 0) {
				c.followId = c.playerIndex;
			}
			c.hitDelay = this.getHitDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
			c.oldPlayerIndex = i;
			c.oldSpellId = c.spellId;
			c.spellId = 0;
			Player o = PlayerHandler.players[i];
			if (Player.MAGIC_SPELLS[c.oldSpellId][0] == 12891 && o.isMoving) {
				// c.sendMessage("Barrage projectile..");
				c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 85, 368, 25, 25, -i - 1,
						this.getStartDelay());
			}
			if (Misc.random(o.getCombat().mageDef()) > Misc.random(this.mageAtk())) {
				c.magicFailed = true;
			} else {
				c.magicFailed = false;
			}
			int freezeDelay = this.getFreezeTime();// freeze

			if (o.equipment[PlayerConstants.SHIELD] == 2890) {
				freezeDelay = freezeDelay / 2;
			}

			if (freezeDelay > 0 && PlayerHandler.players[i].freezeTimer <= -3 && !c.magicFailed) {
				PlayerHandler.players[i].freezeTimer = freezeDelay;
				o.resetWalkingQueue();
				o.sendMessage("You have been frozen.");
				o.frozenBy = c.playerId;
			}
			if (spellGFX > 0) {
				if (this.getStartGfxHeight() == 100) {
					c.gfx100(spellGFX);
				} else {
					c.gfx0(spellGFX);
				}
			}
			if (!c.autocasting && c.spellId <= 0) {
				c.playerIndex = 0;
				resetPlayerAttack();
			}
		}
		if (usingBow && Config.CRYSTAL_BOW_DEGRADES) { // crystal bow degrading
			if (c.equipment[Player.playerWeapon] == 4212) { // new crystal bow
				// becomes full
				// bow on the
				// first shot
				c.getItems().wearItem(4214, 1, 3);
			}
			if (c.crystalBowArrowCount >= 250) {
				switch (c.equipment[Player.playerWeapon]) {
				case 4223: // 1/10 bow
					c.getItems().wearItem(-1, 1, 3);
					c.sendMessage("Your crystal bow has fully degraded.");
					if (!c.getItems().addItem(4207, 1)) {
						ItemDrops.createGroundItem(c, 4207, c.getX(), c.getY(), 1, c.getId(), false);
					}
					c.crystalBowArrowCount = 0;
					break;
				default:
					c.getItems().wearItem(++c.equipment[Player.playerWeapon], 1, 3);
					c.sendMessage("Your crystal bow degrades.");
					c.crystalBowArrowCount = 0;
					break;
				}
			}
		}
	}

	public int bestMeleeAtk() {
		if (c.playerBonus[0] > c.playerBonus[1] && c.playerBonus[0] > c.playerBonus[2]) {
			return 0;
		}
		if (c.playerBonus[1] > c.playerBonus[0] && c.playerBonus[1] > c.playerBonus[2]) {
			return 1;
		}
		return c.playerBonus[2] <= c.playerBonus[1] || c.playerBonus[2] <= c.playerBonus[0] ? 0 : 2;
	}

	public int bestMeleeDef() {
		if (c.playerBonus[5] > c.playerBonus[6] && c.playerBonus[5] > c.playerBonus[7]) {
			return 5;
		}
		if (c.playerBonus[6] > c.playerBonus[5] && c.playerBonus[6] > c.playerBonus[7]) {
			return 6;
		}
		return c.playerBonus[7] <= c.playerBonus[5] || c.playerBonus[7] <= c.playerBonus[6] ? 5 : 7;
	}

	private final boolean hasSpear() {
		String name = c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase();
		if (name.contains("spear") || name.contains("halberd"))
			return true;
		return false;
	}

	/**
	 * Melee
	 */

	public int calculateMeleeAttack() {
		int attackLevel = c.level[0];
		/**
		 * attack style bonus - should accurate attack styles give extra level?
		 */
		/**
		 * prayer bonuses
		 */
		if (c.prayerActive[2]) {
			attackLevel += c.getPA().getLevelForXP(c.xp[PlayerConstants.ATTACK]) * 0.05;
		} else if (c.prayerActive[7]) {
			attackLevel += c.getPA().getLevelForXP(c.xp[PlayerConstants.ATTACK]) * 0.1;
		} else if (c.prayerActive[15]) {
			attackLevel += c.getPA().getLevelForXP(c.xp[PlayerConstants.ATTACK]) * 0.15;
		} else if (c.prayerActive[24]) {
			attackLevel += c.getPA().getLevelForXP(c.xp[PlayerConstants.ATTACK]) * 0.15;
		} else if (c.prayerActive[25]) {
			attackLevel += c.getPA().getLevelForXP(c.xp[PlayerConstants.ATTACK]) * 0.2;
		} else if (c.curseActive[19]) {
			attackLevel += c.getPA().getLevelForXP(c.xp[PlayerConstants.ATTACK]) * 0.15;
		}
		/**
		 * void bonus
		 */
		if (ItemUtility.hasVoidSet(c, ItemUtility.VoidSet.MELEE)) {
			attackLevel += c.getPA().getLevelForXP(c.xp[PlayerConstants.ATTACK]) * 0.1;
		}
		/**
		 * spec accuracy
		 */
		attackLevel *= c.specAccuracy;
		/**
		 * Equipment bonus
		 */
		int i = c.playerBonus[c.getCombatType().getAttackBonusIndex()];
		/***
		 * no idea what the fuck this is but it changes per attack
		 */
		i += c.bonusAttack;
		/**
		 *
		 */
		if (c.equipment[c.playerAmulet] == 11128 && c.equipment[Player.playerWeapon] == 6528) {
			i *= 1.30;
		}

		double bonus = 1;
		boolean spearReduction = false;
		/**
		 * slayer item bonuses
		 */
		if (c.npcIndex > 0) {
			NPC npc = NPCHandler.npcs[c.npcIndex];
			if (npc != null) {
				// spear & halberd reduction
				if (!hasSpear() && npc.type == 8133) {
					spearReduction = true;
					Random r = new Random();
					if (r.nextInt(15) + 1 == 5)
						c.sendMessage(
								"You are not dealing 100% damage to the beast. His weaknesses are spears and halberds.");
				}
				// black mask
				if (c.equipment[PlayerConstants.HAT] >= 8901 && c.equipment[PlayerConstants.HAT] <= 8922
						&& Slayer.isFightingSlayerTask(c, npc)) {
					bonus += 0.15;
				}
				// slayer helmet
				if (c.equipment[PlayerConstants.HAT] == 13263 && Slayer.isFightingSlayerTask(c, npc)) {
					bonus += 0.15;
				}
				// full slayer helmet
				if (c.equipment[PlayerConstants.HAT] == 15492 && Slayer.isFightingSlayerTask(c, npc)) {
					bonus += 0.3;
				}
			}
		}

		int hit = (int) Math.floor((attackLevel + i) * bonus) / (spearReduction ? 2 : 1);

		if (spearReduction)
			spearReduction = !spearReduction;
		/**
		 * need to find out where this algorithm comes from the 0.15 / 0.05 etc values
		 */
		return hit;// (int) (attackLevel + (attackLevel * 0.15) + (i + i * 0.05));
	}

	/**
	 * @param other the entity which is attacking us
	 * @return
	 */
	public int calculateMeleeDefence(Object other) {
		int defenceLevel = c.level[1];
		@SuppressWarnings("unused")
		int i = 0;
		if (other instanceof Player) {
			i = c.playerBonus[((Player) other).getCombatType().getDefenceBonusIndex()];
		} else {
			// npcs use this
			i = c.playerBonus[this.bestMeleeDef()];
		}
		/**
		 *
		 */
		if (c.prayerActive[0]) {
			defenceLevel += c.getPA().getLevelForXP(c.xp[PlayerConstants.DEFENCE]) * 0.05;
		} else if (c.prayerActive[5]) {
			defenceLevel += c.getPA().getLevelForXP(c.xp[PlayerConstants.DEFENCE]) * 0.1;
		} else if (c.prayerActive[13]) {
			defenceLevel += c.getPA().getLevelForXP(c.xp[PlayerConstants.DEFENCE]) * 0.15;
		} else if (c.prayerActive[24]) {
			defenceLevel += c.getPA().getLevelForXP(c.xp[PlayerConstants.DEFENCE]) * 0.2;
		} else if (c.prayerActive[25]) {
			defenceLevel += c.getPA().getLevelForXP(c.xp[PlayerConstants.DEFENCE]) * 0.25;
		} else if (c.curseActive[19]) {
			defenceLevel += c.getPA().getLevelForXP(c.xp[PlayerConstants.DEFENCE]) * 0.15;
		}
		/**
		 *
		 */
		return (int) (defenceLevel + (defenceLevel * 0.15) + (i + i * 0.05));
	}

	public int calculateMeleeMaxHit() {
		return MaxHits.calculateMeleeMaxHit(c);
	}

	/**
	 * Range
	 */
	public int calculateRangeAttack() {
		int attackLevel = c.level[4];
		attackLevel *= c.specAccuracy;
		if (ItemUtility.hasVoidSet(c, ItemUtility.VoidSet.RANGE)) {
			attackLevel *= 1.1;
		}
		if (c.prayerActive[3]) {
			attackLevel *= 1.05;
		} else if (c.prayerActive[11]) {
			attackLevel *= 1.10;
		} else if (c.prayerActive[19]) {
			attackLevel *= 1.15;
		}

		if (c.prayerActive[26]) { // rigour
			attackLevel *= 1.20;
		}

		// dbow spec
		if (ItemUtility.hasVoidSet(c, ItemUtility.VoidSet.RANGE) && c.specAccuracy > 1.15) {
			attackLevel *= 1.75;
		}

		/**
		 * slayer item bonuses
		 */
		if (c.npcIndex > 0) {
			NPC npc = NPCHandler.npcs[c.npcIndex];
			if (npc != null) {
				// focus sight
				if (c.equipment[PlayerConstants.HAT] == 15490 && Slayer.isFightingSlayerTask(c, npc)) {
					attackLevel *= 1.15;
				}
				// full slayer helmet
				if (c.equipment[PlayerConstants.HAT] == 15492 && Slayer.isFightingSlayerTask(c, npc)) {
					attackLevel *= 1.3;
				}
			}
		}

		int itemUsed = c.usingBow ? c.lastArrowUsed : c.lastWeaponUsed;
		int bonus = c.playerBonus[4];

		if (itemUsed == Blowpipe.BLOWPIPE) {
			if (c.getData().blowpipeAmmoId > 0) {
				Optional<ItemDefinition> definition = ItemDefinition.get(c.getData().blowpipeAmmoId);
				if (definition.isPresent()) {
					ItemDefinition def = definition.get();
					bonus += def.getBonuses()[4];
				}
			}
		}

		return (int) (attackLevel + (bonus * 1.95));
	}

	public int calculateRangeDefence() {
		int defenceLevel = c.level[1];
		if (c.prayerActive[0]) {
			defenceLevel += c.getPA().getLevelForXP(c.xp[PlayerConstants.DEFENCE]) * 0.05;
		} else if (c.prayerActive[5]) {
			defenceLevel += c.getPA().getLevelForXP(c.xp[PlayerConstants.DEFENCE]) * 0.1;
		} else if (c.prayerActive[13]) {
			defenceLevel += c.getPA().getLevelForXP(c.xp[PlayerConstants.DEFENCE]) * 0.15;
		} else if (c.prayerActive[24]) {
			defenceLevel += c.getPA().getLevelForXP(c.xp[PlayerConstants.DEFENCE]) * 0.2;
		} else if (c.prayerActive[25]) {
			defenceLevel += c.getPA().getLevelForXP(c.xp[PlayerConstants.DEFENCE]) * 0.25;
		} else if (c.prayerActive[26]) { // rigour
			defenceLevel += c.getPA().getLevelForXP(c.xp[PlayerConstants.DEFENCE]) * 0.25;
		} else if (c.prayerActive[27]) { // augury
			defenceLevel += c.getPA().getLevelForXP(c.xp[PlayerConstants.DEFENCE]) * 0.25;
		}
		return (int) (defenceLevel + c.playerBonus[9] + (c.playerBonus[9] / 2));
	}

	public boolean checkMagicReqs(int spell) {
		if (!c.usingMagic) {
			return true;
		}
		if (c.npcIndex < 0) {
			if (!c.inWild() && !c.inPits) {
				return false;
			}
		}
		if (c.npcIndex < 0 && c.playerIndex < 0) {
			return false;
		}
		/*
		 * if(!c.inWild() && !c.inPits && c.npcIndex == 0 && spell != 49 && spell != 50)
		 * { return false; }
		 */
		if (c.usingMagic && Config.RUNES_REQUIRED) { // check for runes
			for (int i = 8; i <= 14; i += 2) {
				int[] requiredRune = Player.MAGIC_SPELLS[spell];
				if (requiredRune[i] > 0) {
					if (!wearingStaff(requiredRune[i])
							&& !c.getItems().playerHasItem(requiredRune[i], requiredRune[i + 1])) {
						c.sendMessage("You don't have the required runes to cast this spell.");
						return false;
					}
				}
			}
		}
		if (c.usingMagic && c.playerIndex > 0) {
			if (PlayerHandler.players[c.playerIndex] != null) {
				for (int r = 0; r < c.REDUCE_SPELLS.length; r++) { // reducing
					// spells,
					// confuse
					// etc
					if (PlayerHandler.players[c.playerIndex].REDUCE_SPELLS[r] == Player.MAGIC_SPELLS[spell][0]) {
						c.reduceSpellId = r;
						if ((System.currentTimeMillis()
								- PlayerHandler.players[c.playerIndex].reduceSpellDelay[c.reduceSpellId]) > PlayerHandler.players[c.playerIndex].REDUCE_SPELL_TIME[c.reduceSpellId]) {
							PlayerHandler.players[c.playerIndex].canUseReducingSpell[c.reduceSpellId] = true;
						} else {
							PlayerHandler.players[c.playerIndex].canUseReducingSpell[c.reduceSpellId] = false;
						}
						break;
					}
				}
				if (!PlayerHandler.players[c.playerIndex].canUseReducingSpell[c.reduceSpellId]) {
					c.sendMessage("That player is currently immune to this spell.");
					c.usingMagic = false;
					c.stopMovement();
					this.resetPlayerAttack();
					return false;
				}
			}
		}
		int staffRequired = this.getStaffNeeded();
		if (c.usingMagic && staffRequired > 0 && Config.RUNES_REQUIRED) { // staff
			// required
			if (c.equipment[Player.playerWeapon] != staffRequired) {
				c.sendMessage(
						"You need a " + c.getItems().getItemName(staffRequired).toLowerCase() + " to cast this spell.");
				return false;
			}
		}
		if (c.usingMagic && Config.MAGIC_LEVEL_REQUIRED) { // check magic level
			if (c.level[6] < Player.MAGIC_SPELLS[spell][1]) {
				c.sendMessage(
						"You need to have a magic level of " + Player.MAGIC_SPELLS[spell][1] + " to cast this spell.");
				return false;
			}
		}
		if (c.usingMagic && Config.RUNES_REQUIRED) {
			if (c.equipment[Player.playerWeapon] == 15486 && Misc.random(8) == 1) {
				c.sendMessage("You didn't use any runes for this spell.");
				return true;
			}
			if (Player.MAGIC_SPELLS[spell][8] > 0) { // deleting runes
				if (!this.wearingStaff(Player.MAGIC_SPELLS[spell][8])) {
					c.getItems().deleteItem(Player.MAGIC_SPELLS[spell][8],
							c.getItems().getItemSlot(Player.MAGIC_SPELLS[spell][8]), Player.MAGIC_SPELLS[spell][9]);
				}
			}
			if (Player.MAGIC_SPELLS[spell][10] > 0) {
				if (!this.wearingStaff(Player.MAGIC_SPELLS[spell][10])) {
					c.getItems().deleteItem(Player.MAGIC_SPELLS[spell][10],
							c.getItems().getItemSlot(Player.MAGIC_SPELLS[spell][10]), Player.MAGIC_SPELLS[spell][11]);
				}
			}
			if (Player.MAGIC_SPELLS[spell][12] > 0) {
				if (!this.wearingStaff(Player.MAGIC_SPELLS[spell][12])) {
					c.getItems().deleteItem(Player.MAGIC_SPELLS[spell][12],
							c.getItems().getItemSlot(Player.MAGIC_SPELLS[spell][12]), Player.MAGIC_SPELLS[spell][13]);
				}
			}
			if (Player.MAGIC_SPELLS[spell][14] > 0) {
				if (!this.wearingStaff(Player.MAGIC_SPELLS[spell][14])) {
					c.getItems().deleteItem(Player.MAGIC_SPELLS[spell][14],
							c.getItems().getItemSlot(Player.MAGIC_SPELLS[spell][14]), Player.MAGIC_SPELLS[spell][15]);
				}
			}
		}
		return true;
	}

	public boolean checkMultiBarrageReqs(int i) {
		if (PlayerHandler.players[i] == null) {
			return false;
		}
		if (i == c.playerId) {
			return false;
		}
		if (c.inPits && PlayerHandler.players[i].inPits) {
			return true;
		}
		if (!PlayerHandler.players[i].inWild()) {
			return false;
		}
		/*
		 * if(Config.COMBAT_LEVEL_DIFFERENCE && !c.inFunPk() &&
		 * !Server.playerHandler.players[i].inFunPk()) { int combatDif1 =
		 * c.getCombat().getCombatDifference(c.combatLevel,
		 * Server.playerHandler.players[i].combatLevel); if(combatDif1 > c.wildLevel ||
		 * combatDif1 > Server.playerHandler.players[i].wildLevel) { c.sendMessage(
		 * "3Your combat level difference is too great to attack that player here." );
		 * return false; } }
		 */
		if (Config.SINGLE_AND_MULTI_ZONES) {
			if (!Areas.inMulti(PlayerHandler.players[i])) { // single combat
				// zones
				if (PlayerHandler.players[i].underAttackBy != c.playerId
						&& PlayerHandler.players[i].underAttackBy != 0) {
					return false;
				}
				if (PlayerHandler.players[i].playerId != c.underAttackBy && c.underAttackBy != 0) {
					c.sendMessage("You are already in combat.");
					return false;
				}
			}
		}
		return true;
	}

	public boolean checkMultiBarrageReqsNPC(int i) {
		if (NPCHandler.npcs[i] == null) {
			return false;
		} else if (NPCHandler.npcs[i].type == 3782) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Wildy and duel info
	 */

	public boolean checkReqs() {
		if (PlayerHandler.players[c.playerIndex] == null) {
			return false;
		}

		Player target = PlayerHandler.players[c.playerIndex];

		if (c.playerIndex == c.playerId) {
			return false;
		}
		if (c.inPits && target.inPits) {
			return true;
		}
		if (!target.canBeAttacked) {
			c.sendMessage("This player cannot be attacked.");
			return false;
		}

		if (c.getCastleWarsState() != null) {
			return true;
		}

		if (c.getDuel() != null && c.getDuel().getStage() == DuelArena.Stage.FIGHTING) {
			if (c.playerIndex == c.getDuel().getpId()) {
				return true;
			} else {
				c.sendMessage("This isn't your opponent!");
				return false;
			}
		}

		/*
		 * if(!Server.playerHandler.players[c.playerIndex].inWild() && !c.inFunPk() &&
		 * !c.inPits() && !c.inClanArena() &&
		 * !Server.playerHandler.players[c.playerIndex].inClanArena() && c.rights != 3)
		 * { c.sendMessage("That player is not in the wilderness."); c.stopMovement();
		 * c.getCombat().resetPlayerAttack(); return false; } if(!c.inWild() &&
		 * !c.inPits() && !c.inFunPk() && !c.inClanArena() && c.rights != 3) {
		 * c.sendMessage("You are not in the wilderness."); c.stopMovement();
		 * c.getCombat().resetPlayerAttack(); return false; }
		 */
		/*
		 * if (!c.inFightPits() && !c.inClanArena()) { // if(c.inSafeZone() &&
		 * c.safeTimer <= 0) { c.sendMessage("@red@You are not in PvP.");
		 * c.stopMovement(); c.getCombat().resetPlayerAttack(); return false; }
		 */

		/*
		 * if(!c.inWild() && !c.inPits() && !c.inFunPk() && !c.inClanArena() && c.rights
		 * != 3) { c.sendMessage("You are not in the wilderness."); c.stopMovement();
		 * c.getCombat().resetPlayerAttack(); return false; }
		 */
		/*
		 * if(Config.COMBAT_LEVEL_DIFFERENCE && !c.inPits() && !c.inFunPk() &&
		 * !c.inClanArena() && c.rights != 3 && !c.inTrinityWar()) { int combatDif1 =
		 * c.getCombat().getCombatDifference(c.combatLevel,
		 * Server.playerHandler.players[c.playerIndex].combatLevel); if(combatDif1 >
		 * c.wildLevel || combatDif1 >
		 * Server.playerHandler.players[c.playerIndex].wildLevel) { c.sendMessage
		 * ("2Your combat level difference is too great to attack that player here." );
		 * c.stopMovement(); c.getCombat().resetPlayerAttack(); return false; } }
		 */

		if (!target.inWild() && !c.inPits()) {
			c.sendMessage("That player is not in the wilderness.");
			c.stopMovement();
			c.getCombat().resetPlayerAttack();
			return false;
		}
		if (!c.inWild() && !c.inPits() && c.rights != 3) {
			c.sendMessage("You are not in the wilderness.");
			c.stopMovement();
			c.getCombat().resetPlayerAttack();
			return false;
		}
		if (Config.COMBAT_LEVEL_DIFFERENCE && !c.inPits() && c.rights != 3 && !c.inTrinityWar()
				&& !ClanWars.inArena(c)) {

			int myCombat = c.combatLevel;
			if (c.getFamiliar() == null && !Summoning.hasPoucheInInventory(c)) {
				myCombat -= c.getSummoningCombat();
			}

			int targetCombat = target.combatLevel;
			if (target.getFamiliar() == null && !Summoning.hasPoucheInInventory(target)) {
				targetCombat -= target.getSummoningCombat();
			}

			int combatDif1 = c.getCombat().getCombatDifference(myCombat, targetCombat);
			if (combatDif1 > c.wildLevel || combatDif1 > target.wildLevel) {
				c.sendMessage("Your combat level difference is too great to attack that player here.");
				c.stopMovement();
				c.getCombat().resetPlayerAttack();
				return false;
			}
		}

		if (Config.SINGLE_AND_MULTI_ZONES && c.rights != 3) {
			if (!Areas.inMulti(target) && !c.inPits() && !ClanWars.inArena(c)) { // single combat zones
				if (target.underAttackBy != c.playerId && target.underAttackBy != 0) {
					c.sendMessage("That player is already in combat.");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return false;
				}
				if (target.playerId != c.underAttackBy && c.underAttackBy != 0
						|| c.underAttackByNpcID > 0 && !c.inPits()) {
					c.sendMessage("You are already in combat.");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return false;
				}
			}
		}
		return true;
	}

	public boolean checkSpecAmount(int weapon) {
		switch (weapon) {
		case 1249:
		case 1215:
		case 1231:
		case 10887:
		case 5680:
		case 5698:
		case 1305:
		case 1434:
		case 13899:
			double amount = 2.5;
			if (c.equipment[PlayerConstants.RING] == 19669) {
				amount *= 0.90;
			}

			if (c.specAmount >= amount) {
				c.specAmount -= amount;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;
		case 25040: // blowpipe
		case 4151:
		case 15441:
		case 15442:
		case 15443:
		case 15444:
		case 14661:
		case 11694:
		case 14484:
		case 11698:
		case 13905:
		case 13879:
		case 4153:
		case 15241:
		case 13883: // morrigans throwing axe
			amount = 5;
			if (c.equipment[PlayerConstants.RING] == 19669) {
				amount *= 0.90;
			}

			if (c.specAmount >= amount) {
				c.specAmount -= amount;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;
		case 21371:
			amount = 6;
			if (c.equipment[PlayerConstants.RING] == 19669) {
				amount *= 0.90;
			}

			if (c.specAmount >= amount) {
				c.specAmount -= amount;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;
		case 19784:
		case 19780:
			amount = 6;
			if (c.equipment[PlayerConstants.RING] == 19669) {
				amount *= 0.90;
			}

			if (c.specAmount >= amount) {
				c.specAmount -= amount;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;
		case 3204:
		case 13904:
		case 13902:
			amount = 3;
			if (c.equipment[PlayerConstants.RING] == 19669) {
				amount *= 0.90;
			}

			if (c.specAmount >= amount) {
				c.specAmount -= amount;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;
		case 1377:
		case 11696:
		case 15001:
		case 11730:
		case 7158:
			amount = 10;
			if (c.equipment[PlayerConstants.RING] == 19669) {
				amount *= 0.90;
			}

			if (c.specAmount >= amount) {
				c.specAmount -= amount;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;
		case 25053: // d warhammer
		case 20173:
			amount = 5;
			if (c.equipment[PlayerConstants.RING] == 19669) {
				amount *= 0.90;
			}

			if (c.specAmount >= amount) {
				c.specAmount -= amount;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;
		case 4587:
		case 859:
		case 861:
		case 11235:
		case 11700:
			amount = 5.5;
			if (c.equipment[PlayerConstants.RING] == 19669) {
				amount *= 0.90;
			}

			if (c.specAmount >= amount) {
				c.specAmount -= amount;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;
		default:
			return true; // incase u want to test a weapon
		}
	}

	public void curseActivateGFX(int i) {
		switch (i) {
		case 0:
			c.gfx0(2213);
			c.startAnimation(12567);
			break;
		case 5:
			c.gfx0(2266);
			c.startAnimation(12589);
			c.sendMessage("You somehow feel your boosted stats will last longer.");
			break;
		case 19:
			c.gfx0(2226);
			c.startAnimation(12565);
			break;
		}
	}

	public void deflectDamage(int damage, int targetFocus) {
		int damage2 = 0;
		if (damage < 10) {
			damage2 = 0;
		} else {
			damage2 = damage / 10;
		}
		c.dealDamage(damage2);
		c.handleHitMask(damage2, 0, 5, 0);// Zero, will never be critical
		c.sendMessage("Your opponent deflects some damage back.");
	}

	public void delayedHit(int i) { // npc hit delay

		NPC npc = NPCHandler.npcs[i];

		if (npc != null) {
			if (npc.attackTimer <= 3 && !NPCConfig.canAttack(npc.type)
					|| npc.attackTimer == 0 && npc.oldIndex == 0 && !c.castingMagic && !NPCConfig.canAttack(npc.type)) {

				if (npc.type < 2042 && npc.type > 2045) {
					npc.animNumber = NPCAnimations.getBlockEmote(npc.type);
					npc.updateRequired = true;
					npc.animUpdateRequired = true;
				}
			}
			if (npc.isDead) {
				c.npcIndex = 0;
				return;
			}

			if (c.equipment[PlayerConstants.HAT] == SerpentineHelmet.HELM) {
				c.getData().serpentineHelmetCharges--;
				if (c.getData().serpentineHelmetCharges <= 0) {
					c.getItems().setEquipment(-1, 0, PlayerConstants.HAT);
					c.getItems().addOrBank(SerpentineHelmet.UNCHARGED_HELM, 1);
					c.sendMessage("Your Serpentine Helmet has ran out of charges and has been unequipped.");
				}
			}

			// In addition, if an opponent attacks a player with the helmet equipped, there
			// is a 1 in 6 chance (16.7%) the opponent will be inflicted with venom
			if (c.equipment[PlayerConstants.HAT] == SerpentineHelmet.HELM) {
				if (Misc.random(5) == 0) {
					npc.appendPoison(6);
				}
			}

			npc.facePlayer(c.playerId);
			if (npc.underAttackBy > 0 && NPCHandler.getsPulled(i)) {
				npc.killerId = c.playerId;
			} else if (npc.underAttackBy < 0 && !NPCHandler.getsPulled(i)) {
				npc.killerId = c.playerId;
			}
			if (c.projectileStage == 0 && !c.usingMagic && !c.castingMagic) { // melee
				// hit
				// damage
				if (!c.usingClaws) {
					this.applyNpcMeleeDamage(i, 1, Misc.random(this.calculateMeleeMaxHit()));
				}
				if (c.doubleHit && !c.usingClaws) {
					this.applyNpcMeleeDamage(i, 2, Misc.random(this.calculateMeleeMaxHit()));
				}
				/*
				 * if(c.vengOn && damage > 0) { appendNPCVengeance(i, damage); }
				 */
				if (c.doubleHit && c.usingClaws) {
					c.delayedDamage = c.clawDamage;
					c.delayedDamage2 = c.clawDamage / 2;
					this.applyNpcMeleeDamage(i, 1, c.clawDamage);
					this.applyNpcMeleeDamage(i, 2, c.clawDamage / 2);
				}
			}
			this.addCharge();
			if (!c.castingMagic && c.projectileStage > 0) { // range hit damage
				int maxHit = this.rangeMaxHit();

				NpcDefinition definition = NPCHandler.getNPCDefinition(npc.type);
				if (definition.getWeakness().equals(Weaknesses.RANGE)) {
					maxHit *= 1.15;
				}

				int damage = Misc.random(maxHit);
				int damage2 = -1;
				boolean ignoreDef = false;
				if (Misc.random(5) == 1 && c.lastArrowUsed == 9243) {
					ignoreDef = true;
					npc.gfx0(758);
				}
				if (Misc.random(npc.defence) > Misc.random(10 + this.calculateRangeAttack()) && !ignoreDef) {
					damage = 0;
				} else if (npc.type == 2881 || npc.type == 2883 && !ignoreDef) {
					damage = 0;
				}

				/// warriors guild animators
				if (npc.type >= 4278 && npc.type <= 4284) {
					damage = 0;
				}

				if (Misc.random(4) == 1 && c.lastArrowUsed == 9242 && damage > 0
						&& c.lastWeaponUsed != Blowpipe.BLOWPIPE) {

					boolean effect = true;
					if (npc.type >= 13447 && npc.type <= 13450) {
						Nex nex = (Nex) npc.getCombat();
						if (nex.minionSpawned()) {
							effect = false;
						}
					}

					if (effect) {
						int playerHp = (int) Math.round(c.level[3] * 0.10);

						// check we have the hp to spare
						if (c.level[PlayerConstants.HITPOINTS] - playerHp > 0) {
							npc.gfx100(754);

							// the 20%
							int hp = (int) Math.round(npc.getHP() * 0.20);
							if (npc.getHP() - hp < 0) {
								hp = npc.getHP();
							}
							npc.dealdamage(c.playerIndex, hp);
							npc.handleHitMask(hp, 100, 2, 0);

							// the 10%
							c.dealDamage(playerHp);
							c.handleHitMask(playerHp, 100, 2, 0);// I don't
						}
					}
				}

				if (c.lastWeaponUsed == Blowpipe.BLOWPIPE && Misc.random(0, 3) == 0) {
					npc.appendPoison(6);
				}

				if (c.lastWeaponUsed == 11235 || c.bowSpecShot == 1 || c.dbowSpec && c.lastWeaponUsed == 20173) {
					damage2 = Misc.random(this.rangeMaxHit());
				}
				if (this.usingHCSpec() && !c.dbowSpec) {
					damage2 = Misc.random(this.rangeMaxHit());
					c.hcSpec = false;
				}

				/// warriors guild animators
				if (npc.type >= 4278 && npc.type <= 4284) {
					damage2 = 0;
				}

				if (c.dbowSpec && !this.usingHC()) {
					npc.gfx100(c.lastWeaponUsed == 20173 ? 576 : 1100);
					if (damage < 8 && c.bowDelay == 2) {
						damage = c.lastWeaponUsed == 20173 ? damage : 8;
					}
					if (damage2 < 8 && c.bowDelay == 1) {
						damage2 = c.lastWeaponUsed == 20173 ? damage2 : 8;
					}
					c.dbowSpec = false;
					c.bowDelay = 0;
				}

				// chins
				if (Areas.inMulti(c)) {
					if (c.equipment[Player.playerWeapon] == 10033 || c.equipment[Player.playerWeapon] == 10034) {

						List<NPC> npcs = new ArrayList<>();
						for (NPC n : NPCHandler.npcs) {
							if (n == null || n == npc || !n.isWithinDistance(c.absX, c.absY, 2)) {
								continue;
							}

							if (npcs.size() >= 3) {
								continue;
							}

							npcs.add(n);
						}

						final int chinDamage = damage;

						npcs.forEach(x -> {

							int d = chinDamage;

							if (d >= x.getHP()) {
								d = x.getHP();
							}

							x.underAttack = true;
							x.underAttackBy = c.playerId;
							x.handleHitMask(d, c.getCombat().rangeMaxHit(), 2, i);
							x.updateRequired = true;
							c.killingNpcIndex = c.oldNpcIndex;
							x.dealdamage(c.getDatabaseId(), d);
						});
					}
				}

				if (c.doJavelinSpecial && c.equipment[PlayerConstants.WEAPON] == 13879) {
					npc.javelinDamage = damage;
					GameCycleTaskHandler.addEvent(npc, new GameCycleTask() {
						@Override
						public void execute(GameCycleTaskContainer container) {

							if (npc.isDead()) {
								container.stop();
								return;
							}

							int damage;
							if (npc.javelinDamage >= 5) {
								damage = 5;
							} else {
								damage = npc.javelinDamage;
							}
							npc.dealdamage(c.getDatabaseId(), damage);
							npc.handleHitMask(damage, c.getCombat().rangeMaxHit(), 2, i);
							// npc.getPA().refreshSkill(3);
							npc.updateRequired = true;
							npc.javelinDamage -= damage;
							if (npc.javelinDamage <= 0) {
								container.stop();
							}
						}
					}, 3);
					c.doJavelinSpecial = false;
				}

				if (damage > 0 && Misc.random(5) == 1 && c.lastArrowUsed == 9244 && !this.usingHC()) {
					damage *= 1.45;
					npc.gfx0(756);
				}

				// slayer range
				if (Slayer.isFightingSlaverNPC(c, npc, Tasks.GARGOYLE)) {
					if (!c.getItems().playerHasItem(4162)) {
						if (damage >= npc.getHP()) {
							damage = 0;
						}
					}
				}
				if (Slayer.isFightingSlaverNPC(c, npc, Tasks.TUROTH)
						|| Slayer.isFightingSlaverNPC(c, npc, Tasks.KURASK)) {
					if (c.equipment[PlayerConstants.WEAPON] != 13290 && c.equipment[PlayerConstants.WEAPON] != 4158) {
						damage = 0;
					}
				}
				if (Slayer.isFightingSlaverNPC(c, npc, Tasks.ROCKSLUG) && npc.getHP() <= 25) {
					if (!c.getItems().playerHasItem(4161)) {
						damage = 0;
					} else {
						c.getItems().deleteItem(4161, 1);
						damage = npc.getHP();
					}
				}

				// glacor damage
				if (npc.type == 14301) {
					damage = (int) (damage * 0.10);
					damage2 = 0;
				}

				if (npc.getHP() - damage < 0) {
					damage = npc.getHP();
				}
				if (npc.getHP() - damage <= 0 && damage2 > 0) {
					damage2 = 0;
				}

				if (npc.getCombat() != null && (damage > 0 || damage2 > 0)) {
					damage = npc.getCombat().damageDealtByPlayer(c, damage);
					if (damage2 > 0)
						damage2 = npc.getCombat().damageDealtByPlayer(c, damage2);
				}

				if (Zombies.inGameArea(c.getLocation())) {

					Game game = (Game) c.getAttribute("zombie_game");
					if (game != null && game.instantKill()) {
						damage = npc.getHP();
					}
				}

				if (c.inPestControl) {
					PestControl.dealtNpcDamage(c, damage);
					PestControl.dealtNpcDamage(c, damage2);
				}

				if (c.soulSplitDelay <= 0) {
					this.applySoulSplitNPC(i, damage);
				}

				if (damage > 0) {
					int xp = damage * 4;
					if (c.getCombatStyle() == CombatStyle.LONG_RANGE) {
						c.getPA().addSkillXP(xp / 3, 4);
						c.getPA().addSkillXP(xp / 3, 1);
						c.getPA().addSkillXP(xp / 3, 3);
					} else {
						c.getPA().addSkillXP(xp, 4);
						c.getPA().addSkillXP(xp / 3, 3);
					}
				}
				if (damage > 0) {
					if (npc.type >= 6142 && npc.type <= 6145) {
						c.pcDamage += damage;
					}
				}
				boolean dropArrows = true;
				for (int noArrowId : c.NO_ARROW_DROP) {
					if (c.lastWeaponUsed == noArrowId) {
						dropArrows = false;
						break;
					}
				}
				if (dropArrows) {
					c.getItems().dropArrowNpc();
				}
				npc.underAttack = true;
				if (damage2 > -1) {
					npc.handleHitMask(damage2, this.rangeMaxHit(), 2, i);
					npc.dealdamage(c.getDatabaseId(), damage2);
				}
				c.killingNpcIndex = c.oldNpcIndex;
				npc.dealdamage(c.getDatabaseId(), damage);

				npc.handleHitMask(damage, this.rangeMaxHit(), 2, i);
				if (damage2 > -1) {
					npc.hitUpdateRequired2 = true;
				}
				npc.updateRequired = true;
			} else if (c.projectileStage > 0) { // magic hit damage search op
				// barrage
				int maxDamage = MaxHits.calculateMagicMaxHit(c, c.oldSpellId);

				NpcDefinition definition = NPCHandler.getNPCDefinition(npc.type);
				if (definition.getWeakness().equals(Weaknesses.MAGIC)) {
					maxDamage *= 1.15;
				}

				if (definition.getWeakness().equals(Weaknesses.AIR)) {
					switch (Player.MAGIC_SPELLS[c.oldSpellId][0]) {
					case 1152: // wind strike
					case 1160: // wind bolt
					case 1172: // wind blast
					case 1183: // wind wave
					case 54270: // air surge
						maxDamage *= 1.15;
						break;
					}
				}

				if (definition.getWeakness().equals(Weaknesses.WATER)) {
					switch (Player.MAGIC_SPELLS[c.oldSpellId][0]) {
					case 1154: // water strike
					case 1163: // water bolt
					case 1175: // water blast
					case 1185: // water wave
					case 54280: // water surge
						maxDamage *= 1.15;
						break;
					}
				}

				if (definition.getWeakness().equals(Weaknesses.EARTH)) {
					switch (Player.MAGIC_SPELLS[c.oldSpellId][0]) {
					case 1156: // earth strike
					case 1166: // earth bolt
					case 1177: // earth blast
					case 1188: // earth wave
					case 54292: // earth surge
						maxDamage *= 1.15;
						break;
					}
				}

				if (definition.getWeakness().equals(Weaknesses.FIRE)) {
					switch (Player.MAGIC_SPELLS[c.oldSpellId][0]) {
					case 1158: // fire strike
					case 1169: // fire bolt
					case 1181: // fire blast
					case 1189: // fire wave
					case 54300: // fire surge
						maxDamage *= 1.15;
						break;
					}
				}

				int damage = Misc.random(maxDamage);

				if (Player.MAGIC_SPELLS[c.oldSpellId][0] == 54264) {
					int[] armadylDamage = MaxHits.getArmadylStormDamage(c.level[PlayerConstants.MAGIC]);
					maxDamage = armadylDamage[1];
					damage = Misc.random(armadylDamage[0], armadylDamage[1]);
				}

				if (c.equipment[PlayerConstants.WEAPON] == TridentOfTheSeas.CHARGED_TRIDENT) {
					c.getData().tridentSeaCharges--;
					if (c.getData().tridentSeaCharges <= 0) {
						c.sendMessage("Your staff has ran out of charges.");
					}

					damage = 23;
					int magic = c.getPA().getActualLevel(PlayerConstants.MAGIC);
					damage += (int) Math.floor((double) (magic - 75) / (double) 3);
					damage = Misc.random(damage);
				}

				if (c.equipment[PlayerConstants.WEAPON] == TridentOfTheSwamp.CHARGED_TRIDENT) {
					c.getData().tridentSwampCharges--;
					if (c.getData().tridentSwampCharges <= 0) {
						c.sendMessage("Your staff has ran out of charges.");
					}

					damage = 28;
					int magic = c.getPA().getActualLevel(PlayerConstants.MAGIC);
					damage += (int) Math.floor((double) (magic - 75) / (double) 3);
					damage = Misc.random(damage);

					if (Misc.random(4) == 0) {
						npc.appendPoison(6);
					}
				}

				if (this.godSpells()) {
					if (System.currentTimeMillis() - c.godSpellDelay < Config.GOD_SPELL_CHARGE) {
						damage += Misc.random(10);
					}
				}
				if (c.equipment[Player.playerWeapon] == 15001) {
					damage += 10;
				}
				boolean magicFailed = false;
				// c.npcIndex = 0;
				int bonusAttack = this.getBonusAttack(i);
				if (Misc.random(npc.defence) > 10 + Misc.random(this.mageAtk()) + bonusAttack) {
					damage = 0;
					magicFailed = true;
				} else if (npc.type == 2881 || npc.type == 2882) {
					damage = 0;
					magicFailed = true;
				}

				/// warriors guild animators
				if (npc.type >= 4278 && npc.type <= 4284) {
					damage = 0;
					magicFailed = true;
				}

				if (npc.getCombat() != null && damage > 0) {
					damage = npc.getCombat().damageDealtByPlayer(c, damage);
				}

				for (int j = 0; j < NPCHandler.npcs.length; j++) {
					if (NPCHandler.npcs[j] != null && NPCHandler.npcs[j].maxHP > 0) {

						if (c.heightLevel != NPCHandler.npcs[j].heightLevel) {
							continue;
						}

						int nX = NPCHandler.npcs[j].getX();
						int nY = NPCHandler.npcs[j].getY();
						int pX = npc.getX();
						int pY = npc.getY();
						if ((nX - pX == -1 || nX - pX == 0 || nX - pX == 1)
								&& (nY - pY == -1 || nY - pY == 0 || nY - pY == 1)) {
							if (this.multiSpell() && Areas.inMulti(npc)) {
								if (NPCHandler.npcs[j] == npc) {
									continue;
								}
								this.appendMultiBarrageNPC(j, c.magicFailed);
								NPCHandler.attackPlayer(c, NPCHandler.npcs[j]);
							}
						}
					}
				}

				// slayer magic
				if (Slayer.isFightingSlaverNPC(c, npc, Tasks.GARGOYLE)) {
					if (!c.getItems().playerHasItem(4162)) {
						if (damage >= npc.getHP()) {
							damage = 0;
						}
					}
				}
				if (Slayer.isFightingSlaverNPC(c, npc, Tasks.TUROTH)
						|| Slayer.isFightingSlaverNPC(c, npc, Tasks.KURASK)) {
					if (c.equipment[PlayerConstants.WEAPON] != 13290 && c.equipment[PlayerConstants.WEAPON] != 4158) {
						damage = 0;
					}
				}
				if (Slayer.isFightingSlaverNPC(c, npc, Tasks.ROCKSLUG) && npc.getHP() <= 25) {
					if (!c.getItems().playerHasItem(4161)) {
						damage = 0;
					} else {
						c.getItems().deleteItem(4161, 1);
						damage = npc.getHP();
					}
				}

				// glacor damage
				int spellId = Player.MAGIC_SPELLS[c.oldSpellId][0];
				if (npc.type == 14301 && spellId != 1158 && spellId != 1169 && spellId != 1181 && spellId != 1189
						&& spellId != 54300) {
					damage = (int) (damage * 0.10);
				}

				if (npc.getHP() - damage < 0) {
					damage = npc.getHP();
				}

				if (Zombies.inGameArea(c.getLocation())) {

					Game game = (Game) c.getAttribute("zombie_game");
					if (game != null && game.instantKill()) {
						damage = npc.getHP();
					}
				}

				if (c.inPestControl) {
					PestControl.dealtNpcDamage(c, damage);
				}

				if (c.soulSplitDelay <= 0) {
					this.applySoulSplitNPC(i, damage);
				}

				if (damage > 0) {
					if (c.equipment[PlayerConstants.WEAPON] == TridentOfTheSeas.CHARGED_TRIDENT
							|| c.equipment[PlayerConstants.WEAPON] == TridentOfTheSwamp.CHARGED_TRIDENT) {
						c.getPA().addSkillXP((damage * 2), 6);
						c.getPA().addSkillXP((damage * 2) / 3, 3);
					} else {
						c.getPA().addSkillXP(Player.MAGIC_SPELLS[c.oldSpellId][7] + (damage * 2), 6);
						c.getPA().addSkillXP(Player.MAGIC_SPELLS[c.oldSpellId][7] + (damage * 2) / 3, 3);
					}
				}
				if (damage > 0) {
					if (npc.type >= 6142 && npc.type <= 6145) {
						c.pcDamage += damage;
					}
				}
				if (!magicFailed && npc.type < 2042 && npc.type > 2045) { // end GFX
					if (this.getEndGfxHeight() == 100) {
						npc.gfx100(Player.MAGIC_SPELLS[c.oldSpellId][5]);
					} else {
						npc.gfx0(Player.MAGIC_SPELLS[c.oldSpellId][5]);
					}
				}
				if (magicFailed && npc.type < 2042 && npc.type > 2045) {
					npc.gfx100(85);
				}
				if (!magicFailed) {
					int freezeDelay = this.getFreezeTime();// freeze
					if (freezeDelay > 0 && npc.freezeTimer == 0) {
						npc.freezeTimer = freezeDelay;
					}
					switch (Player.MAGIC_SPELLS[c.oldSpellId][0]) {
					case 12901:
					case 12919: // blood spells
					case 12911:
					case 12929:
						int boost = Food.getHpBoost(c);
						int heal = (int) (damage / 4);
						if (c.level[3] + heal > c.getPA().getLevelForXP(c.xp[3]) + boost) {
							c.level[3] = c.getPA().getLevelForXP(c.xp[3]) + boost;
						} else {
							c.level[3] += heal;
						}
						c.getPA().refreshSkill(3);
						break;
					}
				}
				npc.underAttack = true;
				if (Player.MAGIC_SPELLS[c.oldSpellId][6] != 0) {
					npc.handleHitMask(damage, maxDamage, 3, i);
					npc.hitDiff = damage;
					npc.hitUpdateRequired = true;
					npc.dealdamage(c.getDatabaseId(), damage);
				}
				c.killingNpcIndex = c.oldNpcIndex;
				npc.updateRequired = true;
				c.usingMagic = false;
				c.castingMagic = false;
				c.dbowSpec = false;
				c.oldSpellId = 0;
			}
		}
		if (c.bowSpecShot <= 0) {
			c.oldNpcIndex = 0;
			c.projectileStage = 0;
			c.doubleHit = false;
			c.lastWeaponUsed = 0;
			c.bowSpecShot = 0;
		}
		if (c.bowSpecShot >= 2) {
			c.bowSpecShot = 0;
			// c.attackTimer =
			// getAttackDelay(c.getItems().getItemName(c.equipment[c.playerWeapon]).toLowerCase());
		}
		if (c.bowSpecShot == 1) {
			this.fireProjectileNpc();
			c.hitDelay = 2;
			c.bowSpecShot = 0;
		}
	}

	public void fireProjectileNpc() {
		if (c.oldNpcIndex > 0) {
			if (NPCHandler.npcs[c.oldNpcIndex] != null) {

				c.projectileStage = 2;

				int pX = c.getX();
				int pY = c.getY();
				int nX = NPCHandler.npcs[c.oldNpcIndex].getX();
				int nY = NPCHandler.npcs[c.oldNpcIndex].getY();
				int offX = (pY - nY) * -1;
				int offY = (pX - nX) * -1;

				if (c.attributeExists("zulrah_special")) {
					c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, this.getProjectileSpeed(), 1043, 43, 31,
							c.oldNpcIndex + 1, this.getStartDelay());
				} else if (!this.usingHC()) {
					c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, this.getProjectileSpeed(),
							this.getRangeProjectileGFX(), 43, 31, c.oldNpcIndex + 1, this.getStartDelay());
				}
				if (c.lastWeaponUsed == 20173 && c.dbowSpec) {
					c.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, this.getProjectileSpeed(),
							this.getRangeProjectileGFX(), 60, 31, -c.oldPlayerIndex - 1, this.getStartDelay(), 35);
				}
				if (this.usingDbow()) {
					c.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, this.getProjectileSpeed(),
							this.getRangeProjectileGFX(), 60, 31, c.oldNpcIndex + 1, this.getStartDelay(), 35);
				}
				if (this.usingHC()) {
					c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, this.getProjectileSpeed(),
							this.getRangeProjectileGFX(), 31, 31, c.oldNpcIndex + 1, this.getStartDelay());
					c.gfx0(2141);
				}
				if (this.usingHCSpec()) {
					c.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, 150, this.getRangeProjectileGFX(), 31,
							31, c.oldNpcIndex + 1, 65, 10);
				}
			}
		}
	}

	public void fireProjectilePlayer() {
		if (c.oldPlayerIndex > 0) {
			if (PlayerHandler.players[c.oldPlayerIndex] != null) {
				c.projectileStage = 2;
				int pX = c.getX();
				int pY = c.getY();
				int oX = PlayerHandler.players[c.oldPlayerIndex].getX();
				int oY = PlayerHandler.players[c.oldPlayerIndex].getY();
				int offX = (pY - oY) * -1;
				int offY = (pX - oX) * -1;

				if (c.attributeExists("zulrah_special")) {
					c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, this.getProjectileSpeed(), 1043, 43, 31,
							-c.oldPlayerIndex - 1, this.getStartDelay());
				} else if (!c.msbSpec && !this.usingHC()) {
					c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, this.getProjectileSpeed(),
							this.getRangeProjectileGFX(), 43, 31, -c.oldPlayerIndex - 1, this.getStartDelay());
				} else if (c.msbSpec) {
					c.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, this.getProjectileSpeed(),
							this.getRangeProjectileGFX(), 43, 31, -c.oldPlayerIndex - 1, this.getStartDelay(), 10);
					c.msbSpec = false;
				}
				if (this.usingDbow()) {
					c.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, this.getProjectileSpeed(),
							this.getRangeProjectileGFX(), 60, 31, -c.oldPlayerIndex - 1, this.getStartDelay(), 35);
				}
				if (c.lastWeaponUsed == 20173 && c.dbowSpec) {
					c.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, this.getProjectileSpeed(),
							this.getRangeProjectileGFX(), 60, 31, -c.oldPlayerIndex - 1, this.getStartDelay(), 35);
				}
				if (this.usingHC()) {
					c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, this.getProjectileSpeed(),
							this.getRangeProjectileGFX(), 31, 31, c.oldNpcIndex + 1, this.getStartDelay());
					c.gfx0(2141);
				}
				if (this.usingHCSpec()) {
					c.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, 150, this.getRangeProjectileGFX(), 31,
							31, c.oldNpcIndex + 1, 65, 10);
				}
			}
		}
	}

	public void freezePlayer(int i) {
	}

	/**
	 * Weapon and magic attack speed!
	 */

	public int getAttackDelay(String s) {
		if (c.usingMagic) {

			if (c.equipment[PlayerConstants.WEAPON] == TridentOfTheSwamp.CHARGED_TRIDENT) {
				return 6;
			}

			if (c.equipment[PlayerConstants.WEAPON] == TridentOfTheSeas.CHARGED_TRIDENT) {
				return 4;
			}

			// 2.4 armadyl storm with staff
			if (Player.MAGIC_SPELLS[c.spellId][0] == 54264 && c.equipment[PlayerConstants.WEAPON] == 21777) {
				return 4;
			}

			switch (Player.MAGIC_SPELLS[c.spellId][0]) {
			case 12871: // ice blitz
			case 13023: // shadow barrage
			case 12891: // ice barrage
				return 5;
			default:
				return 5;
			}
		}
		if (c.equipment[Player.playerWeapon] == -1) {
			return 4;// unarmed
		}
		switch (c.equipment[Player.playerWeapon]) {
		case Blowpipe.BLOWPIPE:
			if (c.playerIndex > 0) {
				return 4;
			}
			return 3;
		case 11235:
			return 9;
		case 11730:
			return 4;
		case 20173:
			return 8;
		case 15037:
			return 4;
		case 15038:
			return 5;
		case 13883:
			return 6;
		case 9703:
		case 6528:
			return 7;
		case 15039:
			return 7;
		case 15241:
			return 9;
		case 18349: // chaotic rapier
			return 3;
		}
		if (s.endsWith("greataxe")) {
			return 7;
		} else if (s.equals("torags hammers")) {
			return 5;
		} else if (s.contains("anchor")) {
			return 7;
		} else if (s.equals("guthans warspear")) {
			return 5;
		} else if (s.equals("veracs flail")) {
			return 5;
		} else if (s.equals("ahrims staff")) {
			return 6;
		} else if (s.contains("staff")) {
			if (s.contains("zamarok") || s.contains("guthix") || s.contains("saradomian") || s.contains("slayer")
					|| s.contains("ancient")) {
				return 4;
			} else {
				return 5;
			}
		} else if (s.contains("bow")) {
			if (s.contains("composite") || s.equals("seercull")) {
				return 5;
			} else if (s.contains("aril")) {
				return 4;
			} else if (s.contains("Ogre")) {
				return 8;
			} else if (s.contains("short") || s.contains("hunt") || s.contains("sword")) {
				return 4;
			} else if (s.contains("long") || s.contains("crystal")) {
				return 6;
			} else if (s.contains("'bow") || s.contains("rossbow")) {
				return 7;
			}
			return 5;
		} else if (s.contains("dagger")) {
			return 4;
		} else if (s.contains("godsword") || s.contains("2h")) {
			return 6;
		} else if (s.contains("longsword")) {
			return 5;
		} else if (s.contains("sword")) {
			return 4;
		} else if (s.contains("scimitar")) {
			return 4;
		} else if (s.contains("mace")) {
			return 5;
		} else if (s.contains("battleaxe")) {
			return 6;
		} else if (s.contains("pickaxe")) {
			return 5;
		} else if (s.contains("thrownaxe")) {
			return 5;
		} else if (s.contains("axe")) {
			return 5;
		} else if (s.contains("warhammer")) {
			return 6;
		} else if (s.contains("2h")) {
			return 7;
		} else if (s.contains("spear")) {
			return 5;
		} else if (s.contains("claw")) {
			return 4;
		} else if (s.contains("halberd")) {
			return 7;
		}
		// sara sword, 2400ms
		else if (s.equals("granite maul")) {
			return 7;
		} else if (s.equals("toktz-xil-ak"))// sword
		{
			return 4;
		} else if (s.equals("tzhaar-ket-em"))// mace
		{
			return 5;
		} else if (s.equals("tzhaar-ket-om"))// maul
		{
			return 7;
		} else if (s.equals("toktz-xil-ek"))// knife
		{
			return 4;
		} else if (s.equals("toktz-xil-ul"))// rings
		{
			return 4;
		} else if (s.equals("toktz-mej-tal"))// staff
		{
			return 6;
		} else if (s.contains("whip")) {
			return 4;
		} else if (s.contains("dart")) {
			return 3;
		} else if (s.contains("knife")) {
			return 3;
		} else if (s.contains("javelin")) {
			return 6;
		}
		return 5;
	}

	/**
	 * Block emotes
	 */

	public int getBlockEmote() {
		if (c.equipment[c.playerShield] >= 8844 && c.equipment[c.playerShield] <= 8850) {
			return 4177;
		}
		switch (c.equipment[Player.playerWeapon]) {
		case 4024:
		case 4029:
			return 1393;
		case 4026:
		case 4027:
			return 1403;
		case 4031: // fuck this shitty configs
			return 221;

		case 4755:
			return 2063;
		case 9703:
			return 10923;
		case 10887:
			return 5866;
		case 20072:
			return 4177;
		case 4153:
			return 1666;
		case 4151:
		case 21371:
		case 15441:
		case 15442:
		case 15443:
		case 15444:
		case 14661:
			return 11974;
		case 15037:
			return 388;
		case 4587:
		case 15038:
			return 12030;
		case 11694:
		case 11698:
		case 11700:
		case 11696:
		case 11730:
		case 7158:
			return 7050;
		default:
			return 397;
		}
	}

	public int getBonusAttack(int i) {
		switch (NPCHandler.npcs[i].type) {
		case 2883:
			return Misc.random(50) + 30;
		case 2026:
		case 2027:
		case 2029:
		case 2030:
			return Misc.random(50) + 30;
		}
		return 0;
	}

	public int getCombatDifference(int combat1, int combat2) {
		if (combat1 > combat2) {
			return combat1 - combat2;
		}
		if (combat2 > combat1) {
			return combat2 - combat1;
		}
		return 0;
	}

	public int getEndGfxHeight() {
		switch (Player.MAGIC_SPELLS[c.oldSpellId][0]) {
		case 12987:
		case 12901:
		case 12861:
		case 12445:
		case 1192:
		case 13011:
		case 12919:
		case 12881:
		case 12999:
		case 12911:
		case 12871:
		case 13023:
		case 12929:
		case 12891:
			return 0;
		default:
			return 100;
		}
	}

	public int getEndHeight() {
		switch (Player.MAGIC_SPELLS[c.spellId][0]) {
		case 1562: // stun
			return 10;
		case 12939: // smoke rush
			return 20;
		case 12987: // shadow rush
			return 28;
		case 12861: // ice rush
			return 10;
		case 12951: // smoke blitz
			return 28;
		case 12999: // shadow blitz
			return 15;
		case 12911: // blood blitz
			return 10;
		default:
			return 31;
		}
	}

	public int getFreezeTime() {
		switch (Player.MAGIC_SPELLS[c.oldSpellId][0]) {
		case 1572:
		case 12861: // ice rush
			return 10;
		case 1582:
		case 12881: // ice burst
			return 17;
		case 1592:
		case 12871: // ice blitz
			return 25;
		case 12891: // ice barrage
			return 33;
		default:
			return 0;
		}
	}

	/**
	 * How long it takes to hit your enemy
	 */

	public int getHitDelay(String weaponName) {
		if (c.usingMagic) {
			switch (Player.MAGIC_SPELLS[c.spellId][0]) {
			case 12891:
				return 4;
			case 12871:
				return 6;
			default:
				return 4;
			}
		} else {
			if (weaponName.endsWith("blowpipe")) {
				return 3;
			}

			if (weaponName.contains("knife") || weaponName.contains("dart") || weaponName.contains("javelin")
					|| weaponName.contains("thrownaxe") || c.equipment[Player.playerWeapon] == 15015
					|| c.equipment[Player.playerWeapon] == 13883) {
				return 3;
			}
			if (weaponName.contains("cross") || weaponName.contains("c'bow")) {
				return 4;
			}
			if (weaponName.contains("bow") && !c.dbowSpec) {
				return 4;
			} else if (c.dbowSpec) {
				return 4;
			}
			switch (c.equipment[Player.playerWeapon]) {
			case 6522: // Toktz-xil-ul
				return 3;
			case 15241:
				return 4;
			default:
				return 2;
			}
		}
	}

	public int getKillerId(int playerId) {
		int oldDamage = 0;
		int killerId = 0;
		for (int i = 1; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				if (PlayerHandler.players[i].killedBy == playerId) {
					if (PlayerHandler.players[i].withinDistance(PlayerHandler.players[playerId])) {
						if (PlayerHandler.players[i].totalPlayerDamageDealt > oldDamage) {
							oldDamage = PlayerHandler.players[i].totalPlayerDamageDealt;
							killerId = i;
						}
					}
					PlayerHandler.players[i].totalPlayerDamageDealt = 0;
					PlayerHandler.players[i].killedBy = 0;
				}
			}
		}
		return killerId;
	}

	public int getProjectileShowDelay() {
		switch (c.equipment[Player.playerWeapon]) {
		case 863:
		case 864:
		case 865:
		case 866: // knives
		case 867:
		case 868:
		case 869:
		case 806:
		case 807:
		case 808:
		case 809: // darts
		case 810:
		case 811:
		case 825:
		case 826:
		case 827: // javelin
		case 828:
		case 829:
		case 830:
		case 800:
		case 801:
		case 802:
		case 803: // axes
		case 804:
		case 805:
		case 4734:
		case 9185:
		case 4935:
		case 4936:
		case 4937:
		case 15241:
			return 15;
		default:
			return 5;
		}
	}

	public int getProjectileSpeed() {
		if (c.dbowSpec) {
			return 100;
		}
		if (c.equipment[Player.playerWeapon] == 15241) {
			return 90;
		}
		return 70;
	}

	public int getRangeProjectileGFX() {

		if (c.equipment[PlayerConstants.WEAPON] == Blowpipe.BLOWPIPE) {
			final int[][] DARTS = { { 806, 226 }, { 807, 227 }, { 808, 228 }, { 809, 229 }, { 810, 230 }, { 811, 231 },
					{ 11230, 1123 } };
			for (int index = 0; index < DARTS.length; index++) {
				if (DARTS[index][0] == c.getData().blowpipeAmmoId) {
					return DARTS[index][1];
				}
			}
		}

		// chins
		if (c.equipment[Player.playerWeapon] == 10033) {
			return 908;
		}
		// red chins
		if (c.equipment[Player.playerWeapon] == 10034) {
			return 909;
		}

		if (c.dbowSpec) {
			if (c.equipment[Player.playerWeapon] == 20173) {
				return 249;
			}
			if (c.equipment[c.playerArrows] == 11212) {
				return 1099;
			} else {
				return 1101;
			}
		}
		if (c.equipment[Player.playerWeapon] == 20173)
			return 249;
		if (c.bowSpecShot > 0) {
			switch (c.rangeItemUsed) {
			default:
				return 249;
			}
		}
		if (c.equipment[Player.playerWeapon] == 9185 || c.equipment[Player.playerWeapon] == 15041) {
			return 27;
		}
		if (c.equipment[Player.playerWeapon] == 15241) {
			return 2143;
		}
		switch (c.rangeItemUsed) {
		case 863:
		case 20173:
			return 213;
		case 864:
			return 212;
		case 865:
			return 214;
		case 866: // knives
			return 216;
		case 867:
			return 217;
		case 868:
			return 218;
		case 869:
			return 215;
		case 806:
			return 226;
		case 807:
			return 227;
		case 808:
			return 228;
		case 809: // darts
			return 229;
		case 810:
			return 230;
		case 811:
			return 231;
		case 825:
			return 200;
		case 826:
			return 201;
		case 827: // javelin
			return 202;
		case 828:
			return 203;
		case 829:
			return 204;
		case 830:
			return 205;
		case 13883:
			return 1839;
		case 13879: // morrigan's
			return 1837;
		case 6522: // Toktz-xil-ul
			return 442;
		case 800:
			return 36;
		case 801:
			return 35;
		case 802:
			return 37; // axes
		case 803:
			return 38;
		case 804:
			return 39;
		case 805:
			return 40;
		case 882:
			return 10;
		case 884:
			return 9;
		case 886:
			return 11;
		case 888:
			return 12;
		case 890:
			return 13;
		case 892:
			return 15;
		case 11212:
			return 17;
		case 4740: // bolt rack
			return 27;
		case 4212:
		case 4214:
		case 4215:
		case 4216:
		case 4217:
		case 4218:
		case 4219:
		case 4220:
		case 4221:
		case 4222:
		case 4223:
			return 249;
		case 11283:
		case 11285:
			return 1166;
		}
		return -1;
	}

	public int getRangeStartGFX() {
		if (c.equipment[Player.playerWeapon] == 20173)
			return 250;
		switch (c.rangeItemUsed) {
		case 863:
			return 220;
		case 864:
			return 219;
		case 865:
			return 221;
		case 866: // knives
			return 223;
		case 867:
			return 224;
		case 868:
			return 225;
		case 869:
			return 222;
		case 806:
			return 232;
		case 807:
			return 233;
		case 808:
			return 234;
		case 809: // darts
			return 235;
		case 810:
			return 236;
		case 811:
			return 237;
		case 825:
			return 206;
		case 826:
			return 207;
		case 827: // javelin
			return 208;
		case 828:
			return 209;
		case 829:
			return 210;
		case 830:
			return 211;
		case 800:
			return 42;
		case 801:
			return 43;
		case 802:
			return 44; // axes
		case 803:
			return 45;
		case 804:
			return 46;
		case 805:
			return 48;
		case 882:
			return 19;
		case 884:
			return 18;
		case 886:
			return 20;
		case 888:
			return 21;
		case 890:
			return 22;
		case 892:
			return 24;
		case 11212:
			return 26;
		case 4212:
		case 4214:
		case 4215:
		case 4216:
		case 4217:
		case 4218:
		case 4219:
		case 4220:
		case 4221:
		case 4222:
		case 4223:
			return 250;
		case 15241:
			return 2141;
		}
		return -1;
	}

	public int getRequiredDistance() {
		if (c.followId > 0 && c.freezeTimer <= 0 && !c.isMoving) {
			return 2;
		} else if (c.followId > 0 && c.freezeTimer <= 0 && c.isMoving) {
			return 3;
		} else {
			return 1;
		}
	}

	public int getStaffNeeded() {
		switch (Player.MAGIC_SPELLS[c.spellId][0]) {
		case 1539:
			return 1409;
		case 12037:
			return 4170;
		case 1190:
			return 2415;
		case 1191:
			return 2416;
		case 1192:
			return 2417;
		default:
			return 0;
		}
	}

	public int getStartDelay() {

		// chins
		if (c.equipment[Player.playerWeapon] == 10033) {
			return 15;
		}

		// red chins
		if (c.equipment[Player.playerWeapon] == 10034) {
			return 15;
		}

		switch (Player.MAGIC_SPELLS[c.spellId][0]) {
		case 1539:
			return 60;
		default:
			return 53;
		}
	}

	public int getStartGfxHeight() {
		switch (Player.MAGIC_SPELLS[c.spellId][0]) {

		case 1154: // strike
		case 1156:
		case 1158:

		case 1163: // bolt
		case 1166:
		case 1169:

		case 1175: // blast
		case 1177:
		case 1181:

		case 1185: // wave
		case 1188:
		case 1189:

		case 54280: // surges
		case 54292:
		case 54300:
		case 54264:

		case 12871:
		case 12891:
			return 0;
		default:
			return 100;
		}
	}

	public int getStartHeight() {

		if (c.equipment[PlayerConstants.WEAPON] == TridentOfTheSeas.CHARGED_TRIDENT
				|| c.equipment[PlayerConstants.WEAPON] == TridentOfTheSwamp.CHARGED_TRIDENT) {
			return 30;
		}

		switch (Player.MAGIC_SPELLS[c.spellId][0]) {
		case 1562: // stun
			return 25;
		case 12939:// smoke rush
			return 35;
		case 12987: // shadow rush
			return 38;
		case 12861: // ice rush
			return 15;
		case 12951: // smoke blitz
			return 38;
		case 12999: // shadow blitz
			return 25;
		case 12911: // blood blitz
			return 25;
		default:
			return 43;
		}
	}

	public int getWeaponDistance(String weaponName) {
		if (c.usingMagic) {
			return 10;
		}
		if (weaponName.contains("knife")) {
			if (c.getCombatStyle() == CombatStyle.LONG_RANGE) {
				return 6;
			} else {
				return 4;
			}
		}
		if (weaponName.contains("dart")) {
			if (c.getCombatStyle() == CombatStyle.LONG_RANGE) {
				return 5;
			} else {
				return 3;
			}
		}
		if (weaponName.contains("thrownaxe") || c.equipment[Player.playerWeapon] == 13883) {
			if (c.getCombatStyle() == CombatStyle.LONG_RANGE) {
				return 6;
			} else {
				return 4;
			}
		}
		if (weaponName.contains("javelin") || c.equipment[Player.playerWeapon] == 15015) {
			if (c.getCombatStyle() == CombatStyle.LONG_RANGE) {
				return 7;
			} else {
				return 5;
			}
		}
		if (weaponName.contains("shortbow")) {
			if (c.getCombatStyle() == CombatStyle.LONG_RANGE) {
				return 9;
			} else {
				return 7;
			}
		}
		if (weaponName.contains("crossbow")) {
			if (c.getCombatStyle() == CombatStyle.LONG_RANGE) {
				return 9;
			} else {
				return 7;
			}
		}
		if (weaponName.contains("composite bow") || weaponName.contains("and cannon")) {
			if (c.getCombatStyle() == CombatStyle.LONG_RANGE) {
				return 11;
			} else {
				return 10;
			}
		}
		if (weaponName.contains("dark bow") || c.equipment[Player.playerWeapon] == 20173) {
			if (c.getCombatStyle() == CombatStyle.LONG_RANGE) {
				return 13;
			} else {
				return 12;
			}
		}
		if (weaponName.contains("longbow") || weaponName.contains("crystal")) {
			return 10;
		}
		if (weaponName.contains("halberd")) {
			return 2;
		}
		switch (c.equipment[Player.playerWeapon]) {
		case 6522:
			if (c.getCombatStyle() == CombatStyle.LONG_RANGE) {
				return 6;
			} else {
				return 5;
			}
		}
		return 1;
	}

	public boolean godSpells() {
		switch (Player.MAGIC_SPELLS[c.spellId][0]) {
		case 1190:
			return true;
		case 1191:
			return true;
		case 1192:
			return true;
		default:
			return false;
		}
	}

	public void handleDfs() {
		if (c.dfsCharges <= 0) {
			c.sendMessage("You do not have enough DFS charges.");
			return;
		}
		try {

			if (c.playerIndex <= 0 || PlayerHandler.players[c.playerIndex] == null) {
				c.sendMessage("I should be in combat before using this.");
				return;
			}
			c.projectileStage = 2;
			final int pX = c.getX();
			final int pY = c.getY();
			final int oX = PlayerHandler.players[c.playerIndex].getX();
			final int oY = PlayerHandler.players[c.playerIndex].getY();
			final int offX = (pY - oY) * -1;
			final int offY = (pX - oX) * -1;
			if (System.currentTimeMillis() - c.dfsDelay > 120000) {
				final int damage = Misc.random(15) + 10;
				c.startAnimation(6696);
				c.gfx0(1165);
				Server.getTickManager().submit(new Tick(2) {

					@Override
					public void execute() {
						if (PlayerHandler.players[c.oldPlayerIndex] != null) {
							if (c.playerIndex > -1 && PlayerHandler.players[c.playerIndex] != null) {
								PlayerHandler.players[c.playerIndex].gfx100(1167);
								PlayerHandler.players[c.playerIndex].level[3] -= damage;
								PlayerHandler.players[c.playerIndex].hitDiff2 = damage;
								PlayerHandler.players[c.playerIndex].hitUpdateRequired2 = true;
								PlayerHandler.players[c.playerIndex].updateRequired = true;
							}
						}
						this.stop();
					}
				});
				Server.getTickManager().submit(new Tick(2) {

					@Override
					public void execute() {
						if (!c.isDisconnected()) {
							c.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, 50, 1166, 30, 30,
									-c.playerIndex - 1, 30, 5);
						}
						this.stop();
					}
				});
				c.dfsDelay = System.currentTimeMillis();
				c.dfsCharges--;
			} else {
				c.sendMessage("My shield hasn't finished recharging yet.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleDfsNPC() {
		if (c.dfsCharges <= 0) {
			c.sendMessage("You do not have enough DFS charges.");
			return;
		}
		try {
			if (c.npcIndex > 0) {
				if (NPCHandler.npcs[c.npcIndex] != null) {
					c.projectileStage = 2;
					final int pX = c.getX();
					final int pY = c.getY();
					final int nX = NPCHandler.npcs[c.npcIndex].getX();
					final int nY = NPCHandler.npcs[c.npcIndex].getY();
					final int offX = (pY - nY) * -1;
					final int offY = (pX - nX) * -1;
					if (System.currentTimeMillis() - c.dfsDelay > 120000) {
						if (c.npcIndex > 0 && NPCHandler.npcs[c.npcIndex] != null) {
							final int damage = Misc.random(15) + 10;
							c.startAnimation(6696);
							c.gfx0(1165);
							NPCHandler.npcs[c.npcIndex].hitUpdateRequired2 = true;
							NPCHandler.npcs[c.npcIndex].updateRequired = true;
							NPCHandler.npcs[c.npcIndex].hitDiff2 = damage;
							NPCHandler.npcs[c.npcIndex].dealdamage(c.getDatabaseId(), damage);
							NPCHandler.npcs[c.npcIndex].gfx100(1167);
							Server.getTickManager().submit(new Tick(2) {

								@Override
								public void execute() {
									if (!c.isDisconnected()) {
										c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 50, 1166, 31, 35,
												c.npcIndex - 1, 30);
									}
									this.stop();
								}
							});
							if (NPCHandler.npcs[c.npcIndex].isDead) {
								c.sendMessage("This NPC is already dead!");
								return;
							}
							c.dfsDelay = System.currentTimeMillis();
							c.dfsCharges--;
						} else {
							c.sendMessage("I should be in combat before using this.");
						}
					} else {
						c.sendMessage("My shield hasn't finished recharging yet.");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleGmaulSpec() {
		if (c.respawnTimer <= 0) {
			if (c.playerIndex > 0) {
				Player o = PlayerHandler.players[c.playerIndex];
				if (c.goodDistance(c.getX(), c.getY(), o.getX(), o.getY(), c.getCombat().getRequiredDistance())) {
					if (c.getCombat().checkReqs()) {
						if (c.getCombat().checkSpecAmount(4153)) {
							boolean hit = Misc.random(c.getCombat().calculateMeleeAttack()) > Misc
									.random(o.getCombat().calculateMeleeDefence(c));
							int damage = 0;
							if (hit) {
								damage = Misc.random(c.getCombat().calculateMeleeMaxHit());
							}
							if (o.prayerActive[18] && System.currentTimeMillis() - o.protMeleeDelay > 1500) {
								damage *= .5;
							}
							if (o.level[3] - damage <= 0) {
								damage = o.level[3];
							}
							if (o.level[3] > 0) {
								o.handleHitMask(damage, c.getCombat().calculateMeleeMaxHit(), 1, c.playerIndex);
								c.startAnimation(1667);
								o.gfx100(337);
								o.dealDamage(damage);
							}
						}
					}
				}
			} else if (c.npcIndex > 0) {
				int x = NPCHandler.npcs[c.npcIndex].absX;
				int y = NPCHandler.npcs[c.npcIndex].absY;
				if (c.goodDistance(c.getX(), c.getY(), x, y, 2)) {
					if (c.getCombat().checkSpecAmount(4153)) {
						int damage = Misc.random(c.getCombat().calculateMeleeMaxHit());
						if (NPCHandler.npcs[c.npcIndex].getHP() - damage < 0) {
							damage = NPCHandler.npcs[c.npcIndex].getHP();
						}
						if (NPCHandler.npcs[c.npcIndex].getHP() > 0) {
							NPCHandler.npcs[c.npcIndex].handleHitMask(damage, c.getCombat().calculateMeleeMaxHit(), 1,
									c.npcIndex);
							NPCHandler.npcs[c.npcIndex].dealdamage(c.getDatabaseId(), damage);
							c.startAnimation(1667);
							c.gfx100(337);
						}
					}
				}
			}
		}
	}

	public void handlePrayerDrain() {
		c.usingPrayer = false;
		double toRemove = 0.0;
		for (int j = 0; j < PlayerConstants.PRAYER_DRAIN_RATE.length; j++) {
			if (c.prayerActive[j]) {

				toRemove += PlayerConstants.PRAYER_DRAIN_RATE[j] / 10;
				c.usingPrayer = true;
			}
		}
		for (int j = 0; j < PlayerConstants.CURSE_DRAIN_RATE.length; j++) {
			if (c.curseActive[j]) {
				toRemove += PlayerConstants.CURSE_DRAIN_RATE[j] / 10;
				c.usingPrayer = true;
			}
		}

		if (toRemove > 0) {
			toRemove /= 1 + (0.035 * c.playerBonus[11]);
		}
		c.prayerPoint -= toRemove;
		if (c.prayerPoint <= 0) {
			c.prayerPoint = 1.0 + c.prayerPoint;
			this.reducePrayerLevel();
		}
	}

	/**
	 * MAGIC
	 */
	public int mageAtk() { // search me
		int attackLevel = c.level[6];
		if (ItemUtility.hasVoidSet(c, ItemUtility.VoidSet.MAGE)) {
			attackLevel += c.getPA().getLevelForXP(c.xp[6]) * 0.2;
		}
		if (c.prayerActive[4]) {
			attackLevel *= 1.05;
		} else if (c.prayerActive[12]) {
			attackLevel *= 1.10;
		} else if (c.prayerActive[20]) {
			attackLevel *= 1.15;
		} else if (c.prayerActive[27]) {
			attackLevel *= 1.20;
		}

		/**
		 * slayer item bonuses
		 */
		if (c.npcIndex > 0) {
			NPC npc = NPCHandler.npcs[c.npcIndex];
			if (npc != null) {
				// hexchrest
				if (c.equipment[PlayerConstants.HAT] == 15488 && Slayer.isFightingSlayerTask(c, npc)) {
					attackLevel *= 1.15;
				}
				// full slayer helmet
				if (c.equipment[PlayerConstants.HAT] == 15492 && Slayer.isFightingSlayerTask(c, npc)) {
					attackLevel *= 1.3;
				}
			}
		}

		return (int) (attackLevel + (c.playerBonus[3] * 2));
	}

	// todo: should defence and maic level play a part in calculating magic defence
	// level
	public int mageDef() {
		int defenceLevel = c.level[1] / 2 + c.level[6] / 2;
		if (c.prayerActive[0]) {
			defenceLevel += c.getPA().getLevelForXP(c.xp[PlayerConstants.DEFENCE]) * 0.05;
		} else if (c.prayerActive[3]) {
			defenceLevel += c.getPA().getLevelForXP(c.xp[PlayerConstants.DEFENCE]) * 0.1;
		} else if (c.prayerActive[9]) {
			defenceLevel += c.getPA().getLevelForXP(c.xp[PlayerConstants.DEFENCE]) * 0.15;
		} else if (c.prayerActive[18]) {
			defenceLevel += c.getPA().getLevelForXP(c.xp[PlayerConstants.DEFENCE]) * 0.2;
		} else if (c.prayerActive[19]) {
			defenceLevel += c.getPA().getLevelForXP(c.xp[PlayerConstants.DEFENCE]) * 0.25;
		}
		return (int) (defenceLevel + c.playerBonus[8] + (c.playerBonus[8] / 3));
	}

	public boolean multiSpell() {
		switch (Player.MAGIC_SPELLS[c.oldSpellId][0]) {
		case 12891:
		case 12881:
		case 13011:
		case 13023:
		case 12919: // blood spells
		case 12929:
		case 12963:
		case 12975:
			return true;
		}
		return false;
	}

	public void multiSpellEffect(int playerId, int damage) {
		switch (Player.MAGIC_SPELLS[c.oldSpellId][0]) {
		case 13011:
		case 13023:
			if (System.currentTimeMillis() - PlayerHandler.players[playerId].reduceStat > 35000) {
				PlayerHandler.players[playerId].reduceStat = System.currentTimeMillis();
				PlayerHandler.players[playerId].level[0] -= (PlayerHandler.players[playerId].getPA()
						.getLevelForXP(PlayerHandler.players[playerId].xp[0]) * 10) / 100;
			}
			break;
		case 12919: // blood spells
		case 12929:
			int heal = (int) (damage / 4);
			if (c.level[3] + heal >= c.getPA().getLevelForXP(c.xp[3])) {
				c.level[3] = c.getPA().getLevelForXP(c.xp[3]);
			} else {
				c.level[3] += heal;
			}
			c.getPA().refreshSkill(3);
			break;
		case 12891:
		case 12881:
			if (PlayerHandler.players[playerId].freezeTimer < -4) {

				int freeTimer = getFreezeTime();

				if (PlayerHandler.players[playerId].equipment[PlayerConstants.SHIELD] == 2890) {
					freeTimer = freeTimer / 2;
				}

				PlayerHandler.players[playerId].freezeTimer = freeTimer;
				PlayerHandler.players[playerId].stopMovement();
			}
			break;
		}
	}

	public void multiSpellEffectNPC(int npcId, int damage) {
		switch (Player.MAGIC_SPELLS[c.oldSpellId][0]) {
		case 12891:
		case 12881:
			if (NPCHandler.npcs[npcId].freezeTimer < -4) {
				NPCHandler.npcs[npcId].freezeTimer = this.getFreezeTime();
			}
			break;
		}
	}

	public void npcMageDamage(int damage) {
		int i = c.npcIndex;
		c.previousDamage = damage;
		int bonusAttack = this.getBonusAttack(i);
		if (Misc.random(NPCHandler.npcs[i].defence) > 10 + Misc.random(this.mageAtk()) + bonusAttack) {
			damage /= 2;
		} else if (NPCHandler.npcs[i].type == 2881 || NPCHandler.npcs[i].type == 2882) {
			damage /= 2;
		}
		if (NPCHandler.npcs[i].getHP() - damage < 0) {
			damage = NPCHandler.npcs[i].getHP();
		}
		c.getPA().addSkillXP(damage, 6);
		c.getPA().addSkillXP(damage / 3, 3);
		if (damage > 0) {
			if (NPCHandler.npcs[i].type >= 3777 && NPCHandler.npcs[i].type <= 3780) {
				c.pcDamage += damage;
			}
		}
		NPCHandler.npcs[i].underAttack = true;
		c.killingNpcIndex = c.npcIndex;
		NPCHandler.npcs[i].hitDiff = damage;
		NPCHandler.npcs[i].dealdamage(c.getDatabaseId(), damage);
		NPCHandler.npcs[i].hitUpdateRequired = true;
		NPCHandler.npcs[i].updateRequired = true;
	}

	public void playerDelayedHit(int i) {
		if (PlayerHandler.players[i] != null) {
			if (PlayerHandler.players[i].isDead || c.isDead || PlayerHandler.players[i].level[3] <= 0
					|| c.level[3] <= 0) {
				c.playerIndex = 0;
				return;
			}
			if (PlayerHandler.players[i].respawnTimer > 0) {
				c.faceUpdate(0);
				c.playerIndex = 0;
				return;
			}

			if (c.equipment[PlayerConstants.HAT] == SerpentineHelmet.HELM) {
				c.getData().serpentineHelmetCharges--;
				if (c.getData().serpentineHelmetCharges <= 0) {
					c.getItems().setEquipment(-1, 0, PlayerConstants.HAT);
					c.getItems().addOrBank(SerpentineHelmet.UNCHARGED_HELM, 1);
					c.sendMessage("Your Serpentine Helmet has ran out of charges and has been unequipped.");
				}
			}

			// In addition, if an opponent attacks a player with the helmet equipped, there
			// is a 1 in 6 chance (16.7%) the opponent will be inflicted with venom
			if (PlayerHandler.players[i].equipment[PlayerConstants.HAT] == SerpentineHelmet.HELM) {
				if (Misc.random(5) == 0) {
					c.getPA().appendPoison(6);
				}
			}

			Player o = PlayerHandler.players[i];
			o.getPA().removeAllWindows();
			if (o.playerIndex <= 0 && o.npcIndex <= 0) {
				if (o.autoRet == 1) {
					o.playerIndex = c.playerId;
				}
			}
			if (o.attackTimer <= 3 || o.attackTimer == 0 && o.playerIndex == 0 && !c.castingMagic) { // block animation
				o.startAnimation(o.getCombat().getBlockEmote());
			}
			if (o.inTrade) {
				o.getTradeAndDuel().declineTrade();
			}
			if (c.projectileStage == 0 && !c.usingMagic && !c.castingMagic) { // melee
				// hit
				// damage
				if (!c.usingClaws) {
					this.applyPlayerMeleeDamage(i, 1, CombatHelper.getMeleeDamage(c, o));
				}
				if (c.doubleHit && !c.usingClaws) {
					this.applyPlayerMeleeDamage(i, 2, CombatHelper.getMeleeDamage(c, o));
				}
				if (c.doubleHit && c.usingClaws) {
					c.delayedDamage = c.clawDamage;
					c.delayedDamage2 = c.clawDamage / 2;
					this.applyPlayerMeleeDamage(i, 1, c.clawDamage);
					this.applyPlayerMeleeDamage(i, 2, c.clawDamage / 2);
				}
			}
			this.addCharge();
			if (!c.castingMagic && c.projectileStage > 0) { // range hit damage
				int damage = Misc.random(this.rangeMaxHit());
				int damage2 = -1;
				if (c.lastWeaponUsed == 11235 || c.bowSpecShot == 1 || c.dbowSpec && c.lastWeaponUsed == 20173) {
					damage2 = Misc.random(this.rangeMaxHit());
				}
				boolean ignoreDef = false;
				if (Misc.random(4) == 1 && c.lastArrowUsed == 9243 && c.lastWeaponUsed == 9185) {
					ignoreDef = true;
					o.gfx0(758);
				}
				if (Misc.random(10 + o.getCombat().calculateRangeDefence()) > Misc
						.random(10 + this.calculateRangeAttack()) && !ignoreDef) {
					damage = 0;
				}
				if (Misc.random(4) == 1 && c.lastArrowUsed == 9242 && damage > 0
						&& c.lastWeaponUsed != Blowpipe.BLOWPIPE) {
					int playerHp = (int) Math.round(c.level[3] * 0.10);

					// check we have the hp to spare
					if (c.level[PlayerConstants.HITPOINTS] - playerHp > 0) {
						PlayerHandler.players[i].gfx100(754);

						// the 20%
						int hp = (int) Math.round(PlayerHandler.players[i].level[PlayerConstants.HITPOINTS] * 0.20);
						if (PlayerHandler.players[i].level[PlayerConstants.HITPOINTS] - hp < 0) {
							hp = PlayerHandler.players[i].level[PlayerConstants.HITPOINTS];
						}
						PlayerHandler.players[i].dealDamage(hp);
						PlayerHandler.players[i].handleHitMask(hp, 100, 2, 0);

						// the 10%
						c.dealDamage(playerHp);
						c.handleHitMask(playerHp, 100, 2, 0);// I don't
					}
				}
				if (c.lastWeaponUsed == 11235 || c.bowSpecShot == 1 || c.dbowSpec && c.lastWeaponUsed == 20173) {
					if (Misc.random(10 + o.getCombat().calculateRangeDefence()) > Misc
							.random(10 + this.calculateRangeAttack())) {
						damage2 = 0;
					}
				}
				if (c.lastWeaponUsed == Blowpipe.BLOWPIPE && Misc.random(0, 3) == 0) {
					o.getPA().appendPoison(6);
				}

				if ((c.lastWeaponUsed == 15241 || this.usingHC()) && this.usingHCSpec() && !c.dbowSpec) {
					damage2 = Misc.random(this.rangeMaxHit());
					c.hcSpec = false;
				}
				if (o.equipment[Player.playerWeapon] == 15486 && damage >= 1 && o.SolProtect >= 1) {
					damage = (int) damage / 2;
					o.gfx0(2320);
				}
				if (o.equipment[Player.playerWeapon] == 15001 && damage >= 1 && o.SolProtect >= 1) {
					damage = (int) damage / 2;
					damage2 = (int) damage2 / 2;
				}
				if (c.dbowSpec) {
					o.gfx100(c.lastWeaponUsed == 20173 ? 576 : 1100);
					if (damage < 8) {
						damage = c.lastWeaponUsed == 20173 ? damage : 8;
					}
					if (damage2 < 8) {
						damage2 = c.lastWeaponUsed == 20173 ? damage2 : 8;
					}
					c.dbowSpec = false;
				}
				if (damage > 0 && Misc.random(5) == 1 && c.lastArrowUsed == 9244 && c.lastWeaponUsed == 9185) {
					damage *= 1.45;
					o.gfx0(756);
				}

				if (o.equipment[PlayerConstants.SHIELD] == 9731 && damage > 0) {
					damage = (int) Math.floor((double) damage * 0.85);
				}

				if (c.doJavelinSpecial && c.equipment[PlayerConstants.WEAPON] == 13879) {
					o.javelinDamage = damage;
					GameCycleTaskHandler.addEvent(o, new GameCycleTask() {
						@Override
						public void execute(GameCycleTaskContainer container) {

							if (o.isDead()) {
								container.stop();
								return;
							}

							int damage;
							if (o.javelinDamage >= 5) {
								damage = 5;
							} else {
								damage = o.javelinDamage;
							}
							o.dealDamage(damage);
							o.handleHitMask(damage, c.getCombat().rangeMaxHit(), 2, i);
							o.getPA().refreshSkill(3);
							o.updateRequired = true;
							o.javelinDamage -= damage;
							if (o.javelinDamage <= 0) {
								container.stop();
							}
						}

						@Override
						public void stop() {

						}

					}, 3);
					c.doJavelinSpecial = false;
				}

				if (o.equipment[o.playerShield] == 13740) { // divine
					// effect
					// reduce by
					// 30%
					o.level[5] -= damage * 0.15;
					if (o.level[5] >= 1) {
						damage = (int) damage * 70 / 100;
						damage2 = (int) damage2 * 70 / 100;
					}
					if (o.level[5] <= 0) {
						o.level[5] = 0;
					}
					o.getPA().refreshSkill(5);
				}
				if (o.equipment[o.playerShield] == 13742) { // elysian
					// effect
					// reduce by
					// 25% 70%
					// of the
					// time
					int random = Misc.random(9);
					if (random >= 0 && random <= 6) {
						damage = (int) damage * 75 / 100;
						damage2 = (int) damage2 * 75 / 100;
					}
				}
				if (o.equipment[o.playerShield] == 15042 && (damage >= 15 || damage2 >= 15)) {
					damage = (int) damage * 85 / 100;
					damage2 = (int) damage2 * 85 / 100;
				}
				if (o.prayerActive[17] && System.currentTimeMillis() - o.protRangeDelay > 1500) { // if
					// prayer
					// active
					// reduce
					// damage
					// by
					// half
					damage = (int) damage * 60 / 100;
					if (c.lastWeaponUsed == 11235 || c.bowSpecShot == 1 || c.dbowSpec && c.lastWeaponUsed == 20173) {
						damage2 = (int) damage2 * 60 / 100;
					}
				}
				if (o.curseActive[8] && System.currentTimeMillis() - o.protRangeDelay > 1100) { // if
					// prayer
					// active
					// reduce
					// damage
					// by
					// half
					damage = (int) damage * 60 / 100;
					if (c.lastWeaponUsed == 11235 || c.bowSpecShot == 1 || c.dbowSpec && c.lastWeaponUsed == 20173) {
						damage2 = (int) damage2 * 60 / 100;
					}
					if (Misc.random(3) == 0) {
						o.gfx0(2229);
						o.startAnimation(12573);
						this.deflectDamage(damage, i);
					}
				}
				if (PlayerHandler.players[i].level[3] - damage < 0) {
					damage = PlayerHandler.players[i].level[3];
				}
				if (PlayerHandler.players[i].level[3] - damage - damage2 < 0) {
					damage2 = PlayerHandler.players[i].level[3] - damage;
				}
				if (damage < 0) {
					damage = 0;
				}
				if (damage2 < 0 && damage2 != -1) {
					damage2 = 0;
				}
				if (o.vengOn) {
					this.appendVengeance(i, damage);
					this.appendVengeance(i, damage2);
				}
				if (damage > 0) {
					this.applyRecoil(damage, i);
				}
				if (damage2 > 0) {
					this.applyRecoil(damage2, i);
				}
				if (damage > 0) {
					int xp = damage * 4;
					if (c.getCombatStyle() == CombatStyle.LONG_RANGE) {
						c.getPA().addSkillXP(xp / 3, 4);
						c.getPA().addSkillXP(xp / 3, 1);
						c.getPA().addSkillXP(xp / 3, 3);
					} else {
						c.getPA().addSkillXP(xp, 4);
					}
				}
				boolean dropArrows = true;
				for (int noArrowId : c.NO_ARROW_DROP) {
					if (c.lastWeaponUsed == noArrowId) {
						dropArrows = false;
						break;
					}
				}
				if (dropArrows) {
					c.getItems().dropArrowPlayer();
				}
				PlayerHandler.players[i].underAttackBy = c.playerId;
				PlayerHandler.players[i].logoutDelay = System.currentTimeMillis();
				PlayerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
				PlayerHandler.players[i].killerId = c.playerId;
				PlayerHandler.players[i].dealDamage(damage);
				PlayerHandler.players[i].damageTaken[c.playerId] += damage;
				c.killedBy = PlayerHandler.players[i].playerId;
				PlayerHandler.players[i].handleHitMask(damage, this.rangeMaxHit(), 2, i);
				if (damage2 != -1) {
					PlayerHandler.players[i].dealDamage(damage2);
					PlayerHandler.players[i].damageTaken[c.playerId] += damage2;
					PlayerHandler.players[i].handleHitMask(damage2, this.rangeMaxHit(), 2, i);
				}
				o.getPA().refreshSkill(3);
				PlayerHandler.players[i].updateRequired = true;
				this.applySmite(i, damage);
				this.applyRedemption(i, damage);
				if (damage2 != -1) {
					this.applySmite(i, damage2);
				}
				if (c.leechSpecialDelay <= 0) {
					this.applyLeechSpecial(i, damage);
				}
				if (c.leechDefenceDelay <= 0) {
					this.applyLeechDefence(i, damage);
				}
				if (c.leechStrengthDelay <= 0) {
					this.applyLeechStrength(i, damage);
				}
				if (c.soulSplitDelay <= 0) {
					this.applySoulSplit(i, damage);
				}
				if (c.leechAttackDelay <= 0) {
					this.applyLeechAttack(i, damage);
				}
				// applyDivineEffect(i, damage);
				if (c.leechRangeDelay <= 0) {
					this.applyRangeLeech(i, damage);
				}
				if (c.leechMagicDelay <= 0) {
					this.applyLeechMagic(i, damage);
				}
				if (damage2 != -1) {
					if (c.soulSplitDelay <= 0) {
						this.applySoulSplit(i, damage2);
					}
					if (c.leechSpecialDelay <= 0) {
						this.applyLeechSpecial(i, damage2);
					}
					if (c.leechDefenceDelay <= 0) {
						this.applyLeechDefence(i, damage2);
					}
					if (c.leechStrengthDelay <= 0) {
						this.applyLeechStrength(i, damage2);
					}
					if (c.leechAttackDelay != -1) {
						this.applyLeechAttack(i, damage2);
					}
					if (c.leechRangeDelay <= 0) {
						this.applyRangeLeech(i, damage2);
					}
					if (c.leechMagicDelay <= 0) {
						this.applyLeechMagic(i, damage2);
					}
				}
			} else if (c.projectileStage > 0) { // magic hit damage search op
				// barrage
				int maxDamage = MaxHits.calculateMagicMaxHit(c, c.oldSpellId);
				int damage = Misc.random(maxDamage);

				if (Player.MAGIC_SPELLS[c.oldSpellId][0] == 54264) {
					int[] armadylDamage = MaxHits.getArmadylStormDamage(c.level[PlayerConstants.MAGIC]);
					maxDamage = armadylDamage[1];
					damage = Misc.random(armadylDamage[0], armadylDamage[1]);
				}

				if (this.godSpells()) {
					if (System.currentTimeMillis() - c.godSpellDelay < Config.GOD_SPELL_CHARGE) {
						damage += 15;
					}
				}
				if (c.magicFailed) {
					damage = 0;
				}
				if (o.prayerActive[16] && System.currentTimeMillis() - o.protMageDelay > 1500) { // if
					// prayer
					// active
					// reduce
					// damage
					// by
					// half
					damage = (int) Math.floor(damage * 0.40);
				}
				if (o.curseActive[7] && System.currentTimeMillis() - o.protMageDelay > 1100) { // if
					// prayer
					// active
					// reduce
					// damage
					// by
					// half
					damage = (int) Math.floor(damage * 0.40);
					if (Misc.random(3) == 0) {
						o.gfx0(2228);
						o.startAnimation(12573);
						this.deflectDamage(damage, i);
					}
				}
				if (PlayerHandler.players[i].level[3] - damage < 0) {
					damage = PlayerHandler.players[i].level[3];
				}
				if (o.equipment[Player.playerWeapon] == 15486 && damage >= 1 && o.SolProtect >= 1) {
					damage = (int) damage / 2;
					o.gfx0(2320);
				}
				if (o.vengOn) {
					this.appendVengeance(i, damage);
				}
				if (damage > 0) {
					this.applyRecoil(damage, i);
				}
				c.getPA().addSkillXP(Player.MAGIC_SPELLS[c.oldSpellId][7] + (damage * 2), 6);
				c.getPA().addSkillXP(Player.MAGIC_SPELLS[c.oldSpellId][7] + (damage * 2) / 3, 3);

				if (this.getEndGfxHeight() == 100 && !c.magicFailed) { // end GFX
					PlayerHandler.players[i].gfx100(Player.MAGIC_SPELLS[c.oldSpellId][5]);
				} else if (!c.magicFailed) {
					PlayerHandler.players[i].gfx0(Player.MAGIC_SPELLS[c.oldSpellId][5]);

				} else if (c.magicFailed) {
					PlayerHandler.players[i].gfx100(85);
				}
				if (Player.MAGIC_SPELLS[c.oldSpellId][5] == 369
						&& PlayerHandler.players[i].freezeTimer < (this.getFreezeTime() - 1)
						&& PlayerHandler.players[i].freezeTimer > 0) {
					PlayerHandler.players[i].gfx50(1677);
				}
				if (!c.magicFailed) {
					if (System.currentTimeMillis() - PlayerHandler.players[i].reduceStat > 35000) {
						PlayerHandler.players[i].reduceStat = System.currentTimeMillis();
						switch (Player.MAGIC_SPELLS[c.oldSpellId][0]) {
						case 12987:
						case 13011:
						case 12999:
						case 13023:
							PlayerHandler.players[i].level[0] -= (o.getPA()
									.getLevelForXP(PlayerHandler.players[i].xp[0]) * 10) / 100;
							break;
						}
					}
					switch (Player.MAGIC_SPELLS[c.oldSpellId][0]) {
					case 12445: // teleblock
						if (System.currentTimeMillis() - o.teleBlockDelay > o.teleBlockLength) {

							if (o.equipment[PlayerConstants.SHIELD] == 2890 && Misc.random(99) <= 30) {
								o.sendMessage("Your elemental shield deflects the teleblock back!");

								if (c.prayerActive[16] && System.currentTimeMillis() - c.protMageDelay > 1500) {
									c.teleBlockLength = 150000;
								} else {
									c.teleBlockLength = 300000;
								}

								c.magicFailed = true;
								c.gfx100(345);
								c.sendMessage("You have been teleblocked.");
								break;
							}

							o.teleBlockDelay = System.currentTimeMillis();
							o.sendMessage("You have been teleblocked.");
							if (o.prayerActive[16] && System.currentTimeMillis() - o.protMageDelay > 1500) {
								o.teleBlockLength = 150000;
							} else {
								o.teleBlockLength = 300000;
							}
						}
						break;
					case 12901:
					case 12919: // blood spells
					case 12911:
					case 12929:
						int boost = Food.getHpBoost(c);
						int heal = (int) (damage / 4);
						if (c.level[3] + heal > c.getPA().getLevelForXP(c.xp[3]) + boost) {
							c.level[3] = c.getPA().getLevelForXP(c.xp[3]) + boost;
						} else {
							c.level[3] += heal;
						}
						c.getPA().refreshSkill(3);
						break;
					case 1153:
						PlayerHandler.players[i].level[0] -= (o.getPA().getLevelForXP(PlayerHandler.players[i].xp[0])
								* 5) / 100;
						o.sendMessage("Your attack level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(0);
						break;
					case 1157:
						PlayerHandler.players[i].level[2] -= (o.getPA().getLevelForXP(PlayerHandler.players[i].xp[2])
								* 5) / 100;
						o.sendMessage("Your strength level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(2);
						break;
					case 1161:
						PlayerHandler.players[i].level[1] -= (o.getPA().getLevelForXP(PlayerHandler.players[i].xp[1])
								* 5) / 100;
						o.sendMessage("Your defence level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(1);
						break;
					case 1542:
						PlayerHandler.players[i].level[1] -= (o.getPA().getLevelForXP(PlayerHandler.players[i].xp[1])
								* 10) / 100;
						o.sendMessage("Your defence level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(1);
						break;
					case 1543:
						PlayerHandler.players[i].level[2] -= (o.getPA().getLevelForXP(PlayerHandler.players[i].xp[2])
								* 10) / 100;
						o.sendMessage("Your strength level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(2);
						break;
					case 1562:
						PlayerHandler.players[i].level[0] -= (o.getPA().getLevelForXP(PlayerHandler.players[i].xp[0])
								* 10) / 100;
						o.sendMessage("Your attack level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(0);
						break;
					}
				}
				PlayerHandler.players[i].logoutDelay = System.currentTimeMillis();
				PlayerHandler.players[i].underAttackBy = c.playerId;
				PlayerHandler.players[i].killerId = c.playerId;
				PlayerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
				if (Player.MAGIC_SPELLS[c.oldSpellId][6] != 0) {
					PlayerHandler.players[i].dealDamage(damage);
					PlayerHandler.players[i].damageTaken[c.playerId] += damage;
					c.totalPlayerDamageDealt += damage;
					if (!c.magicFailed) {
						// Server.playerHandler.players[i].setHitDiff(damage);
						// Server.playerHandler.players[i].setHitUpdateRequired(true);
						PlayerHandler.players[i].handleHitMask(damage, maxDamage, 3, i);
					}
				}
				this.applySmite(i, damage);
				this.applyRedemption(i, damage);
				c.killedBy = PlayerHandler.players[i].playerId;
				o.getPA().refreshSkill(3);
				PlayerHandler.players[i].updateRequired = true;
				c.usingMagic = false;
				c.castingMagic = false;
				if (c.soulSplitDelay <= 0) {
					this.applySoulSplit(i, damage);
				}
				if (Areas.inMulti(o) && this.multiSpell()) {
					c.barrageCount = 0;
					for (int j = 0; j < PlayerHandler.players.length; j++) {
						if (PlayerHandler.players[j] != null) {
							if (j == o.playerId) {
								continue;
							}
							if (c.barrageCount >= 9) {
								break;
							}
							if (o.goodDistance(o.getX(), o.getY(), PlayerHandler.players[j].getX(),
									PlayerHandler.players[j].getY(), 1)) {
								this.appendMultiBarrage(j, c.magicFailed);
							}
						}
					}
				}
				c.getPA().refreshSkill(3);
				c.getPA().refreshSkill(6);
				c.oldSpellId = 0;
			}
		}
		c.dbowSpec = false;
		c.getPA().requestUpdates();
		if (c.bowSpecShot <= 0) {
			c.oldPlayerIndex = 0;
			c.projectileStage = 0;
			c.lastWeaponUsed = 0;
			c.doubleHit = false;
			c.bowSpecShot = 0;
		}
		if (c.bowSpecShot != 0) {
			c.bowSpecShot = 0;
		}
	}

	/**
	 * Staff of light special.
	 */
	public void activateSolSpecial() {
		if (c.specAmount >= 10) {
			c.specAmount -= 10;
			c.getItems().addSpecialBar(15486);
			c.SolProtect = 100;
			c.startAnimation(10516);
			c.gfx0(2319);
		} else {
			c.sendMessage("You must have 100% of your special attack abr in order to use this.");
		}
	}

	/**
	 * The abyssal whip vine special attack.
	 *
	 * @param i
	 */
	public void applyVineDamage(int i) {

		if (c.playerIndex > 0) {
			Player o = PlayerHandler.players[c.playerIndex];
			o.sendMessage("Vines start attacking you!");
			Server.getTickManager().submit(new Tick(3) {
				int attacksLeft = 10;

				@Override
				public void execute() {
					if (o.isDead) {
						stop();
						return;
					}
					if (attacksLeft == 0) {
						stop();
						return;
					}
					if (c.attributeExists("cancel_vine")) {
						c.removeAttribute("cancel_vine");
						stop();
						return;
					}
					int damage = Misc.random(calculateMeleeMaxHit()) / 5;
					PlayerHandler.players[i].handleHitMask(damage, calculateMeleeMaxHit(), 1, i);
					o.dealDamage(damage);
					o.damageTaken[c.playerId] += damage;
					c.totalPlayerDamageDealt += damage;
					o.updateRequired = true;
					o.getPA().refreshSkill(3);
					attacksLeft--;
				}

				@Override
				public void stop() {
					o.sendMessage("The vines have vanished.");
					super.stop();
					return;
				}
			});
		} else if (c.npcIndex > 0) {
			NPC n = NPCHandler.npcs[c.npcIndex];

			GameCycleTaskHandler.addEvent(n, new GameCycleTask() {

				int attacksLeft = 10;

				@Override
				public void execute(GameCycleTaskContainer container) {
					if (n == null) {
						container.stop();
						return;
					}
					if (n.getHP() <= 0) {
						container.stop();
						return;
					}
					if (attacksLeft == 0) {
						container.stop();
						return;
					}

					int damage = Misc.random(calculateMeleeMaxHit()) / 5;
					n.handleHitMask(damage, calculateMeleeMaxHit(), 1, c.npcIndex);
					n.dealdamage(c.getDatabaseId(), damage);

					c.totalPlayerDamageDealt += damage;
					attacksLeft--;
				}
			}, 3);
		}
	}

	public boolean properBolts() {
		return c.equipment[c.playerArrows] >= 9140 && c.equipment[c.playerArrows] <= 9144
				|| c.equipment[c.playerArrows] >= 9240 && c.equipment[c.playerArrows] <= 9244;
	}

	public int rangeMaxHit() {
		return MaxHits.rangeMaxHit(c);
	}

	public void reducePrayerLevel() {
		if (c.level[5] - 1 > 0) {
			c.level[5] -= 1;
		} else {
			c.sendMessage("You have run out of prayer points!");
			c.level[5] = 0;
			this.resetAllPrayers();
			c.prayerId = -1;
		}
		c.getPA().refreshSkill(5);
	}

	public void resetCurses() {
		for (int i = 0; i < c.curseActive.length; i++) {
			c.curseActive[i] = false;
			c.getPA().sendFrame36(c.CURSE_GLOW[i], 0);
		}
		// c.headIcon = -1;
		c.getPA().requestUpdates();
	}

	public void resetPlayerAttack() {
		c.usingMagic = false;
		c.npcIndex = 0;
		c.faceUpdate(0);
		c.playerIndex = 0;
		c.getPA().resetFollow();
		// c.sendMessage("Reset attack.");
	}

	public void resetPrayers() {
		for (int i = 0; i < c.prayerActive.length; i++) {
			c.prayerActive[i] = false;
			c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
		}
		// c.headIcon = -1;
		c.getPA().requestUpdates();
	}

	public void resetAllPrayers() { // nice naming
		for (int i = 0; i < c.prayerActive.length; i++) {
			c.prayerActive[i] = false;
			c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
		}
		for (int i = 0; i < c.curseActive.length; i++) {
			c.curseActive[i] = false;
			c.getPA().sendFrame36(c.CURSE_GLOW[i], 0);
		}
		c.headIcon = -1;
		c.getPA().requestUpdates();
	}

	public boolean usingBolts() {
		return c.equipment[c.playerArrows] >= 9130 && c.equipment[c.playerArrows] <= 9145
				|| c.equipment[c.playerArrows] >= 9230 && c.equipment[c.playerArrows] <= 9245;
	}

	public boolean usingCrystalBow() {
		return c.equipment[Player.playerWeapon] >= 4212 && c.equipment[Player.playerWeapon] <= 4223
				|| c.equipment[Player.playerWeapon] == 20173;
	}

	public boolean usingDbow() {
		return c.equipment[Player.playerWeapon] == 11235;
	}

	public boolean usingHally() {
		switch (c.equipment[Player.playerWeapon]) {
		case 3190:
		case 3192:
		case 3194:
		case 3196:
		case 3198:
		case 3200:
		case 3202:
		case 3204:
			return true;
		default:
			return false;
		}
	}

	public boolean usingHC() {
		return c.equipment[Player.playerWeapon] == 15241;
	}

	public boolean usingHCSpec() {
		return c.equipment[Player.playerWeapon] == 15241 && c.hcSpec == true;
	}

	public boolean wearingStaff(int runeId) {
		int wep = c.equipment[Player.playerWeapon];
		switch (runeId) {

		case 21773: // armadyl rune
			if (wep == 21777) { // Armadyl battlestaff
				return true;
			}
			break;

		case 554: // fire rune
			if (wep == 1387 || wep == 1401 || wep == 1393) {
				return true;
			}
			break;
		case 555: // water rune
			if (wep == 1383 || wep == 1403 || wep == 1395) {
				return true;
			}
			break;
		case 556: // air rune
			if (wep == 1381 || wep == 1405 || wep == 1397) {
				return true;
			}
			break;
		case 557: // earth rune rune
			if (wep == 1385 || wep == 1407 || wep == 1399) {
				return true;
			}
			break;
		}
		return false;
	}

	public boolean usingCrossbow(int item) {
		Optional<ItemDefinition> optional = ItemDefinition.get(item);

		if (!optional.isPresent()) {
			return false;
		}

		ItemDefinition definition = optional.get();

		if (definition.getName() == null) {
			return false;
		}

		String name = definition.getName().toLowerCase();

		if (name.contains("c'bow") || name.contains("crossbow")) {
			return true;
		}

		return false;
	}

}
