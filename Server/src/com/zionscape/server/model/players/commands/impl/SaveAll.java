package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.PlayerSave;
import com.zionscape.server.model.players.commands.Command;

public class SaveAll implements Command {

	@Override
	public void execute(Player client, String message) {

		if (client.rights < 3) {
			return;
		}

		client.sendMessage("Saving all players");

		for (Player plr : PlayerHandler.players) {
			if (plr == null) {
				continue;
			}
			PlayerSave.saveGame(plr);
		}

		client.sendMessage("Done saving");
	}

	@Override
	public String getCommandString() {
		return "saveall";
	}

}
