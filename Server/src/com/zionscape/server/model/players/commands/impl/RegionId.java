package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class RegionId implements Command {

	@Override
	public void execute(Player client, String message) {
		client.sendMessage("region id: " + client.getRegionID(client.absX, client.absY));
	}

	@Override
	public String getCommandString() {
		return "regionid";
	}

}
