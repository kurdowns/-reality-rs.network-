package com.zionscape.server.model.players.skills.summoning.specials;

import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.skills.summoning.FamiliarSpecial;
import com.zionscape.server.model.players.skills.summoning.Summoning;

public class WinterStorage extends FamiliarSpecial {

	public WinterStorage(Player player) {
		super(player);
	}

	@Override
	public boolean execute() {
		getPlayer().sendMessage("You must use the scroll on the item you wish to bank.");
		return false;
	}

	@Override
	public boolean onItemOnItem(Item item, int slot, Item otherItem, int otherSlot) {

		// some anti dupe checks
		if (getPlayer().isBanking || getPlayer().inTrade || getPlayer().attributeExists("shop") || getPlayer().getDuel() != null
				|| getPlayer().storingFamiliarItems) {
			return false;
		}
		if (getPlayer().level[PlayerConstants.HITPOINTS] <= 0 || getPlayer().isDead) {
			return false;
		}

		Item itemToStore = Summoning.getScrollByItemId(item.getId()) == null ? item : otherItem;

		// need gfx and anim
		getPlayer().startAnimation(7660);
		getPlayer().gfx100(1316);

		// npc gfx
		// todo find the gfx id can't find it on rune-com.zionscape.server

		getPlayer().getItems().deleteItem(itemToStore.getId(), itemToStore.getAmount());

		// bank the item
		getPlayer().getItems().addDirectlyToBank(itemToStore.getId(), itemToStore.getAmount());
		return true;
	}

}