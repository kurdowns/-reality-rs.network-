package com.zionscape.server.world;

import com.zionscape.server.Server;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.tick.Tick;

public class LadderHandler {

	private static void handleLadder(final Player player, final int height, final boolean sameArea) {

		if (player.getCooldowns().exists("ladder")) {
			return;
		}

		player.startAnimation(828);
		player.getCooldowns().add("ladder", 3);
		Server.getTickManager().submit(new Tick(2) {

			@Override
			public void execute() {
				if (sameArea) {
					player.getPA().movePlayer(player.absX, player.absY, player.heightLevel + height);
				} else {
					player.getPA().movePlayer(player.absX,
							(player.absY > 6400 ? (player.absY - 6400) : (player.absY + 6400)),
							player.heightLevel + height);
				}
				stop();
			}
		});
	}

	public static boolean onFirstClickObject(Player player, int object, Location location) {
		switch (object) {
			case 32015:
				// kbd ladder by spiders
				if (location.equals(3069, 10256)) {
					player.getPA().movePlayer(3016, 3849, 0);
					return true;
				}

			case 1754:
			case 1757:
			case 1755:
			case 1568:
			case 1759:
			case 26933:
			case 26934:
			case 20987:
			case 416:
			case 30942:
			case 3926:
			case 3878:
			case 3925:
			case 3877:
			case 3923:
			case 3876:
			case 36693:
			case 36694:
			case 36646:
			case 36645:
			case 36691:
			case 36644:
				handleLadder(player, 0, false);
				return true;
			case 1747:
			case 25938:
			case 26118:
			case 6280:
			case 6281:
			case 3595:
			case 3579:
			case 36363:
			case 36347:
				handleLadder(player, 1, true);
				return true;
			case 4472:
			case 1746:
			case 4911:
			case 25939:
			case 4471:
			case 3581:
			case 3597:
			case 36365:
			case 36349:
				handleLadder(player, -1, true);
				return true;
		}
		return false;
	}

	@SuppressWarnings("unused")
	private enum Move {
		UP, DOWN, SAME_AREA
	}

}
