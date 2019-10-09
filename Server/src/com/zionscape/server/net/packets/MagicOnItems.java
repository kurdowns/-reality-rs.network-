package com.zionscape.server.net.packets;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;

/**
 * Magic on items
 */
public class MagicOnItems implements PacketType {

	@SuppressWarnings("unused")
	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {
		int slot = stream.readSignedWord();
		int itemId = stream.readSignedWordA();
		int junk = stream.readSignedWord();
		int spellId = stream.readSignedWordA();
		c.lampVfy = false;
		c.usingMagic = true;
		c.getPA().magicOnItems(slot, itemId, spellId);
		c.usingMagic = false;
	}
}
