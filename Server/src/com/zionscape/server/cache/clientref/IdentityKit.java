package com.zionscape.server.cache.clientref;

import com.zionscape.server.util.Stream;

public final class IdentityKit {

    public static void unpackConfig(StreamLoader streamLoader) {
        Stream stream = new Stream(streamLoader.getDataForName("idk.dat"));
        length = stream.readUnsignedWord();
        if (cache == null)
            cache = new IdentityKit[length];
        for (int j = 0; j < length; j++) {
            if (cache[j] == null)
                cache[j] = new IdentityKit();
            cache[j].readValues(stream);
            cache[j].recolourOriginal[0] = 55232;
            cache[j].recolourTarget[0] = 6798;
        }
    }

    private void readValues(Stream stream) {
        do {
            int i = stream.readUnsignedByte();
            if (i == 0)
                return;
            if (i == 1)
                bodyPartID = stream.readUnsignedByte();
            else if (i == 2) {
                int modelCount = stream.readUnsignedByte();
                bodyModelIDs = new int[modelCount];
                for (int k = 0; k < modelCount; k++)
                    bodyModelIDs[k] = stream.readUnsignedWord();

            } else if (i == 3)
                notSelectable = true;
            else if (i >= 40 && i < 50)
                recolourOriginal[i - 40] = stream.readUnsignedWord();
            else if (i >= 50 && i < 60)
                recolourTarget[i - 50] = stream.readUnsignedWord();
            else if (i >= 60 && i < 70)
                headModelIDs[i - 60] = stream.readUnsignedWord();
            else
                System.out.println("Error unrecognised config code: " + i);
        } while (true);
    }

    private IdentityKit() {
        bodyPartID = -1;
        recolourOriginal = new int[6];
        recolourTarget = new int[6];
        notSelectable = false;
    }

    public static int length;
    public static IdentityKit cache[];
    public int bodyPartID;
    private int[] bodyModelIDs;
    private final int[] recolourOriginal;
    private final int[] recolourTarget;
    private final int[] headModelIDs = {-1, -1, -1, -1, -1};
    public boolean notSelectable;
}
