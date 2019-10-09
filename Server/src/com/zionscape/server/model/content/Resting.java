package com.zionscape.server.model.content;

import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.players.Player;

public class Resting {

	static final int CYCLES_PER_INCREASE = 3, REST_ANIMATION = 11786,
			STAND_UP_ANIMATION = 11788, ENERGY_INCREASE = 4;

	public static void start(Player player) {


		if (player.getDuel() != null) {
			return;
		}

		player.startAnimation(REST_ANIMATION);
		player.stopMovement();
		player.setAttribute("resting", true);

		GameCycleTaskHandler.addEvent(player, (GameCycleTaskContainer container) -> {
			if (!player.attributeExists("resting")) {
				container.stop();
				return;
			}
			if (player.getDuel() != null) {
				container.stop();
				return;
			}

			if (player.getData().energy < 100) {
				player.getData().energy += 3;
				if (player.getData().energy > 100) {
					player.getData().energy = 100;
				}
				player.getPA().sendEnergy();
			}
		}, 3);
	}

	public static void stop(Player player) {
		if (!player.attributeExists("resting")) {
			return;
		}
		player.startAnimation(STAND_UP_ANIMATION);
		player.stopMovement();
		player.removeAttribute("resting");
	}

}