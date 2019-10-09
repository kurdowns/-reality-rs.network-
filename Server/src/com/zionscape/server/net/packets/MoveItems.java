package com.zionscape.server.net.packets;

import com.google.common.base.MoreObjects;
import com.zionscape.server.model.content.minigames.DuelArena;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;

/**
 * Move Items
 */
public class MoveItems implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {
		int window = stream.readSignedWordBigEndianA();
		@SuppressWarnings("unused")
		boolean inserting = stream.readUnsignedByteC() == 1;
		int itemFrom = stream.readSignedWordBigEndianA();// slot1
		int itemTo = stream.readSignedWordBigEndian(); // slot2
		int toWindow = stream.readDWord();


		if (c.rights >= 3) {
			c.sendMessage(MoreObjects.toStringHelper(MoveItems.class)
					.add("window", window)
					.add("inserting", inserting)
					.add("itemFrom", itemFrom)
					.add("itemTo", itemTo)
					.add("toWindow", toWindow).toString());
		}

		if (c.getDuel() != null) {
			if (c.getDuel().getStage() == DuelArena.Stage.REQUESTING
					|| c.getDuel().getStage() == DuelArena.Stage.STAKING
					|| c.getDuel().getStage() == DuelArena.Stage.CONFIRMING_STAKE) {
				return;
			}
		}

		if (c.isBanking) {
			c.getBank().moveItems(itemFrom, itemTo, window, toWindow, inserting);
			return;
		}

		// c.sendMessage("junk: " + somejunk);
		c.lampVfy = false;
		c.getItems().moveItems(itemFrom, itemTo, window);
		if (c.inTrade) {
			c.getTradeAndDuel().declineTrade();
			return;
		}
		if (c.tradeStatus == 1) {
			c.getTradeAndDuel().declineTrade();
			return;
		}
	}
}
