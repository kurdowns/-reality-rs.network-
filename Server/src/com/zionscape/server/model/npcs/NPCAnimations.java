package com.zionscape.server.model.npcs;

import com.zionscape.server.model.content.minigames.FightCaves;


public class NPCAnimations {

	public static int getAttackEmote(int i) {
		NPC[] npcs = NPCHandler.npcs;
		NPC npc = npcs[i];

		if (npc.getCombat() != null) {
			return npc.getCombat().getAttackAnimation();
		}

		// steel dragons
		if (npc.type == 1592 && npc.attackType == 3) {
			return 14246;
		}

		NpcDefinition definition = NPCHandler.getNpcDefinition(npc.type);

		if (definition != null && definition.getAttackAnimation() > 0) {
			return definition.getAttackAnimation();
		}
		switch (NPCHandler.npcs[i].type) {

			case 3772://Brawler
			case 3773:
			case 3774:
			case 3775:
			case 3776:
				return 3897;

			case 3732: // shifters
			case 3734:
			case 3740:
			case 3736:
				return 3898;
			case 6142: // portals
			case 6143:
			case 6144:
			case 6145:
				return -1;

			case 8528: //Nomad
				return 12693;
			case 907:
			case 910:
				return 729;

			case 909:
				return 5319;

			case 908:
				return 132;

			case 911:
				return 69;
			case 7343:
				return 8183;
			case 9462:
			case 9463:
			case 9464:
			case 9465:
			case 9466:
			case 9467:
				return 12791;

			case 3220:
			case 1795:
			case 1796:
			case 1797:
				return 99;

			case 3247: // Hobgoblin
				return 6184;

			case 5452:
				return 5725;
			case 8596: // avatar destruction
				return 11197;

			case 117: // Hill Giant
			case 6270: // Cyclops
			case 6269: // Ice cyclops
				return 4652;

			case 6219: // Spiritual Warrior
			case 6255: // Spiritual Warrior
				return 451;

			case 6229: // Spirtual Warrior arma
				return 6954;

			case 6218: // Gorak
				return 4300;

			case 6212: // Werewolf
				return 6536;

			case 6220: // Spirtual Ranger
			case 6256: // Spirtual Ranger
				return 426;

			case 6257: // Spirtual Mage
			case 6221: // Spirtual Mage
				return 811;

			case 6276: // Spirtual Ranger
			case 6278: // Spirtual Mage
			case 6272: // Ork
			case 6274: // Ork
			case 6277: // Spirtual Warrior bandos
				return 4320;

			case 6230: // Spirtual Ranger
			case 6233: // Aviansie
			case 6239: // Aviansie
			case 6232: // Aviansie
				return 6953;

			case 6254: // Saradomin Priest
				return 440;
			case 6258: // Saradomin Knight
				return 7048;
			case 6231: // Spirtual Mage
				return 6952;
			case 100:
				return 309;
			case 1459:// monkey guard
				return 1402;
			case 3406:
				return 8080;
			case 1677: // Experiment
				return 1616;
			case 1678: // Experiment
				return 1612;
			case 1676: // Experiment
				return 1626;
			case 2607:
				return 9286;

			case 95:
			case 141:
			case 96:
				return 75;

			case 1125:
			case 1138:
			case 1134:
				return 284;
			case 4278:
			case 4279:
			case 4280:
			case 4281:
			case 4282:
			case 4283:
			case 4284: {
				return 451;
			}
			case 4291:
			case 4292: {
				return 4652;
			}
			case 1115:
				return 1142;
			case FightCaves.TZTOK_JAD:
				switch (npcs[i].attackType) {
					case 0:
						return 9277;
					case 1:
						return 9276;
					case 2:
						return 9300;
				}
			case FightCaves.KET_ZEK:// ket-zek
			case 2744:
				if (npcs[i].attackType == 0) {
					return 9265;
				} else {
					return 9266;
				}
			case FightCaves.YT_MEJKOT:
			case 2742:
				if (npcs[i].attackType == 0) {
					return 9252;
				} else {
					return 9254;
				}
			case FightCaves.TOK_XIL:
			case 2739:
			case 2740:
				if (npcs[i].attackType == 1) {
					return 9243;
				} else {
					return 9245;
				}
			case 13478:// ork
				return 7411;
			// case 2631:
			// return 2633;
			case 2746:
				return 2637;
			case 3847:
				return 3991;
			case 1158:
				return 6231;
			case 1160:
				if (npcs[i].kQueen == 0) {
					return 6235;
				}
				return 6234;
			case 49:// hellhound
				return 6579;
			case 50:
				return 81;
			case 7133:
				return 8754;
			case 7135:
				return 8760;
			case 6260:
				if (npcs[i].attackType == 0) {
					return 7060;
				} else {
					return 7063;
				}
			case 2892:
			case 2894:
				return 2868;
			case 857:
				return 359;
			case 5666:
				switch (npcs[i].attackType) {
					case 0:
						return 5894;
					case 1:
						return 5895;
				}
			case FightCaves.TZ_KIH:
				return 2622;
			case FightCaves.TZ_KEK:
			case FightCaves.TZ_KEK_SPAWN:
				return 9233;
			case 3340:
				if (npcs[i].attackType == 7) {
					return 3311;
				} else if (npcs[i].attackType == 0) // melee
				{
					return 3312;
				}

			/*
			 * case 8133: if(npcs[i].attackType == 4) return 10066; else return 10058;
			 */
			case 8349: // tormented demon
				if (npcs[i].attackType == 5) // mage
				{
					return 10917;
				} else if (npcs[i].attackType == 6)// range
				{
					return 10918;
				} else if (npcs[i].attackType == 0)// melee
				{
					return 10922;
				}
			case 8133:
				if (npcs[i].attackType == 2) // mage
				{
					return 10066;
				} else if (npcs[i].attackType == 1) // range
				{
					return 10053;
				} else if (npcs[i].attackType == 0) // melee
				{
					return 10057; // melee
				} else {
					return 10058; // other melee
				}

				// frostdragon
			case 10773:
				// case 5362:
				// case 5363:
				return 13155;
			// bandos gwd
			case 6261:
			case 6263:
			case 6265:
				return 6154;
			// end of gwd
			// arma gwd
			case 6222:
				return 3505;
			case 6225:
				return 6953;
			case 6223:
				return 6952;
			case 6227:
				return 6954;
			// end of arma gwd
			// sara gwd
			case 6247:
				return 6964;
			case 6248:
				return 6376;
			case 6250:
				return 7018;
			case 6252:
				return 7009;
			// end of sara gwd
			case 13: // wizards
				return 711;
			case 103:
			case 655:
				return 123;
			case 1624:
				return 1557;
			case 1648:
				return 9125;
			case 2783: // dark beast
				return 2733;
			case 1615: // abby demon
				return 1537;
			case 1613: // nech
				return 9487;
			case 1610:
			case 1611: // garg
				return 9454;
			case 1643:
				return 7183;
			case 1616: // basilisk
				return 1546;
			case 90: // skele
				return 5485;
			case 2496:
				return 10922;
			case 53:
			case 54:
			case 55:
			case 941:
			case 1590:
			case 1591:
			case 5362:
			case 1592:
				// return 80;
			case 5363: // Mith dragon
				if (npcs[i].attackType == 3) {
					return 13152;
				} else {
					return 14247;
				}
			case 124: // earth warrior
				return 390;
			case 803: // monk
				return 422;
			case 52: // baby drag
			case 1028:
				return 25;
			case 58: // Shadow Spider
			case 59: // Giant Spider
			case 60: // Giant Spider
			case 61: // Spider
			case 62: // Jungle Spider
			case 63: // Deadly Red Spider
			case 64: // Ice Spider
			case 134:
				return 143;
			case 105: // Bear
			case 106: // Bear
				return 41;
			case 412:
			case 78:
				return 30;
			case 2033: // rat
				return 138;
			case 2031: // bloodworm
				return 2070;
			case 101: // goblin
				return 309;
			case 81: // cow
				return 0x03B;
			case 21: // hero
				return 451;
			case 41: // chicken
				return 55;
			case 9: // guard
			case 32: // guard
			case 20: // paladin
				return 451;
			case 1338: // dagannoth
			case 1340:
			case 1342:
				return 1341;
			case 19: // white knight
				return 406;
			case 110:
			case 111: // ice giant
				// case 112:
			case 112: // Moss giant
				return 4652;
			case 2452:
				return 1312;
			case 2889:
				return 2859;
			case 118:
			case 119:
				return 99;
			case 82:// Lesser Demon
			case 83:// Greater Demon
			case 84:// Black Demon
			case 1472:// jungle demon
				return 64;
			case 1267:
			case 1265:
				return 1312;
			case 125: // ice warrior
			case 178:
				return 451;
			case 1153: // Kalphite Worker
			case 1154: // Kalphite Soldier
			case 1155: // Kalphite guardian
			case 1156: // Kalphite worker
			case 1157: // Kalphite guardian
				return 1184;
			case 123:
			case 122:
				return 164;
			case 2028: // karil
				return 2075;
			case 2025: // ahrim
				return 729;
			case 2026: // dharok
				return 2067;
			case 2027: // guthan
				return 2080;
			case 2029: // torag
				return 0x814;
			case 2030: // verac
				return 2062;
			case 2881: // supreme
				return 2855;
			case 2882: // prime
				return 2854;
			case 2883: // rex
				return 2851;
			case 3200:
				return 3146;
			case 114: // Ogre
			case 115: // Ogre
			case 374: // Ogre
				return 8577;
			default:
				return 0x326;
		}
	}

	/**
	 * Emotes
	 */
	public static int getBlockEmote(int type) {

		NpcDefinition definition = NPCHandler.getNpcDefinition(type);
		if (definition != null && definition.getBlockAnimation() > 0) {
			return definition.getBlockAnimation();
		}

		switch (type) {
			case 9462:
			case 9463:
			case 9464:
			case 9465:
			case 9466:
			case 9467:
				return 12792;
			case 7343:
				return 8185;
			case 8596: // avatar destruction
				return 11198;
			case 1459:// monkey guard
				return 1403;
			case 100:
				return 312;
			case 114:
			case 115:
				return 360;
			case 95:
			case 141:
			case 96:
				return 76;
			case 1138:
			case 1134:
			case 1125:
				return 285;
			case 1115:
				return 285;
			case 4278:
			case 4279:
			case 4280:
			case 4281:
			case 4282:
			case 4283:
			case 4284: {
				return 424;

			}
			case 4291:
			case 4292: {
				return 4651;
			}
			case 10773:// frost dragon
				return 13154;
			case 3847:
				return 3990;
			case 50:// drags
			case 53:
			case 54:
			case 55:
			case 941:
			case 1590:
			case 1591:
			case 1592:
			case 5363:
			case 5362:
				return 12251;
			case 51:// baby drags
			case 52:
			case 1589:
			case 3376:
				return 26;
			case FightCaves.TZ_KEK:
			case FightCaves.TZ_KEK_SPAWN:
				return 9235;
			case FightCaves.TZ_KIH:
				return 9231;
			case FightCaves.TOK_XIL: {
				return 9242;
			}
			case FightCaves.YT_MEJKOT: {
				return 9253;
			}
			case FightCaves.KET_ZEK: {
				return 9268;
			}
			default:
				return -1;
		}
	}

	public static int getDeadEmote(int i) {

		NPC[] npcs = NPCHandler.npcs;

		NpcDefinition definition = NPCHandler.getNpcDefinition(npcs[i].type);
		if (definition != null && definition.getDeathAnimation() > 0) {
			return definition.getDeathAnimation();
		}
		switch (npcs[i].type) { // sec

			case 491: // tentacle
				return 3620;
			case 493: // kraken
				return 3993;

			case 2045: // snakeling
				return 2408;

			case 5073: // crimson swift
				return 6778;


			case 3772://Brawler
			case 3773:
			case 3774:
			case 3775:
			case 3776:
				return 3894;

			case 6142: // pest portals
			case 6143:
			case 6144:
			case 6145:
				return 3935;

			case 9462:
			case 9463:
			case 9464:
			case 9465:
			case 9466:
			case 911:
				return 68;
			case 909:
				return 5321;
			case 908:
				return 133;
			case 910:
			case 907:
				return 714;
			case 9467:
				return 12793;
			case 8528: //Nomad
				return 12694;
			case 7343:
				return 8184;
			case 3220:
			case 1795:
			case 1796:
			case 1797:
				return 102;

			case 374: // Ogre
			case 114: // Ogre
			case 115: // Ogre
				return 8576;

			case 112: // Moss giant
			case 117: // Hill giant
				return 4653;
			case 8596: // avatar destruction
				return 11199;
			case 5452:
				return 5726;
			case 100:
				return 313;
			case 1459:// monkey guard
				return 1404;
			case 3406:
				return 8078;
			case 1678: // Experiment
				return 1612;
			case 1676: // Experiment
				return 1628;
			case 1677: // Experiment
				return 1618;
			case 2607:
				return 9288;
			case 95:
			case 141:
			case 96:
				return 75;

			case 1138:
			case 1134:
			case 1125:
				return 287;
			case 1115:
				return 287;
			case 4278:
			case 4279:
			case 4280:
			case 4281:
			case 4282:
			case 4283:
			case 4284: {
				return 2304;
			}
			case 4291:
			case 4292: {
				return 4653;
			}
			case FightCaves.TZ_KIH: {
				return 9230;
			}
			case FightCaves.TZ_KEK:
			case FightCaves.TZ_KEK_SPAWN: {
				return 9234;
			}
			case FightCaves.TOK_XIL: {
				return 9239;
			}
			case FightCaves.YT_MEJKOT:
			case 2742: {
				return 9257;
			}
			case FightCaves.KET_ZEK:
			case 2744: {
				return 9269;
			}
			case FightCaves.TZTOK_JAD:
				return 9279;
			case 2746:
				return 2638;
			case 2747:
				return 9279;
			case 3847:// sea troll queen
				return 3993;
			case 1158:
				return 6228;
			case 1160:
				return 6233;
			case 7133:
				return 8756;
			case 13478:// ork
				return 7412;
			case 7135:
				return 8761;
			case 3340:
				return 3310;
			case 2739:
			case 2740:
				return 9239;
			case 5666:
				return 5898;
			case 10773:// frost dragon
				return 13153;
			// sara gwd
			case 6247:
				return 6965;
			case 6248:
				return 6377;
			case 90: // skele
				return 5488;
			case 6250:
				return 7016;
			case 6252:
				return 7011;
			case 8133: // corp
				return 10050;
			case 8349: // td
				return 10924;
			// bandos gwd
			case 6261:
			case 6263:
			case 6265:
				return 6156;
			case 6260:
				return 7062;
			case 2892:
			case 2894:
				return 2865;
			case 1612: // banshee
				return 9449;
			case 6222:
				return 3503;
			case 6223:
			case 6225:
			case 6227:
				return 6956;
			case 3777:
			case 3778:
			case 3779:
			case 3780:
				return -1;
			case 3200:
				return 3147;
			case 2035: // spider
				return 146;
			case 2033: // rat
				return 141;
			case 2031: // bloodvel
				return 2073;
			case 101: // goblin
				return 313;
			case 81: // cow
				return 0x03E;
			case 41: // chicken
				return 57;
			case 1338: // dagannoth
			case 1340:
			case 1342:
				return 1342;
			case 2881:
			case 2882:
			case 2883:
				return 2856;
			case 111: // ice giant
				return 131;
			case 125: // ice warrior
				return 843;
			case 751:// Zombies!!
				return 302;
			case 1626:
			case 1627:
			case 1628:
			case 1629:
			case 1630:
			case 1631:
			case 1632: // turoth!
				return 1597;
			case 1616: // basilisk
				return 1548;
			case 1653: // hand
				return 1590;
			case 82:// demons
			case 83:
			case 84:
				return 67;
			case 1605:// abby spec
				return 1508;
			case 51:// baby drags
			case 52:
			case 1028:
			case 1589:
			case 3376:
				return 28;
			case 1610:
			case 1611:
				return 1518;
			case 1618:
			case 1619:
				return 9130;
			case 1620:
			case 1621:
				return 1563;
			case 2783:
				return 2732;
			case 1615:
				return 1538;
			case 1624:
				return 1558;
			case 1613:
				return 1530;
			case 1633:
			case 1634:
			case 1635:
			case 1636:
				return 1580;
			case 1648:
			case 1649:
			case 1650:
			case 1651:
			case 1652:
			case 1654:
			case 1655:
			case 1656:
			case 1657:
				return 9125;
			case 102:
				return 313;
			case 105:
			case 106:
				return 44;
			case 412:
			case 78:
				return 36;
			case 122:
			case 123:
				return 167;
			case 58:
			case 59:
			case 60:
			case 61:
			case 62:
			case 63:
			case 64:
			case 134:
				return 146;
			case 1153:
			case 1154:
			case 1155:
			case 1156:
			case 1157:
				return 1190;
			case 103:
			case 104:
				return 123;
			case 118:
			case 119:
				return 102;
			case 2496:
				return 10924;

			case 5363: // Mithril Dragon
				return 14248;

			case 50:// drags
			case 53:
			case 54:
			case 55:
			case 941:
			case 1590:
			case 1591:
			case 1592:
			case 5362:
			case 11773:
				return 12250;
			default:
				return 2304;
		}
	}

}
