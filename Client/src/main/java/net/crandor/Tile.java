package net.crandor;

final class Tile extends Node {

    Tile(int i, int j, int k) {
        interactiveObjects = new InteractiveObject[5];
        anIntArray1319 = new int[5];
        anInt1310 = anInt1307 = i;
        anInt1308 = j;
        anInt1309 = k;
    }

    int anInt1307;
    final int anInt1308;
    final int anInt1309;
    final int anInt1310;
    PlainTile plainTile;
    ShapedTile shapedtile;
    WallObject wallObject;
    WallDecoration obj2;
    GroundDecoration groundDecoration;
    GroundItemTile obj4;
    int entityCount;
    final InteractiveObject[] interactiveObjects;
    final int[] anIntArray1319;
    int anInt1320;
    int anInt1321;
    boolean aBoolean1322;
    boolean aBoolean1323;
    boolean aBoolean1324;
    int anInt1325;
    int anInt1326;
    int anInt1327;
    int anInt1328;
    Tile aClass30_Sub3_1329;
}
