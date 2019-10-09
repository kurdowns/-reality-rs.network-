package com.zionscape.server.model.players.skills.summoning.specials;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.skills.summoning.FamiliarSpecial;

public class MagicFocus extends FamiliarSpecial {

	public MagicFocus(Player player) {
		super(player);
	}

	@Override
	public boolean execute() {
		getPlayer().level[PlayerConstants.MAGIC] = getPlayer().getPA().getLevelForXP(PlayerConstants.MAGIC) + 7;
		getPlayer().getPA().refreshSkill(PlayerConstants.MAGIC);

		getPlayer().startAnimation(7660);
		getPlayer().gfx0(1296);

		getNPC().gfx0(1296);
		getNPC().startAnimation(7660);
		return true;
	}

}