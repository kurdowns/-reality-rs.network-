package net.crandor;

public class TextureTransformed extends Node {

	private static int[] animatedPixels;
	boolean needsAnimating = false;
	boolean animates;
	int[] originalPixels;
	int textureId;

	public void resetPixels() {
		originalPixels = null;
	}

	public boolean transform(int id) {
		Texture texture = Texture.get(id);
		if (texture == null) {
			return false;
		}
		int pixelsCount = 16384;
		originalPixels = new int[pixelsCount * 4];
		if (texture.width == 64) {
			for (int y = 0; y < 128; y++) {
				for (int x = 0; x < 128; x++) {
					originalPixels[x + (y << 7)] = texture.getPixel((x >> 1) + ((y >> 1) << 6));
				}
			}
		} else {
			for (int texelPtr = 0; texelPtr < pixelsCount; texelPtr++) {
				originalPixels[texelPtr] = texture.getPixel(texelPtr);
			}
		}

		TextureDef def = id >= 0 && id < TextureDef.textures.length ? TextureDef.textures[id] : null;
		int blendType = def == null ? 0 : def.anInt1226;
		if (blendType != 1 && blendType != 2)
			blendType = 0;

		for (int l1 = 0; l1 < pixelsCount; l1++) {
			int texel = originalPixels[l1];
			int alpha;
			if (blendType == 2) {
				alpha = texel >>> 24;
			} else if (blendType == 1) {
				alpha = (texel != 0 ? 0xff : 0);
			} else {
				alpha = texel >>> 24;
				if (def != null && !def.aBoolean1223)
					alpha /= 5;// 5

			}

			texel &= 0xffffff;
			texel = Rasterizer.adjustBrightness(texel, Rasterizer.brightness);
			texel = Rasterizer.adjustBrightnessLinear(texel, 200);
			texel &= 0xf8f8ff;
			originalPixels[l1] = texel | (alpha << 24);
			originalPixels[16384 + l1] = Rasterizer.adjustBrightnessLinear(texel, 256 - 8) | (alpha << 24);
			originalPixels[32768 + l1] = Rasterizer.adjustBrightnessLinear(texel, 256 - 16) | (alpha << 24);
			originalPixels[49152 + l1] = Rasterizer.adjustBrightnessLinear(texel, 256 - 32) | (alpha << 24);
		}

		return true;
	}

	public void animateTexture(int cycle) {
		if (originalPixels != null) {
			if (animatedPixels == null || animatedPixels.length < originalPixels.length)
				animatedPixels = new int[originalPixels.length];
			int size;
			if (originalPixels.length == 16384)
				size = 64;
			else
				size = 128;
			int pixelLen = originalPixels.length / 4;
			int direction = size * cycle * -2;
			int lastPixelPos = pixelLen - 1;
			for (int pixelId = 0; pixelId < pixelLen; pixelId++) {
				int nextPixel = pixelId + direction & lastPixelPos;
				animatedPixels[pixelId] = originalPixels[nextPixel];
				animatedPixels[pixelId + pixelLen] = originalPixels[nextPixel + pixelLen];
				animatedPixels[pixelId + pixelLen + pixelLen] = (originalPixels[nextPixel + pixelLen + pixelLen]);
				animatedPixels[pixelId + pixelLen + pixelLen + pixelLen] = (originalPixels[nextPixel + pixelLen + pixelLen + pixelLen]);
			}
			int[] is = originalPixels;
			originalPixels = animatedPixels;
			animatedPixels = is;
		}
	}

}
