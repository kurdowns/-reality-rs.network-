package net.crandor;

public class CustomizedItem {

	public short[] newModelColors;
	public int id;

	public CustomizedItem(ItemDefinition itemDefinition) {
		id = itemDefinition.id;
		if (itemDefinition.newModelColors != null) {
			newModelColors = new short[itemDefinition.newModelColors.length];
			System.arraycopy(itemDefinition.newModelColors, 0, newModelColors, 0, newModelColors.length);
		}
	}

	static CustomizedItem decode(ItemDefinition itemDefinition, Stream buffer) {
		CustomizedItem customized = new CustomizedItem(itemDefinition);
		int mask = buffer.readUnsignedByte();
		boolean readModelColors = (mask & 0x1) != 0;
		if (readModelColors) {
			int length = buffer.readUnsignedByte();
			for (int i = 0; i < length; i++) {
				int index = buffer.readUnsignedByte();
				int color = buffer.readUnsignedWord();
				customized.newModelColors[index] = (short) color;
			}
		}

		return customized;
	}

}
