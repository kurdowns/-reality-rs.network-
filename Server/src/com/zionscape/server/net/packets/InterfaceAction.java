package com.zionscape.server.net.packets;

import com.zionscape.server.model.clan.Clan;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;

public class InterfaceAction implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize, Stream stream) {
		int id = stream.readUnsignedWord();
		int action = stream.readUnsignedWord();

		if (player.isBanking) {
			player.getBank().interfaceAction(id, action);
			return;
		}

		switch (id) {
			case 18304:
				if (action == 1) {
					if (player.getPA().getClan() == null) {
						return;
					}
					player.getPA().getClan().delete();
					player.getPA().setClanData();
				}
				break;
			case 18307:
			case 18310:
			case 18313:
			case 18316:
				Clan clan = player.getPA().getClan();
				if (clan != null) {
					if (id == 18307) {
						clan.setRankCanJoin(action == 0 ? -1 : action);
					} else if (id == 18310) {
						clan.setRankCanTalk(action == 0 ? -1 : action);
					} else if (id == 18313) {
						clan.setRankCanKick(action == 0 ? -1 : action);
					} else if (id == 18316) {
						clan.setRankCanBan(action == 0 ? -1 : action);
					}
					String title = "";
					if (id == 18307) {
						title = clan.getRankTitle(clan.whoCanJoin)
								+ (clan.whoCanJoin > Clan.Rank.ANYONE && clan.whoCanJoin < Clan.Rank.OWNER ? "+" : "");
					} else if (id == 18310) {
						title = clan.getRankTitle(clan.whoCanTalk)
								+ (clan.whoCanTalk > Clan.Rank.ANYONE && clan.whoCanTalk < Clan.Rank.OWNER ? "+" : "");
					} else if (id == 18313) {
						title = clan.getRankTitle(clan.whoCanKick)
								+ (clan.whoCanKick > Clan.Rank.ANYONE && clan.whoCanKick < Clan.Rank.OWNER ? "+" : "");
					} else if (id == 18316) {
						title = clan.getRankTitle(clan.whoCanBan)
								+ (clan.whoCanBan > Clan.Rank.ANYONE && clan.whoCanBan < Clan.Rank.OWNER ? "+" : "");
					}

					player.getPA().sendFrame126(title, id + 2);
				}
				break;
			default:
				// System.out.println("Interface action: [id=" + id +",action=" + action +"]");
				break;
		}
		if (id >= 18323 && id < 18423) {
			Clan clan = player.getPA().getClan();
			if (clan != null && clan.rankedMembers != null && !clan.rankedMembers.isEmpty()) {
				String member = clan.rankedMembers.get(id - 18323);
				switch (action) {
					case 0:
						clan.demote(member);
						break;
					default:
						clan.setRank(member, action);
						break;
				}
				player.getPA().setClanData();
			}
		}
		if (id >= 18424 && id < 18524) {
			Clan clan = player.getPA().getClan();
			if (clan != null && clan.bannedMembers != null && !clan.bannedMembers.isEmpty()) {
				String member = clan.bannedMembers.get(id - 18424);
				switch (action) {
					case 0:
						clan.unbanMember(member);
						break;
				}
				player.getPA().setClanData();
			}
		}
		if (id >= 18144 && id < 18244) {
			for (int index = 0; index < 100; index++) {
				if (id == index + 18144) {
					String member = player.clan.activeMembers.get(id - 18144).username;
					switch (action) {
						case 0:
							if (player.clan.isFounder(player.username)) {
								player.getPA().showInterface(18300);
							}
							break;
						case 1:
							if (member.equalsIgnoreCase(player.username)) {
								player.sendMessage("You can't kick yourself!");
							} else {
								if (player.clan.canKick(player.username)) {
									player.clan.kickMember(member);
								} else {
									player.sendMessage("You do not have sufficient privileges to do this.");
								}
							}
							break;
						case 2:
							if (member.equalsIgnoreCase(player.username)) {
								player.sendMessage("You can't ban yourself!");
							} else {
								if (player.clan.canBan(player.username)) {
									player.clan.banMember(member);
									player.getPA().setClanData();
								} else {
									player.sendMessage("You do not have sufficient privileges to do this.");
								}
							}
							break;
					}
					break;
				}
			}
		}
	}


}