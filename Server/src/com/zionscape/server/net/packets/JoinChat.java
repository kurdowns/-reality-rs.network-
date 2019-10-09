package com.zionscape.server.net.packets;

import com.zionscape.server.Server;
import com.zionscape.server.model.clan.Clan;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Misc;
import com.zionscape.server.util.Stream;

public class JoinChat implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize, Stream stream) {
		String owner = Misc.longToString(stream.readQWord()).replaceAll("_", " ");
		if (owner != null && !owner.isEmpty()) {
			if (player.clan == null) {
				Clan clan = Server.clanManager.getClan(owner);
				if (clan != null) {
					clan.addMember(player);
					Achievements.progressMade(player, Achievements.Types.JOIN_CLAN_CHAT);
				} else if (owner.equalsIgnoreCase(player.username)) {
					Server.clanManager.create(player);
					Achievements.progressMade(player, Achievements.Types.JOIN_CLAN_CHAT);
				} else {
					player.sendMessage(Misc.formatPlayerName(owner) + " has not created a clan yet.");
				}
			}
		}
	}
}
