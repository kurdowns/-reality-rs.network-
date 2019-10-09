package com.zionscape.server.tools;

import com.zionscape.server.Server;
import com.zionscape.server.util.DatabaseUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Timeonline {

	private static final int ITEM_ID = 11695;
	private static final int AMOUNT = 10;

	private static int gold;

	public static void main(String[] unused) throws Exception {

		Server.loadSettings();
		DatabaseUtil.init();

		int accounts = 0;
		long minutesOnline = 0;

		try (PreparedStatement ps = DatabaseUtil.getConnection().prepareStatement("select *from players where last_updated > DATE_SUB(NOW(), INTERVAL 60 DAY)")) {
			try (ResultSet results = ps.executeQuery()) {
				while (results.next()) {
					String[] lines = results.getString("data").split("\n");

					accounts++;

					for (String s : lines) {
						if(s.contains("minutesOnline")) {
							s = s.replace("  \"minutesOnline\": ", "").replace(",", "");
							try {
								minutesOnline += Long.parseLong(s);
							} catch (Exception e) {

							}
						}
					}
				}
			}

			System.out.println("average: " + (minutesOnline / (long)accounts));
		}


	}

}
