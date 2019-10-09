package com.zionscape.server.net.packets;

import com.zionscape.server.ServerEvents;
import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;

public class ItemOnPlayer implements PacketType {


	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {
		int fromInterface = stream.readUnsignedWordA();
		int otherPlayerId = stream.readUnsignedWord();
		int useItemId = stream.readUnsignedWord();
		int useItemSlot = stream.readSignedWordBigEndian();

		if (otherPlayerId < 0 || otherPlayerId > PlayerHandler.players.length) {
			return;
		}
		if ((c.inventory[useItemSlot] - 1) != useItemId) {
			return;
		}

		Player target = PlayerHandler.players[otherPlayerId];

		if (c.rights > 2) {
			System.out.println("item id: " + useItemId + " slot: " + useItemSlot + " interface: " + fromInterface + " used on: " + target.username);
		}

		if (ServerEvents.onItemOnPlayer(c, target, new Item(useItemId, c.inventoryN[useItemSlot]))) {
			return;
		}

		c.sendMessage("Nothing interesting happens.");
	}

}