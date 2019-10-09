package com.zionscape.server.model.content.achievements;

import com.google.gson.annotations.SerializedName;
import com.zionscape.server.model.content.achievements.impl.DefenderAchievement;
import com.zionscape.server.model.content.achievements.impl.GodSpellAchievement;
import com.zionscape.server.model.content.achievements.impl.GwdBossAchievement;
import com.zionscape.server.model.content.achievements.impl.SteppedAchievement;
import com.zionscape.server.model.players.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Achievements {

	private static final List<Achievement> achievementList = new ArrayList<>();

	//TODO needs to be put in a file
	static {
		/**
		 * easy
		 */
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.EASY, Types.KILL_250_COWS, "Kill 250 cows", 53342, 208094, 0, 250));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.EASY, Types.COMPLETE_TUTORIAL, "Complete the Draynor tutorial", 53343, 208095, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.EASY, Types.BURY_100_BONES, "Bury 100 bones", 53349, 208101, 0, 100));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.EASY, Types.SUMMON_FAMILIAR, "Summon a familiar", 53355, 208107, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.EASY, Types.PICKPOCKET_A_MAN, "Pickpocket a man 50 times", 53344, 208096, 0, 50));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.EASY, Types.SWITCH_MAGIC_BOOKS, "Switch magic books", 53345, 208097, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.EASY, Types.JOIN_CLAN_CHAT, "Join a clan chat", 53347, 208099, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.EASY, Types.LIGHT_100_FIRES, "Light 100 fires", 53354, 208106, 0, 100));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.EASY, Types.VOTE_5_TIMES, "Vote 5 times", 53348, 208100, 0, 5));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.EASY, Types.CHANGE_APPEARANCE, "Change character appearance", 53353, 208105, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.EASY, Types.PERFORM_SPECIAL_ATTACK, "Perform a special attack", 53352, 208104, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.EASY, Types.EAT_100_LOBSTERS, "Eat 100 lobsters", 53356, 208108, 0, 10));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.EASY, Types.SMELT_100_BRONZE_BARS, "Smelt 100 bronze bars", 53350, 208102, 0, 100));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.EASY, Types.USE_50_ESS, "Use 50 rune essence", 53359, 208111, 0, 50));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.EASY, Types.WIN_A_DUEL_FIGHT, "Win a duel arena fight", 53360, 208112, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.EASY, Types.MAKE_A_BANK_TAB, "Make a bank tab", 53361, 208113, 0, 1));
		achievementList.add(new GodSpellAchievement(Achievement.Difficulty.EASY, Types.CAST_EACH_GOD_SPELL, "Cast each God spell", 54320, 212048, 0));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.EASY, Types.COMPLETE_GNOME_COURSE, "Complete Gnome course", 54321, 212049, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.EASY, Types.WAVE_10_ZOMBIES, "Get to wave 10 at Zombies", 54372, 212100, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.EASY, Types.CHOP_150_TREES, "Chop 150 trees", 54376, 212104, 0, 150));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.EASY, Types.FISH_200_SHRIMP, "Fish 200 shrimp", 54377, 212105, 0, 200));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.EASY, Types.MINE_100_COPPER_ORE, "Mine 100 copper ore", 54378, 212106, 0, 100));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.EASY, Types.CRAFT_100_UNCUT_SAPPHIRE, "Craft 100 uncut sapphires", 54379, 212107, 0, 100));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.EASY, Types.COMPLETE_10_EASY_SLAYER_TASKS, "Complete 10 easy slayer tasks", 54380, 212108, 0, 10));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.EASY, Types.COMPLETE_2_EASY_CLUE_SCROLLS, "Complete 2 easy clue scrolls", 54381, 212109, 0, 2));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.EASY, Types.CATCH_100_BIRDS, "Catch 100 birds", 18787, 73099, 0, 100));

		/**
		 * medium
		 */
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.VOTE_20_TIMES, "Vote 20 times", 53371, 208123, 0, 20));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.DIE_20_TIMES, "Die 20 times", 53364, 208116, 0, 20));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.WIN_20_DUELS, "Win 20 duel arena fights", 53366, 208118, 0, 20));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.LOOT_STALL_350_TIMES, "Loot a stall 350 times", 53367, 208119, 0, 350));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.FISH_500_LOBSTERS, "Fish 500 lobsters", 53368, 208120, 0, 500));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.BURY_250_BIG_BONES, "Bury 250 big bones", 53369, 208121, 0, 250));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.WIELD_DRAGON_WEAPON, "Wield a dragon weapon", 53370, 208112, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.KILL_150_STEEL_DRAGONS, "Kill 150 steel dragons", 53372, 208124, 0, 150));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.USE_WILDERNESS_OBELISK, "Use a wilderness obelisk", 53374, 208126, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.SMELT_300_STEEL_BARS, "Smelt 300 steel bars", 53376, 208128, 0, 300));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.BURN_400_MAPLE_LOGS, "Burn 400 maple logs", 53377, 208129, 0, 400));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.COMPLETE_KOLODION_MINIGAME, "Complete Kolodion's minigame", 54322, 212050, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.ENTER_A_GWD_CHAMBER, "Enter a GWD chamber", 54323, 212051, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.COMPLETE_100_GNOME_COURSES, "Complete Gnome course 100 times", 54324, 212052, 0, 100));
		achievementList.add(new DefenderAchievement(Achievement.Difficulty.MEDIUM, Types.EARN_EVERY_DEFENDER, "Earn every Defender", 54325, 212053, 0));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.PERFORM_A_SKILLCAPE_EMOTE, "Perform a Skillcape emote", 54326, 212054, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.WAVE_15_ZOMBIES, "Get to wave 15 at Zombies", 54373, 212101, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.CATCH_200_IMPS, "Catch 200 imps", 18788, 73100, 0, 200));


		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.COMPLETE_BARROWS_50_TIMES, "Complete barrows 50 times", 54411, 212139, 0, 50));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.COMPLETE_PEST_CONTROL_100_TIMES, "Complete pest control 100 times", 54382, 212110, 0, 100));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.FISH_500_TUNA, "Fish 500 tuna", 54383, 212111, 0, 500));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.COOK_500_TUNA, "Cook 500 tuna", 54384, 212112, 0, 500));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.CUT_500_MAPLES, "Cut 500 maples", 54385, 212113, 0, 500));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.COMPLETE_25_MEDIUM_TASKS, "Complete 25 medium slayer tasks", 54387, 212115, 0, 25));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.IDENTIFY_250_HERBS, "Identify 250 herbs", 54388, 212116, 0, 250));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.SMITH_500_MITHRIL_BARS, "Smith 500 mithril bars", 54389, 212117, 0, 500));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.MEDIUM, Types.COMPLETE_15_MEDIUM_CLUES, "Complete 15 medium clues", 54390, 212118, 0, 15));

		/**
		 * hard
		 */
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.DEFEAT_JAD_TWICE, "Defeat Jad twice", 53380, 208132, 0, 2));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.WIN_75_CASTLEWARS_GAMES, "Win 15 castlewars", 53381, 208133, 0, 15));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.KILL_250_KBD, "Kill 50 king black dragons", 53382, 208134, 0, 50));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.COOK_1000_SHARKS, "Cook 1000 sharks", 53384, 208136, 0, 1000));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.LIGHT_1000_MAGIC_LOGS, "Light 1000 magics logs", 53385, 208137, 0, 1000));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.HIT_500_PLUS, "Hit a 500+ with melee", 53387, 208139, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.WIN_500_PEST_GAMES, "Win 75 pest control games", 53389, 208141, 0, 75));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.EARN_100_MAGE_POINTS, "Earn 500 mage points", 53391, 208143, 0, 500));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.WIN_50_CLAN_BATTLES, "Win 10 clan battles", 53392, 208144, 0, 10));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.COMPLETE_TT_PUZZLE, "Complete a TT puzzle", 53393, 208145, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.USE_1000_RUNE_ESS, "Use 1000 rune essence", 53394, 208146, 0, 1000));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.WIELD_A_WHIP, "Wield a whip", 53395, 208147, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.SMITH_A_DFS, "Smith a DFS", 53396, 208148, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.FARM_500_SEEDS, "Farm 250 seeds", 53397, 208149, 0, 250));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.BURY_500_DRAGON_BONES, "Bury 500 dragon bones", 53398, 208150, 0, 500));
		achievementList.add(new GwdBossAchievement(Achievement.Difficulty.HARD, Types.KILL_EACH_GWD_BOSS, "Kill each GWD boss", 54327, 212055, 0));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.EARN_1B, "Earn 1B cash", 54328, 212056, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.CREATE_A_VINE_WHIP, "Create a Vine Whip", 54329, 212057, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.FLETCH_500_ARROW, "Fletch 500 arrows", 54330, 212057, 0, 500));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.COMPLETE_WILDY_COURSE_10_TIMES, "Complete Wildy course 50 times", 54331, 212059, 0, 50));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.BURY_500_FROST_BONES, "Bury 500 frost dragon bones", 54354, 212082, 0, 500));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.WAVE_25_ZOMBIES, "Get to wave 25 at Zombies", 54374, 212102, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.CATCH_500_GREENWALLS, "Catch 250 grenwalls", 18789, 73101, 0, 250));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.COMPLETE_75_DUO_TASKS, "Complete 20 duo slayer tasks", 54412, 212140, 0, 20));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.CRAFT_500_DIAMONDS, "Cut 500 diamonds", 54391, 212119, 0, 500)); //waiting
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.CUT_1000_YEW_LOGS, "Chop 1000 yew logs", 54392, 212120, 0, 1000));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.BURN_1000_YEW_TREES, "Burn 1000 yew logs", 54393, 212121, 0, 1000));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.SMITH_1000_ADDY_BARS, "Smelt 1000 adamant bars", 54394, 212122, 0, 1000));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.CRAFT_500_BLACK_BODIES, "Craft 500 black dhide bodies", 54395, 212123, 0, 500));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.SMITH_500_ADDY_PLATES, "Smith 100 adamant platebodies", 54396, 212124, 0, 100));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.COMPLETE_50_HARD_SLAYER_TASKS, "Complete 25 hard slayer tasks", 54397, 212125, 0, 25));
		// achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.WAVE_25_ZOMBIES, "Wear full void", 54398, 212126, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.PICKPOCKET_250_PALADINS, "Pickpocket 250 palidins", 54399, 212127, 0, 250));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.HARD, Types.EARN_1000_WARRIOR_TOKENS, "Earn 1000 warrior tokens", 54400, 212128, 0, 1000));


		/**
		 * elite
		 */
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.EARN_MAX_CASH, "Earn max cash", 54332, 212060, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.VOTE_100_TIMES, "Vote 50 times", 54333, 212061, 0, 50));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.COMPLETE_200_HARD_CLUES, "Complete 75 hard clues", 54334, 212062, 0, 75));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.KILL_NEX_100_TIMES, "Kill Nex 50 times", 54336, 212064, 0, 50));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.WAVE_50_IN_ZOMBIES, "Get to wave 50 in Zombies", 54337, 212065, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.COMPLETE_KILN_20_TIMES, "Complete Fight Kiln 20 times", 54338, 212066, 0, 20));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.KILL_JAD_50_TIMES, "Kill Jad 50 times", 54339, 212067, 0, 50));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.KILL_1500_FROST_DRAGONS, "Kill 1000 Frost dragons", 54340, 212068, 0, 1000));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.WIN_50_CASTLEWARS_GAMES, "Win 25 Castlewar games", 54342, 212070, 0, 25));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.WIN_750_PEST_CONTROL_GAMES, "Win 175 pest control games", 54343, 212071, 0, 175));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.KILL_400_TORMENTED_DEMONS, "Kill 400 Tormented demons", 54344, 212072, 0, 400));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.WIN_OVER_200_DUELS, "Win over 50 duels", 54346, 212074, 0, 50));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.KILL_500_JADINKOS, "Kill 500 Jadinkos", 54347, 212075, 0, 500));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.KILL_500_REVENANTS, "Kill 500 Revenants", 54348, 212076, 0, 500));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.FISH_2000_MANTA_RAY, "Fish 2000 Manta ray", 54349, 212077, 0, 2000));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.USE_2500_ESS, "Use 2500 Rune essence", 54350, 212078, 0, 2500));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.COMPLETE_150_DUO_SLAYER_TASKS, "Complete 35 Duo Slayer tasks", 54351, 212079, 0, 35));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.EARN_200M_XP_IN_A_SKILL, "Earn 200m XP in a skill", 54353, 212081, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.KILL_500_GLACORS, "Kill 500 Glacors", 54345, 212073, 0, 500));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.WAVE_25_ZOMBIES_SOLO, "Get to wave 25 at Zombies solo", 54375, 212103, 0, 1));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.FIRE_5000_CANNON_SHOTS, "Fire 1000 cannon shots", 54341, 212069, 0, 1000));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.COMPLETE_350_BARROW_RUNS, "Complete 135 barrow runs", 54413, 212141, 0, 135));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.MINE_500_RUNE_ORE, "Mine 500 rune ore", 54402, 212130, 0, 500));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.SMELT_500_RUNE_BARS, "Smelt 400 rune bars", 54403, 212131, 0, 400));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.SMITH_300_RUNE_PLATES, "Smith 300 rune platebodies", 54404, 212132, 0, 300));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.BURY_1000_FROST_BONES, "Bury 1000 frost bones", 54406, 212134, 0, 1000));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.MINE_2500_RUNE_ESS, "Mine 1500 rune essence", 54407, 212135, 0, 2500));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.CHOP_2000_MAGIC_LOGS, "Chop 2000 magic logs", 54408, 212136, 0, 2000));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.PICKPOCKET_500_HEROES, "Pickpocket 250 heroes", 54409, 212137, 0, 250));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.FLETCH_750_MAGIC_BOWS, "Fletch 750 magic bows", 54410, 212138, 0, 750));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.CATCH_500_KINGLY_IMPS, "Catch 500 Kingly imps", 18790, 73102, 0, 500));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.KILL_500_CORPS, "Kill 250 Corporal beasts", 18791, 73103, 0, 250));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.BURN_2000_MAGIC_LOGS, "Burn 2000 magic logs", 18792, 73104, 0, 2000));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.KILL_400_ZULRAHS, "Kill 400 Zulrahs", 15264, 59160, 0, 400));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.KILL_400_CERBERUS, "Kill 225 Cerberuss", 15268, 59164, 0, 225));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.KILL_500_KRAKENS, "Kill 300 Krakens", 15269, 59165, 0, 300));

		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.KILL_300_THERMO, "Kill 300 Thermo smoke devils", 18833, 73145, 0, 300));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.KILL_250_CALLISTO, "Kill 250 Callistos", 18834, 73146, 0, 250));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.KILL_250_VETIONS, "Kill 250 Vet'ions", 18835, 73147, 0, 250));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.KILL_300_VENENATIS, "Kill 300 Venenatises", 18836, 73148, 0, 300));
		achievementList.add(new SteppedAchievement(Achievement.Difficulty.ELITE, Types.EARN_100_WILD_KEYS, "Earn 30 Wilderness Keys", 254, 254, 0, 30));
	}


	public static void setIronmanCompletedAchievements(Player player) {
		// pest
		player.getData().getAchievementProgress().put(Types.COMPLETE_PEST_CONTROL_100_TIMES, new AchievementProgress(100));
		player.getData().getAchievementProgress().put(Types.WIN_500_PEST_GAMES, new AchievementProgress(500));
		player.getData().getAchievementProgress().put(Types.WIN_750_PEST_CONTROL_GAMES, new AchievementProgress(750));

		// duel
		player.getData().getAchievementProgress().put(Types.WIN_A_DUEL_FIGHT, new AchievementProgress(1));
		player.getData().getAchievementProgress().put(Types.WIN_20_DUELS, new AchievementProgress(20));
		player.getData().getAchievementProgress().put(Types.WIN_OVER_200_DUELS, new AchievementProgress(200));

		// death pk
		player.getData().getAchievementProgress().put(Types.DIE_20_TIMES, new AchievementProgress(20));

		// vote
		player.getData().getAchievementProgress().put(Types.VOTE_5_TIMES, new AchievementProgress(5));
		player.getData().getAchievementProgress().put(Types.VOTE_20_TIMES, new AchievementProgress(20));
		player.getData().getAchievementProgress().put(Types.VOTE_100_TIMES, new AchievementProgress(100));

		player.getData().getAchievementProgress().put(Types.EARN_100_WILD_KEYS, new AchievementProgress(30));
	}

	public static void onPlayerLoggedIn(Player player) {

		if (player.ironman) {
			setIronmanCompletedAchievements(player);
		}

		achievementList.forEach(x -> {
			String color = "@red@";

			if (player.getData().getAchievementProgress().containsKey(x.getType())) {
				if (x.isComplete(player.getData().getAchievementProgress().get(x.getType()))) {
					color = "@gre@";
				} else {
					color = "@yel@";
				}
			}

			player.getPA().sendFrame126(color + x.getName(), x.getInterfaceId());
		});
		player.getPA().sendFrame126("Achievement Points: " + player.getData().getAchievementPoints(), 53339);
		player.getPA().sendFrame126("Achievements - " + getCompletedAchievementAmount(player) + " / " + achievementList.size(), 53336);
	}

	public static boolean onClickingButtons(Player player, int button) {
		Achievement achievement = achievementList.stream().filter(x -> x.getActionButtonId() == button).findFirst().orElse(null);
		if (achievement != null) {
			achievement.display(player);
			return true;
		}
		return false;
	}

	public static void progressMade(Player player, Types type) {
		Achievements.progressMade(player, type, null);
	}

	public static void progressMade(Player player, Types type, Object object) {
		Achievement achievement = achievementList.stream().filter(x -> x.getType().equals(type)).findFirst().orElse(null);
		Map<Types, AchievementProgress> progress = player.getData().getAchievementProgress();
		if (achievement == null) {
			return;
		}
		boolean started = false;
		if (!progress.containsKey(type)) {
			progress.put(type, achievement.getDefaultProgressValue());
			started = true;
		}

		if (achievement.isComplete(progress.get(type))) {
			return;
		}

		progress.put(type, achievement.getNewProgressObject(progress.get(type), object));

		if (achievement.isComplete(progress.get(type))) {
			player.getPA().sendFrame126("@gre@" + achievement.getName(), achievement.getInterfaceId());
			player.sendMessage("@red@Congratulations on completing " + achievement.getName().toLowerCase() + " achievement.");

			int points = 0;

			if (type.equals(Types.EARN_MAX_CASH)) {
				points = 4;
				player.sendMessage("@red@You have been awarded 4 achievement points!");
			} else {
				switch (achievement.getDifficulty()) {
					case EASY:
						points = 1;
						//  player.getBank().depositItem(995, 1_000_000, -1, true, false);
						//  player.sendMessage("You have been awarded 1 achievement point, and 1m has been put into your bank!");
						break;
					case MEDIUM:
						points = 2;
						player.getItems().addItem(995, 100_000);
						player.sendMessage("@red@You have been awarded 2 achievement points, and 100k has been put into your money pouch!");
						break;
					case HARD:
						points = 3;
						player.getItems().addItem(995, 2_000_000);
						player.sendMessage("@red@You have ben awarded 3 achievement points, and 2m has been put into your money pouch!");
						break;
					case ELITE:
						points = 4;
						player.getItems().addItem(995, 10_000_000);
						player.sendMessage("@red@You have been awarded 4 achievement point, and 10m has been put into your money pouch!");
						break;
				}
			}

			player.getData().incrementAchievementPoints(points);
			player.getPA().sendFrame126("Achievement Points: " + player.getData().getAchievementPoints(), 53339);
			player.getPA().sendFrame126("Achievements - " + getCompletedAchievementAmount(player) + " / " + achievementList.size(), 53336);

		} else if (started) {
			player.sendMessage("@red@You have started the achievement " + achievement.getName().toLowerCase() + ".");
			player.getPA().sendFrame126("@yel@" + achievement.getName(), achievement.getInterfaceId());
		}
	}

	public static boolean completedAllAchievements(Player player) {
		return getCompletedAchievementAmount(player) >= achievementList.size();
	}

	public static int getCompletedAchievementAmount(Player player) {

		int count = 0;

		for (Achievement achievement : achievementList) {
			if (player.getData().getAchievementProgress().containsKey(achievement.getType())) {
				if (achievement.isComplete(player.getData().getAchievementProgress().get(achievement.getType()))) {
					count++;
				}
			}
		}

		return count;
	}

	public enum Types {
		@SerializedName("KILL_250_COWS")
		KILL_250_COWS,
		@SerializedName("SUMMON_FAMILIAR")
		SUMMON_FAMILIAR,
		@SerializedName("BURY_100_BONES")
		BURY_100_BONES,
		@SerializedName("COMPLETE_TUTORIAL")
		COMPLETE_TUTORIAL,
		@SerializedName("PICKPOCKET_A_MAN")
		PICKPOCKET_A_MAN,
		@SerializedName("SWITCH_MAGIC_BOOKS")
		SWITCH_MAGIC_BOOKS,
		@SerializedName("JOIN_CLAN_CHAT")
		JOIN_CLAN_CHAT,
		@SerializedName("LIGHT_100_FIRES")
		LIGHT_100_FIRES,
		@SerializedName("VOTE_5_TIMES")
		VOTE_5_TIMES,
		@SerializedName("VOTE_20_TIMES")
		VOTE_20_TIMES,
		@SerializedName("CHANGE_APPEARANCE")
		CHANGE_APPEARANCE,
		@SerializedName("PERFORM_SPECIAL_ATTACK")
		PERFORM_SPECIAL_ATTACK,
		@SerializedName("EARN_PK_POINT")
		EARN_PK_POINT,
		@SerializedName("EARN_20_PK_POINTS")
		EARN_20_PK_POINTS,
		@SerializedName("EAT_100_LOBSTERS")
		EAT_100_LOBSTERS,
		@SerializedName("SMELT_100_BRONZE_BARS")
		SMELT_100_BRONZE_BARS,
		@SerializedName("RUNECRAFT_50_ESS")
		USE_50_ESS,
		@SerializedName("WIN_A_DUEL_FIGHT")
		WIN_A_DUEL_FIGHT,
		@SerializedName("MAKE_A_BANK_TAB")
		MAKE_A_BANK_TAB,
		@SerializedName("DIE_20_TIMES")
		DIE_20_TIMES,
		@SerializedName("WIN_20_DUELS")
		WIN_20_DUELS,
		@SerializedName("LOOT_STALL_350_TIMES")
		LOOT_STALL_350_TIMES,
		@SerializedName("FISH_500_LOBSTERS")
		FISH_500_LOBSTERS,
		@SerializedName("BURY_250_BIG_BONES")
		BURY_250_BIG_BONES,
		@SerializedName("WIELD_DRAGON_WEAPON")
		WIELD_DRAGON_WEAPON,
		@SerializedName("KILL_150_STEEL_DRAGONS")
		KILL_150_STEEL_DRAGONS,
		@SerializedName("USE_WILDERNESS_OBELISK")
		USE_WILDERNESS_OBELISK,
		@SerializedName("SMELT_300_STEEL_BARS")
		SMELT_300_STEEL_BARS,
		@SerializedName("BURN_400_MAPLE_LOGS")
		BURN_400_MAPLE_LOGS,
		@SerializedName("DEFEAT_JAD_TWICE")
		DEFEAT_JAD_TWICE,
		@SerializedName("WIN_75_CASTLEWARS_GAMES")
		WIN_75_CASTLEWARS_GAMES,
		@SerializedName("KILL_250_KBD")
		KILL_250_KBD,
		@SerializedName("COOK_1000_SHARKS")
		COOK_1000_SHARKS,
		@SerializedName("LIGHT_1000_MAGIC_LOGS")
		LIGHT_1000_MAGIC_LOGS,
		@SerializedName("HIT_500_PLUS")
		HIT_500_PLUS,
		@SerializedName("DEFEAT_ALL_REVS")
		DEFEAT_ALL_REVS,
		@SerializedName("WIN_500_PEST_GAMES")
		WIN_500_PEST_GAMES,
		@SerializedName("WIN_25_ZOMBIE_GAMES")
		WIN_25_ZOMBIE_GAMES,
		@SerializedName("EARN_100_MAGE_POINTS")
		EARN_100_MAGE_POINTS,
		@SerializedName("WIN_50_CLAN_BATTLES")
		WIN_50_CLAN_BATTLES,
		@SerializedName("COMPLETE_TT_PUZZLE")
		COMPLETE_TT_PUZZLE,
		@SerializedName("CRAFT_1000_RUNE_ESS")
		USE_1000_RUNE_ESS,
		@SerializedName("WIELD_A_WHIP")
		WIELD_A_WHIP,
		@SerializedName("SMITH_A_DFS")
		SMITH_A_DFS,
		@SerializedName("FARM_500_SEEDS")
		FARM_500_SEEDS,
		@SerializedName("BURY_500_DRAGON_BONES")
		BURY_500_DRAGON_BONES,
		@SerializedName("CAST_EACH_GOD_SPELL")
		CAST_EACH_GOD_SPELL,
		@SerializedName("COMPLETE_100_GNOME_COURSES")
		COMPLETE_100_GNOME_COURSES,
		@SerializedName("COMPLETE_KOLODION_MINIGAME")
		COMPLETE_KOLODION_MINIGAME,
		@SerializedName("ENTER_A_GWD_CHAMBER")
		ENTER_A_GWD_CHAMBER,
		@SerializedName("COMPLETE_GNOME_COURSE_50_TIMES")
		COMPLETE_GNOME_COURSE,
		@SerializedName("EARN_EVERY_DEFENDER")
		EARN_EVERY_DEFENDER,
		@SerializedName("PERFORM_A_SKILLCAPE_EMOTE")
		PERFORM_A_SKILLCAPE_EMOTE,
		@SerializedName("KILL_EACH_GWD_BOSS")
		KILL_EACH_GWD_BOSS,
		@SerializedName("EARN_1b")
		EARN_1B,
		@SerializedName("CREATE_A_VINE_WHIP")
		CREATE_A_VINE_WHIP,
		@SerializedName("FLETCH_500_ARROW")
		FLETCH_500_ARROW,
		@SerializedName("COMPLETE_WILDY_COURSE_10_TIMES")
		COMPLETE_WILDY_COURSE_10_TIMES,
		@SerializedName("EARN_MAX_CASH")
		EARN_MAX_CASH,
		@SerializedName("VOTE_100_TIMES")
		VOTE_100_TIMES,
		@SerializedName("COMPLETE_200_HARD_CLUES")
		COMPLETE_200_HARD_CLUES,
		@SerializedName("COMPLETE_EVERY_TT_PUZZLE")
		COMPLETE_EVERY_TT_PUZZLE,
		@SerializedName("KILL_NEX_100_TIMES")
		KILL_NEX_100_TIMES,
		@SerializedName("WAVE_50_IN_ZOMBIES")
		WAVE_50_IN_ZOMBIES,
		@SerializedName("COMPLETE_KILN_20_TIMES")
		COMPLETE_KILN_20_TIMES,
		@SerializedName("KILL_JAD_50_TIMES")
		KILL_JAD_50_TIMES,
		@SerializedName("KILL_1500_FROST_DRAGONS")
		KILL_1500_FROST_DRAGONS,
		@SerializedName("FIRE_5000_CANNON_SHOTS")
		FIRE_5000_CANNON_SHOTS,
		@SerializedName("WIN_50_CASTLEWARS_GAMES")
		WIN_50_CASTLEWARS_GAMES,
		@SerializedName("WIN_1000_PEST_CONTROL_GAMES")
		WIN_750_PEST_CONTROL_GAMES,
		@SerializedName("KILL_400_TORMENTED_DEMONS")
		KILL_400_TORMENTED_DEMONS,
		@SerializedName("KILL_500_GLACORS")
		KILL_500_GLACORS,
		@SerializedName("WIN_OVER_200_DUELS")
		WIN_OVER_200_DUELS,
		@SerializedName("KILL_500_JADINKOS")
		KILL_500_JADINKOS,
		@SerializedName("KILL_500_REVENANTS")
		KILL_500_REVENANTS,
		@SerializedName("FISH_2000_MANTA_RAY")
		FISH_2000_MANTA_RAY,
		@SerializedName("CRAFT_15000_RUNE_ESSENCE")
		USE_2500_ESS,
		@SerializedName("COMPLETE_150_DUO_SLAYER_TASKS")
		COMPLETE_150_DUO_SLAYER_TASKS,
		@SerializedName("CREATE_50_FROZEN_KEYS")
		CREATE_50_FROZEN_KEYS,
		@SerializedName("EARN_200M_XP_IN_A_SKILL")
		EARN_200M_XP_IN_A_SKILL,
		@SerializedName("BURY_500_FROST_BONES")
		BURY_500_FROST_BONES,
		@SerializedName("WEAR_EACH_CASTLEWAR_CAPES")
		WEAR_EACH_CASTLEWAR_CAPES,
		@SerializedName("WAVE_10_ZOMBIES")
		WAVE_10_ZOMBIES,
		@SerializedName("WAVE_15_ZOMBIES")
		WAVE_15_ZOMBIES,
		@SerializedName("WAVE_25_ZOMBIES")
		WAVE_25_ZOMBIES,
		@SerializedName("WAVE_25_ZOMBIES_SOLO")
		WAVE_25_ZOMBIES_SOLO,
		@SerializedName("CHOP_150_TREES")
		CHOP_150_TREES,
		@SerializedName("FISH_200_SHRIMP")
		FISH_200_SHRIMP,
		@SerializedName("MINE_100_COPPER_ORE")
		MINE_100_COPPER_ORE,
		@SerializedName("CRAFT_100_UNCUT_SAPPHIRE")
		CRAFT_100_UNCUT_SAPPHIRE,
		@SerializedName("COMPLETE_10_EASY_SLAYER_TASKS")
		COMPLETE_10_EASY_SLAYER_TASKS,
		@SerializedName("COMPLETE_2_EASY_CLUE_SCROLLS")
		COMPLETE_2_EASY_CLUE_SCROLLS,
		@SerializedName("COMPLETE_BARROWS_50_TIMES")
		COMPLETE_BARROWS_50_TIMES,
		@SerializedName("COMPLETE_PEST_CONTROL_100_TIMES")
		COMPLETE_PEST_CONTROL_100_TIMES,
		@SerializedName("FISH_500_TUNA")
		FISH_500_TUNA,
		@SerializedName("COOK_500_TUNA")
		COOK_500_TUNA,
		@SerializedName("CUT_500_MAPLES")
		CUT_500_MAPLES,
		@SerializedName("LAY_50_CASTLEWARS_GAMES")
		PLAY_50_CASTLEWARS_GAMES,
		@SerializedName("COMPLETE_25_MEDIUM_TASKS")
		COMPLETE_25_MEDIUM_TASKS,
		@SerializedName("IDENTIFY_250_HERBS")
		IDENTIFY_250_HERBS,
		@SerializedName("SMITH_500_MITHRIL_BARS")
		SMITH_500_MITHRIL_BARS,
		@SerializedName("COMPLETE_15_MEDIUM_CLUES")
		COMPLETE_15_MEDIUM_CLUES,
		@SerializedName("COMPLETE_75_DUO_TASKS")
		COMPLETE_75_DUO_TASKS,
		@SerializedName("CRAFT_500_DIAMONDS")
		CRAFT_500_DIAMONDS,
		@SerializedName("CUT_1000_YEW_LOGS")
		CUT_1000_YEW_LOGS,
		@SerializedName(" BURN_1000_YEW_TREES")
		BURN_1000_YEW_TREES,
		@SerializedName("SMITH_1000_ADDY_BARS")
		SMITH_1000_ADDY_BARS,
		@SerializedName("CRAFT_500_BLACK_BODIES")
		CRAFT_500_BLACK_BODIES,
		@SerializedName("SMITH_500_ADDY_PLATES")
		SMITH_500_ADDY_PLATES,
		@SerializedName("COMPLETE_50_HARD_SLAYER_TASKS")
		COMPLETE_50_HARD_SLAYER_TASKS,
		@SerializedName("WEAR_FULL_VOID")
		WEAR_FULL_VOID,
		@SerializedName("PICKPOCKET_250_PALADINS")
		PICKPOCKET_250_PALADINS,
		@SerializedName("EARN_1000_WARRIOR_TOKENS")
		EARN_1000_WARRIOR_TOKENS,
		@SerializedName("COMPLETE_350_BARROW_RUNS")
		COMPLETE_350_BARROW_RUNS,
		@SerializedName("MINE_500_RUNE_ORE")
		MINE_500_RUNE_ORE,
		@SerializedName("SMELT_500_RUNE_BARS")
		SMELT_500_RUNE_BARS,
		@SerializedName("SMITH_300_RUNE_PLATES")
		SMITH_300_RUNE_PLATES,
		@SerializedName("BURY_1000_FROST_BONES")
		BURY_1000_FROST_BONES,
		@SerializedName("MINE_2500_RUNE_ESS")
		MINE_2500_RUNE_ESS,
		@SerializedName("CHOP_2000_MAGIC_LOGS")
		CHOP_2000_MAGIC_LOGS,
		@SerializedName("PICKPOCKET_500_HEROES")
		PICKPOCKET_500_HEROES,
		@SerializedName("FLETCH_750_MAGIC_BOWS")
		FLETCH_750_MAGIC_BOWS,
		@SerializedName("CATCH_100_BIRDS")
		CATCH_100_BIRDS,
		@SerializedName("CATCH_200_IMPS")
		CATCH_200_IMPS,
		@SerializedName("CATCH_500_GREENWALLS")
		CATCH_500_GREENWALLS,
		@SerializedName("CATCH_500_KINGLY_IMPS")
		CATCH_500_KINGLY_IMPS,
		@SerializedName("KILL_500_CORPS")
		KILL_500_CORPS,
		@SerializedName("BURN_2000_MAGIC_LOGS")
		BURN_2000_MAGIC_LOGS,
		@SerializedName("KILL_400_ZULRAHS")
		KILL_400_ZULRAHS,
		@SerializedName("KILL_400_CERBERUS")
		KILL_400_CERBERUS,
		@SerializedName("KILL_500_KRAKENS")
		KILL_500_KRAKENS,
		@SerializedName("KILL_300_THERMO")
		KILL_300_THERMO,
		@SerializedName("KILL_250_CALLISTO")
		KILL_250_CALLISTO,
		@SerializedName("KILL_250_VETIONS")
		KILL_250_VETIONS,
		@SerializedName("KILL_300_VENENATIS")
		KILL_300_VENENATIS,
		@SerializedName("EARN_100_WILD_KEYS")
		EARN_100_WILD_KEYS
	}
}