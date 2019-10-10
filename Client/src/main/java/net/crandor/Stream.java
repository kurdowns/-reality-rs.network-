package net.crandor;

import java.math.BigInteger;

final class Stream extends NodeSub {

    private static final int[] anIntArray1409 = {0, 1, 3, 7, 15, 31, 63, 127,
            255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 0x1ffff,
            0x3ffff, 0x7ffff, 0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff,
            0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 0x3fffffff,
            0x7fffffff, -1};
    public static long[] CRC_64 = new long[256];
    byte buffer[];
    int currentOffset;
    int bitPosition;
    ISAACRandomGen encryption;

    private Stream() {
    }

    Stream(byte abyte0[]) {
        buffer = abyte0;
        currentOffset = 0;
    }

    static Stream create() {
        Stream stream_1 = new Stream();
        stream_1.currentOffset = 0;
        stream_1.buffer = new byte[10000];
        return stream_1;
    }

    void createFrame(int i) {
        buffer[currentOffset++] = (byte) (i + encryption.getNextKey());
    }

    void putByte(int i) {
        buffer[currentOffset++] = (byte) i;
    }

    void putShort(int i) {
        buffer[currentOffset++] = (byte) (i >> 8);
        buffer[currentOffset++] = (byte) i;
    }

    void putLEShort(int i) {
        buffer[currentOffset++] = (byte) i;
        buffer[currentOffset++] = (byte) (i >> 8);
    }

    void put24Int(int i) {
        buffer[currentOffset++] = (byte) (i >> 16);
        buffer[currentOffset++] = (byte) (i >> 8);
        buffer[currentOffset++] = (byte) i;
    }

    void putInt(int i) {
        buffer[currentOffset++] = (byte) (i >> 24);
        buffer[currentOffset++] = (byte) (i >> 16);
        buffer[currentOffset++] = (byte) (i >> 8);
        buffer[currentOffset++] = (byte) i;
    }

    void putLEInt(int j) {
        buffer[currentOffset++] = (byte) j;
        buffer[currentOffset++] = (byte) (j >> 8);
        buffer[currentOffset++] = (byte) (j >> 16);
        buffer[currentOffset++] = (byte) (j >> 24);
    }

	void putLong(long l) {
		buffer[currentOffset++] = (byte) (int) (l >> 56);
		buffer[currentOffset++] = (byte) (int) (l >> 48);
		buffer[currentOffset++] = (byte) (int) (l >> 40);
		buffer[currentOffset++] = (byte) (int) (l >> 32);
		buffer[currentOffset++] = (byte) (int) (l >> 24);
		buffer[currentOffset++] = (byte) (int) (l >> 16);
		buffer[currentOffset++] = (byte) (int) (l >> 8);
		buffer[currentOffset++] = (byte) (int) l;
	}

    void putString(String s) {
        System.arraycopy(s.getBytes(), 0, buffer, currentOffset, s.length());
        currentOffset += s.length();
        buffer[currentOffset++] = 10;
    }

    void putBytes(byte abyte0[], int i, int j) {
        for (int k = j; k < j + i; k++)
            buffer[currentOffset++] = abyte0[k];
    }

    void putSize(int i) {
        buffer[currentOffset - i - 1] = (byte) i;
    }

    int readUnsignedByte() {
        return buffer[currentOffset++] & 0xff;
    }

    byte readSignedByte() {
        return buffer[currentOffset++];
    }

    int readUnsignedWord() {
        currentOffset += 2;
        return ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] & 0xff);
    }

    int readSignedWord() {
        currentOffset += 2;
        int i = ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] & 0xff);
        if (i > 32767)
            i -= 0x10000;
        return i;
    }

    int read3Bytes() {
        currentOffset += 3;
        return ((buffer[currentOffset - 3] & 0xff) << 16)
                + ((buffer[currentOffset - 2] & 0xff) << 8)
                + (buffer[currentOffset - 1] & 0xff);
    }

    int readDWord() {
        currentOffset += 4;
        return ((buffer[currentOffset - 4] & 0xff) << 24)
                + ((buffer[currentOffset - 3] & 0xff) << 16)
                + ((buffer[currentOffset - 2] & 0xff) << 8)
                + (buffer[currentOffset - 1] & 0xff);
    }

    long readQWord() {
        long l = readDWord() & 0xffffffffL;
        long l1 = readDWord() & 0xffffffffL;
        return (l << 32) + l1;
    }

    String readString() {
        int i = currentOffset;
        while (buffer[currentOffset++] != 10)
            ;
        return new String(buffer, i, currentOffset - i - 1);
    }

    byte[] readBytes() {
        int i = currentOffset;
        while (buffer[currentOffset++] != 10)
            ;
        byte abyte0[] = new byte[currentOffset - i - 1];
        System.arraycopy(buffer, i, abyte0, i - i, currentOffset - 1 - i);
        return abyte0;
    }

    void readBytes(int i, int j, byte abyte0[]) {
        for (int l = j; l < j + i; l++)
            abyte0[l] = buffer[currentOffset++];
    }

    void initBitAccess() {
        bitPosition = currentOffset * 8;
    }

    int readBits(int i) {
        int k = bitPosition >> 3;
        int l = 8 - (bitPosition & 7);
        int i1 = 0;
        bitPosition += i;
        for (; i > l; l = 8) {
            i1 += (buffer[k++] & anIntArray1409[l]) << i - l;
            i -= l;
        }
        if (i == l)
            i1 += buffer[k] & anIntArray1409[l];
        else
            i1 += buffer[k] >> l - i & anIntArray1409[i];
        return i1;
    }

    void finishBitAccess() {
        currentOffset = (bitPosition + 7) / 8;
    }

    int method421() {
        int i = buffer[currentOffset] & 0xff;
        if (i < 128)
            return readUnsignedByte() - 64;
        else
            return readUnsignedWord() - 49152;
    }

    int readShort2() {
        currentOffset += 2;
        int i = ((buffer[currentOffset - 2] & 0xff) << 8)
                + (buffer[currentOffset - 1] & 0xff);
        if (i > 60000)
            i = -65535 + i;
        return i;

    }

    int method422() {
        int i = buffer[currentOffset] & 0xff;
        if (i < 128)
            return readUnsignedByte();
        else
            return readUnsignedWord() - 32768;
    }

    void doKeys() {
        int i = currentOffset;
        currentOffset = 0;
        byte abyte0[] = new byte[i];
        readBytes(i, 0, abyte0);
        BigInteger biginteger2 = new BigInteger(abyte0);
        BigInteger biginteger3 = biginteger2/* .modPow(biginteger, biginteger1) */;
        byte abyte1[] = biginteger3.toByteArray();
        currentOffset = 0;
        putByte(abyte1.length);
        putBytes(abyte1, abyte1.length, 0);
    }

    void method424(int i) {
        buffer[currentOffset++] = (byte) (-i);
    }

    void method425(int j) {
        buffer[currentOffset++] = (byte) (128 - j);
    }

    int method427() {
        return -buffer[currentOffset++] & 0xff;
    }

    int getUnsignedByteA() {
        return buffer[currentOffset++] - 128 & 0xff;
    }

    int getUnsignedByteS() {
        return 128 - buffer[currentOffset++] & 0xff;
    }

    byte method429() {
        return (byte) (-buffer[currentOffset++]);
    }

    byte method430() {
        return (byte) (128 - buffer[currentOffset++]);
    }

    void method431(int i) {
        buffer[currentOffset++] = (byte) i;
        buffer[currentOffset++] = (byte) (i >> 8);
    }

    void writeSignedShortA(int j) {
        buffer[currentOffset++] = (byte) (j >> 8);
        buffer[currentOffset++] = (byte) (j + 128);
    }

    void writeSignedShortBigEndianA(int j) {
        buffer[currentOffset++] = (byte) (j + 128);
        buffer[currentOffset++] = (byte) (j >> 8);
    }

    int method434() {
        currentOffset += 2;
        return ((buffer[currentOffset - 1] & 0xff) << 8)
                + (buffer[currentOffset - 2] & 0xff);
    }

    int readUnsignedWordA() {
        currentOffset += 2;
        return ((buffer[currentOffset - 2] & 0xff) << 8)  + (buffer[currentOffset - 1] - 128 & 0xff);
    }

    int method436() {
        currentOffset += 2;
        return ((buffer[currentOffset - 1] & 0xff) << 8)  + (buffer[currentOffset - 2] - 128 & 0xff);
    }

    int method437() {
        currentOffset += 2;
        int j = ((buffer[currentOffset - 1] & 0xff) << 8)
                + (buffer[currentOffset - 2] & 0xff);
        if (j > 32767)
            j -= 0x10000;
        return j;
    }

    int method438() {
        currentOffset += 2;
        int j = ((buffer[currentOffset - 1] & 0xff) << 8)
                + (buffer[currentOffset - 2] - 128 & 0xff);
        if (j > 32767)
            j -= 0x10000;
        return j;
    }

    int method439() {
        currentOffset += 4;
        return ((buffer[currentOffset - 2] & 0xff) << 24)
                + ((buffer[currentOffset - 1] & 0xff) << 16)
                + ((buffer[currentOffset - 4] & 0xff) << 8)
                + (buffer[currentOffset - 3] & 0xff);
    }

    int method440() {
        currentOffset += 4;
        return ((buffer[currentOffset - 3] & 0xff) << 24)
                + ((buffer[currentOffset - 4] & 0xff) << 16)
                + ((buffer[currentOffset - 1] & 0xff) << 8)
                + (buffer[currentOffset - 2] & 0xff);
    }

    void method441(int i, byte abyte0[], int j) {
        for (int k = (i + j) - 1; k >= i; k--)
            buffer[currentOffset++] = (byte) (abyte0[k] + 128);

    }

    void method442(int i, int j, byte abyte0[]) {
        for (int k = (j + i) - 1; k >= j; k--)
            abyte0[k] = buffer[currentOffset++];

    }

	static {
		for (int index = 0; index < 256; index++) {
			long crc = index;

			for (int bit = 0; bit < 8; bit++) {
				if ((crc & 0x1L) == 1L) {
					crc = crc >>> 1 ^ ~0x3693a86a2878f0bdL;
				} else {
					crc >>>= 1;
				}
			}

			CRC_64[index] = crc;
		}
	}

}
