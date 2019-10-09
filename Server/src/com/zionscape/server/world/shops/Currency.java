package com.zionscape.server.world.shops;

import com.zionscape.server.model.players.Player;

public interface Currency {

	/**
	 * @return
	 */
	public String getName();

	/**
	 * @param client
	 * @param amount
	 */
	public void add(Player client, int amount);

	/***
	 * @param client
	 * @return
	 */
	public int has(Player client);

	/**
	 * @param client
	 * @param amount
	 */
	public void remove(Player client, int amount);

	/**
	 * @param itemId
	 * @return
	 */
	public int sellValue(int itemId, boolean fullPrice);

	/**
	 * @param itemId
	 * @return
	 */
	public int buyValue(int itemId);

}