package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.items.ItemDefinition;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class ReloadItemDefs implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.rights != 3) {
			return;
		}
		ItemDefinition.load();
		client.sendMessage("item definitions reloaded");
	}

	@Override
	public String getCommandString() {
		return "reloaditemdefs";
	}

}
