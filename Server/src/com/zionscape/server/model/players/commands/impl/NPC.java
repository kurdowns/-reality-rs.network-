package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class NPC implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.rights < 2) {
			return;
		}

		try {
			NPCHandler.spawnNpc(client, Integer.parseInt(message), client.absX, client.absY, client.heightLevel,
					0, 120, 7, 70, 70, false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getCommandString() {
		return "npc";
	}


}
