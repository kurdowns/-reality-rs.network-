package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class DungeonEmote implements Command {

	@Override
	public void execute(Player client, String message) {
		if ((client.isMember > 0 && client.dungeonEmote > 0) || client.rights > 1) {
			client.startAnimation(13190);
			client.gfx0(2442);
		}
	}

	@Override
	public String getCommandString() {
		return "dungeonemote";
	}
}
