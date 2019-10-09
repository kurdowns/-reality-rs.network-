package com.zionscape.server.net;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.util.Stream;

public interface PacketType {

	void processPacket(Player c, int packetType, int packetSize, Stream stream);

}
