package com.zionscape.server.net.packets;

import com.google.common.base.MoreObjects;
import com.zionscape.server.Config;
import com.zionscape.server.ServerEvents;
import com.zionscape.server.model.content.Tanner;
import com.zionscape.server.model.content.minigames.DuelArena;
import com.zionscape.server.model.content.minigames.gambling.Gambling;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.skills.fletching.Fletching;
import com.zionscape.server.model.players.skills.smithing.Smithing;
import com.zionscape.server.model.players.skills.summoning.Summoning;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;
import com.zionscape.server.world.shops.Shops;

/**
 * Bank X Items
 */
public class BankX2 implements PacketType {

	private static void resetXValues(Player player) {
		player.xRemoveSlot = 0;
		player.xInterfaceId = 0;
		player.xRemoveId = 0;
	}

	@Override
	public void processPacket(Player player, int packetType, int packetSize, Stream stream) {
		int amount = stream.readDWord();
		if (amount <= 0 || amount > Integer.MAX_VALUE) {
			resetXValues(player);
			return;
		}

		if (player.rights == 3) {
			player.sendMessage(MoreObjects.toStringHelper(BankX2.class).add("interface id", player.xInterfaceId).add("amount", amount).toString());
		}

		if (ServerEvents.onXAmountEntered(player, player.xInterfaceId, amount)) {
			resetXValues(player);
			return;
		}

		if(player.attributeExists("ironman_unoting")) {
			int playerAmount = player.getItems().getItemAmount(player.xInterfaceId);

			if(amount > playerAmount) {
				amount = playerAmount;
			}

			if(amount <= 0) {
				return;
			}

			int unoted = ItemUtility.getUnotedId(player.xInterfaceId);

			// just to double check
			if(unoted == -1) {
				return;
			}

			if (player.getItems().freeInventorySlots() == 0 && amount != 1) {
				player.sendMessage("Not enough space in your inventory.");
				return;
			}

			if (amount != player.getItems().freeInventorySlots() + 1 && amount > player.getItems().freeInventorySlots()) {
				amount = player.getItems().freeInventorySlots();

				player.sendMessage("Not enough space in your inventory.");
			}

			player.getItems().deleteItem(player.xInterfaceId, amount);
			player.getItems().addItem(unoted, amount);
			player.removeAttribute("ironman_unoting");

			return;
		}

		/*
		 * if(c.username.equalsIgnoreCase("tylers pur3")) System.out.println("Xamount:  " + Xamount); if (Xamount <
		 * 0)//this should work fine Xamount = c.getItems().getItemAmount(c.xRemoveId); if (Xamount == 0) Xamount = 1;
		 */
		if (player.buyingX) {

			if (amount > Config.MAX_ALLOWED_AMOUNT_TO_BUY_FROM_SHOPS) {
				amount = Config.MAX_ALLOWED_AMOUNT_TO_BUY_FROM_SHOPS;
			}

			Shops.buyItem(player, player.xRemoveId, amount, player.xRemoveSlot);

			resetXValues(player);
			player.buyingX = false;
		}
		switch (player.xInterfaceId) {

			case 19356:
				Gambling.stakeItem(player, player.xRemoveId, amount);
				break;
			case 19353:
				Gambling.removeItem(player, player.xRemoveId, amount);
				return;

			case 9110: // smithing buy x
			case 15148:
			case 15152:
			case 15156:
			case 15160:
			case 16062:
			case 29018:
			case 29023:
				Smithing.startSmelting(player, player.xInterfaceId, amount, false);
				break;

			case 3823: // shops selling
				Shops.sellItem(player, player.xRemoveId, amount, -1);
				break;

			case 34182:
				Fletching.startFletching(player, amount, "shortbow");
				break;
			case 34186:
				if (player.getAttribute("logId") != "1511") {
					Fletching.startFletching(player, amount, "shaft");
				}
				break;
			case 34190:
				Fletching.startFletching(player, amount, "longbow");
				break;

			case 52710:
			case 51710:
				Summoning.onMakeItem(player, player.xInterfaceId, player.xRemoveId, amount, player.xRemoveSlot);
				break;
			case 57209:
				Tanner.exchangeHide(player, 1739, 1741, amount, Tanner.SOFT_LEATHER_COST);
				break;
			case 57210:
				Tanner.exchangeHide(player, 1739, 1741, amount, Tanner.HARD_LEATHER_COST);
				break;
			case 57211:
				Tanner.exchangeHide(player, 6287, 6289, amount, Tanner.SNAKE_LEATHER_COST);
				break;
			case 57212:
				Tanner.exchangeHide(player, 6287, 6289, amount, Tanner.SNAKE_LEATHER_COST);
				break;
			case 57213:
				Tanner.exchangeHide(player, 1753, 1745, amount, Tanner.DRAGON_LEATHER_COST);
				break;
			case 57214:
				Tanner.exchangeHide(player, 1751, 2505, amount, Tanner.DRAGON_LEATHER_COST);
				break;
			case 57215:
				Tanner.exchangeHide(player, 1749, 2507, amount, Tanner.DRAGON_LEATHER_COST);
				break;
			case 57216:
				Tanner.exchangeHide(player, 1747, 2509, amount, Tanner.DRAGON_LEATHER_COST);
				break;
			case 2700:
				if (player.storingFamiliarItems) {
					Summoning.withdrawItem(player, player.xRemoveId, amount, player.xRemoveSlot);
					break;
				}
				break;
			case 5064:
				if (player.inTrade) {
					break;
				}
				if (player.storingFamiliarItems) {
					Summoning.depositItem(player, player.xRemoveId, amount, player.xRemoveSlot);
					break;
				}
				player.getBank().depositItem(player.xRemoveId, amount, player.xRemoveSlot);
				break;
			case 15292:
			case 15293:
			case 15294:
			case 15295:
			case 15296:
			case 15297:
			case 15298:
			case 15299:
			case 15300:
			case 15302:
				player.getBank().withdrawItem(player.xRemoveId, amount, player.xRemoveSlot, player.xInterfaceId);
				break;
			case 3322:
				if (!player.getItems().playerHasItem(player.xRemoveId, amount))
					break;
				if (player.getDuel() != null && player.getDuel().getStage() == DuelArena.Stage.STAKING) {
					DuelArena.stakeItem(player, player.xRemoveId, amount, player.xRemoveSlot);
				} else {
					player.getTradeAndDuel().tradeItem(player.xRemoveId, player.xRemoveSlot, amount);
				}
				break;
			case 3415:
				if (player.getDuel() == null || player.getDuel().getStage() != null
						&& player.getDuel().getStage() != DuelArena.Stage.STAKING) {
					player.getTradeAndDuel().fromTrade(player.xRemoveId, player.xRemoveSlot, amount);
				}
				break;
			case 6669:
				if (!player.getItems().playerHasItem(player.xRemoveId, amount))
					break;
				if (player.getDuel() != null && player.getDuel().getStage() == DuelArena.Stage.STAKING) {
					DuelArena.removeItem(player, player.xRemoveId, amount, player.xRemoveSlot);
				}
				break;
		}

		resetXValues(player);
	}

}