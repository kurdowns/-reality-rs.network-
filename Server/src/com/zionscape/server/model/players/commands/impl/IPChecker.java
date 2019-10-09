package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.Config;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.commands.Command;

public class IPChecker implements Command {

	@Override
	public void execute(Player c, String message) {
		if (c.rights > 2) {
			try {
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].username.equalsIgnoreCase(message)) {
							c.sendMessage("IP: " + PlayerHandler.players[i].connectedFrom);
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
	}

	@Override
	public String getCommandString() {
		return "ipcheck";
	}

}
