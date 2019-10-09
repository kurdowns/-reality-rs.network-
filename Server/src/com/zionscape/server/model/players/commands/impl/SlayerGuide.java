package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class SlayerGuide implements Command {

	@Override
	public void execute(Player client, String message) {
		// client.getPA().sendFrame126("www.forums.Draynor.org", 12000);
		client.getPA().sendFrame126("url: forums.Draynor.org", 12000);

	}

	@Override
	public String getCommandString() {
		return "slayerguide";
	}

}
