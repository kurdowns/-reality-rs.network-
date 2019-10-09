package com.zionscape.server.net.packets;

import com.google.common.base.MoreObjects;
import com.google.common.primitives.Ints;
import com.zionscape.server.Config;
import com.zionscape.server.Server;
import com.zionscape.server.ServerEvents;
import com.zionscape.server.gamecycle.GameCycleTask;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.content.grandexchange.GrandExchange;
import com.zionscape.server.model.content.minigames.zombies.Zombies;
import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.items.TridentOfTheSeas;
import com.zionscape.server.model.items.TridentOfTheSwamp;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.skills.Fishing;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.DatabaseUtil;
import com.zionscape.server.util.Stream;
import com.zionscape.server.world.ItemDrops;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Drop Item
 */
public class DropItem implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {
		int itemId = stream.readUnsignedWordA();
		int interfaceId = stream.readUnsignedWord();
		int slot = stream.readUnsignedWordA();

		if (c.puzzle != null) {
			c.puzzle.movePiece(slot, itemId, false);
			return;
		}

		if (slot < 0 || slot > 28 || itemId == -1 || c.inventory[slot] != itemId + 1) {
			return;
		}

		if (c.rights > 3) {
			System.out.println(MoreObjects.toStringHelper(DropItem.class).add("player", c).add("item", itemId).add("interface id", interfaceId).add("slot", slot));
		}

		c.lampVfy = false;
		if (System.currentTimeMillis() - c.lastAlch < 1800) {
			return;
		}
		if (c.level[3] <= 0) {
			c.sendMessage("You can't drop items when you are dead!");
			return;
		}

		if (c.tradeTimer > 0) {
			c.sendMessage("You must wait 15 minutes from the creation of your account to drop items.");
			return;
		}

		if (c.inTrade) {
			c.sendMessage("You can't drop items while trading!");
			return;
		}

		if(Zombies.inGameArea(c.getLocation()) || Zombies.inLobbyArea(c.getLocation())) {
			return;
		}

		if (GrandExchange.usingGrandExchange(c)) {
			return;
		}

		if (c.playerIsFishing) {
			Fishing.resetFishing(c);
		}

		// exploding potions
		if (itemId == 4045) {
			c.startAnimation(827);
			c.getItems().deleteItem(itemId, slot, c.inventoryN[slot]);

			GameCycleTaskHandler.addEvent(c, new GameCycleTask() {

				@Override
				public void execute(GameCycleTaskContainer container) {

					int damage = 15;

					if (c.level[3] - damage < 0) {
						damage = c.level[3];
					}

					c.level[3] -= damage;
					c.handleHitMask(damage, 100, 3, c.playerIndex);
					c.getPA().refreshSkill(3);
					c.forcedChat("Ow! That really hurt!");
					container.stop();
				}
			}, 1);
			return;
		}

		if (ItemUtility.getName(itemId).toLowerCase().contains("clue scroll") || Ints.asList(Config.UNDROPPABLE_ITEMS).contains(itemId)) {
			// if (Ints.asList(Config.UNDROPPABLE_ITEMS).contains(itemId)) {
			String itemName = c.getItems().getItemName(itemId);
			String[][] info = {{"Are you sure you want to destroy this item?", "14174"},
					{"Yes.", "14175"}, {"No.", "14176"}, {"", "14177"}, {"", "14182"}, {"", "14183"},
					{itemName, "14184"}};

			c.getPA().sendFrame34(itemId, 0, 14171, c.inventoryN[slot]);
			for (int i = 0; i < info.length; i++) {
				c.getPA().sendFrame126(info[i][0], Integer.parseInt(info[i][1]));
			}
			c.destroyItem = itemId;
			c.destroyAmount = c.inventoryN[slot];
			c.getPA().sendFrame164(14170);
			return;
		}

		if (ServerEvents.onPlayerDroppedItem(c, new Item(itemId, c.inventoryN[slot]))) {
			return;
		}

		int amount = c.inventoryN[slot];

		final int item = itemId, x = c.absX, y = c.absY, z = c.heightLevel;
		Server.submitWork(() -> {
			try (Connection connection = DatabaseUtil.getConnection()) {
				try (PreparedStatement ps = connection.prepareStatement("INSERT INTO player_drops (player_id, item, amount, x, y, z, ip, uuid) VALUES(?, ?, ?, ?, ?, ?, ?, ?)")) {
					ps.setInt(1, c.getDatabaseId());
					ps.setInt(2, item);
					ps.setInt(3, amount);
					ps.setInt(4, x);
					ps.setInt(5, y);
					ps.setInt(6, z);
					ps.setString(7, c.connectedFrom);
					ps.setString(8, c.uuid);
					ps.execute();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

		c.getItems().deleteItem(itemId, slot, amount);

		if(itemId == TridentOfTheSeas.CHARGED_TRIDENT) {
			itemId = TridentOfTheSeas.UNCHARGED_TRIDENT;
			c.getData().tridentSeaCharges = 0;
		}
		if(itemId == TridentOfTheSwamp.CHARGED_TRIDENT) {
			itemId = TridentOfTheSwamp.UNCHARGED_TRIDENT;
			c.getData().tridentSwampCharges = 0;
		}

		ItemDrops.createGroundItem(c, itemId, c.getX(), c.getY(), amount, c.getId(), true);
	}

}