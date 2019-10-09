package com.zionscape.server.model.content;

import com.zionscape.server.model.players.Player;

public class PotionMixing {

	private static final int VIAL = 227;
	private static final int EMPTY_VIAL = 229;

	/*
     * Constants for use later.
     */
	private final Player player;

	public PotionMixing(final Player player) {
		this.player = player;
	}

	/**
	 * @param firstPotID
	 * @param secondPotID
	 * @date 2/28/12
	 * @author 0021sordna & 210jrellik
	 */

	public void potionCombination(int firstPotID, int secondPotID) {
		if(player.isBusy()) {
			return;
		}

		CombiningDoses potion = CombiningDoses.getPotionByID(firstPotID);

		if (potion == null) {
			return;
		}

		if (potion.toString().contains("NOTED")) {
			return;
		}

		// if(Item.itemIsNote[potion] == null) {
		// return;
		// }

		if (potion.getDoseForID(secondPotID) > 0) {
			int firstPotAmount = potion.getDoseForID(firstPotID);
			int secondPotAmount = potion.getDoseForID(secondPotID);

			if (firstPotAmount + secondPotAmount <= 4) {
				player.getItems().deleteItem(firstPotID, player.getItems().getItemSlot(firstPotID), 1);
				player.getItems().deleteItem(secondPotID, player.getItems().getItemSlot(secondPotID), 1);
				player.getItems().addItem(potion.getIDForDose(firstPotAmount + secondPotAmount), 1);
				player.getItems().addItem(EMPTY_VIAL, 1);
			} else {
				int overflow = (firstPotAmount + secondPotAmount) - 4;
				player.getItems().deleteItem(firstPotID, player.getItems().getItemSlot(firstPotID), 1);
				player.getItems().deleteItem(secondPotID, player.getItems().getItemSlot(secondPotID), 1);
				player.getItems().addItem(potion.getIDForDose(4), 1);
				player.getItems().addItem(potion.getIDForDose(overflow), 1);
			}
		} // else {
		// c.sendMessage("You can't combine a " + potion.getPotionName() +
		// " potion with a " +
		// CombiningDoses.getPotionByID(secondPotID).getPotionName() +
		// " potion.");
		// }
	}

	public void combineAll() {

		player.setAttribute("dont_update_inventory", true);

		for (CombiningDoses doses : CombiningDoses.values()) {
			int totalDoses = 0;

			totalDoses += player.getItems().getItemAmount(doses.getDoseID1());
			totalDoses += player.getItems().getItemAmount(doses.getDoseID2()) * 2;
			totalDoses += player.getItems().getItemAmount(doses.getDoseID3()) * 3;

			player.getItems().deleteItem(doses.getDoseID1(), Integer.MAX_VALUE);
			player.getItems().deleteItem(doses.getDoseID2(), Integer.MAX_VALUE);
			player.getItems().deleteItem(doses.getDoseID3(), Integer.MAX_VALUE);

			if (totalDoses == 0) {
				continue;
			}

			if (totalDoses < 4) {
				player.getItems().addItem(doses.getIDForDose(totalDoses), 1);
			} else {
				int fullDoses = (int) Math.floor(totalDoses / 4);
				int leftDoses = totalDoses - (fullDoses * 4);

				player.getItems().addItem(doses.getIDForDose(4), fullDoses);
				if (leftDoses > 0) {
					player.getItems().addItem(doses.getIDForDose(leftDoses), 1);
				}
			}
		}

		player.removeAttribute("dont_update_inventory");
		player.getItems().resetItems(3214);
	}

	/**
	 * @author 210jrellik
	 * @date 2-28-2012
	 */

	public enum CombiningDoses {

		STRENGTH(119, 117, 115, 113, VIAL, "Strength"),
		SUPER_STRENGTH(161, 159, 157, 2440, VIAL, "Super strength"),
		ATTACK(125, 123, 121, 2428, VIAL, "Attack"),
		SUPER_ATTACK(149, 147, 145, 2436, VIAL, "Super attack"),
		DEFENCE(137, 135, 133, 2432, VIAL, "Defence"),
		SUPER_DEFENCE(167, 165, 163, 2442, VIAL, "Super defence"),
		RANGING_POTION(173, 171, 169, 2444, VIAL, "Ranging"),
		FISHING(155, 153, 151, 2438, VIAL, "Fishing"),
		PRAYER(143, 141, 139, 2434, VIAL, "Prayer"),
		ANTIFIRE(2458, 2456, 2454, 2452, VIAL, "Antifire"),
		ZAMORAK_BREW(193, 191, 189, 2450, VIAL, "Zamorakian brew"),
		ANTIPOISON(179, 177, 175, 2446, VIAL, "Antipoison"),
		RESTORE(131, 129, 127, 2430, VIAL, "Restoration"),
		MAGIC_POTION(3046, 3044, 3042, 3040, VIAL, "Magic"),
		RECOVER_SPECIAL(15303, 15302, 15301, 15300, VIAL, "Recoverspecial"),
		SUPER_RESTORE(3030, 3028, 3026, 3024, VIAL, "Super Restoration"),
		ENERGY(3014, 3012, 3010, 3008, VIAL, "Energy"),
		SUPER_ENERGY(3022, 3020, 3018, 3016, VIAL, "Super Energy"),
		AGILITY(3038, 3036, 3034, 3032, VIAL, "Agility"),
		SARADOMIN_BREW(6691, 6689, 6687, 6685, VIAL, "Saradomin brew"),
		ANTIPOISON1(5949, 5947, 5945, 5943, VIAL, "Antipoison(+)"),
		ANTIPOISON2(5958, 5956, 5954, 5952, VIAL, "Antipoison(++)"),
		SUPER_ANTIPOISON(185, 183, 181, 2448, VIAL, "Super Antipoison"),
		RELICYMS_BALM(4848, 4846, 4844, 4842, VIAL, "Relicym's balm"),
		SERUM_207(3414, 3412, 3410, 3408, VIAL, "Serum 207"),
		COMBAT(9745, 9743, 9741, 9739, VIAL, "Combat"),
		OVERLOADS(15335, 15334, 15333, 15332, VIAL, "Overload"),
		EXTREME_STR(15315, 15314, 15313, 15312, VIAL, "Extreme Strength"),
		EXTREME_ATT(15311, 15310, 15309, 15308, VIAL, "Extreme Attack"),
		EXTREME_DEF(15319, 15318, 15317, 15316, VIAL, "Extreme Defence"),
		EXTREME_RANGE(15327, 15326, 15325, 15324, VIAL, "Extreme Ranging"),
		EXTREME_MAGIC(15323, 15322, 15321, 15320, VIAL, "Extreme Magic"),
		SUPER_ANTIFIRE(15307, 15306, 15305, 15304, VIAL, "Super Antifire"),
		SUPER_PRAYER(15331, 15330, 15329, 15328, VIAL, "Super Prayer"),

		STRENGTH_NOTED(120, 118, 116, 114, VIAL, "Strength"),
		SUPER_STRENGTH_NOTED(162, 160, 158, 2441, VIAL, "Super strength"),
		ATTACK_NOTED(126, 124, 122, 2429, VIAL, "Attack"),
		SUPER_ATTACK_NOTED(150, 148, 146, 2437, VIAL, "Super attack"),
		DEFENCE_NOTED(138, 136, 134, 2433, VIAL, "Defence"),
		SUPER_DEFENCE_NOTED(168, 166, 164, 2443, VIAL, "Super defence"),
		RANGING_POTION_NOTED(174, 172, 170, 2445, VIAL, "Ranging"),
		FISHING_NOTED(156, 154, 152, 2439, VIAL, "Fishing"),
		PRAYER_NOTED(144, 142, 140, 2435, VIAL, "Prayer"),
		ANTIFIRE_NOTED(2459, 2457, 2455, 2453, VIAL, "Antifire"),
		ZAMORAK_BREW_NOTED(194, 192, 190, 2451, VIAL, "Zamorakian brew"),
		ANTIPOISON_NOTED(180, 178, 176, 2447, VIAL, "Antipoison"),
		RESTORE_NOTED(132, 130, 128, 2431, VIAL, "Restoration"),
		MAGIC_POTION_NOTED(3047, 3045, 3043, 3041, VIAL, "Magic"),
		SUPER_RESTORE_NOTED(3031, 3029, 3027, 3025, VIAL, "Super Restoration"),
		ENERGY_NOTED(3015, 3013, 3011, 3009, VIAL, "Energy"),
		SUPER_ENERGY_NOTED(3023, 3021, 3019, 3017, VIAL, "Super Energy"),
		AGILITY_NOTED(3039, 3037, 3035, 3033, VIAL, "Agility"),
		SARADOMIN_BREW_NOTED(6692, 6690, 6688, 6686, VIAL, "Saradomin brew"),
		ANTIPOISON1_NOTED(5950, 5948, 5946, 5944, VIAL, "Antipoison(+)"),
		ANTIPOISON2_NOTED(5959, 5957, 5955, 5953, VIAL, "Antipoison(++)"),
		SUPER_ANTIPOISON_NOTED(186, 184, 182, 2449, VIAL, "Super Antipoison"),
		RELICYMS_BALM_NOTED(4849, 4847, 4845, 4843, VIAL, "Relicym's balm"),
		COMBAT_NOTED(9746, 9744, 9742, 9740, VIAL, "Combat"),
		OVERLOADS_NOTED(15343, 15342, 15341, 15340, VIAL, "Overload");

		/*
         * This is what the data in the above enumeration is, in order. EX:
		 * COMBAT(oneDosePotionID, twoDosePotionID, threeDosePotionID,
		 * fourDosePotionID, vial, "potionName");
		 */

		int oneDosePotionID, twoDosePotionID, threeDosePotionID,
				fourDosePotionID, vial;
		String potionName;

		/**
		 * @param oneDosePotionID   - This is the ID for the potion when it contains one dose.
		 * @param twoDosePotionID   - This is the ID for the potion when it contains two
		 *                          doses.
		 * @param threeDosePotionID - This is the ID for the potion when it contains three
		 *                          doses.
		 * @param fourDosePotionID  - This is the ID for the (full) potion when it contains
		 *                          four doses.
		 * @param vial              - This is referenced from: private static final int VIAL =
		 *                          227; It's a constant and its value never changes.
		 * @param potionName        - This is a string which is used to set a name for the
		 *                          potion. Within an enumeration you can use the name().
		 *                          method to take the name in-front of the stored data. This
		 *                          however could not be done because of some potion NAMES.
		 */

		private CombiningDoses(int oneDosePotionID, int twoDosePotionID,
							   int threeDosePotionID, int fourDosePotionID, int vial,
							   String potionName) {
			this.oneDosePotionID = oneDosePotionID;
			this.twoDosePotionID = twoDosePotionID;
			this.threeDosePotionID = threeDosePotionID;
			this.fourDosePotionID = fourDosePotionID;
			this.vial = vial;
			this.potionName = potionName;
		}

		/*
         * These are code getters to use the data stored in the above
		 * enumeration.
		 */

		/**
		 * @param ID
		 * @return The potion that matches the ID. ID can be any dose of the
		 * potion.
		 * @date 2/28/12
		 * @author 0021sordna
		 */

		public static CombiningDoses getPotionByID(int id) {
			for (CombiningDoses potion : CombiningDoses.values()) {
				if (id == potion.oneDosePotionID) {
					return potion;
				}
				if (id == potion.twoDosePotionID) {
					return potion;
				}
				if (id == potion.threeDosePotionID) {
					return potion;
				}
				if (id == potion.fourDosePotionID) {
					return potion;
				}
			}
			return null;
		}

		public int getDoseID1() {
			return oneDosePotionID;
		}

		public int getDoseID2() {
			return twoDosePotionID;
		}

		public int getDoseID3() {
			return threeDosePotionID;
		}

		public int getFourDosePotionID() {
			return fourDosePotionID;
		}

		public int getVial() {
			return vial;
		}

		public String getPotionName() {
			return potionName;
		}

		/**
		 * @param id
		 * @return The dose that this id represents for this potion, or -1 if it
		 * doesn't belong to this potion.
		 * @date 2/28/12
		 * @author 0021sordna
		 */

		public int getDoseForID(int id) {
			if (id == this.oneDosePotionID) {
				return 1;
			}
			if (id == this.twoDosePotionID) {
				return 2;
			}
			if (id == this.threeDosePotionID) {
				return 3;
			}
			if (id == this.fourDosePotionID) {
				return 4;
			}
			return -1;
		}

		/**
		 * @param dose
		 * @return The ID for this dose of the potion, or -1 if this dose
		 * doesn't exist.
		 * @date 2/28/12
		 * @author 0021sordna
		 */

		public int getIDForDose(int dose) {
			if (dose == 1) {
				return this.oneDosePotionID;
			}
			if (dose == 2) {
				return this.twoDosePotionID;
			}
			if (dose == 3) {
				return this.threeDosePotionID;
			}
			if (dose == 4) {
				return this.fourDosePotionID;
			}
			if (dose == 0) {
				return EMPTY_VIAL;
			}
			return -1;
		}
	}

}