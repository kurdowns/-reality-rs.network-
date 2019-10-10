package net.crandor;

import java.io.*;

public class Settings {

	// chris: Note, if you want to add another thing to save, increase the version and make sure to check for the version in loading.
	private static final Stream BUFFER = Stream.create();
	private static final int VERSION = 1;

	public static void write() {
		BUFFER.currentOffset = 0;
		BUFFER.putInt(VERSION);
		BUFFER.putString(Settings.remember ? Client.myUsername : "");
		BUFFER.putByte(Settings.fog ? 1 : 0);
		BUFFER.putByte(Settings.newHpBar ? 1 : 0);
		BUFFER.putByte(Settings.newHitNumbers ? 1 : 0);
		BUFFER.putByte(Settings.hitmarks);
		BUFFER.putByte(Settings.playerShadow ? 1 : 0);
		BUFFER.putByte(Settings.npcShadow ? 1 : 0);
		BUFFER.putByte(Settings.tweening ? 1 : 0);
		BUFFER.putByte(Settings.minimapShading ? 1 : 0);
		BUFFER.putByte(Settings.mouseHover ? 1 : 0);
		BUFFER.putByte(Settings.detail);
		BUFFER.putByte(Client.clientSize);
		BUFFER.putByte(Client.preloadedCache ? 1 : 0);
		try (RandomAccessFile raf = new RandomAccessFile(signlink.findcachedir() + "/settings.dat", "rw")) {
			raf.write(BUFFER.buffer, 0, BUFFER.currentOffset);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void load() {
		File file = new File(signlink.findcachedir() + "/settings.ini");
		if (file.exists()) {
			file.delete();
			Client.preloadedCache = true;
		}

		try (RandomAccessFile raf = new RandomAccessFile(signlink.findcachedir() + "/settings.dat", "rw")) {
			byte[] buffer = new byte[(int) raf.length()];

			int bytesRead;
			for (int off = 0; off < buffer.length; off += bytesRead) {
				bytesRead = raf.read(buffer, off, buffer.length - off);
				if (bytesRead == -1) {
					throw new IOException("EOF");
				}
			}

			Stream stream = new Stream(buffer);
			if (stream.buffer.length - stream.currentOffset >= 4) {
				int version = stream.readDWord();
				if (version >= 1) {
					String username = stream.readString();
					if (!username.isEmpty()) {
						Client.myUsername = username;
						Settings.remember = true;
					}
					fog = stream.readUnsignedByte() == 1;
					newHpBar = stream.readUnsignedByte() == 1;
					newHitNumbers = stream.readUnsignedByte() == 1;
					hitmarks = stream.readUnsignedByte();
					playerShadow = stream.readUnsignedByte() == 1;
					npcShadow = stream.readUnsignedByte() == 1;
					tweening = stream.readUnsignedByte() == 1;
					minimapShading = stream.readUnsignedByte() == 1;
					mouseHover = stream.readUnsignedByte() == 1;
					detail = stream.readUnsignedByte();
					Client.clientSize = stream.readUnsignedByte();
					Client.preloadedCache = stream.readUnsignedByte() == 1;
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	static boolean fog = true;
	static int detail;
	static boolean newHpBar = true;
	static boolean remember = false;
	static int hitmarks = Constants.HITMARKS_634;
	static boolean newHitNumbers = true;
	static boolean playerShadow = false;
	static boolean npcShadow = false;
	static boolean mouseHover = false;
	static boolean tweening = true;
	static boolean minimapShading = false;
}
