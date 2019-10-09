package com.zionscape.server.model.items;

public class Bindable {

	private int bindedID, unBindedID;

	public Bindable(int bindedID, int unBindedID) {
		super();
		this.bindedID = bindedID;
		this.unBindedID = unBindedID;
	}

	public int getBindedID() {
		return bindedID;
	}

	public int getUnBindedID() {
		return unBindedID;
	}
}
