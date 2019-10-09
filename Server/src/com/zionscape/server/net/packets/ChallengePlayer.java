package com.zionscape.server.net.packets;

import com.zionscape.server.Config;
import com.zionscape.server.model.content.minigames.DuelArena;
import com.zionscape.server.model.content.minigames.gambling.Gambling;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;

/**
 * Challenge Player
 */
public class ChallengePlayer implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {

		int answerPlayer = stream.readUnsignedWord();

		if (answerPlayer < 0 || answerPlayer > Config.MAX_PLAYERS) {
			return;
		}

		Player other = PlayerHandler.players[answerPlayer];

		if(other == null) {
			return;
		}

		if(Gambling.inArea(c) && Gambling.inArea(other)) {
			Gambling.request(c, other);
			return;
		}

		if (c.getLocation().isWithinInteractionDistance(other.getLocation())) {
			DuelArena.requestDuel(c, answerPlayer);
		} else {
			c.duelRequestId = answerPlayer;
		}
	}

}