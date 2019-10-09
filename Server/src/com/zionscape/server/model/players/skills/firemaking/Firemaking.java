package com.zionscape.server.model.players.skills.firemaking;

import com.zionscape.server.Server;
import com.zionscape.server.cache.Collision;
import com.zionscape.server.gamecycle.GameCycleTask;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.objects.GameObject;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.skills.SkillUtils;
import com.zionscape.server.util.Misc;
import com.zionscape.server.world.ItemDrops;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class Firemaking {

	private static final int TINDERBOX = 590;
	private static ArrayList<Fire> fires = new ArrayList<>();

	public static boolean onItemOnObject(Player player, int itemId, int objectId, Location objectLocation) {
		if(objectId != 2732) {
			return false;
		}

		if(player.isBusy()) {
			return false;
		}

		Logs fireType = Arrays.asList(Logs.values()).stream().filter(x -> x.getId() == itemId).findAny().orElse(null);
		if (fireType == null) {
			return false;
		}

		if (fireType.getLevel() > player.level[PlayerConstants.FIREMAKING]) {
			player.sendMessage("You need a fire making level of " + fireType.getLevel() + " to light this.");
			return true;
		}

		Optional<Fire> optional = fires.stream().filter(x -> x.getLocation().equals(objectLocation)).findAny();
		if(!optional.isPresent()) {
			return false;
		}
		Fire fire = optional.get();

		player.turnPlayerTo(objectLocation.getX(), objectLocation.getY());
		player.startAnimation(16703);
		player.gfx50(fireType.getBonfireGraphic());
		player.setBusy(true);
		fire.setUsing(fire.getUsing() + 1);

		Location playerLocation = player.getLocation();

		GameCycleTaskHandler.addEvent(player, new GameCycleTask() {
			@Override
			public void execute(GameCycleTaskContainer container) {

				if(fires.stream().filter(x -> x.getLocation().equals(objectLocation)).count() == 0) {
					container.stop();
					return;
				}

				// players moved
				if (!player.getLocation().equals(playerLocation)) {
					container.stop();
					return;
				}

				if(!player.getItems().playerHasItem(itemId)) {
					container.stop();
					return;
				}

				Achievements.progressMade(player, Achievements.Types.LIGHT_100_FIRES);

				if (fireType == Logs.MAPLE) {
					Achievements.progressMade(player, Achievements.Types.BURN_400_MAPLE_LOGS);
				}

				if (fireType == Logs.YEW) {
					Achievements.progressMade(player, Achievements.Types.BURN_1000_YEW_TREES);
				}
				if (fireType == Logs.MAGIC) {
					Achievements.progressMade(player, Achievements.Types.LIGHT_1000_MAGIC_LOGS);
					Achievements.progressMade(player, Achievements.Types.BURN_2000_MAGIC_LOGS);
				}

				if(SkillUtils.checkSkillRequirement(player, PlayerConstants.FIREMAKING) && SkillUtils.givePet(player.getActualLevel(PlayerConstants.FIREMAKING), fireType.getPetIndex())) {
					player.sendMessage("You have been given a Phoenix pet.");
					player.getItems().addOrBank(25636, 1);
				}

				if(!player.attributeExists("fire_boost")) {

					if(!player.attributeExists("logs_burned")) {
						player.setAttribute("logs_burned", 0);
					}

					player.setAttribute("logs_burned", (int)player.getAttribute("logs_burned") + 1);
					if ((int)player.getAttribute("logs_burned") >= 5) {
						player.setAttribute("fire_boost", getHpBoost(player.getActualLevel(PlayerConstants.FIREMAKING)));
						player.removeAttribute("logs_burned");
						GameCycleTaskHandler.addEvent(player, container1 -> {
							player.sendMessage("Your firemaking HP boost has ran out.");
							player.removeAttribute("fire_boost");
							container1.stop();
						}, 1000);
						player.sendMessage("You have received a HP boost from burning the logs.");
					}
				}

				player.startAnimation(16703);
				player.gfx50(fireType.getBonfireGraphic());
				player.getItems().deleteItem(itemId, 1);

				double percentage = 1;
				if(fire.getUsing() > 1) {
					percentage += (0.01 * fire.getUsing()) - 0.01;

					if(percentage > 1.05) {
						percentage = 1.05;
					}
				}

				int xp = fireType.getBonfireXp();
				if(percentage > 1) {
					xp = (int)Math.ceil((double)xp * percentage);
				}

				player.getPA().addSkillXP(xp, PlayerConstants.FIREMAKING);
			}

			@Override
			public void stop() {
				player.setBusy(false);
			}
		}, 5).setName("bonfire:" + objectLocation.getX() + ":" + objectLocation.getY());
		return false;
	}

	public static boolean itemOnItem(Player player, int itemUsed, int usedWith) {

		if (player.isBusy()) {
			return false;
		}

		if (itemUsed != TINDERBOX && usedWith != TINDERBOX) {
			return false;
		}

		Logs fireType = Arrays.asList(Logs.values()).stream().filter(x -> x.getId() == itemUsed || x.getId() == usedWith).findAny().orElse(null);
		if (fireType == null) {
			return false;
		}

		if (ItemDrops.itemExists(player.getLocation()) || fires.stream().filter(x -> x.getLocation().equals(player.getLocation())).count() > 0) {
			player.sendMessage("You cannot light a fire here.");
			return true;
		}
		if (Collision.getClipping(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()) != 0) {
			player.sendMessage("You cannot light a fire here.");
			return true;
		}
		if (fireType.getLevel() > player.level[PlayerConstants.FIREMAKING]) {
			player.sendMessage("You need a fire making level of " + fireType.getLevel() + " to light this.");
			return true;
		}
		player.setBusy(true);
		player.startAnimation(733);
		player.getItems().deleteItem(fireType.getId(), 1);

		final int cycles = Misc.random((10 + Misc.random(fireType.getLevel() / 10)) - Misc.random(player.getPA().getActualLevel(PlayerConstants.FIREMAKING) / 10));
		final Location playerLocation = Location.create(player.absX, player.absY, player.heightLevel);

		GameCycleTaskHandler.addEvent(player, new GameCycleTask() {

			@Override
			public void execute(final GameCycleTaskContainer container) {

				// players moved
				if (!player.getLocation().equals(playerLocation)) {
					container.stop();
					return;
				}

				Achievements.progressMade(player, Achievements.Types.LIGHT_100_FIRES);

				if (fireType == Logs.MAPLE) {
					Achievements.progressMade(player, Achievements.Types.BURN_400_MAPLE_LOGS);
				}

				if (fireType == Logs.YEW) {
					Achievements.progressMade(player, Achievements.Types.BURN_1000_YEW_TREES);
				}
				if (fireType == Logs.MAGIC) {
					Achievements.progressMade(player, Achievements.Types.LIGHT_1000_MAGIC_LOGS);
					Achievements.progressMade(player, Achievements.Types.BURN_2000_MAGIC_LOGS);
				}

				if(SkillUtils.checkSkillRequirement(player, PlayerConstants.FIREMAKING) && SkillUtils.givePet(player.getActualLevel(PlayerConstants.FIREMAKING), fireType.getPetIndex())) {
					player.sendMessage("You have been given a Phoenix pet.");
					player.getItems().addOrBank(25636, 1);
				}

				if (ItemDrops.itemExists(player.getLocation()) || fires.stream().filter(x -> x.equals(player.getLocation())).findAny().orElse(null) != null) {
					player.sendMessage("You cannot light a fire here.");
					container.stop();
					return;
				}

				fires.add(new Fire(playerLocation));

				GameObject fireObject = new GameObject(2732, player.getX(), player.getY(), player.heightLevel, 0, 10, -1, -1);
				GameCycleTaskHandler.addEvent(null, new GameCycleTask() {
					@Override
					public void execute(final GameCycleTaskContainer container) {
						fires.removeIf(x -> x.getLocation().equals(playerLocation));

						Server.objectManager.removeObject(fireObject);
						Server.objectManager.objects.remove(fireObject);

						//  ItemDrops.createGroundItem(player, 592, playerLocation.getX(), playerLocation.getY(), 1, player.getId());
						container.stop();
					}
				}, 200).setName("fire:" + playerLocation.getX() + " " + playerLocation.getY());

				player.startAnimation(65535);
				player.getPA().addSkillXP(fireType.getXp(), PlayerConstants.FIREMAKING);
				player.setFiremakingWalk(true);
				container.stop();
			}

			@Override
			public void stop() {
				player.setBusy(false);
			}

		}, cycles);
		return true;
	}

	public static void clippedFiremaking(final Player player) {
		if (!Collision.blockedWest(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getY())) {
			player.getPA().walkTo(-1, 0);
		} else if (!Collision.blockedEast(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getY())) {
			player.getPA().walkTo(1, 0);
		} else if (!Collision.blockedSouth(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getY())) {
			player.getPA().walkTo(0, -1);
		} else if (!Collision.blockedNorth(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getY())) {
			player.getPA().walkTo(0, 1);
		}

		player.setFiremakingWalk(false);
	}

	private static double getHpBoost(int level) {

		if(level >= 10 && level <= 19) {
			return 0.013;
		}

		if(level >= 20 && level <= 29) {
			return 0.015;
		}

		if(level >= 30 && level <= 39) {
			return 0.02;
		}

		if(level >= 40 && level <= 49) {
			return 0.025;
		}

		if(level >= 50 && level <= 59) {
			return 0.027;
		}

		if(level >= 60 && level <= 69) {
			return 0.036;
		}

		if(level >= 70 && level <= 79) {
			return 0.039;
		}

		if(level >= 80 && level <= 89) {
			return 0.042;
		}

		if(level >= 90 && level <= 97) {
			return 0.046;
		}

		if(level >= 98) {
			return 0.05;
		}

		return 0.01;
	}

}