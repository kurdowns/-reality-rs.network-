package net.crandor;

public class FogHandler {
	
	public static int fogColor = 0xc6bfa6;
	private static int fogStartDistance = 2000;
	private static int fogEndDistance = 3000;
	private static float fogLength;

	public static void init() {
		fogLength = fogEndDistance - fogStartDistance;
	}

	public static int applyFog(int pixel, float z) {
		if (!Settings.fog) {
			return pixel;
		}

		float relative = (((z * 256) - (fogStartDistance << 8)) / 250);
		float factor = relative / fogLength;
		factor = Math.min(1f, Math.max(factor, 0f));
		return mix(pixel, fogColor, factor);
	}

	public static int mix(int pixels, int color, float factor) {
		if (factor >= 1f) {
			return color;
		}
		if (factor <= 0f) {
			return pixels;
		}
		int aR = (pixels >> 16) & 0xFF;
		int aG = (pixels >> 8) & 0xFF;
		int aB = (pixels) & 0xFF;

		int bR = (color >> 16) & 0xFF;
		int bG = (color >> 8) & 0xFF;
		int bB = (color) & 0xFF;

		int dR = bR - aR;
		int dG = bG - aG;
		int dB = bB - aB;

		int nR = (int) (aR + (dR * factor));
		int nG = (int) (aG + (dG * factor));
		int nB = (int) (aB + (dB * factor));
		return (nR << 16) + (nG << 8) + nB;
	}
}
