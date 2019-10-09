package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.content.minigames.quests.QuestInterfaceGenerator;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class Commands implements Command {

	@Override
	public void execute(Player client, String message) {

		QuestInterfaceGenerator qig = new QuestInterfaceGenerator("Commands");

		qig.add("::resetatt ::slayerguide");
		qig.add("::resetstr ::clueguide");
		qig.add("::resetdef ::starter");
		qig.add("::resethp ::drops");
		qig.add("::resetrange ::voteguide");
		qig.add("::resetpray ::vote");
		qig.add("::resetmag ::donate");
		qig.add("::players ::forums");
		qig.add("::claimvotes ::claim ");
		qig.add("::account ::changepassword");
		qig.add("::wildguide ::starstatus");
		qig.add("::maps ::account");

		qig.writeQuest(client);
	}

	@Override
	public String getCommandString() {
		return "commands";
	}

}
