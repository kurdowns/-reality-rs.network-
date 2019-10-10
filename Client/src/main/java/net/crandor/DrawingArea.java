package net.crandor;

class DrawingArea extends NodeSub {

	static float[] depthBuffer;
	static int pixels[];
	static int width;
	static int height;
	static int topY;
	static int bottomY;
	static int topX;
	static int bottomX;
	private static final float[][] BLUR_MATRIX = { {1 / 16f, 1 / 8f, 1 / 16f},
			   {1 / 8f, 1 / 4f, 1 / 8f},
			   {1 / 16f, 1 / 8f, 1 / 16f}};

	static void drawAlphaFilledPixels(int xPos, int yPos, int pixelWidth, int pixelHeight, int color, int alpha) { // method586
        if (xPos < topX) {
            pixelWidth -= topX - xPos;
            xPos = topX;
        }
        if (yPos < topY) {
            pixelHeight -= topY - yPos;
            yPos = topY;
        }
        if (xPos + pixelWidth > bottomX)
            pixelWidth = bottomX - xPos;
        if (yPos + pixelHeight > bottomY)
            pixelHeight = bottomY - yPos;
        color = ((color & 0xff00ff) * alpha >> 8 & 0xff00ff)
                + ((color & 0xff00) * alpha >> 8 & 0xff00);
        int k1 = 256 - alpha;
        int l1 = width - pixelWidth;
        int i2 = xPos + yPos * width;
        for (int j2 = 0; j2 < pixelHeight; j2++) {
            for (int k2 = -pixelWidth; k2 < 0; k2++) {
                int l2 = pixels[i2];
                l2 = ((l2 & 0xff00ff) * k1 >> 8 & 0xff00ff)
                        + ((l2 & 0xff00) * k1 >> 8 & 0xff00);
                pixels[i2++] = color + l2;
            }
            i2 += l1;
        }
    }

    static void initDrawingArea(int ai[], int j, int i, float pixelsDepth[]) {
        pixels = ai;
        depthBuffer = pixelsDepth;
        width = j;
        height = i;
        setDrawingArea(0, 0, j, i);
    }

	static final void fillPixels(int x, int y, int color, int[] yCoords, int[] xCoords) {
		int step = x + y * width;
		for (y = 0; y < yCoords.length; y++) {
			int pixel = step + yCoords[y];
			for (x = -xCoords[y]; x < 0; x++) {
				pixels[pixel++] = color;
			}
			step += width;
		}
	}

    static void defaultDrawingAreaSize() {
        topX = 0;
        topY = 0;
        bottomX = width;
        bottomY = height;
    }

    static void setDrawingArea(int x, int y, int w, int h) {
        if (x < 0)
            x = 0;
        if (y < 0)
            y = 0;
        if (w > width)
            w = width;
        if (h > height)
            h = height;
        topX = x;
        topY = y;
        bottomX = w;
        bottomY = h;
    }

    static void setAllPixelsToZero(int rgb) {
        int i = width * height;
        for (int j = 0; j < i; j++) {
            pixels[j] = rgb;
        }
    }

    static void setAllPixelsToZero2(int rgb) {
        int i = width * height;
        for (int j = 0; j < i; j++) {
            pixels[j] = rgb;
            depthBuffer[j] = 2.14748365E9F;
        }
    }

    static void clearZBuffer() {
        int i = width * height;
        for (int j = 0; j < i; j++) {
            depthBuffer[j] = 2.14748365E9F;
        }
    }

	public static void blur() {
		for (int str = 0; str < 5; str++) {
			for (int y = 1; y < DrawingArea.height - 3; y++) {
				for (int x = 1; x < DrawingArea.width - 3; x++) {
					float sumRed = 0;
					float sumGreen = 0;
					float sumBlue = 0;
					for (int ky = -1; ky <= 1; ky++) {
						for (int kx = -1; kx <= 1; kx++) {
							// Calculate the adjacent pixel for this kernel point
							int pos = (y + ky) * DrawingArea.width + (x + kx);
							// Obtain the rgb values
							float valRed = (pixels[pos] >> 16) & 0xff;
							float valGreen = (pixels[pos] >> 8) & 0xff;
							float valBlue = pixels[pos] & 0xff;
							// Multiply adjacent pixels based on the kernel values
							sumRed += BLUR_MATRIX[ky + 1][kx + 1] * valRed;
							sumGreen += BLUR_MATRIX[ky + 1][kx + 1] * valGreen;
							sumBlue += BLUR_MATRIX[ky + 1][kx + 1] * valBlue;
						}
					}
					// For this pixel in the new image, set the rgb
					// based on the sum from the kernel
					pixels[x + y * DrawingArea.width] = (((int) sumRed << 16) & 0xff0000) | (((int) sumGreen << 8) & 0x00ff00) | ((int) sumBlue & 0xff);
				}
			}
		}
	}

    static void fillRectAlpha(int x, int y, int w, int h, int color, int alpha) {
        if (x < topX) {
            w -= topX - x;
            x = topX;
        }
        if (y < topY) {
            h -= topY - y;
            y = topY;
        }
        if (x + w > bottomX) {
            w = bottomX - x;
        }
        if (y + h > bottomY) {
            h = bottomY - y;
        }
		color = ((color & 0xff00ff) * alpha >> 8 & 0xff00ff) + ((color & 0xff00) * alpha >> 8 & 0xff00);
		int alpha2 = 256 - alpha;
		int pixelStep = width - w;
		int pixelPos = x + y * width;

		// stuart: fixes mouse over hovers
		if (pixelPos > pixels.length - 1) {
			return;
		}

		int destPixel;
		for (int yOff = 0; yOff < h; yOff++) {
			for (int xOff = -w; xOff < 0; xOff++) {
				destPixel = pixels[pixelPos];
				destPixel = ((destPixel & 0xff00ff) * alpha2 >> 8 & 0xff00ff) + ((destPixel & 0xff00) * alpha2 >> 8 & 0xff00);
				pixels[pixelPos++] = color + destPixel;
			}
			pixelPos += pixelStep;
		}
    }

    static void fillRect(int k, int j, int i1, int i, int l) {
        if (k < topX) {
            i1 -= topX - k;
            k = topX;
        }
        if (j < topY) {
            i -= topY - j;
            j = topY;
        }
        if (k + i1 > bottomX)
            i1 = bottomX - k;
        if (j + i > bottomY)
            i = bottomY - j;
        int k1 = width - i1;
        int l1 = k + j * width;
        for (int i2 = -i; i2 < 0; i2++) {
            for (int j2 = -i1; j2 < 0; j2++)
                pixels[l1++] = l;

            l1 += k1;
        }

    }

    static void drawRect(int x, int y, int w, int h, int color) {
        drawHorizontalLine(x, y, w, color);
        drawHorizontalLine(x, (y + h) - 1, w, color);
        drawVerticalLine(x, y, h, color);
        drawVerticalLine((x + w) - 1, y, h, color);
    }

    static void drawRectAlpha(int x, int y, int w, int h, int color, int alpha) {
        drawHorizontalLineAlpha(x, y, w, color, alpha);
        drawHorizontalLineAlpha(x, (y + h) - 1, w, color, alpha);
        if (h >= 3) {
            drawVerticalLineAlpha(x, y + 1, h - 2, color, alpha);
            drawVerticalLineAlpha((x + w) - 1, y + 1, h - 2, color, alpha);
        }
    }

    static void drawHorizontalLine(int x, int y, int length, int color) {
        if (y < topY || y >= bottomY)
            return;
        if (x < topX) {
            length -= topX - x;
            x = topX;
        }
        if (x + length > bottomX)
            length = bottomX - x;
        int i1 = x + y * width;
        for (int j1 = 0; j1 < length; j1++)
            pixels[i1 + j1] = color;
    }

    private static void drawHorizontalLineAlpha(int x, int y, int length, int color, int alpha) {
        if (y < topY || y >= bottomY)
            return;
        if (x < topX) {
            length -= topX - x;
            x = topX;
        }
        if (x + length > bottomX)
            length = bottomX - x;
        int j1 = 256 - alpha;
        int k1 = (color >> 16 & 0xff) * alpha;
        int l1 = (color >> 8 & 0xff) * alpha;
        int i2 = (color & 0xff) * alpha;
        int i3 = x + y * width;
        for (int j3 = 0; j3 < length; j3++) {
            int j2 = (pixels[i3] >> 16 & 0xff) * j1;
            int k2 = (pixels[i3] >> 8 & 0xff) * j1;
            int l2 = (pixels[i3] & 0xff) * j1;
            int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8)
                    + (i2 + l2 >> 8);
            pixels[i3++] = k3;
        }

    }

    static void drawVerticalLine(int x, int y, int length, int color) {
        if (x < topX || x >= bottomX)
            return;
        if (y < topY) {
            length -= topY - y;
            y = topY;
        }
        if (y + length > bottomY)
            length = bottomY - y;
        int j1 = x + y * width;
        for (int k1 = 0; k1 < length; k1++)
            pixels[j1 + k1 * width] = color;

    }

    private static void drawVerticalLineAlpha(int x, int y, int length, int color, int alpha) {
        if (x < topX || x >= bottomX)
            return;
        if (y < topY) {
            length -= topY - y;
            y = topY;
        }
        if (y + length > bottomY)
            length = bottomY - y;
        int j1 = 256 - alpha;
        int k1 = (color >> 16 & 0xff) * alpha;
        int l1 = (color >> 8 & 0xff) * alpha;
        int i2 = (color & 0xff) * alpha;
        int i3 = x + y * width;
        for (int j3 = 0; j3 < length; j3++) {
            int j2 = (pixels[i3] >> 16 & 0xff) * j1;
            int k2 = (pixels[i3] >> 8 & 0xff) * j1;
            int l2 = (pixels[i3] & 0xff) * j1;
            int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8)
                    + (i2 + l2 >> 8);
            pixels[i3] = k3;
            i3 += width;
        }
    }

    private static void fillCircleAlpha(int posX, int posY, int radius,
                                       int colour, int alpha) {
        int dest_intensity = 256 - alpha;
        int src_red = (colour >> 16 & 0xff) * alpha;
        int src_green = (colour >> 8 & 0xff) * alpha;
        int src_blue = (colour & 0xff) * alpha;
        int i3 = posY - radius;
        if (i3 < 0)
            i3 = 0;
        int j3 = posY + radius;
        if (j3 >= height)
            j3 = height - 1;
        for (int y = i3; y <= j3; y++) {
            int l3 = y - posY;
            int i4 = (int) Math.sqrt(radius * radius - l3 * l3);
            int x = posX - i4;
            if (x < 0)
                x = 0;
            int k4 = posX + i4;
            if (k4 >= width)
                k4 = width - 1;
            int pixel_offset = x + y * width;
            for (int i5 = x; i5 <= k4; i5++) {
                int dest_red = (pixels[pixel_offset] >> 16 & 0xff)
                        * dest_intensity;
                int dest_green = (pixels[pixel_offset] >> 8 & 0xff)
                        * dest_intensity;
                int dest_blue = (pixels[pixel_offset] & 0xff) * dest_intensity;
                int result_rgb = ((src_red + dest_red >> 8) << 16)
                        + ((src_green + dest_green >> 8) << 8)
                        + (src_blue + dest_blue >> 8);
                pixels[pixel_offset++] = result_rgb;
            }
        }
    }

    static void drawBubble(int x, int y, int radius, int colour,
                                  int initialAlpha) {
        fillCircleAlpha(x, y, radius, colour, initialAlpha);
        fillCircleAlpha(x, y, radius + 2, colour, 8);
        fillCircleAlpha(x, y, radius + 4, colour, 6);
        fillCircleAlpha(x, y, radius + 6, colour, 4);
        fillCircleAlpha(x, y, radius + 8, colour, 2);
    }

}
