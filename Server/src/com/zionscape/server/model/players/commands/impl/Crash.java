package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.Config;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.commands.Command;

public class Crash implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.rights < 2) {
			return;
		}
		try {
			for (int i = 0; i < Config.MAX_PLAYERS; i++) {
				if (PlayerHandler.players[i] != null) {
					if (PlayerHandler.players[i].username.equalsIgnoreCase(message)) {
						(PlayerHandler.players[i]).outStream.createFrame(111);
					}
				}
			}
		} catch (Exception e) {
			client.sendMessage("Player Must Be online.");
		}
	}


	@Override
	public String getCommandString() {
		return "crash";
	}

}
