package com.zionscape.server.model.items;

import com.sun.corba.se.impl.orbutil.closure.Constant;
import com.zionscape.server.Config;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;

import java.util.Arrays;

public class TridentOfTheSeas {

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

    public static final int CHARGED_TRIDENT = 25096;
    public static final int UNCHARGED_TRIDENT = 25097;

    private static final int DEATH_RUNE = 560;
    private static final int CHAOS_RUNE = 562;
    private static final int FIRE_RUNE = 554;

    private static final int MAX_CHARGES = 2500;

    public static boolean itemOnItem(Player player, int firstItemId, int firstItemSlot, int secondItemId, int secondItemSlot) {

        if((firstItemId == DEATH_RUNE || firstItemId == CHAOS_RUNE || firstItemId == FIRE_RUNE) && (secondItemId == CHARGED_TRIDENT || secondItemId == UNCHARGED_TRIDENT)) {
            if(player.getData().tridentSeaCharges >= MAX_CHARGES) {
                player.sendMessage("The staff is already fully charged.");
                return true;
            }

            int death = player.getItems().getItemAmount(DEATH_RUNE);
            int chaos = player.getItems().getItemAmount(CHAOS_RUNE);
            int fire = player.getItems().getItemAmount(FIRE_RUNE);
            int coins = player.getItems().getItemAmount(995);

            if(death < 1 || chaos < 1 || fire < 5) {
                player.sendMessage("You do not have the required runes.");
                return true;
            }

            if(coins < 10) {
                player.sendMessage("You do not have enough coins.");
                return true;
            }

            int[] amounts = { death, chaos, fire / 5, coins / 10 };
            Arrays.sort(amounts);

            int charges = amounts[0];
            if(charges > MAX_CHARGES) {
                charges = MAX_CHARGES;
            }

            if(player.getData().tridentSeaCharges + charges > MAX_CHARGES) {
                charges = MAX_CHARGES - player.getData().tridentSeaCharges;
            }

            player.getItems().deleteItem(DEATH_RUNE, charges);
            player.getItems().deleteItem(CHAOS_RUNE, charges);
            player.getItems().deleteItem(FIRE_RUNE, charges * 5);
            player.getItems().deleteItem(995, charges * 10);

            if(secondItemId == UNCHARGED_TRIDENT) {
                player.getItems().deleteItem(UNCHARGED_TRIDENT, 1);
                player.getItems().addItem(CHARGED_TRIDENT, 1);
            }

            player.getData().tridentSeaCharges += charges;
            player.sendMessage("You add " + charges + " charge/s to the Staff.");

            return true;
        }

        return false;
    }

    public static boolean onItemClicked(Player player, int itemId, int itemSlot, int option) {

        if(itemId == CHARGED_TRIDENT) {
            switch (option) {
                case 2: // check charges
                    player.sendMessage("Trident of the Seas - Charges left: " + player.getData().tridentSeaCharges);
                    break;
                case 3: // unload

                    if(player.getPA().getActualLevel(PlayerConstants.MAGIC) < 75) {
                        player.sendMessage("This requires 75 magic to charge.");
                        break;
                    }

                    if(player.getItems().freeInventorySlots() < 3) {
                        player.sendMessage("You need at least 3 inventory spots to unload the staff.");
                        break;
                    }

                    int charges = player.getData().tridentSeaCharges;

                    player.getItems().addItem(DEATH_RUNE, charges);
                    player.getItems().addItem(CHAOS_RUNE, charges);
                    player.getItems().addItem(FIRE_RUNE, charges * 5);
                  //  player.getItems().addItem(995, charges * 10);

                    player.getItems().deleteItem(CHARGED_TRIDENT, 1);
                    player.getItems().addItem(UNCHARGED_TRIDENT, 1);

                    player.getData().tridentSeaCharges = 0;
                    player.sendMessage("You unload the Trident of the seas staff.");
                    break;
            }
            return true;
        }

        return false;
    }


}
