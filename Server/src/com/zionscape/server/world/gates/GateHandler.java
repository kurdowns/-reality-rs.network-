package com.zionscape.server.world.gates;

import com.zionscape.server.Server;
import com.zionscape.server.model.DynamicMapObject;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.objects.GameObject;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.region.RegionUtility;

/**
 * this is trash and needs to be done properly but works for now
 */
public final class GateHandler {

	static {

		// edgeville gate
		RegionUtility.addEntity(new DynamicMapObject(15514, Location.create(3080, 3500, 0), 0, 2), Location.create(3080, 3500, 0));
		RegionUtility.addEntity(new DynamicMapObject(15516, Location.create(3080, 3499, 0), 0, 2), Location.create(3080, 3499, 0));

		// chickens at lumbridge
		RegionUtility.addEntity(new DynamicMapObject(15513, Location.create(3236, 3295, 0), 0, 3), Location.create(3236, 3295, 0));
		RegionUtility.addEntity(new DynamicMapObject(15515, Location.create(3235, 3295, 0), 0, 3), Location.create(3235, 3295, 0));

		// soth of chickens at lumby
		RegionUtility.addEntity(new DynamicMapObject(36913, Location.create(3236, 3284, 0), 0, 3), Location.create(3236, 3284, 0));
		RegionUtility.addEntity(new DynamicMapObject(36915, Location.create(3235, 3284, 0), 0, 3), Location.create(3235, 3284, 0));

		// lumbridge cows
		RegionUtility.addEntity(new DynamicMapObject(15514, Location.create(3251, 3265, 0), 0, 1), Location.create(3251, 3265, 0));
		RegionUtility.addEntity(new DynamicMapObject(15516, Location.create(3252, 3265, 0), 0, 1), Location.create(3252, 3265, 0));
	}

	public static boolean onObjectClick(Player player, int objectId, Location objLoc, int option) {
		/*Region region = Server.getRegionRepository().fromLocation(objLoc);
		Set<MapObject> mapObjects = region.getEntities(objLoc, Entity.EntityType.STATIC_OBJECT, Entity.EntityType.DYNAMIC_OBJECT);

        Optional<MapObject> optional = mapObjects.stream().filter(x -> x.getId() == objectId).findFirst();
        if(optional.isPresent()) {
            System.out.println(optional.get());
        }*/

		//edgeville gate
		if (objLoc.equals(3080, 3501, 0) || objLoc.equals(3079, 3501, 0) || objLoc.equals(3080, 3499, 0) || objLoc.equals(3080, 3500, 0)) {
			Server.objectManager.removeObject(Location.create(3080, 3499, 0), 0, false);
			Server.objectManager.removeObject(Location.create(3080, 3500, 0), 0, false);
			Server.objectManager.removeObject(Location.create(3080, 3501, 0), 0, false);
			Server.objectManager.removeObject(Location.create(3079, 3501, 0), 0, false);

			if (objLoc.equals(3080, 3501, 0) || objLoc.equals(3079, 3501, 0)) { // open
				new GameObject(-1, 3080, 3501, 0, 0, 0, -1, -1);
				new GameObject(-1, 3079, 3501, 0, 0, 0, -1, -1);

				new GameObject(15514, 3080, 3500, 0, 2, 0, -1, -1);
				new GameObject(15516, 3080, 3499, 0, 2, 0, -1, -1);
			} else {
				new GameObject(-1, 3080, 3499, 0, 3, 0, -1, -1);
				new GameObject(-1, 3080, 3500, 0, 3, 0, -1, -1);
				new GameObject(15514, 3080, 3501, 0, 3, 0, -1, -1);
				new GameObject(15516, 3079, 3501, 0, 3, 0, -1, -1);
			}
			return true;
		}

		//lumbridge cow gate
		if (objLoc.equals(3253, 3267, 0) || objLoc.equals(3253, 3266, 0) || objLoc.equals(3252, 3265, 0) || objLoc.equals(3251, 3265, 0)) {
			Server.objectManager.removeObject(Location.create(3253, 3267, 0), 0, false);
			Server.objectManager.removeObject(Location.create(3253, 3266, 0), 0, false);
			Server.objectManager.removeObject(Location.create(3252, 3265, 0), 0, false);
			Server.objectManager.removeObject(Location.create(3251, 3265, 0), 0, false);

			if (objLoc.equals(3253, 3267, 0) || objLoc.equals(3253, 3266, 0)) {
				new GameObject(-1, 3253, 3267, 0, 0, 0, -1, -1);
				new GameObject(-1, 3253, 3266, 0, 0, 0, -1, -1);

				new GameObject(15516, 3252, 3265, 0, 1, 0, -1, -1);
				new GameObject(15514, 3251, 3265, 0, 1, 0, -1, -1);
			} else {
				new GameObject(-1, 3252, 3265, 0, 0, 0, -1, -1);
				new GameObject(-1, 3251, 3265, 0, 0, 0, -1, -1);

				new GameObject(15516, 3253, 3267, 0, 0, 0, -1, -1);
				new GameObject(15514, 3253, 3266, 0, 0, 0, -1, -1);
			}
			return true;
		}

		//lumbridge south gate
		if (objLoc.equals(3237, 3285, 0) || objLoc.equals(3237, 3284, 0) || objLoc.equals(3236, 3284, 0) || objLoc.equals(3235, 3284, 0)) {
			Server.objectManager.removeObject(Location.create(3237, 3285, 0), 0, false);
			Server.objectManager.removeObject(Location.create(3237, 3284, 0), 0, false);
			Server.objectManager.removeObject(Location.create(3236, 3284, 0), 0, false);
			Server.objectManager.removeObject(Location.create(3235, 3284, 0), 0, false);

			if (objLoc.equals(3237, 3285, 0) || objLoc.equals(3237, 3284, 0)) {
				new GameObject(-1, 3237, 3285, 0, 0, 0, -1, -1);
				new GameObject(-1, 3237, 3284, 0, 0, 0, -1, -1);

				new GameObject(36913, 3236, 3284, 0, 3, 0, -1, -1);
				new GameObject(36915, 3235, 3284, 0, 3, 0, -1, -1);
			} else {
				new GameObject(-1, 3236, 3284, 0, 3, 0, -1, -1);
				new GameObject(-1, 3235, 3284, 0, 3, 0, -1, -1);

				new GameObject(36915, 3237, 3285, 0, 0, 0, -1, -1);
				new GameObject(36913, 3237, 3284, 0, 0, 0, -1, -1);
			}
			return true;
		}

		// lumbridge chickens gate
		if (objLoc.equals(3237, 3295, 0) || objLoc.equals(3237, 3296, 0) || objLoc.equals(3236, 3295, 0) || objLoc.equals(3235, 3295, 0)) {
			Server.objectManager.removeObject(Location.create(3237, 3295, 0), 0, false);
			Server.objectManager.removeObject(Location.create(3237, 3296, 0), 0, false);
			Server.objectManager.removeObject(Location.create(3236, 3295, 0), 0, false);
			Server.objectManager.removeObject(Location.create(3235, 3295, 0), 0, false);

			if (objLoc.equals(3237, 3295, 0) || objLoc.equals(3237, 3296, 0)) {
				new GameObject(-1, 3237, 3295, 0, 3, 0, -1, -1);
				new GameObject(-1, 3237, 3296, 0, 3, 0, -1, -1);

				RegionUtility.addEntity(new DynamicMapObject(36913, Location.create(3236, 3295, 0), 0, 3), Location.create(3236, 3295, 0));
				RegionUtility.addEntity(new DynamicMapObject(36915, Location.create(3235, 3295, 0), 0, 3), Location.create(3235, 3295, 0));
			} else {
				new GameObject(-1, 3236, 3295, 0, 3, 0, -1, -1);
				new GameObject(-1, 3235, 3295, 0, 3, 0, -1, -1);

				new GameObject(36913, 3237, 3295, 0, 0, 0, -1, -1);
				new GameObject(36915, 3237, 3296, 0, 0, 0, -1, -1);
			}
			return true;
		}

		return false;
	}

}
