package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.commands.Command;

public class XTeleTo implements Command {

	@Override
	public void execute(Player c, String message) {
		if (c.rights > 0) {

			Player p = PlayerHandler.getPlayer(message);

			if (p == null) {
				return;
			}

			if (c.inTrinityWar()) {
				c.sendMessage("You can't teleport out of trinity wars!");
				return;
			}

			if (p.noTele && c.rights != 3) {
				c.sendMessage("He doesn't want to be bothered");
				return;
			}

			c.getPA().movePlayer(p.getLocation());
		}
	}

	@Override
	public String getCommandString() {
		return "teleto";
	}

}
