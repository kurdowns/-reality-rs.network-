package com.zionscape.server.model.players.skills.agility;

import com.zionscape.server.model.players.Player;

public class Agility {

	public static boolean entryGate = false;
	public static boolean otherGate = false;

	public static boolean handleObjectClick(final Player player, int objectId, int objX, int objY) {

		return false;
	}

	public enum Obstacle {
		WILDPIPE(2288, 3004, 3935, 3004, 3938, 0, 0, 3004, 3950, 844, 3100, 0, false, true, false, true, 3004, 3937, "You pulled yourself through the pipes.", null),
		ROPE(2283, 3004, 3953, 3006, 3954, 0, 0, 3005, 3958, 751, 4100, 1, true, false, false, false, 3005, 3953, "You skilfully swing across.", null),
		STEP(1, 3001, 3960, 3005, 3960, 0, 0, 2996, 3960, 741, 5100, 2, false, false, false, true, 3002, 3960, "You carefully start crossing the stepping stones..", "... and reach the other side safely."),
		LOG(2297, 3002, 3945, 3002, 3945, 0, 0, 2994, 3945, 762, 5100, 3, false, false, false, true, 3002, 3945, "You walk carefully across the slippery log...", "You skilfully edge across the gap."),
		WALL(2328, 2994, 3935, 2994, 3938, 0, 0, 2994, 3932, 2328, 4100, 4, false, false, true, true, 2994, 3937, null, null);
		public boolean isWild;
		private int objectId, lowestStartX, lowestStartY, highestStartX, highestStartY, startZ, endX, endY, endZ,
				animation, startAnimation, endAnimation, xp, stage, bestStartX, bestStartY;
		private String firstText, secondText;
		private boolean climbingObstacle = false, pipe = false, resetAfter = false, doOnWalk = false;

		Obstacle(int objectId, int lowestStartX, int lowestStartY, int highestStartX, int highestStartY, int startZ,
				 int endZ, int endX, int endY, int animation, int xp, int stage, boolean climbingObstacle, boolean pipe,
				 boolean resetAfter, boolean doOnWalk, int bestStartX, int bestStartY, String firstText,
				 String secondText) {
			this.objectId = objectId;
			this.lowestStartX = lowestStartX;
			this.lowestStartY = lowestStartY;
			this.highestStartX = highestStartX;
			this.highestStartY = highestStartY;
			this.startZ = startZ;
			this.endZ = endZ;
			this.endX = endX;
			this.endY = endY;
			this.animation = animation;
			this.startAnimation = animation;
			this.xp = xp;
			this.stage = stage;
			this.climbingObstacle = climbingObstacle;
			this.pipe = pipe;
			this.resetAfter = resetAfter;
			this.doOnWalk = doOnWalk;
			this.bestStartX = bestStartX;
			this.bestStartY = bestStartY;
			this.firstText = firstText;
			this.secondText = secondText;
			isWild = true;
		}

		Obstacle(boolean doOnWalk, int objectId, int lowestStartX, int lowestStartY, int highestStartX,
				 int highestStartY, int endX, int endY, int height, int animation, int xp, int stage) {
			this.doOnWalk = true;
			this.objectId = objectId;
			this.lowestStartX = lowestStartX;
			this.lowestStartY = lowestStartY;
			this.highestStartX = highestStartX;
			this.highestStartY = highestStartY;
			this.startZ = height;
			this.endX = endX;
			this.endY = endY;
			this.animation = animation;
			this.xp = xp;
			this.stage = stage;
		}

		Obstacle(int objectId, int lowestStartX, int lowestStartY, int highestStartX, int highestStartY, int startZ,
				 int endX, int endY, int endZ, int animation, int xp, int stage) {
			this.objectId = objectId;
			this.lowestStartX = lowestStartX;
			this.lowestStartY = lowestStartY;
			this.highestStartX = highestStartX;
			this.highestStartY = highestStartY;
			this.startZ = startZ;
			this.endX = endX;
			this.endY = endY;
			this.endZ = endZ;
			this.animation = animation;
			this.xp = xp;
			this.climbingObstacle = true;
			this.stage = stage;
		}

		Obstacle(boolean doOnWalk, int objectId, int lowestStartX, int lowestStartY, int highestStartX,
				 int highestStartY, int endX, int endY, int height, int animation, int xp, int stage, boolean pipe) {
			this.objectId = objectId;
			this.lowestStartX = lowestStartX;
			this.lowestStartY = lowestStartY;
			this.highestStartX = highestStartX;
			this.highestStartY = highestStartY;
			this.startZ = height;
			this.endX = endX;
			this.endY = endY;
			this.animation = animation;
			this.xp = xp;
			this.stage = stage;
			this.pipe = pipe;
			if (pipe) {
				resetAfter = true;
			}
			this.doOnWalk = doOnWalk;
		}

		public int getBestStartX() {
			return bestStartX;
		}

		public int getBestStartY() {
			return bestStartY;
		}

		public String getFirstText() {
			return firstText;
		}

		public String getSecondText() {
			return secondText;
		}

		public boolean getDoOnWalk() {
			return doOnWalk;
		}

		public boolean isResetAfter() {
			return resetAfter;
		}

		public int getStage() {
			return stage;
		}

		public int getStartAnimation() {
			return startAnimation;
		}

		public int getEndAnimation() {
			return endAnimation;
		}

		public boolean isPipe() {
			return pipe;
		}

		public int getLowestStartX() {
			return lowestStartX;
		}

		public int getLowestStartY() {
			return lowestStartY;
		}

		public int getHighestStartX() {
			return highestStartX;
		}

		public int getHighestStartY() {
			return highestStartY;
		}

		public boolean isClimbingObstacle() {
			return climbingObstacle;
		}

		public int getObjectId() {
			return objectId;
		}

		public int getStartZ() {
			return startZ;
		}

		public int getEndX() {
			return endX;
		}

		public int getEndY() {
			return endY;
		}

		public int getEndZ() {
			return endZ;
		}

		public int getAnimation() {
			return animation;
		}

		public int getXp() {
			return xp;
		}
	}
}