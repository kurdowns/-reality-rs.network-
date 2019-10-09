package com.zionscape.server.model.players;

import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.achievements.AchievementProgress;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.npcs.other.MissCheevers;
import com.zionscape.server.model.players.skills.SkillUtils;
import com.zionscape.server.model.players.skills.farming.CompostBin;
import com.zionscape.server.model.players.skills.farming.Patch;
import com.zionscape.server.model.players.skills.slayer.CurrentTask;
import com.zionscape.server.model.players.skills.slayer.Tasks;
import jnr.ffi.annotations.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerData {

	public int wildernessChestsOpened = 0;
	public boolean[] skillAnnouncement100m = new boolean[30];
	public boolean paidColorChangeFee = false;
	public Map<Integer, Map<Integer, Integer>> itemColors = new HashMap<>();

	public Map<SkillUtils.SkillRequirements, Integer> skillRequirements = new HashMap<>();
	public List<String> trustedIps = new ArrayList<>();
	public long pin;
	public int warriorsGuildRoomTicks = 0;
	public int gnomeGliderDownStatus = 0;
	public int petType;
	public String lastClassPath;
	public int harvestingAmount = 0;
	public List<Item> shopUntradables = new ArrayList<>();
	public int doubleXpTime;
	public MissCheevers.AccountPerk votePerk;
	public int clueScrollSteps = 0;
	public long moneyPouchAmount = 0;
	public Map<Integer, Integer> bossKillCounts = new HashMap<>();
	public int blowpipeCharges = 0;
	public int blowpipeAmmo = 0;
	public int blowpipeAmmoId = 0;
	public int serpentineHelmetCharges = 0;

	public int tridentSwampCharges = 0;
	public int tridentSeaCharges = 0;

	public List<PlayerKill> kills = new ArrayList<>();
	public int pkPoints = 0;
	public int killstreak;

	public int loggedOutZombiePoints = 0;
	public int loggedOutZombieWave = 0;
	public List<Location> loggedOutZombieDoors = new ArrayList<>();

	public int bossKills = 0;
	public int pestControlPoints = 0;
	public boolean respectedMember;
	public long lastClaimed;
	public boolean completedPuzzle = false;
	public boolean splitChat;
	public int brightness = 2;
	//default to chat clan
	public boolean xpLock;
	public String lastClanChat = "chat";
	public long xpCounter = 0;
	public int highestZombieWave = 0;
	public int zombiePoints;
	public boolean receivedSkillingBonus;
	public int energy = 100;
	public boolean completedTutorial = false;
	public List<String> minigamesPlayed = new ArrayList<>();
	public int timedKilledByNpc = 0;
	public int monstersKilled = 0;
	public long minutesOnline = 0;
	public long totalBeersDrank = 0;
	public long totalCoinsThrownDownWell = 0;
	public int totalCompletedDuoSlayerTasks = 0;
	public int totalCompleteTrades = 0;
	public int totalTimesVoted = 0;
	public String titleHexColor = "None";
	public String title = "None";
	public int fightCaveWave = -1;
	public int fightKilnWave = -1;
	public boolean enteredFightKiln = false;
	public long lastOverloadPotion;
	public int impsCaught = 0;
	private Map<Integer, Patch> patches = new HashMap<>();
	private Map<Location, CompostBin> compostBins = new HashMap<>();
	private Map<String, Integer> questProgress = new HashMap<>();
	private Map<Achievements.Types, AchievementProgress> achievementProgress = new HashMap<>();
	private int achievementPoints = 0;
	private int castleWarsGamesPlayed = 0;
	private boolean hadMostCastleWarsKills = false;
	private boolean hadMostCastleWarsCaptures = false;
	private CurrentTask slayerTask = null;
	private int consecutiveSlayerTasks = 0;
	private int slayerPoints = 0;
	private CurrentTask previousSlayerTask = null;
	private List<Tasks> removedSlayerTasks = new ArrayList<>();

	public int getCastleWarsGamesPlayed() {
		return castleWarsGamesPlayed;
	}

	public void setCastleWarsGamesPlayed(int castleWarsGamesPlayed) {
		this.castleWarsGamesPlayed = castleWarsGamesPlayed;
	}

	public boolean isHadMostCastleWarsKills() {
		return hadMostCastleWarsKills;
	}

	public void setHadMostCastleWarsKills(boolean hadMostCastleWarsKills) {
		this.hadMostCastleWarsKills = hadMostCastleWarsKills;
	}

	public boolean isHadMostCastleWarsCaptures() {
		return hadMostCastleWarsCaptures;
	}

	public void setHadMostCastleWarsCaptures(boolean hadMostCastleWarsCaptures) {
		this.hadMostCastleWarsCaptures = hadMostCastleWarsCaptures;
	}

	public Map<Achievements.Types, AchievementProgress> getAchievementProgress() {
		return achievementProgress;
	}

	public int getAchievementPoints() {
		return achievementPoints;
	}

	public void setAchievementPoints(int points) {
		this.achievementPoints = points;
	}

	public void incrementAchievementPoints(int amount) {
		achievementPoints += amount;
	}

	public CurrentTask getSlayerTask() {
		return slayerTask;
	}

	public void setSlayerTask(CurrentTask slayerTask) {
		this.slayerTask = slayerTask;
	}

	public int getConsecutiveSlayerTasks() {
		return consecutiveSlayerTasks;
	}

	public void setConsecutiveSlayerTasks(int amount) {
		this.consecutiveSlayerTasks = amount;
	}

	public void increaseConsecutiveSlayerTasks(int amount) {
		this.consecutiveSlayerTasks += amount;
	}

	public int getSlayerPoints() {
		return slayerPoints;
	}

	public void setSlayerPoints(int slayerPoints) {
		this.slayerPoints = slayerPoints;
	}

	public List<Tasks> getRemovedSlayerTasks() {
		return removedSlayerTasks;
	}

	public CurrentTask getPreviousSlayerTask() {
		return previousSlayerTask;
	}

	public void setPreviousSlayerTask(CurrentTask previousSlayerTask) {
		this.previousSlayerTask = previousSlayerTask;
	}
	
	private Tasks lastCancelledSlayerTask;

	public Tasks getLastCancelledSlayerTask() {
		return lastCancelledSlayerTask;
	}
	
	public void setLastCancelledSlayerTask(Tasks lastCancelledSlayerTask) {
		this.lastCancelledSlayerTask = lastCancelledSlayerTask;
	}

	public Map<String, Integer> getQuestProgress() {
		return questProgress;
	}

	public Map<Integer, Patch> getPatches() {
		return patches;
	}

	public Map<Location, CompostBin> getCompostBins() {
		return compostBins;
	}
}