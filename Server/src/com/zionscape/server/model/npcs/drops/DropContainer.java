package com.zionscape.server.model.npcs.drops;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DropContainer {

	private final int npcId;

	private final List<Drop> alwaysDrops = new ArrayList<>();
	private final List<Drop> commonDrops = new ArrayList<>();
	private final List<Drop> rareDrops = new ArrayList<>();

	public DropContainer(int npcId) {
		this.npcId = npcId;
	}

	public int getNpcId() {
		return npcId;
	}

	public List<Drop> getAlwaysDrops() {
		return alwaysDrops;
	}

	public List<Drop> getCommonDrops() {
		return commonDrops;
	}

	public List<Drop> getRareDrops() {
		return rareDrops;
	}

	public void addDrop(Drop drop) {

		if (drop.getRate().equals(Rates.ALWAYS)) {
			alwaysDrops.add(drop);
			return;
		}

		double probability = (((double) 1 / (double) drop.getRate().getB()) * 100);

		if (probability >= 50 || drop.getRate().equals(Rates.COMMON)) {
			commonDrops.add(drop);
		} else {
			rareDrops.add(drop);
			sortRareDrops();
		}
	}

	public void sortRareDrops() {
		Collections.sort(rareDrops, (Drop o1, Drop o2) -> {

			int sum1 = o1.getRate().getB();
			int sum2 = o2.getRate().getB();

			if (sum1 > sum2) {
				return -1;
			} else if (sum1 < sum2) {
				return 1;
			}

			return 0;
		});
	}

}