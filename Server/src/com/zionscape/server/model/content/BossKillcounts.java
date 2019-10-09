package com.zionscape.server.model.content;

import com.zionscape.server.model.content.minigames.quests.QuestInterfaceGenerator;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.util.CollectionUtil;

import java.util.Arrays;
import java.util.Comparator;

public class BossKillcounts {

	private static final Boss[] bosses = new Boss[]{
			new Boss("Abyssal Demon", new int[]{1615}),
			new Boss("Glacor", new int[]{14301}),
			new Boss("Dark Beast", new int[]{2783}),
			new Boss("Revenant", new int[]{13465, 13466, 13467, 13468, 13469, 13470, 13471, 13472, 13472, 13473, 13474, 13475, 13476, 13477, 13478, 13479, 13480, 13481}),
			new Boss("Wildy Wyrm", new int[]{3334}),
			new Boss("Chaos Element", new int[]{3200}),
			new Boss("Frost Dragon", new int[]{10773}),
			new Boss("Bronze Dragon", new int[]{1590}),
			new Boss("Iron Dragon", new int[]{1591}),
			new Boss("Steel Dragon", new int[]{1592}),
			new Boss("Mithril Dragon", new int[]{5363}),
			new Boss("Nechryael", new int[]{1613}),
			new Boss("Waterfiend", new int[]{5361}),
			new Boss("Skeletal Wyvern", new int[]{3068}),
			new Boss("Turoth", new int[]{1623}),
			new Boss("Basilisk", new int[]{1616}),
			new Boss("Mutated Jadinko Baby", new int[]{13820}),
			new Boss("Mutated Jadinko GUard", new int[]{13821}),
			new Boss("Mutated Jadinko Male", new int[]{13822}),
			new Boss("Black Demon", new int[]{84}),
			new Boss("Black Dragon", new int[]{54}),
			new Boss("Desert Strykworm", new int[]{9464}),
			new Boss("Ice Strykworm", new int[]{9462}),
			new Boss("Jungle Strykworm", new int[]{9466}),
			new Boss("Dagannoth Supreme", new int[]{2881}),
			new Boss("Dagannoth Prime", new int[]{2882}),
			new Boss("Dagannoth Rex", new int[]{2883}),
			new Boss("Kalphite Queen", new int[]{1160}),
			new Boss("King Black Dragon", new int[]{50}),
			new Boss("General Graardo", new int[]{6260}),
			new Boss("K'ril Tstsaroth", new int[]{6203}),
			new Boss("Kree'arra", new int[]{6222}),
			new Boss("Commander Zilyana", new int[]{6247}),
			new Boss("Tormented Demon", new int[]{8349}),
			new Boss("Jungle Demon", new int[]{1472}),
			new Boss("Green Dragon", new int[]{941}),
			new Boss("Blue Dragon", new int[]{55}),
			new Boss("Red Dragon", new int[]{53}),
			new Boss("Bork", new int[]{7133}),
			new Boss("Barrelchest", new int[]{5666}),
			new Boss("Sea Troll Queen", new int[]{3847}),
			new Boss("Corporeal Beast", new int[]{8133}),
			new Boss("Avatar of Destruction", new int[]{8596}),
			new Boss("Nex", new int[]{13447}),
			new Boss("Nomad", new int[]{8528}),
			new Boss("Zulrah", new int[]{2042, 2043, 2044}),
			new Boss("Cerberus", new int[]{5862}),
			new Boss("Kraken", new int[]{493}),
			new Boss("Callisto", new int[]{14383}),
			new Boss("Venenatis", new int[]{14384}),
			new Boss("Thermonuclear smoke devil", new int[]{14385}),
			new Boss("Vet'ion", new int[]{14386})
	};

	static {
		Arrays.sort(bosses, new Comparator<Boss>() {
			@Override
			public int compare(Boss o1, Boss o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
	}

	public static boolean onClickingButtons(Player player, int button) {
		if (button == 212155) {
			QuestInterfaceGenerator questInterfaceGenerator = new QuestInterfaceGenerator("Boss Kill Counts");

			for (Boss boss : bosses) {
				questInterfaceGenerator.add(boss.getName() + ": " + getBossKills(player, boss));
			}

			questInterfaceGenerator.writeQuest(player);
			return true;
		}
		return false;
	}

	public static void onNpcKilled(Player player, NPC npc) {
		for (Boss boss : bosses) {
			if (CollectionUtil.getIndexOfValue(boss.getIds(), npc.type) > -1) {
				int count = 0;
				if (player.getData().bossKillCounts.containsKey(npc.type)) {
					count = player.getData().bossKillCounts.get(npc.type);
				}
				player.getData().bossKillCounts.put(npc.type, count + 1);

			}
		}
	}

	private static int getBossKills(Player player, Boss boss) {
		int count = 0;

		for (int id : boss.getIds()) {
			if (player.getData().bossKillCounts.containsKey(id)) {
				count += player.getData().bossKillCounts.get(id);
			}
		}

		return count;
	}

	private static class Boss {
		private final String name;
		private final int[] ids;

		Boss(String name, int[] ids) {
			this.name = name;
			this.ids = ids;
		}

		String getName() {
			return name;
		}

		int[] getIds() {
			return ids;
		}
	}

}
