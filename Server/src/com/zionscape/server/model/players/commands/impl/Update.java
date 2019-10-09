package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.commands.Command;

public class Update implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.rights > 2) {
			try {
				int temp = Integer.parseInt(message);
				PlayerHandler.updateSeconds = temp;
				PlayerHandler.updateAnnounced = false;
				PlayerHandler.updateRunning = true;
				PlayerHandler.updateStartTime = System.currentTimeMillis();
			} catch (NumberFormatException e) {
			}
		}
	}

	@Override
	public String getCommandString() {
		return "update";
	}

}
