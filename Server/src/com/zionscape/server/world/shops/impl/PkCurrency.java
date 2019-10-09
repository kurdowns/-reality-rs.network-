package com.zionscape.server.world.shops.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.world.shops.Currency;

public final class PkCurrency implements Currency {

	@Override
	public String getName() {
		return "PK Points";
	}

	@Override
	public void add(Player client, int amount) {
		client.getData().pkPoints += amount;
		client.getPA().sendFrame126("@or1@[@whi@" + client.getData().pkPoints + "@or1@] Pk Points", 54431);
	}

	@Override
	public int has(Player client) {
		return client.getData().pkPoints;
	}

	@Override
	public void remove(Player client, int amount) {
		client.getData().pkPoints -= amount;
		client.getPA().sendFrame126("@or1@[@whi@" + client.getData().pkPoints + "@or1@] Pk Points", 54431);
	}

	@Override
	public int sellValue(int itemId, boolean fullPrice) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int buyValue(int itemId) {
		throw new UnsupportedOperationException();
	}

}