package com.zionscape.server.model.players;

import com.zionscape.server.model.Area;
import com.zionscape.server.model.InstancesArea;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.minigames.gambling.Gambling;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.npcs.combat.kraken.Kraken;
import com.zionscape.server.model.npcs.combat.zulrah.Zulrah;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TeleportInterface {

	private static Map<Integer, Location> teleports = new HashMap<>();

	static {

		//training
		teleports.put(208178, Location.create(2781, 10071, 0)); // beginners area
		teleports.put(208182, Location.create(3173, 2981, 0)); // bandits
		teleports.put(208186, Location.create(2460, 5159, 0)); // tzhaar
		teleports.put(208190, Location.create(3012, 3500, 0)); // ice mountain
		teleports.put(208194, Location.create(2828, 3518, 0)); // white wolf
		teleports.put(208198, Location.create(2491, 3086, 0)); // ogres
		teleports.put(208202, Location.create(2763, 2784, 0)); // ape atoll
		teleports.put(208206, Location.create(2896, 3437, 0)); // druids
		teleports.put(208212, Location.create(3556, 9945, 0)); // experiments
		teleports.put(208216, Location.create(2860, 9572, 0)); // karamja dung
		teleports.put(208221, Location.create(3097, 9868, 0)); // edgeville dung
		teleports.put(208226, Location.create(2884, 9798, 0)); // tav dung
		teleports.put(208231, Location.create(2713, 9564, 0)); // brimhaven dung
		teleports.put(208236, Location.create(1763, 5365, 1)); // ancient cavern
		teleports.put(208240, Location.create(3242, 9868, 0)); // varrock sewers

		//slayer
		teleports.put(209082, Location.create(2807, 10003, 0)); // relleka dung
		teleports.put(209087, Location.create(3424, 3538, 0)); // slayer tower
		teleports.put(209091, Location.create(3145, 5555, 0)); // chaos tunnels
		teleports.put(209095, Location.create(3283, 4322, 0)); // pollnivneach dung
		teleports.put(209100, Location.create(3015, 9258, 0)); // jadinko lair
		teleports.put(209104, Location.create(3048, 9582, 0)); // asgarnia ice dungeon
		teleports.put(209109, Location.create(1633, 5281, 0)); // kuradel dungeon


		//skilling
		teleports.put(209203, Location.create(3044, 9778, 0)); // mining
		teleports.put(209207, Location.create(2604, 3401, 0)); // fishing
		teleports.put(209215, Location.create(2816, 3464, 0)); // farming
		teleports.put(209219, Location.create(2910, 4833, 0)); // rune essence
		teleports.put(209223, Location.create(2207, 5345, 0)); // summoning
		teleports.put(209227, Location.create(2726, 3481, 0)); // woodcutting


		// pking
		teleports.put(210083, Location.create(3088, 3503, 0)); // edgeville
		teleports.put(210087, Location.create(2539, 4715, 0)); // mage bank
		teleports.put(210091, Location.create(3355, 3680, 0)); // green drags
		teleports.put(210095, Location.create(3051, 10132, 0)); // revenants

		// minigames
		teleports.put(210200, Location.create(2659, 2659, 0)); // pest control
		teleports.put(210204, Location.create(4198, 4001, 0)); // zombies
		teleports.put(210208, Location.create(2440, 5172, 0)); // jad
		teleports.put(210212, Location.create(3367, 3265, 0)); // duel arena
		teleports.put(210216, Location.create(2399, 5178, 0)); // fight caves
		teleports.put(210220, Location.create(2444, 3089, 0)); // castle wars
		teleports.put(210224, Location.create(3273, 3686, 0)); // clan wars
		teleports.put(210228, Location.create(2875, 3545, 0)); // warriors guild
		teleports.put(210234, Location.create(2539, 4715, 0)); // mage bank game
		teleports.put(210238, Location.create(3565, 3312, 0)); // barrows

		// bossing
		teleports.put(211080, Location.create(3508, 9493, 0)); // kq
		teleports.put(211088, Location.create(2871, 5317, 2)); // godwars
		teleports.put(211092, Location.create(2533, 5805, 0)); // tormented demons
		teleports.put(211097, Location.create(2722, 2747, 0)); // jungle demon
		teleports.put(211101, Location.create(3233, 2758, 0)); // bork
		teleports.put(211105, Location.create(2367, 4956, 0)); // barrelchest
		teleports.put(211109, Location.create(2526, 4570, 0)); // sea troll
		teleports.put(211115, Location.create(2957, 4382, 0)); // corp beaast
		teleports.put(211119, Location.create(2442, 10147, 0));  // dag kings
		teleports.put(211123, Location.create(2635, 9828, 0)); // avatar of destruction
		teleports.put(211127, Location.create(2858, 5207, 0)); // nex
		teleports.put(211131, Location.create(3361, 5857, 0)); // nomad
		teleports.put(211135, Location.create(4188, 5720, 0)); // glacors

		//donor
		teleports.put(74080, Location.create(4000, 4019, 0)); // donor zone
		teleports.put(74084, Location.create(2780, 3860, 0)); // respected zone
		teleports.put(74088, Location.create(3040, 4380, 0)); // extreme zone
	}


	// 2266, 3072 north
	// 2266, 3062 south


	public static boolean onDialogueOption(Player player, int option) {
		if (player.getDialogueOwner() == null || !player.getDialogueOwner().equals(TeleportInterface.class)) {
			return false;
		}

		if (player.getCurrentDialogueId() == 1) {
			if(option == 2 && !player.isMember()) {
				player.sendMessage("This is for donators only.");
				return true;
			}

			player.getPA().spellTeleport(1240, 1238, (option - 1) * 4);
			return true;
		}

		if (player.getCurrentDialogueId() == 2) {
			switch (option) {
				case 1: // Gnome Course
					player.getPA().spellTeleport(2480, 3437, 0);
					break;
				case 2: // "Barbarian Course"
					player.getPA().spellTeleport(2552, 3558, 0);
					break;
				case 3: // "Ape Atoll Course"
					player.getPA().spellTeleport(2768, 2751, 0);
					break;
			}
			return true;
		}

		if(player.getCurrentDialogueId() == 3) {
			switch (option) {
				case 1:
					player.getPA().spellTeleport(3005, 3850, 0);
					player.resetDialogue();
					break;
				case 2:
					player.resetDialogue();
					break;
			}
			return true;
		}

		return true;
	}

	public static boolean onClickingButtons(Player player, int button) {

		if(button == 210242) {
			Gambling.teleport(player);
			return true;
		}

		if(button == 211084) { // jkbd
			player.getPA().sendOptions(Optional.of("This teleport is in the wilderness, continue teleporting?"), "Yes", "No");
			player.setDialogueOwner(TeleportInterface.class);
			player.setCurrentDialogueId(3);
			return true;
		}

		if (button == 209211) {
			player.getPA().sendOptions("Gnome Course", "Barbarian Course", "Ape Atoll Course");
			player.setDialogueOwner(TeleportInterface.class);
			player.setCurrentDialogueId(2);
			return true;
		}

		if (button == 211143) { // cerb
			player.getPA().sendOptions("Room 1", "Room 2 [Donator Only]");
			player.setDialogueOwner(TeleportInterface.class);
			player.setCurrentDialogueId(1);
			return true;
		}

		if(button == 211149) { // kraken
			// cant teleport when already there
			if (Kraken.inArea(player.getLocation())) {
				return true;
			}

			if (player.getFamiliar() != null) {
				player.sendMessage("You may not take Summoning familiars into this area.");
				return true;
			}

			// make sure no existing instance exists
			if (player.instancesArea != null) {
				player.instancesArea.leave(true);
				player.instancesArea = null;
			}

			player.instancesArea = new InstancesArea(
					player,
					new Area(3586, 5798, 3706, 5820, player.getId() * 4),
					// whirlpools
					NPCHandler.spawnNpc2(492, 3690, 5810, player.getId() * 4, 0, 50, 2, 200, 150),
					NPCHandler.spawnNpc2(492, 3690, 5814, player.getId() * 4, 0, 50, 2, 200, 150),
					NPCHandler.spawnNpc2(492, 3700, 5810, player.getId() * 4, 0, 50, 2, 200, 150),
					NPCHandler.spawnNpc2(492, 3700, 5814, player.getId() * 4, 0, 50, 2, 200, 150),

					// big whirlpool
					NPCHandler.spawnNpc2(490, 3694, 5810, player.getId() * 4, 0, 255, 1, 375, 265)
			);
			player.getPA().spellTeleport(3696, 5807, player.getId() * 4);
			return true;
		}

		if (button == 74084 && player.getTotalDonated() < 750) {
			player.sendMessage("You must of donated 750 or more to use this teleport.");
			return true;
		}

		if (button == 74088 && player.getTotalDonated() < 50 && player.rights == 0) {
			player.sendMessage("You must of donated 50 or more to use this teleport.");
			return true;
		}

		if (button == 211139) { //zulrah

			// cant teleport when already there
			if (Zulrah.inArea(player)) {
				return true;
			}

			if (player.getFamiliar() != null) {
				player.sendMessage("You may not take Summoning familiars into this area.");
				return true;
			}

			// make sure no existing instance exists
			if (player.instancesArea != null) {
				player.instancesArea.leave(true);
				player.instancesArea = null;
			}

			player.instancesArea = new InstancesArea(
					player,
					new Area(2250, 3050, 2280, 3090, player.getId() * 4),
					// do not change hp (500) it will break the code
					NPCHandler.spawnNpc2(2042, 2266, 3072, player.getId() * 4, 0, 500, 1, Zulrah.ATTACK, Zulrah.DEFENCE)
			);
			player.getPA().spellTeleport(2268, 3070, player.getId() * 4);
			return true;
		}

		if (button == 211123) {
			player.sendMessage("This Boss has been moved to level 41 wilderness.");
			return true;
		}
		if (button == 74080 && !player.isMember() && player.rights == 0) {
			player.sendMessage("This is a donator only teleport, type ::donate to purchase donator status.");
			return true;
		}

		if (button == 209109 && player.getPA().getActualLevel(PlayerConstants.SLAYER) < 75) {
			player.sendMessage("This teleport requires 75 Slayer.");
			return true;
		}

		if (button == 210095) { // rev
			player.getPA().sendStatement("You can find the entrances in level 17 and 31 wilderness north of Edgeville.");
			return true;
		}

		if (teleports.containsKey(button)) {
			player.getPA().closeAllWindows();

			Location loc = teleports.get(button);
			player.getPA().spellTeleport(loc.getX(), loc.getY(), loc.getZ());
			return true;
		}
		return false;
	}


}
