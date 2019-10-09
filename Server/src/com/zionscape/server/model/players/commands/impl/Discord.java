package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class Discord implements Command {

	@Override
	public void execute(Player client, String message) {
		client.getPA().sendFrame126("url: discordapp.com/channels/237708195567632384/237708195567632384", 12000);
	}

	@Override
	public String getCommandString() {
		return "discord";
	}

}
