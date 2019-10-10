package net.crandor;

final class ObjectManager {

    private static final int anIntArray137[] = {1, 0, -1, 0};
    private static final int anIntArray140[] = {16, 32, 64, 128};
    private static final int anIntArray144[] = {0, -1, 0, 1};
    private static final int anIntArray152[] = {1, 2, 4, 8};
    public static boolean disableGroundTextures = true;
    static int anInt131;
    static int setZ = 99;
    static boolean lowMem = true;
    static int[] hueBuffer;
    static int[] saturationBuffer;
    static int[] lightnessBuffer;
    static int[] hueDivider;
    static int[] bufferSize;
    static int[][][] tileHeight;
    static byte[][][] tileTypeLayer1;
    static byte[][][] objectShadowData;
    static int[][][] anIntArrayArrayArray135;
    static byte[][][] tileShapeLayer1;
    static int[][] anIntArrayArray139;
    static byte[][][] tileTypeLayer0;
    static int sizeX;
    static int sizeZ;
    static byte[][][] tileOrientationLayer1;
    static byte[][][] tileFlags;
    public static int underlay_floor_texture;

    public static void init(byte abyte0[][][], int ai[][][]) {
        setZ = 99;
        sizeX = 104;
        sizeZ = 104;
        tileHeight = ai;
        tileFlags = abyte0;
        tileTypeLayer0 = new byte[4][sizeX][sizeZ];
        tileTypeLayer1 = new byte[4][sizeX][sizeZ];
        tileShapeLayer1 = new byte[4][sizeX][sizeZ];
        tileOrientationLayer1 = new byte[4][sizeX][sizeZ];
        anIntArrayArrayArray135 = new int[4][sizeX + 1][sizeZ + 1];
        objectShadowData = new byte[4][sizeX + 1][sizeZ + 1];
        anIntArrayArray139 = new int[sizeX + 1][sizeZ + 1];
        hueBuffer = new int[sizeZ];
        saturationBuffer = new int[sizeZ];
        lightnessBuffer = new int[sizeZ];
        hueDivider = new int[sizeZ];
        bufferSize = new int[sizeZ];
    }
    
    public static void reset() {
    	tileHeight = null;
    	tileFlags = null;
    	tileTypeLayer0 = null;
        tileTypeLayer1 = null;
        tileShapeLayer1 = null;
        tileOrientationLayer1 = null;
        anIntArrayArrayArray135 = null;
        objectShadowData = null;
        anIntArrayArray139 = null;
        hueBuffer = null;
        saturationBuffer = null;
        lightnessBuffer = null;
        hueDivider = null;
        bufferSize = null;
    }

    private static int method170(int i, int j) {
        int k = i + j * 57;
        k = k << 13 ^ k;
        int l = k * (k * k * 15731 + 0xc0ae5) + 0x5208dd0d & 0x7fffffff;
        return l >> 19 & 0xff;
    }

    private static int method172(int i, int j) {
        int k = (method176(i + 45365, j + 0x16713, 4) - 128)
                + (method176(i + 10294, j + 37821, 2) - 128 >> 1)
                + (method176(i, j, 1) - 128 >> 2);
        k = (int) (k * 0.29999999999999999D) + 35;
        if (k < 10)
            k = 10;
        else if (k > 60)
            k = 60;
        return k;
    }

    public static void method173(Stream stream, OnDemandFetcher class42_sub1) {
        label0:
        {
            int i = -1;
            do {
                int j = stream.method422();
                if (j == 0)
                    break label0;
                i += j;
                ObjectDefinition class46 = ObjectDefinition.forID(i);
                class46.requestModels(class42_sub1);
                do {
                    int k = stream.method422();
                    if (k == 0)
                        break;
                    stream.readUnsignedByte();
                } while (true);
            } while (true);
        }
    }

    private static int method176(int i, int j, int k) {
        int l = i / k;
        int i1 = i & k - 1;
        int j1 = j / k;
        int k1 = j & k - 1;
        int l1 = method186(l, j1);
        int i2 = method186(l + 1, j1);
        int j2 = method186(l, j1 + 1);
        int k2 = method186(l + 1, j1 + 1);
        int l2 = method184(l1, i2, i1, k);
        int i3 = method184(j2, k2, i1, k);
        return method184(l2, i3, k1, k);
    }

    public static boolean method178(int i, int j) {
        ObjectDefinition objectDef = ObjectDefinition.forID(i);
        if (j == 11)
            j = 10;
        if (j >= 5 && j <= 8)
            j = 4;
        return objectDef.isTypeCached(j);
    }

    private static int method184(int i, int j, int k, int l) {
        int i1 = 0x10000 - Rasterizer.COSINE[(k * 1024) / l] >> 1;
        return (i * (0x10000 - i1) >> 16) + (j * i1 >> 16);
    }

    private static int method186(int i, int j) {
        int k = method170(i - 1, j - 1) + method170(i + 1, j - 1)
                + method170(i - 1, j + 1) + method170(i + 1, j + 1);
        int l = method170(i - 1, j) + method170(i + 1, j) + method170(i, j - 1)
                + method170(i, j + 1);
        int i1 = method170(i, j);
        return k / 16 + l / 8 + i1 / 4;
    }

    private static int method187(int i, int j) {
        if (i == -1)
            return 0xbc614e;
        j = (j * (i & 0x7f)) / 128;
        if (j < 2)
            j = 2;
        else if (j > 126)
            j = 126;
        return (i & 0xff80) + j;
    }

    public static void method188(WorldController worldController, int angle, int z,
                                 int type, int l, TileSetting class11, int ai[][][], int x, int j1, int k1) {
        int l1 = ai[l][x][z];
        int i2 = ai[l][x + 1][z];
        int j2 = ai[l][x + 1][z + 1];
        int k2 = ai[l][x][z + 1];
        int l2 = l1 + i2 + j2 + k2 >> 2;
        ObjectDefinition class46 = ObjectDefinition.forID(j1);
        long i3 = x | (z << 7) | (type << 14) | (angle << 20) | 0x40000000;
        if (class46.hasActions == 0) {
            i3 |= ~0x7fffffffffffffffL;
        }
        if (class46.supportItems == 1) {
        	i3 |= 0x400000L;
        }
        i3 |= (long) j1 << 32;
        if (type == 22) {
        	Entity obj;
            if (class46.animationId == -1 && class46.alternativeIDS == null)
                obj = class46.generateModel(22, angle, l1, i2, j2, k2);
            else
                obj = new ObjectInstance(j1, angle, 22, i2, j2, l1, k2,
                        class46.animationId, true);
            worldController.addGroundDecoration(k1, l2, z, obj, i3, x);
            if (class46.isUnwalkable && class46.hasActions == 1)
                class11.orClipTableSET(z, x);
            return;
        }
        if (type == 10 || type == 11) {
        	Entity obj1;
            if (class46.animationId == -1 && class46.alternativeIDS == null)
                obj1 = class46.generateModel(10, angle, l1, i2, j2, k2);
            else
                obj1 = new ObjectInstance(j1, angle, 10, i2, j2, l1, k2,
                        class46.animationId, true);
            if (obj1 != null) {
                int j5 = 0;
                if (type == 11)
                    j5 += 256;
                int k4;
                int i5;
                if (angle == 1 || angle == 3) {
                    k4 = class46.height;
                    i5 = class46.width;
                } else {
                    k4 = class46.width;
                    i5 = class46.height;
                }
                worldController.addEntityB(i3, l2, i5, obj1,
                        k4, k1, j5, z, x);
            }
            if (class46.isUnwalkable)
                class11.method212(class46.impenetrable, class46.width,
                        class46.height, x, z, angle);
            return;
        }
        if (type >= 12) {
        	Entity obj2;
            if (class46.animationId == -1 && class46.alternativeIDS == null)
                obj2 = class46.generateModel(type, angle, l1, i2, j2, k2);
            else
                obj2 = new ObjectInstance(j1, angle, type, i2, j2, l1, k2,
                        class46.animationId, true);
            worldController.addEntityB(i3, l2, 1, obj2, 1, k1,
                    0, z, x);
            if (class46.isUnwalkable)
                class11.method212(class46.impenetrable, class46.width,
                        class46.height, x, z, angle);
            return;
        }
        if (type == 0) {
        	Entity obj3;
            if (class46.animationId == -1 && class46.alternativeIDS == null)
                obj3 = class46.generateModel(0, angle, l1, i2, j2, k2);
            else
                obj3 = new ObjectInstance(j1, angle, 0, i2, j2, l1, k2,
                        class46.animationId, true);
            worldController.addWallObject(anIntArray152[angle], obj3,
                    i3, z, x, null, l2, 0, k1);
            if (class46.isUnwalkable)
                class11.method211(z, angle, x, type, class46.impenetrable);
            return;
        }
        if (type == 1) {
        	Entity obj4;
            if (class46.animationId == -1 && class46.alternativeIDS == null)
                obj4 = class46.generateModel(1, angle, l1, i2, j2, k2);
            else
                obj4 = new ObjectInstance(j1, angle, 1, i2, j2, l1, k2,
                        class46.animationId, true);
            worldController.addWallObject(anIntArray140[angle], obj4,
                    i3, z, x, null, l2, 0, k1);
            if (class46.isUnwalkable)
                class11.method211(z, angle, x, type, class46.impenetrable);
            return;
        }
        if (type == 2) {
            int j3 = angle + 1 & 3;
            Entity obj11;
            Entity obj12;
            if (class46.animationId == -1 && class46.alternativeIDS == null) {
                obj11 = class46.generateModel(2, 4 + angle, l1, i2, j2, k2);
                obj12 = class46.generateModel(2, j3, l1, i2, j2, k2);
            } else {
                obj11 = new ObjectInstance(j1, 4 + angle, 2, i2, j2, l1, k2,
                        class46.animationId, true);
                obj12 = new ObjectInstance(j1, j3, 2, i2, j2, l1, k2,
                        class46.animationId, true);
            }
            worldController.addWallObject(anIntArray152[angle], obj11,
                    i3, z, x, obj12, l2, anIntArray152[j3],
                    k1);
            if (class46.isUnwalkable)
                class11.method211(z, angle, x, type, class46.impenetrable);
            return;
        }
        if (type == 3) {
        	Entity obj5;
            if (class46.animationId == -1 && class46.alternativeIDS == null)
                obj5 = class46.generateModel(3, angle, l1, i2, j2, k2);
            else
                obj5 = new ObjectInstance(j1, angle, 3, i2, j2, l1, k2,
                        class46.animationId, true);
            worldController.addWallObject(anIntArray140[angle], obj5,
                    i3, z, x, null, l2, 0, k1);
            if (class46.isUnwalkable)
                class11.method211(z, angle, x, type, class46.impenetrable);
            return;
        }
        if (type == 9) {
        	Entity obj6;
            if (class46.animationId == -1 && class46.alternativeIDS == null)
                obj6 = class46.generateModel(type, angle, l1, i2, j2, k2);
            else
                obj6 = new ObjectInstance(j1, angle, type, i2, j2, l1, k2,
                        class46.animationId, true);
            worldController.addEntityB(i3, l2, 1, obj6, 1, k1,
                    0, z, x);
            if (class46.isUnwalkable)
                class11.method212(class46.impenetrable, class46.width,
                        class46.height, x, z, angle);
            return;
        }
        if (class46.adjustToTerrain)
            if (angle == 1) {
                int k3 = k2;
                k2 = j2;
                j2 = i2;
                i2 = l1;
                l1 = k3;
            } else if (angle == 2) {
                int l3 = k2;
                k2 = i2;
                i2 = l3;
                l3 = j2;
                j2 = l1;
                l1 = l3;
            } else if (angle == 3) {
                int i4 = k2;
                k2 = l1;
                l1 = i2;
                i2 = j2;
                j2 = i4;
            }
        if (type == 4) {
        	Entity obj7;
            if (class46.animationId == -1 && class46.alternativeIDS == null)
                obj7 = class46.generateModel(4, 0, l1, i2, j2, k2);
            else
                obj7 = new ObjectInstance(j1, 0, 4, i2, j2, l1, k2,
                        class46.animationId, true);
            worldController.addWallDecoration(i3, z, angle * 512, k1, 0, l2,
                    obj7, x, 0, anIntArray152[angle]);
            return;
        }
        if (type == 5) {
            int j4 = 16;
            long l4 = worldController.method300(k1, x, z);
            if (l4 != 0L) {
                j4 = ObjectDefinition.forID((int) (l4 >>> 32) & 0x7fffffff).decorDisplacement;
            }
            Entity obj13;
            if (class46.animationId == -1 && class46.alternativeIDS == null)
                obj13 = class46.generateModel(4, 0, l1, i2, j2, k2);
            else
                obj13 = new ObjectInstance(j1, 0, 4, i2, j2, l1, k2,
                        class46.animationId, true);
            worldController.addWallDecoration(i3, z, angle * 512, k1,
                    anIntArray137[angle] * j4, l2, obj13, x, anIntArray144[angle] * j4,
                    anIntArray152[angle]);
            return;
        }
        if (type == 6) {
        	Entity obj8;
            if (class46.animationId == -1 && class46.alternativeIDS == null)
                obj8 = class46.generateModel(4, 0, l1, i2, j2, k2);
            else
                obj8 = new ObjectInstance(j1, 0, 4, i2, j2, l1, k2,
                        class46.animationId, true);
            worldController.addWallDecoration(i3, z, angle, k1, 0, l2, obj8,
                    x, 0, 256);
            return;
        }
        if (type == 7) {
        	Entity obj9;
            if (class46.animationId == -1 && class46.alternativeIDS == null)
                obj9 = class46.generateModel(4, 0, l1, i2, j2, k2);
            else
                obj9 = new ObjectInstance(j1, 0, 4, i2, j2, l1, k2,
                        class46.animationId, true);
            worldController.addWallDecoration(i3, z, angle, k1, 0, l2, obj9,
                    x, 0, 512);
            return;
        }
        if (type == 8) {
        	Entity obj10;
            if (class46.animationId == -1 && class46.alternativeIDS == null)
                obj10 = class46.generateModel(4, 0, l1, i2, j2, k2);
            else
                obj10 = new ObjectInstance(j1, 0, 4, i2, j2, l1, k2,
                        class46.animationId, true);
            worldController.addWallDecoration(i3, z, angle, k1, 0, l2,
                    obj10, x, 0, 768);
        }
    }

    public static boolean isObjectBlockCached(int blockX, byte[] is, int blockZ) {
        boolean fullyCached = true;
        Stream stream = new Stream(is);
        int objectID = -1;
        for (; ; ) {
            int deltaID = stream.method422();
            if (deltaID == 0)
                break;
            objectID += deltaID;
            int pos = 0;
            boolean foundInstance = false;
            for (; ; ) {
                if (foundInstance) {
                    int i_256_ = stream.method422();
                    if (i_256_ == 0)
                        break;
                    stream.readUnsignedByte();
                } else {
                    int deltaPos = stream.method422();
                    if (deltaPos == 0)
                        break;
                    pos += deltaPos - 1;
                    int tileY = pos & 0x3f;
                    int tileX = pos >> 6 & 0x3f;
                    int objectType = stream.readUnsignedByte() >> 2;
                    int objectX = tileX + blockX;
                    int objectZ = tileY + blockZ;
                    if (objectX > 0 && objectZ > 0 && objectX < 103 && objectZ < 103) {
                        ObjectDefinition object = ObjectDefinition.forID(objectID);
                        if (objectType != 22 || !lowMem || object.hasActions == 1 || object.groundObstructive) {
                            foundInstance = true;
                            if (!object.isCached()) {
                            	fullyCached = false;
                            }
                        }
                    }
                }
            }
        }
        return fullyCached;
    }

	public static void addTiles(TileSetting tileSettings[], WorldController scene) {
		for (int level = 0; level < 4; level++) {
			for (int x = 0; x < 104; x++) {
				for (int z = 0; z < 104; z++)
					if ((tileFlags[level][x][z] & 1) == 1) {
						int transformedLevel = level;
						if ((tileFlags[1][x][z] & 2) == 2) {
							transformedLevel--;
						}
						if (transformedLevel >= 0) {
							tileSettings[transformedLevel].orClipTableSET(z, x);
						}
					}

			}
		}
		for (int level = 0; level < 4; level++) {
			byte shadow_intensity[][] = objectShadowData[level];
			byte directional_light_initial_intensity = 96;
			char specular_distribution_factor = '\u0300';
			byte directional_light_x = -50;
			byte directional_light_y = -10;
			byte directional_light_z = -50;
			int directional_light_length = (int) Math.sqrt((directional_light_x * directional_light_x + directional_light_y * directional_light_y + directional_light_z * directional_light_z));
			int specular_distribution = specular_distribution_factor * directional_light_length >> 8;
			for (int z = 1; z < sizeZ - 1; z++) {
				for (int x = 1; x < sizeX - 1; x++) {
					int x_height_difference = tileHeight[level][x + 1][z] - tileHeight[level][x - 1][z];
					int y_height_difference = tileHeight[level][x][z + 1] - tileHeight[level][x][z - 1];
					int normal_length = (int) Math.sqrt((x_height_difference * x_height_difference + 0x10000 + y_height_difference * y_height_difference));
					int normalized_normal_x = (x_height_difference << 8) / normal_length;
					int normalized_normal_y = 0x10000 / normal_length;
					int normalized_normal_z = (y_height_difference << 8) / normal_length;
					int directional_light_intensity = directional_light_initial_intensity + ((directional_light_x * normalized_normal_x) + (directional_light_y * normalized_normal_y) + (directional_light_z * normalized_normal_z)) / specular_distribution;

					int weighted_shadow_intensity = (shadow_intensity[x - 1][z] >> 2) + (shadow_intensity[x + 1][z] >> 3) + (shadow_intensity[x][z - 1] >> 2) + (shadow_intensity[x][z + 1] >> 3) + (shadow_intensity[x][z] >> 1);
					anIntArrayArray139[x][z] = directional_light_intensity - weighted_shadow_intensity;
				}

			}

			int[][] paletteIndices = new int[sizeX][sizeZ];
			for (int z = 0; z < sizeZ; z++) {
				hueBuffer[z] = 0;
				saturationBuffer[z] = 0;
				lightnessBuffer[z] = 0;
				hueDivider[z] = 0;
				bufferSize[z] = 0;
			}

			for (int x = -5; x < sizeX; x++) {
				for (int z = 0; z < sizeZ; z++) {
					int xForwardOffset = x + 5;
					if (xForwardOffset < sizeX) {
						int underlayId = tileTypeLayer0[level][xForwardOffset][z] & 0xff;
						if (underlayId > 0) {
							FloorUnderlay flo = FloorUnderlay.getFloor(underlayId - 1, ObjectDefinition.useOsrsIndex());
							hueBuffer[z] += flo.hue2;
							saturationBuffer[z] += flo.saturation;
							lightnessBuffer[z] += flo.lightness;
							hueDivider[z] += flo.hueWeight;
							bufferSize[z]++;
						}
					}
					int xBackwardOffset = x - 5;
					if (xBackwardOffset >= 0) {
						int underlayId = tileTypeLayer0[level][xBackwardOffset][z] & 0xff;
						if (underlayId > 0) {
							FloorUnderlay flo_1 = FloorUnderlay.getFloor(underlayId - 1, ObjectDefinition.useOsrsIndex());
							hueBuffer[z] -= flo_1.hue2;
							saturationBuffer[z] -= flo_1.saturation;
							lightnessBuffer[z] -= flo_1.lightness;
							hueDivider[z] -= flo_1.hueWeight;
							bufferSize[z]--;
						}
					}
				}
				if (x >= 0) {
					int hueSum = 0;
					int saturationSum = 0;
					int lightnessSum = 0;
					int dividerSum = 0;
					int sizeSum = 0;
					for (int z = -5; z < sizeZ; z++) {
						int zForwardOffset = z + 5;
						if (zForwardOffset < sizeZ) {
							hueSum += hueBuffer[zForwardOffset];
							saturationSum += saturationBuffer[zForwardOffset];
							lightnessSum += lightnessBuffer[zForwardOffset];
							dividerSum += hueDivider[zForwardOffset];
							sizeSum += bufferSize[zForwardOffset];
						}
						int zBackwardOffset = z - 5;
						if (zBackwardOffset >= 0) {
							hueSum -= hueBuffer[zBackwardOffset];
							saturationSum -= saturationBuffer[zBackwardOffset];
							lightnessSum -= lightnessBuffer[zBackwardOffset];
							dividerSum -= hueDivider[zBackwardOffset];
							sizeSum -= bufferSize[zBackwardOffset];
						}
						if (z >= 0 && dividerSum > 0 && sizeSum > 0) {
							int hue = (hueSum * 256) / dividerSum;
							int sat = saturationSum / sizeSum;
							int light = lightnessSum / sizeSum;
							paletteIndices[x][z] = FloorUnderlay.hslToRuneTekPalette(hue, sat, light);
						}
					}

				}
			}
			addTile(level, paletteIndices, sizeX, sizeZ, scene);
			for (int j8 = 1; j8 < sizeZ - 1; j8++) {
				for (int i10 = 1; i10 < sizeX - 1; i10++)
					scene.method278(level, i10, j8, getLogicHeight(j8, level, i10));
			}
		}
		scene.method305(-10, -50, -50);
		for (int j1 = 0; j1 < sizeX; j1++) {
			for (int l1 = 0; l1 < sizeZ; l1++)
				if ((tileFlags[1][j1][l1] & 2) == 2)
					scene.method276(l1, j1);
		}
		int i2 = 1;
		int j2 = 2;
		int k2 = 4;
		for (int l2 = 0; l2 < 4; l2++) {
			if (l2 > 0) {
				i2 <<= 3;
				j2 <<= 3;
				k2 <<= 3;
			}
			for (int i3 = 0; i3 <= l2; i3++) {
				for (int k3 = 0; k3 <= sizeZ; k3++) {
					for (int i4 = 0; i4 <= sizeX; i4++) {
						if ((anIntArrayArrayArray135[i3][i4][k3] & i2) != 0) {
							int k4 = k3;
							int l5 = k3;
							int i7 = i3;
							int k8 = i3;
							for (; k4 > 0 && (anIntArrayArrayArray135[i3][i4][k4 - 1] & i2) != 0; k4--)
								;
							for (; l5 < sizeZ && (anIntArrayArrayArray135[i3][i4][l5 + 1] & i2) != 0; l5++)
								;
							label0: for (; i7 > 0; i7--) {
								for (int j10 = k4; j10 <= l5; j10++)
									if ((anIntArrayArrayArray135[i7 - 1][i4][j10] & i2) == 0)
										break label0;
							}
							label1: for (; k8 < l2; k8++) {
								for (int k10 = k4; k10 <= l5; k10++)
									if ((anIntArrayArrayArray135[k8 + 1][i4][k10] & i2) == 0)
										break label1;
							}
							int l10 = ((k8 + 1) - i7) * ((l5 - k4) + 1);
							if (l10 >= 8) {
								char c1 = '\360';
								int k14 = tileHeight[k8][i4][k4] - c1;
								int l15 = tileHeight[i7][i4][k4];
								WorldController.createCullingCluster(l2, i4 * 128, l15, i4 * 128, l5 * 128 + 128, k14, k4 * 128, 1);
								for (int l16 = i7; l16 <= k8; l16++) {
									for (int l17 = k4; l17 <= l5; l17++)
										anIntArrayArrayArray135[l16][i4][l17] &= ~i2;
								}
							}
						}
						if ((anIntArrayArrayArray135[i3][i4][k3] & j2) != 0) {
							int l4 = i4;
							int i6 = i4;
							int j7 = i3;
							int l8 = i3;
							for (; l4 > 0 && (anIntArrayArrayArray135[i3][l4 - 1][k3] & j2) != 0; l4--)
								;
							for (; i6 < sizeX && (anIntArrayArrayArray135[i3][i6 + 1][k3] & j2) != 0; i6++)
								;
							label2: for (; j7 > 0; j7--) {
								for (int i11 = l4; i11 <= i6; i11++)
									if ((anIntArrayArrayArray135[j7 - 1][i11][k3] & j2) == 0)
										break label2;
							}
							label3: for (; l8 < l2; l8++) {
								for (int j11 = l4; j11 <= i6; j11++)
									if ((anIntArrayArrayArray135[l8 + 1][j11][k3] & j2) == 0)
										break label3;
							}
							int k11 = ((l8 + 1) - j7) * ((i6 - l4) + 1);
							if (k11 >= 8) {
								char c2 = '\360';
								int l14 = tileHeight[l8][l4][k3] - c2;
								int i16 = tileHeight[j7][l4][k3];
								WorldController.createCullingCluster(l2, l4 * 128, i16, i6 * 128 + 128, k3 * 128, l14, k3 * 128, 2);
								for (int i17 = j7; i17 <= l8; i17++) {
									for (int i18 = l4; i18 <= i6; i18++)
										anIntArrayArrayArray135[i17][i18][k3] &= ~j2;
								}
							}
						}
						if ((anIntArrayArrayArray135[i3][i4][k3] & k2) != 0) {
							int i5 = i4;
							int j6 = i4;
							int k7 = k3;
							int i9 = k3;
							for (; k7 > 0 && (anIntArrayArrayArray135[i3][i4][k7 - 1] & k2) != 0; k7--)
								;
							for (; i9 < sizeZ && (anIntArrayArrayArray135[i3][i4][i9 + 1] & k2) != 0; i9++)
								;
							label4: for (; i5 > 0; i5--) {
								for (int l11 = k7; l11 <= i9; l11++)
									if ((anIntArrayArrayArray135[i3][i5 - 1][l11] & k2) == 0)
										break label4;
							}
							label5: for (; j6 < sizeX; j6++) {
								for (int i12 = k7; i12 <= i9; i12++)
									if ((anIntArrayArrayArray135[i3][j6 + 1][i12] & k2) == 0)
										break label5;
							}
							if (((j6 - i5) + 1) * ((i9 - k7) + 1) >= 4) {
								int j12 = tileHeight[i3][i5][k7];
								WorldController.createCullingCluster(l2, i5 * 128, j12, j6 * 128 + 128, i9 * 128 + 128, j12, k7 * 128, 4);
								for (int k13 = i5; k13 <= j6; k13++) {
									for (int i15 = k7; i15 <= i9; i15++)
										anIntArrayArrayArray135[i3][k13][i15] &= ~k2;

								}
							}
						}
					}
				}
			}
		}
	}

	private static void addTile(int level, int[][] paletteIndices, int sizeX, int sizeZ, WorldController scene) {
		try {
			for (int x = 0; x < sizeX; x++) {
				int nextX = x >= sizeX - 1 ? x : x + 1;
				for (int z = 0; z < sizeZ; z++) {
					int nextZ = z >= sizeZ - 1 ? z : z + 1;
					if ((!lowMem || (tileFlags[0][x][z] & 2) != 0 || (tileFlags[level][x][z] & 0x10) == 0 && getLogicHeight(z, level, x) == anInt131)) {
						if (level < setZ)
							setZ = level;
						int underlayIdA = tileTypeLayer0[level][x][z] & 0xff;
						int underlayIdB = tileTypeLayer0[level][nextX][z] & 0xff;
						int underlayIdC = tileTypeLayer0[level][nextX][nextZ] & 0xff;
						int underlayIdD = tileTypeLayer0[level][x][nextZ] & 0xff;
						int overlayId = tileTypeLayer1[level][x][z] & 0xff;
						if (underlayIdA > 0 || overlayId > 0) {
							int tileHeightA = tileHeight[level][x][z];
							int tileHeightB = tileHeight[level][x + 1][z];
							int tileHeightC = tileHeight[level][x + 1][z + 1];
							int tileHeightD = tileHeight[level][x][z + 1];
							int tileShadowA = anIntArrayArray139[x][z];
							int tileShadowB = anIntArrayArray139[x + 1][z];
							int tileShadowC = anIntArrayArray139[x + 1][z + 1];
							int tileShadowD = anIntArrayArray139[x][z + 1];
							int paletteIndexA = -1;
							int paletteIndexB = -1;
							int paletteIndexC = -1;
							int paletteIndexD = -1;
							if (underlayIdA > 0) {
								paletteIndexA = paletteIndices[x][z];
								if (underlayIdB > 0) {
									paletteIndexB = paletteIndices[nextX][z];
								}
								if (underlayIdC > 0) {
									paletteIndexC = paletteIndices[nextX][nextZ];
								}
								if (underlayIdD > 0) {
									paletteIndexD = paletteIndices[x][nextZ];
								}

								if (paletteIndexB == -1) {
									paletteIndexB = paletteIndexA;
								}
								
								if (paletteIndexC == -1) {
									paletteIndexC = paletteIndexA;
								}
								
								if (paletteIndexD == -1) {
									paletteIndexD = paletteIndexA;
								}
							}
							if (level > 0) {
								boolean hide_underlay = true;
								if (underlayIdA == 0 && tileShapeLayer1[level][x][z] != 0)
									hide_underlay = false;

								if (overlayId > 0 && FloorOverlay.getFloorLength(ObjectDefinition.useOsrsIndex()) > overlayId - 2 && !FloorOverlay.getFloor(overlayId - 1, ObjectDefinition.useOsrsIndex()).occludeOverlay)
									hide_underlay = false;

								if (hide_underlay && tileHeightA == tileHeightB && tileHeightA == tileHeightC && tileHeightA == tileHeightD)
									anIntArrayArrayArray135[level][x][z] |= 0x924;

							}
							int minimapRgb = 0;
							if (paletteIndexA != -1)
								minimapRgb = Rasterizer.hsl2rgb[method187(paletteIndexA, 96)];
							if (overlayId == 0) {
								if (disableGroundTextures) {
									underlay_floor_texture = -1;
									scene.method279(level, x, z, 0, 0, -1, tileHeightA, tileHeightB, tileHeightC, tileHeightD, method187(paletteIndexA, tileShadowA), method187(paletteIndexB, tileShadowB), method187(paletteIndexC, tileShadowC), method187(paletteIndexD, tileShadowD), 0, 0, 0, 0, minimapRgb, 0, 0);// last
								} else {
									int underlay_texture_id = 154; // sandy
									underlay_floor_texture = underlay_texture_id;
									scene.method279(level, x, z, 1, 0, underlay_texture_id, tileHeightA, tileHeightB, tileHeightC, tileHeightD, 0, 0, 0, 0, method187(paletteIndexA, tileShadowA), method187(paletteIndexA, tileShadowB), method187(paletteIndexA, tileShadowC), method187(paletteIndexA, tileShadowD), 0, minimapRgb, 0);
								}
							} else {
								int overlayPaletteIndex;
								int overlayRgb;
								int overlayMinimapRgb = 0;
								int shape = tileShapeLayer1[level][x][z] + 1;
								byte angle = tileOrientationLayer1[level][x][z];
								FloorOverlay over = FloorOverlay.getFloor(overlayId - 1, ObjectDefinition.useOsrsIndex());
								int overlay_texture_id = over.groundTextureOverlay;
								if (Client.disableOverlayTexture)
									overlay_texture_id = -1;
								if (overlayId - 1 >= FloorOverlay.getFloorLength(ObjectDefinition.useOsrsIndex())) {
									overlayId = 1;
								}
								if (disableGroundTextures && overlayId >= 51) {
									overlay_texture_id = -1;
									underlay_floor_texture = -1;
								}
								if (over.detailedColor != -1) {
									overlayMinimapRgb = (Rasterizer.hsl2rgb[over.detailedColor] != 1) ? Rasterizer.hsl2rgb[over.detailedColor] : 0;
								}
								if ((disableGroundTextures ? (overlay_texture_id >= 0 && overlay_texture_id < 51) : (overlay_texture_id >= 0))) {
									overlayPaletteIndex = -1;
									if (over.groundColorOverlay != 0xff00ff) {
										overlayPaletteIndex = over.groundColorOverlay;
										overlayRgb = (overlayPaletteIndex != -1 ? Rasterizer.hsl2rgb[overlayPaletteIndex] : 0);
									} else {
										overlayRgb = over.detailedColor;
										overlayPaletteIndex = -2;
									}
								} else if (over.groundColorOverlay == -1) {
									overlayRgb = overlayMinimapRgb;
									overlayPaletteIndex = -2;
									// ?
									if (level > 0)
										underlay_floor_texture = -1;

									overlay_texture_id = -1;
								} else {
									overlayPaletteIndex = over.groundColorOverlay;
									int overlay_floor_texture_color = method185(overlayPaletteIndex, 96);
									overlayRgb = Rasterizer.hsl2rgb[overlay_floor_texture_color];
								}
								scene.method279(level, x, z, shape, angle, overlay_texture_id, tileHeightA, tileHeightB, tileHeightC, tileHeightD, method187(paletteIndexA, tileShadowA), method187(paletteIndexA, tileShadowB), method187(paletteIndexA, tileShadowC), method187(paletteIndexA, tileShadowD), method185(overlayPaletteIndex, tileShadowA), method185(overlayPaletteIndex, tileShadowB), method185(overlayPaletteIndex, tileShadowC), method185(overlayPaletteIndex, tileShadowD), minimapRgb, overlayRgb, underlay_floor_texture);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			/*
			 * Debug any tile errors with textures, colors,
			 * etc
			 */
			e.printStackTrace();
		}
	}

    public static void clearRegion(int blockZ, int blockH, int l, int i1) {
        for (int tileZ = blockZ; tileZ <= blockZ + blockH; tileZ++) {
            for (int tileX = i1; tileX <= i1 + l; tileX++)
                if (tileX >= 0 && tileX < sizeX && tileZ >= 0 && tileZ < sizeZ) {
                    objectShadowData[0][tileX][tileZ] = 127;
                    if (tileX == i1 && tileX > 0)
                        tileHeight[0][tileX][tileZ] = tileHeight[0][tileX - 1][tileZ];
                    if (tileX == i1 + l && tileX < sizeX - 1)
                        tileHeight[0][tileX][tileZ] = tileHeight[0][tileX + 1][tileZ];
                    if (tileZ == blockZ && tileZ > 0)
                        tileHeight[0][tileX][tileZ] = tileHeight[0][tileX][tileZ - 1];
                    if (tileZ == blockZ + blockH && tileZ < sizeZ - 1)
                        tileHeight[0][tileX][tileZ] = tileHeight[0][tileX][tileZ + 1];
                }

        }
    }

    private static void addObject(int z, WorldController worldController,
                           TileSetting tileSetting, int j, int k, int x, int objectID, int angle) {
        if (lowMem && (tileFlags[0][x][z] & 2) == 0) {
            if ((tileFlags[k][x][z] & 0x10) != 0)
                return;
            if (getLogicHeight(z, k, x) != anInt131)
                return;
        }
        if (k < setZ)
            setZ = k;
        int k1 = tileHeight[k][x][z];
        int l1 = tileHeight[k][x + 1][z];
        int i2 = tileHeight[k][x + 1][z + 1];
        int j2 = tileHeight[k][x][z + 1];
        int k2 = k1 + l1 + i2 + j2 >> 2;
        ObjectDefinition object = ObjectDefinition.forID(objectID);
        long l2 = x | (z << 7) | (j << 14) | (angle << 20) + 0x40000000;
        if (object.hasActions == 0) {
            l2 |= ~0x7fffffffffffffffL;
        }
        if (object.supportItems == 1) {
        	l2 |= 0x400000L;
        }
        l2 |= (long) objectID << 32;
        if (j == 22) {
            if (lowMem && object.hasActions == 0 && !object.groundObstructive)
                return;
            Entity obj;
            if (object.animationId == -1 && object.alternativeIDS == null)
                obj = object.generateModel(22, angle, k1, l1, i2, j2);
            else
                obj = new ObjectInstance(objectID, angle, 22, l1, i2, k1, j2,
                        object.animationId, true);
            worldController.addGroundDecoration(k, k2, z, obj, l2, x);
            if (object.isUnwalkable && object.hasActions == 1 && tileSetting != null)
                tileSetting.orClipTableSET(z, x);
            return;
        }
        if (j == 10 || j == 11) {
            Entity obj1;
            if (object.animationId == -1 && object.alternativeIDS == null)
                obj1 = object.generateModel(10, angle, k1, l1, i2, j2);
            else
                obj1 = new ObjectInstance(objectID, angle, 10, l1, i2, k1, j2,
                        object.animationId, true);
            if (obj1 != null) {
                int i5 = 0;
                if (j == 11)
                    i5 += 256;
                int j4;
                int l4;
                if (angle == 1 || angle == 3) {
                    j4 = object.height;
                    l4 = object.width;
                } else {
                    j4 = object.width;
                    l4 = object.height;
                }
                if (worldController.addEntityB(l2, k2, l4, obj1,
                        j4, k, i5, z, x)
						&& object.castsShadow) {
					int l5 = 15;
					if (obj1 instanceof Model) {
						l5 = ((Model) obj1).getLowestX() / 4;
						if (l5 > 30) {
							l5 = 30;
						}
					}
					for (int j5 = 0; j5 <= j4; j5++) {
						for (int k5 = 0; k5 <= l4; k5++) {
							if (l5 > objectShadowData[k][x + j5][z + k5])
								objectShadowData[k][x + j5][z + k5] = (byte) l5;
						}
					}
				}
            }
            if (object.isUnwalkable && tileSetting != null)
                tileSetting.method212(object.impenetrable, object.width,
                        object.height, x, z, angle);
            return;
        }
        if (j >= 12) {
            Entity obj2;
            if (object.animationId == -1 && object.alternativeIDS == null)
                obj2 = object.generateModel(j, angle, k1, l1, i2, j2);
            else
                obj2 = new ObjectInstance(objectID, angle, j, l1, i2, k1, j2,
                        object.animationId, true);
            worldController.addEntityB(l2, k2, 1, obj2, 1, k,
                    0, z, x);
            if (j >= 12 && j <= 17 && j != 13 && k > 0)
                anIntArrayArrayArray135[k][x][z] |= 0x924;
            if (object.isUnwalkable && tileSetting != null)
                tileSetting.method212(object.impenetrable, object.width,
                        object.height, x, z, angle);
            return;
        }
        if (j == 0) {
            Entity obj3;
            if (object.animationId == -1 && object.alternativeIDS == null)
                obj3 = object.generateModel(0, angle, k1, l1, i2, j2);
            else
                obj3 = new ObjectInstance(objectID, angle, 0, l1, i2, k1, j2,
                        object.animationId, true);
            worldController.addWallObject(anIntArray152[angle], obj3,
                    l2, z, x, null, k2, 0, k);
            if (angle == 0) {
                if (object.castsShadow) {
                    objectShadowData[k][x][z] = 50;
                    objectShadowData[k][x][z + 1] = 50;
                }
                if (object.occludes)
                    anIntArrayArrayArray135[k][x][z] |= 0x249;
            } else if (angle == 1) {
                if (object.castsShadow) {
                    objectShadowData[k][x][z + 1] = 50;
                    objectShadowData[k][x + 1][z + 1] = 50;
                }
                if (object.occludes)
                    anIntArrayArrayArray135[k][x][z + 1] |= 0x492;
            } else if (angle == 2) {
                if (object.castsShadow) {
                    objectShadowData[k][x + 1][z] = 50;
                    objectShadowData[k][x + 1][z + 1] = 50;
                }
                if (object.occludes)
                    anIntArrayArrayArray135[k][x + 1][z] |= 0x249;
            } else if (angle == 3) {
                if (object.castsShadow) {
                    objectShadowData[k][x][z] = 50;
                    objectShadowData[k][x + 1][z] = 50;
                }
                if (object.occludes)
                    anIntArrayArrayArray135[k][x][z] |= 0x492;
            }
            if (object.isUnwalkable && tileSetting != null)
                tileSetting.method211(z, angle, x, j, object.impenetrable);
            if (object.decorDisplacement != 16)
                worldController.method290(z, object.decorDisplacement, x, k);
            return;
        }
        if (j == 1) {
            Entity obj4;
            if (object.animationId == -1 && object.alternativeIDS == null)
                obj4 = object.generateModel(1, angle, k1, l1, i2, j2);
            else
                obj4 = new ObjectInstance(objectID, angle, 1, l1, i2, k1, j2,
                        object.animationId, true);
            worldController.addWallObject(anIntArray140[angle], obj4,
                    l2, z, x, null, k2, 0, k);
            if (object.castsShadow)
                if (angle == 0)
                    objectShadowData[k][x][z + 1] = 50;
                else if (angle == 1)
                    objectShadowData[k][x + 1][z + 1] = 50;
                else if (angle == 2)
                    objectShadowData[k][x + 1][z] = 50;
                else if (angle == 3)
                    objectShadowData[k][x][z] = 50;
            if (object.isUnwalkable && tileSetting != null)
                tileSetting.method211(z, angle, x, j, object.impenetrable);
            return;
        }
        if (j == 2) {
            int i3 = angle + 1 & 3;
            Entity obj11;
            Entity obj12;
            if (object.animationId == -1 && object.alternativeIDS == null) {
                obj11 = object.generateModel(2, 4 + angle, k1, l1, i2, j2);
                obj12 = object.generateModel(2, i3, k1, l1, i2, j2);
            } else {
                obj11 = new ObjectInstance(objectID, 4 + angle, 2, l1, i2, k1, j2,
                        object.animationId, true);
                obj12 = new ObjectInstance(objectID, i3, 2, l1, i2, k1, j2,
                        object.animationId, true);
            }
            worldController.addWallObject(anIntArray152[angle], obj11,
                    l2, z, x, obj12, k2, anIntArray152[i3],
                    k);
            if (object.occludes)
                if (angle == 0) {
                    anIntArrayArrayArray135[k][x][z] |= 0x249;
                    anIntArrayArrayArray135[k][x][z + 1] |= 0x492;
                } else if (angle == 1) {
                    anIntArrayArrayArray135[k][x][z + 1] |= 0x492;
                    anIntArrayArrayArray135[k][x + 1][z] |= 0x249;
                } else if (angle == 2) {
                    anIntArrayArrayArray135[k][x + 1][z] |= 0x249;
                    anIntArrayArrayArray135[k][x][z] |= 0x492;
                } else if (angle == 3) {
                    anIntArrayArrayArray135[k][x][z] |= 0x492;
                    anIntArrayArrayArray135[k][x][z] |= 0x249;
                }
            if (object.isUnwalkable && tileSetting != null)
                tileSetting.method211(z, angle, x, j, object.impenetrable);
            if (object.decorDisplacement != 16)
                worldController.method290(z, object.decorDisplacement, x, k);
            return;
        }
        if (j == 3) {
        	Entity obj5;
            if (object.animationId == -1 && object.alternativeIDS == null)
                obj5 = object.generateModel(3, angle, k1, l1, i2, j2);
            else
                obj5 = new ObjectInstance(objectID, angle, 3, l1, i2, k1, j2,
                        object.animationId, true);
            worldController.addWallObject(anIntArray140[angle], obj5,
                    l2, z, x, null, k2, 0, k);
            if (object.castsShadow)
                if (angle == 0)
                    objectShadowData[k][x][z + 1] = 50;
                else if (angle == 1)
                    objectShadowData[k][x + 1][z + 1] = 50;
                else if (angle == 2)
                    objectShadowData[k][x + 1][z] = 50;
                else if (angle == 3)
                    objectShadowData[k][x][z] = 50;
            if (object.isUnwalkable && tileSetting != null)
                tileSetting.method211(z, angle, x, j, object.impenetrable);
            return;
        }
        if (j == 9) {
        	Entity obj6;
            if (object.animationId == -1 && object.alternativeIDS == null)
                obj6 = object.generateModel(j, angle, k1, l1, i2, j2);
            else
                obj6 = new ObjectInstance(objectID, angle, j, l1, i2, k1, j2,
                        object.animationId, true);
            worldController.addEntityB(l2, k2, 1, obj6, 1, k,
                    0, z, x);
            if (object.isUnwalkable && tileSetting != null)
                tileSetting.method212(object.impenetrable, object.width,
                        object.height, x, z, angle);
            return;
        }
        if (object.adjustToTerrain)
            if (angle == 1) {
                int j3 = j2;
                j2 = i2;
                i2 = l1;
                l1 = k1;
                k1 = j3;
            } else if (angle == 2) {
                int k3 = j2;
                j2 = l1;
                l1 = k3;
                k3 = i2;
                i2 = k1;
                k1 = k3;
            } else if (angle == 3) {
                int l3 = j2;
                j2 = k1;
                k1 = l1;
                l1 = i2;
                i2 = l3;
            }
        if (j == 4) {
        	Entity obj7;
            if (object.animationId == -1 && object.alternativeIDS == null)
                obj7 = object.generateModel(4, 0, k1, l1, i2, j2);
            else
                obj7 = new ObjectInstance(objectID, 0, 4, l1, i2, k1, j2,
                        object.animationId, true);
            worldController.addWallDecoration(l2, z, angle * 512, k, 0, k2,
                    obj7, x, 0, anIntArray152[angle]);
            return;
        }
        if (j == 5) {
            int i4 = 16;
            long k4 = worldController.method300(k, x, z);
            if (k4 != 0L) {
                i4 = ObjectDefinition.forID((int) (k4 >>> 32) & 0x7fffffff).decorDisplacement;
            }
            Entity obj13;
            if (object.animationId == -1 && object.alternativeIDS == null)
                obj13 = object.generateModel(4, 0, k1, l1, i2, j2);
            else
                obj13 = new ObjectInstance(objectID, 0, 4, l1, i2, k1, j2,
                        object.animationId, true);
            worldController.addWallDecoration(l2, z, angle * 512, k, anIntArray137[angle]
                    * i4, k2, obj13, x, anIntArray144[angle]
                    * i4, anIntArray152[angle]);
            return;
        }
        if (j == 6) {
        	Entity obj8;
            if (object.animationId == -1 && object.alternativeIDS == null)
                obj8 = object.generateModel(4, 0, k1, l1, i2, j2);
            else
                obj8 = new ObjectInstance(objectID, 0, 4, l1, i2, k1, j2,
                        object.animationId, true);
            worldController.addWallDecoration(l2, z, angle, k, 0, k2, obj8,
                    x, 0, 256);
            return;
        }
        if (j == 7) {
        	Entity obj9;
            if (object.animationId == -1 && object.alternativeIDS == null)
                obj9 = object.generateModel(4, 0, k1, l1, i2, j2);
            else
                obj9 = new ObjectInstance(objectID, 0, 4, l1, i2, k1, j2,
                        object.animationId, true);
            worldController.addWallDecoration(l2, z, angle, k, 0, k2, obj9,
                    x, 0, 512);
            return;
        }
        if (j == 8) {
        	Entity obj10;
            if (object.animationId == -1 && object.alternativeIDS == null)
                obj10 = object.generateModel(4, 0, k1, l1, i2, j2);
            else
                obj10 = new ObjectInstance(objectID, 0, 4, l1, i2, k1, j2,
                        object.animationId, true);
            worldController.addWallDecoration(l2, z, angle, k, 0, k2,
                    obj10, x, 0, 768);
        }
        // }
    }

    public static void loadTerrainSubBlock(int i, int j, TileSetting tileSettings[], int l,
                                          int i1, byte abyte0[], int j1, int k1, int l1) {
        for (int i2 = 0; i2 < 8; i2++) {
            for (int j2 = 0; j2 < 8; j2++)
                if (l + i2 > 0 && l + i2 < 103 && l1 + j2 > 0 && l1 + j2 < 103)
                    tileSettings[k1].clipData[l + i2][l1 + j2] &= 0xfeffffff;

        }
        Stream stream = new Stream(abyte0);
        for (int l2 = 0; l2 < 4; l2++) {
            for (int i3 = 0; i3 < 64; i3++) {
                for (int j3 = 0; j3 < 64; j3++)
                    if (l2 == i && i3 >= i1 && i3 < i1 + 8 && j3 >= j1
                            && j3 < j1 + 8)
                        loadTerrainTile(l1 + MapUtility.rotateTerrainBlockZ(j3 & 7, j, i3 & 7), 0,
                                stream,
                                l + MapUtility.rotateTerrainBlockX(j, j3 & 7, i3 & 7), k1, j,
                                0);
                    else
                        loadTerrainTile(-1, 0, stream, -1, 0, 0, 0);

            }

        }

    }

    public static void loadTerrainBlock(byte abyte0[], int i, int j, int k, int l,
                                       TileSetting tileSetting[]) {
        for (int i1 = 0; i1 < 4; i1++) {
            for (int j1 = 0; j1 < 64; j1++) {
                for (int k1 = 0; k1 < 64; k1++)
                    if (j + j1 > 0 && j + j1 < 103 && i + k1 > 0
                            && i + k1 < 103)
                        tileSetting[i1].clipData[j + j1][i + k1] &= 0xfeffffff;

            }

        }

        Stream stream = new Stream(abyte0);
        for (int l1 = 0; l1 < 4; l1++) {
            for (int i2 = 0; i2 < 64; i2++) {
                for (int j2 = 0; j2 < 64; j2++)
                    loadTerrainTile(j2 + i, l, stream, i2 + j, l1, 0, k);

            }

        }
    }

    private static void loadTerrainTile(int i, int j, Stream stream, int k, int l, int i1,
                                 int k1) {
        if (k >= 0 && k < 104 && i >= 0 && i < 104) {
            tileFlags[l][k][i] = 0;
            do {
                int l1 = stream.readUnsignedByte();
                if (l1 == 0)
                    if (l == 0) {
                        tileHeight[0][k][i] = -method172(0xe3b7b
                                + k + k1, 0x87cce + i + j) * 8;
                        return;
                    } else {
                        tileHeight[l][k][i] = tileHeight[l - 1][k][i] - 240;
                        return;
                    }
                if (l1 == 1) {
                    int j2 = stream.readUnsignedByte();
                    if (j2 == 1)
                        j2 = 0;
                    if (l == 0) {
                        tileHeight[0][k][i] = -j2 * 8;
                        return;
                    } else {
                        tileHeight[l][k][i] = tileHeight[l - 1][k][i]
                                - j2 * 8;
                        return;
                    }
                }
                if (l1 <= 49) {
                    tileTypeLayer1[l][k][i] = stream.readSignedByte();
                    tileShapeLayer1[l][k][i] = (byte) ((l1 - 2) / 4);
                    tileOrientationLayer1[l][k][i] = (byte) ((l1 - 2) + i1 & 3);
                } else if (l1 <= 81)
                    tileFlags[l][k][i] = (byte) (l1 - 49);
                else
                    tileTypeLayer0[l][k][i] = (byte) (l1 - 81);
            } while (true);
        }
        do {
            int i2 = stream.readUnsignedByte();
            if (i2 == 0)
                break;
            if (i2 == 1) {
                stream.readUnsignedByte();
                return;
            }
            if (i2 <= 49)
                stream.readUnsignedByte();
        } while (true);
    }

    private static int getLogicHeight(int i, int j, int k) {
        if ((tileFlags[j][k][i] & 8) != 0)
            return 0;
        if (j > 0 && (tileFlags[1][k][i] & 2) != 0)
            return j - 1;
        return j;
    }

    public static void loadObjectSubBlock(TileSetting aclass11[],
                                         WorldController worldController, int i, int j, int k, int l,
			byte abyte0[], int i1, int j1, int k1) {
		Stream stream = new Stream(abyte0);
		int l1 = -1;
		for (;;) {
			int i2 = stream.method422();
			if (i2 == 0)
				break;
			l1 += i2;
			int j2 = 0;
			for (;;) {
				int k2 = stream.method422();
				if (k2 == 0)
					break;
				j2 += k2 - 1;
				int l2 = j2 & 0x3f;
				int i3 = j2 >> 6 & 0x3f;
				int j3 = j2 >> 12;
				int k3 = stream.readUnsignedByte();
				int l3 = k3 >> 2;
				int i4 = k3 & 3;
				if (j3 == i && i3 >= i1 && i3 < i1 + 8 && l2 >= k && l2 < k + 8) {
					ObjectDefinition class46 = ObjectDefinition.forID(l1);
					int j4 = j + MapUtility.rotateObjectBlockX(j1, class46.height, i3 & 7, l2 & 7, class46.width, i4);
					int k4 = k1 + MapUtility.rotateObjectBlockZ(l2 & 7, class46.height, j1, class46.width, i3 & 7, i4);
					if (j4 > 0 && k4 > 0 && j4 < 103 && k4 < 103) {
						int l4 = j3;
						if ((tileFlags[1][j4][k4] & 2) == 2)
							l4--;
						TileSetting class11 = null;
						if (l4 >= 0)
							class11 = aclass11[l4];
						addObject(k4, worldController, class11, l3, l, j4, l1, i4 + j1 & 3);
					}
				}
			}
		}
	}

    private static int method185(int i, int j) {
        if (i == -2)
            return 0xbc614e;
        if (i == -1) {
            if (j < 0)
                j = 0;
            else if (j > 127)
                j = 127;
            j = 127 - j;
            return j;
        }
        j = (j * (i & 0x7f)) / 128;
        if (j < 2)
            j = 2;
        else if (j > 126)
            j = 126;
        return (i & 0xff80) + j;
    }

    public static void loadObjectBlock(int i, TileSetting aclass11[], int j,
			WorldController worldController, byte abyte0[], int baseX, int baseY) {
		Stream stream = new Stream(abyte0);
		int l = -1;
		for (;;) {
			int i1 = stream.method422();
			if (i1 == 0) {
				break;
			}
			l += i1;
			int j1 = 0;
			for (;;) {
				int k1 = stream.method422();
				if (k1 == 0)
					break;
				j1 += k1 - 1;
				int l1 = j1 & 0x3f;
				int i2 = j1 >> 6 & 0x3f;
				int j2 = j1 >> 12;
				int k2 = stream.readUnsignedByte();
				int l2 = k2 >> 2;
				int i3 = k2 & 3;
				int j3 = i2 + i;
				int k3 = l1 + j;
				if (j3 > 0 && k3 > 0 && j3 < 103 && k3 < 103 && j2 < aclass11.length) {
					int l3 = j2;
					if ((tileFlags[1][j3][k3] & 2) == 2)
						l3--;
					TileSetting class11 = null;
					if (l3 >= 0)
						class11 = aclass11[l3];
					addObject(k3, worldController, class11, l2, j2, j3, l, i3);
				}
			}
		}
	}

}
