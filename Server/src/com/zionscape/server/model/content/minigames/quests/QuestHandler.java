package com.zionscape.server.model.content.minigames.quests;

import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.players.Player;

public class QuestHandler {

	public static final int MAX_QUEST_POINTS = 3;

	public static boolean onClickingButtons(Player player, int button) {
		switch (button) {
			case 74217:
				TheStolenCannon.sendQuestInformation(player);
				return true;
		}
		return false;
	}

	public static boolean onNpcClick(Player player, NPC npc, int option) {
		if (TheStolenCannon.OnNpcClick(player, npc, option)) {
			return true;
		}
		return false;
	}

	public static boolean onDialogueOption(Player player, int option) {
		if (TheStolenCannon.onDialogueOption(player, option)) {
			return true;
		}
		return false;
	}

	public static boolean onDialogueContinue(Player player) {
		if (TheStolenCannon.onDialogueContinue(player)) {
			return true;
		}
		return false;
	}

	public static void onNpcKilled(Player player, NPC npc) {
		TheStolenCannon.onNpcKilled(player, npc);
	}

	public static void onPlayerLoggedIn(Player player) {
		TheStolenCannon.onPlayerLoggedIn(player);

		sendQuestPoints(player);
		sendQuests(player);
	}

	public static void sendQuests(Player player) {
		String color = "@red@";
		if (player.theStolenCannonStatus == TheStolenCannon.COMPLETED_STAGE) {
			color = "@gre@";
		} else if (player.theStolenCannonStatus > 0) {
			color = "@yel@";
		}
		player.getPA().sendFrame126(color + "The Stolen Cannon", 19161);
	}

	public static void sendQuestRewardInterface(Player player, String quest, String... line) {
		if (line.length > 6) {
			return;
		}
		player.getPA().showInterface(12140);
		player.getPA().sendFrame126("You have completed " + quest + ".", 12144);
		player.getPA().sendFrame126(player.questPoints + "", 12147);
		for (int i = 0; i < 6; i++) {
			player.getPA().sendFrame126(i > line.length - 1 ? "" : line[i], 12150 + i);
		}
	}

	public static void sendQuestPoints(Player player) {
		player.getPA().sendFrame126("Quest Points: " + player.questPoints, 19159);
	}

}