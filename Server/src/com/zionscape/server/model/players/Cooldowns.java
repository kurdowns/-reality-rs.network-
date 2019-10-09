package com.zionscape.server.model.players;

import com.zionscape.server.gamecycle.GameCycleTask;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;

import java.util.ArrayList;
import java.util.List;

public class Cooldowns {

	private final List<String> cooldowns = new ArrayList<>();
	private Player player;

	public Cooldowns(Player player) {
		this.player = player;
	}

	public void add(String name, int cycles) {
		cooldowns.add(name);
		GameCycleTaskHandler.addEvent(player, new GameCycleTask() {

			@Override
			public void execute(GameCycleTaskContainer container) {
				cooldowns.remove(name);
			}
		}, cycles);
	}

	public boolean exists(String name) {
		return cooldowns.contains(name);
	}

}
