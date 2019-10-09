package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class Invincible implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.rights < 2) {
			return;
		}
		client.invincible = !client.invincible;
		client.sendMessage("You are " + (client.invincible ? "now" : "not") + " invincible.");
	}

	@Override
	public String getCommandString() {
		return "invincible";
	}
}
