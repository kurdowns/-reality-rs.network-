package com.zionscape.server.net.packets;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;

public class ItemOnGroundItem implements PacketType {

	@SuppressWarnings("unused")
	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {
		int a1 = stream.readSignedWordBigEndian();
		int itemUsed = stream.readSignedWordA();
		int groundItem = stream.readUnsignedWord();
		int gItemY = stream.readSignedWordA();
		int itemUsedSlot = stream.readSignedWordBigEndianA();
		int gItemX = stream.readUnsignedWord();
		switch (itemUsed) {
			default:
				if (c.rights == 3) {
					System.out.println("ItemUsed " + itemUsed + " on Ground Item " + groundItem);
				}
				break;
		}
	}
}
