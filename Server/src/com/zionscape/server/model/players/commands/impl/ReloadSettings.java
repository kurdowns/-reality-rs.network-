package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.Server;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

import java.io.IOException;

public class ReloadSettings implements Command {

	@Override
	public void execute(Player client, String message) {
		if(client.rights < 3) {
			return;
		}
		try {
			Server.loadSettings();
			client.sendMessage("server settings reloaded");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getCommandString() {
		return "reloadsettings";
	}

}
