package com.zionscape.server.plugin.impl.items;

import com.zionscape.server.events.Listener;
import com.zionscape.server.events.impl.DialogueContinueEvent;
import com.zionscape.server.events.impl.DialogueOptionEvent;
import com.zionscape.server.events.impl.ItemClickedEvent;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.dialogue.CloseDialogue;
import com.zionscape.server.plugin.Plugin;
import com.zionscape.server.plugin.impl.npcs.SirElitePlugin;

import java.util.Optional;

public class CapeColorChanging implements Listener, Plugin {

    @Override
    public void onItemClicked(ItemClickedEvent event) {
        if((event.getItemId() == 20769 || event.getItemId() == 20767) && event.getOption() == 2) {

            event.setHandled(true);

            Player player = event.getPlayer();

            player.setAttribute("color_item", event.getItemId());

            if(!player.getData().paidColorChangeFee) {
                player.setDialogueOwner(CapeColorChanging.class);
                player.getPA().sendNpcChat(1, "You must pay a one off fee of 25m to change cape colors.");
                return;
            }

            player.getPA().sendColorInterface(event.getItemId());
            player.getPA().showInterface(25300);
        }
    }

    @Override
    public void onDialogueContinue(DialogueContinueEvent event) {
        Player player = event.getPlayer();

        if (player.getDialogueOwner() == null || !player.getDialogueOwner().equals(CapeColorChanging.class)) {
            return;
        }

        event.setHandled(true);

        player.getPA().sendOptions(Optional.of("Pay 25m to change colors?"), "Yes", "No");
    }

    @Override
    public void onDialogueOption(DialogueOptionEvent event) {
        Player player = event.getPlayer();

        if (player.getDialogueOwner() == null || !player.getDialogueOwner().equals(CapeColorChanging.class)) {
            return;
        }

        event.setHandled(true);

        if(event.getOption() == 2) {
            player.resetDialogue();
            return;
        }

        if(event.getOption() == 1) {
            if(!player.getItems().playerHasItem(995, 25_000_000)) {
                player.getPA().sendNpcChat(1, "You do not have 25m.");
                player.setDialogueOwner(CloseDialogue.class);
                return;
            }

            player.getItems().deleteItem(995, 25_000_000);
            player.getData().paidColorChangeFee = true;

            player.getPA().sendNpcChat(1, "You may now change your cape colors as much as you like.");
            player.setDialogueOwner(CloseDialogue.class);
        }
    }

}
