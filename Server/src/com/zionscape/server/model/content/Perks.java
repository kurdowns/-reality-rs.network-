package com.zionscape.server.model.content;

/**
 * @author Jinrake
 */
public class Perks {

	public static final int COUNT = 9;

	public static int getSlot(String key) {
		return Perk.getPerk(key).getSlot();
	}

	public enum Perk {
		DFS_OP("dfso", 0, "Dragon Fire Shield Operation", "Reduces the count to operate a DFS by 10", "", 10), DFS_STORE(
				"dfss", 1, "Dragon Fire Shield Storage", "Unlimited storage of DFS charges", "", 20), PVP_DROPS("pvp",
				2, "PvP Drops", "Gives you additional PvP drops in the wilderness and an EP level", "", 10), STATS(
				"stat", 3, "Stat Wizard", "Change any COMBAT stat to a desired level", "", 5), UNLIM_EP("ep", 4,
				"Unlimited EP Levels", "Your EP count can go to " + Integer.MAX_VALUE, "Needs PvP Drops", 50), EXTRA_PK(
				"pk", 5, "Extra PK Points", "Gain an exta 1.5x bonus on your PK Points", "", 40), SPEC_1("s1", 6,
				"Spec-UP [1]", "Restores special amount 1.5x faster", "", 20), SPEC_2("s2", 7, "Spec-UP [2]",
				"Restores special amount 3.0x faster", "Need SPEC-UP[1]", 20), PRAY("pray", 8, "Divine Sacrifice",
				"Reduces prayer drain by 1.5x", "", 40), CARDIO("run", 9, "Cardio", "Allows you to run for longer", "",
				20);

		private int cost, slot;
		private String name, info, extra, indx;

		Perk(String indx, int slot, String name, String info, String extra, int cost) {
			this.name = name;
			this.info = info;
			this.extra = extra;
			this.cost = cost;
			this.indx = indx;
			this.slot = slot;
		}

		public static Perk getPerk(String indx) {
			for (Perk p : Perk.values()) {
				if (p.getIndex().equalsIgnoreCase(indx)) {
					return p;
				}
			}
			return null;
		}

		public String getName() {
			return name;
		}

		public String getInfo() {
			return info;
		}

		public String getExtra() {
			return extra;
		}

		public String getIndex() {
			return indx;
		}

		public int getCost() {
			return cost;
		}

		public int getSlot() {
			return slot;
		}
	}
}
