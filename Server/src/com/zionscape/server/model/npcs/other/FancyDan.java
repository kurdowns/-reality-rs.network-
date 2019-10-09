package com.zionscape.server.model.npcs.other;

import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.world.shops.Shops;

public class FancyDan {

	private static final int ID = 4361;

	public static boolean onDialogueOption(Player player, int option) {
		if (player.getDialogueOwner() == null || !player.getDialogueOwner().equals(FancyDan.class)) {
			return false;
		}

		switch (option) {
			case 1:
				Shops.open(player, 77);
				break;
			case 2:
				Shops.open(player, 78);
				break;
			case 3:
				Shops.open(player, 15);
				break;
		}

		return true;
	}

	public static boolean onNpcClick(Player player, NPC npc, int option) {
		if (npc.type == ID) {
			player.setDialogueOwner(FancyDan.class);
			player.getPA().sendOptions("Donator shop 1", "Donator shop 2", "Donator shop 3");
			return true;
		}
		return false;
	}


}
