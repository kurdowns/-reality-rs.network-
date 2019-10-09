package com.zionscape.server.net.packets;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;

public class IdleLogout implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {
		// if (!c.username.equalsIgnoreCase("Sanity"))
		// c.logout();
	}
}