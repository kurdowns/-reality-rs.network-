package com.zionscape.server.model.players.skills.farming;

public class CompostBin {

	private int vegationCount;
	private boolean closed = false;
	private long closedAt;
	private boolean compost;

	public int getVegationCount() {
		return vegationCount;
	}

	public void setVegationCount(int vegationCount) {
		this.vegationCount = vegationCount;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {

		if (closed) {
			closedAt = System.currentTimeMillis();
		}

		this.closed = closed;
	}

	public long getClosedAt() {
		return closedAt;
	}

	public void reset() {
		closedAt = 0;
		closed = false;
		vegationCount = 0;
		compost = false;
	}

	public boolean isCompost() {
		return compost;
	}

	public void setCompost(boolean compost) {
		this.compost = compost;
	}

}
