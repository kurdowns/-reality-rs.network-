package com.zionscape.server.net.packets;

import com.zionscape.server.model.content.minigames.DuelArena;
import com.zionscape.server.model.content.minigames.gambling.Gambling;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.skills.summoning.Summoning;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;
import com.zionscape.server.world.shops.Shops;

/**
 * Bank 5 Items
 */
public class Bank5 implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {
		int interfaceId = stream.readSignedWordBigEndianA();
		int removeId = stream.readSignedWordBigEndianA();
		int removeSlot = stream.readSignedWordBigEndian();

		if (c.rights >= 3) {
			c.sendMessage("bank 5: " + interfaceId + " " + removeSlot + " " + removeId);
		}

		switch (interfaceId) {

			case 19356:
				Gambling.stakeItem(c, removeId, 5);
				break;
			case 19353:
				Gambling.removeItem(c, removeId, 5);
				return;

			case 52710:
			case 51710:
				Summoning.onMakeItem(c, interfaceId, removeId, 5, removeSlot);
				break;
			case 3900:
				Shops.buyItem(c, removeId, 1, removeSlot);
				break;
			case 3823:
				Shops.sellItem(c, removeId, 1, removeSlot);
				break;
			case 2700:
				if (c.storingFamiliarItems) {
					Summoning.withdrawItem(c, removeId, 5, removeSlot);
					return;
				}
				break;
			case 5064:
				if (c.inTrade) {
					return;
				}
				if (c.storingFamiliarItems) {
					Summoning.depositItem(c, removeId, 5, removeSlot);
					return;
				}
				c.getBank().depositItem(removeId, 5, removeSlot);
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
				c.getBank().withdrawItem(removeId, 5, removeSlot, interfaceId);
				break;
			case 3322:
				if (c.getDuel() != null && c.getDuel().getStage() == DuelArena.Stage.STAKING) {
					DuelArena.stakeItem(c, removeId, 5, removeSlot);
				} else {
					c.getTradeAndDuel().tradeItem(removeId, removeSlot, 5);
				}
				break;
			case 3415:
				if(c.attributeExists("gamble_stake")) {
					Gambling.removeItem(c, removeId, 5);
				} else if (c.getDuel() == null || c.getDuel().getStage() != null
						&& c.getDuel().getStage() != DuelArena.Stage.STAKING) {
					c.getTradeAndDuel().fromTrade(removeId, removeSlot, 5);
				}
				break;
			case 6669:
				if (c.getDuel() != null && c.getDuel().getStage() == DuelArena.Stage.STAKING) {
					DuelArena.removeItem(c, removeId, 5, removeSlot);
				}
				break;
			case 1119:
			case 1120:
			case 1121:
			case 1122:
			case 1123:
				c.getSmithing().readInput(c.level[PlayerConstants.SMITHING], Integer.toString(removeId), c, 5);
				break;
		}
	}
}
