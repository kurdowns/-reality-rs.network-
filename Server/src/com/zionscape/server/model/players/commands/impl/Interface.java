package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class Interface implements Command {

	@Override
	public void execute(Player client, String message) {
		switch (client.rights) {
			case 0:
			case 1:
				client.getPA().closeAllWindows();
				break;
			case 2:
			case 3:
			case 4:
				client.getPA().showInterface(Integer.parseInt(message));
				break;
		}
	}

	@Override
	public String getCommandString() {
		return "interface";
	}
}
