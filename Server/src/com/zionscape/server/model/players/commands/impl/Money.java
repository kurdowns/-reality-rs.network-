package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class Money implements Command {

	@Override
	public void execute(Player client, String message) {
		int frame = 8147;
		client.getPA().sendFrame126("@dbl@Money Making", 8144); // Title
		client.getPA().sendFrame126("Have low amount of money? Need some more? Read this.", frame++);
		client.getPA().sendFrame126("Good Luck!", frame++);
		client.getPA().sendFrame126("", frame++);
		client.getPA().sendFrame126("There are many ways on getting money the first way is", frame++);
		client.getPA().sendFrame126("Slayer", frame++);
		client.getPA().sendFrame126("this skill is very helpful actually gives you alot of money", frame++);
		client.getPA().sendFrame126("drops alot of stuff that can be sold easily for 10m+", frame++);
		client.getPA().sendFrame126("Example at 85slayer you can kill abbys", frame++);
		client.getPA().sendFrame126("for whips and sell them but wait! you can also get", frame++);
		client.getPA().sendFrame126("alch runes with you and alch the rune item's", frame++);
		client.getPA().sendFrame126("dropped by the slayer monsters 1-85 slayer", frame++);
		client.getPA().sendFrame126("with alching all rune items will ", frame++);
		client.getPA().sendFrame126("get you around 35m a good start.", frame++);
		client.getPA().sendFrame126("Post suggestions what to write here on forums.", frame++);
		for (int i = 0; i < 80; i++) {
			client.getPA().sendFrame126("", frame + i);
		}
		client.getPA().showInterface(8134);
	}

	@Override
	public String getCommandString() {
		return "money";
	}

}
