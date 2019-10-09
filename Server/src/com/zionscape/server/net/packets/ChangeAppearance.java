package com.zionscape.server.net.packets;

import com.zionscape.server.cache.clientref.IdentityKit;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;

/**
 * Change appearance
 */
public class ChangeAppearance implements PacketType {

	static final int[][] anIntArrayArray1003 = {
			{6798, 107, 10283, 16, 4797, 7744, 5799, 4634, 33697, 22433, 2983, 54193},
			{8741, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621, 4783, 1341, 16578, 35003, 25239},
			{25238, 8742, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621, 4783, 1341, 16578, 35003},
			{4626, 11146, 6439, 12, 4758, 10270},
			{4550, 4537, 5681, 5673, 5790, 6806, 8076, 4574}};

	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {

		int[] appearance = new int[13];

		// gender
		appearance[0] = stream.readSignedByte();

		for (int i = 1; i < appearance.length; i++) {
			appearance[i] = stream.readSignedWord();
		}

		if (c.canChangeAppearance) {

			// validate the appearance
			if(appearance[0] != 0 && appearance[0] != 1) { // gender
				c.sendMessage("Invalid appearance values. Try restarting the client.");
				c.canChangeAppearance = false;
				c.getPA().closeAllWindows();
				return;
			}

			for(int i = 1; i < appearance.length; i++) {
				if(i > 7) {
					if(appearance[i] < 0 || appearance[i] > anIntArrayArray1003[i - 8].length - 1) {
						c.sendMessage("Invalid appearance values. Try restarting the client.");
						c.canChangeAppearance = false;
						c.getPA().closeAllWindows();
						return;
					}
				} else {
					boolean found = false;
					for (int index = 0; index < IdentityKit.cache.length; index++) {

						// we don't parse the jaw value for females
						if(appearance[0] == 1 && (i - 1) + 7 == 8) {
							found = true;
							break;
						}

						if (IdentityKit.cache[index].notSelectable || IdentityKit.cache[index].bodyPartID != (i - 1) + (appearance[0] == 0 ? 0 : 7)) {
							continue;
						}

						if (index == appearance[i]) {
							found = true;
						}
					}

					if (!found) {
						c.sendMessage("Invalid appearance values. Try restarting the client.");
						c.canChangeAppearance = false;
						c.getPA().closeAllWindows();
						return;
					}
				}
			}

			c.appearance[0] = appearance[0]; // gender
			c.appearance[1] = appearance[1]; // head
			c.appearance[2] = appearance[3]; // torso
			c.appearance[3] = appearance[4]; // arms
			c.appearance[4] = appearance[5]; // hands
			c.appearance[5] = appearance[6]; // legs
			c.appearance[6] = appearance[7]; // feet
			c.appearance[7] = appearance[2]; // beard
			c.appearance[8] = appearance[8]; // hair colour
			c.appearance[9] = appearance[9]; // torso colour
			c.appearance[10] = appearance[10]; // legs colour
			c.appearance[11] = appearance[11]; // feet colour
			c.appearance[12] = appearance[12]; // skin colour

			c.getPA().removeAllWindows();
			c.getPA().requestUpdates();
			c.canChangeAppearance = false;

			Achievements.progressMade(c, Achievements.Types.CHANGE_APPEARANCE);
		}

	}
}
