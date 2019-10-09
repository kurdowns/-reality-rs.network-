package com.zionscape.server.world.doors;

import com.google.common.base.Objects;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.StaticMapObject;

/**
 * Created with IntelliJ IDEA. User: Administrator Date: 12/7/13 Time: 5:08 PM To change this template use File |
 * Settings | File Templates.
 */
public final class Door {

	private final int id;
	private final Location location;
	private final int orientation;
	private final StaticMapObject originalObject;

	public Door(int id, Location location) {
		this.id = id;
		this.location = location;
		this.orientation = -1;
		this.originalObject = null;
	}

	public Door(int id, Location location, int orientation, StaticMapObject originalObject) {
		this.id = id;
		this.location = location;
		this.orientation = orientation;
		this.originalObject = originalObject;
	}

	public int getId() {
		return id;
	}

	public Location getLocation() {
		return location;
	}

	public int getOrientation() {
		return orientation;
	}

	public StaticMapObject getOriginalObject() {
		return originalObject;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o instanceof Location) {
			return Objects.equal(this.location, o);
		}

		if (o == null || getClass() != o.getClass()) return false;

		Door that = (Door) o;

		return Objects.equal(this.id, that.id) &&
				Objects.equal(this.location, that.location) &&
				Objects.equal(this.orientation, that.orientation) &&
				Objects.equal(this.originalObject, that.originalObject);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(id, location, orientation, originalObject);
	}

}