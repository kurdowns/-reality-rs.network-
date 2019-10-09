package com.zionscape.server.model.content.minigames.castlewars;

import com.zionscape.server.Server;
import com.zionscape.server.cache.Collision;
import com.zionscape.server.cache.clientref.ItemDef;
import com.zionscape.server.gamecycle.GameCycleTask;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.DynamicMapObject;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.MapObject;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.npcs.other.MissCheevers;
import com.zionscape.server.model.objects.GameObject;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerAssistant;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.combat.Animations;
import com.zionscape.server.model.players.skills.mining.PickAxe;
import com.zionscape.server.model.region.RegionUtility;
import com.zionscape.server.util.Misc;
import com.zionscape.server.world.doors.DoorHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CastleWars {

	private static final List<Rope> ropes = new ArrayList<>();
	private static final boolean[][] barricadeLocations = new boolean[10000][10000];
	private static final List<Player> zamorakLobbyPlayers = new ArrayList<>();
	private static final List<Player> saradominLobbyPlayers = new ArrayList<>();
	private static Team zamorakTeam;
	private static Team saradominTeam;
	private static int lobbyTimer = Constants.LOBBY_LENGTH;
	private static int gameTimer;
	private static boolean waitingForPlayers = true;
	private static boolean gameRunning = false;
	private static boolean gameEnding = false;

	static {
		GameCycleTaskHandler.addEvent(null, (GameCycleTaskContainer container) -> {

			if (!waitingForPlayers && lobbyTimer-- <= 1) {
				CastleWars.startGame();
				return;
			}
			if (gameRunning) {

				if (gameTimer-- <= 0) {
					CastleWars.endGame();
					return;
				}

				// anti cheat checks to see if they have gone out the castlewars area
				saradominTeam.getPlayers().stream().filter(x -> !CastleWars.inCastleWarsGame(x)).forEach(x -> CastleWars.removeFromGame(x, false));
				zamorakTeam.getPlayers().stream().filter(x -> !CastleWars.inCastleWarsGame(x)).forEach(x -> CastleWars.removeFromGame(x, false));

				// destroy burning catapults
				if (saradominTeam.getCatapult().isOnFire()) {
					saradominTeam.getCatapult().setBurningTicks(saradominTeam.getCatapult().getBurningTicks() - 1);
					if (saradominTeam.getCatapult().getBurningTicks() == 0) {
						CastleWars.placeObject(4386, 2413, 3088, 0, 1, 10);
						saradominTeam.setCatapultOperational(false);
						saradominTeam.getCatapult().setOnFire(false);
					}
				}
				if (zamorakTeam.getCatapult().isOnFire()) {
					zamorakTeam.getCatapult().setBurningTicks(zamorakTeam.getCatapult().getBurningTicks() - 1);
					if (zamorakTeam.getCatapult().getBurningTicks() == 0) {
						CastleWars.placeObject(4385, 2384, 3117, 0, 3, 10);
						zamorakTeam.setCatapultOperational(false);
						zamorakTeam.getCatapult().setOnFire(false);
					}
				}

				// destroy burning barricades
				List<Barricade> burningBarricades = new ArrayList<>();
				burningBarricades.addAll(zamorakTeam.getBarricades().stream().filter(Barricade::isBurning)
						.collect(Collectors.toList()));
				burningBarricades.addAll(saradominTeam.getBarricades().stream().filter(Barricade::isBurning)
						.collect(Collectors.toList()));

				Iterator<Barricade> barricadeIterator = burningBarricades.iterator();
				while (barricadeIterator.hasNext()) {
					Barricade barricade = barricadeIterator.next();

					barricade.setBurningTicks(barricade.getBurningTicks() - 1);

					if (barricade.getBurningTicks() == 0) {
						barricade.destroy();

						Team team = barricade.getTeam();
						team.getBarricades().remove(barricade);
					}
				}

				// destroy ropes
				Iterator<Rope> ropeIterator = ropes.iterator();
				while (ropeIterator.hasNext()) {
					Rope rope = ropeIterator.next();

					if (rope.decrementTicksLeft() <= 0) {
						rope.destroy();
						ropeIterator.remove();
					}
				}

				// catapult cooldown
				if (zamorakTeam.getCatapult().getFireCooldown() > 0) {
					zamorakTeam.getCatapult().setFireCooldown(zamorakTeam.getCatapult().getFireCooldown() - 1);
				}
				if (saradominTeam.getCatapult().getFireCooldown() > 0) {
					saradominTeam.getCatapult().setFireCooldown(saradominTeam.getCatapult().getFireCooldown() - 1);
				}

				// return flag back to stand if the taker is from the same team and in their own castle
				if (saradominTeam.getFlagStatus().equals(FlagStatus.TAKEN)
						&& CastleWars.inSaradominCastle(saradominTeam.getFlagTaker().getLocation())) {
					if (saradominTeam.getFlagTaker().getCastleWarsState().equals(State.SARADOMIN_GAME)) {
						Player player = saradominTeam.getFlagTaker();

						player.getItems().setEquipment(-1, 0, PlayerConstants.WEAPON);
						player.sendMessage("Your team flag has been automatically returned back to its stand.");

						CastleWars.returnFlagToStand(saradominTeam);
					}
				}
				if (zamorakTeam.getFlagStatus().equals(FlagStatus.TAKEN)
						&& CastleWars.inZamorakCastle(zamorakTeam.getFlagTaker().getLocation())) {
					if (zamorakTeam.getFlagTaker().getCastleWarsState().equals(State.ZAMORAK_GAME)) {
						Player player = zamorakTeam.getFlagTaker();

						player.getItems().setEquipment(-1, 0, PlayerConstants.WEAPON);
						player.sendMessage("Your team flag has been automatically returned back to its stand.");

						CastleWars.returnFlagToStand(zamorakTeam);
					}
				}

			}
		}, 2);
	}

	private static Barricade barricadeAtLocation(Location location) {

		Barricade barricade = zamorakTeam.getBarricades().stream()
				.filter(x -> x.getNpc().getLocation().equals(location)).findFirst().orElse(null);

		if (barricade != null) {
			return barricade;
		}

		return saradominTeam.getBarricades().stream().filter(x -> x.getNpc().getLocation().equals(location))
				.findFirst().orElse(null);
	}

	private static void endGame() {
		gameEnding = true;

		Player mostKills = null;
		Player mostCaptures = null;

		// remove the players from the game
		for (Player player : zamorakTeam.getPlayers()) {
			if (zamorakTeam.getScore() > saradominTeam.getScore()) {

				int amount = 2;
				if (MissCheevers.isPerkActive(player, MissCheevers.CWARS)) {
					amount *= 2;
				}

				player.getItems().addItem(4067, amount);
				player.sendMessage("Congratulations on beating the Saradomin team.");

				Achievements.progressMade(player, Achievements.Types.WIN_75_CASTLEWARS_GAMES);
				Achievements.progressMade(player, Achievements.Types.WIN_50_CASTLEWARS_GAMES);
			} else if (zamorakTeam.getScore() == saradominTeam.getScore()) {

				int amount = 1;
				if (MissCheevers.isPerkActive(player, MissCheevers.CWARS)) {
					amount *= 2;
				}

				player.getItems().addItem(4067, amount);
				player.sendMessage("The game has resulted in a draw! Take this for your effort!");
			} else {
				player.sendMessage("Oh dear. Looks like you was beaten by the Saradomin team.");
			}

			if (player.getCastleWarsKills() > 0) {
				if (mostKills == null || player.getCastleWarsKills() > mostKills.getCastleWarsKills()) {
					mostKills = player;
				}
			}
			if (player.getCastleWarsCaptures() > 0) {
				if (mostCaptures == null || player.getCastleWarsCaptures() > mostCaptures.getCastleWarsCaptures()) {
					mostCaptures = player;
				}
			}

			player.getData().setCastleWarsGamesPlayed(player.getData().getCastleWarsGamesPlayed() + 1);
			CastleWars.removeFromGame(player, false);
		}

		for (Player player : saradominTeam.getPlayers()) {
			if (saradominTeam.getScore() > zamorakTeam.getScore()) {

				int amount = 2;
				if (MissCheevers.isPerkActive(player, MissCheevers.CWARS)) {
					amount *= 2;
				}

				player.getItems().addItem(4067, amount);
				player.sendMessage("Congratulations on beating the Zamorak team.");

				Achievements.progressMade(player, Achievements.Types.WIN_75_CASTLEWARS_GAMES);
				Achievements.progressMade(player, Achievements.Types.WIN_50_CASTLEWARS_GAMES);
			} else if (zamorakTeam.getScore() == saradominTeam.getScore()) {
				int amount = 1;
				if (MissCheevers.isPerkActive(player, MissCheevers.CWARS)) {
					amount *= 2;
				}

				player.getItems().addItem(4067, amount);
				player.sendMessage("The game has resulted in a draw! Take this for your effort!");
			} else {
				player.sendMessage("Oh dear. Looks like you was beaten by the Zamorak team.");
			}

			if (player.getCastleWarsKills() > 0) {
				if (mostKills == null || player.getCastleWarsKills() > mostKills.getCastleWarsKills()) {
					mostKills = player;
				}
			}
			if (player.getCastleWarsCaptures() > 0) {
				if (mostCaptures == null || player.getCastleWarsCaptures() > mostCaptures.getCastleWarsCaptures()) {
					mostCaptures = player;
				}
			}

			player.getData().setCastleWarsGamesPlayed(player.getData().getCastleWarsGamesPlayed() + 1);
			CastleWars.removeFromGame(player, false);
		}

		if (mostKills != null) {
			mostKills.getData().setHadMostCastleWarsKills(true);
		}
		if (mostCaptures != null) {
			mostCaptures.getData().setHadMostCastleWarsCaptures(true);
		}

		// stop any events running
		GameCycleTaskHandler.stopEvents(CastleWars.class);

		// reset objects
		DoorHandler.handleDoor(null, 4468, Location.create(2384, 3134, 0)); // zammy sidedoor
		DoorHandler.handleDoor(null, 4466, Location.create(2414, 3073, 0)); // sara sidedoor

		DoorHandler.handleDoor(null, 4424, Location.create(2427, 3089, 0)); // sara maindoor
		DoorHandler.handleDoor(null, 4428, Location.create(2372, 3118, 0)); // zammy maindoor

		CastleWars.placeObject(4437, 2409, 9503, 0, 0, 10); // sara rocks
		CastleWars.placeObject(4437, 2401, 9494, 0, 0, 10); // sara rocks
		CastleWars.placeObject(4437, 2391, 9501, 0, 0, 10); // zammy rocks
		CastleWars.placeObject(4437, 2400, 9512, 0, 0, 10); // zammy rocks

		// private static void placeObject(int objectId, int x, int y, int z, int face, int type) {
		CastleWars.placeObject(4381, 2384, 3117, 0, 3, 10);
		CastleWars.placeObject(4382, 2413, 3088, 0, 1, 10); // saradomin castle

		// remove all barricades
		zamorakTeam.getBarricades().forEach(Barricade::destroy);
		saradominTeam.getBarricades().forEach(Barricade::destroy);

		// kill off all ropes
		ropes.stream().forEach(Rope::destroy);
		ropes.clear();

		saradominTeam = null;
		zamorakTeam = null;
		gameRunning = false;
		gameEnding = false;
		lobbyTimer = Constants.LOBBY_LENGTH;
	}

	public static boolean[][] getBarricadeLocations() {
		return barricadeLocations;
	}

	public static boolean inCastleWarsGame(Player player) {
		// underground area
		if (player.getX() >= 2369 && player.getX() <= 2430 && player.getY() >= 9481 && player.getY() <= 9527) {
			return true;
		}
		return player.getX() >= 2368 && player.getX() <= 2431 && player.getY() >= 3072 && player.getY() <= 3135;
	}

	private static boolean inSaradominCastle(Location location) {
		return location.getX() >= 2415 && location.getX() <= 2431 && location.getY() >= 3072 && location.getY() <= 3088;
	}

	private static boolean inZamorakCastle(Location location) {
		return location.getX() >= 2368 && location.getX() <= 2384 && location.getY() >= 3119 && location.getY() <= 3135;
	}

	private static void joinLobby(Player player, Faction faction) {
		if (player.equipment[PlayerConstants.HAT] > 0) {
			player.sendMessage("You cannot have any items equipped in the helmet slot.");
			return;
		}
		if (player.equipment[PlayerConstants.CAPE] > 0) {
			player.sendMessage("You cannot have any items equipped in the cape slot.");
			return;
		}
		if (player.combatLevel < 50) {
			player.sendMessage("Level 50+ combat level is required to play this minigame.");
			return;
		}
		if(player.getFamiliar() !=  null) {
			player.sendMessage("Familiars are not allowed into this minigame.");
			return;
		}
		for (int i = 0; i < player.inventory.length; i++) {

			if (player.inventory[i] == 0) {
				continue;
			}

			int itemId = player.inventory[i] - 1;
			String itemName = ItemUtility.getName(itemId);

			if (itemName != null && itemName.endsWith(" rune")) {
				continue;
			}

			// blowpipe and warhammer
			if(itemId == 25040 || itemId == 25053) {
				continue;
			}

			if (!ItemDef.hasOption(itemId, "drink") && !ItemDef.hasOption(itemId, "wear")
					&& !ItemDef.hasOption(itemId, "wield")) {
				player.sendMessage("You may not take non-combat items(" + itemName + ") into Castle wars.");
				return;
			}
		}

		// auto balance to the correct team
		if (faction.equals(Faction.GUTHIX)) {
			int sara = saradominLobbyPlayers.size();
			int zammy = zamorakLobbyPlayers.size();

			if (sara == zammy) {
				faction = Misc.random(1) == 0 ? Faction.ZAMORAK : Faction.SARADOMIN;
			} else if (sara >= zammy) {
				faction = Faction.ZAMORAK;
			} else {
				faction = Faction.SARADOMIN;
			}
		} else {
			// check if maximum amount of player difference has been met
			if (faction.equals(Faction.ZAMORAK)) {
				if (zamorakLobbyPlayers.size() - saradominLobbyPlayers.size() >= Constants.MAXIMUM_DIFFERENCE_OF_PLAYERS) {
					player.sendMessage("There isn't enough space on this team!");
					return;
				}
			} else {
				if (saradominLobbyPlayers.size() - zamorakLobbyPlayers.size() >= Constants.MAXIMUM_DIFFERENCE_OF_PLAYERS) {
					player.sendMessage("There isn't enough space on this team!");
					return;
				}
			}
		}

		player.setAttribute("block_picking_up", true);
		GameCycleTaskHandler.addEvent(player, container -> { player.removeAttribute("block_picking_up");}, 4);

		if (faction.equals(Faction.SARADOMIN)) {
			saradominLobbyPlayers.add(player);

			player.setCastleWarsState(State.SARADOMIN_LOBBY);
		} else if (faction.equals(Faction.ZAMORAK)) {
			zamorakLobbyPlayers.add(player);

			player.setCastleWarsState(State.ZAMORAK_LOBBY);
		}

		if (waitingForPlayers) {
			if (zamorakLobbyPlayers.size() >= Constants.MINIMUM_PER_TEAM
					&& saradominLobbyPlayers.size() >= Constants.MINIMUM_PER_TEAM) {
				waitingForPlayers = false;
			}
		}

		// join the team lobby
		player.getPA().movePlayer(faction.getLobbyLocation());
		player.getItems().setEquipment(faction.getCapeItemId(), 1, PlayerConstants.CAPE);
		player.getItems().setEquipment(faction.getHoodItemId(), 1, PlayerConstants.HAT);
	}

	public static boolean onWearItem(Player player, int item, int invSlot, int wearSlot) {

		if (player.getCastleWarsState() == null) {
			return false;
		}

		if (!gameRunning) {
			return false;
		}

		if (saradominTeam.getFlagTaker() == player || zamorakTeam.getFlagTaker() == player) {
			Team team = saradominTeam.getFlagTaker() == player ? saradominTeam : zamorakTeam;

			player.getItems().setEquipment(-1, 0, PlayerConstants.WEAPON);
			team.setFlagTaker(null);
			team.setFlagStatus(FlagStatus.DROPPED);
			CastleWars.sendArrowHintToPlayers(team, player.getLocation());
			new GameObject(team.equals(saradominTeam) ? 4900 : 4901, player.absX, player.absY, player.heightLevel, 0, 10, -1, -1);

			Location objectLocation = Location.create(player.absX, player.absY, player.heightLevel);
			RegionUtility.addEntity(new DynamicMapObject(team.equals(saradominTeam) ? 4900 : 4901, objectLocation, 11, 3), objectLocation);

			player.getItems().wearItem(item, invSlot);
			return true;
		}

		return false;
	}

	public static boolean onActionButton(Player player, int button) {

		if (player.getCastleWarsState() == null) {
			return false;
		}

		if (!gameRunning) {
			return false;
		}

		Team team = player.getCastleWarsState().equals(State.SARADOMIN_GAME) ? saradominTeam : zamorakTeam;
		Catapult catapult = team.getCatapult();

		switch (button) {
			case 44057: // up
				if (team.equals(zamorakTeam)) {
					if (catapult.getTargetY() <= 0) {
						return true;
					}
				}
				catapult.decrementY();

				player.getPA().setMediaModelId(11317, 4863 + catapult.getTargetX() / 3); // the digits
				player.getPA().setMediaModelId(11318, 4863 + catapult.getTargetX() / 3); // the digits

				updateCatapultInterface(player, catapult);
				return true;
			case 44058: // down
				if (team.equals(saradominTeam)) {
					if (catapult.getTargetY() >= 90) {
						return true;
					}
				}
				catapult.incrementY();
				updateCatapultInterface(player, catapult);
				return true;
			case 44060: // left
				if (team.equals(zamorakTeam)) {
					if (catapult.getTargetX() <= 0) {
						return true;
					}
				}
				catapult.decrementX();
				updateCatapultInterface(player, catapult);
				return true;
			case 44059: // right
				if (team.equals(saradominTeam)) {
					if (catapult.getTargetX() >= 90) {
						return true;
					}
				}
				catapult.incrementX();
				updateCatapultInterface(player, catapult);
				return true;
			case 44065: // fire
				if (!team.isCatapultOperational()) {
					player.sendMessage("The catapult is no longer operational.");
					catapult.setController(null);
					player.getPA().closeAllWindows();
					return true;
				}
				if (!player.getItems().playerHasItem(4043)) {
					player.sendMessage("You do not have any rocks to fire.");
					catapult.setController(null);
					player.getPA().closeAllWindows();
					return true;
				}

				State state = team.equals(zamorakTeam) ? State.SARADOMIN_GAME : State.ZAMORAK_GAME;
				int x = (2387 + (catapult.getTargetX() / 3));
				int y = (3116 - (catapult.getTargetY() / 3));

				if (player.getCastleWarsState().equals(State.SARADOMIN_GAME)) {
					x = 2412 - ((90 - catapult.getTargetX()) / 3);
				}
				if (player.getCastleWarsState().equals(State.SARADOMIN_GAME)) {
					y = 3091 + ((90 - catapult.getTargetY()) / 3);
				}

				for (Player plr : PlayerHandler.players) {
					if (plr == null || plr.getCastleWarsState() == null) {
						continue;
					}

					if (plr.getCastleWarsState().equals(state) && plr.distanceToPoint(x, y) <= 5) {
						int damage = Misc.random(5, 25);

						if (plr.level[3] - damage < 0) {
							damage = plr.level[3];
						}

						plr.level[3] -= damage;
						plr.handleHitMask(damage, 100, 3, plr.playerIndex);
						plr.getPA().refreshSkill(3);
					}
				}

				// public void createPlayersProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,//int startHeight, int endHeight, int lockon, int time) {

				if (team.equals(zamorakTeam)) {
					// 0 south
					// 3 east
					player.getPA().objectAnim(2384, 3117, 4157, 10, 3); // east
					player.getPA().createPlayersProjectile(2384, 3117, 2384 - x, 3157 - y, 50, 60, 61, 10, 0, -1, 30);

				} else {
					// 2 north
					// 1 west
					player.getPA().objectAnim(2413, 3088, 4157, 10, 1);
					player.getPA().createPlayersProjectile(2413, 3088, 2413 - x, 3088 - y, 50, 60, 61, 10, 0, -1, 30);
				}
				player.getItems().deleteItem(4043, 1);
				player.sendMessage("You fire the catapult");
				player.getPA().closeAllWindows();
				catapult.setController(null);
				catapult.setFireCooldown(15);
				return true;
		}

		return false;
	}

	public static boolean onAttack(Player player, Player target) {
		if (target.getCastleWarsState() == null) {
			return false;
		}

		if (player.getCastleWarsState().equals(target.getCastleWarsState())) {
			player.sendMessage("You cannot attack your own team mates.");
			return false;
		}
		return true;
	}

	public static boolean onClickingStuff(Player player) {
		if (player.getCastleWarsState() != null && gameRunning) {
			if (zamorakTeam.getCatapult().getController() != null && zamorakTeam.getCatapult().getController().equals(player)) {
				zamorakTeam.getCatapult().setController(null);
			}
			if (saradominTeam.getCatapult().getController() != null
					&& saradominTeam.getCatapult().getController().equals(player)) {
				saradominTeam.getCatapult().setController(null);
			}
		}
		return false;
	}

	@SuppressWarnings("unused")
	public static boolean onItemClick(Player player, int itemId, int option) {
		switch (itemId) {
			case 4053: // barricade setup
				Team team = player.getCastleWarsState().equals(State.ZAMORAK_GAME) ? zamorakTeam : saradominTeam;
				if (team.getBarricades().size() >= 10) {
					player.sendMessage("Your team already has the maximum amount of barricades out.");
					return true;
				}

				if (CastleWars.barricadeAtLocation(player.getLocation()) != null) {
					player.sendMessage("You cannot place barricades on top of each other.");
					return true;
				}

				player.startAnimation(827);
				player.getItems().deleteItem(4053, 1);
				NPC npc = NPCHandler.spawnNpc2(1532, player.absX, player.absY, player.heightLevel, 0, 50, 0, 0, 0);
				team.getBarricades().add(new Barricade(team, npc));

				Collision.flag(player.absX, player.absY, player.heightLevel, Collision.GROUND_TILE_BLOCKED);

				return true;
		}
		return false;
	}

	public static boolean onItemOnNPC(Player player, NPC npc, int itemId) {

		// tinderbox on barricade
		if (itemId == 590 && npc.type == 1532) {
			Barricade barricade = barricadeAtLocation(npc.getLocation());

			if (barricade == null) {
				return true;
			}

			if (barricade.isBurning()) {
				return true;
			}

			if (npc.applyDead) {
				return true;
			}

			player.faceUpdate(npc.id);
			player.startAnimation(733);

			npc.applyDead = true;
			npc.isDead = true;
			npc.spawnedBy = 1;

			barricade.setBurning(true);
			barricade
					.setNpc(NPCHandler.spawnNpc2(1533, npc.getX(), npc.getY(), npc.heightLevel, 0, 50, 0, 0, 0));
			return true;
		}

		// bucket on burning barricade
		if (itemId == 1929 && npc.type == 1533) {
			Barricade barricade = barricadeAtLocation(npc.getLocation());

			if (barricade == null) {
				return true;
			}

			if (npc.applyDead) {
				return true;
			}

			player.faceUpdate(npc.id);

			npc.applyDead = true;
			npc.isDead = true;
			npc.spawnedBy = 1;

			player.startAnimation(832);

			player.getItems().deleteItem(1929, 1);
			player.getItems().addItem(1925, 1);

			barricade.setBurningTicks(Constants.BARRICADE_ON_FIRE_DURATION);
			barricade.setBurning(false);
			barricade
					.setNpc(NPCHandler.spawnNpc2(1532, npc.getX(), npc.getY(), npc.heightLevel, 0, 50, 0, 0, 0));
			return true;
		}

		// exploding potions
		if (itemId == 4045 && (npc.type == 1532 || npc.type == 1533)) {
			final Barricade barricade = barricadeAtLocation(npc.getLocation());

			if (barricade == null) {
				return true;
			}

			if (npc.applyDead) {
				return true;
			}

			player.faceUpdate(npc.id);

			npc.gfx100(2739);
			player.startAnimation(832);
			player.getItems().deleteItem(4045, 1);

			GameCycleTaskHandler.addEvent(CastleWars.class, (GameCycleTaskContainer container) -> {
				CastleWars.removeBarricade(npc);
				container.stop();
			}, 1);
			return true;
		}
		return false;
	}

	public static boolean onItemOnObject(Player player, int itemId, int objectId, Location objectLocation) {

		if (player.getCastleWarsState() == null) {
			return false;
		}

		// explosive potion on saradomin catapult
		if (itemId == 4045 && objectId == 4382) {
			if (!saradominTeam.isCatapultOperational()) {
				return true;
			}

			player.startAnimation(832);
			player.getItems().deleteItem(itemId, 1);

			CastleWars.placeObject(4386, 2413, 3088, 0, 1, 10);
			saradominTeam.setCatapultOperational(false);
			return true;
		}

		// toolkit on saradomin catapult
		if (itemId == 1 && objectId == 4386) {
			if (saradominTeam.isCatapultOperational()) {
				return true;
			}

			player.startAnimation(832);
			player.getItems().deleteItem(1, 1);

			CastleWars.placeObject(4382, 2413, 3088, 0, 1, 10);
			saradominTeam.setCatapultOperational(true);

			return true;
		}

		// tinderbox on saradomin catapult
		if (itemId == 590 && objectId == 4382) {
			if (!saradominTeam.isCatapultOperational()) {
				return true;
			}

			player.startAnimation(733);
			CastleWars.placeObject(4905, 2413, 3088, 0, 1, 10);
			saradominTeam.setCatapultOperational(false);
			saradominTeam.getCatapult().setOnFire(true);
			saradominTeam.getCatapult().setBurningTicks(Constants.CATAPULT_ON_FIRE_DURATION);
			return true;
		}

		// bucket of water on burning saradomin catapult
		if (itemId == 1929 && objectId == 4905) {
			if (saradominTeam.isCatapultOperational()) {
				return true;
			}

			player.startAnimation(832);
			CastleWars.placeObject(4382, 2413, 3088, 0, 1, 10);
			saradominTeam.setCatapultOperational(true);
			saradominTeam.getCatapult().setOnFire(false);
			return true;
		}

		// explosive potion on zamorak catapult
		if (itemId == 4045 && objectId == 4381) {
			if (!zamorakTeam.isCatapultOperational()) {
				return true;
			}

			player.startAnimation(832);
			player.getItems().deleteItem(itemId, 1);

			CastleWars.placeObject(4385, 2384, 3117, 0, 3, 10);
			zamorakTeam.setCatapultOperational(false);
			return true;
		}

		// toolkit on zamorak catapult
		if (itemId == 1 && objectId == 4385) {
			if (zamorakTeam.isCatapultOperational()) {
				return true;
			}

			player.startAnimation(832);
			player.getItems().deleteItem(1, 1);

			CastleWars.placeObject(4381, 2384, 3117, 0, 3, 10);
			zamorakTeam.setCatapultOperational(true);

			return true;
		}

		// tinderbox on zamorak catapult
		if (itemId == 590 && objectId == 4381) {
			if (!zamorakTeam.isCatapultOperational()) {
				return true;
			}

			player.startAnimation(733);
			CastleWars.placeObject(4904, 2384, 3117, 0, 3, 10);
			zamorakTeam.setCatapultOperational(false);
			zamorakTeam.getCatapult().setOnFire(true);
			zamorakTeam.getCatapult().setBurningTicks(Constants.CATAPULT_ON_FIRE_DURATION);
			return true;
		}

		// bucket of water on burning zamorak catapult
		if (itemId == 1929 && objectId == 4904) {
			if (zamorakTeam.isCatapultOperational()) {
				return true;
			}

			player.startAnimation(832);
			CastleWars.placeObject(4381, 2384, 3117, 0, 3, 10);
			zamorakTeam.setCatapultOperational(true);
			zamorakTeam.getCatapult().setOnFire(false);
			return true;
		}

		// bucket filling
		if (objectId == 4482 && itemId == 1925) {
			player.startAnimation(832);
			player.getItems().deleteItem(itemId, 1);
			player.getItems().addItem(1929, 1);
			player.sendMessage("You fill the bucket.");
			return true;
		}

		// ropes zammy castle
		if (itemId == 4047 && (objectId == 4447 || objectId == 4446)) {

			// check if one already exists
			if (ropes.stream().filter(x -> x.getLocation().equals(objectLocation)).count() > 0) {
				return true;
			}

			if (Rope.validLocation(objectLocation)) {
				ropes.add(new Rope(objectLocation));

				player.getItems().deleteItem(4047, 1);
				return true;
			}
			return true;
		}

		return false;
	}

	public static void onLoggedOut(Player player) {
		CastleWars.removeFromGame(player, true);
	}

	public static boolean onObjectClick(Player player, int objectId, Location objectLocation, int option) {

		switch (objectId) {

			case 4385: // broken zammy catapult
				if (zamorakTeam.isCatapultOperational()) {
					return true;
				}
				if (!player.getCastleWarsState().equals(State.ZAMORAK_GAME)) {
					player.sendMessage("You cannot fix the opposite teams catapult.");
					return true;
				}
				if (!player.getItems().playerHasItem(1)) {
					player.sendMessage("You do not have a toolkit to fix this catapult.");
					return true;
				}
				player.startAnimation(832);
				player.getItems().deleteItem(1, 1);
				zamorakTeam.setCatapultOperational(true);
				CastleWars.placeObject(4381, 2384, 3117, 0, 3, 10);
				return true;

			case 4386: // broken saradomin catapult
				if (saradominTeam.isCatapultOperational()) {
					return true;
				}

				if (!player.getCastleWarsState().equals(State.SARADOMIN_GAME)) {
					player.sendMessage("You cannot fix the opposite teams catapult.");
					return true;
				}

				if (!player.getItems().playerHasItem(1)) {
					player.sendMessage("You do not have a toolkit to fix this catapult.");
					return true;
				}

				player.startAnimation(832);
				player.getItems().deleteItem(1, 1);
				saradominTeam.setCatapultOperational(true);
				CastleWars.placeObject(4382, 2413, 3088, 0, 1, 10); // saradomin castle
				return true;
			case 4448: // collapse wall
				player.startAnimation(832);

				// zammy rock 2
				if (objectLocation.equals(2390, 9503, 0) && !zamorakTeam.isRock2Collapsed()) {
					CastleWars.placeObject(4437, 2391, 9501, 0, 0, 10); // zammy rocks
					zamorakTeam.setRock2Collapsed(true);
				}
				if (objectLocation.equals(2390, 9500, 0) && !zamorakTeam.isRock2Collapsed()) {
					CastleWars.placeObject(4437, 2391, 9501, 0, 0, 10); // zammy rocks
					zamorakTeam.setRock2Collapsed(true);
				}
				if (objectLocation.equals(2393, 9500, 0) && !zamorakTeam.isRock2Collapsed()) {
					CastleWars.placeObject(4437, 2391, 9501, 0, 0, 10); // zammy rocks
					zamorakTeam.setRock2Collapsed(true);
				}
				if (objectLocation.equals(2393, 9503, 0) && !zamorakTeam.isRock2Collapsed()) {
					CastleWars.placeObject(4437, 2391, 9501, 0, 0, 10); // zammy rocks
					zamorakTeam.setRock2Collapsed(true);
				}

				// zammy rock 1
				if (objectLocation.equals(2399, 9514, 0) && !zamorakTeam.isRock1Collapsed()) {
					CastleWars.placeObject(4437, 2400, 9512, 0, 0, 10);
					zamorakTeam.setRock1Collapsed(true);
				}
				if (objectLocation.equals(2399, 9511, 0) && !zamorakTeam.isRock1Collapsed()) {
					CastleWars.placeObject(4437, 2400, 9512, 0, 0, 10);
					zamorakTeam.setRock1Collapsed(true);
				}
				if (objectLocation.equals(2402, 9511, 0) && !zamorakTeam.isRock1Collapsed()) {
					CastleWars.placeObject(4437, 2400, 9512, 0, 0, 10);
					zamorakTeam.setRock1Collapsed(true);
				}
				if (objectLocation.equals(2402, 9514, 0) && !zamorakTeam.isRock1Collapsed()) {
					CastleWars.placeObject(4437, 2400, 9512, 0, 0, 10);
					zamorakTeam.setRock1Collapsed(true);
				}

				// sara rock 2
				if (objectLocation.equals(2400, 9496, 0) && !saradominTeam.isRock2Collapsed()) {
					CastleWars.placeObject(4437, 2401, 9494, 0, 0, 10);
					saradominTeam.setRock2Collapsed(false);
				}
				if (objectLocation.equals(2403, 9496, 0) && !saradominTeam.isRock2Collapsed()) {
					CastleWars.placeObject(4437, 2401, 9494, 0, 0, 10);
					saradominTeam.setRock2Collapsed(false);
				}
				if (objectLocation.equals(2403, 9493, 0) && !saradominTeam.isRock2Collapsed()) {
					CastleWars.placeObject(4437, 2401, 9494, 0, 0, 10);
					saradominTeam.setRock2Collapsed(false);
				}
				if (objectLocation.equals(2400, 9493, 0) && !saradominTeam.isRock2Collapsed()) {
					CastleWars.placeObject(4437, 2401, 9494, 0, 0, 10);
					saradominTeam.setRock2Collapsed(false);
				}

				// sara rock 1
				if (objectLocation.equals(2411, 9502, 0) && !saradominTeam.isRock1Collapsed()) {
					CastleWars.placeObject(4437, 2409, 9503, 0, 0, 10);
					saradominTeam.setRock1Collapsed(true);
				}
				if (objectLocation.equals(2411, 9505, 0) && !saradominTeam.isRock1Collapsed()) {
					CastleWars.placeObject(4437, 2409, 9503, 0, 0, 10);
					saradominTeam.setRock1Collapsed(true);
				}
				if (objectLocation.equals(2408, 9505, 0) && !saradominTeam.isRock1Collapsed()) {
					CastleWars.placeObject(4437, 2409, 9503, 0, 0, 10);
					saradominTeam.setRock1Collapsed(true);
				}
				if (objectLocation.equals(2408, 9502, 0) && !saradominTeam.isRock1Collapsed()) {
					CastleWars.placeObject(4437, 2409, 9503, 0, 0, 10);
					saradominTeam.setRock1Collapsed(true);
				}

				return true;
			case 4437: // object mining

				// check if a player is already mining it
				if (GameCycleTaskHandler.eventExists("mining" + objectLocation)) {
					return true;
				}

				PickAxe axe = PickAxe.get(player);

				if (axe == null) {
					player.sendMessage("You require a pickaxe to mine this rock.");
					return true;
				}

				player.startAnimation(axe.getAnimation());

				GameCycleTaskHandler.addEvent(CastleWars.class, new GameCycleTask() {

					boolean placed = false;

					@Override
					public void execute(GameCycleTaskContainer container) {
						if (!placed) {
							CastleWars.placeObject(4438, objectLocation.getX(), objectLocation.getY(),
									objectLocation.getZ(), 0, 10);

							placed = true;
						} else {
							CastleWars.placeObject(-1, objectLocation.getX(), objectLocation.getY(), objectLocation.getZ(),
									0, 10);

							if (objectLocation.equals(2409, 9503, 0)) {
								saradominTeam.setRock1Collapsed(false);
							} else if (objectLocation.equals(2401, 9494, 0)) {
								saradominTeam.setRock2Collapsed(false);
							} else if (objectLocation.equals(2391, 9501, 0)) {
								zamorakTeam.setRock2Collapsed(false);
							} else if (objectLocation.equals(2400, 9512, 0)) {
								zamorakTeam.setRock1Collapsed(false);
							}

							player.startAnimation(65535);
							container.stop();
						}
					}
				}, 2).setName("mining" + objectLocation);
				return true;

			case 4483:
				player.getPA().openUpBank();
				return true;

			case 5946: // rope climbing
				if (player.getCooldowns().exists("climb rope")) {
					return false;
				}

				final int[] move = Rope.movePlayerBy(objectLocation);

				if (move != null) {
					player.getCooldowns().add("climb rope", 3);

					player.startAnimation(828);

					GameCycleTaskHandler.addEvent(player, (GameCycleTaskContainer container) -> {
						player.getPA().movePlayer(objectLocation.transform(move[0], move[1], 0));
						container.stop();

					}, 2);
					return true;
				}
				break;

			case 4427: // zamorak door
			case 4428:
			case 4429:

				// door closing
				if (objectId == 4427 && objectLocation.equals(2372, 3118, 0) || objectId == 4428 && objectLocation.equals(2373, 3118, 0)) {
					zamorakTeam.setDoorHealth(100);
					return false;
				}

				// opening the door from the inside
				if (player.getY() >= 3119 && (objectLocation.equals(2373, 3119, 0) || objectLocation.equals(2372, 3119, 0))) {
					if (option == 1) {
						zamorakTeam.setDoorHealth(0);
					}
					return false;
				}

				// opening or attacking the door from the outside
				if (player.getY() <= 3118
						&& (objectId == 4428 && objectLocation.equals(2373, 3119, 0) || objectId == 4427
						&& objectLocation.equals(2372, 3119, 0))) {

					// opening
					if (option == 1) {
						if (player.getCastleWarsState().equals(State.SARADOMIN_GAME)) {
							player.sendMessage("This door is locked.");
							return true;
						}
						zamorakTeam.setDoorHealth(0);
					}

					// attacking
					if (option == 2) {
						if (player.getCastleWarsState().equals(State.ZAMORAK_GAME)) {
							player.sendMessage("You cannot attack your own castle's door.");
							return true;
						}

						if (zamorakTeam.getDoorHealth() <= 0) {
							return true;
						}

						String itemName = ItemUtility.getName(player.equipment[3]);

						// stupid shouldn't have to do this
						if (itemName == null) {
							itemName = "";
						}

						player.startAnimation(Animations.getAttackAnimation(player));
						zamorakTeam.setDoorHealth(zamorakTeam.getDoorHealth() - 10);

						if (zamorakTeam.getDoorHealth() <= 0) {
							DoorHandler.handleDoor(player, objectId, objectLocation);
							return false;
						}
						return true;
					}
				}

				break;
			case 4424: // saradomin castle main door
			case 4423:
			case 4425:

				// door closing
				if (objectId == 4423 && objectLocation.equals(2427, 3089, 0) || objectId == 4424 && objectLocation.equals(2426, 3089, 0)) {
					saradominTeam.setDoorHealth(100);

					return false;
				}

				// opening the door from the inside
				if (player.getY() <= 3088 && (objectLocation.equals(2427, 3088, 0) || objectLocation.equals(2426, 3088, 0))) {
					if (option == 1) {
						saradominTeam.setDoorHealth(0);
					}
				}

				// opening or attacking the door from the outside
				if (objectLocation.equals(2426, 3088, 0) || objectLocation.equals(2427, 3088, 0)) {

					// opening
					if (option == 1) {
						if (player.getCastleWarsState().equals(State.ZAMORAK_GAME)) {
							player.sendMessage("This door is locked.");
							return true;
						}
						saradominTeam.setDoorHealth(0);
					}

					// attacking
					if (option == 2) {
						if (player.getCastleWarsState().equals(State.SARADOMIN_GAME)) {
							player.sendMessage("You cannot attack your own castle's door.");
							return true;
						}

						if (saradominTeam.getDoorHealth() <= 0) {
							return true;
						}

						String itemName = ItemUtility.getName(player.equipment[3]);

						// stupid shouldn't have to do this
						if (itemName == null) {
							itemName = "";
						}

						player.startAnimation(Animations.getAttackAnimation(player));
						saradominTeam.setDoorHealth(saradominTeam.getDoorHealth() - 10);

						if (saradominTeam.getDoorHealth() <= 0) {
							DoorHandler.handleDoor(player, objectId, objectLocation);
							return false;
						}
						return true;
					}
				}
				break;
			case 4466: // saradomin sidedoor
				if (objectLocation.equals(2414, 3073, 0)) { // open
					saradominTeam.setSidedoorLocked(false);
				}
				if (objectLocation.equals(2414, 3073, 0)) {
					if (player.getCastleWarsState().equals(State.ZAMORAK_GAME)) { // closed
						player.sendMessage("This door is locked.");
						return true;
					}
					saradominTeam.setSidedoorLocked(true);
				}
				break;
			case 4467: // zamarak sidedoor
				if (objectLocation.equals(2384, 3134, 0)) { // open
					zamorakTeam.setSidedoorLocked(true);
				}

				if (objectLocation.equals(2385, 3134, 0)) {
					if (player.getCastleWarsState().equals(State.SARADOMIN_GAME)) { // closed
						player.sendMessage("This door is locked.");
						return true;
					}
					zamorakTeam.setSidedoorLocked(false);
				}
				break;
			case 4411: // stepping stones
				int i = objectLocation.getX() - player.getX();
				int j = objectLocation.getY() - player.getY();

				if (i == -1 && j == 1 || i == 1 && j == -1) {
					return true;
				}

				final boolean isRunning = player.getWalkingQueue().isRunningToggled();

				player.getWalkingQueue().setRunningToggled(false);
				player.playerWalkIndex = 769;
				player.updateRequired = true;
				player.appearanceUpdateRequired = true;

				player.getPA().walkTo(i, j);

				GameCycleTaskHandler.addEvent(player, new GameCycleTask() {
					@Override
					public void execute(GameCycleTaskContainer container) {
						player.getWalkingQueue().setRunningToggled(isRunning);
						Animations.setMovementAnimationIds(player);
						player.updateRequired = true;
						player.appearanceUpdateRequired = true;
						stop();
					}
				}, 1);
				return true;
			case 4471:
				if (objectLocation.equals(2429, 3074, 2)) { // trapdoor sara castle second floor to 1st floor spawn area
					if (!player.getCastleWarsState().equals(State.SARADOMIN_GAME)) {
						player.sendMessage("You cannot use a trapdoor into the opposite team area.");
						return true;
					}
					saradominTeam.getRespawnAreaTimes().put(player, System.currentTimeMillis());
				}
				return false;
			case 4472:
				if (objectLocation.equals(2370, 3133, 2)) { // trapdoor zammy castle second floor to 1st floor spawn area
					if (!player.getCastleWarsState().equals(State.ZAMORAK_GAME)) {
						player.sendMessage("You cannot use a trapdoor into the opposite team area.");
						return true;
					}
					zamorakTeam.getRespawnAreaTimes().put(player, System.currentTimeMillis());
				}
				return false;
			case 36484:
				if (player.getLocation().equals(2425, 3077, 2)) { // stairs sara castle second floor to third floor
					player.getPA().movePlayer(2426, 3074, 3);
				}
				return true;
			case 4902: // saradomin stand
			case 4903: // zamorak stand
				Team team = objectId == 4902 ? saradominTeam : zamorakTeam;
				Team oppositeTeam = objectId == 4902 ? zamorakTeam : saradominTeam;

				if (team.getPlayers().contains(player)) {
					if (oppositeTeam.getFlagStatus().equals(FlagStatus.TAKEN) && oppositeTeam.getFlagTaker().equals(player)) {
						player.setCastleWarsCaptures(player.getCastleWarsCaptures() + 1);
						player.getItems().setEquipment(-1, 0, PlayerConstants.WEAPON);

						team.setScore(team.getScore() + 1);
						CastleWars.returnFlagToStand(oppositeTeam);
						return true;
					}

					player.sendMessage("You cannot take your own team flag.");
					return true;
				}

				// takes the flag
				int freeSlotsRequired = 0;
				if (player.equipment[PlayerConstants.WEAPON] > 0) {
					freeSlotsRequired++;
				}
				if (player.equipment[PlayerConstants.SHIELD] > 0) {
					freeSlotsRequired++;
				}
				if (player.getItems().freeInventorySlots() < freeSlotsRequired) {
					player.sendMessage("You do not have enough free inventory space to take the flag.");
					return true;
				}
				if (player.equipment[PlayerConstants.WEAPON] > 0) {
					player.getItems().addItem(player.equipment[PlayerConstants.WEAPON], player.playerEquipmentN[PlayerConstants.WEAPON]);
				}
				if (player.equipment[PlayerConstants.SHIELD] > 0) {
					player.getItems().addItem(player.equipment[PlayerConstants.SHIELD], player.playerEquipmentN[PlayerConstants.SHIELD]);
				}
				team.setFlagTaker(player);

				player.getItems().setEquipment(team == saradominTeam ? 4037 : 4039, 1, PlayerConstants.WEAPON);
				if (player.equipment[PlayerConstants.SHIELD] > 0) {
					player.getItems().setEquipment(-1, 0, PlayerConstants.SHIELD);
				}

				Optional<MapObject> optional = RegionUtility.getMapObject(objectId, objectLocation);
				if(optional.isPresent()) {
					RegionUtility.removeEntity(optional.get(), objectLocation);
				}
				RegionUtility.addEntity(new DynamicMapObject(team == saradominTeam ? 4377 : 4378, objectLocation, 11, 3), objectLocation);

				CastleWars.placeObject(team == saradominTeam ? 4377 : 4378, objectLocation.getX(), objectLocation.getY(), objectLocation.getZ(), 3, 11);
				CastleWars.sendArrowHintToPlayers(team, player.playerId);
				team.setFlagStatus(FlagStatus.TAKEN);
				return true;
			case 4377:
			case 4378:
				team = objectId == 4902 ? saradominTeam : zamorakTeam;
				oppositeTeam = objectId == 4902 ? zamorakTeam : saradominTeam;
				if (team.getPlayers().contains(player)) {
					if (oppositeTeam.getFlagStatus().equals(FlagStatus.TAKEN) && oppositeTeam.getFlagTaker().equals(player)) {
						player.sendMessage("You cannot take your own team flag.");
						return true;
					}

					player.sendMessage("You cannot take your own team flag.");
					return true;
				}
				return true;
			case 4900: // saradomin flag dropped
			case 4901: // zamorak flag dropped
				team = objectId == 4900 ? saradominTeam : zamorakTeam;

				// make sure the team flag is dropped.
				if (!team.getFlagStatus().equals(FlagStatus.DROPPED)) {
					return true;
				}

				GameObject gameObject = Server.objectManager.getObject(objectLocation.getX(), objectLocation.getY(),
						objectLocation.getZ());
				if (gameObject == null) {
					return false;
				}

				// takes the flag
				freeSlotsRequired = 0;
				if (player.equipment[PlayerConstants.WEAPON] > 0) {
					freeSlotsRequired++;
				}
				if (player.equipment[PlayerConstants.SHIELD] > 0) {
					freeSlotsRequired++;
				}
				if (player.getItems().freeInventorySlots() < freeSlotsRequired) {
					player.sendMessage("You do not have enough free inventory space to take the flag.");
					return true;
				}
				if (player.equipment[PlayerConstants.WEAPON] > 0) {
					player.getItems().addItem(player.equipment[PlayerConstants.WEAPON], player.playerEquipmentN[PlayerConstants.WEAPON]);
				}
				if (player.equipment[PlayerConstants.SHIELD] > 0) {
					player.getItems().addItem(player.equipment[PlayerConstants.SHIELD], player.playerEquipmentN[PlayerConstants.SHIELD]);
					player.getItems().setEquipment(-1, 0, PlayerConstants.SHIELD);
				}

				// remove the object
				Server.objectManager.removeObject(objectLocation.getX(), objectLocation.getY(), objectLocation.getZ());
				Server.objectManager.objects.remove(gameObject);
				optional = RegionUtility.getMapObject(objectId, objectLocation);
				if(optional.isPresent()) {
					RegionUtility.removeEntity(optional.get(), objectLocation);
				}

				// set taken etc
				team.setFlagTaker(player);
				player.getItems().setEquipment(team == saradominTeam ? 4037 : 4039, 1, PlayerConstants.WEAPON);
				CastleWars.sendArrowHintToPlayers(team, player.playerId);
				team.setFlagTaker(player);
				team.setFlagStatus(FlagStatus.TAKEN);
				return true;
			case 4387: // saradomin join portal
				CastleWars.joinLobby(player, Faction.SARADOMIN);
				return true;
			case 4388: // zamorak join portal
				CastleWars.joinLobby(player, Faction.ZAMORAK);
				return true;
			case 4408: // guthix join portal
				CastleWars.joinLobby(player, Faction.GUTHIX);
				return true;
			case 4390: // zamorak lobby exit portal
			case 4389: // saradomin lobby exist portal
			case 4407: // zamorak leave portal
			case 4406:
				CastleWars.removeFromGame(player, false);
				return true;
			case 36586: // bandages table
			case 36579:

				if (objectId == 36579) {
					if (!player.getCastleWarsState().equals(State.SARADOMIN_GAME)) {
						return true;
					}
				} else {
					if (!player.getCastleWarsState().equals(State.ZAMORAK_GAME)) {
						return true;
					}
				}

				if (player.getCooldowns().exists("give_item")) {
					return true;
				}
				player.getCooldowns().add("give_item", 1);
				player.startAnimation(832);
				player.getItems().addItem(4049, 1);
				return true;
			case 4470: // zamorak lobby barrier
				if (!player.getCastleWarsState().equals(State.ZAMORAK_GAME)) {
					player.sendMessage("You do not belong to this team.");
					return true;
				}
				if (saradominTeam.getFlagTaker() == player || zamorakTeam.getFlagTaker() == player) {
					player.sendMessage("You cannot walk in the respawn area with a flag.");
					return true;
				}
				// southern one
				if (objectLocation.equals(2373, 3127, 1)) {
					if (player.getLocation().equals(2373, 3127, 1)) {
						player.getPA().walkTo(0, -1);
						zamorakTeam.getRespawnAreaTimes().remove(player);
					}
					if (player.getLocation().equals(2373, 3126, 1)) {
						player.getPA().walkTo(0, 1);
						zamorakTeam.getRespawnAreaTimes().put(player, System.currentTimeMillis());
					}
				}
				// north west
				if (objectLocation.equals(2376, 3131, 1)) {
					if (player.getLocation().equals(2376, 3131, 1)) {
						player.getPA().walkTo(1, 0);
						zamorakTeam.getRespawnAreaTimes().remove(player);
					}
					if (player.getLocation().equals(2377, 3131, 1)) {
						player.getPA().walkTo(-1, 0);
						zamorakTeam.getRespawnAreaTimes().put(player, System.currentTimeMillis());
					}
				}
				return true;
			case 6280: // ladder sara castle spawn area to 2nds floor
				if (objectLocation.equals(2429, 3074, 1)) {
					saradominTeam.getRespawnAreaTimes().remove(player);
				}
				break;
			case 6281: // ladder zammy castle spawn area to 2nd floor
				if (objectLocation.equals(2370, 3133, 1)) {
					zamorakTeam.getRespawnAreaTimes().remove(player);
				}
				break;
			case 4469: // saradomin lobby barrier
				if (!player.getCastleWarsState().equals(State.SARADOMIN_GAME)) {
					player.sendMessage("You do not belong to this team.");
					return true;
				}
				if (saradominTeam.getFlagTaker() == player || zamorakTeam.getFlagTaker() == player) {
					player.sendMessage("You cannot walk in the respawn area with a flag.");
					return true;
				}
				// north
				if (objectLocation.equals(2426, 3080, 1)) {
					if (player.getLocation().equals(2426, 3080, 1)) {
						player.getPA().walkTo(0, 1);
						saradominTeam.getRespawnAreaTimes().remove(player);
					}
					if (player.getLocation().equals(2426, 3081, 1)) {
						player.getPA().walkTo(0, -1);
						saradominTeam.getRespawnAreaTimes().put(player, System.currentTimeMillis());
					}
				}

				// west
				if (objectLocation.equals(2423, 3076, 1)) {
					if (player.getLocation().equals(2423, 3076, 1)) {
						player.getPA().walkTo(-1, 0);
						saradominTeam.getRespawnAreaTimes().remove(player);
					}
					if (player.getLocation().equals(2422, 3076, 1)) {
						player.getPA().walkTo(1, 0);
						saradominTeam.getRespawnAreaTimes().put(player, System.currentTimeMillis());
					}
				}
				return true;
			case 36480: // stairs level 1 sara castle to 2nd floor
				if (player.getLocation().equals(2427, 3081, 1)) {
					player.getPA().movePlayer(2430, 3080, 2);
				}
				return true;
			case 36521:
				// stairs level 1 zammy castle to 2nd level
				if (player.getLocation().equals(2372, 3126, 1)) {
					player.getPA().movePlayer(2369, 3127, 2);
				}
				return true;
			case 4415:
				if (player.getLocation().equals(2369, 3127, 2)) { // stairs zammy castle 2nd floor to 1st floor
					player.getPA().movePlayer(2372, 3126, 1);
				}
				if (player.getLocation().equals(2373, 3133, 3)) { // stairs zammy castle 3rd floor to 2nd floor
					player.getPA().movePlayer(2374, 3130, 2);
				}
				if (player.getLocation().equals(2379, 3127, 1)) { // stairs zammy castle to ground floor
					player.getPA().movePlayer(2380, 3130, 0);
				}
				if (player.getLocation().equals(2383, 3132, 0)) { // stairs zammy castle catapult
					player.getPA().movePlayer(2382, 3129, 0);
				}
				if (player.getLocation().equals(2430, 3080, 2)) { // stairs sara castle 2nd floor to 1st floor
					player.getPA().movePlayer(2427, 3081, 1);
				}
				if (player.getLocation().equals(2426, 3074, 3)) { // stairs sara castle 3rd floor to 2nd floor
					player.getPA().movePlayer(2425, 3077, 2);
				}
				if (player.getLocation().equals(2420, 3080, 1)) { // stairs sara castle 1st floor to ground floor
					player.getPA().movePlayer(2419, 3077, 0);
				}
				if (player.getLocation().equals(2416, 3075, 0)) { // stairs sara castle catapult level to ground level
					player.getPA().movePlayer(2417, 3078, 0);
				}
				return true;
			case 36481:
				if (player.getLocation().equals(2417, 3078, 0)) { // stairs sara castle ground level to catapult level
					player.getPA().movePlayer(2416, 3075, 0);
				}
				return true;
			case 36495:
				if (player.getLocation().equals(2419, 3077, 0)) { // stairs sara castle ground floor to 1st floor
					player.getPA().movePlayer(2420, 3080, 1);
				}
				return true;
			case 36523: // stairs zammy castle 2nd flood to 3rd floor
				if (player.getLocation().equals(2374, 3130, 2)) {
					player.getPA().movePlayer(2373, 3133, 3);
				}
				return true;
			case 36532: // stairs zammy castle ground floor to first floor
				if (player.getLocation().equals(2380, 3130, 0)) {
					player.getPA().movePlayer(2379, 3127, 1);
				}
				return true;
			case 36540: // stairs zammy castle up to catapult
				if (player.getLocation().equals(2382, 3129, 0)) {
					player.getPA().movePlayer(2383, 3132, 0);
				}
				return true;
			case 36582: // barricades table
			case 36575:

				if (objectId == 36575) {
					if (!player.getCastleWarsState().equals(State.SARADOMIN_GAME)) {
						return true;
					}
				} else {
					if (!player.getCastleWarsState().equals(State.ZAMORAK_GAME)) {
						return true;
					}
				}

				if (player.getCooldowns().exists("give_item")) {
					return true;
				}
				player.getCooldowns().add("give_item", 1);
				player.startAnimation(832);
				player.getItems().addItem(4053, 1);
				return true;
			case 36583:
			case 36576: // rope table

				if (objectId == 36576) {
					if (!player.getCastleWarsState().equals(State.SARADOMIN_GAME)) {
						return true;
					}
				} else {
					if (!player.getCastleWarsState().equals(State.ZAMORAK_GAME)) {
						return true;
					}
				}

				if (player.getCooldowns().exists("give_item")) {
					return true;
				}
				player.getCooldowns().add("give_item", 1);
				player.startAnimation(832);
				player.getItems().addItem(4047, 1);
				return true;
			case 36580: // toolkit table
			case 36573:

				if (objectId == 36573) {
					if (!player.getCastleWarsState().equals(State.SARADOMIN_GAME)) {
						return true;
					}
				} else {
					if (!player.getCastleWarsState().equals(State.ZAMORAK_GAME)) {
						return true;
					}
				}

				if (player.getCooldowns().exists("give_item")) {
					return true;
				}
				player.getCooldowns().add("give_item", 1);
				player.startAnimation(832);
				player.getItems().addItem(1, 1);
				return true;
			case 36584: // exploding potion table
			case 36577:

				if (objectId == 36577) {
					if (!player.getCastleWarsState().equals(State.SARADOMIN_GAME)) {
						return true;
					}
				} else {
					if (!player.getCastleWarsState().equals(State.ZAMORAK_GAME)) {
						return true;
					}
				}

				if (player.getCooldowns().exists("give_item")) {
					return true;
				}
				player.getCooldowns().add("give_item", 1);
				player.startAnimation(832);
				player.getItems().addItem(4045, 1);
				return true;
			case 36581:
			case 36574: // rock table

				if (objectId == 36574) {
					if (!player.getCastleWarsState().equals(State.SARADOMIN_GAME)) {
						return true;
					}
				} else {
					if (!player.getCastleWarsState().equals(State.ZAMORAK_GAME)) {
						return true;
					}
				}

				if (player.getCooldowns().exists("give_item")) {
					return true;
				}
				player.getCooldowns().add("give_item", 1);
				player.startAnimation(832);
				player.getItems().addItem(4043, 1);
				return true;
			case 36585:
			case 36578: // pickaxe table

				if (objectId == 36578) {
					if (!player.getCastleWarsState().equals(State.SARADOMIN_GAME)) {
						return true;
					}
				} else {
					if (!player.getCastleWarsState().equals(State.ZAMORAK_GAME)) {
						return true;
					}
				}

				if (player.getCooldowns().exists("give_item")) {
					return true;
				}
				player.getCooldowns().add("give_item", 1);
				player.startAnimation(832);
				player.getItems().addItem(1265, 1);
				return true;
			case 4381: // catapult zammy
				if (!player.getCastleWarsState().equals(State.ZAMORAK_GAME)) {
					player.sendMessage("This is not your catapult to control.");
					return true;
				}
				if (zamorakTeam.getCatapult().getFireCooldown() > 0) {
					player.sendMessage("The catapult is currently on a cooldown. Try again soon.");
					return true;
				}
				if (!zamorakTeam.isCatapultOperational()) {
					player.sendMessage("Your catapult is not operational.");
					return true;
				}
				if (!player.getItems().playerHasItem(4043)) {
					player.sendMessage("You have no ammo for the catapult.");
					return true;
				}
				if (zamorakTeam.getCatapult().getController() != null) {
					player.sendMessage("The catapult is already in use.");
					return true;
				}

				zamorakTeam.getCatapult().setController(player);
				saradominTeam.getCatapult().setTargetX(0);
				saradominTeam.getCatapult().setTargetY(0);

				player.getPA().setMediaModelId(11317, 4863); // the digits
				player.getPA().setMediaModelId(11318, 4863); // the digits
				player.getPA().setMediaModelId(11319, 4863); // the digits
				player.getPA().setMediaModelId(11320, 4863); // the digits
				player.getPA().setComponentPosition(11332, 0, 0); // the target
				player.getPA().showInterface(11169);
				return true;
			case 4382: // catapult sara
				if (!player.getCastleWarsState().equals(State.SARADOMIN_GAME)) {
					player.sendMessage("This is not your catapult to control.");
					return true;
				}
				if (saradominTeam.getCatapult().getFireCooldown() > 0) {
					player.sendMessage("The catapult is currently on a cooldown. Try again soon.");
					return true;
				}
				if (!saradominTeam.isCatapultOperational()) {
					player.sendMessage("Your catapult is not operational.");
					return true;
				}
				if (!player.getItems().playerHasItem(4043)) {
					player.sendMessage("You have no ammo for the catapult.");
					return true;
				}
				if (saradominTeam.getCatapult().getController() != null) {
					player.sendMessage("The catapult is already in use.");
					return true;
				}

				saradominTeam.getCatapult().setController(player);
				saradominTeam.getCatapult().setTargetX(90);
				saradominTeam.getCatapult().setTargetY(90);

				player.getPA().setMediaModelId(11317, 4863); // the digits
				player.getPA().setMediaModelId(11318, 4863); // the digits
				player.getPA().setMediaModelId(11319, 4863); // the digits
				player.getPA().setMediaModelId(11320, 4863); // the digits
				player.getPA().setComponentPosition(11332, 90, 90); // the target
				player.getPA().showInterface(11169);
				return true;
		}
		return false;
	}

	public static void onPlayerDeath(Player player) {
		if (player.equipment[PlayerConstants.WEAPON] == 4037
				|| player.equipment[PlayerConstants.WEAPON] == 4039) {
			Team team = player.equipment[PlayerConstants.WEAPON] == 4037 ? saradominTeam : zamorakTeam;
			if (team.getFlagStatus().equals(FlagStatus.TAKEN) && team.getFlagTaker().equals(player)) {
				team.setFlagStatus(FlagStatus.DROPPED);
				team.setFlagTaker(null);
				CastleWars.sendArrowHintToPlayers(team, player.getLocation());
				new GameObject(team.equals(saradominTeam) ? 4900 : 4901, player.absX, player.absY, player.heightLevel, 0, 10, -1, -1);
				player.getItems().setEquipment(-1, 0, PlayerConstants.WEAPON);

				Location objectLocation = Location.create(player.absX, player.absY, player.heightLevel);
				RegionUtility.addEntity(new DynamicMapObject(team.equals(saradominTeam) ? 4900 : 4901, objectLocation, 11, 3), objectLocation);
			}
		}
		if (player.getCastleWarsState().equals(State.ZAMORAK_GAME)) {
			player.getPA().movePlayer(Faction.ZAMORAK.getGameLobbyLocation());
			zamorakTeam.getRespawnAreaTimes().put(player, System.currentTimeMillis());
		}
		if (player.getCastleWarsState().equals(State.SARADOMIN_GAME)) {
			player.getPA().movePlayer(Faction.SARADOMIN.getGameLobbyLocation());
			saradominTeam.getRespawnAreaTimes().put(player, System.currentTimeMillis());
		}
	}

	public static boolean onPlayerLogin(Player player) {
		// precaution
		if (CastleWars.inCastleWarsGame(player)) {
			player.getPA().movePlayer(Constants.STARTING_AREA);

			// delete minigame items
			for (int itemId : Constants.MINIGAME_ITEM_IDS) {
				player.getItems().deleteItem(itemId, 28);
			}
			if (player.equipment[PlayerConstants.WEAPON] == 4037
					|| player.equipment[PlayerConstants.WEAPON] == 4039) {
				player.getItems().setEquipment(-1, 0, PlayerConstants.WEAPON);
			}
			player.getItems().setEquipment(-1, 0, PlayerConstants.CAPE);
			player.getItems().setEquipment(-1, 0, PlayerConstants.HAT);

		}
		return false;
	}

	public static boolean onPlayerWalk(Player player) {
		if (player.getCastleWarsState() != null && gameRunning) {
			if (zamorakTeam.getCatapult().getController() != null
					&& zamorakTeam.getCatapult().getController().equals(player)) {
				zamorakTeam.getCatapult().setController(null);
			}
			if (saradominTeam.getCatapult().getController() != null
					&& saradominTeam.getCatapult().getController().equals(player)) {
				saradominTeam.getCatapult().setController(null);
			}
		}
		return false;
	}

	@SuppressWarnings("unused")
	public static boolean onRemoveItem(Player player, int itemId, int slot) {
		if (player.getCastleWarsState() == null) {
			return false;
		}
		if (slot == PlayerConstants.HAT || slot == PlayerConstants.CAPE) {
			player.sendMessage("You cannot remove this item whilst in Castlewars.");
			return true;
		}

		if (player.equipment[PlayerConstants.WEAPON] == 4037
				|| player.equipment[PlayerConstants.WEAPON] == 4039) {
			Team team = player.equipment[PlayerConstants.WEAPON] == 4037 ? saradominTeam : zamorakTeam;
			if (team.getFlagStatus().equals(FlagStatus.TAKEN) && team.getFlagTaker().equals(player)) {
				team.setFlagStatus(FlagStatus.DROPPED);
				team.setFlagTaker(null);
				CastleWars.sendArrowHintToPlayers(team, player.getLocation());
				new GameObject(team.equals(saradominTeam) ? 4900 : 4901, player.absX, player.absY, player.heightLevel, 0, 10, -1, -1);

				Location objectLocation = Location.create(player.absX, player.absY, player.heightLevel);
				RegionUtility.addEntity(new DynamicMapObject(team.equals(saradominTeam) ? 4900 : 4901, objectLocation, 11, 3), objectLocation);

				player.getItems().setEquipment(-1, 0, PlayerConstants.WEAPON);
			}
			return true;
		}

		return false;
	}

	public static boolean onTeleport(Player player) {
		if (player.getCastleWarsState() != null) {
			player.sendMessage("Please leave the portal to leave the game.");
			return true;
		}
		return false;
	}

	private static void placeObject(int objectId, int x, int y, int z, int face, int type) {
		GameObject obj = Server.objectManager.getObject(x, y, z);
		if (obj != null) {
			Server.objectManager.objects.remove(obj);
		}
		new GameObject(objectId, x, y, z, face, type, 0, -1);
	}

	public static void removeBarricade(NPC npc) {

		if (!gameRunning) {
			return;
		}

		Barricade barricade = barricadeAtLocation(npc.getLocation());
		if (barricade != null) {
			barricade.destroy();

			Collision.removeClipping(npc.getX(), npc.getY(), npc.heightLevel);

			zamorakTeam.getBarricades().remove(barricade);
			saradominTeam.getBarricades().remove(barricade);
		}
	}

	public static void removeFromGame(Player player, boolean loggedOut) {
		if (player.getCastleWarsState().equals(State.SARADOMIN_LOBBY)) {
			saradominLobbyPlayers.remove(player);

			if (saradominLobbyPlayers.size() == 0) {
				waitingForPlayers = true;
			}
		}

		if (player.getCastleWarsState().equals(State.ZAMORAK_LOBBY)) {
			zamorakLobbyPlayers.remove(player);

			if (zamorakLobbyPlayers.size() == 0) {
				waitingForPlayers = true;
			}
		}

		// delete c war game items
		if (player.getCastleWarsState().equals(State.SARADOMIN_GAME)
				|| player.getCastleWarsState().equals(State.ZAMORAK_GAME)) {

			// delete minigame items
			for (int itemId : Constants.MINIGAME_ITEM_IDS) {
				player.getItems().deleteItem(itemId, 28);
			}

			if (player.equipment[PlayerConstants.WEAPON] == 4037
					|| player.equipment[PlayerConstants.WEAPON] == 4039) {
				player.getItems().setEquipment(-1, 0, PlayerConstants.WEAPON);
			}

			if (!gameEnding) {

				if (player.getCastleWarsState().equals(State.ZAMORAK_GAME)) {
					zamorakTeam.getPlayers().remove(player);

					if (zamorakTeam.getPlayers().size() == 0) {
						CastleWars.endGame();
					}
				} else {
					saradominTeam.getPlayers().remove(player);

					if (saradominTeam.getPlayers().size() == 0) {
						CastleWars.endGame();
					}
				}
			}
		}

		if (loggedOut) {
			player.absX = Constants.STARTING_AREA.getX();
			player.absY = Constants.STARTING_AREA.getY();
			player.heightLevel = Constants.STARTING_AREA.getZ();
		} else {
			player.getPA().movePlayer(Constants.STARTING_AREA);
		}
		player.getPA().drawHeadicon(PlayerAssistant.HintType.PLAYER, -1, null, 0);
		player.setCastleWarsState(null);
		player.getItems().setEquipment(-1, 0, PlayerConstants.CAPE);
		player.getItems().setEquipment(-1, 0, PlayerConstants.HAT);
	}

	private static void returnFlagToStand(Team team) {
		team.setFlagTaker(null);
		team.setFlagStatus(FlagStatus.SAFE);

		if (team.equals(saradominTeam)) {
			Location objectLocation = Location.create(2429, 3074, 3);
			Optional<MapObject> optional = RegionUtility.getMapObject(4377, objectLocation);
			if(optional.isPresent()) {
				RegionUtility.removeEntity(optional.get(), objectLocation);
			}
			RegionUtility.addEntity(new DynamicMapObject(4902, objectLocation, 11, 3), objectLocation);
			CastleWars.placeObject(4902, objectLocation.getX(), objectLocation.getY(), objectLocation.getZ(), 1, 11);
		} else {
			Location objectLocation = Location.create(2370, 3133, 3);
			Optional<MapObject> optional = RegionUtility.getMapObject(4378, objectLocation);
			if(optional.isPresent()) {
				RegionUtility.removeEntity(optional.get(), objectLocation);
			}
			RegionUtility.addEntity(new DynamicMapObject(4903, objectLocation, 11, 3), objectLocation);
			CastleWars.placeObject(4903, objectLocation.getX(), objectLocation.getY(), objectLocation.getZ(), 3, 11);
		}
	}

	private static void sendArrowHintToPlayers(Team team, int playerId) {
		team.getPlayers().forEach(x -> x.getPA().drawHeadicon(PlayerAssistant.HintType.PLAYER, playerId, null, 0));
	}

	private static void sendArrowHintToPlayers(Team team, Location location) {
		team.getPlayers().forEach(x -> x.getPA().drawHeadicon(PlayerAssistant.HintType.LOCATION, 0, location, 2));
	}

	public static void sendInterface(Player player) {
		if (player.getCastleWarsState().equals(State.ZAMORAK_LOBBY) || player.getCastleWarsState().equals(State.SARADOMIN_LOBBY)) {
			player.getPA().sendFrame36(380, waitingForPlayers ? 0 : (int) Math.ceil((double) lobbyTimer / 60));
			player.getPA().walkableInterface(11479);
		}

		if (player.getCastleWarsState().equals(State.SARADOMIN_GAME) || player.getCastleWarsState().equals(State.ZAMORAK_GAME)) {

			player.getPA().sendFrame36(380, (int) Math.ceil((double) gameTimer / 60));

			if (player.getCastleWarsState().equals(State.SARADOMIN_GAME)) {

				if (saradominTeam.getRespawnAreaTimes().containsKey(player)) {
					long time = saradominTeam.getRespawnAreaTimes().get(player);
					if (System.currentTimeMillis() - time > 2 * 60 * 1000) {
						CastleWars.removeFromGame(player, false);
						return;
					}
				}

				player.getPA().sendFrame87(378, saradominTeam.getInterfaceConfig(zamorakTeam));
				player.getPA().sendFrame87(377, zamorakTeam.getInterfaceConfig(saradominTeam));

				if (saradominTeam.getRespawnAreaTimes().containsKey(player)) {
					player.getPA().sendFrame126("You have 2 minutes to leave the respawn room.", 12838);
				} else {
					player.getPA().sendFrame126("", 12838);
				}

				player.getPA().walkableInterface(11344);
			} else {

				if (zamorakTeam.getRespawnAreaTimes().containsKey(player)) {
					long time = zamorakTeam.getRespawnAreaTimes().get(player);
					if (System.currentTimeMillis() - time > 2 * 60 * 1000) {
						CastleWars.removeFromGame(player, false);
						return;
					}
				}

				player.getPA().sendFrame87(378, saradominTeam.getInterfaceConfig(zamorakTeam));
				player.getPA().sendFrame87(377, zamorakTeam.getInterfaceConfig(saradominTeam));

				if (zamorakTeam.getRespawnAreaTimes().containsKey(player)) {
					player.getPA().sendFrame126("You have 2 minutes to leave the respawn room.", 12837);
				} else {
					player.getPA().sendFrame126("", 12837);
				}

				player.getPA().walkableInterface(11344);
			}

			player.getPA().showOption(3, 0, "Attack");
		}
	}

	private static void startGame() {
		saradominTeam = new Team();
		zamorakTeam = new Team();


		// zamarak double door
		if (DoorHandler.isDoorOpen(4427, Location.create(2372, 3118, 0))) {
			DoorHandler.handleDoor(null, 4427, Location.create(2372, 3118, 0));
		}

		// saradomin double door
		if (DoorHandler.isDoorOpen(4423, Location.create(2427, 3089, 0))) {
			DoorHandler.handleDoor(null, 4423, Location.create(2427, 3089, 0));
		}

		// Saradomin sidedoor
		if (DoorHandler.isDoorOpen(4465, Location.create(2414, 3073, 0))) {
			DoorHandler.handleDoor(null, 4465, Location.create(2414, 3073, 0));
		}

		// Zamarak side door
		if (DoorHandler.isDoorOpen(4467, Location.create(2384, 3134, 0))) {
			DoorHandler.handleDoor(null, 4467, Location.create(2384, 3134, 0));
		}


		CastleWars.returnFlagToStand(saradominTeam);
		CastleWars.returnFlagToStand(zamorakTeam);

		long time = System.currentTimeMillis();

		for (Player player : zamorakLobbyPlayers) {
			player.getData().minigamesPlayed.add("castlewars");
			player.getPA().movePlayer(Faction.ZAMORAK.getGameLobbyLocation());
			player.setCastleWarsState(State.ZAMORAK_GAME);

			zamorakTeam.getPlayers().add(player);
			zamorakTeam.getRespawnAreaTimes().put(player, time);
		}

		for (Player player : saradominLobbyPlayers) {
			player.getData().minigamesPlayed.add("castlewars");
			player.getPA().movePlayer(Faction.SARADOMIN.getGameLobbyLocation());
			player.setCastleWarsState(State.SARADOMIN_GAME);

			saradominTeam.getPlayers().add(player);
			saradominTeam.getRespawnAreaTimes().put(player, time);
		}

		zamorakLobbyPlayers.clear();
		saradominLobbyPlayers.clear();
		waitingForPlayers = true;
		lobbyTimer = Constants.LOBBY_LENGTH + Constants.GAME_LENGTH;
		gameTimer = Constants.GAME_LENGTH;
		gameRunning = true;
	}

	private static void updateCatapultInterface(Player player, Catapult catapult) {

		int y = catapult.getTargetY() / 3;
		if (player.getCastleWarsState().equals(State.SARADOMIN_GAME)) {
			y = (90 - catapult.getTargetY()) / 3;
		}

		int minus = 0; // not the best way to do it but my minds blank

		if (y <= 9) {
			player.getPA().setMediaModelId(11317, 4863);
		} else if (y >= 10 && y < 20) {
			player.getPA().setMediaModelId(11317, 4864);
			minus = 10;
		} else if (y >= 20 & y < 30) {
			player.getPA().setMediaModelId(11317, 4865);
			minus = 20;
		} else if (y >= 30) {
			player.getPA().setMediaModelId(11317, 4866);
			minus = 30;
		}
		player.getPA().setMediaModelId(11318, 4863 + (y - minus)); // the digits

		int x = catapult.getTargetX() / 3;
		if (player.getCastleWarsState().equals(State.SARADOMIN_GAME)) {
			x = (90 - catapult.getTargetX()) / 3;
		}

		minus = 0;

		if (x <= 9) {
			player.getPA().setMediaModelId(11319, 4863);
		} else if (x >= 10 & x < 20) {
			player.getPA().setMediaModelId(11319, 4864);
			minus = 10;
		} else if (x >= 20 & x < 30) {
			player.getPA().setMediaModelId(11319, 4865);
			minus = 20;
		} else if (x >= 30) {
			player.getPA().setMediaModelId(11319, 4866);
			minus = 30;
		}
		player.getPA().setMediaModelId(11320, 4863 + (x - minus)); // the digits
		player.getPA().setComponentPosition(11332, catapult.getTargetX(), catapult.getTargetY());
	}

	public enum State {
		ZAMORAK_LOBBY, SARADOMIN_LOBBY, ZAMORAK_GAME, SARADOMIN_GAME
	}

}