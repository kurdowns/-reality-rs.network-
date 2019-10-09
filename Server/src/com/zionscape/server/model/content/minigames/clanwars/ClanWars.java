package com.zionscape.server.model.content.minigames.clanwars;

import com.zionscape.server.model.Area;
import com.zionscape.server.model.Entity;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.clan.Clan;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerAssistant;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.net.packets.Walking;

public class ClanWars {

	public static boolean onAttackPlayer(Player player, Player target) {
		if (ClanWars.inArena(player)) {
			War war = (War) player.getAttribute("clan_war");
			if (war != null) {
				if (war.getStartTimer() > 0) {
					player.sendMessage("The war has not begun yet.");
					return true;
				}
				if (war.getNorthTeam().contains(player) && war.getNorthTeam().contains(target) || war.getSouthTeam().contains(player) && war.getSouthTeam().contains(target)) {
					player.sendMessage("You cannot attack your own team mates.");
					return true;
				}
			}
		}
		if (ClanWars.inLobbyArea(player) && ClanWars.inLobbyArea(target)) {
			if (!player.getLocation().isWithinInteractionDistance(target.getLocation())) {
				player.setAttribute("walk_to_challenger", target);
				return true;
			}
			if (player.clan == null) {
				player.sendMessage("You're not in a clan.");
				return true;
			}
			if (target.clan == null) {
				player.sendMessage(target.username + " is not in a clan.");
				return true;
			}
			if (!player.clan.isFounder(player.username)) {
				player.sendMessage("You must be the owner of your clan to challenge others.");
				return true;
			}
			if (!target.clan.isFounder(target.username)) {
				player.sendMessage(target.username + " is not the owner of his clan.");
				return true;
			}

			if (target.getAttribute("pending_challenger") != null || target.inTrade || target.getAttribute("clan_war") != null) {
				player.sendMessage("The other player is currently busy.");
				return true;
			}

			player.setAttribute("requesting_challenger", target);
			player.sendMessage("Sending challenge request.");
			target.getPA().sendFrame126(player.username + " is inviting your clan to a battle.", 9002);
			target.getPA().showInterface(9000);
			target.setAttribute("pending_challenger", player);
			return true;
		}
		return false;
	}

	public static void onPlayerProcess(Player player) {
		if (player.getAttribute("walk_to_challenger") != null) {
			Player other = (Player) player.getAttribute("walk_to_challenger");
			if (other != null && player.getLocation().isViewableFrom(other.getLocation())) {
				if (player.getLocation().isWithinInteractionDistance(other.getLocation())) {
					player.followId = -1;
					player.setAttribute("walk_to_challenger", null);

					ClanWars.onAttackPlayer(player, other);
				} else {
					player.followId = other.getId();
				}
			} else {
				player.setAttribute("walk_to_challenger", null);
			}
		}
	}

	private static void sendClanWarOptions(Player player) {
		War war = (War) player.getAttribute("clan_war");

		if (war == null) {
			return;
		}

		war.getSouthTeam().setAccepted(false);
		war.getNorthTeam().setAccepted(false);
		war.getSouthTeam().getOwner().getPA().sendFrame126("", 29037);
		war.getNorthTeam().getOwner().getPA().sendFrame126("", 29037);

		// allowed stuff
		for (int i = 700; i <= 706; i++) {
			player.getPA().sendFrame36(i, war.getCombatRules()[i - 700] ? 1 : 0);
		}

		// maps
		for (int i = 707; i <= 711; i++) {
			player.getPA().sendFrame36(i, i == war.getMap().getConfig() ? 1 : 0);
		}

		// time
		int timeConfig = getTimeConfig(war.getTimeLimit());
		for (int i = 712; i <= 720; i++) {
			player.getPA().sendFrame36(i, i == timeConfig ? 1 : 0);
		}

		// knock out
		player.getPA().sendFrame36(721, war.knockOut() ? 1 : 0);

		// kills
		int killsConfig = getKillsConfig(war.getKillsRequired());
		for (int i = 722; i <= 728; i++) {
			player.getPA().sendFrame36(i, i != killsConfig ? 0 : 1);
		}

		// item loosing
		player.getPA().sendFrame126(war.looseItems() ? "... You loose" : "... You keep", 29032);

		// stragglers
		if (war.getStragglers().equals(Stragglers.IGNORE_5)) {
			player.getPA().sendFrame36(729, 0);
			player.getPA().sendFrame36(730, 1);
		} else {
			player.getPA().sendFrame36(729, 1);
			player.getPA().sendFrame36(730, 0);
		}
	}

	public static boolean onClickingStuff(Player player) {
		War war = (War) player.getAttribute("clan_war");

		if (war != null) {
			if (war.isSettingUp()) {
				Player other = war.getSouthTeam().contains(player) ? war.getNorthTeam().getOwner() : war.getSouthTeam().getOwner();
				player.setAttribute("pending_challenger", null);
				player.setAttribute("clan_war", null);
				player.getPA().closeAllWindows();

				other.setAttribute("pending_challenger", null);
				other.setAttribute("clan_war", null);
				other.getPA().closeAllWindows();
				other.sendMessage("The other player has closed the challenge.");
				return true;
			}
		}
		return false;
	}

	public static void onPlayerLoggedIn(Player player) {
		if (inArena(player) || inJail(player)) {
			player.getPA().movePlayer(Constants.LOBBY_SPAWN_LOCATION);
		}
	}

	public static void onPlayerLoggedOut(Player player) {
		War war = (War) player.getAttribute("clan_war");

		if (war != null) {
			if (war.isSettingUp()) {
				Player other = war.getSouthTeam().contains(player) ? war.getNorthTeam().getOwner() : war.getSouthTeam().getOwner();

				other.setAttribute("pending_challenger", null);
				other.setAttribute("clan_war", null);
				other.getPA().closeAllWindows();
				other.sendMessage("The other player has disconnected.");
			} else {
				removePlayer(player);
				player.absX = Constants.LOBBY_SPAWN_LOCATION.getX();
				player.absY = Constants.LOBBY_SPAWN_LOCATION.getY();
				player.heightLevel = Constants.LOBBY_SPAWN_LOCATION.getZ();
			}
		}
	}

	public static boolean onClickingButtons(Player player, int button) {
		War war = (War) player.getAttribute("clan_war");
		switch (button) {
			case 113107: // accept
				if (war == null || !war.isSettingUp() || !war.getSouthTeam().getOwner().equals(player) && !war.getNorthTeam().getOwner().equals(player)) {
					return true;
				}
				if (war.getSouthTeam().getOwner().equals(player)) {
					war.getSouthTeam().setAccepted(true);
				} else {
					war.getNorthTeam().setAccepted(true);
				}
				if (war.getNorthTeam().isAccepted() && war.getSouthTeam().isAccepted()) {
					Location loc1 = war.getMap().getSouthStartingLocation();
					war.getSouthTeam().getOwner().getPA().movePlayer(loc1.getX(), loc1.getY(), war.getHeight());

					Location loc2 = war.getMap().getNorthStartingLocation();
					war.getNorthTeam().getOwner().getPA().movePlayer(loc2.getX(), loc2.getY(), war.getHeight());

					war.getSouthTeam().getOwner().getPA().closeAllWindows();
					war.getNorthTeam().getOwner().getPA().closeAllWindows();

					war.getNorthTeam().getClan().setWar(war);
					war.getSouthTeam().getClan().setWar(war);

					war.setSettingUp(false);

					for (Player plr : war.getNorthTeam().getClan().activeMembers) {
						if (plr != null) {
							plr.getData().minigamesPlayed.add("clanwars");
							plr.sendMessage("@red@A clan war has been started. Enter the Purple portal at Clan wars to join");
						}
					}
					for (Player plr : war.getSouthTeam().getClan().activeMembers) {
						if (plr != null) {
							plr.getData().minigamesPlayed.add("clanwars");
							plr.sendMessage("@red@A clan war has been started. Enter the Purple portal at Clan wars to join.");
						}
					}

					//war.getNorthTeam().getClan().sendMessage("A clan war has been started. Enter the Purple portal at Clan wars to join.");
					//war.getSouthTeam().getClan().sendMessage("A clan war has been started. Enter the Purple portal at Clan wars to join.");
				} else {
					Player other = war.getSouthTeam().contains(player) ? war.getNorthTeam().getOwner() : war.getSouthTeam().getOwner();
					other.getPA().sendFrame126("Other accepted", 29037);
					player.getPA().sendFrame126("Waiting...", 29037);
				}
				return true;
			case 113243: // classic
			case 113247: // plateau
			case 113251: // turrets
			case 113255: // querry
			case 114003: // forrest
				if (war == null || !war.isSettingUp() || !war.getSouthTeam().getOwner().equals(player) && !war.getNorthTeam().getOwner().equals(player)) {
					return true;
				}
				Maps map = null;
				switch (button) {
					case 113243: // classic
						map = Maps.CLASSIC;
						break;
					case 113247: // plateau
						map = Maps.PLATEAU;
						break;
					case 113251: // turrets
						map = Maps.TURRETS;
						break;
					case 113255: // querry
						map = Maps.QUERRY;
						break;
					case 114003: // forrest
						map = Maps.FOREST;
						break;
				}
				war.setMap(map);
				sendClanWarOptions(war.getSouthTeam().getOwner());
				sendClanWarOptions(war.getNorthTeam().getOwner());
				return true;
			case 113203: // time left
			case 113205:
			case 113207:
			case 113209:
			case 113211:
			case 113213:
			case 113215:
			case 113217:
			case 113095:
				if (war == null || !war.isSettingUp() || !war.getSouthTeam().getOwner().equals(player) && !war.getNorthTeam().getOwner().equals(player)) {
					return true;
				}

				int timeLeft = 0;

				switch (button) {
					case 113203:
						timeLeft = 300;
						break;
					case 113205:
						timeLeft = 600;
						break;
					case 113207:
						timeLeft = 1200;
						break;
					case 113209:
						timeLeft = 1800;
						break;
					case 113211:
						timeLeft = 2700;
						break;
					case 113213:
						timeLeft = 3600;
						break;
					case 113215:
						timeLeft = 5400;
						break;
					case 113217:
						timeLeft = 7200;
						break;
					case 113095:
						timeLeft = -1;
						break;
				}

				war.setTimeLimit(timeLeft);
				sendClanWarOptions(war.getSouthTeam().getOwner());
				sendClanWarOptions(war.getNorthTeam().getOwner());
				return true;
			case 113103: // loose items
				if (war == null || !war.isSettingUp() || !war.getSouthTeam().getOwner().equals(player) && !war.getNorthTeam().getOwner().equals(player)) {
					return true;
				}
				war.setLooseItems(!war.looseItems());
				sendClanWarOptions(war.getSouthTeam().getOwner());
				sendClanWarOptions(war.getNorthTeam().getOwner());
				return true;
			case 113173: // kills
			case 113175:
			case 113177:
			case 113179:
			case 113181:
			case 113183:
			case 113097:
				if (war == null || !war.isSettingUp() || !war.getSouthTeam().getOwner().equals(player) && !war.getNorthTeam().getOwner().equals(player)) {
					return true;
				}
				int kills = 0;
				switch (button) {
					case 113173: // kills
						kills = 5;
						break;
					case 113175:
						kills = 10;
						break;
					case 113177:
						kills = 25;
						break;
					case 113179:
						kills = 50;
						break;
					case 113181:
						kills = 200;
						break;
					case 113183:
						kills = 500;
						break;
					case 113097:
						kills = -1;
						break;
				}
				war.setKillsRequired(kills);
				sendClanWarOptions(war.getSouthTeam().getOwner());
				sendClanWarOptions(war.getNorthTeam().getOwner());
				return true;
			case 113099: // kill'em all
			case 113101: // stragglers
				if (war == null || !war.isSettingUp() || !war.getSouthTeam().getOwner().equals(player) && !war.getNorthTeam().getOwner().equals(player)) {
					return true;
				}
				if (button == 113099) {
					if (war.getStragglers().equals(Stragglers.IGNORE_5)) {
						war.setStragglers(Stragglers.KILL_ALL);
					}
				}
				if (button == 113101) {
					if (war.getStragglers().equals(Stragglers.KILL_ALL)) {
						war.setStragglers(Stragglers.IGNORE_5);
					}
				}
				sendClanWarOptions(war.getSouthTeam().getOwner());
				sendClanWarOptions(war.getNorthTeam().getOwner());
				return true;
			case 113091: // knock outs
				if (war == null || !war.isSettingUp() || !war.getSouthTeam().getOwner().equals(player) && !war.getNorthTeam().getOwner().equals(player)) {
					return true;
				}
				war.setKnockOut(!war.knockOut());
				sendClanWarOptions(war.getSouthTeam().getOwner());
				sendClanWarOptions(war.getNorthTeam().getOwner());
				return true;
			case 113084: // combat rules
			case 113085:
			case 113086:
			case 113087:
			case 113088:
			case 113089:
			case 113090:
				if (war == null || !war.isSettingUp() || !war.getSouthTeam().getOwner().equals(player) && !war.getNorthTeam().getOwner().equals(player)) {
					return true;
				}
				war.getCombatRules()[button - 113084] = !war.getCombatRules()[button - 113084];
				sendClanWarOptions(war.getSouthTeam().getOwner());
				sendClanWarOptions(war.getNorthTeam().getOwner());
				return true;
			case 35043: // accept request
				if (player.getAttribute("pending_challenger") == null) {
					player.sendMessage("This challenge request is no longer valid.");
					return true;
				}

				Player challenger = (Player) player.getAttribute("pending_challenger");
				if (challenger == null || challenger.getAttribute("requesting_challenger") == null || challenger.getAttribute("requesting_challenger") != player) {
					player.sendMessage("This challenge request is no longer valid.");
					return true;
				}

				player.setAttribute("pending_challenger", null);
				challenger.setAttribute("pending_challenger", null);
				player.setAttribute("requesting_challenger", null);
				challenger.setAttribute("requesting_challenger", null);

				war = new War(player, challenger);
				player.setAttribute("clan_war", war);
				challenger.setAttribute("clan_war", war);

				sendClanWarOptions(player);
				player.getPA().sendFrame126("Clan Wars Options: Challenging " + challenger.username, 29034);
				player.getPA().sendFrame126("", 29037);
				player.getPA().showInterface(29000);

				sendClanWarOptions(challenger);
				challenger.getPA().sendFrame126("Clan Wars Options: Challenging " + player.username, 29034);
				challenger.getPA().sendFrame126("", 29037);
				challenger.getPA().showInterface(29000);

				PlayerAssistant.resetPlayer(player);
				PlayerAssistant.resetPlayer(challenger);

				war.startTimer();
				return true;
			case 35047: // decline request
				challenger = (Player) player.getAttribute("pending_challenger");
				if (challenger != null) {
					challenger.sendMessage(player.username + " has declined your challenge.");
					challenger.setAttribute("requesting_challenger", null);
				}

				player.getPA().closeAllWindows();
				player.setAttribute("pending_challenger", null);
				return true;
		}
		return false;
	}

	public static boolean onPlayerWalk(Player player, Walking.WalkingType type) {
		player.setAttribute("pending_challenge", null);
		player.setAttribute("requesting_challenge", null);
		if (type != Walking.WalkingType.WALKING_TO) {
			player.setAttribute("walk_to_challenger", null);
		}

		War war = (War) player.getAttribute("clan_war");
		if (war != null) {
			if (war.isSettingUp()) {
				Player other = war.getSouthTeam().contains(player) ? war.getNorthTeam().getOwner() : war.getSouthTeam().getOwner();
				player.setAttribute("clan_War", null);
				player.getPA().closeAllWindows();

				other.setAttribute("clan_war", null);
				other.getPA().closeAllWindows();
				other.sendMessage("The other player has closed the challenge.");
				return true;
			}
		}
		return false;
	}

	public static boolean onObjectClick(Player player, int objectId, Location objectLocation, int option) {
		switch (objectId) {
			case 38699:
				player.sendMessage("This portal is not in service. Please use the wilderness for dangerous pking.");
				return true;
			case 38698: // white portal
				player.getPA().movePlayer(Constants.WHITE_PORTAL_SPAWN);
				return true;
			case 28214: // arena exits
			case 38694:
			case 38695:
			case 38696:
			case 28139:
			case 28696:
				ClanWars.removePlayer(player);
				player.getPA().movePlayer(Constants.LOBBY_SPAWN_LOCATION);
				return true;
			case 28140: // jail exits
			case 38697:
			case 38700: // white leave
				player.getPA().movePlayer(Constants.LOBBY_SPAWN_LOCATION);
				return true;
			case 28213: // join purple portal
				if (player.clan == null) {
					player.sendMessage("You're not in a clan.");
					return true;
				}

				if (player.clan.getWar() == null) {
					player.sendMessage("There is not a clan war in progress.");
					return true;
				}

				War war = player.clan.getWar();

				if (war.getRemovedUsernames().contains(player.username)) {
					player.sendMessage("You have already participated in this clan war.");
					return true;
				}

				if (war.knockOut() && war.isStarted()) {
					player.sendMessage("The clan war has already begun and does not allow walk-Ins.");
					return true;
				}

				PlayerAssistant.resetPlayer(player);
				if (player.clan.equals(war.getSouthTeam().getClan())) {
					Location location = war.getMap().getSouthStartingLocation();
					player.getPA().movePlayer(location.getX(), location.getY(), war.getHeight());
					war.getSouthTeam().getPlayers().add(player);
				} else {
					Location location = war.getMap().getNorthStartingLocation();
					player.getPA().movePlayer(location.getX(), location.getY(), war.getHeight());
					war.getNorthTeam().getPlayers().add(player);
				}
				player.setAttribute("clan_war", war);
				return true;
		}
		return false;
	}

	public static boolean onTeleport(Player player) {
		if (inArena(player)) {
			player.sendMessage("Please use the portal to exit the arena.");
			return true;
		}
		return false;
	}

	public static boolean inLobbyArea(Player player) {
		return Area.inArea(Constants.LOBBY_AREA, player);
	}

	public static boolean inArena(Entity e) {
		if (Area.inArea(Constants.WHITE_PORTAL_AREA, e)) {
			return true;
		}
		for (Maps map : Maps.values()) {
			if (Area.inArea(map.getMapArea(), e)) {
				return true;
			}
		}
		return false;
	}

	public static boolean inJail(Player player) {
		if (player.getX() >= 2689 && player.getX() <= 2691 && player.getY() >= 5505 && player.getY() <= 5566) {
			return true;
		}
		if (player.getX() >= 2689 && player.getX() <= 2750 && player.getY() >= 5505 && player.getY() <= 5507) {
			return true;
		}
		if (player.getX() >= 2746 && player.getX() <= 0 && player.getY() >= 5505 && player.getY() <= 0) {
			return true;
		}
		if (player.getX() >= 2689 && player.getX() <= 2691 && player.getY() >= 5569 && player.getY() <= 5630) {
			return true;
		}
		if (player.getX() >= 2689 && player.getX() <= 2750 && player.getY() >= 5628 && player.getY() <= 5630) {
			return true;
		}
		if (player.getX() >= 2748 && player.getX() <= 2750 && player.getY() >= 5569 && player.getY() <= 5630) {
			return true;
		}
		for (Maps map : Maps.values()) {
			if (map.equals(Maps.TURRETS)) {
				continue;
			}
			if (Area.inArea(map.getNorthJailArea(), player) || Area.inArea(map.getSouthJailArea(), player)) {
				return true;
			}
		}
		return false;
	}

	public static boolean inWhitePortalArea(Player player) {
		return Area.inArea(Constants.WHITE_PORTAL_AREA, player);
	}

	private static int getTimeConfig(int time) {
		switch (time) {
			case 300:
				return 712;
			case 600:
				return 713;
			case 1200:
				return 714;
			case 1800:
				return 715;
			case 2700:
				return 716;
			case 3600:
				return 717;
			case 5400:
				return 718;
			case 7200:
				return 719;
		}
		return 720;
	}

	private static int getKillsConfig(int kills) {
		switch (kills) {
			case 5:
				return 722;
			case 10:
				return 723;
			case 25:
				return 724;
			case 50:
				return 725;
			case 200:
				return 726;
			case 500:
				return 727;
		}
		return 728;
	}

	private static void removePlayer(Player player) {
		PlayerAssistant.resetPlayer(player);

		War war = (War) player.getAttribute("clan_war");
		if (war == null) {
			return;
		}

		if (war.getSouthTeam().contains(player)) {
			war.getSouthTeam().getPlayers().remove(player);
		}
		if (war.getNorthTeam().contains(player)) {
			war.getNorthTeam().getPlayers().remove(player);
		}

		player.setAttribute("clan_war", null);
		war.getRemovedUsernames().add(player.username);

		checkForEndGame(war);
	}

	private static void checkForEndGame(War war) {
		if (war.getNorthTeam().getPlayers().size() == 0 || war.getSouthTeam().getPlayers().size() == 0) {
			endGame(war);
			return;
		}
		if (war.getKillsRequired() > -1) {
			if (war.getNorthTeam().getKills() >= war.getKillsRequired() || war.getSouthTeam().getKills() > war.getKillsRequired()) {
				endGame(war);
			}
		}
	}

	public static void endGame(War war) {

		WarTeam winningTeam = null;

		if (war.getNorthTeam().getPlayers().size() == 0) {
			winningTeam = war.getSouthTeam();
		} else if (war.getSouthTeam().getPlayers().size() == 0) {
			winningTeam = war.getNorthTeam();
		} else if (war.getNorthTeam().getKills() > war.getSouthTeam().getKills()) {
			winningTeam = war.getNorthTeam();
		} else if (war.getSouthTeam().getKills() > war.getNorthTeam().getKills()) {
			winningTeam = war.getSouthTeam();
		}

		for (Player player : PlayerHandler.players) {
			if (player == null) {
				continue;
			}
			if ((inArena(player) || inJail(player)) && player.heightLevel == war.getHeight()) {

				Clan oppositeClan = player.clan == war.getSouthTeam().getClan() ? war.getNorthTeam().getClan() : war.getSouthTeam().getClan();

				if (winningTeam == null) {
					player.sendMessage("You have drawn against clan " + oppositeClan.title + ".");
				} else {
					if (player.clan.equals(winningTeam.getClan())) {

						Achievements.progressMade(player, Achievements.Types.WIN_50_CLAN_BATTLES);

						player.sendMessage("Congratulations you won against clan " + oppositeClan.title + ".");
					} else {
						player.sendMessage("Oh dear. You lost against clan " + oppositeClan.title + ".");
					}
				}

				player.setAttribute("clan_war", null);
				PlayerAssistant.resetPlayer(player);
				player.getPA().movePlayer(Constants.LOBBY_SPAWN_LOCATION);
			}
		}

		war.getNorthTeam().getClan().setWar(null);
		war.getSouthTeam().getClan().setWar(null);
	}

	public static void playerDied(Player player) {
		if (Area.inArea(Constants.WHITE_PORTAL_AREA, player)) {
			player.getPA().movePlayer(Constants.LOBBY_SPAWN_LOCATION);
		}

		War war = (War) player.getAttribute("clan_war");
		if (war == null) {
			return;
		}

		if (war.getSouthTeam().contains(player)) {
			Location loc = war.getMap().getSouthJailLocation();
			player.getPA().movePlayer(loc.getX(), loc.getY(), war.getHeight());
			war.getNorthTeam().incrementKills();
		}
		if (war.getNorthTeam().contains(player)) {
			Location loc = war.getMap().getNorthJailLocation();
			player.getPA().movePlayer(loc.getX(), loc.getY(), war.getHeight());
			war.getSouthTeam().incrementKills();
		}

		ClanWars.removePlayer(player);
		checkForEndGame(war);
	}

	public static void sendInterface(Player player) {
		if (player.clan == null || player.clan.getWar() == null) {
			return;
		}
		War war = player.clan.getWar();

		if (war.getSouthTeam().getClan().equals(player.clan)) {
			player.getPA().sendFrame126(war.getSouthTeam().getPlayers().size() + "", 28550);
			player.getPA().sendFrame126(war.getNorthTeam().getPlayers().size() + "", 28551);
		} else {
			player.getPA().sendFrame126(war.getNorthTeam().getPlayers().size() + "", 28550);
			player.getPA().sendFrame126(war.getSouthTeam().getPlayers().size() + "", 28551);
		}

		if (war.getStartTimer() > 0) {
			int minutes = war.getStartTimer() / 60;
			int seconds = war.getStartTimer() - (minutes * 60);
			player.getPA().sendFrame126("Countdown to battle:", 28552);
			player.getPA().sendFrame126(minutes + "m " + seconds + "s", 28553);
		} else {
			player.getPA().sendFrame126("Countdown till end:", 28552);

			if (war.getGameTimer() > 0) {
				int minutes = war.getGameTimer() / 60;
				int seconds = war.getGameTimer() - (minutes * 60);
				player.getPA().sendFrame126(minutes + "m " + seconds + "s", 28553);
			} else {
				player.getPA().sendFrame126("No time limit", 28553);
			}
		}

		if (war.looseItems()) {
			player.getPA().sendFrame126("@red@Lost", 28560);
		} else {
			player.getPA().sendFrame126("@gre@Kept", 28560);
		}

		player.getPA().walkableInterface(28540);
	}


}