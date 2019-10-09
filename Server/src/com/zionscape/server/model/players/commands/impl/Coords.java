package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class Coords implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.rights >= 2)
			client.sendMessage("Your current location is: [" + client.absX + ", " + client.absY + ", "
					+ client.heightLevel + "].");
	}

	@Override
	public String getCommandString() {
		return "coords";
	}

}
