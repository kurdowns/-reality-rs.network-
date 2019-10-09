package com.zionscape.server.model.npcs.drops;

public class Rates {

	public static final Rate ALWAYS = new Rate(0);
	public static final Rate COMMON = new Rate(2);
	public static final Rate OFTEN = new Rate(30);
	public static final Rate UNCOMMON = new Rate(70);
	public static final Rate SEMI_RARE = new Rate(140);
	public static final Rate RARE = new Rate(200);
	public static final Rate RARER = new Rate(300);
	public static final Rate VERY_RARE = new Rate(450);
	public static final Rate SUPER_RARE = new Rate(800);
	public static final Rate IMPOSSIBLE = new Rate(1500);
}