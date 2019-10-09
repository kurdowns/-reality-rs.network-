package com.zionscape.server.model.content.cannon;

import com.zionscape.server.Server;
import com.zionscape.server.cache.Collision;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.DynamicMapObject;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.MapObject;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.content.minigames.GodWarsDungeon;
import com.zionscape.server.model.content.minigames.quests.TheStolenCannon;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.npcs.combat.zulrah.Zulrah;
import com.zionscape.server.model.objects.GameObject;
import com.zionscape.server.model.players.Areas;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.region.RegionUtility;
import com.zionscape.server.tick.Tick;
import com.zionscape.server.util.Misc;

import java.util.*;

public final class DwarfCannon {

	private static final Map<String, Location> cannons = new HashMap<>();
	private static final int ANIMATION = 827;

	public static boolean clickingItem(Player player, int itemId, int option) {
		if (itemId == 6) {

			if (player.theStolenCannonStatus != TheStolenCannon.COMPLETED_STAGE && player.rights != 3) {
				player.sendMessage("You must of completed The Stolen Cannon to setup a Cannon.");
				return true;
			}

			if (cannons.get(player.username) != null) {
				player.sendMessage("You already have a cannon down.");
				return true;
			}

			Location playerLocation = player.getLocation();
			for (Map.Entry<String, Location> cannonLocations : cannons.entrySet()) {
				if (cannonLocations.getValue().equals(playerLocation)) {
					player.sendMessage("You cannot place a cannon here.");
					return true;
				}
			}

			// cerb
			if (player.getX() >= 1231 && player.getX() <= 1249 && player.getY() >= 1235 && player.getY() <= 1256) {
				player.sendMessage("You cannot place the cannon here.");
				return true;
			}

			if (GodWarsDungeon.inBossRoom(player.getLocation())) {
				player.sendMessage("You cannot place the cannon here.");
				return true;
			}

			if (Areas.inCorpArea(player.getLocation()) || player.inPestControl || Zulrah.inArea(player) || Areas.inNexArea(player.getLocation())
					|| Areas.inHomeArea(player.getLocation())
					|| player.getPA().inMiniGame() && player.inPcBoat() && player.inDuelArena() && player.inFightPits() && player.inCastleWars() && player.inBarrows() && player.inFightCaves() && player.inMageArena() && player.inPcGame() && player.inPits() && player.inPitsWait() && player.inCwGame && player.inCwWait
					|| player.getDuel() != null
					|| Collision.getClipping(player.absX + 1, player.absY + 1, player.heightLevel) != 0) {
				player.sendMessage("You cannot place the cannon here.");
				return true;
			}

			player.startAnimation(ANIMATION);

			GameObject object = new GameObject(7, player.absX, player.absY, player.heightLevel, 0, 10, 0, -1);
			RegionUtility.addEntity(new DynamicMapObject(7, player.getLocation(), 10, 0), player.getLocation());

			if (player.getCannon() == null) {
				player.setCannon(new Cannon(object));
				player.getCannon().setObjectId(7);
			}

			cannons.put(player.username, Location.create(player.absX, player.absY, player.heightLevel));
			player.getItems().deleteItem(itemId, 1);

			return true;
		}
		return false;
	}

	public static boolean clickingObject(final Player player, final int objectId, final Location objLocation,
										 final int option) {

		if (option == 1) {
			// firing cannon
			if (objectId == 6) {

				Location loc = cannons.get(player.username);
				if (loc == null) {
					player.sendMessage("This isn't your cannon.");
					return true;
				}

				if (player.getCannon().getBalls() == 0) {
					player.sendMessage("Your cannon has no ammo.");
					return true;
				}

				if (player.getCannon().isFiring()) {
					return true;
				}

				player.getCannon().setFiring(true);
				Server.getTickManager().submit(new Tick(1) {

					@Override
					public void execute() {
						if (player.getCannon() == null) {
							stop();
							return;
						}

						if (player.getCannon().getBalls() == 0) {
							stop();
							return;
						}

						// reset firing list
						if (player.getCannon().getRotation() == 1) {
							player.getCannon().getFiredAt().clear();
						}

						player.getPA().objectAnim(objLocation.getX(), objLocation.getY(),
								getCannonRotationAnimation(player.getCannon().getRotation()), 10, -1);

						final NPC npc = getNPCWithinDistance(player);
						if (npc != null && npc.getOwnerId() == -1 && (Areas.inMulti(player) && player.npcIndex > 0 || !Areas.inMulti(player) && player.npcIndex == npc.id) && Collision.canProjectileMove(objLocation.getX(), objLocation.getY(), npc.absX, npc.absY, npc.heightLevel, 1, 1) && !player.getCannon().getFiredAt().contains(npc.id)) {
							startCannonballProjectile(player, npc);

							Achievements.progressMade(player, Achievements.Types.FIRE_5000_CANNON_SHOTS);
							player.getCannon().getFiredAt().add(npc.id);
							npc.setAttribute("cannon_last_hit", System.currentTimeMillis());

							GameCycleTaskHandler.addEvent(npc, container -> {
								int damage = Misc.random(20);

								if (damage > npc.getHP()) {
									damage = npc.getHP();
								}

								npc.hitUpdateRequired = true;
								npc.killerId = player.playerId;
								npc.facePlayer(player.playerId);
								npc.dealdamage(player.getDatabaseId(), damage);

								if (damage > 0) {
									player.getPA().addSkillXP(2 * damage, PlayerConstants.RANGED);
								}
								npc.handleHitMask(damage, 20, 5, npc.id);
								container.stop();
							}, 2);

							player.getCannon().setBalls(player.getCannon().getBalls() - 1);
						}

						player.getCannon().setRotation(player.getCannon().getRotation() + 1);
						if (player.getCannon().getRotation() > 8) {
							player.getCannon().setRotation(1);
						}

					}

					@Override
					public void stop() {
						super.stop();

						if (player.getCannon() != null) {
							player.getCannon().setFiring(false);
						}
					}

				});

				return true;
			} else if (objectId >= 7 && objectId <= 9) {
				pickupCannon(player, objectId, objLocation, false);
				return true;
			}
		} else if (option == 2 && objectId == 6) {
			// pick up cannon
			pickupCannon(player, objectId, objLocation, false);
			return true;
		}

		return false;
	}

	private static int getCannonRotationAnimation(int rotation) {
		switch (rotation) {
			case 1: // north
				return 516;
			case 2: // north-east
				return 517;
			case 3: // east
				return 518;
			case 4: // south-east
				return 519;
			case 5: // south
				return 520;
			case 6: // south-west
				return 521;
			case 7: // west
				return 514;
			case 8:
				return 515;
		}
		return -1;
	}

	/**
	 * Credits to
	 * http://www.rune-com.zionscape.server.org/runescape-development/rs2-com.zionscape.server/snippets/368251
	 * -pi-dwarf-multi-cannon-base.html
	 *
	 * @param player
	 * @return
	 */
	private static NPC getNPCWithinDistance(Player player) {
		int cannonX = player.getCannon().getObject().objectX;
		int cannonY = player.getCannon().getObject().objectY;

		Location cannonLocation = cannons.get(player.username);

		for (NPC npc : NPCHandler.npcs) {

			if (npc == null || npc.isDead || npc.getHP() == 0 || !cannonLocation.isWithinDistance(npc.getLocation())) {
				continue;
			}

			if (npc.attributeExists("cannon_last_hit")) {
				if (System.currentTimeMillis() - (long) npc.getAttribute("cannon_last_hit") < 1200) {
					continue;
				}
			}

			int theirX = npc.absX;
			int theirY = npc.absY;

			switch (player.getCannon().getRotation()) {
				case 1: // north
					if (theirY > cannonY && theirX >= cannonX - 1 && theirX <= cannonX + 1)
						return npc;
					break;
				case 2: // north-east
					if (theirX >= cannonX + 1 && theirY >= cannonY + 1)
						return npc;
					break;
				case 3: // east
					if (theirX > cannonX && theirY >= cannonY - 1 && theirY <= cannonY + 1)
						return npc;
					break;
				case 4: // south-east
					if (theirY <= cannonY - 1 && theirX >= cannonX + 1)
						return npc;
					break;
				case 5: // south
					if (theirY < cannonY && theirX >= cannonX - 1 && theirX <= cannonX + 1)
						return npc;
					break;
				case 6: // south-west
					if (theirX <= cannonX - 1 && theirY <= cannonY - 1)
						return npc;
					break;
				case 7: // west
					if (theirX < cannonX && theirY >= cannonY - 1 && theirY <= cannonY + 1)
						return npc;
					break;
				case 8: // north-west
					if (theirX <= cannonX - 1 && theirY >= cannonY + 1)
						return npc;
					break;
			}

		}
		return null;
	}

	private static int getObjectId(int objectId, int itemId) {
		if (objectId == 7 && itemId == 8) {
			return 8;
		}
		if (objectId == 8 && itemId == 10) {
			return 9;
		}
		if (objectId == 9 && itemId == 12) {
			return 6;
		}
		return -1;
	}

	public static boolean itemOnObject(Player player, int objectId, Location objLocation, int itemId) {

		if (objectId >= 6 && objectId <= 9) {

			if (player.getCannon() == null || !cannons.get(player.username).equals(objLocation)) {
				player.sendMessage("This is not your cannon");
				return true;
			}

			if (itemId == 2 && objectId == 6) {
				if (player.getCannon().getBalls() >= 30) {
					player.sendMessage("Cannon is already full of ammo.");
					return true;
				}

				int amountCanAdd = 30 - player.getCannon().getBalls();
				int amountOfBalls = player.getItems().getItemAmount(2);

				if (amountOfBalls > amountCanAdd) {
					amountOfBalls = amountCanAdd;
				}

				player.getCannon().setBalls(player.getCannon().getBalls() + amountOfBalls);
				player.getItems().deleteItem(itemId, amountOfBalls);
				return true;
			}

			int newObjectId = getObjectId(objectId, itemId);
			if (newObjectId == -1) {
				return false;
			}

			Server.objectManager.removeObject(objLocation, 10, true);
			GameObject object = new GameObject(newObjectId, objLocation.getX(), objLocation.getY(), objLocation.getZ(),
					0, 10, 0, -1);
			Optional<MapObject> o = RegionUtility.getMapObject(objectId, objLocation);
			o.ifPresent(obj -> RegionUtility.removeEntity(obj, objLocation));

			RegionUtility.addEntity(new DynamicMapObject(newObjectId, objLocation, 10, 0), objLocation);
			player.getCannon().setObject(object);
			player.getItems().deleteItem(itemId, 1);

			player.getCannon().setObjectId(newObjectId);
		}

		return false;
	}

	private static List<Integer> objectIdToItemList(int objectId) {
		List<Integer> items = new ArrayList<>();
		switch (objectId) {
			case 6:
				items.add(6);
				items.add(8);
				items.add(10);
				items.add(12);
				break;
			case 7:
				items.add(6);
				break;
			case 8:
				items.add(6);
				items.add(8);
				break;
			case 9:
				items.add(6);
				items.add(8);
				items.add(10);
				break;
		}
		return items;
	}

	public static void pickupCannon(Player player, int objectId, Location objLocation, boolean loggedOut) {

		Location loc = cannons.get(player.username);
		if (loc == null) {
			player.sendMessage("This isn't yours to pick up!");
			return;
		}

		// if (loggedOut) {
		//      objectId = player.getCannon().getObject().objectId;
		//  }

		List<Integer> items = objectIdToItemList(player.getCannon().getObjectId());

		boolean giveBalls = false;
		if (player.getCannon().getBalls() > 0) {
			if (!player.getItems().playerHasItem(2)) {
				giveBalls = true;
			}
		}

		int requiredSlots = items.size() + (giveBalls ? 1 : 0);

		if (!loggedOut && player.getItems().freeInventorySlots() < requiredSlots) {
			player.sendMessage("You do not have enough free inventory space to do this.");
			return;
		}

		if (loggedOut && player.getItems().freeInventorySlots() < requiredSlots) {
			for (int item : items) {
				player.getItems().addDirectlyToBank(item, 1);
			}
			if (player.getCannon().getBalls() > 0) {
				player.getItems().addDirectlyToBank(2, player.getCannon().getBalls());
			}
		} else {
			for (int item : items) {
				player.getItems().addItem(item, 1);
			}
			if (player.getCannon().getBalls() > 0) {
				player.getItems().addItem(2, player.getCannon().getBalls());
			}
		}

		Server.objectManager.removeObject(loc, 10, true);

		player.setCannon(null);
		cannons.remove(player.username);
	}

	public static void playerDied(Player player) {
		if (player.getCannon() == null) {
			return;
		}

		Location objLocation = cannons.get(player.username);
		Server.objectManager.removeObject(objLocation, 10, true);

		List<Integer> items = objectIdToItemList(player.getCannon().getObject().objectId);

		for (int item : items) {
			player.getItems().addDirectlyToBank(item, 1);
		}
		if (player.getCannon().getBalls() > 0) {
			player.getItems().addDirectlyToBank(2, player.getCannon().getBalls());
		}

		GameCycleTaskHandler.addEvent(player, container -> {
			player.sendMessage("Your cannon and balls if loaded have been returned to your bank.");
		}, 1);

		player.setCannon(null);
		cannons.remove(player.username);
	}

	private static void startCannonballProjectile(Player player, NPC npc) {
		int oX = player.getCannon().getObject().objectX + 1;
		int oY = player.getCannon().getObject().objectY + 1;
		int offX = ((oX - npc.getX()) * -1);
		int offY = ((oY - npc.getY()) * -1);
		player.getPA().createPlayersProjectile(oX, oY, offY, offX, 50, 60, 53, 20, 20, npc.id + 1, 30);
	}

}