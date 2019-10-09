package com.zionscape.server.model.content.minigames;

import com.zionscape.server.gamecycle.GameCycleTask;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.util.Misc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Stuart
 */
public class FightCaves {

	public static final int TZ_KIH = 2627, TZ_KEK_SPAWN = 2738, TZ_KEK = 2630, TOK_XIL = 2631, YT_MEJKOT = 2741, KET_ZEK = 2743, TZTOK_JAD = 2745;
	public static final int YT_HUR_KOT = 2746;


	private static final int[][] COORDINATES = {{2403, 5094}, {2390, 5096}, {2392, 5077}, {2408, 5080}, {2413, 5108}, {2381, 5106}, {2379, 5072}, {2420, 5082}};

	private static final int[][] WAVE_NPCS = {
			{2627}, // 1
			{2627, 2627}, //2
			{2630}, // 3
			{2630, 2627}, //4
			{2630, 2627, 2627}, //5
			{2630, 2630}, //6
			{2631}, // 7
			{2631, 2627}, //8
			{2631, 2627, 2627}, // 9
			{2631, 2630}, // 10
			{2627, 2631, 2630}, // 11
			{2627, 2627, 2631, 2630}, // 12
			{2631, 2630}, // 13
			{2631, 2631}, // 14
			{2741}, // 15
			{2741, 2627}, // 16
			{2741, 2627, 2627}, // 17
			{2741, 2630}, // 18
			{2630, 2741, 2627}, // 19
			{2741, 2627, 2630, 2627}, // 20
			{2630, 2630, 2741}, // 21
			{2631, 2741}, // 22
			{2741, 2627, 2631}, // 23
			{2627, 2627, 2631, 2741}, // 24
			{2630, 2741, 2631}, // 25
			{2741, 2627, 2630, 2631}, // 26
			{2627, 2627, 2741, 2631, 2630}, // 27
			{2630, 2631, 2741, 2630}, // 28
			{2741, 2631, 2631}, // 29
			{2741, 2741}, // 30
			{2743}, // 31
			{2743, 2627}, // 32
			{2743, 2627, 2627}, // 33
			{2743, 2630}, // 34
			{2627, 2630, 2743}, // 35
			{2630, 2627, 2627, 2743}, // 36
			{2630, 2630, 2743}, // 37
			{2631, 2743}, // 38
			{2627, 2631, 2743}, // 39
			{2627, 2627, 2743, 2631}, // 40
			{2630, 2631, 2743}, // 41
			{2627, 2630, 2631, 2743}, // 42
			{2743, 2631, 2630, 2627, 2627}, // 43
			{2630, 2630, 2743, 2631}, // 44
			{2631, 2631, 2743}, // 45
			{2741, 2743}, // 46
			{2627, 2741, 2743}, // 47
			{2743, 2741, 2627, 2627}, // 48
			{2743, 2630, 2741}, // 49
			{2741, 2743, 2630, 2627}, // 50
			{2627, 2627, 2630, 2741, 2743}, // 51
			{2741, 2743, 2630, 2630}, // 52
			{2631, 2743, 2741}, // 53
			{2741, 2627, 2743, 2631}, // 54
			{2627, 2627, 2631, 2741, 2743}, // 55
			{2741, 2630, 2743, 2631}, // 56
			{2627, 2630, 2631, 2741, 2743}, // 57
			{2627, 2627, 2630, 2631, 2741, 2743}, // 58
			{2630, 2630, 2743, 2741, 2631}, // 59
			{2631, 2631, 2741, 2743}, // 60
			{2741, 2743, 2741}, // 61
			{2743, 2743}, // 62
			{2745}, // 63
	};

	public static void enter(Player player) {
		player.getPA().closeAllWindows();

		player.setAttribute("in_fight_caves", true);
		player.setAttribute("fight_cave_spawned_npcs", new ArrayList<NPC>());

		player.getPA().resetDamageDoneByPlayers();
		player.getData().fightCaveWave = 52;
		player.getPA().movePlayer(2413, 5117, player.playerId * 4);

		player.sendMessage("Your first wave will start in 10 seconds prepare.");

		GameCycleTaskHandler.addEvent(player, new GameCycleTask() {
			@Override
			public void execute(GameCycleTaskContainer container) {
				// make sure we're still in the cave
				if (player.getAttribute("in_fight_caves") == null || !player.inFightCaves()) {
					container.stop();
					return;
				}
				spawnWave(player);
				container.stop();
			}
		}, 17);
	}

	public static void onPlayerLoggedIn(Player player) {
		if (player.getData().fightCaveWave > -1) {

			player.getPA().movePlayer(player.absX, player.absY, player.heightLevel * 4);

			player.sendMessage("Your wave will start in 10 seconds prepare.");

			player.setAttribute("in_fight_caves", true);
			player.setAttribute("fight_cave_spawned_npcs", new ArrayList<NPC>());
			player.setAttribute("fight_caves_npcs_killed", new ArrayList<Integer>());
			GameCycleTaskHandler.addEvent(player, new GameCycleTask() {
				@Override
				public void execute(GameCycleTaskContainer container) {
					// make sure we're still in the cave
					if (player.getAttribute("in_fight_caves") == null || !player.inFightCaves()) {
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
		if (player.getAttribute("in_fight_caves") != null) {
			FightCaves.exit(player, true);
		}
	}


	public static void onPlayerDied(Player player) {
		if (player.getAttribute("in_fight_caves") != null) {
			FightCaves.exit(player, false);
			player.getData().fightCaveWave = -1;
		}
	}

	@SuppressWarnings("unused")
	public static void onNpcKilled(Player player, NPC npc) {
		if (player.getAttribute("in_fight_caves") != null) {
			@SuppressWarnings("unchecked")
			List<NPC> spawnedNpcs = (List<NPC>) player.getAttribute("fight_cave_spawned_npcs");
			if (spawnedNpcs.stream().filter(x -> !x.isDead).count() == 0) {
				player.getData().fightCaveWave++;
				if (player.getData().fightCaveWave > WAVE_NPCS.length - 1) {
					FightCaves.exit(player, false);
					player.getData().fightCaveWave = -1;

					player.getPA().movePlayer(2438, 5169, 0);

					if (player.getItems().freeInventorySlots() == 0) {
						player.getItems().addDirectlyToBank(6570, 1);
						player.getPA().sendNpcChat(2617, "Congratulations on defeating the Fight Caves. You may find your award in your bank.");
					} else {
						player.getItems().addItem(6570, 1);
						player.getPA().sendNpcChat(2617, "Congratulations on defeating the Fight Caves. You may find your award in your inventory.");
					}
				} else {
					player.sendMessage("Your next wave will start in 5 seconds prepare.");
					GameCycleTaskHandler.addEvent(player, new GameCycleTask() {
						@Override
						public void execute(GameCycleTaskContainer container) {
							// make sure we're still in the cave
							if (player.getAttribute("in_fight_caves") == null || !player.inFightCaves()) {
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
	}

	@SuppressWarnings("unused")
	public static boolean onObjectClick(Player player, int objectId, Location objectLocation, int option) {
		if (objectId == 9357) {
			if (player.getAttribute("in_fight_caves") != null) {
				FightCaves.exit(player, false);
				player.getPA().movePlayer(2438, 5169, 0);
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private static void exit(Player player, boolean loggedOut) {
		// kill all existing npcs
		List<NPC> spawnedNpcs = (List<NPC>) player.getAttribute("fight_cave_spawned_npcs");
		spawnedNpcs.stream().forEach(x -> x.isDead = true);

		// set attributes null
		player.removeAttribute("in_fight_caves");
		player.removeAttribute("fight_cave_spawned_npcs");

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
		player.setAttribute("fight_cave_spawned_npcs", new ArrayList<NPC>());

		for (int npcId : WAVE_NPCS[player.getData().fightCaveWave]) {
			int[] cords = COORDINATES[Misc.random(COORDINATES.length - 1)];
			NPC npc = NPCHandler.spawnNpc(player, npcId, 2394 + Misc.random(16), 5076 + Misc.random(15), player.heightLevel, 0, FightCaves.getHp(npcId), FightCaves.getMax(npcId), FightCaves.getAtk(npcId), FightCaves.getDef(npcId), true, false);
			npc.setRespawn(false);
			((List<NPC>) player.getAttribute("fight_cave_spawned_npcs")).add(npc);
		}
	}


	public static boolean isFightCaveNpc(NPC npc) {
		switch (npc.type) {
			case TZ_KIH:
			case TZ_KEK:
			case TOK_XIL:
			case YT_MEJKOT:
			case KET_ZEK:
			case TZTOK_JAD:
			case TZ_KEK_SPAWN: {
				return true;
			}
		}
		return false;
	}

	public static int getHp(int npc) {
		switch (npc) {
			case TZ_KIH:
			case TZ_KEK_SPAWN:
				return 10;
			case TZ_KEK:
				return 20;
			case TOK_XIL:
				return 40;
			case YT_MEJKOT:
				return 80;
			case KET_ZEK:
				return 150;
			case TZTOK_JAD:
				return 250;
		}
		return 100;
	}

	public static int getMax(int npc) {
		switch (npc) {
			case TZ_KIH:
			case TZ_KEK_SPAWN:
				return 4;
			case TZ_KEK:
				return 7;
			case TOK_XIL:
				return 13;
			case 2629:
			case YT_MEJKOT:
				return 28;
			case KET_ZEK:
				return 54;
			case TZTOK_JAD:
				return 97;
		}
		return 5;
	}

	public static int getAtk(int npc) {
		switch (npc) {
			case TZ_KIH:
			case TZ_KEK_SPAWN:
				return 30;
			case TZ_KEK:
				return 50;
			case TOK_XIL:
				return 100;
			case 2629:
			case YT_MEJKOT:
				return 150;
			case KET_ZEK:
				return 450;
			case TZTOK_JAD:
				return 650;
		}
		return 100;
	}

	public static int getDef(int npc) {
		switch (npc) {
			case TZ_KIH:
			case TZ_KEK_SPAWN:
				return 30;
			case TZ_KEK:
				return 50;
			case TOK_XIL:
				return 100;
			case 2629:
			case YT_MEJKOT:
				return 150;
			case KET_ZEK:
				return 300;
			case TZTOK_JAD:
				return 500;
		}
		return 100;
	}
}