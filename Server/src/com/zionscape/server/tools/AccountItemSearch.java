package com.zionscape.server.tools;

import com.zionscape.server.Server;
import com.zionscape.server.util.DatabaseUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountItemSearch {

	private static final int ITEM_ID = 11695;
	private static final int AMOUNT = 10;

	private static int gold;

	public static void main(String[] unused) throws Exception {

		Server.loadSettings();
		DatabaseUtil.init();

		try (PreparedStatement ps = DatabaseUtil.getConnection().prepareStatement("SELECT username, data FROM players WHERE data LIKE ?")) {
			ps.setString(1, "%" + ITEM_ID + "%");
			try (ResultSet results = ps.executeQuery()) {
				while (results.next()) {
					String[] lines = results.getString("data").split("\n");

					for (String s : lines) {
						if (s.startsWith("character-equip") || s.startsWith("item") || s.startsWith("character-bank")) {
							String[] args = s.split("\t");
							if (args[1].trim().equalsIgnoreCase(Integer.toString(ITEM_ID)) && Integer.parseInt(args[2].trim()) >= AMOUNT) {
								System.out.println(results.getString("username") + " " + s);
							}
						}
					}
				}
			}
		}


	}

}
