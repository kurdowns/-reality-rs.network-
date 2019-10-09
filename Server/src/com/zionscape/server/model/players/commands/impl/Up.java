package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class Up implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.rights < 2) {
			return;
		}

		client.getPA().movePlayer(client.getAbsX(), client.getAbsY(), client.heightLevel + 1);
	}

	@Override
	public String getCommandString() {
		return "up";
	}

}
