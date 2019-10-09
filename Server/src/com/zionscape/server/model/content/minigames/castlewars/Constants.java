package com.zionscape.server.model.content.minigames.castlewars;

import com.zionscape.server.model.Location;

class Constants {

	/**
	 * 11169 Catapult! 11301 First two numbers of the catapult 11302 Third and fourth numbers on the catapult
	 */
	static final int CATAPULT_INTERFACE_ID = 11169;
	static final int GAME_INTERFACE_ID = 11146;
	static final int LOBBY_LENGTH = 300; // 3 minutes
	static final int GAME_LENGTH = 900; // 10 mins on release

	static final int CATAPULT_ON_FIRE_DURATION = 30;
	static final int ROPES_ACTIVE_FOR = 180;
	static final int BARRICADE_ON_FIRE_DURATION = 10;
	static final int MINIMUM_PER_TEAM = 1; // change to 10 for release
	static final int MAXIMUM_DIFFERENCE_OF_PLAYERS = 3;

	static final Location STARTING_AREA = Location.create(2442, 3089, 0);
	static final Location SARADOMIN_STAND_CORDS = Location.create(2429, 3074, 3);
	static final Location ZAMORAK_STAND_CORDS = Location.create(2370, 3133, 3);

	static int[] MINIGAME_ITEM_IDS = {4049, 4053, 1, 4045, 4043, 1265, 1925, 1929, 590};

}