package com.zionscape.server.model.items;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import com.zionscape.server.model.content.tradingpost.item.TradingPostItem;

public class Item {

	public static Bindable[] bindables = new Bindable[600];
	private int id;
	private int amount;
	private String name;

	public Item(int id) {
		this.id = id;
		this.amount = 1;
	}

	public Item(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}

	public static void load() {
		try (BufferedReader reader =
				new BufferedReader(new FileReader(new File("./config/cfg/bindable.cfg")))) {
			String line = null;
			int nth = 0;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split("\t");
				bindables[nth++] = new Bindable(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void decrementAmount(int amount) {
		this.amount -= amount;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void incrementAmount(int amount) {
		this.amount += amount;
	}

	public void fillName() {
		name = ItemUtility.getName(id);
	}

	public ItemDefinition getDefinition() {
		Optional<ItemDefinition> optional = ItemDefinition.get(id);
		return optional.isPresent() ? optional.get() : null;
	}


	public Item clone() {
		return new Item(id, amount);
	}
}
