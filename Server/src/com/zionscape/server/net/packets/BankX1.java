package com.zionscape.server.net.packets;

import com.google.common.base.MoreObjects;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;

/**
 * Bank X Items
 */
public class BankX1 implements PacketType {

	public static final int PART1 = 135;
	//public static final int PART2 = 208;

	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {
		if (packetType == 135) {
			c.xRemoveSlot = stream.readSignedWordBigEndian();
			c.xInterfaceId = stream.readUnsignedWordA();
			c.xRemoveId = stream.readSignedWordBigEndian();
		}

		if (c.rights >= 3) {
			c.sendMessage(MoreObjects.toStringHelper(BankX1.class).add("interface id", c.xInterfaceId).add("slot", c.xRemoveSlot).add("item id", c.xRemoveId).toString());
		}

		switch (c.xInterfaceId) {
			case 2700:
			case 5064:
			case 23878:
			case 22767:
				c.getOutStream().createFrame(27);
				break;
		}

		/*
		 * if (c.xInterfaceId == 3900) { c.getShops().buyItem(c.xRemoveId, c.xRemoveSlot, 20);//buy 20 c.xRemoveSlot =
		 * 0; c.xInterfaceId = 0; c.xRemoveId = 0; return; }
		 */
		if (c.xInterfaceId == 3900) {
			c.buyingX = true;
			c.getOutStream().createFrame(27);
			return;
		}
		if (packetType == PART1) {
			c.getOutStream().createFrame(27);
		}
	}
}
