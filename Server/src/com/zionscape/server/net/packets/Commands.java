package com.zionscape.server.net.packets;

import com.zionscape.server.Server;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.CommandManager;
import com.zionscape.server.model.players.commands.PunishmentCommands;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.DatabaseUtil;
import com.zionscape.server.util.Stream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Commands Class
 *
 * @author Lmcruck30
 * @version 1.0
 */
public class Commands implements PacketType {

	@Override
	public void processPacket(final Player c, int packetType, int packetSize, Stream stream) {
		String playerCommand = "";
		try {
			playerCommand = stream.readString();

			// c.sendMessage("command: " + playerCommand);

		} catch (Exception e) {
			return;
		}
		if (c.isDoingTutorial()) {
			return;
		}

		final String command = playerCommand;

		if(c.rights > 0) {
			Server.submitWork(() -> {
				try(Connection connection = DatabaseUtil.getConnection()) {
					try(PreparedStatement ps = connection.prepareStatement("INSERT INTO staff_logs (player_id, username, ip, guid, command) VALUES(?, ?, ?, ?, ?)")) {
						ps.setInt(1, c.getDatabaseId());
						ps.setString(2, c.username);
						ps.setString(3, c.connectedFrom);
						ps.setString(4, c.uuid);
						ps.setString(5, command);
						ps.execute();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
		}

		if (PunishmentCommands.handleCommand(c, playerCommand)) {
			return;
		}

		CommandManager.execute(c, playerCommand.trim());
	}
}