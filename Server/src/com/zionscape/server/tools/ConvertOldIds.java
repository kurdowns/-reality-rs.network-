package com.zionscape.server.tools;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class ConvertOldIds {

	private static final int[][] IDS = {{15037, 18349}, {15038, 18351}, {15039, 18353}, {15040, 18355},
			{15041, 18357}, {15042, 18359}, {15001, 15486}, {15043, 18361},};

	public static void main(String[] args) throws Exception {

		System.out.println("converting ids");

		File dir = new File(System.getProperty("user.home") + "/.zionscape/characters/");
		for (File file : dir.listFiles()) {
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(file);

			StringBuilder sb = new StringBuilder();
			boolean updated = false;

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				if (line.startsWith("character-item") || line.startsWith("character-equip")
						|| line.startsWith("character-bank")) {

					int id = Integer.valueOf(line.split("\t")[1]);

					for (int i = 0; i < IDS.length; i++) {
						if (IDS[i][0] == id) {
							line = line.replace(Integer.toString(id), Integer.toString(IDS[i][1]));
							updated = true;
						}
						if (IDS[i][0] + 1 == id) {
							line = line.replace(Integer.toString(id), Integer.toString(IDS[i][1] + 1));
							updated = true;
						}
					}

				}

				sb.append(line + "\r\n");
			}

			if (updated) {
				try (FileWriter fileWriter = new FileWriter(file)) {
					fileWriter.write(sb.toString());
				}
			}

		}

		System.out.println("finished");

	}

}
