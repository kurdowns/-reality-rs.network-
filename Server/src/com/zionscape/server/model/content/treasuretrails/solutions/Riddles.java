package com.zionscape.server.model.content.treasuretrails.solutions;

import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.treasuretrails.Solution;
import com.zionscape.server.model.players.Player;

public enum Riddles implements Solution {

	AGGIE_DRAYNOR(
			"Aggie I see\nLonely and southern I feel\nI am neither inside nor outside\nthe house, yet no house would\nbe complete without me.\nYour treasure waits beneath me.",
			Location.create(3085, 3256, 0)), SMITHY_PORT_KHAZARD(
			"After trawling for bars, go to the nearest\nplace to smith them and dig by the door.", Location.create(
			2656, 3160, 0)), CLOTHES_SHOP_CANIFIS(
			"A town with a different sort of\nnight-life is your destination. Search for \nsome crates in one of the houses.",
			Location.create(3498, 3507, 0), 24911), KHAZARD_BATTLEFIELD(
			"Being this far north has meant that these \ncrates have escaped being battled over.", Location.create(
			2517, 3254, 0), 360), EDGEVILLE_YEW(
			"Come to the evil ledge.\nYew know yew want to.\nTry not to get stung.", Location.create(3088, 3468, 0)), MONASTRY_SOUTH_ARDY(
			"Find a crate close to the monks who like to\npaaarty.", Location.create(2617, 3204, 0), 37029), GENERAL_BENTNOZE(
			"Generally speaking, his nose was very bent", 4493), HAMID(
			"Identify the back of this over-acting brother.\n[He's a long way from home.]", 1008), EAST_FALADOR_EAST_BANK(
			"Look in the ground floor crates of houses in \nFalador.", Location.create(3029, 3354, 0), 35784), SWENSENS_HOUSE_RELLEKA(
			"Navigating to this crate will be a trial.", Location.create(2648, 3662, 0), 355);

	private final String riddle;
	private final Location location;
	private final int objectId;
	private final int npcId;

	Riddles(String riddle, Location location) {
		this.riddle = riddle;
		this.location = location;
		this.objectId = -1;
		this.npcId = -1;
	}

	Riddles(String riddle, int npcId) {
		this.riddle = riddle;
		this.npcId = npcId;
		this.objectId = -1;
		this.location = null;
	}

	Riddles(String riddle, Location location, int objectId) {
		this.riddle = riddle;
		this.location = location;
		this.objectId = objectId;
		this.npcId = -1;
	}

	@Override
	public void show(Player player) {

		for (int i = 6968; i < 6976; i++) {
			player.getPA().sendFrame126("", i);
		}

		if (!riddle.contains("\n")) {
			player.getPA().sendFrame126(riddle, 6968);
		} else {
			String[] lines = riddle.split("\n");
			for (int i = 0; i < lines.length; i++) {
				player.getPA().sendFrame126(lines[i], 6968 + i);
			}
		}

		player.getPA().showInterface(6965);
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public int getObjectId() {
		return objectId;
	}

	@Override
	public int getNpcId() {
		return npcId;
	}

}