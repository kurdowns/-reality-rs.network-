package com.zionscape.server.model.players.commands;

import com.zionscape.server.model.players.Player;

public interface Command {

	public void execute(Player client, String message);

	public String getCommandString();


}