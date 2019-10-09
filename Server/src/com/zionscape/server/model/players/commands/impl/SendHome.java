package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.Config;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.commands.Command;

public class SendHome implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.rights >= 1) {

			Player p = PlayerHandler.getPlayer(message);

			if (p == null) {
				return;
			}

			p.getPA().movePlayer(Config.RESPAWN_X, Config.RESPAWN_Y, 0);
			client.sendMessage("You have sent " + p.username + " home.");
			p.sendMessage("You have been sent home by " + client.username + ".");
		}
	}

	@Override
	public String getCommandString() {
		return "sendhome";
	}

}
