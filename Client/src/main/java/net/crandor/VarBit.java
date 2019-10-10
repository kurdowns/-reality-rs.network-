package net.crandor;

final class VarBit {

    static void unpackConfig(StreamLoader streamLoader) {
        Stream stream = new Stream(streamLoader.getDataForName("varbit.dat"));
        int cacheSize = stream.readUnsignedWord();
        if (cache == null)
            cache = new VarBit[cacheSize];
        for (int j = 0; j < cacheSize; j++) {
            if (cache[j] == null)
                cache[j] = new VarBit();
            cache[j].readValues(stream);
        }

        if (stream.currentOffset != stream.buffer.length)
            System.out.println("varbit load mismatch");

        // note(stuart) fixes duel interface
        for(int i = 643; i <= 656; i++) {
            cache[i].variable = 286;
            cache[i].leastSignificantBit = 14 + (i - 643);
            cache[i].mostSignificantBit = 14 + (i - 643);
        }

    }

	public static int getValue(int id) {
		VarBit varBit = cache[id];
		int varp = varBit.variable;
		int startBit = varBit.leastSignificantBit;
		int endBit = varBit.mostSignificantBit;
		int mask = Client.bitfieldMask[endBit - startBit];
		return Client.playerVariables[varp] >> startBit & mask;
	}

    private void readValues(Stream stream) {
        variable = stream.readUnsignedWord();
        leastSignificantBit = stream.readUnsignedByte();
        mostSignificantBit = stream.readUnsignedByte();
    }

    static VarBit cache[];
    int variable;
    int leastSignificantBit;
    int mostSignificantBit;
}
