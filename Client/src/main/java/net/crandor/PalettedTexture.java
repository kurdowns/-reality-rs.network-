package net.crandor;

public class PalettedTexture extends Texture {

	final int[] palette;
	byte[] indices;

	public PalettedTexture(int width, int height, Stream buffer, boolean alpha) {
		super(width, height);

		int paletteSize = buffer.readUnsignedByte();
		int[] palette = this.palette = new int[1 + paletteSize];
		for (int i = 0; i < paletteSize; i++) {
			int pixel = buffer.read3Bytes();
			if (!alpha)
				pixel |= 0xff000000;
			palette[i + 1] = pixel;
		}

		int count = width * height;
		byte[] indices = this.indices = new byte[count];
		for (int i = 0; i < count; i++) {
			indices[i] = buffer.readSignedByte();
			if (!alpha && indices[i] == 0)
				opaque = false;
		}

	}

	@Override
	public int getPixel(int i) {
		return palette[indices[i] & 0xff];
	}

}
