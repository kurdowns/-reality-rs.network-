package com.zionscape.server.model.content.minigames;

import com.zionscape.server.gamecycle.GameCycleTask;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.util.Misc;

import java.util.ArrayList;
import java.util.List;

public final class FightKiln {

	// private static final int[][] COORDINATES = {{2403, 5094}, {2390, 5096}, {2392, 5077}, {2408, 5080}, {2413, 5108}, {2381, 5106}, {2379, 5072}, {2420, 5082}};

	private static final int[][] WAVE_NPCS = {
			{2743, 2743}, // wave 1
			{2745}, // wave 2
			{2743, 2745}, // wave 3
			{2743, 2743, 2745}, // wave 4
			{2743, 2743, 2745, 2627}, // wave 5
			{2743, 2743, 2745, 2627, 2627}, // wave 6
			{2743, 2743, 2745, 2627, 2627, 2627}, // wave 7
			{2743, 2743, 2743}, // wave 8
			{2745, 2745} // wave 9
	};

	public static void enterFightKiln(Player player) {
		player.getPA().closeAllWindows();

		player.setAttribute("in_fight_kiln", true);
		player.setAttribute("fight_kiln_spawned_npcs", new ArrayList<NPC>());

		player.getPA().resetDamageDoneByPlayers();
		player.getData().fightKilnWave = 0;
		player.getPA().movePlayer(2413, 5117, player.playerId * 4);

		player.sendMessage("Your first wave will start in 10 seconds prepare.");

		GameCycleTaskHandler.addEvent(player, new GameCycleTask() {
			@Override
			public void execute(GameCycleTaskContainer container) {
				// make sure we're still in the cave
				if (player.getAttribute("in_fight_kiln") == null || !player.inFightCaves()) {
					container.stop();
					return;
				}
				spawnWave(player);
				container.stop();
			}
		}, 17);
	}

	public static void onPlayerLoggedIn(Player player) {
		if (player.getData().fightKilnWave > -1) {

			player.getPA().movePlayer(player.absX, player.absY, player.heightLevel * 4);

			player.sendMessage("Your wave will start in 10 seconds prepare.");

			player.setAttribute("in_fight_kiln", true);
			player.setAttribute("fight_kiln_spawned_npcs", new ArrayList<NPC>());
			player.setAttribute("fight_kiln_npcs_killed", new ArrayList<Integer>());
			GameCycleTaskHandler.addEvent(player, new GameCycleTask() {
				@Override
				public void execute(GameCycleTaskContainer container) {
					// make sure we're still in the cave
					if (player.getAttribute("in_fight_kiln") == null || !player.inFightCaves()) {
						container.stop();
						return;
					}
					spawnWave(player);
					container.stop();
				}
			}, 17);
		}
	}

	public static void onPlayerLoggedOut(Player player) {
		if (player.getAttribute("in_fight_kiln") != null) {
			FightKiln.exit(player, true);
		}
	}


	public static void onPlayerDied(Player player) {
		if (player.getAttribute("in_fight_kiln") != null) {
			FightKiln.exit(player, false);
			player.getData().fightKilnWave = -1;
		}
	}

	@SuppressWarnings("unused")
	public static void onNpcKilled(Player player, NPC npc) {

		if (player.getAttribute("in_fight_kiln") == null) {
			return;
		}

		@SuppressWarnings("unchecked")
		List<NPC> spawnedNpcs = (List<NPC>) player.getAttribute("fight_kiln_spawned_npcs");
		if (spawnedNpcs.contains(npc)) {
			spawnedNpcs.remove(npc);
		} else {
			return;
		}

		player.setAttribute("fight_kiln_spawned_npcs", spawnedNpcs);

		if (spawnedNpcs.size() == 0) {
			player.getData().fightKilnWave++;
			if (player.getData().fightKilnWave > WAVE_NPCS.length - 1) {
				FightKiln.exit(player, false);
				player.getData().fightKilnWave = -1;

				player.getPA().movePlayer(2438, 5169, 0);

				if (player.getItems().freeInventorySlots() == 0) {
					player.getItems().addDirectlyToBank(8465, 1);
					player.getPA().sendNpcChat(2617, "Congratulations on defeating the Fight Kiln. You may find your award in your bank.");
				} else {
					player.getItems().addItem(8465, 1);
					player.getPA().sendNpcChat(2617, "Congratulations on defeating the Fight Kiln. You may find your award in your inventory.");
				}


				Achievements.progressMade(player, Achievements.Types.COMPLETE_KILN_20_TIMES);
			} else {
				player.sendMessage("Your next wave will start in 5 seconds prepare.");
				GameCycleTaskHandler.addEvent(player, new GameCycleTask() {
					@Override
					public void execute(GameCycleTaskContainer container) {
						// make sure we're still in the cave
						if (player.getAttribute("in_fight_kiln") == null || !player.inFightCaves()) {
							container.stop();
							return;
						}
						spawnWave(player);
						container.stop();
					}
				}, 8);
			}
		}
	}

	@SuppressWarnings("unused")
	public static boolean onObjectClick(Player player, int objectId, Location objectLocation, int option) {
		if (objectId == 9357) {
			if (player.getAttribute("in_fight_kiln") != null) {
				FightKiln.exit(player, false);
				player.getPA().movePlayer(2438, 5169, 0);
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private static void exit(Player player, boolean loggedOut) {
		// kill all existing npcs
		List<NPC> spawnedNpcs = (List<NPC>) player.getAttribute("fight_kiln_spawned_npcs");
		spawnedNpcs.stream().forEach(x -> x.isDead = true);

		// set attributes null
		player.removeAttribute("in_fight_kiln");
		player.removeAttribute("fight_kiln_spawned_npcs");

		if (!loggedOut) {
			player.absX = 2438;
			player.absY = 5169;
			player.heightLevel = 0;
			player.getData().fightKilnWave = -1;
			player.getData().fightCaveWave = -1;
		}
	}

	@SuppressWarnings("unchecked")
	private static void spawnWave(Player player) {
		player.setAttribute("fight_kiln_spawned_npcs", new ArrayList<NPC>());

		for (int npcId : WAVE_NPCS[player.getData().fightKilnWave]) {
			NPC npc = NPCHandler.spawnNpc(player, npcId, 2394 + Misc.random(16), 5076 + Misc.random(15), player.heightLevel, 0, FightCaves.getHp(npcId), FightCaves.getMax(npcId), FightCaves.getAtk(npcId), FightCaves.getDef(npcId), true, false);
			npc.setRespawn(false);
			((List<NPC>) player.getAttribute("fight_kiln_spawned_npcs")).add(npc);
		}
	}

}