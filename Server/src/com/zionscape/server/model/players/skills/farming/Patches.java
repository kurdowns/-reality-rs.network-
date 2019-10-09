package com.zionscape.server.model.players.skills.farming;

import com.zionscape.server.model.players.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Patches {

	ALLOTMENT_1(
			"allotment",
			Arrays.asList(Seeds.CABBAGE, Seeds.ONION, Seeds.TOMATO, Seeds.POTATO, Seeds.STRAWBERRY, Seeds.SWEETCORN, Seeds.WATERMELON),
			Arrays.asList(8550, 8551, 8552, 8553),
			(Player p) -> {
				Patch northFally = Farming.getPatch(p, 8550);
				Patch southFally = Farming.getPatch(p, 8551);
				Patch northCatherby = Farming.getPatch(p, 8552);
				Patch southCatherby = Farming.getPatch(p, 8553);

				int value = ((northFally.getState().toValue() << 6) + northFally.getSeed()) +
						(((southFally.getState().toValue() << 6) + southFally.getSeed()) << 8) +
						(((northCatherby.getState().toValue() << 6) + northCatherby.getSeed()) << 16) +
						(((southCatherby.getState().toValue() << 6) + southCatherby.getSeed()) << 24);

				p.getPA().sendConfig(504, value);
			}
	),
	ALLOTMENT_2(
			"allotment",
			Arrays.asList(Seeds.CABBAGE, Seeds.ONION, Seeds.TOMATO, Seeds.POTATO, Seeds.STRAWBERRY, Seeds.SWEETCORN, Seeds.WATERMELON),
			Arrays.asList(8554, 8555, 8556, 8557),
			(Player p) -> {
				Patch northArdy = Farming.getPatch(p, 8554);
				Patch southArdy = Farming.getPatch(p, 8555);
				Patch northPort = Farming.getPatch(p, 8556);
				Patch southPort = Farming.getPatch(p, 8557);

				int value = ((northArdy.getState().toValue() << 6) + northArdy.getSeed()) +
						(((southArdy.getState().toValue() << 6) + southArdy.getSeed()) << 8) +
						(((northPort.getState().toValue() << 6) + northPort.getSeed()) << 16) +
						(((southPort.getState().toValue() << 6) + southPort.getSeed()) << 24);

				p.getPA().sendConfig(505, value);
			}
	),

	FLOWER_1(
			"flower patch",
			Arrays.asList(Seeds.MARIGOLD, Seeds.ROSEMARY, Seeds.NASTURTIUM, Seeds.WOAD, Seeds.LIMPWURT),
			Arrays.asList(7847, 7848, 7849, 7850),
			(Player p) -> {
				Patch falador = Farming.getPatch(p, 7847);
				Patch catherby = Farming.getPatch(p, 7848);
				Patch ardy = Farming.getPatch(p, 7849);
				Patch portPhasmatys = Farming.getPatch(p, 7850);

				int value = ((falador.getState().toValue() << 6) + falador.getSeed()) +
						(((catherby.getState().toValue() << 6) + catherby.getSeed()) << 8) +
						(((ardy.getState().toValue() << 6) + ardy.getSeed()) << 16) +
						(((portPhasmatys.getState().toValue() << 6) + portPhasmatys.getSeed()) << 24);

				p.getPA().sendConfig(508, value);
			}
	),
	HERB_1(
			"herb patch",
			Arrays.asList(Seeds.GUAM, Seeds.MARENTILL, Seeds.TARROMIN, Seeds.HARRALANDER, Seeds.RANAAR, Seeds.TOADFLAX,
					Seeds.IRIT, Seeds.AVANTOE, Seeds.KWUARM, Seeds.SNAPDRAGON, Seeds.CANDANTINE, Seeds.LANTADYME, Seeds.DWARF, Seeds.TORSTOL),
			Arrays.asList(8152, 8153, 8150, 8151),
			(Player p) -> {
				Patch falador = Farming.getPatch(p, 8150);
				Patch catherby = Farming.getPatch(p, 8151);
				Patch ardy = Farming.getPatch(p, 8152);
				Patch portPhasmatys = Farming.getPatch(p, 8153);

				int value = ((falador.getState().toValue() << 7) + falador.getSeed()) +
						(((catherby.getState().toValue() << 7) + catherby.getSeed()) << 8) +
						(((ardy.getState().toValue() << 7) + ardy.getSeed()) << 16) +
						(((portPhasmatys.getState().toValue() << 7) + portPhasmatys.getSeed()) << 24);

				p.getPA().sendConfig(515, value);
			}
	),
	TREE_1(
			"tree patch",
			Arrays.asList(Seeds.OAK, Seeds.WILLOW, Seeds.MAPLE, Seeds.YEW, Seeds.MAGIC),
			Arrays.asList(8390, 8391, 8389, 8388),
			(Player p) -> {
				Patch varrock = Farming.getPatch(p, 8390);
				Patch lumbridge = Farming.getPatch(p, 8391);
				Patch falador = Farming.getPatch(p, 8389);
				Patch taverly = Farming.getPatch(p, 8388);

				int value = ((taverly.getState().toValue() << 6) + taverly.getSeed()) +
						(((falador.getState().toValue() << 6) + falador.getSeed()) << 8) +
						(((varrock.getState().toValue() << 6) + varrock.getSeed()) << 16) +
						(((lumbridge.getState().toValue() << 6) + lumbridge.getSeed()) << 24);

				p.getPA().sendConfig(502, value);
			}
	);

	// 8151 // catherby
	// 8152 ardt
	// 8153 port phas
	// 8150 fally

	private String name;
	private List<Seeds> seeds;
	private List<Integer> objectIds;
	private SendConfig configSender;

	Patches(String name, List<Seeds> seeds, List<Integer> objectIds, SendConfig configSender) {
		this.name = name;
		this.seeds = Collections.unmodifiableList(seeds);
		this.objectIds = Collections.unmodifiableList(objectIds);
		this.configSender = configSender;
	}

	public List<Seeds> getSeeds() {
		return seeds;
	}

	public List<Integer> getObjectIds() {
		return objectIds;
	}

	public void send(Player player) {
		configSender.send(player);
	}

	public String getName() {
		return name;
	}

	private interface SendConfig {
		void send(Player player);
	}


}