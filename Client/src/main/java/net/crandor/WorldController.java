package net.crandor;

final class WorldController {

    private static final int[] anIntArray463 = {53, -53, -53, 53};
    private static final int[] anIntArray464 = {-53, -53, 53, 53};
    private static final int[] anIntArray465 = {-45, 45, 45, -45};
    private static final int[] anIntArray466 = {45, 45, -45, -45};
    private static final int anInt472;
    private static final CullingCluster[] aClass47Array476 = new CullingCluster[500];
    private static final int[] anIntArray478 = {19, 55, 38, 155, 255, 110,
            137, 205, 76};
    private static final int[] anIntArray479 = {160, 192, 80, 96, 0, 144, 80,
            48, 160};
    private static final int[] anIntArray480 = {76, 8, 137, 4, 0, 1, 38, 2, 19};
    private static final int[] anIntArray481 = {0, 0, 2, 0, 0, 2, 1, 1, 0};
    private static final int[] anIntArray482 = {2, 0, 0, 2, 0, 0, 0, 4, 4};
    private static final int[] anIntArray483 = {0, 4, 4, 8, 0, 0, 8, 0, 0};
    private static final int[] anIntArray484 = {1, 1, 0, 0, 0, 8, 0, 0, 8};
    public static boolean lowMem = true;
    public static int clickedTileX = -1;
    public static int clickedTileY = -1;
    private static int anInt446;
    private static int plane;
    private static int anInt448;
    private static int minTileX;
    private static int maxTileX;
    private static int minTileZ;
    private static int maxTileZ;
    private static int xCameraPositionTile;
    private static int yCameraPositionTile;
    private static int xCameraPosition;
    private static int zCameraPosition;
    private static int yCameraPosition;
    private static int yCurveSine;
    private static int yCurveCosine;
    private static int xCurveSine;
    private static int xCurveCosine;
    private static InteractiveObject[] aClass28Array462 = new InteractiveObject[100];
    private static boolean isClicked;
    private static int clickX;
    private static int clickY;
    private static int[] cullingClusterPointer;
    private static CullingCluster[][] cullingClusters;
    private static int anInt475;
    private static NodeList aClass19_477 = new NodeList();
    private static boolean[][] TILE_VISIBILITY_MAPS = new boolean[51][51];//25 + 25 + 1
    private static boolean[][] TILE_VISIBILITY_MAP = new boolean[52][52];//25 + 25 + 2

    static {
        anInt472 = 4;
        cullingClusterPointer = new int[anInt472];
        cullingClusters = new CullingCluster[anInt472][500];
    }

    private final int anInt437;
    private final int xMapSize;
    private final int yMapSize;
    private final int[][][] heightmap;
    private final Tile[][][] tileArray;
    private final InteractiveObject[] obj5Cache;
    private final int[][][] anIntArrayArrayArray445;
    private final int[] anIntArray486;
    private final int[] anIntArray487;
    private final int[][] minimapLayerBoolean = {new int[16],
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1},
            {1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1},
            {0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1}};
    private final int[][] minimapLayerIndex = {
            {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15},
            {12, 8, 4, 0, 13, 9, 5, 1, 14, 10, 6, 2, 15, 11, 7, 3},
            {15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0},
            {3, 7, 11, 15, 2, 6, 10, 14, 1, 5, 9, 13, 0, 4, 8, 12}};
    private boolean opaque_floor_texture = false; // renderPlainTile
    private boolean aBoolean434;
    private int renderFromLevel;
    private int obj5CacheCurrPos;
    private int anInt488;
    public WorldController(int ai[][][]) {
        aBoolean434 = true;
        obj5Cache = new InteractiveObject[5000];
        anIntArray486 = new int[10000];
        anIntArray487 = new int[10000];
        anInt437 = 4;
        xMapSize = 104;
        yMapSize = 104;
        tileArray = new Tile[4][104][104];
        anIntArrayArrayArray445 = new int[4][104 + 1][104 + 1];
        heightmap = ai;
        initToNull();
    }

    public static void nullLoader() {
        aClass28Array462 = null;
        cullingClusterPointer = null;
        cullingClusters = null;
        aClass19_477 = null;
        TILE_VISIBILITY_MAPS = null;
        TILE_VISIBILITY_MAP = null;
    }

    public static void createCullingCluster(int z, int j, int k, int l, int i1, int j1,
                                            int l1, int i2) {
        CullingCluster cullingCluster = new CullingCluster();
        cullingCluster.tileStartX = j / 128;
        cullingCluster.tileEndX = l / 128;
        cullingCluster.tileStartY = l1 / 128;
        cullingCluster.tileEndY = i1 / 128;
        cullingCluster.searchMask = i2;
        cullingCluster.worldStartX = j;
        cullingCluster.worldEndX = l;
        cullingCluster.worldStartY = l1;
        cullingCluster.worldEndY = i1;
        cullingCluster.worldStartZ = j1;
        cullingCluster.worldEndZ = k;
        cullingClusters[z][cullingClusterPointer[z]++] = cullingCluster;
    }

	private static boolean isOnScreen(int tilePosX, int maxTileY, int tileY, int tilePosZ) {
		final int i_20_ = tilePosZ * xCurveCosine - tilePosX * xCurveSine >> 15;
		int i_21_ = maxTileY * yCurveSine + i_20_ * yCurveCosine >> 15;
		if (i_21_ < 1) {
			i_21_ = 1;
		}
		int i_25_ = tileY * yCurveSine + i_20_ * yCurveCosine >> 15;
		if (i_25_ < 1) {
			i_25_ = 1;
		}
		if (i_21_ < 50 && i_25_ < 50 || i_21_ > 3600 && i_25_ > 3600) {
			return false;
		}
		final int i_19_ = tilePosZ * xCurveSine + tilePosX * xCurveCosine >> 15;
		final int i_23_ = (i_19_ * Client.gameAreaWidth) / i_21_;
		final int i_27_ = (i_19_ * Client.gameAreaWidth) / i_25_;
		if (i_23_ < Rasterizer.viewportLeft && i_27_ < Rasterizer.viewportLeft || i_23_ > Rasterizer.viewportRight && i_27_ > Rasterizer.viewportRight) {
			return false;
		}
		final int i_22_ = maxTileY * yCurveCosine - i_20_ * yCurveSine >> 15;
		final int i_24_ = (i_22_ * Client.gameAreaWidth) / i_21_;
		final int i_26_ = tileY * yCurveCosine - i_20_ * yCurveSine >> 15;
		final int i_28_ = (i_26_ * Client.gameAreaWidth) / i_25_;
		if (i_24_ < Rasterizer.viewportTop && i_28_ < Rasterizer.viewportTop || i_24_ > Rasterizer.viewportBottom && i_28_ > Rasterizer.viewportBottom) {
			return false;
		}
		return true;
	}

    public void initToNull() {
        for (int j = 0; j < anInt437; j++) {
            for (int k = 0; k < xMapSize; k++) {
                for (int i1 = 0; i1 < yMapSize; i1++)
                    tileArray[j][k][i1] = null;

            }

        }
        for (int l = 0; l < anInt472; l++) {
            for (int j1 = 0; j1 < cullingClusterPointer[l]; j1++)
                cullingClusters[l][j1] = null;

            cullingClusterPointer[l] = 0;
        }

        for (int k1 = 0; k1 < obj5CacheCurrPos; k1++)
            obj5Cache[k1] = null;

        obj5CacheCurrPos = 0;
        for (int l1 = 0; l1 < aClass28Array462.length; l1++)
            aClass28Array462[l1] = null;

    }

    public void setHeightLevel(int i) {
        renderFromLevel = i;
        for (int k = 0; k < xMapSize; k++) {
            for (int l = 0; l < yMapSize; l++)
                if (tileArray[i][k][l] == null)
                    tileArray[i][k][l] = new Tile(i, k, l);

        }

    }

    public void method276(int i, int j) {
        Tile class30_sub3 = tileArray[0][j][i];
        for (int l = 0; l < 3; l++) {
            Tile class30_sub3_1 = tileArray[l][j][i] = tileArray[l + 1][j][i];
            if (class30_sub3_1 != null) {
                class30_sub3_1.anInt1307--;
                for (int j1 = 0; j1 < class30_sub3_1.entityCount; j1++) {
                    InteractiveObject class28 = class30_sub3_1.interactiveObjects[j1];
                    if ((class28.uid >> 29 & 3) == 2 && class28.anInt523 == j
                            && class28.anInt525 == i)
                        class28.anInt517--;
                }

            }
        }
        if (tileArray[0][j][i] == null)
            tileArray[0][j][i] = new Tile(0, j, i);
        tileArray[0][j][i].aClass30_Sub3_1329 = class30_sub3;
        tileArray[3][j][i] = null;
    }

    public void method278(int i, int j, int k, int l) {
        Tile class30_sub3 = tileArray[i][j][k];
        if (class30_sub3 != null) {
            tileArray[i][j][k].anInt1321 = l;
        }
    }

    public void method279(int z, int x, int y, int opcode, int orientation,
                          int texture, int ya, int yb, int yd, int yc, int shadow_a,
                          int shadow_b, int shadow_d, int shadow_c, int hsl_shadow_a,
                          int hsl_shadow_b, int hsl_shadow_d, int hsl_shadow_c,
                          int rgb_bitset, int rgb, int copy_texture) {
        if (opcode == 0) {
            PlainTile plainTile = new PlainTile(shadow_a, shadow_b, shadow_d,
                    shadow_c, -1, rgb_bitset, false);
            for (int i5 = z; i5 >= 0; i5--)
                if (tileArray[i5][x][y] == null)
                    tileArray[i5][x][y] = new Tile(i5, x, y);
            tileArray[z][x][y].plainTile = plainTile;
            return;
        }
        if (opcode == 1) {
            PlainTile plainTile1 = new PlainTile(hsl_shadow_a, hsl_shadow_b,
                    hsl_shadow_d, hsl_shadow_c, texture, rgb, ya == yb
                    && ya == yd && ya == yc
            );
            for (int j5 = z; j5 >= 0; j5--)
                if (tileArray[j5][x][y] == null)
                    tileArray[j5][x][y] = new Tile(j5, x, y);
            tileArray[z][x][y].plainTile = plainTile1;
            return;
        }
        ShapedTile shapedTile = new ShapedTile(y, hsl_shadow_a, shadow_c, yd, texture,
                hsl_shadow_d, orientation, shadow_a, rgb_bitset, shadow_d, yc,
                yb, ya, opcode, hsl_shadow_c, hsl_shadow_b, shadow_b, x, rgb,
                copy_texture);

        for (int k5 = z; k5 >= 0; k5--)
            if (tileArray[k5][x][y] == null)
                tileArray[k5][x][y] = new Tile(k5, x, y);
        tileArray[z][x][y].shapedtile = shapedTile;
    }

    public void addGroundDecoration(int z, int j, int y, Entity jagexNode,
                                    long i1, int x) {
        if (jagexNode == null)
            return;
        GroundDecoration groundDecoration = new GroundDecoration();
        groundDecoration.aClass30_Sub2_Sub4_814 = jagexNode;
        groundDecoration.xPos = x * 128 + 64;
        groundDecoration.yPos = y * 128 + 64;
        groundDecoration.zPos = j;
        groundDecoration.uid = i1;
        if (tileArray[z][x][y] == null)
            tileArray[z][x][y] = new Tile(z, x, y);
        tileArray[z][x][y].groundDecoration = groundDecoration;
    }

    public void addGroundItemTile(int x, long uid, Entity secondGroundItem, int drawHeight,
                                  Entity thirdGroundItem, Entity firstGroundItem, int z,
                                  int y) {
        GroundItemTile itemTile = new GroundItemTile();
        itemTile.firstGroundItem = firstGroundItem;
        itemTile.xPos = x * 128 + 64;
        itemTile.yPos = y * 128 + 64;
        itemTile.zPos = drawHeight;
        itemTile.uid = uid;
        itemTile.secondGroundItem = secondGroundItem;
        itemTile.thirdGroundItem = thirdGroundItem;
        int j1 = 0;
        Tile class30_sub3 = tileArray[z][x][y];
        if (class30_sub3 != null) {
            for (int k1 = 0; k1 < class30_sub3.entityCount; k1++)
                if ((class30_sub3.interactiveObjects[k1].uid & 0x400000L) == 4194304L && class30_sub3.interactiveObjects[k1].jagexNode instanceof Model) {
                	Model model = ((Model) class30_sub3.interactiveObjects[k1].jagexNode);
                	model.method468();
                    if (j1 < model.highestY)
                        j1 = model.highestY;
                }

        }
        itemTile.anInt52 = j1;
        if (tileArray[z][x][y] == null)
            tileArray[z][x][y] = new Tile(z, x, y);
        tileArray[z][x][y].obj4 = itemTile;
    }

    public void addWallObject(int i, Entity jagexNode, long j, int y,
                              int x, Entity jagexNode2, int i1, int j1, int k1) {
        if (jagexNode == null && jagexNode2 == null)
            return;
        WallObject wallObject = new WallObject();
        wallObject.uid = j;
        wallObject.xPos = x * 128 + 64;
        wallObject.yPos = y * 128 + 64;
        wallObject.zPos = i1;
        wallObject.node1 = jagexNode;
        wallObject.node2 = jagexNode2;
        wallObject.orientation = i;
        wallObject.orientation1 = j1;
        for (int z = k1; z >= 0; z--)
            if (tileArray[z][x][y] == null)
                tileArray[z][x][y] = new Tile(z, x, y);

        tileArray[k1][x][y].wallObject = wallObject;
    }

    public void addWallDecoration(long i, int y, int k, int z, int j1, int k1,
                                  Entity class30_sub2_sub4, int x, int i2, int j2) {
        if (class30_sub2_sub4 == null)
            return;
        WallDecoration class26 = new WallDecoration();
        class26.uid = i;
        class26.xPos = x * 128 + 64 + j1;
        class26.yPos = y * 128 + 64 + i2;
        class26.zPos = k1;
        class26.myEntity = class30_sub2_sub4;
        class26.configBits = j2;
        class26.face = k;
        for (int k2 = z; k2 >= 0; k2--)
            if (tileArray[k2][x][y] == null)
                tileArray[k2][x][y] = new Tile(k2, x, y);

        tileArray[z][x][y].obj2 = class26;
    }

    public boolean addEntityB(long i, int j, int k, Entity class30_sub2_sub4,
                              int l, int i1, int j1, int k1, int l1) {
        if (class30_sub2_sub4 == null) {
            return true;
        } else {
            int i2 = l1 * 128 + 64 * l;
            int j2 = k1 * 128 + 64 * k;
            return method287(i1, l1, k1, l, k, i2, j2, j, class30_sub2_sub4,
                    j1, false, i);
        }
    }

    public boolean addEntity(int i, int j, int k, long l, int i1, int j1,
                             int k1, Entity class30_sub2_sub4, boolean flag) {
        if (class30_sub2_sub4 == null)
            return true;
        int l1 = k1 - j1;
        int i2 = i1 - j1;
        int j2 = k1 + j1;
        int k2 = i1 + j1;
        if (flag) {
            if (j > 5120 && j < 11264)
                k2 += 128;
            if (j > 9216 && j < 15360)
                j2 += 128;
            if (j > 13312 || j < 3072)
                i2 -= 128;
            if (j > 1024 && j < 7168)
                l1 -= 128;
        }
        l1 /= 128;
        i2 /= 128;
        j2 /= 128;
        k2 /= 128;
        return method287(i, l1, i2, (j2 - l1) + 1, (k2 - i2) + 1, k1, i1, k,
                class30_sub2_sub4, j, true, l);
    }

    public boolean method286(int j, int k, Entity class30_sub2_sub4, int l,
                             int i1, int j1, int k1, int l1, int i2, long j2, int k2) {
        return class30_sub2_sub4 == null
                || method287(j, l1, k2, (i2 - l1) + 1, (i1 - k2) + 1, j1, k,
                k1, class30_sub2_sub4, l, true, j2);
    }

    private boolean method287(int i, int j, int k, int l, int i1, int j1,
                              int k1, int l1, Entity class30_sub2_sub4, int i2, boolean flag,
                              long j2) {
        for (int k2 = j; k2 < j + l; k2++) {
            for (int l2 = k; l2 < k + i1; l2++) {
                if (k2 < 0 || l2 < 0 || k2 >= xMapSize || l2 >= yMapSize)
                    return false;
                Tile class30_sub3 = tileArray[i][k2][l2];
                if (class30_sub3 != null && class30_sub3.entityCount >= 5)
                    return false;
            }

        }

        InteractiveObject class28 = new InteractiveObject();
        class28.uid = j2;
        class28.anInt517 = i;
        class28.anInt519 = j1;
        class28.anInt520 = k1;
        class28.anInt518 = l1;
        class28.jagexNode = class30_sub2_sub4;
        class28.anInt522 = i2;
        class28.anInt523 = j;
        class28.anInt525 = k;
        class28.anInt524 = (j + l) - 1;
        class28.anInt526 = (k + i1) - 1;
        for (int i3 = j; i3 < j + l; i3++) {
            for (int j3 = k; j3 < k + i1; j3++) {
                int k3 = 0;
                if (i3 > j)
                    k3++;
                if (i3 < (j + l) - 1)
                    k3 += 4;
                if (j3 > k)
                    k3 += 8;
                if (j3 < (k + i1) - 1)
                    k3 += 2;
                for (int l3 = i; l3 >= 0; l3--)
                    if (tileArray[l3][i3][j3] == null)
                        tileArray[l3][i3][j3] = new Tile(l3, i3, j3);

                Tile class30_sub3_1 = tileArray[i][i3][j3];
                class30_sub3_1.interactiveObjects[class30_sub3_1.entityCount] = class28;
                class30_sub3_1.anIntArray1319[class30_sub3_1.entityCount] = k3;
                class30_sub3_1.anInt1320 |= k3;
                class30_sub3_1.entityCount++;
            }

        }

        if (flag)
            obj5Cache[obj5CacheCurrPos++] = class28;
        return true;
    }

    public void clearObj5Cache() {
        for (int i = 0; i < obj5CacheCurrPos; i++) {
            InteractiveObject object5 = obj5Cache[i];
            method289(object5);
            obj5Cache[i] = null;
        }

        obj5CacheCurrPos = 0;
    }

    private void method289(InteractiveObject class28) {
        for (int j = class28.anInt523; j <= class28.anInt524; j++) {
            for (int k = class28.anInt525; k <= class28.anInt526; k++) {
                Tile class30_sub3 = tileArray[class28.anInt517][j][k];
                if (class30_sub3 != null) {
                    for (int l = 0; l < class30_sub3.entityCount; l++) {
                        if (class30_sub3.interactiveObjects[l] != class28)
                            continue;
                        class30_sub3.entityCount--;
                        for (int i1 = l; i1 < class30_sub3.entityCount; i1++) {
                            class30_sub3.interactiveObjects[i1] = class30_sub3.interactiveObjects[i1 + 1];
                            class30_sub3.anIntArray1319[i1] = class30_sub3.anIntArray1319[i1 + 1];
                        }

                        class30_sub3.interactiveObjects[class30_sub3.entityCount] = null;
                        break;
                    }

                    class30_sub3.anInt1320 = 0;
                    for (int j1 = 0; j1 < class30_sub3.entityCount; j1++)
                        class30_sub3.anInt1320 |= class30_sub3.anIntArray1319[j1];

                }
            }

        }

    }

    public void method290(int i, int k, int l, int i1) {
        Tile class30_sub3 = tileArray[i1][l][i];
        if (class30_sub3 == null)
            return;
        WallDecoration class26 = class30_sub3.obj2;
        if (class26 != null) {
            int j1 = l * 128 + 64;
            int k1 = i * 128 + 64;
            class26.xPos = j1 + ((class26.xPos - j1) * k) / 16;
            class26.yPos = k1 + ((class26.yPos - k1) * k) / 16;
        }
    }

    public void method291(int i, int j, int k, byte byte0) {
        Tile class30_sub3 = tileArray[j][i][k];
        if (byte0 != -119)
            aBoolean434 = !aBoolean434;
        if (class30_sub3 != null) {
            class30_sub3.wallObject = null;
        }
    }

    public void method292(int j, int k, int l) {
        Tile class30_sub3 = tileArray[k][l][j];
        if (class30_sub3 != null) {
            class30_sub3.obj2 = null;
        }
    }

    public void method293(int i, int k, int l) {
        Tile class30_sub3 = tileArray[i][k][l];
        if (class30_sub3 == null)
            return;
        for (int j1 = 0; j1 < class30_sub3.entityCount; j1++) {
            InteractiveObject class28 = class30_sub3.interactiveObjects[j1];
            if ((class28.uid >> 29 & 3) == 2 && class28.anInt523 == k
                    && class28.anInt525 == l) {
                method289(class28);
                return;
            }
        }

    }

    public void method294(int i, int j, int k) {
        Tile class30_sub3 = tileArray[i][k][j];
        if (class30_sub3 == null)
            return;
        class30_sub3.groundDecoration = null;
    }

    public void removeGroundItemTile(int i, int j, int k) {
        Tile class30_sub3 = tileArray[i][j][k];
        if (class30_sub3 != null) {
            class30_sub3.obj4 = null;
        }
    }

    public WallObject method296(int i, int j, int k) {
        Tile class30_sub3 = tileArray[i][j][k];
        if (class30_sub3 == null)
            return null;
        else
            return class30_sub3.wallObject;
    }

    public WallDecoration method297(int i, int k, int l) {
        Tile class30_sub3 = tileArray[l][i][k];
        if (class30_sub3 == null)
            return null;
        else
            return class30_sub3.obj2;
    }

    public InteractiveObject method298(int i, int j, int k) {
        Tile class30_sub3 = tileArray[k][i][j];
        if (class30_sub3 == null)
            return null;
        for (int l = 0; l < class30_sub3.entityCount; l++) {
            InteractiveObject class28 = class30_sub3.interactiveObjects[l];
            if ((class28.uid >> 29 & 0x3L) == 2L && class28.anInt523 == i
                    && class28.anInt525 == j)
                return class28;
        }
        return null;
    }

    public GroundDecoration method299(int i, int j, int k) {
        Tile class30_sub3 = tileArray[k][j][i];
        if (class30_sub3 == null || class30_sub3.groundDecoration == null)
            return null;
        else
            return class30_sub3.groundDecoration;
    }

    public long method300(int i, int j, int k) {
        Tile class30_sub3 = tileArray[i][j][k];
        if (class30_sub3 == null || class30_sub3.wallObject == null) {
            return 0;
        }
        return class30_sub3.wallObject.uid;
    }

    public long method301(int i, int j, int l) {
        Tile class30_sub3 = tileArray[i][j][l];
        if (class30_sub3 == null || class30_sub3.obj2 == null) {
            return 0;
        }
        return class30_sub3.obj2.uid;
    }

    public long method302(int i, int j, int k) {
        Tile class30_sub3 = tileArray[i][j][k];
        if (class30_sub3 == null) {
            return 0;
        }
        for (int l = 0; l < class30_sub3.entityCount; l++) {
            InteractiveObject class28 = class30_sub3.interactiveObjects[l];
            if (class28.anInt523 == j && class28.anInt525 == k) {
                return class28.uid;
            }
        }
        return 0;
    }

    public long method303(int i, int j, int k) {
        Tile class30_sub3 = tileArray[i][j][k];
        if (class30_sub3 == null || class30_sub3.groundDecoration == null) {
            return 0;
        }
        return class30_sub3.groundDecoration.uid;
    }

    public boolean bitPackedMatch(int level, int x, int z, long uid) {
        Tile class30_sub3 = tileArray[level][x][z];
        if (class30_sub3 == null) {
            return false;
        }
        if (class30_sub3.wallObject != null && class30_sub3.wallObject.uid == uid) {
            return true;
        }
        if (class30_sub3.obj2 != null && class30_sub3.obj2.uid == uid) {
            return true;
        }
        if (class30_sub3.groundDecoration != null && class30_sub3.groundDecoration.uid == uid) {
            return true;
        }
        for (int i1 = 0; i1 < class30_sub3.entityCount; i1++) {
            if (class30_sub3.interactiveObjects[i1].uid == uid) {
                return true;
            }
        }
        return false;
    }

	public void method305(int i, int k, int i1) {
		for (int l1 = 0; l1 < anInt437; l1++) {
			for (int i2 = 0; i2 < xMapSize; i2++) {
				for (int j2 = 0; j2 < yMapSize; j2++) {
					Tile class30_sub3 = tileArray[l1][i2][j2];
					if (class30_sub3 != null) {
						WallObject class10 = class30_sub3.wallObject;
						if (class10 != null && class10.node1 instanceof Model) {
							Model model = (Model) class10.node1;
							if (model.vertexNormals != null) {
								method307(l1, 1, 1, i2, j2, model);
								if (class10.node2 instanceof Model) {
									Model model2 = (Model) class10.node2;
									if (model2.vertexNormals != null) {
										method307(l1, 1, 1, i2, j2, model2);
										method308(model, model2, 0, 0, 0, false);
										model2.method480(k, i, i1);
									}
								}
								model.method480(k, i, i1);
							}
						}
						for (int k2 = 0; k2 < class30_sub3.entityCount; k2++) {
							InteractiveObject class28 = class30_sub3.interactiveObjects[k2];
							if (class28 != null && class28.jagexNode instanceof Model) {
								Model model = (Model) class28.jagexNode;
								if (model.vertexNormals != null) {
									method307(l1, (class28.anInt524 - class28.anInt523) + 1, (class28.anInt526 - class28.anInt525) + 1, i2, j2, model);
									model.method480(k, i, i1);
								}
							}
						}

						GroundDecoration class49 = class30_sub3.groundDecoration;
						if (class49 != null && class49.aClass30_Sub2_Sub4_814 instanceof Model) {
							Model model = (Model) class49.aClass30_Sub2_Sub4_814;
							if (model.vertexNormals != null) {
								method306(i2, l1, model, j2);
								model.method480(k, i, i1);
							}
						}
					}
				}

			}

		}

	}

	private void method306(int i, int j, Model model, int k) {
		if (i < xMapSize) {
			Tile class30_sub3 = tileArray[j][i + 1][k];
			if (class30_sub3 != null && class30_sub3.groundDecoration != null && class30_sub3.groundDecoration.aClass30_Sub2_Sub4_814 instanceof Model) {
				Model model2 = (Model) class30_sub3.groundDecoration.aClass30_Sub2_Sub4_814;
				if (model2.vertexNormals != null) {
					method308(model, model2, 128, 0, 0, true);
				}
			}
		}
		if (k < xMapSize) {
			Tile class30_sub3_1 = tileArray[j][i][k + 1];
			if (class30_sub3_1 != null && class30_sub3_1.groundDecoration != null && class30_sub3_1.groundDecoration.aClass30_Sub2_Sub4_814 instanceof Model) {
				Model model2 = (Model) class30_sub3_1.groundDecoration.aClass30_Sub2_Sub4_814;
				if (model2.vertexNormals != null) {
					method308(model, model2, 0, 0, 128, true);
				}
			}
		}
		if (i < xMapSize && k < yMapSize) {
			Tile class30_sub3_2 = tileArray[j][i + 1][k + 1];
			if (class30_sub3_2 != null && class30_sub3_2.groundDecoration != null && class30_sub3_2.groundDecoration.aClass30_Sub2_Sub4_814 instanceof Model) {
				Model model2 = (Model) class30_sub3_2.groundDecoration.aClass30_Sub2_Sub4_814;
				if (model2.vertexNormals != null) {
					method308(model, model2, 128, 0, 128, true);
				}
			}
		}
		if (i < xMapSize && k > 0) {
			Tile class30_sub3_3 = tileArray[j][i + 1][k - 1];
			if (class30_sub3_3 != null && class30_sub3_3.groundDecoration != null && class30_sub3_3.groundDecoration.aClass30_Sub2_Sub4_814 instanceof Model) {
				Model model2 = (Model) class30_sub3_3.groundDecoration.aClass30_Sub2_Sub4_814;
				if (model2.vertexNormals != null) {
					method308(model, model2, 128, 0, -128, true);
				}
			}
		}
	}

	private void method307(int i, int j, int k, int l, int i1, Model model) {
		boolean flag = true;
		int j1 = l;
		int k1 = l + j;
		int l1 = i1 - 1;
		int i2 = i1 + k;
		for (int j2 = i; j2 <= i + 1; j2++)
			if (j2 != anInt437) {
				for (int k2 = j1; k2 <= k1; k2++)
					if (k2 >= 0 && k2 < xMapSize) {
						for (int l2 = l1; l2 <= i2; l2++)
							if (l2 >= 0 && l2 < yMapSize && (!flag || k2 >= k1 || l2 >= i2 || l2 < i1 && k2 != l)) {
								Tile class30_sub3 = tileArray[j2][k2][l2];
								if (class30_sub3 != null) {
									int i3 = (heightmap[j2][k2][l2] + heightmap[j2][k2 + 1][l2] + heightmap[j2][k2][l2 + 1] + heightmap[j2][k2 + 1][l2 + 1]) / 4 - (heightmap[i][l][i1] + heightmap[i][l + 1][i1] + heightmap[i][l][i1 + 1] + heightmap[i][l + 1][i1 + 1]) / 4;
									WallObject class10 = class30_sub3.wallObject;
									if (class10 != null) {
										if (class10.node1 instanceof Model) {
											Model model2 = (Model) class10.node1;
											if (model2.vertexNormals != null) {
												method308(model, model2, (k2 - l) * 128 + (1 - j) * 64, i3, (l2 - i1) * 128 + (1 - k) * 64, flag);
											}
										}
										if (class10.node2 instanceof Model) {
											Model model2 = (Model) class10.node2;
											if (model2.vertexNormals != null) {
												method308(model, model2, (k2 - l) * 128 + (1 - j) * 64, i3, (l2 - i1) * 128 + (1 - k) * 64, flag);
											}
										}
									}
									for (int j3 = 0; j3 < class30_sub3.entityCount; j3++) {
										InteractiveObject class28 = class30_sub3.interactiveObjects[j3];
										if (class28 != null && class28.jagexNode instanceof Model) {
											Model model2 = (Model) class28.jagexNode;
											if (model2.vertexNormals != null) {
												int k3 = (class28.anInt524 - class28.anInt523) + 1;
												int l3 = (class28.anInt526 - class28.anInt525) + 1;
												method308(model, model2, (class28.anInt523 - l) * 128 + (k3 - j) * 64, i3, (class28.anInt525 - i1) * 128 + (l3 - k) * 64, flag);
											}
										}
									}

								}
							}

					}

				j1--;
				flag = false;
			}

	}

    private void method308(Model model, Model model_1, int i, int j, int k,
			boolean flag) {
		model_1.method468();
		anInt488++;
		int l = 0;
		int ai[] = model_1.verticesXCoordinate;
		int i1 = model_1.numberOfVerticeCoordinates;
		for (int j1 = 0; j1 < model.numberOfVerticeCoordinates; j1++) {
			VertexNormal vertexNormal = model.vertexNormals[j1];
			VertexNormal class33_1 = model.vertexNormalOffset[j1];
			if (class33_1.magnitude != 0) {
				int i2 = model.verticesYCoordinate[j1] - j;
				if (i2 <= model_1.lowestY) {
					int j2 = model.verticesXCoordinate[j1] - i;
					if (j2 >= model_1.lowestX && j2 <= model_1.highestX) {
						int k2 = model.verticesZCoordinate[j1] - k;
						if (k2 >= model_1.lowestZ && k2 <= model_1.highestZ) {
							for (int l2 = 0; l2 < i1; l2++) {
								VertexNormal normal = model_1.vertexNormals[l2];
								VertexNormal normalOffset = model_1.vertexNormalOffset[l2];
								if (j2 == ai[l2] && k2 == model_1.verticesZCoordinate[l2] && i2 == model_1.verticesYCoordinate[l2] && normalOffset.magnitude != 0) {
									vertexNormal.x += normalOffset.x;
									vertexNormal.y += normalOffset.y;
									vertexNormal.z += normalOffset.z;
									vertexNormal.magnitude += normalOffset.magnitude;
									normal.x += class33_1.x;
									normal.y += class33_1.y;
									normal.z += class33_1.z;
									normal.magnitude += class33_1.magnitude;
									l++;
									anIntArray486[j1] = anInt488;
									anIntArray487[l2] = anInt488;
								}
							}

						}
					}
				}
			}
		}

		if (l < 3 || !flag)
			return;
		for (int k1 = 0; k1 < model.numberOfTriangleFaces; k1++)
			if (anIntArray486[model.face_a[k1]] == anInt488 && anIntArray486[model.face_b[k1]] == anInt488 && anIntArray486[model.face_c[k1]] == anInt488)
				model.face_render_type[k1] = -1;

		for (int l1 = 0; l1 < model_1.numberOfTriangleFaces; l1++)
			if (anIntArray487[model_1.face_a[l1]] == anInt488 && anIntArray487[model_1.face_b[l1]] == anInt488 && anIntArray487[model_1.face_c[l1]] == anInt488)
				model_1.face_render_type[l1] = -1;
	}

    private void drawMinimapnoneShaded(int pixels[], int pixelPointer, int k, int l, int i1) {
        int j = 512;// was parameter
        Tile currentTile = tileArray[k][l][i1];
        if (currentTile == null)
            return;
        PlainTile plainTile = currentTile.plainTile;
        if (plainTile != null) {
            int tileRGB = plainTile.colourRGB;
            if (tileRGB == 0)
                return;
            for (int linePtr = 0; linePtr < 4; linePtr++) {
                pixels[pixelPointer] = tileRGB;
                pixels[pixelPointer + 1] = tileRGB;
                pixels[pixelPointer + 2] = tileRGB;
                pixels[pixelPointer + 3] = tileRGB;
                pixelPointer += j;
            }

            return;
        }
        ShapedTile shapedtile = currentTile.shapedtile;
        if (shapedtile == null)
            return;
        int l1 = shapedtile.layerBooleanIndex;
        int i2 = shapedtile.layerIndexIndex;
        int j2 = shapedtile.minimapRgb2;
        int k2 = shapedtile.minimapRgb1;
        int ai1[] = minimapLayerBoolean[l1];
        int ai2[] = minimapLayerIndex[i2];
        int l2 = 0;
        if (j2 != 0) {
            for (int i3 = 0; i3 < 4; i3++) {
                pixels[pixelPointer] = ai1[ai2[l2++]] != 0 ? k2 : j2;
                pixels[pixelPointer + 1] = ai1[ai2[l2++]] != 0 ? k2 : j2;
                pixels[pixelPointer + 2] = ai1[ai2[l2++]] != 0 ? k2 : j2;
                pixels[pixelPointer + 3] = ai1[ai2[l2++]] != 0 ? k2 : j2;
                pixelPointer += j;
            }

            return;
        }
        for (int j3 = 0; j3 < 4; j3++) {
            if (ai1[ai2[l2++]] != 0)
                pixels[pixelPointer] = k2;
            if (ai1[ai2[l2++]] != 0)
                pixels[pixelPointer + 1] = k2;
            if (ai1[ai2[l2++]] != 0)
                pixels[pixelPointer + 2] = k2;
            if (ai1[ai2[l2++]] != 0)
                pixels[pixelPointer + 3] = k2;
            pixelPointer += j;
        }

    }

    public void drawMinimapTile(int pixels[], int pixelPointer, int z, int x, int y) {
        if (!Settings.minimapShading) {
            drawMinimapnoneShaded(pixels, pixelPointer, z, x, y);
            return;
        }
        int j = 512;// was parameter
        Tile currentTile = tileArray[z][x][y];
        if (currentTile == null)
            return;
        PlainTile plainTile = currentTile.plainTile;
        if (plainTile != null) {
            int rgb = plainTile.colourRGB;
            if (rgb == 0)
                return;
            if (plainTile.anInt716 != 12345678 && plainTile.anInt718 != 12345678) {
                int hue = plainTile.anInt719 & ~0x7f;
                int c1 = (plainTile.anInt719 & 0x7f);
                int c2 = (plainTile.anInt718 & 0x7f);
                int c3 = (plainTile.anInt716 & 0x7f) - c1;
                int c4 = (plainTile.anInt717 & 0x7f) - c2;
                c1 <<= 2;
                c2 <<= 2;
                for (int py = 0; py < 4; py++) {
                    pixels[pixelPointer] = Rasterizer.hsl2rgb[hue | (c1 >> 2)];
                    pixels[pixelPointer + 1] = Rasterizer.hsl2rgb[hue | (c1 * 3 + c2 >> 4)];
                    pixels[pixelPointer + 2] = Rasterizer.hsl2rgb[hue | (c1 + c2 >> 3)];
                    pixels[pixelPointer + 3] = Rasterizer.hsl2rgb[hue | (c1 + c2 * 3 >> 4)];
                    c1 += c3;
                    c2 += c4;
                    pixelPointer += j;
                }
            } else {
                for (int linePtr = 0; linePtr < 4; linePtr++) {
                    pixels[pixelPointer] = rgb;
                    pixels[pixelPointer + 1] = rgb;
                    pixels[pixelPointer + 2] = rgb;
                    pixels[pixelPointer + 3] = rgb;
                    pixelPointer += j;
                }
            }
        } else {
            ShapedTile shapedtile = currentTile.shapedtile;
            if (shapedtile == null)
                return;
            int l1 = shapedtile.layerBooleanIndex;
            int i2 = shapedtile.layerIndexIndex;
            int j2 = shapedtile.minimapRgb2;
            int k2 = shapedtile.minimapRgb1;
            int ai1[] = minimapLayerBoolean[l1];
            int ai2[] = minimapLayerIndex[i2];
            int l2 = 0;
    	    if (shapedtile.color62 != 12345678) {
    	        int hs1 = shapedtile.color62 & ~0x7f;
    	        int l11 = shapedtile.color92 & 0x7f;
    	        int l21 = shapedtile.color82 & 0x7f;
    	        int l31 = (shapedtile.color62 & 0x7f) - l11;
    	        int l41 = (shapedtile.color72 & 0x7f) - l21;
    	        l11 <<= 2;
    	        l21 <<= 2;
    	        for(int k1 = 0; k1 < 4; k1++) {
    	            if (!ObjectManager.disableGroundTextures) {
    	                if(ai1[ai2[l2++]] != 0) {
    	                    pixels[pixelPointer] = Rasterizer.hsl2rgb[hs1 | (l11 >> 2)];
    	                }
    	                if(ai1[ai2[l2++]] != 0) {
    	                    pixels[pixelPointer + 1] = Rasterizer.hsl2rgb[hs1 | (l11 * 3 + l21 >> 4)];
    	                }
    	                if(ai1[ai2[l2++]] != 0) {
    	                    pixels[pixelPointer + 2] = Rasterizer.hsl2rgb[hs1 | (l11 + l21 >> 3)];
    	                }
    	                if(ai1[ai2[l2++]] != 0) {
    	                    pixels[pixelPointer + 3] = Rasterizer.hsl2rgb[hs1 | (l11 + l21 * 3 >> 4)];
    	                }
    	            } else {
    	                int j1 = k2;
    	                if(ai1[ai2[l2++]] != 0) {
    	                    int lig = 0xff - ((l11 >> 1) * (l11 >> 1) >> 8);
    	                    pixels[pixelPointer] = ((j1 & 0xff00ff) * lig & ~0xff00ff) + ((j1 & 0xff00) * lig & 0xff0000) >> 8;
    	                }
    	                if(ai1[ai2[l2++]] != 0) {
    	                    int lig = 0xff - ((l11 * 3 + l21 >> 3) * (l11 * 3 + l21 >> 3) >> 8);
    	                    pixels[pixelPointer + 1] = ((j1 & 0xff00ff) * lig & ~0xff00ff) + ((j1 & 0xff00) * lig & 0xff0000) >> 8;
    	                }
    	                if(ai1[ai2[l2++]] != 0) {
    	                    int lig = 0xff - ((l11 + l21 >> 2) * (l11 + l21 >> 2) >> 8);
    	                    pixels[pixelPointer + 2] = ((j1 & 0xff00ff) * lig & ~0xff00ff) + ((j1 & 0xff00) * lig & 0xff0000) >> 8;
    	                }
    	                if(ai1[ai2[l2++]] != 0) {
    	                    int lig = 0xff - ((l11 + l21 * 3 >> 3) * (l11 + l21 * 3 >> 3) >> 8);
    	                    pixels[pixelPointer + 3] = ((j1 & 0xff00ff) * lig & ~0xff00ff) + ((j1 & 0xff00) * lig & 0xff0000) >> 8;
    	                }
    	            }
    	            l11 += l31;
    	            l21 += l41;
    	            pixelPointer += 512;
    	        }
    	        if (j2 != 0 && shapedtile.color61 != 12345678) {
    	            pixelPointer -= 512 << 2;
    	            l2 -= 16;
    	            hs1 = shapedtile.color61 & ~0x7f;
    	            l11 = shapedtile.color91 & 0x7f;
    	            l21 = shapedtile.color81 & 0x7f;
    	            l31 = (shapedtile.color61 & 0x7f) - l11;
    	            l41 = (shapedtile.color71 & 0x7f) - l21;
    	            l11 <<= 2;
    	            l21 <<= 2;
    	            for(int k1 = 0; k1 < 4; k1++) {
    	                if(ai1[ai2[l2++]] == 0) {
    	                    pixels[pixelPointer] = Rasterizer.hsl2rgb[hs1 | (l11 >> 2)];
    	                }
    	                if(ai1[ai2[l2++]] == 0) {
    	                    pixels[pixelPointer + 1] = Rasterizer.hsl2rgb[hs1 | (l11 * 3 + l21 >> 4)];
    	                }
    	                if(ai1[ai2[l2++]] == 0) {
    	                    pixels[pixelPointer + 2] = Rasterizer.hsl2rgb[hs1 | (l11 + l21 >> 3)];
    	                }
    	                if(ai1[ai2[l2++]] == 0) {
    	                    pixels[pixelPointer + 3] = Rasterizer.hsl2rgb[hs1 | (l11 + l21 * 3 >> 4)];
    	                }
    	                l11 += l31;
    	                l21 += l41;
    	                pixelPointer += 512;
    	            }
    	        }
    	        return;
    	    }
            if (j2 != 0) {
                for (int i3 = 0; i3 < 4; i3++) {
                    pixels[pixelPointer] = ai1[ai2[l2++]] != 0 ? k2 : j2;
                    pixels[pixelPointer + 1] = ai1[ai2[l2++]] != 0 ? k2 : j2;
                    pixels[pixelPointer + 2] = ai1[ai2[l2++]] != 0 ? k2 : j2;
                    pixels[pixelPointer + 3] = ai1[ai2[l2++]] != 0 ? k2 : j2;
                    pixelPointer += j;
                }
            } else {
                for (int j3 = 0; j3 < 4; j3++) {
                    if (ai1[ai2[l2++]] != 0)
                        pixels[pixelPointer] = k2;
                    if (ai1[ai2[l2++]] != 0)
                        pixels[pixelPointer + 1] = k2;
                    if (ai1[ai2[l2++]] != 0)
                        pixels[pixelPointer + 2] = k2;
                    if (ai1[ai2[l2++]] != 0)
                        pixels[pixelPointer + 3] = k2;
                    pixelPointer += j;
                }
            }
        }
    }

    public void request2DTrace(int x, int y) {
        isClicked = true;
        clickX = x;
        clickY = y;
        clickedTileX = -1;
        clickedTileY = -1;
    }

    public void render(int xCampos, int yCampos, int xCurve, int zCampos, int i1, int yCurve) {
        if (xCampos < 0) {
            xCampos = 0;
        } else if (xCampos >= xMapSize * 128) {
            xCampos = xMapSize * 128 - 1;
        }

        if (yCampos < 0) {
            yCampos = 0;
        } else if (yCampos >= yMapSize * 128) {
            yCampos = yMapSize * 128 - 1;
        }

        yCurveSine = Rasterizer.SINE[yCurve];
        yCurveCosine = Rasterizer.COSINE[yCurve];
        xCurveSine = Rasterizer.SINE[xCurve];
        xCurveCosine = Rasterizer.COSINE[xCurve];
        xCameraPosition = xCampos;
        zCameraPosition = zCampos;
        yCameraPosition = yCampos;
        xCameraPositionTile = xCampos >> 7;
        yCameraPositionTile = yCampos >> 7;
        plane = i1;
        minTileX = xCameraPositionTile - 25;
        if (minTileX < 0)
            minTileX = 0;
        minTileZ = yCameraPositionTile - 25;
        if (minTileZ < 0)
            minTileZ = 0;
        maxTileX = xCameraPositionTile + 25;
        if (maxTileX > xMapSize)
            maxTileX = xMapSize;
        maxTileZ = yCameraPositionTile + 25;
        if (maxTileZ > yMapSize)
            maxTileZ = yMapSize;
        for (int x = 0; x < 52; x++) {
        	for (int z = 0; z < 52; z++) {
        		int tileX = xCameraPositionTile - 25 + x;
        		int tileZ = yCameraPositionTile - 25 + z;
        		if (tileX >= 0 && tileZ >= 0 && tileX < xMapSize && tileZ < yMapSize) {
            		int tilePosX = (x - 25 << 7) - (xCameraPosition & 0x7f);
            		int tilePosZ = (z - 25 << 7) - (yCameraPosition & 0x7f);
        			int tileY = heightmap[0][tileX][tileZ] - zCameraPosition + 128;
        			int maxTileY = heightmap[3][tileX][tileZ] - zCameraPosition - 1000;
        			TILE_VISIBILITY_MAP[x][z] = isOnScreen(tilePosX, maxTileY, tileY, tilePosZ);
        		} else {
        			TILE_VISIBILITY_MAP[x][z] = false;
        		}
        	}
        }
		for (int x = 0; x < 51; x++) {
			for (int z = 0; z < 51; z++) {
				TILE_VISIBILITY_MAPS[x][z] = TILE_VISIBILITY_MAP[x][z] || TILE_VISIBILITY_MAP[x + 1][z] || TILE_VISIBILITY_MAP[x][z + 1] || TILE_VISIBILITY_MAP[x + 1][z + 1];
			}
		}
        processCulling();
        anInt448++;
        anInt446 = 0;
        for (int k1 = renderFromLevel; k1 < anInt437; k1++) {
            Tile floorTiles[][] = tileArray[k1];
            for (int x = minTileX; x < maxTileX; x++) {
                for (int y = minTileZ; y < maxTileZ; y++) {
                    Tile singleTile = floorTiles[x][y];
                    if (singleTile != null)
                        if (singleTile.anInt1321 > i1
                                || !TILE_VISIBILITY_MAPS[x - xCameraPositionTile + 25][y - yCameraPositionTile + 25]) {
                            singleTile.aBoolean1322 = false;
                            singleTile.aBoolean1323 = false;
                            singleTile.anInt1325 = 0;
                        } else {
                            singleTile.aBoolean1322 = true;
                            singleTile.aBoolean1323 = true;
                            singleTile.aBoolean1324 = singleTile.entityCount > 0;
                            anInt446++;
                        }
                }

            }

        }

        for (int l1 = renderFromLevel; l1 < anInt437; l1++) {
            Tile aclass30_sub3_1[][] = tileArray[l1];
            for (int l2 = -25; l2 <= 0; l2++) {
                int i3 = xCameraPositionTile + l2;
                int k3 = xCameraPositionTile - l2;
                if (i3 >= minTileX || k3 < maxTileX) {
                    for (int i4 = -25; i4 <= 0; i4++) {
                        int k4 = yCameraPositionTile + i4;
                        int i5 = yCameraPositionTile - i4;
                        if (i3 >= minTileX) {
                            if (k4 >= minTileZ) {
                                Tile class30_sub3_1 = aclass30_sub3_1[i3][k4];
                                if (class30_sub3_1 != null
                                        && class30_sub3_1.aBoolean1322)
                                    renderTile(class30_sub3_1, true);
                            }
                            if (i5 < maxTileZ) {
                                Tile class30_sub3_2 = aclass30_sub3_1[i3][i5];
                                if (class30_sub3_2 != null
                                        && class30_sub3_2.aBoolean1322)
                                    renderTile(class30_sub3_2, true);
                            }
                        }
                        if (k3 < maxTileX) {
                            if (k4 >= minTileZ) {
                                Tile class30_sub3_3 = aclass30_sub3_1[k3][k4];
                                if (class30_sub3_3 != null
                                        && class30_sub3_3.aBoolean1322)
                                    renderTile(class30_sub3_3, true);
                            }
                            if (i5 < maxTileZ) {
                                Tile class30_sub3_4 = aclass30_sub3_1[k3][i5];
                                if (class30_sub3_4 != null
                                        && class30_sub3_4.aBoolean1322)
                                    renderTile(class30_sub3_4, true);
                            }
                        }
                        if (anInt446 == 0) {
                            isClicked = false;
                            return;
                        }
                    }

                }
            }

        }

        for (int j2 = renderFromLevel; j2 < anInt437; j2++) {
            Tile floorTiles[][] = tileArray[j2];
            for (int j3 = -25; j3 <= 0; j3++) {
                int x = xCameraPositionTile + j3;
                int j4 = xCameraPositionTile - j3;
                if (x >= minTileX || j4 < maxTileX) {
                    for (int l4 = -25; l4 <= 0; l4++) {
                        int y = yCameraPositionTile + l4;
                        int k5 = yCameraPositionTile - l4;
                        if (x >= minTileX) {
                            if (y >= minTileZ) {
                                Tile tile = floorTiles[x][y];
                                if (tile != null
                                        && tile.aBoolean1322)
                                    renderTile(tile, false);
                            }
                            if (k5 < maxTileZ) {
                                Tile class30_sub3_6 = floorTiles[x][k5];
                                if (class30_sub3_6 != null
                                        && class30_sub3_6.aBoolean1322)
                                    renderTile(class30_sub3_6, false);
                            }
                        }
                        if (j4 < maxTileX) {
                            if (y >= minTileZ) {
                                Tile class30_sub3_7 = floorTiles[j4][y];
                                if (class30_sub3_7 != null
                                        && class30_sub3_7.aBoolean1322)
                                    renderTile(class30_sub3_7, false);
                            }
                            if (k5 < maxTileZ) {
                                Tile class30_sub3_8 = floorTiles[j4][k5];
                                if (class30_sub3_8 != null
                                        && class30_sub3_8.aBoolean1322)
                                    renderTile(class30_sub3_8, false);
                            }
                        }
                        if (anInt446 == 0) {
                            isClicked = false;
                            return;
                        }
                    }
                }
            }
        }
        isClicked = false;
    }

	private void renderTile(Tile class30_sub3, boolean flag) {
		aClass19_477.insertHead(class30_sub3);
		do {
			Tile class30_sub3_1 = (Tile) aClass19_477.popHead();
			if (class30_sub3_1 == null) {
				break;
			}
			if (class30_sub3_1.aBoolean1323) {
				int i = class30_sub3_1.anInt1308;
				int j = class30_sub3_1.anInt1309;
				int k = class30_sub3_1.anInt1307;
				int l = class30_sub3_1.anInt1310;
				Tile aclass30_sub3[][] = tileArray[k];
				if (class30_sub3_1.aBoolean1322) {
					if (flag) {
						if (k > 0) {
							Tile class30_sub3_2 = tileArray[k - 1][i][j];
							if (class30_sub3_2 != null && class30_sub3_2.aBoolean1323)
								continue;
						}
						if (i <= xCameraPositionTile && i > minTileX) {
							Tile class30_sub3_3 = aclass30_sub3[i - 1][j];
							if (class30_sub3_3 != null && class30_sub3_3.aBoolean1323 && (class30_sub3_3.aBoolean1322 || (class30_sub3_1.anInt1320 & 1) == 0))
								continue;
						}
						if (i >= xCameraPositionTile && i < maxTileX - 1) {
							Tile class30_sub3_4 = aclass30_sub3[i + 1][j];
							if (class30_sub3_4 != null && class30_sub3_4.aBoolean1323 && (class30_sub3_4.aBoolean1322 || (class30_sub3_1.anInt1320 & 4) == 0))
								continue;
						}
						if (j <= yCameraPositionTile && j > minTileZ) {
							Tile class30_sub3_5 = aclass30_sub3[i][j - 1];
							if (class30_sub3_5 != null && class30_sub3_5.aBoolean1323 && (class30_sub3_5.aBoolean1322 || (class30_sub3_1.anInt1320 & 8) == 0))
								continue;
						}
						if (j >= yCameraPositionTile && j < maxTileZ - 1) {
							Tile class30_sub3_6 = aclass30_sub3[i][j + 1];
							if (class30_sub3_6 != null && class30_sub3_6.aBoolean1323 && (class30_sub3_6.aBoolean1322 || (class30_sub3_1.anInt1320 & 2) == 0))
								continue;
						}
					} else {
						flag = true;
					}
					class30_sub3_1.aBoolean1322 = false;
					if (class30_sub3_1.aClass30_Sub3_1329 != null) {
						Tile class30_sub3_7 = class30_sub3_1.aClass30_Sub3_1329;
						if (class30_sub3_7.plainTile != null) {
							if (!method320(0, i, j))
								renderPlainTile(class30_sub3_7.plainTile, 0, yCurveSine, yCurveCosine, xCurveSine, xCurveCosine, i, j);
						} else if (class30_sub3_7.shapedtile != null && !method320(0, i, j))
							renderShapedtile(i, yCurveSine, xCurveSine, class30_sub3_7.shapedtile, yCurveCosine, j, xCurveCosine);
						WallObject class10 = class30_sub3_7.wallObject;
						if (class10 != null)
							class10.node1.renderAtPoint(0, yCurveSine, yCurveCosine, xCurveSine, xCurveCosine, class10.xPos - xCameraPosition, class10.zPos - zCameraPosition, class10.yPos - yCameraPosition, class10.uid);
						for (int i2 = 0; i2 < class30_sub3_7.entityCount; i2++) {
							InteractiveObject class28 = class30_sub3_7.interactiveObjects[i2];
							if (class28 != null)
								class28.jagexNode.renderAtPoint(class28.anInt522, yCurveSine, yCurveCosine, xCurveSine, xCurveCosine, class28.anInt519 - xCameraPosition, class28.anInt518 - zCameraPosition, class28.anInt520 - yCameraPosition, class28.uid);
						}

					}
					boolean flag1 = false;
					if (class30_sub3_1.plainTile != null) {
						if (!method320(l, i, j)) {
							flag1 = true;
							renderPlainTile(class30_sub3_1.plainTile, l, yCurveSine, yCurveCosine, xCurveSine, xCurveCosine, i, j);
						}
					} else if (class30_sub3_1.shapedtile != null && !method320(l, i, j)) {
						flag1 = true;
						renderShapedtile(i, yCurveSine, xCurveSine, class30_sub3_1.shapedtile, yCurveCosine, j, xCurveCosine);
					}
					int j1 = 0;
					int j2 = 0;
					WallObject class10_3 = class30_sub3_1.wallObject;
					WallDecoration class26_1 = class30_sub3_1.obj2;
					if (class10_3 != null || class26_1 != null) {
						if (xCameraPositionTile == i)
							j1++;
						else if (xCameraPositionTile < i)
							j1 += 2;
						if (yCameraPositionTile == j)
							j1 += 3;
						else if (yCameraPositionTile > j)
							j1 += 6;
						j2 = anIntArray478[j1];
						class30_sub3_1.anInt1328 = anIntArray480[j1];
					}
					if (class10_3 != null) {
						if ((class10_3.orientation & anIntArray479[j1]) != 0) {
							if (class10_3.orientation == 16) {
								class30_sub3_1.anInt1325 = 3;
								class30_sub3_1.anInt1326 = anIntArray481[j1];
								class30_sub3_1.anInt1327 = 3 - class30_sub3_1.anInt1326;
							} else if (class10_3.orientation == 32) {
								class30_sub3_1.anInt1325 = 6;
								class30_sub3_1.anInt1326 = anIntArray482[j1];
								class30_sub3_1.anInt1327 = 6 - class30_sub3_1.anInt1326;
							} else if (class10_3.orientation == 64) {
								class30_sub3_1.anInt1325 = 12;
								class30_sub3_1.anInt1326 = anIntArray483[j1];
								class30_sub3_1.anInt1327 = 12 - class30_sub3_1.anInt1326;
							} else {
								class30_sub3_1.anInt1325 = 9;
								class30_sub3_1.anInt1326 = anIntArray484[j1];
								class30_sub3_1.anInt1327 = 9 - class30_sub3_1.anInt1326;
							}
						} else {
							class30_sub3_1.anInt1325 = 0;
						}
						if ((class10_3.orientation & j2) != 0 && !method321(l, i, j, class10_3.orientation))
							class10_3.node1.renderAtPoint(0, yCurveSine, yCurveCosine, xCurveSine, xCurveCosine, class10_3.xPos - xCameraPosition, class10_3.zPos - zCameraPosition, class10_3.yPos - yCameraPosition, class10_3.uid);
						if ((class10_3.orientation1 & j2) != 0 && !method321(l, i, j, class10_3.orientation1))
							class10_3.node2.renderAtPoint(0, yCurveSine, yCurveCosine, xCurveSine, xCurveCosine, class10_3.xPos - xCameraPosition, class10_3.zPos - zCameraPosition, class10_3.yPos - yCameraPosition, class10_3.uid);
					}
					if (class26_1 != null && !method322(l, i, j, class26_1.myEntity.highestY))
						if ((class26_1.configBits & j2) != 0)
							class26_1.myEntity.renderAtPoint(class26_1.face << 3, yCurveSine, yCurveCosine, xCurveSine, xCurveCosine, class26_1.xPos - xCameraPosition, class26_1.zPos - zCameraPosition, class26_1.yPos - yCameraPosition, class26_1.uid);
						else if ((class26_1.configBits & 0x300) != 0) {
							int j4 = class26_1.xPos - xCameraPosition;
							int l5 = class26_1.zPos - zCameraPosition;
							int k6 = class26_1.yPos - yCameraPosition;
							int i8 = class26_1.face;
							int k9;
							if (i8 == 1 || i8 == 2)
								k9 = -j4;
							else
								k9 = j4;
							int k10;
							if (i8 == 2 || i8 == 3)
								k10 = -k6;
							else
								k10 = k6;
							if ((class26_1.configBits & 0x100) != 0 && k10 < k9) {
								int i11 = j4 + anIntArray463[i8];
								int k11 = k6 + anIntArray464[i8];
								class26_1.myEntity.renderAtPoint(i8 * 4096 + 2048, yCurveSine, yCurveCosine, xCurveSine, xCurveCosine, i11, l5, k11, class26_1.uid);
							}
							if ((class26_1.configBits & 0x200) != 0 && k10 > k9) {
								int j11 = j4 + anIntArray465[i8];
								int l11 = k6 + anIntArray466[i8];
								class26_1.myEntity.renderAtPoint(i8 * 4096 + 10240 & 0x3fff, yCurveSine, yCurveCosine, xCurveSine, xCurveCosine, j11, l5, l11, class26_1.uid);
							}
						}
					if (flag1) {
						GroundDecoration class49 = class30_sub3_1.groundDecoration;
						if (class49 != null)
							class49.aClass30_Sub2_Sub4_814.renderAtPoint(0, yCurveSine, yCurveCosine, xCurveSine, xCurveCosine, class49.xPos - xCameraPosition, class49.zPos - zCameraPosition, class49.yPos - yCameraPosition, class49.uid);
						GroundItemTile object4_1 = class30_sub3_1.obj4;
						if (object4_1 != null && object4_1.anInt52 == 0) {
							if (object4_1.secondGroundItem != null)
								object4_1.secondGroundItem.renderAtPoint(0, yCurveSine, yCurveCosine, xCurveSine, xCurveCosine, object4_1.xPos - xCameraPosition, object4_1.zPos - zCameraPosition, object4_1.yPos - yCameraPosition, object4_1.uid);
							if (object4_1.thirdGroundItem != null)
								object4_1.thirdGroundItem.renderAtPoint(0, yCurveSine, yCurveCosine, xCurveSine, xCurveCosine, object4_1.xPos - xCameraPosition, object4_1.zPos - zCameraPosition, object4_1.yPos - yCameraPosition, object4_1.uid);
							if (object4_1.firstGroundItem != null)
								object4_1.firstGroundItem.renderAtPoint(0, yCurveSine, yCurveCosine, xCurveSine, xCurveCosine, object4_1.xPos - xCameraPosition, object4_1.zPos - zCameraPosition, object4_1.yPos - yCameraPosition, object4_1.uid);
						}
					}
					int k4 = class30_sub3_1.anInt1320;
					if (k4 != 0) {
						if (i < xCameraPositionTile && (k4 & 4) != 0) {
							Tile class30_sub3_17 = aclass30_sub3[i + 1][j];
							if (class30_sub3_17 != null && class30_sub3_17.aBoolean1323)
								aClass19_477.insertHead(class30_sub3_17);
						}
						if (j < yCameraPositionTile && (k4 & 2) != 0) {
							Tile class30_sub3_18 = aclass30_sub3[i][j + 1];
							if (class30_sub3_18 != null && class30_sub3_18.aBoolean1323)
								aClass19_477.insertHead(class30_sub3_18);
						}
						if (i > xCameraPositionTile && (k4 & 1) != 0) {
							Tile class30_sub3_19 = aclass30_sub3[i - 1][j];
							if (class30_sub3_19 != null && class30_sub3_19.aBoolean1323)
								aClass19_477.insertHead(class30_sub3_19);
						}
						if (j > yCameraPositionTile && (k4 & 8) != 0) {
							Tile class30_sub3_20 = aclass30_sub3[i][j - 1];
							if (class30_sub3_20 != null && class30_sub3_20.aBoolean1323)
								aClass19_477.insertHead(class30_sub3_20);
						}
					}
				}
				if (class30_sub3_1.anInt1325 != 0) {
					boolean flag2 = true;
					for (int k1 = 0; k1 < class30_sub3_1.entityCount; k1++) {
						if (class30_sub3_1.interactiveObjects[k1].anInt528 == anInt448 || (class30_sub3_1.anIntArray1319[k1] & class30_sub3_1.anInt1325) != class30_sub3_1.anInt1326)
							continue;
						flag2 = false;
						break;
					}

					if (flag2) {
						WallObject class10_1 = class30_sub3_1.wallObject;
						if (!method321(l, i, j, class10_1.orientation))
							class10_1.node1.renderAtPoint(0, yCurveSine, yCurveCosine, xCurveSine, xCurveCosine, class10_1.xPos - xCameraPosition, class10_1.zPos - zCameraPosition, class10_1.yPos - yCameraPosition, class10_1.uid);
						class30_sub3_1.anInt1325 = 0;
					}
				}
				if (class30_sub3_1.aBoolean1324)
					try {
						int i1 = class30_sub3_1.entityCount;
						class30_sub3_1.aBoolean1324 = false;
						int l1 = 0;
						label0: for (int k2 = 0; k2 < i1; k2++) {
							InteractiveObject class28_1 = class30_sub3_1.interactiveObjects[k2];
							if (class28_1.anInt528 == anInt448)
								continue;
							for (int k3 = class28_1.anInt523; k3 <= class28_1.anInt524; k3++) {
								for (int l4 = class28_1.anInt525; l4 <= class28_1.anInt526; l4++) {
									Tile class30_sub3_21 = aclass30_sub3[k3][l4];
									if (class30_sub3_21.aBoolean1322) {
										class30_sub3_1.aBoolean1324 = true;
									} else {
										if (class30_sub3_21.anInt1325 == 0)
											continue;
										int l6 = 0;
										if (k3 > class28_1.anInt523)
											l6++;
										if (k3 < class28_1.anInt524)
											l6 += 4;
										if (l4 > class28_1.anInt525)
											l6 += 8;
										if (l4 < class28_1.anInt526)
											l6 += 2;
										if ((l6 & class30_sub3_21.anInt1325) != class30_sub3_1.anInt1327)
											continue;
										class30_sub3_1.aBoolean1324 = true;
									}
									continue label0;
								}

							}

							aClass28Array462[l1++] = class28_1;
							int i5 = xCameraPositionTile - class28_1.anInt523;
							int i6 = class28_1.anInt524 - xCameraPositionTile;
							if (i6 > i5)
								i5 = i6;
							int i7 = yCameraPositionTile - class28_1.anInt525;
							int j8 = class28_1.anInt526 - yCameraPositionTile;
							if (j8 > i7)
								class28_1.anInt527 = i5 + j8;
							else
								class28_1.anInt527 = i5 + i7;
						}

						while (l1 > 0) {
							int i3 = -50;
							int l3 = -1;
							for (int j5 = 0; j5 < l1; j5++) {
								InteractiveObject class28_2 = aClass28Array462[j5];
								if (class28_2.anInt528 != anInt448)
									if (class28_2.anInt527 > i3) {
										i3 = class28_2.anInt527;
										l3 = j5;
									} else if (class28_2.anInt527 == i3) {
										int j7 = class28_2.anInt519 - xCameraPosition;
										int k8 = class28_2.anInt520 - yCameraPosition;
										int l9 = aClass28Array462[l3].anInt519 - xCameraPosition;
										int l10 = aClass28Array462[l3].anInt520 - yCameraPosition;
										if (j7 * j7 + k8 * k8 > l9 * l9 + l10 * l10)
											l3 = j5;
									}
							}

							if (l3 == -1)
								break;
							InteractiveObject class28_3 = aClass28Array462[l3];
							class28_3.anInt528 = anInt448;
							if (!method323(l, class28_3.anInt523, class28_3.anInt524, class28_3.anInt525, class28_3.anInt526, class28_3.jagexNode.highestY))
								class28_3.jagexNode.renderAtPoint(class28_3.anInt522, yCurveSine, yCurveCosine, xCurveSine, xCurveCosine, class28_3.anInt519 - xCameraPosition, class28_3.anInt518 - zCameraPosition, class28_3.anInt520 - yCameraPosition, class28_3.uid);
							for (int k7 = class28_3.anInt523; k7 <= class28_3.anInt524; k7++) {
								for (int l8 = class28_3.anInt525; l8 <= class28_3.anInt526; l8++) {
									Tile class30_sub3_22 = aclass30_sub3[k7][l8];
									if (class30_sub3_22.anInt1325 != 0)
										aClass19_477.insertHead(class30_sub3_22);
									else if ((k7 != i || l8 != j) && class30_sub3_22.aBoolean1323)
										aClass19_477.insertHead(class30_sub3_22);
								}

							}

						}
						if (class30_sub3_1.aBoolean1324)
							continue;
					} catch (Exception _ex) {

						_ex.printStackTrace();

						class30_sub3_1.aBoolean1324 = false;
					}
				if (!class30_sub3_1.aBoolean1323 || class30_sub3_1.anInt1325 != 0)
					continue;
				if (i <= xCameraPositionTile && i > minTileX) {
					Tile class30_sub3_8 = aclass30_sub3[i - 1][j];
					if (class30_sub3_8 != null && class30_sub3_8.aBoolean1323)
						continue;
				}
				if (i >= xCameraPositionTile && i < maxTileX - 1) {
					Tile class30_sub3_9 = aclass30_sub3[i + 1][j];
					if (class30_sub3_9 != null && class30_sub3_9.aBoolean1323)
						continue;
				}
				if (j <= yCameraPositionTile && j > minTileZ) {
					Tile class30_sub3_10 = aclass30_sub3[i][j - 1];
					if (class30_sub3_10 != null && class30_sub3_10.aBoolean1323)
						continue;
				}
				if (j >= yCameraPositionTile && j < maxTileZ - 1) {
					Tile class30_sub3_11 = aclass30_sub3[i][j + 1];
					if (class30_sub3_11 != null && class30_sub3_11.aBoolean1323)
						continue;
				}
				class30_sub3_1.aBoolean1323 = false;
				anInt446--;
				GroundItemTile object4 = class30_sub3_1.obj4;
				if (object4 != null && object4.anInt52 != 0) {
					if (object4.secondGroundItem != null)
						object4.secondGroundItem.renderAtPoint(0, yCurveSine, yCurveCosine, xCurveSine, xCurveCosine, object4.xPos - xCameraPosition, object4.zPos - zCameraPosition - object4.anInt52, object4.yPos - yCameraPosition, object4.uid);
					if (object4.thirdGroundItem != null)
						object4.thirdGroundItem.renderAtPoint(0, yCurveSine, yCurveCosine, xCurveSine, xCurveCosine, object4.xPos - xCameraPosition, object4.zPos - zCameraPosition - object4.anInt52, object4.yPos - yCameraPosition, object4.uid);
					if (object4.firstGroundItem != null)
						object4.firstGroundItem.renderAtPoint(0, yCurveSine, yCurveCosine, xCurveSine, xCurveCosine, object4.xPos - xCameraPosition, object4.zPos - zCameraPosition - object4.anInt52, object4.yPos - yCameraPosition, object4.uid);
				}
				if (class30_sub3_1.anInt1328 != 0) {
					WallDecoration class26 = class30_sub3_1.obj2;
					if (class26 != null && !method322(l, i, j, class26.myEntity.highestY))
						if ((class26.configBits & class30_sub3_1.anInt1328) != 0)
							class26.myEntity.renderAtPoint(class26.face << 3, yCurveSine, yCurveCosine, xCurveSine, xCurveCosine, class26.xPos - xCameraPosition, class26.zPos - zCameraPosition, class26.yPos - yCameraPosition, class26.uid);
						else if ((class26.configBits & 0x300) != 0) {
							int l2 = class26.xPos - xCameraPosition;
							int j3 = class26.zPos - zCameraPosition;
							int i4 = class26.yPos - yCameraPosition;
							int k5 = class26.face;
							int j6;
							if (k5 == 1 || k5 == 2)
								j6 = -l2;
							else
								j6 = l2;
							int l7;
							if (k5 == 2 || k5 == 3)
								l7 = -i4;
							else
								l7 = i4;
							if ((class26.configBits & 0x100) != 0 && l7 >= j6) {
								int i9 = l2 + anIntArray463[k5];
								int i10 = i4 + anIntArray464[k5];
								class26.myEntity.renderAtPoint(k5 * 4096 + 2048, yCurveSine, yCurveCosine, xCurveSine, xCurveCosine, i9, j3, i10, class26.uid);
							}
							if ((class26.configBits & 0x200) != 0 && l7 <= j6) {
								int j9 = l2 + anIntArray465[k5];
								int j10 = i4 + anIntArray466[k5];
								class26.myEntity.renderAtPoint(k5 * 4096 + 10240 & 0x3fff, yCurveSine, yCurveCosine, xCurveSine, xCurveCosine, j9, j3, j10, class26.uid);
							}
						}
					WallObject class10_2 = class30_sub3_1.wallObject;
					if (class10_2 != null) {
						if ((class10_2.orientation1 & class30_sub3_1.anInt1328) != 0 && !method321(l, i, j, class10_2.orientation1))
							class10_2.node2.renderAtPoint(0, yCurveSine, yCurveCosine, xCurveSine, xCurveCosine, class10_2.xPos - xCameraPosition, class10_2.zPos - zCameraPosition, class10_2.yPos - yCameraPosition, class10_2.uid);
						if ((class10_2.orientation & class30_sub3_1.anInt1328) != 0 && !method321(l, i, j, class10_2.orientation))
							class10_2.node1.renderAtPoint(0, yCurveSine, yCurveCosine, xCurveSine, xCurveCosine, class10_2.xPos - xCameraPosition, class10_2.zPos - zCameraPosition, class10_2.yPos - yCameraPosition, class10_2.uid);
					}
				}
				if (k < anInt437 - 1) {
					Tile class30_sub3_12 = tileArray[k + 1][i][j];
					if (class30_sub3_12 != null && class30_sub3_12.aBoolean1323)
						aClass19_477.insertHead(class30_sub3_12);
				}
				if (i < xCameraPositionTile) {
					Tile class30_sub3_13 = aclass30_sub3[i + 1][j];
					if (class30_sub3_13 != null && class30_sub3_13.aBoolean1323)
						aClass19_477.insertHead(class30_sub3_13);
				}
				if (j < yCameraPositionTile) {
					Tile class30_sub3_14 = aclass30_sub3[i][j + 1];
					if (class30_sub3_14 != null && class30_sub3_14.aBoolean1323)
						aClass19_477.insertHead(class30_sub3_14);
				}
				if (i > xCameraPositionTile) {
					Tile class30_sub3_15 = aclass30_sub3[i - 1][j];
					if (class30_sub3_15 != null && class30_sub3_15.aBoolean1323)
						aClass19_477.insertHead(class30_sub3_15);
				}
				if (j > yCameraPositionTile) {
					Tile class30_sub3_16 = aclass30_sub3[i][j - 1];
					if (class30_sub3_16 != null && class30_sub3_16.aBoolean1323)
						aClass19_477.insertHead(class30_sub3_16);
				}
			}
		} while (true);
	}

    private void renderPlainTile(PlainTile tile, int i, int j, int k, int l, int i1,
                                 int j1, int k1) {
        int l1;
        int i2 = l1 = (j1 << 7) - xCameraPosition;
        int j2;
        int k2 = j2 = (k1 << 7) - yCameraPosition;
        int l2;
        int i3 = l2 = i2 + 128;
        int j3;
        int k3 = j3 = k2 + 128;
        int l3 = heightmap[i][j1][k1] - zCameraPosition;
        int i4 = heightmap[i][j1 + 1][k1] - zCameraPosition;
        int j4 = heightmap[i][j1 + 1][k1 + 1] - zCameraPosition;
        int k4 = heightmap[i][j1][k1 + 1] - zCameraPosition;
        int l4 = k2 * l + i2 * i1 >> 15;
        k2 = k2 * i1 - i2 * l >> 15;
        i2 = l4;
        l4 = l3 * k - k2 * j >> 15;
        k2 = l3 * j + k2 * k >> 15;
        l3 = l4;
        if (k2 < 50)
            return;
        l4 = j2 * l + i3 * i1 >> 15;
        j2 = j2 * i1 - i3 * l >> 15;
        i3 = l4;
        l4 = i4 * k - j2 * j >> 15;
        j2 = i4 * j + j2 * k >> 15;
        i4 = l4;
        if (j2 < 50)
            return;
        l4 = k3 * l + l2 * i1 >> 15;
        k3 = k3 * i1 - l2 * l >> 15;
        l2 = l4;
        l4 = j4 * k - k3 * j >> 15;
        k3 = j4 * j + k3 * k >> 15;
        j4 = l4;
        if (k3 < 50)
            return;
        l4 = j3 * l + l1 * i1 >> 15;
        j3 = j3 * i1 - l1 * l >> 15;
        l1 = l4;
        l4 = k4 * k - j3 * j >> 15;
        j3 = k4 * j + j3 * k >> 15;
        k4 = l4;
		if (j3 < 50)
			return;
		int i5 = Rasterizer.centerX + (i2 * Client.gameAreaWidth) / k2;
		int j5 = Rasterizer.centerY + (l3 * Client.gameAreaWidth) / k2;
		int k5 = Rasterizer.centerX + (i3 * Client.gameAreaWidth) / j2;
		int l5 = Rasterizer.centerY + (i4 * Client.gameAreaWidth) / j2;
		int i6 = Rasterizer.centerX + (l2 * Client.gameAreaWidth) / k3;
		int j6 = Rasterizer.centerY + (j4 * Client.gameAreaWidth) / k3;
		int k6 = Rasterizer.centerX + (l1 * Client.gameAreaWidth) / j3;
		int l6 = Rasterizer.centerY + (k4 * Client.gameAreaWidth) / j3;

		Rasterizer.alpha = 0;
        if ((i6 - k6) * (l5 - l6) - (j6 - l6) * (k5 - k6) > 0) {
            Rasterizer.restrict_edges = i6 < 0 || k6 < 0 || k5 < 0
                    || i6 > Rasterizer.endX || k6 > Rasterizer.endX
                    || k5 > Rasterizer.endX;
            if (isClicked
                    && isMouseWithinTriangle(clickX, clickY, j6, l6, l5, i6, k6, k5)) {
                clickedTileX = j1;
                clickedTileY = k1;
            }
            if (tile.anInt720 != -1) {
                if (tile.flat) {
                    Rasterizer.drawTexturedTriangle(j6, l6, l5, i6, k6, k5,
                            (float) k3, (float) j3, (float) j2, tile.anInt718, tile.anInt719, tile.anInt717, i2, i3, l1, l3, i4,
                            k4,
                            k2, j2, j3,
                            !lowMem ? tile.anInt720
                                    : -1, true, opaque_floor_texture
                    );
                } else {
                    Rasterizer.drawTexturedTriangle(j6, l6, l5, i6, k6, k5,
                    		(float) k3, (float) j3, (float) j2, tile.anInt718, tile.anInt719, tile.anInt717, l2, l1, i3, j4, k4,
                            i4,
                            k3, j3, j2,
                            !lowMem ? tile.anInt720
                                    : -1, true, opaque_floor_texture
                    );
                }
            } else if (tile.anInt718 != 0xbc614e) {
                Rasterizer.drawGouraudTriangle(j6, l6, l5, i6, k6, k5,
                		(float) k3, (float) j3, (float) j2, tile.anInt718, tile.anInt719, tile.anInt717);
            }
        }
        if ((i5 - k5) * (l6 - l5) - (j5 - l5) * (k6 - k5) > 0) {
            Rasterizer.restrict_edges = i5 < 0 || k5 < 0 || k6 < 0
                    || i5 > Rasterizer.endX || k5 > Rasterizer.endX
                    || k6 > Rasterizer.endX;
            if (isClicked
                    && isMouseWithinTriangle(clickX, clickY, j5, l5, l6, i5, k5, k6)) {
                clickedTileX = j1;
                clickedTileY = k1;
            }
            if (tile.anInt720 == -1) {
                if (tile.anInt716 != 0xbc614e) {
                    Rasterizer.drawGouraudTriangle(j5, l5, l6, i5, k5, k6,
                    		(float) k2, (float) j2, (float) j3, tile.anInt716, tile.anInt717, tile.anInt719);
                }
            } else {
                Rasterizer.drawTexturedTriangle(j5, l5, l6, i5, k5, k6,
                		(float) k2, (float) j2, (float) j3,
                        tile.anInt716, tile.anInt717, tile.anInt719, i2, i3, l1, l3, i4, k4, k2,
                        j2, j3, !lowMem ? tile.anInt720 : -1, true, opaque_floor_texture
                );
                return;
            }
        }
    }

    private void renderShapedtile(int i, int j, int k, ShapedTile tile, int l, int i1,
                                  int j1) {
        int k1 = tile.origVertexX.length;
        for (int l1 = 0; l1 < k1; l1++) {
            int i2 = tile.origVertexX[l1] - xCameraPosition;
            int k2 = tile.origVertexY[l1] - zCameraPosition;
            int i3 = tile.origVertexZ[l1] - yCameraPosition;
            int k3 = i3 * k + i2 * j1 >> 15;
            i3 = i3 * j1 - i2 * k >> 15;
            i2 = k3;
            k3 = k2 * l - i3 * j >> 15;
            i3 = k2 * j + i3 * l >> 15;
            k2 = k3;
            if (i3 < 50)
                return;
            if (tile.anIntArray682 != null) {
                ShapedTile.anIntArray690[l1] = i2;
                ShapedTile.anIntArray691[l1] = k2;
                ShapedTile.anIntArray692[l1] = i3;
            }
            ShapedTile.vertex2dX[l1] = Rasterizer.centerX
                    + (i2 * Client.gameAreaWidth) / i3;
            ShapedTile.vertex2dY[l1] = Rasterizer.centerY
                    + (k2 * Client.gameAreaWidth) / i3;
            ShapedTile.vertex2dZ[l1] = i3;
        }
        Rasterizer.alpha = 0;
        k1 = tile.anIntArray679.length;
        for (int j2 = 0; j2 < k1; j2++) {
            int l2 = tile.anIntArray679[j2];
            int j3 = tile.anIntArray680[j2];
            int l3 = tile.anIntArray681[j2];
            int x1 = ShapedTile.vertex2dX[l2];
            int x2 = ShapedTile.vertex2dX[j3];
            int x3 = ShapedTile.vertex2dX[l3];
            int y1 = ShapedTile.vertex2dY[l2];
            int y2 = ShapedTile.vertex2dY[j3];
            int y3 = ShapedTile.vertex2dY[l3];
            int z1 = ShapedTile.vertex2dZ[l2];
            int z2 = ShapedTile.vertex2dZ[j3];
            int z3 = ShapedTile.vertex2dZ[l3];
            if ((x1 - x2) * (y3 - y2) - (y1 - y2) * (x3 - x2) > 0) {
                Rasterizer.restrict_edges = x1 < 0 || x2 < 0 || x3 < 0
                        || x1 > Rasterizer.endX || x2 > Rasterizer.endX
                        || x3 > Rasterizer.endX;

                if (isClicked
                        && isMouseWithinTriangle(clickX, clickY, y1, y2, y3, x1, x2, x3)) {
                    clickedTileX = i;
                    clickedTileY = i1;
                }
                if (tile.anIntArray682 == null
                        || tile.anIntArray682[j2] == -1) {
                    if (tile.hsl1[j2] != 0xbc614e) {
                        Rasterizer.drawGouraudTriangle(y1, y2, y3, x1, x2, x3,
                        		(float) z1,
                                (float) z2,
                                (float) z3, tile.hsl1[j2], tile.hsl2[j2], tile.hsl3[j2]);
                    }
                } else {
                    Rasterizer
                            .drawTexturedTriangle(
                                    y1,
                                    y2,
                                    y3,
                                    x1,
                                    x2,
                                    x3,
                                    (float) z1,
                                    (float) z2,
                                    (float) z3,
                                    tile.hsl1[j2],
                                    tile.hsl2[j2],
                                    tile.hsl3[j2],
                                    ShapedTile.anIntArray690[0],
                                    ShapedTile.anIntArray690[1],
                                    ShapedTile.anIntArray690[3],
                                    ShapedTile.anIntArray691[0],
                                    ShapedTile.anIntArray691[1],
                                    ShapedTile.anIntArray691[3],
                                    ShapedTile.anIntArray692[0], ShapedTile.anIntArray692[1],
                                    ShapedTile.anIntArray692[3], !lowMem ? tile.anIntArray682[j2]
                                            : -1, true, opaque_floor_texture
                            );

                }

            }
        }
    }

    private boolean isMouseWithinTriangle(int i, int j, int k, int l, int i1, int j1,
                                          int k1, int l1) {
        if (j < k && j < l && j < i1)
            return false;
        if (j > k && j > l && j > i1)
            return false;
        if (i < j1 && i < k1 && i < l1)
            return false;
        if (i > j1 && i > k1 && i > l1)
            return false;
        int i2 = (j - k) * (k1 - j1) - (i - j1) * (l - k);
        int j2 = (j - i1) * (j1 - l1) - (i - l1) * (k - i1);
        int k2 = (j - l) * (l1 - k1) - (i - k1) * (i1 - l);
        return i2 * k2 > 0 && k2 * j2 > 0;
    }

    private void processCulling() {
        int j = cullingClusterPointer[plane];
        CullingCluster aclass47[] = cullingClusters[plane];
        anInt475 = 0;
        for (int k = 0; k < j; k++) {
            CullingCluster class47 = aclass47[k];
            if (class47.searchMask == 1) {
                int l = (class47.tileStartX - xCameraPositionTile) + 25;
                if (l < 0 || l > 50)
                    continue;
                int k1 = (class47.tileStartY - yCameraPositionTile) + 25;
                if (k1 < 0)
                    k1 = 0;
                int j2 = (class47.tileEndY - yCameraPositionTile) + 25;
                if (j2 > 50)
                    j2 = 50;
                boolean flag = false;
                while (k1 <= j2)
                    if (TILE_VISIBILITY_MAPS[l][k1++]) {
                        flag = true;
                        break;
                    }
                if (!flag)
                    continue;
                int j3 = xCameraPosition - class47.worldStartX;
                if (j3 > 32) {
                    class47.tileDistanceEnum = 1;
                } else {
                    if (j3 >= -32)
                        continue;
                    class47.tileDistanceEnum = 2;
                    j3 = -j3;
                }
                class47.worldDistanceFromCameraStartY = (class47.worldStartY - yCameraPosition << 8) / j3;
                class47.worldDistanceFromCameraEndY = (class47.worldEndY - yCameraPosition << 8) / j3;
                class47.worldDistanceFromCameraStartZ = (class47.worldStartZ - zCameraPosition << 8) / j3;
                class47.worldDistanceFromCameraEndZ = (class47.worldEndZ - zCameraPosition << 8) / j3;
                aClass47Array476[anInt475++] = class47;
                continue;
            }
            if (class47.searchMask == 2) {
                int i1 = (class47.tileStartY - yCameraPositionTile) + 25;
                if (i1 < 0 || i1 > 50)
                    continue;
                int l1 = (class47.tileStartX - xCameraPositionTile) + 25;
                if (l1 < 0)
                    l1 = 0;
                int k2 = (class47.tileEndX - xCameraPositionTile) + 25;
                if (k2 > 50)
                    k2 = 50;
                boolean flag1 = false;
                while (l1 <= k2)
                    if (TILE_VISIBILITY_MAPS[l1++][i1]) {
                        flag1 = true;
                        break;
                    }
                if (!flag1)
                    continue;
                int k3 = yCameraPosition - class47.worldStartY;
                if (k3 > 32) {
                    class47.tileDistanceEnum = 3;
                } else {
                    if (k3 >= -32)
                        continue;
                    class47.tileDistanceEnum = 4;
                    k3 = -k3;
                }
                class47.worldDistanceFromCameraStartX = (class47.worldStartX - xCameraPosition << 8) / k3;
                class47.worldDistanceFromCameraEndX = (class47.worldEndX - xCameraPosition << 8) / k3;
                class47.worldDistanceFromCameraStartZ = (class47.worldStartZ - zCameraPosition << 8) / k3;
                class47.worldDistanceFromCameraEndZ = (class47.worldEndZ - zCameraPosition << 8) / k3;
                aClass47Array476[anInt475++] = class47;
            } else if (class47.searchMask == 4) {
                int j1 = class47.worldStartZ - zCameraPosition;
                if (j1 > 128) {
                    int i2 = (class47.tileStartY - yCameraPositionTile) + 25;
                    if (i2 < 0)
                        i2 = 0;
                    int l2 = (class47.tileEndY - yCameraPositionTile) + 25;
                    if (l2 > 50)
                        l2 = 50;
                    if (i2 <= l2) {
                        int i3 = (class47.tileStartX - xCameraPositionTile) + 25;
                        if (i3 < 0)
                            i3 = 0;
                        int l3 = (class47.tileEndX - xCameraPositionTile) + 25;
                        if (l3 > 50)
                            l3 = 50;
                        boolean flag2 = false;
                        label0:
                        for (int i4 = i3; i4 <= l3; i4++) {
                            for (int j4 = i2; j4 <= l2; j4++) {
                                if (!TILE_VISIBILITY_MAPS[i4][j4])
                                    continue;
                                flag2 = true;
                                break label0;
                            }

                        }

                        if (flag2) {
                            class47.tileDistanceEnum = 5;
                            class47.worldDistanceFromCameraStartX = (class47.worldStartX - xCameraPosition << 8)
                                    / j1;
                            class47.worldDistanceFromCameraEndX = (class47.worldEndX - xCameraPosition << 8)
                                    / j1;
                            class47.worldDistanceFromCameraStartY = (class47.worldStartY - yCameraPosition << 8)
                                    / j1;
                            class47.worldDistanceFromCameraEndY = (class47.worldEndY - yCameraPosition << 8)
                                    / j1;
                            aClass47Array476[anInt475++] = class47;
                        }
                    }
                }
            }
        }

    }

    private boolean method320(int i, int j, int k) {
        int l = anIntArrayArrayArray445[i][j][k];
        if (l == -anInt448)
            return false;
        if (l == anInt448)
            return true;
        int i1 = j << 7;
        int j1 = k << 7;
        if (method324(i1 + 1, heightmap[i][j][k], j1 + 1)
                && method324((i1 + 128) - 1,
                heightmap[i][j + 1][k], j1 + 1)
                && method324((i1 + 128) - 1,
                heightmap[i][j + 1][k + 1],
                (j1 + 128) - 1)
                && method324(i1 + 1, heightmap[i][j][k + 1],
                (j1 + 128) - 1)) {
            anIntArrayArrayArray445[i][j][k] = anInt448;
            return true;
        } else {
            anIntArrayArrayArray445[i][j][k] = -anInt448;
            return false;
        }
    }

    private boolean method321(int i, int j, int k, int l) {
        if (!method320(i, j, k))
            return false;
        int i1 = j << 7;
        int j1 = k << 7;
        int k1 = heightmap[i][j][k] - 1;
        int l1 = k1 - 120;
        int i2 = k1 - 230;
        int j2 = k1 - 238;
        if (l < 16) {
            if (l == 1) {
                if (i1 > xCameraPosition) {
                    if (!method324(i1, k1, j1))
                        return false;
                    if (!method324(i1, k1, j1 + 128))
                        return false;
                }
                if (i > 0) {
                    if (!method324(i1, l1, j1))
                        return false;
                    if (!method324(i1, l1, j1 + 128))
                        return false;
                }
                return method324(i1, i2, j1) && method324(i1, i2, j1 + 128);
            }
            if (l == 2) {
                if (j1 < yCameraPosition) {
                    if (!method324(i1, k1, j1 + 128))
                        return false;
                    if (!method324(i1 + 128, k1, j1 + 128))
                        return false;
                }
                if (i > 0) {
                    if (!method324(i1, l1, j1 + 128))
                        return false;
                    if (!method324(i1 + 128, l1, j1 + 128))
                        return false;
                }
                return method324(i1, i2, j1 + 128)
                        && method324(i1 + 128, i2, j1 + 128);
            }
            if (l == 4) {
                if (i1 < xCameraPosition) {
                    if (!method324(i1 + 128, k1, j1))
                        return false;
                    if (!method324(i1 + 128, k1, j1 + 128))
                        return false;
                }
                if (i > 0) {
                    if (!method324(i1 + 128, l1, j1))
                        return false;
                    if (!method324(i1 + 128, l1, j1 + 128))
                        return false;
                }
                return method324(i1 + 128, i2, j1)
                        && method324(i1 + 128, i2, j1 + 128);
            }
            if (l == 8) {
                if (j1 > yCameraPosition) {
                    if (!method324(i1, k1, j1))
                        return false;
                    if (!method324(i1 + 128, k1, j1))
                        return false;
                }
                if (i > 0) {
                    if (!method324(i1, l1, j1))
                        return false;
                    if (!method324(i1 + 128, l1, j1))
                        return false;
                }
                return method324(i1, i2, j1) && method324(i1 + 128, i2, j1);
            }
        }
        if (!method324(i1 + 64, j2, j1 + 64))
            return false;
        if (l == 16)
            return method324(i1, i2, j1 + 128);
        if (l == 32)
            return method324(i1 + 128, i2, j1 + 128);
        if (l == 64)
            return method324(i1 + 128, i2, j1);
        if (l == 128) {
            return method324(i1, i2, j1);
        } else {
            System.out.println("Warning unsupported wall interfaceType");
            return true;
        }
    }

    private boolean method322(int i, int j, int k, int l) {
        if (!method320(i, j, k))
            return false;
        int i1 = j << 7;
        int j1 = k << 7;
        return method324(i1 + 1, heightmap[i][j][k] - l, j1 + 1)
                && method324((i1 + 128) - 1,
                heightmap[i][j + 1][k] - l, j1 + 1)
                && method324((i1 + 128) - 1,
                heightmap[i][j + 1][k + 1] - l,
                (j1 + 128) - 1)
                && method324(i1 + 1, heightmap[i][j][k + 1] - l,
                (j1 + 128) - 1);
    }

    private boolean method323(int i, int j, int k, int l, int i1, int j1) {
        if (j == k && l == i1) {
            if (!method320(i, j, l))
                return false;
            int k1 = j << 7;
            int i2 = l << 7;
            return method324(k1 + 1, heightmap[i][j][l] - j1,
                    i2 + 1)
                    && method324((k1 + 128) - 1,
                    heightmap[i][j + 1][l] - j1, i2 + 1)
                    && method324((k1 + 128) - 1,
                    heightmap[i][j + 1][l + 1] - j1,
                    (i2 + 128) - 1)
                    && method324(k1 + 1, heightmap[i][j][l + 1]
                    - j1, (i2 + 128) - 1);
        }
        for (int l1 = j; l1 <= k; l1++) {
            for (int j2 = l; j2 <= i1; j2++)
                if (anIntArrayArrayArray445[i][l1][j2] == -anInt448)
                    return false;

        }

        int k2 = (j << 7) + 1;
        int l2 = (l << 7) + 2;
        int i3 = heightmap[i][j][l] - j1;
        if (!method324(k2, i3, l2))
            return false;
        int j3 = (k << 7) - 1;
        if (!method324(j3, i3, l2))
            return false;
        int k3 = (i1 << 7) - 1;
        return method324(k2, i3, k3) && method324(j3, i3, k3);
    }

    private boolean method324(int i, int j, int k) {
        for (int l = 0; l < anInt475; l++) {
            CullingCluster class47 = aClass47Array476[l];
            if (class47.tileDistanceEnum == 1) {
                int i1 = class47.worldStartX - i;
                if (i1 > 0) {
                    int j2 = class47.worldStartY + (class47.worldDistanceFromCameraStartY * i1 >> 8);
                    int k3 = class47.worldEndY + (class47.worldDistanceFromCameraEndY * i1 >> 8);
                    int l4 = class47.worldStartZ + (class47.worldDistanceFromCameraStartZ * i1 >> 8);
                    int i6 = class47.worldEndZ + (class47.worldDistanceFromCameraEndZ * i1 >> 8);
                    if (k >= j2 && k <= k3 && j >= l4 && j <= i6)
                        return true;
                }
            } else if (class47.tileDistanceEnum == 2) {
                int j1 = i - class47.worldStartX;
                if (j1 > 0) {
                    int k2 = class47.worldStartY + (class47.worldDistanceFromCameraStartY * j1 >> 8);
                    int l3 = class47.worldEndY + (class47.worldDistanceFromCameraEndY * j1 >> 8);
                    int i5 = class47.worldStartZ + (class47.worldDistanceFromCameraStartZ * j1 >> 8);
                    int j6 = class47.worldEndZ + (class47.worldDistanceFromCameraEndZ * j1 >> 8);
                    if (k >= k2 && k <= l3 && j >= i5 && j <= j6)
                        return true;
                }
            } else if (class47.tileDistanceEnum == 3) {
                int k1 = class47.worldStartY - k;
                if (k1 > 0) {
                    int l2 = class47.worldStartX + (class47.worldDistanceFromCameraStartX * k1 >> 8);
                    int i4 = class47.worldEndX + (class47.worldDistanceFromCameraEndX * k1 >> 8);
                    int j5 = class47.worldStartZ + (class47.worldDistanceFromCameraStartZ * k1 >> 8);
                    int k6 = class47.worldEndZ + (class47.worldDistanceFromCameraEndZ * k1 >> 8);
                    if (i >= l2 && i <= i4 && j >= j5 && j <= k6)
                        return true;
                }
            } else if (class47.tileDistanceEnum == 4) {
                int l1 = k - class47.worldStartY;
                if (l1 > 0) {
                    int i3 = class47.worldStartX + (class47.worldDistanceFromCameraStartX * l1 >> 8);
                    int j4 = class47.worldEndX + (class47.worldDistanceFromCameraEndX * l1 >> 8);
                    int k5 = class47.worldStartZ + (class47.worldDistanceFromCameraStartZ * l1 >> 8);
                    int l6 = class47.worldEndZ + (class47.worldDistanceFromCameraEndZ * l1 >> 8);
                    if (i >= i3 && i <= j4 && j >= k5 && j <= l6)
                        return true;
                }
            } else if (class47.tileDistanceEnum == 5) {
                int i2 = j - class47.worldStartZ;
                if (i2 > 0) {
                    int j3 = class47.worldStartX + (class47.worldDistanceFromCameraStartX * i2 >> 8);
                    int k4 = class47.worldEndX + (class47.worldDistanceFromCameraEndX * i2 >> 8);
                    int l5 = class47.worldStartY + (class47.worldDistanceFromCameraStartY * i2 >> 8);
                    int i7 = class47.worldEndY + (class47.worldDistanceFromCameraEndY * i2 >> 8);
                    if (i >= j3 && i <= k4 && k >= l5 && k <= i7)
                        return true;
                }
            }
        }

        return false;
    }
}
