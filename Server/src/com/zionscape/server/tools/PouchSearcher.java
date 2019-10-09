package com.zionscape.server.tools;

import com.zionscape.server.Server;
import com.zionscape.server.util.DatabaseUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PouchSearcher {

    private static final int ITEM_ID = 11695;
    private static final int AMOUNT = 10;

    private static int gold;

    public static void main(String[] unused) throws Exception {

        Server.loadSettings();
        DatabaseUtil.init();

        try (PreparedStatement ps = DatabaseUtil.getConnection().prepareStatement("SELECT username, data FROM players WHERE last_updated >= DATE(DATE_SUB(NOW(), INTERVAL 30 DAY))")) {
            try (ResultSet results = ps.executeQuery()) {
                while (results.next()) {
                    String[] lines = results.getString("data").split("\n");

                    for (String s : lines) {
                        if (s.contains("moneyPouchAmount")) {
                            long amount = Long.parseLong(s.split(": ")[1].replace(",", ""));
                            if(amount > 5_000_000_000L) {
                                System.out.println(results.getString("username") + " " + amount);
                            }
                        }
                    }
                }
            }
        }


    }

}
