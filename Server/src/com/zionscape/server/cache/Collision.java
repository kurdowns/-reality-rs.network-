package com.zionscape.server.cache;

import com.zionscape.server.Server;
import com.zionscape.server.cache.clientref.ObjectDef;
import com.zionscape.server.model.Entity;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.MapObject;
import com.zionscape.server.model.StaticMapObject;
import com.zionscape.server.model.region.Region;
import com.zionscape.server.util.Stream;
import com.zionscape.server.world.ObjectManager;
import com.zionscape.server.world.RemovedObject;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

public class Collision {

	public static final int NORTH_WEST_BLOCKED = 0x1;
	public static final int NORTH_BLOCKED = 0x2;
	public static final int NORTH_EAST_BLOCKED = 0x4;
	public static final int EAST_BLOCKED = 0x8;
	public static final int SOUTH_EAST_BLOCKED = 0x10;
	public static final int SOUTH_BLOCKED = 0x20;
	public static final int SOUTH_WEST_BLOCKED = 0x40;
	public static final int WEST_BLOCKED = 0x80;
	public static final int TILE_BLOCKED = 0x100;
	public static final int PROJECTILE_NORTH_WEST_BLOCKED = 0x200;
	public static final int PROJECTILE_NORTH_BLOCKED = 0x400;
	public static final int PROJECTILE_NORTH_EAST_BLOCKED = 0x800;
	public static final int PROJECTILE_EAST_BLOCKED = 0x1000;
	public static final int PROJECTILE_SOUTH_EAST_BLOCKED = 0x2000;
	public static final int PROJECTILE_SOUTH_BLOCKED = 0x4000;
	public static final int PROJECTILE_SOUTH_WEST_BLOCKED = 0x8000;
	public static final int PROJECTILE_WEST_BLOCKED = 0x10000;
	public static final int PROJECTILE_TILE_BLOCKED = 0x20000;
	public static final int UNKNOWN = 0x80000;
	public static final int GROUND_TILE_BLOCKED = 0x200000;
	public static final int UNLOADED_TILE = 0x1000000;
	private static final Logger logger = Logger.getLogger(Collision.class.getName());
	// public static int[] DOOR_IDS = new int[]{1530, 15516, 24381, 15514, 94, 90, 35551, 35549, 31814, 29319, 29315,
	// 29261, 29316, 37969};
	private static Map<Integer, Collision> regions = new HashMap<>();
	private int id;
	private int[][][] clips;

	public Collision(int id, boolean members) {
		this.id = id;
	}

	public static boolean blockedEast(int x, int y, int z) {
		return (getClipping(x + 1, y, z) & 0x1280180) != 0;
	}

	public static boolean blockedNorth(int x, int y, int z) {
		return (getClipping(x, y + 1, z) & 0x1280120) != 0;
	}

	public static boolean blockedNorthEast(int x, int y, int z) {
		return (getClipping(x + 1, y + 1, z) & 0x12801e0) != 0;
	}

	public static boolean blockedNorthWest(int x, int y, int z) {
		return (getClipping(x - 1, y + 1, z) & 0x1280138) != 0;
	}

	public static boolean blockedSouth(int x, int y, int z) {
		return (getClipping(x, y - 1, z) & 0x1280102) != 0;
	}

	public static boolean blockedSouthEast(int x, int y, int z) {
		return (getClipping(x + 1, y - 1, z) & 0x1280183) != 0;
	}

	public static boolean blockedSouthWest(int x, int y, int z) {
		return (getClipping(x - 1, y - 1, z) & 0x128010e) != 0;
	}

	public static boolean blockedWest(int x, int y, int z) {
		return (getClipping(x - 1, y, z) & 0x1280108) != 0;
	}

	public static boolean canMove(int startX, int startY, int endX, int endY, int height, int xLength, int yLength) {
		int diffX = endX - startX;
		int diffY = endY - startY;

		if (height < 0) {
			height = 0;
		}

		int max = Math.max(Math.abs(diffX), Math.abs(diffY));

		for (int ii = 0; ii < max; ii++) {
			int currentX = endX - diffX;
			int currentY = endY - diffY;
			for (int i = 0; i < xLength; i++) {
				for (int i2 = 0; i2 < yLength; i2++) {
					if (diffX < 0 && diffY < 0) {
						if ((Collision.getClipping(currentX + i - 1, currentY + i2 - 1, height) & 0x128010e) != 0
								|| (Collision.getClipping(currentX + i - 1, currentY + i2, height) & 0x1280108) != 0
								|| (Collision.getClipping(currentX + i, currentY + i2 - 1, height) & (0x1000000 | 0x200000 | 0x80000 | 0x100 | 0x2)) != 0) {
							return false;
						}
					} else if (diffX > 0 && diffY > 0) {
						if ((Collision.getClipping(currentX + i + 1, currentY + i2 + 1, height) & 0x12801e0) != 0
								|| (Collision.getClipping(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0
								|| (Collision.getClipping(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0) {
							return false;
						}
					} else if (diffX < 0 && diffY > 0) {
						if ((Collision.getClipping(currentX + i - 1, currentY + i2 + 1, height) & 0x1280138) != 0
								|| (Collision.getClipping(currentX + i - 1, currentY + i2, height) & 0x1280108) != 0
								|| (Collision.getClipping(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0) {
							return false;
						}
					} else if (diffX > 0 && diffY < 0) {
						if ((Collision.getClipping(currentX + i + 1, currentY + i2 - 1, height) & 0x1280183) != 0
								|| (Collision.getClipping(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0
								|| (Collision.getClipping(currentX + i, currentY + i2 - 1, height) & 0x1280102) != 0) {
							return false;
						}
					} else if (diffX > 0 && diffY == 0) {
						if ((Collision.getClipping(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0) {
							return false;
						}
					} else if (diffX < 0 && diffY == 0) {
						if ((Collision.getClipping(currentX + i - 1, currentY + i2, height) & 0x1280108) != 0) {
							return false;
						}
					} else if (diffX == 0 && diffY > 0) {
						if ((Collision.getClipping(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0) {
							return false;
						}
					} else if (diffX == 0 && diffY < 0) {
						if ((Collision.getClipping(currentX + i, currentY + i2 - 1, height) & 0x1280102) != 0) {
							return false;
						}
					}
				}
			}
			if (diffX < 0) {
				diffX++;
			} else if (diffX > 0) {
				diffX--;
			}
			if (diffY < 0) {
				diffY++;
			} else if (diffY > 0) {
				diffY--;
			}
		}
		return true;
	}

	public static boolean canProjectileMove(int startX, int startY, int endX, int endY, int height, int xLength, int yLength) {

		int diffX = endX - startX;
		int diffY = endY - startY;

		if (height < 0) {
			height = 0;
		}

		int max = Math.max(Math.abs(diffX), Math.abs(diffY));

		for (int ii = 0; ii < max; ii++) {
			int currentX = endX - diffX;
			int currentY = endY - diffY;

			// int currentX = startX;
			// int currentY = startY;

			for (int i = 0; i < xLength; i++) {
				for (int i2 = 0; i2 < yLength; i2++) {

					if (diffX < 0 && diffY < 0) {
						if ((Collision.getClipping(currentX + i - 1, currentY + i2 - 1, height) & (Collision.UNLOADED_TILE
								| /* BLOCKED_TILE | */Collision.UNKNOWN | Collision.PROJECTILE_TILE_BLOCKED
								| Collision.PROJECTILE_EAST_BLOCKED | Collision.PROJECTILE_NORTH_EAST_BLOCKED | Collision.PROJECTILE_NORTH_BLOCKED)) != 0
								|| (Collision.getClipping(currentX + i - 1, currentY + i2, height) & (Collision.UNLOADED_TILE
								| /* BLOCKED_TILE | */Collision.UNKNOWN | Collision.PROJECTILE_TILE_BLOCKED | Collision.PROJECTILE_EAST_BLOCKED)) != 0
								|| (Collision.getClipping(currentX + i, currentY + i2 - 1, height) & (Collision.UNLOADED_TILE
								| /* BLOCKED_TILE | */Collision.UNKNOWN | Collision.PROJECTILE_TILE_BLOCKED | Collision.PROJECTILE_NORTH_BLOCKED)) != 0) {
							return false;
						}
					} else if (diffX > 0 && diffY > 0) {
						if ((Collision.getClipping(currentX + i + 1, currentY + i2 + 1, height) & (Collision.UNLOADED_TILE
								| /* BLOCKED_TILE | */Collision.UNKNOWN | Collision.PROJECTILE_TILE_BLOCKED
								| Collision.PROJECTILE_WEST_BLOCKED | Collision.PROJECTILE_SOUTH_WEST_BLOCKED | Collision.PROJECTILE_SOUTH_BLOCKED)) != 0
								|| (Collision.getClipping(currentX + i + 1, currentY + i2, height) & (Collision.UNLOADED_TILE
								| /* BLOCKED_TILE | */Collision.UNKNOWN | Collision.PROJECTILE_TILE_BLOCKED | Collision.PROJECTILE_WEST_BLOCKED)) != 0
								|| (Collision.getClipping(currentX + i, currentY + i2 + 1, height) & (Collision.UNLOADED_TILE
								| /* BLOCKED_TILE | */Collision.UNKNOWN | Collision.PROJECTILE_TILE_BLOCKED | Collision.PROJECTILE_SOUTH_BLOCKED)) != 0) {
							return false;
						}
					} else if (diffX < 0 && diffY > 0) {
						if ((Collision.getClipping(currentX + i - 1, currentY + i2 + 1, height) & (Collision.UNLOADED_TILE
								| /* BLOCKED_TILE | */Collision.UNKNOWN | Collision.PROJECTILE_TILE_BLOCKED
								| Collision.PROJECTILE_SOUTH_BLOCKED | Collision.PROJECTILE_SOUTH_EAST_BLOCKED | Collision.PROJECTILE_EAST_BLOCKED)) != 0
								|| (Collision.getClipping(currentX + i - 1, currentY + i2, height) & (Collision.UNLOADED_TILE
								| /* BLOCKED_TILE | */Collision.UNKNOWN | Collision.PROJECTILE_TILE_BLOCKED | Collision.PROJECTILE_EAST_BLOCKED)) != 0
								|| (Collision.getClipping(currentX + i, currentY + i2 + 1, height) & (Collision.UNLOADED_TILE
								| /* BLOCKED_TILE | */Collision.UNKNOWN | Collision.PROJECTILE_TILE_BLOCKED | Collision.PROJECTILE_SOUTH_BLOCKED)) != 0) {
							return false;
						}
					} else if (diffX > 0 && diffY < 0) {
						if ((Collision.getClipping(currentX + i + 1, currentY + i2 - 1, height) & (Collision.UNLOADED_TILE
								| /* BLOCKED_TILE | */Collision.UNKNOWN | Collision.PROJECTILE_TILE_BLOCKED
								| Collision.PROJECTILE_WEST_BLOCKED | Collision.PROJECTILE_NORTH_BLOCKED | Collision.PROJECTILE_NORTH_WEST_BLOCKED)) != 0
								|| (Collision.getClipping(currentX + i + 1, currentY + i2, height) & (Collision.UNLOADED_TILE
								| /* BLOCKED_TILE | */Collision.UNKNOWN | Collision.PROJECTILE_TILE_BLOCKED | Collision.PROJECTILE_WEST_BLOCKED)) != 0
								|| (Collision.getClipping(currentX + i, currentY + i2 - 1, height) & (Collision.UNLOADED_TILE
								| /* BLOCKED_TILE | */Collision.UNKNOWN | Collision.PROJECTILE_TILE_BLOCKED | Collision.PROJECTILE_NORTH_BLOCKED)) != 0) {
							return false;
						}
					} else if (diffX > 0 && diffY == 0) {
						if ((Collision.getClipping(currentX + i + 1, currentY + i2, height) & (Collision.UNLOADED_TILE
								| /* BLOCKED_TILE | */Collision.UNKNOWN | Collision.PROJECTILE_TILE_BLOCKED | Collision.PROJECTILE_WEST_BLOCKED)) != 0) {
							return false;
						}
					} else if (diffX < 0 && diffY == 0) {
						if ((Collision.getClipping(currentX + i - 1, currentY + i2, height) & (Collision.UNLOADED_TILE
								| /* BLOCKED_TILE | */Collision.UNKNOWN | Collision.PROJECTILE_TILE_BLOCKED | Collision.PROJECTILE_EAST_BLOCKED)) != 0) {
							return false;
						}
					} else if (diffX == 0 && diffY > 0) {
						if ((Collision.getClipping(currentX + i, currentY + i2 + 1, height) & (Collision.UNLOADED_TILE
								| /* BLOCKED_TILE | */Collision.UNKNOWN | Collision.PROJECTILE_TILE_BLOCKED | Collision.PROJECTILE_SOUTH_BLOCKED)) != 0) {
							return false;
						}
					} else if (diffX == 0 && diffY < 0) {
						if ((Collision.getClipping(currentX + i, currentY + i2 - 1, height) & (Collision.UNLOADED_TILE
								| /* BLOCKED_TILE | */Collision.UNKNOWN | Collision.PROJECTILE_TILE_BLOCKED | Collision.PROJECTILE_NORTH_BLOCKED)) != 0) {
							return false;
						}
					}
				}
			}
			if (diffX < 0) {
				diffX++;
			} else if (diffX > 0) {
				diffX--;
			}
			if (diffY < 0) {
				diffY++; // change
			} else if (diffY > 0) {
				diffY--;
			}
		}
		return true;
	}

	public static void clipObject(int objectId, int x, int y, int z, int type, int rotation) {

		// zulrah pillar
		if (x == 2268 && y == 3067) {
			return;
		}


		if(objectId == 4188) {
			System.out.println(type + " " + rotation);
		}

		// remove trees at avatar of destruction
		if (x >= 3116 && x <= 3159 && y >= 3778 && y <= 3849) {
			ObjectDef def = ObjectDef.forID(objectId);
			if (def != null && def.name != null && def.name.startsWith("Tree")) {
				ObjectManager.removedObjects.add(new RemovedObject(Location.create(x, y, z), type));
				return;
			}
		}


		// remove trees in zombies minigame
		if (x >= 4139 && x <= 4156 && y >= 4010 && y <= 4026) {
			ObjectDef def = ObjectDef.forID(objectId);
			if (def != null && def.name != null && def.name.equalsIgnoreCase("tree")) {
				return;
			}
		}
		if (x >= 4108 && x <= 4124 && y >= 4002 && y <= 4018) {
			ObjectDef def = ObjectDef.forID(objectId);
			if (def != null && def.name != null && def.name.equalsIgnoreCase("tree")) {
				return;
			}
		}
		if (x >= 4108 && x <= 4124 && y >= 3978 && y <= 3992) {
			ObjectDef def = ObjectDef.forID(objectId);
			if (def != null && def.name != null && def.name.equalsIgnoreCase("tree")) {
				return;
			}
		}
		if (x >= 4136 && x <= 4152 && y >= 3982 && y <= 3998) {
			ObjectDef def = ObjectDef.forID(objectId);
			if (def != null && def.name != null && def.name.equalsIgnoreCase("tree")) {
				return;
			}
		}
		if (x >= 4145 && x <= 4165 && y >= 4000 && y <= 4012) {
			ObjectDef def = ObjectDef.forID(objectId);
			if (def != null && def.name != null && def.name.equalsIgnoreCase("tree")) {
				return;
			}
		}

		Location location = Location.create(x, y, z);
		Region region = Server.getRegionRepository().fromLocation(location);

		// make sure the object doesnt already exist
		Set<MapObject> mapObjects = region.getEntities(location, Entity.EntityType.STATIC_OBJECT, Entity.EntityType.DYNAMIC_OBJECT);
		Optional<MapObject> optional = mapObjects.stream().filter(o -> o.getId() == objectId).findFirst();
		if (!optional.isPresent()) {
			region.addEntity(new StaticMapObject(objectId, location, type, rotation));
		}

		/*
		 * for (int door : DOOR_IDS) { if (door == objectId) { ObjectManager.removeObject(x, y, z, type, rotation);
		 * return; } }
		 */

        /*if (x == 3208 && y == 3211 && z == 0) {
			ObjectManager.removeObject(x, y, z, type, rotation);
        }*/

		ObjectDef definition = ObjectDef.forID(objectId);
		if (definition == null) {
			return;
		}

        /*if (definition.name != null
				&& (definition.name.toLowerCase().contains("door") && !definition.name.equals("Trapdoor") || definition.name
                .toLowerCase().contains("gate"))) {
            DoorHandler.addDoor(new Door(objectId, x, y, z, rotation, type));
        }*/

		int xLength;
		int yLength;
		if (rotation != 1 && rotation != 3) {
			xLength = definition.width;
			yLength = definition.height;
		} else {
			xLength = definition.height;
			yLength = definition.width;
		}
		if (!definition.isUnwalkable) {
			return;
		}

		if (objectId == 29316) {
			System.out.println("object type: " + type + " rotation: " + rotation);
		}

		if (type >= 0 && type <= 3) {
			if (type == 0) {
				if (rotation == 0) {
					Collision.flag(x, y, z, Collision.WEST_BLOCKED);
					Collision.flag(x - 1, y, z, Collision.EAST_BLOCKED);
				} else if (rotation == 1) {
					Collision.flag(x, y, z, Collision.NORTH_BLOCKED);
					Collision.flag(x, y + 1, z, Collision.SOUTH_BLOCKED);
				} else if (rotation == 2) {
					Collision.flag(x, y, z, Collision.EAST_BLOCKED);
					Collision.flag(x + 1, y, z, Collision.WEST_BLOCKED);
				} else if (rotation == 3) {
					Collision.flag(x, y, z, Collision.SOUTH_BLOCKED);
					Collision.flag(x, y - 1, z, Collision.NORTH_BLOCKED);
				}
			} else if (type == 1 || type == 3) {
				if (rotation == 0) {
					Collision.flag(x, y, z, Collision.NORTH_WEST_BLOCKED);
					Collision.flag(x - 1, y + 1, z, Collision.SOUTH_EAST_BLOCKED);
				} else if (rotation == 1) {
					Collision.flag(x, y, z, Collision.NORTH_EAST_BLOCKED);
					Collision.flag(x + 1, y + 1, z, Collision.SOUTH_WEST_BLOCKED);
				} else if (rotation == 2) {
					Collision.flag(x, y, z, Collision.SOUTH_EAST_BLOCKED);
					Collision.flag(x + 1, y - 1, z, Collision.NORTH_WEST_BLOCKED);
				} else if (rotation == 3) {
					Collision.flag(x, y, z, Collision.SOUTH_WEST_BLOCKED);
					Collision.flag(x - 1, y - 1, z, Collision.NORTH_EAST_BLOCKED);
				}
			} else if (type == 2) {
				if (rotation == 0) {
					Collision.flag(x, y, z, Collision.NORTH_BLOCKED | Collision.WEST_BLOCKED);
					Collision.flag(x - 1, y, z, Collision.EAST_BLOCKED);
					Collision.flag(x, y + 1, z, Collision.SOUTH_BLOCKED);
				} else if (rotation == 1) {
					Collision.flag(x, y, z, Collision.NORTH_BLOCKED | Collision.EAST_BLOCKED);
					Collision.flag(x, y + 1, z, Collision.SOUTH_BLOCKED);
					Collision.flag(x + 1, y, z, Collision.WEST_BLOCKED);
				} else if (rotation == 2) {
					Collision.flag(x, y, z, Collision.SOUTH_BLOCKED | Collision.EAST_BLOCKED);
					Collision.flag(x + 1, y, z, Collision.WEST_BLOCKED);
					Collision.flag(x, y - 1, z, Collision.NORTH_BLOCKED);
				} else if (rotation == 3) {
					Collision.flag(x, y, z, Collision.SOUTH_BLOCKED | Collision.WEST_BLOCKED);
					Collision.flag(x, y - 1, z, Collision.NORTH_BLOCKED);
					Collision.flag(x - 1, y, z, Collision.EAST_BLOCKED);
				}
			}
			if (definition.impenetrable) {
				if (type == 0) {
					if (rotation == 0) {
						Collision.flag(x, y, z, Collision.PROJECTILE_WEST_BLOCKED);
						Collision.flag(x - 1, y, z, Collision.PROJECTILE_EAST_BLOCKED);
					} else if (rotation == 1) {
						Collision.flag(x, y, z, Collision.PROJECTILE_NORTH_BLOCKED);
						Collision.flag(x, y + 1, z, Collision.PROJECTILE_SOUTH_BLOCKED);
					} else if (rotation == 2) {
						Collision.flag(x, y, z, Collision.PROJECTILE_EAST_BLOCKED);
						Collision.flag(x + 1, y, z, Collision.PROJECTILE_WEST_BLOCKED);
					} else if (rotation == 3) {
						Collision.flag(x, y, z, Collision.PROJECTILE_SOUTH_BLOCKED);
						Collision.flag(x, y - 1, z, Collision.PROJECTILE_NORTH_BLOCKED);
					}
				} else if (type == 1 || type == 3) {
					if (rotation == 0) {
						Collision.flag(x, y, z, Collision.PROJECTILE_NORTH_WEST_BLOCKED);
						Collision.flag(x - 1, y + 1, z, Collision.PROJECTILE_SOUTH_EAST_BLOCKED);
					} else if (rotation == 1) {
						Collision.flag(x, y, z, Collision.PROJECTILE_NORTH_EAST_BLOCKED);
						Collision.flag(x + 1, y + 1, z, Collision.PROJECTILE_SOUTH_WEST_BLOCKED);
					} else if (rotation == 2) {
						Collision.flag(x, y, z, Collision.PROJECTILE_SOUTH_EAST_BLOCKED);
						Collision.flag(x + 1, y - 1, z, Collision.PROJECTILE_NORTH_WEST_BLOCKED);
					} else if (rotation == 3) {
						Collision.flag(x, y, z, Collision.PROJECTILE_SOUTH_WEST_BLOCKED);
						Collision.flag(x - 1, y - 1, z, Collision.PROJECTILE_NORTH_EAST_BLOCKED);
					}
				} else if (type == 2) {
					if (rotation == 0) {
						Collision.flag(x, y, z, Collision.PROJECTILE_NORTH_BLOCKED | Collision.PROJECTILE_WEST_BLOCKED);
						Collision.flag(x - 1, y, z, Collision.PROJECTILE_EAST_BLOCKED);
						Collision.flag(x, y + 1, z, Collision.PROJECTILE_SOUTH_BLOCKED);
					} else if (rotation == 1) {
						Collision.flag(x, y, z, Collision.PROJECTILE_NORTH_BLOCKED | Collision.PROJECTILE_EAST_BLOCKED);
						Collision.flag(x, y + 1, z, Collision.PROJECTILE_SOUTH_BLOCKED);
						Collision.flag(x + 1, y, z, Collision.PROJECTILE_WEST_BLOCKED);
					} else if (rotation == 2) {
						Collision.flag(x, y, z, Collision.PROJECTILE_EAST_BLOCKED | Collision.PROJECTILE_SOUTH_BLOCKED);
						Collision.flag(x + 1, y, z, Collision.PROJECTILE_WEST_BLOCKED);
						Collision.flag(x, y - 1, z, Collision.PROJECTILE_NORTH_BLOCKED);
					} else if (rotation == 3) {
						Collision.flag(x, y, z, Collision.PROJECTILE_SOUTH_BLOCKED | Collision.PROJECTILE_WEST_BLOCKED);
						Collision.flag(x, y - 1, z, Collision.PROJECTILE_NORTH_BLOCKED);
						Collision.flag(x - 1, y, z, Collision.PROJECTILE_EAST_BLOCKED);
					}
				}
			}
		} else if (type >= 4 && type <= 8) {// Wall Decoration
		} else if ((type == 9) || (type == 10 || type == 11) || (type >= 12 && type <= 21)) {// Diagonal Wall ||
			// Interactive
			// GameObject || Roof
			int mask = Collision.TILE_BLOCKED;
			if (definition.impenetrable) {
				mask += Collision.PROJECTILE_TILE_BLOCKED;
			}

			for (int xOff = 0; xOff < xLength; xOff++) {
				for (int yOff = 0; yOff < yLength; yOff++) {
					Collision.flag(x + xOff, y + yOff, z, mask);
				}
			}
		} else if (type == 22) {// Floor Decoration
			if (definition.hasActions) {
				Collision.flag(x, y, z, Collision.GROUND_TILE_BLOCKED);
			}
		} else {
			// System.err.println("Unknown object type: " + type + " object: " + objectId + " loc: (" + x + ", " + y +
			// ", " + z + ')');
		}
	}

	public static void flag(int x, int y, int height, int shift) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = (regionX / 8 << 8) + regionY / 8;
		Collision collision = regions.get(regionId);
		if (collision != null) {
			collision.addClip(x, y, height, shift);
		}
	}

	public static int getClipping(int x, int y, int height) {
		height %= 4;
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) | (regionY / 8);
		Collision collision = regions.get(regionId);
		if (collision == null) {
			return 0;
		}
		return collision.getClip(x, y, height);
	}

	public static boolean getClipping(int x, int y, int height, int moveTypeX, int moveTypeY) {
		try {
			if (height > 3)
				height = 0;
			int checkX = (x + moveTypeX);
			int checkY = (y + moveTypeY);
			if (moveTypeX == -1 && moveTypeY == 0)
				return (getClipping(x, y, height) & 0x1280108) == 0;
			else if (moveTypeX == 1 && moveTypeY == 0)
				return (getClipping(x, y, height) & 0x1280180) == 0;
			else if (moveTypeX == 0 && moveTypeY == -1)
				return (getClipping(x, y, height) & 0x1280102) == 0;
			else if (moveTypeX == 0 && moveTypeY == 1)
				return (getClipping(x, y, height) & 0x1280120) == 0;
			else if (moveTypeX == -1 && moveTypeY == -1)
				return ((getClipping(x, y, height) & 0x128010e) == 0 && (getClipping(checkX - 1, checkY, height) & 0x1280108) == 0 && (getClipping(checkX - 1, checkY, height) & 0x1280102) == 0);
			else if (moveTypeX == 1 && moveTypeY == -1)
				return ((getClipping(x, y, height) & 0x1280183) == 0 && (getClipping(checkX + 1, checkY, height) & 0x1280180) == 0 && (getClipping(
						checkX, checkY - 1, height) & 0x1280102) == 0);
			else if (moveTypeX == -1 && moveTypeY == 1)
				return ((getClipping(x, y, height) & 0x1280138) == 0 && (getClipping(checkX - 1, checkY, height) & 0x1280108) == 0 && (getClipping(
						checkX, checkY + 1, height) & 0x1280120) == 0);
			else if (moveTypeX == 1 && moveTypeY == 1)
				return ((getClipping(x, y, height) & 0x12801e0) == 0 && (getClipping(checkX + 1, checkY, height) & 0x1280180) == 0 && (getClipping(
						checkX, checkY + 1, height) & 0x1280120) == 0);
			else {
				System.out.println("[FATAL ERROR]: At getClipping: " + x + ", " + y + ", " + height + ", " + moveTypeX + ", " + moveTypeY);
				return false;
			}
		} catch (Exception e) {
			return true;
		}
	}

	public static int getUnsignedSmart(ByteBuffer buf) {
		int peek = buf.get(buf.position()) & 0xFF;
		if (peek < 128) {
			return buf.get() & 0xFF;
		}
		return (buf.getShort() & 0xFFFF) - 32768;
	}

	public static void loadMaps(int regionId, Stream str1, Stream str2) {

		if (regions.containsKey(regionId)) {
			regions.remove(regionId);
		}

		regions.put(regionId, new Collision(regionId, false));

		int x = ((regionId >> 8) & 0xFF) * 64;
		int y = (regionId & 0xFF) * 64;
		byte[][][] tileFlags = new byte[4][64][64];

		for (int z = 0; z < 4; z++) {
			for (int xOff = 0; xOff < 64; xOff++) {
				for (int yOff = 0; yOff < 64; yOff++) {
					while (true) {
						int opcode = str2.readUnsignedByte();
						if (opcode == 0) {
							break;
						}
						if (opcode == 1) {
							str2.readUnsignedByte();
							break;
						}
						if (opcode <= 49) {
							str2.readUnsignedByte();
						} else if (opcode <= 81) {
							tileFlags[z][xOff][yOff] = (byte) (opcode - 49);
						}
					}
				}
			}
		}

		for (int z = 0; z < 4; z++) {
			for (int xOff = 0; xOff < 64; xOff++) {
				for (int yOff = 0; yOff < 64; yOff++) {
					if ((tileFlags[z][xOff][yOff] & 1) == 1) {
						int height_level = z;
						if ((tileFlags[1][xOff][yOff] & 2) == 2) {
							height_level--;
						}
						if (height_level >= 0) {
							Collision.flag(x + xOff, y + yOff, height_level, GROUND_TILE_BLOCKED);
						}
					}
				}
			}
		}

		int objectId = -1;
		while (true) {
			int objectIdOffset = str1.method422();
			if (objectIdOffset == 0) {
				break;
			}
			objectId += objectIdOffset;
			int objectLocInfo = 0;
			while (true) {
				int objectLocInfoOffset = str1.method422();
				if (objectLocInfoOffset == 0) {
					break;
				}
				objectLocInfo += objectLocInfoOffset - 1;
				int relativeX = objectLocInfo >> 6 & 0x3f;
				int relativeY = objectLocInfo & 0x3f;
				int plane = objectLocInfo >> 12;
				int objectInfo = str1.readUnsignedByte();
				int type = objectInfo >> 2;
				int rotation = objectInfo & 3;
				if ((tileFlags[1][relativeX][relativeY] & 0x2) == 2) {
					plane--;
					if (plane < 0) {
						continue;
					}
				}
				Collision.clipObject(objectId, relativeX + x, relativeY + y, plane, type, rotation);
			}
		}

	}

	public static void removeClipping(int x, int y, int z) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = (regionX / 8 << 8) + regionY / 8;
		Collision collision = regions.get(regionId);
		if (collision != null) {
			collision.removeClip(x, y, z);
		}
	}

	/**
	 * @param objectId
	 * @param objLoc
	 * @param rotation
	 * @param type
	 */
	public static void removeObjectClipping(int objectId, Location objLoc, int rotation, int type) {
		if (type == 0) {
			switch (rotation) {
				case 0:
					Collision.removeClipping(objLoc.getX(), objLoc.getY(), objLoc.getZ());
					Collision.removeClipping(objLoc.getX() - 1, objLoc.getY(), objLoc.getZ());
					break;
				case 1:
					Collision.removeClipping(objLoc.getX(), objLoc.getY(), objLoc.getZ());
					Collision.removeClipping(objLoc.getX(), objLoc.getY() + 1, objLoc.getZ());
					break;
				case 2:
					Collision.removeClipping(objLoc.getX(), objLoc.getY(), objLoc.getZ());
					Collision.removeClipping(objLoc.getX() + 1, objLoc.getY(), objLoc.getZ());
					break;
				case 3:
					Collision.removeClipping(objLoc.getX(), objLoc.getY(), objLoc.getZ());
					Collision.removeClipping(objLoc.getX(), objLoc.getY() - 1, objLoc.getZ());
					break;
			}
		} else if (type == 10) {

			ObjectDef def = ObjectDef.forID(objectId);
			if (def == null) {
				return;
			}

			int xLength;
			int yLength;
			if (rotation != 1 && rotation != 3) {
				xLength = def.width;
				yLength = def.height;
			} else {
				xLength = def.height;
				yLength = def.width;
			}

			for (int xOff = 0; xOff < xLength; xOff++) {
				for (int yOff = 0; yOff < yLength; yOff++) {
					Collision.removeClipping(objLoc.getX() + xOff, objLoc.getY() + yOff, objLoc.getZ());
				}
			}
		} else {
			logger.warning("unsupported object type: " + type);
		}
	}

	private void addClip(int x, int y, int height, int shift) {
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (clips == null) {
			clips = new int[4][][];
		}
		if (clips[height] == null) {
			clips[height] = new int[64][64];
		}
		clips[height][x - regionAbsX][y - regionAbsY] |= shift;
	}

	private int getClip(int x, int y, int height) {
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (height < 0) {
			height = 0;
		}
		if (clips == null || clips[height] == null) {
			return 0;
		} else {
			return clips[height][x - regionAbsX][y - regionAbsY];
		}
	}

	public int id() {
		return id;
	}

	private void removeClip(int x, int y, int height) {
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (clips == null) {
			clips = new int[4][][];
		}
		if (clips[height] == null) {
			clips[height] = new int[64][64];
		}
		clips[height][x - regionAbsX][y - regionAbsY] = 0;
	}

}