package com.zionscape.server.net.packets;

import com.zionscape.server.ServerEvents;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.scripting.Scripting;
import com.zionscape.server.scripting.messages.impl.DialogueContinueClickedMessage;
import com.zionscape.server.util.Stream;

/**
 * Dialogue
 */
public class Dialogue implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize, Stream stream) {
		if (Scripting.handleMessage(player, new DialogueContinueClickedMessage())) {
			return;
		}
		if (ServerEvents.onDialogueContinue(player)) {
			return;
		}
		if (player.nextChat > 0) {
			player.getDH().sendDialogues(player.nextChat, player.talkingNpc);
		} else {
			player.getDH().sendDialogues(0, -1);
		}
	}

}