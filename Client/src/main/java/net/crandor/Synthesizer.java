package net.crandor;

final class Synthesizer {

    public static void initialise() {
        NOISE = new int[32768];
        for (int i = 0; i < 32768; i++)
            if (Math.random() > 0.5D)
                NOISE[i] = 1;
            else
                NOISE[i] = -1;

        SINE = new int[32768];
        for (int j = 0; j < 32768; j++)
            SINE[j] = (int) (Math.sin(j / 5215.1903000000002D) * 16384D);

        samples = new int[0x35d54];
    }

    public int[] synthesize(int sampleLength, int j) {
        for (int k = 0; k < sampleLength; k++)
            samples[k] = 0;

        if (j < 10)
            return samples;
        double d = sampleLength / (j + 0.0D);
        samp1.resetValues();
        samp2.resetValues();
        int l = 0;
        int i1 = 0;
        int j1 = 0;
        if (samp3 != null) {
            samp3.resetValues();
            samp4.resetValues();
            l = (int) (((samp3.anInt539 - samp3.anInt538) * 32.768000000000001D) / d);
            i1 = (int) ((samp3.anInt538 * 32.768000000000001D) / d);
        }
        int k1 = 0;
        int l1 = 0;
        int i2 = 0;
        if (samp5 != null) {
            samp5.resetValues();
            samp6.resetValues();
            k1 = (int) (((samp5.anInt539 - samp5.anInt538) * 32.768000000000001D) / d);
            l1 = (int) ((samp5.anInt538 * 32.768000000000001D) / d);
        }
        for (int j2 = 0; j2 < 5; j2++)
            if (anIntArray106[j2] != 0) {
                anIntArray118[j2] = 0;
                anIntArray119[j2] = (int) (anIntArray108[j2] * d);
                anIntArray120[j2] = (anIntArray106[j2] << 14) / 100;
                anIntArray121[j2] = (int) (((samp1.anInt539 - samp1.anInt538) * 32.768000000000001D * Math
                        .pow(1.0057929410678534D, anIntArray107[j2])) / d);
                anIntArray122[j2] = (int) ((samp1.anInt538 * 32.768000000000001D) / d);
            }

        for (int k2 = 0; k2 < sampleLength; k2++) {
            int l2 = samp1.evaluate(sampleLength);
            int j4 = samp2.evaluate(sampleLength);
            if (samp3 != null) {
                int j5 = samp3.evaluate(sampleLength);
                int j6 = samp4.evaluate(sampleLength);
                l2 += evaluate(j6, j1, samp3.form) >> 1;
                j1 += (j5 * l >> 16) + i1;
            }
            if (samp5 != null) {
                int k5 = samp5.evaluate(sampleLength);
                int k6 = samp6.evaluate(sampleLength);
                j4 = j4
                        * ((evaluate(k6, i2, samp5.form) >> 1) + 32768) >> 15;
                i2 += (k5 * k1 >> 16) + l1;
            }
            for (int l5 = 0; l5 < 5; l5++)
                if (anIntArray106[l5] != 0) {
                    int l6 = k2 + anIntArray119[l5];
                    if (l6 < sampleLength) {
                        samples[l6] += evaluate(
                                j4 * anIntArray120[l5] >> 15,
                                anIntArray118[l5], samp1.form);
                        anIntArray118[l5] += (l2 * anIntArray121[l5] >> 16)
                                + anIntArray122[l5];
                    }
                }

        }

        if (aClass29_104 != null) {
            aClass29_104.resetValues();
            aClass29_105.resetValues();
            int i3 = 0;
            boolean flag1 = true;
            for (int i7 = 0; i7 < sampleLength; i7++) {
                int k7 = aClass29_104.evaluate(sampleLength);
                int i8 = aClass29_105.evaluate(sampleLength);
                int k4;
                if (flag1)
                    k4 = aClass29_104.anInt538
                            + ((aClass29_104.anInt539 - aClass29_104.anInt538)
                            * k7 >> 8);
                else
                    k4 = aClass29_104.anInt538
                            + ((aClass29_104.anInt539 - aClass29_104.anInt538)
                            * i8 >> 8);
                if ((i3 += 256) >= k4) {
                    i3 = 0;
                    flag1 = !flag1;
                }
                if (flag1)
                    samples[i7] = 0;
            }

        }
        if (anInt109 > 0 && gain > 0) {
            int j3 = (int) (anInt109 * d);
            for (int l4 = j3; l4 < sampleLength; l4++)
                samples[l4] += (samples[l4 - j3] * gain) / 100;

        }
        if (aClass39_111.anIntArray665[0] > 0
                || aClass39_111.anIntArray665[1] > 0) {
            aClass29_112.resetValues();
            int k3 = aClass29_112.evaluate(sampleLength + 1);
            int i5 = aClass39_111.method544(0, k3 / 65536F);
            int i6 = aClass39_111.method544(1, k3 / 65536F);
            if (sampleLength >= i5 + i6) {
                int j7 = 0;
                int l7 = i6;
                if (l7 > sampleLength - i5)
                    l7 = sampleLength - i5;
                for (; j7 < l7; j7++) {
                    int j8 = (int) ((long) samples[j7 + i5]
                            * (long) Filter.anInt672 >> 16);
                    for (int k8 = 0; k8 < i5; k8++)
                        j8 += (int) ((long) samples[(j7 + i5) - 1 - k8]
                                * (long) Filter.coef_fixedpt[0][k8] >> 16);

                    for (int j9 = 0; j9 < j7; j9++)
                        j8 -= (int) ((long) samples[j7 - 1 - j9]
                                * (long) Filter.coef_fixedpt[1][j9] >> 16);

                    samples[j7] = j8;
                    k3 = aClass29_112.evaluate(sampleLength + 1);
                }

                char c = '\200';
                l7 = c;
                do {
                    if (l7 > sampleLength - i5)
                        l7 = sampleLength - i5;
                    for (; j7 < l7; j7++) {
                        int l8 = (int) ((long) samples[j7 + i5]
                                * (long) Filter.anInt672 >> 16);
                        for (int k9 = 0; k9 < i5; k9++)
                            l8 += (int) ((long) samples[(j7 + i5) - 1
                                    - k9]
                                    * (long) Filter.coef_fixedpt[0][k9] >> 16);

                        for (int i10 = 0; i10 < i6; i10++)
                            l8 -= (int) ((long) samples[j7 - 1 - i10]
                                    * (long) Filter.coef_fixedpt[1][i10] >> 16);

                        samples[j7] = l8;
                        k3 = aClass29_112.evaluate(sampleLength + 1);
                    }

                    if (j7 >= sampleLength - i5)
                        break;
                    i5 = aClass39_111.method544(0, k3 / 65536F);
                    i6 = aClass39_111.method544(1, k3 / 65536F);
                    l7 += c;
                } while (true);
                for (; j7 < sampleLength; j7++) {
                    int i9 = 0;
                    for (int l9 = (j7 + i5) - sampleLength; l9 < i5; l9++)
                        i9 += (int) ((long) samples[(j7 + i5) - 1 - l9]
                                * (long) Filter.coef_fixedpt[0][l9] >> 16);

                    for (int j10 = 0; j10 < i6; j10++)
                        i9 -= (int) ((long) samples[j7 - 1 - j10]
                                * (long) Filter.coef_fixedpt[1][j10] >> 16);

                    samples[j7] = i9;
                    aClass29_112.evaluate(sampleLength + 1);
                }

            }
        }
        for (int i4 = 0; i4 < sampleLength; i4++) {
            if (samples[i4] < -32768)
                samples[i4] = -32768;
            if (samples[i4] > 32767)
                samples[i4] = 32767;
        }

        return samples;
    }

    private int evaluate(int amplitude, int phase, int form) {
        if (form == 1)
            if ((phase & 0x7fff) < 16384)
                return amplitude;
            else
                return -amplitude;
        if (form == 2)
            return SINE[phase & 0x7fff] * amplitude >> 14;
        if (form == 3)
            return ((phase & 0x7fff) * amplitude >> 14) - amplitude;
        if (form == 4)
            return NOISE[phase / 2607 & 0x7fff] * amplitude;
        else
            return 0;
    }

    public void decode(Stream stream) {
        samp1 = new Envelope();
        samp1.decode(stream);
        samp2 = new Envelope();
        samp2.decode(stream);
        int i = stream.readUnsignedByte();
        if (i != 0) {
            stream.currentOffset--;
            samp3 = new Envelope();
            samp3.decode(stream);
            samp4 = new Envelope();
            samp4.decode(stream);
        }
        i = stream.readUnsignedByte();
        if (i != 0) {
            stream.currentOffset--;
            samp5 = new Envelope();
            samp5.decode(stream);
            samp6 = new Envelope();
            samp6.decode(stream);
        }
        i = stream.readUnsignedByte();
        if (i != 0) {
            stream.currentOffset--;
            aClass29_104 = new Envelope();
            aClass29_104.decode(stream);
            aClass29_105 = new Envelope();
            aClass29_105.decode(stream);
        }
        for (int j = 0; j < 10; j++) {
            int k = stream.method422();
            if (k == 0)
                break;
            anIntArray106[j] = k;
            anIntArray107[j] = stream.method421();
            anIntArray108[j] = stream.method422();
        }

        anInt109 = stream.method422();
        gain = stream.method422();
        offset = stream.readUnsignedWord();
        remaining = stream.readUnsignedWord();
        aClass39_111 = new Filter();
        aClass29_112 = new Envelope();
        aClass39_111.method545(stream, aClass29_112);
    }

    public Synthesizer() {
        anIntArray106 = new int[5];
        anIntArray107 = new int[5];
        anIntArray108 = new int[5];
        gain = 100;
        offset = 500;
    }

    private Envelope samp1;
    private Envelope samp2;
    private Envelope samp3;
    private Envelope samp4;
    private Envelope samp5;
    private Envelope samp6;
    private Envelope aClass29_104;
    private Envelope aClass29_105;
    private final int[] anIntArray106;
    private final int[] anIntArray107;
    private final int[] anIntArray108;
    private int anInt109;
    private int gain;
    private Filter aClass39_111;
    private Envelope aClass29_112;
    int offset;
    int remaining;
    private static int[] samples;
    private static int[] NOISE;
    private static int[] SINE;
    private static final int[] anIntArray118 = new int[5];
    private static final int[] anIntArray119 = new int[5];
    private static final int[] anIntArray120 = new int[5];
    private static final int[] anIntArray121 = new int[5];
    private static final int[] anIntArray122 = new int[5];

}
