package com.zionscape.server.model.content.treasuretrails;

import com.google.common.base.Joiner;
import com.zionscape.server.Server;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.content.treasuretrails.solutions.*;
import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.tick.Tick;
import com.zionscape.server.util.CollectionUtil;
import com.zionscape.server.util.Misc;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class TreasureTrails {

	private static final List<Solution> solutions = new ArrayList<>();

	static {
		for (Anagrams anagram : Anagrams.values()) {
			solutions.add(anagram);
		}
		for (Maps map : Maps.values()) {
			solutions.add(map);
		}
		for (Riddles riddle : Riddles.values()) {
			solutions.add(riddle);
		}
	}


	public static boolean onClickingButtons(Player player, int button) {
		if(button == 1040) {

			if(player.puzzle == null) {
				return true;
			}
			if(player.openInterfaceId != 6976) {
				return true;
			}
			if(!player.getItems().playerHasItem(995, 5_000_000)) {
				player.sendMessage("You do not have 5 million gp.");
				return true;
			}

			PuzzleBoxes box = getPuzzleBoxByItemId(player.puzzle.getItemId());
			if(box == null) {
				return true;
			}

			player.getItems().deleteItem(995, 5_000_000);
			completedSolution(player, box.getLevel(), true, box.getItemId());
			player.sendMessage("Congratulations you have completed the puzzle.");
			player.puzzle = null;
			player.getData().completedPuzzle = true;
			player.getPA().closeAllWindows();

			Achievements.progressMade(player, Achievements.Types.COMPLETE_TT_PUZZLE);
			return true;
		}
		return false;
	}


	public static boolean clickingItem(final Player player, final int itemId, final int slot) {

		// Spade
		if (itemId == 952) {
			player.startAnimation(831);

			for (int i = 0; i < player.inventory.length; i++) {
				Clues clue = getClueByItemId(player.inventory[i] - 1);

				if (clue == null) {
					continue;
				}

				if (clue.getSolution().getLocation() != null && clue.getSolution().getLocation().equals(player.getLocation())) {
					player.startAnimation(65535);
					if (!completedSolution(player, clue.getLevel(), false, clue.getItemId())) {
						player.sendMessage("You dig and find an item helping you on your trail.");
					}
					return true;
				}
			}

			// Resets the animation loop
			Server.getTickManager().submit(new Tick(3) {

				@Override
				public void execute() {
					player.startAnimation(65535);
				}
			});
		}

		final PuzzleBoxes box = getPuzzleBoxByItemId(itemId);
		if (box != null) {
			if (player.puzzle != null && box.getPuzzleId() == player.puzzle.getPuzzle()) {
				player.puzzle.openPuzzle();
			} else {
				player.puzzle = new Puzzle(player, box.getPuzzleId(), new PuzzleCompletedEvent() {

					@Override
					public void run() {
						completedSolution(player, box.getLevel(), true, itemId);
						player.sendMessage("Congratulations you have completed the puzzle.");
						player.puzzle = null;
						player.getData().completedPuzzle = true;

						Achievements.progressMade(player, Achievements.Types.COMPLETE_TT_PUZZLE);
					}
				}, itemId).openPuzzle();
			}
			return true;
		}

		final Clues clue = getClueByItemId(itemId);
		if (clue != null) {
			clue.getSolution().show(player);
			return true;
		}

		return false;
	}

	public static boolean clickingNpc(Player player, NPC npc) {
		for (Solution solution : solutions) {
			if (solution.getNpcId() == npc.type) {
				for (Clues clue : Clues.values()) {

					if (clue.getSolution() == solution && player.getItems().playerHasItem(clue.getItemId())) {
						if (!completedSolution(player, clue.getLevel(), false, clue.getItemId())) {
							player.getDH().sendStatement("You have been given an item to help you on your trail!");
						}
						return true;
					}

				}
			}
		}
		return false;
	}

	public static boolean clickingObject(Player player, int objectId, Location objectLocation) {
		for (Solution solution : solutions) {
			if (solution.getObjectId() == objectId && solution.getLocation().equals(objectLocation)) {
				for (Clues clue : Clues.values()) {
					if (clue.getSolution().equals(solution) && player.getItems().playerHasItem(clue.getItemId())) {
						if (!completedSolution(player, clue.getLevel(), false, clue.getItemId())) {
							player.getDH().sendStatement("You search and find an item to help you on your trail!");

							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private static boolean completedSolution(Player player, Level level, boolean puzzle, int itemId) {
		int max = maxSolutionsPerLevel(level);

		player.getItems().deleteItem(itemId, 1);
		player.cluesReceived.get(level).add(itemId);

		if (player.cluesReceived.get(level).size() >= max || Misc.random(max) == 0 || player.getData().clueScrollSteps + 1 > 8) {
			giveReward(player, level);
			return true;
		}

		List<Clues> clues = getCluesByLevel(level);
		Collections.shuffle(clues);

		if (!puzzle && level == Level.HARD && Misc.random(3) == 0 && !player.getData().completedPuzzle) {
			player.getItems().addItem(PuzzleBoxes.values()[Misc.random(PuzzleBoxes.values().length - 1)].getItemId(), 1);
			player.getData().clueScrollSteps++;
			return false;
		}

		while (true) {
			Clues clue = clues.get(Misc.random(clues.size() - 1));

			if (player.cluesReceived.get(level).contains(clue.getItemId())) {
				continue;
			}

			player.getData().clueScrollSteps++;

			player.getItems().addItem(clue.getItemId(), 1);
			break;
		}

		return false;
	}

	public static void giveReward(Player player, Level level) {

		player.getData().completedPuzzle = false;
		player.getData().clueScrollSteps = 0;

		if (level == Level.EASY) {
			Achievements.progressMade(player, Achievements.Types.COMPLETE_2_EASY_CLUE_SCROLLS);
		}

		if (level == Level.MEDIUM) {
			Achievements.progressMade(player, Achievements.Types.COMPLETE_15_MEDIUM_CLUES);
		}

		if (level == Level.HARD) {
			Achievements.progressMade(player, Achievements.Types.COMPLETE_200_HARD_CLUES);
		}

		player.cluesReceived.get(level).clear();

		int amount = Misc.random(3, 5);

		Item[] globalRewards = Rewards.GLOBAL.getItems();
		Item[] rewards = getItemRewardsForLevel(level);
		List<Item> items = new ArrayList<>();

		for (int i = 0; i < amount; i++) {
			if (Misc.random(5) == 0) {
				items.add(rewards[Misc.random(rewards.length - 1)]);
			} else {
				items.add(globalRewards[Misc.random(globalRewards.length - 1)]);
			}
		}

		writeLog(player, items);

		for (int i = 0; i < 5; i++) {
			if (i > (items.size() - 1)) {
				player.getPA().sendFrame34(-1, i, 6963, 0);
			} else {
				Item item = items.get(i);
				player.getItems().addOrDrop(item.getId(), item.getAmount());
				player.getPA().sendFrame34(item.getId(), i, 6963, item.getAmount());
			}
		}

		player.getPA().showInterface(6960);
	}

	private static PuzzleBoxes getPuzzleBoxByItemId(int itemId) {
		for (PuzzleBoxes box : PuzzleBoxes.values()) {
			if (box.getItemId() == itemId) {
				return box;
			}
		}
		return null;
	}

	private static Clues getClueByItemId(int itemId) {
		for (Clues clue : Clues.values()) {
			if (clue.getItemId() == itemId) {
				return clue;
			}
		}
		return null;
	}

	public static Level getLevelByInt(int i) {
		for (Level level : Level.values()) {
			if (i == level.getLevel()) {
				return level;
			}
		}
		return null;
	}

	private static int maxSolutionsPerLevel(Level level) {
		if (level == Level.EASY) {
			return 3;
		}

		if (level == Level.MEDIUM) {
			return 5;
		}

		if (level == Level.HARD) {
			return 8;
		}
		return -1;
	}

	private static Item[] getItemRewardsForLevel(Level level) {

		List<Item> items = new ArrayList<>();

		// third age extra rare
		if (level == Level.HARD && Misc.random(100) <= 1) {
			items.add(CollectionUtil.getRandomElement(Rewards.THIRD_AGE.getItems()));
		}

		for (Rewards rewards : Rewards.values()) {
			if (rewards.getLevel() == level) {
				items.addAll(Arrays.asList(rewards.getItems()));
			}
		}

		if (items.size() == 0) {
			return null;
		}

		Item[] arr = new Item[items.size()];
		items.toArray(arr);

		return arr;
	}

	private static List<Clues> getCluesByLevel(Level level) {
		List<Clues> clues = new ArrayList<>();

		for (Clues clue : Clues.values()) {
			if (clue.getLevel() == level) {
				clues.add(clue);
			}
		}

		return clues;
	}

	private static void writeLog(Player player, List<Item> items) {
		try (FileWriter writer = new FileWriter("./data/logs/treasuretrails.txt", true)) {
			writer.write("[" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + "] " + player.username + " received "
					+ Joiner.on(", ").join(items) + "\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}