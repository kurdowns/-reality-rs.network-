package com.zionscape.server.model.clan;

import com.zionscape.server.Server;
import com.zionscape.server.model.content.minigames.clanwars.War;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.util.Misc;

import java.util.*;

/**
 * This class stores all information about the clan. This includes active members, banned members, ranked members and
 * their ranks, clan title, and clan founder. All clan joining, leaving, and moderation/setup is also handled in this
 * class.
 *
 * @author Galkon
 */
public class Clan {

	/**
	 * The title of the clan.
	 */
	public String title;
	/**
	 * The founder of the clan.
	 */
	public String founder;
	/**
	 * The active clan members.
	 */
	public List<Player> activeMembers = new LinkedList<>();
	/**
	 * The banned members.
	 */
	public List<String> bannedMembers = new LinkedList<>();
	/**
	 * The ranked clan members.
	 */
	public List<String> rankedMembers = new LinkedList<>();
	/**
	 * The clan member ranks.
	 */
	public List<Integer> ranks = new LinkedList<>();
	/**
	 * The ranks privileges require (joining, talking, kicking, banning).
	 */
	public int whoCanJoin = Rank.ANYONE;
	public int whoCanTalk = Rank.ANYONE;
	public int whoCanKick = Rank.GENERAL;
	public int whoCanBan = Rank.OWNER;
	/**
	 *
	 */
	private War war = null;

	/**
	 * Creates a new clan for the specified player.
	 *
	 * @param player
	 */
	public Clan(Player player) {
		this.setTitle(player.username + "'s Clan");
		this.setFounder(player.username.toLowerCase());
	}

	/**
	 * Creates a new clan for the specified title and founder.
	 *
	 * @param title
	 * @param founder
	 */
	public Clan(String title, String founder) {
		this.setTitle(title);
		this.setFounder(founder);
	}
	/**
	 * Formats player name for protocol
	 */
	public static String formatPlayerNameForProtocol(String name) {
		if (name == null)
			return "";
		name = name.replaceAll(" ", "_");
		name = name.toLowerCase();
		return name;
	}
	
	/**
	 * Adds a member into the clan on starter.
	 * 
	 * @param player
	 * @param ownerName
	 */

	public static void addMember(String ownerName, Player player) {
			String formatedName = formatPlayerNameForProtocol(ownerName);
			if (player.clan != null)
				return;
			Clan clan = Server.clanManager.getClan(formatedName);
			if (clan == null)
				return;
			if (clan.isBanned(Misc.formatPlayerName(player.username))) {
				player.sendMessage("<col=FF0000>You are currently banned from this clan chat.</col>");
				return;
			}
			if (clan.whoCanJoin > Rank.ANYONE && !clan.isFounder(player.username)) {
				if(clan.getRank(player.username) < clan.whoCanJoin) {
					player.sendMessage("Only " + clan.getRankTitle(clan.whoCanJoin) + "s+ may join this chat.");
					return;
				}
			}

			if (clan.getFounder().equalsIgnoreCase("Help") && clan.getRank(player.username) == Rank.ANYONE) {
				if (player.getData().minutesOnline > 30) {
					player.sendMessage(
							"<col=FF0000>You have played for over 20 hours. Join the clan chat: 'Help' or any other cc!</col>");
					return;
				}
			}

			player.clan = clan;
			player.getData().lastClanChat = clan.getFounder();
			clan.activeMembers.add(player);
			player.getPA().sendFrame126("Leave chat", 18135);
			player.getPA().sendFrame126("Talking in: <col=FFFF64>" + clan.getTitle() + "</col>", 18139);
			player.getPA().sendFrame126("Owner: <col=FFFFFF>" + Misc.formatPlayerName(clan.getFounder()) + "</col>",
					18140);
			player.sendMessage("Now talking in clan chat <col=FFFF64><shad=0>" + clan.getTitle() + "</shad></col>.");
			player.sendMessage("To talk, start each line of chat with the / symbol.");

			// updateInterface(player, null);

			clan.updateMembers();
	}

	/**
	 * Adds a member to the clan.
	 *
	 * @param player
	 */
	public void addMember(Player player) {

		// make sure the player is not already in the clan
		if(activeMembers.contains(player)) {
			return;
		}

		if (this.isBanned(Misc.formatPlayerName(player.username))) {
			player.sendMessage("<col=FF0000>You are currently banned from this clan chat.</col>");
			return;
		}
		if (whoCanJoin > Rank.ANYONE && !this.isFounder(player.username)) {
			if (this.getRank(player.username) < whoCanJoin) {
				player.sendMessage("Only " + this.getRankTitle(whoCanJoin) + "s+ may join this chat.");
				return;
			}
		}

		if (getFounder().equalsIgnoreCase("Help") && getRank(player.username) == Rank.ANYONE) {
			if (player.getData().minutesOnline > 30) {
				player.sendMessage("<col=FF0000>You have played for over 20 hours. Join the clan chat: 'Help' or any other cc!</col>");
				return;
			}
		}

		player.clan = this;
		player.getData().lastClanChat = this.getFounder();
		activeMembers.add(player);
		player.getPA().sendFrame126("Leave chat", 18135);
		player.getPA().sendFrame126("Talking in: <col=FFFF64>" + this.getTitle() + "</col>", 18139);
		player.getPA().sendFrame126("Owner: <col=FFFFFF>" + Misc.formatPlayerName(this.getFounder()) + "</col>", 18140);
		player.sendMessage("Now talking in clan chat <col=FFFF64><shad=0>" + this.getTitle() + "</shad></col>.");
		player.sendMessage("To talk, start each line of chat with the / symbol.");

		//updateInterface(player, null);

		this.updateMembers();
	}

	/**
	 * Bans the name from entering the clan chat.
	 *
	 * @param name
	 */
	public void banMember(String name) {
		if (bannedMembers.contains(name)) {
			return;
		}
		if (name.equalsIgnoreCase(this.getFounder())) {
			return;
		}
		bannedMembers.add(name);
		this.save();
		Player player = PlayerHandler.getPlayer(name);
		if (player != null && player.clan == this) {
			this.removeMember(player);
			player.sendMessage("You have been banned from the clan chat.");
		}
		this.sendMessage(Misc.formatPlayerName(name) + " has been banned from the clan chat.");
	}

	/**
	 * Can they ban?
	 *
	 * @param name
	 * @return
	 */
	public boolean canBan(String name) {
		if (this.isFounder(name)) {
			return true;
		}
		if (this.getRank(name) >= whoCanBan) {
			return true;
		}
		return false;
	}

	/**
	 * Can they kick?
	 *
	 * @param name
	 * @return
	 */
	public boolean canKick(String name) {
		if (this.isFounder(name)) {
			return true;
		}
		if (this.getRank(name) >= whoCanKick) {
			return true;
		}
		return false;
	}

	/**
	 * Deletes the clan.
	 */
	public void delete() {
		for (Player player: activeMembers) {
			this.removeMember(player);
			if (player == null) {
				continue;
			}
			player.sendMessage("The clan you were in has been deleted.");
		}
		Server.clanManager.delete(this);
	}

	/**
	 * Demotes the specified name.
	 *
	 * @param name
	 */
	public void demote(String name) {
		if (!rankedMembers.contains(name)) {
			return;
		}
		int index = rankedMembers.indexOf(name);
		rankedMembers.remove(index);
		ranks.remove(index);
		this.save();
	}

	/**
	 * Gets the founder of the clan.
	 *
	 * @return
	 */
	public String getFounder() {
		return founder;
	}

	/**
	 * Sets the founder.
	 *
	 * @param founder
	 */
	public void setFounder(String founder) {
		this.founder = founder;
	}

	/**
	 * Gets the rank of the specified name.
	 *
	 * @param name
	 * @return
	 */
	public int getRank(String name) {
		name = Misc.formatPlayerName(name);
		if (rankedMembers.contains(name)) {
			return ranks.get(rankedMembers.indexOf(name));
		}
		if (this.isFounder(name)) {
			return Rank.OWNER;
		}

		// todo disabled for now
		/*if (PlayerSave.isFriend(this.getFounder(), name)) {
			return Rank.FRIEND;
        }*/

		return Rank.ANYONE;
	}

	/**
	 * Gets the rank title as a string.
	 *
	 * @param rank
	 * @return
	 */
	public String getRankTitle(int rank) {
		switch (rank) {
			case 0:
				return "Anyone";
			case 1:
				return "Friend";
			case 2:
				return "Recruit";
			case 3:
				return "Corporal";
			case 4:
				return "Sergeant";
			case 5:
				return "Lieutenant";
			case 6:
				return "Captain";
			case 7:
				return "General";
			case 8:
				return "Only Me";
		}
		return "";
	}

	/**
	 * Gets the title of the clan.
	 *
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title
	 * @return
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns whether or not the specified name is banned.
	 *
	 * @param name
	 * @return
	 */
	public boolean isBanned(String name) {
		if (bannedMembers.contains(name) || bannedMembers.contains(name.toLowerCase())) {
			return true;
		}
		return false;
	}

	/**
	 * Returns whether or not the specified name is the founder.
	 *
	 * @param name
	 * @return
	 */
	public boolean isFounder(String name) {
		return getFounder().equalsIgnoreCase(name);
	}

	/**
	 * Returns whether or not the specified name is a ranked user.
	 *
	 * @param name
	 * @return
	 */
	public boolean isRanked(String name) {
		return rankedMembers.contains(Misc.formatPlayerName(name));
	}

	/**
	 * Kicks the name from the clan chat.
	 *
	 * @param name
	 */
	public void kickMember(String name) {
		if (name.equalsIgnoreCase(getFounder())) {
			return;
		}
		Player player = PlayerHandler.getPlayer(name);
		if (player != null) {
			this.removeMember(player);
			player.sendMessage("You have been kicked from the clan chat.");
		}
		this.sendMessage(Misc.formatPlayerName(name) + " has been kicked from the clan chat.");
	}

	/**
	 * Removes the player from the clan.
	 *
	 * @param player
	 */
	public void removeMember(Player player) {
		if(activeMembers.contains(player)) {
			player.clan = null;
			resetInterface(player);
			activeMembers.remove(player);

			this.updateMembers();
		}
	}

	/**
	 * Resets the clan interface.
	 *
	 * @param player
	 */
	public void resetInterface(Player player) {
		player.getPA().sendFrame126("Join chat", 18135);
		player.getPA().sendFrame126("Talking in: Not in chat", 18139);
		player.getPA().sendFrame126("Owner: None", 18140);
		for (int index = 0; index < 100; index++) {
			player.getPA().sendFrame126("", 18144 + index);
		}
	}

	/**
	 * Saves the clan.
	 */
	public void save() {
		Server.clanManager.save(this);
		this.updateMembers();
	}

	/**
	 * Sends a message to the clan.
	 *
	 * @param player
	 * @param message
	 */
	public void sendChat(Player player, String message) {
		if (this.getRank(player.username) < whoCanTalk) {
			player.sendMessage("Only " + this.getRankTitle(whoCanTalk) + "s+ may talk in this chat.");
			return;
		}
		for(Player p : activeMembers) {
			if (p != null) {
				p.getPA().sendClan(player.username, message, this.getTitle(), Misc.getChatIcon(player), player.elite);
			}
		}
	}

	/**
	 * Sends a message to the clan.
	 *
	 * @param player
	 * @param message
	 */
	public void sendMessage(String message) {
		for(Player plr : activeMembers) {
			plr.sendMessage(message);
		}
	}

	/**
	 * Sets the rank for the specified name.
	 *
	 * @param name
	 * @param rank
	 */
	public void setRank(String name, int rank) {
		if (rankedMembers.contains(name)) {
			ranks.set(rankedMembers.indexOf(name), rank);
		} else {
			rankedMembers.add(name);
			ranks.add(rank);
		}
		this.save();
	}

	/**
	 * Sets the minimum rank that can ban.
	 *
	 * @param rank
	 */
	public void setRankCanBan(int rank) {
		whoCanBan = rank;
	}

	/**
	 * Sets the minimum rank that can join.
	 *
	 * @param rank
	 */
	public void setRankCanJoin(int rank) {
		whoCanJoin = rank;
	}

	/**
	 * Sets the minimum rank that can kick.
	 *
	 * @param rank
	 */
	public void setRankCanKick(int rank) {
		whoCanKick = rank;
	}

	/**
	 * Sets the minimum rank that can talk.
	 *
	 * @param rank
	 */
	public void setRankCanTalk(int rank) {
		whoCanTalk = rank;
	}

	/**
	 * Unbans the name from the clan chat.
	 *
	 * @param name
	 */
	public void unbanMember(String name) {
		if (bannedMembers.contains(name)) {
			bannedMembers.remove(name);
			this.save();
		}
	}

	/**
	 * Updates the members on the interface for the player.
	 *
	 * @param player
	 */
	public void updateInterface(Player player, List<String> list) {
		player.getPA().sendFrame126("Talking in: <col=FFFF64>" + this.getTitle() + "</col>", 18139);
		player.getPA().sendFrame126("Owner: <col=FFFFFF>" + Misc.formatPlayerName(this.getFounder()) + "</col>", 18140);

		if(list == null) {
			list = calculateClanList();
		}
		for(int i = 0; i < list.size(); i++) {
			String username = list.get(i);
			if(username.length() > 0) {
				player.getPA().sendFrame126("<clan=" + getRank(username) + ">" + Misc.formatPlayerName(username), 18144 + i);
			} else {
				player.getPA().sendFrame126("", 18144 + i);
			}
		}
	}

	public List<String> calculateClanList() {
		List<String> list = new ArrayList<>();
		for (int index = 0; index < 100; index++) {
			if (index < activeMembers.size()) {
				list.add(activeMembers.get(index).username);
			}
		}

		// sort alphabetically
		Collections.sort(list);

		// add the bank spaces back in
		for (int index = 0; index < 100; index++) {
			if (index >= activeMembers.size()) {
				list.add("");
			}
		}

		return list;
	}

	public boolean updateMembers;

	/**
	 * Updates the interface for all members.
	 */
	public void updateMembers() {
		updateMembers = true;
	}

	public void sendUpdateInterface() {
		List<String> usernames = calculateClanList();

		Iterator<Player> itr = activeMembers.iterator();
		while(itr.hasNext()) {
			Player player = itr.next();
			if(player == null || player.getOutStream() == null) {
				itr.remove();
				continue;
			}
			updateInterface(player, usernames);
		}
	}

	/**
	 *
	 * @return
	 */
	public War getWar() {
		return war;
	}

	/**
	 *
	 * @param war
	 */
	public void setWar(War war) {
		this.war = war;
	}

	/**
	 * The clan ranks.
	 *
	 * @author Galkon
	 */
	public static class Rank {
		public static final int ANYONE = 0;
		public static final int FRIEND = 1;
		public static final int RECRUIT = 2;
		public static final int CORPORAL = 3;
		public static final int SERGEANT = 4;
		public static final int LIEUTENANT = 5;
		public static final int CAPTAIN = 6;
		public static final int GENERAL = 7;
		public static final int OWNER = 8;
	}

}
