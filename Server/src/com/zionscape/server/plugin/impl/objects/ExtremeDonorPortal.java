package com.zionscape.server.plugin.impl.objects;

import com.zionscape.server.events.Listener;
import com.zionscape.server.events.impl.ClickObjectEvent;
import com.zionscape.server.events.impl.DialogueOptionEvent;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.plugin.Plugin;

public class ExtremeDonorPortal implements Listener, Plugin {

    @Override
    public void onObjectClicked(ClickObjectEvent event) {
        if(event.getObjectId() == 2465 && event.getObjectLocation().equals(3040, 4380)) {
            event.setHandled(true);

            Player player = event.getPlayer();
            player.setDialogueOwner(ExtremeDonorPortal.class);
            player.getPA().sendOptions("The Abyss", "Avatar of desctruction");
        }
    }

    @Override
    public void onDialogueOption(DialogueOptionEvent event) {
        Player player = event.getPlayer();

        if (player.getDialogueOwner() == null || !player.getDialogueOwner().equals(ExtremeDonorPortal.class)) {
            return;
        }

        event.setHandled(true);

        switch (event.getOption()) {
            case 1:
                player.getPA().spellTeleport(3040, 4841, 0);
                break;
            case 2:
                player.getPA().spellTeleport(3141, 3832, 0);
                break;
        }
    }

}
