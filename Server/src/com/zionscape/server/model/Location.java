package com.zionscape.server.model;

import com.google.common.base.MoreObjects;
import com.zionscape.server.util.Misc;

import java.util.Objects;

public class Location {

	/**
	 * The packed integer containing the {@code height, x}, and {@code y} variables.
	 */
	private int x;
	private int y;
	private int height;

	/**
	 * Creates a location.
	 *
	 * @param x      The x coordinate.
	 * @param y      The y coordinate.
	 * @param height The z coordinate.
	 */
	private Location(int x, int y, int height) {
		this.x = x;
		this.y = y;
		this.height = height;
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public static Location create(int x, int y) {
		return new Location(x, y, 0);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static Location create(int x, int y, int z) {
		return new Location(x, y, z);
	}

	public static boolean isWithinDistance(Location src, Location dest, int distance) {
		return isWithinDistance(src, dest, 0, distance);
	}

	/**
	 * @param src
	 * @param dest
	 * @param size
	 * @param distance
	 * @return
	 */
	public static boolean isWithinDistance(Location src, Location dest, int size, int distance) {
		return src.getZ() == dest.getZ() && src.getX() >= (dest.getX() - distance) && src.getX() <= (dest.getX() + size + distance) && src.getY() >= (dest.getY() - distance) && src.getY() <= (dest.getY() + size + distance);
	}

	/**
	 * @param src
	 * @param dest
	 * @param sizeX
	 * @param sizeY
	 * @param distance
	 * @return
	 */
	public static boolean isWithinDistance(Location src, Location dest, int sizeX, int sizeY, int distance) {
		return src.getZ() == dest.getZ() && src.getX() >= (dest.getX() - distance) && src.getX() <= (dest.getX() + sizeX + distance) && src.getY() >= (dest.getY() - distance) && src.getY() <= (dest.getY() + sizeY + distance);
	}

	/**
	 * Gets the absolute x coordinate.
	 *
	 * @return The absolute x coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the absolute y coordinate.
	 *
	 * @return The absolute y coordinate.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Gets the z coordinate, or height.
	 *
	 * @return The z coordinate.
	 */
	public int getZ() {
		return height;
	}

	/**
	 * Gets the x coordinate of the region this position is in.
	 *
	 * @return The region x coordinate.
	 */
	public int getTopLeftRegionX() {
		return getX() / 8 - 6;
	}

	/**
	 * Gets the y coordinate of the region this position is in.
	 *
	 * @return The region y coordinate.
	 */
	public int getTopLeftRegionY() {
		return getY() / 8 - 6;
	}

	/**
	 * Checks if this location is within range of another.
	 *
	 * @param other The other location.
	 * @return <code>true</code> if the location is in range, <code>false</code>
	 * if not.
	 */
	public boolean isWithinDistance(Location other) {
		if (getZ() != other.getZ()) {
			return false;
		}
		int deltaX = other.getX() - getX(), deltaY = other.getY() - getY();
		return deltaX <= 14 && deltaX >= -15 && deltaY <= 14 && deltaY >= -15;
	}

	/**
	 * Gets the local x coordinate relative to this region.
	 *
	 * @return The local x coordinate relative to this region.
	 */
	public int getLocalX() {
		return this.getLocalX(this);
	}

	/**
	 * Gets the local y coordinate relative to this region.
	 *
	 * @return The local y coordinate relative to this region.
	 */
	public int getLocalY() {
		return this.getLocalY(this);
	}

	/**
	 * Gets the local x coordinate relative to a specific region.
	 *
	 * @param l The region the coordinate will be relative to.
	 * @return The local x coordinate.
	 */
	public int getLocalX(Location l) {
		return getX() - 8 * l.getRegionX();
	}

	/**
	 * Gets the local y coordinate relative to a specific region.
	 *
	 * @param l The region the coordinate will be relative to.
	 * @return The local y coordinate.
	 */
	public int getLocalY(Location l) {
		return getY() - 8 * l.getRegionY();
	}

	/**
	 * Gets the region x coordinate.
	 *
	 * @return The region x coordinate.
	 */
	public int getRegionX() {
		return (getX() >> 3) - 6;
	}

	/**
	 * Gets the region y coordinate.
	 *
	 * @return The region y coordinate.
	 */
	public int getRegionY() {
		return (getY() >> 3) - 6;
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean equals(int x, int y) {
		return this.getX() == x && this.getY() == y;
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public boolean equals(int x, int y, int z) {
		return this.getX() == x && this.getY() == y && this.getZ() == getZ();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Location)) {
			return false;
		}
		Location loc = (Location) other;
		return loc.getX() == getX() && loc.getY() == getY() && loc.getZ() == getZ();
	}

	/**
	 * Creates a new location based on this location.
	 *
	 * @param diffX X difference.
	 * @param diffY Y difference.
	 * @param diffZ Z difference.
	 * @return The new location.
	 */
	public Location transform(int diffX, int diffY, int diffZ) {
		return Location.create(getX() + diffX, getY() + diffY, getZ() + diffZ);
	}

	/**
	 * Calculate the distance between a player and a point.
	 *
	 * @param p A point.
	 * @return The square distance.
	 */
	public double getDistance(Location other) {
		int xdiff = this.getX() - other.getX();
		int ydiff = this.getY() - other.getY();
		return Math.sqrt(xdiff * xdiff + ydiff * ydiff);
	}

	/**
	 * Checks if this position is viewable from the other position.
	 *
	 * @param other the other position
	 * @return true if it is viewable, false otherwise
	 */
	public boolean isViewableFrom(Location other) {
		Location p = Misc.delta(this, other);
		return p.getX() <= 14 && p.getX() >= -15 && p.getY() <= 14 && p.getY() >= -15;
	}

	/**
	 * Checks if this location is within interaction range of another.
	 *
	 * @param other The other location.
	 * @return <code>true</code> if the location is in range, <code>false</code> if not.
	 */
	public boolean isWithinInteractionDistance(final Location other) {
		if (getZ() != other.getZ())
			return false;
		final int deltaX = other.getX() - getX(), deltaY = other.getY() - getY();
		return deltaX <= 2 && deltaX >= -3 && deltaY <= 2 && deltaY >= -3;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("x", getX())
				.add("y", getY())
				.add("z", getZ())
				.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getX(), getY(), getZ());
	}

}
