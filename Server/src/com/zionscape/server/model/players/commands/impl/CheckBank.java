package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.commands.Command;

public class CheckBank implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.rights > 0) {

			Player p = PlayerHandler.getPlayer(message);

			if (p == null) {
				return;
			}


			client.sendMessage("Checking " + p.username + " bank. Please do not alter the bank in anyway or form.");
		}
	}

	@Override
	public String getCommandString() {
		return "checkbank";
	}

}
