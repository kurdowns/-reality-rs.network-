package com.zionscape.server.model.npcs.combat;

import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCCombat;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.util.Misc;

public class Callisto extends NPCCombat {

    private boolean special;

    public Callisto(NPC npc) {
        super(npc);
    }

    @Override
    public void attackPlayer(Player player) {

        special = false;

        if(Misc.random(5) == 0) {
            special = true;
        }

        getNpc().attackType = 0;
    }

    @Override
    public int getAttackAnimation() {
        if(special) {
            return 4921;
        }
        return 4925;
    }

    @Override
    public int getDamage() {

        if(special) {
            return 60;
        }

        return 8;

    }
}
