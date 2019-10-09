package com.zionscape.server.model.players;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import com.google.common.base.MoreObjects;
import com.zionscape.server.Config;
import com.zionscape.server.Server;
import com.zionscape.server.ServerEvents;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.Entity;
import com.zionscape.server.model.InstancesArea;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.WalkingQueue;
import com.zionscape.server.model.clan.Clan;
import com.zionscape.server.model.content.Food;
import com.zionscape.server.model.content.GreeGree;
import com.zionscape.server.model.content.Perks;
import com.zionscape.server.model.content.PotionMixing;
import com.zionscape.server.model.content.Potions;
import com.zionscape.server.model.content.cannon.Cannon;
import com.zionscape.server.model.content.minigames.DuelArena;
import com.zionscape.server.model.content.minigames.FightPits;
import com.zionscape.server.model.content.minigames.castlewars.CastleWars;
import com.zionscape.server.model.content.minigames.clanwars.ClanWars;
import com.zionscape.server.model.content.minigames.gambling.Gambling;
import com.zionscape.server.model.content.minigames.zombies.Zombies;
import com.zionscape.server.model.content.tradingpost.TradingPostManager;
import com.zionscape.server.model.content.treasuretrails.Level;
import com.zionscape.server.model.content.treasuretrails.solutions.Puzzle;
import com.zionscape.server.model.items.Bindable;
import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.items.ItemAssistant;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.npcs.other.MissCheevers;
import com.zionscape.server.model.players.chat.Chat;
import com.zionscape.server.model.players.chat.PrivateMessaging;
import com.zionscape.server.model.players.combat.Animations;
import com.zionscape.server.model.players.combat.CombatAssistant;
import com.zionscape.server.model.players.combat.CombatHelper;
import com.zionscape.server.model.players.combat.CombatHelper.CombatStyle;
import com.zionscape.server.model.players.combat.CombatHelper.CombatType;
import com.zionscape.server.model.players.dialogue.Dialogue;
import com.zionscape.server.model.players.skills.Cooking;
import com.zionscape.server.model.players.skills.Crafting;
import com.zionscape.server.model.players.skills.Prayer;
import com.zionscape.server.model.players.skills.Runecrafting;
import com.zionscape.server.model.players.skills.Thieving;
import com.zionscape.server.model.players.skills.agility.Course;
import com.zionscape.server.model.players.skills.firemaking.Firemaking;
import com.zionscape.server.model.players.skills.hunter.Hunter;
import com.zionscape.server.model.players.skills.smithing.Smithing;
import com.zionscape.server.model.players.skills.smithing.SmithingInterface;
import com.zionscape.server.model.players.skills.summoning.PlayerFamiliar;
import com.zionscape.server.net.Packet;
import com.zionscape.server.net.PacketHandler;
import com.zionscape.server.scripting.Scripting;
import com.zionscape.server.scripting.messages.impl.LoginMessage;
import com.zionscape.server.task.impl.PlayerUpdateTask;
import com.zionscape.server.util.DatabaseUtil;
import com.zionscape.server.util.Misc;
import com.zionscape.server.util.Stream;

/**
 * Player Class
 *
 * @author Krista Mainka
 * @version 1.4
 */
public class Player extends Entity {

	private final TradingPostManager tradingPost = new TradingPostManager(this);

	public static final int DEFAULT_WALK_INDEX = 0x333;
	public static final int[][] MAGIC_SPELLS = {
			// example {magicId, level req, animation, startGFX, projectile Id, // endGFX, maxhit, exp gained, rune 1, rune 1 amount, rune 2, rune 2 // amount, rune 3, rune 3 amount, rune 4, rune 4 amount}
			{1152, 1, 14221, 0, 2699, 2700, 2, 5, 556, 1, 558, 1, 0, 0, 0, 0, 4128}, // wind strike
			{1154, 5, 14220, 2701, 2703, 2708, 4, 7, 555, 1, 556, 1, 558, 1, 0, 0, 4130}, // water strike
			{1156, 9, 14222, 2713, 2718, 2723, 6, 9, 557, 2, 556, 1, 558, 1, 0, 0, 4132},// earth strike
			{1158, 13, 14223, 2728, 2729, 2737, 8, 11, 554, 3, 556, 2, 558, 1, 0, 0, 4134}, // fire strike
			{1160, 17, 14221, 0, 2699, 2700, 9, 13, 556, 2, 562, 1, 0, 0, 0, 0, 4136}, // wind bolt
			{1163, 23, 14220, 2702, 2704, 2709, 10, 16, 556, 2, 555, 2, 562, 1, 0, 0, 4139}, // water bolt
			{1166, 29, 14222, 2714, 2719, 2724, 11, 20, 556, 2, 557, 3, 562, 1, 0, 0, 4142}, // earth bolt
			{1169, 35, 14223, 2728, 2731, 2738, 12, 22, 556, 3, 554, 4, 562, 1, 0, 0, 4145}, // fire bolt
			{1172, 41, 14221, 0, 2699, 2700, 13, 25, 556, 3, 560, 1, 0, 0, 0, 0, 4148}, // wind blast
			{1175, 47, 14220, 2702, 2705, 2710, 14, 28, 556, 3, 555, 3, 560, 1, 0, 0, 4151}, // water blast
			{1177, 53, 14222, 2715, 2720, 2725, 15, 31, 556, 3, 557, 4, 560, 1, 0, 0, 4153}, // earth blast
			{1181, 59, 14223, 2728, 2733, 2739, 16, 35, 556, 4, 554, 5, 560, 1, 0, 0, 4157}, // fire blast
			{1183, 62, 14221, 0, 2699, 2700, 14, 36, 556, 5, 565, 1, 0, 0, 0, 4159}, // wind wave
			{1185, 65, 14220, 2702, 2706, 2711, 15, 37, 556, 5, 555, 7, 565, 1, 0, 0, 4161}, // water wave
			{1188, 70, 14222, 2715, 2721, 2726, 16, 40, 556, 5, 557, 7, 565, 1, 0, 0, 4164}, // earth wave
			{1189, 75, 14223, 2728, 2735, 2740, 17, 42, 556, 5, 554, 7, 565, 1, 0, 0, 4165}, // fire wave
			{1153, 3, 716, 102, 103, 104, 0, 13, 555, 3, 557, 2, 559, 1, 0, 0}, // confuse
			{1157, 11, 716, 105, 106, 107, 0, 20, 555, 3, 557, 2, 559, 1, 0, 0}, // weaken
			{1161, 19, 716, 108, 109, 110, 0, 29, 555, 2, 557, 3, 559, 1, 0, 0}, // curse
			{1542, 66, 729, 167, 168, 169, 0, 76, 557, 5, 555, 5, 566, 1, 0, 0}, // vulnerability
			{1543, 73, 729, 170, 171, 172, 0, 83, 557, 8, 555, 8, 566, 1, 0, 0}, // enfeeble
			{1562, 80, 729, 173, 174, 107, 0, 90, 557, 12, 555, 12, 556, 1, 0, 0}, // stun
			{1572, 20, 711, 177, 178, 181, 0, 30, 557, 3, 555, 3, 561, 2, 0, 0}, // bind
			{1582, 50, 711, 177, 178, 180, 2, 60, 557, 4, 555, 4, 561, 3, 0, 0}, // snare
			{1592, 79, 711, 177, 178, 179, 4, 90, 557, 5, 555, 5, 561, 4, 0, 0}, // entangle
			{1171, 39, 724, 145, 146, 147, 15, 25, 556, 2, 557, 2, 562, 1, 0, 0, 4147}, // crumble undead
			{1539, 50, 708, 87, 88, 89, 25, 42, 554, 5, 560, 1, 0, 0, 0, 0, 6003}, // iban blast
			{12037, 50, 1576, 327, 328, 329, 19, 30, 560, 1, 558, 4, 0, 0, 0, 0, 47005}, // magic dart
			{1190, 60, 811, 0, 0, 76, 28, 60, 554, 2, 565, 2, 556, 4, 0, 0, 4166}, // sara strike
			{1191, 60, 811, 0, 0, 77, 28, 60, 554, 1, 565, 2, 556, 4, 0, 0, 4167}, // cause of guthix
			{1192, 60, 811, 0, 0, 78, 28, 60, 554, 4, 565, 2, 556, 1, 0, 0, 4168}, // flames of zammy
			{12445, 85, 1819, 0, 344, 345, 0, 65, 563, 1, 562, 1, 560, 1, 0, 0}, // teleblock
			{12939, 50, 1978, 0, 384, 385, 13, 30, 560, 2, 562, 2, 554, 1, 556, 1, 50139}, // smoke rush
			{12987, 52, 1978, 0, 378, 379, 14, 31, 560, 2, 562, 2, 566, 1, 556, 1, 50187}, // shadow rush
			{12901, 56, 1978, 0, 0, 373, 15, 33, 560, 2, 562, 2, 565, 1, 0, 0, 50101}, // blood rush
			{12861, 58, 1978, 0, 360, 361, 16, 34, 560, 2, 562, 2, 555, 2, 0, 0, 50061}, // ice rush
			{12963, 62, 1979, 0, 0, 389, 18, 36, 560, 2, 562, 4, 556, 2, 554, 2, 50163}, // smoke burst
			{13011, 64, 1979, 0, 0, 382, 19, 37, 560, 2, 562, 4, 556, 2, 566, 2, 50211}, // shadow burst
			{12919, 68, 1979, 0, 0, 376, 20, 39, 560, 2, 562, 4, 565, 2, 0, 0, 50119}, // blood burst
			{12881, 70, 1979, 0, 0, 363, 21, 40, 560, 2, 562, 4, 555, 4, 0, 0, 50081}, // ice burst
			{12951, 74, 1978, 0, 386, 387, 22, 42, 560, 2, 554, 2, 565, 2, 556, 2, 50151}, // smoke blitz
			{12999, 76, 1978, 0, 380, 381, 22, 43, 560, 2, 565, 2, 556, 2, 566, 2, 50199}, // shadow blitz
			{12911, 80, 1978, 0, 374, 375, 24, 45, 560, 2, 565, 4, 0, 0, 0, 0, 50111}, // blood blitz
			{12871, 82, 1978, 366, 0, 367, 25, 46, 560, 2, 565, 2, 555, 3, 0, 0, 50071}, // ice blitz
			{12975, 86, 1979, 0, 0, 391, 25, 48, 560, 4, 565, 2, 556, 4, 554, 4, 50175}, // smokebarrage
			{13023, 88, 1979, 0, 0, 383, 26, 49, 560, 4, 565, 2, 556, 4, 566, 3, 50223}, // shadow barrage
			{12929, 92, 1979, 0, 0, 377, 28, 51, 560, 4, 565, 4, 566, 1, 0, 0, 50129}, // bloodbarrage
			{12891, 94, 1979, 0, 0, 369, 28, 52, 560, 4, 565, 2, 555, 6, 0, 0, 50091}, // ice barrage
			{-1, 80, 811, 301, 0, 0, 0, 0, 554, 3, 565, 3, 556, 3, 0, 0}, // charge
			{-1, 21, 712, 112, 0, 0, 0, 2, 554, 3, 561, 1, 0, 0, 0, 0}, // low alch
			{-1, 55, 713, 113, 0, 0, 0, 5, 554, 5, 561, 1, 0, 0, 0, 0}, // high alch
			{-1, 33, 728, 142, 143, 144, 0, 35, 556, 1, 563, 1, 0, 0, 0, 0}, // telegrab
			{54270, 81, 10546, 457, 462, 2700, 19, 80, 565, 1, 560, 1, 556, 7, 0, 0, 211254}, // air surge
			{54280, 85, 10542, 2701, 2707, 2712, 21, 80, 565, 1, 560, 1, 556, 7, 555, 10, 212008}, // water surge
			{54292, 90, 14209, 2717, 2722, 2727, 23, 80, 565, 1, 560, 1, 556, 7, 557, 10, 212020}, // earth surge
			{54300, 95, 2791, 2728, 2735, 2741, 25, 80, 565, 1, 560, 1, 556, 7, 554, 10, 212028}, // fire surge
			{54264, 77, 10546, 457, 1019, 1019, 50, 70, 21773, 1, 0, 0, 0, 0, 0, 0, 211248}, // armadyl strike
			{ -1, 75, 719, 1251, 1252, 1253, 29, 35, 0, 0, 0, 0, 0, 0, 0, 0 }, // Trident of the seas
			{ -1, 75, 719, 665, 1040, 1042, 32, 35, 0, 0, 0, 0, 0, 0, 0, 0 } // Trident of the swamp
	};

	public long lastDuelRequest;
	public int pouchUpdateAmount;
	public long lastPouchUpdate;
	public static final int playerWeapon = 3;
	public static final int maxPlayerListSize = Config.MAX_PLAYERS;
	public static final int maxNPCListSize = NPCHandler.maxNPCs;
	public static final int[] POUCH_SIZE = {3, 6, 9, 12};
	public static final int[] BOWS = {25106, 25040, 18357, 15241, 9185, 839, 845, 847, 851, 855, 859, 841, 843, 849, 853, 857, 861, 4212,
			4214, 4215, 11235, 4216, 4217, 4218, 4219, 4220, 4221, 4222, 4223, 6724, 4734, 4934, 4935, 4936, 4937,
			15041, 20173};
	public static final int[] ARROWS = {882, 884, 886, 888, 890, 892, 4740, 11212, 9140, 9141, 4142, 9143, 9144, 9240, 9241,
			9242, 9243, 9244, 9245};
	public static final int[] NO_ARROW_DROP = {20173, 4212, 4214, 4215, 4216, 4217, 4218, 4219, 4220, 4221, 4222, 4223, 4734, 4934,
			4935, 4936, 4937, 10034, 10033};
	public static final int[] OTHER_RANGE_WEAPONS = {863, 864, 865, 866, 867, 868, 869, 806, 807, 808, 809, 810, 811, 825,
			826, 827, 828, 829, 830, 800, 801, 802, 803, 804, 805, 6522, 13883, 13879, 10034, 10033, 11230};
	public static final int[] REDUCE_SPELL_TIME = {250000, 250000, 250000, 500000, 500000, 500000}; // how long does the
	public static final int[] REDUCE_SPELLS = {1153, 1157, 1161, 1542, 1543, 1562};
	public static final int[] PRAYER_DRAIN_RATE = {500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
			500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500};
	public static final int[] PRAYER_LEVEL_REQUIRED = {1, 4, 7, 8, 9, 10, 13,
			16, 19, 22, 25, 26, 27, 28, 31, 34, 37, 40, 43, 44, 45, 46, 49, 52,
			60, 70, 74, 77};
	public static final int[] PRAYER = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
			12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27};
	public static final String[] PRAYER_NAME = {"Thick Skin", "Burst of Strength", "Clarity of Thought", "Sharp Eye",
			"Mystic Will", "Rock Skin", "Superhuman Strength", "Improved Reflexes", "Rapid Restore", "Rapid Heal",
			"Protect Item", "Hawk Eye", "Mystic Lore", "Steel Skin", "Ultimate Strength", "Incredible Reflexes",
			"Protect from Magic", "Protect from Missiles", "Protect from Melee", "Eagle Eye", "Mystic Might",
			"Retribution", "Redemption", "Smite", "Chivalry", "Piety", "Rigour", "Augury"};
	public static final int[] PRAYER_GLOW = {83, 84, 85, 601, 602, 86, 87, 88,
			89, 90, 91, 603, 604, 92, 93, 94, 95, 96, 97, 605, 606, 98, 99,
			100, 607, 608, 609, 610};
	public static final int[] PRAYER_HEAD_ICONS = {-1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, 2, 1, 0, -1, -1, 3, 5, 4, -1,
			-1, -1, -1};
	public static final int[] CURSE_LEVEL_REQUIRED = {50, 50, 52, 54, 56, 59, 62, 65, 68, 71, 74, 76, 78, 80, 82, 84, 86, 89,
			92, 95};
	public static final int[] CURSE_ID = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
	public static final String[] CURSE_NAME = {"Protect Item", "Sap Warrior", "Sap Ranger", "Sap Mage", "Sap Spirit",
			"Berserker", "Deflect Summoning", "Deflect Magic", "Deflect Missiles", "Deflect Melee", "Leech Attack",
			"Leech Ranged", "Leech Magic", "Leech Defence", "Leech Strength", "Leech Energy", "Leech Special Attack",
			"Wrath", "Soul Split", "Turmoil"};
	public static final int[] CURSE_GLOW = {83, 84, 85, 101, 102, 86, 87, 88, 89, 90, 91, 103, 104, 92, 93, 94, 95, 96, 97,
			105, 105};
	public static final int[] CURSE_HEAD_ICONS = {-1, -1, -1, -1, -1, -1, -1, 11, 12, 10, -1, -1, -1, -1, -1, -1, -1, 17, 18,
			-1};
	public static Player huntingPlayer = null;
	public static int[] ranks = new int[11];
	public static String[] rankPpl = new String[11];
	private final Cooldowns cooldowns = new Cooldowns(this);
	private final Bank bank = new Bank(this);
	private final Chat chat = new Chat(this);

	private final Queue<Packet> queuedPackets = new LinkedList<>();
	private final Queue<Packet> walkingQueuedPackets = new LinkedList<>();

	public boolean packetSend;

	public InstancesArea instancesArea;


	public Map<Integer, Map<Integer, Integer>> npcDrops = new HashMap<>();

	public int strBonus;
	public int pestControlTempPoints;
	public boolean inPestControl;
	public boolean winGamble = false;
	public boolean bot = false;
	public boolean ironman = false;
	public boolean hardcoreIronman = false;
	public boolean elite;
	public String uuid;
	public int openInterfaceId = -1;
	public int smallPouchP, smallPouchE;
	public int mediumPouchP, mediumPouchE;
	public int largePouchP, largePouchE;
	public int giantPouchP, giantPouchE;
	public int mediumPouchDecay, largePouchDecay, giantPouchDecay;
	public int currentEffigy = -1;
	public int mageState = 0;
	public boolean hasSolBuff = false;
	public List<NPC> localNpcs = new LinkedList<>();

	private NPC pet;
	public WalkingQueue walkingQueue = new WalkingQueue(this);
	public boolean timedOut = false;
	public boolean receivedFreeCannon = false;
	public int questPoints = 0;
	public List<Integer> theStolenCannonNpcsKilled = new ArrayList<>();
	public int theStolenCannonStatus = 0;
	public int duelRequestId = -1;
	public boolean canBeAttacked = true;
	public Map<Level, List<Integer>> cluesReceived = new HashMap<>();
	public Puzzle puzzle = null;
	public boolean inWild;
	public int addVotePoints = 0;
	public int zamorakKills;
	public int saradominKills;
	public int bandosKills;
	public int armadylKills;
	public int baubleMaking = 0;
	public int marionetteMaking = 0;
	public int christmasEvent = 0;
	public int giftAmount = 0;
	public int puppetBoxesCompleted = 0;
	public int baubleBoxesCompleted = 0;
	public boolean blueBox;
	public boolean redBox;
	public boolean greenBox;
	public boolean pinkBox;
	public boolean yellowBox;
	public boolean blockWalking = false;
	public int redAmount = 0;
	public int blueAmount = 0;
	public int greenAmount = 0;
	public String color = "";
	public String action = "";
	public boolean greenHead;
	public boolean blueHead;
	public boolean redHead;
	public boolean doingAction = false;
	private List<Player> localPlayers = new ArrayList<>();
	public int starBauble = 0;
	public boolean muted = false;
	public int boxBauble = 0;
	public int diamondBauble = 0;
	public int treeBauble = 0;
	public int bellBauble = 0;
	public long packetCheck;
	public boolean checkCheatPackets = true;
	/* Agility Variables */
	public boolean onObstacle = false;
	public boolean skilling = false;
	public List<ArrayList<Boolean>> obstaclesCompleted = new ArrayList<>(Course.values().length);
	/**
	 * WeaponGame
	 */
	public int currentClass = 0;
	public int meleeClassLvl = 1;
	public int rangedClassLvl = 1;
	public int magicClassLvl = 1;
	public int weaponKills = 0;
	public int weaponDeaths = 0;
	public int gambleRequestId = -1;
	public int lvlPotential = 0;
	public boolean canLeaveDeath = false;
	public boolean hadTarget = false, shouldGetDrops = true;
	public int targPos = -1;
	public String targetString = "";
	public int lockedEXP = 0;
	public int[] achievements;
	public Boolean[] perks = new Boolean[Perks.COUNT];
	public int killStreak = 0, maxKillStreak = 0;
	public String lastKilled = "", nemisis = "";
	public int achievePoints = 0, epAmount = 0;
	public int attempts = 3;
	public Clan clan;
	public int gnomeStage = 0;
	public int wildStage = 0;
	public boolean timedLogout = false;
	public boolean forceDisconnect = false;
	public int zombiePoints;
	public boolean lampVfy = false;
	public String lastConnection = "?~~?";
	public int lastConnectionDay = 0;
	public ArrayList<Integer> attackedPlayers = new ArrayList<>();
	public ArrayList<String> lastKilledPlayers = new ArrayList<>();
	public int npcId2 = 0;
	public int earningPotential, epDelay;
	public int shopAction, switchAction, bowDelay, tradeTimer, sendOptionChoice, delay, actionTimer, gwdAltarTimer;
	public int assaultPoints, isMember = 0, dfsCount = 0, gameInterface;
	public int playerKillCount, playerDeaths;
	public boolean playerIsFishing;
	public int stardustMined;
	public int yInterfaceId;
	public int LOGIN_TAB_ID = 0;
	public int TAB_NUMBER = 0;
	public boolean NEXT = false;
	public int[] fishingProp = new int[10];
	public boolean stopPlayerSkill;
	public long lastSwitch, lastSkillEmote;
	public long tradeDelay;
	public boolean isNpc, operating;
	public int said = 0;
	public int hasDoneCaves = 0;
	public int npcWave = 1;
	public boolean storingFamiliarItems;
	public boolean invincible;
	public boolean animationBusy;
	public boolean buyingX, initialized = false, ruleAgreeButton = false,
			isBanking = false, noTele, isActive = false, isSkulled = false, newPlayer = false,
			hasMultiSign = false, mouseButton = false, chatEffects = true,
			acceptAid = false, autocasting = false, mageFollow = false, dbowSpec = false, craftingLeather = false,
			properLogout = false, maxNextHit = false, ssSpec = false, vengOn = false, addStarter = false,
			accountFlagged = false, msbSpec = false, overpower = false, isNotFighting, fireCape = false,
			dungTele = false;
	public int donatorPoint,
			processPackets = 0, // enabledYellTimer,
			desertStage = 0, kills, deaths, playerKilled,
			totalPlayerDamageDealt, killedBy, lastChatId = 1, privateChat, dialogueId, randomCoffin, newLocation,
			specEffect, specBarId,
			followId, summonEmote, dungeonEmote, skullTimer, nextChat = 0, talkingNpc = -1, dialogueAction = 0,
			autocastId, followId2, barrageCount = 0, delayedDamage = 0, delayedDamage2 = 0,
			pcPoints = 0, magePoints = 0, SolProtect = 0, lastArrowUsed = -1, autoRet = 0, pcDamage = 0,
			xInterfaceId = 0, xRemoveId = 0, xRemoveSlot = 0, frozenBy = 0,
			poisonDamage = 0, teleAction = 0, bonusAttack = 0, deathCount, killCount, myDamnTimer,
			yellPoints, pestAction, votePoints, helpTimer, helpClear, tournamentPoints;

	public long jailTimer = 0;
	public int cageOneX = 2608;
	public int cageOneY = 3166;
	public int cageTwoX = 2608;
	public int cageTwoY = 3163;
	public int cageThreeX = 2608;
	public int cageThreeY = 3159;
	// public static int deathCount, killCount;
	public int clawDelay = 0;
	public int previousDamage = 0;
	public boolean usingClaws = false;
	public String type;
	public int[] voidStatus = new int[5];
	public int[] pouches = new int[4];
	public boolean[] invSlot = new boolean[28], equipSlot = new boolean[14];
	public double specAmount = 0;
	public double specAccuracy = 1;
	public double specDamage = 1;
	public long lastAlch = System.currentTimeMillis();
	public double prayerPoint = 1.0;
	public int teleGrabItem, teleGrabX, teleGrabY, underAttackBy, underAttackByNpcID, wildLevel, teleTimer, respawnTimer,
			saveTimer = 0, teleBlockLength, poisonDelay;
	public long lastPoison, lastPoisonSip, poisonImmune, lastSpear, lastProtItem, dfsDelay, lastVeng, teleGrabDelay,
			protMageDelay, protMeleeDelay, protRangeDelay, lastAction, lastThieve, lastLockPick, alchDelay,
			specDelay = System.currentTimeMillis(), teleBlockDelay, godSpellDelay, singleCombatDelay,
			singleCombatDelay2, reduceStat, restoreStatsDelay, logoutDelay, buryDelay, foodDelay, potDelay;
	public boolean canChangeAppearance = false;
	public boolean mageAllowed;
	public byte poisonMask = 0;
	public int ag1, ag2, ag3, ag4, ag5, ag6;
	public int tehTimer;
	public boolean killed, hcSpec = false;
	public boolean extAtt, extDef, extStr, extRange, extMage;
	public int[][] barrowsNpcs = {{2030, 0}, // verac
			{2029, 0}, // toarg
			{2028, 0}, // karil
			{2027, 0}, // guthan
			{2026, 0}, // dharok
			{2025, 0} // ahrim
	};
	public int barrowsKillCount;
	public int reduceSpellId;
	// other player stay
	// immune to the spell
	public long[] reduceSpellDelay = new long[6];
	public boolean[] canUseReducingSpell = {true, true, true, true, true, true};
	public int soulSplitDelay = 0, leechAttackDelay = 0, leechRangeDelay = 0, leechMagicDelay = 0,
			leechDefenceDelay = 0, leechStrengthDelay = 0, leechSpecialDelay = 0, voted;
	public int leechAttackTimer = 0, vestaDelay = 0;
	public int playerPrayerBook = 0;
	public int prayerId = -1;
	public int headIcon = -1;
	public long stopPrayerDelay, prayerDelay;
	public boolean usingPrayer;
	public boolean[] prayerActive = new boolean[28];
	public boolean[] curseActive = {false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false, false, false, false, false};
	public int headIconPk = -1, headIconHints;
	public boolean turmoilBoolean, berserkerBoolean, soulsplitBoolean, protectSummon, protectMissiles, protectMelee,
			wrathBoolean, protectMage;
	public boolean specleech = false;
	public boolean attackleech = false;
	public boolean rangedleech = false;
	public boolean magicleech = false;
	public boolean defenceleech = false;
	public boolean strengthleech = false;
	public boolean doubleHit, usingSpecial, usingRangeWeapon, usingBow, usingMagic, castingMagic;
	public int specMaxHitIncrease, freezeDelay, freezeTimer = -6, killerId, playerIndex, oldPlayerIndex,
			lastWeaponUsed, projectileStage, crystalBowArrowCount, playerMagicBook, teleGfx, teleEndAnimation,
			teleEndGfx, teleHeight, teleX, teleY, rangeItemUsed, killingNpcIndex, oldNpcIndex,
			attackTimer, npcIndex, npcClickIndex, npcType, castingSpellId, oldSpellId, spellId, hitDelay;
	public boolean magicFailed;
	public int bowSpecShot, clickNpcType, clickObjectType, objectId, objectX, objectY, objectXOffset, objectYOffset,
			objectDistance;
	public int pItemX, pItemY, pItemId;
	public boolean isMoving, walkingToItem;
	public int myShopId;
	public int tradeStatus, tradeWith;
	public boolean forcedChatUpdateRequired, tradeAccepted, goodTrade, inTrade, tradeRequested, tradeResetNeeded,
			tradeConfirmed, tradeConfirmed2, canOffer, acceptTrade, acceptedTrade;
	public int animationRequest = -1, animationWaitCycles;
	public int[] playerBonus = new int[12];
	public boolean takeAsNote;
	public int combatLevel;
	public int[] appearance = new int[13];
	public int apset;
	public int actionID;
	public int XremoveSlot, XinterfaceID, XremoveID, Xamount;
	public int tutorial = 15;
	public boolean usingGlory = false;
	public boolean usingRingOfWealth = false;
	public boolean fishing = false;
	public int antiFirePot;
	/**
	 * Castle Wars
	 */
	public int castleWarsTeam;
	public boolean inCwGame;
	public boolean inCwWait;
	public boolean inCombat = false;
	public boolean protectItem = false;
	public boolean zerker = false;
	public int NewDrain = 0, CurrentDrain = 0, DrainDelay = 0;
	public int DefPray = 0;
	public int StrPray = 0;
	public int AtkPray = 0;
	public boolean turmoil = false;
	/**
	 * Fight Pits
	 */
	public boolean inPits = false;
	public int pitsStatus = 0;
	public boolean derp;
	public int voteReset;
	public long lastXpgiven;
	public boolean choseMage, choseMelee, choseRange, gameReady;
	public int trinityWarKills, trinityPoints;
	public String team;
	public int clanTele = 1;
	public int clanAction;
	public int timeInWilderness = 0;
	public String connectedFrom = "";
	public int playerId = -1;
	public long encodedName = -1;
	public String username = null;
	public String email = "";
	public String playerName2 = null;
	public String playerPass = null;
	public int rights;
	public int[] inventory = new int[28];
	public int[] inventoryN = new int[28];
	public int playerStandIndex = 0x328;
	public int playerTurnIndex = 0x337;
	/*
	 * public boolean inFunPk() { return absX > 2359 && absX < 2434 && absY > 3067 && absY < 3146 || absX > 2128 && absX
	 * < 2173 && absY > 5084 && absY < 5111 || absX > 2627 && absX < 2654 && absY > 9889 && absY < 9917; //return (absX
	 * > 2128 && absX < 2173 && absY > 5084 && absY < 5111); }
	 */
	public int playerWalkIndex = 0x333;
	public int playerTurn180Index = 0x334;
	public int playerTurn90CWIndex = 0x335;
	public int playerTurn90CCWIndex = 0x336;
	public int playerRunIndex = 0x338;
	public int playerHat = 0;
	public int playerCape = 1;
	public int playerAmulet = 2;
	public int playerChest = 4;
	public int playerShield = 5;
	public int playerLegs = 7;
	public int playerHands = 9;
	public int playerFeet = 10;
	public int playerRing = 12;
	public int playerArrows = 13;
	public int[] equipment = new int[14];
	public int[] playerEquipmentN = new int[14];
	public int[] level = new int[25];
	public int[] xp = new int[25];
	public int destroyItem = -1, destroyAmount = -1;
	public int playerSE = 0x328;
	public int playerSEW = 0x333;
	public int playerSER = 0x334;

	public int forceMovementStartX;
	public int forceMovementStartY;
	public int forceMovementEndX;
	public int forceMovementEndY;
	public int forceMovementSpeed1;
	public int forceMovementSpeed2;
	public int forceMovementDirection;
	public boolean forceMovementUpdate;


	public boolean updateRequired = true;
	public boolean itemNameUpdate = false;
	private Location teleportLocation = null;
	public boolean didTeleport = false;
	public boolean mapRegionDidChange = false;
	public boolean createItems = false;
	public int DirectionCount = 0;
	public boolean appearanceUpdateRequired = true;
	public int hitDiff2;
	public int hitDiff = 0, hitIcon, hitFocus, hitMax, hitIcon2, hitFocus2, hitMax2; // edited
	public boolean hitUpdateRequired2;
	public boolean hitUpdateRequired = false; // edited private boolean hitUpdateRequired = false;
	public boolean isDead = false, isSummoning = false;
	public Stream cache = null;
	public String forcedText = "null";
	/**
	 * Graphics
	 */
	public int mask100var1 = 0;
	public int mask100var2 = 0;
	public int face = -1;
	public int FocusPointX = -1, FocusPointY = -1;
	public int[] damageTaken = new int[Config.MAX_PLAYERS];
	/**
	 * The list of local npcs.
	 */
	public boolean inProcess = false;
	public Player requestedWar = null;
	public Player requestingWar = null;
	public int[] bind = new int[5];
	// public int tournamentPoints;
	public int clawIndex;
	public int clawType = 0;
	public int playerRank = 0;
	public int clawDamage;
	public Stream outStream = null;
	public boolean korasiSpec;
	public int lowMemoryVersion = 0;
	public int timeOutCounter = 0;
	public int returnCode = 2;
	public long lastSpecialUse;
	public long lastDungEmote;
	public long lastPray = 0;
	public int dfsCharges = 0;
	public boolean isCooking;
	public Cooking cooking = new Cooking(this);
	public int currMassacre = 0;
	public boolean keepItem;
	public int skillInput;
	public boolean mask100update = false;
	/**
	 * Face Update
	 */
	public boolean faceUpdateRequired = false;
	boolean wasInPits;
	boolean wasInArena;
	private boolean disconnected = false;
	private long lastEnergyIncrease = System.currentTimeMillis();
	private Dialogue currentDialogue;
	private int databaseId;
	private boolean busy = false;
	private boolean firemakingWalk = false;
	private long lastSaved = System.currentTimeMillis();
	private CastleWars.State castleWarsState;
	private int castleWarsCaptures = 0;
	private int castleWarsKills = 0;
	private PlayerData data = new PlayerData();
	private PlayerFamiliar familiar = null;
	private boolean leavePacketsQueued = false;
	private Object dialogueOwner = null;
	private int currentDialogueId = -1;
	private int nextDialogueId = -1;
	private Cannon cannon = null;
	private DuelArena.Duel duel = null;
	private int attackStyle = 1;
	private CombatType combatType = CombatType.CHRUSH;
	private CombatStyle combatStyle = CombatStyle.ACCURATE;
	private byte tutorialProgress = 0;
	private boolean doingTutorial = false;
	private boolean hidden = false;
	private boolean awaitingUpdate = false;
	private boolean chatTextUpdateRequired = false;
	private byte[] chatText = new byte[4096];
	private byte chatTextSize = 0;
	private int chatTextColor = 0;
	private int chatTextEffects = 0;
	private Channel session;
	private ItemAssistant itemAssistant = new ItemAssistant(this);
	private Trading tradeAndDuel = new Trading(this);
	private PlayerAssistant playerAssistant = new PlayerAssistant(this);
	private CombatAssistant combatAssistant = new CombatAssistant(this);
	private ActionHandler actionHandler = new ActionHandler(this);
	private DialogueHandler dialogueHandler = new DialogueHandler(this);
	private Potions potions = new Potions(this);
	private PotionMixing potionMixing = new PotionMixing(this);
	private Food food = new Food(this);
	/**
	 * Skill instances
	 */
	private TradeLog tradeLog = new TradeLog(this);
	// private Slayer slayer = new Slayer();
	private Runecrafting runecrafting = new Runecrafting(this);
	private Crafting crafting = new Crafting(this);
	private Smithing smith = new Smithing(this);
	private Prayer prayer = new Prayer(this);
	private SmithingInterface smithInt = new SmithingInterface(this);
	private Thieving thieving = new Thieving(this);

	public Player(Channel s, int _playerId, boolean bot) {
		this(_playerId);
		this.session = s;
		outStream = new Stream(new byte[5000]);
		outStream.currentOffset = 0;
		this.bot = bot;

		// remove the netty timeout handler
		if (!bot) {
			s.getPipeline().remove("timeout");
		}
	}

	public Player(int _playerId) {
		playerId = _playerId;
		rights = 0;
		// achievements = new int[Achievements.AMOUNT+1];
		for (int i = 0; i < inventory.length; i++) {
			inventory[i] = 0;
		}
		for (int i = 0; i < inventoryN.length; i++) {
			inventoryN[i] = 0;
		}
		for (int i = 0; i < level.length; i++) {
			if (i == 3) {
				level[i] = 10;
			} else {
				level[i] = 1;
			}
		}
		for (int i = 0; i < xp.length; i++) {
			if (i == 3) {
				xp[i] = 1300;
			} else {
				xp[i] = 0;
			}
		}
		appearance[0] = 0; // gender
		appearance[1] = 6; // head
		appearance[2] = 18;// Torso
		appearance[3] = 26; // arms
		appearance[4] = 9; // hands
		appearance[5] = 36; // legs
		appearance[6] = 42; // feet
		appearance[7] = 10; // beard
		appearance[8] = 0; // hair colour
		appearance[9] = 5; // torso colour
		appearance[10] = 4; // legs colour
		appearance[11] = 0; // feet colour
		appearance[12] = 0; // skin colour
		apset = 0;
		actionID = 0;
		equipment[playerHat] = -1;
		equipment[playerCape] = -1;
		equipment[playerAmulet] = -1;
		equipment[playerChest] = -1;
		equipment[playerShield] = -1;
		equipment[playerLegs] = -1;
		equipment[playerHands] = -1;
		equipment[playerFeet] = -1;
		equipment[playerRing] = -1;
		equipment[playerArrows] = -1;
		equipment[playerWeapon] = -1;
		heightLevel = 0;

		setTeleportLocation(Location.create(Config.START_LOCATION_X, Config.START_LOCATION_Y, 0));

		absX = absY = -1;

		for (Level level : Level.values()) {
			cluesReceived.put(level, new ArrayList<>());
		}

		this.resetWalkingQueue();
	}

	/**
	 *
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param speed1
	 * @param speed2
	 * @param direction north 0, east 1, south 2, west 3
	 */
	public void forceMovement(int startX, int startY, int endX, int endY, int speed1, int speed2, int direction) {
		forceMovementStartX = startX;
		forceMovementStartY = startY;
		forceMovementEndX = endX;
		forceMovementEndY = endY;
		forceMovementSpeed1 = speed1;
		forceMovementSpeed2 = speed2;
		forceMovementDirection = direction;

		appearanceUpdateRequired = true;
		forceMovementUpdate = true;
		updateRequired = true;


	}

	public static int dateToInt() {
		Calendar cal = new GregorianCalendar();
		return cal.get(Calendar.DAY_OF_YEAR);
	}

	public static void writeData(String data, String file) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file + ".txt", true))) {
			writer.write(data);
			writer.newLine();
			writer.flush();
		} catch (IOException IOE) {
			IOE.printStackTrace();
		}
	}

	public void addNewNPC(NPC npc, Stream str, Stream updateBlock) {
		int id = npc.id;
		str.writeBits(14, id);
		int z = npc.absY - this.absY;
		if (z < 0) {
			z += 32;
		}
		str.writeBits(5, z);
		z = npc.absX - this.absX;
		if (z < 0) {
			z += 32;
		}
		str.writeBits(5, z);
		str.writeBits(1, 0);
		str.writeBits(14, npc.type);
		boolean savedUpdateRequired = npc.updateRequired;
		npc.updateRequired = true;
		npc.appendNPCUpdateBlock(updateBlock);
		npc.updateRequired = savedUpdateRequired;
		str.writeBits(1, 1);
	}

	public boolean Area(int x, int x1, int y, int y1) {
		return absX >= x && absX <= x1 && absY >= y && absY <= y1;
	}

	// public String spellName = "Select Spell";
	public boolean assignAutocast(int button) {
		for (int i = 0; i < MAGIC_SPELLS.length; i++) {
			if (MAGIC_SPELLS[i].length == 17) {
				if (MAGIC_SPELLS[i][16] == button) {
					autocasting = true;
					autocastId = i;
					return true;
				}
			}
		}
		return false;
	}

	public void bind(int itemId, int index) {
		for (int i = 0; i < bind.length; i++) {
			if (bind[i] == 0) {
				Bindable bindable = Item.bindables[index];
				if (this.getItems().playerHasItem(itemId)) {
					this.getItems().deleteItem(itemId, 1);
					this.getItems().addItem(bindable.getBindedID(), 1);
					bind[i] = bindable.getBindedID();
					this.sendMessage("You succesfully bound this item.");
					return;
				}
			}
		}
		this.sendMessage("You are unable to bind this item.");
	}

	/**
	 * SouthWest, NorthEast, SouthWest, NorthEast
	 */
	public boolean cageOne() {
		return absX >= 2607 && absX <= 2611 && absY >= 3165 && absY <= 3167;
	}

	public boolean cageThree() {
		return absX >= 2607 && absX <= 2611 && absY >= 3158 && absY <= 3161;
	}

	public boolean cageTwo() {
		return absX >= 2607 && absX <= 2611 && absY >= 3162 && absY <= 3164;
	}

	public int calculateCombatLevel() {
		int mag = (int) ((getPA().getLevelForXP(xp[6])) * 1.5);
		int ran = (int) ((getPA().getLevelForXP(xp[4])) * 1.5);
		int attstr = (int) ((double) (getPA().getLevelForXP(xp[0])) + (double) (getPA().getLevelForXP(xp[2])));
		if (ran > attstr) {
			combatLevel = (int) (((getPA().getLevelForXP(xp[1])) * 0.25) + ((getPA().getLevelForXP(xp[3])) * 0.25)
					+ ((getPA().getLevelForXP(xp[5])) * 0.125) + ((getPA().getLevelForXP(xp[4])) * 0.4875) + ((getPA().getLevelForXP(xp[PlayerConstants.SUMMONING])) * 0.125));
		} else if (mag > attstr) {
			combatLevel = (int) (((getPA().getLevelForXP(xp[1])) * 0.25) + ((getPA().getLevelForXP(xp[3])) * 0.25)
					+ ((getPA().getLevelForXP(xp[5])) * 0.125) + ((getPA().getLevelForXP(xp[6])) * 0.4875) + ((getPA().getLevelForXP(xp[PlayerConstants.SUMMONING])) * 0.125));
		} else {
			combatLevel = (int) (((getPA().getLevelForXP(xp[1])) * 0.25) + ((getPA().getLevelForXP(xp[3])) * 0.25)
					+ ((getPA().getLevelForXP(xp[5])) * 0.125) + ((getPA().getLevelForXP(xp[0])) * 0.325)
					+ ((getPA().getLevelForXP(xp[2])) * 0.325) + ((getPA().getLevelForXP(xp[PlayerConstants.SUMMONING])) * 0.125));
		}
		return combatLevel;
	}

	public int getSummoningCombat() {
		return (int) ((getPA().getLevelForXP(xp[PlayerConstants.SUMMONING])) * 0.125);
	}

	public void clearUpdateFlags() {
		updateRequired = false;
		itemNameUpdate = false;
		this.setChatTextUpdateRequired(false);
		this.setAppearanceUpdateRequired(false);
		this.setHitUpdateRequired(false);
		hitUpdateRequired2 = false;
		forcedChatUpdateRequired = false;
		mask100update = false;
		animationRequest = -1;
		FocusPointX = -1;
		FocusPointY = -1;
		poisonMask = -1;
		faceUpdateRequired = false;
		face = 65535;
		cache = null;
		mapRegionDidChange = false;
		didTeleport = false;
		forceMovementUpdate = false;
	}

	public void correctCoordinates() {
		if (this.inPcGame()) {
			this.getPA().movePlayer(2657, 2639, 0);
		}
	}

	public boolean immuneToDamage;

	public void dealDamage(int damage) {

		if(immuneToDamage) {
			return;
		}

		if (teleTimer <= 0) {

			// spirit shield effect
			if (equipment[PlayerConstants.SHIELD] == 13740 || equipment[PlayerConstants.SHIELD] == 13742 || equipment[PlayerConstants.SHIELD] == 13738) {

				if (level[PlayerConstants.PRAYER] > 0) {

					int prayer = level[PlayerConstants.PRAYER];
					int damageReduce = (int) (damage * 0.3);

					if (damageReduce >= prayer) {
						damageReduce = prayer;
					}

					damage -= damageReduce;
					level[PlayerConstants.PRAYER] -= damageReduce / 2;

					sendMessage("Your spirit shield deflects some of the damage!");

					if (level[PlayerConstants.PRAYER] <= 0) {
						sendMessage("You have run out of prayer points!");
						level[5] = 0;
						getCombat().resetAllPrayers();
						prayerId = -1;
					}

					getPA().refreshSkill(5);
				}
			}

			if (!invincible) {
				if (level[3] - damage < 0) {
					damage = level[3];
				}
				level[3] -= damage;
			}

			// wat would be melee ? :D
			// handleHitMask(damage, 10, 1, 0);
		} else {
			if (hitUpdateRequired) {
				hitUpdateRequired = false;
			}
			if (hitUpdateRequired2) {
				hitUpdateRequired2 = false;
			}
		}
	}

	public boolean debind(int itemId) {
		for (int i = 0; i < bind.length; i++) {
			if (bind[i] == itemId) {
				this.getItems().deleteItem(itemId, 1);
				bind[i] = 0;
				return true;
			}
		}
		return false;
	}

	void destruct() {
		if (!bot && session == null) {
			return;
		}

		chat.logout();
		if (clan != null) {
			clan.removeMember(this);
		}
		/*
		 * if (bounty.getTarget() != null) { bounty.getTarget().sendMessage("Your target has disconnected.");
		 * bounty.resetTarget(); } if (bhTarg != null) { bhTarg.logout(this); }
		 */

		if (getFamiliar() != null) {
			getFamiliar().getNpc().isDead = true;
			getFamiliar().getNpc().needRespawn = true;
		}

		/*
         * if (Massacre.ref.get(username.toLowerCase()) != null) { Massacre.resetItems(this); getPA().movePlayer(3333,
		 * 3333, 0); Massacre.ref.remove(username.toLowerCase()); Massacre.players.remove(this); }
		 */
		// PlayerSaving.getSingleton().requestSave(playerId);
		// if (qualify && rights < 2) OLD HIGHSCORES
		// Highscores.execute(this);

		System.out.println("[DISCONNECTED]: " + username + "");

		// HostList.getHostList().remove(session);
		disconnected = true;
		if(!bot) {
			session.close();
			session = null;
		}
		outStream = null;
		isActive = false;

		/*Region region = Server.getRegionRepository().fromLocation(getLocation());
		if (region.contains(this)) {
			region.removeEntity(this);
		}*/

		absX = absY = -1;
		this.resetWalkingQueue();
	}

	public int distanceToPoint(int pointX, int pointY) {
		int xdiff = absX - pointX;
		int ydiff = absY - pointY;
		return (int) Math.sqrt(xdiff * xdiff + ydiff * ydiff);
	}

	public void faceUpdate(int index) {

		// fixes hp overlay and it should be -1 anyway
		if(index == 0) {
			index = -1;
		}

		face = index;
		faceUpdateRequired = true;
		updateRequired = true;
	}

	public void flushOutStream() {


		if (session == null) {
			return;
		}

		if (!session.isConnected() || disconnected || outStream.currentOffset == 0) {
			return;
		}
		byte[] temp = new byte[outStream.currentOffset];
		System.arraycopy(outStream.buffer, 0, temp, 0, temp.length);
		Packet packet = new Packet(-1, Packet.Type.FIXED, ChannelBuffers.wrappedBuffer(temp));
		session.write(packet);
		outStream.currentOffset = 0;
	}

	public synchronized void writePacket(Packet p) {
		if (bot || !session.isConnected()) {
			return;
		}

		session.write(p);
	}

	public void forcedChat(String text) {
		forcedText = text;
		forcedChatUpdateRequired = true;
		updateRequired = true;
		this.setAppearanceUpdateRequired(true);
	}

	public int getAbsX() {
		return absX;
	}

	/*
     * public boolean inNoTrade() { return absX >= 3164 && absX <= 3257 && absY >= 3398 && absY <= 3470; }
	 */

	/*
     * public boolean inZombieMap() { return absX >= 3636 && absX <= 3745 && absY >= 3456 && absY <= 3539; }
	 */

	public int getAbsY() {
		return absY;
	}

	public ActionHandler getActions() {
		return actionHandler;
	}

	public int getActualLevel(int level) {
		return getPA().getLevelForXP(xp[level]);
	}

	public int getAttackStyle() {
		return attackStyle;
	}

	public void setAttackStyle(int attackStyle) {
		this.attackStyle = attackStyle;
	}

	public boolean getAwaitingUpdate() {
		return awaitingUpdate;
	}

	public void setAwaitingUpdate(boolean update) {
		awaitingUpdate = update;
	}

	public Bank getBank() {
		return bank;
	}

	public int getBestRatio(Player player) {
		return combatLevel - player.combatLevel;
	}

	public Cannon getCannon() {
		return cannon;
	}

	public void setCannon(Cannon cannon) {
		this.cannon = cannon;
	}

	public int getCastleWarsCaptures() {
		return castleWarsCaptures;
	}

	public void setCastleWarsCaptures(int castleWarsCaptures) {
		this.castleWarsCaptures = castleWarsCaptures;
	}

	public int getCastleWarsKills() {
		return castleWarsKills;
	}

	public void setCastleWarsKills(int castleWarsKills) {
		this.castleWarsKills = castleWarsKills;
	}

	public CastleWars.State getCastleWarsState() {
		return castleWarsState;
	}

	public void setCastleWarsState(CastleWars.State castleWarsState) {
		this.castleWarsState = castleWarsState;
	}

	public Chat getChat() {
		return chat;
	}

	public byte[] getChatText() {
		return chatText;
	}

	public void setChatText(byte[] b) {
		this.chatText = b;
	}

	public int getChatTextColor() {
		return chatTextColor;
	}

	public void setChatTextColor(int chatTextColor) {
		this.chatTextColor = chatTextColor;
	}

	public int getChatTextEffects() {
		return chatTextEffects;
	}

	public void setChatTextEffects(int chatTextEffects) {
		this.chatTextEffects = chatTextEffects;
	}

	public byte getChatTextSize() {
		return chatTextSize;
	}

	public void setChatTextSize(byte chatTextSize) {
		this.chatTextSize = chatTextSize;
	}

	public CombatAssistant getCombat() {
		return combatAssistant;
	}

	public CombatStyle getCombatStyle() {
		return combatStyle;
	}

	public void setCombatStyle(CombatStyle combatStyle) {
		this.combatStyle = combatStyle;
	}

	public CombatType getCombatType() {
		return combatType;
	}

	public void setCombatType(CombatType combatType) {
		this.combatType = combatType;
	}

	public Cooking getCooking() {
		return cooking;
	}

	public Cooldowns getCooldowns() {
		return cooldowns;
	}

	public Crafting getCrafting() {
		return crafting;
	}

	public int getCurrentDialogueId() {
		return currentDialogueId;
	}

	public void setCurrentDialogueId(int currentDialogueId) {
		this.currentDialogueId = currentDialogueId;
	}

	public PlayerData getData() {
		return data;
	}

	public void setData(PlayerData data) {
		this.data = data;
	}

	public DialogueHandler getDH() {
		return dialogueHandler;
	}

	public Object getDialogueOwner() {
		return dialogueOwner;
	}

	public void setDialogueOwner(Object dialogueOwner) {
		this.dialogueOwner = dialogueOwner;
	}

	public DuelArena.Duel getDuel() {
		return duel;
	}

	public void setDuel(DuelArena.Duel duel) {
		this.duel = duel;
	}

	public PlayerFamiliar getFamiliar() {
		return familiar;
	}

	public void setFamiliar(PlayerFamiliar familiar) {
		this.familiar = familiar;

		appearanceUpdateRequired = true;
		updateRequired = true;
	}

	public Food getFood() {
		return food;
	}

	public int getHeightLevel() {
		return heightLevel;
	}

	public int getHitDiff() {
		return hitDiff;
	}

	public void setHitDiff(int amount) {
		this.hitDiff = amount;
	}

	public int getHitDiff2() {
		return hitDiff2;
	}

	public void setHitDiff2(int amount) {
		this.hitDiff2 = amount;
	}

	public boolean getHitUpdateRequired() {
		return hitUpdateRequired;
	}

	public boolean getHitUpdateRequired2() {
		return hitUpdateRequired2;
	}

	public void setHitUpdateRequired2(boolean hitUpdateRequired2) {
		this.hitUpdateRequired2 = hitUpdateRequired2;
	}

	public int getId() {
		return playerId;
	}

	public ItemAssistant getItems() {
		return itemAssistant;
	}

	public Location getLocation() {
		return Location.create(absX, absY, heightLevel);
	}

	public int getNextDialogueId() {
		return nextDialogueId;
	}

	public void setNextDialogueId(int nextDialogueId) {
		this.nextDialogueId = nextDialogueId;
	}

	public Stream getOutStream() {
		if (outStream == null) {
			setDisconnected(true, "outStream is null");
		}
		return outStream;
	}

	public PlayerAssistant getPA() {
		return playerAssistant;
	}

	public Potions getPotions() {
		return potions;
	}

	public PotionMixing getPotMixing() {
		return potionMixing;
	}

	public Prayer getPrayer() {
		return prayer;
	}

	public int getRegionID(int x, int y) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		return (regionX / 8 << 8) + regionY / 8;
	}

	public Runecrafting getRunecrafting() {
		return runecrafting;
	}

	public Channel getSession() {
		return session;
	}

	/*
     * public void checkPvpLevels(){ if (combatLevel < 15){ int Low = 3; int High = combatLevel + 12;
	 * getPA().sendFrame126("@red@"+Low+" - "+High+"", 6570); getPA().sendFrame126("@red@"+Low+" - "+High+"", 6570); }
	 * if (combatLevel > 15 && combatLevel < 114){ int Low = combatLevel - 12; int High = combatLevel + 12;
	 * getPA().sendFrame126("@red@"+Low+" - "+High+"", 6570); } if (combatLevel > 114){ int Low = combatLevel - 12; int
	 * High = 126; getPA().sendFrame126("@red@"+Low+" - "+High+"", 6570); }
	 * 
	 * }
	 */

	/**
	 * Skill Constructors
	 */
	//public Slayer getSlayer() {
	//    return slayer;
	//  }
	public Smithing getSmithing() {
		return smith;
	}

	public SmithingInterface getSmithingInt() {
		return smithInt;
	}

	public Thieving getThieving() {
		return thieving;
	}

	public Trading getTradeAndDuel() {
		return tradeAndDuel;
	}

	public TradeLog getTradeLog() {
		return tradeLog;
	}

	/**
	 * @return
	 */
	public byte getTutorialProgress() {
		return tutorialProgress;
	}

	/**
	 * @param tutorialProgress
	 */
	public void setTutorialProgress(byte tutorialProgress) {
		this.tutorialProgress = tutorialProgress;
	}

	public void gfx0(int gfx) {
		mask100var1 = gfx;
		mask100var2 = 65536;
		mask100update = true;
		updateRequired = true;
	}

	public void gfx100(int gfx) {
		mask100var1 = gfx;
		mask100var2 = 6553600;
		mask100update = true;
		updateRequired = true;
	}

	public void gfx50(int gfx) {
		mask100var1 = gfx;
		mask100var2 = 655360;
		mask100update = true;
		updateRequired = true;
	}

	/*
	 * new public boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) { if
	 * (clickObjectAtX > 0) { // click object at a certain point - alk update if (clickObjectAtX != playerX ||
	 * clickObjectAtY != playerY) distance--; } for (int i = 0; i <= distance; i++) { for (int j = 0; j <= distance;
	 * j++) { if ((objectX + i) == playerX && ((objectY + j) == playerY || (objectY - j) == playerY || objectY ==
	 * playerY)) { return true; } else if ((objectX - i) == playerX && ((objectY + j) == playerY || (objectY - j) ==
	 * playerY || objectY == playerY)) { return true; } else if (objectX == playerX && ((objectY + j) == playerY ||
	 * (objectY - j) == playerY || objectY == playerY)) { return true; } } } return false; }
	 */
	public boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		for (int i = 0; i <= distance; i++) {
			for (int j = 0; j <= distance; j++) {
				if ((objectX + i) == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if ((objectX - i) == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if (objectX == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				}
			}
		}
		return false;
	}

	public void handleHitMask(int damage, int max, int icon, int focus) {
		if (!hitUpdateRequired) {
			hitUpdateRequired = true;
			hitDiff = damage;
			hitIcon = icon;
			hitFocus = focus;
			hitMax = max;
		} else if (!hitUpdateRequired2) {
			hitUpdateRequired2 = true;
			hitDiff2 = damage;
			hitIcon2 = icon;
			hitFocus2 = focus;
			hitMax2 = max;
		}

		updateRequired = true;
	}

	public int highestDrainRate() {
		if (AtkPray == 7 && DefPray == 7 && StrPray == 7) {
			return 4;
		} else if (AtkPray == 6 && DefPray == 6 && StrPray == 6) {
			return 4;
		} else if (headIcon == 4) {
			return 4;
		} else if (headIcon == 0) {
			return 6;
		} else if (headIcon == 2) {
			return 6;
		} else if (headIcon == 1) {
			return 6;
		} else if (headIcon == 5) {
			return 6;
		} else if (headIcon == 3) {
			return 6;
		} else if (headIcon > 6) {
			return 5;
		} else if (AtkPray == 5) {
			return 6;
		} else if (AtkPray == 4) {
			return 6;
		} else if (AtkPray == 3) {
			return 6;
		} else if (StrPray == 3) {
			return 6;
		} else if (DefPray == 3) {
			return 6;
		} else if (headIcon == 5) {
			return 12;
		} else if (AtkPray == 2) {
			return 12;
		} else if (StrPray == 2) {
			return 12;
		} else if (DefPray == 2) {
			return 12;
		} else if (headIcon == 4) {
			return 24;
		} else if (AtkPray == 1) {
			return 24;
		} else if (StrPray == 1) {
			return 24;
		} else if (DefPray == 1) {
			return 24;
		} else if (turmoil) {
			return 3;
		} else if (specleech) {
			return 4;
		} else if (attackleech || defenceleech || rangedleech || strengthleech || magicleech) {
			return 8;
		} else if (zerker) {
			return 10;
		} else if (protectItem) {
			return 36;
		} else {
			return 0;
		}
	}

	public boolean inArea(int x, int y, int x1, int y1) {
		if (absX > x && absX < x1 && absY < y && absY > y1) {
			return true;
		}
		return false;
	}

	public boolean inBarrows() {
		if (absX > 3520 && absX < 3598 && absY > 9653 && absY < 9750) {
			return true;
		}
		return false;
	}

	public boolean inCastleWars() {
		return castleWarsState != null;
	}

	public boolean inDeathChamber() {
		if (this.absX >= 1666 && this.absX <= 1668 && this.absY >= 5703 && this.absY <= 5705) {
			return true;
		}
		if (this.absX >= 1650 && this.absX <= 1652 && this.absY >= 5702 && this.absY <= 5704) {
			return true;
		}
		if (this.absX >= 1655 && this.absX <= 1657 && this.absY >= 5682 && this.absY <= 5684) {
			return true;
		}
		if (this.absX >= 1675 && this.absX <= 1677 && this.absY >= 5687 && this.absY <= 5689) {
			return true;
		}
		return false;
	}

	public boolean inDonatorZone() {
		return absX > 2777 && absX < 2812 && absY > 3771 && absY < 3799;
	}

	public boolean inDuelArena() {
		if ((getLocation().getX() > 3322 && getLocation().getX() < 3394 && getLocation().getY() > 3195 && getLocation()
				.getY() < 3291)
				|| (getLocation().getX() > 3311 && getLocation().getX() < 3323 && getLocation().getY() > 3223 && getLocation()
				.getY() < 3248))
			return true;
		return false;
	}

	public boolean inFightCaves() {
		return absX >= 2360 && absX <= 2445 && absY >= 5045 && absY <= 5125;
	}

	public boolean inFightPits() {
		return absX > 2423 && absX < 2502 && absY > 5059 && absY < 5189;
	}

	public void initialize() {
		outStream.createFrame(249);
		outStream.writeByteA(1); // 1 for members, zero for free
		outStream.writeWordBigEndianA(playerId);
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (j == playerId) {
				continue;
			}
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].username.equalsIgnoreCase(username)) {
					setDisconnected(true, "someone else with the same username is online");
				}
			}
		}
		// LoginHandler.startInterface(this);
		for (int i = 0; i < 25; i++) {
			getPA().refreshSkill(i);
		}

		for (int i = 0; i < Course.values().length; i++) {
			obstaclesCompleted.add(new ArrayList<>());
			for (int j = 0; j < Course.values()[i].getNumberOfObstacles(); j++) {
				obstaclesCompleted.get(i).add(false);
			}
		}

		for (int p = 0; p < PRAYER.length; p++) { // reset prayer glows
			prayerActive[p] = false;
			this.getPA().sendFrame36(PRAYER_GLOW[p], 0);
		}

		this.getPA().sendFrame36(43, getAttackStyle());

		CombatHelper.setAttackVars(this);
		this.getPA().handleLoginText();
		accountFlagged = this.getPA().checkForFlags();

		getPA().sendFrame126("update counter:" + getData().xpCounter, 0);

		// getPA().sendFrame36(43, fightMode-1);
		this.getPA().sendFrame36(108, 0);// resets autocast button
		this.getPA().sendFrame36(172, 1);
		this.getPA().sendFrame107(); // reset screen
		this.getPA().setChatOptions(0, 0, 0); // reset private messages options

		this.setSidebarInterface(0, 2423);
		this.setSidebarInterface(1, 3917);
		this.setSidebarInterface(2, 638);
		this.setSidebarInterface(3, 3213);
		this.setSidebarInterface(4, 1644);

		if (playerPrayerBook == 0) { // regular
			this.setSidebarInterface(5, 5608);
		} else if (playerPrayerBook == 1) { // curses
			this.setSidebarInterface(5, 22500);
		} else {
			this.setSidebarInterface(5, 5608);
		}
		if (playerMagicBook == 2) {
			this.setSidebarInterface(6, 25555); // Lunar
		}
		if (playerMagicBook == 0) {
			this.setSidebarInterface(6, 1151); // modern
		}
		if (playerMagicBook == 1) {
			this.setSidebarInterface(6, 12855); // ancient
		}
		this.setSidebarInterface(7, 18128);
		this.setSidebarInterface(8, 5065);
		this.setSidebarInterface(9, 5715);
		this.setSidebarInterface(10, 2449); // 2449
		this.setSidebarInterface(11, 904); // wrench tab
		this.setSidebarInterface(12, 147); // run tab
		this.setSidebarInterface(13, 962); // music tab 6299 for lowdetail. 962 for
		// highdetail
		this.setSidebarInterface(14, 53335); // acheivement
		this.setSidebarInterface(15, 14317); // blank
		this.setSidebarInterface(16, 6000); // blank
		GreeGree.transform(this);

		// this.setSidebarInterface(18, 18017); // blank

		/*
		 * setSidebarInterface(1, 3917); setSidebarInterface(2, 638); setSidebarInterface(3, 3213);
		 * setSidebarInterface(4, 1644); if (playerPrayerBook == 0) { // regular setSidebarInterface(5, 5608); } else if
		 * (playerPrayerBook == 1) { // curses setSidebarInterface(5, 22500); } else { setSidebarInterface(5, 5608); }
		 * if (playerMagicBook == 2) { setSidebarInterface(6, 29999); // Lunar } if (playerMagicBook == 0) {
		 * setSidebarInterface(6, 1151); // modern } if (playerMagicBook == 1) { setSidebarInterface(6, 12855); //
		 * ancient } correctCoordinates(); setSidebarInterface(7, 18128); setSidebarInterface(8, 5065);
		 * setSidebarInterface(9, 5715); setSidebarInterface(10, 2449); // setSidebarInterface(11, 4445); // wrench tab
		 * setSidebarInterface(11, 904); // wrench tab setSidebarInterface(12, 147); // run tab setSidebarInterface(13,
		 * -1); setSidebarInterface(0, 2423);
		 */
		if (this.inPits() || this.cageOne() || this.cageTwo() || this.cageThree()) {
			this.getPA().movePlayer(2610 + Misc.random(2), 3149 + Misc.random(2), 0);
		}


		this.sendMessage("Welcome to Varrock.");
		//this.sendMessage("@red@Remember to stay active on the ::forums for latest updates/news and community interaction");
		//this.sendMessage("@red@Halloween event added! Talk to King Bolren at home!");
	//	this.sendMessage("@red@Huge wilderness updates + future updates! Type ::updates");
		//this.sendMessage("@red@Please check ::forums for the latest announcment about downtime on 6/16/16");
		// this.sendMessage("@red@Talk to miss cheevers to see the new 'account perks' from voting, ironmen included");
		//  if(Config.DOUBLE_XP) {
		//      this.sendMessage("@red@Double xp & vote rewards has been enabled! Check the announcement on the ::forums!");
		//  }


		MissCheevers.sendPerk(this);
		GameCycleTaskHandler.addEvent(this, container -> {
			MissCheevers.sendPerk(this);
		}, 100);

		getItems().sendMoneyPouch();
		getPA().sendEnergy();
		getPA().sendFrame36(173, getWalkingQueue().isRunningToggled() ? 1 : 0);

		if (email == null || email.length() == 0) {
			sendMessage("Your account is not secure. Please set an email address for recovery via ::account.");
		} else {
			sendMessage("Your account is secure. You can change your email address at any time via ::account.");
		}

		if (getDatabaseId() > 0) {
			Server.submitWork(() -> {
				try (Connection connection = DatabaseUtil.getConnection()) {
					try (PreparedStatement ps1 = connection.prepareStatement("insert into player_login_history (player_id, username, ip, uuid) values(?, ?, ?, ?)")) {
						ps1.setInt(1, databaseId);
						ps1.setString(2, username);
						ps1.setString(3, connectedFrom);
						ps1.setString(4, uuid);
						ps1.execute();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
		}

		getPA().sendUptime();
		GameCycleTaskHandler.addEvent(this, (GameCycleTaskContainer container) -> {
			getPA().sendUptime();
		}, 25);

		getPA().sendTimeOnline();
		GameCycleTaskHandler.addEvent(this, (GameCycleTaskContainer container) -> {
			getData().minutesOnline++;
			getPA().sendTimeOnline();

			if (clan != null && clan.getFounder().equalsIgnoreCase("Help") && clan.getRank(username) == Clan.Rank.ANYONE) {
				if (getData().minutesOnline > 30) {
					clan.removeMember(this);
					sendMessage("<col=FF0000>You may join the \"Help\" clan group now you've played 24 hours.</col>");
				}
			}

		}, 100);
		// sendMessage("Christmas even has arrived! To start it talk to Diago in Draynor");
		// sendMessage("@blu@Frost Dragons have now been added in brimhaven dungeon!");
		// sendMessage("@blu@Easter shop has been added at home for 1 day only!");
		// sendMessage("@red@Vote system will be online shortly.");
		// sendMessage("@red@If your login with a black screen relog or teleport home.");
		// sendMessage("@red@If your login with a black screen fro.");old src
		// sendMessage("@gre@Server might be dc'ing commonly just today due to fixing updates/more!");
		if (rights == 0 && isMember > 0) {
			// sendMessage("Hello, "+username+"! Thanks for supporting us with your donation!");
		}
		this.getPA().showOption(4, 0, "Trade With");
		this.getPA().showOption(5, 0, "Follow");
		this.getItems().resetItems(3214);
		this.getItems().sendWeapon(equipment[playerWeapon],
				this.getItems().getItemName(equipment[playerWeapon]));
		this.getItems().resetBonus();
		this.getItems().getBonus();
		this.getItems().writeBonus();
		getPA().sendFrame126("Total Level: " + getPA().getTotalLevel(), 3984);
		this.getItems().setEquipment(equipment[playerHat], 1, playerHat);
		this.getItems().setEquipment(equipment[playerCape], 1, playerCape);
		this.getItems().setEquipment(equipment[playerAmulet], 1, playerAmulet);
		this.getItems().setEquipment(equipment[playerArrows], playerEquipmentN[playerArrows], playerArrows);
		this.getItems().setEquipment(equipment[playerChest], 1, playerChest);
		this.getItems().setEquipment(equipment[playerShield], 1, playerShield);
		this.getItems().setEquipment(equipment[playerLegs], 1, playerLegs);
		this.getItems().setEquipment(equipment[playerHands], 1, playerHands);
		this.getItems().setEquipment(equipment[playerFeet], 1, playerFeet);
		this.getItems().setEquipment(equipment[playerRing], 1, playerRing);
		this.getItems().setEquipment(equipment[playerWeapon], playerEquipmentN[playerWeapon], playerWeapon);
		Animations.setMovementAnimationIds(this);
		chat.getPrivateMessaging().updateStatus(PrivateMessaging.Status.CONNECTED);
		chat.initialize();
		this.getItems().addSpecialBar(equipment[playerWeapon]);
		saveTimer = Config.SAVE_TIMER;

		System.out.println("[REGISTERED]: " + username + "");

		Server.lottery.checkUnclaimedWinners(this);
		// Server.clanChat.clearClanChat(this);
		// Server.clanChat.updateClanText(this);
		this.getPA().resetFollow();
		if (voteReset <= 0) {
			voted = 0;
			voteReset += 10;
		}

		getPA().sendFrame36(287, getData().splitChat ? 1 : 0);
		getPA().sendFrame36(166, getData().brightness);
		if (autoRet == 1) {
			this.getPA().sendFrame36(172, 1);
		} else {
			this.getPA().sendFrame36(172, 0);
		}
		this.gfx0(1844);
		this.getPA().setClanData();
		if (getData().lastClanChat != null && !getData().lastClanChat.isEmpty()) {
			Clan clan = Server.clanManager.getClan(getData().lastClanChat);
			if (clan != null) {
				clan.addMember(this);
			} else {
				Clan.addMember("help", this);
			}
		}
		// BountyHunter.login(this);
		// Achievements.showInterface(this);Stop moving plox
		myDamnTimer += 240 + Misc.random(600);

		ServerEvents.onPlayerLoggedIn(this);
		Scripting.handleMessage(this, new LoginMessage());

		update();
	}

	public void update() {
		new PlayerUpdateTask(this).execute();
		PlayerHandler.updateNPC(this, outStream);
		flushOutStream();
	}

	public boolean inJail() {
		return absX > 3018 && absX < 3036 && absY > 3500 && absY < 3521;
	}

	public boolean inKbd() {
		return absX == 2271 && absY == 4680;
	}

	public boolean inKQ() {
		return absX > 3455 && absX < 3526 && absY > 9474 && absY < 9535;
	}

	public boolean inLobby() {
		return absX >= 2605 && absX <= 2612 && absY >= 3145 && absY <= 3151;
	}

	public boolean inManHuntedLobby() {
		return absX > 2436 && absX < 2449 && absY > 4951 && absY < 4961;
	}

	public boolean inManHunterLobby() {
		return absX > 2446 && absX < 2458 && absY > 4933 && absY < 4945;
	}

	public boolean inMageArena() {
		return absX >= 3084 && absX <= 3126 && absY >= 3914 && absY <= 3952;
	}

	public boolean inPcBoat() {
		return absX >= 2660 && absX <= 2663 && absY >= 2638 && absY <= 2643;
	}

	public boolean inPcGame() {
		return absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619;
	}

	public boolean inPits() {
		return this.Area(2370, 2430, 5122, 5168);
	}

	public boolean inPitsWait() {
		return this.Area(2394, 2404, 5169, 5175);
	}

	public boolean inSeaTroll() {
		return absX > 2493 && absX < 2564 && absY > 4544 && absY < 4601;
	}

	public boolean insideGodwars() {
		return absX >= 2816 && absX < 2944 && absY >= 5250 && absY < 5376;
	}

	boolean insideWarriors() {
		return absX >= 2838 && absY >= 3534 && absX <= 2875 && absY <= 3555;
	}

	boolean insideWarriorsCyclops() {
		if (heightLevel != 2) {
			return false;
		}
		if (absX == 2847 && absY == 3537) {
			return false;
		}
		if (absX <= 2876 && absY <= 3556) {
			if (absX >= 2847 && absY >= 3534) {
				return true;
			}
			if (absX >= 2838 && absY >= 3543) {
				return true;
			}
		}
		return false;
	}

	public boolean inTrinityWar() {
		return absX > 26823 && absX < 27430 && absY > 94939 && absY < 93541;
	}

	public boolean inVote() {
		return absX > 3135 && absX < 3201 && absY > 9736 && absY < 9782;
	}

	public boolean inWild() {
		if (ClanWars.inArena(this) && !ClanWars.inJail(this)) {
			return true;
		}
		if (ClanWars.inLobbyArea(this)) {
			return false;
		}
		if (absX > 2941 && absX < 3392 && absY > 3521 && absY < 3966 || absX > 2252 && absX < 2291 && absY > 4675
				&& absY < 4716 ||
				/* absX > 2359 && absX < 2434 && absY > 3067 && absY < 3146 || */
				absX > 2128 && absX < 2173 && absY > 5084 && absY < 5111 || absX > 3134 && absX < 3396 && absY > 3779
				&& absY < 3984 ||
				/*
				 * this.inFunPk() || //hold a sec please go to wild inagem and show me one safe zone within wild?
				 * this.wealthPk() || //inTrinityWar() ||
				 */
				absX > 2941 && absX < 3392 && absY > 9918 && absY < 10366) { // rev
			return true;
		}
		return false;
	}

	public boolean isAppearanceUpdateRequired() {
		return appearanceUpdateRequired;
	}

	public void setAppearanceUpdateRequired(boolean appearanceUpdateRequired) {
		this.appearanceUpdateRequired = appearanceUpdateRequired;
	}

	public boolean isChatTextUpdateRequired() {
		return chatTextUpdateRequired;
	}

	public void setChatTextUpdateRequired(boolean chatTextUpdateRequired) {
		this.chatTextUpdateRequired = chatTextUpdateRequired;
	}

	/**
	 * Get if the player is doing the tutorial or not
	 *
	 * @return is the player doing tutorial
	 */
	public boolean isDoingTutorial() {
		return doingTutorial;
	}

	/**
	 * Set if the player is doing the tutorial
	 *
	 * @param doingTutorial
	 */
	public void setDoingTutorial(boolean doingTutorial) {
		this.doingTutorial = doingTutorial;
	}

	/**
	 * Get if the the player is hidden from other players view
	 *
	 * @return is this player hidden
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * Hides a player from other players view
	 *
	 * @param hidden hidden or not
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean isHitUpdateRequired() {
		return hitUpdateRequired;
	}

	public void setHitUpdateRequired(boolean hitUpdateRequired) {
		this.hitUpdateRequired = hitUpdateRequired;
	}

	public boolean isMember() {
		return isMember > 0;
	}

	/*
	 * public boolean readDonation() { try { MySQLResultCallable query = new MySQLResultCallable(0,
	 * "SELECT * FROM payments WHERE player=" + username); Future<ResultSet> future =
	 * Server.connection.getWorker().submit(query); ResultSet rs = future.get(); while (rs.next()) { if
	 * (rs.getInt("given") == 0) { try { int pId = rs.getInt("productid"); int amount = rs.getInt("amount"); String
	 * method = rs.getString("method"); String tranType = rs.getString("transactiontype"); String address =
	 * rs.getString("remoteaddress"); String pName = rs.getString("productname"); // Process Items switch (pId) { case
	 * 869762:// Blue phat this.getItems().addItem(1042, 1); break; case 869822:// White phat
	 * this.getItems().addItem(1048, 1); break; case 869802:// Red phat this.getItems().addItem(1038, 1); break; case
	 * 869790:// Green phat this.getItems().addItem(1044, 1); break; case 869824:// Yellow phat
	 * this.getItems().addItem(1040, 1); break; case 869798:// Purple phat this.getItems().addItem(1046, 1); break; case
	 * 869800:// Red hween mask this.getItems().addItem(1057, 1); break; case 869760:// Blue hween mask
	 * this.getItems().addItem(1055, 1); break; case 869788:// Green hween mask this.getItems().addItem(1053, 1); break;
	 * case 869754:// Bandos Godsword this.getItems().addItem(11696, 1); break; case 869748:// Armadyl Godsword
	 * this.getItems().addItem(11694, 1); break; case 869806:// Sara Godsword this.getItems().addItem(11698, 1); break;
	 * case 869826:// Zammy Godsword this.getItems().addItem(11700, 1); break; case 869780:// Dragon Claws
	 * this.getItems().addItem(14484, 1); break; case 869810:// Staff of light this.getItems().addItem(15001, 1); break;
	 * case 869792:// Handcannon this.getItems().addItem(15241, 1); break; case 869778:// Divine spirit shield
	 * this.getItems().addItem(13740, 1); break; case 869782:// Dragon Def this.getItems().addItem(20072, 1); break;
	 * case 869746:// 7 Mill EXP this.getItems().addItem(2528, 1); break; case 869776:// Dark bow
	 * this.getItems().addItem(11235, 1); break; case 869758:// Berk ring(i) this.getItems().addItem(15220, 1); break;
	 * case 869784:// Dragon fire shield this.getItems().addItem(11283, 1); break; case 869794:// Korasi sword
	 * this.getItems().addItem(19780, 1); break; case 869756:// Barrelchest anchor this.getItems().addItem(10887, 1);
	 * break; case 869766:// Chaotic Longsword this.getItems().addItem(15038, 1); break; case 869770:// Chaotic rapier
	 * this.getItems().addItem(15037, 1); break; case 869768:// Chaotic maul this.getItems().addItem(15039, 1); break;
	 * case 869764:// Chaotic xbow this.getItems().addItem(15041, 1); break; case 869772:// Chaotic Staff
	 * this.getItems().addItem(15040, 1); break; case 869786:// Dragon pl8 body this.getItems().addItem(19342, 1);
	 * break; case 869816:// Vesta Set this.getItems().addItem(13887, 1); this.getItems().addItem(13893, 1);
	 * this.getItems().addItem(13899, 1); this.getItems().addItem(13905, 1); break; case 869812:// Statius Set
	 * this.getItems().addItem(13890, 1); this.getItems().addItem(13884, 1); this.getItems().addItem(13896, 1);
	 * this.getItems().addItem(13902, 1); break; case 869752:// Bandos Armour Set this.getItems().addItem(11724, 1);
	 * this.getItems().addItem(11726, 1); this.getItems().addItem(11728, 1); break; case 869814:// Torva Armour Set
	 * this.getItems().addItem(20135, 1); this.getItems().addItem(20139, 1); this.getItems().addItem(20143, 1); break;
	 * case 869820:// Virtus Set this.getItems().addItem(20159, 1); this.getItems().addItem(20163, 1);
	 * this.getItems().addItem(20167, 1); break; case 869750:// Armadyl Set this.getItems().addItem(11718, 1);
	 * this.getItems().addItem(11720, 1); this.getItems().addItem(11722, 1); break; case 869804:// Santa Hat
	 * this.getItems().addItem(1050, 1); break; case 869796:// Pernix Range Set this.getItems().addItem(20151, 1);
	 * this.getItems().addItem(20147, 1); this.getItems().addItem(20155, 1); break; } // End rs.updateInt("given", 1);
	 * isMember = 1; } catch (Exception e) { } return true; } } rs.close(); return true; } catch (Exception e) { return
	 * false; } }
	 */
	public boolean isStaffOverAdmin() {
		return rights >= 2 || username.equalsIgnoreCase("saint");
	}

	public boolean leavePacketsQueued() {
		return leavePacketsQueued;
	}

	public void logout() {

		if (getDuel() != null) {
			sendMessage("You cannot log out in a duel.");
			return;
		}
		if (System.currentTimeMillis() - logoutDelay > 10000) {
			if (clan != null) {
				clan.removeMember(this);
			}
			outStream.createFrame(109);
			//setDisconnected(true, "using logout button");
			System.out.println(this + " disconnected using logout button");
			properLogout = true;
		} else {
			this.sendMessage("You must wait a few seconds from being out of combat to logout.");
		}
	}

	public void println(String str) {
		System.out.println("[player-" + playerId + "]: " + str);
	}

	public void println_debug(String str) {
		System.out.println("[player-" + playerId + "]: " + str);
	}

	public void process() {

		//if(inWild() && GreeGree.isGreeGree(this)) {
			//getPA().movePlayer(3222, 3222, 0);
		//	sendMessage("You cannot use Gree gree in the wilderness.");
		//}

		/*if (Areas.inRespectedArea(getLocation()) && !isRespectedDonor()) {
			getPA().movePlayer(3222, 3222, 0);
		}
		if(rights == 0) {
			if (Areas.inExtremeArea(getLocation()) && !isExtremeDonor()) {
				getPA().movePlayer(3222, 3222, 0);
			}
		}*/

		if(getData().doubleXpTime > 0) {
			getData().doubleXpTime--;
		}

		if (bot) {
			forcedChat("HELLO 2442494843948348348384398439843984398439849");
		}

		// reset dual requests after 30 seconds or when they leave the area
		if(getDuel() != null && getDuel().getStage() == DuelArena.Stage.REQUESTING && (System.currentTimeMillis() - lastDuelRequest > 30_000 || !inDuelArena())) {
			setDuel(null);
		}

		if (getData().votePerk != null) {
			getData().votePerk.incrementTicks();
			if (getData().votePerk.getTicks() >= getData().votePerk.getTicksToExpire()) {
				sendMessage("Your account perk has expired.");
				getData().votePerk = null;
				MissCheevers.sendPerk(this);
			}
		}

		// move the player back to middle if on top of a zulrah pillar
		if (absX == 2265 && absY == 3071 || absX == 2271 && absY == 3071) {
			getPA().movePlayer(2268, 3070);
		}

		// save every 15 minutes
		if (System.currentTimeMillis() - lastSaved > 15 * 60 * 1000) {
			PlayerSave.saveGame(new SaveRequest(this, false));
			lastSaved = System.currentTimeMillis();

			System.out.println("saving " + username + " 15 minute");
		}

		// needed to show the correct combat level when entering and leaving wilderness
		boolean isInWild = inWild();
		if (inWild && !isInWild || !inWild && isInWild) {
			inWild = isInWild;
			appearanceUpdateRequired = true;
			updateRequired = true;
		}

		if (getData().killstreak > 0 && !inWild()) {
			getData().killstreak = 0;
		}

		PlayerKill.process(this);

		if (antiFirePot > 0) {
			antiFirePot--;

			if (antiFirePot == 30) {
				sendMessage("@red@Your anti-fire potion effect is about to wear off!");
			}

			if (antiFirePot == 0) {
				sendMessage("@red@Your anti-fire potion effect has worn off!");
			}
		}

		if (instancesArea != null && teleTimer == 0 && !instancesArea.getArea().inArea(getLocation())) {
			instancesArea.leave(true);
			instancesArea = null;
		}

		if (getData().energy < 100 && !attributeExists("resting")) {
			if (System.currentTimeMillis() - lastEnergyIncrease > (5000 - (level[PlayerConstants.AGILITY] * 40))) {
				lastEnergyIncrease = System.currentTimeMillis();
				getData().energy = getData().energy + 1;
				getPA().sendEnergy();
			}
		}

		if (!this.inPitsWait() && wasInPits) {
			this.getPA().walkableInterface(-1);
		}

		ServerEvents.onPlayerProcess(this);

		if (isFiremakingWalk()) {
			Firemaking.clippedFiremaking(this);
		}

		/**
		 * Walking to a challenged duel request
		 */
		if (duelRequestId > -1) {

			Player other = PlayerHandler.players[duelRequestId];
			if (other == null) {
				duelRequestId = -1;
				followId = -1;
			}

			if (getLocation().isWithinInteractionDistance(other.getLocation())) {
				DuelArena.requestDuel(this, duelRequestId);
				duelRequestId = -1;
				followId = -1;
			} else {
				followId = duelRequestId;
			}

		}
		/***
		 * Walking to a challenges gamble request
		 */
		if(gambleRequestId >= 0) {
			Player other = PlayerHandler.players[gambleRequestId];
			if(other == null) {
				gambleRequestId = -1;
				//followId = -1;
			} else {
				if(getLocation().isWithinInteractionDistance(other.getLocation())) {
					Gambling.request(this, other);
					gambleRequestId = -1;
					//followId = -1;
				} else {
					//followId = gambleRequestId;
				}
			}
		}
		if (SolProtect > 0) {
			if (equipment[playerWeapon] != 15486) {
				this.sendMessage("You are no longer protected as you unequipped Staff of light.");
				SolProtect = 0;
				return;
			}
			SolProtect--;
			if (SolProtect == 1) {
				this.sendMessage("Your lightness protection slowly leaves your soul...");
			}
		}
		if (inTrade && tradeResetNeeded) {
			Player o = PlayerHandler.players[tradeWith];
			if (o != null) {
				if (o.tradeResetNeeded) {
					this.getTradeAndDuel().resetTrade();
					o.getTradeAndDuel().resetTrade();
				}
			}
		}
		if (tehTimer > 0) {
			tehTimer--;
		} else if (tehTimer == -1 && killed) {
			this.getPA().applyDead();
		}
		if (helpTimer > 0) {
			helpTimer--;
		}
		if (helpClear > 0) {
			helpClear--;
		} else if (helpClear <= 0) {
			this.getPA().playerNeedsHelp.clear();
			helpClear += 1200;
		}
		if (actionTimer > 0) {
			actionTimer--;
		}
		if (gwdAltarTimer > 0) {
			gwdAltarTimer--;
		}
		if (bowDelay > 0) {
			bowDelay--;
		}
		if (jailTimer > 0) {
			jailTimer--;


			if(!Areas.inJail(this)) {
				getPA().movePlayer(2602, 4775, 0);
			}


		}
		if (!inWild() && System.currentTimeMillis() - teleBlockDelay < teleBlockLength) {
			teleBlockDelay = 0;
			teleBlockLength = 0;
		}	
		if (tradeTimer > 0) {
			tradeTimer--;
		}
		if (this.getAwaitingUpdate()) {
			this.getItems().resetItems(3214);
		}
		if (followId > 0 || followId2 > 0) {
			this.getPA().follow();
		}
		this.getCombat().handlePrayerDrain();
		if (System.currentTimeMillis() - singleCombatDelay > 3300) {
			underAttackBy = 0;
		}
		if (System.currentTimeMillis() - singleCombatDelay2 > 3300) {
			underAttackByNpcID = 0;
		}

		/*
		 * if (System.currentTimeMillis() - teleGrabDelay > 1550 && usingMagic) { usingMagic = false; if
		 * (Server.itemHandler.itemExists(teleGrabItem, teleGrabX, teleGrabY)) {
		 * Server.itemHandler.removeGroundItem(this, teleGrabItem, teleGrabX, teleGrabY, true); } }
		 */
		// bounty.tick();
		if (this.isDoingTutorial()) {
			this.getPA().sendTutorialIslandInterface(this.getTutorialProgress());
		}

		if (ClanWars.inArena(this) || ClanWars.inJail(this)) {
			if (ClanWars.inJail(this)) {
				getPA().showOption(3, 0, "Null");
			} else {
				getPA().showOption(3, 0, "Attack");
			}
			if (!ClanWars.inWhitePortalArea(this)) {
				ClanWars.sendInterface(this);
			}
		} else if (this.inWild()) {
			int modY = absY > 6400 ? absY - 6400 : absY;
			wildLevel = (((modY - 3520) / 8) + 1);
			this.getPA().walkableInterface(197);
			this.getPA().showOption(3, 0, "Attack");
			this.getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
		} else if (ClanWars.inLobbyArea(this)) {
			if (clan != null && clan.isFounder(username)) {
				getPA().showOption(3, 0, "Challenge");
			}
			getPA().walkableInterface(-1);
		} else if (this.inFightCaves()) {
			this.getPA().walkableInterface(21400);
			this.getPA().sendFrame126("@yel@Wave: " + ((getData().fightKilnWave > -1 ? getData().fightKilnWave : getData().fightCaveWave) + 1), 21401);
		} else if (inDuelArena()) {
			getPA().walkableInterface(201);
			if (duel != null && duel.getStage() == DuelArena.Stage.FIGHTING) {
				getPA().showOption(3, 0, "Attack");
			} else {
				getPA().showOption(3, 0, "Challenge");
			}
		} else if(Gambling.inArea(this) && getLocation().getZ() == 0) {
			getPA().showOption(3, 0, "Challenge");
		} else if (this.insideGodwars()) {
			this.getPA().walkableInterface(16210);
			this.getPA().sendFrame126("" + zamorakKills, 16219);
			this.getPA().sendFrame126("" + saradominKills, 16218);
			this.getPA().sendFrame126("" + bandosKills, 16217);
			this.getPA().sendFrame126("" + armadylKills, 16216);
		} else if (this.insideWarriors()) {
			this.getPA().walkableInterface(21400);
			//this.getPA().sendFrame126("Tokens: " + animationTokens, 21401);
			// this.getPA().sendFrame126("", 21403); //minigames
		} else if (this.inPcBoat()) {
			this.getPA().walkableInterface(21119);
		} else if (this.inPcGame()) {
			this.getPA().walkableInterface(21100);
		} else if (this.inPits()) {
			this.getPA().showOption(3, 0, "Attack");
			FightPits.updateInterfaces(this);
		} else if (this.inBarrows()) {
			this.getPA().sendFrame99(2);
			this.getPA().sendFrame126("Kill Count: " + barrowsKillCount, 4536);
			this.getPA().walkableInterface(4535);
		} else if (this.inPitsWait() || this.inPits()) {
			FightPits.updateInterfaces(this);
		} else if (inCwGame || this.inTrinityWar()) {
			this.getPA().showOption(3, 0, "Attack");
		} else if (inCastleWars()) {
			CastleWars.sendInterface(this);
		} else if (Zombies.inGameArea(getLocation()) || Zombies.inLobbyArea(getLocation())) {
			Zombies.sendInterface(this);
		} else {
			this.getPA().showOption(3, 0, "Null");
			getPA().walkableInterface(-1);
			getPA().sendFrame99(0);
		}

		if (!hasMultiSign && Areas.inMulti(this)) {
			hasMultiSign = true;
			this.getPA().multiWay(1);
		}
		if (hasMultiSign && !Areas.inMulti(this)) {
			hasMultiSign = false;
			this.getPA().multiWay(-1);
		}
		if (isDead && respawnTimer == -6) {

			this.getPA().applyDead();

			// note(stuart) i can't imagine what this is for but commented it just in case
           /* if (!this.inFightCaves()) {
                this.getPA().applyDead();
            } else {
                isDead = false;
                level[3] = getPA().getLevelForXP(xp[3]);
                this.getPA().requestUpdates();
                for (int i = 0; i < level.length; i++) {
                    level[i] = getPA().getLevelForXP(xp[i]);
                    this.getPA().refreshSkill(i);
                }
            }*/
		}
		if (respawnTimer == 7) {
			respawnTimer = -6;
			this.getPA().giveLife();
		} else if (respawnTimer == 12) {
			respawnTimer--;
			this.startAnimation(0x900);
			poisonDamage = -1;
		}
		if (respawnTimer > -6) {
			respawnTimer--;
		}
		if (clawDelay > 0) {
			clawDelay--;
		}
		if (clawDelay == 1) {
			delayedDamage = clawDamage / 4;
			delayedDamage2 = (clawDamage / 4) + 1;
			if (clawType == 2) {
				this.getCombat().applyNpcMeleeDamage(clawIndex, 1, clawDamage / 4);
			}
			if (clawType == 1) {
				this.getCombat().applyPlayerMeleeDamage(clawIndex, 1, clawDamage / 4);
			}
			if (clawType == 2) {
				this.getCombat().applyNpcMeleeDamage(clawIndex, 2, (clawDamage / 4) + 1);
			}
			if (clawType == 1) {
				this.getCombat().applyPlayerMeleeDamage(clawIndex, 2, (clawDamage / 4) + 1);
			}
			clawDelay = 0;
			specEffect = 0;
			previousDamage = 0;
			usingClaws = false;
			clawType = 0;
		}
		if (freezeTimer > -6) {
			freezeTimer--;
			if (frozenBy > 0) {
				if (PlayerHandler.players[frozenBy] == null) {
					freezeTimer = -1;
					frozenBy = -1;
				} else if (!this.goodDistance(absX, absY, PlayerHandler.players[frozenBy].absX,
						PlayerHandler.players[frozenBy].absY, 20)) {
					freezeTimer = -1;
					frozenBy = -1;
				}
			}
		}

		if (hitDelay > 0) {
			hitDelay--;
		}
		if (hitDelay == 1) {
			if (oldNpcIndex > 0) {
				this.getCombat().delayedHit(oldNpcIndex);
			}
			if (oldPlayerIndex > 0) {
				this.getCombat().playerDelayedHit(oldPlayerIndex);
			}
		}

		if (attackTimer > 0) {
			attackTimer--;
		}
		if (attackTimer == 1) {
			if (npcIndex > 0 && clickNpcType == 0) {
				this.getCombat().attackNpc(npcIndex);
			}
			if (playerIndex > 0) {
				this.getCombat().attackPlayer(playerIndex);
			}
		} else if (attackTimer <= 0 && (npcIndex > 0 || playerIndex > 0)) {
			if (npcIndex > 0) {
				attackTimer = 0;
				this.getCombat().attackNpc(npcIndex);
			} else if (playerIndex > 0) {
				attackTimer = 0;
				this.getCombat().attackPlayer(playerIndex);
			}
		}

		if (clickObjectType > 0 && Math.floor(getLocation().getDistance(Location.create(objectX, objectY))) <= objectDistance) {
			if (clickObjectType == 1) {
				this.getActions().firstClickObject(objectId, objectX, objectY);
			}
			if (clickObjectType == 2) {
				this.getActions().secondClickObject(objectId, objectX, objectY);
			}
			if (clickObjectType == 3) {
				this.getActions().thirdClickObject(objectId, objectX, objectY);
			}
		}
		/*
		 * new if(clickObjectType > 0 && (goodDistance(objectX + objectXOffset, objectY + objectYOffset, getX(), getY(),
		 * objectDistance) || goodDistance(objectX + objectXOffset, objectY, getX(), getY(), objectDistance) ||
		 * goodDistance(objectX, objectY + objectYOffset, getX(), getY(), objectDistance) || goodDistance(objectX,
		 * objectY, getX(), getY(), objectDistance))) { if(clickObjectType == 1) {
		 * getActions().firstClickObject(objectId, objectX, objectY); getPA().resetAction(); } if(clickObjectType == 2)
		 * { getActions().secondClickObject(objectId, objectX, objectY); getPA().resetAction(); } if(clickObjectType ==
		 * 3) { getActions().thirdClickObject(objectId, objectX, objectY); getPA().resetAction(); } }
		 */
		if ((clickNpcType > 0) && NPCHandler.npcs[npcClickIndex] != null) {

			int distance = 1;
			NPC npc = NPCHandler.npcs[npcClickIndex];

			if (Hunter.isImp(npc)) {
				distance = 2;
			}


			if (this.goodDistance(this.getX(), this.getY(), NPCHandler.npcs[npcClickIndex].getX(),
					NPCHandler.npcs[npcClickIndex].getY(), distance)) {
				if (clickNpcType == 1) {
					this.turnPlayerTo(NPCHandler.npcs[npcClickIndex].getX(), NPCHandler.npcs[npcClickIndex].getY());
					NPCHandler.npcs[npcClickIndex].facePlayer(playerId);
					this.getActions().firstClickNpc(npcType);
				}
				if (clickNpcType == 2) {
					this.turnPlayerTo(NPCHandler.npcs[npcClickIndex].getX(), NPCHandler.npcs[npcClickIndex].getY());
					NPCHandler.npcs[npcClickIndex].facePlayer(playerId);
					this.getActions().secondClickNpc(npcType);
				}
				if (clickNpcType == 3) {
					this.turnPlayerTo(NPCHandler.npcs[npcClickIndex].getX(), NPCHandler.npcs[npcClickIndex].getY());
					NPCHandler.npcs[npcClickIndex].facePlayer(playerId);
					this.getActions().thirdClickNpc(npcType);
				}
				if (clickNpcType == 4) {
					this.turnPlayerTo(NPCHandler.npcs[npcClickIndex].getX(), NPCHandler.npcs[npcClickIndex].getY());
					NPCHandler.npcs[npcClickIndex].facePlayer(playerId);
					this.getActions().fourthClickNpc(npcType);
				}
			}
		}
		if (skullTimer > 0) {
			skullTimer--;
			if (skullTimer == 1) {
				isSkulled = false;
				attackedPlayers.clear();
				headIconPk = -1;
				skullTimer = -1;
				this.getPA().requestUpdates();
			}
		}
		if (System.currentTimeMillis() - restoreStatsDelay > 60000) {
			restoreStatsDelay = System.currentTimeMillis();
			for (int level = 0; level < this.level.length; level++) {
				if (this.level[level] < getPA().getLevelForXP(xp[level])) {
					if (level != 5 && level != PlayerConstants.SUMMONING) { // prayer and summoning doesn't replenish
						// over time
						this.level[level] += 1;
						this.getPA().refreshSkill(level);
					}
				} else if (this.level[level] > getPA().getLevelForXP(xp[level]) + (level == PlayerConstants.HITPOINTS ? Food.getHpBoost(this) : 0)) {
					this.level[level] -= 1;
					this.getPA().refreshSkill(level);
				}
			}
		}
		if (System.currentTimeMillis() - this.specDelay > 25000) {
			specDelay = System.currentTimeMillis();
			if (specAmount < 10) {
				specAmount += 1;
				if (specAmount > 10) {
					specAmount = 10;
				}
				this.getItems().addSpecialBar(equipment[playerWeapon]);
			}
		}
		if (System.currentTimeMillis() - lastPoison > 20000 && poisonDamage > 0) {
			int damage = poisonDamage / 2;
			if (damage > 0) {
				this.sendMessage("You are affected by the poison...");
				if (!this.getHitUpdateRequired()) {
					this.setHitUpdateRequired(true);
					this.setHitDiff(damage);
					updateRequired = true;
					hitIcon = 0;
					poisonMask = 1;
				} else if (!this.getHitUpdateRequired2()) {
					this.setHitUpdateRequired2(true);
					this.setHitDiff2(damage);
					updateRequired = true;
					hitIcon = 0;
					poisonMask = 2;
				}
				lastPoison = System.currentTimeMillis();
				poisonDamage--;
				this.dealDamage(damage);
			} else {
				poisonDamage = -1;
				this.sendMessage("You are no longer poisoned.");
			}
		}
	}

	public void processQueuedPackets() throws Exception {
		timeOutCounter++;
		if (!bot && timeOutCounter > Config.TIMEOUT && !disconnected) {
			timedOut = true;
			setDisconnected(true, "timed out");
			return;
		}

		// so we lock for the shortest time, we create a shallow copy of the current queue
		Queue<Packet> packets = new LinkedList<>();

		synchronized (walkingQueuedPackets) {
			packets.addAll(walkingQueuedPackets);
			walkingQueuedPackets.clear();
		}
		synchronized (queuedPackets) {
			packets.addAll(queuedPackets);
			queuedPackets.clear();
		}

		for (Packet packet; (packet = packets.poll()) != null; ) {
			Stream stream = new Stream();
			stream.buffer = packet.getPayload().array();

			try {
				PacketHandler.processPacket(this, packet.getOpcode(), packet.getLength(), stream);
			} catch (Exception e) {
				throw new Exception("error processing packet " + packet.getOpcode());
			}

			timeOutCounter = 0;
		}
	}

	/**
	 * End of Skill Constructors
	 */
	public void queueMessage(Packet arg1) {
		if (arg1.getOpcode() == 98 || arg1.getOpcode() == 164 || arg1.getOpcode() == 248) {
			synchronized (walkingQueuedPackets) {
				walkingQueuedPackets.add(arg1);
			}
		} else {
			synchronized (queuedPackets) {
				queuedPackets.add(arg1);
			}
		}
	}

	public void resetDialogue() {
		setDialogueOwner(null);
		setCurrentDialogueId(-1);
		setNextDialogueId(-1);
		getPA().closeAllWindows();
		removeAttribute("dialogue");
	}

	public boolean isMoving() {
		return getWalkingQueue().isMoving();
	}

	public void resetWalkingQueue() {
		stopMovement();
	}

	public void sendFrame34(int frame, int item, int slot, int amount) {
		outStream.createFrameVarSizeWord(34);
		outStream.writeWord(frame);
		outStream.writeByte(slot);
		outStream.writeWord(item + 1);
		outStream.writeByte(255);
		outStream.writeDWord(amount);
		outStream.endFrameVarSizeWord();
	}


	public void sendMessage(String s) {
		sendMessage(s, false);
	}

	public void sendMessage(String s, boolean wrap) {

		s = PlayerAssistant.replaceColors(s);

		if (s.length() > 86 && wrap) {
			String[] lines = Misc.wrapText(s, 86);
			for (String line : lines) {
				outStream.createFrameVarSize(253);
				outStream.writeString(line);
				outStream.endFrameVarSize();
			}
		} else {
			outStream.createFrameVarSize(253);
			outStream.writeString(s);
			outStream.endFrameVarSize();
		}
		this.flushOutStream();
	}

	public boolean serverOwner() {
		return rights == 3;
	}

	public boolean serverStaff() {
		return rights >= 1 && rights < 4;
	}

	public void setHitMax(int amount) {
		this.hitMax = amount;
	}

	public void setLeavePacketsQueued(boolean leavePacketsQueued) {
		this.leavePacketsQueued = leavePacketsQueued;
	}

	public void setSidebarInterface(int menuId, int form) {
		outStream.createFrame(71);
		outStream.writeWord(form);
		outStream.writeByteA(menuId);
		this.flushOutStream();
	}

	public Location getTeleportLocation() {
		return teleportLocation;
	}

	public void setTeleportLocation(Location l) {
		teleportLocation = l;
	}

	/**
	 * Animations
	 */
	public void startAnimation(int animId) {
		animationRequest = animId;
		animationWaitCycles = 0;
		updateRequired = true;
	}

	public void startAnimation(int animId, int time) {
		animationRequest = animId;
		animationWaitCycles = time;
		updateRequired = true;
	}

	public void stopAnimation() {
		startAnimation(65535);
	}

	public void stopMovement() {
		getWalkingQueue().reset();
	}

	public void turnPlayerTo(int pointX, int pointY) {
		FocusPointX = 2 * pointX + 1;
		FocusPointY = 2 * pointY + 1;
		updateRequired = true;
	}

	/**
	 * wealth pk coords*
	 */
	// 1761 5193
	public boolean wealthPk() {
		return absX >= 1727 && absX <= 1792 && absY >= 5130 && absY <= 5249;
	}

	public boolean wearing2h() {
		Player c = (Player) this;
		String s = c.getItems().getItemName(c.equipment[Player.playerWeapon]);
		if (s.contains("2h")) {
			return true;
		} else if (s.contains("godsword")) {
			return true;
		}
		return false;
	}

	public boolean withinDistance(NPC npc) {
		if (heightLevel != npc.heightLevel) {
			return false;
		}
		if (npc.needRespawn == true) {
			return false;
		}
		int deltaX = npc.absX - absX, deltaY = npc.absY - absY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public boolean withinDistance(Player otherPlr) {
		if (heightLevel != otherPlr.heightLevel) {
			return false;
		}
		int deltaX = otherPlr.absX - absX, deltaY = otherPlr.absY - absY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}

	public boolean isFiremakingWalk() {
		return firemakingWalk;
	}

	public void setFiremakingWalk(boolean firemakingWalk) {
		this.firemakingWalk = firemakingWalk;
	}

	public int getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
	}

	public Dialogue getCurrentDialogue() {
		return currentDialogue;
	}

	public void setCurrentDialogue(Dialogue currentDialogue) {
		this.currentDialogue = currentDialogue;
	}

	@Override
	public boolean isDead() {
		return isDead || level[PlayerConstants.HITPOINTS] <= 0;
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.PLAYER;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(Player.class).add("username", username).add("ip", connectedFrom).add("location", getLocation()).toString();
	}

	public boolean isDisconnected() {
		return disconnected;
	}

	public void setDisconnected(boolean disconnected, String message) {
		System.out.println(this + " disconnected reason: " + message);

		this.disconnected = disconnected;
	}

	public boolean antiFirePot() {
		return antiFirePot > 0;
	}

	public List<Player> getLocalPlayers() {
		return localPlayers;
	}

	public WalkingQueue getWalkingQueue() {
		return walkingQueue;
	}

	public NPC getPet() {
		return pet;
	}

	public void setPet(NPC pet) {
		this.pet = pet;
	}
	
	private int totalDonated;
	
	public int getTotalDonated() {
		return totalDonated;
	}
	
	public void setTotalDonated(int totalDonated) {
		this.totalDonated = totalDonated;
	}
	
	public boolean isDonator() {
		return totalDonated >= 10 && totalDonated <= 49;
	}
	
	public boolean isSuperDonator() {
		return totalDonated >= 50 && totalDonated <= 249;
	}
	
	public boolean isExtremeDonator() {
		return totalDonated >= 250 && totalDonated <= 749;
	}

	public boolean isLegendaryDonator() {
		return totalDonated >= 750 && totalDonated <= 2499;
	}
	
	public boolean isUberDonator() {
		return totalDonated >= 2500;
	}

	private boolean isOverloaded;
	
	public boolean isOverloaded() {
		return isOverloaded;
	}
	
	public void setOverloaded(boolean overloaded) {
		this.isOverloaded = overloaded;
	}

	/**
	 * Gets the tradingPost
	 *
	 * @return the tradingPost
	 */
	public TradingPostManager getTradingPost() {
		return tradingPost;
	}

	public String getUsername() {
		return username;
	}
	
}