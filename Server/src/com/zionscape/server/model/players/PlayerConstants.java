package com.zionscape.server.model.players;

public class PlayerConstants {

	public static final int HAT = 0;
	public static final int CAPE = 1;
	public static final int AMULET = 2;
	public static final int WEAPON = 3;
	public static final int CHEST = 4;
	public static final int SHIELD = 5;
	public static final int LEGS = 7;
	public static final int HANDS = 9;
	public static final int FEET = 10;
	public static final int RING = 12;
	public static final int ARROW = 13;

	public static final int ATTACK = 0;
	public static final int DEFENCE = 1;
	public static final int STRENGTH = 2;
	public static final int HITPOINTS = 3;
	public static final int RANGED = 4;
	public static final int PRAYER = 5;
	public static final int MAGIC = 6;
	public static final int COOKING = 7;
	public static final int WOODCUTTING = 8;
	public static final int FLETCHING = 9;
	public static final int FISHING = 10;
	public static final int FIREMAKING = 11;
	public static final int CRAFTING = 12;
	public static final int SMITHING = 13;
	public static final int MINING = 14;
	public static final int HERBLORE = 15;
	public static final int AGILITY = 16;
	public static final int THIEVING = 17;
	public static final int SLAYER = 18;
	public static final int FARMING = 19;
	public static final int RUNECRAFTING = 20;
	public static final int CONSTRUCTION = 21;
	public static final int HUNTER = 22;
	public static final int SUMMONING = 23;
	public static final int DUNGEONEERING = 24;

	public static boolean nonCombatSkill(int index) {
		return index == PRAYER || index >= COOKING;
	}
	public static final String[] SKILL_NAMES = {
			"Attack",
			"Defence",
			"Strength",
			"Hitpoints",
			"Ranged",
			"Prayer",
			"Magic",
			"Cooking",
			"Woodcutting",
			"Fletching",
			"Fishing",
			"Firemaking",
			"Crafting",
			"Smithing",
			"Mining",
			"Herblore",
			"Agility",
			"Thieving",
			"Slayer",
			"Farming",
			"Runecrafting",
			"Construction",
			"Hunter",
			"Summoning",
			"Dungeoneering"
	};

	public static final int[] DUEL_RULE_ID = new int[]{1, 2, 16, 32, 64, 128, 256, 512, 1024, 4096, 8192, 16384,
			'\u8000', 65536, 131072, 262144, 524288, 2097152, 8388608, 16777216, 67108864, 134217728};

	public static final int[] DUEL_CONFIRM_RULE_PARENT_IDS = {8242, 8243, 8244, 8245, 6571, 8246, 8247, 8248, 8249,
			8251, 8252, 8253};

	public static final String[] RULE_OPTIONS = {
			"Players cannot forfeit.", // 0
			"Players cannot move.", // 1
			"Players cannot use range.", // 2
			"Players cannot use melee", // 3
			"Players cannot use magic.", // 4
			"Players cannot drink pots..", // 5
			"Players cannot eat food.", // 6
			"Players cannot use prayer.", // 7
			"This game will include obstacles.", // 8
			"Players cannot use fun weapons.", // 9
			"Players cannot use special attacks.", // 10
			"", // 11
			"", // 12
			"", // 13
			"", // 14
			"", // 15
			"", // 16
			"", // 17
			"", // 18
			"", // 19
			"", // 20
			"", // 21
			"" // 22
	};

	public static final int DUEL_RANGE = 2;
	public static final int DUEL_MELEE = 3;
	public static final int DUEL_MAGE = 4;
	public static final int DUEL_SPECIAL = 10;
	public static final int DUEL_SAME_WEAPON = 0;
	public static final int DUEL_DDS_WHIP = 9;
	public static final int DUEL_DRINKS = 5;
	public static final int DUEL_FOOD = 6;
	public static final int DUEL_PRAYER = 7;
	public static final int DUEL_MOVEMENT = 1;
	public static final int DUEL_OBSTICALS = 8;
	public static final int DUEL_HELMET = 11;
	public static final int DUEL_CAPE = 12;
	public static final int DUEL_AMMY = 13;
	public static final int DUEL_ARROWS = 21;
	public static final int DUEL_WEAPON = 14;
	public static final int DUEL_BODY = 15;
	public static final int DUEL_SHIELD = 16;
	public static final int DUEL_LEGS = 17;
	public static final int DUEL_GLOVES = 18;
	public static final int DUEL_BOOTS = 19;
	public static final int DUEL_RINGS = 20;

	public static final double[] PRAYER_DRAIN_RATE = {
			0.5, // Thick Skin.
			0.5, // Burst of Strength.
			0.5, // Clarity of Thought.
			0.5, // Sharp Eye.
			0.5, // Mystic Will.
			1, // Rock Skin.
			1, // SuperHuman Strength.
			1, // Improved Reflexes.
			0.15, // Rapid restore
			0.3, // Rapid Heal.
			0.3, // Protect Items
			1, // Hawk eye.
			1, // Mystic Lore.
			2, // Steel Skin.
			2, // Ultimate Strength.
			2, // Incredible Reflexes.
			2, // Protect from Magic.
			2, // Protect from Missiles.
			2, // Protect from Melee.
			2, // Eagle Eye.
			2, // Mystic Might.
			0.5, // Retribution.
			1, // Redemption.
			2, // Smite
			2, // Chivalry.
			4, // Piety.
			4, // Rigour
			4, // Augury
	/*
	 * 1, // Thick Skin. 1, // Burst of Strength. 1, // Clarity of Thought. 1, // Sharp Eye. 1, // Mystic Will. 2, //
	 * Rock Skin. 2, // SuperHuman Strength. 2, // Improved Reflexes. 0.4, // Rapid restore. 0.6, // Rapid Heal. 0.6, //
	 * Protect Items. 1.5, // Hawk eye. 2, // Mystic Lore. 4, // Steel Skin. 4, // Ultimate Strength. 4, // Incredible
	 * Reflexes. 4, // Protect from Magic. 4, // Protect from Missiles. 4, // Protect from Melee. 4, // Eagle Eye. 4, //
	 * Mystic Might. 1, // Retribution. 2, // Redemption. 6, // Smite. 8, // Chivalry. 8, // Piety.
	 */
	};
	public static final double[] CURSE_DRAIN_RATE = {0.3, // Protect Item
			2.5, // Sap Warrior
			2.5, // Sap Ranger
			2.5, // Sap Mage
			2.5, // Sap Spirit
			0.3, // Berserker
			2, // Deflect Summoning
			2, // Deflect Magic
			2, // Deflect Missiles
			2, // Deflect Melee
			1.6, // Leech Attack
			1.6, // Leech Ranged
			1.6, // Leech Magic
			1.6, // Leech Defence
			1.6, // Leech Strength
			1.6, // Leech Energy
			1.6, // Leech Special
			0.5, // Wrath
			3, // SS
			3, // Turmoil
	};

	public static final int[] XP_FOR_LEVELS = {0, 83, 174, 276, 388, 512, 650, 801, 969, 1154, 1358, 1584, 1833, 2107, 2411, 2746, 3115, 3523, 3973,
			4470, 5018, 5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031, 13363, 14833, 16456, 18247, 20224, 22406, 24815, 27473, 30408, 33648,
			37224, 41171, 45529, 50339, 55649, 61512, 67983, 75127, 83014, 91721, 101333, 111945, 123660, 136594, 150872, 166636, 184040, 203254,
			224466, 247886, 273742, 302288, 333804, 368599, 407015, 449428, 496254, 547953, 605032, 668051, 737627, 814445, 899257, 992895, 1096278,
			1210421, 1336443, 1475581, 1629200, 1798808, 1986068, 2192818, 2421087, 2673114, 2951373, 3258594, 3597792, 3972294, 4385776, 4842295,
			5346332, 5902831, 6517253, 7195629, 7944614, 8771558, 9684577, 10692629, 11805606, 13034431};

}
