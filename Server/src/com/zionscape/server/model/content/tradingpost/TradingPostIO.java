package com.zionscape.server.model.content.tradingpost;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zionscape.server.model.content.tradingpost.item.TradingPostItem;
import com.zionscape.server.model.content.tradingpost.item.collection.TradingPostItemCollection;

/**
 * Handling server settings
 * 
 * @author 2012
 *
 */
public class TradingPostIO {

	/**
	 * The file location
	 */
	private static final String LOCATION = TradingPostManager.SAVE_FILE + "TradingPost.json";

	/**
	 * Loading
	 */
	public static void load() {

		Path path = Paths.get(LOCATION);
		File file = path.toFile();

		if (!file.exists()) {
			return;
		}

		try (FileReader fileReader = new FileReader(file)) {
			JsonParser fileParser = new JsonParser();
			Gson builder = new GsonBuilder().create();
			JsonObject reader = (JsonObject) fileParser.parse(fileReader);

			if (reader.has("global-history")) {
				TradingPostItem[] string = builder
						.fromJson(reader.get("global-history").getAsJsonArray(), TradingPostItem[].class);
				for (TradingPostItem traps : string) {
					TradingPostManager.globalHistory.add(traps);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * Load trading post collection
		 */
		TradingPostItemCollection.init();
	}

	/**
	 * Saving
	 */
	public static void save() {
		Path path = Paths.get(LOCATION);
		File file = path.toFile();
		file.getParentFile().setWritable(true);

		if (!file.getParentFile().exists()) {
			try {
				file.getParentFile().mkdirs();
			} catch (SecurityException e) {
			}
		}
		try (FileWriter writer = new FileWriter(file)) {

			Gson builder = new GsonBuilder().setPrettyPrinting().create();
			JsonObject object = new JsonObject();
			object.add("global-history", builder.toJsonTree(TradingPostManager.globalHistory));

			writer.write(builder.toJson(object));
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
