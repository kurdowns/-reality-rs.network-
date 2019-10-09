package com.zionscape.server.model.npcs.combat.kraken;

import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCCombat;
import com.zionscape.server.model.players.Player;

public class Tentacle extends NPCCombat {

    public Tentacle(NPC npc) {
        super(npc);
    }

    @Override
    public void attackPlayer(Player player) {
        getNpc().attackType = 2;
    }

    @Override
    public int getDamage() {
        return 2;
    }

    @Override
    public int getAttackAnimation() {
        return 3618;
    }

    @Override
    public boolean overrideProtectionPrayers() {
        return true;
    }
}