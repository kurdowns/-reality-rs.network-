package com.zionscape.server.model.players;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

/**
 * TradeLog class
 *
 * @author Aintaro
 */
public class TradeLog {

	private Player c;

	public TradeLog(Player Player) {
		this.c = Player;
	}

	/**
	 * Will write what kind of item a player has traded with another player. MONTH = 0 = January DAY OF MONTH = 30 || 31
	 */
	@SuppressWarnings("static-access")
	public void tradeGive(String itemName, int itemAmount) {
		Player o = PlayerHandler.players[c.tradeWith];
		Calendar C = Calendar.getInstance();
		try (BufferedWriter bItem = new BufferedWriter(
				new FileWriter("./data/trades/gave/" + c.username + ".txt", true))) {
			bItem.newLine();
			bItem.write("Year : " + C.get(Calendar.YEAR) + "\tMonth : " + C.get(Calendar.MONTH) + "\tDay : "
					+ C.get(Calendar.DAY_OF_MONTH));
			bItem.newLine();
			bItem.write("Gave " + itemAmount + " " + itemName + " To " + o.username);
			bItem.newLine();
			bItem.write("--------------------------------------------------");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Will write what kind of item a player has received. MONTH = 0 = January DAY OF MONTH = 30 || 31
	 */
	public void tradeReceived(String itemName, int itemAmount) {
		Player o = PlayerHandler.players[c.tradeWith];
		Calendar C = Calendar.getInstance();
		try (BufferedWriter bItem = new BufferedWriter(new FileWriter("./data/trades/received/" + c.username + ".txt",
				true))) {
			try {
				bItem.newLine();
				bItem.write("Year : " + C.get(Calendar.YEAR) + "\tMonth : " + C.get(Calendar.MONTH) + "\tDay : "
						+ C.get(Calendar.DAY_OF_MONTH));
				bItem.newLine();
				bItem.write("Received " + itemAmount + " " + itemName + " From " + o.username);
				bItem.newLine();
				bItem.write("--------------------------------------------------");
			} finally {
				bItem.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}