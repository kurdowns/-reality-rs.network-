package com.zionscape.server.net.packets;

/**
 * @author Ryan / Lmctruck30
 */

import com.google.common.base.MoreObjects;
import com.zionscape.server.Server;
import com.zionscape.server.events.impl.ItemOnObjectEvent;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.cannon.DwarfCannon;
import com.zionscape.server.model.content.minigames.castlewars.CastleWars;
import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.items.UseItem;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.skills.firemaking.Firemaking;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.scripting.Scripting;
import com.zionscape.server.scripting.messages.impl.ItemOnObjectMessage;
import com.zionscape.server.util.Stream;

public class ItemOnObject implements PacketType {

	@SuppressWarnings("unused")
	@Override
	public void processPacket(Player player, int packetType, int packetSize, Stream stream) {
		/*
		 * a = ? b = ?
		 */
		int a = stream.readUnsignedWord();
		int objectId = stream.readUnsignedWord();
		int objectY = stream.readSignedWordBigEndianA();
		int slot = stream.readSignedWordBigEndian();
		int objectX = stream.readSignedWordBigEndianA();
		int itemId = stream.readUnsignedWord();

		if(player.rights >= 3) {
			player.sendMessage(MoreObjects.toStringHelper(ItemOnObject.class).add("objectId", objectId).add("objectY", objectY).add("objectX", objectX).add("slot", slot).add("item", itemId).toString());
		}


		Location objectLocation = Location.create(objectX, objectY, player.heightLevel);

		if(!player.getLocation().isWithinInteractionDistance(objectLocation)) {
			return;
		}

		if (!player.getItems().playerHasItem(itemId, 1)) {
			return;
		}

		ItemOnObjectEvent event = new ItemOnObjectEvent(player, itemId, objectId, objectLocation);
		Server.getEventBus().post(event);
		if(event.isHandled()) {
			return;
		}

		if(Firemaking.onItemOnObject(player, itemId, objectId, objectLocation)) {
			return;
		}
		if (CastleWars.onItemOnObject(player, itemId, objectId, objectLocation)) {
			return;
		}
		if (DwarfCannon.itemOnObject(player, objectId, objectLocation, itemId)) {
			return;
		}

		if (Scripting.handleMessage(player, new ItemOnObjectMessage(objectId, objectLocation, new Item(itemId, player.inventoryN[slot]), slot))) {
			return;
		}

		UseItem.ItemonObject(player, objectId, objectX, objectY, itemId);
	}
}
