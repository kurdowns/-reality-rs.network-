package net.crandor;

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
            cache[j].recolourOriginal[0] = (short) 55232;
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
                recolourOriginal[i - 40] = (short) stream.readUnsignedWord();
            else if (i >= 50 && i < 60)
                recolourTarget[i - 50] = (short) stream.readUnsignedWord();
            else if (i >= 60 && i < 70)
                headModelIDs[i - 60] = stream.readUnsignedWord();
            else
                System.out.println("Error unrecognised config code: " + i);
        } while (true);
    }

    public boolean isBodyDownloaded() {
        if (bodyModelIDs == null)
            return true;
        boolean isDownloaded = true;
        for (int j = 0; j < bodyModelIDs.length; j++)
            if (!Model.isCached(bodyModelIDs[j]))
                isDownloaded = false;

        return isDownloaded;
    }

    public Model getBodyModel() {
        if (bodyModelIDs == null)
            return null;
        Model subModels[] = new Model[bodyModelIDs.length];
        for (int i = 0; i < bodyModelIDs.length; i++)
            subModels[i] = Model.getModel(bodyModelIDs[i]);

        Model model;
        if (subModels.length == 1)
            model = subModels[0];
        else
            model = new Model(subModels,
                    subModels.length);
        for (int j = 0; j < 6; j++) {
            if (recolourOriginal[j] == 0)
                break;
            model.setColor(recolourOriginal[j], recolourTarget[j]);
        }

        return model;
    }

    public boolean isHeadDownloaded() {
        boolean isDownloaded = true;
        for (int i = 0; i < 5; i++)
            if (headModelIDs[i] != -1 && !Model.isCached(headModelIDs[i]))
                isDownloaded = false;

        return isDownloaded;
    }

    public Model getHeadModel() {
        Model subModels[] = new Model[5];
        int j = 0;
        for (int k = 0; k < 5; k++)
            if (headModelIDs[k] != -1)
                subModels[j++] = Model
                        .getModel(headModelIDs[k]);

        Model model = new Model(subModels, j);
        for (int l = 0; l < 6; l++) {
            if (recolourOriginal[l] == 0)
                break;
            model.setColor(recolourOriginal[l], recolourTarget[l]);
        }

        return model;
    }

    private IdentityKit() {
        bodyPartID = -1;
        recolourOriginal = new short[6];
        recolourTarget = new short[6];
        notSelectable = false;
    }

    public static int length;
    public static IdentityKit cache[];
    public int bodyPartID;
    private int[] bodyModelIDs;
    private final short[] recolourOriginal;
    private final short[] recolourTarget;
    private final int[] headModelIDs = {-1, -1, -1, -1, -1};
    public boolean notSelectable;
}
