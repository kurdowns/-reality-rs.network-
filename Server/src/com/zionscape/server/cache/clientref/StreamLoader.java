package com.zionscape.server.cache.clientref;

import com.zionscape.server.util.Stream;

/**
 * **************************************************************************** Copyright 2014 Aeresan LTD
 *
 * @author Poesy700
 * @author Lost Valentino
 * @author Wolfs Darker
 *         <p>
 *         No redistribution allowed in any way shape or form other than a obfuscated client executable for usage by
 *         players. ****************************************************************************
 */
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

public class StreamLoader {

	private final byte[] outputData;
	private final int dataSize;
	private final int[] myNameIndexes;
	private final int[] myFileSizes;
	private final int[] myOnDiskFileSizes;
	private final int[] myFileOffsets;
	private final boolean isCompressed;

	public StreamLoader(byte data[]) {
		Stream stream = new Stream(data);
		int i = stream.read3Bytes();
		int j = stream.read3Bytes();
		if (j != i) {
			byte abyte1[] = new byte[i];
			BZ2InputStream.decompressBuffer(abyte1, i, data, j, 6);
			outputData = abyte1;
			stream = new Stream(outputData);
			isCompressed = true;
		} else {
			outputData = data;
			isCompressed = false;
		}
		dataSize = stream.readUnsignedWord();
		myNameIndexes = new int[dataSize];
		myFileSizes = new int[dataSize];
		myOnDiskFileSizes = new int[dataSize];
		myFileOffsets = new int[dataSize];
		int k = stream.currentOffset + dataSize * 10;
		for (int l = 0; l < dataSize; l++) {
			myNameIndexes[l] = stream.readDWord();
			myFileSizes[l] = stream.read3Bytes();
			myOnDiskFileSizes[l] = stream.read3Bytes();
			myFileOffsets[l] = k;
			k += myOnDiskFileSizes[l];
		}
	}

	public byte[] getDataForName(String s) {
		byte data[] = null; // was a parameter
		int i = 0;
		s = s.toUpperCase();
		for (int j = 0; j < s.length(); j++)
			i = (i * 61 + s.charAt(j)) - 32;

		for (int k = 0; k < dataSize; k++)
			if (myNameIndexes[k] == i) {
				if (data == null)
					data = new byte[myFileSizes[k]];
				if (!isCompressed) {
					BZ2InputStream.decompressBuffer(data, myFileSizes[k], outputData, myOnDiskFileSizes[k],
							myFileOffsets[k]);
				} else {
					System.arraycopy(outputData, myFileOffsets[k], data, 0, myFileSizes[k]);

				}
				return data;
			}

		return null;
	}
}
