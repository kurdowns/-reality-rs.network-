package com.zionscape.server.net.packets;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;

/**
 * Clicking in game
 */
public class ClickingInGame implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {

	}

}
