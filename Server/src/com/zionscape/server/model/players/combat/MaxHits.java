package com.zionscape.server.model.players.combat;

import com.zionscape.server.model.items.Blowpipe;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.skills.slayer.Slayer;

public class MaxHits {

	public static int[] getArmadylStormDamage(int magicLevel) {

		if (magicLevel > 99) {
			magicLevel = 99;
		}

		int minimum = (magicLevel - 77) * 5;
		int maximum = (magicLevel - 77) * 5 + 225;

		if (minimum > 0) {
			minimum /= 10;
		}

		if (maximum > 0) {
			maximum /= 10;
		}

		return new int[]{minimum, maximum};
	}

	public static int calculateMagicMaxHit(Player player, int spell) {

		double damage = Player.MAGIC_SPELLS[spell][6];

		int magicLevel = player.getPA().getLevelForXP(player.xp[PlayerConstants.MAGIC]);

		// magic potions
		if (player.level[PlayerConstants.MAGIC] > magicLevel) {
			int increase = player.level[PlayerConstants.MAGIC] - magicLevel;
			if (increase > 5) {
				increase = 5;
			}
			damage *= 1.0 + (increase * 0.03);
		}

		if(player.prayerActive[27]) { // augury
			damage *= 1.20;
		}

		switch (player.equipment[player.playerAmulet]) {
			case 18333: // Arcane pulse necklace
				damage *= 1.05;
				break;
			case 18334: // Arcane blast necklace
				damage *= 1.10;
				break;
			case 18335: // Arcane stream necklace
				damage *= 1.15;
				break;
		}

		switch (player.equipment[Player.playerWeapon]) {
			case 18371: // Gravite staff
				damage *= 1.05;
				break;
			case 4675: // Ancient staff
			case 4710: // Ahrim's staff
			case 6914: // Master wand
			case 8841: // Void knight mace
			case 13867: // Zuriel's staff
				damage *= 1.10;
				break;
			case 15486: // staff of light
				damage *= 1.15;
				break;
			case 18355: // chaotic staff
				damage *= 1.20;
				break;
		}

		/**
		 * slayer item bonuses
		 */
		if (player.npcIndex > 0) {
			NPC npc = NPCHandler.npcs[player.npcIndex];
			if (npc != null) {
				// hex chest
				if (player.equipment[PlayerConstants.HAT] == 15488 && Slayer.isFightingSlayerTask(player, npc)) {
					damage *= 1.15;
				}
				// full slayer helmet
				if (player.equipment[PlayerConstants.HAT] == 15492 && Slayer.isFightingSlayerTask(player, npc)) {
					damage *= 1.15;
				}
			}
		}

		return (int) damage;
	}

	public static int calculateMeleeMaxHit(Player c) {
		double maxHit = 0;
		// Player o = (Player)Server.playerHandler.players[c.playerIndex];
		// int turmoilBonus = o.level[2]/10;
		int strBonus = c.strBonus;
		int strength = c.level[2];
		int lvlForXP = c.getPA().getLevelForXP(c.xp[2]);
		if (c.prayerActive[1]) {
			strength += (int) (lvlForXP * .05);
		} else if (c.prayerActive[6]) {
			strength += (int) (lvlForXP * .10);
		} else if (c.prayerActive[14]) {
			strength += (int) (lvlForXP * .15);
		} else if (c.prayerActive[24]) {
			strength += (int) (lvlForXP * .18);
		} else if (c.prayerActive[25]) {
			strength += (int) (lvlForXP * .23);
		}
		if (c.getCombatStyle() == CombatHelper.CombatStyle.AGGRESSIVE) {
			lvlForXP += 3;
		} else if (c.getCombatStyle() == CombatHelper.CombatStyle.ACCURATE) {
			lvlForXP += 1;
		}
		if (c.curseActive[19]) {
			strength += (int) (lvlForXP * .23);
		}
		if (c.equipment[c.playerHat] == 2526 && c.equipment[c.playerChest] == 2520
				&& c.equipment[c.playerLegs] == 2522) {
			maxHit += maxHit * 10 / 100;
		}
		maxHit += 1.05D + (double) (strBonus * strength) * 0.00175D;
		maxHit += (double) strength * 0.11D;

		if (c.equipment[Player.playerWeapon] == 4718 && c.equipment[c.playerHat] == 4716
				&& c.equipment[c.playerChest] == 4720 && c.equipment[c.playerLegs] == 4722) {
			double dh = c.getPA().getLevelForXP(c.xp[3]) - c.level[3];
			dh /= 100;
			dh += 1;
			maxHit *= dh;
		}
		if (c.specDamage > 1) {
			maxHit = (int) (maxHit * c.specDamage);
		}
		if (ItemUtility.hasVoidSet(c, ItemUtility.VoidSet.MELEE)) {
			maxHit = (int) (maxHit * 1.10);
		}
		if (c.equipment[c.playerAmulet] == 11128 && c.equipment[Player.playerWeapon] == 6528) {
			maxHit *= 1.20;
		}

		/**
		 * slayer item bonuses
		 */
		if (c.npcIndex > 0) {
			NPC npc = NPCHandler.npcs[c.npcIndex];
			if (npc != null) {
				// black mask
				if (c.equipment[PlayerConstants.HAT] >= 8901 && c.equipment[PlayerConstants.HAT] <= 8922 && Slayer.isFightingSlayerTask(c, npc)) {
					maxHit *= 1.15;
				}
				// slayer helmet
				if (c.equipment[PlayerConstants.HAT] == 13263 && Slayer.isFightingSlayerTask(c, npc)) {
					maxHit *= 1.15;
				}
				// full slayer helmet
				if (c.equipment[PlayerConstants.HAT] == 15492 && Slayer.isFightingSlayerTask(c, npc)) {
					maxHit *= 1.15;
				}
			}
		}


		if (maxHit < 0) {
			maxHit = 1;
		}
		return (int) Math.floor(maxHit);
	}

	public static int getRangeStr(int i) {
		if (i == 4214) {
			return 70;
		}
		switch (i) {
			// darts
			case 806:
				return 1;
			case 807:
				return 3;
			case 808:
				return 4;
			case 3093:
				return 6;
			case 809:
				return 7;
			case 810:
				return 10;
			case 811:
				return 14;
			case 11230:
				return 20;


			// bronze to rune bolts
			case 877:
				return 10;
			case 9140:
				return 46;
			case 9141:
				return 64;
			case 9142:
			case 9241:
			case 9240:
				return 82;
			case 9143:
			case 9243:
			case 9242:
			case 9339:
				return 100;
			case 9144:
			case 9244:
			case 9245:
				return 115;
			// bronze to dragon arrows
			case 882:
				return 7;
			case 884:
				return 10;
			case 886:
				return 16;
			case 888:
				return 22;
			case 890:
				return 31;
			case 892:
			case 4740:
				return 49;
			case 11212:
				return 60;
			// knifes
			case 864:
				return 3;
			case 863:
				return 4;
			case 865:
				return 7;
			case 13883:
				return 140;
			case 13879:
				return 150;
			case 866:
				return 10;
			case 867:
				return 14;
			case 868:
				return 24;
		}
		return 0;
	}

	public static int rangeMaxHit(Player c) {
		int rangeLevel = c.level[4];
		double modifier = 1.0;
		double wtf = c.specDamage;
		int itemUsed = c.usingBow ? c.lastArrowUsed : c.lastWeaponUsed;

		if (c.lastWeaponUsed == Blowpipe.BLOWPIPE) {
			itemUsed = Blowpipe.BLOWPIPE;
		}

		if (c.prayerActive[3]) {
			modifier += 0.05;
		} else if (c.prayerActive[11]) {
			modifier += 0.10;
		} else if (c.prayerActive[19]) {
			modifier += 0.15;
		}
		if (ItemUtility.hasVoidSet(c, ItemUtility.VoidSet.RANGE)) {
			modifier += 0.20;
		}
		int rangeStr = getRangeStr(itemUsed);

		if (itemUsed == Blowpipe.BLOWPIPE) {
			rangeStr = 100;

			if (c.getData().blowpipeAmmo > 0) {
				rangeStr += getRangeStr(c.getData().blowpipeAmmoId);
			}
		}

		if (c.lastWeaponUsed == 20173) {
			rangeStr = 167;
		}
		if (c.lastWeaponUsed == 15241) {
			rangeStr += 90;
		}
		double x = modifier * rangeLevel;
		double max = (x + 8) * (rangeStr + 64) / 640;
		if (wtf > 1) {
			max = (int) (max * wtf);
		}
		if (max < 1) {
			max = 1;
		}

		double bonus = 1;

		/**
		 * slayer item bonuses
		 */
		if (c.npcIndex > 0) {
			NPC npc = NPCHandler.npcs[c.npcIndex];
			if (npc != null) {
				// focus sight
				if (c.equipment[PlayerConstants.HAT] == 15490 && Slayer.isFightingSlayerTask(c, npc)) {
					bonus *= 1.15;
				}
				// full slayer helmet
				if (c.equipment[PlayerConstants.HAT] == 15492 && Slayer.isFightingSlayerTask(c, npc)) {
					bonus *= 1.15;
				}
			}
		}

		return (int) Math.floor(max * bonus);
	}

}
