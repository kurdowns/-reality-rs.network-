package com.zionscape.server.net.packets;

import com.zionscape.server.Config;
import com.zionscape.server.model.content.minigames.DuelArena;
import com.zionscape.server.model.content.minigames.gambling.Gambling;
import com.zionscape.server.model.items.GameItem;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.impl.Interface;
import com.zionscape.server.model.players.skills.summoning.Summoning;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;
import com.zionscape.server.world.shops.Shops;

/**
 * Bank All Items
 */
public class BankAll implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {
		int removeSlot = stream.readUnsignedWordA();
		int interfaceId = stream.readUnsignedWord();
		int removeId = stream.readUnsignedWordA();

		if (c.rights >= 3) {
			c.sendMessage("bank all: " + interfaceId + " " + removeSlot + " " + removeId);
		}

		switch (interfaceId) {
			case 19356:
				Gambling.stakeItem(c, removeId, c.inventoryN[removeSlot]);
				break;
			case 19353:
				Gambling.removeItem(c, removeId, Integer.MAX_VALUE);
				return;
			case 52710:
			case 51710:
				Summoning.onMakeItem(c, interfaceId, removeId, Integer.MAX_VALUE, removeSlot);
				break;
			case 3900:
				Shops.buyItem(c, removeId, 10, removeSlot);
				break;
			case 3823:
				Shops.sellItem(c, removeId, 10, removeSlot);
				break;
			case 2700:
				if (c.storingFamiliarItems) {
					Summoning.withdrawItem(c, removeId, Integer.MAX_VALUE, removeSlot);
					return;
				}
				break;
			case 5064:
				if (c.inTrade) {
					return;
				}
				if (c.storingFamiliarItems) {
					Summoning.depositItem(c, removeId, Integer.MAX_VALUE, removeSlot);
					return;
				}
				c.getBank().depositItem(removeId, Config.MAXITEM_AMOUNT, removeSlot);
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
				c.getBank().withdrawItem(removeId, Config.MAXITEM_AMOUNT, removeSlot, interfaceId);
				break;
			case 3322:
				if (c.getDuel() != null && c.getDuel().getStage() == DuelArena.Stage.STAKING) {
					if (ItemUtility.isStackable(removeId) || ItemUtility.isNote(removeId)) {
						DuelArena.stakeItem(c, removeId, c.inventoryN[removeSlot], removeSlot);
					} else {
						DuelArena.stakeItem(c, removeId, 28, removeSlot);
					}
				} else {
					if (ItemUtility.isStackable(removeId)) {
						c.getTradeAndDuel().tradeItem(removeId, removeSlot, c.inventoryN[removeSlot]);
					} else {
						c.getTradeAndDuel().tradeItem(removeId, removeSlot, 28);
					}
				}
				break;
			case 3415:
				if (c.getDuel() != null && c.getDuel().getStage() == DuelArena.Stage.STAKING) {

				} else {
					if (ItemUtility.isStackable(removeId)) {
						for (GameItem item : c.getTradeAndDuel().offeredItems) {
							if (item.id == removeId) {
								c.getTradeAndDuel().fromTrade(removeId, removeSlot,
										c.getTradeAndDuel().offeredItems.get(removeSlot).amount);
							}
						}
					} else {
						for (GameItem item : c.getTradeAndDuel().offeredItems) {
							if (item.id == removeId) {
								c.getTradeAndDuel().fromTrade(removeId, removeSlot, 28);
							}
						}
					}
				}
				break;
			case 6669:
				if (c.getDuel() != null && c.getDuel().getStage() == DuelArena.Stage.STAKING) {
					DuelArena.removeItem(c, removeId, Integer.MAX_VALUE, removeSlot);
				}
				break;
		}
	}
}
