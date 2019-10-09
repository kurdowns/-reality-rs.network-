package com.zionscape.server.model.players.skills.slayer;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum TaskLevels {

	@SerializedName("EASY")
	EASY(new Tasks[]{
			Tasks.BANSHEE,
			Tasks.BAT,
			Tasks.CAVE_BUG,
			Tasks.COW,
			Tasks.GHOST,
			Tasks.ICEFIEND,
			Tasks.SCORPION,
			Tasks.SKELETON,
			Tasks.SPIDER,
			Tasks.ZOMBIE
	}),
	@SerializedName("MEDIUM")
	MEDIUM(new Tasks[]{
			Tasks.ABERRANT_SPECTRE,
			Tasks.ANKOU,
			Tasks.BASILISK,
			Tasks.BLOODVELD,
			Tasks.BLUE_DRAGON,
			Tasks.BRONZE_DRAGON,
			Tasks.CAVE_BUG,
			Tasks.CAVER_CRAWLE,
			Tasks.COCKATRICE,
			Tasks.CRAWLING_HANDS,
			Tasks.DAGANNOTH,
			Tasks.DUST_DEVIL,
			Tasks.EARTH_WARRIOR,
			Tasks.FIRE_GIANT,
			Tasks.GREEN_DRAGON,
			Tasks.HELL_HOUND,
			Tasks.HILL_GIANT,
			Tasks.ICE_GIANT,
			Tasks.ICE_WARRIOR,
			Tasks.INFERNAL_MAGE,
			Tasks.JELLY,
			Tasks.KURASK,
			Tasks.LESSER_DEMON,
			Tasks.MOGRE,
			Tasks.MOSS_GIANT,
			Tasks.PYREFIEND,
			Tasks.ROCKSLUG,
			Tasks.TUROTH,
	}),
	@SerializedName("HARD")
	HARD(new Tasks[]{
			Tasks.MUTATED_JADINKO_MALE,
			Tasks.ABERRANT_SPECTRE,
			Tasks.ABYSSAL_DEMON,
			Tasks.AVANSIE,
			Tasks.BASILISK,
			Tasks.BLACK_DEMON,
			Tasks.BLACK_DRAGON,
			Tasks.BLOODVELD,
			Tasks.BRONZE_DRAGON,
			Tasks.DAGANNOTH,
			Tasks.DARK_BEAST,
			Tasks.DESERT_STRKEWYRM,
			Tasks.DUST_DEVIL,
			Tasks.FIRE_GIANT,
			Tasks.GARGOYLE,
			Tasks.GREATER_DEMON,
			Tasks.HELL_HOUND,
			Tasks.ICE_STRKEWYRM,
			Tasks.INFERNAL_MAGE,
			Tasks.IRON_DRAGON,
			Tasks.JELLY,
			Tasks.JUNGLE_STRKEWYRM,
			Tasks.MITHRIL_DRAGON,
			Tasks.NECHRYAEL,
			Tasks.SKELETAL_WYVERN,
			Tasks.SPIRITUAL_MAGE,
			Tasks.SPIRITUAL_RANGER,
			Tasks.SPIRITUAL_WARRIOR,
			Tasks.STEEL_DRAGON,
			Tasks.WATER_FIEND
	}),
	@SerializedName("ELITE")
	ELITE(new Tasks[] {
			Tasks.GLACOR,
			Tasks.WILDY_WYRM,
			Tasks.CHAOS_ELEMENT,
			Tasks.DAG_SUPREME,
			Tasks.DAG_PRIME,
			Tasks.DAG_REX,
			Tasks.KALPHITE_QUEEN,
			Tasks.KBD,
			Tasks.BANDOS,
			Tasks.TSTSAROTH,
			Tasks.KREE_ARR,
			Tasks.CZULYANA,
			Tasks.TORMENTED_DEMON,
			Tasks.JUNGLE_BOOK,
			Tasks.BORK,
			Tasks.BARRELCHEST,
			Tasks.SEA_TROLL_QUEEN,
			Tasks.CORP_BEAST,
			Tasks.AVATAR,
			Tasks.NEX,
			Tasks.NOMAD,
			Tasks.ZULRAH,
			Tasks.CERB
	});

	private final List<Tasks> tasks;

	TaskLevels(Tasks[] tasks) {
		this.tasks = Collections.unmodifiableList(Stream.of(tasks).collect(Collectors.toList()));
	}

	public List<Tasks> getTasks() {
		return tasks;
	}

}
