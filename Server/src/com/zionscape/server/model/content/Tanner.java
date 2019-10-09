package com.zionscape.server.model.content;

import com.zionscape.server.model.players.Player;

public class Tanner {

	public static final int SOFT_LEATHER_COST = 1000;
	public static final int HARD_LEATHER_COST = 3000;
	public static final int SNAKE_LEATHER_COST = 10000;
	public static final int DRAGON_LEATHER_COST = 10000;

	public static void sendHideExchangeInterface(Player player) {
		player.getPA().removeAllWindows();

		player.getPA().sendFrame246(14769, 175, 1739);
		player.getPA().sendFrame126("@red@Soft leather", 14777);
		player.getPA().sendFrame126("@red@" + SOFT_LEATHER_COST + " coins", 14785);

		player.getPA().sendFrame246(14770, 175, 1739);
		player.getPA().sendFrame126("@red@Hard leather", 14778);
		player.getPA().sendFrame126("@red@" + HARD_LEATHER_COST + " coins", 14786);

		player.getPA().sendFrame246(14771, 175, 6289);
		player.getPA().sendFrame126("@red@Snakeskin", 14779);
		player.getPA().sendFrame126("@red@" + SNAKE_LEATHER_COST + " coins", 14787);

		player.getPA().sendFrame246(14772, 175, 6287);
		player.getPA().sendFrame126("@red@Snakeskin", 14780);
		player.getPA().sendFrame126("@red@" + SNAKE_LEATHER_COST + " coins", 14788);

		player.getPA().sendFrame246(14773, 175, 1753);
		player.getPA().sendFrame126("@red@Green d'hide", 14781);
		player.getPA().sendFrame126("@red@" + DRAGON_LEATHER_COST + " coins", 14789);

		player.getPA().sendFrame246(14774, 175, 1751);
		player.getPA().sendFrame126("@red@Blue d'hide", 14782);
		player.getPA().sendFrame126("@red@" + DRAGON_LEATHER_COST + " coins", 14790);

		player.getPA().sendFrame246(14775, 175, 1749);
		player.getPA().sendFrame126("@red@Red d'hide", 14783);
		player.getPA().sendFrame126("@red@" + DRAGON_LEATHER_COST + " coins", 14791);

		player.getPA().sendFrame246(14776, 175, 1747);
		player.getPA().sendFrame126("@red@Black d'hide", 14784);
		player.getPA().sendFrame126("@red@" + DRAGON_LEATHER_COST + " coins", 14792);

		player.getPA().showInterface(14670);
	}

	public static boolean onActionButton(Player player, int button) {
		switch (button) {
			case 57225: // soft leather
				exchangeHide(player, 1739, 1741, 1, SOFT_LEATHER_COST);
				return true;
			case 57217:
				exchangeHide(player, 1739, 1741, 5, SOFT_LEATHER_COST);
				return true;
			case 57209:
				player.getPA().sendX(57209);
				return true;
			case 57201:
				exchangeHide(player, 1739, 1741, 28, SOFT_LEATHER_COST);
				return true;
			case 57226: // hard leather
				exchangeHide(player, 1739, 1743, 1, HARD_LEATHER_COST);
				return true;
			case 57218:
				exchangeHide(player, 1739, 1743, 5, HARD_LEATHER_COST);
				return true;
			case 57210:
				player.getPA().sendX(57210);
				return true;
			case 57202:
				exchangeHide(player, 1739, 1743, 28, HARD_LEATHER_COST);
				return true;
			case 57227: // Snakeskin
				exchangeHide(player, 6287, 6289, 1, SNAKE_LEATHER_COST);
				return true;
			case 57219:
				exchangeHide(player, 6287, 6289, 5, SNAKE_LEATHER_COST);
				return true;
			case 57211:
				player.getPA().sendX(57211);
				return true;
			case 57203:
				exchangeHide(player, 6287, 6289, 28, SNAKE_LEATHER_COST);
				return true;
			case 57228: // Snakeskin
				exchangeHide(player, 6287, 6289, 1, SNAKE_LEATHER_COST);
				return true;
			case 57220:
				exchangeHide(player, 6287, 6289, 5, SNAKE_LEATHER_COST);
				return true;
			case 57212:
				player.getPA().sendX(57212);
				return true;
			case 57204:
				exchangeHide(player, 6287, 6289, 28, SNAKE_LEATHER_COST);
				return true;
			case 57229: // green d'hide
				exchangeHide(player, 1753, 1745, 1, DRAGON_LEATHER_COST);
				return true;
			case 57221:
				exchangeHide(player, 1753, 1745, 5, DRAGON_LEATHER_COST);
				return true;
			case 57213:
				player.getPA().sendX(57213);
				return true;
			case 57205:
				exchangeHide(player, 1753, 1745, 28, DRAGON_LEATHER_COST);
				return true;
			case 57230: // blue d'hide
				exchangeHide(player, 1751, 2505, 1, DRAGON_LEATHER_COST);
				return true;
			case 57222:
				exchangeHide(player, 1751, 2505, 5, DRAGON_LEATHER_COST);
				return true;
			case 57214:
				player.getPA().sendX(57214);
				return true;
			case 57206:
				exchangeHide(player, 1751, 2505, 28, DRAGON_LEATHER_COST);
				return true;
			case 57231: // red'dhide
				exchangeHide(player, 1749, 2507, 1, DRAGON_LEATHER_COST);
				return true;
			case 57223:
				exchangeHide(player, 1749, 2507, 5, DRAGON_LEATHER_COST);
				return true;
			case 57215:
				player.getPA().sendX(57215);
				return true;
			case 57207:
				exchangeHide(player, 1749, 2507, 28, DRAGON_LEATHER_COST);
				return true;
			case 57232: // black d'hide
				exchangeHide(player, 1747, 2509, 1, DRAGON_LEATHER_COST);
				return true;
			case 57224:
				exchangeHide(player, 1747, 2509, 5, DRAGON_LEATHER_COST);
				return true;
			case 57216:
				player.getPA().sendX(57216);
				return true;
			case 57208:
				exchangeHide(player, 1747, 2509, 28, DRAGON_LEATHER_COST);
				return true;
		}
		return false;
	}

	public static void exchangeHide(Player player, int hide, int leather, int amount, int cost) {
		player.getPA().removeAllWindows();
		int amountHas = player.getItems().getItemAmount(hide);
		if (amount > amountHas) {
			amount = amountHas;
		}
		if (amount <= 0) {
			player.sendMessage("You do not have any hide of this type.");
			return;
		}
		int totalCost = amount * cost;
		if (totalCost > player.getItems().getItemAmount(995)) {
			player.sendMessage("You do not have enough money.");
			return;
		}
		player.getItems().deleteItem(hide, amount);
		player.getItems().addItem(leather, amount);
	}

}
