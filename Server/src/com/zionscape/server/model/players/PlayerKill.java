package com.zionscape.server.model.players;

import java.util.Optional;

public class PlayerKill {

	private static final int NUM_MINS = 15;

	private final long createdAt;
	private final String username;
	private final String ip;
	private final String uid;

	public PlayerKill(String username, String ip, String uid) {
		this.createdAt = System.currentTimeMillis();
		this.username = username;
		this.ip = ip;
		this.uid = uid;
	}

	public static void process(Player player) {
		player.getData().kills.removeIf(x -> System.currentTimeMillis() - x.getCreatedAt() > (NUM_MINS * 60 * 1000));
	}

	public static boolean addKill(Player killer, Player opponent) {

		if(killer.connectedFrom.equals(opponent.connectedFrom)) {
			return false;
		}
		if(killer.uuid.equals(opponent.uuid)) {
			return false;
		}

		Optional<PlayerKill> optionalKill = killer.getData().kills.stream().filter(x -> x.getIp().equalsIgnoreCase(opponent.connectedFrom) || x.getUid().equalsIgnoreCase(opponent.uuid) || x.getUsername().equalsIgnoreCase(opponent.username)).findAny();

		if (optionalKill.isPresent()) {
			return false;
		}

		killer.getData().kills.add(new PlayerKill(opponent.username, opponent.connectedFrom, opponent.uuid));
		return true;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public String getUsername() {
		return username;
	}

	public String getIp() {
		return ip;
	}

	public String getUid() {
		return uid;
	}

}
