package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;
import com.zionscape.server.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Claim implements Command {

	@Override
	public void execute(Player client, String message) {

		if (client.ironman) {
			return;
		}

		if (System.currentTimeMillis() - client.getData().lastClaimed < 5 * 60 * 1000) {
			client.sendMessage("You may try to claim points every 5 minutes.");
			return;
		}

		client.getData().lastClaimed = System.currentTimeMillis();
		try (Connection connection = DatabaseUtil.getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM purchases WHERE username = ? AND received = 0")) {
				ps.setString(1, client.username);
				try (ResultSet resultSet = ps.executeQuery()) {
					int amount = 0;

					while (resultSet.next()) {

						try (PreparedStatement ps2 = connection.prepareStatement("UPDATE purchases SET received = 1, received_at = NOW() WHERE id = ?")) {
							ps2.setInt(1, resultSet.getInt("id"));
							ps2.executeUpdate();
						}

						client.isMember = 1;
						client.donatorPoint += resultSet.getInt("quantity");
						client.sendMessage("You have claimed " + resultSet.getInt("quantity") + " Donation points.");
						client.sendMessage("You now have a total of " + client.donatorPoint + " Donation points.");

						amount++;
					}

					if (amount == 0) {
						client.sendMessage("You have no points to claim.");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getCommandString() {
		return "claim";
	}

}