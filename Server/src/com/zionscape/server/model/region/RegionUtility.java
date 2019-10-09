package com.zionscape.server.model.region;

import com.zionscape.server.Server;
import com.zionscape.server.model.Entity;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.MapObject;

import java.util.Optional;
import java.util.Set;

public final class RegionUtility {

	public static void addEntity(Entity entity, Location location) {
		Region region = Server.getRegionRepository().fromLocation(location);
		region.addEntity(entity);
	}

	public static void removeEntity(Entity entity, Location location) {
		Region region = Server.getRegionRepository().fromLocation(location);
		region.removeEntity(entity);
	}

	public static Optional<MapObject> getMapObject(int objectId, Location location) {
		Region region = Server.getRegionRepository().fromLocation(location);
		Set<MapObject> mapObjects = region.getEntities(location, Entity.EntityType.STATIC_OBJECT, Entity.EntityType.DYNAMIC_OBJECT);

		return mapObjects.stream().filter(x -> x.getId() == objectId).findFirst();
	}


	public static Set<MapObject> getMapObject(Location location) {
		Region region = Server.getRegionRepository().fromLocation(location);
		Set<MapObject> mapObjects = region.getEntities(location, Entity.EntityType.STATIC_OBJECT, Entity.EntityType.DYNAMIC_OBJECT);

		return mapObjects;
	}

}
