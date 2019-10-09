package com.zionscape.server.net.packets;

import com.zionscape.server.model.content.Resting;
import com.zionscape.server.model.content.minigames.DuelArena;
import com.zionscape.server.model.content.minigames.gambling.Gambling;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;

/**
 * Follow Player
 */
public class FollowPlayer implements PacketType {

	@SuppressWarnings("static-access")
	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {
		int followPlayer = stream.readUnsignedWordBigEndian();
		if (followPlayer > PlayerHandler.players.length || followPlayer < 0) {
			return;
		}
		if (PlayerHandler.players[followPlayer] == null) {
			return;
		}

		if(c.attributeExists("gamble_stake")) {
			return;
		}

		Player target = PlayerHandler.players[followPlayer];
		if (c.getDuel() == null && target.getDuel() != null && target.getDuel().getStage() == DuelArena.Stage.FIGHTING)
			return;

		Resting.stop(c);
		c.playerIndex = 0;
		c.npcIndex = 0;
		c.mageFollow = false;
		c.usingBow = false;
		c.usingRangeWeapon = false;
		c.followId = followPlayer;
	}
}
