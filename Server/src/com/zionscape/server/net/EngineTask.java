package com.zionscape.server.net;

import java.util.concurrent.Callable;

public abstract class EngineTask implements Callable<Object> {

	public Object call() throws Exception {
		this.performTask();
		return null;
	}

	public abstract void performTask();
}
