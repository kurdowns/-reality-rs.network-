package com.zionscape.server.util;

/**
 * Stream Class
 *
 * @author Lmctruck30
 * @version 1.2
 */
public class Stream {

	private static final int frameStackSize = 10;
	private int[] frameStack = new int[frameStackSize];
	public static int[] bitMaskOut = new int[32];

	static {
		for (int i = 0; i < 32; i++) {
			bitMaskOut[i] = (1 << i) - 1;
		}
	}

	public byte[] buffer = null;
	public int currentOffset = 0;
	public int bitPosition = 0;
	public ISAACRandomGen packetEncryption = null;
	private int frameStackPtr = -1;

	public Stream() {
	}

	public Stream(byte[] abyte0) {
		buffer = abyte0;
		currentOffset = 0;
	}

	public int method422() {
		int i = buffer[currentOffset] & 0xff;
		if (i < 128)
			return readUnsignedByte();
		else
			return readUnsignedWord() - 32768;
	}

	public byte readSignedByteA() {
		return (byte) (buffer[currentOffset++] - 128);
	}

	public byte readSignedByteC() {
		return (byte) -buffer[currentOffset++];
	}

	public byte readSignedByteS() {
		return (byte) (128 - buffer[currentOffset++]);
	}

	public int readUnsignedByteA() {
		return buffer[currentOffset++] - 128 & 0xff;
	}

	public int readUnsignedByteC() {
		return -buffer[currentOffset++] & 0xff;
	}

	public int readUnsignedByteS() {
		return 128 - buffer[currentOffset++] & 0xff;
	}

	public void writeByteA(int i) {
		this.ensureCapacity(1);
		buffer[currentOffset++] = (byte) (i + 128);
	}

	public void writeByteS(int i) {
		this.ensureCapacity(1);
		buffer[currentOffset++] = (byte) (128 - i);
	}

	public void writeByteC(int i) {
		this.ensureCapacity(1);
		buffer[currentOffset++] = (byte) -i;
	}

	public int readSignedWordBigEndian() {
		currentOffset += 2;
		int i = ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] & 0xff);
		if (i > 32767) {
			i -= 0x10000;
		}
		return i;
	}

	public int readSignedWordA() {
		currentOffset += 2;
		int i = ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] - 128 & 0xff);
		if (i > 32767) {
			i -= 0x10000;
		}
		return i;
	}

	public int readSignedWordBigEndianA() {
		currentOffset += 2;
		int i = ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] - 128 & 0xff);
		if (i > 32767) {
			i -= 0x10000;
		}
		return i;
	}

	public int readUnsignedWordBigEndian() {
		currentOffset += 2;
		return ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] & 0xff);
	}

	public int readUnsignedWordA() {
		currentOffset += 2;
		return ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] - 128 & 0xff);
	}

	public int readUnsignedWordBigEndianA() {
		currentOffset += 2;
		return ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] - 128 & 0xff);
	}

	public void writeWordBigEndianA(int i) {
		this.ensureCapacity(2);
		buffer[currentOffset++] = (byte) (i + 128);
		buffer[currentOffset++] = (byte) (i >> 8);
	}

	public void writeWordA(int i) {
		this.ensureCapacity(2);
		buffer[currentOffset++] = (byte) (i >> 8);
		buffer[currentOffset++] = (byte) (i + 128);
	}

	public void writeWordBigEndian_dup(int i) {
		this.ensureCapacity(2);
		buffer[currentOffset++] = (byte) i;
		buffer[currentOffset++] = (byte) (i >> 8);
	}

	public int readDWord_v1() {
		currentOffset += 4;
		return ((buffer[currentOffset - 2] & 0xff) << 24) + ((buffer[currentOffset - 1] & 0xff) << 16)
				+ ((buffer[currentOffset - 4] & 0xff) << 8) + (buffer[currentOffset - 3] & 0xff);
	}

	public int readDWord_v2() {
		currentOffset += 4;
		return ((buffer[currentOffset - 3] & 0xff) << 24) + ((buffer[currentOffset - 4] & 0xff) << 16)
				+ ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] & 0xff);
	}

	public void writeDWord_v1(int i) {
		this.ensureCapacity(4);
		buffer[currentOffset++] = (byte) (i >> 8);
		buffer[currentOffset++] = (byte) i;
		buffer[currentOffset++] = (byte) (i >> 24);
		buffer[currentOffset++] = (byte) (i >> 16);
	}

	public void writeDWord_v2(int i) {
		this.ensureCapacity(4);
		buffer[currentOffset++] = (byte) (i >> 16);
		buffer[currentOffset++] = (byte) (i >> 24);
		buffer[currentOffset++] = (byte) i;
		buffer[currentOffset++] = (byte) (i >> 8);
	}

	public void readBytes_reverse(byte[] abyte0, int i, int j) {
		for (int k = (j + i) - 1; k >= j; k--) {
			abyte0[k] = buffer[currentOffset++];
		}
	}

	public void writeBytes_reverse(byte[] abyte0, int i, int j) {
		this.ensureCapacity(i);
		for (int k = (j + i) - 1; k >= j; k--) {
			buffer[currentOffset++] = abyte0[k];
		}
	}

	public void readBytes_reverseA(byte[] abyte0, int i, int j) {
		this.ensureCapacity(i);
		for (int k = (j + i) - 1; k >= j; k--) {
			abyte0[k] = (byte) (buffer[currentOffset++] - 128);
		}
	}

	public void writeBytes_reverseA(byte[] abyte0, int i, int j) {
		this.ensureCapacity(i);
		for (int k = (j + i) - 1; k >= j; k--) {
			buffer[currentOffset++] = (byte) (abyte0[k] + 128);
		}
	}

	public void createFrame(int id) {
		this.ensureCapacity(1);
		buffer[currentOffset++] = (byte) (id);
	}

	public void createFrameVarSize(int id) {
		this.ensureCapacity(3);
		this.buffer[this.currentOffset++] = (byte) (id);
		this.buffer[this.currentOffset++] = 0;
		if (this.frameStackPtr >= 9) {
			throw new RuntimeException("Stack overflow");
		} else {
			this.frameStack[this.frameStackPtr += 1] = this.currentOffset;
		}
	}

	public void createFrameVarSizeWord(int id) {
		this.ensureCapacity(2);
		this.buffer[this.currentOffset++] = (byte) (id);
		this.writeWord(0);
		if (this.frameStackPtr >= 9) {
			throw new RuntimeException("Stack overflow");
		} else {
			this.frameStack[this.frameStackPtr += 1] = this.currentOffset;
		}
	}

	public void endFrameVarSize() {
		if (frameStackPtr < 0) {
			throw new RuntimeException("Stack empty");
		} else {
			this.writeFrameSize(currentOffset - frameStack[frameStackPtr--]);
		}
	}

	public void endFrameVarSizeWord() {
		if (frameStackPtr < 0) {
			throw new RuntimeException("Stack empty");
		} else {
			this.writeFrameSizeWord(currentOffset - frameStack[frameStackPtr--]);
		}
	}

	public void writeByte(int i) {
		this.ensureCapacity(1);
		buffer[currentOffset++] = (byte) i;
	}

	public void writeWord(int i) {
		this.ensureCapacity(2);
		buffer[currentOffset++] = (byte) (i >> 8);
		buffer[currentOffset++] = (byte) i;
	}

	public void writeWordBigEndian(int i) {
		this.ensureCapacity(2);
		buffer[currentOffset++] = (byte) i;
		buffer[currentOffset++] = (byte) (i >> 8);
	}

	public int read3Bytes() {
		currentOffset += 3;
		return ((buffer[currentOffset - 3] & 0xff) << 16) + ((buffer[currentOffset - 2] & 0xff) << 8)
				+ (buffer[currentOffset - 1] & 0xff);
	}

	public byte[] readBytes() {
		int i = currentOffset;
		while (buffer[currentOffset++] != 10)
			;
		byte abyte0[] = new byte[currentOffset - i - 1];
		System.arraycopy(buffer, i, abyte0, i - i, currentOffset - 1 - i);
		return abyte0;
	}

	public void write3Byte(int i) {
		this.ensureCapacity(3);
		buffer[currentOffset++] = (byte) (i >> 16);
		buffer[currentOffset++] = (byte) (i >> 8);
		buffer[currentOffset++] = (byte) i;
	}

	public void writeDWord(int i) {
		this.ensureCapacity(4);
		buffer[currentOffset++] = (byte) (i >> 24);
		buffer[currentOffset++] = (byte) (i >> 16);
		buffer[currentOffset++] = (byte) (i >> 8);
		buffer[currentOffset++] = (byte) i;
	}

	public void writeDWordBigEndian(int i) {
		this.ensureCapacity(4);
		buffer[currentOffset++] = (byte) i;
		buffer[currentOffset++] = (byte) (i >> 8);
		buffer[currentOffset++] = (byte) (i >> 16);
		buffer[currentOffset++] = (byte) (i >> 24);
	}

	public void writeQWord(long l) {
		this.ensureCapacity(8);
		buffer[currentOffset++] = (byte) (int) (l >> 56);
		buffer[currentOffset++] = (byte) (int) (l >> 48);
		buffer[currentOffset++] = (byte) (int) (l >> 40);
		buffer[currentOffset++] = (byte) (int) (l >> 32);
		buffer[currentOffset++] = (byte) (int) (l >> 24);
		buffer[currentOffset++] = (byte) (int) (l >> 16);
		buffer[currentOffset++] = (byte) (int) (l >> 8);
		buffer[currentOffset++] = (byte) (int) l;
	}

	public void writeString(String s) {
		this.ensureCapacity(s.length());
		System.arraycopy(s.getBytes(), 0, buffer, currentOffset, s.length());
		currentOffset += s.length();
		buffer[currentOffset++] = 10;
	}

	public void writeBytes(byte[] abyte0, int i, int j) {
		this.ensureCapacity(i);
		for (int k = j; k < j + i; k++) {
			buffer[currentOffset++] = abyte0[k];
		}
	}

	public void writeFrameSize(int i) {
		buffer[currentOffset - i - 1] = (byte) i;
	}

	public void writeFrameSizeWord(int i) {
		buffer[currentOffset - i - 2] = (byte) (i >> 8);
		buffer[currentOffset - i - 1] = (byte) i;
	}

	public int readUnsignedByte() {
		return buffer[currentOffset++] & 0xff;
	}

	public byte readSignedByte() {
		return buffer[currentOffset++];
	}

	public int readUnsignedWord() {
		currentOffset += 2;
		return ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] & 0xff);
	}

	public int readSignedWord() {
		currentOffset += 2;
		int i = ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] & 0xff);
		if (i > 32767) {
			i -= 0x10000;
		}
		return i;
	}

	public int readDWord() {
		currentOffset += 4;
		return ((buffer[currentOffset - 4] & 0xff) << 24) + ((buffer[currentOffset - 3] & 0xff) << 16)
				+ ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] & 0xff);
	}

	public long readQWord() {
		long l = (long) this.readDWord() & 0xffffffffL;
		long l1 = (long) this.readDWord() & 0xffffffffL;
		return (l << 32) + l1;
	}

	public String readString() {
		int i = currentOffset;
		while (buffer[currentOffset++] != 10) {
			;
		}
		return new String(buffer, i, currentOffset - i - 1);
	}

	public void readBytes(byte[] abyte0, int i, int j) {
		for (int k = j; k < j + i; k++) {
			abyte0[k] = buffer[currentOffset++];
		}
	}

	public void initBitAccess() {
		bitPosition = currentOffset * 8;
	}

	public void writeBits(int numBits, int value) {
		this.ensureCapacity((int) Math.ceil(numBits * 8) * 4);
		int bytePos = bitPosition >> 3;
		int bitOffset = 8 - (bitPosition & 7);
		bitPosition += numBits;
		for (; numBits > bitOffset; bitOffset = 8) {
			buffer[bytePos] &= ~bitMaskOut[bitOffset];
			buffer[bytePos++] |= (value >> (numBits - bitOffset)) & bitMaskOut[bitOffset];
			numBits -= bitOffset;
		}
		if (numBits == bitOffset) {
			buffer[bytePos] &= ~bitMaskOut[bitOffset];
			buffer[bytePos] |= value & bitMaskOut[bitOffset];
		} else {
			buffer[bytePos] &= ~(bitMaskOut[numBits] << (bitOffset - numBits));
			buffer[bytePos] |= (value & bitMaskOut[numBits]) << (bitOffset - numBits);
		}
	}

	public void finishBitAccess() {
		currentOffset = (bitPosition + 7) / 8;
	}

	public void ensureCapacity(int len) {
		if ((currentOffset + len + 1) >= buffer.length) {
			byte[] oldBuffer = buffer;
			int newLength = buffer.length * 2;
			buffer = new byte[newLength];
			System.arraycopy(oldBuffer, 0, buffer, 0, oldBuffer.length);
			this.ensureCapacity(len);
		}
	}

}
