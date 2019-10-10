package net.crandor;

final class Envelope {

    public void decode(Stream stream) {
        form = stream.readUnsignedByte();
        anInt538 = stream.readDWord();
        anInt539 = stream.readDWord();
        decodeSegments(stream);
    }

    public void decodeSegments(Stream stream) {
        segmentcount = stream.readUnsignedByte();
        segmentDuration = new int[segmentcount];
        segmentPeak = new int[segmentcount];
        for (int i = 0; i < segmentcount; i++) {
            segmentDuration[i] = stream.readUnsignedWord();
            segmentPeak[i] = stream.readUnsignedWord();
        }

    }

    void resetValues() {
        checkPoint = 0;
        segmentPtr = 0;
        step = 0;
        amplitude = 0;
        tick = 0;
    }

    int evaluate(int i) {
        if (tick >= checkPoint) {
            amplitude = segmentPeak[segmentPtr++] << 15;
            if (segmentPtr >= segmentcount)
                segmentPtr = segmentcount - 1;
            checkPoint = (int) ((segmentDuration[segmentPtr] / 65536D) * i);
            if (checkPoint > tick)
                step = ((segmentPeak[segmentPtr] << 15) - amplitude)
                        / (checkPoint - tick);
        }
        amplitude += step;
        tick++;
        return amplitude - step >> 15;
    }

    public Envelope() {
    }

    private int segmentcount;
    private int[] segmentDuration;
    private int[] segmentPeak;
    int anInt538;
    int anInt539;
    int form;
    private int checkPoint;
    private int segmentPtr;
    private int step;
    private int amplitude;
    private int tick;
    
}
