package com.zionscape.server.plugin.impl.objects;

import com.zionscape.server.cache.clientref.ObjectDef;
import com.zionscape.server.events.Listener;
import com.zionscape.server.events.impl.ItemOnObjectEvent;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.plugin.Plugin;

public class WaterFilling implements Listener, Plugin {

    @Override
    public void onItemOnObject(ItemOnObjectEvent event) {
        Player player = event.getPlayer();
        if(event.getItem() == 1925 && event.getObjectId() == 10388) {
            event.setHandled(true);

            player.getItems().deleteItem(event.getItem(), 1);
            player.getItems().addItem(1929, 1);
            player.sendMessage("You fill the bucket with water.");
        }
    }

}
