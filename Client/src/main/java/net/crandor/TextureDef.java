package net.crandor;

final class TextureDef {

	static void unpackConfig(StreamLoader streamLoader) {
		Stream buffer = new Stream(streamLoader.getDataForName("textures.dat"));
		int count = buffer.readUnsignedWord();
		textures = new TextureDef[count];
		for (int i = 0; i < count; i++) {
			if (buffer.readUnsignedByte() == 1) {
				textures[i] = new TextureDef();
			}
		}

		for (int i = 0; i < count; i++)
			if (textures[i] != null)
				textures[i].aBoolean1223 = buffer.readUnsignedByte() == 1;

		for (int i = 0; i < count; i++)
			if (textures[i] != null) {
				buffer.readUnsignedByte();
			}

		for (int i = 0; i < count; i++)
			if (textures[i] != null) {
				buffer.readUnsignedByte();
			}

		for (int i = 0; i < count; i++)
			if (textures[i] != null) {
				buffer.readSignedByte();
			}

		for (int i = 0; i < count; i++)
			if (textures[i] != null) {
				buffer.readSignedByte();
			}

		for (int i = 0; i < count; i++)
			if (textures[i] != null) {
				buffer.readSignedByte();
			}

		for (int i = 0; i < count; i++)
			if (textures[i] != null) {
				buffer.readSignedByte();
			}

		for (int i = 0; i < count; i++)
			if (textures[i] != null) {
				buffer.readUnsignedWord();
			}

		for (int i = 0; i < count; i++)
			if (textures[i] != null) {
				buffer.readSignedByte();
			}

		for (int i = 0; i < count; i++)
			if (textures[i] != null) {
				buffer.readSignedByte();
			}

		for (int i = 0; i < count; i++)
			if (textures[i] != null) {
				buffer.readUnsignedByte();
			}
		for (int i = 0; i < count; i++)
			if (textures[i] != null) {
				buffer.readUnsignedByte();
			}

		for (int i = 0; i < count; i++)
			if (textures[i] != null) {
				buffer.readSignedByte();
			}

		for (int i = 0; i < count; i++)
			if (textures[i] != null) {
				buffer.readUnsignedByte();
			}

		for (int i = 0; i < count; i++)
			if (textures[i] != null) {
				buffer.readUnsignedByte();
			}

		for (int i = 0; i < count; i++)
			if (textures[i] != null) {
				buffer.readUnsignedByte();
			}

		for (int i = 0; i < count; i++)
			if (textures[i] != null) {
				buffer.readUnsignedByte();
			}

		for (int i = 0; i < count; i++)
			if (textures[i] != null) {
				buffer.readDWord();
			}

		for (int i = 0; i < count; i++)
			if (textures[i] != null) {
				textures[i].anInt1226 = buffer.readUnsignedByte();
			}
	}

	boolean aBoolean1223;
	int anInt1226;
	static TextureDef[] textures;
}
