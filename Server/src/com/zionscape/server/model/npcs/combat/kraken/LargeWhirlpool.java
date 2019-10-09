package com.zionscape.server.model.npcs.combat.kraken;

import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCCombat;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.players.Player;

public class LargeWhirlpool extends NPCCombat {

    public LargeWhirlpool(NPC npc) {
        super(npc);
    }

    @Override
    public void attackedByPlayer(Player player) {

        player.setAttribute("cancel_attacking_npc", true);

        int whirlpoolCount = 0;
        for(NPC npc : NPCHandler.npcs) {
            if(npc == null) {
                continue;
            }
            if(npc.heightLevel == getNpc().heightLevel && npc.type == 492) {
                whirlpoolCount++;
            }
        }

        if(whirlpoolCount > 0) {
            player.sendMessage("Try disturbing the smaller whirlpools first.");
            return;
        }

        getNpc().type = 493;
        getNpc().requestTransform(getNpc().type);
        getNpc().setCombat(new Kraken(getNpc()));
    }

}
