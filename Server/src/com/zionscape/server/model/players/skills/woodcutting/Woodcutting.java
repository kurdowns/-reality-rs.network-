package com.zionscape.server.model.players.skills.woodcutting;

import com.zionscape.server.gamecycle.GameCycleTask;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.MapObject;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.objects.GameObject;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.skills.SkillItems;
import com.zionscape.server.model.players.skills.SkillUtils;
import com.zionscape.server.model.players.skills.firemaking.Logs;
import com.zionscape.server.model.region.RegionUtility;
import com.zionscape.server.util.CollectionUtil;
import com.zionscape.server.util.Misc;
import com.zionscape.server.world.ItemDrops;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Handles the actions for the skill Woodcutting
 *
 * @author Stuart
 */
public final class Woodcutting {

	private static List<Location> depletedTrees = new ArrayList<>();

	private static final int SEED_NEST = 5073;
	private static final int RING_NEST = 5074;
	private static final int EMPTY_NEST = 5075;
	private static final int[] SEEDS = new int[] { 5312, 5283, 5284, 5285, 5313, 5286, 5314, 5484,5288, 5289, 5290, 5315, 5316, 5317 };
	private static final int[] RINGS = new int[] { 1635, 1637, 1639, 1641, 1643 };

	private static int getNestRate(int log) {

		switch (log) {
			case 1511: // normal and evergreen
				return 500;
			case 1521: // oak
				return 400;
			case 1519: // willow
				return 350;
			case 1517: // maple
				return 350;
			case 1515: // yew
				return 250;
			case 1513: // magic
				return 200;
		}

		return Integer.MAX_VALUE;
	}

	public static boolean onItemClicked(Player player, int itemId, int itemSlot, int option) {

		if(itemId == SEED_NEST) {
			player.getItems().deleteItem(SEED_NEST, 1);
			player.getItems().addItem(EMPTY_NEST, 1);
			player.getItems().addItem(SEEDS[Misc.random(SEEDS.length - 1)], 1);
			return true;
		}

		if(itemId == RING_NEST) {
			player.getItems().deleteItem(RING_NEST, 1);
			player.getItems().addItem(EMPTY_NEST, 1);
			player.getItems().addItem(RINGS[Misc.random(RINGS.length - 1)], 1);
			return true;
		}

		return false;
	}

	/**
	 * Handles woodcutting
	 *
	 * @param player         the player
	 * @param objectId       tree id
	 * @param objectLocation tree location
	 * @return false not a tree true handled.
	 */
	public static boolean onObjectClick(Player player, int objectId, Location objectLocation, int option) {
		if (player.isBusy())
			return false;

		final Tree tree = Tree.getTree(objectId);
		if (tree == null)
			return false;

		final Axe axe = Axe.get(player);
		if (axe == null) {
			player.sendMessage("You need a hatchet to chop down this tree.");
			return true;
		}

		if (player.getActualLevel(PlayerConstants.WOODCUTTING) < tree.getLevelRequired()) {
			player.sendMessage("You need a Woodcutting level of " + tree.getLevelRequired() + " to chop down this tree.");
			return true;
		}

		if (player.getItems().freeInventorySlots() <= 0) {
			player.sendMessage("You do not have enough inventory space.");
			return true;
		}

		if (depletedTrees.contains(objectLocation)) {
			player.sendMessage("This tree has not grown back yet.");
			return true;
		}

		final Location playerLocation = player.getLocation();
		player.setBusy(true);
		player.skilling = true;
		player.startAnimation(axe.getAnimation());
		GameCycleTaskHandler.addEvent(player, new GameCycleTask() {

			int wcCount = wcAlgorithm(player.getActualLevel(PlayerConstants.WOODCUTTING), axe.getBonus(), tree.getDelay());
			int tempCount = 0;
			int count = 0;

			@Override
			public void execute(final GameCycleTaskContainer container) {
				count++;
				tempCount++;

				if (!player.skilling) {
					container.stop();
					return;
				}

				//if (!playerLocation.equals(player.getLocation())) {
				//    container.stop();
				//    return;
				// }
				if (!player.isBusy()) {
					container.stop();
					return;
				}
				if (count >= 2) {
					player.startAnimation(axe.getAnimation());
					count = 0;
				}

				if (depletedTrees.contains(objectLocation)) {
					container.stop();
					return;
				}

				if (tempCount >= wcCount) {

					double xpMulti = 1;
					int skillItemCount = SkillItems.getLumberJactItemsWorn(player);

					if(skillItemCount > 0) {
						if(skillItemCount >= 4) {
							xpMulti += 0.10;
						} else {
							xpMulti += skillItemCount * 0.02;
						}
					}

					wcCount = wcAlgorithm(player.getActualLevel(PlayerConstants.WOODCUTTING), axe.getBonus(), tree.getDelay());
					tempCount = 0;

					if(skillItemCount >= 4 && Misc.random(5) == 0) {
						player.getItems().addItem(ItemUtility.getNotedId(tree.getLogs()), 1);
					} else {
						player.getItems().addItem(tree.getLogs(), 1);
					}

					player.getPA().addSkillXP((int)Math.round(xpMulti * (double)tree.getXp()), PlayerConstants.WOODCUTTING);


					// there is a 1/3 chance that the adze's heat will instantly incinerate any logs that it cuts,
					// leaving behind no ashes and granting full Woodcutting and Firemaking experience for cutting
					// and burning the logs.
					boolean dontGiveLogs = false;
					if (axe == Axe.INFERNO && Misc.random(2) == 0) {
						Logs fireType = Arrays.asList(Logs.values()).stream().filter(x -> x.getId() == tree.getLogs()).findAny().orElse(null);
						if (fireType != null) {
							player.getPA().addSkillXP(fireType.getXp(), PlayerConstants.FIREMAKING);
							player.sendMessage("Your Inferno Adze incinerates the logs granting you Firemaking xp.");
							dontGiveLogs = true;
						}
					}

					if(SkillUtils.givePet(player.getActualLevel(PlayerConstants.WOODCUTTING), tree.getLogs(), skillItemCount >= 4 ? 0.05 : 0)) {
						player.getItems().addOrBank(25082, 1);
						player.sendMessage("You have been given a Beaver pet.");
					}

					if(Misc.random(getNestRate(tree.getLogs())) == 0) {
						ItemDrops.createGroundItem(player, SEED_NEST + Misc.random(1), player.getX() + 1, player.getY(), 1, player.getId(), true);
						player.sendMessage("A bird nest appears.");
					}

					if (tree.getLogs() == 1511) {
						Achievements.progressMade(player, Achievements.Types.CHOP_150_TREES);
					}
					if (tree.getLogs() == 1517) {
						Achievements.progressMade(player, Achievements.Types.CUT_500_MAPLES);
					}
					if (tree.getLogs() == 1515) {
						Achievements.progressMade(player, Achievements.Types.CUT_1000_YEW_LOGS);
					}
					if (tree.getLogs() == 1513) {
						Achievements.progressMade(player, Achievements.Types.CHOP_2000_MAGIC_LOGS);
					}

					if (!dontGiveLogs) {
						player.sendMessage("You get some logs.");
						if (player.getItems().freeInventorySlots() <= 0) {
							player.sendMessage("You cannot hold any more logs.");
							container.stop();
							return;
						}
					}
					if (tree.getAmount() == 1 || Misc.random(tree.getAmount()) == 0) {

						Optional<MapObject> optional = RegionUtility.getMapObject(objectId, objectLocation);
						if (optional.isPresent()) {

							depletedTrees.add(objectLocation);

							MapObject mapObject = optional.get();
							//public GameObject(int id, int x, int y, int height, int face, int type, int newId, int ticks) {
							new GameObject(tree.getStump(), objectLocation.getX(), objectLocation.getY(), objectLocation.getZ(), mapObject.getOrientation(), mapObject.getType(), objectId, tree.getRespawnTime());
							//ObjectHandler.addObject(new MapObject(tree.getStump(), objectLoc, object.getType(), object.getRotation(), object, tree.getRespawnTime(), Type.ORIGINAL));

							GameCycleTaskHandler.addEvent(Woodcutting.class, container1 -> {
								depletedTrees.remove(objectLocation);
								container1.stop();
							}, tree.getRespawnTime());

						}
						GameCycleTaskHandler.stopEvents("wc" + objectLocation.getX() + ":" + objectLocation.getY());
					}
				}
			}

			@Override
			public void stop() {
				player.startAnimation(65535);
				player.setBusy(false);
			}
		}, 2).setName("wc" + objectLocation.getX() + ":" + objectLocation.getY());
		return true;
	}

	private static int wcAlgorithm(final int level, final int axe, final int delay) {
		return Misc.random(((20 - (level / 8)) - axe) + delay) / 2;
	}

}
