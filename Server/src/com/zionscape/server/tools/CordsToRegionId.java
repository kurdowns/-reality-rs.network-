package com.zionscape.server.tools;

public class CordsToRegionId {

	public static void main(String[] args) {
		int regionX = 3109 >> 3;
		int regionY = 10152 >> 3;
		int regionId = (regionX / 8 << 8) + regionY / 8;

		System.out.println("region id: " + regionId);
	}

}
