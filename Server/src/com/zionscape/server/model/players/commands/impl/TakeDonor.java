package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.commands.Command;

public class TakeDonor implements Command {

	@Override
	public void execute(Player player, String message) {
		if (player.rights < 2)
			return;
		String[] cmd = message.split(" ");
		if (cmd.length < 1) {
			player.sendMessage("Usage - ::takedonor username -> ::takedonor oly");
			return;
		}

		Player target = PlayerHandler.getPlayer(cmd[0]);
		if (target == null) {
			player.sendMessage(cmd[1] + " is not online.");
			return;
		}
		
		target.setTotalDonated(0);
		target.sendMessage("Your donor rights have been taken away by " + player.username + ".");
		player.sendMessage("You have taken away " + target.username + "'s donator ranks.");
		return;
	}

	@Override
	public String getCommandString() {
		return "takedonor";
	}

}
