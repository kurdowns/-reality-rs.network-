package com.zionscape.server.model.content.minigames.zombies;

import com.google.common.collect.Lists;
import com.zionscape.server.Server;
import com.zionscape.server.cache.Collision;
import com.zionscape.server.gamecycle.GameCycleTask;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.DynamicMapObject;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.clan.Clan;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.npcs.other.MissCheevers;
import com.zionscape.server.model.objects.GameObject;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.region.RegionUtility;
import com.zionscape.server.util.Misc;
import com.zionscape.server.util.pathfinding.CollisionMap;
import com.zionscape.server.world.ItemDrops;
import com.zionscape.server.world.shops.Shops;

import java.util.*;
import java.util.stream.Collectors;

public class Zombies {

	private static final List<Game> games = Lists.newArrayList();
	private static int lobbyTimer = Constants.LOBBY_TIME_LENGTH;

	static {

		// bridge clipping fix
		for (int x = 4125; x <= 4131; x++) {
			for (int y = 3993; y <= 3994; y++) {
				Collision.removeClipping(x, y, 0);
			}
		}

		// gate
		Collision.flag(4132, 3993, 0, Collision.WEST_BLOCKED);
		Collision.flag(4132, 3994, 0, Collision.WEST_BLOCKED);


		GameCycleTaskHandler.addEvent(null, (GameCycleTaskContainer container) -> {

			// anti cheat
			for (Player player : PlayerHandler.players) {
				if (player != null && inGameArea(player.getLocation()) && !player.attributeExists("zombie_game") && player.getData().loggedOutZombieWave == 0) {
					player.getPA().movePlayer(Constants.LOBBY_SPAWN);
				}
			}

			// anti cheat
			for (Game game : games) {
				List<Player> players = game.getPlayers().stream().filter(x -> !Constants.GAME_AREA.inArea(x.getLocation())).collect(Collectors.toList());
				players.stream().forEach(x -> x.getPA().movePlayer(Constants.GAME_SPAWN.getX(), Constants.GAME_SPAWN.getY(), game.getHeight()));

				// stop no clipping to other side
				if (!game.isEastGateUnlocked()) {
					game.getPlayers().stream().filter(player -> player.getX() >= 4132).forEach(player -> player.getPA().movePlayer(Constants.GAME_SPAWN.getX(), Constants.GAME_SPAWN.getY(), game.getHeight()));
				}

				for (NPC npc : game.getZombies()) {
					if (!Constants.GAME_AREA.inArea(npc.getLocation())) {
						Location location = getRandomSpawnLocation(game);
						npc.absX = location.getX();
						npc.absY = location.getY();
					}
				}

				// perk ticks
				if (game.getPerkTicks() > 0) {
					game.setPerkTicks(game.getPerkTicks() - 1);

					if (game.getPerkTicks() == 0) {
						game.setHeal(false);
						game.setDoublePoints(false);
						game.setInstantKill(false);
					}
				}

				int count = 0;
				for (NPC npc : game.getZombies()) {
					if (npc.smartPathfinding) {
						count++;
					}
				}

				if (count < 3) {
					for (NPC npc : game.getZombies()) {
						if (!npc.smartPathfinding) {
							// npc.smartPathfinding = true;
							count++;
						}
						if (count >= 3) {
							break;
						}
					}
				}
			}

			lobbyTimer--;
			if (lobbyTimer != 0) {
				return;
			}

			// random
			List<Player> randomLobbyPlayers = Arrays.asList(PlayerHandler.players).stream().filter(x ->
					x != null && Constants.RANDOM_LOBBY_AREA.inArea(x.getLocation())
			).collect(Collectors.toList());

			if (randomLobbyPlayers.size() > 0) {
				if (randomLobbyPlayers.size() == 0) {
					randomLobbyPlayers.forEach(x -> x.sendMessage("Not enough players to start the game."));
				} else {
					while (randomLobbyPlayers.size() >= 1) {

						int count = 0;
						List<Player> players = Lists.newArrayList();

						Iterator<Player> itr = randomLobbyPlayers.iterator();
						while (itr.hasNext()) {
							Player player = itr.next();

							players.add(player);
							itr.remove();

							if (++count >= 5) {
								break;
							}
						}

						Game game = createNewGame(Type.RANDOM, players, 1);
						players.stream().forEach(x -> joinGame(x, game));
					}
				}
			}

			// clan
			List<Player> clanLobbyPlayers = Arrays.asList(PlayerHandler.players).stream().filter(x ->
					x != null && Constants.CLAN_LOBBY_AREA.inArea(x.getLocation())
			).collect(Collectors.toList());

			if (clanLobbyPlayers.size() > 0) {
				List<Clan> clans = Lists.newArrayList();
				for (Player player : clanLobbyPlayers) {
					if (!clans.contains(player.clan)) {
						clans.add(player.clan);
					}
				}
				if (clans.size() > 0) {
					for (Clan clan : clans) {
						List<Player> clanMembers = clanLobbyPlayers.stream().filter(x -> x.clan == clan).collect(Collectors.toList());
						if (clanMembers.size() <= 1) {
							clanMembers.forEach(x -> x.sendMessage("Not enough clan members participating to begin a game."));
							continue;
						}
						Game game = createNewGame(Type.RANDOM, clanMembers, 1);
						clanMembers.stream().forEach(x -> joinGame(x, game));
					}
				}
			}

			lobbyTimer = Constants.LOBBY_TIME_LENGTH;
		}, 2);
	}

	public static boolean onObjectClick(Player player, int objectId, Location objectLocation, int option) {

		switch (objectId) {
			case 4389:
				Game game = (Game) player.getAttribute("zombie_game");
				if (game == null) {
					return false;
				}
				leaveGame(player, false);
				return true;
			case 1536:
				game = (Game) player.getAttribute("zombie_game");
				if (game == null) {
					return false;
				}

				// open doors
				if (objectLocation.equals(4103, 4018) || objectLocation.equals(4103, 3979) || objectLocation.equals(4152, 4018)) {
					return true;
				}

				//public GameObject(int id, int x, int y, int height, int face, int type, int newId, int ticks) {

				if (objectLocation.equals(4103, 4019)) {
					game.getGameObjects().add(new GameObject(-1, 4103, 4019, player.getHeightLevel(), 0, 0, 0, -1));
					game.getGameObjects().add(new GameObject(1536, 4103, 4018, player.getHeightLevel(), 0, 0, 0, -1));

					game.getCollisionMap().setClipping(4103, 4018, 0);
					game.getCollisionMap().setClipping(4103, 4019, 0);
					return true;
				}

				if (objectLocation.equals(4103, 3978)) {

					if (option != 7) {
						player.getPA().sendStatement("Would you like to spend 50 points to open this door?");
						player.setDialogueOwner(Zombies.class);
						player.setCurrentDialogueId(7);
						return true;
					}

					player.resetDialogue();

					int points = (int) player.getAttribute("zombie_points");
					if (points < 50) {
						player.sendMessage("You do not have 50 points to unlock this door.");
						return true;
					}

					player.setAttribute("zombie_points", points - 50);

					game.getGameObjects().add(new GameObject(-1, 4103, 3978, player.getHeightLevel(), 0, 0, 0, -1));
					game.getGameObjects().add(new GameObject(1536, 4103, 3979, player.getHeightLevel(), 2, 0, 0, -1));

					game.getCollisionMap().setClipping(4103, 3979, 0);
					game.getCollisionMap().setClipping(4103, 3978, 0);
					return true;
				}

				if (objectLocation.equals(4152, 4019)) {

					if (option != 8) {
						player.getPA().sendStatement("Would you like to spend 100 points to open this door?");
						player.setDialogueOwner(Zombies.class);
						player.setCurrentDialogueId(8);
						return true;
					}

					player.resetDialogue();

					int points = (int) player.getAttribute("zombie_points");
					if (points < 100) {
						player.sendMessage("You do not have 100 points to unlock this door.");
						return true;
					}

					player.setAttribute("zombie_points", points - 100);

					game.getGameObjects().add(new GameObject(-1, 4152, 4019, player.getHeightLevel(), 0, 0, 0, -1));
					game.getGameObjects().add(new GameObject(1536, 4152, 4018, player.getHeightLevel(), 0, 0, 0, -1));

					game.getCollisionMap().setClipping(4152, 4018, 0);
					game.getCollisionMap().setClipping(4152, 4019, 0);
					return true;
				}

				return false;
			case 2883:
			case 2882:
				if (objectLocation.equals(4132, 3994) || objectLocation.equals(4132, 3993)) {
					game = (Game) player.getAttribute("zombie_game");
					if (game == null || game.isEastGateUnlocked()) {
						return true;
					}

					if (option != 9) {
						player.getPA().sendStatement("Would you like to spend 150 points to open this gate?");
						player.setDialogueOwner(Zombies.class);
						player.setCurrentDialogueId(9);
						return true;
					}

					player.resetDialogue();

					int points = (int) player.getAttribute("zombie_points");
					if (points < 150) {
						player.sendMessage("You do not have 150 points to unlock this gate.");
						return true;
					}

					player.setAttribute("zombie_points", points - 150);

					//public GameObject(int id, int x, int y, int height, int face, int type, int newId, int ticks) {
					game.getGameObjects().add(new GameObject(2882, 4132, 3994, player.getHeightLevel(), 1, 0, 0, -1));
					game.getGameObjects().add(new GameObject(2883, 4132, 3993, player.getHeightLevel(), 3, 0, 0, -1));

					game.getCollisionMap().setClipping(4131, 3994, 0);
					game.getCollisionMap().setClipping(4131, 3993, 0);
					game.getCollisionMap().setClipping(4132, 3994, 0);
					game.getCollisionMap().setClipping(4132, 3993, 0);


					game.setEastGateUnlocked(true);
					return true;
				}
				return false;
			case 4470:

				if (player.getAbsY() == 4005) { // exiting
					player.getPA().walkTo(0, -1);
					return true;
				}

				// single
				if (objectLocation.equals(4191, 4004, 0)) {
					if (player.getAbsX() != 4191) {
						return true;
					}

					if (player.getAbsY() == 4004) {
						player.getPA().sendStatement("Would you like to enter a solo zombie game?");
						player.setDialogueOwner(Zombies.class);
						player.setCurrentDialogueId(4);
						return true;
					}

					//clickingBarrierLobby(player, Type.SOLO);
					return true;
				}

				// random
				if (objectLocation.equals(4198, 4004, 0)) {
					if (player.getAbsX() != 4198) {
						return true;
					}

					if (player.getAbsY() == 4004) {
						player.getPA().sendStatement("Would you like to enter a random team zombie game?");
						player.setDialogueOwner(Zombies.class);
						player.setCurrentDialogueId(5);
						return true;
					}

					// clickingBarrierLobby(player, Type.RANDOM);
					return true;
				}

				// clans
				if (objectLocation.equals(4205, 4004, 0)) {
					if (player.getAbsX() != 4205) {
						return true;
					}

					if (player.getAbsY() == 4004) {
						player.getPA().sendStatement("Would you like to enter a clan zombie game?");
						player.setDialogueOwner(Zombies.class);
						player.setCurrentDialogueId(6);
						return true;
					}

					// clickingBarrierLobby(player, Type.CLAN);
					return true;
				}
				return false;
			case 13291:
				if (objectLocation.equals(4153, 4022)) {

					game = (Game) player.getAttribute("zombie_game");
					if (game == null) {
						return true;
					}

					if (option != 10) {
						player.getPA().sendStatement("Would you like to spend 45 points to open this chest?");
						player.setDialogueOwner(Zombies.class);
						player.setCurrentDialogueId(10);
						return true;
					}

					player.resetDialogue();

					int points = (int) player.getAttribute("zombie_points");
					if (points < 45) {
						player.sendMessage("You do not have 45 points to unlock this gate.");
						return true;
					}

					player.setAttribute("zombie_points", points - 45);

					int item = Constants.CHEST_ITEM_IDS[Misc.random(Constants.CHEST_ITEM_IDS.length - 1)];
					player.getItems().addItem(item, 1);
					player.sendMessage("You have received a " + ItemUtility.getName(item) + ".");
					return true;
				}
				return false;
		}
		return false;
	}

	public static void onPlayerLoggedOut(Player player) {
		Game game = (Game) player.getAttribute("zombie_game");
		if (game == null) {
			return;
		}

		player.getData().loggedOutZombiePoints = (int) player.getAttribute("zombie_points");
		player.getData().loggedOutZombieWave = game.getWave();

		for (GameObject object : game.getGameObjects()) {
			// first door
			if (object.objectId == -1 && object.objectX == 4103 && object.objectY == 3978) {
				player.getData().loggedOutZombieDoors.add(Location.create(4103, 3978));
			}

			// second door
			if (object.objectId == -1 && object.objectX == 4152 && object.objectY == 4019) {
				player.getData().loggedOutZombieDoors.add(Location.create(4152, 4019));
			}

			// gates
			if (object.objectId == 2882 && object.objectX == 4132 && object.objectY == 3994) {
				player.getData().loggedOutZombieDoors.add(Location.create(4132, 3994));
			}
		}


		// player.absX = Constants.LOBBY_SPAWN.getX();
		// player.absY = Constants.LOBBY_SPAWN.getY();
		// player.heightLevel = 0;
		leaveGame(player, true);
	}

	public static void onPlayerLoggedIn(Player player) {
		if (inGameArea(player.getLocation()) && player.getData().loggedOutZombieWave > 0) {

			Game game = createNewGame(Type.SOLO, Arrays.asList(player), player.getData().loggedOutZombieWave);

			player.setAttribute("zombie_game", game);
			player.setAttribute("zombie_points", player.getData().loggedOutZombiePoints);

			// first door
			if (player.getData().loggedOutZombieDoors.contains(Location.create(4103, 3978))) {
				game.getGameObjects().add(new GameObject(-1, 4103, 3978, player.getHeightLevel(), 0, 0, 0, -1));
				game.getGameObjects().add(new GameObject(1536, 4103, 3979, player.getHeightLevel(), 2, 0, 0, -1));

				game.getCollisionMap().setClipping(4103, 3979, 0);
				game.getCollisionMap().setClipping(4103, 3978, 0);
			}
			// second door
			if (player.getData().loggedOutZombieDoors.contains(Location.create(4152, 4019))) {
				game.getGameObjects().add(new GameObject(-1, 4152, 4019, player.getHeightLevel(), 0, 0, 0, -1));
				game.getGameObjects().add(new GameObject(1536, 4152, 4018, player.getHeightLevel(), 0, 0, 0, -1));
				game.getCollisionMap().setClipping(4152, 4018, 0);
				game.getCollisionMap().setClipping(4152, 4019, 0);
			}

			// gate
			if (player.getData().loggedOutZombieDoors.contains(Location.create(4132, 3994))) {
				game.getGameObjects().add(new GameObject(2882, 4132, 3994, player.getHeightLevel(), 1, 0, 0, -1));
				game.getGameObjects().add(new GameObject(2883, 4132, 3993, player.getHeightLevel(), 3, 0, 0, -1));

				game.getCollisionMap().setClipping(4131, 3994, 0);
				game.getCollisionMap().setClipping(4131, 3993, 0);
				game.getCollisionMap().setClipping(4132, 3994, 0);
				game.getCollisionMap().setClipping(4132, 3993, 0);


				game.setEastGateUnlocked(true);
			}


			player.getPA().movePlayer(player.getX(), player.getY(), game.getHeight());

			player.getData().loggedOutZombiePoints = 0;
			player.getData().loggedOutZombieWave = 0;
			player.getData().loggedOutZombieDoors.clear();

			player.sendMessage("Your zombie wave will resume immediately.");
			//player.getItems().deleteAllItems();
			//player.getPA().movePlayer(Constants.LOBBY_SPAWN);
		}
	}

	public static void onPlayerDied(Player player) {
		Game game = (Game) player.getAttribute("zombie_game");
		if (game == null) {
			return;
		}
		player.sendMessage("Oh dear you died.");
		leaveGame(player, false);
	}

	private static void clickingBarrierLobby(Player player, Type type) {

		player.getPA().closeAllWindows();

		if (player.getFamiliar() != null) {
			player.sendMessage("You may not take familiars into this minigame.");
			return;
		}

		if (checkPlayerForItems(player)) {
			player.sendMessage("You cannot take any items into this minigame.");
			return;
		}

		if (player.isBanking || player.inTrade || player.getDuel() != null) {
			player.sendMessage("Please relog and try again.");
			return;
		}

		if (type.equals(Type.SOLO)) {
			joinGame(player, createNewGame(Type.SOLO, Collections.singletonList(player), 1));
			return;
		}

		if (type.equals(Type.CLAN) && player.clan == null) {
			player.sendMessage("You're not in a clan.");
			return;
		}

		player.setAttribute("block_picking_up", true);
		GameCycleTaskHandler.addEvent(player, container -> { player.removeAttribute("block_picking_up"); }, 4);

		if (player.getAbsY() == 4004) { // entering
			player.getPA().walkTo(0, 1);
		} else if (player.getAbsY() == 4005) { // exiting
			player.getPA().walkTo(0, -1);
		}
	}

	public static void joinGame(Player player, Game game) {
		player.setAttribute("zombie_game", game);
		player.setAttribute("zombie_points", 50);

		player.getPA().movePlayer(Constants.GAME_SPAWN.getX(), Constants.GAME_SPAWN.getY(), game.getHeight());
	}

	public static void leaveGame(Player player, boolean logout) {
		Game game = (Game) player.getAttribute("zombie_game");
		if (game == null) {
			return;
		}

		if (game.getSpawnTask().isPresent()) {
			GameCycleTask task = game.getSpawnTask().get();

			GameCycleTaskHandler.stopEvents(task);
		}

		if (!logout) {
			if (game.getWave() >= 10) {
				Achievements.progressMade(player, Achievements.Types.WAVE_10_ZOMBIES);
			}
			if (game.getWave() >= 15) {
				Achievements.progressMade(player, Achievements.Types.WAVE_15_ZOMBIES);
			}
			if (game.getWave() >= 25) {
				Achievements.progressMade(player, Achievements.Types.WAVE_25_ZOMBIES);
			}
			if (game.getWave() >= 50) {
				Achievements.progressMade(player, Achievements.Types.WAVE_50_IN_ZOMBIES);
			}
			if (game.getType().equals(Type.SOLO) && game.getWave() >= 25) {
				Achievements.progressMade(player, Achievements.Types.WAVE_25_ZOMBIES_SOLO);
			}

			if (game.getWave() > player.getData().highestZombieWave) {
				player.getData().highestZombieWave = game.getWave();
			}

			if (game.getWave() >= 4) {
				int points = 0;
				for (int i = 4; i <= game.getWave(); i++) {
					points += (int) Math.floor(game.getWave() / 2);
				}

				if (MissCheevers.isPerkActive(player, MissCheevers.ZOMBIE)) {
					points *= 2;
				}

				player.getData().zombiePoints += points;
				player.sendMessage("You have been awarded " + points + " reward points.");
			} else {
				player.sendMessage("You need to reach wave 4 to begin receiving reward points.");
			}
		}

		if (game.getPlayers().size() <= 1) {
			for (NPC npc : game.getZombies()) {
				npc.isDead = true;
				npc.needRespawn = true;
				npc.actionTimer = 0;
				npc.setRespawn(false);
				
			}
			for (NPC npc : game.getShopNpcs()) {
				npc.isDead = true;
				npc.needRespawn = true;
				npc.actionTimer = 0;
				npc.setRespawn(false);
			}
			for (GameObject gameObject : game.getGameObjects()) {
				Server.objectManager.removeObject(Location.create(gameObject.objectX, gameObject.objectY, gameObject.height), gameObject.type);
			}

			games.remove(game);
		} else {
			game.getPlayers().removeIf(x -> x.equals(player));
		}

		if (!logout) {
			player.getItems().deleteAllItems();
			player.removeAttribute("zombie_game");
			player.getPA().movePlayer(Constants.LOBBY_SPAWN);
		}
	}

	private static Game createNewGame(Type type, List<Player> players, int wave) {

		int height = 0;

		for (Game game : games) {
			if (game.getHeight() > height) {
				height = game.getActualHeight();
			}
		}

		Game game = new Game(type, players, (height + 1));
		game.setWave(wave);

		game.getShopNpcs().add(NPCHandler.spawnNpc(400, 4100, 4025, game.getHeight(), 0, 0, 0, 0, 0)); // north west
		game.getShopNpcs().add(NPCHandler.spawnNpc(5965, 4100, 3973, game.getHeight(), 0, 0, 0, 0, 0)); // south west
		game.getShopNpcs().add(NPCHandler.spawnNpc(1202, 4155, 4025, game.getHeight(), 0, 0, 0, 0, 0)); // north east

		game.getGameObjects().add(new GameObject(-1, 4103, 4019, game.getHeight(), 0, 0, 0, -1));
		game.getGameObjects().add(new GameObject(1536, 4103, 4018, game.getHeight(), 0, 0, 0, -1));
		game.getCollisionMap().setClipping(4103, 4018, 0);
		game.getCollisionMap().setClipping(4103, 4019, 0);

		//public GameObject(int id, int x, int y, int height, int face, int type, int newId, int ticks) {
		game.getGameObjects().add(new GameObject(13291, 4153, 4022, game.getHeight(), 0, 10, 0, -1));
		RegionUtility.addEntity(new DynamicMapObject(13291, Location.create(4153, 4022, 0), 0, 1), Location.create(4153, 4022, 0));

		// exit portal
		game.getGameObjects().add(new GameObject(4389, 4104, 4025, game.getHeight(), 0, 10, 0, -1));
		RegionUtility.addEntity(new DynamicMapObject(4389, Location.create(4104, 4025, 0), 10, 1), Location.create(4104, 4025, 0));

		games.add(game);

		startWave(game, true);

		return game;
	}

	private static boolean checkPlayerForItems(Player player) {
		for (int item : player.equipment) {
			if (item > 0) {
				return true;
			}
		}
		for (int item : player.inventory) {
			if (item > 0) {
				return true;
			}
		}
		return false;
	}

	public static void onNpcKilled(Player player, NPC npc) {

		Game game = (Game) player.getAttribute("zombie_game");
		if (game == null) {
			return;
		}
		if (!game.getZombies().contains(npc)) {
			return;
		}

		game.getZombies().remove(npc);

		boolean doublePoints = game.doublePoints();
		player.setAttribute("zombie_points", (int) player.getAttribute("zombie_points") + (doublePoints ? 10 : 5));

		if (Misc.random(8) == 0 && !game.doublePoints() && !game.instantKill() && !game.heal()) {
			int random = Misc.random(3);
			switch (random) {
				case 0:
					game.setInstantKill(true);
					game.setPerkTicks(30);
					break;
				case 1:
					game.setDoublePoints(true);
					game.setPerkTicks(30);
					break;
				case 2:
					game.setHeal(true);
					game.setPerkTicks(4);
					for (Player plr : game.getPlayers()) {
						plr.level[PlayerConstants.HITPOINTS] = plr.getActualLevel(PlayerConstants.HITPOINTS);
						plr.getPA().refreshSkill(PlayerConstants.HITPOINTS);

                       /* plr.level[PlayerConstants.PRAYER] = plr.getActualLevel(PlayerConstants.PRAYER);
						plr.getPA().refreshSkill(PlayerConstants.PRAYER);*/
					}
					break;
			}
		}

		// drop food
		if (Misc.random(5) == 0) {

			int foodId = 361;
			if (game.getWave() >= 5 && game.getWave() < 10) {
				foodId = 379;
			}
			if (game.getWave() >= 10 && game.getWave() < 15) {
				foodId = 373;
			}
			if (game.getWave() >= 15 && game.getWave() < 20) {
				foodId = 7946;
			}
			if (game.getWave() >= 20 && game.getWave() < 25) {
				foodId = 385;
			}
			if (game.getWave() >= 25 && game.getWave() < 30) {
				foodId = 391;
			}
			if (game.getWave() >= 30) {
				foodId = 15272;
			}

			ItemDrops.createGroundItem(player, foodId, npc.absX, npc.absY, 1, player.playerId, false);
		}

		if (game.getZombies().size() == 0) {
			game.setWave(game.getWave() + 1);

			startWave(game, false);
		}
	}

	private static Location getRandomSpawnLocation(Game game) {
		List<Location> locations = new ArrayList<>();
		locations.addAll(Arrays.asList(Constants.WEST_ZOMBIE_SPAWN_POINTS));
		if (game.isEastGateUnlocked()) {
			locations.addAll(Arrays.asList(Constants.EAST_ZOMBIE_SPAWN_POINTS));
		}

		Location location = locations.get((int) (Math.random() * locations.size()));

		return Location.create(location.getX(), location.getY(), game.getHeight());
	}

	public static boolean inLobbyArea(Location location) {

		if (Constants.CLAN_LOBBY_AREA.inArea(location) || Constants.RANDOM_LOBBY_AREA.inArea(location)) {
			return true;
		}

		return false;
	}

	public static boolean inGameArea(Location location) {
		return Constants.GAME_AREA.inArea(location);
	}

	public static Optional<CollisionMap> getCollisionMap(int height) {
		Optional<Game> game = games.stream().filter(x -> x.getHeight() == height).findFirst();

		return game.isPresent() ? Optional.of(game.get().getCollisionMap()) : Optional.empty();
	}

	public static void sendInterface(Player player) {

		if (inLobbyArea(player.getLocation())) {
			player.getPA().walkableInterface(54370);
			player.getPA().sendFrame126("Zombie game begins in: " + lobbyTimer + " seconds", 54371);
			return;
		}


		Game game = (Game) player.getAttribute("zombie_game");
		if (game == null) {
			return;
		}
		player.getPA().walkableInterface(54365);
		player.getPA().sendFrame126("Wave: " + game.getWave(), 54366);
		player.getPA().sendFrame126("Points: " + player.getAttribute("zombie_points"), 54367);

		if (game.doublePoints()) {
			player.getPA().sendFrame126("DOUBLE POINTS", 54368);
			player.getPA().sendFrame126("0:" + game.getPerkTicks(), 54369);
		} else if (game.instantKill()) {
			player.getPA().sendFrame126("INSTANT KILL", 54368);
			player.getPA().sendFrame126("0:" + game.getPerkTicks(), 54369);
		} else if (game.heal()) {
			player.getPA().sendFrame126("RESTORED", 54368);
			player.getPA().sendFrame126("(Your health has been fully restored)", 54369);
		} else {
			player.getPA().sendFrame126("", 54368);
			player.getPA().sendFrame126("", 54369);
		}
	}

	public static void startWave(Game game, boolean start) {
		GameCycleTask task = (GameCycleTaskContainer container) -> {
			int amount = 3;
			if (game.getWave() >= 3) {
				amount += game.getPlayers().size() * Math.round(game.getWave() / 3);
			}

			for (int i = 0; i < amount; i++) {
				Location location = getRandomSpawnLocation(game);

				NPC spawnedZombie = NPCHandler.spawnNpc(14205, location.getX(), location.getY(), location.getZ(), 0,
						10 + Math.round(game.getWave() * 3), // hp
						2 + game.getWave(),  // maxhit
						10 + Math.round(game.getWave() * 3), // attack
						10 + Math.round(game.getWave() * 2) // defence
				);

				if (spawnedZombie == null) {
					continue;
				}

				spawnedZombie.setRespawn(false);
				game.getZombies().add(spawnedZombie);
				game.setSpawnTask(Optional.empty());
				container.stop();
			}
		};

		game.setSpawnTask(Optional.of(task));
		GameCycleTaskHandler.addEvent(null, task, start ? 50 : 25);

		if (start) {
			game.getPlayers().stream().forEach(x -> x.sendMessage("The first wave will begin in 30 seconds."));
		} else {
			game.getPlayers().stream().forEach(x -> x.sendMessage("Wave " + game.getWave() + " will begin in 15 seconds."));
		}
	}

	public static boolean onNpcClick(Player player, NPC npc, int option) {

		if (npc.type == 5623) {
			player.sendMessage("You have " + player.getData().zombiePoints + " Zombie points.");
			Shops.open(player, 69);
			return true;
		}

		// lobby npc
		Game game = (Game) player.getAttribute("zombie_game");
		if (game == null) {
			return false;
		}

		// shops
		if (npc.type == 400) {
			player.getPA().sendOptions("Beginner Melee", "Beginner Range", "Beginner Mage");
			player.setDialogueOwner(Zombies.class);
			player.setCurrentDialogueId(1);
			return true;
		}
		if (npc.type == 5965) {
			player.getPA().sendOptions("Intermediate Melee", "Intermediate Range", "Intermediate Mage");
			player.setDialogueOwner(Zombies.class);
			player.setCurrentDialogueId(2);
			return true;
		}
		if (npc.type == 1202) {
			player.getPA().sendOptions("Advanced Melee", "Advanced Range", "Advanced Mage");
			player.setDialogueOwner(Zombies.class);
			player.setCurrentDialogueId(3);
			return true;
		}

		return false;
	}

	public static boolean onDialogueContinue(Player player) {
		if (player.getDialogueOwner() == null || !player.getDialogueOwner().equals(Zombies.class)) {
			return false;
		}
		player.getPA().sendOptions("Yes", "No");
		return true;
	}

	public static boolean onDialogueOption(Player player, int option) {

		if (player.getDialogueOwner() == null || !player.getDialogueOwner().equals(Zombies.class)) {
			return false;
		}

		switch (player.getCurrentDialogueId()) {
			case 1: // beginner
				switch (option) {
					case 1:
						Shops.open(player, 130);
						break;
					case 2:
						Shops.open(player, 131);
						break;
					case 3:
						Shops.open(player, 132);
						break;
				}
				break;
			case 2: // intermediate
				switch (option) {
					case 1:
						Shops.open(player, 133);
						break;
					case 2:
						Shops.open(player, 134);
						break;
					case 3:
						Shops.open(player, 135);
						break;
				}
				break;
			case 3: // advanced
				switch (option) {
					case 1:
						Shops.open(player, 136);
						break;
					case 2:
						Shops.open(player, 137);
						break;
					case 3:
						Shops.open(player, 138);
						break;
				}
				break;
			case 4:
				if (option == 1) {
					clickingBarrierLobby(player, Type.SOLO);
				} else {
					player.resetDialogue();
				}
				break;
			case 5:
				if (option == 1) {
					clickingBarrierLobby(player, Type.RANDOM);
				} else {
					player.resetDialogue();
				}
				break;
			case 6:
				if (option == 1) {
					clickingBarrierLobby(player, Type.CLAN);
				} else {
					player.resetDialogue();
				}
				break;
			case 7:
				if (option == 1) {
					onObjectClick(player, 1536, Location.create(4103, 3978), 7);
				} else {
					player.resetDialogue();
				}
				break;
			case 8:
				if (option == 1) {
					onObjectClick(player, 1536, Location.create(4152, 4019), 8);
				} else {
					player.resetDialogue();
				}
				break;
			case 9:
				if (option == 1) {
					onObjectClick(player, 2883, Location.create(4132, 3994), 9);
				} else {
					player.resetDialogue();
				}
				break;
			case 10:
				if (option == 1) {
					onObjectClick(player, 13291, Location.create(4153, 4022), 10);
				} else {
					player.resetDialogue();
				}
				break;
		}

		return true;
	}


	public static int getItemPrice(int item) {
		switch (item) {

			// beginner melee
			case 1115:
				return 4;
			case 1153:
				return 3;
			case 1067:
				return 4;
			case 1191:
				return 4;
			case 1127:
				return 7;
			case 1163:
				return 5;
			case 1079:
				return 7;
			case 1201:
				return 7;
			case 1309:
				return 3;
			case 1323:
				return 3;
			case 1293:
				return 3;
			case 1363:
				return 3;
			case 1319:
				return 7;
			case 1333:
				return 5;
			case 1303:
				return 5;
			case 1373:
				return 7;
			case 1007:
				return 1;
			case 1019:
				return 1;
			case 1021:
				return 1;
			case 1023:
				return 1;
			case 4131:
				return 5;
			case 1725:
				return 3;
			case 7458:
				return 2;

			// beginner range
			case 841:
				return 3;
			case 839:
				return 3;
			case 882:
				return 1;
			case 886:
				return 1;
			case 1099:
				return 5;
			case 1135:
				return 5;
			case 1169:
				return 3;
			case 1065:
				return 2;
			case 6131:
				return 4;
			case 6133:
				return 5;
			case 6135:
				return 5;
			case 6143:
				return 3;
			case 6149:
				return 2;

			// beginner mage
			case 1381:
				return 1;
			case 1385:
				return 1;
			case 1383:
				return 1;
			case 6111:
				return 2;
			case 6109:
				return 2;
			case 6107:
				return 5;
			case 6108:
				return 5;
			case 6106:
				return 1;
			case 6110:
				return 1;
			case 4091:
				return 5;
			case 4089:
				return 3;
			case 4093:
				return 5;
			case 4097:
				return 2;
			case 4095:
				return 2;
			case 554:
				return 2;
			case 558:
				return 1;
			case 556:
				return 1;
			case 562:
				return 1;
			case 555:
				return 2;
			case 557:
				return 1;
			case 559:
				return 1;

			// medium melee
			case 4585:
				return 20;
			case 1149:
				return 5;
			case 1187:
				return 25;
			case 13481:
				return 25;
			case 13488:
				return 25;
			case 10551:
				return 30;
			case 1305:
				return 20;
			case 4587:
				return 25;
			case 6568:
				return 15;
			case 7460:
				return 5;
			case 13445:
				return 10;
			case 10828:
				return 10;
			case 6524:
				return 7;
			case 8850:
				return 10;
			case 1712:
				return 5;
			case 3751:
				return 5;
			case 1231:
				return 5;
			case 7158:
				return 20;

			// medium range
			case 861:
				return 15;
			case 9144:
				return 1;
			case 892:
				return 1;
			case 2503:
				return 15;
			case 2497:
				return 15;
			case 2491:
				return 4;
			case 12957:
				return 4;
			case 3749:
				return 3;
			case 1478:
				return 3;
			case 3105:
				return 15;
			case 859:
				return 15;
			case 9185:
				return 15;

			// medium mage
			case 6914:
				return 15;
			case 6922:
				return 3;
			case 6920:
				return 3;
			case 6916:
				return 10;
			case 6924:
				return 10;
			case 6918:
				return 10;
			case 2413:
				return 3;
			case 2414:
				return 3;
			case 2412:
				return 3;
			case 6889:
				return 10;
			case 1727:
				return 3;
			case 3755:
				return 5;
			case 560:
				return 5;
			case 4675:
				return 20;

			// advanced melee

			case 13462:
				return 30;
			case 13442:
				return 30;
			case 13429:
				return 25;

			case 13458:
				return 70;
			case 13460:
				return 70;
			case 13459:
				return 25;
			case 13444:
				return 45;


			// advanced range
			case 4732:
				return 40;
			case 4736:
				return 50;
			case 4738:
				return 50;
			case 11718:
				return 55;
			case 13455:
				return 70;
			case 13456:
				return 70;
			case 13457:
				return 20;
			case 10499:
				return 50;
			case 13427:
				return 40;
			case 11212:
				return 5;
			case 9341:
				return 5;
			case 4740:
				return 5;

			// advanced mage
			case 21777:
				return 50;
			case 15486:
				return 50;
			case 4708:
				return 45;
			case 4712:
				return 50;
			case 4714:
				return 50;
			case 565:
				return 5;
			case 566:
				return 5;
			case 21773:
				return 5;
		}
		return 0;
	}

}