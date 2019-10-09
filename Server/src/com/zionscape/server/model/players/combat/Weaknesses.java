package com.zionscape.server.model.players.combat;

public enum Weaknesses {

    NONE("NONE"),MELEE("MELEE"),RANGE("RANGE"),MAGIC("MAGIC"),AIR("AIR"),EARTH("EARTH"),WATER("WATER"),FIRE("FIRE");

    private String name;

    Weaknesses(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Weaknesses stringToWeakness(String str) {
        for(Weaknesses weakness : Weaknesses.values()) {
            if(weakness.getName().equals(str)) {
                return weakness;
            }
        }
        return NONE;
    }

}
