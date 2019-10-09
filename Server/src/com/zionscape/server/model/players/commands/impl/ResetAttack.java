package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class ResetAttack implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.inWild()) {
			return;
		}
		for (int j = 0; j < client.equipment.length; j++) {
			if (client.equipment[j] > 0) {
				client.sendMessage("Please take all your armour and weapons off before using this command.");
				//   client.sendMessage("Register on forums or this command will be removed www.eximinus.com/forum/");
				return;
			}
		}
		try {
			int skill = 0;
			int level = 1;
			client.xp[skill] = client.getPA().getXPForLevel(level) + 5;
			client.level[skill] = client.getPA().getLevelForXP(client.xp[skill]);
			client.getPA().refreshSkill(skill);
		} catch (Exception e) {
		}
	}

	@Override
	public String getCommandString() {
		return "resetatt";
	}


}
