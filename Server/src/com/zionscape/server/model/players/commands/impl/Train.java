package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class Train implements Command {

	@Override
	public void execute(Player client, String message) {
		client.getPA().spellTeleport(2781, 10071, 0);
	}

	@Override
	public String getCommandString() {
		return "train";
	}

}
