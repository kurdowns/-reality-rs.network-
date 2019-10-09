package com.zionscape.server.model.content.treasuretrails.solutions;

import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.treasuretrails.Solution;
import com.zionscape.server.model.players.Player;

public enum Maps implements Solution {

	/*
	 * 9275 Clue Scroll map // draynor 9359 Clue Scroll map // Its blank useless 9507 Clue Scroll map // Missing map
	 * data http://i.imgur.com/icOwdlb.png 18055 Clue Scroll map // Map is pwned by some other interface
	 */

	DRAYNOR_FISH_SPOT(7113, Location.create(3093, 3227, 0)), FALADOR_ROCK_AREA(7271, Location.create(3043, 3398, 0)), SOUTH_EAST_DRAYNOR(
			9043, Location.create(2616, 3076, 0)), HOUSE_NEAR_COAL_TRUCKS(9108, Location.create(2611, 3481, 0)), MCGRUBORS_WOODS_CRATE(
			9196, Location.create(2658, 3488, 0), 357), NECROMANCER_SOUTH_ARDOUGNE(9632, Location.create(2650, 3230, 0)), CLOCKWORK_TOWER(
			9720, Location.create(2565, 3248, 0), 354), RIMMINGTON(9839, Location.create(2924, 3210, 0)), NORTH_FALADOR(
			17537, Location.create(2970, 3414, 0)), LEGENDS_GUILD(17634, Location.create(2723, 3339, 0)), CHAOS_ALTER(
			17888, Location.create(2454, 3230, 0)), CAMMY_MAGIC_TREE_PLACE(7162, Location.create(2702, 3429, 0)),
	// GOBLIN_VILLAGE(9454, Location.create(2951, 3508, 0), 16560),
	LEVEL_50_WILD(17620, Location.create(3020, 3912, 0)), RELLEKA_LIGHTHOUSE(17907, Location.create(2579, 3597, 0)), MISCELLANIA(
			17687, Location.create(2534, 3866, 0)), MORYTANIA(17774, Location.create(3434, 3265, 0)), CHAMPIONS_GUID(
			6994, Location.create(3166, 3360, 0)), VARROCK_EAST_MINE(7045, Location.create(3290, 3372, 0));

	private final int interfaceId;
	private final Location location;
	private final int objectId;

	Maps(int interfaceId, Location location) {
		this.interfaceId = interfaceId;
		this.location = location;
		objectId = -1;
	}

	Maps(int interfaceId, Location location, int objectId) {
		this.interfaceId = interfaceId;
		this.location = location;
		this.objectId = objectId;
	}

	public int getInterfaceId() {
		return interfaceId;
	}

	@Override
	public void show(Player player) {
		player.getPA().showInterface(interfaceId);
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public int getObjectId() {
		return objectId;
	}

	@Override
	public int getNpcId() {
		return -1;
	}

}
