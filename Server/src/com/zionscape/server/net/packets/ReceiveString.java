package com.zionscape.server.net.packets;

import com.zionscape.server.Server;
import com.zionscape.server.model.clan.Clan;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Misc;
import com.zionscape.server.util.Stream;

public class ReceiveString implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize, Stream stream) {
		String text = stream.readString();
		int index = text.indexOf(",");
		int id = Integer.parseInt(text.substring(0, index));
		String string = text.substring(index + 1);
		switch (id) {
			case 0:
				if (player.clan != null) {
					player.clan.removeMember(player);
					player.getData().lastClanChat = "";
				}
				break;
			case 1:
				if (string.isEmpty()) {
					break;
				} else if (string.length() > 15) {
					string = string.substring(0, 15);
				}
				Clan clan = player.getPA().getClan();
				if (clan == null) {
					Server.clanManager.create(player);
					clan = player.getPA().getClan();
				}
				if (clan != null) {
					clan.setTitle(string);
					player.getPA().sendFrame126(clan.getTitle(), 18306);
					clan.save();
				}
				break;
			case 2:
				if (string.isEmpty()) {
					break;
				} else if (string.length() > 12) {
					string = string.substring(0, 12);
				}
				if (string.equalsIgnoreCase(player.username)) {
					break;
				}
				clan = player.getPA().getClan();
				if (clan != null) {
					if (clan.isBanned(string)) {
						player.sendMessage("You cannot promote a banner member.");
						break;
					}
					clan.setRank(Misc.formatPlayerName(string), 1);
					player.getPA().setClanData();
					clan.save();
				}
				break;
			case 3:
				if (string.isEmpty()) {
					break;
				} else if (string.length() > 12) {
					string = string.substring(0, 12);
				}
				if (string.equalsIgnoreCase(player.username)) {
					break;
				}
				clan = player.clan;

				if (clan == null) {
					break;
				}

				if (clan.isRanked(string)) {
					player.sendMessage("You cannot ban a ranked member.");
					break;
				}

				if (!player.clan.canBan(player.username)) {
					player.sendMessage("You do not have sufficient privileges to do this.");
					break;
				}

				clan.banMember(Misc.formatPlayerName(string));
				player.getPA().setClanData();
				clan.save();
				break;
			default:
				System.out.println("Received string: identifier=" + id + ", string=" + string);
				break;
		}
	}
}