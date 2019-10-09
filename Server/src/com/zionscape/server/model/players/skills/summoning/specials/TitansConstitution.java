package com.zionscape.server.model.players.skills.summoning.specials;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.skills.summoning.FamiliarSpecial;

public class TitansConstitution extends FamiliarSpecial {

	public TitansConstitution(Player player) {
		super(player);
	}

	@Override
	public boolean execute() {

		// TODO hp boost

		getPlayer().level[PlayerConstants.DEFENCE] = (int) (getPlayer().getActualLevel(PlayerConstants.DEFENCE) * 1.125);
		getPlayer().getPA().refreshSkill(PlayerConstants.DEFENCE);

		return false;
	}

}