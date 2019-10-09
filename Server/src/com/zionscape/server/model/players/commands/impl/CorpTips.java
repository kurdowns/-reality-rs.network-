package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;

public class CorpTips implements Command {

	@Override
	public void execute(Player client, String message) {
		int frame = 8147;
		client.getPA().sendFrame126("@dbl@Corp Tips & Drops", 8144); // Title
		client.getPA().sendFrame126("", 8146);
		client.getPA().sendFrame126("The corporeal beast is an extremely hard monster", frame++);
		client.getPA().sendFrame126("You NEED a team of about 5-10 people", frame++);
		client.getPA().sendFrame126("He will hit high - Up to 65 with his magic", frame++);
		client.getPA().sendFrame126("Pray magic while in there", frame++);
		client.getPA().sendFrame126("Bring a lot of food, preferably rocktails", frame++);
		client.getPA().sendFrame126("------------Drops------------", frame++);
		client.getPA().sendFrame126("@blu@Common drops:", frame++);
		client.getPA().sendFrame126("Coins - 100", frame++);
		client.getPA().sendFrame126("Magic Scroll - 1", frame++);
		client.getPA().sendFrame126("Dragon Bolt Tips - 100", frame++);
		client.getPA().sendFrame126("Spirit Shield - 1", frame++);
		client.getPA().sendFrame126("Dragon Scimitar - 1", frame++);
		client.getPA().sendFrame126("@red@Rare Drops:", frame++);
		client.getPA().sendFrame126("Holy Elixir - 1", frame++);
		client.getPA().sendFrame126("Spectral Sigil - 1", frame++);
		client.getPA().sendFrame126("Arcane Sigil - 1", frame++);
		client.getPA().sendFrame126("Elysian Sigil - 1", frame++);
		client.getPA().sendFrame126("Divine Sigil - 1", frame++);
		client.getPA().sendFrame126("------------Sigil Guide------------", frame++);
		client.getPA().sendFrame126("Combine a spirit shield and holy elixir first", frame++);
		client.getPA().sendFrame126("That will create a blessed spirit shield", frame++);
		client.getPA().sendFrame126("Then, combine that with any sigil you want", frame++);
		client.getPA().sendFrame126("NOTE: Sigils can NOT be removed from a spirit shield!", frame++);
		client.getPA().sendFrame126("Good luck!", frame++);
		for (int i = 0; i < 80; i++) {
			client.getPA().sendFrame126("", frame + i);
		}
		client.getPA().showInterface(8134);
	}

	@Override
	public String getCommandString() {
		return "corp";
	}

}
