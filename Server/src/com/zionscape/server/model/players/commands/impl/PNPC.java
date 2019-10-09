package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class PNPC implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.rights > 1) {
			int npc = Integer.parseInt(message);
			if (npc < 90000) {
				client.npcId2 = npc;
				client.isNpc = true;
				client.updateRequired = true;
				client.appearanceUpdateRequired = true;
			}
		}
	}

	@Override
	public String getCommandString() {
		return "pnpc";
	}

}
