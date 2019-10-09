package com.zionscape.server.plugin.impl.objects;

import com.zionscape.server.events.Listener;
import com.zionscape.server.events.impl.ClickObjectEvent;
import com.zionscape.server.events.impl.DialogueOptionEvent;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.plugin.Plugin;

public class RespectedDonorPortal implements Listener, Plugin {

    @Override
    public void onObjectClicked(ClickObjectEvent event) {
        if(event.getObjectId() == 2465 && event.getObjectLocation().equals(2772, 3865)) {
            event.setHandled(true);

            Player player = event.getPlayer();
            player.setDialogueOwner(RespectedDonorPortal.class);
            player.getPA().sendOptions("Respected boss zone", "Nex", "The Abyss", "Avatar of destruction", "Flask mining");
        }
    }

    @Override
    public void onDialogueOption(DialogueOptionEvent event) {
        Player player = event.getPlayer();

        if (player.getDialogueOwner() == null || !player.getDialogueOwner().equals(RespectedDonorPortal.class)) {
            return;
        }

        event.setHandled(true);

        switch (event.getOption()) {
            case 1:
                player.getPA().spellTeleport(2269, 3324, 0);
                break;
            case 2:
                player.getPA().spellTeleport(2903, 5204, 0);
                break;
            case 3:
                player.getPA().spellTeleport(3040, 4841, 0);
                break;
            case 4:
                player.getPA().spellTeleport(3141, 3832, 0);
                break;
            case 5:
                player.getPA().spellTeleport(3370, 3497, 0);
                break;
        }
    }

}
