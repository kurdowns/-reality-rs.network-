package com.zionscape.server.model.players.skills.slayer;

import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.util.Misc;
import com.zionscape.server.world.shops.Shops;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Stuart
 */
public final class Slayer {

	static {
		for (Tasks task : TaskLevels.ELITE.getTasks()) {
			task.setElite(true);
		}
	}

	public static boolean onClickingButtons(Player player, int button) {
		switch (button) {
		case 77053: // purchase cancel
			if (player.getData().getSlayerTask() == null) {
				player.sendMessage("You do not currently have a slayer task.");
				break;
			}
			if (player.getData().getSlayerPoints() < 30) {
				player.sendMessage("You do not have enough Slayer points to do this.");
				break;
			}
			if (player.getData().getSlayerTask().getPartner() != null) {
				Player partner = PlayerHandler.getPlayer(player.getData().getSlayerTask().getPartner());

				if (partner != null) {
					partner.getData().setSlayerTask(null);
					partner.getData().setPreviousSlayerTask(player.getData().getSlayerTask());

					partner.sendMessage("Your duo slayer task has been canceled by your partner.");
				}
			}
			player.getData().setSlayerPoints(player.getData().getSlayerPoints() - 30);
			player.getData().setPreviousSlayerTask(player.getData().getSlayerTask());
			player.getData().setLastCancelledSlayerTask(player.getData().getSlayerTask().getTask());
			player.getData().setSlayerTask(null);
			player.getPA().sendFrame126("Cancel current task: No Task", 19780);
			player.getPA().sendFrame126("Never assign task again: No Task", 19781);
			player.getPA().sendFrame126(
					"Reassign previous task: " + player.getData().getPreviousSlayerTask().getTask().getName(), 19790);
			player.getPA().sendFrame126("Slayer Points: " + player.getData().getSlayerPoints(), 19795);
			player.sendMessage("You have canceled your slayer task.");
			break;
		case 77056: // purchase never assign
			if (player.getData().getSlayerTask() == null) {
				player.sendMessage("You do not currently have a slayer task.");
				break;
			}
			if (player.getData().getSlayerPoints() < 100) {
				player.sendMessage("You do not have enough Slayer points to do this.");
				break;
			}
			if (player.getData().getRemovedSlayerTasks().size() > 6) {
				player.sendMessage("Your current never assign list is full. Please remove a task.");
				break;
			}

			int index = ((button - 77084) / 3);
			if (index > player.getData().getRemovedSlayerTasks().size() - 1) {
				player.sendMessage("There is no task to remove from here.");
				break;
			}

			player.sendMessage(player.getData().getSlayerTask().getTask().getName() + " will not be assigned again.");
			player.getData().getRemovedSlayerTasks().add(player.getData().getSlayerTask().getTask());

			for (int i = 0; i < 6; i++) {
				if (player.getData().getRemovedSlayerTasks().size() - 1 >= i) {
					player.getPA().sendFrame126(player.getData().getRemovedSlayerTasks().get(i).getName(), 19784 + i);
				} else {
					player.getPA().sendFrame126("None", 19784 + i);
				}
			}

			player.getData().setSlayerPoints(player.getData().getSlayerPoints() - 100);
			player.getPA().sendFrame126("Slayer Points: " + player.getData().getSlayerPoints(), 19795);

			// cancel the current task
			if (player.getData().getSlayerTask().getPartner() != null) {
				Player partner = PlayerHandler.getPlayer(player.getData().getSlayerTask().getPartner());

				if (partner != null) {
					partner.getData().setSlayerTask(null);
					partner.getData().setPreviousSlayerTask(player.getData().getSlayerTask());

					partner.sendMessage("Your duo slayer task has been canceled by your partner.");
				}
			}

			if (player.getData().getPreviousSlayerTask() != null && player.getData().getPreviousSlayerTask().getTask()
					.getName().equalsIgnoreCase(player.getData().getSlayerTask().getTask().getName())) {
				player.getData().setPreviousSlayerTask(null);
				player.getPA().sendFrame126("Reassign previous task: No Task", 19790);
			}

			player.getData().setSlayerTask(null);
			player.getPA().sendFrame126("Cancel current task: No Task", 19780);
			player.getPA().sendFrame126("Never assign task again: No Task", 19781);
			break;
		case 77080: // purchase previous task
			if (player.getData().getSlayerTask() != null) {
				player.sendMessage("You already have a slayer task. Please cancel it first.");
				break;
			}
			if (player.getData().getPreviousSlayerTask() == null) {
				player.sendMessage("You currently do not have a previous slayer task to reassign.");
				break;
			}
			if (player.getData().getSlayerPoints() < 30) {
				player.sendMessage("You do not have enough Slayer points to do this.");
				break;
			}
			if ((player.getData().getPreviousSlayerTask().getLevel().equals(TaskLevels.ELITE))) {
				player.sendMessage("You cannot re-assign elite tasks.");
				break;
			}

			Slayer.giveTask(player, TaskLevels.HARD, player.attributeExists("duo_slayer"), true);

			player.getData().setSlayerPoints(player.getData().getSlayerPoints() - 30);
			player.getPA().sendFrame126("Slayer Points: " + player.getData().getSlayerPoints(), 19795);
			player.getPA().sendFrame126("Cancel current task: " + player.getData().getSlayerTask().getTask().getName(),
					19780);
			player.getPA().sendFrame126(
					"Never assign task again: " + player.getData().getSlayerTask().getTask().getName(), 19781);
			break;
		case 77084: // Remove never assign
		case 77087:
		case 77090:
		case 77093:
		case 77096:
		case 77099:
			index = ((button - 77084) / 3);
			if (index > player.getData().getRemovedSlayerTasks().size() - 1) {
				player.sendMessage("There is no task to remove from here.");
				break;
			}
			player.getData().getRemovedSlayerTasks().remove(index);
			for (int i = 0; i < 6; i++) {
				if (player.getData().getRemovedSlayerTasks().size() - 1 >= i) {
					player.getPA().sendFrame126(player.getData().getRemovedSlayerTasks().get(i).getName(), 19784 + i);
				} else {
					player.getPA().sendFrame126("None", 19784 + i);
				}
			}
			break;
		default:
			return false;
		}

		return true;
	}

	public static boolean onNpcClick(Player player, NPC npc, int option) {
		if (npc.type != 9085) {
			return false;
		}
		switch (option) {
		case 1:
			player.getPA().sendNpcChat(9085, "'Ello. I am a Slayer master.");
			player.setDialogueOwner(Slayer.class);
			player.setCurrentDialogueId(1);
			break;
		case 5:
			player.setDialogueOwner(Slayer.class);
			player.setCurrentDialogueId(3);

			Slayer.onDialogueContinue(player);
			break;
		case 2:
			Shops.open(player, 126);
			break;
		case 3:
			Shops.open(player, 127);
			player.sendMessage("You currently have " + player.getData().getSlayerPoints() + " Slayer points to spend.");
			break;
		case 4:
			Slayer.openEditAssignments(player);
			// cancel interface
			break;
		default:
			return false;
		}
		return true;
	}

	public static boolean onDialogueContinue(Player player) {
		if (player.getDialogueOwner() == null || !player.getDialogueOwner().equals(Slayer.class)) {
			return false;
		}

		switch (player.getCurrentDialogueId()) {
		case 1:
			player.getPA().sendOptions("I need an assignment.", "Do you have anything to trade?",
					"I would like to view the rewards.", "Er. nothing");
			player.setCurrentDialogueId(2);
			break;
		case 3:
			if (player.getData().getSlayerTask() != null) {
				player.getPA().sendNpcChat(9085,
						"You're still hunting " + player.getData().getSlayerTask().getTask().getName()
								+ ". Come back when you've finished your task.");
				player.setCurrentDialogueId(4);
			} else {
				player.removeAttribute("duo_slayer");
				player.setCurrentDialogueId(9);
				player.getPA().sendOptions("Normal Task", "Duo Task");
			}
			break;
		case 4:
			player.setDialogueOwner(null);
			player.getPA().closeAllWindows();
			break;
		case 7:
			player.getPA().sendPlayerChat("Okay, great!");
			player.setCurrentDialogueId(8);
			break;
		case 8:
			player.getPA().sendNpcChat(9085, "Good luck! Don't forget to come back when you need a new assignment.");
			player.setCurrentDialogueId(4);
			break;
		default:
			return true;
		}
		return true;
	}

	public static boolean onDialogueOption(Player player, int option) {
		if (player.getDialogueOwner() == null || !player.getDialogueOwner().equals(Slayer.class)) {
			return false;
		}

		switch (player.getCurrentDialogueId()) {
		case 2:
			switch (option) {
			case 1:
				player.getPA().sendPlayerChat("I need an assignment.");
				player.setCurrentDialogueId(3);
				break;
			case 2:
				player.setDialogueOwner(null);
				Shops.open(player, 126);
				break;
			case 3:
				player.setDialogueOwner(null);
				Shops.open(player, 127);
				player.sendMessage(
						"You currently have " + player.getData().getSlayerPoints() + " Slayer points to spend.");
				break;
			case 4:
				player.setDialogueOwner(null);
				player.getPA().closeAllWindows();
				break;
			}
			break;
		case 5:
			if (!player.attributeExists("duo_slayer") && player.attributeExists("duo_partner")) {
				Player other = PlayerHandler.players[(int) player.getAttribute("duo_partner")];
				if (other != null) {
					other.removeAttribute("duo_partner");
					other.sendMessage("Your Slayer duo with " + player.username
							+ " has been ended as they picked a normal task.");
				}
				player.removeAttribute("duo_partner");
			}
			switch (option) {
			case 1:
				Slayer.giveTask(player, TaskLevels.EASY, player.attributeExists("duo_slayer"), false);
				break;
			case 2:
				Slayer.giveTask(player, TaskLevels.MEDIUM, player.attributeExists("duo_slayer"), false);
				break;
			case 3:
				Slayer.giveTask(player, TaskLevels.HARD, player.attributeExists("duo_slayer"), false);
				break;
			case 4:
				Slayer.giveTask(player, TaskLevels.ELITE, player.attributeExists("duo_slayer"), false);
				break;
			}
			player.setCurrentDialogueId(7);
			break;
		case 9:
			switch (option) {
			case 1:
				player.setCurrentDialogueId(5);
				player.getPA().sendOptions("Easy", "Medium", "Hard", "Elite");
				break;
			case 2:
				if (!player.attributeExists("duo_partner")) {
					player.getPA().sendNpcChat(9085, "You do not have a partner to duo with.");
					player.setCurrentDialogueId(4);
					break;
				}
				player.setCurrentDialogueId(5);
				player.getPA().sendOptions("Easy", "Medium", "Hard");
				player.setAttribute("duo_slayer", true);
				break;
			}
			break;
		case 10:
			if (!player.attributeExists("pending_duo_partner")) {
				player.setDialogueOwner(null);
				player.getPA().closeAllWindows();
				player.sendMessage("The request is no longer valid.");
				break;
			}

			Player other = PlayerHandler.players[(int) player.getAttribute("pending_duo_partner")];

			if (other == null || !other.attributeExists("pending_duo_partner")
					|| (int) other.getAttribute("pending_duo_partner") != player.playerId) {
				player.setDialogueOwner(null);
				player.getPA().closeAllWindows();
				player.sendMessage("The request is no longer valid.");
				break;
			}

			switch (option) {
			case 1:
				player.setAttribute("duo_partner", other.playerId);
				player.removeAttribute("pending_duo_partner");
				player.setDialogueOwner(null);
				player.getPA().closeAllWindows();
				player.sendMessage("You are now doing a Slayer duo with " + other.username + ".");
				player.sendMessage("Now get a Slayer task from Kuradel at Edgeville.");

				other.setAttribute("duo_partner", player.playerId);
				other.removeAttribute("pending_duo_partner");
				other.sendMessage("You are now doing a Slayer duo with " + player.username + ".");
				other.sendMessage("Now get a Slayer task from Kuradel at Edgeville.");
				break;
			case 2:
				player.sendMessage("You have declined " + other.username + "'s request to Duo Slayer.");
				player.removeAttribute("pending_duo_partner");
				player.getPA().closeAllWindows();
				player.setDialogueOwner(null);

				other.sendMessage(player.username + " has declined your request to Duo Slayer.");
				other.removeAttribute("pending_duo_partner");
				break;
			}
			break;
		}

		return true;
	}

	public static void onNpcKilled(Player player, NPC npc) {
		if (player.getData().getSlayerTask() == null) {
			return;
		}

		CurrentTask currentTask = player.getData().getSlayerTask();

		if (!currentTask.getTask().getNpcIds().contains(npc.type)
				&& !currentTask.getTask().getNpcIds().contains(npc.transformId) || currentTask.getAmount() == 0) {
			return;
		}

		currentTask.decreaseAmount(1);

		Player partner = null;
		if (currentTask.getPartner() != null) {
			partner = PlayerHandler.getPlayer(currentTask.getPartner());
			partner.getData().getSlayerTask().decreaseAmount(1);
		}

		// give slayer xp
		player.getPA().addSkillXP((int) Math.floor(currentTask.getTask().getXp() * 4.2), PlayerConstants.SLAYER);
		// partner.getPA().addSkillXP((int) Math.floor(currentTask.getTask().getXp() *
		// 4.2), PlayerConstants.SLAYER);

		if (currentTask.getAmount() != 0) {
			return;
		}

		int slayerPoints = 0;
		if (currentTask.getLevel().equals(TaskLevels.EASY)) {
			slayerPoints = 5;
		} else if (currentTask.getLevel().equals(TaskLevels.MEDIUM)) {
			slayerPoints = 12;
		} else if (currentTask.getLevel().equals(TaskLevels.HARD)) {
			slayerPoints = 18;
		} else if (currentTask.getLevel().equals(TaskLevels.ELITE)) {
			slayerPoints = 25;
		}

		// completing task
		if (partner != null) {

			Achievements.progressMade(player, Achievements.Types.COMPLETE_75_DUO_TASKS);
			Achievements.progressMade(player, Achievements.Types.COMPLETE_150_DUO_SLAYER_TASKS);

			Achievements.progressMade(partner, Achievements.Types.COMPLETE_75_DUO_TASKS);
			Achievements.progressMade(partner, Achievements.Types.COMPLETE_150_DUO_SLAYER_TASKS);

			slayerPoints = slayerPoints / 2;

			player.getData().totalCompletedDuoSlayerTasks++;
			player.getData().setSlayerPoints(player.getData().getSlayerPoints() + slayerPoints);
			player.sendMessage("You've completed your duo slayer task and received " + slayerPoints
					+ " points; return to the Slayer Master.");
			player.getPA().sendFrame126("@or1@[@whi@" + player.getData().getSlayerPoints() + "@or1@] Slayer Points",
					54429);

			partner.getData().totalCompletedDuoSlayerTasks++;
			partner.getData().setSlayerPoints(partner.getData().getSlayerPoints() + slayerPoints);
			partner.sendMessage("You've completed your duo slayer task and received " + slayerPoints
					+ " points; return to the Slayer Master.");
			partner.getPA().sendFrame126("@or1@[@whi@" + partner.getData().getSlayerPoints() + "@or1@] Slayer Points",
					54429);

			player.getData().setSlayerTask(null);
			partner.getData().setSlayerTask(null);
		} else {
			if (player.getData().getPreviousSlayerTask() != null
					&& currentTask.getLevel().equals(player.getData().getPreviousSlayerTask().getLevel())) {
				player.getData().increaseConsecutiveSlayerTasks(1);
			} else {
				player.getData().setConsecutiveSlayerTasks(1);
				player.getData().setPreviousSlayerTask(currentTask);
			}

			int consecutiveTasks = player.getData().getConsecutiveSlayerTasks();

			if (consecutiveTasks % 50 == 0) {
				slayerPoints = slayerPoints + ((400 / 100) * slayerPoints);
			} else if (consecutiveTasks % 10 == 0) {
				slayerPoints = slayerPoints + ((1400 / 100) * slayerPoints);
			}

			if (currentTask.getLevel().equals(TaskLevels.EASY)) {
				Achievements.progressMade(player, Achievements.Types.COMPLETE_10_EASY_SLAYER_TASKS);
			}
			if (currentTask.getLevel().equals(TaskLevels.MEDIUM)) {
				Achievements.progressMade(player, Achievements.Types.COMPLETE_25_MEDIUM_TASKS);
			}
			if (currentTask.getLevel().equals(TaskLevels.HARD)) {
				Achievements.progressMade(player, Achievements.Types.COMPLETE_50_HARD_SLAYER_TASKS);
			}

			player.getData().setSlayerPoints(player.getData().getSlayerPoints() + slayerPoints);
			player.sendMessage("You've completed " + consecutiveTasks + " task" + (consecutiveTasks > 1 ? "s" : "")
					+ " in a row and received " + slayerPoints + " points; return to the Slayer Master.");
			player.getPA().sendFrame126("@or1@[@whi@" + player.getData().getSlayerPoints() + "@or1@] Slayer Points",
					54429);
			player.getData().setSlayerTask(null);
		}
	}

	public static boolean onItemOnPlayer(Player player, Player other, Item item) {
		if (item.getId() != 4155) {
			return false;
		}

		if (player.attributeExists("duo_partner")) {
			player.sendMessage("You are already in a duo.");
			return true;
		}

		if (player.getData().getSlayerTask() != null) {
			player.sendMessage(
					"You have a Slayer assignment assigned and cannot invite " + other.username + " to duo.");
			return true;
		}
		if (other.getData().getSlayerTask() != null) {
			player.sendMessage(other.username + " already has a Slayer assignment.");
			return true;
		}
		if (!player.getLocation().isWithinInteractionDistance(other.getLocation())) {
			player.sendMessage(other.username + " is too far away.");
			return true;
		}
		if (other.isBanking || other.inTrade || other.attributeExists("shop") || player.getDuel() != null
				|| other.attributeExists("pending_duo_partner") || other.attributeExists("duo_partner")
				|| other.playerIndex > 0 || other.npcIndex > 0) {
			player.sendMessage(other.username + " is currently busy.");
			return true;
		}

		other.setCurrentDialogueId(10);
		other.setDialogueOwner(Slayer.class);
		other.setAttribute("pending_duo_partner", player.playerId);
		other.getPA().sendOptions("Yes, I would like to do a Slayer duo with " + player.username + ".", "No, thanks.");

		player.setAttribute("pending_duo_partner", other.playerId);
		player.sendMessage("Sending Slayer duo request to " + other.username + "...");
		return true;
	}

	public static void onLoggedOut(Player player) {
		if (player.getData().getSlayerTask() == null) {
			return;
		}
		if (player.getData().getSlayerTask().getPartner() == null) {
			return;
		}
		Player partner = PlayerHandler.getPlayer(player.getData().getSlayerTask().getPartner());
		if (partner != null) {
			partner.sendMessage("Your duo slayer partner has logged out. You're now on your own.");
			partner.getData().getSlayerTask().setPartner(null);
		}

		player.getData().setSlayerTask(null);
	}

	private static void giveTask(Player player, TaskLevels level, boolean duo, boolean reassign) {
		if (level.equals(TaskLevels.MEDIUM) && player.combatLevel < 52) {
			player.getPA().sendNpcChat(9085, "Medium tasks require combat level 52+!");
			player.setCurrentDialogueId(4);
			return;
		}
		if (level.equals(TaskLevels.HARD) && player.combatLevel < 90) {
			player.getPA().sendNpcChat(9085, "Hard tasks require combat level 90+!");
			player.setCurrentDialogueId(4);
			return;
		}
		if (level.equals(TaskLevels.ELITE) && player.combatLevel < 90) {
			player.getPA().sendNpcChat(9085, "Elite tasks require combat level 90+!");
			player.setCurrentDialogueId(4);
			return;
		}

		if (level.equals(TaskLevels.ELITE) && player.level[PlayerConstants.SLAYER] < 93) {
			player.getPA().sendNpcChat(9085, "Elite tasks require slayer level of 93+!");
			player.setCurrentDialogueId(4);
			return;
		}

		if (level.equals(TaskLevels.ELITE) && duo) {
			player.getPA().sendNpcChat(9085, "Elite tasks cannot be done duo.");
			player.setCurrentDialogueId(4);
			return;
		}

		int taskAmount = Misc.random(20, 60);

		/*
		 * int taskAmount = 50; if (level.equals(TaskLevels.MEDIUM)) { taskAmount = 100;
		 * } else if (level.equals(TaskLevels.HARD)) { taskAmount = 200; }
		 */
		if (duo) {
			taskAmount *= 1.50;
		}

		List<Tasks> tasks = level.getTasks().stream()
				.filter(x -> x.getRequiredSlayer() < player.getPA().getActualLevel(PlayerConstants.SLAYER)
						&& checkTaskRequirement(x, player) && !player.getData().getRemovedSlayerTasks().contains(x))
				.collect(Collectors.toList());

		if (tasks.size() == 0) {
			player.getPA().sendNpcChat(9085, "There is no available tasks for you at this level.");
			player.setCurrentDialogueId(4);
			return;
		}

		Tasks task = reassign ? player.getData().getPreviousSlayerTask().getTask()
				: tasks.get(Misc.random(tasks.size() - 1));

		if (!duo) {

			int amount = Misc.random(20, taskAmount);
			if (level.equals(TaskLevels.ELITE)) {
				amount = Misc.random(15, 35);
			}
			if (player.getData().getLastCancelledSlayerTask() != null) {
				if (task.getName().equalsIgnoreCase(player.getData().getLastCancelledSlayerTask().getName()))
					task = reassign ? player.getData().getPreviousSlayerTask().getTask()
							: tasks.get(Misc.random(tasks.size() - 1));
			}
			player.getData().setSlayerTask(new CurrentTask(amount, task, level));
			if (!reassign) {
				player.getPA().sendNpcChat(9085,
						"Excellent. Your new task is to kill " + player.getData().getSlayerTask().getAmount() + " "
								+ player.getData().getSlayerTask().getTask().getName() + ".");
			} else {
				player.sendMessage("Your new task is to kill " + player.getData().getSlayerTask().getAmount() + " "
						+ player.getData().getSlayerTask().getTask().getName() + ".");
			}
		} else {
			Player other = PlayerHandler.players[(int) player.getAttribute("duo_partner")];

			if (other == null) {
				player.getPA().sendNpcChat(9085, "Looks like your duo partner has left you.");
				player.setCurrentDialogueId(4);
				return;
			}

			int amount = Misc.random(20, taskAmount);

			player.getData().setSlayerTask(new CurrentTask(amount, task, level, other.username));
			other.getData().setSlayerTask(new CurrentTask(amount, task, level, player.username));

			if (!reassign) {
				player.getPA().sendNpcChat(9085,
						"Your duo task is to kill " + player.getData().getSlayerTask().getAmount() + " "
								+ player.getData().getSlayerTask().getTask().getName() + ".");
			} else {
				player.sendMessage("Your new duo slayer task is to kill " + player.getData().getSlayerTask().getAmount()
						+ " " + player.getData().getSlayerTask().getTask().getName() + ".");
			}
			other.getPA().sendNpcChat(9085,
					"Your duo slayer task is to kill " + other.getData().getSlayerTask().getAmount() + " "
							+ other.getData().getSlayerTask().getTask().getName() + ".");

			player.removeAttribute("duo_partner");
			other.removeAttribute("duo_partner");
		}
	}

	private static void openEditAssignments(Player player) {
		if (player.getData().getSlayerTask() != null) {
			player.getPA().sendFrame126("Cancel current task: " + player.getData().getSlayerTask().getTask().getName(),
					19780);
		} else {
			player.getPA().sendFrame126("Cancel current task: No Task", 19780);
		}
		if (player.getData().getSlayerTask() != null) {
			player.getPA().sendFrame126(
					"Never assign task again: " + player.getData().getSlayerTask().getTask().getName(), 19781);
		} else {
			player.getPA().sendFrame126("Never assign task again: No Task", 19781);
		}
		if (player.getData().getPreviousSlayerTask() != null) {
			player.getPA().sendFrame126(
					"Reassign previous task: " + player.getData().getPreviousSlayerTask().getTask().getName(), 19790);
		} else {
			player.getPA().sendFrame126("Reassign previous task: No Task", 19790);
		}

		for (int i = 0; i < 6; i++) {
			if (player.getData().getRemovedSlayerTasks().size() - 1 >= i) {
				player.getPA().sendFrame126(player.getData().getRemovedSlayerTasks().get(i).getName(), 19784 + i);
			} else {
				player.getPA().sendFrame126("None", 19784 + i);
			}
		}

		player.getPA().sendFrame126("Slayer Points: " + player.getData().getSlayerPoints(), 19795);

		player.getPA().showInterface(19748);
	}

	public static boolean isFightingSlayerTask(Player player, NPC npc) {
		return player.getData().getSlayerTask() != null
				&& player.getData().getSlayerTask().getTask().getNpcIds().contains(npc.type);
	}

	public static boolean isFightingSlaverNPC(Player player, NPC npc, Tasks task) {

		if (player.getData().getSlayerTask() == null) {
			return false;
		}

		if (player.getData().getSlayerTask().getTask() != task) {
			return false;
		}

		return task.getNpcIds().contains(npc.type);
	}

	private static boolean checkTaskRequirement(Tasks task, Player player) {
		if (task.equals(Tasks.ABERRANT_SPECTRE) || task.equals(Tasks.BANSHEE) || task.equals(Tasks.COCKATRICE)
				|| task.equals(Tasks.BASILISK)) {
			if (player.level[PlayerConstants.DEFENCE] < 20) {
				return false;
			}
		}
		return true;
	}

	public static void operateMablesRing(Player player) {

		if (System.currentTimeMillis() - player.logoutDelay < 10000) {
			player.sendMessage("You cannot teleport whilst in combat.");
			return;
		}

		if (player.getData().getSlayerTask() == null) {
			player.sendMessage("You do not have a Slayer task assigned.");
			return;
		}

		CurrentTask currentTask = player.getData().getSlayerTask();

		Location teleportLocation = null;

		// 2731 9489 - Bronze dragon
		// 2719 - 9450 - Iron/Steel Dragon
		// 1777 5341 - Mith dragon
		// 3313 5451 - Green Dragons

		// 2898 5204 - nex
		// 2910 5265 - commander zilyana
		// 2925 5337 2- krill
		// 2859 5354 2 - general greador
		// 2839 5290 2 - kreeara

		if (currentTask.getTask().equals(Tasks.BRONZE_DRAGON)) {
			teleportLocation = Location.create(2731, 8489, 0);
		} else if (currentTask.getTask().equals(Tasks.IRON_DRAGON)
				|| currentTask.getTask().equals(Tasks.STEEL_DRAGON)) {
			teleportLocation = Location.create(2719, 9450, 0);
		} else if (currentTask.getTask().equals(Tasks.MITHRIL_DRAGON)) {
			teleportLocation = Location.create(1777, 5341, 0);
		} else if (currentTask.getTask().equals(Tasks.GREEN_DRAGON)) {
			teleportLocation = Location.create(3313, 5451, 0);
		} else if (currentTask.getTask().equals(Tasks.NEX)) {
			teleportLocation = Location.create(2898, 5204, 0);
		} else if (currentTask.getTask().equals(Tasks.CZULYANA)) {
			teleportLocation = Location.create(2910, 5265, 0);
		} else if (currentTask.getTask().equals(Tasks.TSTSAROTH)) {
			teleportLocation = Location.create(2925, 5337, 2);
		} else if (currentTask.getTask().equals(Tasks.BANDOS)) {
			teleportLocation = Location.create(2859, 5354, 2);
		} else if (currentTask.getTask().equals(Tasks.KREE_ARR)) {
			teleportLocation = Location.create(2839, 5290, 2);
		} else {
			for (NPC npc : NPCHandler.npcs) {
				if (npc == null) {
					continue;
				}
				if (npc.type == currentTask.getTask().getNpcIds().get(0)) {
					teleportLocation = Location.create(npc.makeX, npc.makeY, npc.heightLevel);
					break;
				}
			}
		}

		if (teleportLocation == null) {
			player.sendMessage("No teleport location could be found for that task.");
		} else {
			player.getPA().spellTeleport(teleportLocation.getX(), teleportLocation.getY(), teleportLocation.getZ());
		}

	}

}