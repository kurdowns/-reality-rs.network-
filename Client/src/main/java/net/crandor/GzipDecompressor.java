package net.crandor;

import java.util.zip.Inflater;

public class GzipDecompressor {

	private Inflater inflater = new Inflater(true);

	final int decompress(byte[] input, byte[] output) {
		int offset = calcOffset(input);

		int uncompressedBytes = -1;
		try {
			inflater.setInput(input, offset, input.length - offset - 8);//Trim gzip footer 8 bytes
			uncompressedBytes = inflater.inflate(output);
		} catch (final Exception exception) {
			inflater.reset();
			throw new RuntimeException("Invalid GZIP compressed data!");
		}
		inflater.reset();

		return uncompressedBytes;
	}

	private int calcOffset(byte[] input) {
		if (input[0] != 31 || input[1] != -117)
			throw new RuntimeException("invalid gzip header");

		int flags = input[3];

		int offset = 10;

		if ((flags & (1 << 1)) != 0) {
			offset += 2;
		}

		if ((flags & (1 << 2)) != 0) {
			offset += 2;
		}

		if ((flags & (1 << 3)) != 0) {
			for (; input[offset++] != '\0';) {

			}
		}

		if ((flags & (1 << 4)) != 0) {
			for (; input[offset++] != '\0';) {

			}
		}

		if ((flags & (1 << 5)) != 0) {
			offset += 12;
		}

		return offset;
	}

}
