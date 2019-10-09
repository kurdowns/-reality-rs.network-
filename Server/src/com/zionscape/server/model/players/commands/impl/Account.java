package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class Account implements Command {

	@Override
	public void execute(Player client, String message) {
		client.getPA().sendFrame126("url: www.Draynor.org/account", 12000);
	}

	@Override
	public String getCommandString() {
		return "account";
	}

}
