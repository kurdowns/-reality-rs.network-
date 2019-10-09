package com.zionscape.server.model.content.achievements;

import java.util.List;

public class AchievementProgress {

	private String str;
	private int i;
	private Object o;
	private List<Integer> list;

	public AchievementProgress(String str) {
		this.str = str;
	}

	public AchievementProgress(int i) {
		this.i = i;
	}

	public AchievementProgress(Object o) {
		this.o = o;
	}

	public AchievementProgress(List<Integer> list) {
		this.list = list;
	}

	public String getString() {
		return str;
	}

	public int getInteger() {
		return i;
	}

	public Object getObject() {
		return o;
	}

	public List<Integer> getList() {
		return list;
	}

	public void setInteger(int i) {
		this.i = i;
	}
}