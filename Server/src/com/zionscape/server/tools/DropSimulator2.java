package com.zionscape.server.tools;

import com.zionscape.server.Server;
import com.zionscape.server.model.items.ItemDefinition;
import com.zionscape.server.model.npcs.drops.Drop;
import com.zionscape.server.model.npcs.drops.NPCDropsHandler;
import com.zionscape.server.util.DatabaseUtil;

import java.util.List;

public class DropSimulator2 {


	private static final int NPC_ID = 2042;
	private static final int ITEM_ID = 25036;

	public static void main(String[] unused) throws Exception {

		Server.loadSettings();
		DatabaseUtil.init();
		ItemDefinition.load();
		NPCDropsHandler.loadDrops();

		int average = 0;

		for (int a = 0; a < 10000; a++) {
			int kills = 0;

			outer:
			while (true) {
				kills++;

				List<Drop> drops = NPCDropsHandler.getRandomDrops(null, NPC_ID, 1);

				if (drops != null) {
					for (Drop drop : drops) {
						if (drop.getItemId() == ITEM_ID) {
							break outer;
						}
					}
				}
			}

			average += kills;

			System.out.println("took: " + kills + " to get the item");
		}

		System.out.println("average kills to get item: " + (average / 1000));

	}

}