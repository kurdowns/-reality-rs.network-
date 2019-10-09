package com.zionscape.server.events.impl;

import com.zionscape.server.events.Event;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.players.Player;

public class NpcDiedEvent extends Event {

    private final Player player;
    private final NPC npc;

    public NpcDiedEvent(Player player, NPC npc) {
        this.player = player;
        this.npc = npc;
    }

    public Player getPlayer() {
        return player;
    }

    public NPC getNpc() {
        return npc;
    }

}
