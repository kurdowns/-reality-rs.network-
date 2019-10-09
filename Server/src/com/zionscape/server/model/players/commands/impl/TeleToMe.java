package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.commands.Command;

public class TeleToMe implements Command {
	
	@Override
	public void execute(Player c, String message) {
		if (c.rights > 0) {

			Player p = PlayerHandler.getPlayer(message);

			if (p == null) {
				return;
			}

			if (p.inTrinityWar()) {
				c.sendMessage("You can't teleport him out of trinity wars!");
				return;
			}

			p.getPA().movePlayer(c.getLocation());
		}
	}

	@Override
	public String getCommandString() {
		return "teletome";
	}


}
