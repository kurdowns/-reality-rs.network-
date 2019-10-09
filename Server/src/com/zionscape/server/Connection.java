package com.zionscape.server;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Connection Check Class
 *
 * @author Ryan / Lmctruck30
 */
public class Connection {

	public static ArrayList<String> starterRecieved1 = new ArrayList<>();
	public static ArrayList<String> starterRecieved2 = new ArrayList<>();

	public static void addIpToStarter1(String IP) {
		starterRecieved1.add(IP);
		Connection.addIpToStarterList1(IP);
	}

	public static void addIpToStarter2(String IP) {
		starterRecieved2.add(IP);
		Connection.addIpToStarterList2(IP);
	}

	public static void addIpToStarterList1(String Name) {
		try (BufferedWriter out = new BufferedWriter(new FileWriter("./data/starters/FirstStarterRecieved.txt", true))) {
			out.newLine();
			out.write(Name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addIpToStarterList2(String Name) {
		try (BufferedWriter out = new BufferedWriter(new FileWriter("./data/starters/SecondStarterRecieved.txt", true))) {
			out.newLine();
			out.write(Name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void appendStarters() {
		try {
			File file = new File("./data/starters/FirstStarterRecieved.txt");

			if (!file.exists()) {
				file.createNewFile();
			}

			String data = null;
			try (BufferedReader in = new BufferedReader(new FileReader(file))) {
				while ((data = in.readLine()) != null) {
					starterRecieved1.add(data);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	public static void appendStarters2() {
		try {

			File file = new File("./data/starters/SecondStarterRecieved.txt");

			if (!file.exists()) {
				file.createNewFile();
			}

			BufferedReader in = new BufferedReader(new FileReader(file));
			String data = null;
			try {
				while ((data = in.readLine()) != null) {
					starterRecieved2.add(data);
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void deleteFromFile(String file, String name) {
		try (BufferedReader r = new BufferedReader(new FileReader(file));
			 BufferedWriter w = new BufferedWriter(new FileWriter(file));) {
			ArrayList<String> contents = new ArrayList<String>();
			while (true) {
				String line = r.readLine();
				if (line == null) {
					break;
				} else {
					line = line.trim();
				}
				if (!line.equalsIgnoreCase(name)) {
					contents.add(line);
				}
			}
			for (String line : contents) {
				w.write(line, 0, line.length());
				w.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean hasRecieved1stStarter(String IP) {
		if (starterRecieved1.contains(IP)) {
			return true;
		}
		return false;
	}

	public static boolean hasRecieved2ndStarter(String IP) {
		if (starterRecieved2.contains(IP)) {
			return true;
		}
		return false;
	}

	/**
	 * Adds the banned usernames and ips from the text file to the ban list
	 */
	public static void initialize() {
		Connection.appendStarters();
		Connection.appendStarters2();
	}

	/**
	 * Saves the logged in IP
	 */
	@SuppressWarnings("resource")
	public static void saveIpToLog(String Name, String IP) {
		try {
			Calendar cal = new GregorianCalendar();
			String am_pm;
			if (cal.get(Calendar.AM_PM) == 0) {
				am_pm = "AM";
			} else {
				am_pm = "PM";
			}
			BufferedWriter LV = new BufferedWriter(new FileWriter("./data/playerIPS/" + Name + ".txt", true));
			LV.write("[" + cal.get(Calendar.DAY_OF_YEAR) + "   " + cal.get(Calendar.HOUR) + ":"
					+ cal.get(Calendar.MINUTE) + "" + am_pm + "]" + IP);
			LV.newLine();
			LV.flush();
		} catch (IOException IOE) {
		}
	}

}