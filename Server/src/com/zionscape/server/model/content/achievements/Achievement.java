package com.zionscape.server.model.content.achievements;

import com.zionscape.server.model.players.Player;

public abstract class Achievement {

	private final Achievements.Types type;
	private final String name;
	private final int interfaceId;
	private final int actionButtonId;
	private final Difficulty difficulty;

	protected Achievement(Achievements.Types type, String name, int interfaceId, int actionButtonId, int pointsReward, Difficulty difficulty) {
		this.type = type;
		this.name = name;
		this.interfaceId = interfaceId;
		this.actionButtonId = actionButtonId;
		this.difficulty = difficulty;
	}

	public final Achievements.Types getType() {
		return type;
	}

	public final String getName() {
		return name;
	}

	public final int getInterfaceId() {
		return interfaceId;
	}

	public int getActionButtonId() {
		return actionButtonId;
	}

	public abstract AchievementProgress getDefaultProgressValue();

	public abstract AchievementProgress getNewProgressObject(AchievementProgress currentProgress, Object object);

	public abstract boolean isComplete(AchievementProgress progress);

	public abstract void display(Player player);

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public enum Difficulty {
		EASY, MEDIUM, HARD, ELITE;
	}
}