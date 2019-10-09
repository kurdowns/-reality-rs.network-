package com.zionscape.server.model.players.skills.fletching;

import java.util.Arrays;
import java.util.Optional;

import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;

public class Fletching {

	public static boolean itemOnItem(Player player, int firstItemId, int firstItemSlot, int secondItemId,
			int secondItemSlot) {

		if (firstItemId != 314 && secondItemId != 314) {
			return false;
		}

		Optional<Bolts> boltOptional = Arrays.asList(Bolts.values()).stream()
				.filter(x -> x.tipId == firstItemId || x.tipId == secondItemId).findAny();
		if (boltOptional.isPresent()) {
			Bolts bolt = boltOptional.get();

			if (player.getActualLevel(PlayerConstants.FLETCHING) < bolt.levelRequired) {
				player.sendMessage("Level " + bolt.levelRequired + " fletching is required to make these into bolts.");
				return true;
			}

			int amount = player.getItems().getItemAmount(bolt.tipId);
			int featherAmount = player.getItems().getItemAmount(314);

			if (featherAmount < amount) {
				amount = featherAmount;
			}

			player.getItems().deleteItem(bolt.tipId, amount);
			player.getItems().deleteItem(314, amount);

			player.getItems().addItem(bolt.dartId, amount);
			player.getPA().addSkillXP(amount * bolt.xp, PlayerConstants.FLETCHING);

			player.sendMessage("You convert the unfinished bolts into working bolts.");
			return true;
		}

		Optional<Darts> dartOptional = Arrays.asList(Darts.values()).stream()
				.filter(x -> x.tipId == firstItemId || x.tipId == secondItemId).findAny();
		if (dartOptional.isPresent()) {
			Darts dart = dartOptional.get();

			if (player.getActualLevel(PlayerConstants.FLETCHING) < dart.levelRequired) {
				player.sendMessage("Level " + dart.levelRequired + " fletching is required to make these into darts.");
				return true;
			}

			int amount = player.getItems().getItemAmount(dart.tipId);
			int featherAmount = player.getItems().getItemAmount(314);

			if (featherAmount < amount) {
				amount = featherAmount;
			}

			player.getItems().deleteItem(dart.tipId, amount);
			player.getItems().deleteItem(314, amount);

			player.getItems().addItem(dart.dartId, amount);
			player.getPA().addSkillXP(amount * dart.xp, PlayerConstants.FLETCHING);

			player.sendMessage("You convert the dart tips into working darts.");
			return true;
		}

		return false;
	}

	private static int getLogId(Player player) {
		if (player.getAttribute("logId") == null)
			return -1;
		return (Integer) player.getAttribute("logId");
	}

	public static void setLogId(Player player, int id) {
		player.setAttribute("logId", id);
	}

	private static int getIntArray(int[] array1, int[] array2, int bow) {
		int a = 0;
		for (int object : array1) {
			if (object == bow)
				return array2[a];
			a++;
		}
		return -1;
	}

	private static String getStringArray(int[] array1, String[] array2, int bow) {
		int a = 0;
		for (int object : array1) {
			if (object == bow)
				return array2[a];
			a++;
		}
		return "";
	}

	private static int contains(int[] array, int value) {
		for (int i : array)
			if (i == value)
				return value;
		return 0;
	}

	public static boolean isFletchable(Player player, int useItem, int usedItem) {
		// bows
		if (useItem == 946 && usedItem == contains(Constants.LOGS, usedItem)
				|| useItem == contains(Constants.LOGS, useItem) && usedItem == 946)
			return chooseItem(player, (useItem == contains(Constants.LOGS, useItem) ? useItem : usedItem));
		// string bows
		if (useItem == 1777 && usedItem == contains(Constants.UNSTRUNG_BOWS, usedItem)
				|| useItem == contains(Constants.UNSTRUNG_BOWS, useItem) && usedItem == 1777)
			return stringBow(player, (useItem == contains(Constants.UNSTRUNG_BOWS, useItem) ? useItem : usedItem));
		// arrows
		if (useItem == 52 && usedItem == 314 || useItem == 314 && usedItem == 52)
			return createHeadlessArrows(player, 52);
		// head on arrow
		if (useItem == 53 && usedItem == contains(Constants.ARROW_HEADS, usedItem)
				|| useItem == contains(Constants.ARROW_HEADS, useItem) && usedItem == 53)
			return createArrows(player, (useItem == contains(Constants.ARROW_HEADS, useItem) ? useItem : usedItem));
		return false;
	}

	private static boolean createHeadlessArrows(Player player, int item) {
		if (player.isBusy())
			return true;
		int amount = player.getItems().getItemAmount(item);
		if (player.getItems().freeInventorySlots() >= 1) {
			player.getItems().deleteItem(314, amount > 15 ? 15 : amount);
			player.getItems().deleteItem(item, amount > 15 ? 15 : amount);
			player.getItems().addItem(53, amount > 15 ? 15 : amount);
			player.getPA().addSkillXP(100, PlayerConstants.FLETCHING);
			player.sendMessage("You make some headless arrows.");
		} else
			player.sendMessage("You have no space in your inventory");
		return true;
	}

	private static boolean createArrows(Player player, int item) {
		if (player.isBusy())
			return true;

		int amount = player.getItems().getItemAmount(item);

		if (amount > 15) {
			amount = 15;
		}

		for (int i = 0; i < amount; i++) {
			Achievements.progressMade(player, Achievements.Types.FLETCH_500_ARROW);
		}

		if (player.getItems().freeInventorySlots() >= 1) {
			if (player.level[PlayerConstants.FLETCHING] >= getIntArray(Constants.ARROW_HEADS, Constants.ARROW_LEVELS,
					item)) {
				player.getItems().deleteItem(53, amount);
				player.getItems().deleteItem(item, amount);
				player.getItems().addItem(getIntArray(Constants.ARROW_HEADS, Constants.ARROWS, item), amount);

				player.getPA().addSkillXP(getIntArray(Constants.ARROW_HEADS, Constants.ARROW_EXPERIENCE, item) * amount,
						PlayerConstants.FLETCHING);

				player.sendMessage("You make some arrows.");
			} else
				player.sendMessage("You need a fletching level of" + " "
						+ getIntArray(Constants.ARROW_HEADS, Constants.ARROW_LEVELS, item) + " to make these arrows.");
		} else
			player.sendMessage("You have no space in your inventory");
		return true;
	}

	private static boolean stringBow(Player player, int bow) {
		if (player.isBusy())
			return true;
		if (player.level[PlayerConstants.FLETCHING] < getIntArray(Constants.UNSTRUNG_BOWS, Constants.FLETCHING_LEVELS,
				bow)) {
			player.sendMessage("You need a fletching level of" + " "
					+ getIntArray(Constants.UNSTRUNG_BOWS, Constants.FLETCHING_LEVELS, bow) + " to string that bow.");
			return true;
		}
		int bowAmt = player.getItems().amountOfItem(bow), stringAmt = player.getItems().amountOfItem(1777);
		int finalAmount = bowAmt >= stringAmt ? stringAmt : bowAmt <= stringAmt ? bowAmt : 1;
		GameCycleTaskHandler.addEvent(player, container -> {
			
			if (player.getItems().getItemAmount(bow) <= 0 || player.getItems().getItemAmount(1777) <= 0) {
				player.sendMessage("You don't have the items required to string the bow.");
				container.stop();
				return;
			}

			if (player.getItems().freeInventorySlots() < 0) {
				player.sendMessage("You have no space in your inventory.");
				container.stop();
				return;
			}

			player.startAnimation(getIntArray(Constants.UNSTRUNG_BOWS, Constants.STRINGING_ANIMS, bow));
			player.getItems().deleteItem(bow, 1);
			player.getItems().deleteItem(1777, 1);
			player.getItems().addItem(getIntArray(Constants.UNSTRUNG_BOWS, Constants.STRUNG_BOWS, bow), 1);
			player.getPA().addSkillXP(getIntArray(Constants.UNSTRUNG_BOWS, Constants.EXPERIENCE, bow) / 2,
					PlayerConstants.FLETCHING);
			player.sendMessage("You attach the bowstring to the bow.");

			if (++iter == finalAmount) {
				iter = 0;
				container.stop();
				return;
			}
		}, 2);
		return true;
	}

	private static boolean chooseItem(Player player, int log) {
		if (player.isBusy())
			return true;
		player.getPA().removeAllWindows();
		player.getPA().sendFrame164(8880);
		player.getPA().sendFrame246(8883, 200, getIntArray(Constants.LOGS, Constants.LEFT_ITEM, log));
		player.getPA().sendFrame246(8884, 200, (log == 1511 ? 52 : -1));
		player.getPA().sendFrame246(8885, 200, getIntArray(Constants.LOGS, Constants.RIGHT_ITEM, log));
		player.getPA().sendFrame126(getStringArray(Constants.LOGS, Constants.LEFT_ITEM_NAME, log), 8897);
		player.getPA().sendFrame126(log == 1511 ? "Arrow Shaft" : "", 8893);
		player.getPA().sendFrame126(getStringArray(Constants.LOGS, Constants.RIGHT_ITEM_NAME, log), 8889);
		setLogId(player, log);
		return true;
	}

	private static int iter = 0;

	public static boolean startFletching(Player player, int amount, String length) {
		if (player.isBusy())
			return true;
		int unstrungBow;
		int log2 = getLogId(player);
		if (length.equalsIgnoreCase("shortbow"))
			unstrungBow = getIntArray(Constants.LOGS, Constants.LEFT_ITEM, log2);
		else if (length.equalsIgnoreCase("longbow"))
			unstrungBow = getIntArray(Constants.LOGS, Constants.RIGHT_ITEM, log2);
		else
			unstrungBow = 52;
		if (player.level[PlayerConstants.FLETCHING] < getIntArray(Constants.UNSTRUNG_BOWS, Constants.FLETCHING_LEVELS,
				unstrungBow)) {
			player.sendMessage("You need a fletching level of "
					+ getIntArray(Constants.UNSTRUNG_BOWS, Constants.FLETCHING_LEVELS, unstrungBow)
					+ " to fletch that bow.");
			return true;
		}
		final Location playerLocation = player.getLocation();
		player.setBusy(true);
		player.startAnimation(1248);

		GameCycleTaskHandler.addEvent(player, container -> {
			if (log2 == -1 || !player.isBusy() || !playerLocation.equals(player.getLocation())) {
				player.startAnimation(65535);
				player.setBusy(false);
				player.removeAttribute("logId");
				container.stop();
				return;
			}

			if (player.getItems().getItemAmount(log2) <= 0) {
				player.sendMessage("You don't have the item to fletch.");
				player.startAnimation(65535);
				player.setBusy(false);
				player.removeAttribute("logId");
				container.stop();
				return;
			}

			if (player.getItems().freeInventorySlots() < 0) {
				player.sendMessage("You have no space in your inventory.");
				player.startAnimation(65535);
				player.setBusy(false);
				player.removeAttribute("logId");
				container.stop();
				return;
			}

			if (unstrungBow == 72 || unstrungBow == 70) {
				Achievements.progressMade(player, Achievements.Types.FLETCH_750_MAGIC_BOWS);
			}

			player.startAnimation(1248);
			player.getItems().deleteItem(log2, 1);
			player.getItems().addItem(unstrungBow == 52 ? 52 : unstrungBow, unstrungBow == 52 ? 15 : 1);
			player.getPA().addSkillXP(
					unstrungBow == 52 ? 50
							: getIntArray(Constants.UNSTRUNG_BOWS, Constants.EXPERIENCE, unstrungBow) / 2,
					PlayerConstants.FLETCHING);
			if (unstrungBow == 52) {
				player.sendMessage("You fletch some arrow shafts.");
			} else {
				player.sendMessage("You fletch the bow.");
			}

			if (++iter == amount) {
				player.startAnimation(65535);
				player.setBusy(false);
				player.removeAttribute("logId");
				iter = 0;
				container.stop();
				return;
			}
		}, 2);

		return true;
	}

	private enum Bolts {

		BRONZE(9375, 877, 9, 1), IRON(9377, 9140, 39, 2), STEEL(9378, 9141, 46, 4), MITHRIL(9379, 9142, 54,
				5), ADAMANT(9380, 9143, 61, 7), RUNE(9381, 9144, 69, 10);

		private int tipId;
		private int dartId;
		private int levelRequired;
		private int xp;

		Bolts(int tipId, int dartId, int levelRequired, int xp) {
			this.tipId = tipId;
			this.dartId = dartId;
			this.levelRequired = levelRequired;
			this.xp = xp;
		}

	}

	private enum Darts {

		BRONZE(819, 806, 1, 2), IRON(820, 807, 22, 4), STEEL(821, 808, 38, 8), MITHRIL(822, 809, 52, 12), ADAMANT(823,
				810, 67, 15), RUNE(824, 811, 81, 19), DRAGON(11232, 11230, 95, 25);

		private int tipId;
		private int dartId;
		private int levelRequired;
		private int xp;

		Darts(int tipId, int dartId, int levelRequired, int xp) {
			this.tipId = tipId;
			this.dartId = dartId;
			this.levelRequired = levelRequired;
			this.xp = xp;
		}

	}

}
