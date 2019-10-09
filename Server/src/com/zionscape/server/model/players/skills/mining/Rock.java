package com.zionscape.server.model.players.skills.mining;

import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.util.Misc;

public final class Rock {

    private static final Rock[] rocks = {
            new Rock(new int[]{2491}, 1, 5, 1436, 2, 3, false), // Rune ess
            new Rock(new int[]{2109, 2108, 9713, 9711}, 1, 18, 434, 1, 3, false), // clay
            new Rock(new int[]{2090, 2091, 11960, 11961, 29231, 11936, 11937, 31080, 31081, 31082, 9708, 9709, 9710, 11962}, 1, 18, 436, 1, 4, false), // bronze
            new Rock(new int[]{2094, 2095, 11957, 11959, 29226, 29224, 11933, 11934, 31077, 31078, 31079, 9714, 9716, 11958}, 1, 18, 438, 1, 4, false), // tin
            new Rock(new int[]{4541, 4539, 2092, 2093, 11955, 11956, 29222, 37309, 37307, 31071, 31072, 31073, 14856, 14857, 14858, 9717, 9718, 9719}, 15, 35, 440, 3, 6, false), // iron

            new Rock(new int[]{2311, 2100, 2101, 4537, 4538, 4536, 37304, 37305, 37306}, 20, 40, 442, 5, 18, false), // silver
            new Rock(new int[]{2098, 2099, 4542, 37310, 37310, 37312, 31065, 31066, 9722, 9720}, 40, 65, 444, 7, 20, false), // gold
            new Rock(new int[]{2096, 2097, 11931, 11930, 11932, 31070, 31069, 31068, 14850, 14851, 14852, 11963, 11964}, 30, 50, 453, 9, 10, false), // coal
            new Rock(new int[]{2103, 2102, 11942, 11944, 31086, 31087, 31088, 14853, 14854, 14855}, 55, 80, 447, 13, 14, false), // mith
            new Rock(new int[]{2104, 2105, 11939, 29233, 11941, 31085, 31083, 14862, 14863, 14864}, 70, 106, 449, 18, 20, false), // adamant
            new Rock(new int[]{14859, 14860}, 85, 135, 451, 26, 60, false), // rune
            new Rock(new int[]{2111}, 75, 15, 2892, 50, 10, false), // Elemental ore
            new Rock(new int[]{17004, 17005, 17006}, 40, 65, -1, 24, 16, false), // gem rock


            new Rock(new int[]{4027}, 80, 114, 25617, 24, 16, false), // flask rock
    };

    public static int getGemId() {

        int random = Misc.random(100);


        //1625 opal
        // 1627 jade %30
        // 1629 red topaz %25
        // 1623 sapphire %20
        // 1621 emerald %15
        // 1619 ruby - %10
        // 1617 diamond %5
        if (random >= 0 && random <= 28) { // 25
            return 1625;
        }

        if (random >= 29 && random <= 45) { // 15
            return 1627;
        }

        if (random >= 46 && random <= 60) { // 14
            return 1629;
        }

        if (random >= 61 && random <= 73) { // 12
            return 1623;
        }

        if (random >= 74 && random <= 84) { // 10
            return 1621;
        }

        if (random >= 85 && random <= 93) { // 8
            return 1619;
        }

        if (random >= 94 && random <= 100) { // 6
            return 1617;
        }

        return -1;
    }

    private final int[] ids;
    private final int levelRequired;
    private final int xp;
    private final int delay;
    private final int ore;
    private final String name;
    private final int respawnTime;
    private final boolean gemRock;

    private Rock(final int[] ids, final int level, final int xp, final int ore, final int delay, int respawnTime, boolean gemRock) {
        this.ids = ids;
        this.levelRequired = level;
        this.xp = xp;
        this.ore = ore;
        this.delay = delay;
        this.name = ItemUtility.getName(ore);
        this.respawnTime = respawnTime;
        this.gemRock = gemRock;
    }

    public int[] getIds() {
        return ids;
    }

    public int getLevelRequired() {
        return levelRequired;
    }

    public int getXp() {
        return xp;
    }

    public int getDelay() {
        return delay;
    }

    public int getOre() {
        return ore;
    }

    public String getName() {
        return name;
    }

    public static Rock getRock(int id) {
        for (Rock rock : rocks) {
            for (int rockId : rock.getIds()) {
                if (rockId == id) {
                    return rock;
                }
            }
        }
        return null;
    }

    public int getRespawnTime() {
        return respawnTime;
    }
}
