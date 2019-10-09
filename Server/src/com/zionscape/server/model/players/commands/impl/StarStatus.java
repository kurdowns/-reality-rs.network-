package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;
import com.zionscape.server.plugin.impl.division.ShootingStars;

public class StarStatus implements Command {

	@Override
	public void execute(Player client, String message) {

		if(ShootingStars.locationName.isEmpty()) {
			client.sendMessage("There is currently no shooting star.");
		} else {
			client.sendMessage("The shooting start is currently at " + ShootingStars.locationName + ".");
		}
	}

	@Override
	public String getCommandString() {
		return "starstatus";
	}

}
