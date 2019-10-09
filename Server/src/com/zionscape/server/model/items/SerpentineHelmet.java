package com.zionscape.server.model.items;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;

public class SerpentineHelmet {

	public static final int UNCHARGED_HELM = 25043;
	public static final int HELM = 25045;
	private static final int CHIESEL = 1755;
	private static final int VISAGE = 25041;
	private static final int ZULRAH_SCALES = 25048;
	private static final int MAX_AMOUNT = 11_000;

	public static boolean itemOnItem(Player player, int firstItemId, int firstItemSlot, int secondItemId, int secondItemSlot) {
		if (firstItemId == CHIESEL && secondItemId == VISAGE) {

			if (player.getActualLevel(PlayerConstants.CRAFTING) < 52) {
				player.sendMessage("This requires level 52 crafting to craft.");
				return true;
			}

			player.getItems().deleteItem(VISAGE, 1);
			player.getItems().addItem(UNCHARGED_HELM, 1);
			player.getPA().addSkillXP(120, PlayerConstants.CRAFTING);

			player.sendMessage("You have crafted a Serpentine Helmet.");
			return true;
		}

		if (firstItemId == UNCHARGED_HELM && secondItemId == ZULRAH_SCALES) {
			int amount = player.getItems().getItemAmount(ZULRAH_SCALES);
			if (amount > MAX_AMOUNT) {
				amount = MAX_AMOUNT;
			}

			player.getItems().deleteItem(ZULRAH_SCALES, amount);
			player.getItems().deleteItem(UNCHARGED_HELM, 1);
			player.getItems().addItem(HELM, 1);

			player.getData().serpentineHelmetCharges = amount;

			player.sendMessage("You charge the helmet with " + amount + " Zulrah scales.");

			return true;
		}

		if (firstItemId == HELM && secondItemId == ZULRAH_SCALES) {

			if (player.getData().serpentineHelmetCharges == MAX_AMOUNT) {
				player.sendMessage("Your Serpentine Helmet is already fully charged.");
				return true;
			}

			int amount = player.getItems().getItemAmount(ZULRAH_SCALES);

			if (amount + player.getData().blowpipeCharges > MAX_AMOUNT) {
				amount = MAX_AMOUNT - player.getData().serpentineHelmetCharges;
			}

			player.getItems().deleteItem(ZULRAH_SCALES, amount);

			player.getData().serpentineHelmetCharges = player.getData().serpentineHelmetCharges + amount;

			player.sendMessage("You charge the helmet with " + amount + " Zulrah scales.");
			return true;
		}

		return false;
	}

	public static boolean onPlayerDroppedItem(Player player, Item item) {
		// uncharge
		if (item.getId() == HELM) {

			if (player.getData().serpentineHelmetCharges > 0) {

				if(!player.getItems().playerHasItem(ZULRAH_SCALES) && player.getItems().freeInventorySlots() == 0) {
					player.sendMessage("You do not have enough inventory space.");
					return true;
				}

				player.getItems().addItem(ZULRAH_SCALES, player.getData().serpentineHelmetCharges);
				player.getData().serpentineHelmetCharges = 0;
			}

			player.getItems().deleteItem(HELM, 1);
			player.getItems().addItem(UNCHARGED_HELM, 1);

			player.sendMessage("You uncharge the Serpentine Helmet.");
			return true;
		}

		return false;
	}

	public static boolean onItemClicked(Player player, int itemId, int itemSlot, int option) {
		if (itemId == UNCHARGED_HELM) {
			if (option == 3) {
				player.sendMessage("The Serpentine Helmet must be charged with Zulrah scales before it can be worn.");
				return true;
			}
		}
		if (itemId == HELM) {
			switch (option) {
				case 2: // check charges
					player.sendMessage("Serpentine Helmet - Charges left: " + player.getData().serpentineHelmetCharges);
					break;
			}
			return true;
		}
		return false;
	}


}
