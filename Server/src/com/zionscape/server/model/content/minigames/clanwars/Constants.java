package com.zionscape.server.model.content.minigames.clanwars;

import com.zionscape.server.model.Area;
import com.zionscape.server.model.Location;

public class Constants {

	public static final int MELEE_RULE = 0;
	public static final int MAGIC_RULE = 1;
	public static final int RANGE_RULE = 2;
	public static final int PRAYER_RULE = 3;
	public static final int SUMMONING_RULE = 4;
	public static final int FOOD_RULE = 5;
	public static final int POTIONS_RULE = 6;

	public static final Location LOBBY_SPAWN_LOCATION = Location.create(3273, 3686, 0);
	public static final Area LOBBY_AREA = new Area(3264, 3672, 3279, 3695, 0);
	//public static final Area WHITE_PORTAL_AREA = new Area(2756, 5512, 2875, 5627, 0);
	public static final Area WHITE_PORTAL_AREA = new Area(2756, 5505, 2875, 5620, 0);
	public static final Location WHITE_PORTAL_SPAWN = Location.create(2815, 5511, 0);

}