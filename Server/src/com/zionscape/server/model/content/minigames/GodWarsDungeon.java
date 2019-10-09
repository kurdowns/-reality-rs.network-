package com.zionscape.server.model.content.minigames;

import com.zionscape.server.Server;
import com.zionscape.server.model.Area;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.tick.Tick;

/**
 * Created with IntelliJ IDEA. User: Administrator Date: 11/14/13 Time: 11:32 PM To change this template use File |
 * Settings | File Templates.
 */
public final class GodWarsDungeon {

	public static boolean isObject(Player client, int objectId, int objectX, int objectY) {
		switch (objectId) {
			case 26384:
				if (client.getActualLevel(PlayerConstants.STRENGTH) < 70) {
					client.sendMessage("Level 70 strength is required to tackle this object.");
					return true;
				}
				bangDoor(client, objectX, objectY);
				return true;
			case 26439:
				if (client.getActualLevel(PlayerConstants.HITPOINTS) < 70) {
					client.sendMessage("Level 70 hitpoints is required to tackle this object.");
					return true;
				}
				crossIceBridge(client);
				return true;
			case 26444:
			case 26445:
			case 1996:
				if (objectId == 26444 && client.getActualLevel(PlayerConstants.AGILITY) < 70) {
					client.sendMessage("Level 70 agility is required to tackle this object.");
					return true;
				}
				climbRock(client, objectId);
				return true;
			case 26303:
				if (client.getActualLevel(PlayerConstants.RANGED) < 70) {
					client.sendMessage("Level 70 ranged is required to tackle this object.");
					return true;
				}
				grappleHook(client, objectX, objectY);
				return true;
			case 26425:
				if (client.getX() <= objectX) {
					checkBossDoor(client, objectX, objectY, client.bandosKills, 1, 0, GodType.BANDOS);
				} else {
					client.sendMessage("The door appears to be locked...");
				}
				return true;
			case 26426:
				if (client.getY() <= objectY) {
					checkBossDoor(client, objectX, objectY, client.armadylKills, 0, 1, GodType.ARMADYL);
				} else {
					client.sendMessage("The door appears to be locked...");
				}
				return true;
			case 26427:
				if (client.getX() >= objectX) {
					checkBossDoor(client, objectX, objectY, client.saradominKills, -1, 0, GodType.SARADOMIN);
				} else {
					client.sendMessage("The door appears to be locked...");
				}
				return true;
			case 26428:
				if (client.getY() >= objectY) {
					checkBossDoor(client, objectX, objectY, client.zamorakKills, 0, -1, GodType.ZAMAROK);
				} else {
					client.sendMessage("The door appears to be locked...");
				}
				return true;
		}
		return false;
	}

	private static void checkBossDoor(Player player, int objectX, int objectY, int killcount, int walkToX, int walkToY, GodType type) {
		if (killcount < (player.isExtremeDonator() ? 5 : 10) && !player.isLegendaryDonator()) {
			player.sendMessage("You need a killcount of 10 to enter this door.");
			return;
		}

		if (type == GodType.ZAMAROK) {
			player.zamorakKills = 0;
			player.getPA().movePlayer(2925, 5331);
		} else if (type == GodType.SARADOMIN) {
			player.saradominKills = 0;
			player.getPA().movePlayer(2907, 5265);
		} else if (type == GodType.BANDOS) {
			player.bandosKills = 0;
			player.getPA().movePlayer(2864, 5354);
		} else if (type == GodType.ARMADYL) {
			player.armadylKills = 0;
			player.getPA().movePlayer(2839, 5296);
		}

		Achievements.progressMade(player, Achievements.Types.ENTER_A_GWD_CHAMBER);
	}

	public static void resetKillcount(Player player) {
		player.armadylKills = 0;
		player.bandosKills = 0;
		player.saradominKills = 0;
		player.zamorakKills = 0;
	}

	private static void bangDoor(Player client, int objectX, int objectY) {
		if (client.level[PlayerConstants.STRENGTH] < 70) {
			client.sendMessage("You need a strength level of 70 to open this door.");
			return;
		}
		if (client.getItems().playerHasItem(2347)) {
			client.startAnimation(7002);
			final int x = client.absX >= objectX ? -1 : 1;
			client.getPA().walkTo(x, 0);
		} else {
			client.sendMessage("You need a hammer to bang this door.");
		}
	}

	private static void crossIceBridge(final Player client) {
		if (client.animationBusy) {
			return;
		}
		if (client.level[3] < 70) {
			client.sendMessage("You need at least 700 life points to cross this river!");
			return;
		}
		client.animationBusy = true;
		final int y = client.getY() == 5332 ? 5345 : 5332;
		client.getPA().showInterface(8677);
		Server.getTickManager().submit(new Tick(2) {

			@Override
			public void execute() {
				client.getPA().movePlayer(2885, y, 2);
				client.getPA().removeAllWindows();
				client.animationBusy = false;
				stop();
			}
		});
	}

	private static void climbRock(Player client, int objectId) {
		if (client.level[PlayerConstants.AGILITY] < 70) {
			client.sendMessage("You need an agility level of 70 to pass this.");
			return;
		}
		final int x = objectId == 26444 ? 2915 : 2920;
		final int y = objectId == 26444 ? 5300 : 5274;
		final int z = objectId == 26444 ? 1 : 0;
		client.startAnimation(827);
		client.getPA().movePlayer(x, y, z);
	}

	private static void grappleHook(final Player client, final int objectX, final int objectY) {
		if (client.level[PlayerConstants.RANGED] < 70) {
			client.sendMessage("You need a ranged level of 70 to cross this obstacle.");
			return;
		}
		if (client.equipment[Player.playerWeapon] != 9185 || client.equipment[13] != 9419) {
			client.sendMessage("You need to equip a rune crossbow and mith grapple to cross this obstacle.");
			return;
		}
		if (client.animationBusy) {
			return;
		}
		client.animationBusy = true;
		client.gfx0(1036);
		client.startAnimation(7081);
		int offX = (client.getY() - objectY) * -1;
		int offY = (client.getX() - objectX) * -1;
		client.getPA().createPlayersProjectile(client.getX(), client.getY(), offX, offY, 50, 78, 760, 0, 0, -1, 50);
		Server.getTickManager().submit(new Tick(4) {

			@Override
			public void execute() {
				final int y = client.getY() > objectY ? -10 : 10;
				client.getPA().movePlayer(client.getX(), client.getY() + y);
				client.animationBusy = false;
				stop();
			}
		});
	}

	private static boolean inZamorakBossRoom(Location location) {
		return new Area(2918, 5318, 2936, 5335, 2).inArea(location);
	}

	private static boolean inSaradominBossRoom(Location location) {
		return new Area(2890, 5258, 2907, 5272, 0).inArea(location);
	}

	private static boolean inBandosBossRoom(Location location) {
		return new Area(2864, 5351, 2876, 5375, 2).inArea(location);
	}

	private static boolean inArmadylBossRoom(Location location) {
		return new Area(2820, 5296, 2842, 5308, 2).inArea(location);
	}

	public static boolean inBossRoom(Location location) {
		return inZamorakBossRoom(location) || inSaradominBossRoom(location) || inBandosBossRoom(location) || inArmadylBossRoom(location);
	}

	private enum GodType {
		BANDOS, ARMADYL, SARADOMIN, ZAMAROK
	}
}
