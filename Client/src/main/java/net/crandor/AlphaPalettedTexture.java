package net.crandor;

final class AlphaPalettedTexture extends PalettedTexture {

	private final byte[] alpha;

	AlphaPalettedTexture(int width, int height, Stream buffer) {
		super(width, height, buffer, true);

		int count = width * height;
		byte[] alpha = this.alpha = new byte[count];
		for (int i = 0; i < count; i++) {
			alpha[i] = buffer.readSignedByte();
			if (alpha[i] == 0) {
				indices[i] = 0;
				opaque = false;
			} else if (alpha[i] != -1) {
				opaque = false;
				hasAlpha = true;
			}
		}
	}

	@Override
	public int getPixel(int i) {
		return ((alpha[i] & 0xff) << 24) | palette[indices[i] & 0xff];
	}

}
