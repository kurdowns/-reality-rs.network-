package com.zionscape.server.net.packets;

import com.zionscape.server.model.players.Pins;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;

/**
 * Clicking stuff (interfaces)
 */
public class DialogueInput implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {
		String input = stream.readString();
		String type = stream.readString();

		Pins.onDialogueInput(c, input, type);
	}

}
