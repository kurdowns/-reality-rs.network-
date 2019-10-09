package com.zionscape.server.net.packets;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;

/**
 * Chat : Credits to Coder Alex account
 */
public class Chat implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {

		if (c.muted) {
			return;
		}

		/*
		 * if (Connection.isMuted(c)) { c.sendMessage("You're currently muted."); return; }
		 */

		int eff = 0, color = 0;
		try {
			eff = stream.readUnsignedByteS();
			color = stream.readUnsignedByteS();
		} catch (Exception e) {
			return;
		}
		if (color > 11 || color < 0 || eff > 5 || eff < 0) {
			return;
		}

		byte[] buff = new byte[packetSize - 2];


		stream.readBytes_reverseA(buff, buff.length, 0);

		c.setChatTextEffects(eff);
		c.setChatTextColor(color);
		c.setChatTextSize((byte) buff.length);
		c.setChatText(buff);
		/*
		 * for (int i : instaMute) { if(term.contains(instaMute[i])) { c.said++;
		 * c.sendMessage("Please, do not use that here."); return; } }
		 */
		if (c.inVote()) {
			c.sendMessage("You can't speak in here!");
		}

		c.setChatTextUpdateRequired(true);
		c.updateRequired = true;
	}

}