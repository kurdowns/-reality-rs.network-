package com.zionscape.server.model.players.skills.agility;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zionscape.server.Server;
import com.zionscape.server.gamecycle.GameCycleTask;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.Direction;
import com.zionscape.server.model.content.GreeGree;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerAssistant;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.skills.SkillUtils;
import com.zionscape.server.tick.Tick;
import com.zionscape.server.util.Misc;

/**
 * @author Scott Perretta
 */
public class AgilityHandler {

	public static List<Obstacle> obstacles;

	public static void loadObstacles() throws JsonSyntaxException, IOException {
		obstacles = Misc.getGson().fromJson(FileUtils.readFileToString(new File("./config/agility/obstacles.json")),
				new TypeToken<List<Obstacle>>() {
				}.getType());
	}

	private static int h;

	public static void completeObstacle(Player player, int objectId) {
		Obstacle obstacle = obstacles.stream().filter(x -> x.getObjectId() == objectId).findFirst().orElse(null);
		if (obstacle == null) {
			return;
		}
		if (player.level[PlayerConstants.AGILITY] < obstacle.getCourse().getLevelReq()) {
			player.sendMessage("You need an agility level of at least " + obstacle.getCourse().getLevelReq()
					+ " to complete this obstacle.");
			return;
		}
		if (obstacle.getCourse().equals(Course.APE_ATOLL)) {
			if (player.equipment[player.playerWeapon] < 4024 || player.equipment[player.playerWeapon] > 4031) {
				player.sendMessage("You must be in monkey form to complete this course.");
				return;
			}
		}
		if (player.onObstacle) {
			player.sendMessage("You can't do anything whilst completing an obstacle.");
			return;
		}
		if (obstacle.getCourse().compareTo(Course.APE_ATOLL) == 0) {
			if (!GreeGree.isGreeGree(player)) {
				player.sendMessage("You must be in monkey form to complete this obstacle!");
				return;
			}
		}

		for (int i = 0; i < obstacle.getStartingCoordinate().length; i++) {// TODO when theres enough time to do this in
																			// a better fashion
			h = i;
			int x = obstacle.getStartingCoordinate()[i][0], y = obstacle.getStartingCoordinate()[i][1],
					z = obstacle.getStartingCoordinate()[i][2];
			if (objectId == 1948) {
				if (player.objectX == 2536) {
					player.getPA().playerWalk(2535, 3553);
					obstacle.getEndingCoordinate()[i][0] = 2537;
				} else if (player.objectX == 2539) {
					player.getPA().playerWalk(2538, 3553);
					obstacle.getEndingCoordinate()[i][0] = 2540;
				} else if (player.objectX == 2542) {
					player.getPA().playerWalk(2541, 3553);
					obstacle.getEndingCoordinate()[i][0] = 2543;
				}
			} else if (objectId == 20210) {
				if (player.absY > 3559) {
					player.getPA().playerWalk(obstacle.getStartingCoordinate()[1][0],
							obstacle.getStartingCoordinate()[1][1]);
					obstacle.getEndingCoordinate()[1][1] = obstacle.getStartingCoordinate()[0][1];
				} else {
					player.getPA().playerWalk(obstacle.getStartingCoordinate()[0][0],
							obstacle.getStartingCoordinate()[0][1]);
					obstacle.getEndingCoordinate()[1][1] = obstacle.getStartingCoordinate()[1][1];
				}
			} else
				player.getPA().playerWalk(x, y);

			GameCycleTaskHandler.addEvent(player, container -> {
				if (!player.onObstacle)
					container.setStopUponWalking(true);
				if (((player.absX == x) && (player.absY == y) && (player.heightLevel == z)) || player.onObstacle)
					container.stop();

			}, 1);

			GameCycleTaskHandler.addEvent(player, container -> {
				if (!player.onObstacle)
					container.setStopUponWalking(true);
				if ((player.absX == x) && (player.absY == y) && (player.heightLevel == z)) {
					player.resetWalkingQueue();
					doAction(player, obstacle, h);
					container.stop();
				}

			}, 3);
			// GameCycleTaskHandler.stopEvents(AgilityHandler.class);
		}
	}

	private static void doAction(Player player, Obstacle obstacle, int i) {
		if (player.onObstacle)
			return;
		player.onObstacle = true;
		if (i >= obstacle.getEndingCoordinate().length) {
			i = 0;
		}
		switch (obstacle.getType()) {
		case LOG:
		case PIPE:
		case BALANCING_LEDGE:
		case OVER_OBJECT:
		case STONES:
		case MONKEYBARS:
			walkToObstacle(player, obstacle, i);
			break;
		case ROPE:
			ropeObstacle(player, obstacle, i);
			break;
		case CLIMBING:
			climbingObstacle(player, obstacle, i);
			break;
		}
	}

	private static void handleExperience(Player player, Obstacle obstacle) {
		if (obstacle.getPosition()[0] != -1) {
			if (!player.obstaclesCompleted.get(obstacle.getCourse().getCourseIndex())
					.get(getCurrentPosition(player, obstacle))) {
				player.getPA().addSkillXP(obstacle.getXP(), PlayerConstants.AGILITY);
				player.obstaclesCompleted.get(obstacle.getCourse().getCourseIndex())
						.add(getCurrentPosition(player, obstacle), true);
			} else {
				player.getPA().addSkillXP((obstacle.getPosition().length - 1 > 1) ? obstacle.getXP() : 0,
						PlayerConstants.AGILITY);
			}
			if (player.obstaclesCompleted.get(obstacle.getCourse().getCourseIndex())
					.get(obstacle.getCourse().getNumberOfObstacles())) {
				completeCourse(player, obstacle);
			}
		}
	}

	private static int getCurrentPosition(Player player, Obstacle obstacle) {
		for (int i = 0; i < player.obstaclesCompleted.get(obstacle.getCourse().getCourseIndex()).size(); i++) {
			if (i == obstacle.getPosition().length - 1) {
				return obstacle.getPosition()[i];
			}
			if (!player.obstaclesCompleted.get(obstacle.getCourse().getCourseIndex()).get(obstacle.getPosition()[i])) {
				return obstacle.getPosition()[i];
			}
		}
		return obstacle.getPosition()[0];
	}

	private static void resetCourse(Player player, Course course) {
		player.obstaclesCompleted.get(course.getCourseIndex()).clear();
		for (int j = 0; j < Course.valueOf(course.toString()).getNumberOfObstacles(); j++) {
			player.obstaclesCompleted.get(course.getCourseIndex()).add(false);
		}
	}

	private static void completeCourse(Player player, Obstacle obstacle) {
		for (int j = 0; j < Course.values()[obstacle.getCourse().getCourseIndex()].getNumberOfObstacles(); j++) {
			if (!player.obstaclesCompleted.get(obstacle.getCourse().getCourseIndex()).get(j)) {
				return;
			}
		}
		switch (obstacle.getCourse()) {
		case GNOME:

			if (SkillUtils.checkSkillRequirement(player, PlayerConstants.AGILITY)
					&& SkillUtils.givePet(player.getActualLevel(PlayerConstants.AGILITY), 1)) {
				player.getItems().addOrBank(25619, 1);
				player.sendMessage("You have been given a Giant squirrel pet.");
			}

			Achievements.progressMade(player, Achievements.Types.COMPLETE_GNOME_COURSE);
			Achievements.progressMade(player, Achievements.Types.COMPLETE_100_GNOME_COURSES);
			break;
		case WILDERNESS:
			if (SkillUtils.checkSkillRequirement(player, PlayerConstants.AGILITY)
					&& SkillUtils.givePet(player.getActualLevel(PlayerConstants.AGILITY), 2)) {
				player.getItems().addOrBank(25619, 1);
				player.sendMessage("You have been given a Giant squirrel pet.");
			}

			Achievements.progressMade(player, Achievements.Types.COMPLETE_WILDY_COURSE_10_TIMES);
			break;

		case BARBARIAN:
			if (SkillUtils.checkSkillRequirement(player, PlayerConstants.AGILITY)
					&& SkillUtils.givePet(player.getActualLevel(PlayerConstants.AGILITY), 3)) {
				player.getItems().addOrBank(25619, 1);
				player.sendMessage("You have been given a Giant squirrel pet.");
			}
			break;

		case APE_ATOLL:
			if (SkillUtils.checkSkillRequirement(player, PlayerConstants.AGILITY)
					&& SkillUtils.givePet(player.getActualLevel(PlayerConstants.AGILITY), 4)) {
				player.getItems().addOrBank(25619, 1);
				player.sendMessage("You have been given a Giant squirrel pet.");
			}
			break;
		}
		player.getPA().addSkillXP(obstacle.getCourse().getCompletionExperience(), PlayerConstants.AGILITY);
		resetCourse(player, obstacle.getCourse());
	}

	/**
	 * Obstacles that involve the walkTo method and changing the walking index
	 * animation.
	 *
	 * @param player
	 *            player
	 * @param obstacle
	 *            obstacle
	 * @param i
	 *            index
	 */
	private static void walkToObstacle(Player player, Obstacle obstacle, final int i) {
		player.getPA().walkTo(obstacle.getEndingCoordinate()[i][0] - player.absX,
				obstacle.getEndingCoordinate()[i][1] - player.absY);
		player.playerWalkIndex = obstacle.getType().getAnim();
		player.blockWalking = true;
		player.getPA().requestUpdates();

		final boolean wasRunning = player.getWalkingQueue().isRunningToggled();
		player.getWalkingQueue().setRunningToggled(false);

		GameCycleTaskHandler.addEvent(player, container -> {
			if (obstacle.getEndingCoordinate()[i][0] == player.absX
					&& obstacle.getEndingCoordinate()[i][1] == player.absY) {
				player.blockWalking = false;
				player.onObstacle = false;
				player.resetWalkingQueue();
				player.playerWalkIndex = player.getWalkingQueue().isRunningToggled() ? 0x338 : 0x333;
				player.getPA().requestUpdates();
				handleExperience(player, obstacle);
				player.sendMessage(obstacle.getType().getMessage());
				if (obstacle.getType().compareTo(ObstacleType.MONKEYBARS) == 0) {
					player.getPA().movePlayer(player.absX, player.absY, 0);
				}

				player.getWalkingQueue().setRunningToggled(wasRunning);

				container.stop();
			}
		}, 1);
	}

	/**
	 * Obstacles that change your height level.
	 *
	 * @param player
	 *            player
	 * @param obstacle
	 *            obstacle
	 * @param i
	 *            index
	 */
	private static void climbingObstacle(Player player, Obstacle obstacle, final int i) {
		player.blockWalking = true;
		player.startAnimation(
				(player.heightLevel > obstacle.getEndingCoordinate()[i][2]) ? obstacle.getType().getAnim() - 1
						: obstacle.getType().getAnim());
		final boolean wasRunning = player.getWalkingQueue().isRunningToggled();
		player.getWalkingQueue().setRunningToggled(false);

		GameCycleTaskHandler.addEvent(player, container -> {
			player.blockWalking = false;
			player.onObstacle = false;
			player.getPA().movePlayer(obstacle.getEndingCoordinate()[i][0], obstacle.getEndingCoordinate()[i][1],
					obstacle.getEndingCoordinate()[i][2]);
			handleExperience(player, obstacle);
			player.sendMessage(obstacle.getType().getMessage());
			player.getWalkingQueue().setRunningToggled(wasRunning);
			container.stop();
		}, 1);
	}

	private static void ropeObstacle(Player player, Obstacle obstacle, final int i) {
		player.blockWalking = true;
		player.startAnimation(obstacle.getType().getAnim());
		final boolean wasRunning = player.getWalkingQueue().isRunningToggled();
		player.getWalkingQueue().setRunningToggled(false);

		GameCycleTaskHandler.addEvent(player, container -> {
			player.blockWalking = false;
			player.onObstacle = false;
			player.getPA().movePlayer(obstacle.getEndingCoordinate()[i][0], obstacle.getEndingCoordinate()[i][1],
					obstacle.getEndingCoordinate()[i][2]);
			handleExperience(player, obstacle);
			player.sendMessage(obstacle.getType().getMessage());
			player.getWalkingQueue().setRunningToggled(wasRunning);
			container.stop();
		}, 3);
	}

}
