package com.zionscape.server.net.packets;

import com.google.common.base.MoreObjects;
import com.zionscape.server.Server;
import com.zionscape.server.model.content.minigames.DuelArena;
import com.zionscape.server.model.players.Pins;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.DatabaseUtil;
import com.zionscape.server.util.Misc;
import com.zionscape.server.util.Stream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WithdrawFromMoneyPouch implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {
		long amount = stream.readQWord();

		if (c.getDuel() != null && c.getDuel().getStage() == DuelArena.Stage.FIGHTING) {
			c.sendMessage("You cannot do this whilst in a duel.");
			return;
		}

		if(Pins.checkPin(c)) {
			Pins.open(c, Pins.Type.MONEY_POUCH);
			return;
		}

		if (amount > c.getData().moneyPouchAmount) {
			amount = c.getData().moneyPouchAmount;
		}

		if (amount <= 0) {
			return;
		}
		if (amount > Integer.MAX_VALUE) {
			c.sendMessage("You may only withdraw a max of " + Misc.formatNumber(Integer.MAX_VALUE) + " gp.");

			amount = Integer.MAX_VALUE;
		}

		System.out.println(MoreObjects.toStringHelper(WithdrawFromMoneyPouch.class).add("player", c).add("amount", amount).toString());

		long hasAmount = 0;
		for (int i = 0; i < c.inventory.length; i++) {
			if (c.inventory[i] == 996) {
				hasAmount += c.inventoryN[i];
			}
		}

		if (hasAmount + amount > Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE - hasAmount;
			c.sendMessage("You may only store " + Misc.formatNumber(Integer.MAX_VALUE) + " of gp in your inventory.");
		}

		// final 0 amount check
		if (amount == 0) {
			return;
		}

		// make sure we have inventory space
		if (!c.getItems().playerHasItem(995) && c.getItems().freeInventorySlots() == 0) {
			c.sendMessage("You do not have enough inventory space.");
			return;
		}

		c.getData().moneyPouchAmount -= amount;
		c.getItems().sendMoneyPouch();
		c.getPA().sendMoneyPouchUpdate(-(int) amount);

		c.getItems().addMoneyToInventory((int) amount);

		final int finalAmount = (int) amount;

		Server.submitWork(() -> {
			try (Connection connection = DatabaseUtil.getConnection()) {
				try (PreparedStatement ps = connection.prepareStatement("INSERT INTO pouch_logs (player_id, username, location, amount, pouch_amount, type) VALUES(?, ?, ?, ?, ?, ?)")) {
					ps.setInt(1, c.getDatabaseId());
					ps.setString(2, c.username);
					ps.setString(3, c.getLocation().toString());
					ps.setInt(4, finalAmount);
					ps.setLong(5, c.getData().moneyPouchAmount);
					ps.setString(6, "WITHDRAW");
					ps.execute();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});


		if (c.attributeExists("shop")) {
			c.getItems().resetItems(3823);
		}
		if (c.isBanking) {
			c.getItems().resetItems(5064);
		}
		if (c.getDuel() != null) {
			c.getItems().resetItems(3322);
		}
		if (c.inTrade) {
			c.getItems().resetItems(3322);
		}
		if (c.storingFamiliarItems) {
			c.getItems().resetItems(5064);
		}
	}

}