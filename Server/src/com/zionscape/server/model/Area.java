package com.zionscape.server.model;

/**
 * @author Stuart.
 * @created {$DATA}.
 */
public final class Area {

	private final Location southWest;
	private final Location northEast;
	private int height;

	/**
	 * Creates a new area
	 *
	 * @param leastX The most western cord
	 * @param leastY The most southern cord
	 * @param mostX  The most eastern cord
	 * @param mostY  The most northern cord
	 * @param height The height of this area use -1 for all
	 */
	public Area(int leastX, int leastY, int mostX, int mostY, int height) {
		this.southWest = Location.create(leastX, leastY, -1);
		this.northEast = Location.create(mostX, mostY, -1);
		this.height = height;
	}

	/**
	 * Creates a new area
	 *
	 * @param southWest the most south western location
	 * @param northEast the most north westest location
	 * @param height    The height of this area use -1 for all
	 */
	public Area(Location southWest, Location northEast, int height) {
		this.southWest = southWest;
		this.northEast = northEast;
		this.height = height;
	}

	//x >= WEST && x <= EAST && y >= SOUTH && y <= NORTH

	/**
	 * is the given cords in this area
	 *
	 * @param x      the x cord
	 * @param y      the y cord
	 * @param height the height
	 * @return if the given cords are in this area
	 */
	public final boolean inArea(int x, int y, int height) {
		return inArea(Location.create(x, y, height));
	}

	/**
	 * Is the given location in this area
	 *
	 * @param location the location
	 * @return if the given location is in this area or not
	 */
	public final boolean inArea(Location location) {
		if (height > -1 && location.getZ() != height) {
			return false;
		}
		return location.getX() >= southWest.getX() && location.getX() <= northEast.getX() && location.getY() >= southWest.getY() && location.getY() <= northEast.getY();
	}

	public static boolean inArea(Area area, Entity entity) {
		return area.inArea(entity.getLocation());
	}

	public static boolean inArea(Area area, Location location) {
		return area.inArea(location);
	}

}