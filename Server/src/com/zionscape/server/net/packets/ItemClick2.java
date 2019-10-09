package com.zionscape.server.net.packets;

import com.zionscape.server.ServerEvents;
import com.zionscape.server.model.content.minigames.DuelArena;
import com.zionscape.server.model.content.minigames.christmas.ChristmasEvent;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.skills.slayer.Slayer;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;

/**
 * Item Click 2 Or Alternative Item Option 1
 *
 * @author Ryan / Lmctruck30
 *         <p>
 *         Proper Streams
 */
public class ItemClick2 implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize, Stream stream) {

		int itemId = stream.readSignedWordA();
		int slot = stream.readSignedWordBigEndianA();
		int junk = stream.readSignedWordBigEndianA();


		if (!player.getItems().playerHasItem(itemId, 1)) {
			return;
		}

		if (player.rights >= 3) {
			player.sendMessage("itemclick2 " + itemId);
		}

		if (ServerEvents.onItemClicked(player, itemId, slot, 2)) {
			return;
		}

		if (player.getDuel() != null
				&& (player.getDuel().getStage() == DuelArena.Stage.STAKING || player.getDuel().getStage() == DuelArena.Stage.CONFIRMING_STAKE)) {
			DuelArena.declineDuel(player);
		}

		switch (itemId) {
			case 18803:
				if(!player.getItems().playerHasItem(18803)) {
					break;
				}
				Slayer.operateMablesRing(player);
				break;
			case 6865:
				player.action = "bow";
				ChristmasEvent.blueMarionetteActions(player);
				break;
			case 6866:
				player.action = "bow";
				ChristmasEvent.greenMarionetteActions(player);
				break;
			case 6867:
				player.action = "bow";
				ChristmasEvent.redMarionetteActions(player);
				break;
			case 11694:
				if(player.getItems().freeInventorySlots() < 2) {
					player.sendMessage("You do not have enough inventory space.");
					break;
				}
				player.getItems().deleteItem(itemId, 1);
				player.getItems().addItem(11690, 1);
				player.getItems().addItem(11702, 1);
				player.sendMessage("You dismantle the godsword blade from the hilt.");
				break;
			case 11696:
				if(player.getItems().freeInventorySlots() < 2) {
					player.sendMessage("You do not have enough inventory space.");
					break;
				}
				player.getItems().deleteItem(itemId, 1);
				player.getItems().addItem(11690, 1);
				player.getItems().addItem(11704, 1);
				player.sendMessage("You dismantle the godsword blade from the hilt.");
				break;
			case 11698:
				if(player.getItems().freeInventorySlots() < 2) {
					player.sendMessage("You do not have enough inventory space.");
					break;
				}
				player.getItems().deleteItem(itemId, 1);
				player.getItems().addItem(11690, 1);
				player.getItems().addItem(11706, 1);
				player.sendMessage("You dismantle the godsword blade from the hilt.");
				break;
			case 11700:
				if(player.getItems().freeInventorySlots() < 2) {
					player.sendMessage("You do not have enough inventory space.");
					break;
				}
				player.getItems().deleteItem(itemId, 1);
				player.getItems().addItem(11690, 1);
				player.getItems().addItem(11708, 1);
				player.sendMessage("You dismantle the godsword blade from the hilt.");
				break;
			case 19335:
				if(player.getItems().freeInventorySlots() < 2) {
					player.sendMessage("You do not have enough inventory space.");
					break;
				}
				player.getItems().deleteItem(itemId, 1);
				player.getItems().addItem(6585, 1);
				player.getItems().addItem(19333, 1);
				player.sendMessage("You split the two parts and end with a kit and amulet");
				break;
			default:
				if (player.rights == 3) {
					System.out.println(player.username + " - Item3rdOption: " + itemId);
				}
				break;
		}
	}
}
