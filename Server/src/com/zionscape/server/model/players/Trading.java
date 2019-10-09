package com.zionscape.server.model.players;

import com.zionscape.server.Config;
import com.zionscape.server.model.content.grandexchange.GrandExchange;
import com.zionscape.server.model.content.minigames.gambling.Gambling;
import com.zionscape.server.model.content.minigames.quests.TheStolenCannon;
import com.zionscape.server.model.content.minigames.zombies.Zombies;
import com.zionscape.server.model.items.GameItem;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.util.Misc;

import java.util.concurrent.CopyOnWriteArrayList;

public class Trading {

	/**
	 * Trading
	 */
	public CopyOnWriteArrayList<GameItem> offeredItems = new CopyOnWriteArrayList<GameItem>();
	private Player c;

	public Trading(Player Player) {
		this.c = Player;
	}

	public static boolean onClickingButtons(Player player, int button) {
		switch (button) {
			case 13092:
				Player ot = PlayerHandler.players[player.tradeWith];
				if (ot == null) {
					player.getTradeAndDuel().declineTrade();
					player.sendMessage("Trade declined as the other player has disconnected.");
					return true;
				}
				if (ot.playerId == player.playerId) {
					return true;
				}

				if (player.openInterfaceId != 3323) {
					player.getTradeAndDuel().declineTrade();
					return true;
				}
				if (ot.openInterfaceId != 3323) {
					player.getTradeAndDuel().declineTrade();
					return true;
				}
				if (!ot.getLocation().isWithinInteractionDistance(player.getLocation())) {
					player.getTradeAndDuel().declineTrade();
					return true;
				}


				player.getPA().sendFrame126("Waiting for other player...", 3431);
				ot.getPA().sendFrame126("Other player has accepted", 3431);
				player.goodTrade = true;
				ot.goodTrade = true;
				for (GameItem item : player.getTradeAndDuel().offeredItems) {
					if (item.id > 0) {
						if (ot.getItems().freeInventorySlots() < player.getTradeAndDuel().offeredItems.size()) {
							player.sendMessage(ot.username + " only has " + ot.getItems().freeInventorySlots()
									+ " free slots, please remove "
									+ (player.getTradeAndDuel().offeredItems.size() - ot.getItems().freeInventorySlots()) + " items.");
							ot.sendMessage(player.username + " has to remove "
									+ (player.getTradeAndDuel().offeredItems.size() - ot.getItems().freeInventorySlots())
									+ " items or you could offer them "
									+ (player.getTradeAndDuel().offeredItems.size() - ot.getItems().freeInventorySlots()) + " items.");
							player.goodTrade = false;
							ot.goodTrade = false;
							player.getPA().sendFrame126("Not enough inventory space...", 3431);
							ot.getPA().sendFrame126("Not enough inventory space...", 3431);
							break;
						} else {
							player.getPA().sendFrame126("Waiting for other player...", 3431);
							ot.getPA().sendFrame126("Other player has accepted", 3431);
							player.goodTrade = true;
							ot.goodTrade = true;
						}
					}
				}
				if (player.inTrade && !player.tradeConfirmed && ot.goodTrade && player.goodTrade) {
					player.tradeConfirmed = true;
					if (ot.tradeConfirmed) {
						player.getTradeAndDuel().confirmScreen();
						ot.getTradeAndDuel().confirmScreen();
						return true;
					}
				}
				break;
			case 13218:
				player.tradeAccepted = true;
				player.tradeDelay = System.currentTimeMillis();
				Player ot1 = PlayerHandler.players[player.tradeWith];
				if (ot1 == null) {
					player.getTradeAndDuel().declineTrade();
					player.sendMessage("Trade declined as the other player has disconnected.");
					return true;
				}
				if (ot1.playerId == player.playerId) {
					return true;
				}

				if (player.openInterfaceId != 3443) {
					player.getTradeAndDuel().declineTrade();
					return true;
				}
				if (ot1.openInterfaceId != 3443) {
					player.getTradeAndDuel().declineTrade();
					return true;
				}
				if (!ot1.getLocation().isWithinInteractionDistance(player.getLocation())) {
					player.getTradeAndDuel().declineTrade();
					return true;
				}

				if (player.inTrade && player.tradeConfirmed && ot1.tradeConfirmed && !player.tradeConfirmed2) {
					player.tradeConfirmed2 = true;
					if (ot1.tradeConfirmed2) {
						player.acceptedTrade = true;
						ot1.acceptedTrade = true;

						player.getData().totalCompleteTrades++;
						ot1.getData().totalCompleteTrades++;

						player.getTradeAndDuel().giveItems();
						ot1.getTradeAndDuel().giveItems();
						break;
					}
					ot1.getPA().sendFrame126("Other player has accepted.", 3535);
					player.getPA().sendFrame126("Waiting for other player...", 3535);
				}
				return true;
		}
		return false;
	}

	public void requestTrade(int id) {
		// c.sendMessage("Disabled for now, patching a dupe.");
		// c.sendMessage("Trade is disabled due to dupes: Will fix 30th of october");
		try {
			Player o = PlayerHandler.players[id];
			if (o == null) {
				return;
			}
			if (c.inWild()) {
				return;
			}
			/*
			 * if (o.clan != null && o.clan.getWar() != null) { if (o.clan.getWar().inWarArea(o)) { return; } }
			 */
			if (id == c.playerId) {
				return;
			}

			if(Gambling.inStake(c)) {
				return;
			}

			if (c.ironman && o.rights < 3) {
				c.sendMessage("You cannot do this on an ironman account.");
				return;
			}

			if (GrandExchange.usingGrandExchange(c)) {
				return;
			}

			if (GrandExchange.usingGrandExchange(o)) {
				return;
			}

			if(Zombies.inGameArea(c.getLocation()) || Zombies.inLobbyArea(c.getLocation())) {
				return;
			}

			/*
			 * if(c.inNoTrade()) { enjoying?
			 * c.sendMessage("You cannot trade at home anymore, teleport to the market area to trade!"); return; }
			 */
			c.tradeWith = id;
			// if (c.connectedFrom.equals(o.connectedFrom) || o.connectedFrom.equals(c.connectedFrom)) {
			// c.sendMessage("You cannot trade to the same ip. Nice try cheater!");
			// return;
			// }
			if (c.tradeTimer > 0) {
				c.sendMessage("You must wait 15 minutes from the creation of your account to trade.");
				return;
			}
			if (o.tradeTimer > 0) {
				c.sendMessage("Player must wait 15 minutes from the creation of the account to trade.");
				return;
			}
			// if(c.absX >= 3079 && c.absX <= 3100 && c.absY >= 3481 && c.absY <= 3501) {
			// c.sendMessage("Trade at the marketplace, not home!");
			// return;
			// }
			c.tradeWith = id;
			if (!c.inTrade && o.tradeRequested && o.tradeWith == c.playerId) {
				c.getTradeAndDuel().openTrade();
				o.getTradeAndDuel().openTrade();
			} else if (!c.inTrade) {
				c.tradeRequested = true;
				c.sendMessage("Sending trade request...");
				o.sendMessage(c.username + ":tradereq:");
			}
		} catch (Exception e) {
			System.out.println("Error requesting trade.");
			e.printStackTrace();
		}
	}

	public void openTrade() {
		Player o = PlayerHandler.players[c.tradeWith];
		if (o == null) {
			return;
		}

		c.inTrade = true;
		c.canOffer = true;
		c.tradeStatus = 1;
		c.tradeRequested = false;
		c.getItems().resetItems(3322);
		this.resetTItems(3415);
		this.resetOTItems(3416);
		String out = o.username;
		c.getPA().sendFrame126(
				"Trading with: " + o.username + " who has @gre@" + o.getItems().freeInventorySlots() + " free slots", 3417);
		c.getPA().sendFrame126("", 3431);
		c.getPA().sendFrame126("Are you sure you want to make this trade?", 3535);
		c.getPA().sendFrame248(3323, 3321);
	}

	public void resetTItems(int WriteFrame) {
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(WriteFrame);
		int len = offeredItems.toArray().length;
		int current = 0;
		c.getOutStream().writeWord(len);
		for (GameItem item : offeredItems) {
			if (item.amount > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord_v2(item.amount);
			} else {
				c.getOutStream().writeByte(item.amount);
			}
			c.getOutStream().writeWordBigEndianA(item.id + 1);
			current++;
		}
		if (current < 27) {
			for (int i = current; i < 28; i++) {
				c.getOutStream().writeByte(1);
				c.getOutStream().writeWordBigEndianA(-1);
			}
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
	}

	public boolean fromTrade(int itemID, int fromSlot, int amount) {
		Player o = PlayerHandler.players[c.tradeWith];
		if (o == null) {
			return false;
		}
		if (o.tradeWith != c.playerId) {
			return false;
		}
		if (c.openInterfaceId != 3323) {
			c.getTradeAndDuel().declineTrade();
			return true;
		}
		if (o.openInterfaceId != 3323) {
			o.getTradeAndDuel().declineTrade();
			return true;
		}
		for (GameItem item : offeredItems) {
			if (item == null) {
				continue;
			}
			if (item.id == itemID) {
				if (item.amount < amount) {
					amount = item.amount;
				}
			}
		}
		try {
			if (!c.inTrade || !c.canOffer) {
				this.declineTrade();
				return false;
			}
			c.tradeConfirmed = false;
			o.tradeConfirmed = false;
			if (!ItemUtility.isStackable(itemID)) {
				if (amount > 28) {
					amount = 28;
				}
				for (int a = 0; a < amount; a++) {
					for (GameItem item : offeredItems) {
						if (item.id == itemID) {
							if (!item.stackable) {
								offeredItems.remove(item);
								c.getItems().addItem(itemID, 1);
							} else {
								if (item.amount > amount) {
									item.amount -= amount;
									c.getItems().addItem(itemID, amount);
								} else {
									amount = item.amount;
									offeredItems.remove(item);
									c.getItems().addItem(itemID, amount);
								}
							}
							break;
						}
					}
				}
			}
			for (GameItem item : offeredItems) {
				if (item.id == itemID) {
					if (!item.stackable) {
					} else {
						if (item.amount > amount) {
							item.amount -= amount;
							c.getItems().addItem(itemID, amount);
						} else {
							amount = item.amount;
							offeredItems.remove(item);
							c.getItems().addItem(itemID, amount);
						}
					}
					break;
				}
			}
			o.getPA().sendFrame126(
					"Trading with: " + c.username + " who has @gre@" + c.getItems().freeInventorySlots() + " free slots", 3417);
			c.tradeConfirmed = false;
			o.tradeConfirmed = false;
			c.getItems().resetItems(3322);
			this.resetTItems(3415);
			o.getTradeAndDuel().resetOTItems(3416);
			c.getPA().sendFrame126("", 3431);
			o.getPA().sendFrame126("", 3431);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean tradeItem(int itemID, int fromSlot, int amount) {
		Player o = PlayerHandler.players[c.tradeWith];
		if (o == null) {
			return false;
		}
		if (o.tradeWith != c.playerId) {
			return false;
		}
		if (amount == 0) {
			this.declineTrade();
			return false;
		}
		if (c.openInterfaceId != 3323) {
			c.getTradeAndDuel().declineTrade();
			return true;
		}
		if (o.openInterfaceId != 3323) {
			o.getTradeAndDuel().declineTrade();
			return true;
		}

		if (ItemUtility.getName(itemID).toLowerCase().contains("clue scroll")) {
			c.sendMessage("You can't trade this item.");
			return false;
		}

		if (!c.serverOwner()) {
			for (int i : Config.UNTRADABLE_ITEM_IDS) {
				if (i == itemID) {
					c.sendMessage("You can't trade this item.");
					return false;
				}
			}
		}

		if (itemID >= 6 && itemID <= 12 && c.theStolenCannonStatus != TheStolenCannon.COMPLETED_STAGE) {
			c.sendMessage("You can't trade this item without completing The Stolen Cannon quest");
			return false;
		}

		c.tradeConfirmed = false;
		o.tradeConfirmed = false;

		if (c.getItems().getItemCount(itemID) < amount) {
			amount = c.getItems().getItemCount(itemID);
			if (amount == 0) {
				return false;
			}
		}

		for (GameItem item : offeredItems) {
			if (item.id == itemID && (long) amount + (long) item.amount > Integer.MAX_VALUE) {
				amount = Integer.MAX_VALUE - item.amount;
				c.sendMessage("You may only trade a max of " + Misc.formatNumber(Integer.MAX_VALUE) + " gp.");
			}
		}

		if (amount == 0) {
			return false;
		}

		if (!ItemUtility.isStackable(itemID) && !ItemUtility.isNote(itemID)) {
			if (amount > 28) {
				amount = 28;
			}
			for (int a = 0; a < amount; a++) {
				if (c.getItems().playerHasItem(itemID, 1)) {
					offeredItems.add(new GameItem(itemID, 1));
					c.getItems().deleteItem(itemID, c.getItems().getItemSlot(itemID), 1);
					o.getPA()
							.sendFrame126(
									"Trading with: " + c.username + " who has @gre@" + c.getItems().freeInventorySlots()
											+ " free slots", 3417);
				}
			}
			o.getPA().sendFrame126(
					"Trading with: " + c.username + " who has @gre@" + c.getItems().freeInventorySlots() + " free slots", 3417);
			c.getItems().resetItems(3322);
			this.resetTItems(3415);
			o.getTradeAndDuel().resetOTItems(3416);
			c.getPA().sendFrame126("", 3431);
			o.getPA().sendFrame126("", 3431);
		}
		if (!c.inTrade || !c.canOffer) {
			this.declineTrade();
			return false;
		}
		if (ItemUtility.isStackable(itemID) || ItemUtility.isNote(itemID)) {
			boolean inTrade = false;
			for (GameItem item : offeredItems) {
				if (item.id == itemID) {
					inTrade = true;
					item.amount += amount;
					c.getItems().deleteItem(itemID, amount);
					o.getPA()
							.sendFrame126(
									"Trading with: " + c.username + " who has @gre@" + c.getItems().freeInventorySlots()
											+ " free slots", 3417);
					break;
				}
			}
			if (!inTrade) {
				offeredItems.add(new GameItem(itemID, amount));
				c.getItems().deleteItem(itemID, amount);
				o.getPA().sendFrame126(
						"Trading with: " + c.username + " who has @gre@" + c.getItems().freeInventorySlots() + " free slots",
						3417);
			}
		}
		o.getPA().sendFrame126(
				"Trading with: " + c.username + " who has @gre@" + c.getItems().freeInventorySlots() + " free slots", 3417);
		c.getItems().resetItems(3322);
		this.resetTItems(3415);
		o.getTradeAndDuel().resetOTItems(3416);
		c.getPA().sendFrame126("", 3431);
		o.getPA().sendFrame126("", 3431);
		return true;
	}

	public void resetTrade() {
		offeredItems.clear();
		c.inTrade = false;
		c.tradeWith = 0;
		c.canOffer = true;
		c.tradeConfirmed = false;
		c.tradeConfirmed2 = false;
		c.acceptedTrade = false;
		c.getPA().removeAllWindows();
		c.tradeResetNeeded = false;
		c.getPA().sendFrame126("Are you sure you want to make this trade?", 3535);
	}

	public void declineTrade() {
		if (!c.acceptedTrade) {
			c.tradeStatus = 0;
			this.declineTrade(true);
		} else {
			System.out.println("DUPE: " + c.username);
		}
	}

	public void declineTrade(boolean tellOther) {
		c.getPA().removeAllWindows();
		Player o = PlayerHandler.players[c.tradeWith];
		if (o == null) {
			return;
		}
		if (o.tradeWith != c.playerId) {
			return;
		}
		if (tellOther) {
			o.getTradeAndDuel().declineTrade(false);
			o.getTradeAndDuel().c.getPA().removeAllWindows();
		}
		for (GameItem item : offeredItems) {
			if (item.amount < 1) {
				continue;
			}
			if (item.stackable) {
				c.getItems().addItem(item.id, item.amount);
			} else {
				for (int i = 0; i < item.amount; i++) {
					c.getItems().addItem(item.id, 1);
				}
			}
		}
		this.resetTrade();
	}

	@SuppressWarnings("static-access")
	public void resetOTItems(int WriteFrame) {
		synchronized (c) {
			Player o = PlayerHandler.players[c.tradeWith];
			if (o == null) {
				return;
			}
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(WriteFrame);
			int len = o.getTradeAndDuel().offeredItems.toArray().length;
			int current = 0;
			c.getOutStream().writeWord(len);
			for (GameItem item : o.getTradeAndDuel().offeredItems) {
				if (item.amount > 254) {
					c.getOutStream().writeByte(255); // item's stack count. if over 254, write byte 255
					c.getOutStream().writeDWord_v2(item.amount);
				} else {
					c.getOutStream().writeByte(item.amount);
				}
				c.getOutStream().writeWordBigEndianA(item.id + 1); // item id
				current++;
			}
			if (current < 27) {
				for (int i = current; i < 28; i++) {
					c.getOutStream().writeByte(1);
					c.getOutStream().writeWordBigEndianA(-1);
				}
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	public void confirmScreen() {
		Player o = PlayerHandler.players[c.tradeWith];
		if (o == null) {
			return;
		}
		if (o.tradeWith != c.playerId) {
			return;
		}

		c.getPA().sendFrame126(o.username, 3451);

		c.canOffer = false;
		c.getItems().resetItems(3214);
		String SendTrade = "Absolutely nothing!";
		String SendAmount = "";
		int Count = 0;
		for (GameItem item : offeredItems) {
			if (item.id > 0) {
				if (item.amount >= 1000 && item.amount < 1000000) {
					SendAmount = "@cya@" + (item.amount / 1000) + "K @whi@(" + Misc.format(item.amount) + ")";
				} else if (item.amount >= 1000000) {
					SendAmount = "@gre@" + (item.amount / 1000000) + " million @whi@(" + Misc.format(item.amount) + ")";
				} else {
					SendAmount = "" + Misc.format(item.amount);
				}
				if (Count == 0) {
					SendTrade = c.getItems().getItemName(item.id);
				} else {
					SendTrade = SendTrade + "\\n" + c.getItems().getItemName(item.id);
				}
				if (item.stackable) {
					SendTrade = SendTrade + " x " + SendAmount;
				}
				Count++;
			}
		}
		c.getPA().sendFrame126(SendTrade, 3557);
		SendTrade = "Absolutely nothing!";
		SendAmount = "";
		Count = 0;
		for (GameItem item : o.getTradeAndDuel().offeredItems) {
			if (item.id > 0) {
				if (item.amount >= 1000 && item.amount < 1000000) {
					SendAmount = "@cya@" + (item.amount / 1000) + "K @whi@(" + Misc.format(item.amount) + ")";
				} else if (item.amount >= 1000000) {
					SendAmount = "@gre@" + (item.amount / 1000000) + " million @whi@(" + Misc.format(item.amount) + ")";
				} else {
					SendAmount = "" + Misc.format(item.amount);
				}
				if (Count == 0) {
					SendTrade = c.getItems().getItemName(item.id);
				} else {
					SendTrade = SendTrade + "\\n" + c.getItems().getItemName(item.id);
				}
				if (item.stackable) {
					SendTrade = SendTrade + " x " + SendAmount;
				}
				Count++;
			}
		}
		c.getPA().sendFrame126(SendTrade, 3558);
		// TODO: find out what 197 does eee 3213
		c.getPA().sendFrame248(3443, 197);
	}

	public void giveItems() {
		Player o = PlayerHandler.players[c.tradeWith];
		if (o == null) {
			return;
		}
		try {
			for (GameItem item : o.getTradeAndDuel().offeredItems) {
				if (item.id > 0) {
					c.getItems().addItem(item.id, item.amount);
					c.getTradeLog().tradeReceived(c.getItems().getItemName(item.id), item.amount);
					o.getTradeLog().tradeGive(c.getItems().getItemName(item.id), item.amount);
				}
			}
			c.getPA().removeAllWindows();
			c.tradeResetNeeded = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}