package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.Server;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class Updates implements Command {

	@Override
	public void execute(Player client, String message) {
		client.getPA().sendFrame126("url: " + Server.getSetting("update_url"), 12000);
	}

	@Override
	public String getCommandString() {
		return "updates";
	}

}
