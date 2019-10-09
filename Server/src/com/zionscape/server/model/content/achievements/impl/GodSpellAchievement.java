package com.zionscape.server.model.content.achievements.impl;

import com.zionscape.server.model.content.achievements.Achievement;
import com.zionscape.server.model.content.achievements.AchievementProgress;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.content.minigames.quests.QuestInterfaceGenerator;
import com.zionscape.server.model.players.Player;

import java.util.ArrayList;
import java.util.List;

public class GodSpellAchievement extends Achievement {

	private static final int[] SPELL_IDS = {1190, 1191, 1192};

	public GodSpellAchievement(Difficulty difficulty, Achievements.Types type, String name, int interfaceId, int actionButtonId, int pointsReward) {
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
		return progress.getList() != null && progress.getList().size() == SPELL_IDS.length;
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
		qig.add("Saradomin strike", progress == null || !progress.contains(1190) ? false : true);
		qig.add("Claws of Guthix", progress == null || !progress.contains(1191) ? false : true);
		qig.add("Flames of Zamorak", progress == null || !progress.contains(1192) ? false : true);

		qig.writeQuest(player);
	}

}