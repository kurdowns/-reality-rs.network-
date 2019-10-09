package com.zionscape.server.net.packets;

import com.zionscape.server.ServerEvents;
import com.zionscape.server.model.content.minigames.DuelArena;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;
import com.zionscape.server.world.shops.Shops;

/**
 * Clicking stuff (interfaces)
 */
public class ClickingStuff implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {
		// Nope bro its not even being called by the client.
		// This packet handles when you click a button which is meant to close a window.
		// System.out.println("CLOSED WINDOW");

		if (ServerEvents.onClickingStuff(c)) {
			return;
		}

		if (c.inTrade) { // This was your trade declining it should work....
			if (!c.acceptedTrade) { // OMICRON TRADE EDIT
				System.out.println("trade reset");
				c.getTradeAndDuel().declineTrade();
			}
		}

		if (c.getDuel() != null && c.getDuel().getStage() == DuelArena.Stage.STAKING || c.getDuel() != null
				&& c.getDuel().getStage() == DuelArena.Stage.CONFIRMING_STAKE) {
			DuelArena.declineDuel(c);
		}

		if (c.isBanking) {
			c.isBanking = false;
		}

		Shops.close(c);

	}
}
