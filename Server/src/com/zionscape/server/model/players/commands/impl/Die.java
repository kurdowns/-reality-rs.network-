package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class Die implements Command {

	@Override
	public void execute(Player client, String message) {
		if(client.rights >= 2) {
			client.isDead = true;
		}
	}

	@Override
	public String getCommandString() {
		return "die";
	}

}
