package com.zionscape.server.model.content.minigames;

import com.zionscape.server.gamecycle.GameCycleTask;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.npcs.other.MissCheevers;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.util.Misc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Pest Control minigame
 *
 * @author Stuart
 */
public class PestControl {

	/**
	 * How long a game is in seconds default 3 minutes
	 */
	private final static int GAME_TIMER = 1200;
	/**
	 * the minimum amount of players to start a game
	 */
	private final static int MIN_GAME = 3;
	/**
	 * the maximum amount of players allowed to play
	 */
	private final static int MAX_GAME = 50;
	/**
	 * CombatHelper level required to play Pest Control
	 */
	private final static int COMBAT_REQUIRED = 40;
	/**
	 * Cords of void attacks
	 */
	private final static int[][] VOID_ATTACKER_CORDS = {{2649, 2593}, {2656, 2586}, {2664, 2592}};
	private static final List<NPC> npcs = new ArrayList<>();
	private static final List<Player> boat = new ArrayList<>();
	private static final List<Player> game = new ArrayList<>();
	private static final NPC[] npcPortals = new NPC[4];
	private static final List<NPC> voidNpcs = new ArrayList<>();
	/**
	 * Give an incentive for players to play
	 */
	public static boolean cycleIncentive = false;
	public static boolean canAttack = false;
	private static int gameTimer;
	private static int waitTimer = 30;
	private static boolean gameRunning = false;
	private static NPC voidKnight;
	private static int portalDef = 0;
	private static NPC[] spinners = new NPC[4];
	private static long lastShieldChange;

	/**
	 * Get a random VoidNpc
	 *
	 * @return A VoidNpc
	 */
	private static VoidNpc getVoidNpc() {
		final int random = Misc.random(2);
		if (random == 0)
			return VoidNpc.SPINNER1;
		else if (random == 1)
			return VoidNpc.SPINNER2;
		else if (random == 2)
			return VoidNpc.SPINNER3;
		return VoidNpc.SPINNER1;
	}

	/**
	 * Get the amount of players currently playing
	 *
	 * @return
	 */
	public static int playerCount() {
		int count = 0;
		count += boat.size();
		count += game.size();
		return count;
	}

	public static boolean onObjectClicked(Player player, int objectId, Location objectPosition) {
		switch (objectId) {
			case 14315:
				enterBoat(player);
				return true;
			case 14314:
				leaveBoat(player);
				return true;
		}
		return false;
	}

	/**
	 * Called every server tick
	 */
	public static void process() {
		if (gameTimer > 0) {
			gameTimer--;
		}
		if (waitTimer > 0) {
			waitTimer--;
		}
		if (waitTimer == 0) {
			startGame();
		}
		if (gameRunning) {
			if (gameTimer == 0 || game.size() == 0 || allPortalsDead() || isVoidDead()) {
				endGame();
			} else if (canAttack) {
				if (System.currentTimeMillis() - lastShieldChange > 125 * 1000) {
					updateShields();
					lastShieldChange = System.currentTimeMillis();
				}
				for (NPC npc : voidNpcs) {
					if (npc.attackingNpcId <= 0) {
						npc.attackingNpcId = voidKnight.slot;
					}
				}
				for (int i = 0; i < spinners.length; i++) {
					if (Misc.random(3) == 0 && spinners[i].getHP() > 0 && !spinners[i].needRespawn && !npcPortals[i].needRespawn && npcPortals[i].getHP() < 200) {
						npcPortals[i].incrementHP(Misc.random(10, 20));//10-20
						npcPortals[i].hitUpdateRequired = true;
					}
				}
			}
		}

		Iterator<Player> playerIterator = boat.iterator();
		while (playerIterator.hasNext()) {
			Player player = playerIterator.next();

			if (player.isDisconnected() || player.outStream == null) {
				playerIterator.remove();
				continue;
			}
			waitInterface(player);
		}

		playerIterator = game.iterator();
		while (playerIterator.hasNext()) {
			Player player = playerIterator.next();

			if (player.isDisconnected() || player.outStream == null) {
				playerIterator.remove();
				continue;
			}
			gameInterface(player);
		}
	}

	/**
	 * Start a game of pest control
	 */
	public static void startGame() {
		if (boat.size() >= MIN_GAME) {
			spawnNpcs();
			waitTimer = GAME_TIMER + 60;
			gameTimer = GAME_TIMER;
			gameRunning = true;
			int slot = 0;

			int totalCombatLevel = 0;

			for (final Player player : boat) {

				if (game.contains(player)) {
					System.out.println("some how " + player + " is already in the game wtf?");
					continue;
				}

				totalCombatLevel += player.combatLevel;
				game.add(player);

				player.inPestControl = true;
				resetPlayer(player, false);
				teleport(player, 2658, 2611, 0);
				slot++;
			}
			updateShields();
			portalDef = (int) Math.round(totalCombatLevel * 0.25);
			boat.clear();
		} else {
			waitTimer = 60;
			for (final Player player : boat)
				player.sendMessage("There needs to be at least " + MIN_GAME + " players to start a game of pest control.");
		}
	}

	/**
	 * Ends the minigame
	 */
	public static void endGame() {
		waitTimer = 60;
		gameTimer = GAME_TIMER;
		gameRunning = false;
		for (final Player player : game) {
			resetPlayer(player, true);
			if (gameTimer == 0)
				player.sendMessage("Oh dear looks like you ran out of time!");
			else if (allPortalsDead()) {
				if (player.pestControlTempPoints < 50) {
					player.sendMessage("The knights noticed your lack of zeal. Try harder next time!");
				} else {
					//int points = player.pestControlTempPoints / 100;
					int points;
					
					if (player.isUberDonator())
						points = 12;
					else if(player.isLegendaryDonator())
						points = 9;
					else if (player.isExtremeDonator())
						points = 8;
					else if (player.isSuperDonator())
						points = 7;
					else if (player.isDonator())
						points = 6;
					else 
						points = 5;
					// if(ServerEvents.isEventActive(ServerEvents.DOUBLE_PEST_CONTROL_POINTS)) {
					//     points *= 2;
					//  }


					Achievements.progressMade(player, Achievements.Types.COMPLETE_PEST_CONTROL_100_TIMES);
					Achievements.progressMade(player, Achievements.Types.WIN_500_PEST_GAMES);
					Achievements.progressMade(player, Achievements.Types.WIN_750_PEST_CONTROL_GAMES);

					if (MissCheevers.isPerkActive(player, MissCheevers.PC)) {
						points *= 2;
					}

					player.pcPoints += points;
					player.getPA().sendFrame126("@or1@[@whi@" + player.pcPoints + "@or1@] Pest Control Points", 54430);
					player.sendMessage("Congratulations you have accumulated " + points + " points!");
					player.sendMessage("You now have a total of " + player.pcPoints + " Pest points!");
					player.getItems().addItem(995, 175000);
				}
			} else if (isVoidDead())
				player.sendMessage("Oh dear it looks like the Void Knight was killed!");

			player.inPestControl = false;
			player.pestControlTempPoints = 0;
			player.getPA().movePlayer(2657, 2639, 0);
			player.getPA().restorePlayer();
		}
		killNpcs();
		game.clear();
		canAttack = false;

		GameCycleTaskHandler.stopEvents(PestControl.class);

		if (cycleIncentive) {
			cycleIncentive = false;
		}
	}

	/**
	 * Player entering boat
	 *
	 * @param player
	 */
	public static void enterBoat(final Player player) {

		if(player.teleTimer > 0) {
			return;
		}

		if (player.combatLevel < COMBAT_REQUIRED) {
			player.sendMessage("You must be at least " + COMBAT_REQUIRED + " combat level to play Pest Control.");
			return;
		}
		if (boat.size() >= MAX_GAME) {
			player.sendMessage("There is already a max amount of " + MAX_GAME + " playing.");
			return;
		}

		boat.add(player);
		player.getPA().movePlayer(2661, 2639, 0);
		player.inPestControl = true;
	}

	/**
	 * Player leaving the boat
	 *
	 * @param player
	 */
	public static void leaveBoat(final Player player) {
		boat.remove(player);
		player.inPestControl = false;
		player.getPA().movePlayer(2657, 2639, 0);
	}

	/**
	 * Player died
	 *
	 * @param player
	 */
	public static void playerDied(final Player player) {
		resetPlayer(player, false);
		teleport(player, 2658, 2611, 0);
	}

	/**
	 * Player logging out
	 *
	 * @param player The Player
	 */
	public static void playerLoggedOut(final Player player) {
		if (boat.remove(player)) {
			player.absX = 2657;
			player.absY = 2639;
			player.heightLevel = 0;
		}
		if (game.remove(player)) {
			player.absX = 2657;
			player.absY = 2639;
			player.heightLevel = 0;
		}
	}

	/**
	 * Is the void knight dead?
	 *
	 * @return True / False
	 */
	private static boolean isVoidDead() {
		return voidKnight.isDead || voidKnight.getHP() == 0;
	}

	/**
	 * Player has dealt damage to an npc in the game
	 *
	 * @param player The player
	 * @param damage The amount of damage
	 */
	public static void dealtNpcDamage(final Player player, final int damage) {
		player.pestControlTempPoints += damage;
	}

	/**
	 * Have all the portals been killed?
	 *
	 * @return
	 */
	private static boolean allPortalsDead() {
		int count = 0;
		for (int i = 0; i < npcPortals.length; i++) {
			if (npcPortals[i].needRespawn) {
				count++;
			}
		}
		return (count == 4);
	}

	/**
	 * Teleports a player without notifying the minigames a player has teleports
	 *
	 * @param player The player
	 * @param x      The x cord
	 * @param y      The Y cord
	 * @param z      The z cord
	 */
	public static void teleport(Player player, int x, int y, int z) {
		player.setTeleportLocation(Location.create(x, y, z));
		player.getPA().requestUpdates();
	}

	/**
	 * Spawns all of the NPC's
	 */
	private static void spawnNpcs() {
		/**
		 * Portals
		 */
		npcPortals[0] = NPCHandler.spawnNpc2(6142, 2628, 2591, 0, 0, 500, 0, 0, portalDef + (boat.size() * 4)); // Purple
		npcPortals[1] = NPCHandler.spawnNpc2(6143, 2680, 2588, 0, 0, 500, 0, 0, portalDef + (boat.size() * 4)); // Blue
		npcPortals[2] = NPCHandler.spawnNpc2(6144, 2669, 2570, 0, 0, 500, 0, 0, portalDef + (boat.size() * 4)); // Yellow
		npcPortals[3] = NPCHandler.spawnNpc2(6145, 2645, 2569, 0, 0, 500, 0, 0, portalDef + (boat.size() * 4)); // Grey
		for (int i = 0; i < npcPortals.length; i++) {
			npcs.add(npcPortals[i]);
		}
		/**
		 * Void Knight
		 */
		voidKnight = NPCHandler.spawnNpc2(3782, 2656, 2592, 0, 0, 2000, 0, 0, 0);
		npcs.add(voidKnight);
		/**
		 * Shifters Attacking Void Knight
		 */

		GameCycleTaskHandler.addEvent(PestControl.class, new GameCycleTask() {
			@Override
			public void execute(final GameCycleTaskContainer container) {
				canAttack = true;
				for (int i = 0; i < 2; i++) {
					final VoidNpc npc = getVoidNpc();
					final int[] cords = VOID_ATTACKER_CORDS[(int) (Math.random() * VOID_ATTACKER_CORDS.length)];

					NPC spawn = NPCHandler.spawnNpc2(npc.id, cords[0], cords[1], 0, 0, npc.hp, npc.maxHit, npc.attack, npc.defence);
					spawn.setRespawn(false);
					voidNpcs.add(spawn);
				}
				for (NPC npc : voidNpcs) {
					if (npc.attackingNpcId <= 0) {
						npc.attackingNpcId = voidKnight.slot;
					}
					GameCycleTaskHandler.addEvent(PestControl.class, new GameCycleTask() {

						@Override
						public void execute(final GameCycleTaskContainer container) {
							if (!gameRunning) {
								container.stop();
								return;
							}
							final VoidNpc npc = getVoidNpc();
							final int[] cords = VOID_ATTACKER_CORDS[(int) (Math.random() * VOID_ATTACKER_CORDS.length)];
							NPC spawn = NPCHandler.spawnNpc2(npc.id, cords[0], cords[1], 0, 0, npc.hp, npc.maxHit, npc.attack, npc.defence);
							spawn.setRespawn(false);
							voidNpcs.add(spawn);
						}
					}, 60 - ((game.size() > 15 ? 15 : game.size()) * 2));
					container.stop();
				}
			}
		}, 5);
		/**
		 * Spinners
		 */
		spinners[0] = NPCHandler.spawnNpc2(3747, 2626, 2589, 0, 0, 55, 12, 100, 85); // Purple
		spinners[0].setRespawn(false);
		spinners[1] = NPCHandler.spawnNpc2(3747, 2678, 2586, 0, 0, 55, 12, 100, 85); // Blue
		spinners[1].setRespawn(false);
		spinners[2] = NPCHandler.spawnNpc2(3747, 2667, 2568, 0, 0, 55, 12, 100, 85); // Yellow
		spinners[2].setRespawn(false);
		spinners[3] = NPCHandler.spawnNpc2(3747, 2643, 2567, 0, 0, 55, 12, 100, 85); // Grey
		spinners[3].setRespawn(false);

		for (int i = 0; i < spinners.length; i++) {
			npcs.add(spinners[i]);
		}
		/**
		 * Random Aggressive NPC's
		 */
		NPC npc = NPCHandler.spawnNpc2(3740, 2678, 2587, 0, 0, 100, 9, 30, 30);
		npc.setRespawn(false);
		npcs.add(npc);

		npc = NPCHandler.spawnNpc2(3740, 2666, 2572, 0, 0, 100, 9, 30, 30);
		npc.setRespawn(false);
		npcs.add(npc);

		npc = NPCHandler.spawnNpc2(3740, 2647, 2575, 0, 0, 100, 9, 30, 30);
		npc.setRespawn(false);
		npcs.add(npc);

		npc = NPCHandler.spawnNpc2(3740, 2632, 2595, 0, 0, 100, 9, 30, 30);
		npc.setRespawn(false);
		npcs.add(npc);

		npc = NPCHandler.spawnNpc2(3775, 2634, 2589, 0, 0, 100, 14, 45, 50);
		npc.setRespawn(false);
		npcs.add(npc);

		npc = NPCHandler.spawnNpc2(3775, 2649, 2569, 0, 0, 100, 14, 45, 50);
		npc.setRespawn(false);
		npcs.add(npc);

		npc = NPCHandler.spawnNpc2(3775, 2670, 2569, 0, 0, 100, 14, 45, 50);
		npc.setRespawn(false);
		npcs.add(npc);

		npc = NPCHandler.spawnNpc2(3775, 2678, 2591, 0, 0, 100, 14, 45, 50);
		npc.setRespawn(false);
		npcs.add(npc);

		npc = NPCHandler.spawnNpc2(3775, 2676, 2589, 0, 0, 100, 14, 45, 50);
		npc.setRespawn(false);
		npcs.add(npc);
	}

	/**
	 * Kills off all the NPC's
	 */
	private static void killNpcs() {
		for (NPC npc : npcs) {
			npc.animNumber = 0x328;
			npc.updateRequired = true;
			npc.animUpdateRequired = true;

			for (int i = 0; i < NPCHandler.npcs.length; i++) {
				if (NPCHandler.npcs[i] == npc) {
					NPCHandler.npcs[i] = null;
				}
			}
		}
		for (NPC npc : voidNpcs) {
			npc.animNumber = 0x328;
			npc.updateRequired = true;
			npc.animUpdateRequired = true;

			for (int i = 0; i < NPCHandler.npcs.length; i++) {
				if (NPCHandler.npcs[i] == npc) {
					NPCHandler.npcs[i] = null;
				}
			}
		}

		spinners = new NPC[4];
		voidKnight = null;

		voidNpcs.clear();
		npcs.clear();
	}

	/**
	 * Time left as String
	 *
	 * @param timer
	 * @return
	 */
	private static String getTime(final int timer) {
		final int remainder = timer % 3600;
		final int minutes = remainder / 60;
		final int seconds = remainder % 60;
		return ((minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds);
	}

	/**
	 * Interface sent while in the boat
	 *
	 * @param player
	 */
	private static void waitInterface(final Player player) {
		player.getPA().sendFrame126("Next Departure: " + getTime(waitTimer), 21120);
		player.getPA().sendFrame126("Players Ready: " + boat.size() + "", 21121);
		player.getPA().sendFrame126("(Need " + MIN_GAME + " to " + MAX_GAME + " players)", 21122);
		player.getPA().sendFrame126("Points: " + player.pcPoints + "", 21123);
	}

	/**
	 * Interface sent while in a game
	 *
	 * @param player
	 */
	private static void gameInterface(final Player player) {
		player.getPA().sendFrame126("" + voidKnight.getHP() + "", 21115);
		player.getPA().sendFrame126("" + player.pestControlTempPoints + "", 21116);
		player.getPA().sendFrame126(getTime(gameTimer), 21117);
		for (int j = 0; j < NPCHandler.npcs.length; j++) {
			if (NPCHandler.npcs[j] != null) {
				if (NPCHandler.npcs[j].type == 6142 || NPCHandler.npcs[j].type == 6146)
					if (!NPCHandler.npcs[j].needRespawn)
						player.getPA().sendFrame126("" + NPCHandler.npcs[j].getHP() + "", 21111);
					else
						player.getPA().sendFrame126("@red@0", 21111);
				if (NPCHandler.npcs[j].type == 6143 || NPCHandler.npcs[j].type == 6147)
					if (!NPCHandler.npcs[j].needRespawn)
						player.getPA().sendFrame126("" + NPCHandler.npcs[j].getHP() + "", 21112);
					else
						player.getPA().sendFrame126("@red@0", 21112);
				if (NPCHandler.npcs[j].type == 6144 || NPCHandler.npcs[j].type == 6148)
					if (!NPCHandler.npcs[j].needRespawn)
						player.getPA().sendFrame126("" + NPCHandler.npcs[j].getHP() + "", 21113);
					else
						player.getPA().sendFrame126("@red@0", 21113);
				if (NPCHandler.npcs[j].type == 6145 || NPCHandler.npcs[j].type == 6149)
					if (!NPCHandler.npcs[j].needRespawn)
						player.getPA().sendFrame126("" + NPCHandler.npcs[j].getHP() + "", 21114);
					else
						player.getPA().sendFrame126("@red@0", 21114);
			}
		}
	}

	/**
	 * Reset a players variables
	 *
	 * @param player
	 */
	private static void resetPlayer(final Player player, boolean teleported) {
		/*Prayer.resetPrayers(player);
		if (teleported) {
            for (int i = 0; i < player.playerLevel.length; i++) {
                player.playerLevel[i] = player.actualLevels[i];
                player.getPA().refreshSkill(i);
            }
            player.specAmount = player.originalSpec;
            player.getItems().addSpecialBar(player.equipment[PlayerConstants.WEAPON]);
            player.setEnergyLeft(player.originalEnergy);
            player.getPA().sendFrame126(player.getEnergyLeft() + "%", 149);
        } else {
            for (int i = 0; i < player.playerLevel.length; i++) {
                player.playerLevel[i] = player.actualLevels[i];
                player.getPA().refreshSkill(i);
            }
            player.specAmount = 10;
            player.getItems().addSpecialBar(player.equipment[PlayerConstants.WEAPON]);
            player.setEnergyLeft(100);
            player.getPA().sendFrame126(player.getEnergyLeft() + "%", 149);
        }*/
	}

	private static void updateShields() {
		List<Integer> ids = new ArrayList<>();

		for (int i = 0; i < npcPortals.length; i++) {
			if (!npcPortals[i].needRespawn) {
				ids.add(i);
			}
		}

		if (ids.size() == 0) {
			return;
		}

		int random = ids.get(Misc.random(ids.size() - 1));

		for (Integer i : ids) {
			NPC npc = npcPortals[i];

			// should never happen but will check anyway
			if (npc == null) {
				continue;
			}
			if (i == random) {
				npc.damageImmune = false;

				if (npc.type > 6145) {
					npc.type -= 4;
				}

				String message = "";

				switch (npc.type) {
					case 6142:
						message = "@blu@The purple, western portal shield has dropped!";
						break;
					case 6143:
						message = "@blu@The blue, eastern portal shield has dropped!";
						break;
					case 6144:
						message = "@blu@The yellow, south-eastern portal shield has dropped!";
						break;
					case 6145:
						message = "@blu@The red, south-western portal shield has dropped!";
						break;
				}

				for (Player player : game) {
					player.sendMessage(message);
				}

			} else {
				npc.damageImmune = true;
				if (npc.type < 6146) {
					npc.type += 4;
				}
			}

			npc.requestTransform(npc.type);
		}

		lastShieldChange = System.currentTimeMillis();
	}


	public static void npcDied(NPC npc) {
		if (npc.type >= 6142 && npc.type <= 6149) {
			updateShields();
		}
	}

	public static void npcRespawn(NPC npc) {
		if (npcs.remove(npc)) {

			// spinners
			for (int i = 0; i < spinners.length; i++) {
				if (spinners[i] == npc) {
					spinners[i] = NPCHandler.spawnNpc2(3747, npc.absX, npc.absY, npc.heightLevel, 0, 55, 12, 100, 85); // Purple
					spinners[i].setRespawn(false);

					npcs.add(spinners[i]);
					return;
				}
			}

			// npcs attacking the void knight
			if (voidNpcs.contains(npc)) {
				voidNpcs.remove(npc);
				return;
			}

			// other aggressive npcs
			npc = NPCHandler.spawnNpc2(npc.type, npc.absX, npc.absY, npc.heightLevel, 0, npc.maxHP, npc.maxHit, npc.attack, npc.defence);
			npc.setRespawn(false);
			npcs.add(npc);
		}
	}

	public static boolean isShifter(int npc) {
		if (npc == 3732 || npc == 3734 || npc == 3736) {
			return true;
		}
		return false;
	}

	private enum VoidNpc {

		SPINNER1(3732, 45, 4, 50, 100), SPINNER2(3734, 65, 11, 80, 100), SPINNER3(3736, 85, 6, 100, 100);

		public int id;
		public int hp;
		public int maxHit;
		public int attack;
		public int defence;

		VoidNpc(final int a, final int b, final int c, final int d, final int e) {
			this.id = a;
			this.hp = b;
			this.maxHit = c;
			this.attack = d;
			this.defence = e;
		}

	}

}