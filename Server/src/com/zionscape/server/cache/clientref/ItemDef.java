package com.zionscape.server.cache.clientref;

/*******************************************************************************
 * Copyright 2014 Aeresan LTD
 *
 * @author Poesy700
 * @author Lost Valentino
 * @author Wolfs Darker
 * <p>
 * No redistribution allowed in any way shape or form other than a obfuscated client executable for usage by players.
 ******************************************************************************/

import com.zionscape.server.util.Stream;

public final class ItemDef {

	public static ItemDef[] cache;
	public static int cacheIndex;
	public static boolean isMembers = true;
	public static Stream stream;
	public static int[] streamIndices;
	public static int totalItems = 20085;
	public byte equippedModelFemaleTranslateY;
	public int value;
	public int[] editedModelColor;
	public int id;
	public int[] newModelColor;
	public boolean membersObject;
	public int equippedModelFemale3;
	public int notedItemID;
	public int equippedModelFemale2;
	public int equippedModelMale1;
	public int maleDialogueModel2;
	public int modelScaleX;
	public String groundActions[];
	public int translateX;
	public String name;
	public int femaleDialogueModel2;
	public int inventoryModel;
	public int maleDialogueModel1;
	public boolean stackable;
	public String description;
	public int unNotedItemID;
	public int modelZoom;
	public int lightMagnitude;
	public int equippedModelMale3;
	public int equippedModelMale2;
	public String actions[];
	public int rotationX;
	public int modelScaleZ;
	public int modelScaleY;
	public int[] stackIDs;
	public int translateYZ;
	public int lightIntensity;
	public int femaleDialogueModel1;
	public int rotationY;
	public int equippedModelFemale1;
	public int[] stackAmounts;
	public int team;
	public int rotationZ;
	public byte equippedModelMaleTranslateY;
	public int lendID;
	public int lentItemID;

	public ItemDef() {
		id = -1;
	}

	public static void unpackConfig(StreamLoader streamLoader) {
		stream = new Stream(streamLoader.getDataForName("obj.dat"));
		Stream stream = new Stream(streamLoader.getDataForName("obj.idx"));
		totalItems = stream.readUnsignedWord();
		System.out.println("634 Items Amount: " + totalItems);
		streamIndices = new int[totalItems + 1000];
		int i = 2;
		for (int j = 0; j < totalItems; j++) {
			streamIndices[j] = i;
			i += stream.readUnsignedWord();
		}
		cache = new ItemDef[10];
		for (int k = 0; k < 10; k++)
			cache[k] = new ItemDef();

	}

	public static ItemDef forID(int i) {
		if (i > totalItems || i < 0)
			return null;
		for (int j = 0; j < 10; j++)
			if (cache[j].id == i)
				return cache[j];
		cacheIndex = (cacheIndex + 1) % 10;
		ItemDef itemDef = cache[cacheIndex];
		stream.currentOffset = streamIndices[i];
		itemDef.id = i;
		itemDef.setDefaults();
		itemDef.readValues(stream);
		if (itemDef.notedItemID != -1)
			itemDef.toNote();
		if (itemDef.lentItemID != -1)
			itemDef.toLend();
		if (!isMembers && itemDef.membersObject) {
			itemDef.name = "Members Object";
			itemDef.description = "Login to a members' com.zionscape.server to use this object.";
			itemDef.groundActions = null;
			itemDef.actions = null;
			itemDef.team = 0;
		}
		// for (int blackItems : Configuration.ITEMS_WITH_BLACK) {
		// if(itemDef.ID == blackItems) {
		if (itemDef.editedModelColor != null) {
			int[] oldc = itemDef.editedModelColor;
			int[] newc = itemDef.newModelColor;
			itemDef.editedModelColor = new int[oldc.length + 1];
			itemDef.newModelColor = new int[newc.length + 1];
			for (int index = 0; index < itemDef.newModelColor.length; index++) {
				if (index < itemDef.newModelColor.length - 1) {
					itemDef.editedModelColor[index] = oldc[index];
					itemDef.newModelColor[index] = newc[index];
				} else {
					itemDef.editedModelColor[index] = 0;
					itemDef.newModelColor[index] = 1;
				}
			}
		} else {
			itemDef.editedModelColor = new int[1];
			itemDef.newModelColor = new int[1];
			itemDef.editedModelColor[0] = 0;
			itemDef.newModelColor[0] = 1;
		}
		// }
		// }
		switch (i) {

			case 17748:
				itemDef.name = "Abyssal vine whip";
				itemDef.modelZoom = 848;
				itemDef.rotationX = 280;
				itemDef.rotationY = 0;
				itemDef.translateYZ = 0;
				itemDef.translateX = 0;
				itemDef.groundActions = new String[]{null, null, "Take", null, null};
				itemDef.actions = new String[]{null, "Wield", "Split", null, "Drop"};
				itemDef.inventoryModel = 10247;
				itemDef.equippedModelMale1 = 10253;
				itemDef.equippedModelFemale1 = 10253;
				break;

			case 6568: // To replace Transparent black with opaque black.
				itemDef.editedModelColor = new int[1];
				itemDef.newModelColor = new int[1];
				itemDef.editedModelColor[0] = 0;
				itemDef.newModelColor[0] = 2059;
				break;
			case 996:
			case 997:
			case 998:
			case 999:
			case 1000:
			case 1001:
			case 1002:
			case 1003:
			case 1004:
				itemDef.name = "Coins";
				break;

		}
		return itemDef;
	}

	public static boolean hasOption(int itemId, String option) {
		ItemDef def = forID(itemId);
		if (def != null && def.actions != null) {
			for (int i = 0; i < def.actions.length; i++) {
				if (def.actions[i] != null && def.actions[i].trim().equalsIgnoreCase(option)) {
					return true;
				}
			}
		}
		return false;
	}

	public void setDefaults() {
		inventoryModel = 0;
		name = null;
		description = null;
		editedModelColor = null;
		newModelColor = null;
		modelZoom = 2000;
		rotationX = 0;
		rotationY = 0;
		rotationZ = 0;
		translateX = 0;
		translateYZ = 0;
		stackable = false;
		value = 1;
		membersObject = false;
		groundActions = null;
		actions = null;
		equippedModelMale1 = -1;
		equippedModelMale2 = -1;
		equippedModelMaleTranslateY = 0;
		equippedModelFemale1 = -1;
		equippedModelFemale2 = -1;
		equippedModelFemaleTranslateY = 0;
		equippedModelMale3 = -1;
		equippedModelFemale3 = -1;
		maleDialogueModel1 = -1;
		maleDialogueModel2 = -1;
		femaleDialogueModel1 = -1;
		femaleDialogueModel2 = -1;
		stackIDs = null;
		stackAmounts = null;
		unNotedItemID = -1;
		notedItemID = -1;
		modelScaleX = 128;
		modelScaleY = 128;
		modelScaleZ = 128;
		lightIntensity = 0;
		lightMagnitude = 0;
		team = 0;
		lendID = -1;
		lentItemID = -1;
	}

	private void readValues(Stream stream) {
		do {
			int i = stream.readUnsignedByte();
			if (i == 0)
				return;
			if (i == 1) {
				inventoryModel = stream.readUnsignedWord();
			} else if (i == 2)
				name = stream.readString();
			else if (i == 3)
				description = stream.readString();
			else if (i == 4)
				modelZoom = stream.readUnsignedWord();
			else if (i == 5)
				rotationX = stream.readUnsignedWord();
			else if (i == 6)
				rotationY = stream.readUnsignedWord();
			else if (i == 7) {
				translateX = stream.readUnsignedWord();
				if (translateX > 32767)
					translateX -= 0x10000;
			} else if (i == 8) {
				translateYZ = stream.readUnsignedWord();
				if (translateYZ > 32767)
					translateYZ -= 0x10000;
			} else if (i == 10)
				stream.readUnsignedWord();
			else if (i == 11)
				stackable = true;
			else if (i == 12)
				value = stream.readDWord();
			else if (i == 16)
				membersObject = true;
			else if (i == 23) {
				equippedModelMale1 = stream.readUnsignedWord();
				equippedModelMaleTranslateY = stream.readSignedByte();
			} else if (i == 24)
				equippedModelMale2 = stream.readUnsignedWord();
			else if (i == 25) {
				equippedModelFemale1 = stream.readUnsignedWord();
				equippedModelFemaleTranslateY = stream.readSignedByte();
			} else if (i == 26)
				equippedModelFemale2 = stream.readUnsignedWord();
			else if (i >= 30 && i < 35) {
				if (groundActions == null)
					groundActions = new String[5];
				groundActions[i - 30] = stream.readString();
				if (groundActions[i - 30].equalsIgnoreCase("hidden"))
					groundActions[i - 30] = null;
			} else if (i >= 35 && i < 40) {
				if (actions == null)
					actions = new String[5];
				actions[i - 35] = stream.readString();
				if (actions[i - 35].equalsIgnoreCase("null"))
					actions[i - 35] = null;
			} else if (i == 40) {
				int j = stream.readUnsignedByte();
				editedModelColor = new int[j];
				newModelColor = new int[j];
				for (int k = 0; k < j; k++) {
					editedModelColor[k] = stream.readUnsignedWord();
					newModelColor[k] = stream.readUnsignedWord();
				}
			} else if (i == 78)
				equippedModelMale3 = stream.readUnsignedWord();
			else if (i == 79)
				equippedModelFemale3 = stream.readUnsignedWord();
			else if (i == 90)
				maleDialogueModel1 = stream.readUnsignedWord();
			else if (i == 91)
				femaleDialogueModel1 = stream.readUnsignedWord();
			else if (i == 92)
				maleDialogueModel2 = stream.readUnsignedWord();
			else if (i == 93)
				femaleDialogueModel2 = stream.readUnsignedWord();
			else if (i == 95)
				rotationZ = stream.readUnsignedWord();
			else if (i == 97)
				unNotedItemID = stream.readUnsignedWord();
			else if (i == 98)
				notedItemID = stream.readUnsignedWord();
			else if (i >= 100 && i < 110) {
				if (stackIDs == null) {
					stackIDs = new int[10];
					stackAmounts = new int[10];
				}
				stackIDs[i - 100] = stream.readUnsignedWord();
				stackAmounts[i - 100] = stream.readUnsignedWord();
			} else if (i == 110)
				modelScaleX = stream.readUnsignedWord();
			else if (i == 111)
				modelScaleY = stream.readUnsignedWord();
			else if (i == 112)
				modelScaleZ = stream.readUnsignedWord();
			else if (i == 113)
				lightIntensity = stream.readSignedByte();
			else if (i == 114)
				lightMagnitude = stream.readSignedByte() * 5;
			else if (i == 115)
				team = stream.readUnsignedByte();
			else if (i == 121)
				lendID = stream.readUnsignedWord();
			else if (i == 122)
				lentItemID = stream.readUnsignedWord();
		} while (true);
	}

	public void toNote() {
		ItemDef itemDef = forID(notedItemID);
		inventoryModel = itemDef.inventoryModel;
		modelZoom = itemDef.modelZoom;
		rotationX = itemDef.rotationX;
		rotationY = itemDef.rotationY;
		rotationZ = itemDef.rotationZ;
		translateX = itemDef.translateX;
		translateYZ = itemDef.translateYZ;
		editedModelColor = itemDef.editedModelColor;
		newModelColor = itemDef.newModelColor;
		ItemDef itemDef_1 = forID(unNotedItemID);
		name = itemDef_1.name;
		membersObject = itemDef_1.membersObject;
		value = itemDef_1.value;
		String s = "a";
		char c = itemDef_1.name.charAt(0);
		if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U')
			s = "an";
		description = ("Swap this note at any bank for " + s + " " + itemDef_1.name + ".");
		stackable = true;
	}

	private void toLend() {
		ItemDef itemDef = forID(lentItemID);
		actions = new String[5];
		inventoryModel = itemDef.inventoryModel;
		translateX = itemDef.translateX;
		rotationY = itemDef.rotationY;
		translateYZ = itemDef.translateYZ;
		modelZoom = itemDef.modelZoom;
		rotationX = itemDef.rotationX;
		rotationZ = itemDef.rotationZ;
		value = 0;
		ItemDef itemDef_1 = forID(lendID);

		if (itemDef_1 == null) {
			return;
		}

		maleDialogueModel2 = itemDef_1.maleDialogueModel2;
		editedModelColor = itemDef_1.editedModelColor;
		equippedModelMale3 = itemDef_1.equippedModelMale3;
		equippedModelMale2 = itemDef_1.equippedModelMale2;
		femaleDialogueModel2 = itemDef_1.femaleDialogueModel2;
		maleDialogueModel1 = itemDef_1.maleDialogueModel1;
		groundActions = itemDef_1.groundActions;
		equippedModelMale1 = itemDef_1.equippedModelMale1;
		name = itemDef_1.name;
		equippedModelFemale1 = itemDef_1.equippedModelFemale1;
		membersObject = itemDef_1.membersObject;
		femaleDialogueModel1 = itemDef_1.femaleDialogueModel1;
		equippedModelFemale2 = itemDef_1.equippedModelFemale2;
		equippedModelFemale3 = itemDef_1.equippedModelFemale3;
		newModelColor = itemDef_1.newModelColor;
		team = itemDef_1.team;
		if (itemDef_1.actions != null) {
			for (int i_33_ = 0; i_33_ < 4; i_33_++)
				actions[i_33_] = itemDef_1.actions[i_33_];
		}
		actions[4] = "Discard";
	}

}
