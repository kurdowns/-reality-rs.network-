package com.zionscape.server.model.players.skills;

import com.google.gson.annotations.SerializedName;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.util.Misc;

public class SkillUtils {

    public static boolean checkSkillRequirement(Player player, int skill) {

        SkillRequirements requirements = null;

        switch (skill) {
            case PlayerConstants.AGILITY:
                requirements = SkillRequirements.AGILITY;
                break;
            case PlayerConstants.THIEVING:
                requirements = SkillRequirements.THIEVING;
                break;
            case PlayerConstants.FARMING:
                requirements = SkillRequirements.FARMING;
                break;
            case PlayerConstants.RUNECRAFTING:
                requirements = SkillRequirements.RC;
                break;
            case PlayerConstants.FIREMAKING:
                requirements = SkillRequirements.FIREMAKING;
                break;

        }

        if(requirements == null) {
            return true;
        }

        if(!player.getData().skillRequirements.containsKey(requirements)) {
            player.getData().skillRequirements.put(requirements, 0);
        }

        if(player.getData().skillRequirements.get(requirements) >= requirements.getRequirement()) {
            return true;
        }

        player.getData().skillRequirements.put(requirements, player.getData().skillRequirements.get(requirements) + 1);
        return false;
    }

    /**
     * @param level
     * @param item
     * @return
     */
    public static boolean givePet(int level, int item) {
        return givePet(level, item, 0);
    }

    /**
     * Actual chance is base chance - (level * 25).[1] // http://2007.runescape.wikia.com/wiki/Rock_golem
     *
     * @param level
     * @param item
     * @return
     */
    public static boolean givePet(int level, int item, double reduce) {
        int chance = getPetBaseChance(item);
        int base = chance;

        if (reduce > 0) {
            base -= (int) (Math.round((double) chance * reduce));
        }

        if (base == -1) {
            return false;
        }

        return Misc.random(base - (int) Math.ceil(level * 25)) == 0;
    }

    /**
     * rates were found at the following urls
     * http://2007.runescape.wikia.com/wiki/Heron
     * http://2007.runescape.wikia.com/wiki/Rock_golem
     * http://2007.runescape.wikia.com/wiki/Beaver
     *
     * @param item
     * @return
     */
    private static int getPetBaseChance(int item) {
        switch (item) {

            case 11: // normal burning
                return 100_000;
            case 12: // achey burning
                return 60_000;
            case 13: // oak burning
                return 60_000;
            case 14: // willow burning
                return 50_000;
            case 15: // teak burning
                return 40_000;
            case 16: // maple burning
                return 45_000;
            case 17:  // MAHOGANY burning
                return 40_000;
            case 18: // yew burning
                return 39_000;
            case 19: // magic burning
                return 28_000;

            case 565: // blood
                return 15_000;
            case 560: // death
                return 20_000;
            case 563: // law
                return 25_000;
            case 561: // nat
                return 25_000;
            case 562: // chaos
                return 50_000;
            case 564: // cosmic
                return 50_000;
            case 559: // body
                return 100_000;
            case 554: // fire
                return 100_000;
            case 557: // earth
                return 100_000;
            case 555: // water
                return 100_000;
            case 558: // mind
                return 100_000;
            case 556: // air
                return 100_000;

            case 10: // thieving
                return 30_000;

            case 5: // oak tree farming
                return 30_000;
            case 6: // willow tree farming
                return 30_000;
            case 7: // maple tree farming
                return 30_000;
            case 8: // yew tree farming
                return 15_000;
            case 9: // magic tree farming
                return 5_000;


            case 1: // gnome agility
                return 30_000;
            case 2: // wilderness agility
                return 15_000;
            case 3: // barbarian agility
                return 20_000;
            case 4: // ape atoll
                return 20_000;


            case 10033: // Grey chinchompas
                return 65_000;
            case 10034: // Red chinchompas
                return 30_000;

            /**
             * log ids
             */
            case 1511: // logs
                return 140_000;
            case 1521: // oak
                return 91_000;
            case 1519: // willow
                return 81_000;
            case 1517: // maple
                return 71_918;
            case 1515: // yew
                return 40_000;
            case 1513: // magic
                return 23_000;

            /**
             * raw fish ids
             */
            case 317: // shrimp
            case 321: // anchovies
                return 150_000;
            case 353: // mackerel
            case 341: // cod
            case 363: // bass
                return 109_609;
            case 335: // trout
            case 331: // salmon
                return 94_808;
            case 349: // pike
                return 89_792;
            case 359: // tuna
            case 371: // swordfish
                return 69_885;
            case 377: // lobster
                return 63_129;
            case 7944: // monkfish
                return 46_583;
            case 383: // sharks
                return 27_000;

            /**
             * ore ids
             */
            case 434: // clay
            case 436: // copper
            case 438: // tin
            case 440: // iron
            case 442: // silver
                return 150_600;
            case 453: // coal
            case 444: // gold
                return 65_640;
            case 447: // mithril
                return 45_320;
            case 449: // addy
                return 35_000;
            case 451: //runite
                return 10_377;
        }
        return -1;
    }

    public enum SkillRequirements {
        @SerializedName("AGILITY")
        AGILITY(100),
        @SerializedName("THIEVING")
        THIEVING(300),
        @SerializedName("FARMING")
        FARMING(20),
        @SerializedName("RC")
        RC(75),
        @SerializedName("FIREMAKING")
        FIREMAKING(1500);

        private final int requirement;

        SkillRequirements(int requirement) {
            this.requirement = requirement;
        }

        public int getRequirement() {
            return requirement;
        }
    }

}
