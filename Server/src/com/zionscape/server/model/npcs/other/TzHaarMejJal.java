package com.zionscape.server.model.npcs.other;

import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.dialogue.CloseDialogue;
import com.zionscape.server.util.Misc;

import java.util.Optional;

public class TzHaarMejJal {

    private static final int TzHaarMejJal = 2617;
    private static final int fireCape = 6570;

    public static boolean onDialogueOption(Player player, int option) {
        if (player.getDialogueOwner() == null || !player.getDialogueOwner().equals(TzHaarMejJal.class)) {
            return false;
        }

        if(player.getCurrentDialogueId() == 2) {
            switch (option) {
                case 1:
                    player.getPA().sendOptions(Optional.of("Sacrifice your firecape for a chance at TzRek-Jad?"), "Yes, I know I won't get my cape back.", "No, I like my cape!");
                    player.setCurrentDialogueId(3);
                    break;
                case 2:
                    player.resetDialogue();
                    break;
            }
            return true;
        }
        if(player.getCurrentDialogueId() == 3) {
            switch (option) {
                case 1:

                    if(!player.getItems().playerHasItem(fireCape)) {
                        player.resetDialogue();
                        return true;
                    }

                    player.getItems().deleteItem(fireCape, 1);
                    if(Misc.random(40) == 0) {
                        player.getItems().addItem(25074, 1);
                        player.getPA().sendNpcChat(TzHaarMejJal, "You got lucky, congratulations " + player.username + ". The pet is in your inventory.");
                    } else {
                        player.getPA().sendNpcChat(TzHaarMejJal, "You not lucky. Maybe next time, " + player.username + ".");
                    }
                    player.setDialogueOwner(CloseDialogue.class);
                    break;
                case 2:
                    player.resetDialogue();
                    break;
            }
            return true;
        }

        return true;
    }

    public static boolean onDialogueContinue(Player player) {
        if (player.getDialogueOwner() == null || !player.getDialogueOwner().equals(TzHaarMejJal.class)) {
            return false;
        }

        switch (player.getCurrentDialogueId()) {
            case 1:
                player.getPA().sendOptions(Optional.of("Sell your fire cape?"), "Bargain for TzRek-Jad.", "No, keep it.");
                player.setCurrentDialogueId(2);
                break;
        }

        return true;
    }

    public static boolean onNpcClick(Player player, NPC npc, int option) {
        if (npc.type == TzHaarMejJal) {
            player.setDialogueOwner(TzHaarMejJal.class);
            player.setCurrentDialogueId(1);

            if(player.getItems().playerHasItem(fireCape)) {
                player.getPA().sendPlayerChat("I have a fire cape here.");
            } else {
                player.getPA().sendNpcChat(TzHaarMejJal, "When you have a firecape come back to me to have a chance of exchanging for a Tzrek-jad pet.");
                player.setDialogueOwner(CloseDialogue.class);
            }
            return true;
        }
        return false;
    }

}
