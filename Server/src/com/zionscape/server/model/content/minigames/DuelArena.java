package com.zionscape.server.model.content.minigames;

import com.google.common.base.Joiner;
import com.zionscape.server.Config;
import com.zionscape.server.Server;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.content.grandexchange.GrandExchange;
import com.zionscape.server.model.items.GameItem;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.tick.Tick;
import com.zionscape.server.util.Misc;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DuelArena {

	public static boolean clickingButton(Player player, int button) {
		switch (button) {
			case 26065: // no forfeit
			case 26040:
				selectRule(player, PlayerConstants.DUEL_SAME_WEAPON, -1);
				return true;
			case 26066: // no movement
			case 26048:
				selectRule(player, PlayerConstants.DUEL_MOVEMENT, -1);
				return true;
			case 26069: // no range
			case 26042:
				selectRule(player, PlayerConstants.DUEL_RANGE, -1);
				return true;
			case 26070: // no melee
			case 26043:
				selectRule(player, PlayerConstants.DUEL_MELEE, -1);
				return true;
			case 26071: // no mage
			case 26041:
				selectRule(player, PlayerConstants.DUEL_MAGE, -1);
				return true;
			case 26072: // no drinks
			case 26045:
				selectRule(player, PlayerConstants.DUEL_DRINKS, -1);
				return true;
			case 26073: // no food
			case 26046:
				selectRule(player, PlayerConstants.DUEL_FOOD, -1);
				return true;
			case 26074: // no prayer
			case 26047:
				selectRule(player, PlayerConstants.DUEL_PRAYER, -1);
				return true;
			case 26076: // obsticals
			case 26075:
				selectRule(player, PlayerConstants.DUEL_OBSTICALS, -1);
				return true;
			case 2158: // fun weapons
			case 2157:
				selectRule(player, PlayerConstants.DUEL_DDS_WHIP, -1);
				return true;
			case 30136: // sp attack
			case 30137:
				selectRule(player, PlayerConstants.DUEL_SPECIAL, -1);
				return true;
			case 53245: // no helm
				selectRule(player, PlayerConstants.DUEL_HELMET, 0);
				return true;
			case 53246: // no cape
				selectRule(player, PlayerConstants.DUEL_CAPE, 1);
				return true;
			case 53247: // no ammy
				selectRule(player, PlayerConstants.DUEL_AMMY, 2);
				return true;
			case 53249: // no weapon.
				selectRule(player, PlayerConstants.DUEL_WEAPON, 3);
				return true;
			case 53250: // no body
				selectRule(player, PlayerConstants.DUEL_BODY, 4);
				return true;
			case 53251: // no shield
				selectRule(player, PlayerConstants.DUEL_SHIELD, 5);
				return true;
			case 53252: // no legs
				selectRule(player, PlayerConstants.DUEL_LEGS, 7);
				return true;
			case 53255: // no gloves
				selectRule(player, PlayerConstants.DUEL_GLOVES, 9);
				return true;
			case 53254: // no boots
				selectRule(player, PlayerConstants.DUEL_BOOTS, 10);
				return true;
			case 53253: // no rings
				selectRule(player, PlayerConstants.DUEL_RINGS, 12);
				return true;
			case 53248: // no arrows
				selectRule(player, PlayerConstants.DUEL_ARROWS, 13);
				return true;
			case 26018: // First screen confirm

				if (player.getDuel().getStage() != Stage.STAKING) {
					break;
				}

				final Player o = PlayerHandler.players[player.getDuel().getpId()];

				player.getDuel().setConfirm(true);

				if (player.getDuel().isConfirm() && o.getDuel().isConfirm()) {
					player.getDuel().setStage(Stage.CONFIRMING_STAKE);
					o.getDuel().setStage(Stage.CONFIRMING_STAKE);
					player.getDuel().setConfirm(false);
					o.getDuel().setConfirm(false);
					confirmDuel(player, o);
					confirmDuel(o, player);
					break;
				}

				if (player.getDuel().confirm && !o.getDuel().confirm) {
					player.getPA().sendFrame126("Waiting for other player...", 6684);
					o.getPA().sendFrame126("Other player has accepted.", 6684);
				}

				if (player.getDuel().isConfirm() && o.getDuel().isConfirm()) {
					startDuel(player, o);
					startDuel(o, player);
					break;
				}

				break;
			case 25120:
				if (player.getDuel().getStage() != Stage.CONFIRMING_STAKE) {
					break;
				}
				final Player o1 = PlayerHandler.players[player.getDuel().getpId()];
				player.getDuel().setConfirm(true);
				if (player.getDuel().isConfirm() && o1.getDuel().isConfirm()) {
					startDuel(player, o1);
					startDuel(o1, player);
				} else {
					player.getPA().sendFrame126("Waiting for other player...", 6571);
					o1.getPA().sendFrame126("Other player has accepted", 6571);
				}
				break;
		}
		return false;
	}

	public static boolean clickingObject(Player player, int objectId) {

		if (objectId == 3203) {

			if (player.getDuel() == null) {
				return false;
			}

			if (player.level[3] <= 0 || player.isDead) {
				return false;
			}

			Player other = PlayerHandler.players[player.getDuel().getpId()];

			if (other == null) {
				return false;
			}

			if (other.level[3] <= 0 || other.isDead) {
				return false;
			}

			endGame(player, false);
			endGame(other, true);

			player.setDuel(null);
			other.setDuel(null);

			return true;
		}

		return false;
	}

	public static void confirmDuel(Player player, Player other) {

		String itemId = "";
		for (final GameItem item : player.getDuel().getStakedItems())
			if (ItemUtility.isStackable(item.id) || ItemUtility.isNote(item.id))
				itemId += player.getItems().getItemName(item.id) + " x " + Misc.format(item.amount) + "\\n";
			else
				itemId += player.getItems().getItemName(item.id) + "\\n";

		player.getPA().sendFrame126(itemId, 6516);

		itemId = "";
		for (final GameItem item : other.getDuel().getStakedItems())
			if (ItemUtility.isStackable(item.id) || ItemUtility.isNote(item.id))
				itemId += player.getItems().getItemName(item.id) + " x " + Misc.format(item.amount) + "\\n";
			else
				itemId += player.getItems().getItemName(item.id) + "\\n";

		player.getPA().sendFrame126(itemId, 6517);
		player.getPA().sendFrame126("", 8242);
		for (int i = 8238; i <= 8253; i++)
			player.getPA().sendFrame126("", i);
		player.getPA().sendFrame126("Hitpoints will be restored.", 8250);
		player.getPA().sendFrame126("Boosted stats will be restored.", 8238);
		/*
		 * if (duelRule[8]) c.getPA().sendFrame126("There will be obstacles in the arena.", 8239);
		 */
		player.getPA().sendFrame126("", 8240);
		player.getPA().sendFrame126("", 8241);

		int index = 0;
		for (int i = 0; i < player.getDuel().getRules().length; i++) {
			if (player.getDuel().getRules()[i]) {

				if (i >= 11) {
					break;
				}

				if (PlayerConstants.RULE_OPTIONS[i].length() == 0) {
					continue;
				}

				player.getPA().sendFrame126("" + PlayerConstants.RULE_OPTIONS[i], PlayerConstants.DUEL_CONFIRM_RULE_PARENT_IDS[index++]);
			}
		}
		player.getPA().sendFrame126("", 6571);
		player.getPA().sendFrame248(6412, 197);
	}

	public static void declineDuel(Player player) {

		if (player.getDuel() == null) {
			return;
		}

		if (player.getDuel().getStage() == Stage.STAKING || player.getDuel().getStage() == Stage.CONFIRMING_STAKE) {
			for (GameItem item : player.getDuel().getStakedItems()) {
				player.getItems().addItem(item.id, item.amount);
			}
			Player other = PlayerHandler.players[player.getDuel().getpId()];
			if (other != null && other.getDuel() != null) {
				for (GameItem item : other.getDuel().getStakedItems()) {
					other.getItems().addItem(item.id, item.amount);
				}
				other.getPA().closeAllWindows();
				other.setDuel(null);
				other.sendMessage("The other player has declined the duel.");
			}

			player.getPA().closeAllWindows();
			player.setDuel(null);
			player.sendMessage("You have declined the duel.");
		}
	}

	private static void duelRewardInterface(Player player, List<GameItem> items) {
		player.getOutStream().createFrameVarSizeWord(53);
		player.getOutStream().writeWord(6822);
		player.getOutStream().writeWord(items.size());
		for (GameItem item : items) {
			if (item.amount > 254) {
				player.getOutStream().writeByte(255);
				player.getOutStream().writeDWord_v2(item.amount);
			} else {
				player.getOutStream().writeByte(item.amount);
			}
			player.getOutStream().writeWordBigEndianA(item.id + 1);
		}
		player.getOutStream().endFrameVarSizeWord();
		player.getPA().showInterface(6733);
		player.flushOutStream();
	}

	private static void endGame(Player player, boolean won) {
		player.getCombat().resetPrayers();

		for (int i = 0; i < player.level.length; i++) {
			player.level[i] = player.getPA().getLevelForXP(player.xp[i]);
			player.getPA().refreshSkill(i);
		}

		GameCycleTaskHandler.stopEvents("overload-damage" + player.username);
		player.setAttribute("cancel_vine", true);

		player.specAmount = 10;
		player.getItems().addSpecialBar(player.equipment[PlayerConstants.WEAPON]);

		player.vengOn = false;
		player.poisonDamage = 0;
		player.getPA().requestUpdates();
		player.getPA().createPlayerHints(10, -1);
		player.getCombat().resetPlayerAttack();
		player.getPA().movePlayer(Config.DUELING_RESPAWN_X + Misc.random(8), Config.DUELING_RESPAWN_Y + Misc.random(4),
				0);

		Player other = PlayerHandler.players[player.getDuel().getpId()];

		if (won) {
			Achievements.progressMade(player, Achievements.Types.WIN_A_DUEL_FIGHT);
			Achievements.progressMade(player, Achievements.Types.WIN_20_DUELS);
			Achievements.progressMade(player, Achievements.Types.WIN_OVER_200_DUELS);

			duelRewardInterface(player, other.getDuel().getStakedItems());
			giveItems(player, other.getDuel().getStakedItems());

			writeLog(player, "won", "against " + other.username, other.getDuel().getStakedItems());
		} else {
			writeLog(player, "lost", "against " + other.username, player.getDuel().getStakedItems());
		}
	}

	private static void giveItems(Player player, List<GameItem> items) {

		List<GameItem> allItems = new ArrayList<>();
		allItems.addAll(player.getDuel().getStakedItems());
		allItems.addAll(items);

		boolean depositedInBank = false;

		for (GameItem item : allItems) {

			int requiredSlots = item.amount;
			boolean stackable = ItemUtility.isStackable(item.id);

			if (stackable) {
				if (player.getItems().playerHasItem(item.id)) {
					requiredSlots = 0;
				} else {
					requiredSlots = 1;
				}
			}

			if (requiredSlots > player.getItems().freeInventorySlots()) {
				player.getBank().depositItem(item.id, item.amount, -1, true, false);
				depositedInBank = true;
			} else {
				player.getItems().addItem(item.id, item.amount);
			}
		}

		if (depositedInBank) {
			player.sendMessage("Some of your stake winnings have been deposited in your bank.");
		}
	}

	private static void openInterface(final Player player, final Player other) {

		player.getPA().sendFrame126("Players must start with the same weapons and cannot switch.", 8260);
		player.getPA().sendFrame126("Same Weapons", 6696);
		other.getPA().sendFrame126("Players must start with the same weapons and cannot switch.", 8260);
		other.getPA().sendFrame126("Same Weapons", 6696);

		player.getPA().sendFrame126("DDS + Whip only", 669);
		other.getPA().sendFrame126("DDS + Whip only", 669);

		player.getPA().sendFrame126("Both players will be allowed to use DDS or Whip only as a weapon.", 8278);
		other.getPA().sendFrame126("Both players will be allowed to use DDS or Whip only as a weapon.", 8278);

		refreshStakeScreen(player, other);
		refreshStakeScreen(other, player);
		player.getPA().sendFrame87(286, 0);
		for (int i = 0; i < player.equipment.length; i++)
			sendDuelEquipment(player, player.equipment[i], player.playerEquipmentN[i], i);

		player.getPA().sendFrame126("Dueling with: " + other.username + " (level-" + other.combatLevel + ")", 6671);
		player.getPA().sendFrame126("", 6684);
		player.getPA().sendFrame248(6575, 3321);
		player.getItems().resetItems(3322);
	}

	public static void playerDied(Player player) {
		final Player o = PlayerHandler.players[player.getDuel().getpId()];
		endGame(player, false);
		endGame(o, true);
		player.setDuel(null);
		o.setDuel(null);
	}

	public static void playerDisconnected(Player player) {
		if (player.getDuel() != null) {
			if (player.getDuel().getStage() == Stage.STAKING || player.getDuel().getStage() == Stage.CONFIRMING_STAKE) {
				declineDuel(player);
			} else if (player.getDuel().getStage() == Stage.FIGHTING) {

				player.absX = Config.DUELING_RESPAWN_X + Misc.random(8);
				player.absY = Config.DUELING_RESPAWN_Y + Misc.random(4);
				player.heightLevel = 0;

				player.specAmount = 10;

				for (int i = 0; i < player.level.length; i++) {
					player.level[i] = player.getPA().getLevelForXP(player.xp[i]);
					player.getPA().refreshSkill(i);
				}

				Player other = PlayerHandler.players[player.getDuel().getpId()];
				endGame(other, true);
				other.setDuel(null);
			}
		}
	}

	public static void refreshStakeScreen(final Player player, final Player other) {
		player.getOutStream().createFrameVarSizeWord(53);
		player.getOutStream().writeWord(6669);
		player.getOutStream().writeWord(player.getDuel().getStakedItems().size());
		int current = 0;
		for (final GameItem item : player.getDuel().getStakedItems()) {
			if (item.amount > 254) {
				player.getOutStream().writeByte(255);
				player.getOutStream().writeDWord_v2(item.amount);
			} else
				player.getOutStream().writeByte(item.amount);
			player.getOutStream().writeWordBigEndianA(item.id + 1);
			current++;
		}
		if (current < 27)
			for (int i = current; i < 28; i++) {
				player.getOutStream().writeByte(1);
				player.getOutStream().writeWordBigEndianA(-1);
			}
		player.getOutStream().endFrameVarSizeWord();
		player.flushOutStream();

		player.getOutStream().createFrameVarSizeWord(53);
		player.getOutStream().writeWord(6670);
		player.getOutStream().writeWord(other.getDuel().getStakedItems().size());
		current = 0;
		for (final GameItem item : other.getDuel().getStakedItems()) {
			if (item.amount > 254) {
				player.getOutStream().writeByte(255);
				player.getOutStream().writeDWord_v2(item.amount);
			} else
				player.getOutStream().writeByte(item.amount);
			player.getOutStream().writeWordBigEndianA(item.id + 1);
			current++;
		}


		if (current < 27)
			for (int i = current; i < 28; i++) {
				player.getOutStream().writeByte(1);
				player.getOutStream().writeWordBigEndianA(-1);
			}
		player.getOutStream().endFrameVarSizeWord();
	}

	public static void removeItem(Player player, int itemID, int amount, int slot) {
		if (player.getDuel() == null || player.getDuel().getStage() != Stage.STAKING) {
			return;
		}

		int count = 0;
		for (final GameItem item : player.getDuel().getStakedItems()) {
			if (item.id == itemID) {
				count += item.amount;
			}
		}
		if (count == 0) {
			return;
		}
		if (amount > count) {
			amount = count;
		}

		Player o = PlayerHandler.players[player.getDuel().getpId()];
		if (player.getItems().freeInventorySlots() - (ItemUtility.isStackable(itemID) ? 1 : amount) < player.getDuel()
				.getDuelSpaceReq()) {
			player.sendMessage("You have too many rules set to remove that item from the stake.");
			return;
		}

		player.getPA().sendFrame126("", 6684);
		o.getPA().sendFrame126("", 6684);

		boolean goodSpace = true;
		if (!ItemUtility.isStackable(itemID)) {
			for (int a = 0; a < amount; a++) {
				for (final GameItem item : player.getDuel().getStakedItems()) {
					if (item.id == itemID) {
						if (!item.stackable) {
							if (player.getItems().freeInventorySlots() - 1 < player.getDuel().getDuelSpaceReq()) {
								goodSpace = false;
								break;
							}
							player.getDuel().getStakedItems().remove(item);
							player.getItems().addItem(itemID, 1);
						} else {
							if (player.getItems().freeInventorySlots() - 1 < player.getDuel().getDuelSpaceReq()) {
								goodSpace = false;
								break;
							}
							if (item.amount > amount) {
								item.amount -= amount;
								player.getItems().addItem(itemID, amount);
							} else {
								if (player.getItems().freeInventorySlots() - 1 < player.getDuel().getDuelSpaceReq()) {
									goodSpace = false;
									break;
								}
								amount = item.amount;
								player.getDuel().getStakedItems().remove(item);
								player.getItems().addItem(itemID, amount);
							}
						}
						break;
					}
				}
			}
		}
		for (final GameItem item : player.getDuel().getStakedItems()) {
			if (item.id == itemID) {
				if (!item.stackable) {
				} else if (item.amount > amount) {
					item.amount -= amount;
					player.getItems().addItem(itemID, amount);
				} else {
					amount = item.amount;
					player.getDuel().getStakedItems().remove(item);
					player.getItems().addItem(itemID, amount);
				}
				break;
			}
		}

		writeLog(player, "removing", "from stake against" + o.username, new GameItem(itemID, amount));

		player.getPA().sendFrame126("", 6684);
		o.getPA().sendFrame126("", 6684);
		player.getDuel().setConfirm(false);
		o.getDuel().setConfirm(false);
		player.getItems().resetItems(3214);
		player.getItems().resetItems(3322);
		o.getItems().resetItems(3214);
		o.getItems().resetItems(3322);
		refreshStakeScreen(player, o);
		refreshStakeScreen(o, player);
		if (!goodSpace) {
			player.sendMessage("You have too many rules set to remove that item.");
		}
	}

	public static void requestDuel(Player player, int pId) {

		if (player.tradeTimer > 0) {
			player.sendMessage("You must wait 15 minutes from the creation of your account to use Duel arena.");
			return;
		}

		if (player.getFamiliar() != null) {
			player.sendMessage("You may not start a duel with a familiar active.");
			return;
		}

		if (GrandExchange.usingGrandExchange(player)) {
			return;
		}

		if (player.ironman) {
			player.sendMessage("You cannot do this on an ironman account.");
			return;
		}

		if (!player.inDuelArena()) {
			player.sendMessage("You cannot duel outside of the duel arena area.");
			return;
		}

		if (player.getId() == pId) {
			player.sendMessage("You cannot duel yourself.");
			return;
		}
		if (pId > PlayerHandler.players.length) {
			player.sendMessage("Invalid player id.");
			return;
		}
		Player o = PlayerHandler.players[pId];
		if (o == null) {
			player.sendMessage("No such player online.");
			return;
		}

		if (!o.inDuelArena()) {
			player.sendMessage("You cannot request a duel with someone outside of duel arena.");
			return;
		}

		if (GrandExchange.usingGrandExchange(o)) {
			return;
		}

		if (o.getDuel() != null && o.getDuel().getStage() != Stage.REQUESTING || o.isBanking || o.inTrade
				|| o.storingFamiliarItems) {
			player.sendMessage("Other player is busy.");
			return;
		}

		// close any existing duels
		if (player.getDuel() != null && (player.getDuel().getStage() == Stage.STAKING || player.getDuel().getStage() == Stage.CONFIRMING_STAKE)) {
			declineDuel(player);
		}

		// were fighting someone we cant accept a duel
		if (player.getDuel() != null && player.getDuel().getStage() == Stage.FIGHTING) {
			return;
		}

		if (o.getDuel() != null && o.getDuel().getStage() == Stage.REQUESTING && o.getDuel().getpId() == player.getId()) {
			player.faceUpdate(pId);
			player.setDuel(new Duel(pId));
			player.getDuel().setStage(Stage.STAKING);
			o.getDuel().setStage(Stage.STAKING);
			openInterface(player, o);
			openInterface(o, player);
		} else {
			player.faceUpdate(pId);
			player.setDuel(new Duel(pId));
			player.sendMessage("Sending duel request...");
			player.lastDuelRequest = System.currentTimeMillis();
			o.sendMessage(player.username + ":duelreq:");
		}
	}

	private static void selectRule(Player player, final int i, final int slot) {

		if (player.getDuel() == null) {
			player.sendMessage("You are not currently in a duel!");
			return;
		}

		if (player.getDuel().getStage() != Stage.STAKING) {
			return;
		}

		switch (i) {
			case PlayerConstants.DUEL_DDS_WHIP:
				Player o = PlayerHandler.players[player.getDuel().getpId()];
				if (player.equipment[PlayerConstants.WEAPON] != 1215 && player.equipment[PlayerConstants.WEAPON] != 1231 && player.equipment[PlayerConstants.WEAPON] != 5680 && player.equipment[PlayerConstants.WEAPON] != 5698 && player.equipment[PlayerConstants.WEAPON] != 4151) {
					player.sendMessage("You can't enable this rule as you're not wearing a dds or whip.");
					return;
				}
				if (o.equipment[PlayerConstants.WEAPON] != 1215 && o.equipment[PlayerConstants.WEAPON] != 1231 && o.equipment[PlayerConstants.WEAPON] != 5680 && o.equipment[PlayerConstants.WEAPON] != 5698 && o.equipment[PlayerConstants.WEAPON] != 4151) {
					player.sendMessage("You can't enable this rule as your opponent is not wearing a dds or whip.");
					return;
				}
				break;
			case PlayerConstants.DUEL_SAME_WEAPON:
				o = PlayerHandler.players[player.getDuel().getpId()];
				if (o.equipment[PlayerConstants.WEAPON] != player.equipment[PlayerConstants.WEAPON]) {
					player.sendMessage("This rule cannot be enabled as your opponent does not have the same weapon.");
					return;
				}
				break;
			case PlayerConstants.DUEL_MAGE:
				if (!player.getDuel().getRules()[PlayerConstants.DUEL_MAGE]
						&& !player.getDuel().getRules()[PlayerConstants.DUEL_RANGE]
						&& player.getDuel().getRules()[PlayerConstants.DUEL_MELEE]) {
					if (player.getDuel().getRules()[PlayerConstants.DUEL_WEAPON]) {
						player.sendMessage("You will not be able to attack each other with this option set!");
						return;
					}
				}
				if (!player.getDuel().getRules()[PlayerConstants.DUEL_MAGE]) {
					if (player.getDuel().getRules()[PlayerConstants.DUEL_MELEE]
							&& player.getDuel().getRules()[PlayerConstants.DUEL_RANGE]) {
						player.sendMessage("You will not be able to attack each other with this option set!");
						return;
					}
				}
				break;
			case PlayerConstants.DUEL_MELEE:
				if (!player.getDuel().getRules()[PlayerConstants.DUEL_MELEE]
						&& !player.getDuel().getRules()[PlayerConstants.DUEL_RANGE]
						&& player.getDuel().getRules()[PlayerConstants.DUEL_RANGE]) {
					if (player.getDuel().getRules()[PlayerConstants.DUEL_WEAPON]) {
						player.sendMessage("You will not be able to attack each other with this option set!");
						return;
					}
				}
				if (!player.getDuel().getRules()[PlayerConstants.DUEL_MELEE]) {
					if (player.getDuel().getRules()[PlayerConstants.DUEL_MAGE]
							&& player.getDuel().getRules()[PlayerConstants.DUEL_RANGE]) {
						player.sendMessage("You will not be able to attack each other with this option set!");
						return;
					}
				}
				break;
			case PlayerConstants.DUEL_RANGE:
				if (!player.getDuel().getRules()[PlayerConstants.DUEL_RANGE]) {
					if (player.getDuel().getRules()[PlayerConstants.DUEL_MELEE]
							&& player.getDuel().getRules()[PlayerConstants.DUEL_MAGE]) {
						player.sendMessage("You will not be able to attack each other with this option set!");
						return;
					}
				}
				break;
		}

		final Player o = PlayerHandler.players[player.getDuel().getpId()];
		if (slot > -1) {

			if (player.equipment[slot] > 0) {
				if (!player.getDuel().getRules()[i]) {
					if (i == PlayerConstants.DUEL_SHIELD) {
						if (ItemUtility.isTwoHanded(player.equipment[PlayerConstants.WEAPON])) {
							player.getDuel().incrementDuelSpaceReq();
						}
					}
					player.getDuel().incrementDuelSpaceReq();
				} else {
					if (i == PlayerConstants.DUEL_SHIELD) {
						if (ItemUtility.isTwoHanded(player.equipment[PlayerConstants.WEAPON])) {
							player.getDuel().decrementDuelSpaceReq();
						}
					}
					player.getDuel().decrementDuelSpaceReq();
				}
			}

			if (o.equipment[slot] > 0) {
				if (!player.getDuel().getRules()[i]) {
					if (i == PlayerConstants.DUEL_SHIELD) {
						if (ItemUtility.isTwoHanded(o.equipment[PlayerConstants.WEAPON])) {
							o.getDuel().incrementDuelSpaceReq();
						}
					}
					o.getDuel().incrementDuelSpaceReq();
				} else {
					if (i == PlayerConstants.DUEL_SHIELD) {
						if (ItemUtility.isTwoHanded(o.equipment[PlayerConstants.WEAPON])) {
							o.getDuel().decrementDuelSpaceReq();
						}
					}
					o.getDuel().decrementDuelSpaceReq();
				}
			}

		}
		if (slot > -1) {
			if (player.getItems().freeInventorySlots() < player.getDuel().getDuelSpaceReq()
					|| o.getItems().freeInventorySlots() < o.getDuel().getDuelSpaceReq()) {
				player.sendMessage("You or your opponent don't have the required space to set this rule.");

				if (player.equipment[slot] > 0) {
					if (i == PlayerConstants.DUEL_WEAPON) {
						if (ItemUtility.isTwoHanded(player.equipment[PlayerConstants.WEAPON])) {
							player.getDuel().decrementDuelSpaceReq();
						}
					}
					player.getDuel().decrementDuelSpaceReq();
				}

				if (o.equipment[slot] > 0) {
					if (i == PlayerConstants.DUEL_WEAPON) {
						if (ItemUtility.isTwoHanded(o.equipment[PlayerConstants.WEAPON])) {
							o.getDuel().decrementDuelSpaceReq();
						}
					}
					o.getDuel().decrementDuelSpaceReq();
				}

				return;
			}
		}

		player.getPA().sendFrame126("The rules have been changed!", 6684);
		o.getPA().sendFrame126("The rules have been changed!", 6684);
		player.getDuel().setConfirm(false);
		o.getDuel().setConfirm(false);

		int duelOption = PlayerConstants.DUEL_RULE_ID[i];

		if (player.getDuel().getRules()[i]) {
			player.getDuel().getRules()[i] = false;
			player.getDuel().setOptions(player.getDuel().getOptions() - duelOption);
			o.getDuel().getRules()[i] = false;
			o.getDuel().setOptions(o.getDuel().getOptions() - duelOption);
		} else {
			player.getDuel().getRules()[i] = true;
			player.getDuel().setOptions(player.getDuel().getOptions() + duelOption);
			o.getDuel().getRules()[i] = true;
			o.getDuel().setOptions(o.getDuel().getOptions() + duelOption);
		}

		player.getPA().sendFrame87(286, player.getDuel().getOptions());
		o.getPA().sendFrame87(286, o.getDuel().getOptions());

		if (player.getDuel().getRules()[PlayerConstants.DUEL_OBSTICALS]) {
			if (player.getDuel().getRules()[PlayerConstants.DUEL_MOVEMENT]) {
				player.getDuel().setDuelTeleX(3366 + Misc.random(12));
				o.getDuel().setDuelTeleX(player.getDuel().getDuelTeleX() - 1);
				player.getDuel().setDuelTeleY(3246 + Misc.random(6));
				o.getDuel().setDuelTeleY(player.getDuel().getDuelTeleY());
			}
		} else {
			if (player.getDuel().getRules()[PlayerConstants.DUEL_MOVEMENT]) {
				player.getDuel().setDuelTeleX(3335 + Misc.random(12));
				o.getDuel().setDuelTeleX(player.getDuel().getDuelTeleX() - 1);
				player.getDuel().setDuelTeleY(3246 + Misc.random(6));
				o.getDuel().setDuelTeleY(player.getDuel().getDuelTeleY());
			}
		}
	}

	private static void sendDuelEquipment(final Player player, final int itemId, final int amount, final int slot) {
		if (itemId != 0) {
			player.getOutStream().createFrameVarSizeWord(34);
			player.getOutStream().writeWord(13824);
			player.getOutStream().writeByte(slot);
			player.getOutStream().writeWord(itemId + 1);
			if (amount > 254) {
				player.getOutStream().writeByte(255);
				player.getOutStream().writeDWord(amount);
			} else
				player.getOutStream().writeByte(amount);
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();
		}
	}

	public static void serverClosed(Player player) {
		if (player.getDuel() != null) {
			if (player.getDuel().getStage() == Stage.STAKING || player.getDuel().getStage() == Stage.CONFIRMING_STAKE) {
				declineDuel(player);
			} else if (player.getDuel().getStage() == Stage.FIGHTING) {

				player.absX = Config.DUELING_RESPAWN_X + Misc.random(8);
				player.absY = Config.DUELING_RESPAWN_Y + Misc.random(4);

				player.heightLevel = 0;
				player.specAmount = 10;

				for (int i = 0; i < player.level.length; i++) {
					player.level[i] = player.getPA().getLevelForXP(player.xp[i]);
					player.getPA().refreshSkill(i);
				}

				for (GameItem item : player.getDuel().getStakedItems()) {
					if (item.id > 0 && item.amount > 0) {
						player.getItems().addItem(item.id, item.amount);
					}
				}
			}
		}
	}

	public static void stakeItem(Player player, final int itemID, int amount, int slot) {
		System.out.println(itemID + " " + amount);

		if (player.getDuel() == null || player.getDuel().getStage() != Stage.STAKING) {
			return;
		}
		if (!player.getItems().playerHasItem(itemID, 1)) {
			return;
		}
		if (amount > player.inventoryN[slot]) {
			amount = player.inventoryN[slot];
		}

		if (amount <= 0)
			return;

		for (int i : Config.UNTRADABLE_ITEM_IDS) {
			if (i == itemID) {
				player.sendMessage("This item cannot be staked.");
				return;
			}
		}

		for (final GameItem item : player.getDuel().getStakedItems()) {
			if (item.id == itemID && (long) amount + (long) item.amount > Integer.MAX_VALUE) {
				amount = Integer.MAX_VALUE - item.amount;
				player.sendMessage("You may only trade a max of " + Misc.formatNumber(Integer.MAX_VALUE) + " gp.");
			}
		}

		if (amount == 0) {
			return;
		}

		Player o = PlayerHandler.players[player.getDuel().getpId()];
		player.getPA().sendFrame126("", 6684);
		o.getPA().sendFrame126("", 6684);
		if (!ItemUtility.isStackable(itemID)) {
			for (int a = 0; a < amount; a++)
				if (player.getItems().playerHasItem(itemID, 1)) {
					System.out.println("ADDING: " + itemID);

					player.getDuel().getStakedItems().add(new GameItem(itemID, 1));
					player.getItems().deleteItem(itemID, player.getItems().getItemSlot(itemID), 1);
				}
		} else if (ItemUtility.isStackable(itemID) || ItemUtility.isNote(itemID)) {
			boolean found = false;
			for (final GameItem item : player.getDuel().getStakedItems())
				if (item.id == itemID) {
					found = true;
					item.amount += amount;
					player.getItems().deleteItem(itemID, slot, amount);
				}
			if (!found) {
				player.getItems().deleteItem(itemID, slot, amount);
				player.getDuel().getStakedItems().add(new GameItem(itemID, amount));
			}
		}

		writeLog(player, "staked", "against " + o.username, new GameItem(itemID, amount));

		player.getPA().sendFrame126("", 6684);
		o.getPA().sendFrame126("", 6684);
		player.getDuel().setConfirm(false);
		o.getDuel().setConfirm(false);

		player.getItems().resetItems(3214);
		player.getItems().resetItems(3322);
		o.getItems().resetItems(3214);
		o.getItems().resetItems(3322);
		refreshStakeScreen(player, o);
		refreshStakeScreen(o, player);
	}

	private static void startDuel(final Player player, Player o) {

		player.headIconHints = 2;
		player.getCombat().resetAllPrayers();

		if (player.getDuel().getRules()[PlayerConstants.DUEL_HELMET])
			player.getItems().removeItem(player.equipment[PlayerConstants.HAT], PlayerConstants.HAT);

		if (player.getDuel().getRules()[PlayerConstants.DUEL_CAPE])
			player.getItems().removeItem(player.equipment[PlayerConstants.CAPE], PlayerConstants.CAPE);

		if (player.getDuel().getRules()[PlayerConstants.DUEL_AMMY])
			player.getItems().removeItem(player.equipment[PlayerConstants.AMULET], PlayerConstants.AMULET);

		if (player.getDuel().getRules()[PlayerConstants.DUEL_ARROWS])
			player.getItems().removeItem(player.equipment[PlayerConstants.ARROW], PlayerConstants.ARROW);

		if (player.getDuel().getRules()[PlayerConstants.DUEL_WEAPON])
			player.getItems().removeItem(player.equipment[PlayerConstants.WEAPON], PlayerConstants.WEAPON);

		if (player.getDuel().getRules()[PlayerConstants.DUEL_BODY])
			player.getItems().removeItem(player.equipment[PlayerConstants.CHEST], PlayerConstants.CHEST);

		if (player.getDuel().getRules()[PlayerConstants.DUEL_SHIELD]) {
			if (ItemUtility.isTwoHanded(player.equipment[PlayerConstants.WEAPON])) {
				player.getItems().removeItem(player.equipment[PlayerConstants.WEAPON], PlayerConstants.WEAPON);
			}
			player.getItems().removeItem(player.equipment[PlayerConstants.SHIELD], PlayerConstants.SHIELD);
		}

		if (player.getDuel().getRules()[PlayerConstants.DUEL_LEGS])
			player.getItems().removeItem(player.equipment[PlayerConstants.LEGS], PlayerConstants.LEGS);

		if (player.getDuel().getRules()[PlayerConstants.DUEL_GLOVES])
			player.getItems().removeItem(player.equipment[PlayerConstants.HANDS], PlayerConstants.HANDS);

		if (player.getDuel().getRules()[PlayerConstants.DUEL_BOOTS])
			player.getItems().removeItem(player.equipment[PlayerConstants.FEET], PlayerConstants.FEET);

		if (player.getDuel().getRules()[PlayerConstants.DUEL_RINGS])
			player.getItems().removeItem(player.equipment[PlayerConstants.RING], PlayerConstants.RING);

		GameCycleTaskHandler.stopEvents("overload-restore" + player.username);
		GameCycleTaskHandler.stopEvents("overload-damage" + player.username);

		player.getPA().sendFrame126("" + o.combatLevel, 6839);
		player.getPA().sendFrame126(o.username, 6840);
		player.getDuel().setStage(Stage.FIGHTING);
		player.getPA().removeAllWindows();

		player.vengOn = false;
		player.doubleHit = false;
		player.usingSpecial = false;
		player.doJavelinSpecial = false;
		player.specAmount = 10;
		player.getItems().addSpecialBar(player.equipment[PlayerConstants.WEAPON]);

		if (player.getDuel().getRules()[PlayerConstants.DUEL_OBSTICALS]) {
			if (player.getDuel().getRules()[PlayerConstants.DUEL_MOVEMENT])
				player.getPA().movePlayer(player.getDuel().getDuelTeleX(), player.getDuel().getDuelTeleY(), 0);
			else
				player.getPA().movePlayer(3366 + Misc.random(12), 3246 + Misc.random(6), 0);
		} else if (player.getDuel().getRules()[PlayerConstants.DUEL_MOVEMENT])
			player.getPA().movePlayer(player.getDuel().getDuelTeleX(), player.getDuel().getDuelTeleY(), 0);
		else
			player.getPA().movePlayer(3335 + Misc.random(12), 3246 + Misc.random(6), 0);

		player.getPA().createPlayerHints(10, o.getId());

		for (int i = 0; i < 20; i++) {
			player.level[i] = player.getPA().getLevelForXP(player.xp[i]);
			player.getPA().refreshSkill(i);
		}

		player.getPA().requestUpdates();

		Server.getTickManager().submit(new Tick(2) {

			int count = 3;

			@Override
			public void execute() {
				if (count != 0) {
					player.forcedChat("" + count);
				} else {
					player.forcedChat("FIGHT!");
					player.getDuel().setStarted(true);
					stop();
				}
				count--;
			}
		});
	}

	private static void writeLog(Player player, String start, String end, GameItem item) {
		try (FileWriter writer = new FileWriter("./data/logs/duels.txt", true)) {
			writer.write("[" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + "] " + player.username + " " + start + " " + item + " "
					+ end + "\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void writeLog(Player player, String start, String end, List<GameItem> items) {
		try (FileWriter writer = new FileWriter("./data/logs/duels.txt", true)) {
			writer.write("[" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + "] " + player.username + " " + start + " "
					+ Joiner.on(", ").join(items) + " " + end + "\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public enum Stage {
		REQUESTING, STAKING, CONFIRMING_STAKE, FIGHTING;
	}

	public static boolean dropItems(Player player) {

		if (player.getDuel() == null) {
			return true;
		}

		if (player.getDuel().getStage() != Stage.FIGHTING) {
			return true;
		}

		return false;
	}

	public static class Duel {

		private final int pId;
		private final List<GameItem> stakedItems = new ArrayList<>();
		private final boolean[] rules = new boolean[22];
		private int options;
		private int duelSpaceReq;
		private boolean confirm;
		private int duelTeleX;
		private int duelTeleY;
		private Stage stage = Stage.REQUESTING;
		private boolean started;

		public Duel(int pId) {
			this.pId = pId;
		}

		public void decrementDuelSpaceReq() {
			this.duelSpaceReq--;
		}

		public int getDuelSpaceReq() {
			return duelSpaceReq;
		}

		public void setDuelSpaceReq(int duelSpaceReq) {
			this.duelSpaceReq = duelSpaceReq;
		}

		public int getDuelTeleX() {
			return duelTeleX;
		}

		public void setDuelTeleX(int duelTeleX) {
			this.duelTeleX = duelTeleX;
		}

		public int getDuelTeleY() {
			return duelTeleY;
		}

		public void setDuelTeleY(int duelTeleY) {
			this.duelTeleY = duelTeleY;
		}

		public int getOptions() {
			return options;
		}

		public void setOptions(int options) {
			this.options = options;
		}

		public int getpId() {
			return pId;
		}

		public boolean[] getRules() {
			return rules;
		}

		public Stage getStage() {
			return stage;
		}

		public void setStage(Stage stage) {
			this.stage = stage;
		}

		public List<GameItem> getStakedItems() {
			return stakedItems;
		}

		public void incrementDuelSpaceReq() {
			this.duelSpaceReq++;
		}

		public boolean isConfirm() {
			return confirm;
		}

		public void setConfirm(boolean confirm) {
			this.confirm = confirm;
		}

		public boolean isStarted() {
			return started;
		}

		public void setStarted(boolean started) {
			this.started = started;
		}

	}

}