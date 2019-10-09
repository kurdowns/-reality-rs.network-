package com.zionscape.server.model.content.minigames.castlewars;

import com.zionscape.server.Server;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.objects.GameObject;

public class Rope {

	private GameObject gameObject;
	private Location location;
	private int ticksLeft = Constants.ROPES_ACTIVE_FOR;

	public Rope(Location location) {
		this.location = location;

		gameObject = new GameObject(5946, location.getX(), location.getY(), location.getZ(), 0, 10, -1, -1);
	}

	public static boolean validLocation(Location location) {

		// zamorak castle
		if (location.getX() >= 2376 && location.getX() <= 2380 && location.getY() == 3117) {
			return true;
		}

		if (location.getX() >= 2383 && location.getX() <= 2385 && location.getY() == 3115) {
			return true;
		}

		if (location.getX() == 2386 && location.getY() >= 3123 && location.getY() <= 3132) {
			return true;
		}

		if (location.getX() == 2388 && location.getY() >= 3118 && location.getY() <= 3120) {
			return true;
		}

		// saradomin castle
		if (location.getX() == 2413 && location.getY() >= 3075 && location.getY() <= 3084) {
			return true;
		}
		if (location.getX() == 2411 && location.getY() >= 3087 && location.getY() <= 3089) {
			return true;
		}
		if (location.getX() >= 2414 && location.getX() <= 246 && location.getY() == 3092) {
			return true;
		}
		if (location.getX() >= 2419 && location.getX() <= 2423 && location.getY() == 3090) {
			return true;
		}

		return false;
	}

	public static int[] movePlayerBy(Location location) {

		// zamorak castle
		if (location.getX() >= 2376 && location.getX() <= 2380 && location.getY() == 3117) {
			return new int[]{0, 1};
		}

		if (location.getX() >= 2383 && location.getX() <= 2385 && location.getY() == 3115) {
			return new int[]{0, 1};
		}

		if (location.getX() == 2386 && location.getY() >= 3123 && location.getY() <= 3132) {
			return new int[]{-1, 0};
		}

		if (location.getX() == 2388 && location.getY() >= 3118 && location.getY() <= 3120) {
			return new int[]{-1, 0};
		}

		// saradomin castle
		if (location.getX() == 2413 && location.getY() >= 3075 && location.getY() <= 3084) {
			return new int[]{1, 0};
		}
		if (location.getX() == 2411 && location.getY() >= 3087 && location.getY() <= 3089) {
			return new int[]{1, 0};
		}
		if (location.getX() >= 2414 && location.getX() <= 246 && location.getY() == 3092) {
			return new int[]{0, -1};
		}
		if (location.getX() >= 2419 && location.getX() <= 2423 && location.getY() == 3090) {
			return new int[]{0, -1};
		}

		return null;
	}

	public int decrementTicksLeft() {
		return --ticksLeft;
	}

	public int getTicksLeft() {
		return ticksLeft;
	}

	public GameObject getGameObject() {
		return gameObject;
	}

	public void destroy() {
		Server.objectManager.removeObject(gameObject);
		Server.objectManager.objects.remove(gameObject);
	}

	public Location getLocation() {
		return location;
	}

}
