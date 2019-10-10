package net.crandor;

final class PlainTile {

    public PlainTile(int i, int j, int k, int l, int i1, int j1, boolean flag) {
        anInt716 = i;// shadow_a
        anInt717 = j;// shadow_b
        anInt718 = k;// shadow_d
        anInt719 = l;// shadow_c
        anInt720 = i1;// texture
        colourRGB = j1;// rgb
        flat = flag;
    }

    final int anInt716;
    final int anInt717;
    final int anInt718;
    final int anInt719;
    final int anInt720;
    boolean flat;
    final int colourRGB;
}
