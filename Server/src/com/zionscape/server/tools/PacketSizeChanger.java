package com.zionscape.server.tools;

public class PacketSizeChanger {

	public static final int[] PACKET_SIZES = {0,0,-1,1,-1,0,0,0,0,0,0,0,0,0,8,0,6,2,2,0,0,2,0,6,0,12,0,0,-1,0,0,0,0,0,0,8,4,0,0,2,2,6,0,6,0,-1,0,0,0,0,0,0,0,12,0,0,0,8,8,12,8,8,0,0,0,0,0,0,0,0,6,0,2,2,8,6,0,-1,0,6,0,0,0,0,0,1,4,6,0,0,0,0,0,0,0,3,0,0,-1,0,0,25,0,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,6,0,0,1,0,6,0,0,0,-1,-1,2,6,0,6,6,8,0,6,0,0,0,2,0,0,0,0,0,6,0,0,0,0,0,0,1,2,0,2,6,0,0,0,0,0,0,0,-1,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,0,3,0,2,0,0,8,1,0,0,12,0,0,0,0,0,0,0,2,0,0,0,2,8,0,0,4,10,4,0,0,4,11,8,0,0,10,0,0,0,0,0,0,0,-1,0,6,0,1,0,0,0,6,0,6,8,1,0,0,4,0,0,0,0,-1,0,-1,6,0,0,6,6,0,0,0,};

	public static void main(String[] unused) {

		PACKET_SIZES[69] = 3;

		for (int i : PACKET_SIZES) {
			System.out.print(i + ",");
		}

	}


}
