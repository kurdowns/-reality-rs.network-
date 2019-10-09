package com.zionscape.server.model.npcs.combat.kraken;

import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCCombat;
import com.zionscape.server.model.players.Player;

public class SmallWhirlpool extends NPCCombat {

    public SmallWhirlpool(NPC npc) {
        super(npc);
    }

    @Override
    public void attackedByPlayer(Player player) {
        getNpc().type = 491;
        getNpc().requestTransform(getNpc().type);
        getNpc().setCombat(new Tentacle(getNpc()));
        getNpc().underAttack = true;
        getNpc().underAttackBy = player.playerId;

        player.setAttribute("cancel_attacking_npc", true);
    }

}
