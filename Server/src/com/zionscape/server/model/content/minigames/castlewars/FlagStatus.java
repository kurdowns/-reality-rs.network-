package com.zionscape.server.model.content.minigames.castlewars;

public enum FlagStatus {

	SAFE(0), TAKEN(1), DROPPED(2);

	private int status;

	FlagStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

}