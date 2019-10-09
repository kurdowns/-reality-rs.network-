package com.zionscape.server.model.players;


public class SaveRequest implements Runnable {

	private Player player;
	private boolean logout;

	public SaveRequest(Player player, boolean logout) {
		this.player = player;
		this.logout = logout;
	}

	public void SaveRequest(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		PlayerSave.saveGame(player);

		if (this.logout) {
			PlayerHandler.addPlayerToDestructQueue(player);
		}
	}

}