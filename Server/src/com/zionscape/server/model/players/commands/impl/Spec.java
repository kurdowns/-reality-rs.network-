package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class Spec implements Command {

	@Override
	public void execute(Player client, String message) {

		if (client.rights != 3) {
			return;
		}

		client.specAmount = 1000;
		client.getItems().updateSpecialBar();

	}

	@Override
	public String getCommandString() {
		return "spec";
	}

}
