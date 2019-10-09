package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class SpawnObject implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.rights < 3) {
			return;
		}
		String[] args = message.split(" ");
		client.getPA().object(Integer.parseInt(args[0]), client.absX, client.absY, 0, 10);

	}

	@Override
	public String getCommandString() {
		return "object";
	}

}
