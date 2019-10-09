package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class Empty implements Command {

	public static boolean onDialogueOption(Player player, int option) {
		if (player.getDialogueOwner() != null && player.getDialogueOwner().equals(Empty.class)) {
			if (option == 1) {
				player.getPA().removeAllItems();
				player.sendMessage("You empty your inventory");
			}
			player.resetDialogue();
			return true;
		}
		return false;
	}

	@Override
	public void execute(Player client, String message) {
		if (client.isMember >= 0 || client.rights > 1) {
			if (client.inWild()) {
				client.sendMessage("You can't use this in the wild!");
				return;
			}
			client.getDH().sendOption2("Empty inventory - You can't get your items back!!", "No thanks.");
			client.setDialogueOwner(Empty.class);
		}
	}

	@Override
	public String getCommandString() {
		return "empty";
	}

}
