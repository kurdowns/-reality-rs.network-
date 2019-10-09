package com.zionscape.server.plugin.impl.npcs;

import com.zionscape.server.events.Listener;
import com.zionscape.server.events.impl.DialogueContinueEvent;
import com.zionscape.server.events.impl.DialogueOptionEvent;
import com.zionscape.server.events.impl.NpcClickedEvent;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.dialogue.CloseDialogue;
import com.zionscape.server.plugin.Plugin;

import java.util.Optional;

public class BobBarter implements Listener, Plugin {

    private static final int BOB = 6524;

    @Override
    public void onNpcClicked(NpcClickedEvent event) {
        if(event.getNpc().type == BOB && event.getOption() == 3) {
            event.setHandled(true);
            Player player = event.getPlayer();

            player.getPA().sendNpcChat(BOB, "I can combine any 3-dose, 2-dose and 1-dose potions (noted or unnoted) in your inventory into vials containing 4 doses.");
            player.setDialogueOwner(BobBarter.class);
            player.setCurrentDialogueId(1);
        }
    }

    @Override
    public void onDialogueContinue(DialogueContinueEvent event) {
        Player player = event.getPlayer();
        if (player.getDialogueOwner() == null || !player.getDialogueOwner().equals(BobBarter.class)) {
            return;
        }
        event.setHandled(true);
        player.getPA().sendOptions(Optional.of("Decant potions?"), "Yes", "No");
    }

    @Override
    public void onDialogueOption(DialogueOptionEvent event) {
        Player player = event.getPlayer();
        if (player.getDialogueOwner() == null || !player.getDialogueOwner().equals(BobBarter.class)) {
            return;
        }
        event.setHandled(true);
        if(event.getOption() == 1) {
            player.getPA().sendNpcChat(BOB, "I have mixed any potions you have in your inventory.");
            player.setDialogueOwner(CloseDialogue.class);
            player.getPotMixing().combineAll();
        }
        if(event.getOption() == 2) {
            event.getPlayer().resetDialogue();
        }
    }

}
