package com.zionscape.server.net.packets;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.chat.Chat;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;

/**
 * Private messages, friends etc
 */
public class PrivateMessaging implements PacketType {

	public final int ADD_FRIEND = 188, SEND_PM = 126, REMOVE_FRIEND = 215, CHANGE_PM_STATUS = 95, REMOVE_IGNORE = 74,
			ADD_IGNORE = 133;

	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {
		switch (packetType) {
			case ADD_FRIEND:
				long friendToAdd = stream.readQWord();
				c.getChat().addFriend(friendToAdd);
				break;
			case REMOVE_FRIEND:
				long friendToRemove = stream.readQWord();
				c.getChat().removeFriend(friendToRemove);
				break;
			case SEND_PM:
				if (c.muted) {
					return;
				}

				long sendMessageToFriendId = stream.readQWord();
				int pmchatTextSize = (byte) (packetSize - 8);
				byte[] pmchatText = new byte[pmchatTextSize];
				stream.readBytes(pmchatText, pmchatTextSize, 0);
				c.getChat().getPrivateMessaging().sendMessage(sendMessageToFriendId, pmchatText);
				break;
			case CHANGE_PM_STATUS:
				int publicChat = stream.readUnsignedByte();
				int privateChat = stream.readUnsignedByte();
				int tradeAndCompete = stream.readUnsignedByte();
				c.getChat().setTradeDisplay(Chat.Display.values()[tradeAndCompete]);
				c.getChat().setPrivateDisplay(Chat.Display.values()[privateChat]);
				c.getChat().setPublicDisplay(Chat.Display.values()[publicChat]);
				break;
			case ADD_IGNORE:
				long ignoreToAdd = stream.readQWord();
				c.getChat().addIgnore(ignoreToAdd);
				break;
			case REMOVE_IGNORE:
				long ignoreToRemove = stream.readQWord();
				c.getChat().removeIgnore(ignoreToRemove);
				break;
		}
	}
}
