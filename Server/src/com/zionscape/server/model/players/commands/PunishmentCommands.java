package com.zionscape.server.model.players.commands;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.util.DatabaseUtil;

import java.sql.*;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PunishmentCommands {

	public static final int MUTE = 0;
	public static final int IPMUTE = 1;
	public static final int BAN = 2;
	public static final int IPBAN = 3;
	public static final int UIDBAN = 4;
	public static final int IPJAIL = 5;

	public static boolean handleCommand(Player player, String command) {

		if (!command.contains("ban") && !command.contains("mute") && !command.contains("ipjail")) {
			return false;
		}

		if (player.rights == 0) {
			return false;
		}

		// check a playername has actually been provided
		if (!command.contains(" ")) {
			return false;
		}

		// check the player is online
		Player target = null;
		Map<String, String> details = null;
		String[] args;

		if (command.contains(",")) {
			String str = command.substring(command.indexOf(" ") + 1);
			String[] split = str.split(",");

			target = PlayerHandler.getPlayer(split[0].trim());
			if (target == null) {
				Optional<Map<String, String>> optional = getOfflinePlayerDetails(split[0].trim());
				if (optional.isPresent()) {
					details = optional.get();
				}
			} else {
				details = new HashMap<>();
				details.put("username", target.username);
				details.put("ip", target.connectedFrom);
				details.put("uuid", target.uuid);
			}

			args = new String[split.length - 1];
			for (int i = 1; i < split.length; i++) {
				args[i - 1] = split[i].trim();
			}
		} else {
			target = PlayerHandler.getPlayer(command.substring(command.indexOf(" ") + 1));
			if (target == null) {
				Optional<Map<String, String>> optional = getOfflinePlayerDetails(command.substring(command.indexOf(" ") + 1));
				if (optional.isPresent()) {
					details = optional.get();
				}
			} else {
				details = new HashMap<>();
				details.put("username", target.username);
				details.put("ip", target.connectedFrom);
				details.put("uuid", target.uuid);
			}

			args = new String[0];
		}

		// check rights
		/*
		 * if (target != null && client.playerRights <= target.playerRights) {
		 * client
		 * .sendMessage("You do not have the rights to punish this player!");
		 * return false; }
		 */

		// assistant+
		if (command.startsWith("mute ")) {

			if (args.length < 2) {
				player.sendMessage("Incorrect usage - ::mute username, time, reason");
				return false;
			}

			if (details == null) {
				player.sendMessage("No such player could be found!");
				return false;
			}

			punishPlayer(player, details.get("username"), details.get("ip"), details.get("uuid"), MUTE, getExpireDate(args[0]), args[1]);
			player.sendMessage("You have muted " + details.get("username") + ".");

			if (target != null) {
				target.muted = true;
				target.sendMessage("You have been muted by " + player.username + ".");
				target.getPA().sendFrame126("@or1@Is Muted:@whi@ " + (target.muted ? "Yes" : "No"), 54434);
			}
			return true;
		}

		// mod+
		if (command.startsWith("unmute ")) {
			unpunishPlayer(player, command.substring(7), MUTE);
			if (target != null) {
				target.muted = false;
				target.sendMessage("You have been umuted by " + player.username + ".");
			}
			return true;
		}

		// mod+
		if (command.startsWith("ipjail ") && command.length() > 7) {
			if (player.rights < 1) {
				return false;
			}
			if (args.length < 1) {
				player.sendMessage("Incorrect usage - ::ipjail username, time");
				return false;
			}
			if (details == null) {
				player.sendMessage("No such player could be found!");
				return false;
			}
			punishPlayer(player, details.get("username"), details.get("ip"), details.get("uuid"), IPJAIL, null, args[0]);
			if (target != null) {
				target.jailTimer = Long.MAX_VALUE;
				target.sendMessage("You have been jailed by " + player.username + ".");
				target.getPA().movePlayer(2602, 4775, 0);
			}
			player.sendMessage("You have IP jailed " + details.get("username") + ".");
			return true;
		}

		// admin+
		if (command.startsWith("unipjail ") && command.length() > 7) {
			if (player.rights < 1) {
				return false;
			}
			unpunishPlayer(player, command.substring(9), IPJAIL);
			return true;
		}

		// mod+
		if (command.startsWith("ipmute ")) {

			if (args.length < 2) {
				player.sendMessage("Incorrect usage - ::ipmute username, time, reason");
				return false;
			}

			if (player.rights < 1) {
				return false;
			}

			if (details == null) {
				player.sendMessage("No such player could be found!");
				return false;
			}

			punishPlayer(player, details.get("username"), details.get("ip"), details.get("uuid"), IPMUTE, getExpireDate(args[0]), args[1]);
			player.sendMessage("You have IP muted " + details.get("username") + ".");
			if (target != null) {
				target.muted = true;
				target.sendMessage("You have been muted by " + player.username + ".");
				target.getPA().sendFrame126("@or1@Is Muted:@whi@ " + (target.muted ? "Yes" : "No"), 54434);
			}
			return true;
		}

		// mod+
		if (command.startsWith("unipmute ")) {
			if (player.rights < 2) {
				return false;
			}
			unpunishPlayer(player, command.substring(9), IPMUTE);
			if (target != null) {
				target.muted = false;
				target.sendMessage("You have been umuted by " + player.username + ".");
			}
			return true;
		}

		// headmod+
		if (command.startsWith("ban ")) {

			if (args.length < 2) {
				player.sendMessage("Incorrect usage - ::ban username, time, reason");
				return false;
			}

			if (player.rights < 1) {
				return false;
			}
			if (details == null) {
				player.sendMessage("No such player could be found!");
				return false;
			}
			punishPlayer(player, details.get("username"), details.get("ip"), details.get("uuid"), BAN, getExpireDate(args[0]), args[1]);
			if (target != null) {
				target.setDisconnected(true, "banned");
				target.properLogout = true;
			}
			player.sendMessage("You have banned " + details.get("username") + ".");
			return true;
		}

		// headmod+
		if (command.startsWith("unban ")) {
			if (player.rights < 1) {
				return false;
			}
			unpunishPlayer(player, command.substring(6), BAN);
			return true;
		}

		// admin+
		if (command.startsWith("ipban ")) {

			if (args.length < 1) {
				player.sendMessage("Incorrect usage - ::ipban username, reason");
				return false;
			}

			if (player.rights < 3) {
				return false;
			}
			if (details == null) {
				player.sendMessage("No such player could be found!");
				return false;
			}
			punishPlayer(player, details.get("username"), details.get("ip"), details.get("uuid"), IPBAN, null, args[0]);
			if (target != null) {
				target.setDisconnected(true, "banned");
				target.properLogout = true;
			}
			player.sendMessage("You have IP banned " + details.get("username") + ".");
			return true;
		}



		// admin+
		if (command.startsWith("unipban ")) {
			if (player.rights < 3) {
				return false;
			}
			unpunishPlayer(player, command.substring(8), IPBAN);
			return true;
		}

		// admin+
		if (command.startsWith("uidban ") && command.length() > 8) {
			if (player.rights < 3) {
				return false;
			}
			if (args.length < 1) {
				player.sendMessage("Incorrect usage - ::uidban username, reason");
				return false;
			}
			if (details == null) {
				player.sendMessage("No such player could be found!");
				return false;
			}
			punishPlayer(player, details.get("username"), details.get("ip"), details.get("uuid"), UIDBAN, null, args[0]);
			if (target != null) {
				target.properLogout = true;
				target.setDisconnected(true, "banned");
			}
			player.sendMessage("You have GUID banned " + details.get("username") + ".");
			return true;
		}

		// admin+
		if (command.startsWith("unuidban ") && command.length() > 7) {
			if (player.rights < 3) {
				return false;
			}
			unpunishPlayer(player, command.substring(10), UIDBAN);
			return true;
		}


		return false;
	}

	private static void unpunishPlayer(Player client, String name, int type) {
		try (Connection connection = DatabaseUtil.getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM punishments WHERE username = ? AND type = ? LIMIT 1")) {
				ps.setString(1, name);
				ps.setInt(2, type);
				try (ResultSet results = ps.executeQuery()) {
					if (results.next()) {
						try (PreparedStatement ps2 = connection.prepareStatement("DELETE FROM punishments WHERE username = ? AND type = ?")) {
							ps2.setString(1, name);
							ps2.setInt(2, type);
							ps2.execute();
							client.sendMessage("Punishment removed from " + name + ".");
						}
					} else {
						client.sendMessage("This player is not currently punished with that type of punishment.");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void punishPlayer(Player client, String username, String ip, String uuid, int level, ZonedDateTime date, String reason) {
		try (Connection connection = DatabaseUtil.getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement("INSERT INTO punishments (username, ip, type, whodidit, uuid, expires, reason) VALUES(?, ?, ?, ?, ?, ?, ?)")) {
				ps.setString(1, username);
				ps.setString(2, ip);
				ps.setInt(3, level);
				if (client == null) {
					ps.setString(4, "Server");
				} else {
					ps.setString(4, client.username);
				}
				ps.setString(5, uuid);
				ps.setTimestamp(6, date == null ? null : new Timestamp(Date.from(date.toInstant()).getTime()));
				ps.setString(7, reason);
				ps.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static int getPunishment(String username, String ip, String guid) {
		try (Connection connection = DatabaseUtil.getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM punishments WHERE username = ? OR ip = ? OR uuid = ? ORDER BY type DESC")) {
				ps.setString(1, username);
				ps.setString(2, ip);
				ps.setString(3, guid);
				try (ResultSet results = ps.executeQuery()) {
					while (results.next()) {

						if (results.getInt("expired") == 1) {
							continue;
						}

						byte type = results.getByte("type");
						if (type == BAN || type == MUTE) {
							if (results.getString("username").equalsIgnoreCase(username)) {
								return results.getByte("type");
							}
						} else if (type == IPMUTE) {
							if (results.getString("username").equalsIgnoreCase(username) || results.getString("ip").equals(ip)) {
								return results.getByte("type");
							}
						} else {
							return results.getByte("type");
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	private static ZonedDateTime getExpireDate(String argument) {
		argument = argument.toLowerCase();

		ZonedDateTime now = ZonedDateTime.now();

		if (argument.endsWith("h")) {
			now = now.plusHours(Integer.parseInt(argument.replace("h", "")));
		} else if (argument.endsWith("m")) {
			now = now.plusMinutes(Integer.parseInt(argument.replace("m", "")));
		} else if (argument.endsWith("d")) {
			now = now.plusDays(Integer.parseInt(argument.replace("d", "")));
		} else {
			now = now.plusDays(Integer.parseInt(argument));
		}

		return now;
	}

	private static Optional<Map<String, String>> getOfflinePlayerDetails(String username) {
		try (Connection connection = DatabaseUtil.getConnection()) {

			try (PreparedStatement ps = connection.prepareStatement("SELECT username, id FROM players where username = ?")) {
				ps.setString(1, username);
				try (ResultSet playerResult = ps.executeQuery()) {
					if (!playerResult.next()) {
						return Optional.empty();
					}

					try (PreparedStatement ps2 = connection.prepareStatement("SELECT * FROM player_login_history WHERE player_id = ? ORDER BY id DESC LIMIT 1")) {
						ps2.setInt(1, playerResult.getInt("id"));
						try (ResultSet loginResult = ps2.executeQuery()) {
							if (!loginResult.next()) {
								return Optional.empty();
							}

							Map<String, String> details = new HashMap<>();
							details.put("ip", loginResult.getString("ip"));
							details.put("uuid", loginResult.getString("uuid"));
							details.put("username", playerResult.getString("username"));

							return Optional.of(details);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return Optional.empty();
	}

}