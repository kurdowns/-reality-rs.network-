package com.zionscape.server.model.players.skills.firemaking;

public enum Logs {

	NORMAL(1511, 1, 40, 50, 3098, 11),
	ACHEY(2862, 1, 40, 180, 3098, 12),
	OAK(1521, 15, 60, 85, 3099, 13),
	WILLOW(1519, 30, 90, 105, 3101, 14),
	TEAK(6333, 35, 105, 120, 3098, 15),
	MAPLE(1517, 45, 135, 150, 3100, 16),
	MAHOGANY(6332, 50, 157, 180, 3098, 17),
	YEW(1515, 60, 202, 240, 3111, 18),
	MAGIC(1513, 75, 303, 310, 3135, 19);

	private int id, level, xp, bonfireXp, bonfireGraphic, petIndex;

	Logs(int id, int level, int xp, int bonfireXp, int bonfireGraphic, int petIndex) {
		this.id = id;
		this.level = level;
		this.xp = xp;
		this.bonfireXp = bonfireXp;
		this.bonfireGraphic = bonfireGraphic;
		this.petIndex = petIndex;
	}

	public int getId() {
		return id;
	}

	public int getLevel() {
		return level;
	}

	public int getXp() {
		return xp;
	}

	public int getBonfireXp() {
		return bonfireXp;
	}

	public int getBonfireGraphic() {
		return bonfireGraphic;
	}

	public int getPetIndex() {
		return petIndex;
	}
}