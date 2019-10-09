package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class CompletePuzzle implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.rights < 3 || client.puzzle == null) {
			return;
		}
		client.puzzle.completePuzzle();
	}

	@Override
	public String getCommandString() {
		return "completepuzzle";
	}
}
