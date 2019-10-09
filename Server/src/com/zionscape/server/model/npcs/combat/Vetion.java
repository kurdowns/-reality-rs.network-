package com.zionscape.server.model.npcs.combat;

import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCCombat;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.util.Misc;

public class Vetion extends NPCCombat {

    public boolean spawnedSkeletons;

    public Vetion(NPC npc) {
        super(npc);
    }

    @Override
    public int damageDealtByPlayer(Player player, int damage) {

        for(NPC npc : NPCHandler.npcs) {
            if(npc == null || npc.isDead()) {
                continue;
            }
            if(npc.type == 14387) {
                player.getCombat().resetPlayerAttack();
                return 0;
            }
        }

        return super.damageDealtByPlayer(player, damage);
    }

    @Override
    public void attackedByPlayer(Player player) {
        super.attackedByPlayer(player);

        for(NPC npc : NPCHandler.npcs) {
            if(npc == null || npc.isDead()) {
                continue;
            }
            if(npc.type == 14387) {
                player.getCombat().resetPlayerAttack();
                player.sendMessage("You should kill his companions first.");
                return;
            }
        }
    }

    @Override
    public void attackPlayer(Player player) {
        super.attackPlayer(player);

        if(getNpc().getHP() < 127 && !spawnedSkeletons) {

            NPC npc = NPCHandler.spawnNpc2(14387, getNpc().getX(), getNpc().getY(), getNpc().heightLevel, 1, 50, 10, 250, 200);
            npc.setRespawn(false);
            npc = NPCHandler.spawnNpc2(14387, getNpc().getX(), getNpc().getY(), getNpc().heightLevel, 1, 50, 10, 250, 200);
            npc.setRespawn(false);

            spawnedSkeletons = true;
        }

        if(Misc.random(4) == 0) {
            getNpc().attackType = 2;
            getNpc().projectileId = 2718;
        } else {
            getNpc().attackType = 0;
        }

    }

    @Override
    public int getDamage() {
        return 45;
    }

    @Override
    public int getAttackAnimation() {
        return 5499;
    }
}
