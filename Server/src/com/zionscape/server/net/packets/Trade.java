package com.zionscape.server.net.packets;

import com.zionscape.server.Config;
import com.zionscape.server.model.content.Resting;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;

/**
 * Trading
 */
public class Trade implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {
		int tradeId = stream.readSignedWordBigEndian();
		c.getPA().resetFollow();
		Resting.stop(c);
		c.lampVfy = false;
		if (tradeId > Config.MAX_PLAYERS || tradeId < 0) {
			return;
		}
		if (c.isDoingTutorial()) {
			return;
		}
		if (c.jailTimer > 1) {
			c.sendMessage("You can't trade in jail");
			return;
		}
		if (c.rights == 2 && !Config.ADMIN_CAN_TRADE) {
			c.sendMessage("Trading as an admin has been disabled.");
			return;
		}
		if (tradeId != c.playerId) {
			c.getTradeAndDuel().requestTrade(tradeId);
		}
	}
}
