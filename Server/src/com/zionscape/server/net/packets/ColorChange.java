package com.zionscape.server.net.packets;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;

import java.util.HashMap;

/**
 * Attack Player
 */
public class ColorChange implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {
		if(c.openInterfaceId != 25300) {
			return;
		}

		if(!c.attributeExists("color_item")) {
			return;
		}

		int itemId = (int)c.getAttribute("color_item");

		if(!c.getData().itemColors.containsKey(itemId)) {
			c.getData().itemColors.put(itemId, new HashMap<>());
		}

		c.getData().itemColors.get(itemId).put(stream.readUnsignedByte(), stream.readUnsignedWord());
		c.setAppearanceUpdateRequired(true);
		c.updateRequired = true;

		c.getPA().sendColorInterface(itemId);
		c.getPA().showInterface(25300);
	}

}