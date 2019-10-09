package com.zionscape.server.net.packets;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;
import com.zionscape.server.world.ItemDrops;

/**
 * Magic on floor items
 */
public class MagicOnFloorItems implements PacketType {

	@SuppressWarnings("unused")
	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {
		int itemY = stream.readSignedWordBigEndian();
		int itemId = stream.readUnsignedWord();
		int itemX = stream.readSignedWordBigEndian();
		int spellId = stream.readUnsignedWordA();
		if (!ItemDrops.itemExists(itemId, itemX, itemY, c.heightLevel)) {
			c.stopMovement();
			return;
		}
		c.sendMessage("Due to recent reports of duping, this has been disabled, will be back soon.");
		/*
		 * c.usingMagic = true; if(!c.getCombat().checkMagicReqs(51)) { c.stopMovement(); return; }
		 * 
		 * if(c.goodDistance(c.getX(), c.getY(), itemX, itemY, 12)) { int offY = (c.getX() - itemX) * -1; int offX =
		 * (c.getY() - itemY) * -1; c.teleGrabX = itemX; c.teleGrabY = itemY; c.teleGrabItem = itemId;
		 * c.turnPlayerTo(itemX, itemY); c.teleGrabDelay = System.currentTimeMillis();
		 * c.startAnimation(c.MAGIC_SPELLS[51][2]); c.gfx100(c.MAGIC_SPELLS[51][3]);
		 * c.getPA().createPlayersStillGfx(144, itemX, itemY, 0, 72); c.getPA().createPlayersProjectile(c.getX(),
		 * c.getY(), offX, offY, 50, 70, c.MAGIC_SPELLS[51][4], 50, 10, 0, 50);
		 * c.getPA().addSkillXP(c.MAGIC_SPELLS[51][7], 6); c.getPA().refreshSkill(6); c.stopMovement(); }
		 */
	}
}
