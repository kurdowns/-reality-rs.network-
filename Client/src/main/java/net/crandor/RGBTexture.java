package net.crandor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class RGBTexture extends Texture {

	public int[] pixels;

	public RGBTexture(int width, int height, Stream buffer) {
		super(width, height);

		int count = width * height;
		int[] pixels = this.pixels = new int[count];
		for (int i = 0; i < count; i++) {
			int pixel = buffer.read3Bytes();
			if (pixel != 0) {
				pixel |= 0xff000000;
			} else {
				opaque = false;
			}

			pixels[i] = pixel;
		}
	}

	public static int getPixel(int x, int y, int textureId) {
		try {
			BufferedImage bi = ImageIO.read(new File(signlink.findcachedir() + "textures/" + textureId + ".png"));
			int pixel = bi.getRGB(x, y);
			if (pixel == 0xff000000)
				pixel = 0;
			return pixel;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static void createTexture(int textureId) {
		DataOutputStream dat;
		try {
			BufferedImage img = ImageIO.read(new File(signlink.findcachedir() + "textures/" + textureId + ".png"));
			dat = new DataOutputStream(new FileOutputStream(textureId + ".dat"));
			dat.writeByte(1); // texture type
			dat.writeShort(img.getWidth()); // width
			dat.writeShort(img.getHeight()); // height
			int pixelAmount = img.getWidth() * img.getHeight();
			int[] texels = new int[pixelAmount];
			for (int y = 0; y < img.getHeight(); y++) {
				for (int x = 0; x < img.getWidth(); x++) {
					texels[x + (y << 7)] = getPixel(x, y, textureId);
				}
			}
			for (int i = 0; i != pixelAmount; i++) {
				writeMedium(dat, texels[i]);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeMedium(DataOutputStream dat, int i) throws IOException {
		dat.write((byte) (i >> 16));
		dat.write((byte) (i >> 8));
		dat.write((byte) (i));
	}

	@Override
	public int getPixel(int i) {
		return pixels[i];
	}

}
