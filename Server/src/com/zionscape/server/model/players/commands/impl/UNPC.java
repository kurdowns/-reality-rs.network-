package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class UNPC implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.rights > 1) {
			client.isNpc = false;
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
		}
	}

	@Override
	public String getCommandString() {
		return "upnpc";
	}

}
