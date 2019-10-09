package com.zionscape.server.model.players.skills.summoning;

public class Scroll {

	private String name;
	private int scrollItemId;
	private double createXp;
	private double useXp;
	private int specialMovePoints;
	private int[] requiredPouchItemIds;
	private Familiar familiar;
	private int[] familiarNpcIds;

	private Scroll() {

	}

	public String getName() {
		return name;
	}

	public int getScrollItemId() {
		return scrollItemId;
	}

	public double getCreateXp() {
		return createXp;
	}

	public double getUseXp() {
		return useXp;
	}

	public int getSpecialMovePoints() {
		return specialMovePoints;
	}

	public int[] getRequiredPouchItemIds() {
		return requiredPouchItemIds;
	}

	public Familiar getFamiliar() {
		return familiar;
	}

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}

	public int[] getFamiliarNpcIds() {
		return familiarNpcIds;
	}

}
