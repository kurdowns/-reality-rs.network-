package com.zionscape.server.net.packets;

import com.google.common.base.MoreObjects;
import com.zionscape.server.Server;
import com.zionscape.server.ServerEvents;
import com.zionscape.server.model.content.minigames.DuelArena;
import com.zionscape.server.model.content.minigames.christmas.ChristmasEvent;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.skills.summoning.Summoning;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.DatabaseUtil;
import com.zionscape.server.util.Stream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Item Click 3 Or Alternative Item Option 1
 *
 * @author Ryan / Lmctruck30
 *         <p>
 *         Proper Streams
 */
public class ItemClick3 implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize, Stream stream) {
		int interfaceId = stream.readSignedWordBigEndianA();
		int itemSlot = stream.readSignedWordBigEndian();
		int itemId = stream.readSignedWordA();

		if (itemSlot < 0 || itemSlot > player.inventory.length - 1) {
			return;
		}

		if (player.inventory[itemSlot] - 1 != itemId) {
			return;
		}

		if (player.rights >= 3) {
			player.sendMessage(MoreObjects.toStringHelper(ItemClick3.class).add("interface id", interfaceId).add("slot", itemSlot).add("id", itemId).toString());
		}

		if (Summoning.onClickItem(player, itemId, 3)) {
			return;
		}

		if (player.getDuel() != null
				&& (player.getDuel().getStage() == DuelArena.Stage.STAKING || player.getDuel().getStage() == DuelArena.Stage.CONFIRMING_STAKE)) {
			DuelArena.declineDuel(player);
		}

		if (ServerEvents.onItemClicked(player, itemId, itemSlot, 3)) {
			return;
		}

		switch (itemId) {
			case 995: // deposit into money pouch
				int amount = player.inventoryN[itemSlot];

				// just a double check this should never happen though haha but its pi so who knows
				if (amount < 0) {
					return;
				}

				if (player.getDuel() != null && player.getDuel().getStage() == DuelArena.Stage.FIGHTING) {
					player.sendMessage("You cannot do this whilst in a duel.");
					return;
				}

				player.getItems().deleteItem(995, amount);
				player.getData().moneyPouchAmount += amount;
				player.getPA().sendMoneyPouchUpdate(amount);
				player.getItems().sendMoneyPouch();

				Server.submitWork(() -> {
					try (Connection connection = DatabaseUtil.getConnection()) {
						try (PreparedStatement ps = connection.prepareStatement("INSERT INTO pouch_logs (player_id, username, location, amount, pouch_amount, type) VALUES(?, ?, ?, ?, ?, ?)")) {
							ps.setInt(1, player.getDatabaseId());
							ps.setString(2, player.username);
							ps.setString(3, player.getLocation().toString());
							ps.setInt(4, amount);
							ps.setLong(5, player.getData().moneyPouchAmount);
							ps.setString(6, "DEPOSIT");
							ps.execute();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				});

				break;
			case 6865:
				player.action = "dance";
				ChristmasEvent.blueMarionetteActions(player);
				break;
			case 6866:
				player.action = "dance";
				ChristmasEvent.greenMarionetteActions(player);
				break;
			case 6867:
				player.action = "dance";
				ChristmasEvent.redMarionetteActions(player);
				break;
			case 1712:
				player.getPA().handleGlory(itemId);
				break;
			case 2572:
				player.getPA().rowTeleports();
				break;
			default:
				if (player.rights == 3) {
					System.out.println(player.username + " - Item3rdOption: " + itemId + " : " + itemSlot + " : " + interfaceId);
				}
				break;
		}
	}
}
