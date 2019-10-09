package com.zionscape.server.model.players.combat;

import com.zionscape.server.model.items.Blowpipe;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;

public class Animations {

	/**
	 * Weapon emotes
	 */

	public static int getAttackAnimation(Player player) {

		String name = ItemUtility.getName(player.equipment[PlayerConstants.WEAPON]).toLowerCase();

		// unarmed
		if (player.equipment[Player.playerWeapon] <= 0) {
			switch (player.getAttackStyle()) {
				case 0:
					return 424;
				case 1:
					return 422; // 423
				case 2:
					return 423;
			}
		}

		if (player.equipment[PlayerConstants.WEAPON] == Blowpipe.BLOWPIPE) {
			return 5061;
		}

		// chins
		if (player.equipment[Player.playerWeapon] == 10033) {
			return 2779;
		}

		// red chins
		if (player.equipment[Player.playerWeapon] == 10034) {
			return 2779;
		}


		// training sword
		if (player.equipment[Player.playerWeapon] == 9703) {
			player.gfx0(1886);
			return 10922;
		}

		// hand cannon
		if (player.equipment[Player.playerWeapon] == 15241) {
			player.gfx0(2141);
			player.projectileStage = 1;
			player.getCombat().fireProjectileNpc();
			return 12152;
		}

		if (name.contains("claws")) {
			switch (player.getAttackStyle()) {
				case 2:
					return 1067;
				default:
					return 393;
			}
		} else if (name.contains("dagger")) {
			switch (player.getAttackStyle()) {
				case 2:
					return 377;
				default:
					return 376;
			}
		} else if (name.equals("hand cannon")) {
			return 12153;
		} else if (name.contains("karil's")) {
			return 427;
		} else if (name.toLowerCase().contains("rapier")) {
			return 386;
		} else if (name.contains("maul") && !name.contains("granite")) {
			return 13055;
		} else if (name.contains("anchor")) {
			return 5865;
		} else if (name.startsWith("boxing")) {
			return 3678;
		} else if (name.equals("tzhaar-ket-om")) {
			return 2661;
		} else if (name.contains("javelin")) {
			return 10501;
		} else if (name.contains("thrownaxe") || name.contains("throwing axe")) {
			return 10504;
		} else if (name.contains("knife") || name.contains("dart")) {
			return 6600;
		} else if (name.contains("bow") && !name.contains("'bow") && !name.contains("crossbow")) {
			return 426;
		} else if (name.contains("crossbow") || name.contains("'bow")) {
			return 4230;
		} else if (name.startsWith("guthan's warspear")) {
			switch (player.getAttackStyle()) {
				case 0:
				case 3:
					return 2080;
				case 1:
					return 2081;
				case 2:
					return 2082;
			}
		} else if (name.contains("karil")) {
			return 2075;
		} else if (name.startsWith("ahrim's staff")) {
			return 2078;
		} else if (name.startsWith("torag's hammers")) {
			return 2068;
		} else if (name.startsWith("dharok's greataxe") || name.startsWith("dharok's axe")) {
			switch (player.getAttackStyle()) {
				case 0:
				case 1:
				case 3:
					return 2067;
				case 2:
					return 2066;
			}
		} else if (name.startsWith("verac's flail")) {
			switch (player.getAttackStyle()) {
				case 0:
				case 1:
				case 3:
					return 2062;
				case 2:
					return 15074;
			}
		} else if (name.equals("granite maul")) {
			return 1665;
		} else if (name.contains("whip")) {
			return 1658;
		} else if (name.contains("warhammer")) {
			return 406;
		} else if (name.contains("spear")) {
			switch (player.getAttackStyle()) {
				case 0:
					return 428;
				case 1:
					return 440;
				case 2:
					return 429;
				case 3:
					return 15074;
			}
		} else if (name.contains("halberd") || name.contains("scythe")) {
			switch (player.getAttackStyle()) {
				case 0:
				case 2:
					return 15074;
				case 1:
					return 440;
			}
		} else if (name.contains("mace")) {
			switch (player.getAttackStyle()) {
				case 0:
				case 1:
				case 3:
					return 406;
				case 2:
					return 15074;
			}
		} else if (name.contains("pickaxe")) {
			switch (player.getAttackStyle()) {
				case 0:
				case 1:
				case 3:
					return 15074;
				case 2:
					return 406;
			}
		} else if (name.contains("axe") || name.contains("hatchet")) {
			switch (player.getAttackStyle()) {
				case 0:
				case 1:
				case 3:
					return 395;
				case 2:
					return 401;
			}
		} else if (name.contains("korasi")) {
			switch (player.getAttackStyle()) {
				case 0:
					return 15072;
				case 1:
				case 2:
					return 15071;
				case 3:
					return 15074;
			}
		} else if (name.contains("staff") || name.contains("wand")) {
			return 406;
		} else if (name.contains("scimitar")) {
			return 15071;
		} else if (name.contains("2h") || name.contains("godsword") || name.equals("saradomin sword")) {
			switch (player.getAttackStyle()) {
				case 0:
				case 1:
				case 3:
					return 407;
				case 2:
					return 406;
			}
		} else if (name.contains("longsword") || name.contains("katana")) {
			switch (player.getAttackStyle()) {
				case 0:
				case 1:
					return 12311;
				case 2:
					return 12310;
				case 3:
					return 15074;
			}
		} else if (name.contains("dagger") || name.contains("sword")) {
			switch (player.getAttackStyle()) {
				case 0:
				case 1:
				case 3:
					if (name.contains("dragon")) {
						return 402;
					} else {
						return 15074;
					}
				case 2:
					return 451;
			}
		} else if (name.equals("unarmed") || name.equals("crate with zanik")) {
			switch (player.getAttackStyle()) {
				case 0:
					return 422;
				case 1:
					return 423;
				case 2:
					return 424;
			}
		}

		if (player.rights >= 3) {
			player.sendMessage("Alert: Your weapon does not have animations set.");
		}

		return -1;
	}

	/**
	 * Weapon stand, walk, run, etc emotes
	 */

	public static void setMovementAnimationIds(Player player) {

		String weaponName = ItemUtility.getName(player.equipment[PlayerConstants.WEAPON]).toLowerCase();

		player.playerStandIndex = 0x328;
		player.playerTurnIndex = 0x337;
		player.playerWalkIndex = 0x333;
		player.playerTurn180Index = 0x334;
		player.playerTurn90CWIndex = 0x335;
		player.playerTurn90CCWIndex = 0x336;
		player.playerRunIndex = 0x338;

		switch (player.equipment[Player.playerWeapon]) {
			case 4024:
			case 4029:
				player.playerStandIndex = 1388;
				player.playerWalkIndex = 1380;
				player.playerRunIndex = 1381;
				return;
			case 4026:
			case 4027:
				player.playerStandIndex = 1401;
				player.playerWalkIndex = 1399;
				player.playerRunIndex = 1400;
				return;
			case 4031: //219, 220, 222
				player.playerStandIndex = 222;
				player.playerWalkIndex = 220;
				player.playerRunIndex = 219;
				return;
		}

		if (weaponName.equals("hand cannon")) {
			player.playerStandIndex = 12155;
			player.playerWalkIndex = 12154;
			player.playerRunIndex = 12154;
		}
		if (weaponName.equals("giant's hand")) {
			player.playerStandIndex = 4650;
			player.playerWalkIndex = 4649;
			return;
		}
		if (weaponName.equals("crate with zanik")) {
			player.playerStandIndex = 4193;
			player.playerWalkIndex = 4194;
			return;
		}
		if (weaponName.contains("scimitar") || weaponName.contains("rapier")) {
			player.playerStandIndex = 15069;
			player.playerWalkIndex = 15073;
			player.playerRunIndex = 15070;
			return;
		}
		if (weaponName.contains("whip")) {
			player.playerStandIndex = 11973;
			player.playerWalkIndex = 11975;
			player.playerRunIndex = 1661;
			return;
		}
		if (weaponName.contains("maul") && !weaponName.contains("granite")) {
			player.playerStandIndex = 12000;
			player.playerWalkIndex = 1663;
			player.playerRunIndex = 1664;
			return;
		}
		if (weaponName.contains("anchor")) {
			player.playerStandIndex = 5869;
			player.playerWalkIndex = 5867;
			player.playerRunIndex = 5868;
			return;
		}
		if (weaponName.contains("wand")) {
			player.playerStandIndex = 8980;
			player.playerWalkIndex = 1210;
			player.playerRunIndex = 1146;
			return;
		}
		if (weaponName.contains("sled")) {
			player.playerStandIndex = 1461;
			player.playerWalkIndex = 1468;
			player.playerRunIndex = 1467;
			return;
		}
		if (weaponName.contains("halberd") || weaponName.contains("guthan")) {
			player.playerStandIndex = 809;
			player.playerWalkIndex = 1146;
			player.playerRunIndex = 1210;
			return;
		}
		if (weaponName.contains("dharok")) {
			player.playerStandIndex = 0x811;
			player.playerWalkIndex = 0x67F;
			player.playerRunIndex = 0x680;
			return;
		}
		if (weaponName.contains("ahrim")) {
			player.playerStandIndex = 809;
			player.playerWalkIndex = 1146;
			player.playerRunIndex = 1210;
			return;
		}
		if (weaponName.contains("verac")) {
			player.playerStandIndex = 0x328;
			player.playerWalkIndex = 0x333;
			player.playerRunIndex = 824;
			return;
		}
		if (weaponName.contains("wand") || weaponName.contains("staff")) {
			player.playerStandIndex = 813;
			player.playerRunIndex = 1210;
			player.playerWalkIndex = 1146;
			return;
		}
		if (weaponName.contains("karil")) {
			player.playerStandIndex = 2074;
			player.playerWalkIndex = 2076;
			player.playerRunIndex = 2077;
			return;
		}
		if (weaponName.contains("2h sword") || weaponName.contains("godsword") || weaponName.contains("saradomin sw")) {
			player.playerStandIndex = 7047;
			player.playerWalkIndex = 7046;
			player.playerRunIndex = 7039;
			/*
			 * c.playerTurnIndex = 7044; c.playerTurn180Index = 7044; c.playerTurn90CWIndex = 7044;
			 * c.playerTurn90CCWIndex = 7044;
			 */
			return;
		}
		if (weaponName.contains("bow")) {
			player.playerStandIndex = 808;
			player.playerWalkIndex = 819;
			player.playerRunIndex = 824;
			return;
		}

		switch (player.equipment[3]) {
			case 4151:
			case 21371:
				player.playerStandIndex = 1832;
				player.playerWalkIndex = 1660;
				player.playerRunIndex = 1661;
				break;
			case 6528:
				player.playerStandIndex = 0x811;
				player.playerWalkIndex = 2064;
				player.playerRunIndex = 1664;
				break;
			case 4153:
				player.playerStandIndex = 1662;
				player.playerWalkIndex = 1663;
				player.playerRunIndex = 1664;
				break;
			case 11694:
			case 11696:
			case 11730:
			case 11698:
			case 11700:
				player.playerStandIndex = 4300;
				player.playerWalkIndex = 4306;
				player.playerRunIndex = 4305;
				break;
			case 1305:
				player.playerStandIndex = 809;
				break;
			case 7671:
			case 7673:
				player.playerStandIndex = 3677;
				player.playerWalkIndex = 3680;
				// c.playerRunIndex = 4305;
		}
	}


}
