package com.zionscape.server.util;

import java.util.HashMap;
import java.util.Map;

public final class StopWatch {

	private Map<String, Integer> times = new HashMap<>();
	private long timestamp;

	/**
	 *
	 */
	public StopWatch() {

	}

	/**
	 *
	 */
	public void start() {
		timestamp = System.currentTimeMillis();
	}

	/**
	 * @param name
	 */
	public void stop(String name) {
		times.put(name, (int) (System.currentTimeMillis() - timestamp));
	}

	/**
	 *
	 */
	public void reset() {
		times.clear();
	}

	/**
	 *
	 */
	public void print() {
		for (Map.Entry<String, Integer> entry : times.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
	}

}
