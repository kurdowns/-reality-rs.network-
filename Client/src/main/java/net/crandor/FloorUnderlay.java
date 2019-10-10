package net.crandor;

public final class FloorUnderlay {

    private static FloorUnderlay osrsCache[];
    private static FloorUnderlay cache[];
    public int colour2;
    public int anInt391;
    public boolean occlude;
    public int hue;
    public int saturation;
    public int lightness;
    public int hue2;
    public int hueWeight;
    public int paletteIndex;

    private FloorUnderlay() {
        anInt391 = -1;
        occlude = true;
    }

    public static int getFloorLength() {
        return cache.length;
    }

    public static FloorUnderlay getFloor(int id, boolean osrs) {
        if (Client.getRegionId() == 14682) {
            osrs = false;
        }

        return osrs ? osrsCache[id] : cache[id];
    }

    public static void setCache(FloorUnderlay[] cache) {
        FloorUnderlay.cache = cache;
    }

    public static void unpackConfig(StreamLoader streamLoader) {
        Stream stream = new Stream(streamLoader.getDataForName("flo.dat"));
        int cacheSize = stream.readUnsignedWord();

        System.out.println("underlay size " + cacheSize);

        if (cache == null)
            cache = new FloorUnderlay[cacheSize];
        for (int j = 0; j < cacheSize; j++) {
            if (cache[j] == null)
                cache[j] = new FloorUnderlay();
            cache[j].readValues(stream);
        }

        stream = new Stream(streamLoader.getDataForName("osrsflo.dat"));
        cacheSize = stream.readUnsignedWord();

        System.out.println("osrs underlay size " + cacheSize);

        if (osrsCache == null)
            osrsCache = new FloorUnderlay[cacheSize];
        for (int j = 0; j < cacheSize; j++) {
            if (osrsCache[j] == null)
                osrsCache[j] = new FloorUnderlay();
            osrsCache[j].readValues(stream);
        }
    }

    private void readValues(Stream stream) {
        do {
            int i = stream.readUnsignedByte();
            if (i == 0)
                return;
            else if (i == 1) {
                colour2 = stream.read3Bytes();
                rgb2hsl(colour2);
            } else if (i == 2)
                anInt391 = stream.readUnsignedByte();
            else if (i == 3) {
            } else if (i == 5)
                occlude = false;
            else if (i == 6)
                stream.readString();
            else if (i == 7) {
                int j = hue;
                int k = saturation;
                int l = lightness;
                int i1 = hue2;
                int j1 = stream.read3Bytes();

                rgb2hsl(j1);
                hue = j;
                saturation = k;
                lightness = l;
                hue2 = i1;
                hueWeight = i1;
            } else {
                System.out.println("Error unrecognised floor underlay config code: " + i);
            }
        } while (true);
    }

    private void rgb2hsl(int i) {
        double r = (i >> 16 & 0xff) / 256D;
        double g = (i >> 8 & 0xff) / 256D;
        double b = (i & 0xff) / 256D;
        double cmin = r;
        if (g < cmin)
            cmin = g;
        if (b < cmin)
            cmin = b;
        double cmax = r;
        if (g > cmax)
            cmax = g;
        if (b > cmax)
            cmax = b;
        double d5 = 0.0D;
        double d6 = 0.0D;
        double d7 = (cmin + cmax) / 2D;
        if (cmin != cmax) {
            if (d7 < 0.5D)
                d6 = (cmax - cmin) / (cmax + cmin);
            if (d7 >= 0.5D)
                d6 = (cmax - cmin) / (2D - cmax - cmin);
            if (r == cmax)
                d5 = (g - b) / (cmax - cmin);
            else if (g == cmax)
                d5 = 2D + (b - r) / (cmax - cmin);
            else if (b == cmax)
                d5 = 4D + (r - g) / (cmax - cmin);
        }
        d5 /= 6D;
        hue = (int) (d5 * 256D);
        saturation = (int) (d6 * 256D);
        lightness = (int) (d7 * 256D);
        if (saturation < 0)
            saturation = 0;
        else if (saturation > 255)
            saturation = 255;
        if (lightness < 0)
            lightness = 0;
        else if (lightness > 255)
            lightness = 255;
        if (d7 > 0.5D)
            hueWeight = (int) ((1.0D - d7) * d6 * 512D);
        else
            hueWeight = (int) (d7 * d6 * 512D);
        if (hueWeight < 1)
            hueWeight = 1;
        hue2 = (int) (d5 * hueWeight);

        paletteIndex = hslToRuneTekPalette(hue, saturation, lightness);
    }

    public static int hslToRuneTekPalette(int hue, int saturation, int lightness) {
        if (lightness > 179) {
            saturation >>= 1;
        } else if (lightness > 192) {
            saturation >>= 2;
        } else if (lightness > 217) {
            saturation >>= 3;
        } else if (lightness > 243) {
            saturation >>= 4;
        }

        return ((hue >> 2 & 0x3f) << 10) + (saturation >> 5 << 7) + (lightness >> 1);
    }

}