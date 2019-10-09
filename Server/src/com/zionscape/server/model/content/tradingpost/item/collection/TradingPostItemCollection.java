package com.zionscape.server.model.content.tradingpost.item.collection;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zionscape.server.Server;
import com.zionscape.server.model.content.tradingpost.TradingPostManager;
import com.zionscape.server.model.items.ItemDefinition;
import com.zionscape.server.model.players.Player;

/**
 * Handles the trading post collection
 * 
 * @author 2012
 *
 */
public class TradingPostItemCollection {

	/**
	 * The awaiting collections
	 */
	private static ArrayList<TradingPostShopEarning> collections =
			new ArrayList<TradingPostShopEarning>();

	/**
	 * The save file
	 */
	private static final String SAVE_FILE = TradingPostManager.SAVE_FILE + "collections.json";

	/**
	 * Adds to collection
	 * 
	 * @param amount the amount
	 * @param username the username
	 */
	public static void addToCollection(long amount, String username) {
		/*
		 * The earning
		 */
		TradingPostShopEarning shop = getForUser(username);
		/*
		 * Found so increase
		 */
		if (shop != null) {
			shop.setAmount(shop.getAmount() + amount);
		} else {
			TradingPostShopEarning earn = new TradingPostShopEarning(amount, username);
			collections.add(earn);
		}
		/*
		 * Save collection
		 */
		saveCollection();
	}

	/**
	 * Sending collection message
	 * 
	 * @param player the player
	 */
	public static void sendCollectionMessage(Player player) {
		/*
		 * Gets the earning
		 */
		TradingPostShopEarning earn = getForUser(player.username);
		/*
		 * Nothing
		 */
		if (earn == null) {
			return;
		}
		/*
		 * Notify
		 */
		player.sendMessage("@blu@You have " + NumberFormat.getInstance().format(earn.getAmount())
						+ " " + ItemDefinition.forId(TradingPostManager.CURRENCY).getName()
						+ " waiting in your collection box");
	}

	/**
	 * Withdraws collection
	 * 
	 * @param player the player
	 */
	public static void withdrawCollection(Player player) {
		/*
		 * The shop earning
		 */
		TradingPostShopEarning earn = getForUser(player.getUsername());
		/*
		 * Invalid earn
		 */
		if (earn == null) {
			return;
		}
		/*
		 * Remove
		 */
		collections.remove(earn);
	}

	/**
	 * Gets the collection amount
	 * 
	 * @param player the player
	 * @return the amount
	 */
	public static int getCollectionAmount(Player player) {
		/*
		 * The shop earning
		 */
		TradingPostShopEarning earn = getForUser(player.getUsername());
		/*
		 * Invalid earn
		 */
		if (earn == null) {
			return -1;
		}
		return (int) earn.getAmount();
	}

	/**
	 * Gets collection by user
	 * 
	 * @param user the user
	 * @return the collection
	 */
	private static TradingPostShopEarning getForUser(String user) {
		/*
		 * The shop collections
		 */
		for (TradingPostShopEarning earn : collections) {
			/*
			 * Invalid
			 */
			if (earn == null) {
				continue;
			}
			/*
			 * Found by name
			 */
			if (earn.getUsername().equalsIgnoreCase(user)) {
				return earn;
			}
		}
		return null;
	}

	/**
	 * Saves the collection
	 */
	public static void saveCollection() {
		Path path = Paths.get(SAVE_FILE);
		File file = path.toFile();
		file.getParentFile().setWritable(true);
		if (!file.getParentFile().exists()) {
			try {
				file.getParentFile().mkdirs();
			} catch (SecurityException e) {
				System.out.println("Unable to create directory for shop collection!");
			}
		}
		try (FileWriter writer = new FileWriter(file)) {
			Gson builder = new GsonBuilder().setPrettyPrinting().create();
			JsonObject object = new JsonObject();
			object.add("ShopEarning", builder.toJsonTree(collections));
			writer.write(builder.toJson(object));
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads thecollection
	 */
	public static void init() {
		Path path = Paths.get(SAVE_FILE);
		File file = path.toFile();
		if (!file.exists()) {
			file.mkdirs();
		}
		try (FileReader fileReader = new FileReader(file)) {
			JsonParser fileParser = new JsonParser();
			Gson builder = new GsonBuilder().create();
			JsonObject reader = (JsonObject) fileParser.parse(fileReader);
			/**
			 * Found user
			 */
			TradingPostShopEarning[] earnings = null;
			if (reader.has("ShopEarning")) {
				earnings =
						builder.fromJson(reader.get("ShopEarning").getAsJsonArray(),
								TradingPostShopEarning[].class);
			}
			if (earnings != null) {
				for (TradingPostShopEarning earn : earnings) {
					if (earn == null) {
						continue;
					}
					collections.add(earn);
				}
			}
			System.out.println("Loaded " + collections.size() + " shop collections.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
