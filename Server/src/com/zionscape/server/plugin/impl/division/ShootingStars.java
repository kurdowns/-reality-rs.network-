package com.zionscape.server.plugin.impl.division;

import com.google.gson.GsonBuilder;
import com.zionscape.server.Server;
import com.zionscape.server.events.Listener;
import com.zionscape.server.events.impl.ClickObjectEvent;
import com.zionscape.server.gamecycle.GameCycleTask;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.DynamicMapObject;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.MapObject;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.objects.GameObject;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.skills.mining.Mining;
import com.zionscape.server.model.players.skills.mining.PickAxe;
import com.zionscape.server.model.region.RegionUtility;
import com.zionscape.server.plugin.Plugin;
import com.zionscape.server.util.CollectionUtil;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Optional;

public class ShootingStars implements Plugin, Listener {

	private static final int DELAY = 30;
	private static final int STARTING_OBJECT_ID = 38660;
	private static final int STARDUST_ID = 13727;
	private static int stage = 0;
	private static int amountMined = 0;
	private static GameObject gameObject;
	public static String locationName = "";
	private static int starId;
	
	private static StarLocation[] starLocations = { new StarLocation(Location.create(3198, 3420), "Varrock"),
			new StarLocation(Location.create(3234, 3225), "Lumbridge"),
			new StarLocation(Location.create(2969, 3400), "Falador"),
			new StarLocation(Location.create(2884, 9800), "Tavelry Dungeon"),
			new StarLocation(Location.create(3170, 2980), "Bandit Camp"),
			new StarLocation(Location.create(3034, 9583), "Asgarnia Ice Dungeon"),
			new StarLocation(Location.create(3342, 3269), "Duel Arena"),
			new StarLocation(Location.create(2728, 3478), "Seers Village"),
			new StarLocation(Location.create(2696, 9564), "Brimhaven dungeon"),
			new StarLocation(Location.create(3565, 3299), "Barrows"),
			// new StarLocation(Location.create(3996, 3996), "Donor zone"),
	};

	static {
		new GsonBuilder().setPrettyPrinting().create();

		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime nextHour = now.plusHours(1).withMinute(0).withSecond(0);

		// while ((nextHour.getHour() % 2) != 0) {
		// nextHour = nextHour.plusHours(1).withMinute(0).withSecond(0);
		// }

		long ms = Duration.between(now, nextHour).toMillis();
		int cycles = (int) Math.floor(ms / 600);

		GameCycleTaskHandler.addEvent(ShootingStars.class, container -> {
			spawnStar();
			container.stop();
		}, cycles);
	}

	private static void spawnStar() {
		// delete existing shooting star if one exists
		if (gameObject != null) {
			Location location = Location.create(gameObject.objectX, gameObject.objectY, gameObject.height);
			Optional<MapObject> optional = RegionUtility.getMapObject(gameObject.objectId, location);

			if (optional.isPresent()) {
				RegionUtility.removeEntity(optional.get(), location);
			}

			GameCycleTaskHandler.stopEvents("star-" + starId);
			GameCycleTaskHandler.stopEvents("star-yell");
			Server.objectManager.removeObject(location, 10, true);
		}

		while (true) {

			StarLocation starLocation = CollectionUtil.getRandomElement(starLocations);
			Location location = starLocation.location;

			// dont respawn in the same place as previous
			if (locationName != null && !locationName.isEmpty()) {
				if (locationName.equalsIgnoreCase(starLocation.name)) {
					continue;
				}
			}

			locationName = starLocation.name;

			PlayerHandler.yell("@blu@A shooting star has appeared in " + starLocation.name);
			PlayerHandler.yell("@blu@A shooting star has appeared in " + starLocation.name);
			PlayerHandler.yell("@blu@A shooting star has appeared in " + starLocation.name);

			GameCycleTaskHandler.addEvent(ShootingStars.class, container -> {
				PlayerHandler.yell("@blu@There is currently a shooting star in " + starLocation.name);
			}, ((6 * 60 * 1000) / 600)).setName("star-yell");

			gameObject = new GameObject(STARTING_OBJECT_ID, location.getX(), location.getY(), location.getZ(), 0, 10,
					-1, -1);
			RegionUtility.addEntity(new DynamicMapObject(STARTING_OBJECT_ID, location, 10, 0), location);

			GameCycleTaskHandler.addEvent(ShootingStars.class, container -> {
				spawnStar();
				container.stop();
			}, ((120 * 60 * 1000) / 600));

			break;
		}
	}

	@Override
	public void onObjectClicked(ClickObjectEvent event) {
		Player player = event.getPlayer();

		if (player.isBusy()) {
			return;
		}

		Optional<ShootingStar> optional = ShootingStar.getShootingStart(event.getObjectId());
		if (!optional.isPresent()) {
			return;
		}

		event.setHandled(true);
		ShootingStar star = optional.get();

		if (event.getOption() == 2) {
			player.setBusy(true);
			GameCycleTaskHandler.addEvent(player, (GameCycleTaskContainer container) -> {
				player.sendMessage("This rock contains star dust.");
				player.setBusy(false);
				container.stop();
			}, 2);
			return;
		}
		starId = star.objectId;
		final PickAxe axe = PickAxe.get(player);
		if (axe == null) {
			player.sendMessage("You need a pickaxe to mine this rock");
			return;
		}
		if (player.getActualLevel(PlayerConstants.MINING) < star.requiredLevel) {
			player.sendMessage("You need a Mining level of " + star.requiredLevel + " to mine this rock");
			return;
		}
		if (player.getItems().freeInventorySlots() <= 0) {
			player.sendMessage("You do not have enough inventory space.");
			return;
		}

		final Location playerLocation = player.getLocation();
		player.setBusy(true);
		player.startAnimation(axe.getAnimation());
		player.skilling = true;
		GameCycleTaskHandler.addEvent(player, new GameCycleTask() {
			int animCount = 0;
			int count = Mining.algorithm(player.getActualLevel(PlayerConstants.MINING), axe.getBonus(), DELAY);
			int tempCount = 0;

			@Override
			public void execute(final GameCycleTaskContainer container) {
				animCount++;
				tempCount++;
				if (animCount >= axe.getAnimationCycles()) {
					animCount = 0;
					player.startAnimation(axe.getAnimation());
				}

				if (!player.skilling) {
					container.stop();
					return;
				}

				if (!playerLocation.equals(player.getLocation())) {
					container.stop();
					return;
				}
				if (!player.isBusy()) {
					container.stop();
					return;
				}
				if (tempCount >= count) {
					count = Mining.algorithm(player.getActualLevel(PlayerConstants.MINING), axe.getBonus(), DELAY);
					tempCount = 0;

					player.getItems().addItem(STARDUST_ID, player.isLegendaryDonator() ? 2 : 1);
					player.getPA().addSkillXP(star.xp, PlayerConstants.MINING);
					player.sendMessage("You get some " + ItemUtility.getName(STARDUST_ID) + ".");

					if (player.isExtremeDonator() && !player.isLegendaryDonator()) {
						player.stardustMined++;

						if (player.stardustMined >= 3) {
							player.getItems().addItem(STARDUST_ID, 1);
							player.stardustMined = 0;
						}
					}

					boolean fullInvent = false;
					animCount = 0;
					player.startAnimation(axe.getAnimation());
					if (player.getItems().freeInventorySlots() <= 0) {
						player.sendMessage("You cannot hold any more ore.");
						fullInvent = true;
					}

					amountMined++;
					if (amountMined >= star.amount) {
						amountMined = 0;

						// stop everyone mining the rock
						GameCycleTaskHandler.stopEvents("star-" + star.objectId);

						stage++;
						if (stage >= 8) {
							Optional<MapObject> optional = RegionUtility.getMapObject(star.objectId,
									event.getObjectLocation());

							if (optional.isPresent()) {
								RegionUtility.removeEntity(optional.get(), event.getObjectLocation());
							}

							locationName = "";
							gameObject = null;
							GameCycleTaskHandler.stopEvents("star-yell");
							stage = 0;
							Server.objectManager.removeObject(event.getObjectLocation(), 10, true);
						} else {
							Optional<MapObject> optional = RegionUtility.getMapObject(star.objectId,
									event.getObjectLocation());

							if (optional.isPresent()) {
								RegionUtility.removeEntity(optional.get(), event.getObjectLocation());
							}

							Server.objectManager.removeObject(event.getObjectLocation(), 10, false);
							gameObject = new GameObject(STARTING_OBJECT_ID + stage, event.getObjectLocation().getX(),
									event.getObjectLocation().getY(), event.getObjectLocation().getZ(), 0, 10, -1, -1);
							RegionUtility.addEntity(
									new DynamicMapObject(STARTING_OBJECT_ID + stage, event.getObjectLocation(), 10, 0),
									event.getObjectLocation());
						}
					}

					if (fullInvent) {
						container.stop();
					}
				}
			}

			@Override
			public void stop() {
				player.startAnimation(65535);
				player.setBusy(false);
			}
		}, 1).setName("star-" + star.objectId);
	}

	private enum ShootingStar {

		/**
		 * 38660 Crashed star 38661 Crashed star 38662 Crashed star 38663 Crashed star
		 * 38664 Crashed star 38665 Crashed star 38666 Crashed star 38667 Crashed star
		 * 38668 Crashed star
		 */

		ROCK_90(90, 38660, 210, 15), ROCK_80(80, 38661, 145, 25), ROCK_70(70, 38662, 114, 40), ROCK_60(60, 38663, 71,
				80), ROCK_50(50, 38664, 47, 175), ROCK_40(40, 38665, 32,
						250), ROCK_30(30, 38666, 29, 439), ROCK_20(20, 38667, 25, 700), ROCK_10(10, 38668, 14, 1200);

		private final int requiredLevel;
		private final int objectId;
		private final int xp;
		private final int amount;

		ShootingStar(int requiredLevel, int objectId, int xp, int amount) {
			this.requiredLevel = requiredLevel;
			this.objectId = objectId;
			this.xp = xp;
			this.amount = amount;
		}

		public static Optional<ShootingStar> getShootingStart(int object) {

			for (ShootingStar star : ShootingStar.values()) {
				if (star.objectId == object) {
					return Optional.of(star);
				}
			}

			return Optional.empty();
		}

	}

	private static class StarLocation {
		private final Location location;
		private final String name;

		private StarLocation(Location location, String name) {
			this.location = location;
			this.name = name;
		}
	}

}
