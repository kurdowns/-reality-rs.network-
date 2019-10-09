package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;
import com.zionscape.server.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClaimVotes implements Command {

	@Override
	public void execute(Player client, String message) {
		if (System.currentTimeMillis() - client.getData().lastClaimed < 3 * 60 * 1000) {
			client.sendMessage("You may try to claim points every 3 minutes.");
			return;
		}

		client.getData().lastClaimed = System.currentTimeMillis();
		try (Connection connection = DatabaseUtil.getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM vote_points WHERE player_id = ? AND received = 0")) {
				ps.setInt(1, client.getDatabaseId());
				try (ResultSet resultSet = ps.executeQuery()) {
					int amount = 0;

					while (resultSet.next()) {

						try (PreparedStatement ps2 = connection.prepareStatement("UPDATE vote_points SET received = 1 WHERE id = ?")) {
							ps2.setInt(1, resultSet.getInt("id"));
							ps2.executeUpdate();
						}

						client.votePoints += 1;

						Achievements.progressMade(client, Achievements.Types.VOTE_5_TIMES);
						Achievements.progressMade(client, Achievements.Types.VOTE_20_TIMES);
						Achievements.progressMade(client, Achievements.Types.VOTE_100_TIMES);

						client.getItems().addItem(995, 1_000_000);

						amount++;
					}

					if (amount == 0) {
						client.sendMessage("You have no points to claim.");
					} else {
						client.sendMessage("@red@You have been credited " + amount + " vote point/s and " + amount + "x1m in your bank.");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getCommandString() {
		return "claimvotes";
	}

}