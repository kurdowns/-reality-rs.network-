package net.crandor;

class Constants {

static final int REPACK_INDEX = -1;
	static final String CLIENT_NAME = "Varrock";
	static final String CLIENT_VERSION = "0.0.1";
	static final int HITMARKS_317 = 0;
	static final int HITMARKS_562 = 1;
	static final int HITMARKS_634 = 2;
	static final int DETAIL_LD = 0;
	static final int DETAIL_SD = 1;
	static final int DETAIL_HD = 2;
	static final int SCREEN_FIXED = 0;
	static final int SCREEN_RESIZABLE = 1;
	static final int SCREEN_FULL = 2;
	//public static String SERVER_IP = "127.0.0.1";
	static boolean isLocal = true;
	//static boolean CACHE = true;
	static String SERVER_IP = isLocal ? "127.0.0.1" : "185.218.235.124";
	//static String RANDOM = CACHE ? "185.218.235.14" : "127.0.0.1";
}