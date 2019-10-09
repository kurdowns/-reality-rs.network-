package com.zionscape.server;

/**
 * Config Class
 *
 * @author Lmctruck30
 * @version 1.2
 */
public class Config {

	/**
	 * Items which will be placed into the untradable shop upon death
	 */
	public static final int[][] UNTRADABLE_SHOP_ITEMS = {
			{25637, 6000000}, // Team cape zero
			{25638, 6000000}, // Team cape x
			{25639, 6000000}, // Team cape y
			{8465, 1500000},//kiln
			{6570, 1000000},//firecape
			{8839, 3000000},//void top
			{8840, 2000000},//void bottom
			{11663, 1000000},//void mage helm
			{11664, 1000000},//void helm
			{11665, 1000000},//void helm
			{19712, 1000000},//deflector
			{10551, 1000000},//torso
			{8842, 500000},//void gloves
			{19785, 4000000},//elite top
			{19786, 3000000},//elite bottom
			{7462, 250000},//barrow gloves
			{10548, 250000},//fighter hat
			{8844, 5000},//bronze def
			{8845, 10000},//iron
			{8846, 15000},//steel
			{8847, 17000},//black
			{8848, 25000},//mith
			{8849, 50000},//addy
			{8850, 150000},//rune
			{20072, 500000},//d defender
			{20767, 5000000},//max cape
			{20768, 1500000},//max hood
			{200769, 5000000},//d defender
			{200770, 1500000},//d defender
			{6, 1500000},//cannon
			{8, 1500000},//cannon
			{10, 1500000},//cannon
			{12, 1500000},//cannon
	};

	/**
	 * CRC's which are expected in the login
	 */

	public static boolean antibot = false;

	public static boolean triplePk = false;
	public static final int[] EXPECTED_CRCS = new int[9];
	public static final int[] PROHIBITTED_GE_ITEMS = {995};
	public static final int[] ENABLED_IRON_MAN_SHOP_IDS = {7, 18, 30, 13, 28, 16, 27, 19, 999, 14, 9, 10, 11, 12, 17, 20, 22, 25, 44, 69, 79, 80, 81, 97, 125, 126, 127, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 32};
	// example git commit from zivik
	// example git commit by tyler
	public static final int MAX_ALLOWED_AMOUNT_TO_BUY_FROM_SHOPS = 10000;
	public static final int[] ITEMS_KEPT_ON_DEATH = {20072};
	public static final String CLIENT_VERSION = "0.0.1";
	public static final boolean SERVER_DEBUG = false;// needs to be false for Real world to work
	public static final int MAXITEM_AMOUNT = Integer.MAX_VALUE;
	public static final int MAX_PLAYERS = 1024;


	public static final int ACCOUNTS_PER_IP_ALLOWED = 5;
	public static final int ACCOUNTS_PER_GUID_ALLOWED = 3;

	// sold in any store
	public static final int[] UNTRADABLE_ITEM_IDS = {
			25637, 25638, 25639,
			964,//pk pet
			964, // reaper pet
			25619, 25620, 25621, 25622, 25623, 25624, 25625, 25626, 25627, 25628, 25629, 25630, 25631, 25632, 25633, 25634, 25635, 25636, // new pets
			25096, 25099, // tridents
			8851, // warriors guild tokens
			10933, 10939, 10940, 10941, 20787, 20791, 20790, 20789, 10037, 10035, 10039, 25085, 25086, 25087, 25093, 25092, 25091, 25090, 25089, 25088, // skilling set items
			13727, // stardust
			13394, 13309, 1811,
			25055, 25056, 25057, 25058, 25059, 25060, 25061, 25062, 25063, 25064, 25065, 25066, 25067, 25068, 25069, 25070, 25072, 25073, 25074, 25075, 25076, 25077, 25078, 25079, 25080, 25081, 25082, 25083, 25084,//pets
			25040,
			9813, 9814, 10662, 19785, 19786, 20747, 13462, 13442, 13429, 13458, 13460, 13459, 13444, 13481, 13488, 13445, 13427, 13457, 13456, 13455, 13450, 13451, 13452, 13453, 14486, 19784, 13461, 13405, 13430, 13472, 526, 13478, 13479,
			8465, 6, 8, 10, 12, 2795, 2798, 2800, 3565, 3567, 3569, 3571, 2422, 6822,
			6828, 6834, 6846, 6840, 6855, 6854, 6823, 6829, 6835, 6841, 6847, 6824, 6830, 6836, 6842, 6848, 6825, 6831,
			6837, 6843, 6849, 6850, 6826, 6832, 6838, 6844, 6827, 6833, 6839, 6845, 6851, 6874, 6864, 6870, 6878, 6868,
			6882, 6869, 6852, 6877, 6876, 6875, 6881, 6880, 6879, 6873, 6872, 6871, 16693, 16699, 16701, 16703, 16705,
			17241, 17247, 17249, 17251, 17253, 16671, 16677, 16679, 16681, 16683, 166933, 16413, 16415, 16417, 16419,
			16407, 16943, 16945, 16947, 16949, 16937, 16961, 16963, 16965, 16967, 16385, 18167, 18175, 12007, 12008,
			12009, 12010, 12011, 12012, 12013, 12014, 12015, 12016, 12017, 12018, 12019, 12020, 12021, 12022, 12023,
			12024, 12025, 12026, 12027, 12028, 12029, 12030, 12031, 12032, 12033, 12034, 12035, 12036, 12037, 12038,
			12040, 12041, 12042, 12043, 12044, 12045, 12046, 12047, 12048, 12049, 12050, 12051, 12052, 12053, 12054,
			12055, 12056, 12057, 12058, 12059, 12060, 12061, 12062, 12063, 12064, 12065, 12066, 12067, 12068, 12069,
			12070, 12071, 12072, 12073, 12074, 12075, 12076, 12077, 12078, 12079, 12080, 12081, 12082, 12083, 12084,
			12085, 12086, 12087, 12088, 12089, 12090, 12091, 12092, 12093, 12094, 12095, 12096, 12097, 12098, 12099,
			12100, 12101, 12102, 12103, 12104, 12105, 12106, 12107, 12108, 18017, 18018, 18019, 18020, 20072, 6335,
			6336, 6337, 6338, 6339, 6340, 3842, 3844, 3840, 8844, 8845, 8846, 8847, 8848, 8849, 8850, 10551, 6570,
			7462, 7461, 7460, 7459, 7458, 7457, 7456, 7455, 7454, 8839, 8840, 8842, 11663, 11664, 11665, 10499, 9748,
			9754, 9751, 9769, 9757, 9760, 9763, 9802, 9808, 9784, 9799, 9805, 9781, 9796, 9793, 9775, 9772, 9778, 9787,
			9811, 9766, 9749, 9755, 9752, 9770, 9758, 9761, 9764, 9803, 9809, 9785, 9800, 9806, 9782, 9797, 9794, 9776,
			9773, 9779, 9788, 9812, 9767, 9747, 9753, 9750, 9768, 9756, 9759, 9762, 9801, 9807, 9783, 9798, 9804, 9780,
			9795, 9792, 9774, 9771, 9777, 9786, 9810, 9765, 15441, 15442, 15443, 15444, 6465, 2566, 15021, 15022,
			15023, 15024, 15025, 15026, 13673, 13674, 13675, 13676, 18509, 18508, 18510, 13901, 13895, 13889, 13892,
			13886, 13898, 13904, 13263, 15492,
			13462, 13442, 13429, 13458, 13460, 13459, 13444, 13481, 13488, 13445, 13427, 13457, 13456, 13455, 13450, 13451, 13452, 13453, 14486, 19784, 13461, 13405, 13430, 13472, 526, 13478, 13479 //zombie points
	}; // what items can't be traded or staked



	public static final int[] UNDROPPABLE_ITEMS = {
			13462, 13442, 13429, 13458, 13460, 13459, 13444, 13481, 13488, 13445, 13427, 13457, 13456, 13455, 13450, 13451, 13452, 13453, 14486, 19784, 13461, 13405, 13430, 13472, 526, 13478, 13479,
			8465, 2795, 2798, 2800, 3565, 3567, 3569, 6529, 6335, 6336, 6337, 6338,
			6339, 6340, 15442, 15443, 15441, 15444, 6465, 15021, 15022, 15023, 15024, 15025, 15026, 13672, 13673,
			13674, 13675, 13676, 13263, 15492, 18508, 18509, 18510}; // what items can't be dropped
	public static final int[] FUN_WEAPONS = {2460, 2461, 2462, 2463, 2464, 2465, 2466, 2467, 2468, 2469, 2470, 2471,
			2471, 2473, 2474, 2475, 2476, 2477}; // fun weapons for dueling
	public static final boolean ADMIN_CAN_TRADE = false; // can admins trade?
	public static final boolean ADMIN_CAN_SELL_ITEMS = false; // can admins sell items?
	public static final int START_LOCATION_X = 3086; // start here//3277
	public static final int START_LOCATION_Y = 3502;// 3167
	public static final int RESPAWN_X = 3086; // when dead respawn here
	public static final int RESPAWN_Y = 3502;
	public static final int DUELING_RESPAWN_X = 3361; // when dead in duel area spawn here
	public static final int DUELING_RESPAWN_Y = 3273;
	public static final int RANDOM_DUELING_RESPAWN = 5; // random coords
	public static final int NO_TELEPORT_WILD_LEVEL = 20; // level you can't tele on and above
	public static final int SKULL_TIMER = 1200; // how long does the skull last? seconds x 2
	public static final int TELEBLOCK_DELAY = 600; // how long does teleblock last for.
	public static final boolean SINGLE_AND_MULTI_ZONES = true; // multi and single zones?
	public static final boolean COMBAT_LEVEL_DIFFERENCE = true; // wildy levels and combat level differences matters
	public static final boolean itemRequirements = true; // attack, def, str, range or magic levels required to wield
	// weapons or wear items?

	public static final int SKILL_EXP_RATE = 15;
	public static final int COMBAT_EXP_RATE = 300;

	public static final int INCREASE_SPECIAL_AMOUNT = 17500; // how fast your special bar refills
	public static final int DOUBLE_SPECIAL_AMOUNT = 8750;
	public static final boolean PRAYER_POINTS_REQUIRED = true; // you need prayer points to use prayer
	public static final boolean PRAYER_LEVEL_REQUIRED = true; // need prayer level to use different prayers
	public static final boolean MAGIC_LEVEL_REQUIRED = true; // need magic level to cast spell
	public static final int GOD_SPELL_CHARGE = 300000; // how long does god spell charge last?
	public static final boolean RUNES_REQUIRED = true; // magic rune required?
	public static final boolean CORRECT_ARROWS = true; // correct arrows for bows?
	public static final boolean CRYSTAL_BOW_DEGRADES = true; // magic rune required?
	public static final int SAVE_TIMER = 120; // save every 1 minute
	public static final int NPC_RANDOM_WALK_DISTANCE = 3; // the square created , 3x3 so npc can't move out of that box
	// when randomly walking
	public static final int NPC_FOLLOW_DISTANCE = 13; // how far can the npc follow you from it's spawn point,
	public static final int[] UNDEAD_NPCS = {6138, 90, 91, 92, 93, 94, 103, 104, 73, 74, 75, 76, 77}; // undead npcs
	/**
	 * Glory
	 */
	public static final int EDGEVILLE_X = 3087;
	public static final int EDGEVILLE_Y = 3500;
	public static final String EDGEVILLE = "";
	/**
	 * Barrows Reward
	 */
	public static final int AL_KHARID_X = 3293;
	public static final int AL_KHARID_Y = 3174;
	public static final String AL_KHARID = "";
	public static final int KARAMJA_X = 3087;
	public static final int KARAMJA_Y = 3500;
	public static final String KARAMJA = "";
	public static final int MAGEBANK_X = 2538;
	public static final int MAGEBANK_Y = 4716;
	public static final String MAGEBANK = "";
	/**
	 * Teleport Spells
	 */
	// modern
	public static final int VARROCK_X = 3210;
	public static final int VARROCK_Y = 3424;
	public static final String VARROCK = "";
	public static final int LUMBY_X = 3222;
	public static final int LUMBY_Y = 3218;
	public static final String LUMBY = "";
	public static final int FALADOR_X = 2964;
	public static final int FALADOR_Y = 3378;
	public static final String FALADOR = "";
	public static final int CAMELOT_X = 2757;
	public static final int CAMELOT_Y = 3477;
	public static final String CAMELOT = "";
	public static final int ARDOUGNE_X = 2662;
	public static final int ARDOUGNE_Y = 3305;
	public static final String ARDOUGNE = "";
	public static final int WATCHTOWER_X = 3087;
	public static final int WATCHTOWER_Y = 3500;
	public static final String WATCHTOWER = "";
	public static final int TROLLHEIM_X = 3243;
	public static final int TROLLHEIM_Y = 3513;
	public static final String TROLLHEIM = "";
	// ancient
	public static final int PADDEWWA_X = 3098;
	public static final int PADDEWWA_Y = 9884;
	public static final int SENNTISTEN_X = 3322;
	public static final int SENNTISTEN_Y = 3336;
	public static final int KHARYRLL_X = 3492;
	public static final int KHARYRLL_Y = 3471;
	public static final int LASSAR_X = 3006;
	public static final int LASSAR_Y = 3471;
	public static final int DAREEYAK_X = 3161;
	public static final int DAREEYAK_Y = 3671;
	public static final int CARRALLANGAR_X = 3156;
	public static final int CARRALLANGAR_Y = 3666;
	public static final int ANNAKARL_X = 3288;
	public static final int ANNAKARL_Y = 3886;
	public static final int GHORROCK_X = 2977;
	public static final int GHORROCK_Y = 3873;
	public static final int TIMEOUT = 20;
	public static final int CYCLE_TIME = 600;
	public static final int MAX_PACKETS_PROCESS = 20;
	public static final int SMITHING_EXPERIENCE = 4;
	public static boolean DOUBLE_DROPS = false;
	public static boolean DOUBLE_XP = false;
	/**
	 * Zombie items
	 */
	// public static final int[] ZOMBIE_ITEMS = { /*18681, 20177, 20178, 20084, 15773, 16733, 16837, 16955, 17039,
	// 17143, 16909, 17259, 17361, 16293, 16315, 16359, 16381, 16403, 16425, 16689, 16711, 16693, 16699, 16757, 16758,
	// 16701, 16703, 16705, 17241, 17247, 17249, 17251, 17253, 16671, 16677, 16679, 16681, 16683, 166933, 16413, 16415,
	// 16417, 16419, 16407, 16943, 16945, 16947, 16949, 16937, 16961, 16963, 16965, 16967, 16385, 18167, 18175, 16182*/
	// };
	public static boolean yellenabled = true;
	public static int MESSAGE_DELAY = 6000;
	public static boolean ADMIN_DROP_ITEMS = false; // can admin drop items?
}
