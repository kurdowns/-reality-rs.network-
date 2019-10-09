package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.commands.Command;

public class Yell implements Command {

	@Override
	public void execute(Player c, String message) {
		if (c.rights > 0 || c.isMember()) {
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					Player c2 = PlayerHandler.players[j];
					String n = c.username;
					n = "" + n.substring(0, 1).toUpperCase() + n.substring(1);
					switch (c.rights) {
					case 1:
						c2.sendMessage("@blu@[Mod]@bla@<img=0>" + n + ": " + "@bla@" + message);
						break;
					case 2:
						c2.sendMessage("@blu@[Admin]@bla@<img=1>" + n + ": " + "@bla@" + message);
						break;
					case 3:
						c2.sendMessage("@blu@[Owner]@bla@<img=1>" + n + ": " + "@bla@" + message);
						break;
					case 4:
						c2.sendMessage("@blu@[Developer]@bla@<img=3>" + n + ": " + "@bla@" + message);
						break;
					default:
						c2.sendMessage((c.isUberDonator() ? "@yel@[Uber Donator]"
								: c.isLegendaryDonator() ? "@pur@[Legendary Donator]"
										: c.isExtremeDonator() ? "@gre@[Extreme Donator]"
												: c.isSuperDonator() ? "@blu@[Super Donator]" : "@red@[Donator]") 
								+ " " + n + ": "
								+ "@bla@" + message);
					}
				}
			}
		}
	}

	@Override
	public String getCommandString() {
		return "yell";
	}

}
