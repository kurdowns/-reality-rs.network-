package com.zionscape.server.model.players.skills.herblore;

import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.util.CollectionUtil;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Jay
 */
public class Herblore extends HerbloreConstants {

	private static final int[] EXTREME_IDS = {15309, 15313, 15317, 15325, 15321};

	public static boolean cleanHerb(Player client, int itemClicked, int slot) {
		CleaningData data = CleaningData.forId(itemClicked);
		if (data != null) {
			if (client.level[15] < data.getLevelRequired()) {
				client.sendMessage("You don't have the level required to clean the herb.");
				return true;
			}

			Achievements.progressMade(client, Achievements.Types.IDENTIFY_250_HERBS);

			client.getItems().deleteItem(data.getDirtyId(), slot, 1);
			client.getPA().addSkillXP((int) data.getXp(), 15);
			client.getItems().addItem(data.getCleanId(), 1);
			client.sendMessage("You clean the " + data.name().toLowerCase() + ".");
		}
		return false;
	}

	//CombiningDoses

	public static boolean itemOnItem(Player player, int firstItemId, int firstItemSlot, int secondItemId, int secondItemSlot) {


		// empty flask
		if(secondItemId == 25615) {
			for(Flasks flask : Flasks.values()) {
				int dose = flask.getCombiningDoses().getDoseForID(firstItemId);
				if(dose > -1) {
					player.getItems().deleteItem(25615, 1);
					player.getItems().addItem(flask.getIds()[dose - 1], 1);
					player.getItems().addItem(229, 1);
					player.getItems().deleteItem(firstItemId, 1);
					return true;
				}
			}

			return true;
		}

		Optional<Flasks> optional = Flasks.getFlask(secondItemId);
		if(!optional.isPresent()) {
			return false;
		}

		Flasks flasks = optional.get();

		int potDose = flasks.getCombiningDoses().getDoseForID(firstItemId);
		if(potDose == -1) {
			return false;
		}

		int flaskDose = CollectionUtil.getIndexOfValue(flasks.getIds(), secondItemId) + 1;

		if(flaskDose == 6) {
			player.sendMessage("Flask is already full.");
			return false;
		}

		int doseToGive = flaskDose + potDose;
		int leftOverDose = 0;

		if(flaskDose + potDose > 6) {
			leftOverDose = (flaskDose + potDose) - 6;
			doseToGive = 6;
		}

		player.getItems().deleteItem(firstItemId, 1);
		if(leftOverDose > 0) {
			player.getItems().addItem(flasks.getCombiningDoses().getIDForDose(leftOverDose), 1);
		} else {
			player.getItems().addItem(229, 1);
		}

		player.getItems().deleteItem(secondItemId, 1);
		player.getItems().addItem(flasks.getIds()[doseToGive - 1], 1);

		return false;
	}

	public static boolean invokeItemOnItem(Player player, int id, int usedWith) {
		PotionData data = PotionData.forId(id, usedWith);
		if (data == null) {
			return false;
		}
		if (player.level[15] < data.levelRequired) {
			player.sendMessage("You don't have the level required to mix the herb.");
			return true;
		}
		for (Integer item : data.itemsRequired) {
			if (!player.getItems().playerHasItem(item)) {
				player.sendMessage("You do not have the required items to make a " + data.name().toLowerCase() + ".");
				player.sendMessage("You require a " + player.getItems().getItemName(item) + " to make this potion.");
				return true;
			}
		}
		int vialToRemove = data.vialUsed == 227 ? -1 : data.vialUsed;
		if (vialToRemove != -1) {
			player.getItems().deleteItem(vialToRemove, 1);
		}
		for (Integer item : data.itemsRequired) {
			player.getItems().deleteItem(item, 1);
		}
		player.getItems().deleteItem(data.unfinishedPotion, 1);
		player.getPA().addSkillXP(data.xp, 15);
		player.getItems().addItem(data.finishedPotion, 1);
		return false;
	}

	public static boolean makeOverloadPotion(Player c, int item, int otherItem) {
		// overloads
		if (item == 269 || otherItem == 269) {
			if (Arrays.binarySearch(EXTREME_IDS, item) > -1 || Arrays.binarySearch(EXTREME_IDS, otherItem) > -1) {
				if (c.level[PlayerConstants.HERBLORE] < 91) {
					c.sendMessage("You require level 91 Herblore to mix this pot.");
					return true;
				}

				for (int i = 0; i < EXTREME_IDS.length; i++) {
					if (!c.getItems().playerHasItem(EXTREME_IDS[i])) {
						c.sendMessage("You do not have all the required ingredients to combine these items.");
						return true;
					}

				}

				c.getItems().deleteItem(269, 1);
				for (int i = 0; i < EXTREME_IDS.length; i++) {
					c.getItems().deleteItem(EXTREME_IDS[i], 1);
				}
				c.getPA().addSkillXP(1000, 15);
				c.getItems().addItem(15333, 1);
				c.sendMessage("You combine the items together to make an Overload potion.");
				return true;
			}
		}
		return false;
	}

	public static boolean useVial(Player client, int usedItem, int usedWith) {


		if (makeOverloadPotion(client, usedItem, usedWith)) {
			return true;
		}

		if (usedItem != 227 && usedWith != 227) {
			return false;
		}
		int herb = usedItem == 227 ? usedWith : usedItem;
		CleaningData data = CleaningData.forCleanId(herb);
		if (data == null) {
			return false;
		}
		if (data != null) {
			if (client.level[15] < data.getLevelRequired()) {
				client.sendMessage("You don't have the level required to mix the herb.");
				return true;
			}
			client.getItems().deleteItem(data.getCleanId(), 1);
			client.getItems().deleteItem(227, 1);
			client.getPA().addSkillXP((int) data.getXp(), 15);
			client.getItems().addItem(data.getUnfinishedPotion(), 1);
			client.sendMessage("You mix the vial and the " + data.name().toLowerCase() + ".");
		}
		return false;
	}

	/**
	 * @param dirtyId       ,
	 * @param cleanId       ,
	 * @param levelRequired ,
	 * @param xp
	 */
	public enum CleaningData {
		GUAM(199, 249, 1, 2.5, GUAM_POTION_UNF), MARRENTILL(201, 251, 5, 3.8, MARRENTILL_POTION_UNF), TARROMIN(203,
				253, 11, 5, TARROMIN_POTION_UNF), HARRALANDER(205, 255, 20, 6.3, HARRALANDER_POTION_UNF), RANARR(207,
				257, 25, 7.5, RANARR_POTION_UNF), IRIT(209, 259, 40, 8.8, IRIT_POTION_UNF), AVANTOE(211, 261, 48, 10,
				AVANTOE_POTION_UNF), KWUARM(213, 263, 54, 11.3, KWUARM_POTION_UNF), CADANTINE(215, 265, 65, 12.5,
				CADANTINE_POTION_UNF), DWARF_WEED(217, 267, 70, 13.8, DWARF_WEED_POTION_UNF), TORSTOL(219, 269, 75, 15,
				TORSTOL_POTION_UNF), SPIRIT_WEED(12174, 12172, 35, 7.8, 12181), WERGALI(14836, 14854, 41, 9.5, 14856), SAGEWORT(
				17494, 17512, 3, 2.1, 17538), VALERIAN(17496, 17514, 4, 3.2, 17540), ALOE(17498, 17516, 8, 4, 17542), WORMWOOD_LEAF(
				17500, 17518, 34, 7.2, 17544), MEGABANE(17502, 17520, 37, 7.7, 17546), FEATHERFOIL(17504, 17522, 41,
				8.6, 17548), WINTERS_GRIP(17506, 17524, 67, 12.7, 17550), LYCOPUS(17508, 17526, 70, 13.1, 17552), BUCKTHORN(
				17510, 17528, 74, 13.8, 17554), LANTADYME(2485, 2481, 67, 13, 2483), SNAPDRAGON(3051, 3000, 59, 12, 3004), TOADFLAX(
				3049, 2998, 30, 8, 3002);
		final int dirtyId, cleanId, levelRequired, unfinishedPotion;
		final double xp;

		CleaningData(int dirtyId, int cleanId, int levelRequired, double xp, int unfinishedPotion) {
			this.dirtyId = dirtyId;
			this.cleanId = cleanId;
			this.levelRequired = levelRequired;
			this.xp = xp;
			this.unfinishedPotion = unfinishedPotion;
		}

		public static CleaningData forCleanId(int id) {
			for (CleaningData data : CleaningData.values()) {
				if (data.getCleanId() == id) {
					return data;
				}
			}
			return null;
		}

		/**
		 * @param id {represents @param dirtyId}
		 */
		public static CleaningData forId(int id) {
			for (CleaningData data : CleaningData.values()) {
				if (data.getDirtyId() == id) {
					return data;
				}
			}
			return null;
		}

		public int getCleanId() {
			return cleanId;
		}

		public int getDirtyId() {
			return dirtyId;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public int getUnfinishedPotion() {
			return unfinishedPotion;
		}

		public double getXp() {
			return xp;
		}
	}

	public enum PotionData {
		ATTACK_POTION(227, 25, 1, CleaningData.GUAM, ATTACK_POTION_3, 221), RANGING_POTION(227, 30, 3,
				CleaningData.GUAM, RANGING_POTION_3, 1951), ANTIPOISION(227, 38, 5, CleaningData.MARRENTILL,
				ANTIPOISON_3, 235), MAGIC_POTION(227, 30, 5, CleaningData.TARROMIN, MAGIC_POTION_3, 1470), STRENTH_POTION(
				227, 50, 12, CleaningData.TARROMIN, STRENGTH_POTION_3, 225), SERUM_207(227, 50, 15,
				CleaningData.TARROMIN, SERUM_207_3, 592), GUTHIX_REST(227, 59, 18, CleaningData.HARRALANDER, 4419, 251), RESTORE_POTION(
				227, 50, 22, CleaningData.HARRALANDER, 127, 223), GUTHIX_BALANCE(127, 50, 22, null, 7662, 1550, 7650), BLAMISH_OIL(
				227, 80, 25, CleaningData.HARRALANDER, 1582, 1581), ENERGY_POTION(227, 67, 26,
				CleaningData.HARRALANDER, ENERGY_POTION_3, 1975), DEFENCE_POTION(227, 75, 30, CleaningData.RANARR,
				DEFENCE_POTION_3, 239), AGILITY_POTION(227, 43, 80, 34, 3002, AGILITY_POTION_3, 2152), COMBAT_POTION(
				227, 84, 36, CleaningData.HARRALANDER, 9741, 9736), PRAYER_POTION(227, 87, 38, CleaningData.RANARR,
				PRAYER_POTION_3, 231), SUMMONING_POTION(227, 92, 40, CleaningData.SPIRIT_WEED, 12142, 12109), CRAFTING_POTION(
				227, 95, 42, CleaningData.WERGALI, 14838, 5004), SUPER_ATTACK(227, 100, 45, CleaningData.IRIT,
				SUPER_ATTACK_3, 221), SUPER_ANTIPOISON(227, 107, 48, CleaningData.IRIT, SUPER_ANTIPOISON_3, 235), FISHING_POTION(
				227, 112, 50, CleaningData.AVANTOE, FISHING_POTION_3, 231), SUPER_ENGERY(227, 118, 52,
				CleaningData.AVANTOE, SUPER_ENERGY_3, 2970), HUNTER_POTION(227, 120, 53, CleaningData.AVANTOE, 10000,
				10111), SUPER_STRENGTH(227, 125, 55, CleaningData.KWUARM, SUPER_STRENGTH_3, 225), FLETCHING_POTION(227,
				130, 58, CleaningData.WERGALI, 14848, 11525), WEAPON_POISON(227, 138, 60, CleaningData.KWUARM, 187, 241), SUPER_RESTORE(
				227, 143, 63, 3004, SUPER_RESTORE_3, 223), SUPER_DEFENCE(227, 150, 66, CleaningData.CADANTINE,
				SUPER_DEFENCE_3, 239), ANTIPOISON(227, 155, 68, 3002, ANTIPOISON_3, 6049), ANTIFIRE(227, 158, 69,
				LANTADYME_POTION_UNF, 2454, 241), SUPER_RANGING(227, 162, 72, CleaningData.DWARF_WEED, 169, 245), SUPER_MAGIC(
				227, 173, 76, LANTADYME_POTION_UNF, MAGIC_POTION_3, 3138), ZAMORAK_BREW(227, 175, 78,
				CleaningData.TORSTOL, ZAMORAK_BREW_3, 247), ANTIPOISON_PLUS_PLUS(227, 177, 79, IRIT_POTION_UNF, 175,
				6051), SUPER_ANTIFIRE(15305, 210, 85, null, 15305, 4621), EXTREME_ATTACK(145, 220, 88, null, 15309, 261), EXTREME_STRENGTH(
				157, 230, 89, null, 15313, 267), EXTREME_DEFENCE(163, 240, 90, null, 15317, 2481), EXTREME_MAGIC(3042,
				240, 91, null, 15321, 9594), EXTREME_RANGING(169, 250, 92, null, 15325, 12539), SUPER_PRAYER(139, 270,
				94, null, 15329, 6812), PRAYER_RENEWAL(227, 190, 94, null, 21632, 21622), SARADOMIN_BREW(227, 180, 81, CleaningData.TOADFLAX, 6687, 6693);
		// OVERLOAD(-1, 1000, 96, CleaningData.TORSTOL, /*15333*/1, 15309, 15313, 15317,
		// 15321, 15325);
		public int vialUsed, xp, levelRequired, unfinishedPotion, finishedPotion;
		public int[] itemsRequired;

		PotionData(int vialUsed, int xp, int levelRequired, CleaningData cleaningData, int finishedPotion,
				   int... itemsRequired) {
			this.vialUsed = vialUsed;
			this.xp = xp;
			this.levelRequired = levelRequired;
			this.unfinishedPotion = cleaningData != null ? cleaningData.getUnfinishedPotion() : -1;
			this.finishedPotion = finishedPotion;
			this.itemsRequired = itemsRequired;
		}

		PotionData(int vialUsed, int xp, int levelRequired, int unfPotion, int finishedPotion, int... itemsRequired) {
			this.vialUsed = vialUsed;
			this.xp = xp;
			this.levelRequired = levelRequired;
			this.unfinishedPotion = unfPotion;
			this.finishedPotion = finishedPotion;
			this.itemsRequired = itemsRequired;
		}

		public static PotionData forId(int id, int id2) {
			for (PotionData data : PotionData.values()) {
				int unfinishedPotion = data.unfinishedPotion;
				if (unfinishedPotion != -1) {
					if ((id == unfinishedPotion && data.requiredItemContains(id2))
							|| (id2 == unfinishedPotion && data.requiredItemContains(id))) {
						return data;
					}
				} else if (unfinishedPotion == -1) {
					int required = data.vialUsed;
					if ((id == required && data.requiredItemContains(id2))
							|| (id2 == required && data.requiredItemContains(id))) {
						return data;
					}
				}
			}
			return null;
		}

		public boolean requiredItemContains(int id) {
			for (Integer required : itemsRequired) {
				if (id == required) {
					return true;
				}
			}
			return false;
		}
	}
}
