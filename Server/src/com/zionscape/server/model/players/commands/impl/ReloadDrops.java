package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.npcs.drops.NPCDropsHandler;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class ReloadDrops implements Command {

	@Override
	public void execute(Player client, String message) {
		try {
			NPCDropsHandler.loadDrops();
			client.sendMessage("Drops reloaded");
		} catch (Exception e) {
			client.sendMessage("failed");
			e.printStackTrace();
		}
	}

	@Override
	public String getCommandString() {
		return "reloaddrops";
	}

}