package com.zionscape.server.model.content.minigames.quests;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.util.Misc;

import java.util.ArrayList;
import java.util.List;

public class QuestInterfaceGenerator {

	private final List<String> content = new ArrayList<>();
	private final String questName;
	private final int status;

	public QuestInterfaceGenerator() {
		questName = "";
		status = -1;
	}

	public QuestInterfaceGenerator(String questName) {
		this.questName = questName;
		this.status = -1;
	}

	public QuestInterfaceGenerator(String questName, int status) {
		this.questName = questName;
		this.status = status;
	}


	/**
	 * Add text to the quest
	 *
	 * @param status         The current status
	 * @param requiredStatus The status required
	 * @param line           The text to add
	 */
	public void add(int status, int requiredStatus, String line) {
		boolean display = false;
		if (status >= requiredStatus) {
			display = true;
		}
		if (display) {
			boolean str = false;
			if (requiredStatus < status) {
				str = true;
			}
			String[] lines = Misc.wrapText(line, 50);
			for (String l : lines) {
				content.add((str ? "<str>" : "") + l);
			}
		}
	}

	/**
	 * Add text to the quest
	 *
	 * @param requiredStatus The status required
	 * @param line           The text to add
	 */
	public void add(int requiredStatus, String line) {
		boolean display = false;
		if (status >= requiredStatus) {
			display = true;
		}
		if (display) {
			boolean str = false;
			if (requiredStatus < status) {
				str = true;
			}
			String[] lines = Misc.wrapText(line, 50);
			for (String l : lines) {
				content.add((str ? "<str>" : "") + l);
			}
		}
	}

	/**
	 * Directly add text to the quest
	 *
	 * @param line The text
	 */
	public void add(final String line) {
		String[] lines = Misc.wrapText(line, 50);
		for (String l : lines)
			content.add(l);
	}

	/**
	 * Add text to the quest
	 *
	 * @param line The text
	 * @param str  Strike through
	 */
	public void add(String line, boolean str) {
		String[] lines = Misc.wrapText(line, 50);
		for (String l : lines)
			content.add((str ? "<str>" : "") + l);
	}

	/**
	 * Adds a line break
	 */
	public void lineBreak() {
		content.add("");
	}

	/**
	 * Get the content as an String Array
	 *
	 * @return the array
	 */
	public String[] getContent() {
		String[] array = new String[content.size()];
		content.toArray(array);
		return array;
	}

	/**
	 * Writes the quest interface to the player with a defined quest name
	 *
	 * @param player The player
	 * @param name   The quest name
	 */
	public void writeQuest(Player player, String name) {
		player.getPA().sendFrame126(name, 8144);
		player.getPA().sendFrame126("", 8145);
		int line = 18837;
		for (String str : content) {
			player.getPA().sendFrame126(str, line++);
		}
		for (int i = line; i < 19004; i++)
			player.getPA().sendFrame126("", i);
		player.getPA().showInterface(8134);
		player.flushOutStream();
	}

	/**
	 * Writes the quest interface to the player using the classes set quest name
	 *
	 * @param player The player
	 */
	public void writeQuest(Player player) {
		player.getPA().sendFrame126(questName, 8144);
		player.getPA().sendFrame126("", 8145);
		int line = 18837;
		for (String str : content) {
			player.getPA().sendFrame126(str, line++);
		}
		for (int i = line; i < 19004; i++)
			player.getPA().sendFrame126("", i);
		player.getPA().showInterface(8134);
		player.flushOutStream();
	}

}
