package com.zionscape.server.model.npcs.combat;

import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCCombat;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.util.Misc;

public class Venenatis extends NPCCombat {

    private boolean special = false;

    public Venenatis(NPC npc) {
        super(npc);
    }

    @Override
    public void attackPlayer(Player player) {
        super.attackPlayer(player);

        if(Misc.random(4) == 0) {
            player.getPA().appendPoison(8);
        }

        if(Misc.random(4) == 0) {
            special = true;
            getNpc().attackType = 0;
        } else {
            special = false;
            getNpc().attackType = 2;
            getNpc().projectileId = 2718;
        }

    }

    @Override
    public int getDamage() {
        if(special) {
            return 50;
        }
        return 25;
    }

    @Override
    public int getAttackAnimation() {

        if(special) {
            return 5320;
        }

        return 5319;
    }
}
