package com.zionscape.server.model.content.treasuretrails.solutions;

import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.treasuretrails.Solution;
import com.zionscape.server.model.players.Player;

public enum Anagrams implements Solution {

	Jaraah("Aha Jar", 962), // Al Kharid duel arena hospital
	Caroline("Arc O Line", 696), // East of Ardougne in the area from the Sea Slug quest.
	Oracle("Are Col", 746), // Ice Mountain
	Kebab_Seller("Bar Bell Seek", 1865), // Al Kharid
	Lowe("El Ow", 550), // Archery Store in Varrock - Next to East Bank
	Recruiter("Err Cure It", 720), // West Ardougne city square
	King_Roald("LArk In Dog", 648), // Varrock Palace
	Cook("Ok Co", 278), // Lumbridge Castle
	Edmond("Nod Med", 714), // Northwest corner of East Ardougne
	Party_Pete("Peaty Pert", 659), // Seers' Village - South of the Bank
	Brundt("Dt Run B", 1294), // Longhall in the centre of Rellekka
	Cam_the_camel("Me Am The Calc", 2812), // Just outside the gates of the Duel Arena, Camulet not required
	Professor_Onglewip("Profs Lose Wrong Pie", 4585), // Ground floor of the Wizards Tower south of Draynor
	Hans("Snah", 7935); // Lumbridge Castle courtyard

	private final String anagram;
	private final int npcId;

	Anagrams(String anagram, int npcId) {
		this.anagram = anagram;
		this.npcId = npcId;
	}

	@Override
	public Location getLocation() {
		return null;
	}

	@Override
	public int getObjectId() {
		return -1;
	}

	@Override
	public int getNpcId() {
		return npcId;
	}

	@Override
	public void show(Player player) {

		for (int i = 6968; i < 6976; i++) {
			player.getPA().sendFrame126("", i);
		}

		player.getPA().sendFrame126("This anagram reveals", 6970);
		player.getPA().sendFrame126("who to speak to next", 6971);
		player.getPA().sendFrame126(anagram, 6973);

		player.getPA().showInterface(6965);
	}

}
