package com.zionscape.server.model.players.skills.farming;

import com.zionscape.server.gamecycle.GameCycleTask;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.skills.SkillUtils;
import com.zionscape.server.util.Misc;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class Farming {

	public static final int SPADE = 952;
	public static final int FULL_PLANT_POT = 5356;
	/**
	 *
	 */
	private static final int RAKE = 5341;
	private static final int NORMAL_COMPOST = 6032;
	private static final int SUPER_COMPOST = 6034;
	private static final int EMPTY_BUCKET = 1925;
	private static final int SEED_DIBBER = 5343;
	private static final int WEEDS = 6055;
	private static final int RAKE_ANIMATION = 2273;
	private static final int WATERING_ANIMATION = 2293;
	private static final int COMPOSTING_ANIMATION = 2283;
	private static final int PLANTING_ANIMATION = 2291;

	private static final int[] REFILLABLE_OBJECT_IDS = {874, 26945, 26966, 24265};

	/**
	 * todo ugly code needs refactoring
	 *
	 * @param player
	 * @param firstItemId
	 * @param firstItemSlot
	 * @param secondItemId
	 * @param secondItemSlot
	 * @return
	 */
	public static boolean itemOnItem(Player player, int firstItemId, int firstItemSlot, int secondItemId, int secondItemSlot) {

		// seed on pot
		if (firstItemId >= 5312 && firstItemId <= 5316 && secondItemId == FULL_PLANT_POT) {
			player.getItems().deleteItem(firstItemId, 1);
			player.getItems().deleteItem(FULL_PLANT_POT, 1);
			player.getItems().addItem(5358 + (firstItemId - 5312), 1);
			return true;
		}

		// watering can on seedling
		if (firstItemId >= 5333 && firstItemId <= 5340 && secondItemId >= 5358 && secondItemId <= 5362) {
			player.getItems().deleteItem(firstItemId, 1);
			player.getItems().addItem(firstItemId - 2, 1);
			player.getItems().deleteItem(secondItemId, 1);
			player.getItems().addItem(5370 + (secondItemId - 5358), 1);
			return true;
		}

		return false;
	}

	/**
	 * @param player
	 * @param objectID
	 * @param objectLocation
	 * @param itemId
	 * @return
	 */
	public static boolean itemOnObject(Player player, int objectID, Location objectLocation, int itemId) {

		if (player.isBusy()) {
			return false;
		}

		// refill watering can
		if (itemId >= 5331 && itemId <= 5340 && Arrays.binarySearch(REFILLABLE_OBJECT_IDS, objectID) > -1) {

			player.getItems().deleteItem(itemId, 1);
			player.getItems().addItem(5340, 1);
			player.sendMessage("You fill the watering can.");

			return true;
		}

		// empty compost bin
		if ((objectID == 7808 || objectID == 7809) && itemId == WEEDS) {
			CompostBin bin = player.getData().getCompostBins().computeIfAbsent(objectLocation, x -> new CompostBin());

			if (bin.getVegationCount() == 15) {
				player.sendMessage("The compost bin is already full.");
				return true;
			}

			int amount = player.getItems().getItemAmount(WEEDS);

			if (amount > 15) {
				amount = 15;
			}

			if (bin.getVegationCount() + amount > 15) {
				amount = 15 - bin.getVegationCount();
			}

			player.startAnimation(832);
			bin.setVegationCount(bin.getVegationCount() + amount);
			player.getItems().deleteItem(WEEDS, amount);

			player.sendMessage("You fill the compost bin with weeds.");

			loadCompostBins(player);
			return true;
		}

		//filling bucket from compost bin
		if ((objectID == 7814 || objectID == 7815) && itemId == EMPTY_BUCKET) {
			CompostBin bin = player.getData().getCompostBins().computeIfAbsent(objectLocation, x -> new CompostBin());

			player.getItems().deleteItem(EMPTY_BUCKET, 1);
			player.getItems().addItem(NORMAL_COMPOST, 1);

			player.startAnimation(832);
			bin.setVegationCount(bin.getVegationCount() - 1);
			if (bin.getVegationCount() == 0) {
				bin.reset();
			}

			loadCompostBins(player);
			return true;
		}

		Optional<Patches> optionalPatch = Arrays.asList(Patches.values()).stream().filter(
				p -> p.getObjectIds().contains(objectID)
		).findAny();

		if (!optionalPatch.isPresent()) {
			return false;
		}

		Patches farmingPatch = optionalPatch.get();
		Patch playerPatch = Farming.getPatch(player, objectID);

		// raking the patch
		if (itemId == RAKE) {
			if (playerPatch.getSeed() >= 3) {
				player.sendMessage("This patch is already free of weeds.");
				return true;
			}

			player.startAnimation(RAKE_ANIMATION);
			player.setBusy(true);

			GameCycleTaskHandler.addEvent(player, container -> {
				playerPatch.setLastUpdated(System.currentTimeMillis());
				playerPatch.setSeed(playerPatch.getSeed() + 1);
				farmingPatch.send(player);

				if (playerPatch.getSeed() == 3) {
					player.getPA().addSkillXP(24, PlayerConstants.FARMING);
					player.getItems().addItem(WEEDS, 1);
					player.setBusy(false);
					container.stop();
				}
			}, 1, true);

			return true;
		}

		// watering can
		if (itemId >= 5333 && itemId <= 5340) {
			if (playerPatch.getSeed() <= 3 || playerPatch.getState() == State.WATERED || farmingPatch.toString().equalsIgnoreCase("HERB_1") || farmingPatch.toString().equalsIgnoreCase("TREE_1")) {
				player.sendMessage("This patch doesn't need watering.");
				return true;
			}
			Seeds seed = playerPatch.getSeedType();
			if (seed.getStateBits()[seed.getStateBits().length - 1] == playerPatch.getSeed()) {
				player.sendMessage("This patch doesn't need watering.");
				return true;
			}

			player.getItems().deleteItem(itemId, 1);
			player.getItems().addItem(itemId == 5333 ? 5331 : itemId - 1, 1);

			player.startAnimation(WATERING_ANIMATION);
			playerPatch.setState(State.WATERED);
			farmingPatch.send(player);
			return true;
		}

		// compost
		if (itemId == NORMAL_COMPOST || itemId == SUPER_COMPOST) {
			if (playerPatch.getSeed() < 3) {
				player.sendMessage("This patch needs weeding first.");
				return true;
			}

			if (playerPatch.getTreatedWith() != null) {
				player.sendMessage("This allotment has already been treated with compost.");
				return true;
			}

			player.getPA().addSkillXP(itemId == NORMAL_COMPOST ? 34 : 52, PlayerConstants.FARMING);
			player.startAnimation(COMPOSTING_ANIMATION);
			playerPatch.setTreatedWith(itemId == NORMAL_COMPOST ? Compost.NORMAL : Compost.SUPER);
			player.getItems().deleteItem(itemId, 1);
			player.getItems().addItem(EMPTY_BUCKET, 1);

			return true;
		}

		//harvesting
		if (itemId == SPADE) {
			harvest(player, playerPatch, farmingPatch);
			return true;
		}

		Optional<Seeds> optional = farmingPatch.getSeeds().stream().filter(x -> x.getSeedId() == itemId).findAny();
		if (!optional.isPresent()) {

			String itemName = ItemUtility.getName(itemId);
			if (itemName.toLowerCase().endsWith("seed")) {
				player.sendMessage("You cannot use this seed on this type of patch.");
			} else {
				player.sendMessage("Nothing interesting happens.");
			}

			return false;
		}

		if (playerPatch.getSeed() < 3) {
			player.sendMessage("The patch needs raking first before you do that.");
			return false;
		}

		if (playerPatch.getSeed() > 3) {
			player.sendMessage("There is already something growing in this patch.");
			return true;
		}


		Seeds plantingSeed = optional.get();
		if (!player.getItems().playerHasItem(SEED_DIBBER)) {
			player.sendMessage("You require a Seed dibber to plant these.");
			return true;
		}
		if (player.getActualLevel(PlayerConstants.FARMING) < plantingSeed.getLevelRequired()) {
			player.sendMessage("A level requirement of " + plantingSeed.getLevelRequired() + " farming is required to plant these.");
			return true;
		}

		Achievements.progressMade(player, Achievements.Types.FARM_500_SEEDS);

		player.startAnimation(PLANTING_ANIMATION);
		player.getItems().deleteItem(plantingSeed.getSeedId(), 1);
		player.getPA().addSkillXP((int) plantingSeed.getPlantingXp(), PlayerConstants.FARMING);
		playerPatch.setSeed(plantingSeed.getStateBits()[0]);
		playerPatch.setSeedType(plantingSeed);
		farmingPatch.send(player);
		return true;
	}

	/**
	 * @param player
	 * @param objectID
	 * @param objectLocation
	 * @param option
	 * @return
	 */
	public static boolean onObjectClick(Player player, int objectID, Location objectLocation, int option) {


		// closing compost bin
		if (objectID == 7810) {
			CompostBin bin = player.getData().getCompostBins().computeIfAbsent(objectLocation, x -> new CompostBin());

			player.startAnimation(832);
			player.sendMessage("You close the compost bin.");
			player.sendMessage("The contents have begun to rot.");

			bin.setClosed(true);
			loadCompostBins(player);
			return true;
		}


		// opening closed bin
		if (objectID == 7813) {
			CompostBin bin = player.getData().getCompostBins().computeIfAbsent(objectLocation, x -> new CompostBin());

			if (System.currentTimeMillis() - bin.getClosedAt() < 10 * 60 * 1000) {
				player.sendMessage("The vegetation hasn't finished rotting yet.");
				return true;
			}

			player.startAnimation(832);
			player.getPA().addSkillXP(5, PlayerConstants.FARMING);
			player.sendMessage("You open the compost bin.");

			bin.setCompost(true);
			bin.setClosed(false);
			loadCompostBins(player);
			return true;
		}

		Optional<Patches> optionalPatch = Arrays.asList(Patches.values()).stream().filter(
				p -> p.getObjectIds().contains(objectID)
		).findAny();

		if (!optionalPatch.isPresent()) {
			return false;
		}

		Patches farmingPatch = optionalPatch.get();
		Patch playerPatch = Farming.getPatch(player, objectID);

		// harvest
		if (option == 1) {
			harvest(player, playerPatch, farmingPatch);
		}

		//inspect
		if (option == 2) {

			StringBuilder builder = new StringBuilder();

			builder.append("This is ");

			if (Misc.startsWithVowel(farmingPatch.getName())) {
				builder.append("an ");
			} else {
				builder.append("a ");
			}

			builder.append(farmingPatch.getName());
			builder.append(". ");

			if (playerPatch.getTreatedWith() != null) {
				if (playerPatch.getTreatedWith() == Compost.NORMAL) {
					builder.append("The soil has been treated with compost. ");
				} else {
					builder.append("The soil has been treated with supercompost. ");
				}
			} else {
				builder.append("The soil has not been treated. ");
			}


			if (playerPatch.getSeed() < 3) {
				builder.append("The patch needs weeding.");
			} else if (playerPatch.getSeed() == 3) {
				builder.append("The patch is empty and weeded.");
			} else {
				Seeds seed = playerPatch.getSeedType();
				if (seed.getStateBits()[seed.getStateBits().length - 1] == playerPatch.getSeed()) {
					builder.append("The patch is fully grown.");
				} else {
					builder.append("The patch has something growing in it.");
				}
			}

			player.sendMessage(builder.toString(), true);
		}

		return true;
	}

	public static Patch getPatch(Player player, int objectId) {

		if (!player.getData().getPatches().containsKey(objectId)) {
			player.getData().getPatches().put(objectId, new Patch());
		}

		return player.getData().getPatches().get(objectId);
	}

	public static void onPlayerLoggedIn(Player player) {

		for (Map.Entry<Integer, Patch> entry : player.getData().getPatches().entrySet()) {
			Patch p = entry.getValue();

			if (p.getSeed() == 0) {
				continue;
			}

			if (p.getSeed() <= 3) {
				p.reset();
				continue;
			}

			Seeds seed = p.getSeedType();

			double divide = (double) seed.getGrowTime() / ((double) seed.getStateBits().length - 1);

			for (int i = 1; i < seed.getStateBits().length; i++) {
				if (System.currentTimeMillis() - p.getLastUpdated() > ((divide * i) * 60 * 1000)) {
					p.setSeed(seed.getStateBits()[i]);
				}
			}
		}

		for (Patches p : Patches.values()) {
			p.send(player);
		}

		// reset patch back to weeded after 1 minute
		GameCycleTaskHandler.addEvent(player, weedsContainer -> {
			for (Map.Entry<Integer, Patch> entry : player.getData().getPatches().entrySet()) {
				int objectId = entry.getKey();
				Patch patch = entry.getValue();

				// reset the weeds status
				if (patch.getSeed() == 3 && System.currentTimeMillis() - patch.getLastUpdated() > 60 * 1000) {
					patch.reset();

					Optional<Patches> farmingPatch = Arrays.asList(Patches.values()).stream().filter(
							x -> x.getObjectIds().contains(objectId)
					).findAny();
					farmingPatch.get().send(player);
					continue;
				}

				Seeds seed = patch.getSeedType();
				if (seed == null) {
					continue;
				}

				boolean updated = false;
				double divide = (double) seed.getGrowTime() / ((double) seed.getStateBits().length - 1);
				for (int i = 1; i < seed.getStateBits().length; i++) {
					if (System.currentTimeMillis() - patch.getLastUpdated() > ((divide * i) * 60 * 1000)) {
						if (seed.getStateBits()[i] > patch.getSeed()) {
							patch.setState(State.GROWING);
							patch.setSeed(seed.getStateBits()[i]);
							updated = true;
						}
					}
				}

				if (updated) {
					Optional<Patches> farmingPatch = Arrays.asList(Patches.values()).stream().filter(
							x -> x.getObjectIds().contains(objectId)
					).findAny();
					farmingPatch.get().send(player);
				}
			}
		}, 2);
	}

	private static void harvest(Player player, Patch playerPatch, Patches farmingPatch) {

		if (playerPatch.getSeed() <= 3) {
			player.sendMessage("There is nothing growing in this patch.");
			return;
		}

		Seeds seed = playerPatch.getSeedType();

		if (seed.getStateBits()[seed.getStateBits().length - 1] != playerPatch.getSeed()) {
			player.sendMessage("This patch is not ready for harvesting yet.");
			return;
		}

		if (!player.getItems().playerHasItem(SPADE)) {
			player.sendMessage("You need a spade to do that.");
			return;
		}

		player.sendMessage("You begin to harvest the patch.");

		if(player.isBusy()) {
			return;
		}

		player.setBusy(true);
		GameCycleTaskHandler.addEvent(player, new GameCycleTask() {

			@Override
			public void execute(GameCycleTaskContainer container) {

				if (player.getItems().freeInventorySlots() == 0) {
					player.sendMessage("You do not have enough inventory space to do this.");
					container.stop();
					return;
				}

				player.startAnimation(831);
				player.getPA().addSkillXP((int) seed.getHarvestingXp(), PlayerConstants.FARMING);
				player.getItems().addItem(seed.getHarvestId(), 1);

				int petItem = -1;
				switch (seed.getHarvestId()) {
					case 1521:
						petItem = 5;
						break;
					case 1519:
						petItem = 6;
						break;
					case 1517:
						petItem = 7;
						break;
					case 1515:
						petItem = 8;
						break;
					case 1513:
						petItem = 9;
						break;
				}

				if(petItem > -1) {
					if(SkillUtils.checkSkillRequirement(player, PlayerConstants.FARMING) && SkillUtils.givePet(player.getActualLevel(PlayerConstants.FARMING), petItem)) {
						player.getItems().addOrBank(25620, 1);
						player.sendMessage("You have been given a Tangleroot pet.");
					}
				}

				int given = player.getData().harvestingAmount++;

				if (farmingPatch.getName().equalsIgnoreCase("herb patch")) {
					given++;
					if (given >= 11 || given > 5 && Misc.random(3) == 0) {
						player.sendMessage("The patch is now empty.");
						playerPatch.reset();
						//playerPatch.setSeed(0);
						farmingPatch.send(player);

						player.getData().harvestingAmount = 0;

						container.stop();
					}
				} else {
					if (Misc.random(4) == 0) {
						player.sendMessage("The patch is now empty.");
						playerPatch.reset();
						//playerPatch.setSeed(0);
						farmingPatch.send(player);

						player.getData().harvestingAmount = 0;

						container.stop();
					}
				}
			}

			@Override
			public void stop() {
				player.setBusy(false);
				player.stopAnimation();
			}
		}, 3, true).setStopUponWalking(true);
	}

	public static void loadCompostBins(Player player) {
		// catherby
		CompostBin bin = player.getData().getCompostBins().computeIfAbsent(Location.create(2804, 3464, 0), x -> new CompostBin());

		if (bin.isClosed()) {
			player.getPA().customObject(7813, 2804, 3464, 0, 3, 10);
		} else if (bin.isCompost()) {
			if (bin.getVegationCount() == 15) {
				player.getPA().customObject(7815, 2804, 3464, 0, 3, 10);
			} else {
				player.getPA().customObject(7814, 2804, 3464, 0, 3, 10);
			}
		} else if (bin.getVegationCount() == 0) {
			player.getPA().customObject(7808, 2804, 3464, 0, 3, 10);
		} else if (bin.getVegationCount() > 0 && bin.getVegationCount() < 15) {
			player.getPA().customObject(7809, 2804, 3464, 0, 3, 10);
		} else if (bin.getVegationCount() == 15) {
			player.getPA().customObject(7810, 2804, 3464, 0, 3, 10);
		}
	}

}
