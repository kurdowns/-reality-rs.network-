package com.zionscape.server.model.items;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.zionscape.server.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public final class ItemDefinition {

	private static final Logger logger = Logger.getLogger(ItemDefinition.class.getName());
	private static final Map<Integer, ItemDefinition> definitions = new LinkedHashMap<>();
	private final int id;
	private final String name;
	private final int value;
	private final int wearSlot;
	private final boolean fullmask;
	private final boolean stackable;
	private final boolean showBeard;
	private final boolean twoHanded;
	private final int[] bonuses;
	private final Map<Integer, Integer> wearRequirements;
	private final boolean geTradable;
	private final int notedId;
	private final int unnotedId;
	private int geValue;

	private ItemDefinition(int id, String name, int notedId, int unnotedId, int value, int geValue,
			int wearSlot, boolean fullmask, boolean stackable, boolean showBeard, boolean twoHanded,
			int[] bonuses, Map<Integer, Integer> wearRequirements, boolean geTradable) {

		this.notedId = notedId;
		this.unnotedId = unnotedId;
		this.id = id;
		this.name = name;
		this.value = value;
		this.geValue = geValue;
		this.wearSlot = wearSlot;
		this.fullmask = fullmask;
		this.stackable = stackable;
		this.showBeard = showBeard;
		this.twoHanded = twoHanded;
		this.bonuses = bonuses;
		this.wearRequirements = wearRequirements;
		this.geTradable = geTradable;
	}

	public static void load() {

		definitions.clear();

		logger.info("loading item definitions");

		Gson gson = new Gson();

		try (Connection connection = DatabaseUtil.getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM item_defs")) {
				try (ResultSet results = ps.executeQuery()) {
					while (results.next()) {
						definitions.put(results.getInt("id"), ItemDefinitionBuilder.anItemDefinition()
								.withId(results.getInt("id")).withName(results.getString("name"))
								.withValue(results.getInt("value"))
								.withWearSlot(results.getInt("wear_slot"))
								.withFullmask(results.getBoolean("full_mask"))
								.withShowBeard(results.getBoolean("show_beard"))
								.withStackable(results.getBoolean("stackable"))
								.withTwoHanded(results.getBoolean("two_handed"))
								.withNotedId(results.getInt("noted_id"))
								.withUnnotedId(results.getInt("unnoted_id"))
								.withBonuses(gson.fromJson(results.getString("bonuses"), int[].class))
								.withGeValue(results.getInt("ge_value"))
								.withGeTradable(results.getBoolean("ge_tradable"))
								.withWearRequirements(gson.fromJson(results.getString("wear_requirements"),
										new TypeToken<Map<Integer, Integer>>() {}.getType()))
								.build());
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("error loading item definitions", e);
		}

		logger.info("loading " + definitions.size() + " item definitions");
	}

	public static Optional<ItemDefinition> get(int id) {
		if (definitions == null) {
			throw new RuntimeException("definitions have not been loaded");
		}
		if (id < 0) {
			return Optional.empty();
		}
		return Optional.ofNullable(definitions.get(id));
	}

	public static ItemDefinition forId(int id) {
		if (definitions == null) {
			throw new RuntimeException("definitions have not been loaded");
		}
		if (id < 0) {
			id = 0;
		}
		return definitions.get(id);
	}

	public static Map<Integer, ItemDefinition> getDefinitions() {
		return Collections.unmodifiableMap(definitions);
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}

	public int getWearSlot() {
		return wearSlot;
	}

	public boolean isFullmask() {
		return fullmask;
	}

	public boolean isStackable() {
		return stackable;
	}

	public boolean isNotable() {
		return unnotedId > -1;
	}

	public boolean isShowBeard() {
		return showBeard;
	}

	public boolean isTwoHanded() {
		return twoHanded;
	}

	public int[] getBonuses() {
		return bonuses.clone();
	}

	public Map<Integer, Integer> getWearRequirements() {
		return wearRequirements;
	}

	public int getGeValue() {
		return geValue;
	}

	public void setGeValue(int geValue) {
		this.geValue = geValue;
	}

	public boolean isGeTradable() {
		return geTradable;
	}

	public int getNotedId() {
		return notedId;
	}

	public int getUnnotedId() {
		return unnotedId;
	}

	private static class ItemDefinitionBuilder {
		private int id;
		private String name;
		private int value;
		private int geValue;
		private int wearSlot;
		private boolean fullmask;
		private boolean stackable;
		private boolean showBeard;
		private boolean twoHanded;
		private int[] bonuses;
		private Map<Integer, Integer> wearRequirements;
		private boolean geTradable;
		private int notedId;
		private int unnotedId;

		private ItemDefinitionBuilder() {}

		public static ItemDefinitionBuilder anItemDefinition() {
			return new ItemDefinitionBuilder();
		}

		public ItemDefinitionBuilder withId(int id) {
			this.id = id;
			return this;
		}

		public ItemDefinitionBuilder withName(String name) {
			this.name = name;
			return this;
		}

		public ItemDefinitionBuilder withValue(int value) {
			this.value = value;
			return this;
		}

		public ItemDefinitionBuilder withWearSlot(int wearSlot) {
			this.wearSlot = wearSlot;
			return this;
		}

		public ItemDefinitionBuilder withFullmask(boolean fullmask) {
			this.fullmask = fullmask;
			return this;
		}

		public ItemDefinitionBuilder withStackable(boolean stackable) {
			this.stackable = stackable;
			return this;
		}

		public ItemDefinitionBuilder withShowBeard(boolean showBeard) {
			this.showBeard = showBeard;
			return this;
		}

		public ItemDefinitionBuilder withTwoHanded(boolean twoHanded) {
			this.twoHanded = twoHanded;
			return this;
		}

		public ItemDefinitionBuilder withBonuses(int[] bonuses) {
			this.bonuses = bonuses;
			return this;
		}

		public ItemDefinitionBuilder withWearRequirements(Map<Integer, Integer> requirements) {
			this.wearRequirements = requirements;
			return this;
		}

		public ItemDefinitionBuilder withGeValue(int geValue) {
			this.geValue = geValue;
			return this;
		}

		public ItemDefinitionBuilder withGeTradable(boolean geTradable) {
			this.geTradable = geTradable;
			return this;
		}

		public ItemDefinitionBuilder withNotedId(int notedId) {
			this.notedId = notedId;
			return this;
		}

		public ItemDefinitionBuilder withUnnotedId(int unnotedId) {
			this.unnotedId = unnotedId;
			return this;
		}

		public ItemDefinition build() {
			return new ItemDefinition(id, name, notedId, unnotedId, value, geValue, wearSlot, fullmask,
					stackable, showBeard, twoHanded, bonuses, wearRequirements, geTradable);
		}
	}

}
