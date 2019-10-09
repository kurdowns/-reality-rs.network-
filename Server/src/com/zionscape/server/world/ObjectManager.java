package com.zionscape.server.world;

import com.zionscape.server.cache.Collision;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.MapObject;
import com.zionscape.server.model.objects.GameObject;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.commands.impl.Kick;
import com.zionscape.server.model.players.skills.farming.Farming;
import com.zionscape.server.model.region.RegionUtility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Sanity
 */
public final class ObjectManager {

	public static ArrayList<RemovedObject> removedObjects = new ArrayList<>();

	public ArrayList<GameObject> objects = new ArrayList<>();
	private ArrayList<GameObject> toRemove = new ArrayList<>();

	public void loadRemovedObjects() {

		removedObjects.add(new RemovedObject(Location.create(3297, 3857), 10));
		removedObjects.add(new RemovedObject(Location.create(3291, 3855), 10));
		removedObjects.add(new RemovedObject(Location.create(3292, 3858), 10));
		removedObjects.add(new RemovedObject(Location.create(3296, 3854), 10));
		removedObjects.add(new RemovedObject(Location.create(3299, 3855), 10));
		removedObjects.add(new RemovedObject(Location.create(3296, 3860), 10));
		removedObjects.add(new RemovedObject(Location.create(3295, 3860), 10));
		removedObjects.add(new RemovedObject(Location.create(3293, 3860), 10));
		removedObjects.add(new RemovedObject(Location.create(3296, 3859), 10));
		removedObjects.add(new RemovedObject(Location.create(3291, 3852), 10));
		removedObjects.add(new RemovedObject(Location.create(3295, 3862), 10));
		removedObjects.add(new RemovedObject(Location.create(3296, 3862), 10));
		removedObjects.add(new RemovedObject(Location.create(3281, 3854), 10));

		removedObjects.add(new RemovedObject(Location.create(2746, 5107, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2745, 5107, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2729, 5098, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2728, 5098, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2727, 5098, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2732, 5074, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2732, 5073, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2731, 5081, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2731, 5082, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2731, 5083, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3259, 3381, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2710, 9850, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2719, 9840, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2729, 9850, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2747, 9848, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2738, 9835, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2728, 5091, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2728, 5092, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2728, 5093, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2728, 5094, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3092, 3274, 0), 0));
		removedObjects.add(new RemovedObject(Location.create(2949, 3379, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3001, 3960, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3078, 3510, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3080, 3510, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3080, 3531, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3077, 3512, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3095, 3498, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3091, 3495, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3092, 3496, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3096, 3501, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3077, 3512, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3081, 3510, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3084, 3511, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3084, 3510, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3084, 3509, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3095, 3499, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3090, 3496, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3090, 3494, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3091, 3499, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2746, 5107, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2745, 5107, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2729, 5098, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2728, 5098, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2727, 5098, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2732, 5074, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2732, 5073, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2731, 5081, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2731, 5082, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2731, 5083, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3259, 3381, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2710, 9850, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2719, 9840, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2729, 9850, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2747, 9848, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2738, 9835, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2728, 5091, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2728, 5092, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2728, 5093, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(2728, 5094, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3092, 3274, 0), 0));
		removedObjects.add(new RemovedObject(Location.create(2949, 3379, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3001, 3960, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3078, 3510, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3080, 3510, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3080, 3531, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3077, 3512, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3095, 3498, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3091, 3495, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3092, 3496, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3096, 3501, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3077, 3512, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3081, 3510, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3084, 3511, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3084, 3510, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3084, 3509, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3095, 3499, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3090, 3496, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3090, 3494, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3091, 3499, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3059, 9564, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3049, 9566, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3100, 3508, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3100, 3507, 0), 10));
		for (int i = 0; i < 9; i++)
			removedObjects.add(new RemovedObject(Location.create(3091 + i, 3507, 0), 10));

		for (int i = 0; i < 4; i++)
			removedObjects.add(new RemovedObject(Location.create(3091, 3508 + i, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3092, 3511, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3094, 3510, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3095, 3510, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3096, 3511, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3100, 3511, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3100, 3512, 0), 10));
		for (int i = 0; i < 5; i++)
			removedObjects.add(new RemovedObject(Location.create(3096 + i, 3513, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3099, 3512, 0), 10));

		// Skilling building
		removedObjects.add(new RemovedObject(Location.create(3078, 3507, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3082, 3507, 0), 10));
		removedObjects.add(new RemovedObject(Location.create(3084, 3512, 0), 10));
		for (int i = 0; i < 8; i++)
			removedObjects.add(new RemovedObject(Location.create(3084 - i, 3513, 0), 10));
		for (int i = 0; i < 4; i++)
			removedObjects.add(new RemovedObject(Location.create(3076, 3509 + i, 0), 10));
		// Done skilling
		for (RemovedObject removedObject : removedObjects) {
			Set<MapObject> mapObjects = RegionUtility.getMapObject(removedObject.getLocation());
			mapObjects.stream().filter(x -> x.getType() == removedObject.getType()).forEach(x -> Collision
					.removeObjectClipping(x.getId(), removedObject.getLocation(), x.getOrientation(), x.getType()));
		}
	}

	public void addObject(GameObject o) {
		if (this.getObject(o.objectX, o.objectY, o.height) == null) {
			objects.add(o);
			this.placeObject(o);
		} else {
			try {
				throw new Exception("");
			} catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println("object already exists at " + o.objectX + " " + o.objectY + " " + o.height);
		}
	}

	public GameObject getObject(int x, int y, int height) {
		for (GameObject o : objects) {
			if (o.objectX == x && o.objectY == y && o.height == height) {
				return o;
			}
		}
		return null;
	}

	public void loadCustomSpawns(Player c) {

		/** skilling area spawns **/
		for (RemovedObject removedObject : removedObjects) {
			c.getPA().customObject(-1, removedObject.getLocation().getX(), removedObject.getLocation().getY(),
					removedObject.getLocation().getZ(), 0, removedObject.getType());
		}
		if (c.christmasEvent == 4) {
			c.getPA().customObject(10672, 3090, 3276, 0, 1, 10);// Take-Marionette (if completed)
		}
		switch (c.giftAmount) {
		case 0:
			// c.getPA().customObject(10652, 3085, 3485, 0, 1, 10);
			break;
		case 1:
			// c.getPA().customObject(10653, 3085, 3485, 0, 1, 10);
			break;
		case 2:
			// c.getPA().customObject(10654, 3085, 3485, 0, 1, 10);
			break;
		case 3:
			// c.getPA().customObject(10658, 3085, 3485, 0, 1, 10);
			break;
		case 4:
			// c.getPA().customObject(10659, 3085, 3485, 0, 1, 10);
			break;
		case 5:
			// c.getPA().customObject(10660, 3085, 3485, 0, 1, 10);
			break;
		}

		Farming.loadCompostBins(c);

		c.getPA().customObject(2213, 3045, 9784, 0, 4, 10);
		c.getPA().customObject(2783, 2403, 4727, 0, 4, 10); // donator anvil
		c.getPA().customObject(11666, 2403, 4723, 0, 4, 10); // donatr furnace
		c.getPA().customObject(1996, 2912, 5300, 0, 3, 10); // sara rock gwd
		c.getPA().customObject(2213, 2408, 4730, 0, 4, 10); // donator bank
		c.getPA().customObject(61, 4008, 4009, 0, 1, 10); // donator altar?
		c.getPA().customObject(14860, 2420, 4729, 0, 4, 10); // rune rocks
		c.getPA().customObject(14859, 2419, 4730, 0, 4, 10); // rune rocks
		c.getPA().customObject(14860, 2420, 4727, 0, 4, 10); // rune rocks
		c.getPA().customObject(6282, 2485, 3042, 0, 4, 10);
		c.getPA().customObject(2213, 2482, 3046, 0, 4, 10);
		// c.getPA().customObject(170, 3551, 9694, 0, 1, 10);//barrows cheap fix
		c.getPA().customObject(1596, 3008, 3850, 0, 1, 0);
		c.getPA().customObject(2561, 3091, 3486, 0, 2, 10);// stalls home
		c.getPA().customObject(2560, 3093, 3486, 0, 2, 10);// stalls home
		c.getPA().customObject(2562, 3095, 3486, 0, 2, 10);// stalls home
		c.getPA().customObject(2564, 3097, 3486, 0, 2, 10);// stalls home
		c.getPA().customObject(6552, 3098, 3506, 0, 2, 10); // Magics Altar
		c.getPA().customObject(411, 3095, 3506, 0, 2, 10);// curse
		c.getPA().customObject(409, 3092, 3506, 0, 2, 10); // altar

		c.getPA().customObject(31085, 3028, 9741, 0, 0, 10); // addy oremining guild
		c.getPA().customObject(31085, 3027, 9741, 0, 0, 10); // addy oremining guild
		c.getPA().customObject(31085, 3026, 9741, 0, 0, 10); // addy oremining guild
		c.getPA().customObject(31085, 3023, 9743, 0, 0, 10); // addy oremining guild
		c.getPA().customObject(31085, 3022, 9743, 0, 0, 10); // addy oremining guild
		c.getPA().customObject(31085, 3021, 9743, 0, 0, 10); // addy ore mining guild
		c.getPA().customObject(31085, 3024, 9737, 0, 0, 10); // addy oremining guild
		c.getPA().customObject(31085, 3025, 9737, 0, 0, 10); // addy oremining guild

		c.getPA().customObject(31085, 3049, 9793, 0, 0, 10); // addy oremining guild
		c.getPA().customObject(31085, 3049, 9792, 0, 0, 10); // addy oremining guild
		c.getPA().customObject(31085, 3048, 9791, 0, 0, 10); // addy oremining guild
		c.getPA().customObject(31085, 3051, 9764, 0, 0, 10); // addy oremining guild
		c.getPA().customObject(31085, 3051, 9765, 0, 0, 10); // addy oremining guild
		c.getPA().customObject(31085, 3049, 9763, 0, 0, 10); // addy oremining guild
		c.getPA().customObject(31085, 3031, 9743, 0, 0, 10); // addy oremining guild
		c.getPA().customObject(31085, 3032, 9743, 0, 0, 10); // addy oremining guild

		c.getPA().customObject(31086, 3044, 9732, 0, 0, 10); // mith oremining guild
		c.getPA().customObject(31086, 3043, 9732, 0, 0, 10); // mith oremining guild
		c.getPA().customObject(31086, 3044, 9735, 0, 0, 10); // mith oremining guild
		c.getPA().customObject(31086, 3034, 9736, 0, 0, 10); // mith oremining guild
		c.getPA().customObject(31086, 3035, 9736, 0, 0, 10); // mith oremining guild
		c.getPA().customObject(31086, 3035, 9737, 0, 0, 10); // mith oremining guild
		c.getPA().customObject(14860, 3225, 3144, 0, 0, 10); // rune ore lumby
		c.getPA().customObject(14859, 3226, 3144, 0, 0, 10); // rune ore lumby
		c.getPA().customObject(14860, 3227, 3146, 0, 0, 10); // rune ore lumby
		c.getPA().customObject(14859, 3231, 3149, 0, 0, 10); // rune ore lumby

		// donator zone rocks
		c.getPA().customObject(31070, 4021, 4015, 0, 0, 10); // coal ore dzone
		c.getPA().customObject(31068, 4022, 4016, 0, 0, 10); // coal ore dzone
		c.getPA().customObject(31069, 4023, 4015, 0, 0, 10); // coal ore dzone
		c.getPA().customObject(31070, 4025, 4019, 0, 0, 10); // coal ore dzone
		c.getPA().customObject(31068, 4026, 4020, 0, 0, 10); // coal ore dzone
		c.getPA().customObject(31069, 4025, 4021, 0, 0, 10); // coal ore dzone

		c.getPA().customObject(14860, 4030, 4018, 0, 0, 10); // rune ore donor zone
		c.getPA().customObject(14860, 4030, 4017, 0, 0, 10); // rune ore donor zone
		c.getPA().customObject(14860, 4030, 4016, 0, 0, 10); // rune ore donor zone
		c.getPA().customObject(14860, 4030, 4015, 0, 0, 10); // rune ore donor zone

		c.getPA().customObject(31085, 4030, 4012, 0, 0, 10); // addy ore donor zone
		c.getPA().customObject(31085, 4030, 4011, 0, 0, 10); // addy ore donor zone
		c.getPA().customObject(31085, 4030, 4010, 0, 0, 10); // addy ore donor zone
		c.getPA().customObject(31085, 4030, 4009, 0, 0, 10); // addy ore donor zone

		c.getPA().customObject(1309, 4016, 4001, 0, 0, 10); // yew donor zone
		c.getPA().customObject(1309, 4022, 4001, 0, 0, 10); // yew donor zone
		c.getPA().customObject(1309, 4023, 4008, 0, 0, 10); // yew donor zone
		c.getPA().customObject(1309, 4017, 4008, 0, 0, 10); // yew donor zone

		c.getPA().customObject(1306, 4022, 3996, 0, 0, 10); // magic donor zone
		c.getPA().customObject(1306, 4016, 3996, 0, 0, 10); // magic donor zone
		c.getPA().customObject(1306, 4021, 3992, 0, 0, 10); // magic donor zone
		c.getPA().customObject(1306, 4017, 3992, 0, 0, 10); // magic donor zone

		// remove edge general store
		// c.getPA().customObject(10660, 3333, 3333, 0, 1, 10);//tree
		// c.getPA().customObject(28716, 3075, 3503, 0, 4, 10);
		
		//sum td

		c.getPA().customObject(4039, 3090, 3492, 0, 3, 10);

		c.getPA().customObject(3735, 3206, 3762, 0, 0, 10); // rev north cave entrance
		c.getPA().customObject(3735, 3181, 3657, 0, 0, 10); // rev south cave entrance

		// Mysterious cape
		c.getPA().customObject(11000, 3090, 3496, 0, 3, 10);

		c.getPA().customObject(4116, 3090, 3494, 0, 1, 10);// pk chest

		c.getPA().customObject(25200, 3363, 2902, 0, 2, 10); // gnomegliderdown quest
		c.getPA().customObject(28847, 3318, 3067, 0, 2, 10); // gnomegliderdown quest
		c.getPA().customObject(38266, 3329, 3292, 0, 2, 10); // gnomegliderdown quest

		c.getPA().customObject(17004, 2838, 3159, 0, 2, 10); // gem rock
		c.getPA().customObject(17004, 2838, 3161, 0, 2, 10); // gem rock
		c.getPA().customObject(17004, 2840, 3163, 0, 2, 10); // gem rock

		c.getPA().customObject(17005, 2842, 3161, 0, 2, 10); // gem rock
		c.getPA().customObject(17005, 2843, 3160, 0, 2, 10); // gem rock
		c.getPA().customObject(17005, 2846, 3158, 0, 2, 10); // gem rock

		c.getPA().customObject(17006, 2838, 3154, 0, 2, 10); // gem rock
		c.getPA().customObject(17006, 2840, 3153, 0, 2, 10); // gem rock

		// respected donor zone
		c.getPA().customObject(1306, 2797, 3863, 0, 0, 10); // magic tree
		c.getPA().customObject(1306, 2793, 3864, 0, 0, 10); // magic tree
		c.getPA().customObject(1306, 2795, 3860, 0, 0, 10); // magic tree
		c.getPA().customObject(1306, 2794, 3857, 0, 0, 10); // magic tree
		c.getPA().customObject(1306, 2799, 3859, 0, 0, 10); // magic tree
		c.getPA().customObject(1306, 2800, 3867, 0, 0, 10); // magic tree

		c.getPA().customObject(14859, 2785, 3864, 0, 0, 10); // rune ore respected
		c.getPA().customObject(14859, 2784, 3864, 0, 0, 10); // rune ore respected
		c.getPA().customObject(14859, 2783, 3864, 0, 0, 10); // rune ore respected
		c.getPA().customObject(14859, 2786, 3864, 0, 0, 10); // rune ore respected
		c.getPA().customObject(14859, 2787, 3864, 0, 0, 10); // rune ore respected
		c.getPA().customObject(14859, 2788, 3864, 0, 0, 10); // rune ore respected
		c.getPA().customObject(14859, 2789, 3864, 0, 0, 10); // rune ore respected

		c.getPA().customObject(31085, 2785, 3861, 0, 0, 10); // addy ore respected
		c.getPA().customObject(31085, 2787, 3861, 0, 0, 10); // addy ore respected
		c.getPA().customObject(31085, 2789, 3861, 0, 0, 10); // addy ore respected
		c.getPA().customObject(31085, 2789, 3859, 0, 0, 10); // addy ore respected
		c.getPA().customObject(31085, 2787, 3859, 0, 0, 10); // addy ore respected
		c.getPA().customObject(31085, 2785, 3859, 0, 0, 10); // addy ore respected
		c.getPA().customObject(31085, 2783, 3859, 0, 0, 10); // addy ore respected
		c.getPA().customObject(31085, 2780, 3859, 0, 0, 10); // addy ore respected

		c.getPA().customObject(31085, 2779, 3857, 0, 0, 10); // addy ore respected
		c.getPA().customObject(31085, 2781, 3857, 0, 0, 10); // addy ore respected
		c.getPA().customObject(31085, 2783, 3857, 0, 0, 10); // addy ore respected
		c.getPA().customObject(31085, 2786, 3857, 0, 0, 10); // addy ore respected
		c.getPA().customObject(31085, 2788, 3857, 0, 0, 10); // addy ore respected

		// c.getPA().customObject(4483, 2781, 3864, 0, 0, 10); // bank

		c.getPA().customObject(4876, 2799, 3856, 0, 0, 10); // thieving stall

		c.getPA().customObject(3044, 2768, 3861, 0, 0, 10); // furnace
		c.getPA().customObject(2783, 2769, 3859, 0, 0, 10); // anvil
		// c.getPA().customObject(4483, 2769, 3858, 0, 0, 10); // bank
		c.getPA().customObject(2465, 2772, 3865, 0, 0, 10); // portal
		c.getPA().customObject(409, 3042, 4381, 0, 0, 10); // alter

		// extreme donor zone
		c.getPA().customObject(1309, 3013, 4357, 0, 0, 10); // yew ext zone
		c.getPA().customObject(1309, 3013, 4360, 0, 0, 10); // yew ext zone
		c.getPA().customObject(1309, 3013, 4364, 0, 0, 10); // yew ext zone
		c.getPA().customObject(1309, 3016, 4367, 0, 0, 10); // yew ext zone

		c.getPA().customObject(1306, 3020, 4366, 0, 0, 10); // magic tree
		c.getPA().customObject(1306, 3019, 4362, 0, 0, 10); // magic tree
		c.getPA().customObject(1306, 3024, 4359, 0, 0, 10); // magic tree
		c.getPA().customObject(1306, 3027, 4362, 0, 0, 10); // magic tree
		c.getPA().customObject(1306, 3029, 4366, 0, 0, 10); // magic tree

		c.getPA().customObject(14860, 3035, 4366, 0, 0, 10); // rune ore ext donor zone
		c.getPA().customObject(14860, 3035, 4365, 0, 0, 10); // rune ore ext donor zone
		c.getPA().customObject(14860, 3033, 4364, 0, 0, 10); // rune ore ext donor zone
		c.getPA().customObject(14860, 3033, 360, 0, 0, 10); // runeore extdonor zone
		c.getPA().customObject(14860, 3034, 4359, 0, 0, 10); // rune ore ext donor zone
		c.getPA().customObject(14860, 3036, 4358, 0, 0, 10); // rune ore ext donor zone

		c.getPA().customObject(31085, 3035, 4357, 0, 0, 10); // addy ore ext donor zone
		c.getPA().customObject(31085, 3036, 4356, 0, 0, 10); // addy ore ext donor zone
		c.getPA().customObject(31085, 3035, 4355, 0, 0, 10); // addy ore ext donor zone
		c.getPA().customObject(31085, 3033, 4355, 0, 0, 10); // addy ore ext donor zone
		c.getPA().customObject(31085, 3032, 4354, 0, 0, 10); // addy ore ext donor zone
		c.getPA().customObject(31085, 3029, 4356, 0, 0, 10); // addy ore ext donor zone
		c.getPA().customObject(31085, 3028, 4356, 0, 0, 10); // addy ore ext donor zone
		c.getPA().customObject(31085, 3027, 4355, 0, 0, 10); // addy ore ext donor zone
		c.getPA().customObject(31085, 3026, 4355, 0, 0, 10); // addy ore ext donor zone
		c.getPA().customObject(31085, 3025, 4355, 0, 0, 10); // addy ore ext donor zone

		c.getPA().customObject(31086, 3023, 4354, 0, 0, 10); // mith ext donor zone
		c.getPA().customObject(31086, 3021, 4354, 0, 0, 10); // mith ext donor zone
		c.getPA().customObject(31086, 3022, 4354, 0, 0, 10); // mith ext donor zone
		c.getPA().customObject(31086, 3020, 4355, 0, 0, 10); // mith ext donor zone
		c.getPA().customObject(31086, 3019, 4356, 0, 0, 10); // mith ext donor zone
		c.getPA().customObject(31086, 3018, 4355, 0, 0, 10); // mith ext donor zone
		c.getPA().customObject(31086, 3017, 4357, 0, 0, 10); // mith ext donor zone
		c.getPA().customObject(31086, 3017, 4356, 0, 0, 10); // mith ext donor zone

		c.getPA().customObject(4027, 3027, 4367, 0, 0, 10); // coal ore dzone
		c.getPA().customObject(4027, 3026, 4366, 0, 0, 10); // coal ore dzone
		c.getPA().customObject(4027, 3024, 4366, 0, 0, 10); // coal ore dzone

		c.getPA().customObject(3044, 3035, 4379, 0, 0, 10); // furnace
		c.getPA().customObject(2783, 3036, 4378, 0, 0, 10); // anvil

		c.getPA().customObject(14859, 3042, 4376, 0, 0, 10);
		c.getPA().customObject(2104, 3042, 4376, 0, 0, 10);
		c.getPA().customObject(2096, 3042, 4376, 0, 0, 10);

		c.getPA().customObject(4876, 3047, 4384, 0, 0, 10); // thieving stall
		c.getPA().customObject(2465, 3040, 4380, 0, 0, 10); // portal

		c.getPA().customObject(409, 2775, 3861, 0, 0, 10); // alter

	}

	public boolean loadForPlayer(GameObject o, Player c) {
		if (o == null || c == null) {
			return false;
		}
		return c.distanceToPoint(o.objectX, o.objectY) <= 60 && c.heightLevel == o.height;
	}

	public void loadObjects(Player c) {

		if (c == null) {
			return;
		}
		for (GameObject o : objects) {
			if (this.loadForPlayer(o, c)) {
				c.getPA().object(o.objectId, o.objectX, o.objectY, o.face, o.type);
			}
		}
		this.loadCustomSpawns(c);
	}

	public void placeObject(GameObject o) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c = PlayerHandler.players[j];
				if (c.heightLevel == o.height && c.distanceToPoint(o.objectX, o.objectY) <= 60) {
					c.getPA().object(o.objectId, o.objectX, o.objectY, o.face, o.type);
				}
			}
		}
	}

	public void process() {
		for (GameObject o : objects) {
			if (o.tick == -1) {
				continue;
			}
			if (o.tick > 0) {
				o.tick--;
			} else {
				this.updateObject(o);
				toRemove.add(o);
			}
		}
		for (GameObject o : toRemove) {
			objects.remove(o);
		}
		toRemove.clear();
	}

	public void removeObject(GameObject o) {
		for (Player p : PlayerHandler.players) {
			if (p == null) {
				continue;
			}
			p.getPA().object(-1, o.objectX, o.objectY, o.face, o.type);
		}
	}

	public void removeObject(int x, int y, int z) {
		for (Player player : PlayerHandler.players) {
			if (player == null) {
				continue;
			}
			if (player.heightLevel == z && player.distanceToPoint(x, y) <= 60) {
				player.getPA().object(-1, x, y, 0, 10);
			}
		}
	}

	public void removeObject(Location location, int type) {
		removeObject(location, type, false);
	}

	public void removeObject(Location location, int type, boolean display) {
		Iterator<GameObject> itr = objects.iterator();
		while (itr.hasNext()) {
			GameObject o = itr.next();
			if (o.objectX == location.getX() && o.objectY == location.getY()) {
				itr.remove();
				// break;
			}
		}
		if (!display) {
			return;
		}
		for (Player p : PlayerHandler.players) {
			if (p == null) {
				continue;
			}
			if (p.heightLevel != location.getZ()) {
				continue;
			}
			if (p.distanceToPoint(location.getX(), location.getY()) <= 60) {
				p.getPA().object(-1, location.getX(), location.getY(), 0, type);
			}
		}
	}

	public void updateObject(GameObject o) {
		for (Player p : PlayerHandler.players) {
			if (p == null) {
				continue;
			}
			if (p.heightLevel != o.height) {
				continue;
			}
			if (p.distanceToPoint(o.objectX, o.objectY) <= 60) {
				p.getPA().object(o.newId, o.objectX, o.objectY, o.face, o.type);
			}
		}
	}

}