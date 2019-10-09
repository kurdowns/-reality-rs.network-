package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.content.minigames.gambling.Gambling;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class Gamble implements Command {

	@Override
	public void execute(Player client, String message) {
		Gambling.teleport(client);
	}

	@Override
	public String getCommandString() {
		return "gamble";
	}

}
