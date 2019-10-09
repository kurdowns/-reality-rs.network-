package com.zionscape.server.net.packets;

import com.zionscape.server.model.content.GreeGree;
import com.zionscape.server.model.content.Resting;
import com.zionscape.server.model.content.minigames.MagicArena;
import com.zionscape.server.model.content.minigames.christmas.ChristmasEvent;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;

/**
 * Wear Item
 */
public class WearItem implements PacketType {

	@SuppressWarnings("unused")
	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {
		int wearId = stream.readUnsignedWord();
		int wearSlot = stream.readUnsignedWordA();
		int interfaceId = stream.readUnsignedWordA();
		if (!c.getItems().playerHasItem(wearId, 1)) {
			return;
		}

		Resting.stop(c);

		int oldCombatTimer = c.attackTimer;
		if (c.playerIndex > 0 || c.npcIndex > 0) {
			c.getCombat().resetPlayerAttack();
		}
		if (wearId == 4155) {

			if (c.getData().getSlayerTask() != null) {
				c.sendMessage("Your current assignment is: " + c.getData().getSlayerTask().getTask().getName() + "; only " + c.getData().getSlayerTask().getAmount() + " more to go.");
			} else {
				c.sendMessage("You currently have no assignment.");
			}

			return;
		}
		if (wearId == 6865) {
			c.action = "walk";
			ChristmasEvent.blueMarionetteActions(c);
			return;
		}
		if (wearId == 6866) {
			c.action = "walk";
			ChristmasEvent.greenMarionetteActions(c);
			return;
		}
		if (wearId == MagicArena.GUTHIX || wearId == MagicArena.ZAMORAK || wearId == MagicArena.SARADOMIN) {
			if (!MagicArena.canWieldStaff(c, wearId)) {
				return;
			}
		}
		if (wearId == 6867) {
			c.action = "walk";
			ChristmasEvent.redMarionetteActions(c);
			return;
		}
		if (wearId == 5509) {
			c.getPA().removeSmallPouch();
			return;
		}
		if (wearId == 5510) {
			c.getPA().removeMediumPouch();
			return;
		}
		if (wearId == 5511) {
			c.getPA().removeMediumPouch();
			return;
		}
		if (wearId == 5512) {
			c.getPA().removeLargePouch();
			return;
		}
		if (wearId == 5513) {
			c.getPA().removeLargePouch();
			return;
		}
		if (wearId == 5514) {
			c.getPA().removeGiantPouch();
			return;
		}
		if (wearId == 5515) {
			c.getPA().removeGiantPouch();
			return;
		}
		// c.attackTimer = oldCombatTimer;
		c.getItems().wearItem(wearId, wearSlot);
		GreeGree.transform(c);
	}
}
