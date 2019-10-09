package com.zionscape.server.tools;

import com.google.gson.reflect.TypeToken;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.npcs.NPCSpawn;
import com.zionscape.server.util.Misc;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConvertSpawns {

	public static void main(String[] unused) throws IOException {
		List<NPCSpawn> spawns = Misc.getGson().fromJson(FileUtils.readFileToString(new File("./config/npcs/spawns.json")), new TypeToken<List<NPCSpawn>>() {
		}.getType());


		String[] lines = FileUtils.readFileToString(new File("./autospawn.cfg")).split("\r\n");

		for (String s : lines) {
			if (s.isEmpty() || s.startsWith("//")) {
				continue;
			}

			s = s.replace("spawn = ", "");
			s = s.replace("spawn =\t", "");

			String[] args = s.split("\t");

			System.out.println(s);

			spawns.add(new NPCSpawn(Integer.parseInt(args[0]), Location.create(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3])), true, "chaos tunnels"));
		}

		FileUtils.writeStringToFile(new File("./config/npcs/spawns.json"), Misc.getGson().toJson(spawns));
	}

}
