package net.crandor;

final class ShapedTile {

    public int color61;
    public int color71;
    public int color81;
    public int color91;
    public int color62;
    public int color92;
    public int color82;
    public int color72;

    public ShapedTile(int y, int hsl_shadow_a, int shadow_c, int zd,
                      int overlay_texture, int hsl_shadow_d, int rotation, int shadow_a,
                      int rgb_bitset, int shadow_d, int zc, int zb, int za, int shape,
                      int hsl_shadow_c, int hsl_shadow_b, int shadow_b, int x, int rgb,
                      int underlay_texture) {
        color61 = shadow_a;
        color71 = shadow_b;
        color91 = shadow_c;
        color81 = shadow_d;
        color62 = hsl_shadow_a;
        color72 = hsl_shadow_b;
        color92 = hsl_shadow_c;
        color82 = hsl_shadow_d;
        layerBooleanIndex = shape;
        layerIndexIndex = rotation;
        minimapRgb2 = rgb_bitset;
        minimapRgb1 = rgb;
        char c = '\200';
        int i5 = c / 2;
        int j5 = c / 4;
        int k5 = (c * 3) / 4;
        int ai[] = anIntArrayArray696[shape];
        int l5 = ai.length;
        origVertexX = new int[l5];
        origVertexY = new int[l5];
        origVertexZ = new int[l5];
        int tile_shadow[] = new int[l5];
        int tile_hsl_shadow[] = new int[l5];
        int i6 = x * c;
        int j6 = y * c;
        for (int k6 = 0; k6 < l5; k6++) {
            int l6 = ai[k6];
            if ((l6 & 1) == 0 && l6 <= 8)
                l6 = (l6 - rotation - rotation - 1 & 7) + 1;
            if (l6 > 8 && l6 <= 12)
                l6 = (l6 - 9 - rotation & 3) + 9;
            if (l6 > 12 && l6 <= 16)
                l6 = (l6 - 13 - rotation & 3) + 13;
            int i7;
            int k7;
            int i8;
            int shadow;
            int hsl_shadow;
            if (l6 == 1) {
                i7 = i6;
                k7 = j6;
                i8 = za;
                shadow = shadow_a;
                hsl_shadow = hsl_shadow_a;
            } else if (l6 == 2) {
                i7 = i6 + i5;
                k7 = j6;
                i8 = za + zb >> 1;
                shadow = shadow_a + shadow_b >> 1;
                hsl_shadow = hsl_shadow_a + hsl_shadow_b >> 1;
            } else if (l6 == 3) {
                i7 = i6 + c;
                k7 = j6;
                i8 = zb;
                shadow = shadow_b;
                hsl_shadow = hsl_shadow_b;
            } else if (l6 == 4) {
                i7 = i6 + c;
                k7 = j6 + i5;
                i8 = zb + zd >> 1;
                shadow = shadow_b + shadow_d >> 1;
                hsl_shadow = hsl_shadow_b + hsl_shadow_d >> 1;
            } else if (l6 == 5) {
                i7 = i6 + c;
                k7 = j6 + c;
                i8 = zd;
                shadow = shadow_d;
                hsl_shadow = hsl_shadow_d;
            } else if (l6 == 6) {
                i7 = i6 + i5;
                k7 = j6 + c;
                i8 = zd + zc >> 1;
                shadow = shadow_d + shadow_c >> 1;
                hsl_shadow = hsl_shadow_d + hsl_shadow_c >> 1;
            } else if (l6 == 7) {
                i7 = i6;
                k7 = j6 + c;
                i8 = zc;
                shadow = shadow_c;
                hsl_shadow = hsl_shadow_c;
            } else if (l6 == 8) {
                i7 = i6;
                k7 = j6 + i5;
                i8 = zc + za >> 1;
                shadow = shadow_c + shadow_a >> 1;
                hsl_shadow = hsl_shadow_c + hsl_shadow_a >> 1;
            } else if (l6 == 9) {
                i7 = i6 + i5;
                k7 = j6 + j5;
                i8 = za + zb >> 1;
                shadow = shadow_a + shadow_b >> 1;
                hsl_shadow = hsl_shadow_a + hsl_shadow_b >> 1;
            } else if (l6 == 10) {
                i7 = i6 + k5;
                k7 = j6 + i5;
                i8 = zb + zd >> 1;
                shadow = shadow_b + shadow_d >> 1;
                hsl_shadow = hsl_shadow_b + hsl_shadow_d >> 1;
            } else if (l6 == 11) {
                i7 = i6 + i5;
                k7 = j6 + k5;
                i8 = zd + zc >> 1;
                shadow = shadow_d + shadow_c >> 1;
                hsl_shadow = hsl_shadow_d + hsl_shadow_c >> 1;
            } else if (l6 == 12) {
                i7 = i6 + j5;
                k7 = j6 + i5;
                i8 = zc + za >> 1;
                shadow = shadow_c + shadow_a >> 1;
                hsl_shadow = hsl_shadow_c + hsl_shadow_a >> 1;
            } else if (l6 == 13) {
                i7 = i6 + j5;
                k7 = j6 + j5;
                i8 = za;
                shadow = shadow_a;
                hsl_shadow = hsl_shadow_a;
            } else if (l6 == 14) {
                i7 = i6 + k5;
                k7 = j6 + j5;
                i8 = zb;
                shadow = shadow_b;
                hsl_shadow = hsl_shadow_b;
            } else if (l6 == 15) {
                i7 = i6 + k5;
                k7 = j6 + k5;
                i8 = zd;
                shadow = shadow_d;
                hsl_shadow = hsl_shadow_d;
            } else {
                i7 = i6 + j5;
                k7 = j6 + k5;
                i8 = zc;
                shadow = shadow_c;
                hsl_shadow = hsl_shadow_c;
            }
            origVertexX[k6] = i7;
            origVertexY[k6] = i8;
            origVertexZ[k6] = k7;
            tile_shadow[k6] = shadow;
            tile_hsl_shadow[k6] = hsl_shadow;
        }

        int ai3[] = anIntArrayArray697[shape];
        int j7 = ai3.length / 4;
        anIntArray679 = new int[j7];
        anIntArray680 = new int[j7];
        anIntArray681 = new int[j7];
        hsl1 = new int[j7];
        hsl2 = new int[j7];
        hsl3 = new int[j7];
        if (!ObjectManager.disableGroundTextures) {
            if (overlay_texture != -1 || underlay_texture != -1) {
                anIntArray682 = new int[j7];
            }
        }
        int l7 = 0;
        for (int j8 = 0; j8 < j7; j8++) {
            int l8 = ai3[l7];
            int k9 = ai3[l7 + 1];
            int i10 = ai3[l7 + 2];
            int k10 = ai3[l7 + 3];
            l7 += 4;
            if (k9 < 4)
                k9 = k9 - rotation & 3;
            if (i10 < 4)
                i10 = i10 - rotation & 3;
            if (k10 < 4)
                k10 = k10 - rotation & 3;
            anIntArray679[j8] = k9;
            anIntArray680[j8] = i10;
            anIntArray681[j8] = k10;
            if (l8 == 0) {
                if (ObjectManager.disableGroundTextures) {
                    if (anIntArray682 != null) {
                        anIntArray682[j8] = -1;// -1
                    }
                } else {
                    if (anIntArray682 != null) {
                        anIntArray682[j8] = underlay_texture;// -1
                    }
                }

                hsl1[j8] = tile_shadow[k9];
                hsl2[j8] = tile_shadow[i10];
                hsl3[j8] = tile_shadow[k10];
            } else {
                if (ObjectManager.disableGroundTextures) {
                    if (anIntArray682 != null) {
                        anIntArray682[j8] = -1;
                    }
                } else {
                    if (anIntArray682 != null) {
                        anIntArray682[j8] = overlay_texture;
                    }
                }
                hsl1[j8] = tile_hsl_shadow[k9];
                hsl2[j8] = tile_hsl_shadow[i10];
                hsl3[j8] = tile_hsl_shadow[k10];
            }
        }

        int i9 = za;
        int l9 = zb;
        if (zb < i9)
            i9 = zb;
        if (zb > l9)
            l9 = zb;
        if (zd < i9)
            i9 = zd;
        if (zd > l9)
            l9 = zd;
        if (zc < i9)
            i9 = zc;
        if (zc > l9)
            l9 = zc;
        i9 /= 14;
        l9 /= 14;
    }

    final int[] origVertexX;
    final int[] origVertexY;
    final int[] origVertexZ;
    final int[] hsl1;
    final int[] hsl2;
    final int[] hsl3;
    final int[] anIntArray679;
    final int[] anIntArray680;
    final int[] anIntArray681;
    int anIntArray682[];
    final int layerBooleanIndex;
    final int layerIndexIndex;
    final int minimapRgb2;
    final int minimapRgb1;
    static final int[] vertex2dX = new int[6];
    static final int[] vertex2dY = new int[6];
    static final int[] vertex2dZ = new int[6];
    static final int[] anIntArray690 = new int[6];
    static final int[] anIntArray691 = new int[6];
    static final int[] anIntArray692 = new int[6];
    static final int[] anIntArray693 = {1, 0};
    static final int[] anIntArray694 = {2, 1};
    static final int[] anIntArray695 = {3, 3};
    private static final int[][] anIntArrayArray696 = {{1, 3, 5, 7},
            {1, 3, 5, 7}, {1, 3, 5, 7}, {1, 3, 5, 7, 6},
            {1, 3, 5, 7, 6}, {1, 3, 5, 7, 6}, {1, 3, 5, 7, 6},
            {1, 3, 5, 7, 2, 6}, {1, 3, 5, 7, 2, 8}, {1, 3, 5, 7, 2, 8},
            {1, 3, 5, 7, 11, 12}, {1, 3, 5, 7, 11, 12},
            {1, 3, 5, 7, 13, 14}};
    private static final int[][] anIntArrayArray697 = {
            {0, 1, 2, 3, 0, 0, 1, 3},
            {1, 1, 2, 3, 1, 0, 1, 3},
            {0, 1, 2, 3, 1, 0, 1, 3},
            {0, 0, 1, 2, 0, 0, 2, 4, 1, 0, 4, 3},
            {0, 0, 1, 4, 0, 0, 4, 3, 1, 1, 2, 4},
            {0, 0, 4, 3, 1, 0, 1, 2, 1, 0, 2, 4},
            {0, 1, 2, 4, 1, 0, 1, 4, 1, 0, 4, 3},
            {0, 4, 1, 2, 0, 4, 2, 5, 1, 0, 4, 5, 1, 0, 5, 3},
            {0, 4, 1, 2, 0, 4, 2, 3, 0, 4, 3, 5, 1, 0, 4, 5},
            {0, 0, 4, 5, 1, 4, 1, 2, 1, 4, 2, 3, 1, 4, 3, 5},
            {0, 0, 1, 5, 0, 1, 4, 5, 0, 1, 2, 4, 1, 0, 5, 3, 1, 5, 4, 3, 1, 4,
                    2, 3},
            {1, 0, 1, 5, 1, 1, 4, 5, 1, 1, 2, 4, 0, 0, 5, 3, 0, 5, 4, 3, 0, 4,
                    2, 3},
            {1, 0, 5, 4, 1, 0, 1, 5, 0, 0, 4, 3, 0, 4, 5, 3, 0, 5, 2, 3, 0, 1,
                    2, 5}};

}
