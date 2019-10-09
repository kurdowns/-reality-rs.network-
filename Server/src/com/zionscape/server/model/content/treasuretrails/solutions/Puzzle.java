package com.zionscape.server.model.content.treasuretrails.solutions;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.util.CollectionUtil;
import com.zionscape.server.util.Misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Puzzle {

	private static final Random random = new Random();
	private Player player;
	private int[] slots;
	private int puzzle;
	private PuzzleCompletedEvent completedEvent;
	private int itemId;

	/*public Puzzle(Player player, PuzzleCompletedEvent completedEvent) {
		this.player = player;
		this.puzzle = Misc.random(2);
		this.completedEvent = completedEvent;
	}*/

	public Puzzle(Player player, int puzzle, PuzzleCompletedEvent completedEvent, int itemId) {
		this.player = player;
		this.puzzle = puzzle;
		this.completedEvent = completedEvent;
		this.itemId = itemId;
	}

	private static int getStartItemId(int puzzle) {
		switch (puzzle) {
			case 0:
				return 2749;
			case 1:
				return 3619;
			case 2:
				return 3643;
			case 3:
				return 18841;
			case 4:
				return 18865;
			case 5:
				return 18889;
			case 6:
				return 18913;
		}
		return -1;
	}

	public Puzzle openPuzzle() {

		if (slots == null) {
			slots = new int[25];

			int id = getStartItemId(puzzle);
			for (int i = 0; i < 24; i++) {
				slots[i] = id++;
			}
			slots[24] = 0;

			List<Integer> moves = new ArrayList<>();
			for (int i = 0; i < 500; i++) {
				int freeSlotIndex = CollectionUtil.getIndexOfValue(slots, 0);

				/**
				 * Vertical movement
				 */
				// top row we can move down
				if (freeSlotIndex >= 0 && freeSlotIndex <= 4) {
					moves.add(freeSlotIndex + 5);
				}

				// were in the middle we can move up or down
				if (freeSlotIndex >= 5 && freeSlotIndex <= 19) {
					moves.add(freeSlotIndex + 5);
					moves.add(freeSlotIndex - 5);
				}

				// we can only move up
				if (freeSlotIndex >= 20 && freeSlotIndex <= 24) {
					moves.add(freeSlotIndex - 5);
				}

				/**
				 * Horizontal Movement
				 */

				// first column
				if (freeSlotIndex % 5 == 0) {
					moves.add(freeSlotIndex + 1);
				} else if (freeSlotIndex == 4 || freeSlotIndex == 9 || freeSlotIndex == 14 || freeSlotIndex == 19 || freeSlotIndex == 24) {
					moves.add(freeSlotIndex - 1);
				} else {
					moves.add(freeSlotIndex - 1);
					moves.add(freeSlotIndex + 1);
				}

				if (moves.size() > 0) {
					int moveSlot = CollectionUtil.getRandomElement(moves);
					movePiece(moveSlot, slots[moveSlot], true);
				}

				moves.clear();
			}

			sendHint();
		}

		for (int i = 0; i < slots.length; i++) {
			if (this.slots[i] != 0) {
				player.getPA().sendFrame34(slots[i], i, 6980, 1);
			} else {
				player.getPA().sendFrame34(-1, i, 6980, 0);
			}
		}
		player.getPA().sendFrame34(slots[4] == 0 ? -1 : slots[4], 4, 6980, slots[4] == 0 ? 0 : 1);

		player.getPA().showInterface(6976);

		return this;
	}

	public int getFreeSlotIndex() {
		for (int i = 0; i < slots.length; i++) {
			if (slots[i] == 0) {
				return i;
			}
		}
		return -1;
	}

	public void movePiece(int slot, int itemId, boolean shuffle) {

		// check for any cheating
		if (slot < 0 || slot > 25 || slots[slot] != itemId) {
			return;
		}

		int freeSlot = getFreeSlot(slot);

		// Move is blocked
		if (freeSlot == -1) {
			return;
		}

		slots[slot] = 0;
		slots[freeSlot] = itemId;

		if (!shuffle) {
			if (isComplete()) {
				player.getPA().closeAllWindows();

				completedEvent.run();
			}

			player.getPA().sendFrame34(-1, slot, 6980, 0);
			player.getPA().sendFrame34(itemId, freeSlot, 6980, 1);
			player.getPA().sendFrame34(slots[4] == 0 ? -1 : slots[4], 4, 6980, slots[4] == 0 ? 0 : 1);
		}
	}

	public void sendHint() {

		final int item = getStartItemId(puzzle);

		for (int i = 0; i < 25; i++) {
			if (i == 24) {
				player.getPA().sendFrame34(-1, 24, 6985, 0);
			} else {
				player.getPA().sendFrame34(item + i, i, 6985, 1);
			}
		}

		player.getPA().sendFrame34(item + 4, 4, 6985, 1);
	}

	private int getFreeSlot(int slot) {

		// we can move north
		if (slot - 5 >= 0) {
			if (slots[slot - 5] == 0) {
				return slot - 5;
			}
		}

		// Move south
		if (slot + 5 < 25) {
			if (slots[slot + 5] == 0) {
				return slot + 5;
			}
		}

		// Move east
		if (slot != 4 && slot != 9 && slot != 14 && slot != 19 && slot != 24) {
			if (slots[slot + 1] == 0) {
				return slot + 1;
			}
		}

		// Move west
		if (slot % 5 != 0) {
			if (slots[slot - 1] == 0) {
				return slot - 1;
			}
		}

		return -1;
	}

	private boolean isComplete() {

		int item = getStartItemId(puzzle);

		for (int i = 0; i < slots.length - 1; i++) {
			if (slots[i] != item++) {
				return false;
			}
		}

		return true;
	}

	public void completePuzzle() {
		player.getPA().closeAllWindows();
		completedEvent.run();
	}

	public int getPuzzle() {
		return puzzle;
	}

	public int getItemId() {
		return itemId;
	}
}