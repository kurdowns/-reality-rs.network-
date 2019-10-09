package com.zionscape.server.model.players.skills.slayer;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public enum Tasks {


	//44477

	@SerializedName("CRAWLING_HANDS")
	CRAWLING_HANDS("Crawling Hands", 5, 1648, 8),
	@SerializedName("CAVE_BUG")
	CAVE_BUG("Cave Bugs", 7, new int[]{1832, 5750}, 7.5),
	@SerializedName("CAVER_CRAWLER")
	CAVER_CRAWLE("Cave Crawlers", 10, new int[]{1600, 7787, 7799}, 25.8),
	@SerializedName("BANSHEE")
	BANSHEE("Banshees", 15, new int[]{1612, 6686, 6694}, 15.5),
	@SerializedName("ROCKSLUG")
	ROCKSLUG("Rockslugs", 20, 1632, 27),
	@SerializedName("COCKATRICE")
	COCKATRICE("Cockatrices", 25, 1620, 20),
	@SerializedName("PYREFIEND")
	PYREFIEND("Pyrefiends", 30, 1633, 34.2),
	@SerializedName("MOGRE")
	MOGRE("Mogres", 32, 114, 27),
	@SerializedName("BASILISK")
	BASILISK("Basilisks", 40, new int[]{1616, 1617, 7799}, 50),
	@SerializedName("INFERNAL_MAGE")
	INFERNAL_MAGE("Infernal Mages", 45, 1643, 96),
	@SerializedName("BLOODVELD")
	BLOODVELD("Bloodvelds", 50, 1618, 38.6),
	@SerializedName("JELLY")
	JELLY("Jellys", 52, new int[]{1637, 1640}, 43.2),
	@SerializedName("TUROTH")
	TUROTH("Turoths", 55, new int[]{1611, 1623, 1627, 7800}, 38),
	@SerializedName("ABERRANT_SPECTRE")
	ABERRANT_SPECTRE("Aberrant Spectres", 60, 7804, 109.5),
	@SerializedName("SPIRITUAL_RANGER")
	SPIRITUAL_RANGER("Spiritual Rangers", 63, new int[]{6220, 6276, 6256, 6230}, 240.4),
	@SerializedName("DUST_DEVIL")
	DUST_DEVIL("Dust Devils", 65, 1624, 199.7),
	@SerializedName("SPIRITUAL_WARRIOR")
	SPIRITUAL_WARRIOR("Spiritual Warriors", 68, new int[]{6219, 6255, 5229, 6277, 6229}, 240.4),
	@SerializedName("KURASK")
	KURASK("Kurasks", 70, new int[]{1608, 7799, 7805, 4229}, 121.2),
	@SerializedName("NECHRYAEL")
	NECHRYAEL("NEchryaels", 80, 1613, 264.6),
	@SerializedName("MUTATED_JADINKO_BABY")
	MUTATED_JADINKO_BABY("Mutated Jadinko Babies", 80, 13820, 98.6),
	@SerializedName("SPIRITUAL_MAGE")
	SPIRITUAL_MAGE("Spiritual Mages", 83, new int[]{6221, 6257, 6278, 6231}, 240.4),
	@SerializedName("ABYSSAL_DEMON")
	ABYSSAL_DEMON("AByssal Demons", 85, 1615, 291),
	@SerializedName("MUTATED_JADINKO_GUARD")
	MUTATED_JADINKO_GUARD("Mutated Jadinko Guards", 86, 13821, 188.4),
	@SerializedName("DARK_BEAST")
	DARK_BEAST("Dark Beasts", 90, 2783, 348.2),
	@SerializedName("MUTED_JADINKO_MALE")
	MUTATED_JADINKO_MALE("Mutated Jadinko Males", 91, 13822, 209.6),
	@SerializedName("BAT")
	BAT("Bats", 0, 78, 8),
	//@SerializedName("TORMENTED_DEMON")
	//TORMENTED_DEMON("Tormented Demons", 0, 8349, 1136),
	@SerializedName("SKELETON")
	SKELETON("Skeletons", 0, new int[]{91, 90, 92, 5386}, 9),
	@SerializedName("ICEFIEND")
	ICEFIEND("Icefiends", 0, 3406, 7),
	//@SerializedName("GLACOR")
	//GLACOR("Glacors", 0, 14301, 1881),
	@SerializedName("WATER_FIEND")
	WATER_FIEND("Water Fiends", 0, 5361, 335),
	@SerializedName("MITHRIL_DRAGON")
	MITHRIL_DRAGON("Mithril Dragons", 0, 5363, 370),
	@SerializedName("BLACK_DRAGON")
	BLACK_DRAGON("Black Dragons", 0, new int[]{50, 54, 3376}, 245),
	@SerializedName("RED_DRAGON")
	RED_DRAGON("Red Dragons", 0, new int[]{1589, 53}, 220.8),
	@SerializedName("AVANSIE")
	AVANSIE("Avansies", 0, new int[]{6233, 6232, 6239, 6222}, 171),
	@SerializedName("STEEL_DRAGON")
	STEEL_DRAGON("Steel Dragons", 0, new int[]{1592}, 350),
	@SerializedName("IRON_DRAGON")
	IRON_DRAGON("Iron Dragons", 0, new int[]{1591}, 245),
	@SerializedName("GARGOYLES")
	GARGOYLE("Gargoyles", 0, 1610, 197.4),
	@SerializedName("BLACK_DEMON")
	BLACK_DEMON("Black Demons", 0, 84, 294.4),
	@SerializedName("MOSS_GIANT")
	MOSS_GIANT("Moss Giants", 0, 112, 49.5),
	@SerializedName("ICE_GIANT")
	ICE_GIANT("Ice Giants", 0, 111, 47),
	@SerializedName("HELL_HOUND")
	HELL_HOUND("Hellhounds", 0, new int[]{49, 5862}, 93.6),
	@SerializedName("GREEN_DRAGON")
	GREEN_DRAGON("Green Dragons", 0, 941, 175),
	@SerializedName("FIRE_GIANT")
	FIRE_GIANT("Fire Giants", 0, 110, 161.2),
	@SerializedName("DAGANNOTH")
	DAGANNOTH("Dagannoths", 0, new int[]{2881, 2882, 2883, 1341}, 57),
	@SerializedName("LESSER_DEMON")
	LESSER_DEMON("Lesser Demons", 0, 82, 86.6),
	@SerializedName("GREATER_DEMON")
	GREATER_DEMON("Greater Demons", 0, new int[]{83, 6203}, 135.4),
	@SerializedName("HILL_GIANT")
	HILL_GIANT("Hill Giants", 0, 117, 39),
	@SerializedName("HOB_GOBLIN")
	HOB_GOBLIN("Hob Goblins", 0, new int[]{6275, 2685}, 49),
	@SerializedName("BLUE_DRAGON")
	BLUE_DRAGON("Blue Dragons", 0, new int[]{52, 55}, 93.8),
	@SerializedName("BRONZE_DRAGON")
	BRONZE_DRAGON("Bronze Dragons", 0, new int[]{1590}, 124.5),
	@SerializedName("COW")
	COW("Cows", 0, 81, 8),
	@SerializedName("GHOST")
	GHOST("Ghosts", 0, 103, 10.5),
	@SerializedName("SCORPION")
	SCORPION("Scorpions", 0, new int[]{107, 109, 144}, 8),
	@SerializedName("SPIDER")
	SPIDER("Spiders", 0, new int[]{134, 59, 60, 61, 2034,}, 5),
	@SerializedName("ZOMBIE")
	ZOMBIE("Zombies", 0, new int[]{75, 73, 5393}, 8),
	@SerializedName("ICE_WARRIOR")
	ICE_WARRIOR("Ice Warriors", 0, 125, 27),
	@SerializedName("ANKOU")
	ANKOU("Ankous", 0, 4383, 34),
	@SerializedName("EARTH_WARRIOR")
	EARTH_WARRIOR("Earth Warriors", 0, 124, 57.5),
	@SerializedName("SKELETAL_WYVERN")
	SKELETAL_WYVERN("Skeletal Wyverns", 0, 3068, 365),
	@SerializedName("DESERT_STRKEWYRM")
	DESERT_STRKEWYRM("Desert Strykewyrm", 77, 9465, 320),
	@SerializedName("JUNGLE_STRKEWYRM")
	JUNGLE_STRKEWYRM("Jungle Strykewyrm", 73, 9467, 300),
	@SerializedName("ICE_STRKEWYRM")
	ICE_STRKEWYRM("Ice Strykewyrm", 93, 9463, 340),

	@SerializedName("GLACOR")
	GLACOR("Glacor", 93, 14301, 370),
	@SerializedName("WILDY_WYRM")
	WILDY_WYRM("Wildy Wyrm", 93, 3334, 291),
	@SerializedName("CHAOS_ELEMENT")
	CHAOS_ELEMENT("Chaos Element", 93, 3200, 335),
	@SerializedName("DAG_SUPREME")
	DAG_SUPREME("Dagannoth Supreme", 93, 2881, 390),
	@SerializedName("DAG_PRIME")
	DAG_PRIME("Dagannoth Prime", 93, 2882, 390),
	@SerializedName("DAG_REX")
	DAG_REX("Dagannoth Rex", 93, 2883, 390),
	@SerializedName("KALPHITE_QUEEN")
	KALPHITE_QUEEN("Kalphite Queen", 93, 1160, 460),
	@SerializedName("KBD")
	KBD("King Black Dragon", 93, 50, 460),
	@SerializedName("BANDOS")
	BANDOS("General Graardo", 93, 6260, 499),
	@SerializedName("TSTSAROTH")
	TSTSAROTH("K'ril Tstsaroth", 93, 6203, 499),
	@SerializedName("KREE_ARR")
	KREE_ARR("Kree'arra", 93, 6222, 510),
	@SerializedName("CZULYANA")
	CZULYANA("Commander Zilyana", 93, 6247, 510),
	@SerializedName("TORMENTED_DEMON")
	TORMENTED_DEMON("Tormented Demon", 93, 8349, 450),
	@SerializedName("JUNGLE_BOOK")
	JUNGLE_BOOK("Jungle Demon", 93, 1472, 430),
	@SerializedName("BORK")
	BORK("Bork", 93, 7133, 390),
	@SerializedName("BARRELCHEST")
	BARRELCHEST("Barrelchest", 93, 5666, 370),
	@SerializedName("SEA_TROLL_QUEEN")
	SEA_TROLL_QUEEN("Sea Troll Queen", 93, 3847, 350),
	@SerializedName("CORP_BEAST")
	CORP_BEAST("Corporeal Beast", 93, 8133, 530),
	@SerializedName("AVATAR")
	AVATAR("Avatar of Destruction", 93, 8596, 515),
	@SerializedName("NEX")
	NEX("Nex", 93, 13447, 550),
	@SerializedName("NOMAD")
	NOMAD("Nomad", 93, 8528, 540),
	@SerializedName("ZULRAH")
	ZULRAH("Zulrah", 93, new int[]{ 2042, 2043, 2044}, 460),
	@SerializedName("CERB")
	CERB("Cerberus", 93, 5862, 440);

	private final String name;
	private final int requiredSlayer;
	private final List<Integer> npcIds;
	private final double xp;
	private boolean elite;

	Tasks(String name, int requiredSlayer, int npcId, double xp) {
		this.name = name;
		this.requiredSlayer = requiredSlayer;
		this.npcIds = Arrays.asList(npcId);
		this.xp = xp;
	}

	Tasks(String name, int requiredSlayer, int[] npcIds, double xp) {
		this.name = name;
		this.requiredSlayer = requiredSlayer;
		this.npcIds = IntStream.of(npcIds).boxed().collect(Collectors.toList());
		this.xp = xp;
	}

	public void setElite(boolean elite) {
		this.elite = elite;
	}

	public static int getRequiredSlayerLevel(int npcType) {
		for (Tasks task : Tasks.values()) {
			if(task.elite) {
				return 0;
			}
			if (task.getNpcIds().contains(npcType)) {
				return task.getRequiredSlayer();
			}
		}
		return 0;
	}

	public String getName() {
		return name;
	}

	public List<Integer> getNpcIds() {
		return npcIds;
	}

	public double getXp() {
		return xp;
	}

	public int getRequiredSlayer() {
		return requiredSlayer;
	}

}