package com.zionscape.server.model.content;

import com.zionscape.server.model.content.minigames.quests.QuestInterfaceGenerator;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InfoTab {

	public static boolean onClickingButtons(Player player, int button) {
		switch (button) {
			case 212148: // online staff members
				List<Player> staff = Arrays.asList(PlayerHandler.players).stream().filter(x -> x != null && x.rights > 0).collect(Collectors.toList());
				QuestInterfaceGenerator qig = new QuestInterfaceGenerator("Online Staff Members");
				if (staff.size() == 0) {
					qig.add("No staff are currently online.");
				} else {
					staff.forEach(x -> qig.add(x.username));
				}
				qig.writeQuest(player);
				return true;
			case 212149: // donate
				player.getPA().sendFrame126("url: www.Draynor.org/shop", 0);
				return true;
			case 212150: // forums
				player.getPA().sendFrame126("url: forums.Draynor.org/", 0);
				return true;
			case 212151: // vote
				player.getPA().sendFrame126("url: www.Draynor.org/vote/", 0);
				return true;
			case 212152: // download client
				player.getPA().sendFrame126("url: www.Draynor.org/play-draynor.jar", 0);
				return true;
			case 212153: // hiscores
				player.getPA().sendFrame126("url: www.Draynor.org/hiscores", 0);
				return true;
			case 212154: // guides
				player.getPA().sendFrame126("url: forums.Draynor.org/", 0);
				return true;
			case 212164: // xp lock
				player.getData().xpLock = !player.getData().xpLock;
				player.getPA().sendFrame126("@or1@EXP Status:@whi@ " + (player.getData().xpLock ? "Locked" : "Unlocked"), 54436);
				return true;
		}
		return false;
	}

	public static void onPlayerLoggedIn(Player player) {
		/*interfaceCache[6116].message = "@or1@[@whi@0@or1@] Vote Points";
		interfaceCache[6117].message = "@or1@[@whi@0@or1@] Slayer Points";
        interfaceCache[6118].message = "@or1@[@whi@0@or1@] Pest Control Points";
        interfaceCache[6119].message = "@or1@[@whi@0@or1@] Pk Points";
        interfaceCache[6120].message = "@or1@[@whi@0@or1@] Kills";
        interfaceCache[6121].message = "@or1@[@whi@0@or1@] Deaths";
        interfaceCache[6122].message = "@or1@Is Muted:@whi@ No";
        interfaceCache[6123].message = "@or1@Is Donator:@whi@ No";
        interfaceCache[6124].message = "@or1@EXP Status:@whi@ Locked";*/


		player.getPA().sendFrame126("@or1@[@whi@" + player.votePoints + "@or1@] Vote Points", 54428);
		player.getPA().sendFrame126("@or1@[@whi@" + player.getData().getSlayerPoints() + "@or1@] Slayer Points", 54429);
		player.getPA().sendFrame126("@or1@[@whi@" + player.pcPoints + "@or1@] Pest Control Points", 54430);
		player.getPA().sendFrame126("@or1@[@whi@" + player.getData().pkPoints + "@or1@] Pk Points", 54431);
		player.getPA().sendFrame126("@or1@[@whi@" + player.kills + "@or1@] Kills", 54432);
		player.getPA().sendFrame126("@or1@[@whi@" + player.deaths + "@or1@] Deaths", 54433);
		player.getPA().sendFrame126("@or1@Is Muted:@whi@ " + (player.muted ? "Yes" : "No"), 54434);
		player.getPA().sendFrame126("@or1@Is Donator:@whi@ " + (player.isMember() ? "Yes" : "No"), 54435);
		player.getPA().sendFrame126("@or1@EXP Status:@whi@ " + (player.getData().xpLock ? "Locked" : "Unlocked"), 54436);
	}


}
