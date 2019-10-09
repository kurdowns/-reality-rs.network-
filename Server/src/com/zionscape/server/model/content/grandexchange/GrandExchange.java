package com.zionscape.server.model.content.grandexchange;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.zionscape.server.Config;
import com.zionscape.server.Server;
import com.zionscape.server.cache.clientref.ItemDef;
import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.items.ItemDefinition;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.players.Pins;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.util.CollectionUtil;
import com.zionscape.server.util.DatabaseUtil;
import com.zionscape.server.util.Misc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class GrandExchange {

	private static final int[] SUBMIT_SELL_IDS = {95191, 95206, 95209, 95212, 95215, 95218};
	private static final int BUY_SELL_ID = 24500;
	private static final int BUY_ID = 24600;
	private static final int SELL_ID = 24700;
	private static final int SELL_COLLECT_ID = 54700;
	private static final int BUY_COLLECT_ID = 53700;
	private static final int[] SUBMIT_BUY_IDS = {95185, 95203, 95194, 95188, 95197, 95200};
	private static final int[] COLLECT_IDS = {95223, 95227, 95231, 95235, 95239, 95243};
	public static boolean enabled = true;
	private static List<Offer> offers = new ArrayList<>();

	private static int cycle = 0;

	private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private static boolean writeOffers = false;

	public static void init() {
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime nextHour = now.plusHours(1).withMinute(0).withSecond(0);

		scheduler.scheduleAtFixedRate(() -> {
			// every 6 hours
			if ((ZonedDateTime.now().getHour() % 3) == 0) {
				try (Connection connection = DatabaseUtil.getConnection()) {
					try (PreparedStatement ps = connection.prepareStatement("SELECT *, ROUND(SUM(price) / COUNT(*)) as average FROM ge_history WHERE type = 'BOUGHT' and date > now() - interval 30 day GROUP BY item_id")) {
						try (PreparedStatement ps2 = connection.prepareStatement("UPDATE item_defs SET ge_value = ? WHERE id = ?")) {
							try (ResultSet results = ps.executeQuery()) {
								while (results.next()) {
									int average = results.getInt("average");
									Optional<ItemDefinition> optional = ItemDefinition.get(results.getInt("item_id"));
									if (optional.isPresent()) {
										ItemDefinition def = optional.get();

										if (def.getGeValue() != average) {
											def.setGeValue(average);

											ps2.setInt(1, average);
											ps2.setInt(2, def.getId());
											ps2.addBatch();
										}
									}
								}
							}
							ps2.executeBatch();
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}, Duration.between(now, nextHour).getSeconds(), (60 * 60), TimeUnit.SECONDS);
	}

	public static void process(boolean override) {

		if (!override && cycle++ < 8) {
			return;
		}

		cycle = 0;

		List<Offer> offerUpdates = offers.stream().filter(Offer::requiresUpdate).collect(Collectors.toList());

		for (Offer offer : offerUpdates) {

			// were all ready complete or aborted
			if (offer.isComplete()) {
				offer.setRequiresUpdate(false);
				continue;
			}

			// if the type is buy check other offers to see if we can complete this offer
			if (offer.getType() == Type.BUY) {
				List<Offer> sellUpdates = offers.stream().filter(x -> offer.getItemId() == x.getItemId() && x.getType() == Type.SELL && x.getPlayerId() != offer.getPlayerId()).collect(Collectors.toList());

				Collections.sort(sellUpdates, new Comparator<Offer>() {
					@Override
					public int compare(Offer o1, Offer o2) {

						if (o1.getPrice() > o2.getPrice()) {
							return 1;
						}

						if (o1.getPrice() < o2.getPrice()) {
							return -1;
						}

						return 0;
					}
				});

				for (Offer sellOffer : sellUpdates) {

					// check if aborted
					if (sellOffer.isComplete()) {
						continue;
					}

					// if the buy price is smaller than the sell offer price
					if (offer.getPrice() < sellOffer.getPrice()) {
						continue;
					}

					int required = offer.getQuantity() - offer.getReceived();
					int amountOffered = sellOffer.getQuantity() - sellOffer.getReceived();

					if (required == 0 || amountOffered == 0) {
						System.out.println("BUY OFFER - GE SOMETHING WENT WRONG " + required + " " + amountOffered + " " + offer.getPlayerId() + " " + sellOffer.getPlayerId());
						continue;
					}

					boolean breakOuter = false;
					int amount;
					writeOffers = true;

					// ie the sell offer is complete
					if (required >= amountOffered) {

						sellOffer.setCollectCoinAmount(sellOffer.getCollectCoinAmount() + (amountOffered * sellOffer.getPrice()));
						sellOffer.setReceived(sellOffer.getQuantity());

						//our offer
						offer.setReceived(offer.getReceived() + amountOffered);
						offer.setCollectItemAmount(offer.getCollectItemAmount() + amountOffered);

						amount = amountOffered;

						// we need to give some change back to the buyer
						if (offer.getPrice() > sellOffer.getPrice()) {
							int myPrice = amountOffered * offer.getPrice();
							int theirPrice = amountOffered * sellOffer.getPrice();

							int change = myPrice - theirPrice;
							offer.setCollectCoinAmount(change);
						}
					} else {

						sellOffer.setCollectCoinAmount(sellOffer.getCollectCoinAmount() + (required * sellOffer.getPrice()));
						sellOffer.setReceived(sellOffer.getReceived() + required);

						// our offer
						offer.setReceived(offer.getQuantity());
						offer.setCollectItemAmount(offer.getCollectItemAmount() + required);

						amount = required;

						if (offer.getPrice() > sellOffer.getPrice()) {
							int myPrice = required * offer.getPrice();
							int theirPrice = required * sellOffer.getPrice();

							int change = myPrice - theirPrice;
							offer.setCollectCoinAmount(change);
						}
					}

					final int finalAmount = amount;

					// history
					if (amount > 0) {
						// the buyer
						Server.submitWork(() -> {
							try (Connection connection = DatabaseUtil.getConnection()) {
								try (PreparedStatement ps = connection.prepareStatement("INSERT INTO ge_history (type, item_id, quantity, price, player_id, from_player_id) VALUES(?, ?, ?, ?, ?, ?)")) {

									// the buyer
									ps.setString(1, "BOUGHT");
									ps.setInt(2, offer.getItemId());
									ps.setInt(3, finalAmount);
									ps.setInt(4, sellOffer.getPrice());
									ps.setInt(5, offer.getPlayerId());
									ps.setInt(6, sellOffer.getPlayerId());
									ps.addBatch();

									// the seller
									ps.setString(1, "SOLD");
									ps.setInt(2, offer.getItemId());
									ps.setInt(3, finalAmount);
									ps.setInt(4, sellOffer.getPrice());
									ps.setInt(5, sellOffer.getPlayerId());
									ps.setInt(6, offer.getPlayerId());
									ps.addBatch();

									ps.executeBatch();
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
						});
					}

					// send interface updates
					Player seller = PlayerHandler.getPlayerByDatabaseId(sellOffer.getPlayerId());
					if (seller != null) {
						if (seller.openInterfaceId == BUY_SELL_ID) {
							openGrandExchange(seller);
						}
						if (seller.openInterfaceId == SELL_COLLECT_ID) {
							if (!seller.attributeExists("ge_slot")) {
								continue;
							}
							if (sellOffer.getSlot() == (int) seller.getAttribute("ge_slot")) {
								sendSellCollectInterface(seller, sellOffer.getSlot(), sellOffer);
							}
						}
						seller.sendMessage("One of your grand exchange offers has updated.");
					}
					Player buyer = PlayerHandler.getPlayerByDatabaseId(offer.getPlayerId());
					if (buyer != null) {
						if (buyer.openInterfaceId == BUY_SELL_ID) {
							openGrandExchange(buyer);
						}
						if (buyer.openInterfaceId == BUY_COLLECT_ID) {
							if (!buyer.attributeExists("ge_slot")) {
								continue;
							}
							if (sellOffer.getSlot() == (int) buyer.getAttribute("ge_slot")) {
								sendBuyCollectInterface(buyer, sellOffer.getSlot(), sellOffer);
							}
						}
						buyer.sendMessage("One of your grand exchange offers has updated.");
					}

					// our offer is complete we don't need to go to the next one
					if (offer.isComplete()) {
						break;
					}
				}
			}

			if (offer.getType() == Type.SELL) {
				List<Offer> buyUpdates = offers.stream().filter(x -> offer.getItemId() == x.getItemId() && x.getType() == Type.BUY && x.getPlayerId() != offer.getPlayerId()).collect(Collectors.toList());

				for (Offer buyOffer : buyUpdates) {

					// check if aborted
					if (buyOffer.isComplete()) {
						continue;
					}

					// if the buy price is smaller than the sell offer price
					if (offer.getPrice() > buyOffer.getPrice()) {
						continue;
					}

					writeOffers = true;

					int amount;
					int required = buyOffer.getQuantity() - buyOffer.getReceived();
					int amountOffered = offer.getQuantity() - offer.getReceived();

					if (required == 0 || amountOffered == 0) {
						System.out.println("SELL-OFFER - GE SOMETHING WENT WRONG " + required + " " + amountOffered + " " + buyOffer.getPlayerId() + " " + offer.getPlayerId());
						continue;
					}

					// if the amount required is bigger than the amount were selling
					if (required >= amountOffered) {
						offer.setCollectCoinAmount(offer.getCollectCoinAmount() + (amountOffered * offer.getPrice()));
						offer.setReceived(offer.getQuantity());

						//our offer
						buyOffer.setReceived(buyOffer.getReceived() + amountOffered);
						buyOffer.setCollectItemAmount(buyOffer.getCollectItemAmount() + amountOffered);

						amount = amountOffered;

						// we need to give some change back to the buyer
						if (buyOffer.getPrice() > offer.getPrice()) {
							int buyPrice = amountOffered * buyOffer.getPrice();
							int sellPrice = amountOffered * offer.getPrice();

							int change = buyPrice - sellPrice;
							buyOffer.setCollectCoinAmount(change);
						}
					} else {
						offer.setCollectCoinAmount(buyOffer.getCollectCoinAmount() + (required * offer.getPrice()));
						offer.setReceived(offer.getReceived() + required);

						// our offer
						buyOffer.setReceived(buyOffer.getReceived() + required);
						buyOffer.setCollectItemAmount(buyOffer.getCollectItemAmount() + required);

						amount = required;

						if (buyOffer.getPrice() > offer.getPrice()) {
							int myPrice = required * buyOffer.getPrice();
							int theirPrice = required * offer.getPrice();

							int change = myPrice - theirPrice;
							buyOffer.setCollectCoinAmount(change);
						}
					}

					final int finalAmount = amount;

					// history
					if (amount > 0) {
						// the buyer
						Server.submitWork(() -> {
							try (Connection connection = DatabaseUtil.getConnection()) {
								try (PreparedStatement ps = connection.prepareStatement("INSERT INTO ge_history (type, item_id, quantity, price, player_id, from_player_id) VALUES(?, ?, ?, ?, ?, ?)")) {

									// the buyer
									ps.setString(1, "BOUGHT");
									ps.setInt(2, offer.getItemId());
									ps.setInt(3, finalAmount);
									ps.setInt(4, offer.getPrice());
									ps.setInt(5, buyOffer.getPlayerId());
									ps.setInt(6, offer.getPlayerId());
									ps.addBatch();

									// the seller
									ps.setString(1, "SOLD");
									ps.setInt(2, offer.getItemId());
									ps.setInt(3, finalAmount);
									ps.setInt(4, offer.getPrice());
									ps.setInt(5, offer.getPlayerId());
									ps.setInt(6, buyOffer.getPlayerId());
									ps.addBatch();

									ps.executeBatch();
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
						});
					}

					// send interface updates
					Player seller = PlayerHandler.getPlayerByDatabaseId(buyOffer.getPlayerId());
					if (seller != null) {
						if (seller.openInterfaceId == BUY_SELL_ID) {
							openGrandExchange(seller);
						}
						if (seller.openInterfaceId == SELL_COLLECT_ID) {
							if (!seller.attributeExists("ge_slot")) {
								continue;
							}
							if (buyOffer.getSlot() == (int) seller.getAttribute("ge_slot")) {
								sendSellCollectInterface(seller, buyOffer.getSlot(), buyOffer);
							}
						}
						seller.sendMessage("One of your grand exchange offers has updated.");
					}
					Player buyer = PlayerHandler.getPlayerByDatabaseId(offer.getPlayerId());
					if (buyer != null) {
						if (buyer.openInterfaceId == BUY_SELL_ID) {
							openGrandExchange(buyer);
						}
						if (buyer.openInterfaceId == BUY_COLLECT_ID) {
							if (!buyer.attributeExists("ge_slot")) {
								continue;
							}
							if (buyOffer.getSlot() == (int) buyer.getAttribute("ge_slot")) {
								sendBuyCollectInterface(buyer, buyOffer.getSlot(), buyOffer);
							}
						}
						buyer.sendMessage("One of your grand exchange offers has updated.");
					}

					// our offer is complete we don't need to go to the next one
					if (offer.isComplete()) {
						break;
					}
				}
			}

			offer.setRequiresUpdate(false);
		}

		if (writeOffers) {
			writeOffers();
			writeOffers = false;
		}

	}

	public static boolean onClickingButtons(Player player, int button) {

		int slot = CollectionUtil.getIndexOfValue(SUBMIT_BUY_IDS, button);
		if (slot > -1) {
			if (!checkPlayerCanUse(player, BUY_SELL_ID, true)) {
				return true;
			}

			if (player.rights == 0 && !player.isMember() && (slot == 4 || slot == 5)) {
				player.sendMessage("You must be a donator to use this slot.");
				return true;
			}

			openBuyInterface(player, slot + 1);
			return true;
		}

		slot = CollectionUtil.getIndexOfValue(COLLECT_IDS, button);
		if (slot > -1) {
			if (!checkPlayerCanUse(player, BUY_SELL_ID, true)) {
				return true;
			}

			int finalSlot = slot + 1;

			Optional<Offer> offerOptional = offers.stream().filter(x -> x.getPlayerId() == player.getDatabaseId() && x.getSlot() == finalSlot).findAny();
			if (!offerOptional.isPresent()) {
				openGrandExchange(player);
				return true;
			}

			Offer o = offerOptional.get();

			if (o.getType() == Type.BUY) {
				sendBuyCollectInterface(player, finalSlot, o);
			} else if (o.getType() == Type.SELL) {
				sendSellCollectInterface(player, finalSlot, o);
			}
			return true;
		}

		slot = CollectionUtil.getIndexOfValue(SUBMIT_SELL_IDS, button);
		if (slot > -1) {
			if (!checkPlayerCanUse(player, BUY_SELL_ID, true)) {
				return true;
			}

			if (player.rights == 0 && !player.isMember() && (slot == 4 || slot == 5)) {
				player.sendMessage("You must be a donator to use this slot.");
				return true;
			}

			openSellInterface(player, slot + 1);
			return true;
		}

		switch (button) {
			case 213230: // sell collect go back
				if (!checkPlayerCanUse(player, SELL_COLLECT_ID, true)) {
					return true;
				}
				openGrandExchange(player);
				return true;
			case 209254: // buy collect go back
				if (!checkPlayerCanUse(player, BUY_COLLECT_ID, true)) {
					return true;
				}
				openGrandExchange(player);
				return true;
			case 96082: // buy go back
				if (!checkPlayerCanUse(player, BUY_ID, true)) {
					return true;
				}
				openGrandExchange(player);
			case 96182: // sell go back
				if (!checkPlayerCanUse(player, SELL_ID, true)) {
					return true;
				}
				openGrandExchange(player);
				return true;
			case 96162: // sell set default price
				if (!checkPlayerCanUse(player, SELL_ID, true)) {
					return true;
				}
				if (!player.attributeExists("ge_item_selected")) {
					player.sendMessage("You have not selected an item to sell.");
					return true;
				}

				player.setAttribute("ge_price", ItemUtility.getGeValue((int) player.getAttribute("ge_item_selected")));
				player.getPA().sendFrame126(Misc.format((int) player.getAttribute("ge_price")) + " gp", 24772);
				sendInterfaceTotalPrice(player, false);
				return true;
			case 96166: // custom price sell
				if (!checkPlayerCanUse(player, SELL_ID, true)) {
					return true;
				}
				player.xInterfaceId = 96166;
				player.getOutStream().createFrame(27);
				return true;
			case 96186: // - 1 price sell
				if (!checkPlayerCanUse(player, SELL_ID, true)) {
					return true;
				}
				if ((int) player.getAttribute("ge_price") - 1 < 1) {
					return true;
				}

				player.setAttribute("ge_price", (int) player.getAttribute("ge_price") - 1);
				player.getPA().sendFrame126("" + Misc.formatNumber((int) player.getAttribute("ge_price")) + " gp", 24772);
				sendInterfaceTotalPrice(player, false);
				return true;
			case 96189: // + 1 price sell
				if (!checkPlayerCanUse(player, SELL_ID, true)) {
					return true;
				}
				if (totalValueRollsOver((int) player.getAttribute("ge_quantity"), (int) player.getAttribute("ge_price") + 1)) {
					player.sendMessage("You may not go over the total price of " + Misc.formatNumber(Integer.MAX_VALUE) + " gp.");
					return true;
				}

				player.setAttribute("ge_price", (int) player.getAttribute("ge_price") + 1);
				player.getPA().sendFrame126("" + Misc.formatNumber((int) player.getAttribute("ge_price")) + " gp", 24772);
				sendInterfaceTotalPrice(player, false);
				return true;
			case 96158: // - 5% price sell
				if (!checkPlayerCanUse(player, SELL_ID, true)) {
					return true;
				}
				int amount = (int) player.getAttribute("ge_price");

				int percentage = (int) Math.floor(amount * 0.05);
				if (percentage <= 0) {
					percentage = 1;
				}

				if (amount - percentage < 1) {
					amount = 1;
				} else {
					amount -= percentage;
				}
				player.setAttribute("ge_price", amount);
				player.getPA().sendFrame126("" + Misc.formatNumber((int) player.getAttribute("ge_price")) + " gp", 24772);
				sendInterfaceTotalPrice(player, false);
				return true;
			case 96170: // + 5% price sell
				if (!checkPlayerCanUse(player, SELL_ID, true)) {
					return true;
				}
				amount = (int) player.getAttribute("ge_price");

				percentage = (int) Math.floor(amount * 0.05);
				if (percentage <= 0) {
					percentage = 1;
				}

				amount += percentage;

				if (totalValueRollsOver((int) player.getAttribute("ge_quantity"), (int) player.getAttribute("ge_price") + amount)) {
					player.sendMessage("You may not go over the total price of " + Misc.formatNumber(Integer.MAX_VALUE) + " gp.");
					return true;
				}

				player.setAttribute("ge_price", amount);
				player.getPA().sendFrame126("" + Misc.formatNumber((int) player.getAttribute("ge_price")) + " gp", 24772);
				sendInterfaceTotalPrice(player, false);
				return true;

			case 96130: // - 1 quantity sell
				if (!checkPlayerCanUse(player, SELL_ID, true)) {
					return true;
				}
				if ((int) player.getAttribute("ge_quantity") - 1 < 1) {
					return true;
				}
				player.setAttribute("ge_quantity", (int) player.getAttribute("ge_quantity") - 1);
				player.getPA().sendFrame126("" + player.getAttribute("ge_quantity") + "", 24771); // quantity
				sendInterfaceTotalPrice(player, false);
				return true;
			case 96154: // sell custom price
				if (!player.attributeExists("ge_item_selected")) {
					player.sendMessage("You have not selected an item to sell.");
					return true;
				}
				player.xInterfaceId = 96154;
				player.getOutStream().createFrame(27);
				return true;
			case 96134:
			case 96138: // + 1 quantity sell
			case 96142: // + 10 quantity sell
			case 96146: // + 100 quantity sell
			case 96150: // + 1000 quantity sell

				if (!checkPlayerCanUse(player, SELL_ID, true)) {
					return true;
				}

				int increase = 1;
				if (button == 96138) {
					increase = 10;
				}
				if (button == 96142) {
					increase = 100;
				}
				if (button == 96146) {
					increase = 100;
				}
				if (button == 96150) {
					increase = Integer.MAX_VALUE - (int) player.getAttribute("ge_quantity");
				}

				if (!player.attributeExists("ge_item_selected")) {
					player.sendMessage("You have not selected an item to sell.");
					return true;
				}

				int itemId = (int) player.getAttribute("ge_item_selected");

				if ((int) player.getAttribute("ge_quantity") + increase > player.getItems().amountOfItem(itemId)) {
					increase = player.getItems().amountOfItem(itemId) - (int) player.getAttribute("ge_quantity");
				}

				if (totalValueRollsOver((int) player.getAttribute("ge_quantity") + increase, (int) player.getAttribute("ge_price"))) {
					player.sendMessage("You may not go over the total price of " + Misc.formatNumber(Integer.MAX_VALUE) + " gp.");
					return true;
				}

				player.setAttribute("ge_quantity", (int) player.getAttribute("ge_quantity") + increase);
				player.getPA().sendFrame126("" + player.getAttribute("ge_quantity") + "", 24771); // quantity
				sendInterfaceTotalPrice(player, false);
				return true;

			case 96174: // sell window confirm

				if (!checkPlayerCanUse(player, SELL_ID, true)) {
					return true;
				}

				if (!player.attributeExists("ge_item_selected")) {
					player.sendMessage("You have not selected an item to buy.");
					return true;
				}

				if ((int) player.getAttribute("ge_quantity") <= 0) {
					player.sendMessage("Item quantity cannot be below zero.");
					return true;
				}

				if ((int) player.getAttribute("ge_price") <= 0) {
					player.sendMessage("Item price cannot be below zero.");
					return true;
				}


				if (totalValueRollsOver((int) player.getAttribute("ge_quantity"), (int) player.getAttribute("ge_price"))) {
					player.sendMessage("You may not go over the total price of " + Misc.formatNumber(Integer.MAX_VALUE) + " gp.");
					return true;
				}

				// the player does not have the amount submitted... something fishy going on here send them back to main interface
				if (!player.getItems().playerHasItem((int) player.getAttribute("ge_item_selected"), (int) player.getAttribute("ge_quantity"))) {
					openGrandExchange(player);
					return true;
				}

				player.getItems().deleteItem((int) player.getAttribute("ge_item_selected"), (int) player.getAttribute("ge_quantity"));

				int currentItemId = (int) player.getAttribute("ge_item_selected");
				if (ItemUtility.isNote(currentItemId)) {
					currentItemId = ItemUtility.getUnotedId(currentItemId);
				}

				Offer offer = new Offer(
						Type.SELL,
						player.getDatabaseId(),
						(int) player.getAttribute("ge_slot"),
						currentItemId,
						(int) player.getAttribute("ge_quantity"),
						(int) player.getAttribute("ge_price")

				);

				Server.submitWork(() -> {
					try (Connection connection = DatabaseUtil.getConnection()) {
						try (PreparedStatement ps = connection.prepareStatement("INSERT INTO ge_history (type, item_id, quantity, price, player_id) VALUES(?, ?, ?, ?, ?)")) {
							ps.setString(1, "SELL_OFFER");
							ps.setInt(2, offer.getItemId());
							ps.setInt(3, offer.getQuantity());
							ps.setInt(4, offer.getPrice());
							ps.setInt(5, player.getDatabaseId());
							ps.execute();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				});


				offers.add(offer);

				writeOffers = true;

				openGrandExchange(player);
				return true;

			case 96030: // - 1 quantity buy

				if (!checkPlayerCanUse(player, BUY_ID, true)) {
					return true;
				}

				if ((int) player.getAttribute("ge_quantity") - 1 < 1) {
					return true;
				}
				player.setAttribute("ge_quantity", (int) player.getAttribute("ge_quantity") - 1);
				player.getPA().sendFrame126("" + player.getAttribute("ge_quantity") + "", 24671); // quantity
				sendInterfaceTotalPrice(player, true);
				return true;
			case 96034:
			case 96038: // + 1 quantity buy
			case 96042: // + 10 quantity buy
			case 96046: // + 100 quantity buy
			case 96050: // + 1000 quantity buy
				if (!checkPlayerCanUse(player, BUY_ID, true)) {
					return true;
				}
				increase = 1;
				if (button == 96042) {
					increase = 10;
				}
				if (button == 96046) {
					increase = 100;
				}
				if (button == 96050) {
					increase = 1000;
				}

				if (totalValueRollsOver((int) player.getAttribute("ge_quantity") + increase, (int) player.getAttribute("ge_price"))) {
					player.sendMessage("You may not go over the total price of " + Misc.formatNumber(Integer.MAX_VALUE) + " gp.");
					return true;
				}

				player.setAttribute("ge_quantity", (int) player.getAttribute("ge_quantity") + increase);
				player.getPA().sendFrame126("" + player.getAttribute("ge_quantity") + "", 24671); // quantity
				sendInterfaceTotalPrice(player, true);
				return true;


			case 96054: // custom quantity buy
				if (!checkPlayerCanUse(player, BUY_ID, true)) {
					return true;
				}
				player.xInterfaceId = 96054;
				player.getOutStream().createFrame(27);
				return true;

			case 96066: // custom price buy
				if (!checkPlayerCanUse(player, BUY_ID, true)) {
					return true;
				}
				player.xInterfaceId = 96066;
				player.getOutStream().createFrame(27);
				return true;

			case 96086: // - 1 price buy
				if (!checkPlayerCanUse(player, BUY_ID, true)) {
					return true;
				}
				if ((int) player.getAttribute("ge_price") - 1 < 1) {
					return true;
				}

				player.setAttribute("ge_price", (int) player.getAttribute("ge_price") - 1);
				player.getPA().sendFrame126("" + Misc.formatNumber((int) player.getAttribute("ge_price")) + " gp", 24672);
				sendInterfaceTotalPrice(player, true);
				return true;
			case 96089: // + 1 price buy
				if (!checkPlayerCanUse(player, BUY_ID, true)) {
					return true;
				}
				if (totalValueRollsOver((int) player.getAttribute("ge_quantity"), (int) player.getAttribute("ge_price") + 1)) {
					player.sendMessage("You may not go over the total price of " + Misc.formatNumber(Integer.MAX_VALUE) + " gp.");
					return true;
				}

				player.setAttribute("ge_price", (int) player.getAttribute("ge_price") + 1);
				player.getPA().sendFrame126("" + Misc.formatNumber((int) player.getAttribute("ge_price")) + " gp", 24672);
				sendInterfaceTotalPrice(player, true);
				return true;
			case 96058: // - 5% price buy
				if (!checkPlayerCanUse(player, BUY_ID, true)) {
					return true;
				}
				amount = (int) player.getAttribute("ge_price");

				percentage = (int) Math.floor(amount * 0.05);
				if (percentage <= 0) {
					percentage = 1;
				}

				if (amount - percentage < 1) {
					amount = 1;
				} else {
					amount -= percentage;
				}
				player.setAttribute("ge_price", amount);
				player.getPA().sendFrame126("" + Misc.formatNumber((int) player.getAttribute("ge_price")) + " gp", 24672);
				sendInterfaceTotalPrice(player, true);
				return true;
			case 96070: // + 5% price buy
				if (!checkPlayerCanUse(player, BUY_ID, true)) {
					return true;
				}
				amount = (int) player.getAttribute("ge_price");

				percentage = (int) Math.floor(amount * 0.05);
				if (percentage <= 0) {
					percentage = 1;
				}

				amount += percentage;

				if (totalValueRollsOver((int) player.getAttribute("ge_quantity"), (int) player.getAttribute("ge_price") + amount)) {
					player.sendMessage("You may not go over the total price of " + Misc.formatNumber(Integer.MAX_VALUE) + " gp.");
					return true;
				}

				player.setAttribute("ge_price", amount);
				player.getPA().sendFrame126("" + Misc.formatNumber((int) player.getAttribute("ge_price")) + " gp", 24672);
				sendInterfaceTotalPrice(player, true);
				return true;

			case 96074: // buy window confirm

				if (!checkPlayerCanUse(player, BUY_ID, true)) {
					return true;
				}

				if (!player.attributeExists("ge_item_selected")) {
					player.sendMessage("You have not selected an item to buy.");
					return true;
				}

				if ((int) player.getAttribute("ge_quantity") <= 0) {
					player.sendMessage("Item quantity cannot be below zero.");
					return true;
				}

				if ((int) player.getAttribute("ge_price") <= 0) {
					player.sendMessage("Item price cannot be below zero.");
					return true;
				}

				if (totalValueRollsOver((int) player.getAttribute("ge_quantity"), (int) player.getAttribute("ge_price"))) {
					player.sendMessage("You may not go over the total price of " + Misc.formatNumber(Integer.MAX_VALUE) + " gp.");
					return true;
				}

				int totalValue = (int) player.getAttribute("ge_quantity") * (int) player.getAttribute("ge_price");

				if (!player.getItems().playerHasItem(995, totalValue)) {
					player.sendMessage("You have insufficient gp.");
					return true;
				}

				player.getItems().deleteItem(995, totalValue);

				currentItemId = (int) player.getAttribute("ge_item_selected");
				if (ItemUtility.isNote(currentItemId)) {
					currentItemId = ItemUtility.getUnotedId(currentItemId);
				}

				offer = new Offer(
						Type.BUY,
						player.getDatabaseId(),
						(int) player.getAttribute("ge_slot"),
						currentItemId,
						(int) player.getAttribute("ge_quantity"),
						(int) player.getAttribute("ge_price")

				);

				Server.submitWork(() -> {
					try (Connection connection = DatabaseUtil.getConnection()) {
						try (PreparedStatement ps = connection.prepareStatement("INSERT INTO ge_history (type, item_id, quantity, price, player_id) VALUES(?, ?, ?, ?, ?)")) {
							ps.setString(1, "BUY_OFFER");
							ps.setInt(2, offer.getItemId());
							ps.setInt(3, offer.getQuantity());
							ps.setInt(4, offer.getPrice());
							ps.setInt(5, player.getDatabaseId());
							ps.execute();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				});

				offers.add(offer);

				writeOffers = true;

				openGrandExchange(player);
				return true;
			case 214016: // sell abort
			case 210040: // buy abort

				if (button == 214016 && !checkPlayerCanUse(player, SELL_COLLECT_ID, true)) {
					return true;
				}
				if (button == 210040 && !checkPlayerCanUse(player, BUY_COLLECT_ID, true)) {
					return true;
				}

				Optional<Offer> offerOptional = offers.stream().filter(x -> x.getPlayerId() == player.getDatabaseId() && x.getSlot() == (int) player.getAttribute("ge_slot")).findAny();
				if (!offerOptional.isPresent()) {
					openGrandExchange(player);
					return true;
				}

				Offer offer1 = offerOptional.get();

				if (offer1.isAborted()) {
					player.sendMessage("This offer has already been aborted.");
					return true;
				}

				if (offer1.isComplete()) {
					return true;
				}

				offer1.setAborted(true);

				Server.submitWork(() -> {
					try (Connection connection = DatabaseUtil.getConnection()) {
						try (PreparedStatement ps = connection.prepareStatement("INSERT INTO ge_history (type, item_id, quantity, price, player_id) VALUES(?, ?, ?, ?, ?)")) {
							ps.setString(1, "ABORT");
							ps.setInt(2, offer1.getItemId());
							if (offer1.getType() == Type.BUY) {
								ps.setInt(3, offer1.getAmountOffered() - (offer1.getReceived() * offer1.getPrice()));
							} else {
								ps.setInt(3, offer1.getAmountOffered() - offer1.getReceived());
							}
							ps.setInt(4, -1);
							ps.setInt(5, offer1.getPlayerId());
							ps.execute();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				});

				if (offer1.getType() == Type.BUY) {
					offer1.setCollectCoinAmount(offer1.getAmountOffered() - (offer1.getReceived() * offer1.getPrice()));
					sendBuyCollectInterface(player, (int) player.getAttribute("ge_slot"), offer1);
				} else if (offer1.getType() == Type.SELL) {
					offer1.setCollectItemAmount(offer1.getAmountOffered() - offer1.getReceived());
					sendSellCollectInterface(player, (int) player.getAttribute("ge_slot"), offer1);
				}

				writeOffers = true;

				return true;
		}

		return false;
	}

	public static boolean onXAmountEntered(Player player, int interfaceId, int amount) {
		if (interfaceId == 96054) { // buy quantity custom value

			if (totalValueRollsOver(amount, (int) player.getAttribute("ge_price"))) {
				player.sendMessage("You may not go over the total price of " + Misc.formatNumber(Integer.MAX_VALUE) + " gp.");
				return true;
			}


			player.setAttribute("ge_quantity", amount);
			player.getPA().sendFrame126("" + player.getAttribute("ge_quantity") + "", 24671); // quantity
			sendInterfaceTotalPrice(player, true);
			return true;
		}

		if (interfaceId == 96066) {

			if (totalValueRollsOver((int) player.getAttribute("ge_quantity"), amount)) {
				player.sendMessage("You may not go over the total price of " + Misc.formatNumber(Integer.MAX_VALUE) + " gp.");
				return true;
			}

			player.setAttribute("ge_price", amount);
			player.getPA().sendFrame126("" + Misc.formatNumber((int) player.getAttribute("ge_price")) + " gp", 24672);
			sendInterfaceTotalPrice(player, true);
			return true;
		}

		if (interfaceId == 96154) {
			if (!player.attributeExists("ge_item_selected")) {
				player.sendMessage("You have not selected an item to sell.");
				return true;
			}

			int itemId = (int) player.getAttribute("ge_item_selected");

			if ((int) player.getAttribute("ge_quantity") + amount > player.getItems().amountOfItem(itemId)) {
				amount = player.getItems().amountOfItem(itemId);
			}

			if (totalValueRollsOver((int) player.getAttribute("ge_quantity") + amount, (int) player.getAttribute("ge_price"))) {
				player.sendMessage("You may not go over the total price of " + Misc.formatNumber(Integer.MAX_VALUE) + " gp.");
				return true;
			}

			player.setAttribute("ge_quantity", amount);
			player.getPA().sendFrame126("" + player.getAttribute("ge_quantity") + "", 24771); // quantity
			sendInterfaceTotalPrice(player, true);

			return true;
		}

		if (interfaceId == 96166) { // custom price sell interface

			if (totalValueRollsOver((int) player.getAttribute("ge_quantity"), amount)) {
				player.sendMessage("You may not go over the total price of " + Misc.formatNumber(Integer.MAX_VALUE) + " gp.");
				return true;
			}

			player.setAttribute("ge_price", amount);
			player.getPA().sendFrame126("" + Misc.formatNumber((int) player.getAttribute("ge_price")) + " gp", 24772);
			sendInterfaceTotalPrice(player, false);
			return true;
		}

		return false;
	}

	public static void openGrandExchange(Player player) {

		if (!enabled) {
			player.sendMessage("The grand exchange is currently disabled.");
			return;
		}

		if (player.tradeTimer > 0) {
			player.sendMessage("You must wait 15 minutes from the creation of your account to use GE.");
			return;
		}

		if (player.ironman) {
			player.sendMessage("Ironmen accounts cannot use Grand Exchange.");
			return;
		}

		if(Pins.checkPin(player)) {
			Pins.open(player, Pins.Type.GRAND_EXCHANGE);
			return;
		}

		if (!checkPlayerCanUse(player, -1, false)) {
			player.sendMessage("You cannot use the GE at this time, try relogging and trying again.");
			return;
		}

		// remove attributes
		player.removeAttribute("ge_slot");
		player.removeAttribute("ge_quantity");
		player.removeAttribute("ge_price");
		player.removeAttribute("ge_item_selected");

		List<Offer> playerOffers = offers.stream().filter(x -> player.getDatabaseId() == x.getPlayerId()).collect(Collectors.toList());

		for (int i = 1; i < 7; i++) {
			int finalSlot = i;
			Optional<Offer> offerOptional = playerOffers.stream().filter(x -> x.getSlot() == finalSlot).findAny();

			// no offer exists at this slot
			if (!offerOptional.isPresent()) {
				player.getOutStream().createFrameVarSizeWord(244);
				player.getOutStream().writeString("<" + i + ">resetslot");
				player.getOutStream().endFrameVarSizeWord();
				continue;
			}

			try {

				Offer offer = offerOptional.get();

				player.getOutStream().createFrameVarSizeWord(244);

				StringBuilder sb = new StringBuilder();

				sb.append("<").append(offer.getSlot()).append(">");

				// item id
				sb.append("item").append("#").append(offer.getItemId()).append("#");
				sb.append("<quantity>").append(offer.getQuantity()).append("</quantity>");
				sb.append("<slotprice>").append(offer.getTotalCost()).append("</slotprice>");

				// percentage

				int percentage = (int) (((double) (offer.getReceived()) / (double) offer.getQuantity()) * 100);
				sb.append("slotpercent").append("{").append(percentage).append("}");

				if (offer.isAborted()) {
					sb.append("slotaborted");
				}
				if (offer.getType() == Type.BUY) {
					sb.append("slotbuy").append("[").append(2).append("]");
				} else if (offer.getType() == Type.SELL) {
					sb.append("slotsell").append("[").append(2).append("]");
				}

				player.getOutStream().writeString(sb.toString());
				player.getOutStream().endFrameVarSizeWord();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		player.getPA().showInterface(BUY_SELL_ID);
	}

	private static void openSellInterface(Player player, int slot) {
		player.getPA().sendFrame126("Choose an item...", 24769);
		player.getPA().sendFrame126("Choose an item from your inventory to sell.", 24770);

		player.setAttribute("ge_slot", slot);
		player.setAttribute("ge_quantity", 0);
		player.setAttribute("ge_price", 0);

		player.getPA().sendFrame126("0", 24771); // quantity
		player.getPA().sendFrame126("1 gp", 24772); // price per item
		player.getPA().sendFrame126("0 gp", 24773); // total price ie quantity * price

		sendItemOnInterface(player, 24780, -1, 0);

		player.getItems().resetItems(3322);

		player.getPA().sendFrame248(SELL_ID, 3321);

		//  player.getPA().showInterface(SELL_ID);
	}

	private static void openBuyInterface(Player player, int slot) {
		player.getPA().sendFrame126("Choose an item...", 24669);
		player.getPA().sendFrame126("Click the icon on the left to search for items...", 24670);

		player.setAttribute("ge_slot", slot);
		player.setAttribute("ge_quantity", 0);
		player.setAttribute("ge_price", 0);

		sendItemOnInterface(player, 24680, -1, 0);

		player.getPA().sendFrame126("0", 24671); // quantity
		player.getPA().sendFrame126("1 gp", 24672); // price per item
		player.getPA().sendFrame126("0 gp", 24673); // total price ie quantity * price

		player.getItems().resetItems(3322);

		player.getPA().sendFrame248(BUY_ID, 3321);
	}

	private static void sendInterfaceTotalPrice(Player player, boolean buy) {
		int quantity = (int) player.getAttribute("ge_quantity");
		int price = (int) player.getAttribute("ge_price");

		if (quantity == 0 || price == 0) {
			player.getPA().sendFrame126("0 gp", 24673); // total price ie quantity * price
			return;
		}

		int totalPrice = quantity * price;

		player.getPA().sendFrame126("" + Misc.formatNumber(totalPrice) + " gp", buy ? 24673 : 24773); // total price ie quantity * price
	}

	private static boolean totalValueRollsOver(int quantity, int price) {
		return (long) quantity * (long) price > Integer.MAX_VALUE;
	}

	public static void playerSelectedItem(Player player, int itemId) {
		if (player.openInterfaceId == BUY_ID) {

			if (player.rights == 3) {
				player.sendMessage("Selected item: " + itemId);
			}

			int currentItemId = itemId;
			if (ItemUtility.isNote(currentItemId)) {
				currentItemId = ItemUtility.getUnotedId(currentItemId);
			}

			Optional<ItemDefinition> optional = ItemDefinition.get(currentItemId);
			if (!optional.isPresent()) {
				return;
			}

			ItemDefinition definition = optional.get();

			if (!definition.isGeTradable()) {
				player.sendMessage("This item is prohibited from buying on the GE.");
				return;
			}

			if (CollectionUtil.getIndexOfValue(Config.UNTRADABLE_ITEM_IDS, itemId) > -1) {
				player.sendMessage("Untradable items cannot be bought from the GE.");
				return;
			}
			if (CollectionUtil.getIndexOfValue(Config.PROHIBITTED_GE_ITEMS, itemId) > -1) {
				player.sendMessage("This item is prohibited from buying on the GE.");
				return;
			}

			player.setAttribute("ge_quantity", 1);
			player.getPA().sendFrame126("" + player.getAttribute("ge_quantity") + "", 24671);

			player.setAttribute("ge_item_selected", itemId);

			player.setAttribute("ge_price", ItemUtility.getGeValue(currentItemId));
			player.getPA().sendFrame126(Misc.format((int) player.getAttribute("ge_price")) + " gp", 24672);

			sendItemOnInterface(player, 24680, currentItemId, 1);

			ItemDef def = ItemDef.forID(currentItemId);

			if (def != null) {
				player.getPA().sendFrame126(def.name == null ? "No name" : def.name, 24669);
				player.getPA().sendFrame126(def.description == null ? "" : def.description, 24670);
			}

			sendInterfaceTotalPrice(player, true);
		}
		if (player.openInterfaceId == SELL_ID) {

			if (!player.getItems().playerHasItem(itemId)) {
				return;
			}

			if (player.rights == 3) {
				player.sendMessage("Selected item: " + itemId);
			}

			int currentItemId = itemId;
			if (ItemUtility.isNote(currentItemId)) {
				currentItemId = ItemUtility.getUnotedId(currentItemId);
			}

			Optional<ItemDefinition> optional = ItemDefinition.get(currentItemId);
			if (!optional.isPresent()) {
				return;
			}

			ItemDefinition definition = optional.get();

			if (!definition.isGeTradable()) {
				player.sendMessage("This item is prohibited from buying on the GE.");
				return;
			}


			if (CollectionUtil.getIndexOfValue(Config.PROHIBITTED_GE_ITEMS, itemId) > -1) {
				player.sendMessage("This item is prohibited from selling on the GE.");
				return;
			}

			if (CollectionUtil.getIndexOfValue(Config.UNTRADABLE_ITEM_IDS, itemId) > -1) {
				player.sendMessage("Untradable items cannot be sold on the GE.");
				return;
			}

			player.setAttribute("ge_quantity", 1);
			player.getPA().sendFrame126("" + player.getAttribute("ge_quantity") + "", 24771);

			player.setAttribute("ge_item_selected", itemId);

			player.setAttribute("ge_price", ItemUtility.getGeValue(currentItemId));
			player.getPA().sendFrame126(Misc.format((int) player.getAttribute("ge_price")) + " gp", 24772);

			sendItemOnInterface(player, 24780, currentItemId, 1);

			ItemDef def = ItemDef.forID(currentItemId);

			if (def != null) {
				player.getPA().sendFrame126(def.name == null ? "No name" : def.name, 24769);
				player.getPA().sendFrame126(def.description == null ? "" : def.description, 24770);
			}

			sendInterfaceTotalPrice(player, false);
		}
	}

	private static void sendBuyCollectInterface(Player player, int slot, Offer offer) {
		player.setAttribute("ge_slot", slot);

		sendItemOnInterface(player, 53780, offer.getItemId(), offer.getQuantity());

		player.getOutStream().createFrameVarSizeWord(244);

		StringBuilder sb = new StringBuilder();

		sb.append("<").append(offer.getSlot()).append(">");

		sb.append("slotselected");

		if (offer.isAborted()) {
			sb.append("slotaborted");
		} else {
			// percentage
			int percentage = (int) (((double) (offer.getReceived()) / (double) offer.getQuantity()) * 100);
			sb.append("slotpercent").append("{").append(percentage).append("}");
		}

		player.getOutStream().writeString(sb.toString());
		player.getOutStream().endFrameVarSizeWord();

		List<Item> collectableItems = new ArrayList<>();

		if (offer.getCollectCoinAmount() != 0) {
			collectableItems.add(new Item(995, offer.getCollectCoinAmount()));
		}
		if (offer.getCollectItemAmount() != 0) {
			collectableItems.add(new Item(offer.getItemId(), offer.getCollectItemAmount()));
		}

		if (collectableItems.size() == 0) {
			sendItemOnInterface(player, 53781, -1, 0);
			sendItemOnInterface(player, 53782, -1, 0);
		} else {
			for (int i = 0; i < 2; i++) {
				if (i > collectableItems.size() - 1) {
					sendItemOnInterface(player, 53781 + i, -1, 0);
				} else {
					Item item = collectableItems.get(i);
					sendItemOnInterface(player, 53781 + i, item.getId(), item.getAmount());
				}
			}
		}

		ItemDef def = ItemDef.forID(offer.getItemId());

		if (def != null) {
			player.getPA().sendFrame126(def.name == null ? "No name" : def.name, 53769);
			player.getPA().sendFrame126(def.description == null ? "" : def.description, 53770);
		}

		player.getPA().sendFrame126(offer.getQuantity() + "", 53771);
		player.getPA().sendFrame126(offer.getPrice() + " gp", 53772);
		player.getPA().sendFrame126(Misc.format(offer.getTotalCost()) + " gp", 53773);

		player.getPA().showInterface(BUY_COLLECT_ID);
	}

	private static void sendSellCollectInterface(Player player, int slot, Offer offer) {
		player.setAttribute("ge_slot", slot);

		sendItemOnInterface(player, 54780, offer.getItemId(), offer.getQuantity());

		player.getOutStream().createFrameVarSizeWord(244);

		StringBuilder sb = new StringBuilder();

		sb.append("<").append(offer.getSlot()).append(">");

		sb.append("slotselected");

		if (offer.isAborted()) {
			sb.append("slotaborted");
		} else {
			// percentage
			int percentage = (int) (((double) (offer.getReceived()) / (double) offer.getQuantity()) * 100);
			sb.append("slotpercent").append("{").append(percentage).append("}");
		}

		player.getOutStream().writeString(sb.toString());
		player.getOutStream().endFrameVarSizeWord();

		List<Item> collectableItems = new ArrayList<>();

		if (offer.getCollectCoinAmount() != 0) {
			collectableItems.add(new Item(995, offer.getCollectCoinAmount()));
		}
		if (offer.getCollectItemAmount() != 0) {
			collectableItems.add(new Item(offer.getItemId(), offer.getCollectItemAmount()));
		}

		if (collectableItems.size() == 0) {
			sendItemOnInterface(player, 54781, -1, 0);
			sendItemOnInterface(player, 54782, -1, 0);
		} else {
			for (int i = 0; i < 2; i++) {
				if (i > collectableItems.size() - 1) {
					sendItemOnInterface(player, 54781 + i, -1, 0);
				} else {
					Item item = collectableItems.get(i);
					sendItemOnInterface(player, 54781 + i, item.getId(), item.getAmount());
				}
			}
		}

		ItemDef def = ItemDef.forID(offer.getItemId());

		if (def != null) {
			player.getPA().sendFrame126(def.name == null ? "No name" : def.name, 54769);
			player.getPA().sendFrame126(def.description == null ? "" : def.description, 54770);
		}

		player.getPA().sendFrame126(offer.getQuantity() + "", 54771);
		player.getPA().sendFrame126(offer.getPrice() + " gp", 54772);
		player.getPA().sendFrame126(Misc.format(offer.getTotalCost()) + " gp", 54773);

		player.getPA().showInterface(SELL_COLLECT_ID);
	}

	private static void sendItemOnInterface(Player player, int interfaceId, int item, int amount) {
		player.getOutStream().createFrameVarSizeWord(53);
		player.getOutStream().writeWord(interfaceId);
		player.getOutStream().writeWord(1); // slot

		if (amount > 254) {
			player.getOutStream().writeByte(255);
			player.getOutStream().writeDWord_v2(amount);
		} else {
			player.getOutStream().writeByte(amount);
		}

		player.getOutStream().writeWordBigEndianA(item + 1);
		player.getOutStream().endFrameVarSizeWord();
		player.flushOutStream();
	}

	public static void removeItem(Player player, int id, int itemId, int removeSlot) {

		if (!player.attributeExists("ge_slot")) {
			return;
		}

		int slot = (int) player.getAttribute("ge_slot");

		switch (id) {

			case 3322: // selling interface offering item
				if (!checkPlayerCanUse(player, SELL_ID, true)) {
					return;
				}
				playerSelectedItem(player, itemId);
				break;


			case 54782:
			case 54781:
			case 53781:
			case 53782:
				// we cant cancel this transaction
				if (player.openInterfaceId != BUY_COLLECT_ID && player.openInterfaceId != SELL_COLLECT_ID) {
					return;
				}

				Optional<Offer> offerOptional = offers.stream().filter(x -> x.getPlayerId() == player.getDatabaseId() && x.getSlot() == slot).findAny();

				if (!offerOptional.isPresent()) {
					return;
				}
				Offer offer = offerOptional.get();

				if (itemId == 995) {
					if (offer.getCollectCoinAmount() == 0) {
						return;
					}

					int requiredSlots = 1;
					if (player.getItems().playerHasItem(995)) {
						if ((long) offer.getCollectCoinAmount() + (long) player.getItems().getItemAmount(995) > Integer.MAX_VALUE) {
							player.sendMessage("You cannot have more than " + Misc.formatNumber(Integer.MAX_VALUE) + " gp in your inventory.");
							return;
						}
						requiredSlots = 0;
					}
					if (requiredSlots > 0 && player.getItems().freeInventorySlots() < requiredSlots) {
						player.sendMessage("You do not have enough free inventory space.");
						return;
					}

					Server.submitWork(() -> {
						try (Connection connection = DatabaseUtil.getConnection()) {
							try (PreparedStatement ps = connection.prepareStatement("INSERT INTO ge_history (type, item_id, quantity, price, player_id) VALUES(?, ?, ?, ?, ?)")) {
								ps.setString(1, "COLLECT");
								ps.setInt(2, 995);
								ps.setInt(3, offer.getCollectCoinAmount());
								ps.setInt(4, -1);
								ps.setInt(5, offer.getPlayerId());
								ps.execute();
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					});

					player.getItems().addItem(995, offer.getCollectCoinAmount());
					offer.setCollectCoinAmount(0);

					if (offer.isComplete() && offer.getCollectCoinAmount() == 0 && offer.getCollectItemAmount() == 0) {
						offers.remove(offer);
						openGrandExchange(player);

						writeOffers = true;
					} else {
						if (player.openInterfaceId == SELL_COLLECT_ID) {
							sendSellCollectInterface(player, slot, offer);
						} else {
							sendBuyCollectInterface(player, slot, offer);
						}
					}

				} else {
					if (itemId != offer.getItemId()) {
						return;
					}

					if (offer.getCollectItemAmount() == 0) {
						return;
					}

					int requiredSlots = offer.getCollectItemAmount();

					boolean stackable = ItemUtility.isStackable(offer.getItemId());
					boolean giveNote = false;

					if (stackable) {
						requiredSlots = 1;
					}

					// we're going to give them a noted version
					if (!stackable && ItemUtility.canBeNoted(offer.getItemId()) && offer.getCollectItemAmount() > 1) {
						requiredSlots = 1;
						giveNote = true;
					}

					// check to see if we already have this if its stackable / notable
					if ((giveNote || stackable) && player.getItems().playerHasItem(giveNote ? ItemUtility.getNotedId(itemId) : itemId)) {
						requiredSlots = 0;
					}

					int amountToTake = offer.getCollectItemAmount();
					int freeSlots = player.getItems().freeInventorySlots();

					if (requiredSlots > 0 && freeSlots < requiredSlots) {
						player.sendMessage("You do not have enough free inventory space.");
						if (freeSlots == 0) {
							return;
						} else {
							amountToTake = freeSlots;
						}
					}

					player.getItems().addItem(giveNote ? ItemUtility.getNotedId(itemId) : itemId, amountToTake);
					offer.setCollectItemAmount(offer.getCollectItemAmount() - amountToTake);

					final int totalAmount = amountToTake;

					Server.submitWork(() -> {
						try (Connection connection = DatabaseUtil.getConnection()) {
							try (PreparedStatement ps = connection.prepareStatement("INSERT INTO ge_history (type, item_id, quantity, price, player_id) VALUES(?, ?, ?, ?, ?)")) {
								ps.setString(1, "COLLECT");
								ps.setInt(2, itemId);
								ps.setInt(3, totalAmount);
								ps.setInt(4, -1);
								ps.setInt(5, offer.getPlayerId());
								ps.execute();
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					});

					if (offer.isComplete() && offer.getCollectCoinAmount() == 0 && offer.getCollectItemAmount() == 0) {
						offers.remove(offer);
						openGrandExchange(player);

						writeOffers = true;

					} else {
						if (player.openInterfaceId == SELL_COLLECT_ID) {
							sendSellCollectInterface(player, slot, offer);
						} else {
							sendBuyCollectInterface(player, slot, offer);
						}
					}
				}

				break;
		}

	}

	private static void writeOffers() {
		long start = System.currentTimeMillis();

		ByteArrayDataOutput buffer = ByteStreams.newDataOutput();

		buffer.writeInt(offers.size());

		for (Offer o : offers) {
			buffer.writeByte(o.getType());
			buffer.writeInt(o.getPlayerId());
			buffer.writeByte(o.getSlot());
			buffer.writeInt(o.getItemId());
			buffer.writeInt(o.getQuantity());
			buffer.writeInt(o.getPrice());
			buffer.writeInt(o.getReceived());
			buffer.writeBoolean(o.isAborted());
			buffer.writeInt(o.getCollectItemAmount());
			buffer.writeInt(o.getCollectCoinAmount());
			buffer.writeLong(o.getCreatedAt());
		}

		try (Connection connection = DatabaseUtil.getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement("UPDATE ge SET bytes = ? where id = 1")) {
				ps.setBytes(1, buffer.toByteArray());
				if (Server.getSetting("localhost") == null) {
					ps.execute();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("ge saving: " + (System.currentTimeMillis() - start) + " ms");
	}

	public static void loadOffers() {

		try (Connection connection = DatabaseUtil.getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM ge WHERE id = 1")) {
				try (ResultSet results = ps.executeQuery()) {
					if (results.next()) {

						byte[] buffer = results.getBytes("bytes");
						if (buffer == null) {
							return;
						}

						ByteArrayDataInput input = ByteStreams.newDataInput(buffer);

						int count = input.readInt();

						for (int i = 0; i < count; i++) {
							int type = input.readByte();
							int playerId = input.readInt();
							int slot = input.readByte();
							int itemId = input.readInt();
							int quantity = input.readInt();
							int price = input.readInt();
							int received = input.readInt();
							boolean aborted = input.readBoolean();
							int itemAmount = input.readInt();
							int coinAmount = input.readInt();
							long createdAt = input.readLong();

							Offer offer = new Offer(
									type,
									playerId,
									slot,
									itemId,
									quantity,
									price
							);
							offer.setReceived(received);
							offer.setAborted(aborted);
							offer.setCollectItemAmount(itemAmount);
							offer.setCollectCoinAmount(coinAmount);
							offer.setCreatedAt(createdAt);

							// we shouldnt be here
							if (offer.isComplete() && offer.getCollectCoinAmount() == 0 && offer.getCollectItemAmount() == 0) {
								System.out.println("OFFER IS COMPLETE - REMOVING OFFER " + offer.getPlayerId() + " " + offer.getItemId() + " " + offer.getQuantity() + " " + (offer.getType() == Type.BUY ? "BUY" : "SELL"));
								continue;
							}

							if (Server.getSetting("localhost") == null) {
								offers.add(offer);
							}
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("loaded " + offers.size() + " ge offers");

	}

	private static boolean checkPlayerCanUse(Player player, int interfaceId, boolean checkInterface) {

		if (checkInterface && player.openInterfaceId != interfaceId) {
			return false;
		}

		if (player.isBanking) {
			return false;
		}

		if (player.storingFamiliarItems) {
			return false;
		}

		if (player.getDuel() != null) {
			return false;
		}

		if (player.inTrade) {
			return false;
		}

		if (player.attributeExists("shop")) {
			return false;
		}

		return true;
	}

	public static boolean usingGrandExchange(Player player) {
		if (player.openInterfaceId == BUY_SELL_ID) {
			return true;
		}

		if (player.openInterfaceId == SELL_ID) {
			return true;
		}

		if (player.openInterfaceId == BUY_ID) {
			return true;
		}

		if (player.openInterfaceId == SELL_COLLECT_ID) {
			return true;
		}

		if (player.openInterfaceId == BUY_COLLECT_ID) {
			return true;
		}

		return false;
	}

}
