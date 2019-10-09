package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.combat.MaxHits;
import com.zionscape.server.model.players.commands.Command;

public class MaxHit implements Command {

	@Override
	public void execute(Player client, String message) {

		if (client.rights < 3) {
			return;
		}

		client.sendMessage("Melee: " + MaxHits.calculateMeleeMaxHit(client));
		client.sendMessage("Range: " + MaxHits.rangeMaxHit(client));
		if (client.spellId > 0) {
			client.sendMessage("Magic: " + MaxHits.calculateMagicMaxHit(client, client.spellId));
		}
	}

	@Override
	public String getCommandString() {
		return "maxhit";
	}

}
