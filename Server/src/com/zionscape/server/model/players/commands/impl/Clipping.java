package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.cache.Collision;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class Clipping implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.rights < 2) {
			return;
		}
		client.sendMessage(Collision.getClipping(client.absX, client.absY, client.heightLevel) + " ");


		client.sendMessage("north: " + Collision.blockedNorth(client.absX, client.absY, client.heightLevel));
		client.sendMessage("east: " + Collision.blockedEast(client.absX, client.absY, client.heightLevel));
		client.sendMessage("south: " + Collision.blockedSouth(client.absX, client.absY, client.heightLevel));
		client.sendMessage("west: " + Collision.blockedWest(client.absX, client.absY, client.heightLevel));
	}

	@Override
	public String getCommandString() {
		return "clipping";
	}

}
