package com.zionscape.server.net.packets;

import com.zionscape.server.Server;
import com.zionscape.server.model.content.minigames.zombies.Zombies;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.tick.Tick;
import com.zionscape.server.util.Stream;
import com.zionscape.server.world.ItemDrops;

/**
 * Pickup Item
 */
public class PickupItem implements PacketType {

	@Override
	public void processPacket(final Player c, int packetType, int packetSize, Stream stream) {
		c.pItemY = stream.readSignedWordBigEndian();
		c.pItemId = stream.readUnsignedWord();
		c.pItemX = stream.readSignedWordBigEndian();
		if (Math.abs(c.getX() - c.pItemX) > 25 || Math.abs(c.getY() - c.pItemY) > 25) {
			c.resetWalkingQueue();
			return;
		}

		if(c.openInterfaceId == 19302 || !c.getData().completedTutorial || c.attributeExists("doing_tutorial")) {
			return;
		}

		if(Zombies.inLobbyArea(c.getLocation())) {
			return;
		}

		if(c.attributeExists("block_picking_up")) {
			return;
		}

		if (c.isDoingTutorial()) {
			c.resetWalkingQueue();
			return;
		}
		c.getCombat().resetPlayerAttack();
		if (c.getX() == c.pItemX && c.getY() == c.pItemY) {
			ItemDrops.removeGroundItem(c, c.pItemId, c.pItemX, c.pItemY, true);
		} else {
			c.walkingToItem = true;
			Server.getTickManager().submit(new Tick(1) {

				public void execute() {
					if (c.isDisconnected()) {
						this.stop();
						return;
					}
					if (c.getX() == c.pItemX && c.getY() == c.pItemY
							|| c.goodDistance(c.getX(), c.getY(), c.pItemX, c.pItemY, 1)) {
						c.walkingToItem = false;
						ItemDrops.removeGroundItem(c, c.pItemId, c.pItemX, c.pItemY, true);
						this.stop();
					}
				}
			});
		}
	}
}
