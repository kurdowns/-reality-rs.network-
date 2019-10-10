package net.crandor;

final class Background extends DrawingArea {

	Background(StreamLoader streamLoader, String s, int i) {
		Stream stream = new Stream(streamLoader.getDataForName(s + ".dat"));
		Stream stream_1 = new Stream(streamLoader.getDataForName("index.dat"));
		stream_1.currentOffset = stream.readUnsignedWord();
		stream_1.readUnsignedWord();//trimx
		stream_1.readUnsignedWord();//trimy
		int j = stream_1.readUnsignedByte();
		anIntArray1451 = new int[j];
		for (int k = 0; k < j - 1; k++)
			anIntArray1451[k + 1] = stream_1.read3Bytes();

		for (int l = 0; l < i; l++) {
			stream_1.currentOffset += 2;
			stream.currentOffset += stream_1.readUnsignedWord()
					* stream_1.readUnsignedWord();
			stream_1.currentOffset++;
		}

		anInt1454 = stream_1.readUnsignedByte();
		anInt1455 = stream_1.readUnsignedByte();
		imgWidth = stream_1.readUnsignedWord();
		anInt1453 = stream_1.readUnsignedWord();
		int i1 = stream_1.readUnsignedByte();
		int j1 = imgWidth * anInt1453;
		imgPixels = new byte[j1];
		if (i1 == 0) {
			for (int k1 = 0; k1 < j1; k1++)
				imgPixels[k1] = stream.readSignedByte();

			return;
		}
		if (i1 == 1) {
			for (int l1 = 0; l1 < imgWidth; l1++) {
				for (int i2 = 0; i2 < anInt1453; i2++)
					imgPixels[l1 + i2 * imgWidth] = stream
							.readSignedByte();

			}
		}
	}

	void drawBackground(int i, int k) {
		i += anInt1454;
		k += anInt1455;
		int l = i + k * width;
		int i1 = 0;
		int j1 = anInt1453;
		int k1 = imgWidth;
		int l1 = width - k1;
		int i2 = 0;
		if (k < topY) {
			int j2 = topY - k;
			j1 -= j2;
			k = topY;
			i1 += j2 * k1;
			l += j2 * width;
		}
		if (k + j1 > bottomY)
			j1 -= (k + j1) - bottomY;
		if (i < topX) {
			int k2 = topX - i;
			k1 -= k2;
			i = topX;
			i1 += k2;
			l += k2;
			i2 += k2;
			l1 += k2;
		}
		if (i + k1 > bottomX) {
			int l2 = (i + k1) - bottomX;
			k1 -= l2;
			i2 += l2;
			l1 += l2;
		}
		if (!(k1 <= 0 || j1 <= 0)) {
			method362(j1, pixels, imgPixels, l1, l, k1, i1,
					anIntArray1451, i2);
		}
	}

	private void method362(int i, int ai[], byte abyte0[], int j, int k, int l,
			int i1, int ai1[], int j1) {
		int k1 = -(l >> 2);
		l = -(l & 3);
		for (int l1 = -i; l1 < 0; l1++) {
			for (int i2 = k1; i2 < 0; i2++) {
				byte byte1 = abyte0[i1++];
				if (byte1 != 0)
					ai[k++] = ai1[byte1 & 0xff];
				else
					k++;
				byte1 = abyte0[i1++];
				if (byte1 != 0)
					ai[k++] = ai1[byte1 & 0xff];
				else
					k++;
				byte1 = abyte0[i1++];
				if (byte1 != 0)
					ai[k++] = ai1[byte1 & 0xff];
				else
					k++;
				byte1 = abyte0[i1++];
				if (byte1 != 0)
					ai[k++] = ai1[byte1 & 0xff];
				else
					k++;
			}

			for (int j2 = l; j2 < 0; j2++) {
				byte byte2 = abyte0[i1++];
				if (byte2 != 0)
					ai[k++] = ai1[byte2 & 0xff];
				else
					k++;
			}

			k += j;
			i1 += j1;
		}

	}

	byte imgPixels[];
	private final int[] anIntArray1451;
	int imgWidth;
	int anInt1453;
	int anInt1454;
	int anInt1455;
}
