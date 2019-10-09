package com.zionscape.server.model.content.minigames.clanwars;

import com.zionscape.server.gamecycle.GameCycleTask;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.players.Player;

import java.util.ArrayList;
import java.util.List;

public class War {

	private final List<String> removedUsernames = new ArrayList<>();
	private WarTeam southTeam;
	private WarTeam northTeam;
	private boolean combatRules[] = new boolean[8];
	private boolean settingUp = true;
	private Maps map = Maps.CLASSIC;
	private boolean knockOut = true;
	private Stragglers stragglers = Stragglers.KILL_ALL;
	private int killsRequired = -1;
	private boolean looseItems = false;
	private int timeLimit = -1;
	private int height;
	private int startTimer = 60;
	private int gameTimer = -1;
	private GameCycleTask timer;
	private boolean started;

	protected War(Player clanOwner1, Player clanOwner2) {
		southTeam = new WarTeam(clanOwner1, clanOwner1.clan);
		northTeam = new WarTeam(clanOwner2, clanOwner2.clan);
		this.height = clanOwner1.playerId * 4;
	}

	public boolean[] getCombatRules() {
		return combatRules;
	}

	public boolean isSettingUp() {
		return settingUp;
	}

	public void setSettingUp(boolean settingUp) {
		this.settingUp = settingUp;
	}

	public Maps getMap() {
		return map;
	}

	public void setMap(Maps map) {
		this.map = map;
	}

	public boolean knockOut() {
		return knockOut;
	}

	public void setKnockOut(boolean knockOut) {
		this.knockOut = knockOut;
	}

	public Stragglers getStragglers() {
		return stragglers;
	}

	public void setStragglers(Stragglers stragglers) {
		this.stragglers = stragglers;
	}

	public int getKillsRequired() {
		return killsRequired;
	}

	public void setKillsRequired(int killsRequired) {
		this.killsRequired = killsRequired;
	}

	public boolean looseItems() {
		return looseItems;
	}

	public void setLooseItems(boolean looseItems) {
		this.looseItems = looseItems;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	public int getStartTimer() {
		return startTimer;
	}

	public void setStartTimer(int startTimer) {
		this.startTimer = startTimer;
	}

	public int getHeight() {
		return height;
	}

	public List<String> getRemovedUsernames() {
		return removedUsernames;
	}

	public int getGameTimer() {
		return gameTimer;
	}

	public void setGameTimer(int gameTimer) {
		this.gameTimer = gameTimer;
	}

	public GameCycleTask getTimer() {
		return timer;
	}

	public void startTimer() {
		final War war = this;
		timer = new GameCycleTask() {
			@Override
			public void execute(GameCycleTaskContainer container) {
				if (startTimer > 0) {
					startTimer--;
				}

				if (startTimer == 0) {
					started = true;
					if (timeLimit == -1) {
						stop();
						return;
					} else {
						gameTimer = timeLimit;
					}
				}

				if (gameTimer > 0) {
					gameTimer--;
				}

				if (gameTimer == 0) {
					ClanWars.endGame(war);
					stop();
					return;
				}

			}
		};
		GameCycleTaskHandler.addEvent(null, timer, 2);
	}

	public boolean isStarted() {
		return started;
	}

	public WarTeam getSouthTeam() {
		return southTeam;
	}

	public WarTeam getNorthTeam() {
		return northTeam;
	}

}