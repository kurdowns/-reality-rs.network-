package com.zionscape.server.model.items;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;

import java.util.Optional;

public class ItemUtility {

	public static String getName(int id) {
		Optional<ItemDefinition> optional = ItemDefinition.get(id);
		return optional.isPresent() ? optional.get().getName() : "no name";
	}

	public static int getNotedId(int id) {

		Optional<ItemDefinition> optional = ItemDefinition.get(id);

		if(!optional.isPresent()) {
			return -1;
		}

		switch (id) {
			case 15332:
				return 15340;
			case 15333:
				return 15341;
			case 15334:
				return 15342;
			case 15335:
				return 15343;

			case 20429: // fury shark
				return 15343;
		}

		return optional.get().getNotedId();
	}

	public static int getUnotedId(int id) {

		Optional<ItemDefinition> optional = ItemDefinition.get(id);

		if(!optional.isPresent()) {
			return -1;
		}

		switch (id) {
			case 15340:
				return 15332;
			case 15341:
				return 15333;
			case 15342:
				return 15334;

			case 15343: // fury shark
				return 20429;
		}

		return optional.get().getUnnotedId();
	}

	public static int getWearIndex(int id) {
		Optional<ItemDefinition> optional = ItemDefinition.get(id);
		return optional.isPresent() ? optional.get().getWearSlot() : -1;
	}

	public static int getValue(int id) {
		Optional<ItemDefinition> optional = ItemDefinition.get(id);
		return optional.isPresent() ? optional.get().getValue() : 0;
	}

	public static int getGeValue(int id) {
		Optional<ItemDefinition> optional = ItemDefinition.get(id);
		return optional.isPresent() ? optional.get().getGeValue() : 0;
	}

	public static boolean isStackable(int id) {
		Optional<ItemDefinition> optional = ItemDefinition.get(id);
		return optional.isPresent() && optional.get().isStackable();
	}

	public static boolean isTwoHanded(int id) {
		Optional<ItemDefinition> optional = ItemDefinition.get(id);
		return optional.isPresent() && optional.get().isTwoHanded();
	}

	public static boolean isNote(int id) {
		Optional<ItemDefinition> optional = ItemDefinition.get(id);
		return optional.isPresent() && optional.get().isNotable();
	}

	public static boolean canBeNoted(int id) {
		Optional<ItemDefinition> optional = ItemDefinition.get(id);
		return optional.isPresent() && optional.get().getNotedId() > -1;
	}

	public static boolean showBeard(int id) {
		Optional<ItemDefinition> optional = ItemDefinition.get(id);
		return optional.isPresent() && optional.get().isShowBeard();
	}

	public static boolean showFullMask(int id) {
		Optional<ItemDefinition> optional = ItemDefinition.get(id);
		return optional.isPresent() && optional.get().isFullmask();
	}

	public static int getBonus(int id, int index) {
		Optional<ItemDefinition> optional = ItemDefinition.get(id);

		return optional.isPresent() ? optional.get().getBonuses()[index] : 0;
	}

	/**
	 * Checks if or not a player has the given Void set
	 *
	 * @param player the player
	 * @param set    The set to check for
	 * @return if or not the player has the set
	 */
	public static boolean hasVoidSet(Player player, VoidSet set) {

		if (player.equipment[PlayerConstants.HAT] != set.getHelmId()) {
			return false;
		}

		int amount = 0;

		if (player.equipment[PlayerConstants.LEGS] == 8840 || player.equipment[PlayerConstants.LEGS] == 19786) {
			amount++;
		}

		if (player.equipment[PlayerConstants.CHEST] == 8839 || player.equipment[PlayerConstants.CHEST] == 19785) {
			amount++;
		}

		if (player.equipment[PlayerConstants.HANDS] == 8842) {
			amount++;
		}

		return (player.equipment[PlayerConstants.SHIELD] == 19712 ? amount >= 2 : amount == 3);
	}

	/**
	 * Different types of void
	 */
	public enum VoidSet {
		MELEE(11665), RANGE(11664), MAGE(11663);

		private int helmId;

		VoidSet(int helmId) {
			this.helmId = helmId;
		}

		public int getHelmId() {
			return helmId;
		}

	}

}
