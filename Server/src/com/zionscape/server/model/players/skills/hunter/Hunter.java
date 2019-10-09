package com.zionscape.server.model.players.skills.hunter;

import com.zionscape.server.Server;
import com.zionscape.server.model.Direction;
import com.zionscape.server.model.DynamicMapObject;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.MapObject;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.npcs.NpcDefinition;
import com.zionscape.server.model.objects.GameObject;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.skills.SkillItems;
import com.zionscape.server.model.players.skills.SkillUtils;
import com.zionscape.server.model.region.RegionUtility;
import com.zionscape.server.util.Misc;
import com.zionscape.server.world.shops.Shops;

import java.util.*;
import java.util.logging.Logger;

public class Hunter {

	private static final int BIRD_SNARE = 10006;
	private static final int BOX_TRAP = 10008;
	private static final int IMPLING_JAR = 11260;
	private static final int BUTTERFLY_NET = 10010;
	private static Logger logger = Logger.getLogger(Hunter.class.getName());
	private static List<Trap> traps = new ArrayList<>();

	public static void process() {

		Iterator<Trap> iterator = traps.iterator();
		while (iterator.hasNext()) {
			Trap trap = iterator.next();


			// nothings been caught change the object id to failed
			if (!trap.isFailed() && System.currentTimeMillis() - trap.getPlacedAt() > 1 * 60 * 1000) {

				int objectId = -1;

				if (trap.getType() == Type.BIRD_SNARE && trap.getObjectId() == 19175) {
					objectId = 19174;
				}

				if (trap.getType() == Type.BOX_TRAP && trap.getObjectId() == 19187) {
					objectId = 19192;
				}

				if (objectId > -1) {
					Optional<MapObject> objectOptional = RegionUtility.getMapObject(trap.getObjectId(), trap.getLocation());
					if (!objectOptional.isPresent()) {
						logger.info(trap + " is not registered in the region system???");
						continue;
					}

					RegionUtility.removeEntity(objectOptional.get(), trap.getLocation());
					Server.objectManager.removeObject(trap.getLocation(), 10, false);

					Location location = trap.getLocation();

					new GameObject(objectId, location.getX(), location.getY(), location.getZ(), 0, 10, -1, -1);
					RegionUtility.addEntity(new DynamicMapObject(objectId, location, 10, 0), location);

					trap.setObjectId(objectId);
					trap.setFailed(true);
				}

				continue;
			}

			// nothings been done with the trap for 5 minutes lets gets rid of it
			if (System.currentTimeMillis() - trap.getPlacedAt() > 3 * 60 * 1000) {

				Optional<MapObject> objectOptional = RegionUtility.getMapObject(trap.getObjectId(), trap.getLocation());
				if (!objectOptional.isPresent()) {
					logger.info(trap + " is not registered in the region system???");
					continue;
				}

				RegionUtility.removeEntity(objectOptional.get(), trap.getLocation());
				Server.objectManager.removeObject(trap.getLocation(), 10, true);

				iterator.remove();
				continue;
			}

		}
	}

	public static boolean onClickingButtons(Player player, int button) {

		if (button == 209231) {
			player.getPA().sendOptions("Puro Puro", "Crimson Swifts", "Golden Warblers", "Copper Longtails", "More...");
			player.setDialogueOwner(Hunter.class);
			player.setCurrentDialogueId(0);
			return true;
		}

		return false;
	}

	public static boolean onDialogueOption(Player player, int option) {
		if (player.getDialogueOwner() == null || !player.getDialogueOwner().equals(Hunter.class)) {
			return false;
		}

		if (player.getCurrentDialogueId() == 0) {
			switch (option) {
				case 1: // puro puro
					player.getPA().spellTeleport(2592, 4317, 0);
					break;
				case 2: // Crimson swift
					player.getPA().spellTeleport(2556, 2921, 0);
					break;
				case 3: //  Golden warbler
					player.getPA().spellTeleport(3402, 3103, 0);
					break;
				case 4: // Copper longtail
					player.getPA().spellTeleport(2375, 3603, 0);
					break;
				case 5: // More
					player.getPA().sendOptions("Cerulean twitch", "Tropical wagtail", "Chinchompa", "Carnivorous chinchompa", "Grenwalls");
					player.setCurrentDialogueId(1);
					break;
			}
			return true;
		}

		if (player.getCurrentDialogueId() == 1) {
			switch (option) {
				case 1: // Cerulean twitch
					player.getPA().spellTeleport(2715, 3783, 0);
					break;
				case 2: // Tropical wagtail
					player.getPA().spellTeleport(2531, 2935, 0);
					break;
				case 3: //  Chinchompa
					player.getPA().spellTeleport(2516, 2880, 0);
					break;
				case 4: // Carnivorous chinchompa
					player.getPA().spellTeleport(2475, 2845, 0);
					break;
				case 5: // Grenwalls
					player.getPA().spellTeleport(2252, 3185, 0);
					break;
			}
			return true;
		}

		return true;
	}

	public static boolean onItemClicked(Player player, int itemId, int itemSlot, int option) {

		if (itemId == BIRD_SNARE || itemId == BOX_TRAP) {

			if (trapExistsAtLocation(player.getLocation())) {
				player.sendMessage("You cannot place a trap on top of another.");
				return true;
			}

			if (numberOfTrapsLaid(player) >= numberOfTrapsPlayerCanLay(player)) {
				player.sendMessage("You can lay a maximum of " + numberOfTrapsPlayerCanLay(player) + " traps.");
				return true;
			}

			player.startAnimation(827);

			Location location = player.getLocation();

            /*if(RegionUtility.getMapObject(location).size() > 0) {
				player.sendMessage("You may not lay traps on top of other objects.");
                return true;
            }*/

			player.getItems().deleteItem(itemId, 1);

			if (itemId == BIRD_SNARE) {
				traps.add(new Trap(player.getDatabaseId(), player.getLocation(), Type.BIRD_SNARE, 19175));

				new GameObject(19175, location.getX(), location.getY(), location.getZ(), 0, 10, -1, -1);
				RegionUtility.addEntity(new DynamicMapObject(19175, location, 10, 0), location);
			}

			if (itemId == BOX_TRAP) {
				traps.add(new Trap(player.getDatabaseId(), player.getLocation(), Type.BOX_TRAP, 19187));

				new GameObject(19187, location.getX(), location.getY(), location.getZ(), 0, 10, -1, -1);
				RegionUtility.addEntity(new DynamicMapObject(19187, location, 10, 0), location);
			}

			return true;
		}

		Optional<Imps> impsOptional = Arrays.asList(Imps.values()).stream().filter(x -> x.getJarId() == itemId).findAny();
		if (!impsOptional.isPresent()) {
			return false;
		}

		Imps imp = impsOptional.get();

		player.getItems().deleteItem(itemId, 1);

		if (imp.getUncommonLoot().length > 0 && Misc.random(100) <= 5) {
			int[] itemArgs = imp.getUncommonLoot()[Misc.random(imp.getUncommonLoot().length - 1)];


			if (player.getItems().hasClueScroll()) {
				while (ItemUtility.getName(itemArgs[0]).toLowerCase().contains("clue scroll")) {
					itemArgs = imp.getUncommonLoot()[Misc.random(imp.getUncommonLoot().length - 1)];
				}
			}

			if (itemArgs.length > 2) {
				player.getItems().addItem(itemArgs[0], Misc.random(itemArgs[1], itemArgs[2]));
			} else {
				player.getItems().addItem(itemArgs[0], itemArgs[1]);
			}

			player.sendMessage("You loot the jar and get " + ItemUtility.getName(itemArgs[0]) + ".");

			return true;
		}

		int[] itemArgs = imp.getCommonLoot()[Misc.random(imp.getCommonLoot().length - 1)];
		if (itemArgs.length > 2) {
			player.getItems().addItem(itemArgs[0], Misc.random(itemArgs[1], itemArgs[2]));
		} else {
			player.getItems().addItem(itemArgs[0], itemArgs[1]);
		}

		player.sendMessage("You loot the jar and get " + ItemUtility.getName(itemArgs[0]) + ".");

		return true;
	}

	public static boolean itemOnObject(Player player, int objectID, Location objectLocation, int itemId) {

		Optional<Trap> optionalTrap = traps.stream().filter(t -> t.getLocation().equals(objectLocation) && t.getObjectId() == objectID).findAny();
		if (!optionalTrap.isPresent()) {
			return false;
		}

		Trap trap = optionalTrap.get();

		if (trap.getPlayerId() != player.getDatabaseId()) {
			player.sendMessage("This trap does not belong to you.");
			return true;
		}

		if (itemId != 12535) { // pawya meet
			return true;
		}

		if (trap.getBait() == 12535) {
			player.sendMessage("This trap is already baited.");
			return true;
		}

		player.startAnimation(827);
		player.getItems().deleteItem(12535, 1);
		trap.setBait(12535);
		player.sendMessage("You bait the trap with Raw pawya meat.");

		return true;
	}

	public static boolean onObjectClick(Player player, int objectId, Location objectLocation, int option) {

		if (objectId == 25029 || objectId == 25016) { // puro puro pass-through


			Optional<MapObject> mapObject = RegionUtility.getMapObject(objectId, objectLocation);
			if (!mapObject.isPresent()) {
				return false;
			}

			MapObject mapObject1 = mapObject.get();

			if (objectLocation.getY() == 4328) { // north
				if (player.absY == 4327) {
					player.getPA().walkTo(0, 2);
				}
				if (player.absY == 4329) {
					player.getPA().walkTo(0, -2);
				}
				return true;
			}

			if (objectLocation.getX() == 2583) { // west
				if (player.absX == 2584) {
					player.getPA().walkTo(-2, 0);
				}
				if (player.absX == 2582) {
					player.getPA().walkTo(2, 0);
				}
				return true;
			}

			if (objectLocation.getY() == 4311) { // south
				if (player.absY == 4312) {
					player.getPA().walkTo(0, -2);
				}
				if (player.absY == 4310) {
					player.getPA().walkTo(0, 2);
				}
				return true;
			}

			if (objectLocation.getX() == 2600) { // east
				if (player.absX == 2599) {
					player.getPA().walkTo(2, 0);
				}
				if (player.absX == 2601) {
					player.getPA().walkTo(-2, 0);
				}
				return true;
			}

			if (mapObject1.getOrientation() == 0) { // north south
				if (player.absY < objectLocation.getY()) {
					player.getPA().walkTo(0, 2);
				} else {
					player.getPA().walkTo(0, -2);
				}
				return true;
			}

			if (mapObject1.getOrientation() == 1) { // west east
				if (player.absX < objectLocation.getX()) {
					player.getPA().walkTo(2, 0);
				} else {
					player.getPA().walkTo(-2, 0);
				}
				return true;
			}

			return true;
		}


		Optional<Trap> optionalTrap = traps.stream().filter(t -> t.getLocation().equals(objectLocation) && t.getObjectId() == objectId).findAny();

		if (!optionalTrap.isPresent()) {
			return false;
		}

		Trap trap = optionalTrap.get();

		if (trap.getPlayerId() != player.getDatabaseId()) {
			player.sendMessage("This trap does not belong to you.");
			return true;
		}

		Optional<MapObject> objectOptional = RegionUtility.getMapObject(trap.getObjectId(), trap.getLocation());
		if (!objectOptional.isPresent()) {
			logger.info(trap + " is not registered in the region system 2");
			return true;
		}

		RegionUtility.removeEntity(objectOptional.get(), trap.getLocation());
		Server.objectManager.removeObject(trap.getLocation(), 10, true);

		int skillItemCount = SkillItems.getHunterItemsWorn(player);
		int xp = getTrapXP(trap.getCapturedType());
		if (xp > -1) {
			int amount = 1;
			if(skillItemCount >= 3 && Misc.random(4) == 0) {
				amount += 1;
			}

			player.getPA().addSkillXP(xp, PlayerConstants.HUNTER);

			if (trap.getCapturedType() == 5079) { // normal chins
				player.getItems().addItem(10033, amount);

				if(SkillUtils.givePet(player.getActualLevel(PlayerConstants.HUNTER), 10033, skillItemCount >= 3 ? 0.05 : 0)) {
					player.getItems().addOrBank(25078 + Misc.random(3), 1);
					player.sendMessage("You have been given a Baby Chincompa pet.");
				}
			}
			if (trap.getCapturedType() == 5080) { // red chins
				player.getItems().addItem(10034, amount);

				if(SkillUtils.givePet(player.getActualLevel(PlayerConstants.HUNTER), 10034, skillItemCount >= 3 ? 0.05 : 0)) {
					player.getItems().addOrBank(25078 + Misc.random(3), 1);
					player.sendMessage("You have been given a Baby Chincompa pet.");
				}
			}

			if (trap.getType() == Type.BIRD_SNARE) {
				player.getItems().addItem(314, Misc.random(3, 8));
				Achievements.progressMade(player, Achievements.Types.CATCH_100_BIRDS);
			}
			if (trap.getCapturedType() == 7010) {
				Achievements.progressMade(player, Achievements.Types.CATCH_500_GREENWALLS);
				player.getItems().addItem(12539, Misc.random(9, 15));
			} else if (trap.getCapturedType() == 7014) {
				player.getItems().addItem(12535, amount);
			}
		}

		if (trap.getType() == Type.BOX_TRAP) {
			player.getItems().addItem(BOX_TRAP, 1);
		} else if (trap.getType() == Type.BIRD_SNARE) {
			player.getItems().addItem(BIRD_SNARE, 1);
		}

		traps.remove(trap);

		return true;
	}

	public static boolean onNpcClick(Player player, NPC npc, int option) {


		if (npc.type == 5110) { // shop
			Shops.open(player, 10);
			return true;
		}

		// check the npc is one used in hunter
		if (!isHunterNpc(npc)) {
			return false;
		}

		Optional<Imps> impsOptional = Arrays.asList(Imps.values()).stream().filter(x -> x.getNpcId() == npc.type).findAny();
		if (!impsOptional.isPresent()) {
			return false;
		}

		Imps imp = impsOptional.get();

		// net check
		if (player.equipment[PlayerConstants.WEAPON] != BUTTERFLY_NET) {
			player.sendMessage("You do not have a net to catch this imp.");
			return true;
		}

		if (imp.getRequiredLevel() > player.level[PlayerConstants.HUNTER]) {
			player.sendMessage("A level requirement of " + imp.getRequiredLevel() + " Hunter is required to do that.");
			return true;
		}

		if (!player.getItems().playerHasItem(IMPLING_JAR)) {
			player.sendMessage("You do not have an impling jar to catch these with.");
			return true;
		}

		player.startAnimation(6606);

		Achievements.progressMade(player, Achievements.Types.CATCH_200_IMPS);

		if (imp == Imps.KINGLY) {
			Achievements.progressMade(player, Achievements.Types.CATCH_500_KINGLY_IMPS);
		}

		npc.absX = -1000;
		npc.absY = -1000;
		npc.isDead = true;

		player.getData().impsCaught++;
		player.getPA().addSkillXP(imp.getXp(), PlayerConstants.HUNTER);

		int skillItemCount = SkillItems.getHunterItemsWorn(player);
		if(skillItemCount >= 3 && Misc.random(100) <= 15) {
			player.getItems().deleteItem(IMPLING_JAR, 2);
		} else {
			player.getItems().deleteItem(IMPLING_JAR, 1);
		}
		player.getItems().addItem(imp.getJarId(), 1);

		return true;
	}

	private static boolean trapExistsAtLocation(Location location) {
		return traps.stream().filter(f -> f.getLocation().equals(location)).count() > 0;
	}

	private static int numberOfTrapsLaid(Player player) {
		return (int) traps.stream().filter(f -> f.getPlayerId() == player.getDatabaseId()).count();
	}

	private static int numberOfTrapsPlayerCanLay(Player player) {
		return Math.floorDiv(player.level[PlayerConstants.HUNTER], 20) + 1;
	}

	public static boolean isImp(NPC npc) {
		for (Imps imp : Imps.values()) {
			if (imp.getNpcId() == npc.type) {
				return true;
			}
		}
		return false;
	}

	public static boolean isHunterNpc(NPC npc) {

		for (Imps imp : Imps.values()) {
			if (imp.getNpcId() == npc.type) {
				return true;
			}
		}

		switch (npc.type) {
			case 7010: // grenwalls
			case 5073: // red
			case 5075: // yellow
			case 5076: // orange
			case 5074: // blue
			case 5072: // green
			case 5081: // ferret
			case 5079: // chincompa
			case 5080: // Carnivorous chinchompa
			case 7014: // pawya
				return true;
		}
		return false;
	}

	public static void npcRandomMoved(NPC npc) {

		// check the npc is one used in hunter
		if (!isHunterNpc(npc)) {
			return;
		}

		Location location = npc.getLocation();

		Optional<Trap> trapOptional = traps.stream().filter(f -> f.getLocation().equals(location)).findAny();
		if (!trapOptional.isPresent()) {
			return;
		}

		Trap trap = trapOptional.get();

		if (trap.isCaptured()) {
			return;
		}

		if (trap.getType() == Type.BIRD_SNARE && trap.getObjectId() == 19175) {


			switch (npc.type) {
				case 5073: // red
				case 5075: // yellow
				case 5076: // orange
				case 5074: // blue
				case 5072: // green
					break;
				default:
					return;
			}

			final int objectId = getSnareObjectId(npc.type);

			if (objectId == -1) {
				return;
			}

			Optional<MapObject> objectOptional = RegionUtility.getMapObject(trap.getObjectId(), trap.getLocation());
			if (!objectOptional.isPresent()) {
				logger.info(trap + " is not registered in the region system???");
				return;
			}

			Player player = Arrays.asList(PlayerHandler.players).stream().filter(x -> x != null && x.getDatabaseId() == trap.getPlayerId()).findAny().orElse(null);
			if (player == null) {
				return;
			}
			if (getRequiredLevel(npc.type) > player.level[PlayerConstants.HUNTER]) {
				return;
			}

			RegionUtility.removeEntity(objectOptional.get(), trap.getLocation());
			Server.objectManager.removeObject(trap.getLocation(), 10, false);

			new GameObject(objectId, location.getX(), location.getY(), location.getZ(), 0, 10, -1, -1);
			RegionUtility.addEntity(new DynamicMapObject(objectId, location, 10, 0), location);

			trap.setObjectId(objectId);
		}

		if (trap.getType() == Type.BOX_TRAP && trap.getObjectId() == 19187) {

			switch (npc.type) {
				case 5081: // ferret
				case 5079: // chincompa
				case 5080: // Carnivorous chinchompa
				case 7010: // grenwall
				case 7014: // pawya
					break;
				default:
					return;
			}


			// grenwalls require pawya meat
			if (npc.type == 7010 && trap.getBait() != 12535) {
				return;
			}

			if (trap.getBait() == 12535 && npc.type != 7010) {
				return;
			}


			Optional<MapObject> objectOptional = RegionUtility.getMapObject(trap.getObjectId(), trap.getLocation());
			if (!objectOptional.isPresent()) {
				logger.info(trap + " is not registered in the region system???");
				return;
			}

			Player player = Arrays.asList(PlayerHandler.players).stream().filter(x -> x != null && x.getDatabaseId() == trap.getPlayerId()).findAny().orElse(null);
			if (player == null) {
				return;
			}
			if (getRequiredLevel(npc.type) > player.level[PlayerConstants.HUNTER]) {
				return;
			}

			RegionUtility.removeEntity(objectOptional.get(), trap.getLocation());
			Server.objectManager.removeObject(trap.getLocation(), 10, false);

			new GameObject(19190, location.getX(), location.getY(), location.getZ(), 0, 10, -1, -1);
			RegionUtility.addEntity(new DynamicMapObject(19190, location, 10, 0), location);

			trap.setObjectId(19190);
		}

		// stop the little cunt walking
		npc.absX = -1000;
		npc.absY = -1000;
		npc.isDead = true;
		npc.randomWalk = false;
		trap.setCapturedType(npc.type);
		trap.setCaptured(true);
	}


	private static int getSnareObjectId(int type) {

		switch (type) {
			case 5073: // red
				return 19180;
			case 5075: // yellow
				return 19184;
			case 5076: // orange
				return 19186;
			case 5074: // blue
				return 19184;
			case 5072: // green
				return 19178;
		}

		return -1;
	}

	private static int getTrapXP(int npc) {

		switch (npc) {
			case 5073: // red
				return 33;
			case 5075: // yellow
				return 48;
			case 5076: // orange
				return 60;
			case 5074: // blue
				return 79;
			case 5072: // green
				return 115;
			case 5081: // ferret
				return 140;
			case 5079: // chincompa
				return 200;
			case 5080: // Carnivorous chinchompa
				return 250;
			case 7010: // grenwall
				return 550;
			case 7014: // pawya
				return 325;
		}

		return -1;
	}

	private static int getRequiredLevel(int npc) {

		switch (npc) {
			case 5073: // red
				return 1;
			case 5075: // yellow
				return 5;
			case 5076: // orange
				return 9;
			case 5074: // blue
				return 11;
			case 5072: // green
				return 19;
			case 5081: // ferret
				return 27;
			case 5079: // chincompa
				return 53;
			case 5080: // Carnivorous chinchompa
				return 63;
			case 7010: // grenwall
				return 77;
			case 7014: // pawya
				return 66;
		}

		return -1;
	}

	public static void spawnInitialImps() {
		for (Imps imp : Imps.values()) {
			for (int i = 0; i < imp.getMaxSpawn(); i++) {
				NpcDefinition definition = NPCHandler.getNpcDefinition(imp.getNpcId());
				NPCHandler.newNPC(
						imp.getNpcId(),
						2562 + Misc.random(59),
						4290 + Misc.random(59),
						0,
						1,
						0, //hp
						0, // maxhit
						0, // attack
						0, // defence
						Direction.NONE
				);
			}
		}
	}

	private enum Imps {
		BABY(6055, 17, 15, 3, 11238,
				new int[][]{
						{1755, 1},
						{1734, 1},
						{1733, 1},
						{946, 1},
						{2347, 1},
						{1799, 1},
						{1607, 1},
						{379, 1},
						{2355, 1},
						{1438, 1},
						{1442, 1},
						{1927, 1},
				},
				new int[][]{
						{995, 100000}
				}
		),
		YOUNG(6056, 22, 30, 3, 11240,
				new int[][]{
						{361, 1},
						{1437, 1, 15},
						{454, 1, 15},
						{1778, 1, 10},
						{1157, 1},
						{1353, 1},
						{2359, 1},
						{133, 1},
						{1539, 1, 5},
						{232, 1, 5},
						{1097, 1},
				},
				new int[][]{
						{995, 100000}
				}
		),
		EARTH(6058, 36, 72, 3, 11244,
				new int[][]{
						{6032, 1},
						{557, 1, 32},
						{1440, 1},
						{1442, 1},
						{444, 1, 6},
						{2354, 1, 3},
						{454, 1, 5},
						{236, 1, 3},
						{448, 1, 3},
						{1273, 1},
						{1603, 1, 3},
						{1605, 1, 3}
				},
				new int[][]{
						{995, 100000}
				}
		),
		ESSENCE(6059, 42, 105, 3, 11246,
				new int[][]{
						{554, 1, 50},
						{555, 1, 40},
						{556, 1, 40},
						{559, 1, 30},
						{560, 1, 30},
						{561, 1, 30},
						{562, 1, 30},
						{563, 1, 30},
						{564, 1, 1},
						{565, 1, 20},
						{566, 1, 30},
						{1437, 1, 50},
						{558, 1, 40},
						{557, 1, 40},
						{1448, 1},
						{4698, 1, 30},
						{21773, 1, 10}
				}, new int[][]{}
		),
		SPIRIT(7866, 54, 140, 4, 15513,
				new int[][]{
						{12158, 1, 5},
						{12159, 1, 5},
						{12160, 1, 5},
						{12163, 1, 5},
						{12155, 45},
						{2350, 1, 5},
						{2352, 1, 5},
						{2354, 1, 5},
						{2356, 1, 5},
						{2358, 1, 5},
						{2360, 1, 5},
						{2362, 1, 3},
						{2364, 1},
						{1636, 5, 20},
						{2135, 10, 25},
						{2139, 10, 25},
						{9979, 10, 25},
						{3227, 10, 25},
						{10095, 1},
						{10103, 1},
						{1520, 10, 35},
						{29, 1},
						{249, 1, 5},
						{1516, 5, 25},
						{384, 10, 20},
				}, new int[][]{}
		),
		NATURE(6061, 58, 165, 3, 11250,
				new int[][]{
						{5294, 2, 2},
						{6016, 1},
						{1514, 2, 15},
						{254, 2, 5},
						{5298, 2, 5},
						{5299, 2, 5},
						{5297, 2, 5},
						{5313, 2, 5},
						{5295, 2, 5},
						{5291, 2, 5},
						{5292, 2, 5},
						{5293, 2, 5},
						{5294, 2, 5},
						{5296, 2, 5},
						{5300, 2, 5},
						{5301, 2, 3},
						{5302, 2, 3},
						{5312, 1, 3},
						{5314, 1, 3},
						{450, 1, 1},
						{448, 1, 1},
						{454, 1, 10},
						{200, 2, 3},
						{202, 2, 3},
						{204, 2, 3},
						{206, 2, 3},
						{208, 2, 3},
						{210, 2, 3},
						{212, 2, 3},
						{214, 2, 3},
						{216, 2, 3},
				},
				new int[][]{
						{5315, 1, 3},
						{995, 150},
						{5303, 1, 3},
						{5304, 1, 3},
						{270, 1, 3},
						{3001, 1, 3},
						{2364, 1, 3},
						{452, 1, 3},
						{218, 1},
						{220, 1},
				}
		),
		NINJA(6053, 74, 195, 3, 11254,
				new int[][]{
						{6289, 1},
						{6322, 1},
						{6324, 1},
						{6326, 1},
						{6328, 1},
						{6330, 1},
						{3385, 1},
						{3391, 1},
						{9242, 10, 25},
						{892, 25, 70},
						{44, 25, 50},
						{811, 25, 70},
						{868, 25, 70},
						{805, 50, 100},
						{1748, 10, 16},
						{1215, 1},
						{1347, 1},
						{1127, 1},
						{1333, 1},
						{1201, 1},
						{9194, 10, 25},
						{9245, 10, 25},
						{2364, 3, 8},
						{454, 15, 35},
				},
				new int[][]{
						{4587, 1},
						{995, 100000},
				}
		),
		PIRATE(7845, 76, 230, 2, 13337,
				new int[][]{
						{2358, 1, 15},
						{7110, 1},
						{7122, 1},
						{7128, 1},
						{7134, 1},
						{7135, 1},
						{13358, 1},
						{13360, 1},
						{13362, 1},
						{7116, 1},
						{7126, 1},
						{7132, 1},
						{7138, 1},
						{13364, 1},
						{13366, 1},
						{13368, 1},
						{7112, 1},
						{7124, 1},
						{7130, 1},
						{7136, 1},
						{8949, 1},
						{8924, 1},
						{8926, 1},
						{995, 1, 500},
						{9000, 1},
				},
				new int[][]{
						{7114, 1},
						{13355, 1},
				}),
		DRAGON(6054, 83, 260, 2, 11256,
				new int[][]{
						{11212, 5, 15},
						{9193, 5, 25},
						{9244, 5, 20},
						{1305, 1},
						{1249, 1},
						{4587, 1},
						{11230, 5, 25},
						{11237, 5, 25},
						{535, 10, 20},
						{537, 10, 20},
						{1615, 1, 2},
						{1704, 1},
						{1434, 1},
						{1377, 1},
						{1305, 1},
						{9242, 10, 25},
				},
				new int[][]{
						{995, 500, 5000},
						{5315, 1},
				}
		),
		ZOMBIE(7902, 87, 295, 2, 15515,
				new int[][]{
						{533, 10, 20},
						{535, 10, 20},
						{537, 10, 15},
						{995, 10000, 20000},
						{4835, 1, 5},
						{527, 1, 5},
						{995, 1000, 5000},
						{18831, 8, 14},
				},
				new int[][]{
						{5315, 1},
						{995, 10000, 20000},

				}),
		KINGLY(7903, 91, 375, 2, 15517,
				new int[][]{
						{1705, 3, 8},
						{9242, 10, 25},
						{11212, 10, 20},
						{11237, 15},
						{11230, 10, 20},
						{11232, 10, 20},
						{1616, 10},
						{9193, 10, 20},
						{9244, 10, 20},
						{9194, 10, 20},
						{9245, 10, 20},
						{2364, 10},
						{210, 1, 3},
						{212, 2, 5},
						{214, 2, 5},
						{216, 2, 5},
						{218, 1},
						{220, 1},
				},
				new int[][]{
						{15509, 1},
						{15503, 1},
						{15505, 1},
						{15507, 1},
						{15511, 1},
						{6571, 1},
						{2366, 1},
						{590, 1},
						{13040, 1},
						{5315, 1},
						{5316, 1},

				}
		);

		private final int npcId;
		private final int requiredLevel;
		private final int xp;
		private final int maxSpawn;
		private final int jarId;
		private final int[][] commonLoot;
		private final int[][] uncommonLoot;

		Imps(int npcId, int requiredLevel, int xp, int maxSpawn, int jarId, int[][] commonLoot, int[][] uncommonLoot) {
			this.npcId = npcId;
			this.requiredLevel = requiredLevel;
			this.xp = xp;
			this.maxSpawn = maxSpawn;
			this.jarId = jarId;
			this.commonLoot = commonLoot;
			this.uncommonLoot = uncommonLoot;
		}

		public int getNpcId() {
			return npcId;
		}

		public int getRequiredLevel() {
			return requiredLevel;
		}

		public int getXp() {
			return xp;
		}

		public int getMaxSpawn() {
			return maxSpawn;
		}

		public int getJarId() {
			return jarId;
		}

		public int[][] getCommonLoot() {
			return commonLoot;
		}

		public int[][] getUncommonLoot() {
			return uncommonLoot;
		}
	}


}