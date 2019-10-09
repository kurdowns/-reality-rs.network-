package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class Item implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.rights > 2) {

			String[] args = message.split(" ");

			if (args.length == 0) {
				client.sendMessage("Use as ::item 995 1000 the amount is optional, you may also add k, m, b to end of amount.");
				return;
			}

			int itemId = Integer.parseInt(args[0]);
			int amount = 1;

			if (args.length == 2) {
				if (args[1].endsWith("k")) {
					amount = Integer.parseInt(args[1].replace("k", "")) * 1_000;
				} else if (args[1].endsWith("m")) {
					amount = Integer.parseInt(args[1].replace("m", "")) * 1_000_000;
				} else if (args[1].endsWith("b")) {
					amount = Integer.parseInt(args[1].replace("b", "")) * 1_000_000_000;
				} else {
					amount = Integer.parseInt(args[1]);
				}
			}

			if (amount < 1) {
				client.sendMessage("Amount must be bigger than 1");
				return;
			}

			client.getItems().addItem(itemId, amount);
		}
	}

	@Override
	public String getCommandString() {
		return "item";
	}


}