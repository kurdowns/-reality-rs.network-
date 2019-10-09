package com.zionscape.server.model.players;

import com.zionscape.server.Config;
import com.zionscape.server.Server;
import com.zionscape.server.ServerEvents;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.Direction;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.tick.Tick;
import com.zionscape.server.util.StopWatch;
import com.zionscape.server.util.Stream;
import com.zionscape.server.world.ItemDrops;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Player Handler Class
 *
 * @author Lmctruck30
 * @author Stuart - Begin refaactoring this mess
 * @version 1.3
 */
public final class PlayerHandler {

	public static Player[] players = new Player[Config.MAX_PLAYERS];
	public static int fightArenaTimer = 180;
	public static int playersInFightArena = 0;
	public static boolean updateAnnounced;
	public static boolean updateRunning;
	public static int updateSeconds;
	public static long updateStartTime;
	public static boolean kickAllPlayers = false;
	private static Queue<Player> destructQueue = new ConcurrentLinkedQueue<>();

	static {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			players[i] = null;
		}
	}

	public static Player getPlayer(long encodedName) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (players[i] != null) {
				if (players[i].encodedName == encodedName) {
					return players[i];
				}
			}
		}
		return null;
	}

	public static void yell(String text) {
		for (Player player : players) {
			if (player != null) {
				player.sendMessage(text);
			}
		}
	}

	public static void staffYell(String text) {
		for (Player player : players) {
			if (player != null && player.rights > 0) {
				player.sendMessage(text);
			}
		}
	}

	public static Player getPlayerByDatabaseId(int id) {
		for (Player player : players) {
			if (player != null && player.getDatabaseId() == id) {
				return player;
			}
		}
		return null;
	}

	public static Player getPlayer(String name) {
		for (Player player : players) {
			if (player != null && player.username.equalsIgnoreCase(name)) {
				return player;
			}
		}
		return null;
	}

	public static int getPlayerCount() {
		int count = 0;
		for (Player player : players) {
			if (player != null) {
				count++;
			}
		}
		return count;
	}
	
	public static int playerCountWithModifier() {
		int count = 0;
		for (Player player : players) {
			if (player != null) {
				count++;
			}
		}
		return (int) (count * 1.25);
	}

	public static boolean newPlayerClient(Player player) {
		int slot = -1;
		for (int i = 1; i < Config.MAX_PLAYERS; i++) {
			if (players[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			return false;
		}
		player.playerId = slot;
		players[slot] = player;
		players[slot].isActive = true;

		if (Config.SERVER_DEBUG) {
			System.out.println("Player Slot " + slot + " slot 0 " + players[0] + " Player Hit " + players[slot]);
		}
		return true;
	}

	public static StopWatch stopWatch = new StopWatch();

	public static void process() {

		stopWatch.reset();

		if (kickAllPlayers) {
			for (int i = 1; i < Config.MAX_PLAYERS; i++) {
				if (players[i] != null) {
					players[i].setDisconnected(true, "kick all players");
				}
			}
		}
		if (fightArenaTimer != 0 && playersInFightArena > 1) {
			fightArenaTimer--;
		}

		stopWatch.start();
		// destruct queue
		Player player;
		while ((player = destructQueue.poll()) != null) {
			player.destruct();
			players[player.playerId] = null;
		}
		stopWatch.stop("x1");

		stopWatch.start();
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (players[i] == null || !players[i].isActive) {
				continue;
			}
			try {
				if (players[i].isDisconnected() && !players[i].timedLogout) {
					// TODO: Make Full System for timed log out 30 seconds no matter what
					Player o = null;
					if (!players[i].timedLogout && System.currentTimeMillis() - players[i].logoutDelay <= 10000
							&& !players[i].forceDisconnect) {
						players[i].timedLogout = true;
						final int index1 = i;
						Server.getTickManager().submit(new Tick(50) {

							@Override
							public void execute() {
								players[index1].timedLogout = false;
								players[index1].forceDisconnect = true;
								this.stop();
							}
						});
						continue;
					} else {
						if (players[i].inTrade) {
							o = players[players[i].tradeWith];
							if (o != null) {
								o.getTradeAndDuel().declineTrade();
							}
						}

						ServerEvents.onPlayerLoggedOut(players[i]);
						GameCycleTaskHandler.stopEvents(players[i]);

						removePlayer(players[i]);

						players[i].isActive = false;
						PlayerSave.saveGame(new SaveRequest(players[i], true));
					}
					continue;
				}
				players[i].processQueuedPackets();
				players[i].processPackets = 0;
				players[i].process();
				players[i].getWalkingQueue().pulse();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		stopWatch.stop("x2");

		stopWatch.start();
		Server.clanManager.sendClanInterface();
		stopWatch.stop("sending clan interface");


		stopWatch.start();
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (players[i] == null || !players[i].isActive) {
				continue;
			}
			try {
				if (!players[i].isDisconnected()) {
					if (!players[i].initialized) {
						players[i].initialize();
						players[i].initialized = true;
					} else {
						players[i].update();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		stopWatch.stop("x3");

		if (updateRunning && !updateAnnounced) {
			updateAnnounced = true;
			Server.UpdateServer = true;
		}
		if (updateRunning && (System.currentTimeMillis() - updateStartTime > (updateSeconds * 1000))) {
			kickAllPlayers = true;
		}
	}

	public static void removePlayer(Player plr) {
		if (plr.privateChat != 2) {
			for (int i = 1; i < Config.MAX_PLAYERS; i++) {
				if (players[i] == null || players[i].isActive == false) {
					continue;
				}
				Player o = PlayerHandler.players[i];
				if (o != null) {
					o.getPA().updatePM(plr.playerId, 0);
				}
			}
		}
	}

	public static void updateNPC(Player plr, Stream str) {
		Stream updateBlock = new Stream(new byte[5000]);
		str.createFrameVarSizeWord(65);
		str.initBitAccess();
		str.writeBits(8, plr.localNpcs.size());

        /*
		 * Iterate through the local npc list.
		 */
		for (Iterator<NPC> it$ = plr.localNpcs.iterator(); it$.hasNext(); ) {
			/*
			 * Get the next NPC.
			 */
			NPC npc = it$.next();

			boolean remove = false;
			if(npc.attributeExists("display_to_owner_only")) {
				if(npc.getOwnerId() != plr.playerId) {
					remove = true;
				}
			}

			/*
			 * If the NPC should still be in our list.
			 */
			if (!remove && plr.withinDistance(npc) && !npc.attributeExists("hidden")) {
				/*
				 * Update the movement.
				 */
				npc.updateNPCMovement(str);

				/*
				 * Check if an update is required, and if so, send the update.
				 */
				if (npc.updateRequired) {
					npc.appendNPCUpdateBlock(updateBlock);
				}
			} else {
				/*
				 * Otherwise, remove the NPC from the list.
				 */
				it$.remove();

				/*
				 * Tell the client to remove the NPC from the list.
				 */
				str.writeBits(1, 1);
				str.writeBits(2, 3);
			}
		}

		for (int i = 0; i < NPCHandler.maxNPCs; ++i) {

			if (NPCHandler.npcs[i] == null) {
				continue;
			}

			NPC npc = NPCHandler.npcs[i];

			int id = npc.id;
			int type = npc.type;

			// if(NPCHandler.npcs[i].changeType) {
			//     continue;
			//  }
			if (type >= 1795 && type <= 1797 && plr.theStolenCannonStatus != 7) {
				continue;
			}
			if (type == 3220 && plr.theStolenCannonStatus != 8) {
				continue;
			}

			// display only to the owner
			if(npc.attributeExists("display_to_owner_only")) {
				if(npc.getOwnerId() != plr.playerId) {
					continue;
				}
			}

			if (plr.withinDistance(npc) && !plr.localNpcs.contains(npc) && !npc.attributeExists("hidden")) {
				plr.localNpcs.add(npc);
				plr.addNewNPC(npc, str, updateBlock);
				if(npc.faceDirection != null && !npc.faceDirection.equals(Direction.NONE)) {
					npc.setAttribute("update_face", true);
				}
			}
		}

		if (updateBlock.currentOffset > 0) {
			str.writeBits(14, 16383);
			str.finishBitAccess();
			str.writeBytes(updateBlock.buffer, updateBlock.currentOffset, 0);
		} else {
			str.finishBitAccess();
		}
		str.endFrameVarSizeWord();
	}

	public static void addPlayerToDestructQueue(Player player) {
		destructQueue.add(player);
	}

	public static List<Player> getLocalPlayers(Player player) {
		List<Player> list = new ArrayList<>();
		for (Player plr : players) {
			if (plr == null) {
				continue;
			}
			if (!plr.initialized) {
				continue;
			}
			if (player == plr) {
				continue;
			}
			if (!player.getLocation().isWithinDistance(plr.getLocation())) {
				continue;
			}
			list.add(plr);
		}
		return list;
	}

	public static boolean isPlayerOnline(Player player) {
		for (Player p : players) {
			if (p == null) {
				continue;
			}
			if (p == player) {
				return true;
			}
		}

		return false;
	}

}