package com.zionscape.server.model.players.skills.summoning;

import com.zionscape.server.model.items.Item;

public class Pouch {

	private String name;
	private int actionButton;
	private int levelRequirement;
	private double xpGained;
	private int pouchItemId;
	private Item[] itemsRequired;
	private Familiar familiar;
	private int familiarId;

	private Pouch() {

	}

	public String getName() {
		return name;
	}

	public int getActionButton() {
		return actionButton;
	}

	public int getLevelRequirement() {
		return levelRequirement;
	}

	public double getXpGained() {
		return xpGained;
	}

	public int getPouchItemId() {
		return pouchItemId;
	}

	public Item[] getItemsRequired() {
		return itemsRequired;
	}

	public Familiar getFamiliar() {
		return familiar;
	}

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}

	public int getFamiliarId() {
		return familiarId;
	}

}