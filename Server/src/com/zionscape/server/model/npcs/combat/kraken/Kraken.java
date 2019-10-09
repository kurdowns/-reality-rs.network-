package com.zionscape.server.model.npcs.combat.kraken;

import com.zionscape.server.model.Area;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCCombat;
import com.zionscape.server.model.players.Player;

public class Kraken extends NPCCombat {

    private static final Area area = new Area(3586, 5798, 3706, 5820, -1);

    public static boolean inArea(Location location) {
        return area.inArea(location);
    }

    public Kraken(NPC npc) {
        super(npc);
    }

    @Override
    public void attackPlayer(Player player) {
        getNpc().attackType = 2;
    }

    @Override
    public int getAttackAnimation() {
        return 3991;
    }

    @Override
    public int getDamage() {
        return 28;
    }
}
