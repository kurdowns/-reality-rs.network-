package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.Config;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.commands.Command;

public class Kick implements Command {

	@Override
	public void execute(Player c, String message) {
		if (c.rights > 0) {
			try {
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].username.equalsIgnoreCase(message)) {
							if ((PlayerHandler.players[i].underAttackBy == 0 && System.currentTimeMillis()
									- PlayerHandler.players[i].logoutDelay > 10000)
									|| c.rights == 3 || !PlayerHandler.players[i].inWild()) {
								PlayerHandler.players[i].setDisconnected(true, "player kicked");
							} else {
								c.sendMessage("Player is in combat.");
							}
						}
					}
				}
			} catch (Exception e) {
				c.sendMessage("Player Must Be Online.");
			}
		}
	}

	@Override
	public String getCommandString() {
		return "kick";
	}


}
