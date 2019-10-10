package net.crandor;

final class MapUtility {

	public static int rotateTerrainBlockX(int orientation, int j, int k) {
		orientation &= 3;
		if (orientation == 0)
			return k;
		if (orientation == 1)
			return j;
		if (orientation == 2)
			return 7 - k;
		else
			return 7 - j;
	}

	public static int rotateTerrainBlockZ(int i, int orientation, int l) {
		orientation &= 3;
		if (orientation == 0)
			return i;
		if (orientation == 1)
			return 7 - l;
		if (orientation == 2)
			return 7 - i;
		else
			return l;
	}

	public static int rotateObjectBlockX(int orientation, int sizeZ, int locationX, int locationZ, int sizeX, int locationAngle) {
		if ((locationAngle & 0x1) != 0) {
			int oldX = sizeX;
			sizeX = sizeZ;
			sizeZ = oldX;
		}
		orientation &= 3;
		if (orientation == 0)
			return locationX;
		if (orientation == 1)
			return locationZ;
		if (orientation == 2)
			return 7 - locationX - (sizeX - 1);
		return 7 - locationZ - (sizeZ - 1);
	}

	public static int rotateObjectBlockZ(int locationZ, int sizeZ, int orientation, int sizeX, int locationX, int locationAngle) {
		if ((locationAngle & 0x1) != 0) {
			int oldX = sizeX;
			sizeX = sizeZ;
			sizeZ = oldX;
		}
		orientation &= 3;
		if (orientation == 0)
			return locationZ;
		if (orientation == 1)
			return 7 - locationX - (sizeX - 1);
		if (orientation == 2)
			return 7 - locationZ - (sizeZ - 1);
		return locationX;
	}

}
