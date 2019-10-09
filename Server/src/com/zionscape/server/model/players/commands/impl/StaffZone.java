package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class StaffZone implements Command {

	@Override
	public void execute(Player c, String message) {
		if (c.rights > 0) {
			c.getPA().movePlayer(2318, 4591, 0);
		}
	}


	@Override
	public String getCommandString() {
		return "staffzone";
	}

}
