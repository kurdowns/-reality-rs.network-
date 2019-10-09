package com.zionscape.server.model.players.skills.farming;

public class Patch {

	private State state = State.GROWING;

	private Seeds seedType;
	private int seed;
	private Compost treatedWith;
	private long lastUpdated;

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Compost getTreatedWith() {
		return treatedWith;
	}

	public void setTreatedWith(Compost treatedWith) {
		this.treatedWith = treatedWith;
	}

	public void reset() {
		lastUpdated = System.currentTimeMillis();
		setSeedType(null);
		setState(State.GROWING);
		seed = 0;
		treatedWith = null;
	}

	public long getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Seeds getSeedType() {
		return seedType;
	}

	public void setSeedType(Seeds seedType) {
		this.seedType = seedType;
	}

}
