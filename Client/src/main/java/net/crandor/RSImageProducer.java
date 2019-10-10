package net.crandor;

import java.awt.*;
import java.awt.image.*;

final class RSImageProducer {

	public RSImageProducer(int i, int j, Component c) {
		this(i, j, c, false);
	}

	public RSImageProducer(int i, int j, Component c, boolean createDepthBuffer) {
		width = i;
		height = j;
		pixels = new int[i * j + 1];
		if (createDepthBuffer) {
			pixelsDepth = new float[pixels.length];
		} else {
			pixelsDepth = null;
		}
		DataBufferInt databufferint = new DataBufferInt(pixels, pixels.length);
		DirectColorModel directcolormodel = new DirectColorModel(32, 16711680, 65280, 255);
		WritableRaster writableraster = Raster.createWritableRaster(directcolormodel.createCompatibleSampleModel(width, height), databufferint, null);
		image = new BufferedImage(directcolormodel, writableraster, false, null);
		component = c;
		initDrawingArea();
	}

	public void initDrawingArea() {
		DrawingArea.initDrawingArea(pixels, width, height, pixelsDepth);
	}

	public void drawGraphics(Graphics graphics, int x, int y) {
		graphics.drawImage(image, x, y, component);
	}

	private final float[] pixelsDepth;
	private final int[] pixels;
	private final int width;
	private final int height;
	private final Image image;
	private final Component component;
}
