package com.zionscape.server.model.content.treasuretrails;

public enum PuzzleBoxes {

	BOX1(2795, Level.HARD, 0), BOX2(2798, Level.HARD, 1), BOX3(2800, Level.HARD, 2), BOX4(3565, Level.HARD, 3), BOX5(
			3567, Level.HARD, 4), BOX6(3569, Level.HARD, 5), BOX7(3571, Level.HARD, 6);

	private final int itemId;
	private final Level level;
	private final int puzzleId;

	PuzzleBoxes(int itemId, Level level, int puzzleId) {
		this.itemId = itemId;
		this.level = level;
		this.puzzleId = puzzleId;
	}

	public int getItemId() {
		return itemId;
	}

	public Level getLevel() {
		return level;
	}

	public int getPuzzleId() {
		return puzzleId;
	}

}
