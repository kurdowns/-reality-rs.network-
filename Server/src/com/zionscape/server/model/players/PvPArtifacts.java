package com.zionscape.server.model.players;

import com.zionscape.server.util.Misc;
import com.zionscape.server.world.ItemDrops;

public class PvPArtifacts {

	public static final int ARTIFACT_RARITY = 30;// higher = more rare drop
	private static final int[][] ARTIFACT_DATA = {{14876, 2500000}, {14877, 1000000}, {14878, 750000},
			{14879, 500000}, {14880, 400000}, {14881, 300000}, {14882, 250000}, {14883, 200000},
			{14884, 150000}, {14885, 100000}, {14886, 75000}, {14888, 40000}, {14889, 30000},
			{14890, 20000}, {14891, 10000}, {14892, 5000}};// itemId, value

	public static void dropRandomArtifact(Player c, int x, int y, int killedId) {
		int item = ARTIFACT_DATA[Misc.random(ARTIFACT_DATA.length - 1)][0];
		if (killedId == -1) {
			return;
		}
		ItemDrops.createGroundItem(c, item, x, y, 1, killedId, false);
		c.sendMessage("You receive a pvp artifact drop.");
	}

	public static void handleMandrith(Player c) {
		int coinsToAdd = 0;
		for (int j = 0; j < ARTIFACT_DATA.length; j++) {
			if (c.getItems().playerHasItem(ARTIFACT_DATA[j][0])) {
				int count = c.getItems().getItemCount(ARTIFACT_DATA[j][0]);
				int coinVal = ARTIFACT_DATA[j][1];
				int coinsIncrement = count * coinVal;
				coinsToAdd += coinsIncrement;
			}
		}
		if (coinsToAdd != 0) {
			c.getItems().addItem(995, coinsToAdd);
		}
		PvPArtifacts.deleteAllArtifacts(c);
		c.getPA().removeAllWindows();
	}

	public static boolean hasArtifact(Player c) {
		for (int j = 0; j < ARTIFACT_DATA.length; j++) {
			if (c.getItems().playerHasItem(ARTIFACT_DATA[j][0])) {
				return true;
			}
		}
		return false;
	}

	private static void deleteAllArtifacts(Player c) {
		for (int j = 0; j < ARTIFACT_DATA.length; j++) {
			int id = ARTIFACT_DATA[j][0];
			int count = c.getItems().getItemAmount(id);
			if (count != 0) {
				c.getItems().deleteItem(id, count);
			}
		}
	}
}