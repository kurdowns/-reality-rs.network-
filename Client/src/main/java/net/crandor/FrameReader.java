package net.crandor;

public final class FrameReader {

	private static FrameReader animationlist[][];
	private static int ai[] = new int[500];
	private static int ai1[] = new int[500];
	private static int ai2[] = new int[500];
	private static int ai3[] = new int[500];
	private static byte ai4[] = new byte[500];
	public SkinList skinList;
	public int stepCount;
	public int opcodeLinkTable[];
	public int modifier1[];
	public int modifier2[];
	public int modifier3[];
	public byte modifier4[];
	public boolean hasAlpha;

	public static void initialize(int i) {
		animationlist = new FrameReader[i][0];
	}

	public static void load(int file, byte[] fileData) {
		try {
			Stream stream = new Stream(fileData);
			SkinList skinList = new SkinList(stream);
			int k1 = stream.readUnsignedWord();
			animationlist[file] = new FrameReader[k1 * 3];
			for (int l1 = 0; l1 < k1; l1++) {
				int i2 = stream.readUnsignedWord();
				FrameReader animationFrame = animationlist[file][i2] = new FrameReader();
				animationFrame.skinList = skinList;
				int j2 = stream.readUnsignedByte();
				int l2 = 0;
				int k2 = -1;
				for (int i3 = 0; i3 < j2; i3++) {
					int j3 = stream.readUnsignedByte();

					if (j3 > 0) {
						if (skinList.opcode[i3] != 0) {
							for (int l3 = i3 - 1; l3 > k2; l3--) {
								if (skinList.opcode[l3] != 0)
									continue;
								ai[l2] = l3;
								ai1[l2] = 0;
								ai2[l2] = 0;
								ai3[l2] = 0;
								l2++;
								break;
							}

						}
						ai[l2] = i3;
						short c = 0;
						if (skinList.opcode[i3] == 3)
							c = (short) 128;

						if ((j3 & 1) != 0)
							ai1[l2] = (short) stream.readShort2();
						else
							ai1[l2] = c;

						if ((j3 & 2) != 0)
							ai2[l2] = stream.readShort2();
						else
							ai2[l2] = c;

						if ((j3 & 4) != 0)
							ai3[l2] = stream.readShort2();
						else
							ai3[l2] = c;
						ai4[l2] = (byte) (j3 >>> 3 & 0x3);
						if (skinList.opcode[i3] == 2) {
							ai1[l2] = ((ai1[l2] & 0xff) << 3) + (ai1[l2] >> 8 & 0x7);
							ai2[l2] = ((ai2[l2] & 0xff) << 3) + (ai2[l2] >> 8 & 0x7);
							ai3[l2] = ((ai3[l2] & 0xff) << 3) + (ai3[l2] >> 8 & 0x7);
							ai1[l2] <<= 3;
							ai2[l2] <<= 3;
							ai3[l2] <<= 3;
						}

						if (skinList.opcode[i3] == 5) {
							animationFrame.hasAlpha = true;
						}

						k2 = i3;
						l2++;
					}
				}

				animationFrame.stepCount = l2;
				animationFrame.opcodeLinkTable = new int[l2];
				animationFrame.modifier1 = new int[l2];
				animationFrame.modifier2 = new int[l2];
				animationFrame.modifier3 = new int[l2];
				animationFrame.modifier4 = new byte[l2];
				for (int k3 = 0; k3 < l2; k3++) {
					animationFrame.opcodeLinkTable[k3] = ai[k3];
					animationFrame.modifier1[k3] = ai1[k3];
					animationFrame.modifier2[k3] = ai2[k3];
					animationFrame.modifier3[k3] = ai3[k3];
					animationFrame.modifier4[k3] = ai4[k3];
				}

			}
		} catch (Exception exception) {
		}
	}

	public static void nullLoader() {
		animationlist = null;
	}

	public static FrameReader forId(int i) {
		try {
			int group = i >> 16;

			// note(stuart) sorry to break your beautiful code and put this cheap hax, but i cannot find the animation
			// 3622, which doesnt throw a gzip error.
			if (group == 3622) {
				return null;
			}

			if (animationlist[group].length == 0) {
				Client.instance.onDemandFetcher.loadToCache(1, group);
				return null;
			}

			int file = i & 0xffff;
			return animationlist[group][file];
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean hasAlpha(int i) {
		FrameReader frame = forId(i);
		if (frame == null) {
			return false;
		}
		return frame.hasAlpha;
	}

	public static boolean isNullFrame(int i) {
		return i == -1;
	}

}
