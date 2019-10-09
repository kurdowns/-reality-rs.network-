package com.zionscape.server.model.players.skills.agility;

public enum Course {

	GNOME(1, 6, 500),
	BARBARIAN(35, 7, 1000),
	APE_ATOLL(48, 5, 2000),
	WILDERNESS(52, 4, 3000);

	private int levelRequirement, numberOfObstacles, completionExperience;

	Course(int levelRequirement, int numberOfObstacles, int completionExperience) {
		this.levelRequirement = levelRequirement;
		this.numberOfObstacles = numberOfObstacles;
		this.completionExperience = completionExperience;
	}

	public int getCourseIndex() {
		return ordinal();
	}

	public int getLevelReq() {
		return levelRequirement;
	}

	public int getNumberOfObstacles() {
		return numberOfObstacles;
	}

	public int getCompletionExperience() {
		return completionExperience;
	}

}
