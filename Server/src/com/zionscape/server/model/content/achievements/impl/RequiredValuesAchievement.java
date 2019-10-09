package com.zionscape.server.model.content.achievements.impl;

import com.zionscape.server.model.content.achievements.Achievement;
import com.zionscape.server.model.content.achievements.AchievementProgress;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.players.Player;

import java.util.ArrayList;
import java.util.List;

public class RequiredValuesAchievement extends Achievement {

	private List<Integer> requiredValues = new ArrayList<>();

	public RequiredValuesAchievement(Difficulty difficulty, Achievements.Types type, String name, int interfaceId, int actionButtonId, int pointsReward, int... requiredValues) {
		super(type, name, interfaceId, actionButtonId, pointsReward, difficulty);
		for (int i : requiredValues) {
			this.requiredValues.add(i);
		}
	}

	@Override
	public AchievementProgress getDefaultProgressValue() {
		return new AchievementProgress(new ArrayList<>());
	}

	@Override
	public AchievementProgress getNewProgressObject(AchievementProgress currentProgress, Object object) {

		if (!requiredValues.contains(object)) {
			return currentProgress;
		}

		if (!currentProgress.getList().contains(object)) {
			currentProgress.getList().add((Integer) object);
		}
		return currentProgress;
	}

	@Override
	public boolean isComplete(AchievementProgress progress) {
		return progress.getList() != null && progress.getList().size() == requiredValues.size();
	}

	@Override
	public void display(Player player) {
		if (isComplete(player.getData().getAchievementProgress().get(getType()))) {
			player.getPA().sendStatement("You have completed " + getName().toLowerCase() + ".");
		} else {
			player.getPA().sendStatement("You have not completed " + getName().toLowerCase() + ".");
		}
	}

}