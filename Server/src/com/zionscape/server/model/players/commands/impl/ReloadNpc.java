package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class ReloadNpc implements Command {

	@Override
	public void execute(Player client, String message) {

		if (client.rights < 1) {
			return;
		}

		int npcId = Integer.parseInt(message);

		for (com.zionscape.server.model.npcs.NPC npc : NPCHandler.npcs) {
			if (npc == null) {
				continue;
			}
			if (npc.type == npcId) {
				npc.isDead = true;
			}
		}
	}

	@Override
	public String getCommandString() {
		return "reloadnpc";
	}

}