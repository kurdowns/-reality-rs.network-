package com.zionscape.server.model.items;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;

import java.util.Arrays;

public class TridentOfTheSwamp {

    /**
     *
     * The staff can hold up 2,500 charges when fully charged. Charging the staff requires 1 death rune, 1 chaos rune,
     * 5 fire runes and 10 coins per cast, costing 1,030,000 coins to fully charge it, and 412 per cast.
     * A fully charged Trident requires 2500 death runes, 2500 chaos runes, 12,500 fire runes and 25,000 coins.
     * Both hands must be free in order to charge the weapon. Trident charges can be removed at anytime to recover the runes,
     * however the coins will not be refunded. (requires 75 Magic to charge)
     *
     *
     */

    public static final int CHARGED_TRIDENT = 25099;
    public static final int UNCHARGED_TRIDENT = 25100;

    private static final int DEATH_RUNE = 560;
    private static final int CHAOS_RUNE = 562;
    private static final int FIRE_RUNE = 554;
    private static final int ZULRAH_SCALES = 25048;
    private static final int MAX_CHARGES = 2500;
    private static final int FANG = 25046;

    public static boolean itemOnItem(Player player, int firstItemId, int firstItemSlot, int secondItemId, int secondItemSlot) {


        // making of the trident of the swamp
        if(firstItemId == FANG && secondItemId == TridentOfTheSeas.UNCHARGED_TRIDENT) {
            if(!player.getItems().playerHasItem(1755)) {
                player.sendMessage("This requires a chisel to craft.");
                return true;
            }
            if(player.getPA().getActualLevel(PlayerConstants.CRAFTING) < 59) {
                player.sendMessage("This requires at least 65 crafting to craft.");
                return true;
            }

            if(!player.getItems().playerHasItem(FANG) || !player.getItems().playerHasItem(TridentOfTheSeas.UNCHARGED_TRIDENT)) {
                return true;
            }

            player.getItems().deleteItem(FANG, 1);
            player.getItems().deleteItem(TridentOfTheSeas.UNCHARGED_TRIDENT, 1);
            player.getItems().addItem(UNCHARGED_TRIDENT, 1);

            player.sendMessage("You make a Trident of the swamp.");
            return true;
        }

        if((firstItemId == DEATH_RUNE || firstItemId == CHAOS_RUNE || firstItemId == FIRE_RUNE || firstItemId == ZULRAH_SCALES) && (secondItemId == CHARGED_TRIDENT || secondItemId == UNCHARGED_TRIDENT)) {
            if(player.getData().tridentSwampCharges >= MAX_CHARGES) {
                player.sendMessage("The staff is already fully charged.");
                return true;
            }

            int death = player.getItems().getItemAmount(DEATH_RUNE);
            int chaos = player.getItems().getItemAmount(CHAOS_RUNE);
            int fire = player.getItems().getItemAmount(FIRE_RUNE);
            int scales = player.getItems().getItemAmount(ZULRAH_SCALES);

            if(death < 1 || chaos < 1 || fire < 5) {
                player.sendMessage("You do not have the required runes.");
                return true;
            }

            if(scales < 1) {
                player.sendMessage("You do not have enough zulrah scales.");
                return true;
            }

            int[] amounts = { death, chaos, fire / 5, scales };
            Arrays.sort(amounts);

            int charges = amounts[0];
            if(charges > MAX_CHARGES) {
                charges = MAX_CHARGES;
            }

            if(player.getData().tridentSwampCharges + charges > MAX_CHARGES) {
                charges = MAX_CHARGES - player.getData().tridentSwampCharges;
            }

            player.getItems().deleteItem(DEATH_RUNE, charges);
            player.getItems().deleteItem(CHAOS_RUNE, charges);
            player.getItems().deleteItem(FIRE_RUNE, charges * 5);
            player.getItems().deleteItem(ZULRAH_SCALES, charges);

            if(secondItemId == UNCHARGED_TRIDENT) {
                player.getItems().deleteItem(UNCHARGED_TRIDENT, 1);
                player.getItems().addItem(CHARGED_TRIDENT, 1);
            }

            player.getData().tridentSwampCharges += charges;
            player.sendMessage("You add " + charges + " charge/s to the Staff.");

            return true;
        }

        return false;
    }

    public static boolean onItemClicked(Player player, int itemId, int itemSlot, int option) {

        if(itemId == UNCHARGED_TRIDENT) {
            switch (option) {

                case 2: // check charges
                    player.sendMessage("Trident of the swamp - Charges left: 0");
                    break;

                case 3: // dismantle
                    if(player.getItems().freeInventorySlots() < 6) {
                        player.sendMessage("You need at least 6 inventory spots to dismantle the staff.");
                        break;
                    }

                    int charges = player.getData().tridentSwampCharges;

                    player.getItems().deleteItem(UNCHARGED_TRIDENT, 1);

                    player.getItems().addItem(FANG, 1);
                    player.getItems().addItem(TridentOfTheSeas.UNCHARGED_TRIDENT, 1);
                    player.getItems().addItem(DEATH_RUNE, charges);
                    player.getItems().addItem(CHAOS_RUNE, charges);
                    player.getItems().addItem(FIRE_RUNE, charges * 5);
                    player.getItems().addItem(ZULRAH_SCALES, charges);

                    player.getData().tridentSwampCharges = 0;

                    player.sendMessage("You dismantle the Trident of the swamp staff.");

                    break;
            }
            return true;
        }

        if(itemId == CHARGED_TRIDENT) {
            switch (option) {
                case 2: // check charges
                    player.sendMessage("Trident of the swamp - Charges left: " + player.getData().tridentSwampCharges);
                    break;
                case 3: // unload

                    if(player.getPA().getActualLevel(PlayerConstants.MAGIC) < 75) {
                        player.sendMessage("This requires 75 magic to charge.");
                        break;
                    }

                    if(player.getItems().freeInventorySlots() < 4) {
                        player.sendMessage("You need at least 4 inventory spots to unload the staff.");
                        break;
                    }

                    int charges = player.getData().tridentSwampCharges;

                    player.getItems().addItem(DEATH_RUNE, charges);
                    player.getItems().addItem(CHAOS_RUNE, charges);
                    player.getItems().addItem(FIRE_RUNE, charges * 5);
                    player.getItems().addItem(ZULRAH_SCALES, charges);

                    player.getItems().deleteItem(CHARGED_TRIDENT, 1);
                    player.getItems().addItem(UNCHARGED_TRIDENT, 1);

                    player.getData().tridentSwampCharges = 0;
                    player.sendMessage("You unload the Trident of the swamp staff.");
                    break;
            }
            return true;
        }

        return false;
    }


}
