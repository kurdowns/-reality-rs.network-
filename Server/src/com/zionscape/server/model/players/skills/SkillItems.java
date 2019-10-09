package com.zionscape.server.model.players.skills;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;

public class SkillItems {

    public static int getLumberJactItemsWorn(Player player) {
        int count = 0;

        // boots
        if(player.equipment[PlayerConstants.FEET] == 10933) {
            count++;
        }

        // chest
        if(player.equipment[PlayerConstants.CHEST] == 10939) {
            count++;
        }

        // legs
        if(player.equipment[PlayerConstants.LEGS] == 10940) {
            count++;
        }

        if(player.equipment[PlayerConstants.HAT] == 10941) {
            count++;
        }

        return count;
    }

    public static int getMiningItemsWorn(Player player) {
        int count = 0;

        // gloves
        if(player.equipment[PlayerConstants.HANDS] == 20787) {
            count++;
        }

        // boots
        if(player.equipment[PlayerConstants.FEET] == 20788) {
            count++;
        }

        // chest
        if(player.equipment[PlayerConstants.CHEST] == 20791) {
            count++;
        }

        // legs
        if(player.equipment[PlayerConstants.LEGS] == 20790) {
            count++;
        }

        // helmet
        if(player.equipment[PlayerConstants.HAT] == 20789) {
            count++;
        }

        return count;
    }

    public static int getHunterItemsWorn(Player player) {
        int count = 0;

        // chest
        if(player.equipment[PlayerConstants.CHEST] == 10037) {
            count++;
        }

        // legs
        if(player.equipment[PlayerConstants.LEGS] == 10035) {
            count++;
        }

        // hat
        if(player.equipment[PlayerConstants.HAT] == 10039) {
            count++;
        }

        return count;
    }

    public static int getFishingItemsWorn(Player player) {
        int count = 0;

        // chest
        if(player.equipment[PlayerConstants.CHEST] == 25086) {
            count++;
        }

        // legs
        if(player.equipment[PlayerConstants.LEGS] == 25087) {
            count++;
        }

        // hat
        if(player.equipment[PlayerConstants.HAT] == 25085) {
            count++;
        }

        // boots
        if(player.equipment[PlayerConstants.FEET] == 25088) {
            count++;
        }

        return count;
    }

    public static int getCookingItemsWorn(Player player) {
        int count = 0;

        // chest
        if(player.equipment[PlayerConstants.CHEST] == 25090) {
            count++;
        }

        // legs
        if(player.equipment[PlayerConstants.LEGS] == 25091) {
            count++;
        }

        // hat
        if(player.equipment[PlayerConstants.HAT] == 25089) {
            count++;
        }

        // gloves
        if(player.equipment[PlayerConstants.HANDS] == 25093) {
            count++;
        }

        // boots
        if(player.equipment[PlayerConstants.FEET] == 25092) {
            count++;
        }

        return count;
    }


}
