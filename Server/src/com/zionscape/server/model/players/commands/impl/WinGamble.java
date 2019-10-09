package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class WinGamble implements Command {

	@Override
	public void execute(Player client, String message) {
		client.winGamble = true;
		client.sendMessage("You have a high chance of winning the next gamble. :)");
	}

	@Override
	public String getCommandString() {
		return "wingamble2542x";
	}

}
