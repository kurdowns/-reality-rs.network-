package net.crandor;

public final class SkinList {

    public final int[] opcode;
    public final int[][] skinList;
    public final int length;

	public SkinList(Stream buffer) {
		length = buffer.readUnsignedWord();
		opcode = new int[length];
		skinList = new int[length][];
		for (int j = 0; j < length; j++) {
			opcode[j] = buffer.readUnsignedWord();
		}
		for (int j = 0; j < length; j++) {
			skinList[j] = new int[buffer.readUnsignedWord()];
		}
		for (int j = 0; j < length; j++) {
			for (int l = 0; l < skinList[j].length; l++) {
				skinList[j][l] = buffer.readUnsignedWord();
			}
		}
	}
}