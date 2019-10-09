package com.zionscape.server.model.content.minigames.zombies;

import com.zionscape.server.model.Area;
import com.zionscape.server.model.Location;

class Constants {

	public static final int LOBBY_TIME_LENGTH = 60;
	public static final Area LOBBBY_AREA = new Area(Location.create(4195, 4005), Location.create(4201, 4011), 0);
	public static final Location LOBBY_SPAWN = Location.create(4198, 4001);
	public static final Area CLAN_LOBBY_AREA = new Area(Location.create(4203, 4005), Location.create(4207, 4010), 0);
	public static final Area RANDOM_LOBBY_AREA = new Area(Location.create(4196, 4005), Location.create(4200, 4010), 0);
	public static final Location GAME_SPAWN = Location.create(4103, 4023);

	public static final Area GAME_AREA = new Area(Location.create(4098, 3970), Location.create(4156, 4027), -1);

	public static final int[] CHEST_ITEM_IDS = new int[]{13450, 13451, 13452, 13453, 14486, 19784, 13461, 13405, 13430, 13472, 526, 13478, 13479};


	public static final Location[] WEST_ZOMBIE_SPAWN_POINTS = {
			Location.create(4116, 4009),
			Location.create(4114, 4010),
			Location.create(4116, 4013),
			Location.create(4117, 4013),
			Location.create(4118, 4012),
			Location.create(4114, 3984),
			Location.create(4117, 3983),
			Location.create(4118, 3985),
			Location.create(4116, 3987),
			Location.create(4116, 3988)
	};

	public static final Location[] EAST_ZOMBIE_SPAWN_POINTS = {
			Location.create(4144, 3992),
			Location.create(4151, 4007),
			Location.create(4143, 4988),
			Location.create(4146, 3989),
			Location.create(4145, 3993),
			Location.create(4143, 3992),
			Location.create(4152, 4004),
			Location.create(4151, 4007),
			Location.create(4153, 4008),
	};

}