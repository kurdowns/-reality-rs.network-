package com.zionscape.server.model.content.treasuretrails;

import com.zionscape.server.model.content.treasuretrails.solutions.Anagrams;
import com.zionscape.server.model.content.treasuretrails.solutions.Maps;
import com.zionscape.server.model.content.treasuretrails.solutions.Riddles;

public enum Clues {

	/**
	 * easy clues
	 */
	CLUE1(2677, Level.EASY, Maps.DRAYNOR_FISH_SPOT), CLUE2(2678, Level.EASY, Maps.FALADOR_ROCK_AREA), CLUE3(2679,
			Level.EASY, Maps.SOUTH_EAST_DRAYNOR), CLUE4(2680, Level.EASY, Anagrams.Cook), CLUE5(2681, Level.EASY,
			Anagrams.Jaraah), CLUE34(2682, Level.EASY, Riddles.AGGIE_DRAYNOR), CLUE35(2683, Level.EASY,
			Riddles.SMITHY_PORT_KHAZARD), CLUE36(2684, Level.EASY, Riddles.CLOTHES_SHOP_CANIFIS),

	/**
	 * Medium 2801
	 */
	CLUE6(2801, Level.MEDIUM, Maps.HOUSE_NEAR_COAL_TRUCKS), CLUE7(2803, Level.MEDIUM, Maps.MCGRUBORS_WOODS_CRATE), CLUE8(
			2805, Level.MEDIUM, Maps.NECROMANCER_SOUTH_ARDOUGNE), CLUE9(2807, Level.MEDIUM, Maps.CLOCKWORK_TOWER), CLUE10(
			2809, Level.MEDIUM, Anagrams.Caroline), CLUE11(2811, Level.MEDIUM, Anagrams.Oracle), CLUE12(2813,
			Level.MEDIUM, Anagrams.Kebab_Seller), CLUE23(2815, Level.MEDIUM, Anagrams.Party_Pete), CLUE37(2817,
			Level.MEDIUM, Riddles.KHAZARD_BATTLEFIELD), CLUE38(2819, Level.MEDIUM, Riddles.EDGEVILLE_YEW), CLUE39(2821,
			Level.MEDIUM, Riddles.MONASTRY_SOUTH_ARDY),

	/**
	 * Hard 2735 - 2799
	 */
	CLUE13(2735, Level.HARD, Maps.RIMMINGTON), CLUE14(2737, Level.HARD, Maps.NORTH_FALADOR), CLUE15(2739, Level.HARD,
			Maps.LEGENDS_GUILD), CLUE16(2741, Level.HARD, Maps.CHAOS_ALTER), CLUE17(2743, Level.HARD,
			Maps.CAMMY_MAGIC_TREE_PLACE), CLUE18(2745, Level.HARD, Maps.CAMMY_MAGIC_TREE_PLACE), CLUE19(2747,
			Level.HARD, Anagrams.Lowe), CLUE20(13010, Level.HARD, Anagrams.Recruiter), CLUE21(13012, Level.HARD,
			Anagrams.King_Roald), CLUE22(13014, Level.HARD, Anagrams.Edmond), CLUE24(13016, Level.HARD,
			Maps.LEVEL_50_WILD), CLUE25(13018, Level.HARD, Maps.RELLEKA_LIGHTHOUSE), CLUE26(13020, Level.HARD,
			Maps.MISCELLANIA), CLUE27(13022, Level.HARD, Maps.MORYTANIA), CLUE28(13024, Level.HARD, Maps.CHAMPIONS_GUID), CLUE29(
			13026, Level.HARD, Maps.VARROCK_EAST_MINE), CLUE30(13028, Level.HARD, Anagrams.Brundt), CLUE31(13030,
			Level.HARD, Anagrams.Cam_the_camel), CLUE32(13032, Level.HARD, Anagrams.Professor_Onglewip), CLUE33(13034,
			Level.HARD, Anagrams.Hans), CLUE40(13036, Level.HARD, Riddles.GENERAL_BENTNOZE), CLUE41(13038, Level.HARD,
			Riddles.HAMID), CLUE42(13040, Level.HARD, Riddles.EAST_FALADOR_EAST_BANK), CLUE43(13042, Level.HARD,
			Riddles.SWENSENS_HOUSE_RELLEKA);

	private final int itemId;
	private final Level level;
	private final Solution solution;

	Clues(int itemId, Level level, Solution solution) {
		this.itemId = itemId;
		this.level = level;
		this.solution = solution;
	}

	public int getItemId() {
		return itemId;
	}

	public Solution getSolution() {
		return solution;
	}

	public Level getLevel() {
		return level;
	}

}
