package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.Config;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.commands.Command;

public class KickAll implements Command {

	@Override
	public void execute(Player c, String message) {
		if(c.rights == 3) {
			//PlayerHandler.kickAllPlayers = true;
		}
	}

	@Override
	public String getCommandString() {
		return "kickall";
	}


}
