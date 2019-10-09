package com.zionscape.server.plugin;

import com.zionscape.server.Server;

public interface Plugin {

    default void onEnable() {
        Server.getEventBus().register(this);
    }

}