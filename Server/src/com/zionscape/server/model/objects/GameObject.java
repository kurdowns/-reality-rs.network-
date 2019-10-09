package com.zionscape.server.model.objects;

import com.zionscape.server.Server;

public class GameObject {

	public int objectId;
	public int objectX;
	public int objectY;
	public int height;
	public int face;
	public int type;
	public int newId;
	public int tick;

	public GameObject(int id, int x, int y, int height, int face, int type, int newId, int ticks) {
		this.objectId = id;
		this.objectX = x;
		this.objectY = y;
		this.height = height;
		this.face = face;
		this.type = type;
		this.newId = newId;
		this.tick = ticks;
		Server.objectManager.addObject(this);
	}

}