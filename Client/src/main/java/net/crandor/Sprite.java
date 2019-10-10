package net.crandor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Sprite extends DrawingArea {

    public int myWidth;
    public int myHeight;
    int myPixels[];
    int offsetX;
    int offsetY;
    int maxWidth;
    int maxHeight;

    public Sprite() {
    }

    Sprite(int i, int j) {
        myPixels = new int[i * j];
        myWidth = maxWidth = i;
        myHeight = maxHeight = j;
        offsetX = offsetY = 0;
    }

    Sprite(StreamLoader streamLoader, String s, int i) {
        Stream stream = new Stream(streamLoader.getDataForName(s + ".dat"));
        Stream stream_1 = new Stream(streamLoader.getDataForName("index.dat"));
        stream_1.currentOffset = stream.readUnsignedWord();
        maxWidth = stream_1.readUnsignedWord();
        maxHeight = stream_1.readUnsignedWord();
        int j = stream_1.readUnsignedByte();
        int ai[] = new int[j];
        for (int k = 0; k < j - 1; k++) {
            ai[k + 1] = stream_1.read3Bytes();
            if (ai[k + 1] == 0)
                ai[k + 1] = 1;
        }

        for (int l = 0; l < i; l++) {
            stream_1.currentOffset += 2;
            stream.currentOffset += stream_1.readUnsignedWord()
                    * stream_1.readUnsignedWord();
            stream_1.currentOffset++;
        }

        offsetX = stream_1.readUnsignedByte();
        offsetY = stream_1.readUnsignedByte();
        myWidth = stream_1.readUnsignedWord();
        myHeight = stream_1.readUnsignedWord();
        int format = stream_1.readUnsignedByte();
        int pixelCount = myWidth * myHeight;
        myPixels = new int[pixelCount];
        if (format == 0) {
            for (int k1 = 0; k1 < pixelCount; k1++) {
                myPixels[k1] = ai[stream.readUnsignedByte()];
            }
        } else if (format == 1) {
            for (int l1 = 0; l1 < myWidth; l1++) {
                for (int i2 = 0; i2 < myHeight; i2++) {
                    myPixels[l1 + i2 * myWidth] = ai[stream.readUnsignedByte()];
                }
            }
        }
        setTransparency(255, 0, 255);
    }

	Sprite(byte spriteData[]) {
		try {
			InputStream inputStream = new ByteArrayInputStream(spriteData);
			BufferedImage bufferedImage = ImageIO.read(inputStream);
			myWidth = bufferedImage.getWidth();
			myHeight = bufferedImage.getHeight();
			maxWidth = myWidth;
			maxHeight = myHeight;
			offsetX = 0;
			offsetY = 0;
			myPixels = new int[myWidth * myHeight];
			PixelGrabber pixelgrabber = new PixelGrabber(bufferedImage, 0, 0, myWidth, myHeight, myPixels, 0, myWidth);
			pixelgrabber.grabPixels();
			setTransparency(255, 0, 255);

			inputStream.close();
			inputStream = null;
			bufferedImage = null;
			pixelgrabber = null;
		} catch (Exception _ex) {
			/* empty */
		}
	}

    private static final void plotAlphaPixels(final int[] destPixels, final int[] sourcePixels, int sourcePixelsPos, int destPixelsPos, final int width, final int height, final int destPixelsStep, final int sourcePixelsStep) {
        int alpha;
        int alpha2;
        int src;
        int dest;
        for (int y = -height; y < 0; y++) {
            for (int x = -width; x < 0; x++) {
                src = sourcePixels[sourcePixelsPos++];
                alpha = src >>> 24;
                if (alpha != 0) {
                    alpha2 = 256 - alpha;
                    dest = destPixels[destPixelsPos];
                    destPixels[destPixelsPos++] = ((src & 0xff00ff) * alpha + (dest & 0xff00ff) * alpha2 & ~0xff00ff) + ((src & 0xff00) * alpha + (dest & 0xff00) * alpha2 & 0xff0000) >>> 8;
                } else {
                    destPixelsPos++;
                }
            }
            destPixelsPos += destPixelsStep;
            sourcePixelsPos += sourcePixelsStep;
        }
    }

    void drawAdvancedSprite(int x, int y, int alpha) {
        x += offsetX;
        y += offsetY;
        int i1 = x + y * width;
        int j1 = 0;
        int k1 = myHeight;
        int l1 = myWidth;
        int i2 = width - l1;
        int j2 = 0;
        if (y < topY) {
            int k2 = topY - y;
            k1 -= k2;
            y = topY;
            j1 += k2 * l1;
            i1 += k2 * width;
        }
        if (y + k1 > bottomY)
            k1 -= (y + k1) - bottomY;
        if (x < topX) {
            int l2 = topX - x;
            l1 -= l2;
            x = topX;
            j1 += l2;
            i1 += l2;
            j2 += l2;
            i2 += l2;
        }
        if (x + l1 > bottomX) {
            int i3 = (x + l1) - bottomX;
            l1 -= i3;
            j2 += i3;
            i2 += i3;
        }
        if (!(l1 <= 0 || k1 <= 0)) {
        	plotAlphaPixels(pixels, myPixels, j1, i1, l1, k1, i2,
                    j2, alpha);
        }
    }

    void drawAdvancedSprite(int x, int y) {
        x += offsetX;
        y += offsetY;
        int i1 = x + y * width;
        int j1 = 0;
        int k1 = myHeight;
        int l1 = myWidth;
        int i2 = width - l1;
        int j2 = 0;
        if (y < topY) {
            int k2 = topY - y;
            k1 -= k2;
            y = topY;
            j1 += k2 * l1;
            i1 += k2 * width;
        }
        if (y + k1 > bottomY)
            k1 -= (y + k1) - bottomY;
        if (x < topX) {
            int l2 = topX - x;
            l1 -= l2;
            x = topX;
            j1 += l2;
            i1 += l2;
            j2 += l2;
            i2 += l2;
        }
        if (x + l1 > bottomX) {
            int i3 = (x + l1) - bottomX;
            l1 -= i3;
            j2 += i3;
            i2 += i3;
        }
        if (!(l1 <= 0 || k1 <= 0)) {
            plotAlphaPixels(pixels, myPixels, j1, i1, l1, k1, i2, j2);
        }
    }

	private void plotAlphaPixels(int[] destPixels, int[] sourcePixels, int sourcePixelsPos, int destPixelsPos, int width, int height, int destPixelsStep, int sourcePixelsStep, int sourceAlpha) {
		int alpha;
		int alpha2;
		int src;
		int dest;
		for (int y = -height; y < 0; y++) {
			for (int x = -width; x < 0; x++) {
				alpha = (sourcePixels[sourcePixelsPos] >>> 24) * sourceAlpha >> 8;
				alpha2 = 256 - alpha;
				src = sourcePixels[sourcePixelsPos++];
				dest = destPixels[destPixelsPos];
				destPixels[destPixelsPos++] = ((src & 0xff00ff) * alpha + (dest & 0xff00ff) * alpha2 & ~0xff00ff) + ((src & 0xff00) * alpha + (dest & 0xff00) * alpha2 & 0xff0000) >>> 8;
			}
			destPixelsPos += destPixelsStep;
			sourcePixelsPos += sourcePixelsStep;
		}
	}

    void drawTransparentSprite(int i, int j, int opacity) {
        i += offsetX;
        j += offsetY;
        int i1 = i + j * width;
        int j1 = 0;
        int k1 = myHeight;
        int l1 = myWidth;
        int i2 = width - l1;
        int j2 = 0;
        if (j < topY) {
            int k2 = topY - j;
            k1 -= k2;
            j = topY;
            j1 += k2 * l1;
            i1 += k2 * width;
        }
        if (j + k1 > bottomY)
            k1 -= (j + k1) - bottomY;
        if (i < topX) {
            int l2 = topX - i;
            l1 -= l2;
            i = topX;
            j1 += l2;
            i1 += l2;
            j2 += l2;
            i2 += l2;
        }
        if (i + l1 > bottomX) {
            int i3 = (i + l1) - bottomX;
            l1 -= i3;
            j2 += i3;
            i2 += i3;
        }
        if (!(l1 <= 0 || k1 <= 0)) {
            method351(j1, l1, pixels, myPixels, j2, k1, i2, opacity, i1);
        }
    }

    public void setAlphaTransparency(int a) {
        for (int pixel = 0; pixel < myPixels.length; pixel++) {
            if (((myPixels[pixel] >> 24) & 255) == a)
                myPixels[pixel] = 0;
        }
    }

    private void setTransparency(int transRed, int transGreen, int transBlue) {
        for (int index = 0; index < myPixels.length; index++)
            if (((myPixels[index] >> 16) & 255) == transRed  && ((myPixels[index] >> 8) & 255) == transGreen && (myPixels[index] & 255) == transBlue)
                myPixels[index] = 0;
    }

    void method343() {
        initDrawingArea(myPixels, myWidth, myHeight, null);
    }

    void method345() {
        int ai[] = new int[maxWidth * maxHeight];
        for (int j = 0; j < myHeight; j++) {
            System.arraycopy(myPixels, j * myWidth, ai, j + offsetY
                    * maxWidth + offsetX, myWidth);
        }

        myPixels = ai;
        myWidth = maxWidth;
        myHeight = maxHeight;
        offsetX = 0;
        offsetY = 0;
    }

    void method346(int i, int j) {
        i += offsetX;
        j += offsetY;
        int l = i + j * width;
        int i1 = 0;
        int j1 = myHeight;
        int k1 = myWidth;
        int l1 = width - k1;
        int i2 = 0;
        if (j < topY) {
            int j2 = topY - j;
            j1 -= j2;
            j = topY;
            i1 += j2 * k1;
            l += j2 * width;
        }
        if (j + j1 > bottomY)
            j1 -= (j + j1) - bottomY;
        if (i < topX) {
            int k2 = topX - i;
            k1 -= k2;
            i = topX;
            i1 += k2;
            l += k2;
            i2 += k2;
            l1 += k2;
        }
        if (i + k1 > bottomX) {
            int l2 = (i + k1) - bottomX;
            k1 -= l2;
            i2 += l2;
            l1 += l2;
        }
        if (k1 <= 0 || j1 <= 0) {
        } else {
            method347(l, k1, j1, i2, i1, l1, myPixels, pixels);
        }
    }

    private void method347(int i, int j, int k, int l, int i1, int k1, int ai[],
                           int ai1[]) {
        int l1 = -(j >> 2);
        j = -(j & 3);
        for (int i2 = -k; i2 < 0; i2++) {
            for (int j2 = l1; j2 < 0; j2++) {
                ai1[i++] = ai[i1++];
                ai1[i++] = ai[i1++];
                ai1[i++] = ai[i1++];
                ai1[i++] = ai[i1++];
            }

            for (int k2 = j; k2 < 0; k2++)
                ai1[i++] = ai[i1++];

            i += k1;
            i1 += l;
        }
    }

    void drawSprite(int x, int y) {
        x += offsetX;
        y += offsetY;
        int destPixelId = x + y * width;
        int srcPixelId = 0;
        int spriteHeight = myHeight;
        int spriteWidth = myWidth;
        int destPixelStep = width - spriteWidth;
        int srcPixelStep = 0;
        if (y < topY) {
            int outOfBoundsY = topY - y;
            spriteHeight -= outOfBoundsY;
            y = topY;
            srcPixelId += outOfBoundsY * spriteWidth;
            destPixelId += outOfBoundsY * width;
        }
        if (y + spriteHeight > bottomY) {
            spriteHeight -= y + spriteHeight - bottomY;

        }
        if (x < topX) {
            int outOfBoundsX = topX - x;
            spriteWidth -= outOfBoundsX;
            x = topX;
            srcPixelId += outOfBoundsX;
            destPixelId += outOfBoundsX;
            srcPixelStep += outOfBoundsX;
            destPixelStep += outOfBoundsX;
        }
        if (x + spriteWidth > bottomX) {
            int outOfBoundsX = x + spriteWidth - bottomX;
            spriteWidth -= outOfBoundsX;
            srcPixelStep += outOfBoundsX;
            destPixelStep += outOfBoundsX;
        }
        if (spriteWidth > 0 && spriteHeight > 0) {
            method349(pixels, myPixels, srcPixelId, destPixelId, spriteWidth, spriteHeight, destPixelStep, srcPixelStep);
        }
    }

    void drawSprite(int i, int k, int color) {
        int tempWidth = myWidth + 2;
        int tempHeight = myHeight + 2;
        int[] tempArray = new int[tempWidth * tempHeight];
        for (int x = 0; x < myWidth; x++) {
            for (int y = 0; y < myHeight; y++) {
                if (myPixels[x + y * myWidth] != 0)
                    tempArray[(x + 1) + (y + 1) * tempWidth] = myPixels[x + y
                            * myWidth];
            }
        }
        for (int x = 0; x < tempWidth; x++) {
            for (int y = 0; y < tempHeight; y++) {
                if (tempArray[(x) + (y) * tempWidth] == 0) {
                    if (x < tempWidth - 1
                            && tempArray[(x + 1) + ((y) * tempWidth)] != 0
                            && tempArray[(x + 1) + ((y) * tempWidth)] != 0xffffff) {
                        tempArray[(x) + (y) * tempWidth] = color;
                    }
                    if (x > 0
                            && tempArray[(x - 1) + ((y) * tempWidth)] != 0
                            && tempArray[(x - 1) + ((y) * tempWidth)] != 0xffffff) {
                        tempArray[(x) + (y) * tempWidth] = color;
                    }
                    if (y < tempHeight - 1
                            && tempArray[(x) + ((y + 1) * tempWidth)] != 0
                            && tempArray[(x) + ((y + 1) * tempWidth)] != 0xffffff) {
                        tempArray[(x) + (y) * tempWidth] = color;
                    }
                    if (y > 0
                            && tempArray[(x) + ((y - 1) * tempWidth)] != 0
                            && tempArray[(x) + ((y - 1) * tempWidth)] != 0xffffff) {
                        tempArray[(x) + (y) * tempWidth] = color;
                    }
                }
            }
        }
        i--;
        k--;
        i += offsetX;
        k += offsetY;
        int l = i + k * width;
        int i1 = 0;
        int j1 = tempHeight;
        int k1 = tempWidth;
        int l1 = width - k1;
        int i2 = 0;
        if (k < topY) {
            int j2 = topY - k;
            j1 -= j2;
            k = topY;
            i1 += j2 * k1;
            l += j2 * width;
        }
        if (k + j1 > bottomY) {
            j1 -= (k + j1) - bottomY;
        }
        if (i < topX) {
            int k2 = topX - i;
            k1 -= k2;
            i = topX;
            i1 += k2;
            l += k2;
            i2 += k2;
            l1 += k2;
        }
        if (i + k1 > bottomX) {
            int l2 = (i + k1) - bottomX;
            k1 -= l2;
            i2 += l2;
            l1 += l2;
        }
        if (!(k1 <= 0 || j1 <= 0)) {
            method349(pixels, tempArray, i1, l, k1, j1, l1, i2);
        }
    }

    private void method349(int ai[], int ai1[], int j, int k, int l, int i1,
                           int j1, int k1) {
        int i;// was parameter
        int l1 = -(l >> 2);
        l = -(l & 3);
        for (int i2 = -i1; i2 < 0; i2++) {
            for (int j2 = l1; j2 < 0; j2++) {
                i = ai1[j++];
                if (i != 0 && i != -1) {
                    ai[k++] = i;
                } else {
                    k++;
                }
                i = ai1[j++];
                if (i != 0 && i != -1) {
                    ai[k++] = i;
                } else {
                    k++;
                }
                i = ai1[j++];
                if (i != 0 && i != -1) {
                    ai[k++] = i;
                } else {
                    k++;
                }
                i = ai1[j++];
                if (i != 0 && i != -1) {
                    ai[k++] = i;
                } else {
                    k++;
                }
            }

            for (int k2 = l; k2 < 0; k2++) {
                i = ai1[j++];
                if (i != 0 && i != -1) {
                    ai[k++] = i;
                } else {
                    k++;
                }
            }
            k += j1;
            j += k1;
        }
    }

    private void method351(int srcPixelId, int width, int destPixels[], int srcPixels[], int srcPixelStep, int height,
                           int destPixelStep, int transparency, int destPixelId) {
        int srcPixel;
		int transDelta = 256 - transparency;
		int destPixel;
		for (int y = -height; y < 0; y++) {
			for (int x = -width; x < 0; x++) {
				srcPixel = srcPixels[srcPixelId++];
				if (srcPixel != 0) {
					destPixel = destPixels[destPixelId];
					destPixels[destPixelId++] = ((srcPixel & 0xff00ff) * transparency + (destPixel & 0xff00ff) * transDelta & ~0xff00ff) + ((srcPixel & 0xff00) * transparency + (destPixel & 0xff00) * transDelta & 0xff0000) >> 8;
				} else {
					destPixelId++;
				}
			}
			destPixelId += destPixelStep;
			srcPixelId += srcPixelStep;
		}
    }

	void shadow(int color) {
		for (int y = this.myHeight - 1; y > 0; y--) {
			int pixelPos = y * this.myWidth;
			for (int x = this.myWidth - 1; x > 0; x--) {
				if (this.myPixels[x + pixelPos] == 0 && this.myPixels[x + pixelPos - 1 - this.myWidth] != 0) {
					this.myPixels[x + pixelPos] = color;
				}
			}
		}
	}

	void outline(int color) {
		int[] pixels = new int[this.myWidth * this.myHeight];
		int pixelPos = 0;
		for (int y = 0; y < this.myHeight; y++) {
			for (int x = 0; x < this.myWidth; x++) {
				int pixel = this.myPixels[pixelPos];
				if (pixel == 0) {
					if (x > 0 && this.myPixels[pixelPos - 1] != 0) {
						pixel = color;
					} else if (y > 0 && this.myPixels[pixelPos - this.myWidth] != 0) {
						pixel = color;
					} else if (x < this.myWidth - 1 && this.myPixels[pixelPos + 1] != 0) {
						pixel = color;
					} else if (y < this.myHeight - 1 && this.myPixels[pixelPos + this.myWidth] != 0) {
						pixel = color;
					}
				}
				pixels[pixelPos++] = pixel;
			}
		}
		this.myPixels = pixels;
	}

    void method352(int height, int angle, int ai[], int hingesize,
                   int ai1[], int centerY, int j1, int k1, int width, int centerX) {
        try {
            int negCenterX = -width / 2;
            int negCenterY = -height / 2;
            double angle2 = (double) (angle & 0xffff) * 9.587379924285257E-5;
            int pyOffset = (int) (Math.sin(angle2) * 65536D);
            int pxOffset = (int) (Math.cos(angle2) * 65536D);
            pyOffset = pyOffset * hingesize >> 8;
            pxOffset = pxOffset * hingesize >> 8;
            int j3 = (centerX << 16)
                    + (negCenterY * pyOffset + negCenterX * pxOffset);
            int k3 = (centerY << 16)
                    + (negCenterY * pxOffset - negCenterX * pyOffset);
            int baseOffset = k1 + j1 * DrawingArea.width;
            for (j1 = 0; j1 < height; j1++) {
                int targetLineOffset = ai1[j1];
                int targetOffset = baseOffset + targetLineOffset;
                int x = j3 + pxOffset * targetLineOffset;
                int y = k3 - pyOffset * targetLineOffset;
                for (k1 = -ai[j1]; k1 < 0; k1++) {
                    if (Settings.minimapShading) {
                        int x1 = x >> 16;
                        int y1 = y >> 16;
                        int x2 = x1 + 1;
                        int y2 = y1 + 1;
                        int c1 = myPixels[x1 + y1 * myWidth];
                        int c2 = myPixels[x2 + y1 * myWidth];
                        int c3 = myPixels[x1 + y2 * myWidth];
                        int c4 = myPixels[x2 + y2 * myWidth];
                        int u1 = (x >> 8) - (x1 << 8);
                        int v1 = (y >> 8) - (y1 << 8);
                        int u2 = (x2 << 8) - (x >> 8);
                        int v2 = (y2 << 8) - (y >> 8);
                        int a1 = u2 * v2;
                        int a2 = u1 * v2;
                        int a3 = u2 * v1;
                        int a4 = u1 * v1;
                        int r = (c1 >> 16 & 0xff) * a1 + (c2 >> 16 & 0xff) * a2 + (c3 >> 16 & 0xff) * a3 + (c4 >> 16 & 0xff) * a4 & 0xff0000;
                        int g = (c1 >> 8 & 0xff) * a1 + (c2 >> 8 & 0xff) * a2 + (c3 >> 8 & 0xff) * a3 + (c4 >> 8 & 0xff) * a4 >> 8 & 0xff00;
                        int b = (c1 & 0xff) * a1 + (c2 & 0xff) * a2 + (c3 & 0xff) * a3 + (c4 & 0xff) * a4 >> 16;
                        pixels[targetOffset++] = r | g | b;
                    } else {
                        pixels[targetOffset++] = myPixels[(x >> 16) + (y >> 16) * myWidth];
                    }
                    x += pxOffset;
                    y -= pyOffset;
                }

                j3 += pyOffset;
                k3 += pxOffset;
                baseOffset += DrawingArea.width;
            }

        } catch (Exception _ex) {
        }
    }

    void greyScale() {
        for (int index = 0; index < myWidth * myHeight; index++) {
            int alpha = myPixels[index] >>> 24;
            int red = myPixels[index] >>> 16 & 0xff;
            int green = myPixels[index] >>> 8 & 0xff;
            int blue = myPixels[index] & 0xff;
            int delta = (red + green + blue) / 3;
            myPixels[index] = delta | delta << 8 | delta << 16 | alpha << 24;
        }
    }

}
