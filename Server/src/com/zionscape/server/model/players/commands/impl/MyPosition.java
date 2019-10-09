package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class MyPosition implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.rights > 2) {
			client.sendMessage("X: " + client.absX + " Y: " + client.absY + " Z: " + client.heightLevel);
		}
	}

	@Override
	public String getCommandString() {
		return "pos";
	}

}
