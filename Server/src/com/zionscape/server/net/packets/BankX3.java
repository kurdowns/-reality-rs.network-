package com.zionscape.server.net.packets;

import com.google.common.base.MoreObjects;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;
import com.zionscape.server.world.shops.Shops;

public class BankX3 implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {
		c.xRemoveSlot = stream.readSignedWordBigEndian();
		c.xInterfaceId = stream.readSignedWordA();
		c.xRemoveId = stream.readSignedWordBigEndian();
		int amount = stream.readDWord();

		if (amount < 1) {
			amount = 1;
		}

		if (c.rights >= 3) {
			c.sendMessage(MoreObjects.toStringHelper(BankX3.class)
					.add("interface id", c.xInterfaceId)
					.add("item", c.xRemoveId)
					.add("slot", c.xRemoveSlot)
					.add("amount", amount)
					.toString());
		}

		switch (c.xInterfaceId) {

			case 3823:
				Shops.sellItem(c, c.xRemoveId, amount, c.xRemoveSlot);
				break;

			case 15292:
			case 15293:
			case 15294:
			case 15295:
			case 15296:
			case 15297:
			case 15298:
			case 15299:
			case 15300:
			case 15302:
				c.getBank().withdrawItem(c.xRemoveId, amount, c.xRemoveSlot, -1);
				break;
		}
	}
}
