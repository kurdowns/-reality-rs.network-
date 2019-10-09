package com.zionscape.server.tools;

import com.zionscape.server.Server;
import com.zionscape.server.model.items.ItemDefinition;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.npcs.drops.Drop;
import com.zionscape.server.model.npcs.drops.NPCDropsHandler;
import com.zionscape.server.util.DatabaseUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DropSimulator {


	private static final int NPC_ID = 2042;

	static Map<Integer, Integer> maps = new HashMap<>();

	public static void main(String[] unused) throws Exception {

		Server.loadSettings();
		DatabaseUtil.init();
		ItemDefinition.load();
		NPCDropsHandler.loadDrops();

		int average = 0;

		for (int a = 0; a < 100; a++) {
			maps.clear();

			System.out.println("SET: " + a);
			System.out.println("");
			for (int i = 0; i < 500; i++) {
				List<Drop> drops = NPCDropsHandler.getRandomDrops(null, NPC_ID, 1);
				if (drops != null) {
					for (Drop drop : drops) {
						if (maps.containsKey(drop.getItemId())) {
							maps.put(drop.getItemId(), maps.get(drop.getItemId()) + drop.getDropAmount());
						} else {
							maps.put(drop.getItemId(), drop.getDropAmount());
						}
					}
				}
			}

			for (Map.Entry<Integer, Integer> entry : maps.entrySet()) {

				if (ItemUtility.getName(entry.getKey()).equals("Elysian sigil")) {
					average += entry.getValue();
				}

				System.out.println(ItemUtility.getName(entry.getKey()) + " - " + entry.getValue());
			}
		}

		System.out.println("Elysian sigil average over 1000x100 kills " + (10000 / (double) average) + " total: " + average);
	}

}