package com.zionscape.server.model.content.minigames.clanwars;

import com.zionscape.server.model.Area;
import com.zionscape.server.model.Location;

public enum Maps {

	CLASSIC(
			707,
			Location.create(3300, 3723, 0),
			Location.create(3291, 3829, 0),
			Location.create(3320, 3768, 0),
			Location.create(3320, 3781, 0),
			new Area(3266, 3713, 3325, 3838, -1),
			new Area(3318, 3762, 3322, 3775, -1),
			new Area(3318, 3776, 3322, 3789, -1)
	),
	FOREST(
			711,
			Location.create(2892, 5642, 0),
			Location.create(2930, 5685, 0),
			Location.create(2929, 5667, 0),
			Location.create(2893, 5660, 0),
			new Area(2884, 5637, 2939, 5690, -1),
			new Area(2927, 5664, 2927, 5664, -1),
			new Area(2891, 5658, 2896, 5663, -1)
	),
	QUERRY(
			710,
			Location.create(2891, 5516, 0),
			Location.create(2933, 5555, 0),
			Location.create(2908, 5536, 0),
			Location.create(2916, 5536, 0),
			new Area(2885, 5509, 2938, 5562, -1),
			new Area(2905, 5533, 2910, 5540, -1),
			new Area(2813, 5533, 2918, 5540, -1)
	),
	TURRETS(
			709,
			Location.create(2731, 5512, 0),
			Location.create(2708, 5622, 0),
			Location.create(2722, 5506, 0),
			Location.create(2717, 5629, 0),
			new Area(2692, 5508, 2747, 5627, -1),
			null,
			null
	),
	PLATEAU(
			708,
			Location.create(2884, 5904, 0),
			Location.create(2883, 5938, 0),
			Location.create(2851, 5910, 0),
			Location.create(2851, 5932, 0),
			new Area(2856, 5895, 2907, 5947, -1),
			new Area(2849, 5904, 2853, 5915, -1),
			new Area(2849, 5926, 2853, 5936, -1)
	);

	private final int config;
	private final Location southStartingLocation;
	private final Location northStartingLocation;
	private final Location southJailLocation;
	private final Location northJailLocation;
	private final Area mapArea;
	private final Area southJailArea;
	private final Area northJailArea;

	Maps(int config, Location southStartingLocation, Location northStartingLocation, Location southJailLocation, Location northJailLocation, Area mapArea, Area southJailArea, Area northJailArea) {
		this.config = config;
		this.southStartingLocation = southStartingLocation;
		this.northStartingLocation = northStartingLocation;
		this.southJailLocation = southJailLocation;
		this.northJailLocation = northJailLocation;
		this.mapArea = mapArea;
		this.southJailArea = southJailArea;
		this.northJailArea = northJailArea;
	}

	public Location getSouthStartingLocation() {
		return southStartingLocation;
	}

	public Location getNorthStartingLocation() {
		return northStartingLocation;
	}

	public Location getSouthJailLocation() {
		return southJailLocation;
	}

	public Location getNorthJailLocation() {
		return northJailLocation;
	}

	public Area getMapArea() {
		return mapArea;
	}

	public int getConfig() {
		return config;
	}

	public Area getSouthJailArea() {
		return southJailArea;
	}

	public Area getNorthJailArea() {
		return northJailArea;
	}

}