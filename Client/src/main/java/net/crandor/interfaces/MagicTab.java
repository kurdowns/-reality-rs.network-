package net.crandor.interfaces;

import net.crandor.RSInterface;

/**
 * Created by Stuart on 28/09/2014.
 */
public class MagicTab {
	
    public static int[] spellIds = {54240, 1152, 1153, 54246, 1154, 1155, 1156, 1157, 1158, 1159, 1160, 1161, 1572, 1162, 1163, 1164, 1165, 1166,
            1167, 1168, 1169, 1170, 1171, 1172, 1173, 1174, 1175, 1176, 1539, 1582, 12037, 1540 ,1177, 1178, 1179, 1180, 1541,
            1181, 1182, 15877, 1190, 1191, 1192, 7455, 1183, 1184, 54252, 1185, 1186, 1542, 1187, 1188, 1543, 12425, 1189, 54264, 1592,
            1562, 1193, 54270, 12435, 12445, 54280, 6003, 54292, 12455, 54300
    };
    
    public static int[] spellHovers = {54241, 1199, 1206, 54247, 1215, 1224, 1231, 1240, 1249,
            1258, 1267, 1274, 1283, 1573, 1290, 1299, 1308, 1315, 1324,
            1333, 1340, 1349, 1358, 1367, 1374, 1381, 1388, 1397, 1404,
            1583, 12038, 1414, 1421, 1430, 1437, 1446, 1453, 1460, 1469,
            15878, 1602, 1613, 1624, 7456, 1478, 1485, 1494, 1503, 1512,
            1521, 1530, 1544, 1553, 1563, 1593, 1635, 12426, 12436, 12446,
            12456, 6004, 54253, 54265, 54271, 54281, 54301, 54293};

    public static int[][] spellPlacement = {
            {1, 11},
            {28, 15},
            {50, 10},
            {82, 13},
            {111, 17},
            {135, 16},
            {2, 41},
            {25, 36},
            {55, 44},
            {77, 39},
            {109, 42},
            {134, 42},
            {-1, 66},
            {28, 67},
            {55, 70},
            {81, 69},
            {110, 69},
            {137, 69},
            {0, 96},
            {28, 94},
            {56, 95},
            {81, 96},
            {110, 95},
            {136, 94},
            {0, 121},
            {28, 122},
            {55, 122},
            {81, 126},
            {108, 124},
            {134, 120},
            {-3, 151},
            {28, 152},
            {54, 152},
            {77, 151},
            {109, 153},
            {136, 151},
            {0, 178},
            {28, 180},
            {56, 182},
            {80, 177},
            {108, 178},
            {135, 178},
            {0, 208},
            {28, 207},
            {54, 203},
            {85, 207},
            {108, 205},
            {134, 202},
            {1, 235},
            {27, 235},
            {58, 235},
            {82, 230},
            {108, 231},
            {136, 232},
            {-2, 257},
            {28, 261},
            {56, 260},
            {83, 260},
            {111, 261},
            {135, 259},
            {0, 288},
            {28, 288},
            {56, 287},
            {86, 290},
            {110, 287},
            {138, 288},
            {-1, 314},
    };

    public static void magicTab() {
        RSInterface tab = RSInterface.addTabInterface(1151);

        RSInterface spellButtons = RSInterface.interfaceCache[12424];
        spellButtons.type = 0;
        spellButtons.scrollMax = 340;
        spellButtons.height = 245;
        spellButtons.width = 160;

        // places the spell container
        tab.totalChildren(68);
        tab.child(0, 12424, 15, 0);

        for (int i = 0; i < spellHovers.length; i++) {
            tab.child(i + 1, spellHovers[i], 5, i > 34 ? 8 : 183);
        }

        /**
         * Home button
         */
        RSInterface.addButton(54240, 1, "magic/on", "Cast <col=00ff00>Home Teleport");
        RSInterface button = RSInterface.interfaceCache[54240];
        button.mOverInterToTrigger = 54241;

        /**
         * Home hover ccc
         */
        RSInterface hover = RSInterface.addTabInterface(54241);
        hover.isMouseoverTriggered = true;
        RSInterface.addText(54242, "Level 0: Home Teleport", 1, 0xFE981F, true, true, 0);
        RSInterface.addText(54243, "A teleport which requires no", 0, 0xAF6A1A, true, true, 0);
        RSInterface.addText(54244, "runes and no required level that", 0, 0xAF6A1A, true, true, 0);
        RSInterface.addText(54245, "teleports you to the main land.", 0, 0xAF6A1A, true, true, 0);
        hover.totalChildren(6);
        hover.child(0, 1196, 3, 4);
        hover.child(1, 1197, 3, 4);
        hover.child(2, 54242, 91, 5);
        hover.child(3, 54243, 91, 30);
        hover.child(4, 54244, 91, 40);
        hover.child(5, 54245, 91, 50);

        /**
         * Enchant
         */
        RSInterface.addButton(54246, 1, "magic/on", "Cast <col=00ff00>Level 4: Enchant Crossbow Bolt");
        button = RSInterface.interfaceCache[54246];
        button.mOverInterToTrigger = 54247;

        button.valueCompareType = new int[3];
        button.valueCompareType[0] = 3;
        button.valueCompareType[1] = 3;
        button.valueCompareType[2] = 3;
        button.requiredValues = new int[3];
        button.requiredValues[0] = 3;
        button.requiredValues[1] = 0;
        button.requiredValues[2] = 0;
        button.valueIndexArray = new int[3][4];
        button.valueIndexArray[0][0] = 1;
        button.valueIndexArray[0][1] = 6;
        button.valueIndexArray[0][2] = 0;
        button.valueIndexArray[1][0] = 4;
        button.valueIndexArray[1][1] = 3214;
        button.valueIndexArray[1][2] = 564;
        button.valueIndexArray[1][3] = 0;
        button.valueIndexArray[2][0] = 4;
        button.valueIndexArray[2][1] = 3214;
        button.valueIndexArray[2][2] = 555;
        button.valueIndexArray[2][3] = 0;

        /**
         * enchant hover ccc
         */
        hover = RSInterface.addTabInterface(54247);
        hover.isMouseoverTriggered = true;
        RSInterface.addText(54248, "Level 4: Enchant Crossbow Bolt", 1, 0xFE981F, true, true, 0);
        RSInterface.drawItemModel(54316, 564, 900);
        RSInterface.addRuneText(54317, 1, 564);
        RSInterface.drawItemModel(54318, 555, 900);
        RSInterface.addRuneText(54319, 1, 555);

        hover.totalChildren(7);
        hover.child(0, 1196, 3, 4);
        hover.child(1, 1197, 3, 4);
        hover.child(2, 54248, 91, 5);
        hover.child(3, 54316, 60, 30);
        hover.child(4, 54317, 75, 60);
        hover.child(5, 54318, 100, 30);
        hover.child(6, 54319, 114, 60);

        /**
         * Trollhiem button
         */
        RSInterface.addButton(54252, 47, "magic/on", "Cast <col=00ff00>Teleport to Ape Atoll");
        button = RSInterface.interfaceCache[54252];
        button.mOverInterToTrigger = 54253;

        /**
         * trollhiem hover
         */
        hover = RSInterface.addTabInterface(54253);
        hover.isMouseoverTriggered = true;
        RSInterface.addText(54254, "Level 64: Teleport to Ape Atoll", 1, 0xFE981F, true, true, 0);
        RSInterface.addText(54255, "Teleports you to Ape Atoll", 0, 0xAF6A1A, true, true, 0);
        RSInterface.drawItemModel(54256, 563, 900);
        RSInterface.addRuneText(54257, 2, 563);
        RSInterface.drawItemModel(54258, 554, 900);
        RSInterface.addRuneText(54259, 2, 554);
        RSInterface.drawItemModel(54260, 555, 900);
        RSInterface.addRuneText(54261, 2, 555);
        RSInterface.drawItemModel(54262, 1963, 1200);
        RSInterface.addRuneText(54263, 1, 1963);
        hover.totalChildren(12);
        hover.child(0, 1196, 3, 4);
        hover.child(1, 1197, 3, 4);
        hover.child(2, 54254, 91, 5);
        hover.child(3, 54255, 91, 20);
        hover.child(4, 54256, 20, 30);
        hover.child(5, 54257, 34, 60);
        hover.child(6, 54258, 60, 30);
        hover.child(7, 54259, 74, 60);
        hover.child(8, 54260, 100, 30);
        hover.child(9, 54261, 114, 60);
        hover.child(10, 54262, 133, 30);
        hover.child(11, 54263, 151, 60);

        /**
         * Storm of Armadyl button
         */
        RSInterface.addButton(54264, 56, "magic/off", "Cast <col=00ff00>Storm of Armadyl");
        button = RSInterface.interfaceCache[54264];
        button.targetBitMask = 10;
        button.type = 5;
        button.atActionType = 2;
        button.targetVerb = "Cast on";
        button.targetName = "Storm of Armadyl";
        button.mOverInterToTrigger = 54265;

        button.valueCompareType = new int[2];
        button.valueCompareType[0] = 3;
        button.valueCompareType[1] = 3;
        button.requiredValues = new int[2];
        button.requiredValues[0] = 76;
        button.requiredValues[1] = 0;
        button.valueIndexArray = new int[2][7];
        button.valueIndexArray[0][0] = 1;
        button.valueIndexArray[0][1] = 6;
        button.valueIndexArray[0][2] = 0;
        button.valueIndexArray[1][0] = 4;
        button.valueIndexArray[1][1] = 3214;
        button.valueIndexArray[1][2] = 21773;
        button.valueIndexArray[1][3] = 10;
        button.valueIndexArray[1][4] = 1688; // equipment interface;
        button.valueIndexArray[1][5] = 21777;
        button.valueIndexArray[1][6] = 0;

        /**
         * Storm of Armadyl hover
         */
        hover = RSInterface.addTabInterface(54265);
        hover.isMouseoverTriggered = true;
        RSInterface.addText(54266, "Level 77: Storm of Armadyl", 1, 0xFE981F, true, true, 0);
        RSInterface.drawItemModel(54267, 21773, 900);
        RSInterface.addRuneText(54268, 1, 21773);
        RSInterface.addText(54269, "A very powerful air spell.", 0, 0xAF6A1A, true, true, 0);
        hover.totalChildren(6);
        hover.child(0, 1196, 3, 4);
        hover.child(1, 1197, 3, 4);
        hover.child(2, 54266, 91, 5);
        hover.child(3, 54267, 80, 30);
        hover.child(4, 54268, 95, 60);
        hover.child(5, 54269, 91, 18);

        /**
         *  wind surge
         */
        RSInterface.addButton(54270, 60, "magic/off", "Cast <col=00ff00>Air Surge");
        button = RSInterface.interfaceCache[54270];
        button.targetBitMask = 10;
        button.type = 5;
        button.atActionType = 2;
        button.targetVerb = "Cast on";
        button.targetName = "Air Surge";
        button.mOverInterToTrigger = 54271;

        button.valueCompareType = new int[4];
        button.valueCompareType[0] = 3;
        button.valueCompareType[1] = 3;
        button.valueCompareType[2] = 3;
        button.valueCompareType[3] = 3;

        button.requiredValues = new int[4];
        button.requiredValues[0] = 80;
        button.requiredValues[1] = 0;
        button.requiredValues[2] = 0;
        button.requiredValues[3] = 6;

        button.valueIndexArray = new int[4][7];
        button.valueIndexArray[0][0] = 1;
        button.valueIndexArray[0][1] = 6;
        button.valueIndexArray[0][2] = 0;

        button.valueIndexArray[1][0] = 4;
        button.valueIndexArray[1][1] = 3214;
        button.valueIndexArray[1][2] = 565;
        button.valueIndexArray[1][3] = 0;

        button.valueIndexArray[2][0] = 4;
        button.valueIndexArray[2][1] = 3214;
        button.valueIndexArray[2][2] = 560;
        button.valueIndexArray[2][3] = 0;

        button.valueIndexArray[3][0] = 4;
        button.valueIndexArray[3][1] = 3214;
        button.valueIndexArray[3][2] = 556;
        button.valueIndexArray[3][3] = 10;
        button.valueIndexArray[3][4] = 1688; // equipment interface;
        button.valueIndexArray[3][5] = 1381;
        button.valueIndexArray[3][6] = 0;

        /**
         * air surge hover
         */
        hover = RSInterface.addTabInterface(54271);
        hover.isMouseoverTriggered = true;
        RSInterface.addText(54272, "Level 81: Air Surge", 1, 0xFE981F, true, true, 0);
        RSInterface.addText(54273, "A very powerful air spell.", 0, 0xAF6A1A, true, true, 0);
        RSInterface.drawItemModel(54274, 565, 900);
        RSInterface.addRuneText(54275, 1, 565);
        RSInterface.drawItemModel(54276, 560, 900);
        RSInterface.addRuneText(54277, 1, 560);
        RSInterface.drawItemModel(54278, 556, 900);
        RSInterface.addRuneText(54279, 7, 556);
        setSpellHoveroverClientScript(54279, 556, 1405, 7);

        hover.totalChildren(10);
        hover.child(0, 1196, 3, 4);
        hover.child(1, 1197, 3, 4);
        hover.child(2, 54272, 91, 5);
        hover.child(3, 54273, 91, 20);
        hover.child(4, 54274, 30, 30);
        hover.child(5, 54275, 44, 60);
        hover.child(6, 54276, 70, 30);
        hover.child(7, 54277, 84, 60);
        hover.child(8, 54278, 110, 30);
        hover.child(9, 54279, 124, 60);

        /**
         *  Water surge
         */
        RSInterface.addButton(54280, 63, "magic/off", "Cast <col=00ff00>Water Surge");
        button = RSInterface.interfaceCache[54280];
        button.targetBitMask = 10;
        button.type = 5;
        button.atActionType = 2;
        button.targetVerb = "Cast on";
        button.targetName = "Water Surge";
        button.mOverInterToTrigger = 54281;

        button.valueCompareType = new int[5];
        button.valueCompareType[0] = 3;
        button.valueCompareType[1] = 3;
        button.valueCompareType[2] = 3;
        button.valueCompareType[3] = 3;
        button.valueCompareType[4] = 3;

        button.requiredValues = new int[5];
        button.requiredValues[0] = 84;
        button.requiredValues[1] = 0;
        button.requiredValues[2] = 0;
        button.requiredValues[3] = 9;
        button.requiredValues[4] = 6;

        button.valueIndexArray = new int[5][7];
        button.valueIndexArray[0][0] = 1;
        button.valueIndexArray[0][1] = 6;
        button.valueIndexArray[0][2] = 0;

        button.valueIndexArray[1][0] = 4;
        button.valueIndexArray[1][1] = 3214;
        button.valueIndexArray[1][2] = 565;
        button.valueIndexArray[1][3] = 0;

        button.valueIndexArray[2][0] = 4;
        button.valueIndexArray[2][1] = 3214;
        button.valueIndexArray[2][2] = 560;
        button.valueIndexArray[2][3] = 0;

        button.valueIndexArray[3][0] = 4;
        button.valueIndexArray[3][1] = 3214;
        button.valueIndexArray[3][2] = 555;
        button.valueIndexArray[3][3] = 10;
        button.valueIndexArray[3][4] = 1688; // equipment interface;
        button.valueIndexArray[3][5] = 1403;
        button.valueIndexArray[3][6] = 0;

        button.valueIndexArray[4][0] = 4;
        button.valueIndexArray[4][1] = 3214;
        button.valueIndexArray[4][2] = 556;
        button.valueIndexArray[4][3] = 10;
        button.valueIndexArray[4][4] = 1688; // equipment interface;
        button.valueIndexArray[4][5] = 1381;
        button.valueIndexArray[4][6] = 0;

        /**
         * water surge hover
         */
        hover = RSInterface.addTabInterface(54281);
        hover.isMouseoverTriggered = true;
        RSInterface.addText(54282, "Level 85: Water Surge", 1, 0xFE981F, true, true, 0);
        RSInterface.addText(54283, "A very powerful water spell.", 0, 0xAF6A1A, true, true, 0);
        RSInterface.drawItemModel(54284, 565, 900);
        RSInterface.addRuneText(54285, 1, 565);

        RSInterface.drawItemModel(54286, 560, 900);
        RSInterface.addRuneText(54287, 1, 560);

        RSInterface.drawItemModel(54288, 555, 900);
        RSInterface.addRuneText(54289, 10, 555);
        setSpellHoveroverClientScript(54289, 555, 1403, 10);

        RSInterface.drawItemModel(54290, 556, 900);
        RSInterface.addRuneText(54291, 7, 556);
        setSpellHoveroverClientScript(54291, 556, 1405, 7);

        hover.totalChildren(12);
        hover.child(0, 1196, 3, 4);
        hover.child(1, 1197, 3, 4);
        hover.child(2, 54282, 91, 5);
        hover.child(3, 54283, 91, 20);
        hover.child(4, 54284, 20, 30);
        hover.child(5, 54285, 34, 60);
        hover.child(6, 54286, 60, 30);
        hover.child(7, 54287, 74, 60);
        hover.child(8, 54288, 100, 30);
        hover.child(9, 54289, 114, 60);
        hover.child(10, 54290, 135, 30);
        hover.child(11, 54291, 151, 60);


        RSInterface.addButton(54292, 60, "magic/off", "Cast <col=00ff00>Earth Surge");
        button = RSInterface.interfaceCache[54292];
        button.targetBitMask = 10;
        button.type = 5;
        button.atActionType = 2;
        button.targetVerb = "Cast on";
        button.targetName = "Earth Surge";
        button.mOverInterToTrigger = 54293;

        button.valueCompareType = new int[5];
        button.valueCompareType[0] = 3;
        button.valueCompareType[1] = 3;
        button.valueCompareType[2] = 3;
        button.valueCompareType[3] = 3;
        button.valueCompareType[4] = 3;

        button.requiredValues = new int[5];
        button.requiredValues[0] = 89;
        button.requiredValues[1] = 0;
        button.requiredValues[2] = 0;
        button.requiredValues[3] = 9;
        button.requiredValues[4] = 6;

        button.valueIndexArray = new int[5][7];
        button.valueIndexArray[0][0] = 1;
        button.valueIndexArray[0][1] = 6;
        button.valueIndexArray[0][2] = 0;

        button.valueIndexArray[1][0] = 4;
        button.valueIndexArray[1][1] = 3214;
        button.valueIndexArray[1][2] = 565; // blood rune
        button.valueIndexArray[1][3] = 0;

        button.valueIndexArray[2][0] = 4;
        button.valueIndexArray[2][1] = 3214;
        button.valueIndexArray[2][2] = 560; // death rune
        button.valueIndexArray[2][3] = 0;

        button.valueIndexArray[3][0] = 4;
        button.valueIndexArray[3][1] = 3214;
        button.valueIndexArray[3][2] = 557;
        button.valueIndexArray[3][3] = 10;
        button.valueIndexArray[3][4] = 1688; // equipment interface;
        button.valueIndexArray[3][5] = 1407; // earth staff
        button.valueIndexArray[3][6] = 0;

        button.valueIndexArray[4][0] = 4;
        button.valueIndexArray[4][1] = 3214;
        button.valueIndexArray[4][2] = 556;
        button.valueIndexArray[4][3] = 10;
        button.valueIndexArray[4][4] = 1688; // equipment interface;
        button.valueIndexArray[4][5] = 1405;
        button.valueIndexArray[4][6] = 0;

        hover = RSInterface.addTabInterface(54293);
        hover.isMouseoverTriggered = true;
        RSInterface.addText(54294, "Level 90: Earth Surge", 1, 0xFE981F, true, true, 0);
        RSInterface.addText(54295, "A very powerful earth spell.", 0, 0xAF6A1A, true, true, 0);
        RSInterface.drawItemModel(54296, 565, 900);
        RSInterface.addRuneText(54297, 1, 565);
        RSInterface.drawItemModel(54298, 560, 900);
        RSInterface.addRuneText(54299, 1, 560);

        RSInterface.drawItemModel(54308, 557, 900);
        RSInterface.addRuneText(54309, 10, 557);
        setSpellHoveroverClientScript(54309, 557, 1407, 10);

        RSInterface.drawItemModel(54310, 556, 900);
        RSInterface.addRuneText(54311, 7, 556);
        setSpellHoveroverClientScript(54311, 556, 1405, 7);

        hover.totalChildren(12);
        hover.child(0, 1196, 3, 4);
        hover.child(1, 1197, 3, 4);
        hover.child(2, 54294, 91, 5);
        hover.child(3, 54295, 91, 20);
        hover.child(4, 54296, 20, 30);
        hover.child(5, 54297, 34, 60);
        hover.child(6, 54298, 60, 30);
        hover.child(7, 54299, 74, 60);
        hover.child(8, 54308, 100, 30);
        hover.child(9, 54309, 114, 60);
        hover.child(10, 54310, 135, 30);
        hover.child(11, 54311, 151, 60);

        RSInterface.addButton(54300, 60, "magic/off", "Cast <col=00ff00>Fire Surge");
        button = RSInterface.interfaceCache[54300];
        button.targetBitMask = 10;
        button.type = 5;
        button.atActionType = 2;
        button.targetVerb = "Cast on";
        button.targetName = "Fire Surge";
        button.mOverInterToTrigger = 54301;

        button.valueCompareType = new int[5];
        button.valueCompareType[0] = 3;
        button.valueCompareType[1] = 3;
        button.valueCompareType[2] = 3;
        button.valueCompareType[3] = 3;
        button.valueCompareType[4] = 3;

        button.requiredValues = new int[5];
        button.requiredValues[0] = 94;
        button.requiredValues[1] = 0;
        button.requiredValues[2] = 0;
        button.requiredValues[3] = 9;
        button.requiredValues[4] = 6;

        button.valueIndexArray = new int[5][10];
        button.valueIndexArray[0][0] = 1;
        button.valueIndexArray[0][1] = 6;
        button.valueIndexArray[0][2] = 0;

        button.valueIndexArray[1][0] = 4;
        button.valueIndexArray[1][1] = 3214;
        button.valueIndexArray[1][2] = 565;
        button.valueIndexArray[1][3] = 0;

        button.valueIndexArray[2][0] = 4;
        button.valueIndexArray[2][1] = 3214;
        button.valueIndexArray[2][2] = 560;
        button.valueIndexArray[2][3] = 0;

        button.valueIndexArray[3][0] = 4;
        button.valueIndexArray[3][1] = 3214;
        button.valueIndexArray[3][2] = 554;
        button.valueIndexArray[3][3] = 10;
        button.valueIndexArray[3][4] = 1688; // equipment interface;
        button.valueIndexArray[3][5] = 1401;
        button.valueIndexArray[3][6] = 10;
        button.valueIndexArray[3][7] = 1688;
        button.valueIndexArray[3][8] = 1387;
        button.valueIndexArray[3][9] = 0;

        button.valueIndexArray[4][0] = 4;
        button.valueIndexArray[4][1] = 3214;
        button.valueIndexArray[4][2] = 556;
        button.valueIndexArray[4][3] = 10;
        button.valueIndexArray[4][4] = 1688; // equipment interface;
        button.valueIndexArray[4][5] = 1405;
        button.valueIndexArray[4][6] = 10;
        button.valueIndexArray[4][7] = 1688; // equipment interface
        button.valueIndexArray[4][8] = 1381;
        button.valueIndexArray[4][9] = 0;

        hover = RSInterface.addTabInterface(54301);
        hover.isMouseoverTriggered = true;
        RSInterface.addText(54302, "Level 95: Fire Surge", 1, 0xFE981F, true, true, 0);
        RSInterface.addText(54303, "A very powerful fire spell.", 0, 0xAF6A1A, true, true, 0);
        RSInterface.drawItemModel(54304, 565, 900);
        RSInterface.addRuneText(54305, 2, 565);

        RSInterface.drawItemModel(54306, 560, 900);
        RSInterface.addRuneText(54307, 1, 560);

        RSInterface.drawItemModel(54312, 554, 900);
        RSInterface.addRuneText(54313, 10, 554);
        setSpellHoveroverClientScript(54313, 554, 1401, 10);

        RSInterface.drawItemModel(54314, 556, 900);
        RSInterface.addRuneText(54315, 7, 556);
        setSpellHoveroverClientScript(54315, 556, 1405, 7);

        hover.totalChildren(12);
        hover.child(0, 1196, 3, 4);
        hover.child(1, 1197, 3, 4);
        hover.child(2, 54302, 91, 5);
        hover.child(3, 54303, 91, 20);
        hover.child(4, 54304, 20, 30);
        hover.child(5, 54305, 34, 60);
        hover.child(6, 54306, 60, 30);
        hover.child(7, 54307, 74, 60);
        hover.child(8, 54312, 100, 30);
        hover.child(9, 54313, 114, 60);
        hover.child(10, 54314, 135, 30);
        hover.child(11, 54315, 151, 60);

        RSInterface.setChildren(spellIds.length, spellButtons);
        for (int i = 0; i < spellIds.length; i++) {
            RSInterface spell = RSInterface.interfaceCache[spellIds[i]];

            if(spell.tooltip != null && spell.tooltip.toLowerCase().contains("teleport")) {
                spell.sprite1 = RSInterface.imageLoader(i + 1, "magic/on");
                spell.sprite2 = RSInterface.imageLoader(i + 1, "magic/on");
            } else {
                spell.sprite1 = RSInterface.imageLoader(i + 1, "magic/off");
                spell.sprite2 = RSInterface.imageLoader(i + 1, "magic/on");
            }

            spell.width = spell.sprite1.myWidth;
            spell.height = spell.sprite1.myHeight;

            spellButtons.child(i, spellIds[i], spellPlacement[i][0], spellPlacement[i][1]);
        }
    }

    private static void setSpellHoveroverClientScript(int interfaceId, int runeId, int staffId, int amount) {

        int[] staffIds = null;
        RuneStaffIds[] values = RuneStaffIds.values();
        for(RuneStaffIds r : values) {
            if(r.runeId == runeId) {
                staffIds = r.staffIds;
                break;
            }
        }

        RSInterface rsInterface = RSInterface.interfaceCache[interfaceId];

        rsInterface.valueCompareType = new int[1];
        rsInterface.requiredValues = new int[1];
        rsInterface.valueCompareType[0] = 3;
        rsInterface.requiredValues[0] = amount == 1 ? 0 : amount;

        rsInterface.valueIndexArray = new int[1][ staffIds == null ? 4 : (staffIds.length * 3) + 4];
        rsInterface.valueIndexArray[0][0] = 4;
        rsInterface.valueIndexArray[0][1] = 3214;
        rsInterface.valueIndexArray[0][2] = runeId;
        rsInterface.valueIndexArray[0][3] = 0;

        int index = 3;
        if(staffIds != null) {
            for(int i = 0; i < staffIds.length; i++) {
                rsInterface.valueIndexArray[0][index++] = 10;
                rsInterface.valueIndexArray[0][index++] = 1688; // equipment interface;
                rsInterface.valueIndexArray[0][index++] = staffIds[i];
                if(i + 1 > staffIds.length) {
                    rsInterface.valueIndexArray[0][index++] = 0;
                }
            }
        }

        staffIds = null;
        values = null;
    }

    private enum RuneStaffIds {

        AIR(556, new int[] { 1381, 1397, 1405}),
        EARTH(557, new int[] { 1395, 1399, 1407}),
        WATER(555, new int[] { 1383, 1395, 1403}),
        FIRE(554, new int[] { 1387, 1393, 1401});

        int runeId;
        int[] staffIds;

        RuneStaffIds(int runeId, int[] staffIds) {
            this.runeId = runeId;
            this.staffIds = staffIds;
        }

    }


}
