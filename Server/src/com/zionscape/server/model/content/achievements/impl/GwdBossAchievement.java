package com.zionscape.server.model.content.achievements.impl;

import com.zionscape.server.model.content.achievements.Achievement;
import com.zionscape.server.model.content.achievements.AchievementProgress;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.content.minigames.quests.QuestInterfaceGenerator;
import com.zionscape.server.model.players.Player;

import java.util.ArrayList;
import java.util.List;

public class GwdBossAchievement extends Achievement {

	private static final int[] IDS = {
			6260, 6222, 6203, 6247, 13447
	};
	private static final String[] NAMES = {
			"General Graardor", "Kree'arra", "K'ril Tsutsaroth", "Commander Zilyana", "Nex"
	};

	public GwdBossAchievement(Difficulty difficulty, Achievements.Types type, String name, int interfaceId, int actionButtonId, int pointsReward) {
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
		return progress.getList() != null && progress.getList().size() == IDS.length;
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

		for (int i = 0; i < IDS.length; i++) {
			qig.add(NAMES[i], progress == null || !progress.contains(IDS[i]) ? false : true);
		}

		qig.writeQuest(player);
	}

}