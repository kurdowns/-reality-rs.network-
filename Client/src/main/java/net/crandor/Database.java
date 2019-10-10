package net.crandor;

import java.util.Base64;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
/*
*//**
 * Created by Stuart on 03/09/2016.
 */
public class Database {
/*
    private static final String URL_LOCATION = "http://185.218.235.14:43597/";

    private static Map<String, String> checksums = new HashMap<>();

    public static void init() throws Exception {

        URL yahoo = new URL(URL_LOCATION + "checksums");
        URLConnection conn = yahoo.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String[] args = inputLine.split("::");

                checksums.put(args[0], args[1]);
            }
        }

        File file = new File(Constants.CACHE_FOLDER + "crandor.db");
        if (file.exists()) {
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + Constants.CACHE_FOLDER + "crandor.db")) {
            try (PreparedStatement ps = connection.prepareStatement("CREATE TABLE files (id INT PRIMARY KEY, name TEXT, md5 TEXT, data BLOB)")) {
                ps.execute();
            }
        }
    }

    public static byte[] getFile(String file) {

        if (!checksums.containsKey(file)) {
            return null;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + Constants.CACHE_FOLDER + "crandor.db")) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM files WHERE name = ?")) {
                ps.setString(1, file);
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        byte[] b = resultSet.getBytes("data");
                        try {
                            if (getChecksum(b).equalsIgnoreCase(checksums.get(file))) {
                                return b;
                            } else {
                                try (PreparedStatement ps1 = connection.prepareStatement("DELETE FROM files WHERE id = ?")) {
                                    ps1.setInt(1, resultSet.getInt("id"));
                                    ps1.execute();
                                }
                            }
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            try {
                URL yahoo = new URL(URL_LOCATION + "files/" + file);
                URLConnection conn = yahoo.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

                int available;
                byte[] buff = new byte[1024];
                try (ByteArrayOutputStream bais = new ByteArrayOutputStream()) {
                    try (InputStream is = conn.getInputStream()) {
                        while ((available = is.read(buff)) > 0) {
                            bais.write(buff, 0, available);
                        }
                    }

                    byte[] b = bais.toByteArray();
                    try (PreparedStatement ps = connection.prepareStatement("INSERT INTO files (name, md5, data) VALUES(?, ?, ?)")) {
                        try {
                            ps.setString(1, file);
                            ps.setString(2, getChecksum(b));
                            ps.setBytes(3, b);
                            ps.execute();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                    }

                    return b;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getChecksum(byte buff[]) throws NoSuchAlgorithmException {
        byte[] hash = MessageDigest.getInstance("MD5").digest(buff);
        return Base64.getEncoder().encodeToString(hash);
    }

}
*/
}