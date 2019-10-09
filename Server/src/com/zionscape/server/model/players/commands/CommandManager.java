package com.zionscape.server.model.players.commands;

import com.google.common.reflect.ClassPath;
import com.zionscape.server.model.players.Player;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {

	private static Map<String, Command> commands = new HashMap<>();

	static {
		try {
			ClassPath classpath = ClassPath.from(ClassLoader.getSystemClassLoader());
			for (ClassPath.ClassInfo classInfo : classpath.getTopLevelClasses("com.zionscape.server.model.players.commands.impl")) {
				Class<?> clazz = Class.forName(classInfo.getName());
				Command command = (Command) clazz.newInstance();
				commands.put(command.getCommandString().toLowerCase(), command);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void execute(Player client, String str) {
		Command command;

		if (str.contains(" ")) {
			command = commands.get(str.split(" ")[0].toLowerCase());
			str = str.substring(str.indexOf(" ") + 1);
		} else {
			command = commands.get(str.toLowerCase());
		}

		if (command == null) {
			return;
		}

		try {
			command.execute(client, str);
		} catch (Exception e) {
			System.out.println(client + " error running command string " + str);
			client.setDisconnected(true, "command exception " + e.getMessage());
			e.printStackTrace();
		}
	}

}
