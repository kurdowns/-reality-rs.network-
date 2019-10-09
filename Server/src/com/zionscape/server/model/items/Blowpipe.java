package com.zionscape.server.model.items;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;

public class Blowpipe {

	public static final int EMPTY_BLOWPIPE = 25038;
	public static final int BLOWPIPE = 25040;
	public static final int ZULRAH_SCALES = 25048;
	private static final int MAX_AMOUNT = 16383;

	private static final int CHIESEL = 1755;
	private static final int TANZANITE_FANG = 25036;


	public static boolean itemOnItem(Player player, int firstItemId, int firstItemSlot, int secondItemId, int secondItemSlot) {
		if (firstItemId == CHIESEL && secondItemId == TANZANITE_FANG) {

			if (player.getActualLevel(PlayerConstants.CRAFTING) < 53) {
				player.sendMessage("This requires level 53 crafting to craft.");
				return true;
			}

			player.getItems().deleteItem(TANZANITE_FANG, 1);
			player.getItems().addItem(EMPTY_BLOWPIPE, 1);

			player.sendMessage("You have crafted a Toxic blowpipe.");
			return true;
		}

		if (firstItemId == EMPTY_BLOWPIPE && secondItemId == ZULRAH_SCALES) {
			int amount = player.getItems().getItemAmount(ZULRAH_SCALES);
			if (amount > MAX_AMOUNT) {
				amount = MAX_AMOUNT;
			}

			player.getItems().deleteItem(ZULRAH_SCALES, amount);
			player.getItems().deleteItem(EMPTY_BLOWPIPE, 1);
			player.getItems().addItem(BLOWPIPE, 1);

			player.getData().blowpipeCharges = amount;

			player.sendMessage("You charge the blowpipe with " + amount + " Zulrah scales.");

			return true;
		}

		if (firstItemId == BLOWPIPE && secondItemId == ZULRAH_SCALES) {

			if (player.getData().blowpipeCharges == MAX_AMOUNT) {
				player.sendMessage("Your Toxic blowpipe is already fully charged.");
				return true;
			}

			int amount = player.getItems().getItemAmount(ZULRAH_SCALES);

			if (amount + player.getData().blowpipeCharges > MAX_AMOUNT) {
				amount = MAX_AMOUNT - player.getData().blowpipeCharges;
			}

			player.getItems().deleteItem(ZULRAH_SCALES, amount);

			player.getData().blowpipeCharges = player.getData().blowpipeCharges + amount;

			player.sendMessage("You charge the blowpipe with " + amount + " Zulrah scales.");
			return true;
		}

		// loading ammo
		if ((firstItemId >= 806 && firstItemId <= 811 || firstItemId == 3093 || firstItemId == 11230) && secondItemId == BLOWPIPE) {
			if (player.getData().blowpipeAmmoId > 0 && player.getData().blowpipeAmmoId != firstItemId) {
				player.sendMessage("Your Toxic blowpipe is loaded with a different type of ammo, please unload it first.");
				return true;
			}

			if (player.getData().blowpipeAmmo >= MAX_AMOUNT) {
				player.sendMessage("Your Toxic blowpipe is already full of ammo.");
				return true;
			}

			int amount = player.getItems().getItemAmount(firstItemId);

			if (amount + player.getData().blowpipeAmmo > MAX_AMOUNT) {
				amount = MAX_AMOUNT - player.getData().blowpipeAmmo;
			}

			player.getItems().deleteItem(firstItemId, amount);
			player.getData().blowpipeAmmo = player.getData().blowpipeAmmo + amount;
			player.getData().blowpipeAmmoId = firstItemId;

			player.getItems().writeBonus();

			player.sendMessage("You load the Toxic blowpipe with " + amount + " darts.");
			return true;
		}

		return false;
	}

	public static boolean onPlayerDroppedItem(Player player, Item item) {

		// uncharge
		if (item.getId() == BLOWPIPE) {

			// inventory space checks first
			if (player.getData().blowpipeCharges > 0) {
				if(!player.getItems().playerHasItem(ZULRAH_SCALES) && player.getItems().freeInventorySlots() == 0) {
					player.sendMessage("You do not have enough inventory space.");
					return true;
				}
			}
			if (player.getData().blowpipeAmmo > 0) {
				if(!player.getItems().playerHasItem(player.getData().blowpipeAmmoId) && player.getItems().freeInventorySlots() == 0) {
					player.sendMessage("You do not have enough inventory space.");
					return true;
				}
			}

			if (player.getData().blowpipeCharges > 0) {
				player.getItems().addItem(ZULRAH_SCALES, player.getData().blowpipeCharges);
				player.getData().blowpipeCharges = 0;
			}

			if (player.getData().blowpipeAmmo > 0) {
				player.getItems().addItem(player.getData().blowpipeAmmoId, player.getData().blowpipeAmmo);
				player.getData().blowpipeAmmo = 0;
				player.getData().blowpipeAmmoId = 0;
			}
			player.getData().blowpipeAmmoId = 0;

			player.getItems().writeBonus();

			player.getItems().deleteItem(BLOWPIPE, 1);
			player.getItems().addItem(EMPTY_BLOWPIPE, 1);

			player.sendMessage("You uncharge the Toxic blowpipe.");
			return true;
		}

		return false;
	}

	public static boolean onItemClicked(Player player, int itemId, int itemSlot, int option) {
		if (itemId == EMPTY_BLOWPIPE) {
			if (option == 3) {
				player.sendMessage("The Toxic blowpipe must be charged with Zulrah scales before it can be worn.");
				return true;
			}
		}
		if (itemId == BLOWPIPE) {
			switch (option) {
				case 2: // check charges
					player.sendMessage("Toxic blowpipe - Charges left: " + player.getData().blowpipeCharges + " Ammo left: " + player.getData().blowpipeAmmo);
					break;
				case 3: // unload
					if (player.getData().blowpipeAmmo == 0) {
						player.sendMessage("Your Toxic blowpipe is not loaded with ammo.");
						break;
					}
					player.getItems().addItem(player.getData().blowpipeAmmoId, player.getData().blowpipeAmmo);
					player.getData().blowpipeAmmo = 0;
					player.getData().blowpipeAmmoId = 0;
					player.getItems().writeBonus();
					player.sendMessage("You unload the Toxic blowpipe.");
					break;
			}
		}
		return false;
	}


}
