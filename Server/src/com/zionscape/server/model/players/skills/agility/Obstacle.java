package com.zionscape.server.model.players.skills.agility;

public class Obstacle {

	private int objectId;
	private Course course;
	private int xp;
	private int[] position;
	private ObstacleType type;
	private int[][] startingCoords;
	private int[][] endingCoords;

	public Obstacle(int objectId, Course course, int xp, int[] position, ObstacleType type, int[][] startingCoords, int[][] endingCoords) {
		this.objectId = objectId;
		this.course = course;
		this.xp = xp;
		this.position = position;
		this.type = type;
		this.startingCoords = startingCoords;
		this.endingCoords = endingCoords;
	}

	public int getObjectId() {
		return objectId;
	}

	public Course getCourse() {
		return course;
	}

	public int getXP() {
		return xp;
	}

	public int[] getPosition() {
		return position;
	}

	public ObstacleType getType() {
		return type;
	}

	public int[][] getStartingCoordinate() {
		return startingCoords;
	}

	public int[][] getEndingCoordinate() {
		return endingCoords;
	}

}
