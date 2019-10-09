package com.zionscape.server.model.players.skills.summoning.specials;

import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.skills.summoning.FamiliarSpecial;

import java.util.Iterator;

public class EssenceShipment extends FamiliarSpecial {

	private static final int PURE_ESSENCE_ID = 0;
	private static final int RUNE_ESSENCE_ID = 0;

	public EssenceShipment(Player player) {
		super(player);
	}

	@Override
	public boolean execute() {

		// some anti dupe checks
		if (getPlayer().isBanking || getPlayer().inTrade || getPlayer().attributeExists("shop") || getPlayer().getDuel() != null
				|| getPlayer().storingFamiliarItems) {
			return false;
		}
		if (getPlayer().level[PlayerConstants.HITPOINTS] <= 0 || getPlayer().isDead) {
			return false;
		}

		int inventoryPureCount = getPlayer().getItems().getItemAmount(PURE_ESSENCE_ID);
		int inventoryRuneCount = getPlayer().getItems().getItemAmount(RUNE_ESSENCE_ID);

		int familiarPureCount = (int) getPlayer().getFamiliar().getInventoryItems().stream()
				.filter(x -> x.getId() == PURE_ESSENCE_ID).count();
		int familiarRuneCount = (int) getPlayer().getFamiliar().getInventoryItems().stream()
				.filter(x -> x.getId() == RUNE_ESSENCE_ID).count();

		if (inventoryPureCount == 0 || inventoryRuneCount == 0 | familiarPureCount == 0 || familiarRuneCount == 0) {
			getPlayer().sendMessage("You do not have any pure/rune essence to bank.");
			return false;
		}

		// need gfx and anim
		getPlayer().startAnimation(7660);
		getPlayer().gfx100(1316);

		// npc gfx
		// todo find the gfx id can't find it on rune-com.zionscape.server

		// delete any ess from the familiar
		Iterator<Item> itr = getPlayer().getFamiliar().getInventoryItems().iterator();
		while (itr.hasNext()) {
			Item item = itr.next();

			if (item.getId() == RUNE_ESSENCE_ID || item.getId() == PURE_ESSENCE_ID) {
				itr.remove();
			}
		}

		// bank the ess we found
		if (familiarPureCount > 0) {
			getPlayer().getItems().addDirectlyToBank(PURE_ESSENCE_ID, familiarPureCount);
		}

		if (familiarRuneCount > 0) {
			getPlayer().getItems().addDirectlyToBank(RUNE_ESSENCE_ID, familiarRuneCount);
		}

		if (inventoryPureCount > 0) {
			getPlayer().getItems().addDirectlyToBank(PURE_ESSENCE_ID, inventoryPureCount);
		}

		if (inventoryRuneCount > 0) {
			getPlayer().getItems().addDirectlyToBank(RUNE_ESSENCE_ID, inventoryRuneCount);
		}

		return true;
	}

}