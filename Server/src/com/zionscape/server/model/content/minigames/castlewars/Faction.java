package com.zionscape.server.model.content.minigames.castlewars;

import com.zionscape.server.model.Location;

public enum Faction {

	ZAMORAK(4042, 4515, Location.create(2421, 9524, 0), Location.create(2372, 3131, 1)), SARADOMIN(4041, 4513, Location
			.create(2377, 9485, 0), Location.create(2426, 3076, 1)), GUTHIX;

	private final int capeItemId;
	private final int hoodItemId;
	private final Location lobbyLocation;
	private final Location gameLobbyLocation;

	Faction() {
		capeItemId = -1;
		hoodItemId = -1;
		lobbyLocation = null;
		gameLobbyLocation = null;
	}

	Faction(int capeItemId, int hoodItemId, Location lobbyLocation, Location gameLobbyLocation) {
		this.capeItemId = capeItemId;
		this.hoodItemId = hoodItemId;
		this.lobbyLocation = lobbyLocation;
		this.gameLobbyLocation = gameLobbyLocation;
	}

	public int getCapeItemId() {
		return capeItemId;
	}

	public int getHoodItemId() {
		return hoodItemId;
	}

	public Location getLobbyLocation() {
		return lobbyLocation;
	}

	public Location getGameLobbyLocation() {
		return gameLobbyLocation;
	}

}