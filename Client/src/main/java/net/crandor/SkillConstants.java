package net.crandor;

final class SkillConstants {

	public static final int skillsCount = 25;
	public static final boolean[] skillEnabled = { true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, true, false, false, false, false };

	public static String goalType = "Target Level: ";
	public static int selectedSkillId = -1;
	public static final int[][] goalData = new int[skillsCount][3];
	public static final int[] HOVER_IDS = { 4040, 4076, 4112, 4046, 4082, 4118, 4052, 4088, 4124, 4058, 4094, 4130, 4064, 4100, 4136, 4070, 4106, 4142, 4160, 2832, 13917, 19005, 19006, 19007 };
	public static final int[] SKILL_IDS = { 0, 3, 14, 2, 16, 13, 1, 15, 10, 4, 17, 7, 5, 12, 11, 6, 9, 8, 20, 18, 19, 21, 22, 23, 24 };

	static {
		for (int i = 0; i < goalData.length; i++) {
			goalData[i][0] = -1;
			goalData[i][1] = -1;
			goalData[i][2] = -1;
		}
	}

	public static int skillHoverIds(int id) {
		for (int hover = 0; hover < HOVER_IDS.length; hover++) {
			if (HOVER_IDS[hover] == id) {
				return HOVER_IDS[hover];
			}
		}
		return 0;
	}

	public static int skillButtonIds(int id) {
		for (int button = 0; button < HOVER_IDS.length; button++) {
			if (HOVER_IDS[button] == id) {
				return SKILL_IDS[button];
			}
		}
		return 0;
	}

	public static int skillIdNames(String name) {
		name = name.toLowerCase().trim();
		for (int i = 0; i < Client.skillNames.length; i++) {
			if (Client.skillNames[i].equalsIgnoreCase(name)) {
				return SKILL_IDS[i];
			}
		}
		return 0;
	}
}