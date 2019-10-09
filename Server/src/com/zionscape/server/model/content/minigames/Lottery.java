package com.zionscape.server.model.content.minigames;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.util.Misc;

import java.io.*;
import java.util.ArrayList;

/**
 * @Author Tyler
 */
public class Lottery {

	private static final String DATA_LOTTERY_FUND_DATA = "./data/lottery/lottery.data";
	public static int lotteryFund = 0;
	public static String lastWinner;
	public ArrayList<String> lotteryPlayerNames = new ArrayList<String>(); // players (playername)
	public ArrayList<String> unclaimedWinners = new ArrayList<String>(); // Winners that havent claimed
	public int prizeAmount = 100; // jackpot amount (in millions)
	public int entryPrice = 5; // price to enter lottery (in millions)
	public int maximumEntryTimes = 5; // maximum times 1 player can enter per draw
	public long lastAnnouncment;
	public int announcmentFrequency = 10; // announcment frequency in mins

	public Lottery() { // constructor
		this.loadLists();
	}

	public void announceFund() {
		// int fund = lotteryFund / 1000000;
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				// Player all = PlayerHandler.players[j];
				// all.sendMessage("<shad=40960>[Lottery]<shad=-1> The Lottery is currently at " + fund +
				// "m. Enter by talking to Gambler in Edgeville");
			}
		}
	}

	public int checkEntriesCount(Player c) {
		int entries = 0;
		entries = 0;
		for (int indexes = 0; indexes < lotteryPlayerNames.size(); indexes++) {
			if (lotteryPlayerNames.get(indexes).equalsIgnoreCase("" + c.username)) {
				entries += 1;
			}
		}
		return entries;
	}

	public void checkUnclaimedWinners(Player c) {
		if (unclaimedWinners.contains(c.username)) {
			if (c.getItems().freeInventorySlots() > 0) {
				c.sendMessage("It's your lucky day! you have won the lottery");
				c.getItems().addItem(995, prizeAmount * 1000000);
				unclaimedWinners.remove(unclaimedWinners.indexOf(c.username));
				this.saveLists();
			} else {
				c.sendMessage("You have won the lottery but do not have space for the reward!");
			}
		}
	}

	public void drawLottery() {
		boolean prizeGiven = false;
		int arraySize = lotteryPlayerNames.size() - 1;
		int winnerIndex = Misc.random(arraySize);
		try {
			String winner = lotteryPlayerNames.get(winnerIndex);
			String player = winner;
			Player c = null;
			for (int i = 0; i < PlayerHandler.players.length; i++) {
				if (PlayerHandler.players[i] != null) {
					if (PlayerHandler.players[i].username.equalsIgnoreCase(player)) {
						c = PlayerHandler.players[i];
						c.sendMessage("You have won the lottery!");
						prizeGiven = true;
						if (c.getItems().freeInventorySlots() > 0) {
							c.getItems().addItem(995, prizeAmount * 1000000);
						} else {
							c.sendMessage("You do not have enough room in your inventory to claim your reward!");
							c.sendMessage("We will try to add your reward again when you next login.");
							unclaimedWinners.add(c.username);
						}
					}
				}
			}
			if (prizeGiven == false) {
				unclaimedWinners.add(winner);
				prizeGiven = true;
			}
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					Player all = PlayerHandler.players[j];
					all.sendMessage("<shad=40960>[Lottery]<shad=-1> The Lottery has been won by " + winner);
					lastWinner = winner;
				}
			}
		} catch (Exception e) {
			System.out.println("Lottery draw failed!");
		}
		lotteryFund = 0;
		lotteryPlayerNames.clear();
		prizeGiven = false;
		this.saveLists();
	}

	public void enterLottery(Player c) {
		if (this.checkEntriesCount(c) < maximumEntryTimes) {
			if (c.getItems().playerHasItem(995, entryPrice * 1000000)) {
				lotteryPlayerNames.add(c.username);
				lotteryFund += entryPrice * 1000000;
				c.getItems().deleteItem(995, entryPrice * 1000000);
				c.sendMessage("You have been entered into the lottery, when the lottery fund reaches 100m a winner");
				c.sendMessage("will be drawn");
			} else {
				c.sendMessage("You dont have enough cash!");
			}
		} else {
			c.sendMessage("You have already entered 5 times!");
		}
		this.saveLists();
	}

	@SuppressWarnings("unchecked")
	public void loadLists() {
		System.out.println("loadLists");
		File file = new File(DATA_LOTTERY_FUND_DATA);
		if (file.exists()) {
			try (FileInputStream fileInputStream = new FileInputStream(file);
				 ObjectInputStream ois = new ObjectInputStream(fileInputStream)) {
				lotteryFund = ois.readInt();
				lotteryPlayerNames = (ArrayList<String>) ois.readObject();
				unclaimedWinners = (ArrayList<String>) ois.readObject();
				lastWinner = ois.readUTF();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	// aleksandr update, save and load arrayList

	public void process() {
		if (lotteryFund >= prizeAmount * 1000000) {
			this.drawLottery();
		}
		if (System.currentTimeMillis() - lastAnnouncment > (1000 * 60 * announcmentFrequency)) {
			this.announceFund();
			lastAnnouncment = System.currentTimeMillis();
		}
	}

	public void saveLists() {
		System.out.println("saveLists");
		File file = new File(DATA_LOTTERY_FUND_DATA);
		file.getParentFile().mkdirs();
		try (FileOutputStream fileOutputStream = new FileOutputStream(file);
			 ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream);) {
			oos.writeInt(lotteryFund);
			oos.writeObject(lotteryPlayerNames);
			oos.writeObject(unclaimedWinners);
			oos.writeUTF(lastWinner == null ? "null" : lastWinner);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}