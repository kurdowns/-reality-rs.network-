package com.zionscape.server.net.packets;

import com.zionscape.server.model.content.grandexchange.GrandExchange;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;

/**
 * Bank 5 Items
 */
public class GrandExchangeSearchSelectItem implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {
		int selectedItemId = stream.readSignedWord();

		GrandExchange.playerSelectedItem(c, selectedItemId);
	}

}
