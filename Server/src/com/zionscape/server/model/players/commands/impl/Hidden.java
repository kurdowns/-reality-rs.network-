package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class Hidden implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.rights < 2) {
			return;
		}
		client.setHidden(!client.isHidden());
		client.sendMessage("hidden: " + client.isHidden());
		if(client.isHidden()) {
			client.setAttribute("hide_me", true);
			GameCycleTaskHandler.addEvent(client, container -> {
				client.removeAttribute("hide_me");
				container.stop();
			}, 2);
		}
	}

	@Override
	public String getCommandString() {
		return "hidden";
	}

}
