package com.zionscape.server.model.players.skills.slayer;

public class CurrentTask {

	private final Tasks task;
	private final TaskLevels level;
	private int amount;
	private String partner;

	public CurrentTask(int amount, Tasks task, TaskLevels level) {
		this.amount = amount;
		this.task = task;
		this.level = level;
	}

	public CurrentTask(int amount, Tasks task, TaskLevels level, String partner) {
		this.amount = amount;
		this.task = task;
		this.level = level;
		this.partner = partner;
	}

	public Tasks getTask() {
		return task;
	}

	public TaskLevels getLevel() {
		return level;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void decreaseAmount(int amount) {
		this.amount -= amount;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

}