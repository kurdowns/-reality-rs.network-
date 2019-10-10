package net.crandor;

final class Varp {

    static void unpackConfig(StreamLoader streamLoader) {
        Stream stream = new Stream(streamLoader.getDataForName("varp.dat"));
        int cacheSize = stream.readUnsignedWord();
        if (cache == null)
            cache = new Varp[cacheSize + 1300];
        for (int j = 0; j < cacheSize + 1300; j++) {
            if (cache[j] == null)
                cache[j] = new Varp();
            if (j < cacheSize)
                cache[j].readValues(stream, j);
        }
        if (stream.currentOffset != stream.buffer.length)
            System.out.println("varptype load mismatch");
    }

    private void readValues(Stream stream, int i) {
        do {
            int j = stream.readUnsignedByte();
            if (j == 0)
                return;
            if (j == 5)
                clientCode = stream.readUnsignedWord();
        } while (true);
    }

    static Varp cache[];
    int clientCode;

}
