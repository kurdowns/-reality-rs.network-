package com.zionscape.server.tests;

import com.zionscape.server.Server;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.npcs.drops.Drop;
import com.zionscape.server.model.npcs.drops.NPCDropsHandler;
import com.zionscape.server.model.players.Player;

import java.util.List;

public class NPCDropsTester {

	// the npc id we're testing drops for.
	private static int NPC_ID = 1;

	public static void main(String[] args) throws Exception {

		Server.loadSettings();

		// load the cache
		new NPCHandler();

		// load the drops
		NPCDropsHandler.loadDrops();

		// 1000 kills
		for (int i = 0; i < 1000; i++) {
			List<Drop> drops = NPCDropsHandler.getRandomDrops(new Player(9362), NPC_ID, 1);

			System.out.println("kill " + i);

			if (drops != null) {
				drops.stream().forEach(
						x -> System.out.println("- dropped id: " + x.getItemId() + " amount: " + x.getDropAmount()));
			} else {
				System.out.println("- dropped nothing");
			}

		}

	}

}