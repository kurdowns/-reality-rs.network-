package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.Config;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.commands.Command;

public class DoubleXP implements Command {

	@Override
	public void execute(Player client, String message) {

		if(client.rights != 3) {
			return;
		}

		Config.DOUBLE_XP = !Config.DOUBLE_XP;

		PlayerHandler.yell("Double XP has been switched " + (Config.DOUBLE_XP ? "on" : "off") + " by " + client.username + "!");
	}

	@Override
	public String getCommandString() {
		return "doublexp";
	}

}
