package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.Location;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.commands.Command;

public class BringMe implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.rights >= 2) {

			Player p = PlayerHandler.getPlayer(message);

			if (p == null) {
				return;
			}
			if (p.inTrinityWar()) {
				client.sendMessage("That player is in trinity wars - You can't teleport to him!");
				return;
			}
			if (client.inTrinityWar()) {
				client.sendMessage("You're in trinity wars - You can't teleport someone to you!");
				return;
			}
			if (p.noTele && client.rights != 3) {
				client.sendMessage("He doesn't want to be teleported");
				return;
			}

			p.setTeleportLocation(Location.create(client.absX, client.absY, client.heightLevel));

			client.sendMessage("You have teleported " + p.username + " to you");
			p.sendMessage("You have been teleported by " + client.username);
			client.updateRequired = true;
			client.appearanceUpdateRequired = true;
			p.updateRequired = true;
			p.appearanceUpdateRequired = true;
		}
	}

	@Override
	public String getCommandString() {
		return "bringme";
	}

}
