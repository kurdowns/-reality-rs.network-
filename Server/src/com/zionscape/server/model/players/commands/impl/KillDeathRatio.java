package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class KillDeathRatio implements Command {

	@Override
	public void execute(Player client, String message) {
		double KDR = (double) client.kills / (double) client.deaths;
		client.forcedChat("My Kill/Death ratio is " + client.kills + "/" + client.deaths + "; " + KDR + ".");
	}

	@Override
	public String getCommandString() {
		return "kd";
	}

}
