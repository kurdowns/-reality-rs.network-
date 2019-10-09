package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.commands.Command;

public class SendPackets implements Command {

	@Override
	public void execute(Player c, String message) {
		if (c.rights >= 3) {

			Player p = PlayerHandler.getPlayer(message);

			if (p == null) {
				return;
			}

			p.packetSend = true;
			c.sendMessage(p.username + " is now sending packet ids");
		}
	}

	@Override
	public String getCommandString() {
		return "sendpackets";
	}

}
