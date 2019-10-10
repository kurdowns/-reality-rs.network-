package net.crandor;

public abstract class Texture extends NodeSub {

	private static MRUNodes recentUse = new MRUNodes(16);
	private static int textureCount;

	boolean opaque = true;
	boolean hasAlpha;
	final int width;
	final int height;

	Texture(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public abstract int getPixel(int i);

	static Texture get(int id) {
		if (id < 0 || id >= textureCount) {
			return null;
		}

		Texture texture = (Texture) recentUse.get(id);
		if (texture != null) {
			return texture;
		}

		Client.instance.onDemandFetcher.loadToCache(4, id);
		return null;
	}

	static void initialize(int count) {
		textureCount = count;
		Rasterizer.textureManager.initTextures(count);
	}

	static void load(int id, byte[] data) {
		if (data != null && data.length >= 5) {
			Stream buffer = new Stream(data);
			int type = buffer.readUnsignedByte();
			int width = buffer.readUnsignedWord();
			int height = buffer.readUnsignedWord();
			Texture texture = null;
			if (type == 0) {
				texture = new PalettedTexture(width, height, buffer, false);
			} else if (type == 1) {
				texture = new RGBTexture(width, height, buffer);
			} else if (type == 2) {
				texture = new AlphaPalettedTexture(width, height, buffer);
			} else if (type == 3) {
				texture = new ARGBTexture(width, height, buffer);
			}
			recentUse.put(texture, id);
		}
	}

	@Override
	public String toString() {
		return width + " X " + height + "	" + (opaque ? "+opaque" : "-opaque") + "	" + (hasAlpha ? "+alpha" : "-alpha");
	}

}
