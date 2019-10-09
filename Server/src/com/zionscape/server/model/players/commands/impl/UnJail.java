package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.Config;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.commands.Command;
import com.zionscape.server.util.Misc;

public class UnJail implements Command {

	@Override
	public void execute(Player c, String message) {
		if (c.rights > 0) {
			try {
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].username.equalsIgnoreCase(message)) {
							Player c2 = PlayerHandler.players[i];
							c2.getPA().movePlayer(Config.RESPAWN_X + Misc.random(2), Config.RESPAWN_Y + Misc.random(2), 0);
							c2.sendMessage("Don't screw up again!");
							c.sendMessage(message + " has been unjailed.");
							c2.jailTimer = 0;
							break;
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
		return "unjail";
	}


}
