package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.commands.Command;

public class ResetPrayer implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.inWild()) {
			return;
		}
		for (int j = 0; j < client.equipment.length; j++) {
			if (client.equipment[j] > 0) {
				client.sendMessage("Please take all your armour and weapons off before using this command.");
				return;
			}
		}
		try {
			int skill = PlayerConstants.PRAYER;
			int level = 1;
			client.xp[skill] = client.getPA().getXPForLevel(level) + 5;
			client.level[skill] = client.getPA().getLevelForXP(client.xp[skill]);
			client.getPA().refreshSkill(skill);
			for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows
				client.prayerActive[p] = false;
				client.getPA().sendFrame36(client.PRAYER_GLOW[p], 0);
			}
			for (int p = 0; p < client.CURSE_ID.length; p++) { // reset prayer glows
				client.curseActive[p] = false;
				client.getPA().sendFrame36(client.CURSE_GLOW[p], 0);
			}
			// writePlayerReport(playerCommand,client.username);
		} catch (Exception e) {
		}
	}

	@Override
	public String getCommandString() {
		return "resetpray";
	}

}
