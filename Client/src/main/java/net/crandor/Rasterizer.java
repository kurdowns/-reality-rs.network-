
package net.crandor;

public class Rasterizer extends DrawingArea {

    public static void nullLoader() {
        shadowDecay = null;
        SINE = null;
        COSINE = null;
        lineOffsets = null;
        hsl2rgb = null;
        brightness = 1.0F;
    }

    public static void nullLoaderSafe() {
        lineOffsets = null;
        brightness = 1.0F;
    }

	static final int farthestBitValue(int i) {
		i = --i | i >>> 1;
		i |= i >>> 2;
		i |= i >>> 4;
		i |= i >>> 8;
		i |= i >>> 16;
		return i + 1;
	}

    public static void setDefaultBounds() {
        setBounds(topX, topY, bottomX, bottomY);
    }

    public static void setBounds(int x, int y, int width, int height) {
		endX = width - x;
		endY = height - y;
		calculateViewport();
		if (lineOffsets == null || lineOffsets.length < endY) {
			lineOffsets = new int[farthestBitValue(endY)];
		}
		int yPos = y * DrawingArea.width + x;
		for (int offY = 0; offY < endY; offY++) {
			lineOffsets[offY] = yPos;
			yPos += DrawingArea.width;
		}
    }

	static final void setViewport(int coordX, int coordY) {
		int offset = lineOffsets[0];
		int offY = offset / DrawingArea.width;
		int offX = offset - offY * DrawingArea.width;
		centerX = coordX - offX;
		centerY = coordY - offY;
		Rasterizer.viewportLeft = -centerX;
		Rasterizer.viewportRight = endX - centerX;
		Rasterizer.viewportTop = -centerY;
		Rasterizer.viewportBottom = endY - centerY;
	}

	static void calculateViewport() {
		centerX = endX / 2;
		centerY = endY / 2;
		viewportLeft = -centerX;
		viewportRight = endX - centerX;
		viewportTop = -centerY;
		viewportBottom = endY - centerY;
	}

    public static void calculatePalette(float brightness) {
        if (Rasterizer.brightness == brightness)
            return;

        Rasterizer.brightness = brightness;
        int hsl = 0;
        for (int k = 0; k < 512; k++) {
            float d1 = k / 8 / 64F + 0.0078125F;
            float d2 = (k & 7) / 8F + 0.0625F;
            for (int k1 = 0; k1 < 128; k1++) {
                float d3 = k1 / 128F;
                float r = d3;
                float g = d3;
                float b = d3;
                if (d2 != 0.0F) {
                    float d7;
                    if (d3 < 0.5F) {
                        d7 = d3 * (1.0F + d2);
                    } else {
                        d7 = (d3 + d2) - d3 * d2;
                    }
                    float d8 = 2F * d3 - d7;
                    float d9 = d1 + 1F / 3F;
                    if (d9 > 1.0F) {
                        d9--;
                    }
                    float d10 = d1;
                    float d11 = d1 - 1F / 3F;
                    if (d11 < 0.0F) {
                        d11++;
                    }
                    if (6F * d9 < 1.0F) {
                        r = d8 + (d7 - d8) * 6F * d9;
                    } else if (2F * d9 < 1.0F) {
                        r = d7;
                    } else if (3F * d9 < 2F) {
                        r = d8 + (d7 - d8) * ((2F / 3F) - d9) * 6F;
                    } else {
                        r = d8;
                    }
                    if (6F * d10 < 1.0F) {
                        g = d8 + (d7 - d8) * 6F * d10;
                    } else if (2F * d10 < 1.0F) {
                        g = d7;
                    } else if (3F * d10 < 2F) {
                        g = d8 + (d7 - d8) * ((2F / 3F) - d10) * 6F;
                    } else {
                        g = d8;
                    }
                    if (6F * d11 < 1.0F) {
                        b = d8 + (d7 - d8) * 6F * d11;
                    } else if (2F * d11 < 1.0F) {
                        b = d7;
                    } else if (3F * d11 < 2F) {
                        b = d8 + (d7 - d8) * ((2F / 3F) - d11) * 6F;
                    } else {
                        b = d8;
                    }
                }
                int rgb = ((int) ((float) Math.pow(r, brightness) * 256F) << 16)
                        + ((int) ((float) Math.pow(g, brightness) * 256F) << 8)
                        + (int) ((float) Math.pow(b, brightness) * 256F);
                if (rgb == 0)
                    rgb = 1;

                hsl2rgb[hsl++] = rgb;
            }
        }
    }

    public static float brightness = 1.0F;

    public static int adjustBrightness(int rgb, float brightness) {
		double red = (double) (rgb >> 16) / 256.0;
		double green = (double) (rgb >> 8 & 0xff) / 256.0;
		double blue = (double) (rgb & 0xff) / 256.0;
		red = Math.pow(red, brightness);
		green = Math.pow(green, brightness);
		blue = Math.pow(blue, brightness);
		int nRed = (int) (red * 256.0);
		int nGreen = (int) (green * 256.0);
		int nBlue = (int) (blue * 256.0);
		return (nRed << 16) + (nGreen << 8) + nBlue;
	}

    public static int adjustBrightnessLinear(int rgb, int factor) {
        return ((((rgb >>> 16) * factor) & 0xff00) << 8)
                | ((((rgb >>> 8) & 0xff) * factor) & 0xff00)
                | (((rgb & 0xff) * factor) >> 8);
    }

    public static void drawFlatTriangle(int y1, int y2, int y3, int x1, int x2, int x3, float z1, float z2, float z3, int rgb) {
        int dx1 = 0;
        float dz1 = 0;
        if (y2 != y1) {
            final int d = (y2 - y1);
            dx1 = (x2 - x1 << 16) / d;
            dz1 = (z2 - z1) / d;
        }
        int dx2 = 0;
        float dz2 = 0;
        if (y3 != y2) {
            final int d = (y3 - y2);
            dx2 = (x3 - x2 << 16) / d;
            dz2 = (z3 - z2) / d;
        }
        int dx3 = 0;
        float dz3 = 0;
        if (y3 != y1) {
            final int d = (y1 - y3);
            dx3 = (x1 - x3 << 16) / d;
            dz3 = (z1 - z3) / d;
        }
        if (y1 <= y2 && y1 <= y3) {
            if (y1 >= bottomY) {
                return;
            }
            if (y2 > bottomY) {
                y2 = bottomY;
            }
            if (y3 > bottomY) {
                y3 = bottomY;
            }
            if (y2 < y3) {
                x3 = x1 <<= 16;
                z3 = z1;
                if (y1 < 0) {
                    x3 -= dx3 * y1;
                    x1 -= dx1 * y1;
                    z3 -= dz3 * y1;
                    z1 -= dz1 * y1;
                    y1 = 0;
                }
                x2 <<= 16;
                if (y2 < 0) {
                    x2 -= dx2 * y2;
                    z2 -= dz2 * y2;
                    y2 = 0;
                }
                if (y1 != y2 && dx3 < dx1 || y1 == y2 && dx3 > dx2) {
                    y3 -= y2;
                    y2 -= y1;
                    for (y1 = lineOffsets[y1]; --y2 >= 0; y1 += width) {
                        drawFlatScanline(pixels, y1, rgb, x3 >> 16, x1 >> 16, z3, z1, depthBuffer);
                        x3 += dx3;
                        x1 += dx1;
                        z3 += dz3;
                        z1 += dz1;
                    }
                    while (--y3 >= 0) {
                        drawFlatScanline(pixels, y1, rgb, x3 >> 16, x2 >> 16, z3, z2, depthBuffer);
                        x3 += dx3;
                        x2 += dx2;
                        z3 += dz3;
                        z2 += dz2;
                        y1 += width;
                    }
                    return;
                }
                y3 -= y2;
                y2 -= y1;
                for (y1 = lineOffsets[y1]; --y2 >= 0; y1 += width) {
                    drawFlatScanline(pixels, y1, rgb, x1 >> 16, x3 >> 16, z1, z3, depthBuffer);
                    x3 += dx3;
                    x1 += dx1;
                    z3 += dz3;
                    z1 += dz1;
                }
                while (--y3 >= 0) {
                    drawFlatScanline(pixels, y1, rgb, x2 >> 16, x3 >> 16, z2, z3, depthBuffer);
                    x3 += dx3;
                    x2 += dx2;
                    z3 += dz3;
                    z2 += dz2;
                    y1 += width;
                }
                return;
            }
            x2 = x1 <<= 16;
            z2 = z1;
            if (y1 < 0) {
                x2 -= dx3 * y1;
                x1 -= dx1 * y1;
                z2 -= dz3 * y1;
                z1 -= dz1 * y1;
                y1 = 0;
            }
            x3 <<= 16;
            if (y3 < 0) {
                x3 -= dx2 * y3;
                z3 -= dz2 * y3;
                y3 = 0;
            }
            if (y1 != y3 && dx3 < dx1 || y1 == y3 && dx2 > dx1) {
                y2 -= y3;
                y3 -= y1;
                for (y1 = lineOffsets[y1]; --y3 >= 0; y1 += width) {
                    drawFlatScanline(pixels, y1, rgb, x2 >> 16, x1 >> 16, z2, z1, depthBuffer);
                    x2 += dx3;
                    x1 += dx1;
                    z2 += dz3;
                    z1 += dz1;
                }
                while (--y2 >= 0) {
                    drawFlatScanline(pixels, y1, rgb, x3 >> 16, x1 >> 16, z3, z1, depthBuffer);
                    x3 += dx2;
                    x1 += dx1;
                    z3 += dz2;
                    z1 += dz1;
                    y1 += width;
                }
                return;
            }
            y2 -= y3;
            y3 -= y1;
            for (y1 = lineOffsets[y1]; --y3 >= 0; y1 += width) {
                drawFlatScanline(pixels, y1, rgb, x1 >> 16, x2 >> 16, z1, z2, depthBuffer);
                x2 += dx3;
                x1 += dx1;
                z2 += dz3;
                z1 += dz1;
            }
            while (--y2 >= 0) {
                drawFlatScanline(pixels, y1, rgb, x1 >> 16, x3 >> 16, z1, z3, depthBuffer);
                x3 += dx2;
                x1 += dx1;
                z3 += dz2;
                z1 += dz1;
                y1 += width;
            }
            return;
        }
        if (y2 <= y3) {
            if (y2 >= bottomY) {
                return;
            }
            if (y3 > bottomY) {
                y3 = bottomY;
            }
            if (y1 > bottomY) {
                y1 = bottomY;
            }
            if (y3 < y1) {
                x1 = x2 <<= 16;
                z1 = z2;
                if (y2 < 0) {
                    x1 -= dx1 * y2;
                    x2 -= dx2 * y2;
                    z1 -= dz1 * y2;
                    z2 -= dz2 * y2;
                    y2 = 0;
                }
                x3 <<= 16;
                if (y3 < 0) {
                    x3 -= dx3 * y3;
                    z3 -= dz3 * y3;
                    y3 = 0;
                }
                if (y2 != y3 && dx1 < dx2 || y2 == y3 && dx1 > dx3) {
                    y1 -= y3;
                    y3 -= y2;
                    for (y2 = lineOffsets[y2]; --y3 >= 0; y2 += width) {
                        drawFlatScanline(pixels, y2, rgb, x1 >> 16, x2 >> 16, z1, z2, depthBuffer);
                        x1 += dx1;
                        x2 += dx2;
                        z1 += dz1;
                        z2 += dz2;
                    }
                    while (--y1 >= 0) {
                        drawFlatScanline(pixels, y2, rgb, x1 >> 16, x3 >> 16, z1, z3, depthBuffer);
                        x1 += dx1;
                        x3 += dx3;
                        z1 += dz1;
                        z3 += dz3;
                        y2 += width;
                    }
                    return;
                }
                y1 -= y3;
                y3 -= y2;
                for (y2 = lineOffsets[y2]; --y3 >= 0; y2 += width) {
                    drawFlatScanline(pixels, y2, rgb, x2 >> 16, x1 >> 16, z2, z1, depthBuffer);
                    x1 += dx1;
                    x2 += dx2;
                    z1 += dz1;
                    z2 += dz2;
                }
                while (--y1 >= 0) {
                    drawFlatScanline(pixels, y2, rgb, x3 >> 16, x1 >> 16, z3, z1, depthBuffer);
                    x1 += dx1;
                    x3 += dx3;
                    z1 += dz1;
                    z3 += dz3;
                    y2 += width;
                }
                return;
            }
            x3 = x2 <<= 16;
            z3 = z2;
            if (y2 < 0) {
                x3 -= dx1 * y2;
                x2 -= dx2 * y2;
                z3 -= dz1 * y2;
                z2 -= dz2 * y2;
                y2 = 0;
            }
            x1 <<= 16;
            if (y1 < 0) {
                x1 -= dx3 * y1;
                z1 -= dz3 * y1;
                y1 = 0;
            }
            if (dx1 < dx2) {
                y3 -= y1;
                y1 -= y2;
                for (y2 = lineOffsets[y2]; --y1 >= 0; y2 += width) {
                    drawFlatScanline(pixels, y2, rgb, x3 >> 16, x2 >> 16, z3, z2, depthBuffer);
                    x3 += dx1;
                    x2 += dx2;
                    z3 += dz1;
                    z2 += dz2;
                }
                while (--y3 >= 0) {
                    drawFlatScanline(pixels, y2, rgb, x1 >> 16, x2 >> 16, z1, z2, depthBuffer);
                    x1 += dx3;
                    x2 += dx2;
                    z1 += dz3;
                    z2 += dz2;
                    y2 += width;
                }
                return;
            }
            y3 -= y1;
            y1 -= y2;
            for (y2 = lineOffsets[y2]; --y1 >= 0; y2 += width) {
                drawFlatScanline(pixels, y2, rgb, x2 >> 16, x3 >> 16, z2, z3, depthBuffer);
                x3 += dx1;
                x2 += dx2;
                z3 += dz1;
                z2 += dz2;
            }
            while (--y3 >= 0) {
                drawFlatScanline(pixels, y2, rgb, x2 >> 16, x1 >> 16, z2, z1, depthBuffer);
                x1 += dx3;
                x2 += dx2;
                z1 += dz3;
                z2 += dz2;
                y2 += width;
            }
            return;
        }
        if (y3 >= bottomY) {
            return;
        }
        if (y1 > bottomY) {
            y1 = bottomY;
        }
        if (y2 > bottomY) {
            y2 = bottomY;
        }
        if (y1 < y2) {
            x2 = x3 <<= 16;
            z2 = z3;
            if (y3 < 0) {
                x2 -= dx2 * y3;
                x3 -= dx3 * y3;
                z2 -= dz2 * y3;
                z3 -= dz3 * y3;
                y3 = 0;
            }
            x1 <<= 16;
            if (y1 < 0) {
                x1 -= dx1 * y1;
                z1 -= dz1 * y1;
                y1 = 0;
            }
            if (dx2 < dx3) {
                y2 -= y1;
                y1 -= y3;
                for (y3 = lineOffsets[y3]; --y1 >= 0; y3 += width) {
                    drawFlatScanline(pixels, y3, rgb, x2 >> 16, x3 >> 16, z2, z3, depthBuffer);
                    x2 += dx2;
                    x3 += dx3;
                    z2 += dz2;
                    z3 += dz3;
                }
                while (--y2 >= 0) {
                    drawFlatScanline(pixels, y3, rgb, x2 >> 16, x1 >> 16, z2, z1, depthBuffer);
                    x2 += dx2;
                    x1 += dx1;
                    z2 += dz2;
                    z1 += dz1;
                    y3 += width;
                }
                return;
            }
            y2 -= y1;
            y1 -= y3;
            for (y3 = lineOffsets[y3]; --y1 >= 0; y3 += width) {
                drawFlatScanline(pixels, y3, rgb, x3 >> 16, x2 >> 16, z3, z2, depthBuffer);
                x2 += dx2;
                x3 += dx3;
                z2 += dz2;
                z3 += dz3;
            }
            while (--y2 >= 0) {
                drawFlatScanline(pixels, y3, rgb, x1 >> 16, x2 >> 16, z1, z2, depthBuffer);
                x2 += dx2;
                x1 += dx1;
                z2 += dz2;
                z1 += dz1;
                y3 += width;
            }
            return;
        }
        x1 = x3 <<= 16;
        z1 = z3;
        if (y3 < 0) {
            x1 -= dx2 * y3;
            x3 -= dx3 * y3;
            z1 -= dz2 * y3;
            z3 -= dz3 * y3;
            y3 = 0;
        }
        x2 <<= 16;
        if (y2 < 0) {
            x2 -= dx1 * y2;
            z2 -= dz1 * y2;
            y2 = 0;
        }
        if (dx2 < dx3) {
            y1 -= y2;
            y2 -= y3;
            for (y3 = lineOffsets[y3]; --y2 >= 0; y3 += width) {
                drawFlatScanline(pixels, y3, rgb, x1 >> 16, x3 >> 16, z1, z3, depthBuffer);
                x1 += dx2;
                x3 += dx3;
                z1 += dz2;
                z3 += dz3;
            }
            while (--y1 >= 0) {
                drawFlatScanline(pixels, y3, rgb, x2 >> 16, x3 >> 16, z2, z3, depthBuffer);
                x2 += dx1;
                x3 += dx3;
                z2 += dz1;
                z3 += dz3;
                y3 += width;
            }
            return;
        }
        y1 -= y2;
        y2 -= y3;
        for (y3 = lineOffsets[y3]; --y2 >= 0; y3 += width) {
            drawFlatScanline(pixels, y3, rgb, x3 >> 16, x1 >> 16, z3, z1, depthBuffer);
            x1 += dx2;
            x3 += dx3;
            z1 += dz2;
            z3 += dz3;
        }
        while (--y1 >= 0) {
            drawFlatScanline(pixels, y3, rgb, x3 >> 16, x2 >> 16, z3, z2, depthBuffer);
            x2 += dx1;
            x3 += dx3;
            z2 += dz1;
            z3 += dz3;
            y3 += width;
        }
    }

    private static void drawFlatScanline(int[] dest, int offset, int rgb, int x1, int x2, float z1, float z2, float[] depthBuffer) {
        if (x1 >= x2) {
            return;
        }
        z2 = (z2 - z1) / (x2 - x1);
        if (restrict_edges) {
            if (x2 > Rasterizer.endX) {
                x2 = Rasterizer.endX;
            }
            if (x1 < 0) {
                z1 -= x1 * z2;
                x1 = 0;
            }
        }
        if (x1 >= x2) {
            return;
        }
        offset += x1 - 1;
        int n = x2 - x1;
        if (alpha == 0) {
            while (--n >= 0) {
            	if (z1 < depthBuffer[++offset]) {
            		dest[offset] = FogHandler.applyFog(rgb, z1);
            		depthBuffer[offset] = z1;
            	}
                z1 += z2;
            }
        } else {
            while (--n >= 0) {
            	if (z1 < depthBuffer[++offset]) {
                    final int a1 = alpha;
                    final int a2 = 256 - alpha;
            		rgb = ((rgb & 0xff00ff) * a2 >> 8 & 0xff00ff) + ((rgb & 0xff00) * a2 >> 8 & 0xff00);
            		dest[offset] = FogHandler.applyFog(rgb + ((dest[offset] & 0xff00ff) * a1 >> 8 & 0xff00ff) + ((dest[offset] & 0xff00) * a1 >> 8 & 0xff00), z1);
            		depthBuffer[offset] = z1;
            	}
                z1 += z2;
            }
        }
    }

    public static void drawGouraudTriangle(int y1, int y2, int y3, int x1, int x2, int x3, float z1, float z2, float z3, int hsl1, int hsl2, int hsl3) {
        final int rgb1 = hsl2rgb[hsl1];
        final int rgb2 = hsl2rgb[hsl2];
        final int rgb3 = hsl2rgb[hsl3];
        int r1 = rgb1 >> 16 & 0xff;
        int g1 = rgb1 >> 8 & 0xff;
        int b1 = rgb1 & 0xff;
        int r2 = rgb2 >> 16 & 0xff;
        int g2 = rgb2 >> 8 & 0xff;
        int b2 = rgb2 & 0xff;
        int r3 = rgb3 >> 16 & 0xff;
        int g3 = rgb3 >> 8 & 0xff;
        int b3 = rgb3 & 0xff;
        int dx1 = 0;
        float dz1 = 0;
        int dr1 = 0;
        int dg1 = 0;
        int db1 = 0;
        if (y2 != y1) {
            final int d = (y2 - y1);
            dx1 = (x2 - x1 << 16) / d;
            dz1 = (z2 - z1) / d;
            dr1 = (r2 - r1 << 16) / d;
            dg1 = (g2 - g1 << 16) / d;
            db1 = (b2 - b1 << 16) / d;
        }
        int dx2 = 0;
        float dz2 = 0;
        int dr2 = 0;
        int dg2 = 0;
        int db2 = 0;
        if (y3 != y2) {
            final int d = (y3 - y2);
            dx2 = (x3 - x2 << 16) / d;
            dz2 = (z3 - z2) / d;
            dr2 = (r3 - r2 << 16) / d;
            dg2 = (g3 - g2 << 16) / d;
            db2 = (b3 - b2 << 16) / d;
        }
        int dx3 = 0;
        float dz3 = 0;
        int dr3 = 0;
        int dg3 = 0;
        int db3 = 0;
        if (y3 != y1) {
            final int d = (y1 - y3);
            dx3 = (x1 - x3 << 16) / d;
            dz3 = (z1 - z3) / d;
            dr3 = (r1 - r3 << 16) / d;
            dg3 = (g1 - g3 << 16) / d;
            db3 = (b1 - b3 << 16) / d;
        }
        if (y1 <= y2 && y1 <= y3) {
            if (y1 >= bottomY) {
                return;
            }
            if (y2 > bottomY) {
                y2 = bottomY;
            }
            if (y3 > bottomY) {
                y3 = bottomY;
            }
            if (y2 < y3) {
                x3 = x1 <<= 16;
                z3 = z1;
                r3 = r1 <<= 16;
                g3 = g1 <<= 16;
                b3 = b1 <<= 16;
                if (y1 < 0) {
                    x3 -= dx3 * y1;
                    x1 -= dx1 * y1;
                    z3 -= dz3 * y1;
                    z1 -= dz1 * y1;
                    r3 -= dr3 * y1;
                    g3 -= dg3 * y1;
                    b3 -= db3 * y1;
                    r1 -= dr1 * y1;
                    g1 -= dg1 * y1;
                    b1 -= db1 * y1;
                    y1 = 0;
                }
                x2 <<= 16;
                r2 <<= 16;
                g2 <<= 16;
                b2 <<= 16;
                if (y2 < 0) {
                    x2 -= dx2 * y2;
                    z2 -= dz2 * y2;
                    r2 -= dr2 * y2;
                    g2 -= dg2 * y2;
                    b2 -= db2 * y2;
                    y2 = 0;
                }
                if (y1 != y2 && dx3 < dx1 || y1 == y2 && dx3 > dx2) {
                    y3 -= y2;
                    y2 -= y1;
                    for (y1 = lineOffsets[y1]; --y2 >= 0; y1 += width) {
                        drawGouraudScanline(pixels, y1, x3 >> 16, x1 >> 16, z3, z1, r3, g3, b3, r1, g1, b1, DrawingArea.depthBuffer);
                        x3 += dx3;
                        x1 += dx1;
                        z3 += dz3;
                        z1 += dz1;
                        r3 += dr3;
                        g3 += dg3;
                        b3 += db3;
                        r1 += dr1;
                        g1 += dg1;
                        b1 += db1;
                    }
                    while (--y3 >= 0) {
                        drawGouraudScanline(pixels, y1, x3 >> 16, x2 >> 16, z3, z2, r3, g3, b3, r2, g2, b2, DrawingArea.depthBuffer);
                        x3 += dx3;
                        x2 += dx2;
                        z3 += dz3;
                        z2 += dz2;
                        r3 += dr3;
                        g3 += dg3;
                        b3 += db3;
                        r2 += dr2;
                        g2 += dg2;
                        b2 += db2;
                        y1 += width;
                    }
                    return;
                }
                y3 -= y2;
                y2 -= y1;
                for (y1 = lineOffsets[y1]; --y2 >= 0; y1 += width) {
                    drawGouraudScanline(pixels, y1, x1 >> 16, x3 >> 16, z1, z3, r1, g1, b1, r3, g3, b3, DrawingArea.depthBuffer);
                    x3 += dx3;
                    x1 += dx1;
                    z3 += dz3;
                    z1 += dz1;
                    r3 += dr3;
                    g3 += dg3;
                    b3 += db3;
                    r1 += dr1;
                    g1 += dg1;
                    b1 += db1;
                }
                while (--y3 >= 0) {
                    drawGouraudScanline(pixels, y1, x2 >> 16, x3 >> 16, z2, z3, r2, g2, b2, r3, g3, b3, DrawingArea.depthBuffer);
                    x3 += dx3;
                    x2 += dx2;
                    z3 += dz3;
                    z2 += dz2;
                    r3 += dr3;
                    g3 += dg3;
                    b3 += db3;
                    r2 += dr2;
                    g2 += dg2;
                    b2 += db2;
                    y1 += width;
                }
                return;
            }
            x2 = x1 <<= 16;
            z2 = z1;
            r2 = r1 <<= 16;
            g2 = g1 <<= 16;
            b2 = b1 <<= 16;
            if (y1 < 0) {
                x2 -= dx3 * y1;
                x1 -= dx1 * y1;
                z2 -= dz3 * y1;
                z1 -= dz1 * y1;
                r2 -= dr3 * y1;
                g2 -= dg3 * y1;
                b2 -= db3 * y1;
                r1 -= dr1 * y1;
                g1 -= dg1 * y1;
                b1 -= db1 * y1;
                y1 = 0;
            }
            x3 <<= 16;
            r3 <<= 16;
            g3 <<= 16;
            b3 <<= 16;
            if (y3 < 0) {
                x3 -= dx2 * y3;
                z3 -= dz2 * y3;
                r3 -= dr2 * y3;
                g3 -= dg2 * y3;
                b3 -= db2 * y3;
                y3 = 0;
            }
            if (y1 != y3 && dx3 < dx1 || y1 == y3 && dx2 > dx1) {
                y2 -= y3;
                y3 -= y1;
                for (y1 = lineOffsets[y1]; --y3 >= 0; y1 += width) {
                    drawGouraudScanline(pixels, y1, x2 >> 16, x1 >> 16, z2, z1, r2, g2, b2, r1, g1, b1, DrawingArea.depthBuffer);
                    x2 += dx3;
                    x1 += dx1;
                    z2 += dz3;
                    z1 += dz1;
                    r2 += dr3;
                    g2 += dg3;
                    b2 += db3;
                    r1 += dr1;
                    g1 += dg1;
                    b1 += db1;
                }
                while (--y2 >= 0) {
                    drawGouraudScanline(pixels, y1, x3 >> 16, x1 >> 16, z3, z1, r3, g3, b3, r1, g1, b1, DrawingArea.depthBuffer);
                    x3 += dx2;
                    x1 += dx1;
                    z3 += dz2;
                    z1 += dz1;
                    r3 += dr2;
                    g3 += dg2;
                    b3 += db2;
                    r1 += dr1;
                    g1 += dg1;
                    b1 += db1;
                    y1 += width;
                }
                return;
            }
            y2 -= y3;
            y3 -= y1;
            for (y1 = lineOffsets[y1]; --y3 >= 0; y1 += width) {
                drawGouraudScanline(pixels, y1, x1 >> 16, x2 >> 16, z1, z2, r1, g1, b1, r2, g2, b2, DrawingArea.depthBuffer);
                x2 += dx3;
                x1 += dx1;
                z2 += dz3;
                z1 += dz1;
                r2 += dr3;
                g2 += dg3;
                b2 += db3;
                r1 += dr1;
                g1 += dg1;
                b1 += db1;
            }
            while (--y2 >= 0) {
                drawGouraudScanline(pixels, y1, x1 >> 16, x3 >> 16, z1, z3, r1, g1, b1, r3, g3, b3, DrawingArea.depthBuffer);
                x3 += dx2;
                x1 += dx1;
                z3 += dz2;
                z1 += dz1;
                r3 += dr2;
                g3 += dg2;
                b3 += db2;
                r1 += dr1;
                g1 += dg1;
                b1 += db1;
                y1 += width;
            }
            return;
        }
        if (y2 <= y3) {
            if (y2 >= bottomY) {
                return;
            }
            if (y3 > bottomY) {
                y3 = bottomY;
            }
            if (y1 > bottomY) {
                y1 = bottomY;
            }
            if (y3 < y1) {
                x1 = x2 <<= 16;
                z1 = z2;
                r1 = r2 <<= 16;
                g1 = g2 <<= 16;
                b1 = b2 <<= 16;
                if (y2 < 0) {
                    x1 -= dx1 * y2;
                    x2 -= dx2 * y2;
                    z1 -= dz1 * y2;
                    z2 -= dz2 * y2;
                    r1 -= dr1 * y2;
                    g1 -= dg1 * y2;
                    b1 -= db1 * y2;
                    r2 -= dr2 * y2;
                    g2 -= dg2 * y2;
                    b2 -= db2 * y2;
                    y2 = 0;
                }
                x3 <<= 16;
                r3 <<= 16;
                g3 <<= 16;
                b3 <<= 16;
                if (y3 < 0) {
                    x3 -= dx3 * y3;
                    z3 -= dz3 * y3;
                    r3 -= dr3 * y3;
                    g3 -= dg3 * y3;
                    b3 -= db3 * y3;
                    y3 = 0;
                }
                if (y2 != y3 && dx1 < dx2 || y2 == y3 && dx1 > dx3) {
                    y1 -= y3;
                    y3 -= y2;
                    for (y2 = lineOffsets[y2]; --y3 >= 0; y2 += width) {
                        drawGouraudScanline(pixels, y2, x1 >> 16, x2 >> 16, z1, z2, r1, g1, b1, r2, g2, b2, DrawingArea.depthBuffer);
                        x1 += dx1;
                        x2 += dx2;
                        z1 += dz1;
                        z2 += dz2;
                        r1 += dr1;
                        g1 += dg1;
                        b1 += db1;
                        r2 += dr2;
                        g2 += dg2;
                        b2 += db2;
                    }
                    while (--y1 >= 0) {
                        drawGouraudScanline(pixels, y2, x1 >> 16, x3 >> 16, z1, z3, r1, g1, b1, r3, g3, b3, DrawingArea.depthBuffer);
                        x1 += dx1;
                        x3 += dx3;
                        z1 += dz1;
                        z3 += dz3;
                        r1 += dr1;
                        g1 += dg1;
                        b1 += db1;
                        r3 += dr3;
                        g3 += dg3;
                        b3 += db3;
                        y2 += width;
                    }
                    return;
                }
                y1 -= y3;
                y3 -= y2;
                for (y2 = lineOffsets[y2]; --y3 >= 0; y2 += width) {
                    drawGouraudScanline(pixels, y2, x2 >> 16, x1 >> 16, z2, z1, r2, g2, b2, r1, g1, b1, DrawingArea.depthBuffer);
                    x1 += dx1;
                    x2 += dx2;
                    z1 += dz1;
                    z2 += dz2;
                    r1 += dr1;
                    g1 += dg1;
                    b1 += db1;
                    r2 += dr2;
                    g2 += dg2;
                    b2 += db2;
                }
                while (--y1 >= 0) {
                    drawGouraudScanline(pixels, y2, x3 >> 16, x1 >> 16, z3, z1, r3, g3, b3, r1, g1, b1, DrawingArea.depthBuffer);
                    x1 += dx1;
                    x3 += dx3;
                    z1 += dz1;
                    z3 += dz3;
                    r1 += dr1;
                    g1 += dg1;
                    b1 += db1;
                    r3 += dr3;
                    g3 += dg3;
                    b3 += db3;
                    y2 += width;
                }
                return;
            }
            x3 = x2 <<= 16;
            z3 = z2;
            r3 = r2 <<= 16;
            g3 = g2 <<= 16;
            b3 = b2 <<= 16;
            if (y2 < 0) {
                x3 -= dx1 * y2;
                x2 -= dx2 * y2;
                z3 -= dz1 * y2;
                z2 -= dz2 * y2;
                r3 -= dr1 * y2;
                g3 -= dg1 * y2;
                b3 -= db1 * y2;
                r2 -= dr2 * y2;
                g2 -= dg2 * y2;
                b2 -= db2 * y2;
                y2 = 0;
            }
            x1 <<= 16;
            r1 <<= 16;
            g1 <<= 16;
            b1 <<= 16;
            if (y1 < 0) {
                x1 -= dx3 * y1;
                z1 -= dz3 * y1;
                r1 -= dr3 * y1;
                g1 -= dg3 * y1;
                b1 -= db3 * y1;
                y1 = 0;
            }
            if (dx1 < dx2) {
                y3 -= y1;
                y1 -= y2;
                for (y2 = lineOffsets[y2]; --y1 >= 0; y2 += width) {
                    drawGouraudScanline(pixels, y2, x3 >> 16, x2 >> 16, z3, z2, r3, g3, b3, r2, g2, b2, DrawingArea.depthBuffer);
                    x3 += dx1;
                    x2 += dx2;
                    z3 += dz1;
                    z2 += dz2;
                    r3 += dr1;
                    g3 += dg1;
                    b3 += db1;
                    r2 += dr2;
                    g2 += dg2;
                    b2 += db2;
                }
                while (--y3 >= 0) {
                    drawGouraudScanline(pixels, y2, x1 >> 16, x2 >> 16, z1, z2, r1, g1, b1, r2, g2, b2, DrawingArea.depthBuffer);
                    x1 += dx3;
                    x2 += dx2;
                    z1 += dz3;
                    z2 += dz2;
                    r1 += dr3;
                    g1 += dg3;
                    b1 += db3;
                    r2 += dr2;
                    g2 += dg2;
                    b2 += db2;
                    y2 += width;
                }
                return;
            }
            y3 -= y1;
            y1 -= y2;
            for (y2 = lineOffsets[y2]; --y1 >= 0; y2 += width) {
                drawGouraudScanline(pixels, y2, x2 >> 16, x3 >> 16, z2, z3, r2, g2, b2, r3, g3, b3, DrawingArea.depthBuffer);
                x3 += dx1;
                x2 += dx2;
                z3 += dz1;
                z2 += dz2;
                r3 += dr1;
                g3 += dg1;
                b3 += db1;
                r2 += dr2;
                g2 += dg2;
                b2 += db2;
            }
            while (--y3 >= 0) {
                drawGouraudScanline(pixels, y2, x2 >> 16, x1 >> 16, z2, z1, r2, g2, b2, r1, g1, b1, DrawingArea.depthBuffer);
                x1 += dx3;
                x2 += dx2;
                z1 += dz3;
                z2 += dz2;
                r1 += dr3;
                g1 += dg3;
                b1 += db3;
                r2 += dr2;
                g2 += dg2;
                b2 += db2;
                y2 += width;
            }
            return;
        }
        if (y3 >= bottomY) {
            return;
        }
        if (y1 > bottomY) {
            y1 = bottomY;
        }
        if (y2 > bottomY) {
            y2 = bottomY;
        }
        if (y1 < y2) {
            x2 = x3 <<= 16;
            z2 = z3;
            r2 = r3 <<= 16;
            g2 = g3 <<= 16;
            b2 = b3 <<= 16;
            if (y3 < 0) {
                x2 -= dx2 * y3;
                x3 -= dx3 * y3;
                z2 -= dz2 * y3;
                z3 -= dz3 * y3;
                r2 -= dr2 * y3;
                g2 -= dg2 * y3;
                b2 -= db2 * y3;
                r3 -= dr3 * y3;
                g3 -= dg3 * y3;
                b3 -= db3 * y3;
                y3 = 0;
            }
            x1 <<= 16;
            r1 <<= 16;
            g1 <<= 16;
            b1 <<= 16;
            if (y1 < 0) {
                x1 -= dx1 * y1;
                z1 -= dz1 * y1;
                r1 -= dr1 * y1;
                g1 -= dg1 * y1;
                b1 -= db1 * y1;
                y1 = 0;
            }
            if (dx2 < dx3) {
                y2 -= y1;
                y1 -= y3;
                for (y3 = lineOffsets[y3]; --y1 >= 0; y3 += width) {
                    drawGouraudScanline(pixels, y3, x2 >> 16, x3 >> 16, z2, z3, r2, g2, b2, r3, g3, b3, DrawingArea.depthBuffer);
                    x2 += dx2;
                    x3 += dx3;
                    z2 += dz2;
                    z3 += dz3;
                    r2 += dr2;
                    g2 += dg2;
                    b2 += db2;
                    r3 += dr3;
                    g3 += dg3;
                    b3 += db3;
                }
                while (--y2 >= 0) {
                    drawGouraudScanline(pixels, y3, x2 >> 16, x1 >> 16, z2, z1, r2, g2, b2, r1, g1, b1, DrawingArea.depthBuffer);
                    x2 += dx2;
                    x1 += dx1;
                    z2 += dz2;
                    z1 += dz1;
                    r2 += dr2;
                    g2 += dg2;
                    b2 += db2;
                    r1 += dr1;
                    g1 += dg1;
                    b1 += db1;
                    y3 += width;
                }
                return;
            }
            y2 -= y1;
            y1 -= y3;
            for (y3 = lineOffsets[y3]; --y1 >= 0; y3 += width) {
                drawGouraudScanline(pixels, y3, x3 >> 16, x2 >> 16, z3, z2, r3, g3, b3, r2, g2, b2, DrawingArea.depthBuffer);
                x2 += dx2;
                x3 += dx3;
                z2 += dz2;
                z3 += dz3;
                r2 += dr2;
                g2 += dg2;
                b2 += db2;
                r3 += dr3;
                g3 += dg3;
                b3 += db3;
            }
            while (--y2 >= 0) {
                drawGouraudScanline(pixels, y3, x1 >> 16, x2 >> 16, z1, z2, r1, g1, b1, r2, g2, b2, DrawingArea.depthBuffer);
                x2 += dx2;
                x1 += dx1;
                z2 += dz2;
                z1 += dz1;
                r2 += dr2;
                g2 += dg2;
                b2 += db2;
                r1 += dr1;
                g1 += dg1;
                b1 += db1;
                y3 += width;
            }
            return;
        }
        x1 = x3 <<= 16;
        z1 = z3;
        r1 = r3 <<= 16;
        g1 = g3 <<= 16;
        b1 = b3 <<= 16;
        if (y3 < 0) {
            x1 -= dx2 * y3;
            x3 -= dx3 * y3;
            z1 -= dz2 * y3;
            z3 -= dz3 * y3;
            r1 -= dr2 * y3;
            g1 -= dg2 * y3;
            b1 -= db2 * y3;
            r3 -= dr3 * y3;
            g3 -= dg3 * y3;
            b3 -= db3 * y3;
            y3 = 0;
        }
        x2 <<= 16;
        r2 <<= 16;
        g2 <<= 16;
        b2 <<= 16;
        if (y2 < 0) {
            x2 -= dx1 * y2;
            z2 -= dz1 * y2;
            r2 -= dr1 * y2;
            g2 -= dg1 * y2;
            b2 -= db1 * y2;
            y2 = 0;
        }
        if (dx2 < dx3) {
            y1 -= y2;
            y2 -= y3;
            for (y3 = lineOffsets[y3]; --y2 >= 0; y3 += width) {
                drawGouraudScanline(pixels, y3, x1 >> 16, x3 >> 16, z1, z3, r1, g1, b1, r3, g3, b3, DrawingArea.depthBuffer);
                x1 += dx2;
                x3 += dx3;
                z1 += dz2;
                z3 += dz3;
                r1 += dr2;
                g1 += dg2;
                b1 += db2;
                r3 += dr3;
                g3 += dg3;
                b3 += db3;
            }
            while (--y1 >= 0) {
                drawGouraudScanline(pixels, y3, x2 >> 16, x3 >> 16, z2, z3, r2, g2, b2, r3, g3, b3, DrawingArea.depthBuffer);
                x2 += dx1;
                x3 += dx3;
                z2 += dz1;
                z3 += dz3;
                r2 += dr1;
                g2 += dg1;
                b2 += db1;
                r3 += dr3;
                g3 += dg3;
                b3 += db3;
                y3 += width;
            }
            return;
        }
        y1 -= y2;
        y2 -= y3;
        for (y3 = lineOffsets[y3]; --y2 >= 0; y3 += width) {
            drawGouraudScanline(pixels, y3, x3 >> 16, x1 >> 16, z3, z1, r3, g3, b3, r1, g1, b1, DrawingArea.depthBuffer);
            x1 += dx2;
            x3 += dx3;
            z1 += dz2;
            z3 += dz3;
            r1 += dr2;
            g1 += dg2;
            b1 += db2;
            r3 += dr3;
            g3 += dg3;
            b3 += db3;
        }
        while (--y1 >= 0) {
            drawGouraudScanline(pixels, y3, x3 >> 16, x2 >> 16, z3, z2, r3, g3, b3, r2, g2, b2, DrawingArea.depthBuffer);
            x2 += dx1;
            x3 += dx3;
            z2 += dz1;
            z3 += dz3;
            r2 += dr1;
            g2 += dg1;
            b2 += db1;
            r3 += dr3;
            g3 += dg3;
            b3 += db3;
            y3 += width;
        }
    }

    public static void drawGouraudScanline(int[] dest, int offset, int x1, int x2, float z1, float z2, int r1, int g1, int b1, int r2, int g2, int b2, float[] depthBuffer) {
        int n = x2 - x1;
        if (n <= 0) {
            return;
        }
        z2 = (z2 - z1) / n;
        r2 = (r2 - r1) / n;
        g2 = (g2 - g1) / n;
        b2 = (b2 - b1) / n;
        if (restrict_edges) {
            if (x2 > Rasterizer.endX) {
                n -= x2 - Rasterizer.endX;
                x2 = Rasterizer.endX;
            }
            if (x1 < 0) {
                n = x2;
                z1 -= x1 * z2;
                r1 -= x1 * r2;
                g1 -= x1 * g2;
                b1 -= x1 * b2;
                x1 = 0;
            }
        }
        if (x1 < x2) {
            offset += x1 - 1;
            if (alpha == 0) {
                while (--n >= 0) {
                    if (z1 < depthBuffer[++offset]) {
                    	dest[offset] = FogHandler.applyFog((r1 & 0xff0000) | (g1 >> 8 & 0xff00) | (b1 >> 16 & 0xff), z1);
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    r1 += r2;
                    g1 += g2;
                    b1 += b2;
                }
            } else {
                int rgb;
                int dst;
                final int a1 = alpha;
                final int a2 = 256 - alpha;
                while (--n >= 0) {
                    if (z1 < depthBuffer[++offset]) {
	                    rgb = (r1 & 0xff0000) | (g1 >> 8 & 0xff00) | (b1 >> 16 & 0xff);
	                    rgb = ((rgb & 0xff00ff) * a2 >> 8 & 0xff00ff) + ((rgb & 0xff00) * a2 >> 8 & 0xff00);
	                    if (rgb != 0) {
		                    dst = dest[offset];
		                    dest[offset] = FogHandler.applyFog(rgb + ((dst & 0xff00ff) * a1 >> 8 & 0xff00ff) + ((dst & 0xff00) * a1 >> 8 & 0xff00), z1);
		                    depthBuffer[offset] = z1;
	                    }
                    }
                    z1 += z2;
                    r1 += r2;
                    g1 += g2;
                    b1 += b2;
                }
            }
        }
    }

    public static void drawTexturedTriangle_model(int y_a, int y_b, int y_c,
                                                  int x_a, int x_b, int x_c, float z_a, float z_b, float z_c,
                                                  int grad_a, int grad_b, int grad_c, int Px, int Mx, int Nx, int Pz, int My,
                                                  int Nz, int Py, int Mz, int Ny, int t_id, boolean force) {
        drawTexturedTriangle_model(y_a, y_b, y_c, x_a, x_b, x_c, z_a,
                z_b, z_c, grad_a, grad_b, grad_c, Px, Mx, Nx, Pz, My, Nz, Py,
                Mz, Ny, t_id, force, false);
    }

    public static void drawTexturedTriangle_model(int y_a, int y_b, int y_c,
                                                  int x_a, int x_b, int x_c, float z_a, float z_b, float z_c,
                                                  int grad_a, int grad_b, int grad_c, int Px, int Mx, int Nx, int Pz, int My,
                                                  int Nz, int Py, int Mz, int Ny, int t_id, boolean force, boolean floor) {
        drawTexturedTriangle(y_a, y_b, y_c,
                x_a, x_b, x_c, z_a, z_b, z_c,
                grad_a, grad_b, grad_c, Px, Mx, Nx, Pz, My,
                Nz, Py, Mz, Ny, t_id, force, true);
    }


    public static void drawTexturedTriangle(int y_a, int y_b, int y_c,
                                            int x_a, int x_b, int x_c, float z_a, float z_b, float z_c,
                                            int grad_a, int grad_b, int grad_c, int Px, int Mx, int Nx, int Pz, int My,
                                            int Nz, int Py, int Mz, int Ny, int t_id,
                                            boolean force, boolean floor) {
		
        if (t_id < 0 || t_id >= TextureDef.textures.length) {
            drawGouraudTriangle(y_a, y_b, y_c, x_a, x_b, x_c, z_a, z_b,
                    z_c, grad_a, grad_b, grad_c);
            return;
        }

        TextureDef def = TextureDef.textures[t_id];
        if (def == null) {
            notTextured = true;
            drawGouraudTriangle(y_a, y_b, y_c, x_a, x_b, x_c, z_a, z_b,
                    z_c, grad_a, grad_b, grad_c);
            return;
        }

        if (!force && !def.aBoolean1223) {
            drawGouraudTriangle(y_a, y_b, y_c, x_a, x_b, x_c, z_a, z_b,
                    z_c, grad_a, grad_b, grad_c);
            return;
        }

        int texture[] = textureManager.getTexturePixels(t_id);
        if (texture == null) {
            notTextured = true;
            drawGouraudTriangle(y_a, y_b, y_c, x_a, x_b, x_c, z_a, z_b,
                    z_c, grad_a, grad_b, grad_c);
            return;
        }

        Mx = Px - Mx;
        My = Pz - My;
        Mz = Py - Mz;
        Nx -= Px;
        Nz -= Pz;
        Ny -= Py;
        int Oa = Nx * Pz - Nz * Px << 14;
        int Ha = (int) (((long) (Nz * Py - Ny * Pz) << 3 << 14) / (long) Client.gameAreaWidth);
        int Va = (int) (((long) (Ny * Px - Nx * Py) << 14) / (long) Client.gameAreaWidth);
        int Ob = Mx * Pz - My * Px << 14;
        int Hb = (int) (((long) (My * Py - Mz * Pz) << 3 << 14) / (long) Client.gameAreaWidth);
        int Vb = (int) (((long) (Mz * Px - Mx * Py) << 14) / (long) Client.gameAreaWidth);
        int Oc = My * Nx - Mx * Nz << 14;
        int Hc = (int) (((long) (Mz * Nz - My * Ny) << 3 << 14) / (long) Client.gameAreaWidth);
        int Vc = (int) (((long) (Mx * Ny - Mz * Nx) << 14) / (long) Client.gameAreaWidth);

        int x_a_off = 0;
        float z_a_off = 0;
        int grad_a_off = 0;

        int col_a = grad_a;
        int col_b = grad_b;
        int col_c = grad_c;
        int col_a_off = 0;
        int col_b_off = 0;
        int col_c_off = 0;

        if (y_b != y_a) {
            x_a_off = (x_b - x_a << 16) / (y_b - y_a);
            z_a_off = (z_b - z_a) / (y_b - y_a);
            grad_a_off = (grad_b - grad_a << 16) / (y_b - y_a);
            col_a_off = (col_b - col_a << 15) / (y_b - y_a);
        }
        int x_b_off = 0;
        float z_b_off = 0;
        int grad_b_off = 0;
        if (y_c != y_b) {
            x_b_off = (x_c - x_b << 16) / (y_c - y_b);
            z_b_off = (z_c - z_b) / (y_c - y_b);
            grad_b_off = (grad_c - grad_b << 16) / (y_c - y_b);
            col_b_off = (col_c - col_b << 15) / (y_c - y_b);
        }
        int x_c_off = 0;
        float z_c_off = 0;
        int grad_c_off = 0;
        if (y_c != y_a) {
            x_c_off = (x_a - x_c << 16) / (y_a - y_c);
            z_c_off = (z_a - z_c) / (y_a - y_c);
            grad_c_off = (grad_a - grad_c << 16) / (y_a - y_c);
            col_c_off = (col_a - col_c << 15) / (y_a - y_c);
        }
        if (y_a <= y_b && y_a <= y_c) {
            if (y_a >= bottomY)
                return;
            if (y_b > bottomY)
                y_b = bottomY;
            if (y_c > bottomY)
                y_c = bottomY;
            if (y_b < y_c) {
                x_c = x_a <<= 16;
                z_c = z_a;
                grad_c = grad_a <<= 16;
                col_c = col_a <<= 15;
                if (y_a < 0) {
                    x_c -= x_c_off * y_a;
                    x_a -= x_a_off * y_a;
                    z_c -= z_c_off * y_a;
                    z_a -= z_a_off * y_a;
                    grad_c -= grad_c_off * y_a;
                    grad_a -= grad_a_off * y_a;
                    col_c -= col_c_off * y_a;
                    col_a -= col_a_off * y_a;
                    y_a = 0;
                }
                x_b <<= 16;
                grad_b <<= 16;
                col_b <<= 15;
                if (y_b < 0) {
                    x_b -= x_b_off * y_b;
                    z_b -= z_b_off * y_b;
                    grad_b -= grad_b_off * y_b;
                    col_b -= col_b_off * y_b;
                    y_b = 0;
                }
                int jA = y_a - centerY;
                Oa += Va * jA;
                Ob += Vb * jA;
                Oc += Vc * jA;
                if (y_a != y_b && x_c_off < x_a_off || y_a == y_b
                        && x_c_off > x_b_off) {
                    y_c -= y_b;
                    y_b -= y_a;
                    y_a = lineOffsets[y_a];
                    while (--y_b >= 0) {
                        drawTexturedLine_floor(pixels, texture, y_a, x_c >> 16,
                                x_a >> 16, grad_c >> 8, grad_a >> 8,
                                col_c >> 7, col_a >> 7, Oa, Ob, Oc, Ha, Hb, Hc,
                                floor, z_c, z_a, DrawingArea.depthBuffer);
                        x_c += x_c_off;
                        x_a += x_a_off;
                        z_c += z_c_off;
                        z_a += z_a_off;
                        grad_c += grad_c_off;
                        grad_a += grad_a_off;
                        col_c += col_c_off;
                        col_a += col_a_off;
                        y_a += width;
                        Oa += Va;
                        Ob += Vb;
                        Oc += Vc;
                    }
                    while (--y_c >= 0) {
                        drawTexturedLine_floor(pixels, texture, y_a, x_c >> 16,
                                x_b >> 16, grad_c >> 8, grad_b >> 8,
                                col_c >> 7, col_b >> 7, Oa, Ob, Oc, Ha, Hb, Hc,
                                floor, z_c, z_b, DrawingArea.depthBuffer);
                        x_c += x_c_off;
                        x_b += x_b_off;
                        z_c += z_c_off;
                        z_b += z_b_off;
                        grad_c += grad_c_off;
                        grad_b += grad_b_off;
                        col_c += col_c_off;
                        col_b += col_b_off;
                        y_a += width;
                        Oa += Va;
                        Ob += Vb;
                        Oc += Vc;
                    }
                    return;
                }
                y_c -= y_b;
                y_b -= y_a;
                y_a = lineOffsets[y_a];
                while (--y_b >= 0) {
                    drawTexturedLine_floor(pixels, texture, y_a, x_a >> 16,
                            x_c >> 16, grad_a >> 8, grad_c >> 8, col_a >> 7,
                            col_c >> 7, Oa, Ob, Oc, Ha, Hb, Hc, floor, z_a,
                            z_c, DrawingArea.depthBuffer);
                    x_c += x_c_off;
                    x_a += x_a_off;
                    z_c += z_c_off;
                    z_a += z_a_off;
                    grad_c += grad_c_off;
                    grad_a += grad_a_off;
                    col_c += col_c_off;
                    col_a += col_a_off;
                    y_a += width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                while (--y_c >= 0) {
                    drawTexturedLine_floor(pixels, texture, y_a, x_b >> 16,
                            x_c >> 16, grad_b >> 8, grad_c >> 8, col_b >> 7,
                            col_c >> 7, Oa, Ob, Oc, Ha, Hb, Hc, floor, z_b,
                            z_c, DrawingArea.depthBuffer);
                    x_c += x_c_off;
                    x_b += x_b_off;
                    z_c += z_c_off;
                    z_b += z_b_off;
                    grad_c += grad_c_off;
                    grad_b += grad_b_off;
                    col_c += col_c_off;
                    col_b += col_b_off;
                    y_a += width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                return;
            }
            x_b = x_a <<= 16;
            z_b = z_a;
            grad_b = grad_a <<= 16;
            col_b = col_a <<= 15;
            if (y_a < 0) {
                x_b -= x_c_off * y_a;
                x_a -= x_a_off * y_a;
                z_b -= z_c_off * y_a;
                z_a -= z_a_off * y_a;
                grad_b -= grad_c_off * y_a;
                grad_a -= grad_a_off * y_a;
                col_b -= col_c_off * y_a;
                col_a -= col_a_off * y_a;
                y_a = 0;
            }
            x_c <<= 16;
            grad_c <<= 16;
            col_c <<= 15;
            if (y_c < 0) {
                x_c -= x_b_off * y_c;
                z_c -= z_b_off * y_c;
                grad_c -= grad_b_off * y_c;
                col_c -= col_b_off * y_c;
                y_c = 0;
            }
            int l8 = y_a - centerY;
            Oa += Va * l8;
            Ob += Vb * l8;
            Oc += Vc * l8;
            if (y_a != y_c && x_c_off < x_a_off || y_a == y_c
                    && x_b_off > x_a_off) {
                y_b -= y_c;
                y_c -= y_a;
                y_a = lineOffsets[y_a];
                while (--y_c >= 0) {
                    drawTexturedLine_floor(pixels, texture, y_a, x_b >> 16,
                            x_a >> 16, grad_b >> 8, grad_a >> 8, col_b >> 7,
                            col_a >> 7, Oa, Ob, Oc, Ha, Hb, Hc, floor, z_b,
                            z_a, DrawingArea.depthBuffer);
                    x_b += x_c_off;
                    x_a += x_a_off;
                    z_b += z_c_off;
                    z_a += z_a_off;
                    grad_b += grad_c_off;
                    grad_a += grad_a_off;
                    col_b += col_c_off;
                    col_a += col_a_off;
                    y_a += width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                while (--y_b >= 0) {
                    drawTexturedLine_floor(pixels, texture, y_a, x_c >> 16,
                            x_a >> 16, grad_c >> 8, grad_a >> 8, col_c >> 7,
                            col_a >> 7, Oa, Ob, Oc, Ha, Hb, Hc, floor, z_c,
                            z_a, DrawingArea.depthBuffer);
                    x_c += x_b_off;
                    x_a += x_a_off;
                    z_c += z_b_off;
                    z_a += z_a_off;
                    grad_c += grad_b_off;
                    grad_a += grad_a_off;
                    col_c += col_b_off;
                    col_a += col_a_off;
                    y_a += width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                return;
            }
            y_b -= y_c;
            y_c -= y_a;
            y_a = lineOffsets[y_a];
            while (--y_c >= 0) {
                drawTexturedLine_floor(pixels, texture, y_a, x_a >> 16,
                        x_b >> 16, grad_a >> 8, grad_b >> 8, col_a >> 7,
                        col_b >> 7, Oa, Ob, Oc, Ha, Hb, Hc, floor, z_a, z_b, DrawingArea.depthBuffer);
                x_b += x_c_off;
                x_a += x_a_off;
                z_b += z_c_off;
                z_a += z_a_off;
                grad_b += grad_c_off;
                grad_a += grad_a_off;
                col_b += col_c_off;
                col_a += col_a_off;
                y_a += width;
                Oa += Va;
                Ob += Vb;
                Oc += Vc;
            }
            while (--y_b >= 0) {
                drawTexturedLine_floor(pixels, texture, y_a, x_a >> 16,
                        x_c >> 16, grad_a >> 8, grad_c >> 8, col_a >> 7,
                        col_c >> 7, Oa, Ob, Oc, Ha, Hb, Hc, floor, z_a, z_c, DrawingArea.depthBuffer);
                x_c += x_b_off;
                x_a += x_a_off;
                z_c += z_b_off;
                z_a += z_a_off;
                grad_c += grad_b_off;
                grad_a += grad_a_off;
                col_c += col_b_off;
                col_a += col_a_off;
                y_a += width;
                Oa += Va;
                Ob += Vb;
                Oc += Vc;
            }
            return;
        }
        if (y_b <= y_c) {
            if (y_b >= bottomY)
                return;
            if (y_c > bottomY)
                y_c = bottomY;
            if (y_a > bottomY)
                y_a = bottomY;
            if (y_c < y_a) {
                x_a = x_b <<= 16;
                z_a = z_b;
                grad_a = grad_b <<= 16;
                col_a = col_b <<= 15;
                if (y_b < 0) {
                    x_a -= x_a_off * y_b;
                    x_b -= x_b_off * y_b;
                    z_a -= z_a_off * y_b;
                    z_b -= z_b_off * y_b;
                    grad_a -= grad_a_off * y_b;
                    grad_b -= grad_b_off * y_b;
                    col_a -= col_a_off * y_b;
                    col_b -= col_b_off * y_b;
                    y_b = 0;
                }
                x_c <<= 16;
                grad_c <<= 16;
                col_c <<= 15;
                if (y_c < 0) {
                    x_c -= x_c_off * y_c;
                    z_c -= z_c_off * y_c;
                    grad_c -= grad_c_off * y_c;
                    col_c -= col_c_off * y_c;
                    y_c = 0;
                }
                int i9 = y_b - centerY;
                Oa += Va * i9;
                Ob += Vb * i9;
                Oc += Vc * i9;
                if (y_b != y_c && x_a_off < x_b_off || y_b == y_c
                        && x_a_off > x_c_off) {
                    y_a -= y_c;
                    y_c -= y_b;
                    y_b = lineOffsets[y_b];
                    while (--y_c >= 0) {
                        drawTexturedLine_floor(pixels, texture, y_b, x_a >> 16,
                                x_b >> 16, grad_a >> 8, grad_b >> 8,
                                col_a >> 7, col_b >> 7, Oa, Ob, Oc, Ha, Hb, Hc,
                                floor, z_a, z_b, DrawingArea.depthBuffer);
                        x_a += x_a_off;
                        x_b += x_b_off;
                        z_a += z_a_off;
                        z_b += z_b_off;
                        grad_a += grad_a_off;
                        grad_b += grad_b_off;
                        col_a += col_a_off;
                        col_b += col_b_off;
                        y_b += width;
                        Oa += Va;
                        Ob += Vb;
                        Oc += Vc;
                    }
                    while (--y_a >= 0) {
                        drawTexturedLine_floor(pixels, texture, y_b, x_a >> 16,
                                x_c >> 16, grad_a >> 8, grad_c >> 8,
                                col_a >> 7, col_c >> 7, Oa, Ob, Oc, Ha, Hb, Hc,
                                floor, z_a, z_c, DrawingArea.depthBuffer);
                        x_a += x_a_off;
                        x_c += x_c_off;
                        z_a += z_a_off;
                        z_c += z_c_off;
                        grad_a += grad_a_off;
                        grad_c += grad_c_off;
                        col_a += col_a_off;
                        col_c += col_c_off;
                        y_b += width;
                        Oa += Va;
                        Ob += Vb;
                        Oc += Vc;
                    }
                    return;
                }
                y_a -= y_c;
                y_c -= y_b;
                y_b = lineOffsets[y_b];
                while (--y_c >= 0) {
                    drawTexturedLine_floor(pixels, texture, y_b, x_b >> 16,
                            x_a >> 16, grad_b >> 8, grad_a >> 8, col_b >> 7,
                            col_a >> 7, Oa, Ob, Oc, Ha, Hb, Hc, floor, z_b,
                            z_a, DrawingArea.depthBuffer);
                    x_a += x_a_off;
                    x_b += x_b_off;
                    z_a += z_a_off;
                    z_b += z_b_off;
                    grad_a += grad_a_off;
                    grad_b += grad_b_off;
                    col_a += col_a_off;
                    col_b += col_b_off;
                    y_b += width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                while (--y_a >= 0) {
                    drawTexturedLine_floor(pixels, texture, y_b, x_c >> 16,
                            x_a >> 16, grad_c >> 8, grad_a >> 8, col_c >> 7,
                            col_a >> 7, Oa, Ob, Oc, Ha, Hb, Hc, floor, z_c,
                            z_a, DrawingArea.depthBuffer);
                    x_a += x_a_off;
                    x_c += x_c_off;
                    z_a += z_a_off;
                    z_c += z_c_off;
                    grad_a += grad_a_off;
                    grad_c += grad_c_off;
                    col_a += col_a_off;
                    col_c += col_c_off;
                    y_b += width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                return;
            }
            x_c = x_b <<= 16;
            z_c = z_b;
            grad_c = grad_b <<= 16;
            col_c = col_b <<= 15;
            if (y_b < 0) {
                x_c -= x_a_off * y_b;
                x_b -= x_b_off * y_b;
                z_c -= z_a_off * y_b;
                z_b -= z_b_off * y_b;
                grad_c -= grad_a_off * y_b;
                grad_b -= grad_b_off * y_b;
                col_c -= col_a_off * y_b;
                col_b -= col_b_off * y_b;
                y_b = 0;
            }
            x_a <<= 16;
            grad_a <<= 16;
            col_a <<= 15;
            if (y_a < 0) {
                x_a -= x_c_off * y_a;
                grad_a -= grad_c_off * y_a;
                col_a -= col_c_off * y_a;
                y_a = 0;
            }
            int j9 = y_b - centerY;
            Oa += Va * j9;
            Ob += Vb * j9;
            Oc += Vc * j9;
            if (x_a_off < x_b_off) {
                y_c -= y_a;
                y_a -= y_b;
                y_b = lineOffsets[y_b];
                while (--y_a >= 0) {
                    drawTexturedLine_floor(pixels, texture, y_b, x_c >> 16,
                            x_b >> 16, grad_c >> 8, grad_b >> 8, col_c >> 7,
                            col_b >> 7, Oa, Ob, Oc, Ha, Hb, Hc, floor, z_c,
                            z_b, DrawingArea.depthBuffer);
                    x_c += x_a_off;
                    x_b += x_b_off;
                    z_c += x_a_off;
                    z_b += x_b_off;
                    grad_c += grad_a_off;
                    grad_b += grad_b_off;
                    col_c += col_a_off;
                    col_b += col_b_off;
                    y_b += width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                while (--y_c >= 0) {
                    drawTexturedLine_floor(pixels, texture, y_b, x_a >> 16,
                            x_b >> 16, grad_a >> 8, grad_b >> 8, col_a >> 7,
                            col_b >> 7, Oa, Ob, Oc, Ha, Hb, Hc, floor, z_a,
                            z_b, DrawingArea.depthBuffer);
                    x_a += x_c_off;
                    x_b += x_b_off;
                    z_a += z_c_off;
                    z_b += z_b_off;
                    grad_a += grad_c_off;
                    grad_b += grad_b_off;
                    col_a += col_c_off;
                    col_b += col_b_off;
                    y_b += width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                return;
            }
            y_c -= y_a;
            y_a -= y_b;
            y_b = lineOffsets[y_b];
            while (--y_a >= 0) {
                drawTexturedLine_floor(pixels, texture, y_b, x_b >> 16,
                        x_c >> 16, grad_b >> 8, grad_c >> 8, col_b >> 7,
                        col_c >> 7, Oa, Ob, Oc, Ha, Hb, Hc, floor, z_b, z_c, DrawingArea.depthBuffer);
                x_c += x_a_off;
                x_b += x_b_off;
                z_c += z_a_off;
                z_b += z_b_off;
                grad_c += grad_a_off;
                grad_b += grad_b_off;
                col_c += col_a_off;
                col_b += col_b_off;
                y_b += width;
                Oa += Va;
                Ob += Vb;
                Oc += Vc;
            }
            while (--y_c >= 0) {
                drawTexturedLine_floor(pixels, texture, y_b, x_b >> 16,
                        x_a >> 16, grad_b >> 8, grad_a >> 8, col_b >> 7,
                        col_a >> 7, Oa, Ob, Oc, Ha, Hb, Hc, floor, z_b, z_a, DrawingArea.depthBuffer);
                x_a += x_c_off;
                x_b += x_b_off;
                z_a += z_c_off;
                z_b += z_b_off;
                grad_a += grad_c_off;
                grad_b += grad_b_off;
                col_a += col_c_off;
                col_b += col_b_off;
                y_b += width;
                Oa += Va;
                Ob += Vb;
                Oc += Vc;
            }
            return;
        }
        if (y_c >= bottomY)
            return;
        if (y_a > bottomY)
            y_a = bottomY;
        if (y_b > bottomY)
            y_b = bottomY;
        if (y_a < y_b) {
            x_b = x_c <<= 16;
            z_b = z_c;
            grad_b = grad_c <<= 16;
            col_b = col_c <<= 15;
            if (y_c < 0) {
                x_b -= x_b_off * y_c;
                x_c -= x_c_off * y_c;
                z_b -= z_b_off * y_c;
                z_c -= z_c_off * y_c;
                grad_b -= grad_b_off * y_c;
                grad_c -= grad_c_off * y_c;
                col_b -= col_b_off * y_c;
                col_c -= col_c_off * y_c;
                y_c = 0;
            }
            x_a <<= 16;
            grad_a <<= 16;
            col_a <<= 15;
            if (y_a < 0) {
                x_a -= x_a_off * y_a;
                z_a -= z_a_off * y_a;
                grad_a -= grad_a_off * y_a;
                col_a -= col_a_off * y_a;
                y_a = 0;
            }
            int k9 = y_c - centerY;
            Oa += Va * k9;
            Ob += Vb * k9;
            Oc += Vc * k9;
            if (x_b_off < x_c_off) {
                y_b -= y_a;
                y_a -= y_c;
                y_c = lineOffsets[y_c];
                while (--y_a >= 0) {
                    drawTexturedLine_floor(pixels, texture, y_c, x_b >> 16,
                            x_c >> 16, grad_b >> 8, grad_c >> 8, col_b >> 7,
                            col_c >> 7, Oa, Ob, Oc, Ha, Hb, Hc, floor, z_b,
                            z_c, DrawingArea.depthBuffer);
                    x_b += x_b_off;
                    x_c += x_c_off;
                    z_b += z_b_off;
                    z_c += z_c_off;
                    grad_b += grad_b_off;
                    grad_c += grad_c_off;
                    col_b += col_b_off;
                    col_c += col_c_off;
                    y_c += width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                while (--y_b >= 0) {
                    drawTexturedLine_floor(pixels, texture, y_c, x_b >> 16,
                            x_a >> 16, grad_b >> 8, grad_a >> 8, col_b >> 7,
                            col_a >> 7, Oa, Ob, Oc, Ha, Hb, Hc, floor, z_b,
                            z_a, DrawingArea.depthBuffer);
                    x_b += x_b_off;
                    x_a += x_a_off;
                    z_b += z_b_off;
                    z_a += z_a_off;
                    grad_b += grad_b_off;
                    grad_a += grad_a_off;
                    col_b += col_b_off;
                    col_a += col_a_off;
                    y_c += width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                return;
            }
            y_b -= y_a;
            y_a -= y_c;
            y_c = lineOffsets[y_c];
            while (--y_a >= 0) {
                drawTexturedLine_floor(pixels, texture, y_c, x_c >> 16,
                        x_b >> 16, grad_c >> 8, grad_b >> 8, col_c >> 7,
                        col_b >> 7, Oa, Ob, Oc, Ha, Hb, Hc, floor, z_c, z_b, DrawingArea.depthBuffer);
                x_b += x_b_off;
                x_c += x_c_off;
                z_b += z_b_off;
                z_c += z_c_off;
                grad_b += grad_b_off;
                grad_c += grad_c_off;
                col_b += col_b_off;
                col_c += col_c_off;
                y_c += width;
                Oa += Va;
                Ob += Vb;
                Oc += Vc;
            }
            while (--y_b >= 0) {
                drawTexturedLine_floor(pixels, texture, y_c, x_a >> 16,
                        x_b >> 16, grad_a >> 8, grad_b >> 8, col_a >> 7,
                        col_b >> 7, Oa, Ob, Oc, Ha, Hb, Hc, floor, z_a, z_b, DrawingArea.depthBuffer);
                x_b += x_b_off;
                x_a += x_a_off;
                z_b += z_b_off;
                z_a += z_a_off;
                grad_b += grad_b_off;
                grad_a += grad_a_off;
                col_b += col_b_off;
                col_a += col_a_off;
                y_c += width;
                Oa += Va;
                Ob += Vb;
                Oc += Vc;
            }
            return;
        }
        x_a = x_c <<= 16;
        z_a = z_c;
        grad_a = grad_c <<= 16;
        col_a = col_c <<= 15;
        if (y_c < 0) {
            x_a -= x_b_off * y_c;
            x_c -= x_c_off * y_c;
            z_a -= z_b_off * y_c;
            z_c -= z_c_off * y_c;
            grad_a -= grad_b_off * y_c;
            grad_c -= grad_c_off * y_c;
            col_a -= col_b_off * y_c;
            col_c -= col_c_off * y_c;
            y_c = 0;
        }
        x_b <<= 16;
        grad_b <<= 16;
        col_b <<= 15;
        if (y_b < 0) {
            x_b -= x_a_off * y_b;
            z_b -= z_a_off * y_b;
            grad_b -= grad_a_off * y_b;
            col_b -= col_a_off * y_b;
            y_b = 0;
        }
        int l9 = y_c - centerY;
        Oa += Va * l9;
        Ob += Vb * l9;
        Oc += Vc * l9;
        if (x_b_off < x_c_off) {
            y_a -= y_b;
            y_b -= y_c;
            y_c = lineOffsets[y_c];
            while (--y_b >= 0) {
                drawTexturedLine_floor(pixels, texture, y_c, x_a >> 16,
                        x_c >> 16, grad_a >> 8, grad_c >> 8, col_a >> 7,
                        col_c >> 7, Oa, Ob, Oc, Ha, Hb, Hc, floor, z_a, z_c, DrawingArea.depthBuffer);
                x_a += x_b_off;
                x_c += x_c_off;
                z_a += z_b_off;
                z_c += z_c_off;
                grad_a += grad_b_off;
                grad_c += grad_c_off;
                col_a += col_b_off;
                col_c += col_c_off;
                y_c += width;
                Oa += Va;
                Ob += Vb;
                Oc += Vc;
            }
            while (--y_a >= 0) {
                drawTexturedLine_floor(pixels, texture, y_c, x_b >> 16,
                        x_c >> 16, grad_b >> 8, grad_c >> 8, col_b >> 7,
                        col_c >> 7, Oa, Ob, Oc, Ha, Hb, Hc, floor, z_b, z_c, DrawingArea.depthBuffer);
                x_b += x_a_off;
                x_c += x_c_off;
                z_b += z_a_off;
                z_c += z_c_off;
                grad_b += grad_a_off;
                grad_c += grad_c_off;
                col_b += col_a_off;
                col_c += col_c_off;
                y_c += width;
                Oa += Va;
                Ob += Vb;
                Oc += Vc;
            }
            return;
        }
        y_a -= y_b;
        y_b -= y_c;
        y_c = lineOffsets[y_c];
        while (--y_b >= 0) {
            drawTexturedLine_floor(pixels, texture, y_c, x_c >> 16, x_a >> 16,
                    grad_c >> 8, grad_a >> 8, col_c >> 7, col_a >> 7, Oa, Ob,
                    Oc, Ha, Hb, Hc, floor, z_c, z_a, DrawingArea.depthBuffer);
            x_a += x_b_off;
            x_c += x_c_off;
            z_a += z_b_off;
            z_c += z_c_off;
            grad_a += grad_b_off;
            grad_c += grad_c_off;
            col_a += col_b_off;
            col_c += col_c_off;
            y_c += width;
            Oa += Va;
            Ob += Vb;
            Oc += Vc;
        }
        while (--y_a >= 0) {
            drawTexturedLine_floor(pixels, texture, y_c, x_c >> 16, x_b >> 16,
                    grad_c >> 8, grad_b >> 8, col_c >> 7, col_b >> 7, Oa, Ob,
                    Oc, Ha, Hb, Hc, floor, z_c, z_b, DrawingArea.depthBuffer);
            x_b += x_a_off;
            x_c += x_c_off;
            z_b += z_a_off;
            z_c += z_c_off;
            grad_b += grad_a_off;
            grad_c += grad_c_off;
            col_b += col_a_off;
            col_c += col_c_off;
            y_c += width;
            Oa += Va;
            Ob += Vb;
            Oc += Vc;
        }
        return;
    }

    private static void drawTexturedLine_floor(int dest[], int texture[],
                                               int dest_off, int start_x, int end_x, int shadeValue, int gradient,
                                               int start_col, int end_col, int arg7, int arg8, int arg9,
                                               int arg10, int arg11, int arg12, boolean floor, float z1,
                                               float z2, float[] depthBuffer) {
        int rgb = 0;
        int loops = 0;
        if (end_x - start_x > 0) {
            z2 = (z2 - z1) / (end_x - start_x);
        }
        if (start_x >= end_x) {
            return;
        }
        int k3;
        int j3;
        if (restrict_edges) {
            j3 = (gradient - shadeValue) / (end_x - start_x);
            if (end_x > Rasterizer.endX) {
                end_x = Rasterizer.endX;
            }
            if (start_x < 0) {
                shadeValue -= start_x * j3;
                z1 -= start_x * z2;
                start_x = 0;
            }
            if (start_x >= end_x) {
                return;
            }
            k3 = end_x - start_x >> 3;
            j3 <<= 12;
            shadeValue <<= 9;
        } else {
            if (end_x - start_x > 7) {
                k3 = end_x - start_x >> 3;
                j3 = (gradient - shadeValue) * shadowDecay[k3] >> 6;
            } else {
                k3 = 0;
                j3 = 0;
            }
            shadeValue <<= 9;
        }
        end_col = (end_col - start_col) * shadowDecay[end_x - start_x >> 2] >> 14;
        dest_off += start_x;
        int j4 = 0;
        int l4 = 0;
        int l6 = start_x - centerX;
        arg7 += (arg10 >> 3) * l6;
        arg8 += (arg11 >> 3) * l6;
        arg9 += (arg12 >> 3) * l6;
        int l5 = arg9 >> 14;
        if (l5 != 0) {
            rgb = arg7 / l5;
            loops = arg8 / l5;
            if (rgb < 0) {
                rgb = 0;
            } else if (rgb > 16256) {
                rgb = 16256;
            }
        }
        arg7 += arg10;
        arg8 += arg11;
        arg9 += arg12;
        l5 = arg9 >> 14;
        if (l5 != 0) {
            j4 = arg7 / l5;
            l4 = arg8 / l5;
            if (j4 < 7) {
                j4 = 7;
            } else if (j4 > 16256) {
                j4 = 16256;
            }
        }
        int j7 = j4 - rgb >> 3;
        int l7 = l4 - loops >> 3;
        rgb += shadeValue & 0x600000;
        int src;
        int dst;
        final int a1 = alpha;
        final int a2 = 256 - alpha;
        while (k3-- > 0) {
			for (int i = 0; i != 8; i++) {
				if (z1 < depthBuffer[dest_off]) {
					src = texture[(loops & 16256) + (rgb >> 7)];
					if (!floor) {
						int pixel = hsl2rgb[(start_col >> 8 & 0xff80) | ((start_col >> 8 & 0x7f) * ((src >> 17 & 0x7f) + (src >> 9 & 0x7f) + (src >> 1 & 0x7f) + 0x7f >> 2) >> 7)];
						dest[dest_off] = FogHandler.applyFog(pixel, z1);
						depthBuffer[dest_off] = z1;
					} else if (src != 0) {
						dst = dest[dest_off];
						src = ((src & 0xff00ff) * a2 >> 8 & 0xff00ff) + ((src & 0xff00) * a2 >> 8 & 0xff00);
						dest[dest_off] = FogHandler.applyFog(src + ((dst & 0xff00ff) * a1 >> 8 & 0xff00ff) + ((dst & 0xff00) * a1 >> 8 & 0xff00), z1);
						depthBuffer[dest_off] = z1;
					}
				}
				z1 += z2;
				dest_off++;
				rgb += j7;
				loops += l7;
			}
            start_col += end_col;
            rgb = j4;
            loops = l4;
            arg7 += arg10;
            arg8 += arg11;
            arg9 += arg12;
            int i6 = arg9 >> 14;
            if (i6 != 0) {
                j4 = arg7 / i6;
                l4 = arg8 / i6;
                if (j4 < 7) {
                    j4 = 7;
                } else if (j4 > 16256) {
                    j4 = 16256;
                }
            }
            j7 = j4 - rgb >> 3;
            l7 = l4 - loops >> 3;
            shadeValue += j3;
            rgb += shadeValue & 0x600000;
        }

        for (k3 = end_x - start_x & 7; k3-- > 0; ) {
			if (z1 < depthBuffer[dest_off]) {
				src = texture[(loops & 16256) + (rgb >> 7)];
				if (!floor) {
					int pixel = hsl2rgb[(start_col >> 8 & 0xff80) | ((start_col >> 8 & 0x7f) * ((src >> 17 & 0x7f) + (src >> 9 & 0x7f) + (src >> 1 & 0x7f) + 0x7f >> 2) >> 7)];
					dest[dest_off] = FogHandler.applyFog(pixel, z1);
					depthBuffer[dest_off] = z1;
				} else if (src != 0) {
					dst = dest[dest_off];
					src = ((src & 0xff00ff) * a2 >> 8 & 0xff00ff) + ((src & 0xff00) * a2 >> 8 & 0xff00);
					dest[dest_off] = FogHandler.applyFog(src + ((dst & 0xff00ff) * a1 >> 8 & 0xff00ff) + ((dst & 0xff00) * a1 >> 8 & 0xff00), z1);
					depthBuffer[dest_off] = z1;
				}
			}
			z1 += z2;
            dest_off++;
            rgb += j7;
            loops += l7;
        }

    }

    public static boolean lowMem = true;
    static boolean restrict_edges;
    public static boolean notTextured = true;
    public static int alpha;
    public static int centerX;
    public static int centerY;
    public static int endX;
    public static int endY;
    public static int viewportLeft;
    public static int viewportRight;
    public static int viewportTop;
    public static int viewportBottom;
    private static int[] shadowDecay;
    public static int SINE[];
    public static int COSINE[];
    public static int lineOffsets[];
    public static TextureManager textureManager = new TextureManager();
    public static int hsl2rgb[] = new int[0x10000];

    static {
        shadowDecay = new int[512];
        SINE = new int[16384];
        COSINE = new int[16384];
        for (int i = 1; i < 512; i++) {
            shadowDecay[i] = 32768 / i;// decay rate for shadows or some
            // *profound* - super_
        }

        double angle = 3.834951969714103E-4;
        for (int i = 0; i < 16384; i++) {
            SINE[i] = (int) (Math.sin((double) i * angle) * 32768.0);
            COSINE[i] = (int) (Math.cos((double) i * angle) * 32768.0);
        }

    }
}
