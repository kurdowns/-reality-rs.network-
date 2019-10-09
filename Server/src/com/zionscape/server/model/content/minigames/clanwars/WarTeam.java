package com.zionscape.server.model.content.minigames.clanwars;

import com.zionscape.server.model.clan.Clan;
import com.zionscape.server.model.players.Player;

import java.util.ArrayList;
import java.util.List;

public class WarTeam {

	private Clan clan;
	private Player owner;
	private boolean accepted;
	private List<Player> players = new ArrayList<>();
	private int kills;

	public WarTeam(Player owner, Clan clan) {
		this.clan = clan;
		this.owner = owner;

		players.add(owner);
	}

	public Clan getClan() {
		return clan;
	}

	public Player getOwner() {
		return owner;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public int getKills() {
		return kills;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public boolean contains(Player player) {
		return players.contains(player);
	}

	public void incrementKills() {
		kills++;
	}

}