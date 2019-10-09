package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.Config;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.commands.Command;

public class TriplePK implements Command {

	@Override
	public void execute(Player client, String message) {
		if(client.rights != 3) {
			return;
		}

		Config.triplePk = !Config.triplePk;

		PlayerHandler.yell("Triple PK points has been switched " + (Config.triplePk ? "on" : "off") + " by " + client.username + "!");
	}

	@Override
	public String getCommandString() {
		return "triplepk";
	}

}
