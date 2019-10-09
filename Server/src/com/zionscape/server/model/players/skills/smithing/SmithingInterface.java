package com.zionscape.server.model.players.skills.smithing;

import com.zionscape.server.model.players.Player;

public class SmithingInterface {

	Player c;

	public SmithingInterface(Player c) {
		this.c = c;
	}

	public void showSmithInterface(int itemId) {
		if (itemId == 2349) {
			this.makeBronzeInterface(c);
		} else if (itemId == 2351) {
			this.makeIronInterface(c);
		} else if (itemId == 2353) {
			this.makeSteelInterface(c);
		} else if (itemId == 2359) {
			this.makeMithInterface(c);
		} else if (itemId == 2361) {
			this.makeAddyInterface(c);
		} else if (itemId == 2363) {
			this.makeRuneInterface(c);
		}
	}

	private void makeRuneInterface(Player c) {
		String fiveb = this.GetForBars(2363, 5, c);
		String threeb = this.GetForBars(2363, 3, c);
		String twob = this.GetForBars(2363, 2, c);
		String oneb = this.GetForBars(2363, 1, c);
		c.getPA().sendFrame126(fiveb + "5bars" + fiveb, 1112);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1109);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1110);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1118);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1111);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1095);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1115);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1090);
		c.getPA().sendFrame126(twob + "2bars" + twob, 1113);
		c.getPA().sendFrame126(twob + "2bars" + twob, 1116);
		c.getPA().sendFrame126(twob + "2bars" + twob, 1114);
		c.getPA().sendFrame126(twob + "2bars" + twob, 1089);
		c.getPA().sendFrame126(twob + "2bars" + twob, 8428);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1124);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1125);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1126);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1127);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1128);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1129);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1130);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1131);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 13357);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 11459);
		c.getPA().sendFrame126(this.GetForlvl(88, c) + "Plate Body" + this.GetForlvl(18, c), 1101);
		c.getPA().sendFrame126(this.GetForlvl(99, c) + "Plate Legs" + this.GetForlvl(16, c), 1099);
		c.getPA().sendFrame126(this.GetForlvl(99, c) + "Plate Skirt" + this.GetForlvl(16, c), 1100);
		c.getPA().sendFrame126(this.GetForlvl(99, c) + "2 Hand Sword" + this.GetForlvl(14, c), 1088);
		c.getPA().sendFrame126(this.GetForlvl(97, c) + "Kite Shield" + this.GetForlvl(12, c), 1105);
		c.getPA().sendFrame126(this.GetForlvl(96, c) + "Chain Body" + this.GetForlvl(11, c), 1098);
		c.getPA().sendFrame126(this.GetForlvl(95, c) + "Battle Axe" + this.GetForlvl(10, c), 1092);
		c.getPA().sendFrame126(this.GetForlvl(94, c) + "Warhammer" + this.GetForlvl(9, c), 1083);
		c.getPA().sendFrame126(this.GetForlvl(93, c) + "Square Shield" + this.GetForlvl(8, c), 1104);
		c.getPA().sendFrame126(this.GetForlvl(92, c) + "Full Helm" + this.GetForlvl(7, c), 1103);
		c.getPA().sendFrame126(this.GetForlvl(92, c) + "Throwing Knives" + this.GetForlvl(7, c), 1106);
		c.getPA().sendFrame126(this.GetForlvl(91, c) + "Long Sword" + this.GetForlvl(6, c), 1086);
		c.getPA().sendFrame126(this.GetForlvl(90, c) + "Scimitar" + this.GetForlvl(5, c), 1087);
		c.getPA().sendFrame126(this.GetForlvl(90, c) + "Arrowtips" + this.GetForlvl(5, c), 1108);
		c.getPA().sendFrame126(this.GetForlvl(89, c) + "Sword" + this.GetForlvl(4, c), 1085);
		c.getPA().sendFrame126(this.GetForlvl(89, c) + "Bolts" + this.GetForlvl(4, c), 9144);
		c.getPA().sendFrame126(this.GetForlvl(89, c) + "Nails" + this.GetForlvl(4, c), 13358);
		c.getPA().sendFrame126(this.GetForlvl(88, c) + "Medium Helm" + this.GetForlvl(3, c), 1102);
		c.getPA().sendFrame126(this.GetForlvl(87, c) + "Mace" + this.GetForlvl(2, c), 1093);
		c.getPA().sendFrame126(this.GetForlvl(85, c) + "Dagger" + this.GetForlvl(1, c), 1094);
		c.getPA().sendFrame126(this.GetForlvl(86, c) + "Axe" + this.GetForlvl(1, c), 1091);

		c.getPA().sendFrame34(1213, 0, 1119, 1); // dagger
		c.getPA().sendFrame34(1359, 0, 1120, 1); // axe
		c.getPA().sendFrame34(1113, 0, 1121, 1); // chain body
		c.getPA().sendFrame34(1147, 0, 1122, 1); // med helm
		c.getPA().sendFrame34(824, 0, 1123, 10); // Bolts

		c.getPA().sendFrame34(1289, 1, 1119, 1); // s-sword
		c.getPA().sendFrame34(1432, 1, 1120, 1); // mace
		c.getPA().sendFrame34(1079, 1, 1121, 1); // platelegs
		c.getPA().sendFrame34(1163, 1, 1122, 1); // full helm
		c.getPA().sendFrame34(44, 1, 1123, 15); // arrowtips

		c.getPA().sendFrame34(1333, 2, 1119, 1); // scimmy
		c.getPA().sendFrame34(1347, 2, 1120, 1); // warhammer
		c.getPA().sendFrame34(1093, 2, 1121, 1); // plateskirt
		c.getPA().sendFrame34(1185, 2, 1122, 1); // Sq. Shield
		c.getPA().sendFrame34(868, 2, 1123, 5); // throwing-knives

		c.getPA().sendFrame34(1303, 3, 1119, 1); // longsword
		c.getPA().sendFrame34(1373, 3, 1120, 1); // battleaxe
		c.getPA().sendFrame34(1127, 3, 1121, 1); // platebody
		c.getPA().sendFrame34(1201, 3, 1122, 1); // kiteshield
		c.getPA().sendFrame34(9431, 3, 1123, 1); // limb
		c.getPA().sendFrame126(oneb + "1bar", 1132);
		c.getPA().sendFrame126(this.GetForlvl(91, c) + "Limbs", 1096);


		c.getPA().sendFrame34(1319, 4, 1119, 1); // 2h sword
		c.getPA().sendFrame34(3101, 4, 1120, 1); // claws
		c.getPA().sendFrame126(twob + "2bar", 8428);
		c.getPA().sendFrame126(this.GetForlvl(98, c) + "Claws", 8429);

		c.getPA().sendFrame34(-1, 4, 1121, 1);
		c.getPA().sendFrame126("", 11459);
		c.getPA().sendFrame126("", 11461);

		c.getPA().sendFrame34(4824, 4, 1122, 15); // nails
		c.getPA().sendFrame126(oneb + "1bar", 13357);
		c.getPA().sendFrame126(this.GetForlvl(89, c) + "Nails", 13358);

		c.getPA().sendFrame34(9381, 4, 1123, 10); // bolts
		c.getPA().sendFrame126(oneb + "1bar", 1135);
		c.getPA().sendFrame126(this.GetForlvl(88, c) + "Bolts", 1134);

		c.getPA().hideShowLayer(1097, true);
		c.getPA().hideShowLayer(1133, true);
		c.getPA().hideShowLayer(8431, true);
		c.getPA().hideShowLayer(11458, true);

		c.getPA().showInterface(994);
	}

	private void makeAddyInterface(Player c) {
		String fiveb = this.GetForBars(2361, 5, c);
		String threeb = this.GetForBars(2361, 3, c);
		String twob = this.GetForBars(2361, 2, c);
		String oneb = this.GetForBars(2361, 1, c);
		c.getPA().sendFrame126(fiveb + "5bars" + fiveb, 1112);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1109);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1110);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1118);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1111);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1095);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1115);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1090);
		c.getPA().sendFrame126(twob + "2bars" + twob, 1113);
		c.getPA().sendFrame126(twob + "2bars" + twob, 1116);
		c.getPA().sendFrame126(twob + "2bars" + twob, 1114);
		c.getPA().sendFrame126(twob + "2bars" + twob, 1089);
		c.getPA().sendFrame126(twob + "2bars" + twob, 8428);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1124);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1125);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1126);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1127);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1128);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1129);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1130);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1131);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 13357);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 11459);
		c.getPA().sendFrame126(this.GetForlvl(88, c) + "Plate Body" + this.GetForlvl(18, c), 1101);
		c.getPA().sendFrame126(this.GetForlvl(86, c) + "Plate Legs" + this.GetForlvl(16, c), 1099);
		c.getPA().sendFrame126(this.GetForlvl(86, c) + "Plate Skirt" + this.GetForlvl(16, c), 1100);
		c.getPA().sendFrame126(this.GetForlvl(84, c) + "2 Hand Sword" + this.GetForlvl(14, c), 1088);
		c.getPA().sendFrame126(this.GetForlvl(82, c) + "Kite Shield" + this.GetForlvl(12, c), 1105);
		c.getPA().sendFrame126(this.GetForlvl(81, c) + "Chain Body" + this.GetForlvl(11, c), 1098);
		c.getPA().sendFrame126(this.GetForlvl(80, c) + "Battle Axe" + this.GetForlvl(10, c), 1092);
		c.getPA().sendFrame126(this.GetForlvl(79, c) + "Warhammer" + this.GetForlvl(9, c), 1083);
		c.getPA().sendFrame126(this.GetForlvl(78, c) + "Square Shield" + this.GetForlvl(8, c), 1104);
		c.getPA().sendFrame126(this.GetForlvl(77, c) + "Full Helm" + this.GetForlvl(7, c), 1103);
		c.getPA().sendFrame126(this.GetForlvl(77, c) + "Throwing Knives" + this.GetForlvl(7, c), 1106);
		c.getPA().sendFrame126(this.GetForlvl(76, c) + "Long Sword" + this.GetForlvl(6, c), 1086);
		c.getPA().sendFrame126(this.GetForlvl(75, c) + "Scimitar" + this.GetForlvl(5, c), 1087);
		c.getPA().sendFrame126(this.GetForlvl(75, c) + "Arrowtips" + this.GetForlvl(5, c), 1108);
		c.getPA().sendFrame126(this.GetForlvl(74, c) + "Sword" + this.GetForlvl(4, c), 1085);
		c.getPA().sendFrame126(this.GetForlvl(74, c) + "Bolts" + this.GetForlvl(4, c), 9143);
		c.getPA().sendFrame126(this.GetForlvl(74, c) + "Nails" + this.GetForlvl(4, c), 13358);
		c.getPA().sendFrame126(this.GetForlvl(73, c) + "Medium Helm" + this.GetForlvl(3, c), 1102);
		c.getPA().sendFrame126(this.GetForlvl(72, c) + "Mace" + this.GetForlvl(2, c), 1093);
		c.getPA().sendFrame126(this.GetForlvl(70, c) + "Dagger" + this.GetForlvl(1, c), 1094);
		c.getPA().sendFrame126(this.GetForlvl(71, c) + "Axe" + this.GetForlvl(1, c), 1091);

		c.getPA().sendFrame34(1211, 0, 1119, 1); // dagger
		c.getPA().sendFrame34(1357, 0, 1120, 1); // axe
		c.getPA().sendFrame34(1111, 0, 1121, 1); // chain body
		c.getPA().sendFrame34(1145, 0, 1122, 1); // med helm
		c.getPA().sendFrame34(823, 0, 1123, 10); // dart tips

		c.getPA().sendFrame34(1287, 1, 1119, 1); // s-sword
		c.getPA().sendFrame34(1430, 1, 1120, 1); // mace
		c.getPA().sendFrame34(1073, 1, 1121, 1); // platelegs
		c.getPA().sendFrame34(1161, 1, 1122, 1); // full helm
		c.getPA().sendFrame34(43, 1, 1123, 15); // arrowtips

		c.getPA().sendFrame34(1331, 2, 1119, 1); // scimmy
		c.getPA().sendFrame34(1345, 2, 1120, 1); // warhammer
		c.getPA().sendFrame34(1091, 2, 1121, 1); // plateskirt
		c.getPA().sendFrame34(1183, 2, 1122, 1); // Sq. Shield
		c.getPA().sendFrame34(867, 2, 1123, 5); // throwing-knives

		c.getPA().sendFrame34(1301, 3, 1119, 1); // longsword
		c.getPA().sendFrame34(1371, 3, 1120, 1); // battleaxe
		c.getPA().sendFrame34(1123, 3, 1121, 1); // platebody
		c.getPA().sendFrame34(1199, 3, 1122, 1); // kiteshield
		c.getPA().sendFrame34(9429, 3, 1123, 1); // limbs
		c.getPA().sendFrame126(oneb + "1bar", 1132);
		c.getPA().sendFrame126(this.GetForlvl(76, c) + "Limbs", 1096);

		c.getPA().sendFrame34(1317, 4, 1119, 1); // 2h sword

		c.getPA().sendFrame34(3100, 4, 1120, 1); // claws
		c.getPA().sendFrame126(twob + "2bar", 8428);
		c.getPA().sendFrame126(this.GetForlvl(83, c) + "Claws", 8429);

		c.getPA().sendFrame34(-1, 4, 1121, 15); // blank
		c.getPA().sendFrame126("", 11459);
		c.getPA().sendFrame126("", 11461);

		c.getPA().sendFrame34(4823, 4, 1122, 15); // nails
		c.getPA().sendFrame126(oneb + "1bar", 13357);
		c.getPA().sendFrame126(this.GetForlvl(74, c) + "Nails", 13358);

		c.getPA().sendFrame34(9380, 4, 1123, 10); // bolts
		c.getPA().sendFrame126(oneb + "1bar", 1135);
		c.getPA().sendFrame126(this.GetForlvl(73, c) + "Bolts", 1134);

		c.getPA().hideShowLayer(1097, true);
		c.getPA().hideShowLayer(1133, true);
		c.getPA().hideShowLayer(8431, true);
		c.getPA().hideShowLayer(11458, true);

		c.getPA().showInterface(994);
	}

	private void makeMithInterface(Player c) {
		String fiveb = this.GetForBars(2359, 5, c);
		String threeb = this.GetForBars(2359, 3, c);
		String twob = this.GetForBars(2359, 2, c);
		String oneb = this.GetForBars(2359, 1, c);
		c.getPA().sendFrame126(fiveb + "5bars" + fiveb, 1112);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1109);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1110);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1118);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1111);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1095);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1115);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1090);
		c.getPA().sendFrame126(twob + "2bars" + twob, 1113);
		c.getPA().sendFrame126(twob + "2bars" + twob, 1116);
		c.getPA().sendFrame126(twob + "2bars" + twob, 1114);
		c.getPA().sendFrame126(twob + "2bars" + twob, 1089);
		c.getPA().sendFrame126(twob + "2bars" + twob, 8428);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1124);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1125);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1126);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1127);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1128);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1129);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1130);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1131);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 13357);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 11459);
		c.getPA().sendFrame126(this.GetForlvl(68, c) + "Plate Body" + this.GetForlvl(18, c), 1101);
		c.getPA().sendFrame126(this.GetForlvl(66, c) + "Plate Legs" + this.GetForlvl(16, c), 1099);
		c.getPA().sendFrame126(this.GetForlvl(66, c) + "Plate Skirt" + this.GetForlvl(16, c), 1100);
		c.getPA().sendFrame126(this.GetForlvl(64, c) + "2 Hand Sword" + this.GetForlvl(14, c), 1088);
		c.getPA().sendFrame126(this.GetForlvl(62, c) + "Kite Shield" + this.GetForlvl(12, c), 1105);
		c.getPA().sendFrame126(this.GetForlvl(61, c) + "Chain Body" + this.GetForlvl(11, c), 1098);
		c.getPA().sendFrame126(this.GetForlvl(60, c) + "Battle Axe" + this.GetForlvl(10, c), 1092);
		c.getPA().sendFrame126(this.GetForlvl(59, c) + "Warhammer" + this.GetForlvl(9, c), 1083);
		c.getPA().sendFrame126(this.GetForlvl(58, c) + "Square Shield" + this.GetForlvl(8, c), 1104);
		c.getPA().sendFrame126(this.GetForlvl(57, c) + "Full Helm" + this.GetForlvl(7, c), 1103);
		c.getPA().sendFrame126(this.GetForlvl(57, c) + "Throwing Knives" + this.GetForlvl(7, c), 1106);
		c.getPA().sendFrame126(this.GetForlvl(56, c) + "Long Sword" + this.GetForlvl(6, c), 1086);
		c.getPA().sendFrame126(this.GetForlvl(55, c) + "Scimitar" + this.GetForlvl(5, c), 1087);
		c.getPA().sendFrame126(this.GetForlvl(55, c) + "Arrowtips" + this.GetForlvl(5, c), 1108);
		c.getPA().sendFrame126(this.GetForlvl(54, c) + "Sword" + this.GetForlvl(4, c), 1085);
		c.getPA().sendFrame126(this.GetForlvl(54, c) + "Bolts" + this.GetForlvl(4, c), 9142);
		c.getPA().sendFrame126(this.GetForlvl(54, c) + "Nails" + this.GetForlvl(4, c), 13358);
		c.getPA().sendFrame126(this.GetForlvl(53, c) + "Medium Helm" + this.GetForlvl(3, c), 1102);
		c.getPA().sendFrame126(this.GetForlvl(52, c) + "Mace" + this.GetForlvl(2, c), 1093);
		c.getPA().sendFrame126(this.GetForlvl(50, c) + "Dagger" + this.GetForlvl(1, c), 1094);
		c.getPA().sendFrame126(this.GetForlvl(51, c) + "Axe" + this.GetForlvl(1, c), 1091);

		c.getPA().sendFrame34(1209, 0, 1119, 1); // dagger
		c.getPA().sendFrame34(1355, 0, 1120, 1); // axe
		c.getPA().sendFrame34(1109, 0, 1121, 1); // chain body
		c.getPA().sendFrame34(1143, 0, 1122, 1); // med helm
		c.getPA().sendFrame34(822, 0, 1123, 10); // dart tips

		c.getPA().sendFrame34(1285, 1, 1119, 1); // s-sword
		c.getPA().sendFrame34(1428, 1, 1120, 1); // mace
		c.getPA().sendFrame34(1071, 1, 1121, 1); // platelegs
		c.getPA().sendFrame34(1159, 1, 1122, 1); // full helm
		c.getPA().sendFrame34(42, 1, 1123, 15); // arrowtips

		c.getPA().sendFrame34(1329, 2, 1119, 1); // scimmy
		c.getPA().sendFrame34(1343, 2, 1120, 1); // warhammer
		c.getPA().sendFrame34(1085, 2, 1121, 1); // plateskirt
		c.getPA().sendFrame34(1181, 2, 1122, 1); // Sq. Shield
		c.getPA().sendFrame34(866, 2, 1123, 5); // throwing-knives

		c.getPA().sendFrame34(1299, 3, 1119, 1); // longsword
		c.getPA().sendFrame34(1369, 3, 1120, 1); // battleaxe
		c.getPA().sendFrame34(1121, 3, 1121, 1); // platebody
		c.getPA().sendFrame34(1197, 3, 1122, 1); // kiteshield
		c.getPA().sendFrame34(9427, 3, 1123, 1); // mithril limbs
		c.getPA().sendFrame126(oneb + "1bar", 1132);
		c.getPA().sendFrame126(this.GetForlvl(56, c) + "Limbs", 1096);

		c.getPA().sendFrame34(1315, 4, 1119, 1); // 2h sword

		c.getPA().sendFrame34(3099, 4, 1120, 1); // claws
		c.getPA().sendFrame126(twob + "2bar", 8428);
		c.getPA().sendFrame126(this.GetForlvl(63, c) + "Claws", 8429);

		c.getPA().sendFrame34(-1, 4, 1121, 1);
		c.getPA().sendFrame126("", 11459);
		c.getPA().sendFrame126("", 11461);

		c.getPA().sendFrame34(4822, 4, 1122, 15); // nails
		c.getPA().sendFrame126(oneb + "1bar", 13357);
		c.getPA().sendFrame126(this.GetForlvl(54, c) + "Nails", 13358);

		c.getPA().sendFrame34(9379, 4, 1123, 10); // bolts
		c.getPA().sendFrame126(oneb + "1bar", 1135);
		c.getPA().sendFrame126(this.GetForlvl(53, c) + "Bolts", 1134);

		c.getPA().hideShowLayer(1097, true);
		c.getPA().hideShowLayer(1133, true);
		c.getPA().hideShowLayer(8431, true);
		c.getPA().hideShowLayer(11458, true);

		c.getPA().showInterface(994);
	}

	private void makeSteelInterface(Player c) {
		String fiveb = this.GetForBars(2353, 5, c);
		String threeb = this.GetForBars(2353, 3, c);
		String twob = this.GetForBars(2353, 2, c);
		String oneb = this.GetForBars(2353, 1, c);
		c.getPA().sendFrame126(fiveb + "5bars" + fiveb, 1112);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1109);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1110);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1118);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1111);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1095);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1115);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1090);
		c.getPA().sendFrame126(twob + "2bars" + twob, 1113);
		c.getPA().sendFrame126(twob + "2bars" + twob, 1116);
		c.getPA().sendFrame126(twob + "2bars" + twob, 1114);
		c.getPA().sendFrame126(twob + "2bars" + twob, 1089);
		c.getPA().sendFrame126(twob + "2bars" + twob, 8428);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1124);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1125);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1126);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1127);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1128);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1129);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1130);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1131);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 13357);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1132);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1135);
		c.getPA().sendFrame126("", 11459);
		c.getPA().sendFrame126(this.GetForlvl(48, c) + "Plate Body" + this.GetForlvl(18, c), 1101);
		c.getPA().sendFrame126(this.GetForlvl(46, c) + "Plate Legs" + this.GetForlvl(16, c), 1099);
		c.getPA().sendFrame126(this.GetForlvl(46, c) + "Plate Skirt" + this.GetForlvl(16, c), 1100);
		c.getPA().sendFrame126(this.GetForlvl(44, c) + "2 Hand Sword" + this.GetForlvl(14, c), 1088);
		c.getPA().sendFrame126(this.GetForlvl(42, c) + "Kite Shield" + this.GetForlvl(12, c), 1105);
		c.getPA().sendFrame126(this.GetForlvl(41, c) + "Chain Body" + this.GetForlvl(11, c), 1098);
		c.getPA().sendFrame126("", 11461);
		c.getPA().sendFrame126(this.GetForlvl(40, c) + "Battle Axe" + this.GetForlvl(10, c), 1092);
		c.getPA().sendFrame126(this.GetForlvl(39, c) + "Warhammer" + this.GetForlvl(9, c), 1083);
		c.getPA().sendFrame126(this.GetForlvl(38, c) + "Square Shield" + this.GetForlvl(8, c), 1104);
		c.getPA().sendFrame126(this.GetForlvl(37, c) + "Full Helm" + this.GetForlvl(7, c), 1103);
		c.getPA().sendFrame126(this.GetForlvl(37, c) + "Throwing Knives" + this.GetForlvl(7, c), 1106);
		c.getPA().sendFrame126(this.GetForlvl(36, c) + "Long Sword" + this.GetForlvl(6, c), 1086);
		c.getPA().sendFrame126(this.GetForlvl(35, c) + "Scimitar" + this.GetForlvl(5, c), 1087);
		c.getPA().sendFrame126(this.GetForlvl(35, c) + "Arrowtips" + this.GetForlvl(5, c), 1108);
		c.getPA().sendFrame126(this.GetForlvl(34, c) + "Sword" + this.GetForlvl(4, c), 1085);
		c.getPA().sendFrame126(this.GetForlvl(34, c) + "Bolts" + this.GetForlvl(4, c), 9141);
		c.getPA().sendFrame126(this.GetForlvl(34, c) + "Nails" + this.GetForlvl(4, c), 13358);
		c.getPA().sendFrame126(this.GetForlvl(33, c) + "Medium Helm" + this.GetForlvl(3, c), 1102);
		c.getPA().sendFrame126(this.GetForlvl(32, c) + "Mace" + this.GetForlvl(2, c), 1093);
		c.getPA().sendFrame126(this.GetForlvl(30, c) + "Dagger" + this.GetForlvl(1, c), 1094);
		c.getPA().sendFrame126(this.GetForlvl(31, c) + "Axe" + this.GetForlvl(1, c), 1091);
		c.getPA().sendFrame126(this.GetForlvl(35, c) + "DwarfCannon Ball" + this.GetForlvl(35, c), 1096);
		c.getPA().sendFrame126(this.GetForlvl(36, c) + "Studs" + this.GetForlvl(36, c), 1134);

		c.getPA().sendFrame34(1207, 0, 1119, 1);
		c.getPA().sendFrame34(1353, 0, 1120, 1);
		c.getPA().sendFrame34(1105, 0, 1121, 1);
		c.getPA().sendFrame34(1141, 0, 1122, 1);
		c.getPA().sendFrame34(821, 0, 1123, 10);

		c.getPA().sendFrame34(1281, 1, 1119, 1);
		c.getPA().sendFrame34(1424, 1, 1120, 1);
		c.getPA().sendFrame34(1069, 1, 1121, 1);
		c.getPA().sendFrame34(1157, 1, 1122, 1);
		c.getPA().sendFrame34(41, 1, 1123, 15);

		c.getPA().sendFrame34(1325, 2, 1119, 1);
		c.getPA().sendFrame34(1339, 2, 1120, 1);
		c.getPA().sendFrame34(1083, 2, 1121, 1);
		c.getPA().sendFrame34(1177, 2, 1122, 1);
		c.getPA().sendFrame34(865, 2, 1123, 5);

		c.getPA().sendFrame34(1295, 3, 1119, 1);
		c.getPA().sendFrame34(1365, 3, 1120, 1);
		c.getPA().sendFrame34(1119, 3, 1121, 1);
		c.getPA().sendFrame34(1193, 3, 1122, 1);

		c.getPA().sendFrame34(9425, 3, 1123, 1);
		c.getPA().sendFrame126(oneb + "1bar", 1132);
		c.getPA().sendFrame126(this.GetForlvl(36, c) + "Limbs", 1096);


		c.getPA().sendFrame34(1311, 4, 1119, 1); // 2h

		c.getPA().sendFrame34(3097, 4, 1120, 1);
		c.getPA().sendFrame126(twob + "2bar", 8428);
		c.getPA().sendFrame126(this.GetForlvl(43, c) + "Claws", 8429);

		c.getPA().sendFrame34(1539, 4, 1121, 15);
		c.getPA().sendFrame126(oneb + "1bar", 11459);
		c.getPA().sendFrame126(this.GetForlvl(34, c) + "Nails", 11461);

		c.getPA().sendFrame34(2, 4, 1122, 4);
		c.getPA().sendFrame126(oneb + "1bar", 13357);
		c.getPA().sendFrame126(this.GetForlvl(35, c) + "Cannon balls", 13358);

		c.getPA().sendFrame34(9378, 4, 1123, 10);
		c.getPA().sendFrame126(oneb + "1bar", 1135);
		c.getPA().sendFrame126(this.GetForlvl(33, c) + "Bolts", 1134);

		c.getPA().hideShowLayer(1097, true);
		c.getPA().hideShowLayer(1133, true);
		c.getPA().hideShowLayer(8431, true);
		c.getPA().hideShowLayer(11458, true);

		c.getPA().showInterface(994);
	}

	private void makeIronInterface(Player c) {
		String fiveb = this.GetForBars(2351, 5, c);
		String threeb = this.GetForBars(2351, 3, c);
		String twob = this.GetForBars(2351, 2, c);
		String oneb = this.GetForBars(2351, 1, c);
		c.getPA().sendFrame126(fiveb + "5bars" + fiveb, 1112);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1109);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1110);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1118);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1111);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1095);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1115);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1090);
		c.getPA().sendFrame126(twob + "2bars" + twob, 1113);
		c.getPA().sendFrame126(twob + "2bars" + twob, 1116);
		c.getPA().sendFrame126(twob + "2bars" + twob, 1114);
		c.getPA().sendFrame126(twob + "2bars" + twob, 1089);
		c.getPA().sendFrame126(twob + "2bars" + twob, 8428);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1124);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1125);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1126);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1127);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1128);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1129);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1130);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1131);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 13357);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 11459);
		c.getPA().sendFrame126(this.GetForlvl(33, c) + "Plate Body" + this.GetForlvl(18, c), 1101);
		c.getPA().sendFrame126(this.GetForlvl(31, c) + "Plate Legs" + this.GetForlvl(16, c), 1099);
		c.getPA().sendFrame126(this.GetForlvl(31, c) + "Plate Skirt" + this.GetForlvl(16, c), 1100);
		c.getPA().sendFrame126(this.GetForlvl(29, c) + "2 Hand Sword" + this.GetForlvl(14, c), 1088);
		c.getPA().sendFrame126(this.GetForlvl(27, c) + "Kite Shield" + this.GetForlvl(12, c), 1105);
		c.getPA().sendFrame126(this.GetForlvl(26, c) + "Chain Body" + this.GetForlvl(11, c), 1098);
		c.getPA().sendFrame126(this.GetForlvl(26, c) + "Oil Lantern Frame" + this.GetForlvl(11, c), 11461);
		c.getPA().sendFrame126(this.GetForlvl(25, c) + "Battle Axe" + this.GetForlvl(10, c), 1092);
		c.getPA().sendFrame126(this.GetForlvl(24, c) + "Warhammer" + this.GetForlvl(9, c), 1083);
		c.getPA().sendFrame126(this.GetForlvl(23, c) + "Square Shield" + this.GetForlvl(8, c), 1104);
		c.getPA().sendFrame126(this.GetForlvl(22, c) + "Full Helm" + this.GetForlvl(7, c), 1103);
		c.getPA().sendFrame126(this.GetForlvl(21, c) + "Throwing Knives" + this.GetForlvl(7, c), 1106);
		c.getPA().sendFrame126(this.GetForlvl(21, c) + "Long Sword" + this.GetForlvl(6, c), 1086);
		c.getPA().sendFrame126(this.GetForlvl(20, c) + "Scimitar" + this.GetForlvl(5, c), 1087);
		c.getPA().sendFrame126(this.GetForlvl(20, c) + "Arrowtips" + this.GetForlvl(5, c), 1108);
		c.getPA().sendFrame126(this.GetForlvl(19, c) + "Sword" + this.GetForlvl(4, c), 1085);
		c.getPA().sendFrame126(this.GetForlvl(19, c) + "Bolts" + this.GetForlvl(4, c), 9140);
		c.getPA().sendFrame126(this.GetForlvl(19, c) + "Nails" + this.GetForlvl(4, c), 13358);
		c.getPA().sendFrame126(this.GetForlvl(18, c) + "Medium Helm" + this.GetForlvl(3, c), 1102);
		c.getPA().sendFrame126(this.GetForlvl(17, c) + "Mace" + this.GetForlvl(2, c), 1093);
		c.getPA().sendFrame126(this.GetForlvl(15, c) + "Dagger" + this.GetForlvl(1, c), 1094);
		c.getPA().sendFrame126(this.GetForlvl(16, c) + "Axe" + this.GetForlvl(1, c), 1091);

		c.getPA().sendFrame34(1203, 0, 1119, 1);
		c.getPA().sendFrame34(1349, 0, 1120, 1);
		c.getPA().sendFrame34(1101, 0, 1121, 1);
		c.getPA().sendFrame34(1137, 0, 1122, 1);
		c.getPA().sendFrame34(820, 0, 1123, 10);

		c.getPA().sendFrame34(1279, 1, 1119, 1);
		c.getPA().sendFrame34(1420, 1, 1120, 1);
		c.getPA().sendFrame34(1067, 1, 1121, 1);
		c.getPA().sendFrame34(1153, 1, 1122, 1);
		c.getPA().sendFrame34(40, 1, 1123, 15);

		c.getPA().sendFrame34(1323, 2, 1119, 1);
		c.getPA().sendFrame34(1335, 2, 1120, 1);
		c.getPA().sendFrame34(1081, 2, 1121, 1);
		c.getPA().sendFrame34(1175, 2, 1122, 1);
		c.getPA().sendFrame34(863, 2, 1123, 5);

		c.getPA().sendFrame34(1293, 3, 1119, 1);
		c.getPA().sendFrame34(1363, 3, 1120, 1);
		c.getPA().sendFrame34(1115, 3, 1121, 1);
		c.getPA().sendFrame34(1191, 3, 1122, 1);
		c.getPA().sendFrame34(9423, 3, 1123, 1); // limbs
		c.getPA().sendFrame126(oneb + "1bar", 1132);
		c.getPA().sendFrame126(this.GetForlvl(23, c) + "Limbs", 1096);

		c.getPA().sendFrame34(1309, 4, 1119, 1); // 2h

		c.getPA().sendFrame34(3096, 4, 1120, 1); // iron claws
		c.getPA().sendFrame126(twob + "2bar", 8428);
		c.getPA().sendFrame126(this.GetForlvl(28, c) + "Claws", 8429);

		c.getPA().sendFrame34(4540, 4, 1121, 1); // oil lantern frame
		c.getPA().sendFrame126(oneb + "1bar", 11459);
		c.getPA().sendFrame126(this.GetForlvl(26, c) + "Oil lantern frame", 11461);

		c.getPA().sendFrame34(4820, 4, 1122, 15); // nails
		c.getPA().sendFrame126(oneb + "1bar", 13357);
		c.getPA().sendFrame126(this.GetForlvl(19, c) + "Nails", 13358);

		c.getPA().sendFrame34(9377, 4, 1123, 10); // bolts
		c.getPA().sendFrame126(oneb + "1bar", 1135);
		c.getPA().sendFrame126(this.GetForlvl(18, c) + "Bolts", 1134);

		c.getPA().hideShowLayer(1097, true);
		c.getPA().hideShowLayer(1133, true);
		c.getPA().hideShowLayer(8431, true);
		c.getPA().hideShowLayer(11458, true);

		c.getPA().showInterface(994);
	}

	private void makeBronzeInterface(Player c) {
		String fiveb = this.GetForBars(2349, 5, c);
		String threeb = this.GetForBars(2349, 3, c);
		String twob = this.GetForBars(2349, 2, c);
		String oneb = this.GetForBars(2349, 1, c);
		c.getPA().sendFrame126(fiveb + "5bars" + fiveb, 1112);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1109);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1110);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1118);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1111);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1095);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1115);
		c.getPA().sendFrame126(threeb + "3bars" + threeb, 1090);
		c.getPA().sendFrame126(twob + "2bars" + twob, 1113);
		c.getPA().sendFrame126(twob + "2bars" + twob, 1116);
		c.getPA().sendFrame126(twob + "2bars" + twob, 1114);
		c.getPA().sendFrame126(twob + "2bars" + twob, 1089);
		c.getPA().sendFrame126(twob + "2bars" + twob, 8428);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1124);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1125);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1126);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1127);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1128);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1129);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1130);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 1131);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 13357);
		c.getPA().sendFrame126(oneb + "1bar" + oneb, 11459);
		c.getPA().sendFrame126(this.GetForlvl(18, c) + "Plate Body" + this.GetForlvl(18, c), 1101);
		c.getPA().sendFrame126(this.GetForlvl(16, c) + "Plate Legs" + this.GetForlvl(16, c), 1099);
		c.getPA().sendFrame126(this.GetForlvl(16, c) + "Plate Skirt" + this.GetForlvl(16, c), 1100);
		c.getPA().sendFrame126(this.GetForlvl(14, c) + "2 Hand Sword" + this.GetForlvl(14, c), 1088);
		c.getPA().sendFrame126(this.GetForlvl(12, c) + "Kite Shield" + this.GetForlvl(12, c), 1105);
		c.getPA().sendFrame126(this.GetForlvl(11, c) + "Chain Body" + this.GetForlvl(11, c), 1098);
		c.getPA().sendFrame126(this.GetForlvl(10, c) + "Battle Axe" + this.GetForlvl(10, c), 1092);
		c.getPA().sendFrame126(this.GetForlvl(9, c) + "Warhammer" + this.GetForlvl(9, c), 1083);
		c.getPA().sendFrame126(this.GetForlvl(8, c) + "Square Shield" + this.GetForlvl(8, c), 1104);
		c.getPA().sendFrame126(this.GetForlvl(7, c) + "Full Helm" + this.GetForlvl(7, c), 1103);
		c.getPA().sendFrame126(this.GetForlvl(7, c) + "Throwing Knives" + this.GetForlvl(7, c), 1106);
		c.getPA().sendFrame126(this.GetForlvl(6, c) + "Long Sword" + this.GetForlvl(6, c), 1086);
		c.getPA().sendFrame126(this.GetForlvl(5, c) + "Scimitar" + this.GetForlvl(5, c), 1087);
		c.getPA().sendFrame126(this.GetForlvl(5, c) + "Arrowtips" + this.GetForlvl(5, c), 1108);
		c.getPA().sendFrame126(this.GetForlvl(4, c) + "Sword" + this.GetForlvl(4, c), 1085);
		c.getPA().sendFrame126(this.GetForlvl(4, c) + "Dart tips" + this.GetForlvl(4, c), 1107);
		c.getPA().sendFrame126(this.GetForlvl(4, c) + "Nails" + this.GetForlvl(4, c), 13358);
		c.getPA().sendFrame126(this.GetForlvl(3, c) + "Medium Helm" + this.GetForlvl(3, c), 1102);
		c.getPA().sendFrame126(this.GetForlvl(2, c) + "Mace" + this.GetForlvl(2, c), 1093);
		c.getPA().sendFrame126(this.GetForlvl(1, c) + "Dagger" + this.GetForlvl(1, c), 1094);
		c.getPA().sendFrame126(this.GetForlvl(1, c) + "Axe" + this.GetForlvl(1, c), 1091);

		c.getPA().sendFrame34(1205, 0, 1119, 1);
		c.getPA().sendFrame34(1351, 0, 1120, 1);
		c.getPA().sendFrame34(1103, 0, 1121, 1);
		c.getPA().sendFrame34(1139, 0, 1122, 1);
		c.getPA().sendFrame34(819, 0, 1123, 10);

		c.getPA().sendFrame34(1277, 1, 1119, 1);
		c.getPA().sendFrame34(1422, 1, 1120, 1);
		c.getPA().sendFrame34(1075, 1, 1121, 1);
		c.getPA().sendFrame34(1155, 1, 1122, 1);
		c.getPA().sendFrame34(39, 1, 1123, 15);

		c.getPA().sendFrame34(1321, 2, 1119, 1);
		c.getPA().sendFrame34(1337, 2, 1120, 1);
		c.getPA().sendFrame34(1087, 2, 1121, 1);
		c.getPA().sendFrame34(1173, 2, 1122, 1);
		c.getPA().sendFrame34(864, 2, 1123, 5);

		c.getPA().sendFrame34(1291, 3, 1119, 1);
		c.getPA().sendFrame34(1375, 3, 1120, 1);
		c.getPA().sendFrame34(1117, 3, 1121, 1);
		c.getPA().sendFrame34(1189, 3, 1122, 1);
		c.getPA().sendFrame34(9420, 3, 1123, 1); // limbs
		c.getPA().sendFrame126(oneb + "1bar", 1132);
		c.getPA().sendFrame126(this.GetForlvl(6, c) + "Limbs", 1096);

		c.getPA().sendFrame34(1307, 4, 1119, 1); // bronze 2h
		c.getPA().sendFrame34(3095, 4, 1120, 1); // claws
		c.getPA().sendFrame126(twob + "2bar", 8428);
		c.getPA().sendFrame126(this.GetForlvl(13, c) + "Claws", 8429);

		c.getPA().sendFrame34(1794, 4, 1121, 1); // wire
		c.getPA().sendFrame126(oneb + "1bar", 11459);
		c.getPA().sendFrame126(this.GetForlvl(4, c) + "Wire", 11461);

		c.getPA().sendFrame34(4819, 4, 1122, 15); // nails
		c.getPA().sendFrame126(oneb + "1bar", 13357);
		c.getPA().sendFrame126(this.GetForlvl(4, c) + "Nails", 13358);

		c.getPA().sendFrame34(9375, 4, 1123, 10); // bolts
		c.getPA().sendFrame126(oneb + "1bar", 1135);
		c.getPA().sendFrame126(this.GetForlvl(4, c) + "Bolts", 1134);

		c.getPA().hideShowLayer(1097, true);
		c.getPA().hideShowLayer(1133, true);
		c.getPA().hideShowLayer(8431, true);
		c.getPA().hideShowLayer(11458, true);

		c.getPA().showInterface(994);
	}

	private String GetForlvl(int i, Player c) {
		if (c.level[13] >= i) {
			return "@whi@";
		}
		return "@bla@";
	}

	private String GetForBars(int i, int j, Player c) {
		if (c.getItems().playerHasItem(i, j)) {
			return "@gre@";
		}
		return "@red@";
	}
}