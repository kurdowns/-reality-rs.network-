package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;
import com.zionscape.server.world.shops.Shops;

public class Shop implements Command {

	@Override
	public void execute(Player client, String message) {
		if(client.rights != 3) {
			return;
		}
		Shops.open(client, Integer.parseInt(message));
	}

	@Override
	public String getCommandString() {
		return "shop";
	}

}
