package com.zionscape.server.net.packets;

/**
 * @author Ryan / Lmctruck30
 */

import com.zionscape.server.ServerEvents;
import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.items.UseItem;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.skills.summoning.Summoning;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;

public class ItemOnItem implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize, Stream stream) {

		int usedWithSlot = stream.readUnsignedWord();
		if (usedWithSlot < 0 || usedWithSlot > player.inventory.length) {
			return;
		}

		int itemUsedSlot = stream.readUnsignedWordA();
		if (itemUsedSlot < 0 || itemUsedSlot > player.inventory.length) {
			return;
		}

		int useWith = player.inventory[usedWithSlot] - 1;
		int itemUsed = player.inventory[itemUsedSlot] - 1;

		if (Summoning.onItemOnItem(player, new Item(useWith, player.inventoryN[usedWithSlot]), usedWithSlot, new Item(itemUsed, player.inventoryN[itemUsedSlot]), itemUsedSlot)) {
			return;
		}

		if (useWith > itemUsed) {
			if (ServerEvents.itemOnItem(player, itemUsed, itemUsedSlot, useWith, usedWithSlot)) {
				return;
			}
		} else {
			if (ServerEvents.itemOnItem(player, useWith, usedWithSlot, itemUsed, itemUsedSlot)) {
				return;
			}
		}

		UseItem.ItemonItem(player, itemUsed, useWith);
	}
}
