package com.zionscape.server.model.content.achievements.impl;

import com.zionscape.server.model.content.achievements.Achievement;
import com.zionscape.server.model.content.achievements.AchievementProgress;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.players.Player;

public class SteppedAchievement extends Achievement {

	private final int progressRequired;

	public SteppedAchievement(Difficulty difficulty, Achievements.Types type, String name, int interfaceId, int actionButtonId, int pointsReward, int progressRequired) {
		super(type, name, interfaceId, actionButtonId, pointsReward, difficulty);
		this.progressRequired = progressRequired;
	}

	@Override
	public AchievementProgress getDefaultProgressValue() {
		return new AchievementProgress(0);
	}

	@Override
	public boolean isComplete(AchievementProgress progress) {
		return progress.getInteger() >= progressRequired;
	}

	@Override
	@SuppressWarnings("unused")
	public AchievementProgress getNewProgressObject(AchievementProgress currentProgress, Object object) {
		return new AchievementProgress(currentProgress.getInteger() + 1);
	}

	public int getProgressRequired() {
		return progressRequired;
	}

	@Override
	public void display(Player player) {
		int progress = 0;
		AchievementProgress o = player.getData().getAchievementProgress().get(getType());
		if (o != null) {
			progress = o.getInteger();
		}
		if (progress >= getProgressRequired()) {
			player.getPA().sendStatement("You have completed " + getName().toLowerCase() + ".");
		} else if (progress == getProgressRequired()) {
			player.getPA().sendStatement("You have not completed " + getName().toLowerCase() + ".");
		} else {
			player.getPA().sendStatement(getName() + " progress: " + progress + "/" + getProgressRequired() + ".");
		}
	}

}