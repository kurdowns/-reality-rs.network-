package com.zionscape.server.model.region;

import com.zionscape.server.model.Location;

/**
 * An immutable class representing the coordinates of a region, where the coordinates ({@code x, y}) are the top-left of
 * the region.
 *
 * @author Graham
 * @author Major
 */
public final class RegionCoordinates {

	/**
	 * Gets the RegionCoordinates for the specified {@link Location}.
	 *
	 * @param Location The Location.
	 * @return The RegionCoordinates.
	 */
	public static RegionCoordinates fromLocation(Location location) {
		return new RegionCoordinates(location.getTopLeftRegionX(), location.getTopLeftRegionY());
	}

	/**
	 * The x coordinate of this region.
	 */
	private final int x;

	/**
	 * The y coordinate of this region.
	 */
	private final int y;

	/**
	 * Creates the RegionCoordinates.
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public RegionCoordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RegionCoordinates) {
			RegionCoordinates other = (RegionCoordinates) obj;
			return x == other.x && y == other.y;
		}

		return false;
	}

	/**
	 * Gets the absolute x coordinate of this Region (which can be compared directly against {@link Location#getX()}.
	 *
	 * @return The absolute x coordinate.
	 */
	public int getAbsoluteX() {
		return Region.SIZE * (x + 6);
	}

	/**
	 * Gets the absolute y coordinate of this Region (which can be compared directly against {@link Location#getY()}.
	 *
	 * @return The absolute y coordinate.
	 */
	public int getAbsoluteY() {
		return Region.SIZE * (y + 6);
	}

	/**
	 * Gets the x coordinate (equivalent to the {@link Location#getTopLeftRegionX()} of a Location within this region).
	 *
	 * @return The x coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y coordinate (equivalent to the {@link Location#getTopLeftRegionY()} of a Location within this region).
	 *
	 * @return The y coordinate.
	 */
	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		return x << 16 | y;
	}

}