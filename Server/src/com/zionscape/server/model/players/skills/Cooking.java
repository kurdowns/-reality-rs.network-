package com.zionscape.server.model.players.skills;

import com.zionscape.server.Server;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.tick.Tick;
import com.zionscape.server.util.Misc;

public class Cooking {

	/*
	 * Level required, stop burning, experience, raw, cooked, burnt
	 */
	private static final short[][] data = {
			// CID ,RID ,BID ,level ,xp ,burnlvl
			{315, 317, 323, 1, 30, 33}, // SHRIMP
			{319, 321, 323, 1, 30, 34}, // ANCHOVIES
			{325, 327, 369, 1, 40, 34}, // SARDINE
			{329, 331, 343, 25, 90, 58}, // SALMON
			{333, 335, 343, 15, 70, 50}, // TROUT
			{339, 341, 343, 18, 75, 39}, // COD
			{347, 345, 357, 5, 50, 37}, // HERRING
			{351, 349, 357, 20, 80, 52}, // PIKE
			{355, 353, 357, 10, 60, 45}, // MACKEREL
			{361, 359, 367, 30, 100, 64}, // TUNA
			{365, 363, 367, 43, 130, 64}, // BASS
			{373, 371, 357, 45, 140, 86}, // SWORDIE
			{379, 377, 381, 40, 120, 65}, // Lobster
			{385, 383, 387, 80, 210, 99}, // Shark
			{391, 389, 393, 91, 216, 99}, // Manta ray
			{397, 395, 399, 82, 211, 99}, // sea turtle
			{7946, 7944, 7948, 62, 150, 90}, // monkfish,
			{15272, 15270, 15274, 93, 380, 99}, // rocktails
	};
	public Player client;
	public int cookingItem;

	public boolean useRange;

	public Cooking(Player c) {
		this.client = c;
	}

	public void cookItem(final int item, final int amount) {
		byte indexID = 0;
		for (byte index = 0; index < data.length; index++) {
			if (data[index][1] == item) {
				indexID = index;
			}
		}

		/*
		 * item checking
		 */
		if (amount == 0 || !client.getItems().playerHasItem(data[indexID][1], 1)) {
			this.setCooking(false);
			return;
		}

		/*
		 * Level checking
		 */
		if (client.level[PlayerConstants.COOKING] < data[indexID][3]) {
			client.sendMessage("You need a cooking level of " + data[indexID][3] + " to cook this fish.");
			return;
		}
		if (useRange) {
			client.startAnimation(896);
		} else {
			client.startAnimation(897);
		}
		client.getPA().removeAllWindows();
		if (client.isCooking) {
			return;
		}
		this.setCooking(true);
		final byte finalIndex = indexID;
		Server.getTickManager().submit(new Tick(5) {

			int amountToCook = amount;

			@Override
			public void execute() {
				if (client == null || client.isDisconnected()) {
					this.stop();
					return;
				}
				if (amountToCook == 0 || !client.getItems().playerHasItem(data[finalIndex][1], 1)) {
					Cooking.this.setCooking(false);
					this.stop();
					return;
				}
				if (useRange) {
					client.startAnimation(896);
				} else {
					client.startAnimation(897);
				}

				double xpMulti = 1;
				int skillItemCount = SkillItems.getCookingItemsWorn(client);

				if(skillItemCount > 0) {
					if(skillItemCount >= 5) {
						xpMulti += 0.15;
					} else {
						xpMulti += skillItemCount * 0.02;
					}
				}

				/*
                 * Checks if it needs to be burned
				 */
				if (client.level[PlayerConstants.COOKING] >= data[finalIndex][5] ? false : Cooking.this.isBurned(finalIndex) && skillItemCount < 3) {
                    /*
                     * Burning part
					 */
					client.sendMessage("You accidently burn the " + client.getItems().getItemName(data[finalIndex][0]) + ".");
					client.getItems().deleteItem(data[finalIndex][1], client.getItems().getItemSlot(data[finalIndex][1]), 1);
					client.getItems().addItem(data[finalIndex][2], 1);
				} else {
					/*
					 * Cooking part
					 */


					client.sendMessage("You managed to cook a " + client.getItems().getItemName(data[finalIndex][0]) + ".");
					client.getPA().addSkillXP((int)Math.round(xpMulti * (double) data[finalIndex][4]), PlayerConstants.COOKING);

					//if(skillItemCount >= 5 && Misc.random(5) == 0) {
					//	client.getItems().deleteItem(data[finalIndex][1], client.getItems().getItemSlot(data[finalIndex][1]), 1);
					//	client.getItems().addItem(data[finalIndex][0], 1);
					//} else {
						client.getItems().deleteItem(data[finalIndex][1], client.getItems().getItemSlot(data[finalIndex][1]), 1);
						client.getItems().addItem(data[finalIndex][0], 1);
				//	}

					if (data[finalIndex][0] == 385) {
						Achievements.progressMade(client, Achievements.Types.COOK_1000_SHARKS);
					}

					if (data[finalIndex][0] == 361) {
						Achievements.progressMade(client, Achievements.Types.COOK_500_TUNA);
					}

				}
				Cooking.this.setCooking(true);
				amountToCook--;
			}
		});
	}

	public boolean isBurned(byte index) {
		short burnLevel = data[index][5];
		short cookLevel = data[index][3];
		/*
		 * Explaining. Level needed : 1 burn level : 100 between 1 and 100 there is a 100% obv in this case 100. max
		 * burn % is 80 so. 100*0,80 = 80 out of 100 burned if level 50 50*0,80 = 40 out of 100 burned if level 90
		 * 10*0,80 = 8 out of 100 burned
		 */
		// 94 - 50 = 44 total
		short burnMax = (short) (burnLevel - cookLevel);
		short burnProcentage = 0;
		// 44*0,80= 35 out of 44
		// 1*0.80 = 0,8 out of 44
		if (!useRange) {
			burnProcentage = (short) (burnMax * 0.70);
		} else {
			burnProcentage = (short) (burnMax * 0.60);
		}
		if (burnMax < 1) {
			return false;
		}
		// Random number out of 44
		short burnRandom = (short) Misc.random(burnMax);
		// if its below 35 it burns;
		if (burnRandom <= burnProcentage) {
			return true;
		}
		return false;
	}

	/*
	 * An array of items which can be cooked.
	 */
	public boolean isCookable(int item) {
		for (int i = 0; i < data.length; i++) {
			if (item == data[i][1]) {
				return true;
			}
		}
		return false;
	}

	public void setCooking(boolean cooking) {
		client.isCooking = cooking;
	}
}