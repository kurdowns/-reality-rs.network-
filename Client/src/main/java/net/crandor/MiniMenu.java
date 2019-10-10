package net.crandor;

public class MiniMenu {

	public static NodeList options = new NodeList();
	public static NodeList highPriorityOptions = new NodeList();
	public static int optionCount;
	public static boolean open;
	public static MiniMenuOption cancelOption;

	public static void init() {
		cancelOption = new MiniMenuOption("Cancel", null, 1107, 0L, 0, 0);
	}

	public static void addOption(String option, String base, int uid) {
		addOption(option, base, uid, 0L, 0, 0);
	}

	public static void addOption(String option, int uid) {
		addOption(option, null, uid, 0L, 0, 0);
	}

	public static void addOption(String option, int uid, long data1, int data2, int data3) {
		addOption(option, null, uid, data1, data2, data3);
	}

	public static void addOption(String option, String base, int uid, long data1, int data2, int data3) {
		if (!open && optionCount < 500) {
			options.insertHead(new MiniMenuOption(option, base, uid, data1, data2, data3));
			optionCount++;
		}
	}

	public static String getOption(MiniMenuOption miniMenuOption) {
		if (miniMenuOption.base == null || miniMenuOption.base.length() <= 0) {
			return miniMenuOption.name;
		}

		return (miniMenuOption.name + " " + miniMenuOption.base);
	}

	public static void clear() {
		options.clear();
		options.insertHead(cancelOption);
		optionCount = 1;
	}

}
