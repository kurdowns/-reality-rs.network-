package com.zionscape.server.model.content;

import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.content.minigames.clanwars.Constants;
import com.zionscape.server.model.content.minigames.clanwars.War;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;

import java.util.HashMap;

/**
 * @author Sanity
 */
public class Food {

	private Player player;

	public Food(Player player) {
		this.player = player;
	}

	public static int getHpBoost(Player player) {
		int hp = 0;

		for (int i = 0; i < player.equipment.length; i++) {
			for (NexEquipment equipment : NexEquipment.values()) {
				if (player.equipment[i] == equipment.item) {
					hp += equipment.hp;
				}
			}
		}

		if (player.attributeExists("fire_boost")) {
			int plus = (int) Math.floor((double) player.getActualLevel(PlayerConstants.HITPOINTS)
					* (double) player.getAttribute("fire_boost"));
			hp += plus;
		}

		return hp;
	}

	public void eat(int id, int slot) {

		if (player.level[3] <= 0 || player.isDead) {
			return;
		}

		if (player.getDuel() != null) {
			if (player.getDuel().getRules()[PlayerConstants.DUEL_FOOD]) {
				player.sendMessage("You may not eat food in this duel.");
				return;
			}
		}

		if (player.getAttribute("clan_war") != null) {
			War war = (War) player.getAttribute("clan_war");
			if (war.getCombatRules()[Constants.FOOD_RULE]) {
				player.sendMessage("You may not eat in this clan war.");
				return;
			}
		}

		if (System.currentTimeMillis() - player.foodDelay >= 1500 && player.level[3] > 0) {
			player.foodDelay = System.currentTimeMillis();
			player.getCombat().resetPlayerAttack();
			player.attackTimer += 2;
			player.startAnimation(829);
			player.getItems().deleteItem(id, slot, 1);
			FoodToEat f = FoodToEat.food.get(id);

			int totalLevel = player.getPA().getLevelForXP(player.xp[3]);
			int boost = getHpBoost(player);

			if (player.level[3] < totalLevel + boost) {
				int heal = f.getHeal();

				if (f.equals(FoodToEat.BANDAGES)) {
					if (player.equipment[PlayerConstants.HANDS] >= 11079
							&& player.equipment[PlayerConstants.HANDS] <= 11084) {
						heal = (int) (totalLevel * 0.15);
					} else {
						heal = (int) (totalLevel * 0.10);
					}
				}

				player.level[3] += heal;
				if (player.level[3] > totalLevel + boost) {
					player.level[3] = totalLevel + boost;
				}

			}
			player.getPA().refreshSkill(3);
			player.sendMessage("You eat the " + f.getName() + ".");

			if (f.getId() == 379)
				Achievements.progressMade(player, Achievements.Types.EAT_100_LOBSTERS);
		}
	}

	public boolean isFood(int id) {
		return FoodToEat.food.containsKey(id);
	}

	public enum FoodToEat {
		FURY_SHARK(20429, 26, "Fury Shark"), MANTA(391, 22, "Manta Ray"), SHARK(385, 20, "Shark"), LOBSTER(379, 12,
				"Lobster"), TROUT(333, 7, "Trout"), SALMON(329, 9, "Salmon"), SWORDFISH(373, 14, "Swordfish"), TUNA(361,
						10, "Tuna"), MONKFISH(7946, 16, "Monkfish"), SEA_TURTLE(397, 21, "Sea Turtle"), CAKE(1891, 4,
								"Cake"), BASS(365, 13, "Bass"), COD(339, 7, "Cod"), POTATO(1942, 1,
										"Potato"), BAKED_POTATO(6701, 4, "Baked Potato"), POTATO_WITH_CHEESE(6705, 16,
												"Potato with Cheese"), EGG_POTATO(7056, 16,
														"Egg Potato"), CHILLI_POTATO(7054, 14,
																"Chilli Potato"), MUSHROOM_POTATO(7058, 20,
																		"Mushroom Potato"), TUNA_POTATO(7060, 22,
																				"Tuna Potato"), SHRIMPS(315, 3,
																						"Shrimps"), HERRING(347, 5,
																								"Herring"), SARDINE(325,
																										4,
																										"Sardine"), CHOCOLATE_CAKE(
																												1897, 5,
																												"Chocolate Cake"), ANCHOVIES(
																														319,
																														1,
																														"Anchovies"), PLAIN_PIZZA(
																																2289,
																																7,
																																"Plain Pizza"), MEAT_PIZZA(
																																		2293,
																																		8,
																																		"Meat Pizza"), ANCHOVY_PIZZA(
																																				2297,
																																				9,
																																				"Anchovy Pizza"), PINEAPPLE_PIZZA(
																																						2301,
																																						11,
																																						"Pineapple Pizza"), BREAD(
																																								2309,
																																								5,
																																								"Bread"), APPLE_PIE(
																																										2323,
																																										7,
																																										"Apple Pie"), REDBERRY_PIE(
																																												2325,
																																												5,
																																												"Redberry Pie"), MEAT_PIE(
																																														2327,
																																														6,
																																														"Meat Pie"), PIKE(
																																																351,
																																																8,
																																																"Pike"), POTATO_WITH_BUTTER(
																																																		6703,
																																																		14,
																																																		"Potato with Butter"), BANANA(
																																																				1963,
																																																				2,
																																																				"Banana"), PEACH(
																																																						6883,
																																																						8,
																																																						"Peach"), ORANGE(
																																																								2108,
																																																								2,
																																																								"Orange"), PINEAPPLE_RINGS(
																																																										2118,
																																																										2,
																																																										"Pineapple Rings"), PINEAPPLE_CHUNKS(
																																																												2116,
																																																												2,
																																																												"Pineapple Chunks"), PURPLE_SWEETS(
																																																														10476,
																																																														12,
																																																														"Purple Sweet"), ROCKTAIL(
																																																																15272,
																																																																24,
																																																																"Rocktail"), SHORT_FINED_EEL(
																																																																		18167,
																																																																		11,
																																																																		"Short fined eel"), WEB_SNIPER(
																																																																				18169,
																																																																				15,
																																																																				"Web Sniper"), BOULDABASS(
																																																																						18171,
																																																																						18,
																																																																						"Bouldabass"), BANDAGES(
																																																																								4049,
																																																																								1,
																																																																								"Bandages"), SALVE_EEL(
																																																																										18173,
																																																																										10,
																																																																										"Salve Eel");
		public static HashMap<Integer, FoodToEat> food = new HashMap<Integer, FoodToEat>();

		static {
			for (FoodToEat f : FoodToEat.values()) {
				food.put(f.getId(), f);
			}
		}

		private int id;
		private int heal;
		private String name;

		FoodToEat(int id, int heal, String name) {
			this.id = id;
			this.heal = heal;
			this.name = name;
		}

		public static FoodToEat forId(int id) {
			return food.get(id);
		}

		public int getId() {
			return id;
		}

		public int getHeal() {
			return heal;
		}

		public String getName() {
			return name;
		}
	}

	private enum NexEquipment {

		TORVA_HELM(20135, 6), TORVA_BODY(20139, 20), TORVA_LEGS(20143, 13), PERNIX_COIF(20147, 6), PERNIX_BODY(20151,
				20), PERNIX_CHAPS(20155, 13), VIRTUS_MASK(20159, 6), VIRTUS_BODY(20163, 20), VIRTUS_LEGS(20167, 13),

		TORVA_HELM_DEG(20137, 6), TORVA_BODY_DEG(20141, 20), TORVA_LEGS_DEG(20145, 13), PERNIX_COIF_DEG(20149,
				6), PERNIX_BODY_DEG(20153, 20), PERNIX_CHAPS_DEG(20157,
						13), VIRTUS_MASK_DEG(20161, 6), VIRTUS_BODY_DEG(20165, 20), VIRTUS_LEGS_DEG(20169, 13);

		private final int item;
		private final int hp;

		NexEquipment(int item, int hp) {
			this.item = item;
			this.hp = hp;
		}

	}
}