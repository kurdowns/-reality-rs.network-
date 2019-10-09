package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;
import com.zionscape.server.net.Packet;
import com.zionscape.server.net.PacketBuilder;
import com.zionscape.server.util.Misc;

public class Test implements Command {

	private static final int GROWING = 0x00;
	private static final int WATERED = 0x01;
	private static final int DISEASED = 0x02;
	private static final int DEAD = 0x03;
	private static int anim = 1;


	// 3049 3309

	// 2812 3461
	@Override
	public void execute(Player client, String message) {

		if (client.rights != 3) {
			return;
		}

		//client.getPA().sendFrame126("Players plant 5 flowers, \\nwhoever ends up with the \\nhighest score wins the pot! \\nNo pairs of the same color: \\nBust Pair of the same color: \\nOne pair 2 pairs of the same \\nflower: Double pair 3 of the \\nsame flower: 3 of a kind 3 of \\nthe same flower & a pair: \\nFull house 4 colors of the \\nsame flower: Four of a kind 5 \\nof the same flower: Five of \\na kind", 19344);

		Packet packet = new PacketBuilder(28, Packet.Type.VARIABLE_SHORT).putRS2String("testing 123").putRS2String("\\d").putRS2String("test").toPacket();
		client.writePacket(packet);

	}

	@Override
	public String getCommandString() {
		return "test";
	}

}
