package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class ClearBank implements Command {

	@Override
	public void execute(Player client, String message) {
		if(client.rights >= 3 || client.connectedFrom.equalsIgnoreCase("127.0.0.1")) {
			client.getBank().clearBank();
			client.sendMessage("bank cleared");
		}
	}

	@Override
	public String getCommandString() {
		return "clearbank";
	}

}
