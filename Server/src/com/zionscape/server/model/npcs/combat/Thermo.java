package com.zionscape.server.model.npcs.combat;

import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCCombat;
import com.zionscape.server.model.players.Player;

public class Thermo extends NPCCombat {

    public Thermo(NPC npc) {
        super(npc);
    }

    @Override
    public void attackPlayer(Player player) {
        getNpc().attackType = 2;
        getNpc().projectileId = 2721;
    }

    @Override
    public int getDamage() {
        return 8;
    }
}
