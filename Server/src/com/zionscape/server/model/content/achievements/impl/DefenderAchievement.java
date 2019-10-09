package com.zionscape.server.model.content.achievements.impl;

import com.zionscape.server.model.content.achievements.Achievement;
import com.zionscape.server.model.content.achievements.AchievementProgress;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.content.minigames.quests.QuestInterfaceGenerator;
import com.zionscape.server.model.players.Player;

import java.util.ArrayList;
import java.util.List;

public class DefenderAchievement extends Achievement {

	private static final int[] DEFENDER_IDS = {
			8844, 8845, 8846, 8847, 8848, 8849, 8850, 20072
	};
	private static final String[] DEFENDER_NAMES = {
			"Bronze", "Iron", "Steel", "Black", "Mithril", "Adamant", "Rune", "Dragon"
	};

	public DefenderAchievement(Difficulty difficulty, Achievements.Types type, String name, int interfaceId, int actionButtonId, int pointsReward) {
		super(type, name, interfaceId, actionButtonId, pointsReward, difficulty);
	}

	@Override
	public AchievementProgress getDefaultProgressValue() {
		return new AchievementProgress(new ArrayList<>());
	}

	@Override
	public AchievementProgress getNewProgressObject(AchievementProgress currentProgress, Object object) {
		if (!currentProgress.getList().contains(object)) {
			currentProgress.getList().add((Integer) object);
		}
		return currentProgress;
	}

	@Override
	public boolean isComplete(AchievementProgress progress) {
		return progress.getList() != null && progress.getList().size() == DEFENDER_IDS.length;
	}

	@Override
	public void display(Player player) {

		List<Integer> progress = null;
		if (player.getData().getAchievementProgress().containsKey(getType())) {
			progress = player.getData().getAchievementProgress().get(getType()).getList();
		}

		QuestInterfaceGenerator qig = new QuestInterfaceGenerator(getName());

		qig.add("Progress");
		qig.lineBreak();

		for (int i = 0; i < DEFENDER_IDS.length; i++) {
			qig.add(DEFENDER_NAMES[i] + " defender", progress == null || !progress.contains(DEFENDER_IDS[i]) ? false : true);
		}

		qig.writeQuest(player);
	}

}