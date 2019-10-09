package com.zionscape.server.model.players.skills.summoning.specials;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.skills.summoning.FamiliarSpecial;

public class EbonThunder extends FamiliarSpecial {

	private boolean resetAttackType = false;

	public EbonThunder(Player player) {
		super(player);
	}

	@Override
	public boolean execute() {

		// player must be in combat
		if (getPlayer().playerIndex == 0) {
			getPlayer().sendMessage("You must be in combat with a player to use this special..");
			return false;
		}

		Player target = PlayerHandler.players[getPlayer().playerIndex];
		if (target != null) {
			// player animation and gfx
			getPlayer().gfx100(1316);
			getPlayer().startAnimation(7660);

			// npc animation and gfx
			getNPC().startAnimation(7986);
			getNPC().gfx0(1492);

			getPlayer().specAmount -= 1;
			if (getPlayer().specAmount < 0) {
				getPlayer().specAmount = 0;
			}
			getPlayer().getItems().updateSpecialBar();
			return true;
		}

		return false;
	}

}
