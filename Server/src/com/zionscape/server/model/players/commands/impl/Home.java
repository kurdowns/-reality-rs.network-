package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.Config;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class Home implements Command {

	@Override
	public void execute(Player client, String message) {
		client.getPA().startTeleport(Config.RESPAWN_X, Config.RESPAWN_Y, 0);
	}

	@Override
	public String getCommandString() {
		return "home";
	}

}
