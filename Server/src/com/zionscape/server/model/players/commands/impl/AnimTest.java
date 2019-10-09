package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.gamecycle.GameCycleTask;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class AnimTest implements Command {

	@Override
	public void execute(Player client, String message) {

		if (client.rights < 3) {
			return;
		}

		final int start = Integer.parseInt(message);

		GameCycleTaskHandler.addEvent(client, new GameCycleTask() {

			int anim = start;

			@Override
			public void execute(GameCycleTaskContainer container) {
				client.startAnimation(anim);
				client.sendMessage("anim: " + anim);
				anim++;
			}
		}, 5);

	}

	@Override
	public String getCommandString() {
		return "animtest";
	}

}
