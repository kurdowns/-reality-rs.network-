package com.zionscape.server.model.players.skills.summoning.specials;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.skills.summoning.FamiliarSpecial;

public class HealingAura extends FamiliarSpecial {

	public HealingAura(Player player) {
		super(player);
	}

	@Override
	public boolean execute() {

		if (getPlayer().isDead || getPlayer().level[PlayerConstants.HITPOINTS] == 0) {
			return false;
		}

		int maxLevel = getPlayer().getPA().getLevelForXP(getPlayer().xp[PlayerConstants.HITPOINTS]);

		if (getPlayer().level[PlayerConstants.HITPOINTS] >= maxLevel) {
			getPlayer().sendMessage("You already have full heath.");
			return false;
		}

		int heal = (int) (maxLevel * 0.10);

		getPlayer().level[PlayerConstants.HITPOINTS] += heal;
		if (getPlayer().level[PlayerConstants.HITPOINTS] > maxLevel) {
			getPlayer().level[PlayerConstants.HITPOINTS] = maxLevel;
		}

		getPlayer().getPA().refreshSkill(PlayerConstants.HITPOINTS);
		return true;
	}

}
