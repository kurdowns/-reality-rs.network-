package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class Vote implements Command {

	@Override
	public void execute(Player client, String message) {
		client.getPA().sendFrame126("url: www.draynor.org/vote", 12000);
	}

	@Override
	public String getCommandString() {
		return "vote";
	}

}
