package net.crandor;

final class FloorOverlay {

    private static FloorOverlay[] cache;
    private static FloorOverlay[] osrsCache;
    /**
     * Hsl
     */
    public int hue;
    public int saturation;
    public int luminance;
    public int anotherHue;
    public int anotherSaturation;
    public int anotherLuminance;
    public int anInt397;
    public int anInt398;
    public int anInt399;
    int groundTextureOverlay = -1;// textureid
    int groundColorOverlay;// rgb
    boolean occludeOverlay;// _5
    int groundHueOverlay;// 394
    int groundSaturationOverlay;// 395
    int groundLightnessOverlay;// 396
    int weighted_hue;// 397
    int chroma_overlay;// 398
    int hslOverlayColor;// 399
    int alpha;// _16
    int detailedTexture;// _15
    int detailedTexture9;// _9
    int detailedTexture11;// _11
    int detailedTexture14;// _14
    int detailedColor;// _7
    int detailedColor13;// _13
    boolean boolean_10;// _10
    boolean boolean_12;// _12
    private FloorOverlay() {
        detailedColor = -1;
        detailedTexture14 = 64;
        boolean_10 = true;
        detailedTexture11 = 8;
        alpha = 127;
        groundTextureOverlay = -1;
        boolean_12 = false;
        detailedColor13 = 1190717;
        groundColorOverlay = 0;
        detailedColor = 0;
        detailedTexture9 = 512;
        occludeOverlay = true;
    }

    public static int getFloorLength(boolean osrs) {
        if (Client.getRegionId() == 14682) {
            osrs = false;
        }

        return osrs ? osrsCache.length : cache.length;
    }

    public static FloorOverlay getFloor(int id, boolean osrs) {
        if (Client.getRegionId() == 14682) {
            osrs = false;
        }

        return osrs ? osrsCache[id] : cache[id];
    }

    static void unpackConfig(StreamLoader archive) {
        Stream data = new Stream(archive.getDataForName("overlay.dat"));
        int cacheSize = data.readUnsignedWord();

        System.out.println("overlay size: " + cacheSize);

        if (cache == null)
            cache = new FloorOverlay[cacheSize];
        for (int index = 0; index < cacheSize; index++) {
            if (cache[index] == null)
                cache[index] = new FloorOverlay();
            cache[index].readValues(data);
        }

        data = new Stream(archive.getDataForName("osrsoverlay.dat"));
        cacheSize = data.readUnsignedWord();

        System.out.println("osrs overlay size: " + cacheSize);

        if (osrsCache == null)
            osrsCache = new FloorOverlay[cacheSize];
        for (int index = 0; index < cacheSize; index++) {
            if (osrsCache[index] == null)
                osrsCache[index] = new FloorOverlay();
            osrsCache[index].readValues(data);
        }
    }

    private static int setColor(int color) {
        return color != 0xff00ff ? rgb2hsl(color) : -1;
    }

    private static int rgb2hsl(int color) {
        double r = ((color >>> 16) & 0xff) / 256.0D;
        double g = ((color >>> 8) & 0xff) / 256.0D;
        double b = (color & 0xff) / 256.0D;
        double min = r;
        if (min > g)
            min = g;

        if (min > b)
            min = b;

        double max = r;
        if (g > max)
            max = g;

        if (b > max)
            max = b;

        double hue = 0.0;
        double saturation = 0.0;
        double luminance = (min + max) / 2.0D;
        if (max != min) {
            if (luminance < 0.5D)
                saturation = (max - min) / (min + max);

            if (max != r) {
                if (max == g)
                    hue = (b - r) / (max - min) + 2.0D;
                else if (max == b)
                    hue = (r - g) / (max - min) + 4.0D;

            } else
                hue = (g - b) / (max - min);

            if (luminance >= 0.5D)
                saturation = (max - min) / (2.0D - min - max);

        }
        hue /= 6.0D;
        int hueOverlay = (int) (hue * 256.0D);
        int satOverlay = (int) (saturation * 256.0D);
        int lumOverlay = (int) (luminance * 256.0D);
        if (satOverlay < 0)
            satOverlay = 0;
        else if (satOverlay > 0xff)
            satOverlay = 0xff;

        if (lumOverlay < 0)
            lumOverlay = 0;
        else if (lumOverlay > 0xff)
            lumOverlay = 0xff;

        if (lumOverlay > 242)
            satOverlay >>= 4;
        else if (lumOverlay > 217)
            satOverlay >>= 3;
        else if (lumOverlay > 192)
            satOverlay >>= 2;
        else if (lumOverlay > 179)
            satOverlay >>= 1;

        return (lumOverlay >> 1)
                + ((satOverlay >> 5 << 7) + ((hueOverlay & 0xff) >> 2 << 10));
    }

    private void readValues(Stream buffer) {
        while (true) {
            int opcode = buffer.readUnsignedByte();// ?
            switch (opcode) {
                case 0:
                    return;
                case 1:
                    groundColorOverlay = setColor(buffer.read3Bytes());
                    break;
                case 2:
                    groundTextureOverlay = buffer.readUnsignedByte();
                    if (groundTextureOverlay == 65535)
                        groundTextureOverlay = -1;
                    break;
                case 3:
                    groundTextureOverlay = buffer.readSignedWord();
                    break;
                case 5:
                    occludeOverlay = false;
                    break;
                case 7:
                    detailedColor = setColor(buffer.read3Bytes());
                    break;
                case 8:
                    break;
                case 9:
                    detailedTexture9 = buffer.readSignedWord() << 2;
                    break;
                case 10:
                    boolean_10 = false;
                    break;
                case 11:
                    detailedTexture11 = buffer.readUnsignedByte();
                    break;
                case 12:
                    boolean_12 = true;
                    break;
                case 13:
                    detailedColor13 = buffer.read3Bytes();
                    break;
                case 14:
                    detailedTexture14 = buffer.readUnsignedByte() << 2;
                    break;
                case 16:
                    alpha = buffer.readUnsignedByte();
                    break;
            }
        }
    }

}
