package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.Location;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class Tele implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.rights >= 2) {
			String[] args = message.split(" ");
			if (args.length == 2) {
				client.setTeleportLocation(Location.create(Integer.parseInt(args[0]), Integer.parseInt(args[1]), client.heightLevel));
			} else if (args.length == 3) {
				client.setTeleportLocation(Location.create(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2])));
			}
		}
	}

	@Override
	public String getCommandString() {
		return "tele";
	}

}