package com.zionscape.server.model.content;

import com.google.common.collect.ImmutableMap;
import com.zionscape.server.model.content.minigames.quests.QuestHandler;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.util.CollectionUtil;
import com.zionscape.server.util.Misc;

import java.util.Map;

/**
 * @author Stuart
 */
public final class LoyaltyTitles {

	private static final int[] SKILLING_IDS = new int[]{
			PlayerConstants.RUNECRAFTING,
			PlayerConstants.AGILITY,
			PlayerConstants.HERBLORE,
			PlayerConstants.THIEVING,
			PlayerConstants.CRAFTING,
			PlayerConstants.FLETCHING,
			PlayerConstants.HUNTER,
			PlayerConstants.MINING,
			PlayerConstants.SMITHING,
			PlayerConstants.FISHING,
			PlayerConstants.COOKING,
			PlayerConstants.FIREMAKING,
			PlayerConstants.WOODCUTTING,
			PlayerConstants.FARMING,
			PlayerConstants.SUMMONING
	};

	private static final int[] BOSS_IDS = new int[]{
			1160,
			50,
			6260,
			6247,
			6203,
			6222,
			8349,
			1472,
			7133,
			5666,
			3847,
			8133,
			2881,
			2882,
			2883,
			8596,
			13447,
			8528,
			14301,
			3334,
			3200,
			2042,
			2043,
			2044,
			5862,
			493,
			14383,
			14384,
			14385,
			14386
	};

	private static final Map<String, Integer> titleSelectedIds = new ImmutableMap.Builder<String, Integer>().
			put("Attacker", 19895).
			put("The Strength", 19898).
			put("Defender", 19901).
			put("Ranger", 19904).
			put("Faithful", 19907).
			put("Magician", 19910).
			put("Runecrafter", 19913).
			put("Healer", 19916).
			put("Agile", 19919).
			put("Forger", 19922).
			put("Thief", 19925).
			put("Crafter", 19928).
			put("Fletcher", 19931).
			put("Miner", 19934).
			put("Blacksmith", 19937).
			put("Fisher", 19940).
			put("Chef", 19943).
			put("Arson", 19946).
			put("Lumberjack", 19949).
			put("Farmer", 19952).
			put("Summoner", 19955).
			put("Raider", 19958).
			put("Hunter", 19961).
			put("Slayer", 19964).
			put("Maxed", 19967).
			put("Overlord", 19970).
			put("Fighter", 20026).
			put("Militia", 20029).
			put("Soldier", 20032).
			put("Brute", 20035).
			put("Mercenary", 20038).
			put("Assassin", 20041).
			put("Warrior", 20044).
			put("Gladiator", 20047).
			put("Warlord", 20050).
			put("The Adventurer", 20072).
			put("Gamer", 20075).
			put("The Wealthy", 20078).
			put("Destroyer", 20081).
			put("Sir", 20084).
			put("Lord", 20087).
			put("Weakling", 20090).
			put("Loyal", 20093).
			put("Voter", 20096).
			put("Donator", 20099).
			put("Merchant", 20102).
			put("Athlete", 20105).
			put("Wingman", 20108).
			put("The Drunk", 20111).
			put("Collector", 20114).
			put("Bosser", 20117).
			put("Skiller", 20120).
			put("Elite", 20123).
			put("Iron", 20126).
			put("Respected Donor", 20129).
			put("Extreme Donor", 20132).
			put("Locksmith",  20135).build();

	// needs converting dunno why i did this
	private static final Object[][] BUTTON_TO_SKILL = {
			{77182, PlayerConstants.ATTACK, "Attacker"},
			{77185, PlayerConstants.STRENGTH, "The Strength"},
			{77188, PlayerConstants.DEFENCE, "Defender"},
			{77191, PlayerConstants.RANGED, "Ranger"},
			{77194, PlayerConstants.PRAYER, "Faithful"},
			{77197, PlayerConstants.MAGIC, "Magician"},
			{77200, PlayerConstants.RUNECRAFTING, "Runecrafter"},
			{77203, PlayerConstants.HITPOINTS, "Healer"},
			{77206, PlayerConstants.AGILITY, "Agile"},
			{77209, PlayerConstants.HERBLORE, "Forger"},
			{77212, PlayerConstants.THIEVING, "Thief"},
			{77215, PlayerConstants.CRAFTING, "Crafter"},
			{77218, PlayerConstants.FLETCHING, "Fletcher"},
			{77221, PlayerConstants.MINING, "Miner"},
			{77224, PlayerConstants.SMITHING, "Blacksmith"},
			{77227, PlayerConstants.FISHING, "Fisher"},
			{77230, PlayerConstants.COOKING, "Chef"},
			{77233, PlayerConstants.FIREMAKING, "Arson"},
			{77236, PlayerConstants.WOODCUTTING, "Lumberjack"},
			{77239, PlayerConstants.FARMING, "Farmer"},
			{77242, PlayerConstants.SUMMONING, "Summoner"},
			{77245, PlayerConstants.DUNGEONEERING, "Raider"},
			{77248, PlayerConstants.HUNTER, "Hunter"},
			{77251, PlayerConstants.SLAYER, "Slayer"},
	};

	// needs converting dunno why i did this
	private static final Object[][] BUTTON_TO_KILLS = {
			{78057, 25, "Fighter"},
			{78060, 50, "Militia"},
			{78063, 100, "Soldier"},
			{78066, 250, "Brute"},
			{78069, 500, "Mercenary"},
			{78072, 1000, "Assassin"},
			{78075, 1500, "Warrior"},
			{78078, 2500, "Gladiator"},
			{78081, 5000, "Warlord"}
	};

	public static void onNpcKilled(Player player, NPC npc) {
		if (CollectionUtil.getIndexOfValue(BOSS_IDS, npc.type) > -1) {
			player.getData().bossKills++;
		}
	}

	public static boolean onClickingButtons(Player player, int button) {
		for (int i = 0; i < BUTTON_TO_SKILL.length; i++) {
			if ((int) BUTTON_TO_SKILL[i][0] == button) {
				if (player.xp[(int) BUTTON_TO_SKILL[i][1]] < 200_000_000) {
					player.sendMessage("You do not meet the requirements for this title.");
					player.sendMessage("You have " + Math.round(player.xp[(int) BUTTON_TO_SKILL[i][1]]) + "m XP of 200m XP required.");
				} else {
					setTitle(player, (String) BUTTON_TO_SKILL[i][2], 0xff7000);
				}
				player.setEventHandled(true);
				return true;
			}
		}
		for (int i = 0; i < BUTTON_TO_KILLS.length; i++) {
			if ((int) BUTTON_TO_KILLS[i][0] == button) {
				if (player.kills < (int) BUTTON_TO_KILLS[i][1]) {
					player.sendMessage("You do not meet the requirements for this title.");
					player.sendMessage("You have " + player.kills + " of the " + (int) BUTTON_TO_KILLS[i][1] + " kills required.");
				} else {
					setTitle(player, (String) BUTTON_TO_KILLS[i][2], 0xff7000);
				}
				player.setEventHandled(true);
				return true;
			}
		}

		// this should of been abstracted
		player.setEventHandled(true);

		switch (button) {

			case 78166:
				if (player.getData().wildernessChestsOpened < 250) {
					player.sendMessage("You have opened only " + player.getData().wildernessChestsOpened + " chests of 250.");
					return true;
				}
				setTitle(player, "Locksmith", 0xff7000);
				return true;

			case 78163:
				if(!player.isExtremeDonator()) {
					player.sendMessage("This title requires that you have donated 50$+.");
					return true;
				}
				setTitle(player, "Extreme Donor", 0xff7000);
				return true;

			case 78160:
				if(!player.isLegendaryDonator()) {
					player.sendMessage("This title requires that you have donated 750$+.");
					return true;
				}
				setTitle(player, "Legendary Donor", 0xff7000);
				return true;

			case 78157:
				if(!player.ironman) {
					player.sendMessage("You must be an ironman, to use this title.");
					return true;
				}
				setTitle(player, "Iron", 0xff7000);
				return true;

			case 78154:
				if(!player.elite) {
					player.sendMessage("You must be an elite, to use this title.");
					return true;
				}
				setTitle(player, "Elite", 0xff7000);
				return true;

			case 78151:
				long totalXp = 0;
				for (int i = 0; i < SKILLING_IDS.length; i++) {
					totalXp += (long) player.xp[SKILLING_IDS[i]];
				}
				if (totalXp < 1_000_000_000) {
					player.sendMessage("You do not have a total of 1b or more xp in skilling skills.");
					return true;
				}
				setTitle(player, "Skiller", 0xff7000);
				return true;

			case 78148:
				if (player.getData().bossKills < 5000) {
					player.sendMessage("You have only killed " + player.getData().bossKills + " bosses of 5000.");
					return true;
				}
				setTitle(player, "Bosser", 0xff7000);
				return true;

			case 78145:
				if (player.getData().impsCaught < 1000) {
					player.sendMessage("You have only caught " + player.getData().impsCaught + ", you need to catch 1000.");
					return true;
				}
				setTitle(player, "Collector", 0xff7000);
				return true;

			case 78121:
				if (player.getData().timedKilledByNpc < 10) {
					player.sendMessage("You have only been killed " + player.getData().timedKilledByNpc + " times by an NPC.");
					return true;
				}
				setTitle(player, "Weakling", 0xff7000);
				return true;
			case 77254:
				for (int i = 0; i < player.level.length; i++) {

					if (i == PlayerConstants.DUNGEONEERING || i == PlayerConstants.CONSTRUCTION) {
						continue;
					}

					if (player.getPA().getActualLevel(i) < 99) {
						player.sendMessage("You do not meet the requirements for this title.");
						return true;
					}
				}
				setTitle(player, "Maxed", 0xff7000);
				return true;
			case 78001:
				for (int i = 0; i < player.level.length; i++) {

					if (i == PlayerConstants.DUNGEONEERING || i == PlayerConstants.CONSTRUCTION) {
						continue;
					}

					if (player.xp[i] < 200_000_000) {
						player.sendMessage("You do not meet the requirements for this title.");
						return true;
					}
				}
				setTitle(player, "Overlord", 0xff7000);
				return true;

			case 78103:
				if (player.questPoints < QuestHandler.MAX_QUEST_POINTS) {
					player.sendMessage("You have not completed all the quests.");
					return true;
				}
				setTitle(player, "The Adventurer", 0xff7000);
				return true;
			case 78106:

				if (player.getData().minigamesPlayed.size() < 3) {
					player.sendMessage("You have not played all the different minigames.");
					return true;
				}

				setTitle(player, "Gamer", 0xff7000);
				return true;
			case 78109:
				if (player.getData().totalCoinsThrownDownWell < 5_000_000_000L) {
					player.sendMessage("You have only thrown " + Misc.coinsToString(player.getData().totalCoinsThrownDownWell) + " down the well.");
					return true;
				}
				setTitle(player, "The Wealthy", 0xff7000);
				return true;
			case 78112:
				if (player.getData().monstersKilled < 2500) {
					player.sendMessage("You have only killed " + player.getData().monstersKilled + " of 2500 monsters.");
					return true;
				}
				setTitle(player, "Destroyer", 0xff7000);
				return true;
			case 78115:
				int totalLevel = player.getPA().getTotalLevel();
				if (totalLevel < 1000) {
					player.sendMessage("You have " + totalLevel + " of 1000 total level required for this title.");
					return true;
				}
				setTitle(player, "Sir", 0xff7000);
				return true;
			case 78118:
				totalLevel = player.getPA().getTotalLevel();
				if (totalLevel < 2000) {
					player.sendMessage("You have " + totalLevel + " of 2000 total level required for this title.");
					return true;
				}
				setTitle(player, "Lord", 0xff7000);
				return true;
			case 78124:
				if (player.getData().minutesOnline < 10080) {
					player.sendMessage("You have not played long enough for this title.");
					return true;
				}
				setTitle(player, "Loyal", 0xff7000);
				return true;
			case 78127:
				if (player.getData().totalTimesVoted < 30) {
					player.sendMessage("You have voted " + player.getData().totalTimesVoted + " of the 30 required times.");
					return true;
				}
				setTitle(player, "Voter", 0xff7000);
				return true;
			case 78130:
				if (!player.isMember()) {
					player.sendMessage("You have not donated to the server.");
					return true;
				}
				setTitle(player, "Donator", 0xff7000);
				return true;
			case 78133:
				if (player.getData().totalCompleteTrades < 1000) {
					player.sendMessage("You have completed " + player.getData().totalCompleteTrades + " trades of the required 1000.");
					return true;
				}
				setTitle(player, "Merchant", 0xff7000);
				return true;
					/*case 78129:
						setTitle(player, "Athlete", 0xff7000);
                        return;*/
			case 78139:
				if (player.getData().totalCompletedDuoSlayerTasks < 100) {
					player.sendMessage("You have complete " + player.getData().totalCompletedDuoSlayerTasks + " duo slayer tasks of the required 100.");
					return true;
				}
				setTitle(player, "Wingman", 0xff7000);
				return true;
			case 78142:
				if (player.getData().totalBeersDrank < 10) {
					player.sendMessage("You have drank a total of " + player.getData().totalBeersDrank + " of the required 10.");
					return true;
				}
				setTitle(player, "The Drunk", 0xff7000);
				return true;
		}
		return false;
	}

	public static boolean onNpcClick(Player player, NPC npc, int option) {
		if (npc.type != 11512) {
			return false;
		}
		player.getPA().showInterface(19882);
		setSelectedTitle(player, player.getData().title);
		return true;
	}

	public static void setTitle(Player player, String title, int color) {
		if (player.getData().title.equalsIgnoreCase(title)) {
			player.getData().title = "None";
			player.getData().titleHexColor = "None";
		} else {
			player.getData().title = title;
			player.getData().titleHexColor = "" + color;
		}
		try {
			setSelectedTitle(player, player.getData().title);
		} catch (NullPointerException e) {
			setTitle(player, "None", -1);
			player.sendMessage("An error occurred setting your title. Your title has been removed.");
			return;
		}
		player.appearanceUpdateRequired = true;
		player.updateRequired = true;
	}

	public static void setSelectedTitle(Player player, String title) throws NullPointerException {
		if (title.equalsIgnoreCase("None")) {
			player.getPA().sendFrame87(5001, 0);
			return;
		}

		if (!titleSelectedIds.containsKey(title)) {
			throw new NullPointerException("title " + title + " config does not exist");
		}

		player.getPA().sendFrame87(5001, titleSelectedIds.get(title) + 1);
	}


}