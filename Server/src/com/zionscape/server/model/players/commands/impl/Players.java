package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.commands.Command;

public class Players implements Command {

	@Override
	public void execute(Player client, String message) {
		// if(client.rights >= 0) {
		client.sendMessage("There are currently " + PlayerHandler.playerCountWithModifier() + " players online Draynor.org.");
		// }
	}

	@Override
	public String getCommandString() {
		return "players";
	}
}
