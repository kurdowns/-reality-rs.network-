package com.zionscape.server.model.region;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.zionscape.server.model.Entity;
import com.zionscape.server.model.Location;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An 8x8 area of the map.
 *
 * @author Major
 */
public final class Region {

	/**
	 * The width and length of a Region, in tiles.
	 */
	public static final int SIZE = 8;

	/**
	 * The default size of newly-created sets, to reduce memory usage.
	 */
	private static final int DEFAULT_SET_SIZE = 2;

	/**
	 * The RegionCoordinates of this Region.
	 */
	private final RegionCoordinates coordinates;

	/**
	 * The Map of Locations to Entities in that Location.
	 */
	private final Map<Location, Set<Entity>> entities = new HashMap<>();

	/**
	 * Creates a new Region.
	 *
	 * @param x The x coordinate of the Region.
	 * @param y The y coordinate of the Region.
	 */
	public Region(int x, int y) {
		this(new RegionCoordinates(x, y));
	}

	/**
	 * Creates a new Region with the specified {@link RegionCoordinates}.
	 *
	 * @param coordinates The coordinates.
	 */
	public Region(RegionCoordinates coordinates) {
		this.coordinates = coordinates;
	}

	/**
	 * Adds a {@link Entity} from to Region. Note that this does not spawn the Entity, or do any other action other than
	 * register it to this Region.
	 *
	 * @param entity The Entity.
	 * @throws IllegalArgumentException If the Entity does not belong in this Region.
	 */
	public void addEntity(Entity entity) {
		Location Location = entity.getLocation();

		try {
			checkLocation(Location);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		Set<Entity> local = entities.computeIfAbsent(Location, key -> new HashSet<>(DEFAULT_SET_SIZE));
		local.add(entity);
	}

	/**
	 * Checks if this Region contains the specified Entity.
	 * <p>
	 * This method operates in constant time.
	 *
	 * @param entity The Entity.
	 * @return {@code true} if this Region contains the Entity, otherwise {@code false}.
	 */
	public boolean contains(Entity entity) {
		Location Location = entity.getLocation();
		Set<Entity> local = entities.get(Location);

		return local != null && local.contains(entity);
	}

	/**
	 * Gets this Region's {@link RegionCoordinates}.
	 *
	 * @return The Region coordinates.
	 */
	public RegionCoordinates getCoordinates() {
		return coordinates;
	}

	/**
	 * Gets a shallow copy of the {@link Set} of {@link Entity} objects at the specified {@link Location}. The returned
	 * type will be immutable.
	 *
	 * @param location The Location containing the entities.
	 * @return The list.
	 */
	public Set<Entity> getEntities(Location location) {
		Set<Entity> set = entities.get(location);

		return (set == null) ? ImmutableSet.of() : ImmutableSet.copyOf(set);
	}

	/**
	 * Gets a shallow copy of the {@link Set} of {@link Entity}s with the specified {@link EntityType}(s). The returned
	 * type will be immutable. Type will be inferred from the call, so ensure that the Entity type and the reference
	 * correspond, or this method will fail at runtime.
	 *
	 * @param location The {@link Location} containing the entities.
	 * @param types    The {@link EntityType}s.
	 * @return The set of entities.
	 */
	public <T extends Entity> Set<T> getEntities(Location location, Entity.EntityType... types) {
		Set<Entity> local = entities.get(location);
		if (local == null) {
			return ImmutableSet.of();
		}

		Set<Entity.EntityType> set = new HashSet<>(Arrays.asList(types));
		@SuppressWarnings("unchecked")
		Set<T> filtered = (Set<T>) local.stream().filter(entity -> set.contains(entity.getEntityType())).collect(Collectors.toSet());
		return ImmutableSet.copyOf(filtered);
	}

	/**
	 * Removes a {@link Entity} from this Region.
	 *
	 * @param entity The Entity.
	 * @throws IllegalArgumentException If the Entity does not belong in this Region, or if it was never added.
	 */
	public void removeEntity(Entity entity) {
		Location Location = entity.getLocation();
		checkLocation(Location);

		Set<Entity> local = entities.get(Location);

		if (local == null || !local.remove(entity)) {
			throw new IllegalArgumentException("Entity belongs in this Region (" + this + ") but does not exist.");
		}
	}

	/**
	 * Checks that the specified {@link Location} is included in this Region.
	 *
	 * @param Location The Location.
	 * @throws IllegalArgumentException If the specified Location is not included in this Region.
	 */
	private void checkLocation(Location Location) {
		Preconditions.checkArgument(coordinates.equals(RegionCoordinates.fromLocation(Location)), "Location is not included in this Region.");
	}

}
