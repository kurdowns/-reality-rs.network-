package com.zionscape.server.model.npcs;

import com.zionscape.server.model.Location;
import com.zionscape.server.util.pathfinding.NpcPathFinder;

public class NPCHelper {

	public static int getNpcIndex(int type) {
		for (NPC npc : NPCHandler.npcs) {
			if (npc == null) {
				continue;
			}
			if (npc.type == type) {
				return npc.id;
			}
		}
		return -1;
	}

	public static int[] findPath(Location src, Location dst) {
		if (!src.isWithinDistance(dst)) {
			return new int[2];
		}
		return findPath(src.getX(), src.getY(), dst.getX(), dst.getY(), src.getZ());
	}

	public static int[] findPath(int srcX, int srcY, int dstX, int dstY, int height) {
		Location src = Location.create(srcX, srcY, height);
		int stepX = 0, stepY = 0;
		int[] path = new int[2];
		if (srcX > dstX && NpcPathFinder.canMove(src, NpcPathFinder.Move.WEST, 1)) {
			stepX = -1;
		} else if (srcX < dstX && NpcPathFinder.canMove(src, NpcPathFinder.Move.EAST, 1)) {
			stepX = 1;
		}
		if (srcY > dstY && NpcPathFinder.canMove(src, NpcPathFinder.Move.SOUTH, 1)) {
			stepY = -1;
		} else if (srcY < dstY && NpcPathFinder.canMove(src, NpcPathFinder.Move.NORTH, 1)) {
			stepY = 1;
		}
		if (stepX != 0 || stepY != 0) {
			path[0] = stepX;
			path[1] = stepY;
		}
		return path;
	}

}
