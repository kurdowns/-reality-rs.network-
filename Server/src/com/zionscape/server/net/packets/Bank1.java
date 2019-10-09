package com.zionscape.server.net.packets;

import com.zionscape.server.model.content.grandexchange.GrandExchange;
import com.zionscape.server.model.content.minigames.DuelArena;
import com.zionscape.server.model.content.minigames.gambling.Gambling;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.skills.summoning.Summoning;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;
import com.zionscape.server.world.shops.Shops;

/**
 * Remove Item
 */
public class Bank1 implements PacketType {

	@SuppressWarnings("unused")
	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {
		int interfaceId = stream.readUnsignedWordA();
		int removeSlot = stream.readUnsignedWordA();
		int removeId = stream.readUnsignedWordA();
		int shop = 0;
		int value = 0;

		if (c.rights >= 3) {
			c.sendMessage("removeitem: " + interfaceId + " " + removeSlot + " " + removeId);
		}

		GrandExchange.removeItem(c, interfaceId, removeId, removeSlot);

		String name = "null";
		switch (interfaceId) {

			case 19356:
				Gambling.stakeItem(c, removeId, 1);
				break;
			case 19353:
				Gambling.removeItem(c, removeId, 1);
				return;

			case 2700:
				if (c.storingFamiliarItems) {
					Summoning.withdrawItem(c, removeId, 1, removeSlot);
					return;
				}
				break;
			case 52710:
			case 51710:
				Summoning.onMakeItem(c, interfaceId, removeId, 1, removeSlot);
				break;
			case 1688:
				c.getItems().removeItem(removeId, removeSlot);
				break;
			case 5064:

				if (c.storingFamiliarItems) {
					Summoning.depositItem(c, removeId, 1, removeSlot);
					return;
				}

				c.getBank().depositItem(removeId, 1, removeSlot);
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
				c.getBank().withdrawItem(removeId, 1, removeSlot, interfaceId);
				break;
			case 3900:
				Shops.buyItemPrice(c, removeId);
				break;
			case 3823:
				Shops.sellItemPrice(c, removeId);
				break;
			case 3322:
				if (c.getDuel() != null && c.getDuel().getStage() == DuelArena.Stage.STAKING) {
					DuelArena.stakeItem(c, removeId, 1, removeSlot);
				} else {
					c.getTradeAndDuel().tradeItem(removeId, removeSlot, 1);
				}
				break;
			case 3415:
				if(c.attributeExists("gamble_stake")) {
					Gambling.removeItem(c, removeId, 1);
				} else if (c.getDuel() == null || c.getDuel().getStage() != null
						&& c.getDuel().getStage() != DuelArena.Stage.STAKING) {
					c.getTradeAndDuel().fromTrade(removeId, removeSlot, 1);
				}
				break;
			case 6669:
				if (c.getDuel() != null && c.getDuel().getStage() == DuelArena.Stage.STAKING) {
					DuelArena.removeItem(c, removeId, 1, removeSlot);
				}
				break;
			case 1119:
			case 1120:
			case 1121:
			case 1122:
			case 1123:
				c.getSmithing().readInput(c.level[PlayerConstants.SMITHING], Integer.toString(removeId), c, 1);
				break;
		}
	}
}
