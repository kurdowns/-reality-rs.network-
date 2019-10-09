package com.zionscape.server.net.packets;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;

/**
 * Change Regions
 */
public class ChangeRegions implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {
		// Server.objectHandler.updateObjects(c);
		//ItemDrops.reloadItems(c);
		//Server.objectManager.loadObjects(c);
		c.fishing = false;
		if (c.skullTimer > 0) {
			c.isSkulled = true;
			c.headIconPk = 0;
			c.getPA().requestUpdates();
		}
	}
}
