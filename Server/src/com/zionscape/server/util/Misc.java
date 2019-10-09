package com.zionscape.server.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zionscape.server.Config;
import com.zionscape.server.Server;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.players.Player;
import org.jboss.netty.buffer.ChannelBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public final class Misc {

	public final static char[] PLAYER_NAME_XLATE_TABLE = {'_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
			'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9'};
	public final static char[] XLATE_TABLE = {' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c',
			'y', 'f', 'g', 'p', 'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			' ', '!', '?', '.', ',', ':', ';', '(', ')', '-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$',
			'%', '"', '[', ']'};
	public final static byte[] DIRECTION_DELTA_X = new byte[]{0, 1, 1, 1, 0, -1, -1, -1};
	public final static byte[] DIRECTION_DELTA_Y = new byte[]{1, 1, 0, -1, -1, -1, 0, 1};
	public final static byte[] XLATE_DIRECTION_TO_CLIENT = new byte[]{1, 2, 4, 7, 6, 5, 3, 0};

	private final static Random random = new Random();
	private static Gson gson = new GsonBuilder().registerTypeAdapter(Location.class, new LocationSerializer()).enableComplexMapKeySerialization().setPrettyPrinting().create();
	private static char[] decodeBuf = new char[4096];

	public static int getRandomGaussianDistribution(int deviation, int mean) {
		return (int) (new Random().nextGaussian() * deviation + mean);
	}

	public static byte[] compress(byte[] data) throws IOException {
		Deflater deflater = new Deflater();
		deflater.setInput(data);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);

		deflater.finish();
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer); // returns the generated code... index
			outputStream.write(buffer, 0, count);
		}
		outputStream.close();
		byte[] output = outputStream.toByteArray();

		deflater.end();
		return output;
	}

	public static byte[] decompress(byte[] data) throws IOException, DataFormatException {
		Inflater inflater = new Inflater();
		inflater.setInput(data);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		while (!inflater.finished()) {
			int count = inflater.inflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		outputStream.close();
		byte[] output = outputStream.toByteArray();

		inflater.end();
		return output;
	}


	/**
	 * @return
	 */
	public static Gson getGson() {
		return gson;
	}

	/**
	 * @return
	 */
	public static Random getRandom() {
		return random;
	}

	public static int getChatIcon(Player player) {

		switch (player.rights) {
			case 1:
				return 1;
			case 2:
			case 3:
				return 3;
		}

		if (player.hardcoreIronman) {
			return 6;
		}

		if (player.ironman) {
			return 5;
		}

		return 0;
	}

	public static boolean startsWithVowel(String word) {
		char first = word.toLowerCase().charAt(0);
		return first == 'a' || first == 'e' || first == 'i' || first == 'o' || first == 'u';
	}

	/**
	 * @param coins
	 * @return
	 */
	public static String coinsToString(final long coins) {
		String shopAdd = "";
		if (coins >= 1000 && coins < 1000000)
			shopAdd = "" + (coins / 1000) + "K";
		else if (coins >= 1000000)
			shopAdd = "" + (coins / 1000000) + "M";
		else
			shopAdd = "" + coins + "GP";
		return shopAdd;
	}

	/**
	 * @param text
	 * @param len
	 * @return
	 */
	public static String[] wrapText(final String text, final int len) {
		// return empty array for null text
		if (text == null)
			return new String[]{};

		// return text if len is zero or less
		if (len <= 0)
			return new String[]{text};

		// return text if less than length
		if (text.length() <= len)
			return new String[]{text};

		final char[] chars = text.toCharArray();
		final Vector<String> lines = new Vector<>();
		final StringBuffer line = new StringBuffer();
		final StringBuffer word = new StringBuffer();

		for (int i = 0; i < chars.length; i++) {
			word.append(chars[i]);

			if (chars[i] == ' ') {
				if ((line.length() + word.length()) > len) {
					lines.add(line.toString());
					line.delete(0, line.length());
				}

				line.append(word);
				word.delete(0, word.length());
			}
		}

		// handle any extra chars in current word
		if (word.length() > 0) {
			if ((line.length() + word.length()) > len) {
				lines.add(line.toString());
				line.delete(0, line.length());
			}
			line.append(word);
		}

		// handle extra line
		if (line.length() > 0)
			lines.add(line.toString());

		final String[] ret = new String[lines.size()];
		int c = 0; // counter
		for (final Enumeration<String> e = lines.elements(); e.hasMoreElements(); c++)
			ret[c] = e.nextElement();

		return ret;
	}

	/**
	 * @param absX
	 * @param absY
	 * @param pointX
	 * @param pointY
	 * @param distance
	 * @return
	 */
	public static boolean isWithinDistance(int absX, int absY, int pointX, int pointY, int distance) {
		return distanceToPoint(absX, absY, pointX, pointY) <= distance;
	}

	/**
	 * @param absX
	 * @param absY
	 * @param pointX
	 * @param pointY
	 * @return
	 */
	public static int distanceToPoint(int absX, int absY, int pointX, int pointY) {
		return (int) Math.sqrt(Math.pow(absX - pointX, 2) + Math.pow(absY - pointY, 2));
	}

	/**
	 * @param aStart
	 * @param aEnd
	 * @return
	 */
	public static int random(int aStart, int aEnd) {
		if (aStart > aEnd)
			return 0;
		final long range = (long) aEnd - (long) aStart + 1;
		final long fraction = (long) (range * random.nextDouble());
		return (int) (fraction + aStart);
	}

	/**
	 * @param buf
	 * @return
	 */
	public static String getRS2String(final ChannelBuffer buf) {
		final StringBuilder bldr = new StringBuilder();
		byte b;
		while (buf.readable() && (b = buf.readByte()) != 10) {
			bldr.append((char) b);
		}
		return bldr.toString();
	}

	/**
	 * @param str
	 * @return
	 */
	public static String formatPlayerName(String str) {
		return Misc.ucFirst(str).replace("_", " ");
	}

	/**
	 * @param l
	 * @return
	 */
	public static String longToString(long l) {
		int i = 0;
		char[] ac = new char[12];
		while (l != 0L) {
			long l1 = l;
			l /= 37L;
			ac[11 - i++] = PLAYER_NAME_XLATE_TABLE[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}

	/**
	 * @param num
	 * @return
	 */
	public static String format(int num) {
		return NumberFormat.getInstance().format(num);
	}

	/**
	 * @param str
	 * @return
	 */
	public static String ucFirst(String str) {
		str = str.toLowerCase();
		if (str.length() > 1) {
			str = str.substring(0, 1).toUpperCase() + str.substring(1);
		} else {
			return str.toUpperCase();
		}
		return str;
	}

	/**
	 * @param data
	 * @param offset
	 * @param len
	 * @return
	 */
	public static int hexToInt(byte[] data, int offset, int len) {
		int temp = 0;
		int i = 1000;
		for (int cntr = 0; cntr < len; cntr++) {
			int num = (data[offset + cntr] & 0xFF) * i;
			temp += (int) num;
			if (i > 1) {
				i = i / 1000;
			}
		}
		return temp;
	}

	/**
	 * @param range
	 * @return
	 */
	public static int random2(int range) {
		return (int) ((Math.random() * range) + 1);
	}

	/**
	 * @param range
	 * @return
	 */
	public static int random(int range) {
		return (int) (Math.random() * (range + 1));
	}

	/**
	 * @param range
	 * @return
	 */
	public static int nextInt(int range) {
		return random.nextInt(range);
	}

	/**
	 * @param s
	 * @return
	 */
	public static long playerNameToInt64(String s) {
		long l = 0L;
		for (int i = 0; i < s.length() && i < 12; i++) {
			char c = s.charAt(i);
			l *= 37L;
			if (c >= 'A' && c <= 'Z') {
				l += (1 + c) - 65;
			} else if (c >= 'a' && c <= 'z') {
				l += (1 + c) - 97;
			} else if (c >= '0' && c <= '9') {
				l += (27 + c) - 48;
			}
		}
		while (l % 37L == 0L && l != 0L) {
			l /= 37L;
		}
		return l;
	}

	/**
	 * @param packedData
	 * @param size
	 * @return
	 */
	public static String textUnpack(byte[] packedData, int size) {
		int idx = 0, highNibble = -1;
		for (int i = 0; i < size * 2; i++) {
			int val = packedData[i / 2] >> (4 - 4 * (i % 2)) & 0xf;
			if (highNibble == -1) {
				if (val < 13) {
					decodeBuf[idx++] = XLATE_TABLE[val];
				} else {
					highNibble = val;
				}
			} else {
				decodeBuf[idx++] = XLATE_TABLE[((highNibble << 4) + val) - 195];
				highNibble = -1;
			}
		}
		return new String(decodeBuf, 0, idx);
	}

	/**
	 * @param srcX
	 * @param srcY
	 * @param destX
	 * @param destY
	 * @return
	 */
	public static int direction(int srcX, int srcY, int destX, int destY) {
		int dx = destX - srcX, dy = destY - srcY;
		if (dx < 0) {
			if (dy < 0) {
				if (dx < dy) {
					return 11;
				} else if (dx > dy) {
					return 9;
				} else {
					return 10;
				}
			} else if (dy > 0) {
				if (-dx < dy) {
					return 15;
				} else if (-dx > dy) {
					return 13;
				} else {
					return 14;
				}
			} else {
				return 12;
			}
		} else if (dx > 0) {
			if (dy < 0) {
				if (dx < -dy) {
					return 7;
				} else if (dx > -dy) {
					return 5;
				} else {
					return 6;
				}
			} else if (dy > 0) {
				if (dx < dy) {
					return 1;
				} else if (dx > dy) {
					return 3;
				} else {
					return 2;
				}
			} else {
				return 4;
			}
		} else {
			if (dy < 0) {
				return 8;
			} else if (dy > 0) {
				return 0;
			} else {
				return -1;
			}
		}
	}

	/**
	 * Returns the delta coordinates. Note that the returned Position is not an actual position, instead it's values
	 * represent the delta values between the two arguments.
	 *
	 * @param a the first position
	 * @param b the second position
	 * @return the delta coordinates contained within a position
	 */
	public static Location delta(Location a, Location b) {
		return Location.create(b.getX() - a.getX(), b.getY() - a.getY(), a.getZ());
	}

	/**
	 * @param number
	 * @return
	 */
	public static String formatNumber(long number) {
		return NumberFormat.getInstance().format(number);
	}

}