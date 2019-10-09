package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.Config;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class Skull implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.isSkulled) {
			client.sendMessage("You are already skulled!");
			return;
		}
		client.headIconPk = 0;
		client.skullTimer = Config.SKULL_TIMER;
		client.isSkulled = true;
		client.getPA().requestUpdates();
		client.sendMessage("You are now skulled. Skull timer -> " + (Config.SKULL_TIMER / 2 / 60) + " minutes.");
		return;
	}

	@Override
	public String getCommandString() {
		return "skull";
	}

}
