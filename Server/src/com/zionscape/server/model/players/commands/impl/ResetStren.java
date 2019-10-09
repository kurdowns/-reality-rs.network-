package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class ResetStren implements Command {

	@Override
	public void execute(Player c, String message) {
		if (c.inWild()) {
			return;
		}
		for (int j = 0; j < c.equipment.length; j++) {
			if (c.equipment[j] > 0) {
				c.sendMessage("Please take all your armour and weapons off before using this command.");
				// c.sendMessage("Register on forums or this command will be removed www.zionscape.com/forum/");
				return;
			}
		}
		try {
			int skill = 2;
			int level = 1;
			c.xp[skill] = c.getPA().getXPForLevel(level) + 5;
			c.level[skill] = c.getPA().getLevelForXP(c.xp[skill]);
			c.getPA().refreshSkill(skill);
			// writePlayerReport(playerCommand,c.username);
		} catch (Exception e) {
		}
	}

	@Override
	public String getCommandString() {
		return "resetstr";
	}

}
