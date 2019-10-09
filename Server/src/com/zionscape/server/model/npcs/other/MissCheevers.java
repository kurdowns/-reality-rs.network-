package com.zionscape.server.model.npcs.other;

import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.world.shops.Shops;

public class MissCheevers {

	public static final int XP = 0;
	public static final int PC = 1;
	public static final int PK = 2;
	public static final int ZOMBIE = 3;
	public static final int CWARS = 4;
	private static final int ID = 2288;

	public static boolean onDialogueOption(Player player, int option) {
		if (player.getDialogueOwner() == null || !player.getDialogueOwner().equals(MissCheevers.class)) {
			return false;
		}

		if (player.ironman) {
			switch (option) {
				case 1:
					Shops.open(player, 11);
					break;
				case 2:
					Shops.open(player, 12);
					player.sendMessage("You have @red@" + player.votePoints + " @bla@vote points.");
					break;
				case 3:
					sendConfig(player);
					player.removeAttribute("perk");
					player.getPA().showInterface(18793);
					player.sendMessage("You have @red@" + player.votePoints + " @bla@vote points.");
					break;
			}
		} else {
			switch (option) {
				case 1:
					Shops.open(player, 31);
					player.sendMessage("You have @red@" + player.votePoints + " @bla@vote points.");
					break;
				case 2:
					sendConfig(player);
					player.removeAttribute("perk");
					player.getPA().showInterface(18793);
					player.sendMessage("You have @red@" + player.votePoints + " @bla@vote points.");
					break;
			}
		}

		return true;
	}

	public static boolean onNpcClick(Player player, NPC npc, int option) {
		if (npc.type == ID) {
			if (player.ironman) {
				player.sendMessage("This vendor is for non ironman accounts only.");
				return true;
			}
			player.setDialogueOwner(MissCheevers.class);
			player.getPA().sendOptions("Item Exchange", "Account Perks");
			return true;
		}
		if (npc.type == 246) {
			if (!player.ironman) {
				player.sendMessage("This vendor is for ironman accounts only.");
				return true;
			}
			player.setDialogueOwner(MissCheevers.class);
			player.getPA().sendOptions("Ironman shop", "Ironman vote shop", "Account Perks");
			return true;
		}
		return false;
	}

	public static boolean onClickingButtons(Player player, int button) {

		if (player.openInterfaceId != 18793) {
			return false;
		}

		switch (button) {
			case 73113: // xp
				player.setAttribute("perk", XP);
				sendConfig(player);
				return true;
			case 73114: // pc
				player.setAttribute("perk", PC);
				sendConfig(player);
				return true;
			case 73115: // pk
				player.setAttribute("perk", PK);
				sendConfig(player);
				return true;
			case 73117: // zombie
				player.setAttribute("perk", ZOMBIE);
				sendConfig(player);
				return true;
			case 73118: // cwars
				player.setAttribute("perk", CWARS);
				sendConfig(player);
				return true;
			case 73107: // confirm

				if (player.getData().votePerk != null) {
					player.sendMessage("You already have a perk active.");
					return true;
				}

				if (!player.attributeExists("perk")) {
					player.sendMessage("You have not selected a perk.");
					return true;
				}

				int perk = (int) player.getAttribute("perk");

				int length = 0;
				int cost = 0;
				switch (perk) {
					case XP:
						length = (45 * 60 * 1000) / 600;
						cost = 6;
						break;
					case PC:
						length = (45 * 60 * 1000) / 600;
						cost = 4;
						break;
					case PK:
						length = (60 * 60 * 1000) / 600;
						cost = 4;
						break;
					case ZOMBIE:
						length = (60 * 60 * 1000) / 600;
						cost = 4;
						break;
					case CWARS:
						length = (120 * 60 * 1000) / 600;
						cost = 2;
						break;
				}

				if (cost > player.votePoints) {
					player.sendMessage("You do not have enough vote points.");
					return true;
				}

				player.votePoints -= cost;
				player.getData().votePerk = new AccountPerk(perk, length);
				player.sendMessage("Your account perk is now active.");
				player.getPA().removeAllWindows();

				sendPerk(player);

				return true;
		}

		return false;
	}

	private static final int[] CONFIG = {533, 534, 535, 537, 538};

	private static void sendConfig(Player player) {
		if (!player.attributeExists("perk")) {
			for (int i = 0; i < CONFIG.length; i++) {
				player.getPA().sendConfig(CONFIG[i], 0);
			}
			return;
		}
		int attribute = (int) player.getAttribute("perk");

		for (int i = 0; i < CONFIG.length; i++) {
			player.getPA().sendConfig(CONFIG[i], i == attribute ? 1 : 0);
		}
	}

	public static class AccountPerk {
		private final int perk;
		private final int ticksToExpire;
		private int ticks = 0;

		public AccountPerk(int perk, int ticksToExpire) {
			this.perk = perk;
			this.ticksToExpire = ticksToExpire;
		}

		public int getPerk() {
			return perk;
		}

		public int getTicks() {
			return ticks;
		}

		public void setTicks(int ticks) {
			this.ticks = ticks;
		}

		public void incrementTicks() {
			this.ticks++;
		}

		public int getTicksToExpire() {
			return ticksToExpire;
		}
	}

	public static boolean isPerkActive(Player player, int perk) {
		if (player.getData().votePerk == null) {
			return false;
		}
		return player.getData().votePerk.getPerk() == perk;
	}

	public static void sendPerk(Player player) {
		if (player.getData().votePerk == null) {
			player.getPA().sendFrame126("@or1@Current Perk: @whi@None", 54438);
		} else {

			String name = "XP";
			switch (player.getData().votePerk.getPerk()) {
				case 1:
					name = "PC";
					break;
				case 2:
					name = "PK";
					break;
				case 3:
					name = "Zombie";
					break;
				case 4:
					name = "Cwars";
					break;
			}

			int timeLeft = player.getData().votePerk.getTicksToExpire() - player.getData().votePerk.getTicks();

			int ms = timeLeft * 600;

			if (ms < 60 * 1000) {
				timeLeft = 1;
			} else {
				timeLeft = (int) Math.ceil(ms / (60 * 1000));
			}

			player.getPA().sendFrame126("@or1@Current Perk: @whi@" + name + " " + timeLeft + "mins", 54438);
		}
	}
}
