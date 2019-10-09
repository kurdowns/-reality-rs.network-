package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.commands.Command;

public class GiveDonor implements Command {

	@Override
	public void execute(Player player, String message) {
		if (player.rights < 2)
			return;
		String[] cmd = message.split(" ");
		if (cmd.length < 2) {
			player.sendMessage("Usage - ::givedonor rankname username -> ::givedonor uber oly");
			return;
		}

		String rankName = cmd[0];
		Player target = PlayerHandler.getPlayer(cmd[1]);
		if (target == null) {
			player.sendMessage(cmd[1] + " is not online.");
			return;
		}

		switch (rankName.toLowerCase()) {
		case "uber":
			target.setTotalDonated(2500);
			break;
		case "legendary":
			target.setTotalDonated(750);
			break;
		case "extreme":
			target.setTotalDonated(250);
			break;
		case "super":
			target.setTotalDonated(50);
			break;
		case "donator":
			target.setTotalDonated(10);
			break;
		default:
			player.sendMessage(rankName + " is not a donator rank.");
			return;
		}
		target.sendMessage("You have been given " + rankName + " donator by <col=ff0000>" + player.username
				+ "</col> and currently have " + target.getTotalDonated() + " total donation amount.");
		
		player.sendMessage("You have given " + target.username + " " + rankName + " donator rank.");
		return;
	}

	@Override
	public String getCommandString() {
		return "givedonor";
	}

}
