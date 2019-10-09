package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.Config;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class ToggleAntibot implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.rights == 0) {
			return;
		}

		Config.antibot = !Config.antibot;

		client.sendMessage("Antibot: " + Config.antibot);
	}

	@Override
	public String getCommandString() {
		return "antibot";
	}

}
