package com.zionscape.server.model.content.minigames;

import com.zionscape.server.Config;
import com.zionscape.server.Server;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.tick.Tick;
import com.zionscape.server.util.Misc;
import com.zionscape.server.world.ItemDrops;

public class FightPits {

	public static int waitTimer = 120;
	public static int gameTimer, stake;
	public static boolean gameEndsEarly;
	public static Player c;
	private static boolean gameStarted = false;

	/**
	 * Start the game
	 */
	public static void startNewGame() {
		gameTimer = 50 * FightPits.inPitsRoomCount();
		Server.getTickManager().submit(new Tick(2) {

			@Override
			public void execute() {
				gameTimer--;
				if (gameTimer == 0 || FightPits.inPitsGameCount() < 5) {
					FightPits.endGame();
					this.stop();
				}
			}
		});
	}

	/*
	 * public static void startNewWait() { waitTimer = 33; World.getWorld().submit(new Tick(2) {
	 * 
	 * @Override public void execute() { if(waitTimer > 0) { waitTimer--; } if (waitTimer == 0 && !gameStarted) {
	 * startGame();//try this.stop(); } } });
	 * 
	 * }
	 */

	/**
	 * Start the game
	 */
	public static void startGame() {
		if (FightPits.inPitsRoomCount() < 5) {
			// startNewWait();
			waitTimer = 120;
			return;
		}
		stake = FightPits.inPitsRoomCount();
		FightPits.startNewGame();
		gameEndsEarly = false;
		gameStarted = true;
		for (int d = 0; d < Config.MAX_PLAYERS; d++) {
			if (PlayerHandler.players[d] != null && PlayerHandler.players[d].isActive) {
				Player temp = PlayerHandler.players[d];
				if (temp.inPitsWait()) {
					temp.getPA().movePlayer(2397 - Misc.random(5), 5158 - Misc.random(5), 0);
					temp.sendMessage("Fight to the Death!");
				}
			}
		}
	}

	/**
	 * End the game
	 */
	public static void endGame() {
		gameTimer = 0;
		int prize = FightPits.inPitsGameCount() > 0 ? stake / FightPits.inPitsGameCount() * 200 : 0;
		for (int d = 0; d < Config.MAX_PLAYERS; d++) {
			if (PlayerHandler.players[d] != null && PlayerHandler.players[d].isActive) {
				Player temp = PlayerHandler.players[d];
				if (temp.inPits() && !temp.isDead) {
					temp.sendMessage("You have survived the Fight Pits!");
					if (temp.getItems().freeInventorySlots() > 0 || temp.getItems().playerHasItem(6529)) {
						temp.getItems().addItem(6529, stake / FightPits.inPitsGameCount() * 200);
					} else {
						ItemDrops.createGroundItem(temp, 6529, 2399, 5177, prize, temp.playerId, false);
						temp.sendMessage("Not enough space in inventory, dropped tokkul on ground.");
					}
					temp.getPA().restorePlayer();
					temp.getPA().movePlayer(2399, 5177, 0);
					gameStarted = false;
				}
			}
		}
		// startNewWait();
		waitTimer = 120;
	}

	public static int inPitsGameCount() {
		int count = 0;
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			if (PlayerHandler.players[i] != null && !PlayerHandler.players[i].isDisconnected()) {
				if (PlayerHandler.players[i].absX > 2374 && PlayerHandler.players[i].absY > 5128
						&& PlayerHandler.players[i].absX < 2419 && PlayerHandler.players[i].absY < 5168) {
					count++;
				}
			}
		}
		return count;
	}

	public static int inPitsRoomCount() {
		int count = 0;
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			if (PlayerHandler.players[i] != null && !PlayerHandler.players[i].isDisconnected()) {
				if (PlayerHandler.players[i].absX > 2392 && PlayerHandler.players[i].absY > 5168
						&& PlayerHandler.players[i].absX < 2405 && PlayerHandler.players[i].absY < 5176) {
					count++;
				}
			}
		}
		return count;
	}

	public static void updateInterfaces(Player c) {
		if (c.inPitsWait()) {
			c.getPA().walkableInterface(27316);
			c.getPA().sendFrame126(gameTimer < 1 ? "@whi@Time till next game: " + waitTimer : "@whi@Game in progress",
					27318);
			c.getPA().sendFrame126(
					gameTimer < 1 ? "Players Waiting to Start: " + FightPits.inPitsRoomCount() : "Players Ingame: "
							+ FightPits.inPitsGameCount(), 27319);
		} else if (c.inPits()) {
			c.getPA().walkableInterface(27316);
			c.getPA().sendFrame126("@whi@Time Left: " + gameTimer, 27318);
			c.getPA().sendFrame126("Enemies Left: " + (FightPits.inPitsGameCount() - 1), 27319);
			/*
			 * } c.getPA().walkableInterface(2804); //c.getPA().sendFrame36(560, 1);
			 */
		}
	}

	/**
	 * Wait for game start
	 */
	public void process() {
		if (waitTimer > 0) {
			waitTimer--;
		}
		if (waitTimer == 0 && !gameStarted) {
			FightPits.startGame();// try
		}
	}
}