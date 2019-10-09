package com.zionscape.server.util.pathfinding;

import com.zionscape.server.cache.Collision;
import com.zionscape.server.model.Location;

import java.util.HashMap;
import java.util.Map;

public class CollisionMap {

	private final Map<Location, Integer> clippingMasks = new HashMap<>();

	/**
	 * @param southWest
	 * @param northEast
	 * @param height
	 */
	public CollisionMap(Location southWest, Location northEast, int height) {
		for (int x = southWest.getX(); x < northEast.getX(); x++) {
			for (int y = southWest.getY(); y < northEast.getY(); y++) {
				clippingMasks.put(Location.create(x, y), Collision.getClipping(x, y, height));
			}
		}
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public int getClipping(int x, int y) {
		Location location = Location.create(x, y);

		if (clippingMasks.containsKey(location)) {
			return clippingMasks.get(location);
		}

		return Collision.TILE_BLOCKED;
	}

	/**
	 * @param x
	 * @param y
	 * @param mask
	 */
	public void setClipping(int x, int y, int mask) {
		clippingMasks.put(Location.create(x, y), mask);
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean blockedEast(int x, int y) {
		return (getClipping(x + 1, y) & 0x1280180) != 0;
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean blockedNorth(int x, int y) {
		return (getClipping(x, y + 1) & 0x1280120) != 0;
	}


	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean blockedNorthEast(int x, int y) {
		return (getClipping(x + 1, y + 1) & 0x12801e0) != 0;
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean blockedNorthWest(int x, int y) {
		return (getClipping(x - 1, y + 1) & 0x1280138) != 0;
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean blockedSouth(int x, int y) {
		return (getClipping(x, y - 1) & 0x1280102) != 0;
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean blockedSouthEast(int x, int y) {
		return (getClipping(x + 1, y - 1) & 0x1280183) != 0;
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean blockedSouthWest(int x, int y) {
		return (getClipping(x - 1, y - 1) & 0x128010e) != 0;
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean blockedWest(int x, int y) {
		return (getClipping(x - 1, y) & 0x1280108) != 0;
	}


}
