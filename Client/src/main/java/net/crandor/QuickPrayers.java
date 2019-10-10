package net.crandor;

import java.util.Arrays;

public class QuickPrayers {

	/*
	 * Var ids for regular prayers [630,657]
	 */

	public static final int[] TYPE_6_PRAYER_CURSE_INDEX = { 17, 18 };
	public static final int[] TYPE_1718_PRAYER_CURSE_INDEX = { 6, 7, 8, 9, 17, 18 };
	public static final int[] TYPE_19_PRAYER_CURSE_INDEX = { 1, 2, 3, 4, 10, 11, 12, 13, 14, 15, 16, 19 };
	public static final int[] curseLevelRequirements = { 50, 50, 52, 54, 56, 59, 62, 65, 68, 71, 74, 76, 78, 80, 82, 84, 86, 89, 92, 95 };
	public static final String[] curseName = { "Protect Item", "Sap Warrior", "Sap Ranger", "Sap Mage", "Sap Spirit", "Berserker", "Deflect Summoning", "Deflect Magic", "Deflect Missiles", "Deflect Melee", "Leech Attack", "Leech Ranged", "Leech Magic", "Leech Defence", "Leech Strength", "Leech Energy", "Leech Special Attack", "Wrath", "Soul Split", "Turmoil" };
	public static final int[] quickConfigIDs = { 630, 631, 632, 633, 634, 635, 636, 637, 638, 639, 640, 641, 642, 643, 644, 645, 646, 647, 648, 649, 650, 651, 652, 653, 654, 655, 656, 657 };
	public static final int[] prayerLevelRequirements = { 1, 4, 7, 8, 9, 10, 13, 16, 19, 22, 25, 26, 27, 28, 31, 34, 37, 40, 43, 44, 45, 46, 49, 52, 60, 70, 74, 77 };
	public static final String[] prayerName = { "Thick Skin", "Burst of Strength", "Clarity of Thought", "Sharp Eye", "Mystic Will", "Rock Skin", "Superhuman Strength", "Improved Reflexes", "Rapid Restore", "Rapid Heal", "Protect Item", "Hawk Eye", "Mystic Lore", "Steel Skin", "Ultimate Strength", "Incredible Reflexes", "Protect from Magic", "Protect from Missiles", "Protect from Melee", "Eagle Eye", "Mystic Might", "Retribution", "Redemption", "Smite", "Chivalry", "Piety", "Rigour", "Augury" };
	public static final int[] defPray = { 0, 5, 13, 24, 25, 26, 27 };
	public static final int[] strPray = { 1, 3, 4, 6, 11, 12, 14, 19, 20, 24, 25 };
	public static final int[] atkPray = { 2, 3, 4, 7, 11, 12, 15, 19, 20, 24, 25 };
	public static final int[] rangeAndMagePray = { 3, 4, 11, 12, 19, 20, 24, 25, 26, 27 };
	public static final int[] rangeAndMagePrayOff = { 1, 2, 3, 4, 6, 7, 11, 12, 14, 15, 19, 20, 24, 25, 26, 27 };
	public static final int[] headPray = { 16, 17, 18, 21, 22, 23 };
	public static final int[] superiorPray = { 0, 1, 2, 3, 4, 5, 6, 7, 11, 12, 13, 14, 15, 19, 20, 24, 25, 26, 27 };
	public static final int[][] prayer = { defPray, strPray, atkPray, rangeAndMagePray, headPray };
	public static final int[] sapCurse = { 1, 2, 3, 4, 19 };
	public static final int[] leechCurse = { 10, 11, 12, 13, 14, 15, 16, 19 };
	private static final int[] deflectCurse = { 7, 8, 9, 17, 18 };
	public static final int[] OLD_PRAYER_INTERFACE_IDS = { 5609, 5610, 5611, 18000, 18002, 5612, 5613, 5614, 5615, 5616, 5617, 18004, 18006, 5618, 5619, 5620, 5621, 5622, 5623, 18008, 18010, 683, 684, 685, 18012, 18014 };
	public static final int[] PRAYER_INTERFACE_IDS = { 25000, 25002, 25004, 25006, 25008, 25010, 25012, 25014, 25016, 25018, 25020, 25022, 25024, 25026, 25028, 25030, 25032, 25034, 25036, 25038, 25040, 25042, 25044, 25046, 25048, 25050, 25112, 25106 };
	public static final int[] CURSE_INTERFACE_IDS = { 22503, 22505, 22507, 22509, 22511, 22513, 22515, 22517, 22519, 22521, 22523, 22525, 22527, 22529, 22531, 22533, 22535, 22537, 22539, 22541 };
	public static final int[] QUICK_PRAYER_SELECT_INTERFACE_IDS = { 17202, 17203, 17204, 17205, 17206, 17207, 17208, 17209, 17210, 17211, 17212, 17213, 17214, 17215, 17216, 17217, 17218, 17219, 17220, 17221, 17222, 17223, 17223, 17224, 17225, 17226, 17227, 17228, 15891 };
	public static final int PRAYER_COUNT = 28;
	public static final int CURSE_COUNT = 20;
	public static final int PRAYER_INTERFACE = 5608;
	public static final int CURSE_INTERFACE = 22500;
	public static final int QUICK_PRAYER_INTERFACE = 17200;
	public static final int QUICK_CURSE_INTERFACE = 17234;

	public static int prayerInterfaceId;// Your current prayer interface that server sent.
	public static boolean resettedQuickPrayers;
	public static boolean quickPrayersActive;
	public static int[] quickCurses = new int[CURSE_COUNT];
	public static int[] quickPrayers = new int[PRAYER_COUNT];

	public static void turnOffPrayers(boolean curses) {
		int count = curses ? CURSE_COUNT : PRAYER_COUNT;

		for (int index = 0; index < count; index++) {
			int componentId = curses ? CURSE_INTERFACE_IDS[index] : PRAYER_INTERFACE_IDS[index];
			RSInterface component = RSInterface.interfaceCache[componentId];
			if (component.valueIndexArray != null && component.valueIndexArray[0][0] == 5) {
				int varId = component.valueIndexArray[0][1];
				if (Client.instance.playerVariables[varId] == 1) {
					Client.instance.sendPacket185(componentId);
				}
			}
		}
	}

	public static void turnOffQuickPrayerPreset() {
		for (int index = 0; index < PRAYER_COUNT; index++) {
			int componentId = QUICK_PRAYER_SELECT_INTERFACE_IDS[index];
			RSInterface component = RSInterface.interfaceCache[componentId];
			if (component.valueIndexArray != null && component.valueIndexArray[0][0] == 5) {
				int varId = component.valueIndexArray[0][1];
				if (Client.instance.playerVariables[varId] == 1) {
					Client.instance.sendPacket185(componentId);
				}
			}
		}
	}

	public static void swapPrayerInterfaces() {
		int currentInterface = Client.tabInterfaceIDs[Client.PRAYER_TAB];
		int interfaceToSet = PRAYER_INTERFACE;

		if (currentInterface == PRAYER_INTERFACE) {
			interfaceToSet = QUICK_PRAYER_INTERFACE;
		} else if (currentInterface == QUICK_PRAYER_INTERFACE) {
			interfaceToSet = PRAYER_INTERFACE;
		} else if (currentInterface == CURSE_INTERFACE) {
			interfaceToSet = QUICK_CURSE_INTERFACE;
		} else if (currentInterface == QUICK_CURSE_INTERFACE) {
			interfaceToSet = CURSE_INTERFACE;
		}

		Client.tabInterfaceIDs[Client.PRAYER_TAB] = interfaceToSet;
		Client.setTab(Client.PRAYER_TAB);
	}

	public static void swapToMainPrayerInterface() {
		Client.tabInterfaceIDs[Client.PRAYER_TAB] = prayerInterfaceId;
	}

	public static void handleQuickAidsActive() {
		if (prayerInterfaceId == PRAYER_INTERFACE) {
			if (quickPrayersCount() > 0) {
				if (quickPrayersActive) {
					turnOffPrayers(false);
				} else {
					for (int index = 0; index < PRAYER_COUNT; index++) {
						if (quickPrayers[index] == 1) {

							int componentId = PRAYER_INTERFACE_IDS[index];
							int toggle = Client.instance.playerVariables[RSInterface.interfaceCache[componentId].valueIndexArray[0][1]];

							// check the prayer is off before trying to turn it on
							if (toggle == 0) {
								Client.instance.sendPacket185(componentId);
							}
						}
					}
				}
				quickPrayersActive = !quickPrayersActive;
			} else {
				// Check if quick prayers are active when we unselected all quick prayers, if so turn it off. Else send a message.
				if (quickPrayersActive) {
					turnOffPrayers(false);
					quickPrayersActive = false;
				} else {
					Client.instance.pushMessage("You don't have any quick prayers selected.", ChatMessage.GAME_MESSAGE);
					Client.instance.pushMessage("Right-click the Prayer button next to the minimap to select some.", ChatMessage.GAME_MESSAGE);
				}
			}

			return;
		}

		if (prayerInterfaceId == CURSE_INTERFACE) {
			if (quickCursesCount() > 0) {
				if (quickPrayersActive) {
					turnOffPrayers(true);
				} else {
					for (int index = 0; index < CURSE_COUNT; index++) {
						if (quickCurses[index] == 1) {
							int componentId = CURSE_INTERFACE_IDS[index];
							int toggle = Client.instance.playerVariables[RSInterface.interfaceCache[componentId].valueIndexArray[0][1]];

							// check the prayer is off before trying to turn it on
							if (toggle == 0) {
								Client.instance.sendPacket185(componentId);
							}
						}
					}
				}
				quickPrayersActive = !quickPrayersActive;
			} else {
				// Check if quick prayers are active when we unselected all quick prayers, if so turn it off. Else send a message.
				if (quickPrayersActive) {
					turnOffPrayers(true);
					quickPrayersActive = false;
				} else {
					Client.instance.pushMessage("You don't have any quick curses selected.", ChatMessage.GAME_MESSAGE);
					Client.instance.pushMessage("Right-click the Curses button next to the minimap to select some.", ChatMessage.GAME_MESSAGE);
				}
			}

			return;
		}
	}

	public static int quickCursesCount() {
		int count = 0;
		for (int index = 0; index < CURSE_COUNT; index++) {
			if (quickCurses[index] == 1) {
				count++;
			}
		}
		return count;
	}

	public static int quickPrayersCount() {
		int count = 0;
		for (int index = 0; index < PRAYER_COUNT; index++) {
			if (quickPrayers[index] == 1) {
				count++;
			}
		}
		return count;
	}

	public static void configureQuickTicks() {
		if (prayerInterfaceId == PRAYER_INTERFACE) {
			if (quickPrayersCount() > 0) {
				for (int index = 0; index < PRAYER_COUNT; index++) {
					if (quickPrayers[index] == 1) {
						if (Client.instance.statsBase[5] >= prayerLevelRequirements[index]) {
							int[] types = getPrayerTypeForIndex(index);
							if (types != null) {
								for (int g = 0; g < rangeAndMagePray.length; g++) {
									if (index == rangeAndMagePray[g]) {
										types = rangeAndMagePrayOff;
									}
								}
								for (int i = 0; i < types.length; i++) {
									if (index != types[i]) {
										if (index == 24 || index == 25 || index == 26 || index == 27) {
											types = superiorPray;
										}
										quickPrayers[types[i]] = 0;
										Client.instance.playerVariables[quickConfigIDs[types[i]]] = 0;
										Client.instance.postVarpChange(quickConfigIDs[types[i]]);
										Client.reDrawTabArea = true;
									} else {
										quickPrayers[index] = 1;
										Client.instance.playerVariables[quickConfigIDs[index]] = quickPrayers[index];
										Client.instance.postVarpChange(quickConfigIDs[index]);
										Client.reDrawTabArea = true;
									}
								}
							} else {
								quickPrayers[index] = 1;
								Client.instance.playerVariables[quickConfigIDs[index]] = quickPrayers[index];
								Client.instance.postVarpChange(quickConfigIDs[index]);
								Client.reDrawTabArea = true;
							}
						}
					}
				}
			}
		} else if (prayerInterfaceId == CURSE_INTERFACE) {
			if (quickCursesCount() > 0) {
				for (int index = 0; index < CURSE_COUNT; index++) {
					if (quickCurses[index] == 1) {
						if (Client.instance.statsBase[5] >= curseLevelRequirements[index]) {
							int[] types = getCurseTypeForIndex(index);
							if (types != null) {
								for (int i = 0; i < types.length; i++) {
									if (index != types[i]) {
										quickCurses[types[i]] = 0;
										Client.instance.playerVariables[quickConfigIDs[types[i]]] = 0;
										Client.instance.postVarpChange(quickConfigIDs[types[i]]);
										Client.reDrawTabArea = true;
									}
								}
							}
							quickCurses[index] = 1;
							Client.instance.playerVariables[quickConfigIDs[index]] = quickCurses[index];
							Client.instance.postVarpChange(quickConfigIDs[index]);
							Client.reDrawTabArea = true;
						}
					}
				}
			}
		}
	}

	public static int[] getPrayerTypeForIndex(int index) {
		int[] type = null;
		for (int i = 0; i < prayer.length; i++) {
			for (int il = 0; il < prayer[i].length; il++) {
				if (index == prayer[i][il]) {
					type = prayer[i];
				}
			}
		}
		return type;
	}

	public static int[] getCurseTypeForIndex(int index) {
		int[] types = null;
		for (int g = 0; g < leechCurse.length; g++) {
			if (index == leechCurse[g]) {
				types = sapCurse;
			}
		}
		for (int g = 0; g < sapCurse.length; g++) {
			if (index == sapCurse[g]) {
				types = leechCurse;
			}
		}
		for (int g = 0; g < deflectCurse.length; g++) {
			if (index == deflectCurse[g]) {
				types = deflectCurse;
			}
		}
		if (index == 6) {
			types = TYPE_6_PRAYER_CURSE_INDEX;
		}
		if (index == 17 || index == 18) {
			types = TYPE_1718_PRAYER_CURSE_INDEX;
		}
		if (index == 19) {
			types = TYPE_19_PRAYER_CURSE_INDEX;
		}
		return types;
	}

	public static void togglePrayerState(int button) {
		int index;
		if (button == 15891) {
			index = 27;
		} else {
			index = button - 17202;
		}
		if (prayerInterfaceId == PRAYER_INTERFACE) {
			if (Client.instance.statsBase[5] >= prayerLevelRequirements[index]) {
				int[] types = getPrayerTypeForIndex(index);
				if (types != null) {
					for (int g = 0; g < rangeAndMagePray.length; g++) {
						if (index == rangeAndMagePray[g]) {
							types = rangeAndMagePrayOff;
						}
					}
					for (int i = 0; i < types.length; i++) {
						if (index != types[i]) {
							if (index == 24 || index == 25 || index == 26 || index == 27) {
								types = superiorPray;
							}
							quickPrayers[types[i]] = 0;
							Client.instance.playerVariables[quickConfigIDs[types[i]]] = 0;
							Client.instance.postVarpChange(quickConfigIDs[types[i]]);
							Client.reDrawTabArea = true;
						} else {
							quickPrayers[index] = 1 - quickPrayers[index];
							Client.instance.playerVariables[quickConfigIDs[index]] = quickPrayers[index];
							Client.instance.postVarpChange(quickConfigIDs[index]);
							Client.reDrawTabArea = true;
						}
					}
				} else {
					quickPrayers[index] = 1 - quickPrayers[index];
					Client.instance.playerVariables[quickConfigIDs[index]] = quickPrayers[index];
					Client.instance.postVarpChange(quickConfigIDs[index]);
					Client.reDrawTabArea = true;
				}
			} else {
				Client.instance.pushMessage("You need a Prayer level of at least " + prayerLevelRequirements[index] + " to use " + prayerName[index] + ".", ChatMessage.GAME_MESSAGE);
			}
		} else if (prayerInterfaceId == CURSE_INTERFACE) {
			if (Client.instance.statsBase[5] >= curseLevelRequirements[index]) {
				int[] types = getCurseTypeForIndex(index);
				if (types != null) {
					for (int i = 0; i < types.length; i++) {
						if (index != types[i]) {
							quickCurses[types[i]] = 0;
							Client.instance.playerVariables[quickConfigIDs[types[i]]] = 0;
							Client.instance.postVarpChange(quickConfigIDs[types[i]]);
							Client.reDrawTabArea = true;
						}
					}
				}
				quickCurses[index] = 1 - quickCurses[index];
				Client.instance.playerVariables[quickConfigIDs[index]] = quickCurses[index];
				Client.instance.postVarpChange(quickConfigIDs[index]);
				Client.reDrawTabArea = true;
			} else {
				Client.instance.pushMessage("You need a Prayer level of at least " + curseLevelRequirements[index] + " to use " + curseName[index] + ".", ChatMessage.GAME_MESSAGE);
			}
		}
	}

}
