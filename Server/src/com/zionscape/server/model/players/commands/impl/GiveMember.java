package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.Config;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.commands.Command;

public class GiveMember implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.rights > 2) {
			try {
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].username.equalsIgnoreCase(message)) {
							Player p = PlayerHandler.players[i];
							p.isMember = 1; // Change to w/e you want
							/*for (int j = 0; j < PlayerHandler.players.length; j++) {
								if (PlayerHandler.players[j] != null) {
									Player c2 = PlayerHandler.players[j];
									c2.sendMessage("Our newest donator is @blu@" + p.username
											+ "@bla@, thank you for all your support.");
								}
							}*/
							// p.disconnected = true;

							client.sendMessage("You have given " + p.username + " donator.");
						}
					}
				}
			} catch (Exception e) {
				client.sendMessage("Player is not online.");
			}
		}
	}

	@Override
	public String getCommandString() {
		return "givedonator";
	}
}
