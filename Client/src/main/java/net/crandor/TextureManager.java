package net.crandor;

public class TextureManager {

	private static final int DISPOSE_TIMER = 20;
	private static final int[] Animated_Textures = { 17, 34, 40 };

	private int disposeTimer = DISPOSE_TIMER;
	private NodeList textureCache = new NodeList();
	private TextureTransformed[] textures;

	final void resetTextures() {
		for (int id = 0; id < textures.length; id++) {
			if (textures[id] != null)
				textures[id].resetPixels();
		}
		textureCache = new NodeList();
		disposeTimer = DISPOSE_TIMER;
	}

	void animateTexture(int cycle) {
		for (int id = 0; id < textures.length; id++) {
			TextureTransformed texture = textures[id];
			if (texture != null && texture.needsAnimating && texture.animates) {
				texture.animateTexture(cycle);
				texture.needsAnimating = false;
			}
		}
	}

	public void initTextures(int count) {
		textures = new TextureTransformed[count];
		for (int i = 0; i < count; i++) {
			textures[i] = new TextureTransformed();
			for (int j = 0; j < Animated_Textures.length; j++) {
				if (i == Animated_Textures[j]) {
					textures[i].animates = true;
				}
			}
		}
	}

	public int[] getTexturePixels(int textureId) {
		switch (textureId) {
		case 669:
			textureId = 24;
			break;
		}
		TextureTransformed texture = textures[textureId];
		if (texture == null) {
			return null;
		}
		texture.textureId = textureId;
		if (texture.originalPixels != null) {
			textureCache.insertHead(texture);
			texture.needsAnimating = true;
			return texture.originalPixels;
		}
		boolean loaded = texture.transform(textureId);
		if (loaded) {
			if (disposeTimer == 0) {
				TextureTransformed last = (TextureTransformed) textureCache.popHead();
				last.resetPixels();
			} else {
				disposeTimer--;
			}
			textureCache.insertHead(texture);
			texture.needsAnimating = true;
			return texture.originalPixels;
		}
		return null;
	}

}
