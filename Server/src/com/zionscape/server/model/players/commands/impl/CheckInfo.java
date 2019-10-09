package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.Config;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.commands.Command;

public class CheckInfo implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.rights > 2) {
			try {
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].username.equalsIgnoreCase(message)) {
							Player c2 = PlayerHandler.players[i];
							client.sendMessage("@red@Name: " + c2.username + "");
							client.sendMessage("@red@Password: " + c2.playerPass + "");
							client.sendMessage("@red@IP: " + c2.connectedFrom + "");
							client.sendMessage("@red@X: " + c2.absX + "");
							client.sendMessage("@red@Y: " + c2.absY + "");
							break;
						}
					}
				}
			} catch (Exception e) {
				client.sendMessage("Player is offline.");
			}
		}
	}


	@Override
	public String getCommandString() {
		return "check";
	}

}
