package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class Master implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.rights >= 2) {
			int i;
			for (i = 0; i < client.level.length; i++) {
				client.level[i] = 99;
				client.xp[i] = 14_000_000;
				client.getPA().refreshSkill(i);
			}
		}
	}

	@Override
	public String getCommandString() {
		return "master";
	}

}
