package com.zionscape.server.model.players.skills.smithing;

import com.zionscape.server.gamecycle.GameCycleTask;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.util.Misc;

/**
 * Smithing.java
 *
 * @author Sanity
 */
public class Smithing {

	private final int[] SMELT_FRAME = { 2405, 2406, 2407, 2409, 2410, 2411, 2412, 2413 };

	// bronze, iron, silver, steel, gold, mithril, addy, rune
	private final int[] SMELT_BARS = { 2349, 2351, 2355, 2353, 2357, 2359, 2361, 2363 };
	public int item;
	public int xp;
	public int remove;
	public int removeamount;
	public int maketimes;
	private Player c;

	public Smithing(Player c) {
		this.c = c;
	}

	// case 3044:
	// case 21303:
	// case 26814:

	public static boolean itemOnObject(Player player, int objectID, Location objectLocation, int itemId) {

		switch (objectID) {
		case 11666:
		case 3044:
		case 21303:
		case 26814:
			if (itemId == 25617) {
				if (player.isBusy()) {
					return true;
				}

				if (player.getActualLevel(PlayerConstants.SMITHING) < 80) {
					player.sendMessage("This requires level 80+ smithing.");
					return true;
				}

				player.setBusy(true);
				final Location location = player.getLocation();
				GameCycleTaskHandler.addEvent(player, container -> {

					if (!player.isBusy() || !player.getLocation().equals(location)) {
						player.setBusy(false);
						container.stop();
						return;
					}

					int amount = player.getItems().getItemAmount(25617);

					if (amount <= 0) {
						player.setBusy(false);
						container.stop();
						return;
					}

					player.getItems().deleteItem(itemId, 1);
					player.getItems().addItem(25618, 1);

					player.getPA().addSkillXP(82, PlayerConstants.SMITHING);

					player.sendMessage("You turn the Red standstone into Robust glass.");

					if (amount - 1 <= 0) {
						player.setBusy(false);
						container.stop();
					}
				}, 4);
				return true;
			}
			break;
		}

		return false;
	}

	public static boolean startSmelting(final Player player, final int iFace, int amount, final boolean superheat) {
		player.getPA().removeAllWindows();
		if (amount > 28)
			amount = 28;
		if (player.isBusy())
			return false;
		SmeltBar bar = null;
		if (superheat) {
			for (final SmeltBar bars : SmeltBar.values()) {
				if (bars.getOreId() == iFace) {
					bar = bars;
					break;
				}
			}
		} else {
			for (final SmeltBar bars : SmeltBar.values())
				for (int i = 0; i < bars.getInterfaceIds().length; i++)
					if (bars.getInterfaceIds()[i] == iFace) {
						bar = bars;
						break;
					}
		}
		if (bar == null) {
			if (superheat) {
				player.sendMessage("Nothing interesting happens.");
			}
			return false;
		}
		if (player.level[PlayerConstants.SMITHING] < bar.getLevelRequired()) {
			player.sendMessage("You require " + bar.getLevelRequired() + " to smith this bar.");
			return true;
		}
		if (!player.getItems().playerHasItem(bar.getOreId(), bar.getOreAmount())) {
			player.sendMessage("You do not have the required ore to create this bar.");
			return true;
		}
		if (bar.getOreId() != 0)
			if (!player.getItems().playerHasItem(bar.getOtherOre(), bar.getOtherOreAmount())) {
				player.sendMessage("You do not have the required ore to create this bar.");
				return true;
			}
		player.setBusy(true);
		if (superheat) {
			player.startAnimation(722);
			player.gfx100(148);
		} else {
			player.startAnimation(899);
		}
		final SmeltBar smeltBar = bar;
		final int smeltAmount = amount;
		GameCycleTaskHandler.addEvent(player, new GameCycleTask() {
			int tempAmount = 0;
			Location l = player.getLocation();

			@Override
			public void execute(final GameCycleTaskContainer container) {
				if (!l.equals(player.getLocation())) {
					container.stop();
					return;
				}
				if (player.level[PlayerConstants.SMITHING] < smeltBar.getLevelRequired()) {
					player.sendMessage("You require " + smeltBar.getLevelRequired() + " to smith this bar.");
					container.stop();
					return;
				}
				if (!player.getItems().playerHasItem(smeltBar.getOreId(), smeltBar.getOreAmount())) {
					player.sendMessage("You do not have the required ore to create this bar.");
					container.stop();
					return;
				}
				if (smeltBar.getOreId() != 0)
					if (!player.getItems().playerHasItem(smeltBar.getOtherOre(), smeltBar.getOtherOreAmount())) {
						player.sendMessage("You do not have the required ore to create this bar.");
						container.stop();
						return;
					}
				if (superheat) {
					player.getItems().deleteItem(561, 1);
					if (player.equipment[PlayerConstants.WEAPON] != 1393)
						player.getItems().deleteItem(554, 4);
				}
				player.getItems().deleteItem(smeltBar.getOreId(), smeltBar.getOreAmount());
				if (smeltBar.getOtherOre() != 0)
					player.getItems().deleteItem(smeltBar.getOtherOre(), smeltBar.getOtherOreAmount());
				boolean doSmelt = true;
				if (smeltBar == SmeltBar.IRON) {
					final int playerLevel = player.level[PlayerConstants.SMITHING];
					int percentage = 0;
					if (player.level[PlayerConstants.SMITHING] > 45)
						percentage = (int) (80 + ((playerLevel - 45) * 0.3703703703703704));
					else
						percentage = player.level[PlayerConstants.SMITHING] + 35;
					if (percentage < Misc.random(100))
						doSmelt = false;
				}
				if (doSmelt) {
					player.getItems().addItem(smeltBar.getBarId(), 1);
					if (smeltBar.equals(SmeltBar.GOLD) && player.equipment[PlayerConstants.HANDS] == 777)
						player.getPA().addSkillXP(56, PlayerConstants.SMITHING);
					else {
						player.getPA().addSkillXP(smeltBar.getXp(), PlayerConstants.SMITHING);
						if (superheat) {
							player.getPA().addSkillXP(53, PlayerConstants.MAGIC);
						}
					}

					if (smeltBar.getBarId() == 2359) {
						Achievements.progressMade(player, Achievements.Types.SMITH_500_MITHRIL_BARS);
					}
					if (smeltBar.getBarId() == 2361) {
						Achievements.progressMade(player, Achievements.Types.SMITH_1000_ADDY_BARS);
					}
					if (smeltBar.getBarId() == 2363) {
						Achievements.progressMade(player, Achievements.Types.SMELT_500_RUNE_BARS);
					}
					if (smeltBar.getBarId() == 2349) {
						Achievements.progressMade(player, Achievements.Types.SMELT_100_BRONZE_BARS);
					}
					if (smeltBar.getBarId() == 2353) {
						Achievements.progressMade(player, Achievements.Types.SMELT_300_STEEL_BARS);
					}

					player.sendMessage("You smelt the ore into a bar.");
				} else
					player.sendMessage("You fail to smelt the iron ore into a bar.");
				if (superheat) {
					container.stop();
					return;
				}
				tempAmount++;
				if (tempAmount >= smeltAmount) {
					container.stop();
					return;
				}
				player.startAnimation(899);
			}

			@Override
			public void stop() {
				player.setBusy(false);
			}

		}, 3);
		return true;
	}

	private void CheckAddy(Player c, int level, int amounttomake, String type) {
		remove = 2361;
		if (type.equalsIgnoreCase("9380") && level >= 73) { // bolt
			xp = 63;
			item = 9380;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("9429") && level >= 76) { // limb
			xp = 63;
			item = 9429;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("823") && level >= 74) { // dart
			xp = 63;
			item = 823;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("3100") && level >= 83) { // claws
			xp = 125;
			item = 3100;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("1357") && level >= 71) // Axe
		{
			xp = 63;
			item = 1357;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("1211") && level >= 70) // Dagger
		{
			xp = 63;
			item = 1211;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1430") && level >= 72) // Mace
		{
			xp = 63;
			item = 1430;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1145") && level >= 73) // Med helm
		{
			xp = 63;
			item = 1145;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("9143") && level >= 74) // Dart tips
		{
			xp = 63;
			item = 9143;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1287") && level >= 74) // Sword (s)
		{
			xp = 63;
			item = 1287;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("4823") && level >= 74) // Nails
		{
			xp = 63;
			item = 4823;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("43") && level >= 75) // Arrow tips
		{
			xp = 63;
			item = 43;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1331") && level >= 75)// Scim
		{
			xp = 125;
			item = 1331;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("1301") && level >= 76) // Longsword
		{
			xp = 125;
			item = 1301;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("867") && level >= 77) // Knives
		{
			xp = 63;
			item = 867;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1161") && level >= 77) // Full Helm
		{
			xp = 125;
			item = 1161;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("1183") && level >= 78) // Square shield
		{
			xp = 125;
			item = 1183;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("1345") && level >= 79) // Warhammer
		{
			xp = 188;
			item = 1345;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1371") && level >= 80) // Battle axe
		{
			xp = 188;
			item = 1371;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1111") && level >= 81) // Chain
		{
			xp = 188;
			item = 1111;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1199") && level >= 82) // Kite
		{
			xp = 188;
			item = 1199;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1317") && level >= 84) // 2h Sword
		{
			xp = 188;
			item = 1317;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1073") && level >= 86) // Platelegs
		{
			xp = 188;
			item = 1073;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1091") && level >= 86) // PlateSkirt
		{
			xp = 188;
			item = 1091;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1123") && level >= 88) // Platebody
		{
			xp = 313;
			item = 1123;
			removeamount = 5;
			maketimes = amounttomake;
		} else {
			c.sendMessage("You don't have a high enough level to make this Item!");
			return;
		}
		this.doaction(c, item, remove, removeamount, maketimes, xp);
	}

	private void CheckBronze(Player c, int level, int amounttomake, String type) {
		remove = 2349;
		if (type.equalsIgnoreCase("9375") && level >= 3) // bolts
		{
			xp = 13;
			item = Integer.parseInt(type);
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("9420") && level >= 6) // limb
		{
			xp = 13;
			item = Integer.parseInt(type);
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("1794") && level >= 4) // wire
		{
			xp = 13;
			item = Integer.parseInt(type);
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("3095") && level >= 13) // claws
		{
			xp = 25;
			item = Integer.parseInt(type);
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("1351") && level >= 1) {
			xp = 13;
			item = 1351;
			remove = 2349;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("1205") && level >= 1) {
			xp = 13;
			item = 1205;
			remove = 2349;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1422") && level >= 2) {
			xp = 13;
			item = 1422;
			remove = 2349;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1139") && level >= 3) {
			xp = 13;
			item = 1139;
			remove = 2349;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("819") && level >= 4) {
			xp = 13;
			item = 819;
			remove = 2349;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1277") && level >= 4) {
			xp = 13;
			item = 1277;
			remove = 2349;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("4819") && level >= 4) {
			xp = 13;
			item = 4819;
			remove = 2349;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("39") && level >= 5) {
			xp = 13;
			item = 39;
			remove = 2349;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1321") && level >= 5) {
			xp = 25;
			item = 1321;
			remove = 2349;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("1291") && level >= 6) {
			xp = 25;
			item = 1291;
			remove = 2349;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("864") && level >= 7) {
			xp = 25;
			item = 864;
			remove = 2349;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1155") && level >= 7) {
			xp = 25;
			item = 1155;
			remove = 2349;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("1173") && level >= 8) {
			xp = 25;
			item = 1173;
			remove = 2349;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("1337") && level >= 9) {
			xp = 38;
			item = 1337;
			remove = 2349;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1375") && level >= 10) {
			xp = 38;
			item = 1375;
			remove = 2349;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1103") && level >= 11) {
			xp = 38;
			item = 1103;
			remove = 2349;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1189") && level >= 12) {
			xp = 38;
			item = 1189;
			remove = 2349;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1307") && level >= 14) {
			xp = 38;
			item = 1307;
			remove = 2349;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1075") && level >= 16) {
			xp = 38;
			item = 1075;
			remove = 2349;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1087") && level >= 16) {
			xp = 38;
			item = 1087;
			remove = 2349;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1117") && level >= 18) {
			xp = 63;
			item = 1117;
			remove = 2349;
			removeamount = 5;
			maketimes = amounttomake;
		} else {
			c.sendMessage("You don't have a high enough level to make this Item!");
			return;
		}
		this.doaction(c, item, remove, removeamount, maketimes, xp);
	}

	private void CheckIron(Player c, int level, int amounttomake, String type) {
		remove = 2351;

		if (type.equalsIgnoreCase("9377") && level >= 18) // bolts
		{
			xp = 25;
			item = Integer.parseInt(type);
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("9423") && level >= 23) // limb
		{
			xp = 25;
			item = Integer.parseInt(type);
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("820") && level >= 19) // dart tips
		{
			xp = 25;
			item = Integer.parseInt(type);
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("4540") && level >= 26) // lantern
		{
			xp = 25;
			item = Integer.parseInt(type);
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("3096") && level >= 28) // claws
		{
			xp = 50;
			item = Integer.parseInt(type);
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("1349") && level >= 16) // Axe
		{
			xp = 25;
			item = 1349;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("1203") && level >= 15) // Dagger
		{
			xp = 25;
			item = 1203;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1420") && level >= 17) // Mace
		{
			xp = 25;
			item = 1420;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1137") && level >= 18) // Med helm
		{
			xp = 25;
			item = 1137;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("9140") && level >= 19) // Dart tips
		{
			xp = 25;
			item = 9140;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1279") && level >= 19) // Sword (s)
		{
			xp = 25;
			item = 1277;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("4820") && level >= 19) // Nails
		{
			xp = 25;
			item = 4820;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("40") && level >= 20) // Arrow tips
		{
			xp = 25;
			item = 40;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1323") && level >= 20)// Scim
		{
			xp = 50;
			item = 1323;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("1293") && level >= 21) // Longsword
		{
			xp = 50;
			item = 1293;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("863") && level >= 22) // Knives
		{
			xp = 25;
			item = 863;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1153") && level >= 22) // Full Helm
		{
			xp = 50;
			item = 1153;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("1175") && level >= 23) // Square shield
		{
			xp = 50;
			item = 1175;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("1335") && level >= 24) // Warhammer
		{
			xp = 38;
			item = 1335;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1363") && level >= 25) // Battle axe
		{
			xp = 75;
			item = 1363;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1101") && level >= 26) // Chain
		{
			xp = 75;
			item = 1101;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1191") && level >= 27) // Kite
		{
			xp = 75;
			item = 1191;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1309") && level >= 29) // 2h Sword
		{
			xp = 75;
			item = 1309;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1067") && level >= 31) // Platelegs
		{
			xp = 75;
			item = 1067;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1081") && level >= 31) // PlateSkirt
		{
			xp = 75;
			item = 1081;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1115") && level >= 33) // Platebody
		{
			xp = 100;
			item = 1115;
			removeamount = 5;
			maketimes = amounttomake;
		} else {
			c.sendMessage("You don't have a high enough level to make this Item!");
			return;
		}
		this.doaction(c, item, remove, removeamount, maketimes, xp);
	}

	private void CheckMith(Player c, int level, int amounttomake, String type) {
		remove = 2359;
		if (type.equalsIgnoreCase("9379") && level >= 53) // bolts
		{
			xp = 50;
			item = Integer.parseInt(type);
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("9427") && level >= 56) // limbs
		{
			xp = 50;
			item = Integer.parseInt(type);
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("822") && level >= 54) // darts
		{
			xp = 50;
			item = Integer.parseInt(type);
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("3099") && level >= 63) // claws
		{
			xp = 100;
			item = Integer.parseInt(type);
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("1355") && level >= 51) // Axe
		{
			xp = 50;
			item = 1355;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("1209") && level >= 50) // Dagger
		{
			xp = 50;
			item = 1209;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1428") && level >= 52) // Mace
		{
			xp = 50;
			item = 1428;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1143") && level >= 53) // Med helm
		{
			xp = 50;
			item = 1143;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("9142") && level >= 54) // Dart tips
		{
			xp = 50;
			item = 9142;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1285") && level >= 54) // Sword (s)
		{
			xp = 50;
			item = 1285;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("4822") && level >= 54) // Nails
		{
			xp = 50;
			item = 4822;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("42") && level >= 55) // Arrow tips
		{
			xp = 50;
			item = 42;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1329") && level >= 55)// Scim
		{
			xp = 100;
			item = 1329;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("1299") && level >= 56) // Longsword
		{
			xp = 100;
			item = 1299;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("866") && level >= 57) // Knives
		{
			xp = 50;
			item = 866;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1159") && level >= 57) // Full Helm
		{
			xp = 100;
			item = 1159;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("1181") && level >= 58) // Square shield
		{
			xp = 100;
			item = 1181;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("1343") && level >= 59) // Warhammer
		{
			xp = 150;
			item = 1343;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1369") && level >= 60) // Battle axe
		{
			xp = 150;
			item = 1369;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1109") && level >= 61) // Chain
		{
			xp = 150;
			item = 1109;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1197") && level >= 62) // Kite
		{
			xp = 150;
			item = 1197;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1315") && level >= 64) // 2h Sword
		{
			xp = 150;
			item = 1315;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1071") && level >= 66) // Platelegs
		{
			xp = 150;
			item = 1071;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1085") && level >= 66) // PlateSkirt
		{
			xp = 150;
			item = 1085;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1121") && level >= 68) // Platebody
		{
			xp = 250;
			item = 1121;
			removeamount = 5;
			maketimes = amounttomake;
		} else {
			c.sendMessage("You don't have a high enough level to make this Item!");
			return;
		}
		this.doaction(c, item, remove, removeamount, maketimes, xp);
	}

	private void CheckRune(Player c, int level, int amounttomake, String type) {
		remove = 2363;
		if (type.equalsIgnoreCase("9381") && level >= 88) { // bolts
			xp = 75;
			item = 9381;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("9431") && level >= 91) { // limbs
			xp = 75;
			item = 9431;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("824") && level >= 89) { // dart tips
			xp = 75;
			item = 824;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("3101") && level >= 98) { // claws
			xp = 150;
			item = 3101;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("1359") && level >= 86) // Axe
		{
			xp = 75;
			item = 1359;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("1213") && level >= 85) // Dagger
		{
			xp = 75;
			item = 1213;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1432") && level >= 87) // Mace
		{
			xp = 75;
			item = 1432;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1147") && level >= 88) // Med helm
		{
			xp = 75;
			item = 1147;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("9144") && level >= 89) // Dart tips
		{
			xp = 75;
			item = 9144;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1289") && level >= 89) // Sword (s)
		{
			xp = 75;
			item = 1289;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("4824") && level >= 89) // Nails
		{
			xp = 75;
			item = 4824;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("44") && level >= 90) // Arrow tips
		{
			xp = 75;
			item = 44;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1333") && level >= 90)// Scim
		{
			xp = 150;
			item = 1333;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("1303") && level >= 91) // Longsword
		{
			xp = 150;
			item = 1303;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("868") && level >= 92) // Knives
		{
			xp = 75;
			item = 868;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1163") && level >= 92) // Full Helm
		{
			xp = 150;
			item = 1163;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("1185") && level >= 93) // Square shield
		{
			xp = 150;
			item = 1185;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("1347") && level >= 94) // Warhammer
		{
			xp = 225;
			item = 1347;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1373") && level >= 95) // Battle axe
		{
			xp = 225;
			item = 1373;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1113") && level >= 96) // Chain
		{
			xp = 225;
			item = 1113;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1201") && level >= 97) // Kite
		{
			xp = 225;
			item = 1201;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1319") && level >= 99) // 2h Sword
		{
			xp = 225;
			item = 1319;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1079") && level >= 99) // Platelegs
		{
			xp = 225;
			item = 1079;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1093") && level >= 99) // PlateSkirt
		{
			xp = 225;
			item = 1093;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1127") && level >= 99) // Platebody
		{
			xp = 313;
			item = 1127;
			removeamount = 5;
			maketimes = amounttomake;
		} else {
			c.sendMessage("You don't have a high enough level to make this Item!");
			return;
		}
		this.doaction(c, item, remove, removeamount, maketimes, xp);
	}

	private void CheckSteel(Player c, int level, int amounttomake, String type) {

		remove = 2353;

		if (type.equalsIgnoreCase("9378") && level >= 33) // bolts
		{
			xp = 38;
			item = Integer.parseInt(type);
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("9425") && level >= 36) // limbs
		{
			xp = 38;
			item = Integer.parseInt(type);
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("821") && level >= 34) // dart tips
		{
			xp = 38;
			item = Integer.parseInt(type);
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("3097") && level >= 43) // claws
		{
			xp = 75;
			item = Integer.parseInt(type);
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("2") && level >= 35) // cannonballs
		{
			if (!c.getItems().playerHasItem(4)) {
				c.sendMessage("An ammo mould is required to make these.");
				return;
			}

			xp = 27;
			item = 2;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("1353") && level >= 31) // Axe
		{
			xp = 38;
			item = 1353;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equalsIgnoreCase("1207") && level >= 30) // Dagger
		{
			xp = 50;
			item = 1207;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1424") && level >= 32) // Mace
		{
			xp = 50;
			item = 1424;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1141") && level >= 33) // Med helm
		{
			xp = 50;
			item = 1141;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("9141") && level >= 34) // Dart tips
		{
			xp = 50;
			item = 9141;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1281") && level >= 34) // Sword (s)
		{
			xp = 50;
			item = 1281;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1539") && level >= 34) // Nails
		{
			xp = 50;
			item = 1539;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("41") && level >= 35) // Arrow tips
		{
			xp = 50;
			item = 41;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1325") && level >= 35)// Scim
		{
			xp = 75;
			item = 1325;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("1295") && level >= 36) // Longsword
		{
			xp = 75;
			item = 1295;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("865") && level >= 37) // Knives
		{
			xp = 50;
			item = 865;
			removeamount = 1;
			maketimes = amounttomake;
		} else if (type.equals("1157") && level >= 37) // Full Helm
		{
			xp = 75;
			item = 1157;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("1177") && level >= 38) // Square shield
		{
			xp = 75;
			item = 1177;
			removeamount = 2;
			maketimes = amounttomake;
		} else if (type.equals("1339") && level >= 39) // Warhammer
		{
			xp = 113;
			item = 1339;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1365") && level >= 40) // Battle axe
		{
			xp = 113;
			item = 1365;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1105") && level >= 41) // Chain
		{
			xp = 113;
			item = 1105;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1193") && level >= 42) // Kite
		{
			xp = 113;
			item = 1193;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1311") && level >= 44) // 2h Sword
		{
			xp = 113;
			item = 1311;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1069") && level >= 46) // Platelegs
		{
			xp = 113;
			item = 1069;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1083") && level >= 46) // PlateSkirt
		{
			xp = 113;
			item = 1083;
			removeamount = 3;
			maketimes = amounttomake;
		} else if (type.equals("1119") && level >= 48) // Platebody
		{
			xp = 188;
			item = 1119;
			removeamount = 5;
			maketimes = amounttomake;
		} else {
			c.sendMessage("You don't have a high enough level to make this Item!");
			return;
		}
		this.doaction(c, item, remove, removeamount, maketimes, xp);
	}

	public boolean doaction(Player c, int toadd, int toremove, int toremove2, int timestomake, int xp) {

		if (c.openInterfaceId != 994) {
			return false;
		}

		int maketimes = timestomake;
		c.getPA().closeAllWindows();
		if (c.getItems().playerHasItem(toremove, toremove2)) {
			c.startAnimation(898);
			if (maketimes > 1 && c.getItems().playerHasItem(toremove, toremove2 * 2)) {
				c.sendMessage("You make some " + c.getItems().getItemName(toadd) + "s");
			} else {
				c.sendMessage("You make a " + c.getItems().getItemName(toadd));
			}
			while (maketimes > 0) {
				if (c.getItems().playerHasItem(toremove, toremove2)) {
					c.getItems().deleteItem(toremove, toremove2);
					if (c.getItems().getItemName(toadd).contains("dart tip")) {
						c.getItems().addItem(toadd, 10);
					} else if (c.getItems().getItemName(toadd).contains("bolt")) {
						c.getItems().addItem(toadd, 10);
					} else if (c.getItems().getItemName(toadd).contains("nail")) {
						c.getItems().addItem(toadd, 15);
					} else if (c.getItems().getItemName(toadd).contains("arrow")) {
						c.getItems().addItem(toadd, 15);
					} else if (c.getItems().getItemName(toadd).contains("knife")) {
						c.getItems().addItem(toadd, 5);
					} else if (c.getItems().getItemName(toadd).contains("Cannon")) {
						c.getItems().addItem(toadd, 4);
					} else {
						c.getItems().addItem(toadd, 1);
					}

					if (toadd == 1123) {
						Achievements.progressMade(c, Achievements.Types.SMITH_500_ADDY_PLATES);
					}
					if (toadd == 1127) {
						Achievements.progressMade(c, Achievements.Types.SMITH_300_RUNE_PLATES);
					}

					c.getPA().addSkillXP(xp, 13);
					maketimes--;
				} else {
					break;
				}
			}
		} else {
			c.sendMessage("You don't have enough bars to make this item!");
			return false;
		}
		return true;
	}

	public void readInput(int level, String type, Player c, int amounttomake) {

		if (!c.getItems().playerHasItem(2347)) {
			c.sendMessage("You require a hammer to use this anvil.");
			return;
		}

		if (c.getItems().getItemName(Integer.parseInt(type)).contains("Bronze")) {
			this.CheckBronze(c, level, amounttomake, type);
		} else if (c.getItems().getItemName(Integer.parseInt(type)).contains("Iron")) {
			this.CheckIron(c, level, amounttomake, type);
		} else if (c.getItems().getItemName(Integer.parseInt(type)).contains("Steel")
				|| c.getItems().getItemName(Integer.parseInt(type)).contains("Cannonball")) {
			this.CheckSteel(c, level, amounttomake, type);
		} else if (c.getItems().getItemName(Integer.parseInt(type)).contains("Mith")) {
			this.CheckMith(c, level, amounttomake, type);
		} else if (c.getItems().getItemName(Integer.parseInt(type)).contains("Adam")
				|| c.getItems().getItemName(Integer.parseInt(type)).contains("Addy")) {
			this.CheckAddy(c, level, amounttomake, type);
		} else if (c.getItems().getItemName(Integer.parseInt(type)).contains("Rune")
				|| c.getItems().getItemName(Integer.parseInt(type)).contains("Runite")) {
			this.CheckRune(c, level, amounttomake, type);
		}
	}

	public void sendSmelting() {
		for (int j = 0; j < SMELT_FRAME.length; j++) {
			c.getPA().sendFrame246(SMELT_FRAME[j], 150, SMELT_BARS[j]);
		}
		c.getPA().sendFrame164(2400);
	}

}