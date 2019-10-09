package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class ClanMessage implements Command {

	@Override
	public void execute(Player client, String message) {

		if (client.muted) {
			return;
		}

		if (message.startsWith("clan")) {
			message = message.substring(4);
			message = message.trim();
		}

		if (message.length() == 0) {
			return;
		}

		if (client.clan != null) {
			client.clan.sendChat(client, message);
		}
	}

	@Override
	public String getCommandString() {
		return "clan";
	}

}
