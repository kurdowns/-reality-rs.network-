package com.zionscape.server.net.packets;

import com.google.common.base.MoreObjects;
import com.zionscape.server.model.content.minigames.castlewars.CastleWars;
import com.zionscape.server.model.items.UseItem;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.skills.summoning.Summoning;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;

public class ItemOnNpc implements PacketType {

	@SuppressWarnings("static-access")
	@Override
	public void processPacket(Player player, int packetType, int packetSize, Stream stream) {
		int itemId = stream.readSignedWordA();
		int i = stream.readSignedWordA();
		int slot = stream.readSignedWordBigEndian();

		if (i < 0 || i > NPCHandler.npcs.length) {
			return;
		}

		if(slot < 0 || slot > player.inventory.length - 1) {
			return;
		}

		NPC npc = NPCHandler.npcs[i];
		if (npc == null) {
			return;
		}

		if(itemId != player.inventory[slot] - 1) {
			return;
		}

		if(player.rights >= 3) {
			player.sendMessage(MoreObjects.toStringHelper(ItemOnNpc.class).add("item", itemId).add("npc index", i).add("npc type", npc.type).add("slot", slot).toString());
		}

		int npcId = npc.type;

		if (Summoning.onItemOnNpc(player, itemId, npc)) {
			return;
		}
		if (CastleWars.onItemOnNPC(player, npc, itemId)) {
			return;
		}

		UseItem.ItemonNpc(player, itemId, npcId, slot);
	}

}