package com.zionscape.server.model.players.combat;

import com.zionscape.server.model.items.Blowpipe;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;

import java.security.SecureRandom;

public class CombatHelper {

	private static final SecureRandom random = new SecureRandom();
	/**
	 * Weapon names which have only 3 attack styles
	 */
	private static final String[] OPTIONS = {"unarmed", "whip", "warhammer", "hammers", "staff", "wand", "halberd",
			"scythe", "bow", "10", "seecull", "dart", "knife", "javelin", "toktz-xil-ul", "granite maul"};

	public static boolean correctBowAndArrow(Player player) {
		int weapon = player.equipment[Player.playerWeapon];
		int arrows = player.equipment[player.playerArrows];

		if (weapon == Blowpipe.BLOWPIPE) {
			return true;
		}

		if (weapon >= 4211 && weapon <= 4223) {
			return true;
		}
		if (weapon >= 20171 && weapon <= 20173) {
			return true;
		}
		switch (player.equipment[Player.playerWeapon]) {
			case 9705:
				return arrows == 9706;
			case 15241: // Hand cannon
				return arrows == 15243;
			case 8880: // Dorgeshuun c'bow
				if (arrows == 8882 || arrows == 877 || arrows == 9140 || arrows == 881 || arrows == 879 || arrows == 880
						|| arrows >= 9236 && arrows <= 9238)
					return true;
				break;
			case 9174: // Bronze Crossbow
				if (arrows == 877 || arrows == 881 || arrows == 879 || arrows >= 9236 && arrows <= 9237)
					return true;
				break;
			case 9177: // Iron Crossbow
				if (arrows == 877 || arrows == 9140 || arrows == 881 || arrows == 879 || arrows == 880 || arrows >= 9236
						&& arrows <= 9238)
					return true;
				break;
			case 9179: // Steel Crossbow
				if (arrows == 877 || arrows >= 9140 && arrows <= 9141 || arrows >= 879 && arrows <= 881 || arrows == 9336
						|| arrows >= 9236 && arrows <= 9239)
					return true;
				break;
			case 9181: // Mithril Crossbow
				if (arrows == 877 || arrows >= 9140 && arrows <= 9142 || arrows >= 879 && arrows <= 881 || arrows >= 9336
						&& arrows <= 9338 || arrows >= 9236 && arrows <= 9241)
					return true;
				break;
			case 9183: // Adamant Crossbow
				if (arrows == 877 || arrows >= 9140 && arrows <= 9143 || arrows >= 879 && arrows <= 881 || arrows >= 9336
						&& arrows <= 9340 || arrows >= 9236 && arrows <= 9243)
					return true;
				break;
			case 25106:
			case 10156:
			case 18357:
			case 9185: // Rune Crossbow
				if (arrows == 877 || arrows >= 9140 && arrows <= 9144 || arrows >= 879 && arrows <= 881 || arrows >= 9336
						&& arrows <= 9342 || arrows >= 9236 && arrows <= 9245)
					return true;
				break;
			case 839: // Shortbow
			case 841: // Longbow
				if (arrows >= 882 && arrows <= 884)
					return true;
				break;
			case 843: // Oak
			case 845:
				if (arrows >= 882 && arrows <= 886)
					return true;
				break;
			case 849: // Willow
			case 847:
				if (arrows >= 882 && arrows <= 888)
					return true;
				break;
			case 853: // Maple
			case 851:
				if (arrows >= 882 && arrows <= 890)
					return true;
				break;
			case 855: // Yew
			case 857:
			case 859: // Magic
			case 861:
			case 13541: // Composite bows
			case 13542:
			case 13543:
			case 19143: // Godbows
			case 19149:
			case 19146:
				if (arrows >= 882 && arrows <= 892)
					return true;
				break;
			case 11235: // Dark bow
			case 15701:
			case 15702:
			case 15703:
			case 15704:
				if (arrows >= 882 && arrows <= 892 || arrows == 11212)
					return true;
				break;
			case 4934:
			case 4935:
			case 4936:
			case 4937:
			case 4734:
				if (arrows == 4740) {
					return true;
				}
				break;
			case 837:
				if (arrows >= 877 && arrows <= 881 || arrows >= 9140 && arrows <= 9144 || arrows >= 9236 && arrows <= 9245
						|| arrows >= 9335 && arrows <= 9342) {
					return true;
				}
				break;
			default:
				if (ItemUtility.getName(weapon).contains("bow")) {
					return true;
				}
				break;
		}
		return false;
	}

	public static int getMeleeDamage(Player a, Player d) {
		double s = 0.895 * ((double) a.getCombat().calculateMeleeAttack() / (double) d.getCombat().calculateMeleeDefence(a));
		if (s > 1) {
			s = 1;
		}

		if (s < random.nextDouble()) {
			return 0;
		} else {
			// so lower numbers are, the higher you hit
			// higher numbers are, lower you hit
			int maxDamage = a.getCombat().calculateMeleeMaxHit();
			if (a.usingSpecial && (CombatHelper.isDragonDagger(a.equipment[3]) ? random.nextInt(4) == 0 : true)
					|| random.nextInt(5) == 0) { // lower to make rare hits more common
				return random.nextInt(maxDamage + 1);
			} else {
				return random.nextInt((int) ((maxDamage + 1) * 0.65)); // lower to make it more common hits
			}
		}
	}

	/**
	 * Does the given weapon name only have 3 attack styles
	 *
	 * @param option
	 * @return
	 */
	private static boolean hasOption(String option) {
		for (String str : OPTIONS) {
			if (option.contains(str)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isDragonDagger(int weapon) {
		switch (weapon) {
			case 1215:
			case 1231:
			case 5680:
			case 5698:
				return true;
		}
		return false;
	}

	/**
	 * Send a player the weapon interface which goes along with the weapon
	 *
	 * @param player     the player to send the interface too
	 * @param weapon
	 * @param weaponName
	 */
	public static void sendWeaponInterface(Player player, int weapon, String weaponName) {
		final String weaponNameCase = weaponName;
		weaponName = weaponName.toLowerCase().replace("(p)", "").replace("(p+)", "").replace("(p++)", "").trim();
		if (weaponName.equals("unarmed")) {
			player.setSidebarInterface(0, 5855);
			player.getPA().sendFrame126(weaponNameCase, 5857);
		} else if (weaponName.equalsIgnoreCase("morrigan's throwing axe")) {
			player.setSidebarInterface(0, 4446);
			player.getPA().sendFrame246(4447, 200, weapon);
			player.getPA().sendFrame126(weaponNameCase, 4449);
		} else if (weaponName.endsWith("whip")) {
			player.setSidebarInterface(0, 12290);
			player.getPA().sendFrame246(12291, 200, weapon);
			player.getPA().sendFrame126(weaponNameCase, 12293);
		} else if (weaponName.endsWith("warhammer") || weaponName.endsWith("hammers")
				|| weaponName.equals("tzhaar-ket-om") || weaponName.contains("maul") && !weaponName.contains("granite")) {
			player.setSidebarInterface(0, 425);
			player.getPA().sendFrame246(426, 200, weapon);
			player.getPA().sendFrame126(weaponNameCase, 428);
		} else if (weaponName.equals("hand cannon") || weaponName.contains("x-bow") || weaponName.endsWith("bow")
				|| weaponName.endsWith("10") || weaponName.endsWith("full") || weaponName.startsWith("seercull")) {
			player.setSidebarInterface(0, 1764);
			player.getPA().sendFrame246(1765, 200, weapon);
			player.getPA().sendFrame126(weaponNameCase, 1767);
		} else if (weaponName.startsWith("staff") && !weaponName.contains("light") || weaponName.endsWith("staff") || weaponName.endsWith("wand")) {
			player.setSidebarInterface(0, 328);
			player.getPA().sendFrame246(329, 200, weapon);
			player.getPA().sendFrame126(weaponNameCase, 331);
		} else if (weaponName.endsWith("dart") || weaponName.endsWith("knife") || weaponName.endsWith("javelin") || weaponName.equals("toktz-xil-ul") || weaponName.endsWith("blowpipe")) {
			player.setSidebarInterface(0, 4446);
			player.getPA().sendFrame246(4447, 200, weapon);
			player.getPA().sendFrame126(weaponNameCase, 4449);
		} else if (weaponName.contains("rapier") || weaponName.endsWith("dagger") || weaponName.endsWith("sword")
				&& !weaponName.endsWith("longsword") && !weaponName.endsWith("godsword")
				&& !weaponName.equals("saradomin sword") & !weaponName.endsWith("2h sword")) {
			player.setSidebarInterface(0, 2276);
			player.getPA().sendFrame246(2277, 200, weapon);
			player.getPA().sendFrame126(weaponNameCase, 2279);
		} else if (weaponName.endsWith("godsword") || weaponName.equals("saradomin sword")
				|| weaponName.endsWith("2h sword") || weaponName.equals("staff of light")) {
			player.setSidebarInterface(0, 4705);
			player.getPA().sendFrame246(4706, 200, weapon);
			player.getPA().sendFrame126(weaponNameCase, 4708);
		} else if (weaponName.endsWith("pickaxe")) {
			player.setSidebarInterface(0, 5570);
			player.getPA().sendFrame246(5571, 200, weapon);
			player.getPA().sendFrame126(weaponNameCase, 5573);
		} else if (weaponName.contains("dharok's axe") || weaponName.endsWith("axe")
				|| weaponName.endsWith("battleaxe")) {
			player.setSidebarInterface(0, 1698);
			player.getPA().sendFrame246(1699, 200, weapon);
			player.getPA().sendFrame126(weaponNameCase, 1701);
		} else if (weaponName.endsWith("halberd") || weaponName.equals("scythe")) {
			player.setSidebarInterface(0, 8460);
			player.getPA().sendFrame246(8461, 200, weapon);
			player.getPA().sendFrame126(weaponNameCase, 8463);
		} else if (weaponName.endsWith("spear")) {
			player.setSidebarInterface(0, 4679);
			player.getPA().sendFrame246(4680, 200, weapon);
			player.getPA().sendFrame126(weaponNameCase, 4682);
		} else if (weaponName.endsWith("mace") || weaponName.endsWith("flail") || weaponName.endsWith("anchor")) {
			player.setSidebarInterface(0, 3796);
			player.getPA().sendFrame246(3797, 200, weapon);
			player.getPA().sendFrame126(weaponNameCase, 3799);
		} else if (weaponName.endsWith("claws")) {
			player.setSidebarInterface(0, 7762);
			player.getPA().sendFrame246(7763, 200, weapon);
			player.getPA().sendFrame126(weaponNameCase, 7765);
		} else if (weapon == 4153) {
			player.setSidebarInterface(0, 425);
			player.getPA().sendFrame246(426, 200, weapon);
			player.getPA().sendFrame126(weaponNameCase, 428);
		} else {
			player.setSidebarInterface(0, 2423);
			player.getPA().sendFrame246(2424, 200, weapon);
			player.getPA().sendFrame126(weaponNameCase, 2426);
		}
	}

	/**
	 * Sets a players attack style, when clicking between the buttons
	 *
	 * @param player
	 * @param button
	 * @return
	 */
	public static boolean setAttackStyle(Player player, int button) {
		// player.sendMessage("button: " + button);
		switch (button) {
			// Unarmed
			case 22230:
				player.setAttackStyle(1);
				return true;
			case 22229:
				player.setAttackStyle(2);
				return true;
			case 22228:
				player.setAttackStyle(0);
				return true;
			// Scimitars / Longswords
			case 9125:
				player.setAttackStyle(0);
				return true;
			case 9128:
				player.setAttackStyle(1);
				return true;
			case 9127:
				player.setAttackStyle(2);
				return true;
			case 9126:
				player.setAttackStyle(3);
				return true;
			// Daggers / Swords
			case 8234:
				player.setAttackStyle(0);
				return true;
			case 8237:
				player.setAttackStyle(1);
				return true;
			case 8236:
				player.setAttackStyle(2);
				return true;
			case 8235:
				player.setAttackStyle(3);
				return true;
			// 2h
			case 18103:
				player.setAttackStyle(0);
				return true;
			case 18106:
				player.setAttackStyle(1);
				return true;
			case 18105:
				player.setAttackStyle(2);
				return true;
			case 18104:
				player.setAttackStyle(3);
				return true;
			// Staffs
			case 1080:
				player.setAttackStyle(0);
				return true;
			case 1079:
				player.setAttackStyle(1);
				return true;
			case 1078:
				player.setAttackStyle(2);
				return true;
			// Axes
			case 6168:
				player.setAttackStyle(0);
				return true;
			case 6171:
				player.setAttackStyle(1);
				return true;
			case 6170:
				player.setAttackStyle(2);
				return true;
			case 6169:
				player.setAttackStyle(3);
				return true;
			// Pickaxes
			case 21200:
				player.setAttackStyle(0);
				return true;
			case 21203:
				player.setAttackStyle(1);
				return true;
			case 21202:
				player.setAttackStyle(2);
				return true;
			case 21201:
				player.setAttackStyle(3);
				return true;
			// Claws
			case 30088:
				player.setAttackStyle(0);
				return true;
			case 30091:
				player.setAttackStyle(1);
				return true;
			case 30090:
				player.setAttackStyle(2);
				return true;
			case 30089:
				player.setAttackStyle(3);
				return true;
			// Maces
			case 14218:
				player.setAttackStyle(0);
				return true;
			case 14221:
				player.setAttackStyle(1);
				return true;
			case 14220:
				player.setAttackStyle(2);
				return true;
			case 14219:
				player.setAttackStyle(3);
				return true;
			// Warhammers / Torag Hammers
			case 1177:
				player.setAttackStyle(0);
				return true;
			case 1176:
				player.setAttackStyle(1);
				return true;
			case 1175:
				player.setAttackStyle(2);
				return true;
			// Whip
			case 48010:
				player.setAttackStyle(0);
				return true;
			case 48009:
				player.setAttackStyle(1);
				return true;
			case 48008:
				player.setAttackStyle(2);
				return true;
			// Guthans warspear / Spears
			case 18077:
				player.setAttackStyle(0);
				return true;
			case 18080:
				player.setAttackStyle(1);
				return true;
			case 18079:
				player.setAttackStyle(2);
				return true;
			case 18078:
				player.setAttackStyle(3);
				return true;
			// Halberds
			case 33018:
				player.setAttackStyle(0);
				return true;
			case 33020:
				player.setAttackStyle(1);
				return true;
			case 33019:
				player.setAttackStyle(2);
				return true;
			// Darts
			case 17102:
				player.setAttackStyle(0);
				return true;
			case 17101:
				player.setAttackStyle(1);
				return true;
			case 17100:
				player.setAttackStyle(2);
				return true;
			// Bows
			case 6236:
				player.setAttackStyle(0);
				return true;
			case 6235:
				player.setAttackStyle(1);
				return true;
			case 6234:
				player.setAttackStyle(2);
				return true;
		}
		return false;
	}

	/**
	 * Set a players attack style and type, when switching weapons
	 *
	 * @param player
	 */
	public static void setAttackVars(final Player player) {
		String name = "unarmed";
		if (player.equipment[3] > 0) {
			// TODO: some items appear to be missing names, these need correcting or the weapon wont be mapped out
			// correctly
			String tempName = ItemUtility.getName(player.equipment[3]);
			if (tempName != null) {
				name = tempName.toLowerCase();
			}
		}
		if (name.equals("hand cannon") || name.endsWith("bow") || name.endsWith("10") || name.endsWith("full")
				|| name.startsWith("seercull") || name.endsWith("throwing axe") || name.endsWith("blowpipe") || name.endsWith(" knife") || name.endsWith(" dart")) {
			player.setCombatType(CombatType.RANGED);
			switch (player.getAttackStyle()) {
				case 0:
					player.setCombatStyle(CombatStyle.ACCURATE);
					break;
				case 1:
					player.setCombatStyle(CombatStyle.RAPID);
					break;
				case 2:
					player.setCombatStyle(CombatStyle.LONG_RANGE);
					break;
			}
		} else if (name.equals("granite maul")) {
			player.setCombatType(CombatType.CHRUSH);
			switch (player.getAttackStyle()) {
				case 0:
					player.setCombatStyle(CombatStyle.ACCURATE);
					break;
				case 1:
					player.setCombatStyle(CombatStyle.AGGRESSIVE);
					break;
				case 2:
					player.setCombatStyle(CombatStyle.DEFENSIVE);
					break;
			}
		} else if (name.contains("whip")) {
			player.setCombatType(CombatType.SLASH);
			switch (player.getAttackStyle()) {
				case 0:
					player.setCombatStyle(CombatStyle.ACCURATE);
					break;
				case 1:
					player.setCombatStyle(CombatStyle.CONTROLLED);
					break;
				case 2:
					player.setCombatStyle(CombatStyle.DEFENSIVE);
					break;
			}
		} else if (name.contains("warhammer") || name.contains("hammers") || name.equals("tzhaar-ket-om")
				|| name.contains("maul") && !name.contains("granite")) {
			player.setCombatType(CombatType.CHRUSH);
			switch (player.getAttackStyle()) {
				case 0:
					player.setCombatStyle(CombatStyle.ACCURATE);
					break;
				case 1:
					player.setCombatStyle(CombatStyle.AGGRESSIVE);
					break;
				case 2:
					player.setCombatStyle(CombatStyle.DEFENSIVE);
					break;
			}
		} else if (name.contains("spear")) {
			switch (player.getAttackStyle()) {
				case 0:
					player.setCombatStyle(CombatStyle.CONTROLLED);
					player.setCombatType(CombatType.STAB);
					break;
				case 1:
					player.setCombatStyle(CombatStyle.CONTROLLED);
					player.setCombatType(CombatType.SLASH);
					break;
				case 2:
					player.setCombatStyle(CombatStyle.CONTROLLED);
					player.setCombatType(CombatType.CHRUSH);
					break;
				case 3:
					player.setCombatStyle(CombatStyle.DEFENSIVE);
					player.setCombatType(CombatType.STAB);
					break;
			}
		} else if (name.contains("halberd") || name.contains("scythe")) {
			switch (player.getAttackStyle()) {
				case 0:
					player.setCombatStyle(CombatStyle.CONTROLLED);
					player.setCombatType(CombatType.STAB);
					break;
				case 1:
					player.setCombatStyle(CombatStyle.AGGRESSIVE);
					player.setCombatType(CombatType.SLASH);
					break;
				case 2:
					player.setCombatStyle(CombatStyle.DEFENSIVE);
					player.setCombatType(CombatType.STAB);
					break;
			}
		} else if (name.contains("mace") || name.contains("flail")) {
			switch (player.getAttackStyle()) {
				case 0:
					player.setCombatStyle(CombatStyle.ACCURATE);
					player.setCombatType(CombatType.CHRUSH);
					break;
				case 1:
					player.setCombatStyle(CombatStyle.AGGRESSIVE);
					player.setCombatType(CombatType.CHRUSH);
					break;
				case 2:
					player.setCombatStyle(CombatStyle.CONTROLLED);
					player.setCombatType(CombatType.STAB);
					break;
				case 3:
					player.setCombatStyle(CombatStyle.DEFENSIVE);
					player.setCombatType(CombatType.CHRUSH);
					break;
			}
		} else if (name.contains("pickaxe")) {
			switch (player.getAttackStyle()) {
				case 0:
					player.setCombatStyle(CombatStyle.ACCURATE);
					player.setCombatType(CombatType.STAB);
					break;
				case 1:
					player.setCombatStyle(CombatStyle.AGGRESSIVE);
					player.setCombatType(CombatType.STAB);
					break;
				case 2:
					player.setCombatStyle(CombatStyle.AGGRESSIVE);
					player.setCombatType(CombatType.CHRUSH);
					break;
				case 3:
					player.setCombatStyle(CombatStyle.DEFENSIVE);
					player.setCombatType(CombatType.STAB);
					break;
			}
		} else if (name.contains("axe")) {
			switch (player.getAttackStyle()) {
				case 0:
					player.setCombatStyle(CombatStyle.ACCURATE);
					player.setCombatType(CombatType.SLASH);
					break;
				case 1:
					player.setCombatStyle(CombatStyle.AGGRESSIVE);
					player.setCombatType(CombatType.SLASH);
					break;
				case 2:
					player.setCombatStyle(CombatStyle.AGGRESSIVE);
					player.setCombatType(CombatType.CHRUSH);
					break;
				case 3:
					player.setCombatStyle(CombatStyle.DEFENSIVE);
					player.setCombatType(CombatType.SLASH);
					break;
			}
		} else if (name.contains("staff") || name.contains("wand")) {
			player.setCombatType(CombatType.CHRUSH);
			switch (player.getAttackStyle()) {
				case 0:
					player.setCombatStyle(CombatStyle.ACCURATE);
					break;
				case 1:
					player.setCombatStyle(CombatStyle.AGGRESSIVE);
					break;
				case 2:
					player.setCombatStyle(CombatStyle.DEFENSIVE);
					break;
			}
		} else if (name.contains("2h") || name.contains("godsword") || name.equals("saradomin sword")) {
			switch (player.getAttackStyle()) {
				case 0:
					player.setCombatStyle(CombatStyle.ACCURATE);
					player.setCombatType(CombatType.SLASH);
					break;
				case 1:
					player.setCombatStyle(CombatStyle.AGGRESSIVE);
					player.setCombatType(CombatType.SLASH);
					break;
				case 2:
					player.setCombatStyle(CombatStyle.AGGRESSIVE);
					player.setCombatType(CombatType.CHRUSH);
					break;
				case 3:
					player.setCombatStyle(CombatStyle.DEFENSIVE);
					player.setCombatType(CombatType.SLASH);
					break;
			}
		} else if (name.contains("scimitar") || name.contains("longsword") || name.contains("claw")
				|| name.contains("katana")) {
			switch (player.getAttackStyle()) {
				case 0:
					player.setCombatStyle(CombatStyle.ACCURATE);
					player.setCombatType(CombatType.SLASH);
					break;
				case 1:
					player.setCombatStyle(CombatStyle.AGGRESSIVE);
					player.setCombatType(CombatType.SLASH);
					break;
				case 2:
					player.setCombatStyle(CombatStyle.CONTROLLED);
					player.setCombatType(CombatType.STAB);
					break;
				case 3:
					player.setCombatStyle(CombatStyle.DEFENSIVE);
					player.setCombatType(CombatType.SLASH);
					break;
			}
		} else if (name.contains("dagger") || name.contains("sword") || name.contains("rapier")) {
			switch (player.getAttackStyle()) {
				case 0:
					player.setCombatStyle(CombatStyle.ACCURATE);
					player.setCombatType(CombatType.STAB);
					break;
				case 1:
					player.setCombatStyle(CombatStyle.AGGRESSIVE);
					player.setCombatType(CombatType.STAB);
					break;
				case 2:
					player.setCombatStyle(CombatStyle.AGGRESSIVE);
					player.setCombatType(CombatType.SLASH);
					break;
				case 3:
					player.setCombatStyle(CombatStyle.DEFENSIVE);
					player.setCombatType(CombatType.STAB);
					break;
			}
		} else if (name.contains("unarmed")) {
			player.setCombatType(CombatType.CHRUSH);
			switch (player.getAttackStyle()) {
				case 2:
					player.setCombatStyle(CombatStyle.AGGRESSIVE);
					break;
				case 0:
					player.setCombatStyle(CombatStyle.DEFENSIVE);
					break;
				case 1:
					player.setCombatStyle(CombatStyle.ACCURATE);
					break;
			}
		}
	}

	/**
	 * Sets the default values for when a player switches weapons
	 *
	 * @param player
	 */
	public static void switchWeapons(Player player) {
		String name = "unarmed";
		if (player.equipment[3] > 0) {
			name = ItemUtility.getName(player.equipment[3]).toLowerCase();
		}
		if (player.getAttackStyle() == 3) {
			if (CombatHelper.hasOption(name)) {
				player.setAttackStyle(2);
			}
		}
		player.getPA().sendFrame36(43, player.getAttackStyle());
		CombatHelper.setAttackVars(player);
	}


	public static int getRequiredDistance(Player c) {
		if (c.playerIndex < 1 && c.npcIndex < 1) {
			return 1;
		}

		// All combat magic spells have an attack range of 10 regardless of the level of the spell of which to cast it.
		if (c.spellId > 0 || c.autocasting || c.usingMagic) {
			return 10;
		}

		if (usingRange(c)) {
			String name = ItemUtility.getName(c.equipment[PlayerConstants.WEAPON]).toLowerCase();

			if (name.endsWith("comp bow")) {
				return 10;
			}
			if (name.contains("crystal bow")) {
				return 10;
			}

			// long range
			if (c.getCombatStyle() == CombatStyle.LONG_RANGE) {
				if (name.endsWith("blowpipe")) {
					return 7;
				}
				if (name.endsWith("shortbow")) {
					return 9;
				}
				if (name.endsWith("longbow")) {
					return 10;
				}
				if (name.endsWith("crossbow") || name.endsWith("c'bow")) {
					return 9;
				}
				if (name.contains("knife")) {
					return 6;
				}
				if (name.contains("dart")) {
					return 5;
				}
				if (name.equalsIgnoreCase("dark bow")) {
					return 10;
				}
				if (name.contains("chinchompa")) {
					return 10;
				}
				if (name.contains("javelin")) {
					return 7;
				}
				if (name.contains("throwing axe")) {
					return 6;
				}
			} else {
				if (name.endsWith("blowpipe")) {
					return 5;
				}
				if (name.endsWith("shortbow")) {
					return 7;
				}
				if (name.endsWith("longbow")) {
					return 9;
				}
				if (name.endsWith("crossbow") || name.endsWith("c'bow")) {
					return 7;
				}
				if (name.contains("knife")) {
					return 4;
				}
				if (name.contains("dart")) {
					return 3;
				}
				if (name.equalsIgnoreCase("dark bow")) {
					return 9;
				}
				if (name.contains("chinchompa")) {
					return 9;
				}
				if (name.contains("javelin")) {
					return 5;
				}
				if (name.contains("throwing axe")) {
					return 4;
				}
			}

			if (c.rights == 3) {
				c.sendMessage("MeleeRequirements::getRequiredDistance unmapped weapon " + name);
			}

			return 8;
		} else if (c.getCombat().usingHally()) {
			return 2;
		} else {
			return 1;
		}
	}

	public static boolean usingRange(Player c) {
		return c.usingBow || c.usingRangeWeapon || c.equipment[PlayerConstants.WEAPON] == 15241;
	}

	/**
	 * The CombatHelper style that a player uses in combat affects what experience they receive. A player's attack style
	 * is chosen on the combat screen section of the interface.
	 *
	 * @author thispixel
	 */
	public enum CombatStyle {
		ACCURATE(0), AGGRESSIVE(2), DEFENSIVE(1), CONTROLLED(-1), RAPID(-1), LONG_RANGE(-1);

		private final int skill;

		CombatStyle(int skill) {
			this.skill = skill;
		}

		public int getSkill() {
			return skill;
		}
	}

	/**
	 * The attack types are the different ways in which a player or non-player character may attack another player,
	 * another NPC, or a piece of interactive scenery that can be attacked. Equipment gives players attack bonuses
	 * towards and defence bonuses against each of the attack types.
	 *
	 * @author thispixel
	 */
	public enum CombatType {
		STAB(0, 5), SLASH(1, 6), CHRUSH(2, 7), MAGIC(3, 8), RANGED(4, 9);

		private final int attackBonusIndex;
		private final int defenceBonusIndex;

		CombatType(int attackBonusIndex, int defenceBonusIndex) {
			this.attackBonusIndex = attackBonusIndex;
			this.defenceBonusIndex = defenceBonusIndex;
		}

		public int getAttackBonusIndex() {
			return attackBonusIndex;
		}

		public int getDefenceBonusIndex() {
			return defenceBonusIndex;
		}
	}

}
