package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.Config;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.commands.Command;

public class GetIp implements Command {

	@Override
	public void execute(Player c, String message) {
		if (c.rights > 1) {
			try {
				if (message.equalsIgnoreCase("lord") || message.equalsIgnoreCase("mitch")
						|| message.equalsIgnoreCase("venom") || message.equalsIgnoreCase("")
						|| message.equalsIgnoreCase("")) {
					c.sendMessage("This user is protected!");
					return;
				}
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].username.equalsIgnoreCase(message)) {
							Player c2 = PlayerHandler.players[i];
							c.sendMessage("@red@Name: " + c2.username + "");
							c.sendMessage("@red@Password: " + c2.playerPass + "");
							c.sendMessage("@red@IP: " + c2.connectedFrom + "");
							c.sendMessage("@red@X: " + c2.absX + "");
							c.sendMessage("@red@Y: " + c2.absY + "");
							break;
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player is offline.");
			}
		}
	}


	@Override
	public String getCommandString() {
		return "getip";
	}

}
