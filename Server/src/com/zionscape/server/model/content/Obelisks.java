package com.zionscape.server.model.content;

import com.zionscape.server.Server;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.objects.GameObject;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.util.Misc;

public class Obelisks {

	private static final Obelisk[] obelisks = {
			new Obelisk(14831, Location.create(3307, 3916, 0)),
			new Obelisk(14830, Location.create(3219, 3656, 0)),
			new Obelisk(14829, Location.create(3156, 3620, 0)),
			new Obelisk(14828, Location.create(3106, 3794, 0)),
			new Obelisk(14826, Location.create(2980, 3866, 0)),
	};

	static {
		GameCycleTaskHandler.addEvent(null, (GameCycleTaskContainer container) -> {

			for (int i = 0; i < obelisks.length; i++) {
				if (obelisks[i].timer > 0) {
					obelisks[i].timer -= 1;

					Obelisk ob = null;
					while (true) {
						ob = obelisks[Misc.random(obelisks.length - 1)];
						if (ob != obelisks[i]) {
							break;
						}
					}

					if (obelisks[i].timer == 0) {
						for (Player player : PlayerHandler.players) {
							if (player == null) {
								continue;
							}

							Location loc = player.getLocation();
							Location tele = obelisks[i].teleportLocation;

							if (loc.getX() >= (tele.getX() - 2) && loc.getX() <= (tele.getX() + 2) && loc.getY() >= (tele.getY() - 2) && loc.getY() <= (tele.getY() + 2)) {
								player.getPA().startTeleport(ob.teleportLocation.getX(), ob.teleportLocation.getY(), 0);

								Achievements.progressMade(player, Achievements.Types.USE_WILDERNESS_OBELISK);
							}
						}
					}
				}
			}
		}, 1);
	}

	public static boolean onObjectClick(Player player, int objectId, Location objectLocation, int option) {
		for (int i = 0; i < obelisks.length; i++) {
			if (obelisks[i].objectId == objectId) {
				if (obelisks[i].timer == 0) {
					Location tele = obelisks[i].teleportLocation;

					// North east
					Location loc = Location.create(tele.getX() - 2, tele.getY() + 2, 0);
					Server.objectManager.removeObject(loc, 10, false);
					Server.objectManager.addObject(new GameObject(14825, loc.getX(), loc.getY(), loc.getZ(), -1, 10, objectId, 12));

					// North west
					loc = Location.create(tele.getX() + 2, tele.getY() + 2, 0);
					Server.objectManager.removeObject(loc, 10, false);
					Server.objectManager.addObject(new GameObject(14825, loc.getX(), loc.getY(), loc.getZ(), -1, 10, objectId, 12));

					// South west
					loc = Location.create(tele.getX() - 2, tele.getY() - 2, 0);
					Server.objectManager.removeObject(loc, 10, false);
					Server.objectManager.addObject(new GameObject(14825, loc.getX(), loc.getY(), loc.getZ(), -1, 10, objectId, 12));

					// South east
					loc = Location.create(tele.getX() + 2, tele.getY() - 2, 0);
					Server.objectManager.removeObject(loc, 10, false);
					Server.objectManager.addObject(new GameObject(14825, loc.getX(), loc.getY(), loc.getZ(), -1, 10, objectId, 12));

					obelisks[i].timer = 10;
				}
				return true;
			}
		}
		return false;
	}

	private static class Obelisk {

		public int objectId;
		public Location teleportLocation;
		public int timer;

		public Obelisk(int objectId, Location teleportLocation) {
			this.objectId = objectId;
			this.teleportLocation = teleportLocation;
		}

	}


}
