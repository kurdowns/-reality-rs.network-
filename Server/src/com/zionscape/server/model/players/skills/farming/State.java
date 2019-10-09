package com.zionscape.server.model.players.skills.farming;

import com.google.gson.annotations.SerializedName;

public enum State {

	@SerializedName("GROWING")
	GROWING(0x00),
	@SerializedName("WATERED")
	WATERED(0x01),
	@SerializedName("DISEASED")
	DISEASED(0x02),
	@SerializedName("DEAD")
	DEAD(0x03);

	private final int value;

	State(int value) {
		this.value = value;
	}

	public int toValue() {
		return value;
	}

}
