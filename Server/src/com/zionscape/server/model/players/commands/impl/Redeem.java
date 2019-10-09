package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;
import com.zionscape.server.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Redeem implements Command {

	@Override
	public void execute(Player client, String message) {
		if (System.currentTimeMillis() - client.getData().lastClaimed < 5 * 60 * 1000) {
			client.sendMessage("You may try to claim a code every 5 minutes.");
			return;
		}

		client.getData().lastClaimed = System.currentTimeMillis();

		try (Connection connection = DatabaseUtil.getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM emails WHERE code = ?")) {
				ps.setString(1, message.trim());
				try (ResultSet resultSet = ps.executeQuery()) {
					if(!resultSet.next()) {
						client.sendMessage("This code is invalid.");
						return;
					}

					// check not already claimed double xp
					try (PreparedStatement ps2 = connection.prepareStatement("SELECT * FROM emails WHERE ip = ? OR uuid = ?")) {
						ps2.setString(1, client.connectedFrom);
						ps2.setString(2, client.uuid);
						try(ResultSet resultSet1 = ps2.executeQuery()) {
							if(resultSet1.next()) {
								client.sendMessage("You have already claimed a code.");
								return;
							}
						}
					}

					try (PreparedStatement ps2 = connection.prepareStatement("UPDATE emails SET redeemed = 1, uuid = ?, ip = ? WHERE code = ?")) {
						ps2.setString(1, client.uuid);
						ps2.setString(2, client.connectedFrom);
						ps2.setString(3, message.trim());
						ps2.executeUpdate();
					}

					client.getData().doubleXpTime = (8 * 60 * 60 * 1000) / 600;
					client.sendMessage("You now have 8 hours of in-game time double xp.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getCommandString() {
		return "redeem";
	}

}