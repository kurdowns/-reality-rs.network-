package com.zionscape.server.model.players.skills.summoning;

public class Familiar {

	private String npcName;
	private int npcId;
	private int levelRequired;
	private int minutes;
	private int pointCost;
	private String specialClassName;
	private Pouch pouch;
	private int inventorySize;
	private double xpWhenSummoned;
	private Scroll scroll;
	private boolean canAttack = true;

	private Familiar() {

	}

	public String getNpcName() {
		return npcName;
	}

	public int getNpcId() {
		return npcId;
	}

	public int getLevelRequired() {
		return levelRequired;
	}

	public int getMinutes() {
		return minutes;
	}

	public int getPointCost() {
		return pointCost;
	}

	public String getSpecialClassName() {
		return specialClassName;
	}

	public int getInventorySize() {
		return inventorySize;
	}

	public Pouch getPouch() {
		return pouch;
	}

	public void setPouch(Pouch pouch) {
		this.pouch = pouch;
	}

	public double getXpWhenSummoned() {
		return xpWhenSummoned;
	}

	public Scroll getScroll() {
		return scroll;
	}

	public void setScroll(Scroll scroll) {
		this.scroll = scroll;
	}

	public boolean canAttack() {
		return canAttack;
	}

	public void setCanAttack(boolean canAttack) {
		this.canAttack = canAttack;
	}

}