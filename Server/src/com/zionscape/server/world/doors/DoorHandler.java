package com.zionscape.server.world.doors;

import com.zionscape.server.Server;
import com.zionscape.server.cache.Collision;
import com.zionscape.server.cache.clientref.ObjectDef;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.DynamicMapObject;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.MapObject;
import com.zionscape.server.model.StaticMapObject;
import com.zionscape.server.model.content.minigames.GodWarsDungeon;
import com.zionscape.server.model.content.minigames.zombies.Zombies;
import com.zionscape.server.model.objects.GameObject;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.region.RegionUtility;

import java.util.*;

/**
 * Created with IntelliJ IDEA. User: Administrator Date: 12/7/13 Time: 5:08 PM To change this template use File |
 * Settings | File Templates.
 */
public final class DoorHandler {

	/**
	 * List of doors
	 */
	private static final List<Door> doors = new ArrayList<>();

	/**
	 * orientations
	 */
	private static final int WEST = 0, NORTH = 1, EAST = 2, SOUTH = 3;

	/**
	 * doors with the hinges on the left side
	 */
	private static final Set<Integer> leftHingeDoors = new HashSet<>(Arrays.asList(
			new Integer[]{52, 3506, 35549, 2307, 12349, 24375, 26910, 26906, 36315, 4428, 4424, 12446, 31815, 89, 95, 2115, 29316, 29320, 1596, 1968, 15644, 24369}
	));
	/**
	 * doors with the hinges on the right side
	 */
	private static final Set<Integer> rightHingeDoors = new HashSet<>(Arrays.asList(
			new Integer[]{53, 3507, 35551, 2308, 2116, 36846, 12350, 12348, 24376, 15536, 24378, 26913, 26908, 4427, 4423, 12448, 31814, 90, 940, 29315, 29319, 1597, 1967, 15644, 24370}
	));
	/**
	 * usually doors which are already open
	 */
	private static final Set<Integer> alternateHingeDoors = new HashSet<>(Arrays.asList(
			new Integer[]{24375, 26916}
	));
	/**
	 * Double doors, should find a better way of defining this so don't have to hardcode both open and closed at minimum
	 */
	private static final Set<DoubleDoor> doubleDoors = new HashSet<>(Arrays.asList(
			new DoubleDoor[]{

					// alkarid gate
					new DoubleDoor(new Door(35551, Location.create(3268, 3228, 0)), new Door(35549, Location.create(3268, 3227, 0))),
					new DoubleDoor(new Door(35551, Location.create(3267, 3228, 0)), new Door(35549, Location.create(3267, 3227, 0))),

					// castlewars lobby closed
					new DoubleDoor(new Door(36317, Location.create(2447, 3090, 0)), new Door(36315, Location.create(2447, 3089, 0))),
					// castlewars lobby open
					new DoubleDoor(new Door(36317, Location.create(2446, 3090, 0)), new Door(36315, Location.create(2446, 3089, 0))),

					// zamarak door closed
					new DoubleDoor(new Door(4427, Location.create(2372, 3119, 0)), new Door(4428, Location.create(2373, 3119, 0))),
					// zamorak door open
					new DoubleDoor(new Door(4427, Location.create(2372, 3118, 0)), new Door(4428, Location.create(2373, 3118, 0))),

					// saradomin door closed
					new DoubleDoor(new Door(4423, Location.create(2427, 3088, 0)), new Door(4424, Location.create(2426, 3088, 0))),
					// saradomin door open
					new DoubleDoor(new Door(4423, Location.create(2427, 3089, 0)), new Door(4424, Location.create(2426, 3089, 0))),

					// some goblin area
					new DoubleDoor(new Door(12446, Location.create(2957, 3509, 0)), new Door(12448, Location.create(2958, 3509, 0))),
					new DoubleDoor(new Door(12446, Location.create(2957, 3510, 0)), new Door(12448, Location.create(2958, 3510, 0))),

					new DoubleDoor(new Door(90, Location.create(2662, 9802, 0)), new Door(89, Location.create(2661, 9802, 0))),
					new DoubleDoor(new Door(90, Location.create(2662, 9803, 0)), new Door(89, Location.create(2661, 9803, 0))),

					new DoubleDoor(new Door(95, Location.create(2662, 9815, 0)), new Door(94, Location.create(2661, 9815, 0))),
					new DoubleDoor(new Door(95, Location.create(2662, 9814, 0)), new Door(94, Location.create(2661, 9814, 0))),

					new DoubleDoor(new Door(31815, Location.create(2645, 9828, 0)), new Door(31814, Location.create(2645, 9829, 0))),
					new DoubleDoor(new Door(31815, Location.create(2644, 9828, 0)), new Door(31814, Location.create(2644, 9829, 0))),

					new DoubleDoor(new Door(2116, Location.create(2545, 3569, 0)), new Door(2115, Location.create(2545, 3570, 0))),
					new DoubleDoor(new Door(2116, Location.create(2546, 3569, 0)), new Door(2115, Location.create(2546, 3570, 0))),

					//wilderness gate
					new DoubleDoor(new Door(2307, Location.create(2998, 3931, 0)), new Door(2308, Location.create(2997, 3931, 0))),
					new DoubleDoor(new Door(2307, Location.create(2998, 3930, 0)), new Door(2308, Location.create(2997, 3930, 0))),


					new DoubleDoor(new Door(29315, Location.create(3104, 9910, 0)), new Door(29316, Location.create(3104, 9909, 0))),
					new DoubleDoor(new Door(29315, Location.create(3103, 9910, 0)), new Door(29316, Location.create(3103, 9909, 0))),

					new DoubleDoor(new Door(29320, Location.create(3132, 9918, 0)), new Door(29319, Location.create(3131, 9918, 0))),
					new DoubleDoor(new Door(29320, Location.create(3132, 9917, 0)), new Door(29319, Location.create(3131, 9917, 0))),

					new DoubleDoor(new Door(1597, Location.create(3336, 3896, 0)), new Door(1596, Location.create(3337, 3896, 0))),
					new DoubleDoor(new Door(1597, Location.create(3336, 3895, 0)), new Door(1596, Location.create(3337, 3895, 0))),

					// canifs gate
					new DoubleDoor(new Door(3506, Location.create(3444, 3458, 0)), new Door(3507, Location.create(3443, 3458, 0))),
					new DoubleDoor(new Door(3506, Location.create(3444, 3457, 0)), new Door(3507, Location.create(3443, 3457, 0))),

					new DoubleDoor(new Door(53, Location.create(2649, 3470, 0)), new Door(52, Location.create(2650, 3470, 0))),
					new DoubleDoor(new Door(53, Location.create(2649, 3469, 0)), new Door(52, Location.create(2650, 3469, 0))),

					// warriors guild animator door
					new DoubleDoor(new Door(15644, Location.create(2855, 3546, 0)), new Door(15641, Location.create(2854, 3546, 0))),
					new DoubleDoor(new Door(15644, Location.create(2855, 3545, 0)), new Door(15641, Location.create(2854, 3545, 0))),
			}
	));


	/**
	 * Handles opening and closing of doors, returns true if it was handled otherwise false nothing changed
	 *
	 * @param player   the player opening the door
	 * @param objectId the object id being clicked
	 * @param objLoc   the objects location
	 * @return handled or not
	 */
	public static boolean handleDoor(Player player, int objectId, Location objLoc) {

		if (player != null && Zombies.inGameArea(player.getLocation())) {
			return false;
		}

		// warriors guild door
		if(objLoc.equals(2847, 3541)  && objectId == 15644 || objLoc.equals(2847, 3540) && objectId == 15641) {
			return false;
		}

		// godwars doors
		if (player != null && objectId >= 26425 && objectId <= 26428) {
			GodWarsDungeon.isObject(player, objectId, objLoc.getX(), objLoc.getY());
			return true;
		}

		/**
		 * frozen key door
		 */
		if (player != null && objectId == 57258) {
			if (!player.getLocation().equals(2899, 5204, 0)) {
				player.sendMessage("This door cannot be opened from here.");
				return true;
			}
			if (!player.getItems().playerHasItem(20120) && !player.isLegendaryDonator()) {
				player.sendMessage("This door is locked and requires a special type of key.");
				return true;
			}
			player.getPA().object(-1, objLoc.getX(), objLoc.getY(), 0, 0);
			if(!player.isLegendaryDonator()) {
				player.getItems().deleteItem(20120, 1);
			}
			player.getPA().walkTo(1, 0);

			GameCycleTaskHandler.addEvent(player, (GameCycleTaskContainer container) -> {
				player.getPA().object(objectId, objLoc.getX(), objLoc.getY(), 2, 0);
				container.stop();
			}, 2);
			return true;
		}

		// cheap fix because we do not have real instancing we use player_id * 4
		Optional<MapObject> optional = RegionUtility.getMapObject(objectId, objLoc);
		if (!optional.isPresent() && objLoc.getZ() < 4) {
			return false;
		}

		MapObject mapObject;

		if (objLoc.getZ() >= 4 && !optional.isPresent()) {
			optional = RegionUtility.getMapObject(objectId, Location.create(objLoc.getX(), objLoc.getY(), objLoc.getZ() % 4));
			if (!optional.isPresent()) {
				return false;
			}

			MapObject mapObject2 = optional.get();
			if (!(mapObject2 instanceof StaticMapObject)) {
				return false;
			}

			Location newObjectLocation = objLoc.transform(0, 0, 0);
			mapObject = new StaticMapObject(objectId, newObjectLocation, mapObject2.getType(), mapObject2.getOrientation());

			RegionUtility.addEntity(mapObject, newObjectLocation);
		} else {
			mapObject = optional.get();
		}

		if (!leftHingeDoors.contains(objectId) && !rightHingeDoors.contains(objectId) && !alternateHingeDoors.contains(objectId)) {
			boolean added = false;

			ObjectDef def = ObjectDef.forID(objectId);
			if (def != null && def.name != null) {
				if (def.name.equalsIgnoreCase("door") || def.name.equalsIgnoreCase("metal door")) {
					added = true;
					rightHingeDoors.add(objectId);
				}
			}

			if (!added) {
				return false;
			}
		}

		Optional<Door> door = getDoor(objectId, objLoc);
		if (door.isPresent()) {
			Location loc = door.get().getOriginalObject().getLocation();

			Server.objectManager.removeObject(objLoc, mapObject.getType(), true);
			Server.objectManager.removeObject(loc, mapObject.getType());

			new GameObject(objectId, loc.getX(), loc.getY(), loc.getZ(), door.get().getOriginalObject().getOrientation(), mapObject.getType(), -1, -1);

			RegionUtility.removeEntity(mapObject, objLoc);
			RegionUtility.addEntity(door.get().getOriginalObject(), loc);

			Optional<DoubleDoor> optionalDoor = getDoubleDoor(objectId, objLoc);
			if (optionalDoor.isPresent()) {
				DoubleDoor doubleDoor = optionalDoor.get();
				Door otherDoor = doubleDoor.getFirstDoor().getId() == objectId ? doubleDoor.getSecondDoor() : doubleDoor.getFirstDoor();
				DoorHandler.handleDoor(player, otherDoor.getId(), otherDoor.getLocation());
			}

			doors.remove(door.get());
		} else {

			Collision.removeObjectClipping(objectId, objLoc, mapObject.getOrientation(), mapObject.getType());
			Server.objectManager.removeObject(objLoc, optional.get().getType(), false);

			Location location = translateDoorPosition(mapObject);
			int orientation = translateDoorOrientation(mapObject);

			new GameObject(-1, objLoc.getX(), objLoc.getY(), objLoc.getZ(), orientation, optional.get().getType(), -1, -1);
			new GameObject(objectId, location.getX(), location.getY(), location.getZ(), orientation, optional.get().getType(), -1, -1);

			RegionUtility.removeEntity(mapObject, objLoc);
			RegionUtility.addEntity(new DynamicMapObject(objectId, location, mapObject.getType(), orientation), location);

			if (player.rights == 3) {
				System.out.println("open door " + location + " " + orientation);
			}

			Optional<DoubleDoor> optionalDoor = getDoubleDoor(objectId, objLoc);
			if (optionalDoor.isPresent()) {
				DoubleDoor doubleDoor = optionalDoor.get();
				Door otherDoor = doubleDoor.getFirstDoor().getId() == objectId ? doubleDoor.getSecondDoor() : doubleDoor.getFirstDoor();
				DoorHandler.handleDoor(player, otherDoor.getId(), otherDoor.getLocation());
			}

			doors.add(new Door(objectId, location, orientation, (StaticMapObject) mapObject));
		}
		return true;
	}

	private static Optional<DoubleDoor> getDoubleDoor(int objectId, Location location) {
		return doubleDoors.stream().filter(x -> x.getFirstDoor().getId() == objectId && x.getFirstDoor().getLocation().equals(location) || x.getSecondDoor().getId() == objectId && x.getSecondDoor().getLocation().equals(location)).findFirst();
	}

	private static Optional<Door> getDoor(int objectId, Location location) {
		return doors.stream().filter(x -> x.getId() == objectId && x.getLocation().equals(location)).findFirst();
	}

	public static boolean isDoorOpen(int objectId, Location location) {
		return getDoor(objectId, location).isPresent();
	}

	private static Location translateDoorPosition(MapObject door) {
		Location location = door.getLocation();

		if (alternateHingeDoors.contains(door.getId())) {
			switch (door.getOrientation()) {
				case WEST:
					return location.transform(0, +1, 0);
				case SOUTH:
					return location.transform(-1, 0, 0);
				case NORTH:
					return location.transform(+1, 0, 0);
				case EAST:
					return location.transform(0, -1, 0);
			}
		}

		switch (door.getOrientation()) {
			case WEST: // west
				return location.transform(-1, 0, 0);
			case EAST: // east
				return location.transform(+1, 0, 0);
			case NORTH: // north
				return location.transform(0, +1, 0);
			case SOUTH: // south
				return location.transform(0, -1, 0);

		}

		return location;
	}

	private static int translateDoorOrientation(MapObject door) {

		if (alternateHingeDoors.contains(door.getId()) || leftHingeDoors.contains(door.getId())) {
			switch (door.getOrientation()) {
				case WEST:
					return SOUTH;
				case EAST:
					return NORTH;
				case NORTH:
					return WEST;
				case SOUTH:
					return EAST;

			}
		}

		if (rightHingeDoors.contains(door.getId())) {
			switch (door.getOrientation()) {
				case WEST:
					return NORTH;
				case EAST:
					return SOUTH;
				case NORTH:
					return EAST;
				case SOUTH:
					return WEST;
			}
		}

		return 0;
	}

}
