package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.content.grandexchange.GrandExchange;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class ToggleGE implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.rights == 0) {
			return;
		}

		GrandExchange.enabled = !GrandExchange.enabled;

		client.sendMessage("GE: " + GrandExchange.enabled);
	}

	@Override
	public String getCommandString() {
		return "togglege";
	}

}
