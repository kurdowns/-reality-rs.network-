package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.Config;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.commands.Command;

public class Jail implements Command {

	@Override
	public void execute(Player c, String message) {
		if (c.rights > 0) {

			String[] args = message.split(" ");
			if (args.length < 2) {
				c.sendMessage("Usage - ::jail username time -> ::jail test 999d");
				
				return;
			}

			System.out.println(args[0] + " " + args[1]);


			Player target = PlayerHandler.getPlayer(args[0]);
			if (target == null) {
				c.sendMessage(args[0] + " is not online.");
				return;
			}

			long timer;
			if(args[1].endsWith("m")) {
				timer = Long.parseLong(args[1].trim().replace("m", "")) * 100;
				c.sendMessage("" + timer);
			} else if(args[1].endsWith("h")) {
				timer = Long.parseLong(args[1].trim().replace("h", "")) * 60 * 100;
				c.sendMessage("" + timer);
			} else if(args[1].endsWith("d")) {
				timer = Long.parseLong(args[1].trim().replace("d", "")) * 24 * 60 * 100;
				c.sendMessage("" + timer);
			} else {
				c.sendMessage("Invalid time specified please add m, h or d to end of time to specify the time unit.");
				return;
			}

			target.jailTimer += timer;
			target.sendMessage("Bad Boy. You have been jailed by " + c.username + " for " + args[1].trim() + ".");
			target.getPA().movePlayer(2602, 4775, 0);
			c.sendMessage("You have jailed " + target.username + " for " + args[1].trim() + ".");
		}
	}

	@Override
	public String getCommandString() {
		return "jail";
	}

}
