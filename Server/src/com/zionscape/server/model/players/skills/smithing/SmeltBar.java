package com.zionscape.server.model.players.skills.smithing;

public enum SmeltBar {

    BRONZE(436, 1, 438, 1, 2349, 1, 12, new int[]{15147, 15146, 10247, 9110}), IRON(440, 1, 0, 0, 2351, 15, 12, new int[]{15151, 15150, 15149,
            15148}), SILVER(442, 1, 0, 0, 2355, 20, 28, new int[]{15155, 15154, 15153, 15152}), STEEL(440, 1, 453, 1, 2353, 30, 36, new int[]{
            15159, 15158, 15157, 15156}), GOLD(444, 1, 0, 0, 2357, 40, 46, new int[]{15163, 15162, 15161, 15160}), MITHRIL(447, 1, 453, 1, 2359,
            50, 60, new int[]{29017, 29016, 24253, 16062}), ADAMANT(449, 1, 453, 1, 2361, 70, 76, new int[]{29022, 29020, 29019, 29018}), RUNE(
            451, 1, 453, 2, 2363, 85, 100, new int[]{29026, 29025, 29024, 29023});
    private int oreId;
    private int oreAmount;
    private int otherOre;
    private int otherOreAmount;
    private int barId;
    private int levelRequired;
    private int xp;
    private int[] interfaceIds;

    SmeltBar(final int ore, final int oreA, final int otherOre, final int otherOreA, final int bar, final int level, final int xp, final int[] interfaceIds) {
        this.oreId = ore;
        this.oreAmount = oreA;
        this.otherOre = otherOre;
        this.otherOreAmount = otherOreA;
        this.barId = bar;
        this.levelRequired = level;
        this.xp = xp;
        this.interfaceIds = interfaceIds;
    }

    public int getOreId() {
        return oreId;
    }

    public int getOreAmount() {
        return oreAmount;
    }

    public int getOtherOre() {
        return otherOre;
    }

    public int getOtherOreAmount() {
        return otherOreAmount;
    }

    public int getLevelRequired() {
        return levelRequired;
    }

    public int getXp() {
        return xp;
    }

    public int[] getInterfaceIds() {
        return interfaceIds;
    }

    public int getBarId() {
        return barId;
    }

}
