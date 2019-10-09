package com.zionscape.server.world.shops.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.world.shops.Currency;

public final class ZombieCurrency implements Currency {

	@Override
	public String getName() {
		return "Zombie Points";
	}

	@Override
	public void add(Player client, int amount) {
		client.getData().zombiePoints += amount;
	}

	@Override
	public int has(Player client) {
		return client.getData().zombiePoints;
	}

	@Override
	public void remove(Player client, int amount) {
		client.getData().zombiePoints -= amount;
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