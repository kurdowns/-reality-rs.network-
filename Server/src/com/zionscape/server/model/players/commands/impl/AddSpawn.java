package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.commands.Command;
import com.zionscape.server.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddSpawn implements Command {

	@Override
	public void execute(Player client, String message) {
		if (client.rights < 3) {
			return;
		}

		try {
			int npcId = Integer.parseInt(message);
			try (Connection connection = DatabaseUtil.getConnection()) {
				try(PreparedStatement ps = connection.prepareStatement("INSERT INTO npc_spawns(npc_id, x, y, z) VALUES(?, ?, ?, ?)")) {
					ps.setInt(1, npcId);
					ps.setInt(2, client.getX());
					ps.setInt(3, client.getY());
					ps.setInt(4, client.heightLevel);
					ps.execute();
				}
				client.sendMessage("spawn added " + npcId + " at " + client.getLocation());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getCommandString() {
		return "addspawn";
	}

}
