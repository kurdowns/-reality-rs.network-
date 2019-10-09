package com.zionscape.server.model.players.skills.farming;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public enum Seeds {

	@SerializedName("POTATO")
	POTATO(5318, 40, 1, 8, 9, 1942, IntStream.range(6, 12).toArray()),
	@SerializedName("ONION")
	ONION(5319, 40, 5, 9.5, 10.5, 1957, IntStream.range(13, 19).toArray()),
	@SerializedName("CABBAGE")
	CABBAGE(5324, 40, 7, 10, 11.5, 1965, IntStream.range(20, 26).toArray()),
	@SerializedName("TOMATO")
	TOMATO(5322, 40, 12, 12.5, 14, 1982, IntStream.range(27, 33).toArray()),
	@SerializedName("SWEETCORN")
	SWEETCORN(5320, 50, 20, 17, 19, 5986, IntStream.range(34, 42).toArray()),
	@SerializedName("STAWBERRY")
	STRAWBERRY(5323, 60, 31, 26, 29, 5504, IntStream.range(43, 51).toArray()),
	@SerializedName("WATERMELON")
	WATERMELON(5321, 80, 47, 48.5, 54.5, 5982, IntStream.range(52, 62).toArray()),

	@SerializedName("MARIGOLD")
	MARIGOLD(5096, 20, 2, 8.5, 60, 6010, IntStream.range(8, 12).toArray()),
	@SerializedName("ROSEMARY")
	ROSEMARY(5097, 20, 11, 12, 90, 6014, IntStream.range(13, 17).toArray()),
	@SerializedName("NASTURTIUM")
	NASTURTIUM(5098, 20, 24, 19.5, 130.5, 6012, IntStream.range(18, 22).toArray()),
	@SerializedName("WOAD")
	WOAD(5099, 20, 25, 20.5, 115.5, 5738, IntStream.range(23, 27).toArray()),
	@SerializedName("LIMPWURT")
	LIMPWURT(5100, 20, 26, 21.5, 120, 225, IntStream.range(28, 32).toArray()),

	@SerializedName("GUAM")
	GUAM(5291, 80, 9, 12.5, 98.5, 199, IntStream.range(4, 10).toArray()),
	@SerializedName("MARENTILL")
	MARENTILL(5292, 80, 14, 13.5, 118.5, 201, IntStream.range(11, 17).toArray()),
	@SerializedName("TARROMIN")
	TARROMIN(5293, 80, 19, 16, 142, 203, IntStream.range(18, 24).toArray()),
	@SerializedName("HARRALANDER")
	HARRALANDER(5294, 80, 26, 21.5, 189.5, 205, IntStream.range(25, 31).toArray()),
	@SerializedName("RANAAR")
	RANAAR(5295, 80, 32, 27, 240.5, 207, IntStream.range(32, 38).toArray()),
	@SerializedName("TOADFLAX")
	TOADFLAX(5296, 80, 38, 34, 303.5, 2998, IntStream.range(39, 45).toArray()),
	@SerializedName("IRIT")
	IRIT(5297, 80, 44, 43, 382.5, 209, IntStream.range(46, 52).toArray()),
	@SerializedName("AVANTOE")
	AVANTOE(5298, 80, 50, 54.5, 485, 211, IntStream.range(53, 59).toArray()),
	@SerializedName("KWUARM")
	KWUARM(5299, 80, 56, 69, 615, 213, IntStream.range(68, 74).toArray()),
	@SerializedName("SNAPDRAGON")
	SNAPDRAGON(5300, 80, 62, 87.5, 777, 3051, IntStream.range(75, 81).toArray()),
	@SerializedName("CANDANTINE")
	CANDANTINE(5301, 80, 67, 106.5, 946.5, 215, IntStream.range(82, 88).toArray()),
	@SerializedName("LANTADYME")
	LANTADYME(5302, 80, 73, 134.5, 1195, 2485, IntStream.range(89, 95).toArray()),
	@SerializedName("DWARF")
	DWARF(5303, 80, 79, 170.5, 1514.5, 217, IntStream.range(96, 102).toArray()),
	@SerializedName("TORSTOL")
	TORSTOL(5304, 80, 85, 199.5, 1771, 219, IntStream.range(103, 109).toArray()),

	@SerializedName("OAK")
	OAK(5370, 200, 15, 14, 467.3, 1521, IntStream.range(8, 12).toArray()),
	@SerializedName("WILLOW")
	WILLOW(5371, 280, 30, 25, 825, 1519, IntStream.range(15, 21).toArray()),
	@SerializedName("MAPLE")
	MAPLE(5372, 320, 45, 45, 1650, 1517, IntStream.range(24, 32).toArray()),
	@SerializedName("YEW")
	YEW(5373, 400, 60, 81, 3150, 1515, IntStream.range(35, 45).toArray()),
	@SerializedName("MAGIC")
	MAGIC(5374, 480, 75, 145.5, 6100, 1513, IntStream.range(48, 60).toArray());

	private static List<Seeds> seeds = Arrays.asList(Seeds.values());
	private final int seedId;
	private final int growTime;
	private final int levelRequired;
	private final double plantingXp;
	private final double harvestingXp;
	private final int harvestId;
	private final int[] stateBits;

	Seeds(int seedId, int growTime, int levelRequired, double plantingXp, double harvestingXp, int harvestId, int[] stateBits) {
		this.seedId = seedId;
		this.growTime = growTime;
		this.levelRequired = levelRequired;
		this.plantingXp = plantingXp;
		this.harvestingXp = harvestingXp;
		this.stateBits = stateBits;
		this.harvestId = harvestId;
	}

	public static List<Seeds> getSeeds() {
		return seeds;
	}

	public int getSeedId() {
		return seedId;
	}

	public int getGrowTime() {
		return growTime;
	}

	public int getLevelRequired() {
		return levelRequired;
	}

	public double getPlantingXp() {
		return plantingXp;
	}

	public double getHarvestingXp() {
		return harvestingXp;
	}

	public int[] getStateBits() {
		return stateBits;
	}

	public int getHarvestId() {
		return harvestId;
	}
}
