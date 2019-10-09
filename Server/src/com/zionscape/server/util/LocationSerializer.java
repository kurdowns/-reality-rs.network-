package com.zionscape.server.util;

import com.google.gson.*;
import com.zionscape.server.model.Location;

import java.lang.reflect.Type;

public class LocationSerializer implements JsonDeserializer<Location>, JsonSerializer<Location> {

	@Override
	public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		JsonObject obj = jsonElement.getAsJsonObject();
		return Location.create(obj.get("x").getAsInt(), obj.get("y").getAsInt(), obj.get("z").getAsInt());
	}

	@Override
	public JsonElement serialize(Location position, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject obj = new JsonObject();

		obj.addProperty("x", position.getX());
		obj.addProperty("y", position.getY());
		obj.addProperty("z", position.getZ());

		return obj;
	}

}