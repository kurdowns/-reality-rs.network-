package com.zionscape.server.events.impl;

import com.zionscape.server.events.Event;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.players.Player;

public class NpcClickedEvent extends Event {

    private final Player player;
    private final NPC npc;
    private final int option;

    public NpcClickedEvent(Player player, NPC npc, int option) {
        this.player = player;
        this.npc = npc;
        this.option = option;
    }

    public Player getPlayer() {
        return player;
    }

    public NPC getNpc() {
        return npc;
    }

    public int getOption() {
        return option;
    }

}
