package net.crandor;

import net.crandor.interfaces.MagicTab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RSInterface {

	public static final int WHITE_TEXT = 0xFFFFFF;
	public static final int GREY_TEXT = 0xB9B855;
	public static final int ORANGE_TEXT = 0xFF981F;
	public static final MRUNodes aMRUNodes_264 = new MRUNodes(30);


	private static final String TRADING_POST_SPRITES = "Interfaces/TradingPost/sprite";

	public static void tradingPostCollect() {
		int id = 76_000;
		int frame = 0;

		RSInterface tab = addTabInterface(id);
		id++;

		tab.totalChildren(12);

		addSprite(id, 923);
		tab.child(frame++, id, 8, 41);
		id++;

		addText(id, "View and collect your awaiting items from Trading Post", 0xFFFFFF, true, true, 0,
				-1);
		tab.child(frame++, id, 255, 66);
		id++;

		addText(id, "@or1@Varrock Trading Post Collection", 00000, true, true, -1, 2);
		tab.child(frame++, id, 255, 45);
		id++;

		addTextClose(40000);
		setBounds(40000, 420, 46, frame, tab);
		frame++;
		id++;

		System.out.println("trading post collection coin container " + id);
		addToItemGroup(id, 1, 1, 0, 3, true, "Withdraw to @gre@Inventory", "Withdraw to @gre@Bank",
				null);
		setBounds(id, 46, 151, frame, tab);
		frame++;
		id++;

		addHoverButton(id, 912, 111, 35, "Select @gre@Withdraw All To Inventory", -1, id + 1, 5);
		addHoveredButton(id + 1, TRADING_POST_SPRITES, 911, 111, 35, id + 2);
		tab.child(frame++, id, 129, 239);
		tab.child(frame++, id + 1, 129, 239);
		id += 3;

		addText(id, "@or1@Withdraw All\\n@or1@To Inventory", 00000, true, true, 0, -1);
		tab.child(frame++, id, 184, 247);
		id++;

		addHoverButton(id, 912, 111, 35, "Select @gre@Withdraw All To Inventory", -1, id + 1, 5);
		addHoveredButton(id + 1, TRADING_POST_SPRITES, 911, 111, 35, id + 2);
		tab.child(frame++, id, 270, 239);
		tab.child(frame++, id + 1, 270, 239);
		id += 3;

		addText(id, "@or1@Withdraw All\\n@or1@To Bank", 00000, true, true, 0, -1);
		tab.child(frame++, id, 325, 247);
		id++;

		tab.child(frame++, id, 92, 123);

		System.out.println("trading post collection scroll " + id);
		RSInterface scroll = addInterface(id);
		id++;
		scroll.totalChildren(1);
		scroll.height = 86;
		scroll.width = 356;
		scroll.scrollMax = 1400;
		int scroll_frame = 0;

		System.out.println("trading post collection container " + id);
		addToItemGroup(id, 10, 6, 2, 9, true, "Withdraw to @gre@Inventory", "Withdraw to @gre@Bank",
				null);
		setBounds(id, 11, 8, scroll_frame, scroll);
		frame++;
		id++;
	}

	public boolean flashing;
	public int flashRate;

	public static final int TYPE_FLASHING_SPRITE = 23;

	public static RSInterface addFlashingSprite(int id, int flashRate, int spriteId) {
		RSInterface rs = interfaceCache[id] = new RSInterface();
		rs.id = id;
		rs.parentID = id;
		rs.type = TYPE_FLASHING_SPRITE;
		rs.atActionType = 0;
		rs.contentType = 0;
		rs.opacity = 0;
		rs.mOverInterToTrigger = 0;
		Sprite sprite = Client.instance.cacheSprite[spriteId];
		rs.width = sprite.myWidth;
		rs.height = sprite.myHeight;
		rs.sprite1 = sprite;
		rs.sprite2 = sprite;
		rs.flashRate = flashRate;
		return rs;
	}

	public static void tradingPostInventory() {
		int id = 75000;
		int frame = 0;

		RSInterface tab = addTabInterface(id);
		id++;

		tab.totalChildren(2);

		addFlashingSprite(id, 2, 924);
		tab.child(frame++, id, 0, 0);
		id++;

		tab.child(frame++, 3823, 16, 8);
		id++;
	}

	public static void addTextClose(int id) {
		RSInterface rsinterface = addInterface(id);
		rsinterface.id = id;
		rsinterface.parentID = id;
		rsinterface.type = 4;
		rsinterface.atActionType = 3;
		rsinterface.width = 70;
		rsinterface.height = 11;
		rsinterface.contentType = 0;
		rsinterface.opacity = 0;
		rsinterface.mOverInterToTrigger = -1;
		rsinterface.centerText = false;
		rsinterface.textShadow = true;
		rsinterface.textDrawingAreas = 0;
		rsinterface.message = "Close Window";
		rsinterface.tooltip = "Close";
		rsinterface.textColor = 0x808080;
		rsinterface.anInt239 = 0;
		rsinterface.textHoverColor = 0xFFFFFF;
		rsinterface.anInt219 = 0;
	}
	public static void tradingPostHistory() {
		int id = 74500;
		int frame = 0;

		RSInterface tab = addTabInterface(id);
		id++;

		tab.totalChildren(11);

		addSprite(id, 918);
		tab.child(frame++, id, 8, 12);
		id++;

		addText(id, "View your personal and global Trading Post history", 0xFFFFFF, true, true, 0,
				-1);
		tab.child(frame++, id, 255, 46);
		id++;

		addText(id, "@or1@Varrock Trading Post History", 00000, true, true, 2, -1);
		tab.child(frame++, id, 255, 19);
		id++;

		addTextClose(40000);
		setBounds(40000, 420, 19, frame, tab);
		frame++;
		id++;

		addHoverButton(id, 912, 111, 35, "Select @gre@Personal History", -1, id + 1, 5);
		addHoveredButton(id + 1, TRADING_POST_SPRITES, 911, 111, 35, id + 2);
		tab.child(frame++, id, 120, 273);
		tab.child(frame++, id + 1, 120, 273);
		id += 3;

		addHoverButton(id, 912, 111, 35, "Select @gre@Global History", -1, id + 1, 5);
		addHoveredButton(id + 1, TRADING_POST_SPRITES, 911, 111, 35, id + 2);
		tab.child(frame++, id, 260, 273);
		tab.child(frame++, id + 1, 260, 273);
		id += 3;

		addText(id, "@or1@Global History", 00000, true, true, 0, -1);
		tab.child(frame++, id, 316, 285);
		id++;

		addText(id, "@or1@Personal History", 00000, true, true, 0, -1);
		tab.child(frame++, id, 176, 285);
		id++;

		tab.child(frame++, id, 30, 80);

		System.out.println("Trading Post History scroll: " + id);
		RSInterface scroll = addInterface(id);
		id++;
		scroll.totalChildren(1 + (40 * 7));
		scroll.height = 182;
		scroll.width = 433;
		scroll.scrollMax = 1400;
		int scroll_frame = 0;

		int y = 0;
		int x = 0;

		for (int i = 0; i < 40; i++) {

			addSprite(id, i % 2 == 0 ? 921 : 922);
			scroll.child(scroll_frame++, id, x, y);
			id++;

			addText(id, (1 + i) + "", 0xFFFFFF, true, true, 0, -1);
			scroll.child(scroll_frame++, id, x + 6, y + 12);
			id++;

			addText(id, "@or2@" + id, 0xFFFFFF, true, true, 0, -1);
			scroll.child(scroll_frame++, id, x + 88, y + 12);
			id++;

			addText(id, "@or1@" + id, 00000, true, true, 1, -1);
			scroll.child(scroll_frame++, id, x + 170, y + 3);
			id++;

			addText(id, "" + id, 0xFFFFFF, true, true, 0, -1);
			scroll.child(scroll_frame++, id, x + 170, y + 18);
			id++;

			addText(id, "@or3@" + id, 0xFFFFFF, true, true, 0, -1);
			scroll.child(scroll_frame++, id, x + 270, y + 12);
			id++;

			addText(id, "@or1@" + id, 0xFFFFFF, true, true, 0, -1);
			scroll.child(scroll_frame++, id, x + 370, y + 6);
			id++;

			y += 35;
		}

		System.out.println("Trading Post History container: " + id);
		addToItemGroup(id, 1, 40, 0, 3, false, "", "", "");
		setBounds(id, 16, 2, scroll_frame, scroll);
		scroll_frame++;
		id++;
	}

	public static void tradingPostMyShop() {
		int id = 69000;
		int frame = 0;

		RSInterface tab = addTabInterface(id);
		id++;

		String[] AMOUNT = {"+1", "+5", "+10", "All",};

		String[] PRICE = {"+10%", "-10%", "Base", "Enter",};

		tab.totalChildren(17 + (AMOUNT.length * 3) + (PRICE.length * 3));

		addSprite(id, 913);
		tab.child(frame++, id, 8, 12);
		id++;

		addText(id, "View your shop and sell items", 0xFFFFFF, true, true, 0, -1);
		tab.child(frame++, id, 255, 46);
		id++;

		addText(id, "@or1@My Varrock Trading Post", 00000, true, true, 2, -1);
		tab.child(frame++, id, 255, 19);
		id++;

		addTextClose(40000);
		setBounds(40000, 420, 19, frame, tab);
		frame++;
		id++;

		System.out.println("trading post - my shop - item display container: " + id);
		RSInterface item = addToItemGroup(id, 1, 1, 0, 0, false, "", "", "");
		setBounds(id, 182, 235, frame, tab);
		frame++;
		id++;

		addHoverButton(id, 912, 111, 35, "Submit Post", -1, id + 1, 5);
		addHoveredButton(id + 1, TRADING_POST_SPRITES, 911, 111, 35, id + 2);
		tab.child(frame++, id, 240 - 52, 277);
		tab.child(frame++, id + 1, 240 - 52, 277);
		id += 3;

		addText(id, "@or1@" + id, 00000, true, true, 2, 0);
		tab.child(frame++, id, 245, 287);
		id++;

		addText(id, "@or1@" + id, 00000, true, true, 0, 0);
		tab.child(frame++, id, 98, 262);
		id++;

		addText(id, "@or1@" + id, 00000, true, true, 0, 0);
		tab.child(frame++, id, 265, 241);
		id++;

		addConfigButton(id, id, 133, 134, TRADING_POST_SPRITES, 14, 15, "Toggle Feature Item", 2, 5,
				806);
		tab.child(frame++, id++, 58, 286);

		addText(id, "Feature Item", 0xFFFFFF, false, true, 0, -1);
		tab.child(frame++, id, 76, 287);
		id++;

		addText(id, "" + id, 0xFFFFFF, true, true, 0, -1);
		tab.child(frame++, id, 397, 290);
		id++;

		addHoverClickText(id, "Back to Trading Post", "Back to Trading Post", 0, 0x008000, false,
				true, 150);
		setBounds(id, 20, 45, frame, tab);
		frame++;
		id++;

		addHoverClickText(id, "Feature My Shop", "@gre@Feature My Shop", 0, 0x008000, false, true,
				150);
		setBounds(id, 400, 45, frame, tab);
		frame++;
		id++;

		int y = 230;
		int x = 23;

		for (String s : AMOUNT) {
			addHoverButton(id, 914, 38, 28, "Increase amount by @or1@" + s, -1, id + 1, 5);
			addHoveredButton(id + 1, TRADING_POST_SPRITES, 915, 38, 28, id + 2);
			tab.child(frame++, id, x, y);
			tab.child(frame++, id + 1, x, y);
			id += 3;

			addText(id, "@or1@" + s, 00000, true, true, 0, -1);
			tab.child(frame++, id, x + 19, y + 9);
			id++;

			x += 36;
		}

		y = 230;
		x = 323;

		for (String s : PRICE) {
			addHoverButton(id, 914, 38, 28, "@or1@" + s, -1, id + 1, 5);
			addHoveredButton(id + 1, TRADING_POST_SPRITES, 915, 38, 28, id + 2);
			tab.child(frame++, id, x, y);
			tab.child(frame++, id + 1, x, y);
			id += 3;

			addText(id, "@or1@" + s, 00000, true, true, 0, -1);
			tab.child(frame++, id, x + 20, y + 9);
			id++;

			x += 36;
		}

		addText(id, "@or1@" + id, 00000, true, true, 0, -1);
		tab.child(frame++, id, 393, 262);
		id++;

		tab.child(frame++, id, 19, 76);

		System.out.println("trading post my shop scroll: " + id);
		RSInterface scroll = addInterface(id);
		id++;
		scroll.totalChildren(1 + (30 * 5));
		scroll.height = 146;
		scroll.width = 456;
		scroll.scrollMax = 500;
		int scroll_frame = 0;

		y = 0;
		x = 0;

		for (int i = 0; i < 30; i++) {

			addHoverButton(id, 905, 148, 48, "Cancel Post", -1, id + 1, 5);
			addHoveredButton(id + 1, TRADING_POST_SPRITES, 908, 148, 48, id + 2);
			scroll.child(scroll_frame++, id, x, y);
			scroll.child(scroll_frame++, id + 1, x, y);
			id += 3;

			addText(id, "@or1@ " + id, 0xFFFFFF, true, true, 1, 0);
			scroll.child(scroll_frame++, id, x + 92, y + 1);
			id++;

			addText(id, "" + id, 0xFFFFFF, true, true, 0, -1);
			scroll.child(scroll_frame++, id, x + 94, y + 17);
			id++;

			System.out.println("bar: " + id);
			RSInterface bar = addPercentageBar(id, 90, 907, 907, 907, 0, 100, 100, false);
			bar.percentageCompleted = 100;
			scroll.child(scroll_frame++, id, x + 48, y + 30);
			id++;

			id += 5;

			x += 152;

			if (x > 323) {
				x = 0;
				y += 49;
			}
		}
		System.out.println("trading post my shop container: " + id);
		item = addToItemGroup(id, 3, 10, 120, 17, false, "", "", "");
		setBounds(id, 9, 9, scroll_frame++, scroll);
		id++;
	}

	public static void addHoverClickText(int id, String text, String tooltip, int idx, int color,
			boolean center, boolean textShadow, int width) {
		RSInterface rsinterface = addInterface(id);
		rsinterface.id = id;
		rsinterface.parentID = id;
		rsinterface.type = 4;
		rsinterface.atActionType = 1;
		rsinterface.width = width;
		rsinterface.height = idx == 0 ? 12 : 15;
		rsinterface.contentType = 0;
		rsinterface.opacity = 0;
		rsinterface.mOverInterToTrigger = -1;
		rsinterface.centerText = center;
		rsinterface.textShadow = textShadow;
		rsinterface.textDrawingAreas = idx;
		rsinterface.message = text;
		rsinterface.tooltip = tooltip;
		rsinterface.textColor = color;
		rsinterface.anInt219 = 0;
		rsinterface.textHoverColor = color == 0xFFFFFF ? 0 : 0xFFFFFF;
		rsinterface.anInt239 = 0;
	}

	public int percentageCompleted;
	public int percentageTotal;
	public int percentageMultiplier;
	public int percentageSpriteEmpty;
	public int percentageSpriteFull;
	public int percentageBarStart;
	public int percentageBarEnd;
	public int percentageDimension;

	public static final int TYPE_VERTICLE_PERCENTAGE = 22;

	public static RSInterface addPercentageBar(int id, int percentageDimension,
			int percentageBarStart, int percentageBarEnd, int percentageSpritePart,
			int percentageSpriteEmpty, int percentageTotal, int percentageMultiplier,
			boolean verticleBar) {
		RSInterface rs = addInterface(id);
		rs.id = id;
		rs.parentID = id;
		rs.type = verticleBar ? TYPE_VERTICLE_PERCENTAGE : 20;
		rs.atActionType = 0;
		rs.contentType = 0;
		rs.opacity = 0;
		rs.mOverInterToTrigger = 0;
		rs.percentageCompleted = 0;
		rs.percentageDimension = percentageDimension;
		rs.percentageBarStart = percentageBarStart;
		rs.percentageBarEnd = percentageBarEnd;
		rs.percentageSpriteFull = percentageSpritePart;
		rs.percentageSpriteEmpty = percentageSpriteEmpty;
		rs.percentageTotal = percentageTotal;
		rs.percentageMultiplier = percentageMultiplier;
		rs.width = 0;
		rs.height = 0;
		return rs;
	}
	public static void tradingPost() {
		int id = 66000;
		int frame = 0;

		RSInterface tab = addTabInterface(id);
		id++;

		String[] BOTTOM_SELECTION = {"My Shop", "History", "Collection",};

		tab.totalChildren(8 + (4 * 3) + (BOTTOM_SELECTION.length * 4));

		addSprite(id, 900);
		tab.child(frame++, id, 8, 12);
		id++;

		addText(id, "@or1@Varrock Trading Post", 00000, true, true, 2, -1);
		tab.child(frame++, id, 255, 19);
		id++;

		addTextClose(40000);
		setBounds(40000, 420, 19, frame, tab);
		frame++;
		id++;

		addText(id, "Buy and sell items using the Trading Post", 0xFFFFFF, true, true, 0, -1);
		tab.child(frame++, id, 255, 46);
		id++;

		addButton(id, 901, TRADING_POST_SPRITES, "Search for Item");
		tab.child(frame++, id++, 24, 282);

		addText(id, "@or1@Search Item", 0xFFFFFF, false, true, 1, -1);
		tab.child(frame++, id, 52, 284);
		id++;

		id += 5;

		tab.child(frame++, id, 20, 75);

		RSInterface scroll = addInterface(id);
		id++;
		scroll.totalChildren(100);
		scroll.height = 190;
		scroll.width = 113;
		scroll.scrollMax = 1500;
		int scroll_frame = 0;

		int y = 0;

		for (int i = 0; i < 100; i++) {
			addHoverClickText(id, "Username: " + id, "Select", 0, 0xEE9021, false, true, 150);
			setBounds(id, 7, y, scroll_frame, scroll);
			scroll_frame++;
			id++;

			y += 15;
		}

		tab.child(frame++, id, 165, 132);

		System.out.println("trading post search scroll: " + id);
		scroll = addInterface(id);
		id++;
		scroll.totalChildren(1 + (30 * 5));
		scroll.height = 134;
		scroll.width = 308;
		scroll.scrollMax = 1455;
		scroll_frame = 0;

		y = 0;
		int x = 0;

		for (int i = 0; i < 30; i++) {

			addHoverButton(id, 905, 148, 48, "Value post", -1, id + 1, 5);
			addHoveredButton(id + 1, TRADING_POST_SPRITES, 908, 148, 48, id + 2);
			scroll.child(scroll_frame++, id, x, y);
			scroll.child(scroll_frame++, id + 1, x, y);
			id += 3;

			addText(id, "@or1@" + id, 0xFFFFFF, true, true, 1, 0);
			scroll.child(scroll_frame++, id, x + 93, y + 3);
			id++;

			addText(id, "" + id, 0xFFFFFF, true, true, 0, 0);
			scroll.child(scroll_frame++, id, x + 93, y + 17);
			id++;

			RSInterface bar = addPercentageBar(id, 90, 907, 907, 907, 0, 100, 100, false);
			bar.percentageCompleted = 100;
			scroll.child(scroll_frame++, id, x + 48, y + 30);
			id++;

			id += 5;

			x += 153;

			if (x > 153) {
				x = 0;
				y += 49;
			}
		}

		System.out.println("trading post search container: " + id);
		RSInterface item = addToItemGroup(id, 2, 15, 120, 17, false, "", "", "");
		item.actions = new String[5];
		item.actions[0] = "Value";
		item.actions[1] = "Buy 1";
		item.actions[2] = "Buy 5";
		item.actions[3] = "Buy 10";
		item.actions[4] = "Buy X";
		setBounds(id, 9, 9, scroll_frame++, scroll);
		id++;

		x = 155;
		y = 70;

		for (int i = 0; i < 4; i++) {
			addHoverButton(id, 910, 167, 32, "Select Featured Shop", -1, id + 1, 5);
			addHoveredButton(id + 1, TRADING_POST_SPRITES, 909, 167, 32, id + 2);
			tab.child(frame++, id, x, y);
			tab.child(frame++, id + 1, x, y);
			id += 3;

			addText(id, "Featured Shop " + id, 0xFFFFFF, true, true, 0, 0);
			tab.child(frame++, id, x + 82, y + 9);
			id++;

			x += 167;

			if (x > 350) {
				y += 26;
				x = 155;
			}
		}

		x = 155;
		y = 272;

		int[] SECONDARY_SPRITES = {771, 768, 758};

		for (int i = 0; i < BOTTOM_SELECTION.length; i++) {
			addHoverButton(id, 912, 111, 35, "Select @gre@" + BOTTOM_SELECTION[i], -1, id + 1, 5);
			addHoveredButton(id + 1, TRADING_POST_SPRITES, 911, 111, 35, id + 2);
			tab.child(frame++, id, x, y);
			tab.child(frame++, id + 1, x, y);
			id += 3;

			addText(id, "@or2@" + BOTTOM_SELECTION[i], 0xFFFFFF, false, true, 1, 0);
			tab.child(frame++, id, x + 15, y + 10);
			id++;

			addSprite(id, SECONDARY_SPRITES[i]);
			tab.child(frame++, id, x + 85, y + 10);
			id++;

			x += 112;
		}

	}

	public static RSInterface addInterface(int id) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		rsi.id = id;
		rsi.parentID = id;
		rsi.width = 765;
		rsi.height = 503;
		return rsi;
	}
	public static RSInterface addToItemGroup(int id, int w, int h, int x, int y, boolean actions,
			String action1, String action2, String action3) {
		RSInterface rsi = addInterface(id);
		rsi.width = w;
		rsi.height = h;
		rsi.inv = new int[w * h];
		rsi.invStackSizes = new int[w * h];
		rsi.usableItemInterface = false;
		rsi.isInventoryInterface = false;
		rsi.invSpritePadX = x;
		rsi.invSpritePadY = y;
		rsi.spritesX = new int[20];
		rsi.spritesY = new int[20];
		rsi.sprites = new Sprite[20];
		rsi.actions = new String[5];
		if (actions) {
			rsi.actions[0] = action1;
			rsi.actions[1] = action2;
			rsi.actions[2] = action3;
		}
		rsi.type = 2;
		return rsi;
	}
	/**
	 * this has been done this way to allow for new tasks to be added at a later date
	 */
	private static Object[][] EASY_TASKS = {
			{53342, "Kill 250 cows"},
			{53343, "Complete the " + Constants.CLIENT_NAME + " tutorial"},
			{53344, "Pickpocket a man 50 times."},
			{53345, "Switch magic books"},
			{53347, "Join a clan chat"},
			{53348, "Vote once"},
			{53349, "Bury 100 bones"},
			{53350, "Smelt 100 bronze bars"},
			{53352, "Perform a special attack"},
			{53353, "Change character appearance"},
			{53354, "Light 100 fire"},
			{53355, "Summon a familiar"},
			{53356, "Eat 100 lobsters"},
			{53359, "Runecraft 50 rune essence"},
			{53360, "Win a duel arena fight"},
			{53361, "Make a bank tab"},
			{54320, "Cast each God spell"},
			{54321, "Complete Gnome course"},
			{54372, "Get to wave 10 at Zombies"},
			{54376, "Chop 150 trees"},
			{54377, "Fish 200 shrimp"},
			{54378, "Mine 100 copper ore"},
			{54379, "Craft 100 uncut sapphires"},
			{54380, "Complete 10 easy slayer tasks"},
			{54381, "Complete 2 easy clue scrolls"},
			{18787, "Catch 100 birds"}
	};
	private static Object[][] MEDIUM_TASKS = {
			{53364, "Die 20 times"},
			{53366, "Win 20 duel arena fights"},
			{53367, "Loot a stall 350 times"},
			{53368, "Fish 500 lobsters"},
			{53369, "Bury 250 big bones"},
			{53370, "Wield a dragon weapon"},
			{53371, "Vote 20 times"},
			{53372, "Kill 150 steel dragon"},
			{53374, "Use a wilderness obelisk"},
			{53376, "Smelt 300 steel bars"},
			{53377, "Burn 400 maple logs"},
			{54322, "Complete Kolodion's minigame"},
			{54323, "Enter a GWD chamber"},
			{54324, "Complete Gnome course 100 times"},
			{54325, "Earn every Defender"},
			{54326, "Perform a Skillcape emote"},
			{54373, "Get to wave 15 at Zombies"},
			{54411, "Complete barrows 50 times"},
			{54382, "Complete pest control 100 times"},
			{54383, "Fish 500 tuna"},
			{54384, "Cook 500 tuna"},
			{54385, "Cut 500 maples"},
			{54387, "Complete 25 medium slayer tasks"},
			{54388, "Identify 250 herbs"},
			{54389, "Smith 500 mithril bars"},
			{54390, "Complete 15 medium clues"},
			{18788, "Catch 200 imps"}
	};
	private static Object[][] HARD_TASKS = {
			{53380, "Defeat jad twice"},
			{53381, "Win 15 castlewars games"},
			{53382, "Kill 50 king black dragons"},
			{53384, "Cook 1000 sharks"},
			{53385, "Light 1000 magic logs"},
			{53387, "Hit a 500+ with melee"},
			{53389, "Win 75 pest control games"},
			{53391, "Earn 500 mage bank points"},
			{53392, "Win 50 clan battles"},
			{53393, "Complete a TT puzzle"},
			{53394, "Craft 1000 rune essence"},
			{53395, "Wield a whip"},
			{53396, "Smith DFS"},
			{53397, "Farm 250 seeds"},
			{53398, "Bury 500 dragon bones"},
			{54327, "Kill each GWD boss"},
			{54328, "Earn 1B cash"},
			{54329, "Create a Vine Whip"},
			{54330, "Fletch 500 arrows"},
			{54331, "Complete Wildy course 50 times"},
			{54354, "Bury 500 frost dragon bones"},
			{54374, "Get to wave 25 at Zombies"},
			{54412, "Complete 20 duo slayer tasks"},
			{54391, "Cut 500 diamonds"},
			{54392, "Chop 1000 yew logs"},
			{54393, "Burn 1000 yew logs"},
			{54394, "Smelt 1000 adamant bars"},
			{54395, "Craft 500 black dhide bodys"},
			{54396, "Smith 100 adamant platebody"},
			{54397, "Complete 25 hard slayer tasks"},
			//  {54398, "Wear full void"},
			{54399, "Pickpocket 250 palidins"},
			{54400, "Earn 1000 warrior tokens"},
			{18789, "Catch 250 grenwalls"}
	};
	private static Object[][] ELITE_TASKS = {
			{54332, "Earn max cash"},
			{54333, "Vote 50 times"},
			{54334, "Complete 75 hard clues"},
			{54336, "Kill Nex 50 times"},
			{54337, "Get to wave 50 in Zombies"},
			{54338, "Complete Fight Kiln 20 times"},
			{54339, "Kill Jad 50 times"},
			{54340, "Kill 1000 Frost dragons"},
			{54341, "Fire 1000 canon shots"},
			{54342, "Win 25 Castlewar games"},
			{54343, "Win 175 pest control games"},
			{54344, "Kill 400 Tormented demons"},
			{54345, "Kill 500 Glacors"},
			{54346, "Win over 50 duels"},
			{54347, "Kill 500 Jadinkos"},
			{54348, "Kill 500 Revenants"},
			{54349, "Fish 2000 Manta ray"},
			{54350, "Craft 1500 Rune essence"},
			{54351, "Complete 35 Duo Slayer tasks"},
			{54353, "Earn 200m XP in a skill"},
			{54375, "Get to wave 25 at Zombies solo"},
			{54413, "Complete 135 barrow runs"},
			{54402, "Mine 500 rune ore"},
			{54403, "Smelt 400 rune bars"},
			{54404, "Smith 300 rune platebodies"},
			//  {54405, "Complete barbarian agility 350 times"},
			{54406, "Bury 1000 frost bones"},
			{54407, "Mine 1500 rune essence"},
			{54408, "Chop 2000 magic logs"},
			{54409, "Pickpocket 300 hero's"},
			{54410, "Fletch 750 magic bows"},
			{18790, "Catch 250 Kingly imps"},
			{18791, "Kill 250 Corporal beasts"},
			{18792, "Burn 2000 magic logs"},
			{15264, "Kill 400 Zulrahs"},
			{15268, "Kill 225 Cerberuss"},
			{15269, "Kill 300 Krakens"},
			{18833, "Kill 300 Thermo smoke devils"},
			{18834, "Kill 250 Callistos"},
			{18835, "Kill 250 Vet'ions"},
			{18836, "Kill 300 Venenatises"},
			{254, "Earn 30 Wilderness Keys"}
	};
	private static int[] itfChildren = {54240, 12939, 12987, 13035, 12901, 12861, 13045,
			12963, 13011, 13053, 12919, 12881, 13061, 12951, 12999, 13069,
			12911, 12871, 13079, 12975, 13023, 13087, 12929, 12891, 13095,
			54241, 12940, 12988, 13036, 12902, 12862, 13046, 12964, 13012,
			13054, 12920, 12882, 13062, 12952, 13000, 13070, 12912, 12872,
			13080, 12976, 13024, 13088, 12930, 12892, 13096};
	private static int[][] BUTTON_POSITIONS = {
			{0, 0},
			{149, 0},
			{0, 59},
			{149, 59},
			{0, 117},
			{149, 117},
			{0, 175},
			{149, 175},
	};
	private static final int CLOSE_BUTTON = 141, CLOSE_BUTTON_HOVER = 142;
	public static Map<Integer, Integer> teleportPages = new HashMap<>();
	private static String[][] fixedSprites2 = {{"150", "241"},
			{"150a", "242"}, {"349", "243"}, {"349a", "244"},
			{"350", "245"}, {"350a", "246"}, {"7587", "247"},
			{"7600", "248"}, {"7601", "249"}, {"7602", "250"},
			{"7603", "251"}, {"7604", "252"}, {"7605", "253"},
			{"7606", "254"}, {"7607", "255"}, {"7608", "256"},
			{"7609", "257"}, {"7610", "258"}, {"19301", "259"},
			{"19301a", "260"},};
	private static String[][] fixedSprites3 = {
			{"/Interfaces/Skill/Agility", "272"},
			{"/Interfaces/Skill/Attack", "273"},
			{"/Interfaces/Skill/Button", "274"},
			{"/Interfaces/Skill/Construction", "275"},
			{"/Interfaces/Skill/Cook", "276"},
			{"/Interfaces/Skill/Craft", "277"},
			{"/Interfaces/Skill/Defence", "278"},
			{"/Interfaces/Skill/Dungeon", "279"},
			{"/Interfaces/Skill/Farm", "280"},
			{"/Interfaces/Skill/Fire", "281"},
			{"/Interfaces/Skill/Fish", "282"},
			{"/Interfaces/Skill/Fletch", "283"},
			{"/Interfaces/Skill/Herblore", "284"},
			{"/Interfaces/Skill/HP", "285"},
			{"/Interfaces/Skill/Hunter", "286"},
			{"/Interfaces/Skill/Mage", "287"},
			{"/Interfaces/Skill/Mine", "288"},
			{"/Interfaces/Skill/Prayer", "289"},
			{"/Interfaces/Skill/Range", "290"},
			{"/Interfaces/Skill/Rune", "291"},
			{"/Interfaces/Skill/Skill 256", "292"},
			{"/Interfaces/Skill/Slay", "293"},
			{"/Interfaces/Skill/Smith", "294"},
			{"/Interfaces/Skill/Strength", "295"},
			{"/Interfaces/Skill/Summon", "296"},
			{"/Interfaces/Skill/Thief", "297"},
			{"/Interfaces/Skill/Wood", "299"},};
	private static int[] ID1 = {16516, 16517, 16518, 16519, 16520, 16521, 16522, 16523,
			16524, 16525, 16526, 16527, 16528, 16529, 16530, 16531, 16532,
			16533, 16534, 16535, 16536, 16537, 16538, 16539, 16540, 16541};
	private static int[] X = {8, 44, 80, 114, 150, 8, 44, 80, 116, 152, 8, 42, 78, 116,
			152, 8, 44, 80, 116, 150, 6, 44, 80, 116, 150, 6};
	private static int[] Y = {6, 6, 6, 4, 4, 42, 42, 42, 42, 42, 79, 76, 76, 78, 78, 114,
			114, 114, 114, 112, 148, 150, 150, 150, 148, 184};

	private static int[] hoverIDs = {16600, 26601, 16662, 26603, 26674, 16765, 16756,
			26607, 26688, 26609, 26640, 26699, 26652, 26613, 26694, 26615,
			16616, 26677, 16648, 26619, 16667, 26691, 16692, 26663, 26624,
			16675};
	private static int[] hoverX = {12, 8, 20, 12, 24, 2, 2, 6, 6, 50, 6, 6, 10, 6, 6, 5,
			5, 5, 5, 5, 18, 28, 28, 50, 1, 1};
	private static int[] hoverY = {42, 42, 42, 42, 42, 80, 80, 80, 80, 80, 118, 118, 118,
			118, 118, 150, 150, 150, 150, 150, 105, 80, 65, 65, 65, 110};
	private static String[] hoverStrings = {
			"Level 01\nThick Skin\nIncreases your Defence by 5%",
			"Level 04\nBurst of Strength\nIncreases your Strength by 5%",
			"Level 07\nCharity of Thought\nIncreases your Attack by 5%",
			"Level 08\nSharp Eye\nIncreases your Ranged by 5%",
			"Level 09\nMystic Will\nIncreases your Magic by 5%",
			"Level 10\nRock Skin\nIncreases your Defence by 10%",
			"Level 13\nSuperhuman Strength\nIncreases your Strength by 10%",
			"Level 16\nImproved Reflexes\nIncreases your Attack by 10%",
			"Level 19\nRapid Restore\n2x restore rate for all stats\nexcept Hitpoints and Prayer",
			"Level 22\nRapid Heal\n2x restore rate for the\nHitpoints stat",
			"Level 25\nProtect Item\nKeep one extra item if you die",
			"Level 26\nHawk Eye\nIncreases your Ranged by 10%",
			"Level 27\nMystic Lore\nIncreases your Magic by 10%",
			"Level 28\nSteel Skin\nIncreases your Defence by 15%",
			"Level 31\nUltimate Strength\nIncreases your Strength by 15%",
			"Level 34\nIncredible Reflexes\nIncreases your Attack by 15%",
			"Level 37\nProtect from Magic\nProtection from magical attacks",
			"Level 40\nProtect from Missiles\nProtection from ranged attacks",
			"Level 43\nProtect from Melee\nProtection from close attacks",
			"Level 44\nEagle Eye\nIncreases your Ranged by 15%",
			"Level 45\nMystic Might\nIncreases your Magic by 15%",
			"Level 46\nRetribution\nInflicts damage to nearby\ntargets if you die",
			"Level 49\nRedemption\nHeals you when damaged\nand Hitpoints falls\nbelow 10%",
			"Level 52\nSmite\n1/4 of damage dealt is\nalso removed from\nopponents Prayer",
			"Level 60\nChivalry\nIncreases your Defence by 20%,\nStrength by 18% and Attack by\n15%",
			"Level 70\nPiety\nIncreases your Defence by 25%,\nStrength by 23% and Attack by\n20%"};
	private static int[] logoutID = {2450, 2451, 2452};
	private static int[] logoutID2 = {2458};
	private static String[] spriteNames = {"Attack", "HP", "Mine", "Strength", "Agility",
			"Smith", "Defence", "Herblore", "Fish", "Range", "Thief",
			"Cook", "Prayer", "Craft", "Fire", "Mage", "Fletch", "Wood",
			"Rune", "Slay", "Farm", "Construction", "Hunter", "Summon"};
	private static int[] buttons = {8654, 8655, 8656, 8657, 8658, 8659, 8660, 8861, 8662,
			8663, 8664, 8665, 8666, 8667, 8668, 8669, 8670, 8671, 8672,
			12162, 13928, 27123, 27124, 27125};
	private static int[] hovers = {4040, 4076, 4112, 4046, 4082, 4118, 4052, 4088, 4124,
			4058, 4094, 4130, 4064, 4100, 4136, 4070, 4106, 4142, 4160,
			2832, 13917, 19005, 19006, 19007};
	private static int[][] text = {{4004, 4005}, {4016, 4017}, {4028, 4029},
			{4006, 4007}, {4018, 4019}, {4030, 4031}, {4008, 4009},
			{4020, 4021}, {4032, 4033}, {4010, 4011}, {4022, 4023},
			{4034, 4035}, {4012, 4013}, {4024, 4025}, {4036, 4037},
			{4014, 4015}, {4026, 4027}, {4038, 4039}, {4152, 4153},
			{12166, 12167}, {13926, 13927}, {55376, 55379},
			{55377, 55380}, {55378, 55381}};
	private static int[] icons = {3965, 3966, 3967, 3968, 3969, 3970, 3971, 3972, 3973,
			3974, 3975, 3976, 3977, 3978, 3979, 3980, 3981, 3982, 4151,
			12165, 13925, 27127, 27128, 27129};
	private static int[][] buttonCoords = {{3, 5}, {65, 5}, {127, 5}, {3, 33},
			{65, 33}, {127, 33}, {3, 61}, {65, 61}, {127, 61},
			{3, 89}, {65, 89}, {127, 89}, {3, 117}, {65, 117},
			{127, 117}, {3, 145}, {65, 145}, {127, 145},
			{3, 173}, {65, 173}, {127, 173}, {3, 201}, {65, 201},
			{127, 201}};
	private static int[][] iconCoords = {{5, 7}, {68, 8}, {130, 7}, {8, 35},
			{67, 34}, {130, 37}, {8, 65}, {66, 64}, {130, 62},
			{6, 92}, {67, 97}, {132, 91}, {5, 119}, {69, 121},
			{129, 119}, {5, 148}, {68, 147}, {131, 147},
			{5, 174}, {68, 174}, {129, 175}, {5, 203}, {68, 202},
			{130, 203}};
	private static int[][] textCoords = {{31, 7, 44, 18}, {93, 7, 106, 18},
			{155, 7, 168, 18}, {31, 35, 44, 46}, {93, 35, 106, 46},
			{155, 35, 168, 46}, {31, 63, 44, 74}, {93, 63, 106, 74},
			{155, 63, 168, 74}, {31, 91, 44, 102},
			{93, 91, 106, 102}, {155, 91, 168, 102},
			{31, 119, 44, 130}, {93, 119, 106, 130},
			{155, 119, 168, 130}, {31, 149, 44, 158},
			{93, 147, 106, 158}, {155, 147, 168, 158},
			{31, 175, 44, 186}, {93, 175, 106, 186},
			{155, 175, 168, 186}, {31, 203, 44, 214},
			{93, 203, 106, 214}, {155, 203, 168, 214},
			{31, 231, 106, 214}, {93, 231, 168, 214}};

	private static int[][] newText = {{55376, 55377, 55378}, {55379, 55380, 55381}};
	private static int[] kills = {5, 10, 25, 50, 200, 500};
	private static String[] times = {"0:05", "0:10", "0:20", "0:30", "0:45", "1:00", "1:30", "2:00"};
	private static String[] mapNames = {"Clan Wars Classic", "Plateau", "Turrets", "Forsaken Quarry", "Blasted Forest"};
	private static String[][] mapDescriptions = {
			{
					"A varied landscape littered with lava", "pits, crumbled walls and dead trees."
			},
			{
					"A barran plateau rising from a sea of", "lava. Very few obstacles."
			},
			{
					"Small turrets are scattered around", "this battlefield. Good for sniping."
			},
			{
					"An abandoned mine featuring low obstacles", "such as rocks and puddles."
			},
			{
					"A dead forest featuring high obstacles", "such as trees."
			}
	};
	private static String[][] skilling = {
			{"Attacker", "Obtain 200m xp in Attack"},
			{"The Strength", "Obtain 200m xp in Strength"},
			{"Defender", "Obtain 200m xp in Defence"},
			{"Ranger", "Obtain 200m xp in Range"},
			{"Faithful", "Obtain 200m xp in Prayer"},
			{"Magician", "Obtain 200m xp in Magic"},
			{"Runecrafter", "Obtain 200m xp in Runecrafting"},
			{"Healer", "Obtain 200m xp in HP"},
			{"Agile", "Obtain 200m xp in Agility"},
			{"Forger", "Obtain 200m xp in Herblore"},
			{"Thief", "Obtain 200m xp in Thieving"},
			{"Crafter", "Obtain 200m xp in Crafting"},
			{"Fletcher", "Obtain 200m in Fletching"},
			{"Miner", "Obtain 200m in Mining"},
			{"Blacksmith", "Obtain 200m in Smithing"},
			{"Fisher", "Obtain 200m in Fishing"},
			{"Chef", "Obtain 200m in Cooking"},
			{"Arson", "Obtain 200m in Firemaking"},
			{"Lumberjack", "Obtain 200m in Woodcutting"},
			{"Farmer", "Obtain 200m in Farming"},
			{"Summoner", "Obtain 200m in Summoning"},
			{"Raider", "Obtain 200m in Dungoneering"},
			{"Hunter", "Obtain 200m in Hunter"},
			{"Slayer", "Obtain 200m in Hunter"},
			{"Maxed", "Obtain 99 in all skills"},
			{"Overlord", "Obtain 200m XP in all skills"}
	};
	private static String[][] pkin = {
			{"Fighter", "25 kills"},
			{"Militia", "50 kills"},
			{"Soldier", "100 kills"},
			{"Brute", "250 kills"},
			{"Mercenary", "500 kills"},
			{"Assassin", "1000 kills"},
			{"Warrior", "1500 kills"},
			{"Gladiator", "2500 kills"},
			{"Warlord", "5000 kills"}
	};
	private static String[][] misc = {
			{"The Adventurer", "Complete all quests"}, // done
			{"Gamer", "You can unlock this title by playing \na round of all minigames"},
			{"The Wealthy", "You can unlock this by throwing 5 billion \ncoins down the Edgeville wishing well"},
			{"Destroyer", "Defeat 2500 monsters"},
			{"Sir", "Have a total level of 1000 or more"}, // done
			{"Lord", "Have a total level of 2000 or more"}, // done
			{"Weakling", "Die 10 times to a monster"},
			{"Loyal", "Play a total of 7 days"},
			{"Voter", "Vote a total of 30 times"}, // done
			{"Donator", "Donate to the server"},
			{"Merchant", "Make 1000 successful trades"}, // done
			{"Athlete", "Complete every agility course"},
			{"Wingman", "Complete 100 duo slayer tasks"}, // done
			{"The Drunk", "Drink 10 beers"},
			{"Collector", "Catch 1000 imps"},
			{"Bosser", "Kill 5000 bosses"},
			{"Skiller", "Obtain 1b total xp in skilling skills"},
			{"Elite", "Have an elite mode account"},
			{"Iron", "Have an ironman account"},
			{"Respected Donor", "Donate more than $750."},
			{"Extreme Donor", "Donate more than $115."},
			{"Locksmith", "Unlock 250 wilderness chests."}
	};
	
	private static String[] TRAINING_NAMES = {"Beginners Area", "Bandits", "Tzhaar", "Ice Mountain", "White wolf", "Ogres",
			"Ape Atoll", "Druids", "Experiments", "Karamja\nDungeon", "Edgeville\nDungeon", "Taverly\nDungeon",
			"Brimhaven\nDungeon", "Ancient Cavern", "Varrock Sewers"};

	private static String[] SLAYER_NAMES = {
			"Relleka\nDungeon", "Slayer Tower", "Chaos Tunnels", "Pollnivneach\nDungeon", "Jadinko lair", "Asgarnia Ice\nDungeon", "Kuradel Dungeon"
	};
	
	private static String[] quests = {"The Stolen Cannon", "A Beginners Adventure", "Gnome Glider Down"};
	private static String[] titles = {
			"Clan name:", "Who can enter chat?", "Who can talk on chat?",
			"Who can kick on chat?", "Who can ban on chat?"
	};
	private static String[] defaults = {"Chat Disabled", "Anyone", "Anyone", "Anyone", "Anyone"};
	private static String[] whoCan = {"Anyone", "Recruit", "Corporal", "Sergeant", "Lieutenant", "Captain", "General", "Only Me"};

	private static String[] SKILLING_NAMES = {"Mining/Smithing", "Fishing", "Agility", "Farming", "Rune Essence", "Summoning", "Woodcutting", "Hunter"};

	private static String[] PKING_NAMES = {"Edgeville", "Mage Bank", "Green Dragons \n Revs"};

	private static String[] MINIGAME_NAMES = {"Pest Control", "Zombies", "Jad/Fight Kiln", "Duel Arena", "Fight Caves",
			"Castlewars", "Clan Wars", "Warriors Guild", "Mage Bank Game", "Barrows", "Gambling"};
	private static String[] BOSSING_NAMES = {"Kalphite Queen", "KBD", "Godwars", "Tormented\nDemon", "Jungle Demon",
			"Bork", "Barrel Chest", "Sea Troll", "Corporal Beast", "Dagannoth Kings", "Avatar of Dest.",
			"Nex", "Nomad", "Glacors", "Zulrah", "Cerberus", "Kraken"};
	private static String[] DONATOR_NAMES = {"Donator Zone", "Respected Zone", "Extreme Zone"};
	
	private static int ID2[] = {5609, 5610, 5611, 5612, 5613, 5614, 5615, 5616, 5617,
			5618, 5619, 5620, 5621, 5622, 5623, 683, 684, 685, 5632, 5633,
			5634, 5635, 5636, 5637, 5638, 5639, 5640, 5641, 5642, 5643,
			5644, 686, 5645, 5649, 5647, 5648, 16500, 16501, 16502, 16503,
			16504, 16505, 16506, 16507, 16508, 16509, 16510, 16511, 16512,
			16513, 16514, 16515, 5651, 687};
	private static int X2[] = {6, 42, 78, 6, 42, 78, 113, 149, 6, 114, 150, 6, 42, 78,
			114, 42, 78, 114, 8, 44, 80, 8, 44, 80, 116, 152, 8, 116, 152,
			8, 44, 80, 116, 44, 80, 116, 114, 117, 150, 153, 42, 45, 78,
			81, 150, 153, 6, 9, 150, 157, 6, 8, 65, 101};
	private static int Y2[] = {4, 4, 4, 40, 40, 40, 38, 38, 76, 76, 76, 112, 112, 112,
			112, 148, 148, 148, 6, 6, 6, 42, 42, 42, 42, 42, 79, 78, 78,
			114, 114, 114, 114, 150, 150, 150, 4, 8, 4, 7, 76, 80, 76, 79,
			112, 116, 148, 151, 148, 151, 184, 194, 240, 241};

	private static String[] oldPrayerNames = {"Thick Skin", "Burst of Strength",
			"Charity of Thought", "Rock Skin", "Superhuman Strength",
			"Improved Reflexes", "Rapid Restore", "Rapid Heal",
			"Protect Item", "Steel Skin", "Ultimate Strength",
			"Incredible Reflexes", "Protect from Magic",
			"Protect from Missiles", "Protect from Melee", "Retribution",
			"Redemption", "Smite"};
	public static int skillingInterfaceId;
	public static int pkInterfaceId;
	public static int miscInterfaceId;
	public static StreamLoader aClass44;
	public static RSInterface interfaceCache[];
	private static Map<String, Integer> spriteIndex = new HashMap<>();
	private static int summoningLevelRequirements[] = {1, 4, 10, 13, 16, 17,
			18, 19, 22, 23, 25, 28, 29, 31, 32, 33, 34, 34, 34, 34, 36, 40, 41,
			42, 43, 43, 43, 43, 43, 43, 43, 46, 46, 47, 49, 52, 54, 55, 56, 56,
			57, 57, 57, 58, 61, 62, 63, 64, 66, 66, 67, 68, 69, 70, 71, 72, 73,
			74, 75, 76, 76, 77, 78, 79, 79, 79, 80, 83, 83, 85, 86, 88, 89, 92,
			93, 95, 96, 99};
	private static int pouchItems[] = {
			12047, 12043, 12059, 12019, 12009, 12778, 12049, 12055, 12808, 12067,
			12063, 12091, 12800, 12053, 12065, 12021, 12818, 12780, 12798, 12814,
			12073, 12087, 12071, 12051, 12095, 12097, 12099, 12101, 12103, 12105,
			12107, 12075, 12816, 12041, 12061, 12007, 12035, 12027, 12077, 12531,
			12810, 12812, 12784, 12023, 12085, 12037, 12015, 12045, 12079, 12123,
			12031, 12029, 12033, 12820, 12057, 14623, 12792, 12069, 12011, 12081,
			12782, 12794, 12013, 12802, 12804, 12806, 12025, 12017, 12788, 12776,
			12083, 12039, 12786, 12089, 12796, 12822, 12093, 12790
	};
	private static int scrollItems[] = {
			12425, 12445, 12428, 12459, 12533, 12838, 12460, 12432, 12839, 12430,
			12446, 12440, 12834, 12447, 12433, 12429, 12443, 12443, 12443, 12443,
			12461, 12431, 12422, 12448, 12458, 12458, 12458, 12458, 12458, 12458,
			12458, 12462, 12829, 12426, 12444, 12441, 12454, 12453, 12463, 12424,
			12835, 12836, 12840, 12455, 12468, 12427, 12436, 12467, 12464, 12452,
			12439, 12438, 12423, 12830, 12451, 14622, 12826, 12449, 12450, 12465,
			12841, 12831, 12457, 12824, 12824, 12824, 12442, 12456, 12837, 12832,
			12466, 12434, 12833, 12437, 12827, 12828, 12435, 12825
	};
	private static MRUNodes aMRUNodes_238;
	public int itemSpriteId1, itemSpriteIndex;
	public int summonReq;
	public boolean drawItemCount = true;
	public int transparency;
	public Sprite sprite1;
	public int anInt208;
	public Sprite sprites[];
	public int requiredValues[];
	public int contentType;// anInt214
	public int spritesX[];
	public int textHoverColor;
	public int atActionType;
	public String targetName;
	public int anInt219;
	public int width;
	public String tooltip;
	public String targetVerb;
	public boolean centerText;
	public int scrollPosition;
	public String actions[];
	public int valueIndexArray[][];
	public boolean filled;
	public String enabledText;
	public int mOverInterToTrigger;

	// {"/Interfaces/Summoning/BOB/store 4", "563"},
	//{"/Interfaces/Summoning/BOB/store 3", "562"},
	//{"/Interfaces/Summoning/BOB/store 2", "561"},
	//{"/Interfaces/Summoning/BOB/store 1", "560"},
	public int invSpritePadX;
	public int textColor;
	public int mediaType;
	public int mediaID;
	public boolean itemsDraggable;
	public int parentID;
	public int targetBitMask;
	public int anInt239;
	public int children[];
	public int childX[];
	public boolean usableItemInterface;
	public int textDrawingAreas;
	public int invSpritePadY;

	/*public static void achievementTab() {
		RSInterface Interface = addTabInterface(20638);
        setChildren(3, Interface);

        addText(29145, "Achievements", 0xFF981F, false, true, 52, TDA, 2);
        addSprite(29157, 4, "Interfaces/Quest/QUEST");
        addSprite(29158, 0, "Interfaces/Quest/QUEST");
        addText(29159, "Achievement Points:", 0xFF981F, false, true, 52, TDA, 0);

        setBounds(29145, 4, 2, 0, Interface);
        setBounds(29157, 0, 24, 1, Interface);
        setBounds(29158, 0, 22, 2, Interface);
        setBounds(29160, 0, 24, 1, Interface);
        setBounds(29154, 167, 5, 2, Interface);



        Interface = addTabInterface(29160);
        setChildren(8, Interface);
        addText(29161, "Easy", TDA, 2, 0xFF9900, false, true);
        addText(29162, "Medium", TDA, 2, 0xFF9900, false, true);
        addText(29163, "Hard", TDA, 2, 0xFF9900, false, true);

        addText(29164, "", 0xFF0000, false, true, 52, TDA, 0, 0xFFFFFF);
        addText(29165, "", 0xFF0000, false, true, 52, TDA, 0, 0xFFFFFF);
        addText(29166, "", 0xFF0000, false, true, 52, TDA, 0, 0xFFFFFF);
        addText(29167, "", 0xFF0000, false, true, 52, TDA, 0, 0xFFFFFF);
        addText(29168, "", 0xFF0000, false, true, 52, TDA, 0, 0xFFFFFF);

        setBounds(29161, 4, 4, 0, Interface);
        setBounds(29162, 4, 74, 1, Interface);
        setBounds(29163, 4, 144, 2, Interface);
        setBounds(29164, 14, 40, 3, Interface);
        setBounds(29165, 14, 52, 4, Interface);
        setBounds(29166, 14, 64, 5, Interface);
        setBounds(29167, 14, 76, 6, Interface);
        setBounds(29168, 14, 88, 7, Interface);
    }*/
	public int valueCompareType[];
	public int anInt246;
	public int spritesY[];
	public String message;
	public boolean isInventoryInterface;
	public int id;
	public int invStackSizes[];
	public int inv[];
	public byte opacity;
	public int disabledAnimation;
	public int enabledAnimation;
	public boolean aBoolean259;
	public Sprite sprite2;
	public int scrollMax;
	public int type;
	public int offsetX;
	public int offsetY;
	public boolean isMouseoverTriggered;
	public int height;
	public boolean textShadow;
	public int modelZoom;
	public int rotationX;
	public int rotationY;
	public int childY[];
	public boolean newScroller = false;
	private int enabledModelType;
	private int enabledModelId;

	public RSInterface() {
		mOverInterToTrigger = -1;
	}

	public static void unpack(StreamLoader streamLoader, StreamLoader streamLoader_1) {
		String[] sprites = new String(streamLoader.getDataForName("sprite-index.dat")).split("\r\n");
		for (String s : sprites) {
			String[] sprite = s.split("\t");

			if (sprite[0].toLowerCase().contains("Interfaces/Skill/Total".toLowerCase())) {
				System.out.println("sprite: " + sprite[1]);
			}

			spriteIndex.put(sprite[0].toLowerCase(), Integer.parseInt(sprite[1]));
			sprite = null;
		}

		aMRUNodes_238 = new MRUNodes(200);
		Stream stream = new Stream(streamLoader.getDataForName("data"));
		int i = -1;
		int j = stream.readUnsignedWord();
		interfaceCache = new RSInterface[j + 80000];

		while (stream.currentOffset < stream.buffer.length) {
			int k = stream.readUnsignedWord();
			if (k == 65535) {
				i = stream.readUnsignedWord();
				k = stream.readUnsignedWord();
			}
			RSInterface rsInterface = interfaceCache[k] = new RSInterface();
			rsInterface.id = k;
			rsInterface.parentID = i;
			rsInterface.type = stream.readUnsignedByte();
			rsInterface.atActionType = stream.readUnsignedByte();
			rsInterface.contentType = stream.readUnsignedWord();
			rsInterface.width = stream.readUnsignedWord();
			rsInterface.height = stream.readUnsignedWord();
			rsInterface.opacity = (byte) stream.readUnsignedByte();
			rsInterface.mOverInterToTrigger = stream.readUnsignedByte();
			if (rsInterface.mOverInterToTrigger != 0)
				rsInterface.mOverInterToTrigger = (rsInterface.mOverInterToTrigger - 1 << 8)
						+ stream.readUnsignedByte();
			else
				rsInterface.mOverInterToTrigger = -1;
			int i1 = stream.readUnsignedByte();
			if (i1 > 0) {
				rsInterface.valueCompareType = new int[i1];
				rsInterface.requiredValues = new int[i1];
				for (int j1 = 0; j1 < i1; j1++) {
					rsInterface.valueCompareType[j1] = stream
							.readUnsignedByte();
					rsInterface.requiredValues[j1] = stream.readUnsignedWord();
				}

			}
			int k1 = stream.readUnsignedByte();
			if (k1 > 0) {
				rsInterface.valueIndexArray = new int[k1][];
				for (int l1 = 0; l1 < k1; l1++) {
					int i3 = stream.readUnsignedWord();
					rsInterface.valueIndexArray[l1] = new int[i3];
					for (int l4 = 0; l4 < i3; l4++)
						rsInterface.valueIndexArray[l1][l4] = stream
								.readUnsignedWord();

				}

			}
			if (rsInterface.type == 0) {
				rsInterface.scrollMax = stream.readUnsignedWord();
				rsInterface.isMouseoverTriggered = stream.readUnsignedByte() == 1;
				int i2 = stream.readUnsignedWord();
				rsInterface.children = new int[i2];
				rsInterface.childX = new int[i2];
				rsInterface.childY = new int[i2];
				for (int j3 = 0; j3 < i2; j3++) {
					rsInterface.children[j3] = stream.readUnsignedWord();
					rsInterface.childX[j3] = stream.readSignedWord();
					rsInterface.childY[j3] = stream.readSignedWord();
				}
			}
			if (rsInterface.type == 1) {
				stream.readUnsignedWord();
				stream.readUnsignedByte();
			}
			if (rsInterface.type == 2) {
				rsInterface.inv = new int[rsInterface.width * rsInterface.height];
				rsInterface.invStackSizes = new int[rsInterface.width * rsInterface.height];
				rsInterface.aBoolean259 = stream.readUnsignedByte() == 1;
				rsInterface.isInventoryInterface = stream.readUnsignedByte() == 1;
				rsInterface.usableItemInterface = stream.readUnsignedByte() == 1;
				rsInterface.itemsDraggable = stream.readUnsignedByte() == 1;
				rsInterface.invSpritePadX = stream.readUnsignedByte();
				rsInterface.invSpritePadY = stream.readUnsignedByte();
				rsInterface.spritesX = new int[20];
				rsInterface.spritesY = new int[20];
				rsInterface.sprites = new Sprite[20];
				for (int j2 = 0; j2 < 20; j2++) {
					int k3 = stream.readUnsignedByte();
					if (k3 == 1) {
						rsInterface.spritesX[j2] = stream.readSignedWord();
						rsInterface.spritesY[j2] = stream.readSignedWord();
						String s1 = stream.readString();
						if (streamLoader_1 != null && s1.length() > 0) {
							int i5 = s1.lastIndexOf(",");
							rsInterface.sprites[j2] = method207(
									Integer.parseInt(s1.substring(i5 + 1)),
									streamLoader_1, s1.substring(0, i5));
						}
					}
				}
				rsInterface.actions = new String[5];
				for (int l3 = 0; l3 < 5; l3++) {
					rsInterface.actions[l3] = stream.readString().intern();

					if (rsInterface.actions[l3].length() == 0)
						rsInterface.actions[l3] = null;
					if (rsInterface.parentID == 3824) {
						rsInterface.actions[0] = "Info";
						rsInterface.actions[4] = "Buy X";
					}
					if (rsInterface.parentID == 1644)
						rsInterface.actions[2] = "Operate";
				}

				if (rsInterface.parentID == 3822) {
					rsInterface.actions = new String[]{"Value", "Sell 1", "Sell 5", "Sell 10", "Sell X", "Sell All"};
				}
			}
			if (rsInterface.type == 3)
				rsInterface.filled = stream.readUnsignedByte() == 1;

			if (rsInterface.type == 4 || rsInterface.type == 1) {
				rsInterface.centerText = stream.readUnsignedByte() == 1;
				rsInterface.textDrawingAreas = stream.readUnsignedByte();
				rsInterface.textShadow = stream.readUnsignedByte() == 1;
			}
			if (rsInterface.type == 4) {
				rsInterface.message = stream.readString().replaceAll("RuneScape", Constants.CLIENT_NAME).intern();
				rsInterface.enabledText = stream.readString().intern();
			}
			if (rsInterface.type == 1 || rsInterface.type == 3
					|| rsInterface.type == 4)
				rsInterface.textColor = stream.readDWord();
			if (rsInterface.type == 3 || rsInterface.type == 4) {
				rsInterface.anInt219 = stream.readDWord();
				rsInterface.textHoverColor = stream.readDWord();
				rsInterface.anInt239 = stream.readDWord();
			}
			if (rsInterface.type == 5) {
				String s = stream.readString();
				if (streamLoader_1 != null && s.length() > 0) {
					int i4 = s.lastIndexOf(",");
					rsInterface.sprite1 = method207(
							Integer.parseInt(s.substring(i4 + 1)),
							streamLoader_1, s.substring(0, i4));
				}
				s = stream.readString();
				if (streamLoader_1 != null && s.length() > 0) {
					int j4 = s.lastIndexOf(",");
					rsInterface.sprite2 = method207(
							Integer.parseInt(s.substring(j4 + 1)),
							streamLoader_1, s.substring(0, j4));
				}
			}
			if (rsInterface.type == 6) {
				int l = stream.readUnsignedByte();
				if (l != 0) {
					rsInterface.mediaType = 1;
					rsInterface.mediaID = (l - 1 << 8) + stream.readUnsignedByte();
				}
				l = stream.readUnsignedByte();
				if (l != 0) {
					rsInterface.enabledModelType = 1;
					rsInterface.enabledModelId = (l - 1 << 8)
							+ stream.readUnsignedByte();
				}
				l = stream.readUnsignedByte();
				if (l != 0)
					rsInterface.disabledAnimation = (l - 1 << 8) + stream.readUnsignedByte();
				else
					rsInterface.disabledAnimation = -1;
				l = stream.readUnsignedByte();
				if (l != 0)
					rsInterface.enabledAnimation = (l - 1 << 8)
							+ stream.readUnsignedByte();
				else
					rsInterface.enabledAnimation = -1;
				rsInterface.modelZoom = stream.readUnsignedWord();
				rsInterface.rotationX = stream.readUnsignedWord();
				rsInterface.rotationY = stream.readUnsignedWord();
			}
			if (rsInterface.type == 7) {
				rsInterface.inv = new int[rsInterface.width
						* rsInterface.height];
				rsInterface.invStackSizes = new int[rsInterface.width
						* rsInterface.height];
				rsInterface.centerText = stream.readUnsignedByte() == 1;
				rsInterface.textDrawingAreas = stream.readUnsignedByte();
				rsInterface.textShadow = stream.readUnsignedByte() == 1;
				rsInterface.textColor = stream.readDWord();
				rsInterface.invSpritePadX = stream.readSignedWord();
				rsInterface.invSpritePadY = stream.readSignedWord();
				rsInterface.isInventoryInterface = stream.readUnsignedByte() == 1;
				rsInterface.actions = new String[5];
				for (int k4 = 0; k4 < 5; k4++) {
					rsInterface.actions[k4] = stream.readString().intern();
					if (rsInterface.actions[k4].length() == 0)
						rsInterface.actions[k4] = null;
				}

			}
			if (rsInterface.atActionType == 2 || rsInterface.type == 2) {
				rsInterface.targetVerb = stream.readString().intern();
				rsInterface.targetName = stream.readString().intern();
				rsInterface.targetBitMask = stream.readUnsignedWord();
			}

			if (rsInterface.type == 8) {
				rsInterface.message = stream.readString().replaceAll("RuneScape", Constants.CLIENT_NAME).intern();
			}

			if (rsInterface.atActionType == 1 || rsInterface.atActionType == 4
					|| rsInterface.atActionType == 5
					|| rsInterface.atActionType == 6) {
				rsInterface.tooltip = stream.readString().intern();

				if (rsInterface.tooltip.length() == 0) {
					if (rsInterface.atActionType == 1)
						rsInterface.tooltip = "Ok";
					if (rsInterface.atActionType == 4)
						rsInterface.tooltip = "Select";
					if (rsInterface.atActionType == 5)
						rsInterface.tooltip = "Select";
					if (rsInterface.atActionType == 6)
						rsInterface.tooltip = "Continue";
				}
			}
		}
		aClass44 = streamLoader;

		configureLunar();
		constructLunar();
		Trade();
		friendsTab();
		ignoreTab();
		attackStyle();
		skillTab602();
		questTab();
		achievementTab();
		questInterface();
		equipmentTab();

		clanChatTab();
		clanChatSetup();

		shop();
		abuse();
		audioOptions();
		vidOptions();
		//priceChecker(textDrawingAreas);
		optionTab();
		toggleOptions();
		prayerTab();
		curses();
		equipmentScreen();
		itemsOnDeath();
		itemsOnDeathDATA();

		MagicTab.magicTab();
		ancientMagicTab();

		emoteTab();
		summoningTab();
		bobInterface();
		pouches();
		scrolls();
		summonOrb();
		shopTab();
		pestpanel();
		pestpanel2();
		constructionHome();
		godWars();
		quickCurses();
		quickPrayers();
		skillInterface();
		duelInterface();
		bank();
		infoTab();
		zombiesLobbyDisplay();
		zombiesDisplay();
		fightpitsDisplay();

		clanBattleRequest();
		prayerMenu();

		warriorsGuild();
		skillLampInterface();

		skillLampInterface();
		clanswarsGame();
		buildClanwarsInterface();

		slayerAssignmentTab();

		teleportInterface();
		loyaltyInterface();

		zombiesInterface();
		zombieLobbyInterface();

		ironmanInterface();

		BuyandSell();
		collectSell();
		collectBuy();
		Buy();
		Sell();
		voteRewards();
		puzzle();

		gambleSelect();
		gambleTrade();
		compCustColor();
		compCust();
		mysteryBox();

		addText(251, "0", 0, 0xFFFFFF, true, true, 0);
		addText(252, "0", 0, 0xFFFFFF, true, true, 0);
		addText(253, "0", 0, 0xFFFFFF, true, true, 0);

		// 53096

		for (int a = interfaceCache.length - 1; a >= 0; a--) {
			RSInterface component = interfaceCache[a];
			if (component != null) {
				if (component.tooltip != null && component.tooltip.contains("@")) {
					component.tooltip = TextClass.convertMarkup(component.tooltip);
				}
				if (component.targetVerb != null && component.targetVerb.contains("@")) {
					component.targetVerb = TextClass.convertMarkup(component.targetVerb);
				}
				if (component.targetName != null && component.targetName.contains("@")) {
					component.targetName = TextClass.convertMarkup(component.targetName);
				}
				if (component.message != null && component.message.contains("@")) {
					component.message = TextClass.convertMarkup(component.message);
				}
				if (component.enabledText != null && component.enabledText.contains("@")) {
					component.enabledText = TextClass.convertMarkup(component.enabledText);
				}
			}
		}

		/*int lastNull = -1;
		for (int a = interfaceCache.length - 1; a > 0; a--) {
            if(lastNull == -1 && interfaceCache[a] == null) {
                lastNull = a;
            }
            if(lastNull > 0 && interfaceCache[a] != null) {
                System.out.println("START " + (a + 1) + " LAST: " + lastNull + " SIZE: " + (lastNull - a));
                lastNull = -1;
			}
		}

		for (int a = interfaceCache.length - 1; a > 0; a--) {
			if (interfaceCache[a] != null) {
                System.out.println("last interface id: " + a);
                if (interfaceCache[a].message != null) {
                    System.out.println(a + " " + interfaceCache[a].message);
                }
                break;
            }
		}*/
		
		aClass44 = null;
		aMRUNodes_238.unlinkAll();
		aMRUNodes_238 = null;
		spriteIndex.clear();
		spriteIndex = null;
		streamLoader = null;
		EASY_TASKS = null;
		MEDIUM_TASKS = null;
		HARD_TASKS = null;
		ELITE_TASKS = null;
		itfChildren = null;
		BUTTON_POSITIONS = null;
		fixedSprites2 = null;
		fixedSprites3 = null;
		summoningLevelRequirements = null;
		pouchItems = null;
		scrollItems = null;
		ID1 = null;
		X = null;
		Y = null;
		hoverIDs = null;
		hoverX = null;
		hoverY = null;
		hoverStrings = null;
		ID2 = null;
		X2 = null;
		Y2 = null;
		oldPrayerNames = null;
		sprites = null;
		stream = null;
		skilling = null;
		pkin = null;
		misc = null;
		TRAINING_NAMES = null;
		SLAYER_NAMES = null;
		SKILLING_NAMES = null;
		PKING_NAMES = null;
		MINIGAME_NAMES = null;
		BOSSING_NAMES = null;
		DONATOR_NAMES = null;
		logoutID = null;
		logoutID2 = null;
		spriteNames = null;
		buttons = null;
		hovers = null;
		text = null;
		icons = null;
		buttonCoords = null;
		iconCoords = null;
		textCoords = null;
		newText = null;
		kills = null;
		times = null;
		mapNames = null;
		mapDescriptions = null;
		quests = null;
		titles = null;
		defaults = null;
		whoCan = null;
		MagicTab.spellIds = null;
		MagicTab.spellHovers = null;
		MagicTab.spellPlacement = null;
	}

	public static void mysteryBox() {

		String sprites = "/Interfaces/MysteryBox/sprite";
		int frame = 0;
		int id = 0;

		RSInterface tab = addTabInterface(id++);
		setChildren(1, tab);

		addHoverButton(18132, "/Clan Chat/sprite", 6, 72, 32, "Clan Chat Setup", -1, 18133, 5);
		addHoveredButton(18133, "/Clan Chat/sprite", 7, 72, 32, 18134);

		addText(18135, "Join Chat", 0, 0xff9b00, true, true, 0);

		addSprite(id, 0, sprites);
		setBounds(id, 30, 74, frame++, tab);
	}

	public static void compCustColor() {
		RSInterface tab = addTabInterface(25400);
		addSprite(25401, 1, "CompCust/BACKGROUND");
		addText(25402, "Select Color...", 2, 0xff9b00, true, true, 0);

		addHoverButton(25403, "CompCust/Close", 0, 16, 16, "Close", -1, 25404, 3);
		addHoveredButton(25404, "CompCust/Close", 1, 16, 16, 25405);

		addHoverButton(25406, "CompCust/BUTTON", 0, 89, 24, "Accept", -1, 25407, 1);
		addHoveredButton(25407, "CompCust/BUTTON", 1, 89, 24, 25408);

		addText(25409, "Accept", 1, 0xff9b00, true, true, 0);
		addContainer(25410, 512, 700, 1337);

		tab.totalChildren(8);
		tab.child(0, 25401, 130, 20); // Background
		tab.child(1, 25402, 258, 24); // Title text
		tab.child(2, 25403, 356, 23); // close
		tab.child(3, 25404, 356, 23); // close
		tab.child(4, 25406, 210, 264); // accept
		tab.child(5, 25407, 210, 264); // accept
		tab.child(6, 25409, 255, 268); // accept text
		tab.child(7, 25410, 130, 20); // the color overlay
	}

	public static void compCust() {
		RSInterface tab = addTabInterface(25300);
		addSprite(25301, 0, "CompCust/BACKGROUND");
		addText(25302, "Cape Customizer", 2, 0xff9b00, true, true, 0);
		addText(25303, "First Color", 1, 0xff9b00, true, true, 0);
		addText(25304, "Second Color", 1, 0xff9b00, true, true, 0);
		addText(25305, "Third Color", 1, 0xff9b00, true, true, 0);
		addText(25306, "Fourth Color", 1, 0xff9b00, true, true, 0);
		addText(25307, "Preview Colors", 1, 0xff9b00, true, true, 0);

		addHoverButton(25309, "CompCust/Close", 0, 16, 16, "Close", -1, 25310, 3);
		addHoveredButton(25310, "CompCust/Close", 1, 16, 16, 25311);
		addContainer(25312, 65, 63, 1338);
		addContainer(25313, 65, 63, 1339);
		addContainer(25314, 65, 63, 1340);
		addContainer(25315, 65, 63, 1341);
		addRecoloredModel(25316, 450);

		tab.totalChildren(14);
		tab.child(0, 25301, 5, 8); // Background
		tab.child(1, 25302, 258, 12); // Title text
		tab.child(2, 25303, 95, 68); // color1 text
		tab.child(3, 25304, 245, 68); // color2 text
		tab.child(4, 25305, 95, 198); // color3 text
		tab.child(5, 25306, 245, 198); // color4 text
		tab.child(6, 25307, 417, 48); // preview color text
		tab.child(7, 25309, 480, 12); // close
		tab.child(8, 25310, 480, 12); // close
		tab.child(9, 25312, 63, 84); // Preview 1
		tab.child(10, 25313, 213, 84); // Preview 2
		tab.child(11, 25314, 63, 214); // Preview 3
		tab.child(12, 25315, 213, 214); // Preview 4
		tab.child(13, 25316, 342, 200); // Character
	}

	public static void gambleGame() {
		RSInterface tab = addInterface(19320, 512, 334);
		addText(19321, "Your Offer:", 1, 0xff9933, false, true, 0);
		addText(19322, "{player_name} Offer:", 1, 0xff9933, false, true, 0);

		tab.totalChildren(2);
		tab.child(0, 19321, 10, 22);//your text
		tab.child(1, 19322, 360, 22);//their text

	}

	public static void gambleSelect() {
		RSInterface tab = addInterface(19323, 512, 334);

		addTransparentSprite(19324, 0, "gamble/sprite");
		addText(19325, "Select the type of gambling...", 2, 0xff9933, true, true, 0);
		addText(19326, "Flower Poker", 1, 0xFFFFFF, false, true, 0);
		addText(19327, "Dice Duel", 1, 0xFFFFFF, false, true, 0);
		addText(19333, "Send gamble request to: {player_name}", 1, 0xff9933, true, true, 0);

		addConfigButton(19328, 19323, 0, 1, "VoteReward/CHECK", 21, 20, "Select", 1, 5, 1155);
		addConfigButton(19329, 19323, 0, 1, "VoteReward/CHECK", 21, 20, "Select", 2, 5, 1155);

		addHoverButton(19330, "gamble/sprite", 4, 72, 32, "Confirm", -1, 19331, 1);
		addHoveredButton(19331, "gamble/sprite", 5, 72, 32, 19332);

		addHoverButton(19359, "VoteReward/Close", 0, 16, 16, "Close", -1, 19360, 3);
		addHoveredButton(19360, "VoteReward/Close", 1, 16, 16, 19361);

		addText(19357, "Confirm", 0, 0xff9933, true, true, 0);

		tab.totalChildren(12);
		tab.child(0, 19324, 100, 100);//background
		tab.child(1, 19325, 255, 135);//select gambling text
		tab.child(2, 19326, 160, 180);//flowergame text
		tab.child(3, 19327, 300, 180);//dice game text
		tab.child(4, 19328, 140, 180); //select flow
		tab.child(5, 19329, 280, 180); //select dice

		tab.child(6, 19330, 225, 240);//confirm button
		tab.child(7, 19331, 225, 240);//confirm button
		tab.child(8, 19333, 255, 215);//sending to name text
		tab.child(9, 19359, 390, 104);//close button
		tab.child(10, 19360, 390, 104);//close button

		tab.child(11, 19357, 258, 243); // button text
	}

	public static void gambleTrade() {
		RSInterface tab = addInterface(19336, 512, 334);
		addTransparentSprite(19337, 1, "gamble/sprite");

		addTransparentSprite(19338, 2, "gamble/sprite");
		addTransparentSprite(19339, 3, "gamble/sprite");

		addText(19340, "Gamble Name", 2, 0xff9933, true, true, 0);

		addText(19341, "Your Offer:", 0, 0xff9933, false, true, 0);
		addText(19342, "Opponents Offer {player_name}:", 0, 0xff9933, false, true, 0);
		addText(19343, "Information:", 3, 0xff9933, true, true, 0);
		addText(19344, "This is a game of gambling.\\n\\nThe point of the game is to\\ndo some crazy gambling and"
				+ "\\nhopefully end up winning.\\n\\nThe rules are simple:\\nWhoever has the highest"
				+ "\\n gambling scheme in their\\nmouth wins the rainbow.", 1, 0xffffff, true, true, 0);

		addHoverButton(19345, "gamble/sprite", 4, 70, 18, "Accept", -1, 19346, 1);
		addHoveredButton(19346, "gamble/sprite", 5, 70, 18, 19347);

		addHoverButton(19348, "gamble/sprite", 4, 70, 18, "Decline", -1, 19349, 1);
		addHoveredButton(19349, "gamble/sprite", 5, 70, 18, 19350);

		addText(19351, "Accept", 0, 0xff9933, true, true, 0);
		addText(19352, "Decline", 0, 0xff9933, true, true, 0);

		itemContainer(19353, new String[]{"Remove 1", "Remove 5", "Remove 10", "Remove All", "Remove X"}, 28, 4, 7);
		itemContainer(19354, new String[]{}, 28, 4, 7);


		// inventory
		RSInterface inventory = addTabInterface(19355);
		inventory.width = 512;
		inventory.height = 334;
		inventory.parentID = 19335;
		inventory.id = 19355;
		inventory.totalChildren(1);
		inventory.child(0, 19356, 16, 8);

		RSInterface inventoryContainer = addTabInterface(19356);
		inventoryContainer.actions = new String[]{"Stake 1", "Stake 5", "Stake 10", "Stake All", "Stake X"};
		inventoryContainer.contentType = 0;
		inventoryContainer.type = 2;
		inventoryContainer.width = 4;
		inventoryContainer.height = 7;
		inventoryContainer.invSpritePadX = 10;
		inventoryContainer.invSpritePadY = 4;
		inventoryContainer.invStackSizes = new int[28];
		inventoryContainer.inv = new int[28];
		inventoryContainer.aBoolean259 = true;
		inventoryContainer.id = 19356;
		inventoryContainer.parentID = 19355;

		addText(19358, "", 0, 0xffffff, true, true, 0);

		tab.totalChildren(18);
		tab.child(0, 19337, 15, 20);//background
		tab.child(1, 19338, 23, 31);//bg your offer
		tab.child(2, 19338, 347, 31);//bg other offer
		tab.child(3, 19339, 172, 57);//bg information
		tab.child(4, 19340, 256, 38);//gamble name text
		tab.child(5, 19341, 26, 35);//your offer text
		tab.child(6, 19342, 350, 35);//opp offer text
		tab.child(7, 19343, 256, 74);//information text
		tab.child(8, 19344, 256, 104);//actual info text

		tab.child(9, 19345, 182, 275);//confirm button
		tab.child(10, 19346, 182, 275);//confirm button
		tab.child(11, 19348, 266, 275);//decline button
		tab.child(12, 19349, 266, 275);//decline button
		tab.child(13, 19351, 216, 278);//accept text text
		tab.child(14, 19352, 301, 278);//decline text text

		tab.child(15, 19353, 28, 50); // stake items
		tab.child(16, 19354, 350, 50); // stake items
		tab.child(17, 19358, 257, 260); // confirm text
	}

	private static void itemContainer(int id, String[] options, int size, int width, int height) {
		RSInterface rsInterface = addTabInterface(id);
		rsInterface.actions = options;
		rsInterface.contentType = 206;
		rsInterface.type = 2;
		rsInterface.width = width;
		rsInterface.height = height;
		rsInterface.invSpritePadX = 3;
		rsInterface.invSpritePadY = 6;
		rsInterface.invStackSizes = new int[size];
		rsInterface.inv = new int[size];
		rsInterface.aBoolean259 = true;
		rsInterface.id = id;
	}

	public static void voteRewards() {
		RSInterface tab = addTabInterface(18793);

		addSprite(18794, 0, "VoteReward/BACKGROUND");

		addHoverButton(18795, "VoteReward/BUTTON", 0, 89, 24, "Confirm", -1, 18796, 1);
		addHoveredButton(18796, "VoteReward/BUTTON", 1, 89, 24, 18797);

		addHoverButton(18798, "VoteReward/Close", 0, 16, 16, "Close", -1, 18799, 3);
		addHoveredButton(18799, "VoteReward/Close", 1, 16, 16, 18800);

		addConfigButton(18801, 18801, 0, 1, "VoteReward/CHECK", 15, 15, "Check", 1, 4, 533); // 1

		addConfigButton(18802, 18802, 0, 1, "VoteReward/CHECK", 15, 15, "Check", 1, 4, 534); // 2
		addConfigButton(18803, 18803, 0, 1, "VoteReward/CHECK", 15, 15, "Check", 1, 4, 535); // 3
		//addConfigButton(18804, 18804, 0, 1, "VoteReward/CHECK", 15, 15, "Check", 1, 4, 536); // 4
		addConfigButton(18805, 18805, 0, 1, "VoteReward/CHECK", 15, 15, "Check", 1, 4, 537); // 5
		addConfigButton(18806, 18806, 0, 1, "VoteReward/CHECK", 15, 15, "Check", 1, 4, 538); // 6
		//addConfigButton(18807, 18807, 0, 1, "VoteReward/CHECK", 15, 15, "Check", 1, 4, 539); // 7
		//addConfigButton(18808, 18808, 0, 1, "VoteReward/CHECK", 15, 15, "Check", 1, 4, 540); // 8

		addSprite(18809, 1, "VoteReward/STOCK"); // 1
		addSprite(18810, 1, "VoteReward/STOCK"); // 2
		addSprite(18811, 1, "VoteReward/STOCK"); // 3
		//addSprite(18812, 1, "VoteReward/STOCK"); // 4
		addSprite(18813, 1, "VoteReward/STOCK"); // 5
		addSprite(18814, 1, "VoteReward/STOCK"); // 6
		//	addSprite(18815, 1, "VoteReward/STOCK"); // 7
		//addSprite(18816, 1, "VoteReward/STOCK"); // 8

		addText(18817, "1.5x EXP 45mins", 1, 0xff9b00, true, true, 0); //1
		addText(18818, "Double PC Points 45mins", 1, 0xff9b00, true, true, 0); // 2
		addText(18819, "Double Pk Points 1h", 1, 0xff9b00, true, true, 0); // 3
		//addText(18820, "X2 Zombie Points 1h", tda, 1, 0xff9b00, true, true); // 4
		addText(18821, "Double Zombie Points 1h", 1, 0xff9b00, true, true, 0); // 5
		addText(18822, "Double Cwar Tickets 2h", 1, 0xff9b00, true, true, 0); // 6
		//	addText(18823, "Vote Reward 7", tda, 1, 0xff9b00, true, true); // 7
		//	addText(18824, "Vote Reward 8", tda, 1, 0xff9b00, true, true); // 8

		addText(18825, "Confirm", 2, 0xff9b00, true, true, 0);

		addText(18826, "Bonus Vote Rewards", 2, 0xff9b00, true, true, 0);

		addText(18827, "Cost: 6 Points", 0, 0xffff00, true, true, 0);
		addText(18828, "Cost: 4 Points", 0, 0xffff00, true, true, 0);
		addText(18829, "Cost: 4 Points", 0, 0xffff00, true, true, 0);
		//addText(18830, "Cost: 40 Points", tda, 0, 0xffff00, true, true);
		addText(18831, "Cost: 4 Points", 0, 0xffff00, true, true, 0);
		addText(18832, "Cost: 2 Points", 0, 0xffff00, true, true, 0);
		//addText(18833, "Cost: 70 Points", tda, 0, 0xffff00, true, true);
		//addText(18834, "Cost: 80 Points", tda, 0, 0xffff00, true, true);


		int index = 0;
		tab.totalChildren(27);
		tab.child(index++, 18794, ((515 - 375) / 2), ((337 - 319) / 2)); // Background
		tab.child(index++, 18809, 104, 40); // Text Sprite 1
		tab.child(index++, 18810, 104, 100); // Text Sprite 2
		tab.child(index++, 18811, 104, 160); // Text Sprite 3
		//tab.child(index++, 18812, 104, 220); // Text Sprite 4
		tab.child(index++, 18813, 281, 40); // Text Sprite 5
		tab.child(index++, 18814, 281, 100); // Text Sprite 6
		//tab.child(index++, 18815, 281, 160); // Text Sprite 7
		//tab.child(index++, 18816, 281, 220); // Text Sprite 8
		tab.child(index++, 18801, 241, 70); // Button 1 Check
		tab.child(index++, 18802, 241, 129); // Button 2 Check
		tab.child(index++, 18803, 241, 189); // Button 3 Check
		//	tab.child(index++, 18804, 241, 249); // Button 4 Check
		tab.child(index++, 18805, 418, 70); // Button 5 Check
		tab.child(index++, 18806, 418, 129); // Button 6 Check
		//	tab.child(index++, 18807, 418, 189); // Button 7 Check
		//	tab.child(index++, 18808, 418, 249); // Button 8 Check
		tab.child(index++, 18795, ((515 - 89) / 2), 240); // Confirm
		tab.child(index++, 18796, ((515 - 89) / 2), 240); // Confirm Hover
		tab.child(index++, 18798, 421, 12); // Close
		tab.child(index++, 18799, 421, 12); // Close Hover
		tab.child(index++, 18817, 170, 69); // Text 1
		tab.child(index++, 18818, 170, 129); // Text 2
		tab.child(index++, 18819, 170, 189); // Text 3
		//	tab.child(index++, 18820, 170, 249); // Text 4
		tab.child(index++, 18821, 347, 69); // Text 5
		tab.child(index++, 18822, 347, 129); // Text 6
		//	tab.child(index++, 18823, 347, 189); // Text 7
		//	tab.child(index++, 18824, 347, 249); // Text 8
		tab.child(index++, 18825, 257, 245); // Confirm Text
		tab.child(index++, 18826, 257, 14); // Title Text
		tab.child(index++, 18827, 171, 46); // Cost Text 1
		tab.child(index++, 18828, 171, 107); // Cost Text 2
		tab.child(index++, 18829, 171, 166); // Cost Text 3
		//tab.child(index++, 18830, 171, 226); // Cost Text 4
		tab.child(index++, 18831, 348, 46); // Cost Text 5
		tab.child(index++, 18832, 348, 107); // Cost Text 6
		//tab.child(index++, 18833, 348, 166); // Cost Text 7
		//tab.child(index++, 18834, 348, 226); // Cost Text 8
	}

	public static void ironmanInterface() {
		RSInterface tab = addInterface(19302, 512, 334);
		addSprite(19303, -1, "ironman/background");
		addText(19304, "Choose your account types...", 2, 0xff9933, true, true, 0);
		addText(19305, "Normal Mode", 1, 0xFFFFFF, false, true, 0);
		addText(19306, "Play the standard most popular \\nmode with a balanced xp rate", 0, 0xff9933, false, true, 0);
		addText(25625, "Elite Mode", 1, 0xFFFFFF, false, true, 0);
		addText(19307, "Play the harder Elite mode that \\nis 3.5x harder xp rate. This mode \\ngives a custom minimap dot, title, \\nand icon next to your name", 0, 0xff9933, false, true, 0);

		addConfigButton(19308, 19302, 1, 2, "ironman/select", 15, 15, "Select", 1, 5, 728); // normal
		addConfigButton(19309, 19302, 1, 2, "ironman/select", 15, 15, "Select", 2, 5, 728); // elite
		addConfigButton(19310, 19302, 1, 2, "ironman/select", 15, 15, "Select", 1, 5, 729); // iron
		addConfigButton(19311, 19302, 1, 2, "ironman/select", 15, 15, "Select", 2, 5, 729); // hciron
		addConfigButton(19312, 19302, 1, 2, "ironman/select", 15, 15, "Select", 3, 5, 729); // normal

		addText(19313, "None", 1, 0xFFFFFF, false, true, 0);
		addText(19314, "Standard Iron Man", 1, 0xFFFFFF, false, true, 0);
		addText(19315, "Ultimate Iron Man", 1, 0xFFFFFF, false, true, 0);
		addText(19316, "No Iron Man restrictions will apply to this account.", 0, 0xff9933, false, true, 0);
		addText(19317, "An Iron Man does not receive items or assistance from other players.\\nThey cannot trade, stake, receive PK loot, scavenge dropped items,\\nnor play certain multiplayer minigames.", 0, 0xff9933, false, true, 0);
		addText(19318, "In addition to the standard Iron Man rules, an Ultimate Iron Man\\ncannot use banks, nor retain any items on death in dangerous areas.", 0, 0xff9933, false, true, 0);

		addText(19319, "Account Mode", 2, 0xFFFFFF, true, true, 0);
		addText(19320, "Iron Man Mode", 2, 0xFFFFFF, true, true, 0);

		addHoverButton(19321, "VoteReward/BUTTON", 0, 89, 24, "Confirm", -1, 19322, 1);
		addHoveredButton(19322, "VoteReward/BUTTON", 1, 89, 24, 19323);

		addText(255, "Confirm", 2, 0xff9b00, true, true, 0);

		tab.totalChildren(22);
		tab.child(0, 19303, 10, 2);//background
		tab.child(1, 19304, 259, 5);//titletext
		tab.child(2, 19319, 259, 44);//account mode text
		tab.child(3, 19305, 87, 62);//elite mode text
		tab.child(4, 25625, 276, 62);//normal mode text
		tab.child(5, 19306, 69, 78);//elite mode info text
		tab.child(6, 19307, 259, 78);//normal mode info text
		tab.child(7, 19309, 68, 61);//elite select
		tab.child(8, 19308, 256, 61);//normal select

		tab.child(9, 19320, 259, 149);//iron man mode text
		tab.child(10, 19313, 87, 166);//standard iron man text
		tab.child(11, 19316, 69, 181);//standard iron man info text
		tab.child(12, 19314, 87, 195);//ultimate iron man text
		tab.child(13, 19317, 69, 212);//ultimate iron man info text
		tab.child(14, 19315, 87, 245);//none text
		tab.child(15, 19318, 69, 261);//none info text
		tab.child(16, 19310, 68, 165);//iron select
		tab.child(17, 19311, 68, 195);//ultimate select
		tab.child(18, 19312, 68, 245);//none select

		tab.child(19, 19321, 205, 297);//Confirm
		tab.child(20, 19322, 205, 297);//Confirm Hover

		tab.child(21, 255, 248, 300);//Confirm Hover
	}


	private static void zombiesInterface() {
		RSInterface rsa = addTabInterface(54365);

		setChildren(4, rsa);

		addText(54366, "Wave: 1", 2, 0xFF9900, false, true, 0);//Title
		addText(54367, "Points: 0", 2, 0xFF9900, false, true, 0);//Title

		addText(54368, "INSTANT KILL", 2, 0xFF0000, false, true, 0);//Title
		addText(54369, "0:30", 1, 0xFF9900, false, true, 0);//Title

		setBounds(54366, 5, 20, 0, rsa);//background
		setBounds(54367, 5, 35, 1, rsa);//background
		setBounds(54368, 230, 300, 2, rsa);//background
		setBounds(54369, 255, 315, 3, rsa);//background
	}

	private static void zombieLobbyInterface() {
		RSInterface rsa = addTabInterface(54370);

		setChildren(1, rsa);
		addText(54371, "Zombie game begins in: 30 seconds", 2, 0xFFFFFF, true, true, 0);//Title
		setBounds(54371, 250, 15, 0, rsa);//background
	}

	public static void skillLampInterface() {
		RSInterface Interface = addTabInterface(23700);

		setChildren(56, Interface);
		addSprite(23701, 1, "Interfaces/SkillLampInterface/BACKGROUND");// Background
		addHover(23702, 3, 0, 23703, 1, "", 17, 17, "Exit");//Close Button
		addHovered(23703, 2, "", 17, 17, 23704);//Close Button
		addText(23705, "Skill Lamp", 2, 0xFF9900, false, true, 0);//Title
		addText(23706, "What skill would you like to advance?", 1, 0xFF9900, false, true, 0);//What skill would you like to advance?
		addButton(23707, 0, "Interfaces/SkillLampInterface/IMAGE", 64, 64, "Buying 7M Attack Xp", 1);// Attack Button
		addText(23708, "Attack", 0, 0xFF9900, false, true, 0);//Attack Text

		addButton(23709, 1, "Interfaces/SkillLampInterface/IMAGE", 64, 64, "Buying 7M Strength Xp", 1);// strength Button
		addText(23710, "Strength", 0, 0xFF9900, false, true, 0);//Strength Text

		addButton(23711, 2, "Interfaces/SkillLampInterface/IMAGE", 64, 64, "Buying 7M Defence Xp", 1);// defence Button
		addText(23712, "Defence", 0, 0xFF9900, false, true, 0);//Defence Text

		addButton(23713, 3, "Interfaces/SkillLampInterface/IMAGE", 64, 64, "Buying 7M Constitution Xp", 1);// constitution Button
		addText(23714, "Constitution", 0, 0xFF9900, false, true, 0);//constitution Text

		addButton(23715, 4, "Interfaces/SkillLampInterface/IMAGE", 64, 64, "Buying 7M Range Xp", 1);// Range Button
		addText(23716, "Ranged", 0, 0xFF9900, false, true, 0);//Ranged Text

		addButton(23717, 6, "Interfaces/SkillLampInterface/IMAGE", 64, 64, "Buying 7M Prayer Xp", 1);// prayer Button
		addText(23718, "Prayer", 0, 0xFF9900, false, true, 0);//Prayer Text

		addButton(23719, 5, "Interfaces/SkillLampInterface/IMAGE", 64, 64, "Buying 7M Magic Xp", 1);// mage Button
		addText(23720, "Magic", 0, 0xFF9900, false, true, 0);//Mage Text

		addButton(23721, 7, "Interfaces/SkillLampInterface/IMAGE", 64, 64, "Buying 7M Agility Xp", 1);// agility Button
		addText(23722, "Agility", 0, 0xFF9900, false, true, 0);//Agility Text

		addButton(23723, 8, "Interfaces/SkillLampInterface/IMAGE", 64, 64, "Buying 7M Herblore Xp", 1);// herblore Button
		addText(23724, "Herblore", 0, 0xFF9900, false, true, 0);//Herblore Text

		addButton(23725, 9, "Interfaces/SkillLampInterface/IMAGE", 64, 64, "Buying 7M Thieving Xp", 1);// thieving Button
		addText(23726, "Thieving", 0, 0xFF9900, false, true, 0);//Thieving Text

		addButton(23727, 10, "Interfaces/SkillLampInterface/IMAGE", 64, 64, "Buying 7M Crafting Xp", 1);// crafting Button
		addText(23728, "Crafting", 0, 0xFF9900, false, true, 0);//Crafting Text

		addButton(23729, 11, "Interfaces/SkillLampInterface/IMAGE", 64, 64, "Buying 7M Fletching Xp", 1);// fletching Button
		addText(23730, "Fletching", 0, 0xFF9900, false, true, 0);//Fletching Text

		addButton(23731, 12, "Interfaces/SkillLampInterface/IMAGE", 64, 64, "Buying 7M Slayer Xp", 1);// slayer Button
		addText(23732, "Slayer", 0, 0xFF9900, false, true, 0);//Slayer Text

		addButton(23733, 13, "Interfaces/SkillLampInterface/IMAGE", 64, 64, "Buying 7M Mining Xp", 1);// mining Button
		addText(23734, "Mining", 0, 0xFF9900, false, true, 0);//Mining Text

		addButton(23735, 14, "Interfaces/SkillLampInterface/IMAGE", 64, 64, "Buying 7M Smithing Xp", 1);// smithing Button
		addText(23736, "Smithing", 0, 0xFF9900, false, true, 0);//Smithing Text

		addButton(23737, 15, "Interfaces/SkillLampInterface/IMAGE", 64, 64, "Buying 7M Fishing Xp", 1);// fishing Button
		addText(23738, "Fishing", 0, 0xFF9900, false, true, 0);//Fishing Text

		addButton(23739, 16, "Interfaces/SkillLampInterface/IMAGE", 64, 64, "Buying 7M Cooking Xp", 1);// cooking Button
		addText(23740, "Cooking", 0, 0xFF9900, false, true, 0);//Cooking Text

		addButton(23741, 17, "Interfaces/SkillLampInterface/IMAGE", 64, 64, "Buying 7M Firemaking Xp", 1);// firemaking Button
		addText(23742, "Firemaking", 0, 0xFF9900, false, true, 0);//Firemaking Text

		addButton(23743, 18, "Interfaces/SkillLampInterface/IMAGE", 64, 64, "Buying 7M Woodcutting Xp", 1);// woodcutting Button
		addText(23744, "Woodcutting", 0, 0xFF9900, false, true, 0);//Woodcutting Text

		addButton(23745, 19, "Interfaces/SkillLampInterface/IMAGE", 64, 64, "Buying 7M Farming Xp", 1);// farming Button
		addText(23746, "Farming", 0, 0xFF9900, false, true, 0);//Farming Text

		addButton(23747, 20, "Interfaces/SkillLampInterface/IMAGE", 64, 64, "Buying 7M Runecrafting Xp", 1);// runecraft Button
		addText(23748, "Runecrafting", 0, 0xFF9900, false, true, 0);//Runecrafting Text

		addButton(23749, 21, "Interfaces/SkillLampInterface/IMAGE", 64, 64, "Buying 7M Hunter Xp", 1);// Hunter button
		addText(23750, "Hunting", 0, 0xFF9900, false, true, 0);//Hunting Text

		addButton(23751, 22, "Interfaces/SkillLampInterface/IMAGE", 64, 64, "Buying 7M Summoning Xp", 1);// Summoning button
		addText(23752, "Summoning", 0, 0xFF9900, false, true, 0);//Summoning Text

		addButton(23753, 23, "Interfaces/SkillLampInterface/IMAGE", 64, 64, "Buying Dung Xp", 1);// Dungeoneering Button
		addText(23754, "Dungeoneering", 0, 0xFF9900, false, true, 0);//Dungeoneering Text

		addText(23755, "To advance a skill click the text next to your desired skills icon then press done at the bottom", 0, 0xFF9900, false, true, 0);//Second Title
		addHoverButton(23756, "Interfaces/SkillLampInterface/DONE", 1, 64, 18, "Done", -1, 23757, 1);//Done Button
		addHoveredButton(23757, "Interfaces/SkillLampInterface/DONE", 2, 64, 18, 23758);//Done Button Hover
		addText(23759, "Done", 1, 0xFFFFFF, false, true, 0);//Done Button Text

		setBounds(23701, 0, 10, 0, Interface);//background
		setBounds(23702, 488, 14, 1, Interface);//Close Button
		setBounds(23705, 235, 16, 2, Interface);//Title
		setBounds(23706, 150, 67, 3, Interface);//What skill would you like to advance?
		setBounds(23707, 25, 99, 4, Interface);//Attack Button
		setBounds(23708, 44, 106, 5, Interface);//Attack Text
		setBounds(23709, 25, 135, 6, Interface);//Strength Button
		setBounds(23710, 44, 142, 7, Interface);//Strength Text
		setBounds(23711, 25, 171, 8, Interface);//Defence Button
		setBounds(23712, 44, 178, 9, Interface);//Defence Text
		setBounds(23713, 22, 207, 10, Interface);//Constituion Button
		setBounds(23714, 44, 214, 11, Interface);//Constituion Text
		setBounds(23715, 22, 243, 12, Interface);//Ranged Button
		setBounds(23716, 44, 250, 13, Interface);//Ranged Text
		setBounds(23717, 22, 275, 14, Interface);//Prayer Button
		setBounds(23718, 44, 282, 15, Interface);//Prayer Text
		setBounds(23719, 144, 99, 16, Interface);//Mage Button
		setBounds(23720, 168, 106, 17, Interface);//Mage Text
		setBounds(23721, 144, 135, 18, Interface);//Agility Button
		setBounds(23722, 168, 142, 19, Interface);//Agility Text
		setBounds(23723, 144, 171, 20, Interface);//Herblore Button
		setBounds(23724, 168, 178, 21, Interface);//Herblore Text
		setBounds(23725, 144, 207, 22, Interface);//Thieving Button
		setBounds(23726, 168, 214, 23, Interface);//Thieving Text
		setBounds(23727, 144, 243, 24, Interface);//Crafting Button
		setBounds(23728, 168, 250, 25, Interface);//Crafting Text
		setBounds(23729, 144, 279, 26, Interface);//Fletching Button
		setBounds(23730, 168, 286, 27, Interface);//Fletching Text
		setBounds(23731, 263, 99, 28, Interface);//Slayer Button
		setBounds(23732, 292, 106, 29, Interface);//Slayer Text
		setBounds(23733, 263, 135, 30, Interface);//Mining Button
		setBounds(23734, 292, 142, 31, Interface);//Mining Text
		setBounds(23735, 263, 171, 32, Interface);//Smithing Button
		setBounds(23736, 292, 178, 33, Interface);//Smithing Text
		setBounds(23737, 263, 207, 34, Interface);//Fishing Button
		setBounds(23738, 292, 214, 35, Interface);//Fishing Text
		setBounds(23739, 263, 243, 36, Interface);//Cooking Button
		setBounds(23740, 292, 250, 37, Interface);//Cooking Text
		setBounds(23741, 263, 279, 38, Interface);//Firemaking Button
		setBounds(23742, 292, 286, 39, Interface);//Firemaking Text
		setBounds(23743, 382, 99, 40, Interface);//Woodcutting Button
		setBounds(23744, 416, 106, 41, Interface);//Woodcutting Text
		setBounds(23745, 382, 135, 42, Interface);//Farming Button
		setBounds(23746, 416, 142, 43, Interface);//Farming Text
		setBounds(23747, 382, 171, 44, Interface);//RuneCrafting Button
		setBounds(23748, 416, 178, 45, Interface);//RuneCrafting Text
		setBounds(23749, 382, 207, 46, Interface);//Hunting Button
		setBounds(23750, 416, 214, 47, Interface);//Hunting Text
		setBounds(23751, 382, 243, 48, Interface);//Summoning Button
		setBounds(23752, 416, 250, 49, Interface);//Summoning Text
		setBounds(23753, 382, 279, 50, Interface);//Dungeoneering Button
		setBounds(23754, 416, 286, 51, Interface);//Dungeoneering Text
		setBounds(23755, 22, 44, 52, Interface);//Second Title
		setBounds(23756, 230, 304, 53, Interface);//Done Button
		setBounds(23757, 230, 304, 54, Interface);//Done Button Hover
		setBounds(23759, 246, 306, 55, Interface);//Done Button Text
	}

	public static void prayerMenu() {
		RSInterface rsinterface = addInterface(5608, 512, 334);
		int i = 0;
		int j = 0;
		byte byte0 = 10;
		byte byte1 = 50;
		byte byte2 = 10;
		byte byte3 = 86;
		byte byte4 = 10;
		byte byte5 = 122;
		byte byte6 = 10;
		char c = '\237';
		byte byte7 = 10;
		byte byte8 = 86;
		int k = 1;
		byte byte9 = 52;
		RSInterface currentPray = addText(687, "", 0xff981f, false, true, -1, 1);
		currentPray.valueIndexArray = new int[2][];
		currentPray.valueIndexArray[0] = new int[3];
		currentPray.valueIndexArray[0][0] = 1;
		currentPray.valueIndexArray[0][1] = 5;
		currentPray.valueIndexArray[0][2] = 0;
		currentPray.valueIndexArray[1] = new int[3];
		currentPray.valueIndexArray[1][0] = 2;
		currentPray.valueIndexArray[1][1] = 5;
		currentPray.valueIndexArray[1][2] = 0;
		currentPray.message = "%1 / %2";
		addSprite(25105, 0, "Interfaces/PrayerTab/PRAYERICON");
		addPrayerWithTooltip(25000, 0, 83, 0, j, 25052, "Activate <col=ff9040>Thick Skin");
		j++;
		addPrayerWithTooltip(25002, 0, 84, 3, j, 25054, "Activate <col=ff9040>Burst of Strength");
		j++;
		addPrayerWithTooltip(25004, 0, 85, 6, j, 25056, "Activate <col=ff9040>Clarity of Thought");
		j++;
		addPrayerWithTooltip(25006, 0, 601, 7, j, 25058, "Activate <col=ff9040>Sharp Eye");
		j++;
		addPrayerWithTooltip(25008, 0, 602, 8, j, 25060, "Activate <col=ff9040>Mystic Will");
		j++;
		addPrayerWithTooltip(25010, 0, 86, 9, j, 25062, "Activate <col=ff9040>Rock Skin");
		j++;
		addPrayerWithTooltip(25012, 0, 87, 12, j, 25064, "Activate <col=ff9040>Superhuman Strength");
		j++;
		addPrayerWithTooltip(25014, 0, 88, 15, j, 25066, "Activate <col=ff9040>Improved Reflexes");
		j++;
		addPrayerWithTooltip(25016, 0, 89, 18, j, 25068, "Activate <col=ff9040>Rapid Restore");
		j++;
		addPrayerWithTooltip(25018, 0, 90, 21, j, 25070, "Activate <col=ff9040>Rapid Heal");
		j++;
		addPrayerWithTooltip(25020, 0, 91, 24, j, 25072, "Activate <col=ff9040>Protect Item");
		j++;
		addPrayerWithTooltip(25022, 0, 603, 25, j, 25074, "Activate <col=ff9040>Hawk Eye");
		j++;
		addPrayerWithTooltip(25024, 0, 604, 26, j, 25076, "Activate <col=ff9040>Mystic Lore");
		j++;
		addPrayerWithTooltip(25026, 0, 92, 27, j, 25078, "Activate <col=ff9040>Steel Skin");
		j++;
		addPrayerWithTooltip(25028, 0, 93, 30, j, 25080, "Activate <col=ff9040>Ultimate Strength");
		j++;
		addPrayerWithTooltip(25030, 0, 94, 33, j, 25082, "Activate <col=ff9040>Incredible Reflexes");
		j++;
		addPrayerWithTooltip(25032, 0, 95, 36, j, 25084, "Activate <col=ff9040>Protect from Magic");
		j++;
		addPrayerWithTooltip(25034, 0, 96, 39, j, 25086, "Activate <col=ff9040>Protect from Missles");
		j++;
		addPrayerWithTooltip(25036, 0, 97, 42, j, 25088, "Activate <col=ff9040>Protect from Melee");
		j++;
		addPrayerWithTooltip(25038, 0, 605, 43, j, 25090, "Activate <col=ff9040>Eagle Eye");
		j++;
		addPrayerWithTooltip(25040, 0, 606, 44, j, 25092, "Activate <col=ff9040>Mystic Might");
		j++;
		addPrayerWithTooltip(25042, 0, 98, 45, j, 25094, "Activate <col=ff9040>Retribution");
		j++;
		addPrayerWithTooltip(25044, 0, 99, 48, j, 25096, "Activate <col=ff9040>Redemption");
		j++;
		addPrayerWithTooltip(25046, 0, 100, 51, j, 25098, "Activate <col=ff9040>Smite");
		j++;
		addPrayerWithTooltip(25048, 0, 607, 59, j, 25100, "Activate <col=ff9040>Chivalry");
		j++;
		addPrayerWithTooltip(25050, 0, 608, 69, j, 25102, "Activate <col=ff9040>Piety");
		j++;
		addPrayerWithTooltip(25112, 0, 609, 73, j, 25108, "Activate <col=ff9040>Rigour");
		j++;
		addPrayerWithTooltip(25106, 0, 610, 76, j, 25110, "Activate <col=ff9040>Augury");
		j++;

		//public static void addPrayerWithTooltip(int i, int configId, int configFrame, int anIntArray212, int prayerSpriteID, int Hover, String tooltip) {


		//public static void addPrayerWithTooltip(int i, int configId, int configFrame, int anIntArray212, int prayerSpriteID, int Hover, String tooltip) {

		addTooltip(25052, "Level 01\nThick Skin\nIncreases your Defence by 5%");
		addTooltip(25054, "Level 04\nBurst of Strength\nIncreases your Strength by 5%");
		addTooltip(25056, "Level 07\nClarity of Thought\nIncreases your Attack by 5%");
		addTooltip(25058, "Level 08\nSharp Eye\nIncreases your Ranged by 5%");
		addTooltip(25060, "Level 09\nMystic Will\nIncreases your Magic by 5%");
		addTooltip(25062, "Level 10\nRock Skin\nIncreases your Defence by 10%");
		addTooltip(25064, "Level 13\nSuperhuman Strength\nIncreases your Strength by 10%");
		addTooltip(25066, "Level 16\nImproved Reflexes\nIncreases your Attack by 10%");
		addTooltip(25068, "Level 19\nRapid Restore\n2x restore rate for all stats\nexcept Hitpoints, Summon" + "ing\nand Prayer");
		addTooltip(25070, "Level 22\nRapid Heal\n2x restore rate for the\nHitpoints stat");
		addTooltip(25072, "Level 25\nProtect Item\nKeep 1 extra item if you die");
		addTooltip(25074, "Level 26\nHawk Eye\nIncreases your Ranged by 10%");
		addTooltip(25076, "Level 27\nMystic Lore\nIncreases your Magic by 10%");
		addTooltip(25078, "Level 28\nSteel Skin\nIncreases your Defence by 15%");
		addTooltip(25080, "Level 31\nUltimate Strength\nIncreases your Strength by 15%");
		addTooltip(25082, "Level 34\nIncredible Reflexes\nIncreases your Attack by 15%");
		addTooltip(25084, "Level 37\nProtect from Magic\nProtection from magical attacks");
		addTooltip(25086, "Level 40\nProtect from Missles\nProtection from ranged attacks");
		addTooltip(25088, "Level 43\nProtect from Melee\nProtection from melee attacks");
		addTooltip(25090, "Level 44\nEagle Eye\nIncreases your Ranged by 15%");
		addTooltip(25092, "Level 45\nMystic Might\nIncreases your Magic by 15%");
		addTooltip(25094, "Level 46\nRetribution\nInflicts damage to nearby\ntargets if you die");
		addTooltip(25096, "Level 49\nRedemption\nHeals you when damaged\nand Hitpoints falls\nbelow 10%");
		addTooltip(25098, "Level 52\nSmite\n1/4 of damage dealt is\nalso removed from\nopponent's Prayer");
		addTooltip(25100, "Level 60\nChivalry\nIncreases your Defence by 20%,\nStrength by 18%, and Attack " + "by\n15%");
		addTooltip(25102, "Level 70\nPiety\nIncreases your Defence by 25%,\nStrength by 23%, and Attack by\n" + "20%");
		addTooltip(25108, "Level 74\nRigour\nIncreases your Ranged by\n20% and Defence by 25%");
		addTooltip(25110, "Level 77\nAugury\nIncreases magical accuracy\nby 20% and Defence by 25%");

		setChildren(86, rsinterface);
		setBounds(687, 85, 241, i, rsinterface);
		i++;
		setBounds(25105, 65, 241, i, rsinterface);
		i++;
		setBounds(25000, 2, 5, i, rsinterface);
		i++;
		setBounds(25001, 5, 8, i, rsinterface);
		i++;
		setBounds(25002, 40, 5, i, rsinterface);
		i++;
		setBounds(25003, 44, 8, i, rsinterface);
		i++;
		setBounds(25004, 76, 5, i, rsinterface);
		i++;
		setBounds(25005, 79, 11, i, rsinterface);
		i++;
		setBounds(25006, 113, 5, i, rsinterface);
		i++;
		setBounds(25007, 116, 10, i, rsinterface);
		i++;
		setBounds(25008, 150, 5, i, rsinterface);
		i++;
		setBounds(25009, 153, 9, i, rsinterface);
		i++;
		setBounds(25010, 2, 45, i, rsinterface);
		i++;
		setBounds(25011, 5, 48, i, rsinterface);
		i++;
		setBounds(25012, 39, 45, i, rsinterface);
		i++;
		setBounds(25013, 44, 47, i, rsinterface);
		i++;
		setBounds(25014, 76, 45, i, rsinterface);
		i++;
		setBounds(25015, 79, 49, i, rsinterface);
		i++;
		setBounds(25016, 113, 45, i, rsinterface);
		i++;
		setBounds(25017, 116, 50, i, rsinterface);
		i++;
		setBounds(25018, 151, 45, i, rsinterface);
		i++;
		setBounds(25019, 154, 50, i, rsinterface);
		i++;
		setBounds(25020, 2, 82, i, rsinterface);
		i++;
		setBounds(25021, 4, 84, i, rsinterface);
		i++;
		setBounds(25022, 40, 82, i, rsinterface);
		i++;
		setBounds(25023, 44, 87, i, rsinterface);
		i++;
		setBounds(25024, 77, 82, i, rsinterface);
		i++;
		setBounds(25025, 81, 85, i, rsinterface);
		i++;
		setBounds(25026, 114, 83, i, rsinterface);
		i++;
		setBounds(25027, 117, 85, i, rsinterface);
		i++;
		setBounds(25028, 153, 83, i, rsinterface);
		i++;
		setBounds(25029, 156, 87, i, rsinterface);
		i++;
		setBounds(25030, 2, 120, i, rsinterface);
		i++;
		setBounds(25031, 5, 125, i, rsinterface);
		i++;
		setBounds(25032, 40, 120, i, rsinterface);
		i++;
		setBounds(25033, 43, 124, i, rsinterface);
		i++;
		setBounds(25034, 78, 120, i, rsinterface);
		i++;
		setBounds(25035, 83, 124, i, rsinterface);
		i++;
		setBounds(25036, 114, 120, i, rsinterface);
		i++;
		setBounds(25037, 115, 121, i, rsinterface);
		i++;
		setBounds(25038, 151, 120, i, rsinterface);
		i++;
		setBounds(25039, 154, 124, i, rsinterface);
		i++;
		setBounds(25040, 2, 158, i, rsinterface);
		i++;
		setBounds(25041, 5, 160, i, rsinterface);
		i++;
		setBounds(25042, 39, 158, i, rsinterface);
		i++;
		setBounds(25043, 41, 158, i, rsinterface);
		i++;
		setBounds(25044, 76, 158, i, rsinterface);
		i++;
		setBounds(25045, 79, 163, i, rsinterface);
		i++;
		setBounds(25046, 114, 158, i, rsinterface);
		i++;
		setBounds(25047, 116, 158, i, rsinterface);
		i++;
		setBounds(25048, 153, 158, i, rsinterface);
		i++;
		setBounds(25049, 161, 160, i, rsinterface);
		i++;
		setBounds(25050, 2, 196, i, rsinterface);
		i++;
		setBounds(25051, 4, 207, i, rsinterface);
		i++;
		setBounds(25112, 40, 195, i, rsinterface); // rigour glow
		i++;
		setBounds(25113, 43, 198, i, rsinterface); // rigour on?
		i++;
		setBounds(25106, 78, 193, i, rsinterface); // augury glow
		i++;
		setBounds(25107, 81, 197, i, rsinterface); // Augury


		setBoundry(++i, 25052, byte0 - 2, byte1, rsinterface);
		setBoundry(++i, 25054, byte0 - 5, byte1, rsinterface);
		setBoundry(++i, 25056, byte0, byte1, rsinterface);
		setBoundry(++i, 25058, byte0, byte1, rsinterface);
		setBoundry(++i, 25060, byte0, byte1, rsinterface);
		setBoundry(++i, 25062, byte2 - 9, byte3, rsinterface);
		setBoundry(++i, 25064, byte2 - 11, byte3, rsinterface);
		setBoundry(++i, 25066, byte2, byte3, rsinterface);
		setBoundry(++i, 25068, byte2, byte3, rsinterface);
		setBoundry(++i, 25070, byte2 + 25, byte3, rsinterface);
		setBoundry(++i, 25072, byte4, byte5, rsinterface);
		setBoundry(++i, 25074, byte4 - 2, byte5, rsinterface);
		setBoundry(++i, 25076, byte4, byte5, rsinterface);
		setBoundry(++i, 25078, byte4 - 7, byte5, rsinterface);
		setBoundry(++i, 25080, byte4 - 10, byte5, rsinterface);
		setBoundry(++i, 25082, byte6, c, rsinterface);
		setBoundry(++i, 25084, byte6 - 8, c, rsinterface);
		setBoundry(++i, 25086, byte6 - 7, c, rsinterface);
		setBoundry(++i, 25088, byte6 - 2, c, rsinterface);
		setBoundry(++i, 25090, byte6 - 2, c, rsinterface);
		setBoundry(++i, 25092, byte7, byte8, rsinterface);
		setBoundry(++i, 25094, byte7, byte8 - 20, rsinterface);
		setBoundry(++i, 25096, byte7, byte8 - 25, rsinterface);
		setBoundry(++i, 25098, byte7 + 15, byte8 - 25, rsinterface);
		setBoundry(++i, 25100, byte7 - 12, byte8 - 20, rsinterface);
		setBoundry(++i, 25102, k - 2, byte9, rsinterface);
		setBoundry(++i, 25108, byte7 - 12, byte8 + 35, rsinterface);
		setBoundry(++i, 25110, k - 2, byte9 + 65, rsinterface);
		i++;
	}

	public static void addTooltip(int id, String text) {
		RSInterface rsinterface = addTabInterface(id);
		rsinterface.parentID = id;
		rsinterface.type = 0;
		rsinterface.isMouseoverTriggered = true;
		rsinterface.mOverInterToTrigger = -1;
		addTooltipBox(id + 1, text);
		rsinterface.totalChildren(1);
		rsinterface.child(0, id + 1, 0, 0);
	}

	public static void addTooltipBox(int id, String text) {
		RSInterface rsi = addInterface(id, 512, 334);
		rsi.id = id;
		rsi.parentID = id;
		rsi.type = 9;
		rsi.message = text;
	}

	public static void addPrayerWithTooltip(int i, int configId, int configFrame, int requiredValues, int prayerSpriteID, int Hover, String tooltip) {
		RSInterface Interface = addTabInterface(i);
		Interface.id = i;
		Interface.parentID = 5608;
		Interface.type = 5;
		Interface.atActionType = 4;
		Interface.contentType = 0;
		Interface.opacity = 0;
		Interface.mOverInterToTrigger = Hover;
		Interface.sprite1 = imageLoader(0, "Interfaces/PrayerTab/PRAYERGLOW");
		Interface.sprite2 = imageLoader(1, "Interfaces/PrayerTab/PRAYERGLOW");
		Interface.width = 34;
		Interface.height = 34;
		Interface.valueCompareType = new int[1];
		Interface.requiredValues = new int[1];
		Interface.valueCompareType[0] = 1;
		Interface.requiredValues[0] = configId;
		Interface.valueIndexArray = new int[1][3];
		Interface.valueIndexArray[0][0] = 5;
		Interface.valueIndexArray[0][1] = configFrame;
		Interface.valueIndexArray[0][2] = 0;
		Interface.tooltip = tooltip;
		Interface = addTabInterface(i + 1);
		Interface.id = i + 1;
		Interface.parentID = 5608;
		Interface.type = 5;
		Interface.atActionType = 0;
		Interface.contentType = 0;
		Interface.opacity = 0;
		Interface.sprite1 = imageLoader(prayerSpriteID, "Interfaces/PrayerTab/PRAYERON");
		Interface.sprite2 = imageLoader(prayerSpriteID, "Interfaces/PrayerTab/PRAYEROFF");
		Interface.width = 34;
		Interface.height = 34;
		Interface.valueCompareType = new int[1];
		Interface.requiredValues = new int[1];
		Interface.valueCompareType[0] = 2;
		Interface.requiredValues[0] = requiredValues + 1;
		Interface.valueIndexArray = new int[1][3];
		Interface.valueIndexArray[0][0] = 2;
		Interface.valueIndexArray[0][1] = 5;
		Interface.valueIndexArray[0][2] = 0;
	}

	public static void setBoundry(int frame, int ID, int X, int Y, RSInterface RSInterface) {
		RSInterface.children[frame] = ID;
		RSInterface.childX[frame] = X;
		RSInterface.childY[frame] = Y;
	}

	public static void duelInterface() {
		RSInterface Interface = addInterface(37888, 512, 334);

		Interface.centerText = true;
		addSprite(37889, 0, "duel");

		addConfigButton(37890, 904, 199, 200, "Interfaces/DUEL", 15,
				15, "No Ranged", 1, 5, 780);
		addConfigButton(37891, 904, 199, 200, "Interfaces/DUEL", 15,
				15, "No Melee", 1, 5, 781);
		addConfigButton(37892, 904, 199, 200, "Interfaces/DUEL", 15,
				15, "No Magic", 1, 5, 782);
		addConfigButton(37893, 904, 199, 200, "Interfaces/DUEL", 15,
				15, "Fun Weapons", 1, 5, 783);
		addConfigButton(37894, 904, 199, 200, "Interfaces/DUEL", 15,
				15, "No Forfeit", 1, 5, 784);
		addConfigButton(37895, 904, 199, 200, "Interfaces/DUEL", 15,
				15, "No Drinks", 1, 5, 785);
		addConfigButton(37896, 904, 199, 200, "Interfaces/DUEL", 15,
				15, "No Food", 1, 5, 786);
		addConfigButton(37897, 904, 199, 200, "Interfaces/DUEL", 15,
				15, "No Prayer", 1, 5, 787);
		addConfigButton(37898, 904, 199, 200, "Interfaces/DUEL", 15,
				15, "No Movement", 1, 5, 788);
		addConfigButton(37899, 904, 199, 200, "Interfaces/DUEL", 15,
				15, "Obstacles", 1, 5, 789);
		addConfigButton(37900, 904, 199, 200, "Interfaces/DUEL", 15,
				15, "No Special Attack", 1, 5, 790);

		addHover(3442, 3, 0, 3325, 1, "Interfaces/Bank/BANK", 17, 17,
				"Close Window");
		addHovered(3325, 2, "Interfaces/Bank/BANK", 17, 17, 3326);

		addText(17800, "Accept", 0, 0x33ff00, false, true, 0);
		addText(17801, "Decline", 0, 0xff0000, false, true, 0);

		addHoverButton(37910, "Interfaces/Rules/SPRITE", 6, 72, 32, "Accept",
				-1, 37911, 1);
		addHoveredButton(37911, "Interfaces/Rules/SPRITE", 7, 72, 32, 37912);
		addHoverButton(37913, "Interfaces/Rules/SPRITE", 6, 72, 32, "Decline",
				-1, 37914, 1);
		addHoveredButton(37914, "Interfaces/Rules/SPRITE", 7, 72, 32, 37915);

		addConfigButton(37926, 904, 2, 1, "Interfaces/DUEL", 36,
				36, "Helmet", 1, 5, 890);
		addConfigButton(37916, 904, 2, 1, "Interfaces/DUEL", 36,
				36, "Neck", 1, 5, 891);
		addConfigButton(37917, 904, 2, 1, "Interfaces/DUEL", 36,
				36, "Body", 1, 5, 892);
		addConfigButton(37918, 904, 2, 1, "Interfaces/DUEL", 36,
				36, "Legs", 1, 5, 893);
		addConfigButton(37919, 904, 2, 1, "Interfaces/DUEL", 36,
				36, "Feet", 1, 5, 894);
		addConfigButton(37920, 904, 2, 1, "Interfaces/DUEL", 36,
				36, "Cape", 1, 5, 895);
		addConfigButton(37921, 904, 2, 1, "Interfaces/DUEL", 36,
				36, "Arrows", 1, 5, 896);
		addConfigButton(37922, 904, 2, 1, "Interfaces/DUEL", 36,
				36, "Weapon", 1, 5, 897);
		addConfigButton(37923, 904, 2, 1, "Interfaces/DUEL", 36,
				36, "Shield", 1, 5, 898);
		addConfigButton(37924, 904, 2, 1, "Interfaces/DUEL", 36,
				36, "Gloves", 1, 5, 899);
		addConfigButton(37925, 904, 2, 1, "Interfaces/DUEL", 36,
				36, "Ring", 1, 5, 900);

		setChildren(33, Interface);
		setBounds(37889, 0, 0, 0, Interface);
		setBounds(3442, 482, 21, 1, Interface);
		setBounds(3325, 482, 21, 2, Interface);

		setBounds(37890, 350, 51, 3, Interface);
		setBounds(37891, 350, 69, 4, Interface);
		setBounds(37892, 350, 87, 5, Interface);
		setBounds(37893, 350, 105, 6, Interface);
		setBounds(37894, 350, 123, 7, Interface);
		setBounds(37895, 350, 141, 8, Interface);
		setBounds(37896, 350, 159, 9, Interface);
		setBounds(37897, 350, 177, 10, Interface);
		setBounds(37898, 350, 195, 11, Interface);
		setBounds(37899, 350, 213, 12, Interface);
		setBounds(37900, 350, 231, 13, Interface);

		setBounds(37910, 247, 260, 14, Interface);
		setBounds(37911, 247, 260, 15, Interface);

		setBounds(37913, 377, 260, 16, Interface);
		setBounds(37914, 377, 260, 17, Interface);

		setBounds(17800, 265, 270, 18, Interface);
		setBounds(17801, 395, 270, 19, Interface);

		setBounds(38140, -1, 38, 20, Interface);
		setBounds(38240, -1, 179, 21, Interface);

		setBounds(37926, 242, 50, 22, Interface);
		setBounds(37916, 242, 89, 23, Interface);
		setBounds(37917, 242, 128, 24, Interface);
		setBounds(37918, 242, 168, 25, Interface);
		setBounds(37919, 242, 208, 26, Interface);
		setBounds(37920, 201, 89, 27, Interface);
		setBounds(37921, 283, 89, 28, Interface);
		setBounds(37922, 186, 128, 29, Interface);
		setBounds(37923, 298, 128, 30, Interface);
		setBounds(37924, 186, 208, 31, Interface);
		setBounds(37925, 298, 208, 32, Interface);

		Interface = addInterface(38140, 163, 122);
		Interface.scrollMax = 300;
		setChildren(31, Interface);
		int Ypos = 18;
		int frameID = 0;
		for (int iD = 38145; iD <= 38175; iD++) {
			addText(iD, "", 0x000080, true, false, 52, 1);
			setBounds(iD, 202, Ypos, frameID, Interface);
			frameID++;
			Ypos += 19;
			Ypos++;
		}
		Interface = addInterface(38240, 163, 119);
		Interface.scrollMax = 300;
		setChildren(31, Interface);
		int Y2pos = 18;
		int frameID2 = 0;
		for (int iD = 38245; iD <= 38275; iD++) {
			addText(iD, "", 0x000080, true, false, 52, 1);
			setBounds(iD, 202, Y2pos, frameID2, Interface);
			frameID2++;
			Ypos += 19;
			Ypos++;
		}

	}

	public static void skillInterface() {
		RSInterface rsInterface = addInterface(28134, 512, 334);
		rsInterface.centerText = true;
		addSprite(28135, 0, "skilltab");
		addSprite(28136, 1, "skilltab");
		addHover(28137, 3, 0, 28138, 0, "CLOSE", 26, 23, "Close");
		addHovered(28138, 1, "CLOSE", 26, 23, 28139);

		setChildren(5, rsInterface);
		setBounds(28136, 1, 6, 0, rsInterface);
		setBounds(28135, 1, 6, 1, rsInterface);
		setBounds(28137, 465, 9, 2, rsInterface);
		setBounds(28138, 465, 9, 3, rsInterface);
		setBounds(28140, -100, 86, 4, rsInterface);

		rsInterface = addInterface(28140, 404, 217);
		rsInterface.scrollMax = 1300;
		setChildren(51, rsInterface);
		int Ypos = 18;
		int frameID = 0;
		for (int iD = 28145; iD <= 28195; iD++) {
			addText(iD, "", 0x000080, true, false, 52, 1);
			setBounds(iD, 202, Ypos, frameID, rsInterface);
			frameID++;
			Ypos += 19;
			Ypos++;
		}
	}

	public static void quickCurses() {
		int frame = 0;
		RSInterface tab = addTabInterface(17234);
		//addSprite(17201, 3, "/Interfaces/QuickPrayer/Sprite");
		addText(17235, "Select your quick curses:", 0, 0xFF981F, false, true, 0);
		//addTransparentSprite(17229, 0, "/Interfaces/QuickPrayer/Sprite", 50);

		for (int i = 17202, j = 630; i <= 17222 || j <= 656; i++, j++) {
			addConfigButton(i, 17234, 2, 1, "/Interfaces/QuickPrayer/Sprite", 14, 15, "Select", 0, 1, j);
		}
		addHoverButton(17231, "/Interfaces/QuickPrayer/Sprite", 4, 190, 24, "Confirm Selection", -1, 17232, 1);
		addHoveredButton(17232, "/Interfaces/QuickPrayer/Sprite", 5, 190, 24, 17233);

		setChildren(46, tab);
		setBounds(22504, 5, 8 + 20, frame++, tab);
		setBounds(22506, 44, 8 + 20, frame++, tab);
		setBounds(22508, 79, 11 + 20, frame++, tab);
		setBounds(22510, 116, 10 + 20, frame++, tab);
		setBounds(22512, 153, 9 + 20, frame++, tab);
		setBounds(22514, 5, 48 + 20, frame++, tab);
		setBounds(22516, 44, 47 + 20, frame++, tab);
		setBounds(22518, 79, 49 + 20, frame++, tab);
		setBounds(22520, 116, 50 + 20, frame++, tab);
		setBounds(22522, 154, 50 + 20, frame++, tab);
		setBounds(22524, 4, 84 + 20, frame++, tab);
		setBounds(22526, 44, 87 + 20, frame++, tab);
		setBounds(22528, 81, 85 + 20, frame++, tab);
		setBounds(22530, 117, 85 + 20, frame++, tab);
		setBounds(22532, 156, 87 + 20, frame++, tab);
		setBounds(22534, 5, 125 + 20, frame++, tab);
		setBounds(22536, 43, 124 + 20, frame++, tab);
		setBounds(22538, 83, 124 + 20, frame++, tab);
		setBounds(22540, 115, 121 + 20, frame++, tab);
		setBounds(22542, 154, 124 + 20, frame++, tab);
		setBounds(17229, 0, 25, frame++, tab);//Faded backing
		setBounds(17201, 0, 22, frame++, tab);//Split
		setBounds(17201, 0, 237, frame++, tab);//Split
		setBounds(17202, 5 - 3, 8 + 17, frame++, tab);
		setBounds(17203, 44 - 3, 8 + 17, frame++, tab);
		setBounds(17204, 79 - 3, 8 + 17, frame++, tab);
		setBounds(17205, 116 - 3, 8 + 17, frame++, tab);
		setBounds(17206, 153 - 3, 8 + 17, frame++, tab);
		setBounds(17207, 5 - 3, 48 + 17, frame++, tab);
		setBounds(17208, 44 - 3, 48 + 17, frame++, tab);
		setBounds(17209, 79 - 3, 48 + 17, frame++, tab);
		setBounds(17210, 116 - 3, 48 + 17, frame++, tab);
		setBounds(17211, 153 - 3, 48 + 17, frame++, tab);
		setBounds(17212, 5 - 3, 85 + 17, frame++, tab);
		setBounds(17213, 44 - 3, 85 + 17, frame++, tab);
		setBounds(17214, 79 - 3, 85 + 17, frame++, tab);
		setBounds(17215, 116 - 3, 85 + 17, frame++, tab);
		setBounds(17216, 153 - 3, 85 + 17, frame++, tab);
		setBounds(17217, 5 - 3, 124 + 17, frame++, tab);
		setBounds(17218, 44 - 3, 124 + 17, frame++, tab);
		setBounds(17219, 79 - 3, 124 + 17, frame++, tab);
		setBounds(17220, 116 - 3, 124 + 17, frame++, tab);
		setBounds(17221, 153 - 3, 124 + 17, frame++, tab);
		setBounds(17235, 5, 5, frame++, tab);//text
		setBounds(17231, 0, 237, frame++, tab);//confirm
		setBounds(17232, 0, 237, frame++, tab);//Confirm hover
	}

	public static void constructionHome() {
		RSInterface rsinterface = addInterface(12512, 512, 334);// house
		addSprite(12513, 0, "/Interfaces/Construction/home");
		addText(12514, "There's no place like home...", 0xFFFFFF, false, true,
				0, 2);
		setChildren(3, rsinterface);
		setBounds(13583, 0, 0, 0, rsinterface);
		setBounds(12513, 105, 30, 1, rsinterface);
		setBounds(12514, 155, 270, 2, rsinterface);
	}

	public static void godWars() {
		RSInterface rsinterface = addInterface(16210, 512, 334);
		addText(16211, "NPC killcount", 0, 0xff9040, false);
		addText(16212, "Armadyl kills", 0, 0xff9040, false);
		addText(16213, "Bandos kills", 0, 0xff9040, false);
		addText(16214, "Saradomin kills", 0, 0xff9040, false);
		addText(16215, "Zamorak kills", 0, 0xff9040, false);
		addText(16216, "0", 0, 0x66FFFF, false);// armadyl
		addText(16217, "0", 0, 0x66FFFF, false);// bandos
		addText(16218, "0", 0, 0x66FFFF, false);// saradomin
		addText(16219, "0", 0, 0x66FFFF, false);// zamorak
		rsinterface.scrollMax = 0;
		rsinterface.children = new int[9];
		rsinterface.childX = new int[9];
		rsinterface.childY = new int[9];
		rsinterface.children[0] = 16211;
		rsinterface.childX[0] = -52 + 375 + 30;
		rsinterface.childY[0] = 7;
		rsinterface.children[1] = 16212;
		rsinterface.childX[1] = -52 + 375 + 30;
		rsinterface.childY[1] = 30;
		rsinterface.children[2] = 16213;
		rsinterface.childX[2] = -52 + 375 + 30;
		rsinterface.childY[2] = 44;
		rsinterface.children[3] = 16214;
		rsinterface.childX[3] = -52 + 375 + 30;
		rsinterface.childY[3] = 58;
		rsinterface.children[4] = 16215;
		rsinterface.childX[4] = -52 + 375 + 30;
		rsinterface.childY[4] = 73;

		rsinterface.children[5] = 16216;
		rsinterface.childX[5] = -52 + 460 + 60;
		rsinterface.childY[5] = 31;
		rsinterface.children[6] = 16217;
		rsinterface.childX[6] = -52 + 460 + 60;
		rsinterface.childY[6] = 45;
		rsinterface.children[7] = 16218;
		rsinterface.childX[7] = -52 + 460 + 60;
		rsinterface.childY[7] = 59;
		rsinterface.children[8] = 16219;
		rsinterface.childX[8] = -52 + 460 + 60;
		rsinterface.childY[8] = 74;
	}

	public static void pestpanel() {
		RSInterface RSinterface = addInterface(21119, 512, 334);
		addText(21120, "Next Departure:", 0x999999, false, true, 52, 1);
		addText(21121, "Players Ready:", 0x33cc00, false, true, 52, 1);
		addText(21122, "(Need 5 to 25 players)", 0xFFcc33, false, true, 52,
				1);
		addText(21123, "Commendations:", 0x33ccff, false, true, 52, 1);
		int last = 4;
		RSinterface.children = new int[last];
		RSinterface.childX = new int[last];
		RSinterface.childY = new int[last];
		setBounds(21120, 15, 12, 0, RSinterface);
		setBounds(21121, 15, 30, 1, RSinterface);
		setBounds(21122, 15, 48, 2, RSinterface);
		setBounds(21123, 15, 66, 3, RSinterface);
	}

	public static void pestpanel2() {
		RSInterface RSinterface = addInterface(21100, 512, 334);
		addSprite(21101, 0, "Interfaces/Pest Control/PEST1");
		addSprite(21102, 1, "Interfaces/Pest Control/PEST1");
		addSprite(21103, 2, "Interfaces/Pest Control/PEST1");
		addSprite(21104, 3, "Interfaces/Pest Control/PEST1");
		addSprite(21105, 4, "Interfaces/Pest Control/PEST1");
		addSprite(21106, 5, "Interfaces/Pest Control/PEST1");
		addText(21107, "", 0xCC00CC, false, true, 52, 1);
		addText(21108, "", 0x0000FF, false, true, 52, 1);
		addText(21109, "", 0xFFFF44, false, true, 52, 1);
		addText(21110, "", 0xCC0000, false, true, 52, 1);
		addText(21111, "250", 0x99FF33, false, true, 52, 1);// w purp
		addText(21112, "250", 0x99FF33, false, true, 52, 1);// e blue
		addText(21113, "250", 0x99FF33, false, true, 52, 1);// se yel
		addText(21114, "250", 0x99FF33, false, true, 52, 1);// sw red
		addText(21115, "200", 0x99FF33, false, true, 52, 1);// attacks
		addText(21116, "0", 0x99FF33, false, true, 52, 1);// knights hp
		addText(21117, "Time Remaining:", 0xFFFFFF, false, true, 52, 0);
		addText(21118, "", 0xFFFFFF, false, true, 52, 0);
		int last = 18;
		RSinterface.children = new int[last];
		RSinterface.childX = new int[last];
		RSinterface.childY = new int[last];
		setBounds(21101, 361, 26, 0, RSinterface);
		setBounds(21102, 396, 26, 1, RSinterface);
		setBounds(21103, 436, 26, 2, RSinterface);
		setBounds(21104, 474, 26, 3, RSinterface);
		setBounds(21105, 3, 21, 4, RSinterface);
		setBounds(21106, 3, 50, 5, RSinterface);
		setBounds(21107, 371, 60, 6, RSinterface);
		setBounds(21108, 409, 60, 7, RSinterface);
		setBounds(21109, 443, 60, 8, RSinterface);
		setBounds(21110, 479, 60, 9, RSinterface);
		setBounds(21111, 362, 10, 10, RSinterface);
		setBounds(21112, 398, 10, 11, RSinterface);
		setBounds(21113, 436, 10, 12, RSinterface);
		setBounds(21114, 475, 10, 13, RSinterface);
		setBounds(21115, 32, 32, 14, RSinterface);
		setBounds(21116, 32, 62, 15, RSinterface);
		setBounds(21117, 8, 88, 16, RSinterface);
		setBounds(21118, 87, 88, 17, RSinterface);
	}

	public static void shopTab() {
		RSInterface rsinterface = addTabInterface(46000);
		addSprite(46001, 0, "shoptab");
		addInv(46002, 10, 2);
		addInv(46022, 10, 2);

		addText(46003, "0", 0, 0xffff00, false, false, 0);
		addText(46004, "0", 0, 0xffff00, false, false, 0);
		addText(46005, "0", 0, 0xffff00, false, false, 0);
		addText(46006, "0", 0, 0xffff00, false, false, 0);
		addText(46007, "0", 0, 0xffff00, false, false, 0);

		addText(46008, "0", 0, 0xffff00, false, false, 0);
		addText(46009, "0", 0, 0xffff00, false, false, 0);
		addText(46010, "0", 0, 0xffff00, false, false, 0);
		addText(46011, "0", 0, 0xffff00, false, false, 0);
		addText(46012, "0", 0, 0xffff00, false, false, 0);

		addText(46013, "0", 0, 0xffff00, false, false, 0);
		addText(46014, "0", 0, 0xffff00, false, false, 0);

		addHover(46015, 1, 0, 46016, 1, "Interfaces/Bank/BANK", 17, 17, "Close");
		addHovered(46016, 2, "Interfaces/Bank/BANK", 17, 17, 46667);
		addSprite(46017, 2, "shoptab");

		addHover(46018, 1, 0, 46019, 1, "Interfaces/SH", 60, 38, "Buy");
		addHovered(46019, 1, "shoptab", 60, 37, 46666);

		addText(46020, "Pink skirt", 0, 0xffffff, false, true, 0);
		addText(46021, "Cost:", 1, 0xffffff, false, true, 0);
		addText(46023, "Buy", 1, 0xffffff, false, true, 0);

		RSInterface inv3 = interfaceCache[46002];
		inv3.inv[0] = 1014;

		RSInterface inv2 = interfaceCache[46022];
		inv2.inv[0] = 996;

		/*
         * int amount = 0;
		 *
		 * RSInterface realinv = interfaceCache[3822]; if (realinv.inv != null)
		 * for (int i = 0; i < realinv.inv.length; i ++) if (realinv.inv[i] ==
		 * 995) amount = realinv.invStackSizes[i];
		 *
		 * System.out.println(""+amount);
		 */

		rsinterface.totalChildren(23);
		rsinterface.child(0, 46001, 0, 0);
		rsinterface.child(1, 46002, 12, 27);
		rsinterface.child(2, 46003, 19, 75);
		rsinterface.child(3, 46004, 19, 86);
		rsinterface.child(4, 46005, 19, 97);
		rsinterface.child(5, 46006, 19, 108);
		rsinterface.child(6, 46007, 19, 119);
		rsinterface.child(7, 46008, 175, 75);
		rsinterface.child(8, 46009, 175, 86);
		rsinterface.child(9, 46010, 175, 97);
		rsinterface.child(10, 46011, 175, 108);
		rsinterface.child(11, 46012, 175, 119);
		rsinterface.child(12, 46013, 175, 134);
		rsinterface.child(13, 46014, 175, 145);
		rsinterface.child(14, 46015, 173, 4);
		rsinterface.child(15, 46016, 173, 4);
		rsinterface.child(16, 46017, 21, 162);
		rsinterface.child(17, 46018, 127, 210);
		rsinterface.child(18, 46019, 127, 210);
		rsinterface.child(19, 46020, 60, 37);
		rsinterface.child(20, 46021, 33, 222);
		rsinterface.child(21, 46022, 83, 214);
		rsinterface.child(22, 46023, 146, 222);

	}

	public static void addInv(int id, int h, int w) {
		RSInterface Tab = addInterface(id, 512, 334);
		Tab.inv = new int[w * h];
		Tab.invStackSizes = new int[w * h];
		for (int i1 = 0; i1 < w * h; i1++) {
			Tab.invStackSizes[i1] = 1;
			Tab.inv[i1] = 0;
		}
		Tab.invSpritePadX = 177;
		Tab.invSpritePadY = 14;
		Tab.width = (short) w;
		Tab.mOverInterToTrigger = -1;
		Tab.parentID = id;
		Tab.id = id;
		Tab.scrollMax = 0;
		Tab.type = 2;
		Tab.height = (short) h;
	}

	public static void ancientMagicTab() {
		RSInterface tab = addInterface(12855, 512, 334);

		tab.totalChildren(itfChildren.length);

		int x = 18;
		int y = 8;
		for (int i = 0; i < itfChildren.length; i++) {
			if (i < 25) {
				tab.child(i, itfChildren[i], x, y);
				x += 45;
				if (x > 181) {
					x = 18;
					y += 28;
				}
			} else {
				y = i < 41 ? 181 : 1;
				tab.child(i, itfChildren[i], 4, y);

				RSInterface hover = interfaceCache[itfChildren[i]];

				int[] childID = hover.children;
				int[] childX = hover.childX;
				int[] childY = hover.childY;

				setChildren(childID.length + 2, hover);

				hover.child(0, 1196, 3, 4);
				hover.child(1, 1197, 3, 4);
				for (int a = 0; a < childID.length; a++) {
					hover.child(a + 2, childID[a], childX[a], childY[a]);
				}
			}
		}
	}

	public static void drawItemModel(int frameId, int itemId, int zoom) {
		RSInterface rune = addInterface(frameId, 28, 28);
		rune.mediaID = itemId;
		rune.mediaType = 4;
		rune.rotationX = 512;
		rune.rotationY = 1024;
		rune.modelZoom = zoom;
		rune.type = 6;
	}

	public static void emoteTab() {
		RSInterface tab = addTabInterface(147);
		RSInterface scroll = addTabInterface(148);
		tab.totalChildren(1);
		tab.child(0, 148, 0, 1);
		addButton(168, 0, "Interfaces/Emotes/EMOTE", "Yes", 41, 47);
		addButton(169, 1, "Interfaces/Emotes/EMOTE", "No", 41, 47);
		addButton(164, 2, "Interfaces/Emotes/EMOTE", "Bow", 41, 47);
		addButton(165, 3, "Interfaces/Emotes/EMOTE", "Angry", 41, 47);
		addButton(162, 4, "Interfaces/Emotes/EMOTE", "Think", 41, 47);
		addButton(163, 5, "Interfaces/Emotes/EMOTE", "Wave", 41, 47);
		addButton(13370, 6, "Interfaces/Emotes/EMOTE", "Shrug", 41, 47);
		addButton(171, 7, "Interfaces/Emotes/EMOTE", "Cheer", 41, 47);
		addButton(167, 8, "Interfaces/Emotes/EMOTE", "Beckon", 41, 47);
		addButton(170, 9, "Interfaces/Emotes/EMOTE", "Laugh", 41, 47);
		addButton(13366, 10, "Interfaces/Emotes/EMOTE", "Jump for Joy", 41, 47);
		addButton(13368, 11, "Interfaces/Emotes/EMOTE", "Yawn", 41, 47);
		addButton(166, 12, "Interfaces/Emotes/EMOTE", "Dance", 41, 47);
		addButton(13363, 13, "Interfaces/Emotes/EMOTE", "Jig", 41, 47);
		addButton(13364, 14, "Interfaces/Emotes/EMOTE", "Spin", 41, 47);
		addButton(13365, 15, "Interfaces/Emotes/EMOTE", "Headbang", 41, 47);
		addButton(161, 16, "Interfaces/Emotes/EMOTE", "Cry", 41, 47);
		addButton(11100, 17, "Interfaces/Emotes/EMOTE", "Blow kiss", 41, 47);
		addButton(13362, 18, "Interfaces/Emotes/EMOTE", "Panic", 41, 47);
		addButton(13367, 19, "Interfaces/Emotes/EMOTE", "Raspberry", 41, 47);
		addButton(172, 20, "Interfaces/Emotes/EMOTE", "Clap", 41, 47);
		addButton(13369, 21, "Interfaces/Emotes/EMOTE", "Salute", 41, 47);
		addButton(13383, 22, "Interfaces/Emotes/EMOTE", "Goblin Bow", 41, 47);
		addButton(13384, 23, "Interfaces/Emotes/EMOTE", "Goblin Salute", 41, 47);
		addButton(667, 24, "Interfaces/Emotes/EMOTE", "Glass Box", 41, 47);
		addButton(6503, 25, "Interfaces/Emotes/EMOTE", "Climb Rope", 41, 47);
		addButton(6506, 26, "Interfaces/Emotes/EMOTE", "Lean On Air", 41, 47);
		addButton(666, 27, "Interfaces/Emotes/EMOTE", "Glass Wall", 41, 47);
		addButton(18464, 28, "Interfaces/Emotes/EMOTE", "Zombie Walk", 41, 47);
		addButton(18465, 29, "Interfaces/Emotes/EMOTE", "Zombie Dance", 41, 47);
		addButton(15166, 30, "Interfaces/Emotes/EMOTE", "Scared", 41, 47);
		addButton(18686, 32, "Interfaces/Emotes/EMOTE", "Skillcape Emote", 41,
				47);
		addConfigButton(154, 147, 32, 33, "EMOTE", 41, 47, "Skillcape Emote",
				0, 1, 700);
		scroll.totalChildren(33);
		scroll.child(0, 168, 10, 7);
		scroll.child(1, 169, 54, 7);
		scroll.child(2, 164, 98, 14);
		scroll.child(3, 165, 137, 7);
		scroll.child(4, 162, 9, 56);
		scroll.child(5, 163, 48, 56);
		scroll.child(6, 13370, 95, 56);
		scroll.child(7, 171, 137, 56);
		scroll.child(8, 167, 7, 105);
		scroll.child(9, 170, 51, 105);
		scroll.child(10, 13366, 95, 104);
		scroll.child(11, 13368, 139, 105);
		scroll.child(12, 166, 6, 154);
		scroll.child(13, 13363, 50, 154);
		scroll.child(14, 13364, 90, 154);
		scroll.child(15, 13365, 135, 154);
		scroll.child(16, 161, 8, 204);
		scroll.child(17, 11100, 51, 203);
		scroll.child(18, 13362, 99, 204);
		scroll.child(19, 13367, 137, 203);
		scroll.child(20, 172, 10, 253);
		scroll.child(21, 13369, 53, 253);
		scroll.child(22, 13383, 88, 258);
		scroll.child(23, 13384, 138, 252);
		scroll.child(24, 667, 2, 303);
		scroll.child(25, 6503, 49, 302);
		scroll.child(26, 6506, 93, 302);
		scroll.child(27, 666, 137, 302);
		scroll.child(28, 18464, 9, 352);
		scroll.child(29, 18465, 50, 352);
		scroll.child(30, 15166, 94, 356);
		scroll.child(31, 18686, 132, 353);
		scroll.child(32, 154, 5, 401);
		scroll.width = 173;
		scroll.height = 258;
		scroll.scrollMax = 403;
	}

	public static void itemsOnDeath() {
		RSInterface rsinterface = addInterface(17100, 512, 334);
		addSprite(17101, 2, "Interfaces/Equipment/SPRITE");
		addHover(17102, 3, 0, 10601, 1, "Interfaces/Equipment/SPRITE", 17, 17,
				"Close Window");
		addHovered(10601, 3, "Interfaces/Equipment/SPRITE", 17, 17, 10602);
		addText(17103, "Items Kept On Death", 0xff981f, false, true, 0, 2);
		addText(17104, "Items you will keep on death (if not skulled):",
				0xff981f, false, true, 0, 2);
		addText(17105, "Items you will lose on death (if not skulled):",
				0xff981f, false, true, 0, 2);
		addText(17106, "Information", 0xff981f, false, true, 0, 1);
		addText(17107, "Max items kept on death:", 0xff981f, false, true, 0,
				1);
		addText(17108, "~ 3 ~", 0xffcc33, false, true, 0, 1);
		rsinterface.scrollMax = 0;
		rsinterface.children = new int[12];
		rsinterface.childX = new int[12];
		rsinterface.childY = new int[12];

		rsinterface.children[0] = 17101;
		rsinterface.childX[0] = 7;
		rsinterface.childY[0] = 8;
		rsinterface.children[1] = 17102;
		rsinterface.childX[1] = 480;
		rsinterface.childY[1] = 17;
		rsinterface.children[2] = 17103;
		rsinterface.childX[2] = 185;
		rsinterface.childY[2] = 18;
		rsinterface.children[3] = 17104;
		rsinterface.childX[3] = 22;
		rsinterface.childY[3] = 50;
		rsinterface.children[4] = 17105;
		rsinterface.childX[4] = 22;
		rsinterface.childY[4] = 110;
		rsinterface.children[5] = 17106;
		rsinterface.childX[5] = 347;
		rsinterface.childY[5] = 47;
		rsinterface.children[6] = 17107;
		rsinterface.childX[6] = 349;
		rsinterface.childY[6] = 270;
		rsinterface.children[7] = 17108;
		rsinterface.childX[7] = 398;
		rsinterface.childY[7] = 298;
		rsinterface.children[8] = 17115;
		rsinterface.childX[8] = 348;
		rsinterface.childY[8] = 64;
		rsinterface.children[9] = 10494;
		rsinterface.childX[9] = 26;
		rsinterface.childY[9] = 74;
		rsinterface.children[10] = 10600;
		rsinterface.childX[10] = 26;
		rsinterface.childY[10] = 133;
		rsinterface.children[11] = 10601;
		rsinterface.childX[11] = 480;
		rsinterface.childY[11] = 17;
		rsinterface = interfaceCache[10494];
		rsinterface.invSpritePadX = 6;
		rsinterface.invSpritePadY = 5;
		rsinterface = interfaceCache[10600];
		rsinterface.invSpritePadX = 6;
		rsinterface.invSpritePadY = 5;
	}

	public static void itemsOnDeathDATA() {
		RSInterface RSinterface = addInterface(17115, 512, 334);
		addText(17109, "", 0xff981f, false, false, 0, 0);
		addText(17110, "The normal amount of", 0xff981f, false, false, 0, 0);
		addText(17111, "items kept is three.", 0xff981f, false, false, 0, 0);
		addText(17112, "", 0xff981f, false, false, 0, 0);
		addText(17113, "If you are skulled,", 0xff981f, false, false, 0, 0);
		addText(17114, "you will lose all your", 0xff981f, false, false, 0,
				0);
		addText(17117, "items, unless an item", 0xff981f, false, false, 0, 0);
		addText(17118, "protecting prayer is", 0xff981f, false, false, 0, 0);
		addText(17119, "used.", 0xff981f, false, false, 0, 0);
		addText(17120, "", 0xff981f, false, false, 0, 0);
		addText(17121, "Item protecting prayers", 0xff981f, false, false, 0,
				0);
		addText(17122, "will allow you to keep", 0xff981f, false, false, 0,
				0);
		addText(17123, "one extra item.", 0xff981f, false, false, 0, 0);
		addText(17124, "", 0xff981f, false, false, 0, 0);
		addText(17125, "The items kept are", 0xff981f, false, false, 0, 0);
		addText(17126, "selected by the server", 0xff981f, false, false, 0,
				0);
		addText(17127, "and include the most", 0xff981f, false, false, 0, 0);
		addText(17128, "expensive items you're", 0xff981f, false, false, 0,
				0);
		addText(17129, "carrying.", 0xff981f, false, false, 0, 0);
		addText(17130, "", 0xff981f, false, false, 0, 0);
		RSinterface.parentID = 17115;
		RSinterface.id = 17115;
		RSinterface.type = 0;
		RSinterface.atActionType = 0;
		RSinterface.contentType = 0;
		RSinterface.width = 130;
		RSinterface.height = 197;
		RSinterface.opacity = 0;
		RSinterface.scrollMax = 280;
		RSinterface.children = new int[20];
		RSinterface.childX = new int[20];
		RSinterface.childY = new int[20];
		RSinterface.children[0] = 17109;
		RSinterface.childX[0] = 0;
		RSinterface.childY[0] = 0;
		RSinterface.children[1] = 17110;
		RSinterface.childX[1] = 0;
		RSinterface.childY[1] = 12;
		RSinterface.children[2] = 17111;
		RSinterface.childX[2] = 0;
		RSinterface.childY[2] = 24;
		RSinterface.children[3] = 17112;
		RSinterface.childX[3] = 0;
		RSinterface.childY[3] = 36;
		RSinterface.children[4] = 17113;
		RSinterface.childX[4] = 0;
		RSinterface.childY[4] = 48;
		RSinterface.children[5] = 17114;
		RSinterface.childX[5] = 0;
		RSinterface.childY[5] = 60;
		RSinterface.children[6] = 17117;
		RSinterface.childX[6] = 0;
		RSinterface.childY[6] = 72;
		RSinterface.children[7] = 17118;
		RSinterface.childX[7] = 0;
		RSinterface.childY[7] = 84;
		RSinterface.children[8] = 17119;
		RSinterface.childX[8] = 0;
		RSinterface.childY[8] = 96;
		RSinterface.children[9] = 17120;
		RSinterface.childX[9] = 0;
		RSinterface.childY[9] = 108;
		RSinterface.children[10] = 17121;
		RSinterface.childX[10] = 0;
		RSinterface.childY[10] = 120;
		RSinterface.children[11] = 17122;
		RSinterface.childX[11] = 0;
		RSinterface.childY[11] = 132;
		RSinterface.children[12] = 17123;
		RSinterface.childX[12] = 0;
		RSinterface.childY[12] = 144;
		RSinterface.children[13] = 17124;
		RSinterface.childX[13] = 0;
		RSinterface.childY[13] = 156;
		RSinterface.children[14] = 17125;
		RSinterface.childX[14] = 0;
		RSinterface.childY[14] = 168;
		RSinterface.children[15] = 17126;
		RSinterface.childX[15] = 0;
		RSinterface.childY[15] = 180;
		RSinterface.children[16] = 17127;
		RSinterface.childX[16] = 0;
		RSinterface.childY[16] = 192;
		RSinterface.children[17] = 17128;
		RSinterface.childX[17] = 0;
		RSinterface.childY[17] = 204;
		RSinterface.children[18] = 17129;
		RSinterface.childX[18] = 0;
		RSinterface.childY[18] = 216;
		RSinterface.children[19] = 17130;
		RSinterface.childX[19] = 0;
		RSinterface.childY[19] = 228;
	}

	public static void equipmentScreen() {
		RSInterface tab = addTabInterface(15106);
		addSprite(15107, 7, "Interfaces/Equipment/CUSTOM");
		addHoverButton(15210, "Interfaces/Equipment/CUSTOM", 8, 21, 21,
				"Close", 250, 15211, 3, -1);
		addHoveredButton(15211, "Interfaces/Equipment/CUSTOM", 9, 21, 21, 15212);
		addText(15111, "Equip Your Character...", 2, 0xe4a146, false, true, 0);
		addText(15112, "Attack bonus", 2, 0xe4a146, false, true, 0);
		addText(15113, "Defence bonus", 2, 0xe4a146, false, true, 0);
		addText(15114, "Other bonuses", 2, 0xe4a146, false, true, 0);
		for (int i = 1675; i <= 1684; i++) {
			textSize(i, 1);
		}
		textSize(1686, 1);
		textSize(1687, 1);
		addChar(15125, 510, 328);
		tab.totalChildren(44);
		tab.child(0, 15107, 4, 20);
		tab.child(1, 15210, 476, 27);
		tab.child(2, 15211, 476, 27);
		tab.child(3, 15111, 14, 30);
		int Child = 4;
		int Y = 69;
		for (int i = 1675; i <= 1679; i++) {
			tab.child(Child, i, 20, Y);
			Child++;
			Y += 14;
		}
		tab.child(9, 1680, 20, 161);
		tab.child(10, 1681, 20, 177);
		tab.child(11, 1682, 20, 192);
		tab.child(12, 1683, 20, 207);
		tab.child(13, 1684, 20, 221);
		tab.child(14, 1686, 20, 262);
		tab.child(17, 1687, 20, 276);
		tab.child(15, 15125, 170, 200);
		tab.child(16, 15112, 16, 55);
		tab.child(18, 15113, 16, 147);
		tab.child(19, 15114, 16, 248);
		tab.child(20, 1645, 104 + 295, 149 - 52);
		tab.child(21, 1646, 399, 163);
		tab.child(22, 1647, 399, 163);
		tab.child(23, 1648, 399, 58 + 146);
		tab.child(24, 1649, 26 + 22 + 297 - 2, 110 - 44 + 118 - 13 + 5);
		tab.child(25, 1650, 321 + 22, 58 + 154);
		tab.child(26, 1651, 321 + 134, 58 + 118);
		tab.child(27, 1652, 321 + 134, 58 + 154);
		tab.child(28, 1653, 321 + 48, 58 + 81);
		tab.child(29, 1654, 321 + 107, 58 + 81);
		tab.child(30, 1655, 321 + 58, 58 + 42);
		tab.child(31, 1656, 321 + 112, 58 + 41);
		tab.child(32, 1657, 321 + 78, 58 + 4);
		tab.child(33, 1658, 321 + 37, 58 + 43);
		tab.child(34, 1659, 321 + 78, 58 + 43);
		tab.child(35, 1660, 321 + 119, 58 + 43);
		tab.child(36, 1661, 321 + 22, 58 + 82);
		tab.child(37, 1662, 321 + 78, 58 + 82);
		tab.child(38, 1663, 321 + 134, 58 + 82);
		tab.child(39, 1664, 321 + 78, 58 + 122);
		tab.child(40, 1665, 321 + 78, 58 + 162);
		tab.child(41, 1666, 321 + 22, 58 + 162);
		tab.child(42, 1667, 321 + 134, 58 + 162);
		tab.child(43, 1688, 50 + 297 - 2, 110 - 13 + 5);
		for (int i = 1675; i <= 1684; i++) {
			RSInterface rsi = interfaceCache[i];
			rsi.textColor = 0xe4a146;
			rsi.centerText = false;
		}
		for (int i = 1686; i <= 1687; i++) {
			RSInterface rsi = interfaceCache[i];
			rsi.textColor = 0xe4a146;
			rsi.centerText = false;
		}
	}

	public static void curses() {
		RSInterface Interface = addTabInterface(22500);
		int index = 0;
		setChildren(62, Interface);
		RSInterface prayerInterface = addText(687, "99/99", 0xFF981F, false, false, -1, 1);
		prayerInterface.valueIndexArray = new int[2][];
		prayerInterface.valueIndexArray[0] = new int[3];
		prayerInterface.valueIndexArray[0][0] = 1;
		prayerInterface.valueIndexArray[0][1] = 5;
		prayerInterface.valueIndexArray[0][2] = 0;
		prayerInterface.valueIndexArray[1] = new int[3];
		prayerInterface.valueIndexArray[1][0] = 2;
		prayerInterface.valueIndexArray[1][1] = 5;
		prayerInterface.valueIndexArray[1][2] = 0;
		prayerInterface.message = "%1 / %2";
		/*Top Row*/
		addPrayer(22503, 0, 83, 49, 7, "Protect Item", 22582);
		setBounds(22503, 2, 5, index, Interface);
		index++;//Glow
		setBounds(22504, 8, 8, index, Interface);
		index++;//Icon
		addPrayer(22505, 0, 84, 49, 4, "Sap Warrior", 22544);
		setBounds(22505, 40, 5, index, Interface);
		index++;//Glow
		setBounds(22506, 47, 12, index, Interface);
		index++;//Icon
		addPrayer(22507, 0, 85, 51, 5, "Sap Ranger", 22546);
		setBounds(22507, 76, 5, index, Interface);
		index++;//Glow
		setBounds(22508, 82, 11, index, Interface);
		index++;//Icon
		addPrayer(22509, 0, 101, 53, 3, "Sap Mage", 22548);
		setBounds(22509, 113, 5, index, Interface);
		index++;//Glow
		setBounds(22510, 116, 8, index, Interface);
		index++;//Icon
		addPrayer(22511, 0, 102, 55, 2, "Sap Spirit", 22550);
		setBounds(22511, 150, 5, index, Interface);
		index++;//Glow
		setBounds(22512, 155, 10, index, Interface);
		index++;//Icon
		/*2nd Row*/
		addPrayer(22513, 0, 86, 58, 18, "Berserker", 22552);
		setBounds(22513, 2, 45, index, Interface);
		index++;//Glow
		setBounds(22514, 9, 48, index, Interface);
		index++;//Icon
		addPrayer(22515, 0, 87, 61, 15, "Deflect Summoning", 22554);
		setBounds(22515, 39, 45, index, Interface);
		index++;//Glow
		setBounds(22516, 42, 47, index, Interface);
		index++;//Icon
		addPrayer(22517, 0, 88, 64, 17, "Deflect Magic", 22556);
		setBounds(22517, 76, 45, index, Interface);
		index++;//Glow
		setBounds(22518, 79, 48, index, Interface);
		index++;//Icon
		addPrayer(22519, 0, 89, 67, 16, "Deflect Missiles", 22558);
		setBounds(22519, 113, 45, index, Interface);
		index++;//Glow
		setBounds(22520, 116, 48, index, Interface);
		index++;//Icon
		addPrayer(22521, 0, 90, 70, 6, "Deflect Melee", 22560);
		setBounds(22521, 151, 45, index, Interface);
		index++;//Glow
		setBounds(22522, 154, 48, index, Interface);
		index++;//Icon
		/*3rd Row*/
		addPrayer(22523, 0, 91, 73, 9, "Leech Attack", 22562);
		setBounds(22523, 2, 82, index, Interface);
		index++;//Glow
		setBounds(22524, 6, 86, index, Interface);
		index++;//Icon
		addPrayer(22525, 0, 103, 75, 10, "Leech Ranged", 22564);
		setBounds(22525, 40, 82, index, Interface);
		index++;//Glow
		setBounds(22526, 42, 86, index, Interface);
		index++;//Icon
		addPrayer(22527, 0, 104, 77, 11, "Leech Magic", 22566);
		setBounds(22527, 77, 82, index, Interface);
		index++;//Glow
		setBounds(22528, 79, 86, index, Interface);
		index++;//Icon
		addPrayer(22529, 0, 92, 79, 12, "Leech Defence", 22568);
		setBounds(22529, 114, 83, index, Interface);
		index++;//Glow
		setBounds(22530, 119, 87, index, Interface);
		index++;//Icon
		addPrayer(22531, 0, 93, 81, 13, "Leech Strength", 22570);
		setBounds(22531, 153, 83, index, Interface);
		index++;//Glow
		setBounds(22532, 156, 86, index, Interface);
		index++;//Icon
		/*Bottom Row*/
		addPrayer(22533, 0, 94, 83, 14, "Leech Energy", 22572);
		setBounds(22533, 2, 120, index, Interface);
		index++;//Glow
		setBounds(22534, 7, 125, index, Interface);
		index++;//Icon
		addPrayer(22535, 0, 95, 85, 19, "Leech Special Attack", 22574);
		setBounds(22535, 40, 120, index, Interface);
		index++;//Glow
		setBounds(22536, 45, 124, index, Interface);
		index++;//Icon
		addPrayer(22537, 0, 96, 88, 1, "Wrath", 22576);
		setBounds(22537, 78, 120, index, Interface);
		index++;//Glow
		setBounds(22538, 86, 124, index, Interface);
		index++;//Icon
		addPrayer(22539, 0, 97, 91, 8, "Soul Split", 22578);
		setBounds(22539, 114, 120, index, Interface);
		index++;//Glow
		setBounds(22540, 120, 125, index, Interface);
		index++;//Icon
		addPrayer(22541, 0, 105, 94, 20, "Turmoil", 22580);
		setBounds(22541, 151, 120, index, Interface);
		index++;//Glow
		setBounds(22542, 153, 127, index, Interface);
		index++;//Icon
		/*Prayer Icon/Text*/
		addSprite(22502, 0, "Curses/ICON");
		setBounds(687, 85, 241, index, Interface);
		index++;//Text
		setBounds(22502, 65, 241, index, Interface);
		index++;//Icon
		/*Tooltips/Hover Boxes*/
		addTooltip(22582, "Level 50\nProtect Item\nKeep 1 extra item if you die", 100, 185);
		addTooltip(22544, "Level 50\nSap Warrior\nDrains 10% of enemy Attack,\nStrength and Defence,\nincreasing to 20% over time", 100, 185);
		addTooltip(22546, "Level 52\nSap Ranger\nDrains 10% of enemy Ranged\nand Defence, increasing to 20%\nover time", 100, 185);
		addTooltip(22548, "Level 54\nSap Mage\nDrains 10% of enemy Magic\nand Defence, increasing to 20%\nover time", 100, 185);
		addTooltip(22550, "Level 56\nSap Spirit\nDrains enenmy special attack\nenergy", 100, 185);
		addTooltip(22552, "Level 59\nBerserker\nBoosted stats last 15% longer", 100, 185);
		addTooltip(22554, "Level 62\nDeflect Summoning\nReduces damage dealt from\nSummoning scrolls, prevents the\nuse of a familiar's special\nattack, and can deflect some of\ndamage back to the attacker", 125, 185);
		addTooltip(22556, "Level 65\nDeflect Magic\nProtects against magical attacks\nand can deflect some of the\ndamage back to the attacker", 100, 185);
		addTooltip(22558, "Level 68\nDeflect Missiles\nProtects against ranged attacks\nand can deflect some of the\ndamage back to the attacker", 100, 185);
		addTooltip(22560, "Level 71\nDeflect Melee\nProtects against melee attacks\nand can deflect some of the\ndamage back to the attacker", 100, 185);
		addTooltip(22562, "Level 74\nLeech Attack\nBoosts Attack by 5%, increasing\nto 10% over time, while draining\nenemy Attack by 10%,\nincreasing to 25% over time", 100, 185);
		addTooltip(22564, "Level 76\nLeech Ranged\nBoosts Ranged by 5%, increasing\nto 10% over time,\nwhile draining enemy Ranged by\n10%, increasing to 25% over\ntime", 113, 185);
		addTooltip(22566, "Level 78\nLeech Magic\nBoosts Magic by 5%, increasing\nto 10% over time, while draining\nenemy Magic by 10%, increasing\nto 25% over time", 100, 185);
		addTooltip(22568, "Level 80\nLeech Defence\nBoosts Defence by 5%, increasing\nto 10% over time,\nwhile draining enemy Defence by\n10%, increasing to 25% over\ntime", 113, 185);
		addTooltip(22570, "Level 82\nLeech Strength\nBoosts Strength by 5%, increasing\nto 10% over time,\nwhile draining enemy Strength by\n10%, increasing to 25% over\ntime", 113, 185);
		addTooltip(22572, "Level 84\nLeech Energy\nDrains enemy run energy, while\nincreasing your own", 113, 185);
		addTooltip(22574, "Level 86\nLeech Special Attack\nDrains enemy special attack\nenergy, while increasing your\nown", 113, 185);
		addTooltip(22576, "Level 89\nWrath\nInflicts damage to nearby\ntargets if you die", 113, 185);
		addTooltip(22578, "Level 92\nSoul Split\n1/4 of damage dealt is\nalso removed from\nopponent's Prayer and added to\nyour Hitpoints", 113, 185);
		addTooltip(22580, "Level 95\nTurmoil\nIncreases Attack and Defence\nby 15%, plus 15% of enemy's\nlevel, and Strength by 23% plus\n10% of enemy's level", 113, 185);
		setBounds(22582, 10, 40, index, Interface);
		index++;
		setBounds(22544, 20, 40, index, Interface);
		index++;
		setBounds(22546, 20, 40, index, Interface);
		index++;
		setBounds(22548, 20, 40, index, Interface);
		index++;
		setBounds(22550, 20, 40, index, Interface);
		index++;
		setBounds(22552, 10, 80, index, Interface);
		index++;
		setBounds(22554, 10, 80, index, Interface);
		index++;
		setBounds(22556, 10, 80, index, Interface);
		index++;
		setBounds(22558, 10, 80, index, Interface);
		index++;
		setBounds(22560, 10, 80, index, Interface);
		index++;
		setBounds(22562, 10, 120, index, Interface);
		index++;
		setBounds(22564, 10, 120, index, Interface);
		index++;
		setBounds(22566, 10, 120, index, Interface);
		index++;
		setBounds(22568, 5, 120, index, Interface);
		index++;
		setBounds(22570, 5, 120, index, Interface);
		index++;
		setBounds(22572, 10, 160, index, Interface);
		index++;
		setBounds(22574, 10, 160, index, Interface);
		index++;
		setBounds(22576, 10, 160, index, Interface);
		index++;
		setBounds(22578, 10, 160, index, Interface);
		index++;
		setBounds(22580, 10, 160, index, Interface);
		index++;
	}

	public static void addTooltip(int id, String text, int H, int W) {//This needs to be removed when curses are changed
		RSInterface rsi = addTabInterface(id);
		rsi.id = id;
		rsi.type = 0;
		rsi.isMouseoverTriggered = true;
		rsi.mOverInterToTrigger = -1;
		addTooltipBox(id + 1, text);
		rsi.totalChildren(1);
		rsi.child(0, id + 1, 0, 0);
		rsi.height = H;
		rsi.width = W;
	}

	public static void prayerTab() {
		RSInterface tab = addTabInterface(5608);
		RSInterface currentPray = interfaceCache[687];
		addSprite(5651, 0, "Interfaces/Prayer/PRAYER");
		currentPray.valueIndexArray = new int[2][];
		currentPray.valueIndexArray[0] = new int[3];
		currentPray.valueIndexArray[0][0] = 1;
		currentPray.valueIndexArray[0][1] = 5;
		currentPray.valueIndexArray[0][2] = 0;
		currentPray.valueIndexArray[1] = new int[3];
		currentPray.valueIndexArray[1][0] = 2;
		currentPray.valueIndexArray[1][1] = 5;
		currentPray.valueIndexArray[1][2] = 0;
		currentPray.message = "%1 / %2";
		currentPray.textColor = 0xFF981F;
		currentPray.textShadow = true;
		addPrayer(16500, 0, 601, 7, 0, "Sharp Eye");
		addPrayer(16502, 0, 602, 8, 1, "Mystic Will");
		addPrayer(16504, 0, 603, 25, 2, "Hawk Eye");
		addPrayer(16506, 0, 604, 26, 3, "Mystic Lore");
		addPrayer(16508, 0, 605, 43, 4, "Eagle Eye");
		addPrayer(16510, 0, 606, 44, 5, "Mystic Might");
		addPrayer(16512, 0, 607, 59, 6, "Chivalry");
		addPrayer(16514, 0, 608, 69, 7, "Piety");

		for (int i = 0; i < oldPrayerNames.length; i++)
			addOldPrayer(ID2[i], oldPrayerNames[i]);

		for (int i = 0; i < hoverIDs.length; i++)
			addPrayerHover(ID1[i], hoverIDs[i], i, hoverStrings[i]);

		tab.totalChildren(106); // 54
		tab.child(52, 5651, 65, 240);
		for (int ii = 0; ii < 54; ii++) {
			tab.child(ii, ID2[ii], X2[ii], Y2[ii]);
		}

		int frame = 54;
		int frame2 = 0;
		for (int i : ID1) {
			tab.child(frame, i, X[frame2], Y[frame2]);
			frame++;
			frame2++;
		}

		int frame3 = 0;
		for (int i : hoverIDs) {
			tab.child(frame, i, hoverX[frame3], hoverY[frame3]);
			frame++;
			frame3++;
		}
	}

	/**
	 * Adds your current character to an interface.
	 */

	protected static void addOldPrayer(int id, String prayerName) {
		RSInterface rsi = interfaceCache[id];
		rsi.tooltip = "Activate<col=ff7000> " + prayerName;
	}

	public static void addPrayerHover(int i, int hoverID, int prayerSpriteID,
									  String hoverText) {
		RSInterface Interface = addTabInterface(i);
		Interface.id = i;
		Interface.parentID = 5608;
		Interface.type = 5;
		Interface.atActionType = 0;
		Interface.contentType = 0;
		Interface.opacity = 0;
		Interface.mOverInterToTrigger = hoverID;
		Interface.sprite1 = imageLoader(0, "tabs/prayer/hover/PRAYERH");
		Interface.width = 34;
		Interface.height = 34;

		Interface = addTabInterface(hoverID);
		Interface.id = hoverID;
		Interface.parentID = 5608;
		Interface.type = 0;
		Interface.atActionType = 0;
		Interface.contentType = 0;
		Interface.opacity = 0;
		Interface.mOverInterToTrigger = -1;
		Interface.width = 512;
		Interface.height = 334;
		Interface.isMouseoverTriggered = true;
		addBox(hoverID + 1, 0, false, 0x000000, hoverText);
		setChildren(1, Interface);
		setBounds(hoverID + 1, 0, 0, 0, Interface);
	}

	public static void addPrayer(int i, int configId, int configFrame, int requiredValues, int prayerSpriteID, String PrayerName, int Hover) {
		RSInterface Interface = addTabInterface(i);
		Interface.id = i;
		Interface.parentID = 22500;
		Interface.type = 5;
		Interface.atActionType = 4;
		Interface.contentType = 0;
		Interface.opacity = 0;
		Interface.mOverInterToTrigger = Hover;
		Interface.sprite1 = imageLoader(0, "Curses/GLOW");
		Interface.sprite2 = imageLoader(1, "Curses/GLOW");
		Interface.width = 34;
		Interface.height = 34;
		Interface.valueCompareType = new int[1];
		Interface.requiredValues = new int[1];
		Interface.valueCompareType[0] = 1;
		Interface.requiredValues[0] = configId;
		Interface.valueIndexArray = new int[1][3];
		Interface.valueIndexArray[0][0] = 5;
		Interface.valueIndexArray[0][1] = configFrame;
		Interface.valueIndexArray[0][2] = 0;
		Interface.tooltip = "Activate<col=ffb000> " + PrayerName;
		Interface = addTabInterface(i + 1);
		Interface.id = i + 1;
		Interface.parentID = 22500;
		Interface.type = 5;
		Interface.atActionType = 0;
		Interface.contentType = 0;
		Interface.opacity = 0;
		Interface.sprite1 = imageLoader(prayerSpriteID, "Curses/PRAYON");
		Interface.sprite2 = imageLoader(prayerSpriteID, "Curses/PRAYOFF");
		Interface.width = 34;
		Interface.height = 34;
		Interface.valueCompareType = new int[1];
		Interface.requiredValues = new int[1];
		Interface.valueCompareType[0] = 2;
		Interface.requiredValues[0] = requiredValues + 1;
		Interface.valueIndexArray = new int[1][3];
		Interface.valueIndexArray[0][0] = 2;
		Interface.valueIndexArray[0][1] = 5;
		Interface.valueIndexArray[0][2] = 0;
	}

	public static void addPrayer(int i, int configId, int configFrame,
								 int anIntArray212, int spriteID, String prayerName) {
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = 5608;
		tab.type = 5;
		tab.atActionType = 4;
		tab.contentType = 0;
		tab.opacity = 0;
		tab.mOverInterToTrigger = -1;
		tab.sprite1 = imageLoader(0, "PRAYERGLOW");
		tab.sprite2 = imageLoader(1, "PRAYERGLOW");
		tab.width = 34;
		tab.height = 34;
		tab.valueCompareType = new int[1];
		tab.requiredValues = new int[1];
		tab.valueCompareType[0] = 1;
		tab.requiredValues[0] = configId;
		tab.valueIndexArray = new int[1][3];
		tab.valueIndexArray[0][0] = 5;
		tab.valueIndexArray[0][1] = configFrame;
		tab.valueIndexArray[0][2] = 0;
		tab.tooltip = "Activate<col=ff7000> " + prayerName;
		// tab.tooltip = "Select";
		RSInterface tab2 = addTabInterface(i + 1);
		tab2.id = i + 1;
		tab2.parentID = 5608;
		tab2.type = 5;
		tab2.atActionType = 0;
		tab2.contentType = 0;
		tab2.opacity = 0;
		tab2.mOverInterToTrigger = -1;
		tab2.sprite1 = imageLoader(spriteID, "Interfaces/PRAYER/PRAYON");
		tab2.sprite2 = imageLoader(spriteID, "Interfaces/PRAYER/PRAYOFF");
		tab2.width = 34;
		tab2.height = 34;
		tab2.valueCompareType = new int[1];
		tab2.requiredValues = new int[1];
		tab2.valueCompareType[0] = 2;
		tab2.requiredValues[0] = anIntArray212 + 1;
		tab2.valueIndexArray = new int[1][3];
		tab2.valueIndexArray[0][0] = 2;
		tab2.valueIndexArray[0][1] = 5;
		tab2.valueIndexArray[0][2] = 0;
		// RSInterface tab3 = addTabInterface(i + 50);
	}

	public static void toggleOptions() {
		RSInterface rsinterface = addInterface(35010, 512, 334);
		byte byte0 = 2;
		addCacheSprite(35011, 515);
		addHover(35012, 3, 0, 35013, 20, "Interfaces/Options/OPTION", 16, 16,
				"Close Window");
		addHovered(35013, 21, "Interfaces/Options/OPTION", 16, 16, 35014);

		addText(35099, "Game Options", 2, 0xff9b00, true, true, 0);

		addText(35055, "10X Hitmarks", 1, 0xb2aa9f, true, true, 0);
		addText(35056, "New Hitmarks", 1, 0xb2aa9f, true, true, 0);
		addText(35057, "New HP Bar", 1, 0xb2aa9f, true, true, 0);
		addText(35058, "Player Shadow", 1, 0xb2aa9f, true, true, 0);
		addText(35060, "Distance Fog", 1, 0xb2aa9f, true, true, 0);
		addText(35059, "Mouse Hovers", 1, 0xb2aa9f, true, true, 0);
		addText(35061, "Npc Shadows", 1, 0xb2aa9f, true, true, 0);
		addText(35062, "Minimap Shadow", 1, 0xb2aa9f, true, true, 0);
		addText(35063, "Tweening", 1, 0xb2aa9f, true, true, 0);

		addConfigButton(54357, 904, 200, 199, "Interfaces/Options/SPRITE", 48, 52, "Toggle 10X Hitmarks", 1, 5, 175);
		addConfigButton(54358, 904, 200, 200, "Interfaces/Options/SPRITE", 48, 52, "Toggle New Hitmarks", 1, 5, 176);
		addConfigButton(54359, 904, 200, 199, "Interfaces/Options/SPRITE", 48, 52, "Toggle New HP Bar", 1, 5, 177);
		addConfigButton(54360, 904, 200, 199, "Interfaces/Options/SPRITE", 48, 52, "Toggle Player Shadow", 1, 5, 178);
		addConfigButton(54361, 904, 200, 199, "Interfaces/Options/SPRITE", 48, 52, "Toggle Mouse Hovers", 1, 5, 179); // Toggle Mouse Hovers
		addConfigButton(54362, 904, 200, 199, "Interfaces/Options/SPRITE", 48, 52, "Toggle Distance Fog", 1, 5, 180);
		addConfigButton(54363, 904, 200, 199, "Interfaces/Options/SPRITE", 48, 52, "Toggle NPC Shadows", 1, 5, 181);
		addConfigButton(54364, 904, 200, 199, "Interfaces/Options/SPRITE", 48, 52, "Minimap Shadow", 1, 5, 182);
		addConfigButton(55375, 904, 200, 199, "Interfaces/Options/SPRITE", 48, 52, "Toggle Smooth Animations", 1, 5, 183);

		rsinterface.totalChildren(22);
		rsinterface.child(0, 35011, 1, 1 + byte0);

		rsinterface.child(1, 35055, 139, 72);
		rsinterface.child(2, 35056, 293, 72);
		rsinterface.child(3, 35057, 450, 72);
		rsinterface.child(4, 35058, 145, 150);
		rsinterface.child(5, 35059, 295, 150);
		rsinterface.child(6, 35060, 455, 150);

		rsinterface.child(8, 35012, 485, 6);
		rsinterface.child(9, 35013, 485, 6);
		rsinterface.child(10, 35099, 264, 6);

		rsinterface.child(16, 35061, 142, 227);
		rsinterface.child(17, 35062, 300, 227);
		rsinterface.child(20, 35063, 446, 227);

		// ok buttons for he toggle tab

		rsinterface.child(11, 54357, 55, 65);
		rsinterface.child(12, 54358, 205, 65);
		rsinterface.child(13, 54359, 371, 65);

		rsinterface.child(14, 54360, 57, 145);
		rsinterface.child(15, 54361, 207, 145);
		rsinterface.child(7, 54362, 372, 145);

		rsinterface.child(18, 54363, 54, 223);
		rsinterface.child(19, 54364, 206, 224);
		rsinterface.child(21, 55375, 372, 224);

	}

	public static void audioOptions() {
		RSInterface rsinterface = addInterface(49911, 512, 334);
		byte byte0 = 2;
		addSprite(49924, 19, "Interfaces/Options/OPTION");
		addHover(49925, 3, 0, 49926, 20, "Interfaces/Options/OPTION", 16, 16,
				"Close Window");
		addHovered(49926, 21, "Interfaces/Options/OPTION", 16, 16, 49927);
		addText(49928, "Audio Options", 2, 0xff9b00, true, true, 0);
		addConfigButton(930, 904, 19, 24, "Interfaces/Options/SPRITE", 26, 16,
				"Select", 4, 5, 168);
		addConfigButton(931, 904, 20, 25, "Interfaces/Options/SPRITE", 26, 16,
				"Select", 3, 5, 168);
		addConfigButton(932, 904, 21, 26, "Interfaces/Options/SPRITE", 26, 16,
				"Select", 2, 5, 168);
		addConfigButton(933, 904, 22, 27, "Interfaces/Options/SPRITE", 26, 16,
				"Select", 1, 5, 168);
		addConfigButton(934, 904, 23, 28, "Interfaces/Options/SPRITE", 24, 16,
				"Select", 0, 5, 168);

		addConfigButton(49911, 904, 19, 24, "Interfaces/Options/SPRITE", 26,
				16, "Select", 4, 5, 169);
		addConfigButton(49912, 904, 20, 25, "Interfaces/Options/SPRITE", 26,
				16, "Select", 3, 5, 169);
		addConfigButton(49913, 904, 21, 26, "Interfaces/Options/SPRITE", 26,
				16, "Select", 2, 5, 169);
		addConfigButton(49914, 904, 22, 27, "Interfaces/Options/SPRITE", 26,
				16, "Select", 1, 5, 169);
		addConfigButton(49915, 904, 23, 28, "Interfaces/Options/SPRITE", 24,
				16, "Select", 0, 5, 169);

		addConfigButton(49916, 904, 19, 24, "Interfaces/Options/SPRITE", 26,
				16, "Select", 4, 5, 400);
		addConfigButton(49917, 904, 20, 25, "Interfaces/Options/SPRITE", 26,
				16, "Select", 3, 5, 400);
		addConfigButton(49918, 904, 21, 26, "Interfaces/Options/SPRITE", 26,
				16, "Select", 2, 5, 400);
		addConfigButton(49919, 904, 22, 27, "Interfaces/Options/SPRITE", 26,
				16, "Select", 1, 5, 400);
		addConfigButton(49920, 904, 23, 28, "Interfaces/Options/SPRITE", 24,
				16, "Select", 0, 5, 400);

		addSprite(49921, 3, "Interfaces/Options/OPTION");
		addSprite(49922, 5, "Interfaces/Options/OPTION");
		addSprite(49923, 7, "Interfaces/Options/OPTION");

		rsinterface.totalChildren(22);
		rsinterface.child(0, 49924, 190, 55 + byte0);
		rsinterface.child(1, 930, 205, 120 + byte0);
		rsinterface.child(2, 931, 229, 120 + byte0);
		rsinterface.child(3, 932, 254, 120 + byte0);
		rsinterface.child(4, 933, 280, 120 + byte0);
		rsinterface.child(5, 934, 305, 120 + byte0);

		rsinterface.child(6, 49911, 205, 175 + byte0);
		rsinterface.child(7, 49912, 229, 175 + byte0);
		rsinterface.child(8, 49913, 254, 175 + byte0);
		rsinterface.child(9, 49914, 280, 175 + byte0);
		rsinterface.child(10, 49915, 305, 175 + byte0);

		rsinterface.child(11, 49916, 205, 230 + byte0);
		rsinterface.child(12, 49917, 229, 230 + byte0);
		rsinterface.child(13, 49918, 254, 230 + byte0);
		rsinterface.child(14, 499019, 280, 230 + byte0);
		rsinterface.child(15, 49920, 305, 230 + byte0);

		rsinterface.child(16, 49921, 250, 85);
		rsinterface.child(17, 49922, 250, 141);
		rsinterface.child(18, 49923, 250, 197);

		rsinterface.child(19, 49925, 315, 60);
		rsinterface.child(20, 49926, 315, 60);
		rsinterface.child(21, 49928, 264, 60);
	}

	public static void vidOptions() {
		RSInterface rsinterface = addInterface(40030, 512, 334);
		byte byte0 = 2;
		addSprite(40942, 24, "Interfaces/Options/OPTION");
		addHover(40939, 3, 0, 40940, 20, "Interfaces/Options/OPTION", 16, 16,
				"Close Window");
		addHovered(40940, 21, "Interfaces/Options/OPTION", 16, 16, 40099);
		addText(40941, "Graphics Options", 2, 0xff9b00, true, true, 0);
		addConfigButton(906, 904, 10, 14, "Interfaces/Options/SPRITE", 32, 16,
				"Dark", 1, 5, 166);
		addConfigButton(908, 904, 11, 15, "Interfaces/Options/SPRITE", 32, 16,
				"Normal", 2, 5, 166);
		addConfigButton(910, 904, 12, 16, "Interfaces/Options/SPRITE", 32, 16,
				"Bright", 3, 5, 166);
		addConfigButton(912, 904, 13, 17, "Interfaces/Options/SPRITE", 32, 16,
				"Very Bright", 4, 5, 166);

		addConfigButton(941, 904, 19, 24, "Interfaces/Options/SPRITE", 26, 16,
				"Regular Zoom", 4, 5, 169);
		addConfigButton(942, 904, 20, 25, "Interfaces/Options/SPRITE", 26, 16,
				"Zoom +1", 3, 5, 169);
		addConfigButton(943, 904, 21, 26, "Interfaces/Options/SPRITE", 26, 16,
				"Zoom +2", 2, 5, 169);
		addConfigButton(944, 904, 22, 27, "Interfaces/Options/SPRITE", 26, 16,
				"Zoom +3", 1, 5, 169);
		addConfigButton(945, 904, 23, 28, "Interfaces/Options/SPRITE", 24, 16,
				"Zoom +4", 0, 5, 169);

		addSprite(40936, 9, "Interfaces/Options/SPRITE");
		addSprite(40937, 29, "Interfaces/Options/SPRITE");

		// All hovers

		// fixed
		addHoverButton(40943, "Interfaces/Options/SPRITE", 37, 50, 39, "Fixed",
				-1, 40944, 5, 586);
		addHoveredButton(40944, "Interfaces/Options/SPRITE", 38, 50, 39, 40945);
		// resizable
		addHoverButton(40946, "Interfaces/Options/SPRITE", 39, 50, 39,
				"Resizable", -1, 40947, 5, 587);
		addHoveredButton(40947, "Interfaces/Options/SPRITE", 40, 50, 39, 40948);
		// fullscreen
		addHoverButton(40949, "Interfaces/Options/SPRITE", 41, 50, 39,
				"Fullscreen", -1, 40950, 5, 588);
		addHoveredButton(40950, "Interfaces/Options/SPRITE", 42, 50, 39, 40951);

		// ld
		addHoverButton(40089, "Interfaces/Options/SPRITE", -1, 56, 26,
				"Low-Detail", -1, 40090, 5, 599);
		addHoveredButton(40090, "Interfaces/Options/SPRITE", 203, 56, 26, 40091);
		// sd
		addHoverButton(40092, "Interfaces/Options/SPRITE", -1, 56, 26,
				"Medium-Detail", -1, 40093, 5, 600);
		addHoveredButton(40093, "Interfaces/Options/SPRITE", 204, 56, 26, 40094);
		// hd
		addHoverButton(40095, "Interfaces/Options/SPRITE", -1, 56, 26,
				"High-Detail", -1, 40096, 5, 601);
		addHoveredButton(40096, "Interfaces/Options/SPRITE", 205, 56, 26, 40097);

		rsinterface.totalChildren(27);
		rsinterface.child(0, 40942, 1, 0 + byte0);
		rsinterface.child(1, 906, 125, 85 + byte0);
		rsinterface.child(2, 908, 149, 85 + byte0);
		rsinterface.child(3, 910, 174, 85 + byte0);
		rsinterface.child(4, 912, 200, 85 + byte0);

		rsinterface.child(5, 941, 245, 85 + byte0);
		rsinterface.child(6, 942, 269, 85 + byte0);
		rsinterface.child(7, 943, 294, 85 + byte0);
		rsinterface.child(8, 944, 320, 85 + byte0);
		rsinterface.child(9, 945, 345, 85 + byte0);

		rsinterface.child(10, 40936, 160, 50);
		rsinterface.child(11, 40937, 290, 50);

		rsinterface.child(12, 40939, 485, 5);
		rsinterface.child(13, 40940, 485, 5);
		rsinterface.child(14, 40941, 255, 5);

		rsinterface.child(15, 40943, 56, 227);
		rsinterface.child(16, 40944, 56, 227);
		rsinterface.child(17, 40946, 184, 227);
		rsinterface.child(18, 40947, 184, 227);
		rsinterface.child(19, 40949, 334, 227);
		rsinterface.child(20, 40950, 334, 227);

		rsinterface.child(21, 40089, 150, 141);
		rsinterface.child(22, 40090, 150, 141);
		rsinterface.child(23, 40092, 220, 141);
		rsinterface.child(24, 40093, 220, 141);
		rsinterface.child(25, 40095, 290, 141);
		rsinterface.child(26, 40096, 290, 141);

	}

	public static void optionTab() {
		RSInterface rsinterface = addTabInterface(904);
		// RSInterface rsinterface1 = interfaceCache[149];
		// rsinterface1.textColor = 0xff9933;
		addSprite(907, 18, "Interfaces/Options/SPRITE");
		addSprite(909, 29, "Interfaces/Options/SPRITE");
		addSprite(951, 32, "Interfaces/Options/SPRITE");
		addSprite(953, 33, "Interfaces/Options/SPRITE");
		addSprite(955, 34, "Interfaces/Options/SPRITE");
		addSprite(947, 36, "Interfaces/Options/SPRITE");
		addSprite(949, 35, "Interfaces/Options/SPRITE");
		addSprite(40649, 888, "Interfaces/Options/SPRITE");
		addSprite(40001, 4, "Interfaces/Options/SPRITE");
		addSprite(40002, 4, "Interfaces/Options/SPRITE");
		// addText(12222, "Toggle HD/LD Mode", tda, 1, 0xff9933, true, true);
		// addConfigButton(12223, 904, 99, 100, "Interfaces/Options/SPRITE", 12,
		// 12, "Toggle HD/LD mode", 1, 5, 174);
		addConfigButton(13999, 904, 30, 31, "Interfaces/Options/SPRITE", 40,
				40, "Switch to Toggle-tab", 1, 5, 199);
		addConfigButton(913, 904, 30, 31, "Interfaces/Options/SPRITE", 40, 40,
				"Toggle-Mouse Buttons", 0, 5, 170);
		addConfigButton(915, 904, 30, 31, "Interfaces/Options/SPRITE", 40, 40,
				"Toggle-Chat Effects", 0, 5, 171);
		addConfigButton(957, 904, 30, 31, "Interfaces/Options/SPRITE", 40, 40,
				"Toggle-Split Private Chat", 1, 5, 287);
		addConfigButton(12464, 904, 30, 31, "Interfaces/Options/SPRITE", 40,
				40, "Toggle-Accept Aid", 0, 5, 427);

		addConfigButton(47999, 904, 31, 30, "Interfaces/Options/SPRITE", 40,
				40, "House Options", 0, 5, 927);

		addButton(40004, 22, "Interfaces/Options/OPTION", "Graphics Options");
		addButton(40105, 23, "Interfaces/Options/OPTION", "Audio Options");
		addText(40003, "Options", 1, 0xff9b00, true, true, 0);
		rsinterface.totalChildren(17);

		rsinterface.child(0, 913, 15, 153);
		rsinterface.child(1, 955, 19, 159);
		rsinterface.child(2, 915, 75, 153);
		rsinterface.child(3, 953, 79, 160);
		rsinterface.child(4, 957, 135, 153);
		rsinterface.child(5, 951, 139, 159);

		rsinterface.child(6, 12464, 15, 208);

		rsinterface.child(7, 949, 20, 213);
		rsinterface.child(8, 13999, 75, 208);

		rsinterface.child(9, 947, 83, 212);
		// rsinterface.child(10, 149, 113, 231);
		rsinterface.child(10, 40001, 0, 30);
		rsinterface.child(11, 40002, 0, 100);
		rsinterface.child(12, 40003, 93, 8);
		rsinterface.child(13, 40004, 35, 47);
		rsinterface.child(14, 40105, 110, 47);
		rsinterface.child(15, 47999, 135, 208);
		rsinterface.child(16, 40649, 140, 213);

		// tab.child(28, 153, 135, 158);
		// tab.child(29, 970, 20, 141);
		// rsinterface.child(15, 12222, 90, 111);
		// rsinterface.child(16, 12223, 18, 111);
	}

	public static void abuse() {
		RSInterface Interface = addTabInterface(5875);
		setChildren(10, Interface);
		addSprite(5876, 0, "Interfaces/Abuse/ABUSE");
		setBounds(5876, 60, 50, 0, Interface);
		addHover(5952, 3, 0, 5877, 1, "Interfaces/Bank/BANK", 17, 17,
				"Close Window");
		setBounds(5952, 435, 52, 1, Interface);
		addHovered(5877, 2, "Interfaces/Bank/BANK", 17, 17, 5878);
		setBounds(5877, 435, 52, 2, Interface);
		addText(5951, "Report Abuse", 0xF2AA3E, true, true, 52, 2);
		setBounds(5951, 270, 52, 6, Interface);
		addText(5879, "Page 1/2", 0x83815E, true, false, 52, 0);
		setBounds(5879, 85, 55, 4, Interface);
		addText(5880,
				"To report a player who is breaking one of our rules, first enter their \\nname in the box below and then click 'Next'",
				0xDBD7C1, false, true, 52, 0);
		setBounds(5880, 90, 80, 5, Interface);
		addHover(5881, 1, 0, 5882, 1, "Interfaces/Abuse/ABUSE", 51, 18, "Next");
		setBounds(5881, 360, 117, 7, Interface);
		addHovered(5882, 2, "Interfaces/Abuse/ABUSE", 51, 18, 5883);
		setBounds(5882, 360, 117, 8, Interface);
		addText(5884, "Next", 0xFFFFFF, true, false, 52, 0);
		setBounds(5884, 385, 123, 9, Interface);
		setBounds(5984, 220, 120, 3, Interface);
		Interface = interfaceCache[5984];
		Interface.anInt219 = 0x333333;
		Interface = addTabInterface(5885);
		setChildren(67, Interface);
		setBounds(5952, 474, 16, 1, Interface);
		setBounds(5877, 474, 16, 2, Interface);
		addSprite(5886, 3, "Interfaces/Abuse/ABUSE");
		setBounds(5886, 12, 12, 0, Interface);
		addText(5887, "Reporting:", 0xFF9933, true, true, 52, 2);
		setBounds(5887, 265, 16, 3, Interface);
		addSprite(5888, 6, "Interfaces/Abuse/ABUSE");
		setBounds(5888, 22, 104, 4, Interface);
		addSprite(5889, 7, "Interfaces/Abuse/ABUSE");
		setBounds(5889, 182, 104, 5, Interface);
		addSprite(5890, 8, "Interfaces/Abuse/ABUSE");
		setBounds(5890, 340, 104, 6, Interface);
		addHover(5891, 5, 0, 5892, 9, "Interfaces/Abuse/ABUSE", 25, 25,
				"Honour");
		setBounds(5891, 146, 103, 7, Interface);
		addHovered(5892, 10, "Interfaces/Abuse/ABUSE", 25, 25, 5893);
		setBounds(5892, 146, 103, 8, Interface);
		addHover(5894, 5, 0, 5895, 9, "Interfaces/Abuse/ABUSE", 25, 25,
				"Respect");
		setBounds(5894, 305, 103, 9, Interface);
		addHovered(5895, 10, "Interfaces/Abuse/ABUSE", 25, 25, 5896);
		setBounds(5895, 305, 103, 10, Interface);
		addHover(5897, 5, 0, 5898, 9, "Interfaces/Abuse/ABUSE", 25, 25,
				"Security");
		setBounds(5897, 464, 103, 11, Interface);
		addHovered(5898, 10, "Interfaces/Abuse/ABUSE", 25, 25, 5899);
		setBounds(5898, 464, 103, 12, Interface);
		addText(5900, "Honour", 0xF2AA3E, true, true, 52, 2);
		setBounds(5900, 100, 109, 13, Interface);
		addText(5901, "Respect", 0xF2AA3E, true, true, 52, 2);
		setBounds(5901, 255, 109, 14, Interface);
		addText(5902, "Security", 0xF2AA3E, true, true, 52, 2);
		setBounds(5902, 415, 109, 15, Interface);
		addHover(5903, 5, 601, 5904, 11, "Interfaces/Abuse/ABUSE", 154, 28,
				"Select");
		setBounds(5903, 20, 133, 16, Interface);
		addHovered(5904, 12, "Interfaces/Abuse/ABUSE", 154, 28, 5905);
		setBounds(5904, 20, 133, 17, Interface);
		addHover(5906, 5, 602, 5907, 11, "Interfaces/Abuse/ABUSE", 154, 28,
				"Select");
		setBounds(5906, 20, 162, 18, Interface);
		addHovered(5907, 12, "Interfaces/Abuse/ABUSE", 154, 28, 5908);
		setBounds(5907, 20, 165, 19, Interface);
		addHover(5909, 5, 603, 5910, 11, "Interfaces/Abuse/ABUSE", 154, 28,
				"Select");
		setBounds(5909, 20, 191, 20, Interface);
		addHovered(5910, 12, "Interfaces/Abuse/ABUSE", 154, 28, 5911);
		setBounds(5910, 20, 191, 21, Interface);
		addHover(5912, 5, 604, 5913, 11, "Interfaces/Abuse/ABUSE", 154, 28,
				"Select");
		setBounds(5912, 20, 220, 22, Interface);
		addHovered(5913, 12, "Interfaces/Abuse/ABUSE", 154, 28, 5914);
		setBounds(5913, 20, 220, 23, Interface);
		addHover(5915, 5, 605, 5916, 11, "Interfaces/Abuse/ABUSE", 154, 28,
				"Select");
		setBounds(5915, 20, 249, 24, Interface);
		addHovered(5916, 12, "Interfaces/Abuse/ABUSE", 154, 28, 5917);
		setBounds(5916, 20, 249, 25, Interface);
		addHover(5918, 5, 606, 5919, 11, "Interfaces/Abuse/ABUSE", 154, 28,
				"Select");
		setBounds(5918, 20, 278, 26, Interface);
		addHovered(5919, 12, "Interfaces/Abuse/ABUSE", 154, 28, 5920);
		setBounds(5919, 20, 278, 27, Interface);
		addHover(5921, 5, 607, 5922, 11, "Interfaces/Abuse/ABUSE", 154, 28,
				"Select");
		setBounds(5921, 179, 133, 28, Interface);
		addHovered(5922, 12, "Interfaces/Abuse/ABUSE", 154, 28, 5923);
		setBounds(5922, 179, 133, 29, Interface);
		addHover(5924, 5, 608, 5925, 11, "Interfaces/Abuse/ABUSE", 154, 28,
				"Select");
		setBounds(5924, 179, 162, 30, Interface);
		addHovered(5925, 12, "Interfaces/Abuse/ABUSE", 154, 28, 5926);
		setBounds(5925, 179, 162, 31, Interface);
		addHover(5927, 5, 609, 5928, 11, "Interfaces/Abuse/ABUSE", 154, 28,
				"Select");
		setBounds(5927, 179, 191, 32, Interface);
		addHovered(5928, 12, "Interfaces/Abuse/ABUSE", 154, 28, 5929);
		setBounds(5928, 179, 191, 33, Interface);
		addHover(5930, 5, 610, 5931, 11, "Interfaces/Abuse/ABUSE", 154, 28,
				"Select");
		setBounds(5930, 179, 220, 34, Interface);
		addHovered(5931, 12, "Interfaces/Abuse/ABUSE", 154, 28, 5932);
		setBounds(5931, 179, 220, 35, Interface);
		addHover(5933, 5, 611, 5934, 11, "Interfaces/Abuse/ABUSE", 154, 28,
				"Select");
		setBounds(5933, 179, 249, 36, Interface);
		addHovered(5934, 12, "Interfaces/Abuse/ABUSE", 154, 28, 5935);
		setBounds(5934, 179, 249, 37, Interface);

		addHover(5936, 5, 609, 5937, 11, "Interfaces/Abuse/ABUSE", 154, 28,
				"Select");
		setBounds(5936, 337, 133, 38, Interface);
		addHovered(5937, 12, "Interfaces/Abuse/ABUSE", 154, 28, 5938);
		setBounds(5937, 337, 133, 39, Interface);
		addHover(5939, 5, 605, 5940, 11, "Interfaces/Abuse/ABUSE", 154, 28,
				"Select");
		setBounds(5939, 337, 162, 40, Interface);
		addHovered(5940, 12, "Interfaces/Abuse/ABUSE", 154, 28, 5941);
		setBounds(5940, 337, 162, 41, Interface);
		addHover(5942, 5, 607, 5943, 11, "Interfaces/Abuse/ABUSE", 154, 28,
				"Select");
		setBounds(5942, 337, 191, 42, Interface);
		addHovered(5943, 12, "Interfaces/Abuse/ABUSE", 154, 28, 5944);
		setBounds(5943, 337, 191, 43, Interface);
		addText(5945, "Buying or selling an account", 0xDBD7C1, true, true, 52,
				0);
		setBounds(5945, 97, 141, 44, Interface);
		addText(5946, "Encouraging rule breaking", 0xDBD7C1, true, true, 52,
				0);
		setBounds(5946, 97, 170, 45, Interface);
		addText(5947, "Staff impersonation", 0xDBD7C1, true, true, 52, 0);
		setBounds(5947, 97, 198, 46, Interface);
		addText(5948, "Macroing or use of bots", 0xDBD7C1, true, true, 52, 0);
		setBounds(5948, 97, 228, 47, Interface);
		addText(5949, "Scamming", 0xDBD7C1, true, true, 52, 0);
		setBounds(5949, 97, 257, 48, Interface);
		addText(5950, "Exploiting a bug", 0xDBD7C1, true, true, 52, 0);
		setBounds(5950, 97, 287, 49, Interface);
		addText(19142, "Seriously offensive\\nlanguage", 0xDBD7C1, true, true,
				52, 0);
		setBounds(19142, 257, 135, 50, Interface);
		addText(19143, "Solicitation", 0xDBD7C1, true, true, 52, 0);
		setBounds(19143, 257, 170, 51, Interface);
		addText(5953, "Disruptive behavior", 0xDBD7C1, true, true, 52, 0);
		setBounds(5953, 257, 199, 52, Interface);
		addText(5954, "Offensive account name", 0xDBD7C1, true, true, 52, 0);
		setBounds(5954, 257, 228, 53, Interface);
		addText(5955, "real-life threats", 0xDBD7C1, true, true, 52, 0);
		setBounds(5955, 257, 257, 54, Interface);
		addText(5956, "Asking for or providing\\nreal life information",
				0xDBD7C1, true, true, 52, 0);
		setBounds(5956, 415, 136, 55, Interface);
		addText(5957, "Breaking real-world laws", 0xDBD7C1, true, true, 52,
				0);
		setBounds(5957, 415, 170, 56, Interface);
		addText(5958, "Advertising websites", 0xDBD7C1, true, true, 52, 0);
		setBounds(5958, 415, 198, 57, Interface);
		addText(5959,
				"Click on the mouse suitable option from the rules of Aeresan",
				0xDBD7C1, true, true, 52, 0);
		setBounds(5959, 185, 40, 58, Interface);
		addText(5960,
				"This will send a report to our player support team for investigation",
				0xDBD7C1, true, true, 52, 0);
		setBounds(5960, 198, 55, 59, Interface);
		addText(5961, "Page 2/2", 0xFF9933, true, true, 52, 0);
		setBounds(5961, 40, 20, 60, Interface);
		addHover(5962, 5, 610, 5963, 4, "Interfaces/Abuse/ABUSE", 105, 18,
				"Go Back");
		setBounds(5962, 382, 43, 61, Interface);
		addHovered(5963, 5, "Interfaces/Abuse/ABUSE", 105, 18, 5964);
		setBounds(5963, 382, 43, 62, Interface);
		addHover(5965, 5, 0, 599, 4, "Interfaces/Abuse/ABUSE", 105, 18,
				"Cancel Report");
		setBounds(5965, 382, 67, 63, Interface);
		addHovered(5998, 5, "Interfaces/Abuse/ABUSE", 105, 18, 5967);
		setBounds(5998, 382, 67, 64, Interface);
		addText(5967, "Change player", 0xDBD7C1, true, true, 52, 0);
		setBounds(5967, 440, 47, 65, Interface);
		addText(5968, "Cancel report", 0xDBD7C1, true, true, 52, 0);
		setBounds(5968, 440, 70, 66, Interface);
	}

	public static void addButton(int id, int sid, String spriteName,
								 String tooltip, int mOver, int atAction, int width, int height) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = atAction;
		tab.contentType = 0;
		tab.opacity = 0;
		tab.sprite1 = imageLoader(sid, spriteName);
		tab.width = width;
		tab.height = height;
		tab.tooltip = tooltip;
	}

	public static void scrolls() {
		RSInterface rsinterface = addInterface(51700, 512, 334);
		addButton(51701, 0, "Interfaces/Summoning/Scrolls/SPRITE", "Pouches",
				27640, 1, 116, 20);
		addButton(51702, 1, "Interfaces/Summoning/Scrolls/SPRITE", "Scrolls",
				27640, 1, 116, 20);

		RSInterface scroll = addTabInterface(51710);
		scroll.width = 430;
		scroll.height = 230;
		scroll.scrollMax = 550;
		int lastId = 51710;
		int lastImage = 4;

		for (int i = 0; i < 78; i++) {
			addScrolls(lastId + 1, lastImage + 2, "Interfaces/Summoning/Scrolls/Sprite", i);
			lastId += 5;
			lastImage += 2;
		}

		rsinterface.children = new int[7];
		rsinterface.childX = new int[7];
		rsinterface.childY = new int[7];

		rsinterface.children[0] = 52701;
		rsinterface.childX[0] = 14;
		rsinterface.childY[0] = 17;
		rsinterface.children[1] = 52702;
		rsinterface.childX[1] = 475;
		rsinterface.childY[1] = 30;
		rsinterface.children[2] = 52703;
		rsinterface.childX[2] = 475;
		rsinterface.childY[2] = 30;
		rsinterface.children[3] = 51701;
		rsinterface.childX[3] = 25;
		rsinterface.childY[3] = 30;
		rsinterface.children[4] = 51702;
		rsinterface.childX[4] = 128;
		rsinterface.childY[4] = 30;
		rsinterface.children[5] = 52707;
		rsinterface.childX[5] = 268;
		rsinterface.childY[5] = 30;
		rsinterface.children[6] = 51710;
		rsinterface.childX[6] = 35;
		rsinterface.childY[6] = 65;

		scroll.children = new int[78];
		scroll.childX = new int[78];
		scroll.childY = new int[78];

		int lastNumber = -1;
		int lastId2 = 51710;
		int lastX = -52;
		int lastY = 0;
		for (int i = 0; i < 78; i++) {
			scroll.children[lastNumber += 1] = lastId2 += 1;
			scroll.childX[lastNumber] = lastX += 52;
			scroll.childY[lastNumber] = lastY;
			lastId2 += 4;
			if (lastX == 364) {
				lastX = -52;
				lastY += 55;
			}
		}
	}

	public static void pouches() {
		RSInterface rsinterface = addInterface(52700, 512, 334);
		addSprite(52701, 0, "Interfaces/Summoning/Pouches/SPRITE");
		addHover(52702, 3, 0, 52703, 1, "Interfaces/Summoning/Pouches/SPRITE", 17, 17, "Close Window");
		addHovered(52703, 2, "Interfaces/Summoning/Pouches/SPRITE", 17, 17, 52704);
		addButton(52705, 3, "Interfaces/Summoning/Pouches/SPRITE", "Pouches", 27640, 1, 116, 20);
		addButton(52706, 4, "Interfaces/Summoning/Pouches/SPRITE", "Scrolls", 27640, 1, 116, 20);
		addText(52707, "Summoning Pouch Creation", 2, 0xff981f, false, true, 0);

		RSInterface scroll = addTabInterface(52710);
		scroll.width = 430;
		scroll.height = 230;
		scroll.scrollMax = 550;
		int lastId = 52710;
		int lastImage = 4;
		for (int i = 0; i < 78; i++) {
			addPouches(lastId + 1, lastImage + 2, "Interfaces/Summoning/Pouches/Sprite", i);
			lastId += 5;
			lastImage += 2;
		}

		rsinterface.children = new int[7];
		rsinterface.childX = new int[7];
		rsinterface.childY = new int[7];

		rsinterface.children[0] = 52701;
		rsinterface.childX[0] = 14;
		rsinterface.childY[0] = 17;
		rsinterface.children[1] = 52702;
		rsinterface.childX[1] = 475;
		rsinterface.childY[1] = 30;
		rsinterface.children[2] = 52703;
		rsinterface.childX[2] = 475;
		rsinterface.childY[2] = 30;
		rsinterface.children[3] = 52705;
		rsinterface.childX[3] = 25;
		rsinterface.childY[3] = 30;
		rsinterface.children[4] = 52706;
		rsinterface.childX[4] = 128;
		rsinterface.childY[4] = 30;
		rsinterface.children[5] = 52707;
		rsinterface.childX[5] = 268;
		rsinterface.childY[5] = 30;
		rsinterface.children[6] = 52710;
		rsinterface.childX[6] = 35;
		rsinterface.childY[6] = 65;

		scroll.children = new int[78];
		scroll.childX = new int[78];
		scroll.childY = new int[78];

		int lastNumber = -1;
		int lastId2 = 52710;
		int lastX = -52;
		int lastY = 0;
		for (int i = 0; i < 78; i++) {
			scroll.children[lastNumber += 1] = lastId2 += 1;
			scroll.childX[lastNumber] = lastX += 52;
			scroll.childY[lastNumber] = lastY;
			lastId2 += 4;
			if (lastX == 364) {
				lastX = -52;
				lastY += 55;
			}
		}
	}

	private static void summonOrb() {
		addText(42567, "Cast scroll", 0, WHITE_TEXT, false);
	}

	private static void summoningTab() {
		final String dir = "/Interfaces/Summoning/Tab/sprite";
		RSInterface rsi = addInterface(14317, 512, 334);
		addButton(14318, 0, dir, "Cast Special move", 143, 13);

		// addButton(18018, 0, dir, 143, 13, "Cast special", 1);
		addText(14319, "S P E C I A L  M O V E", 0, WHITE_TEXT, false, false, 0);
		addSprite(14320, 1, dir);
		addMobHead(14321, 75, 55, 800);
		addSprite(14322, 2, dir);
		addButton(14323, 4, dir, "Cast scroll", 30, 31);
		addText(14324, "0", 0, ORANGE_TEXT, false);
		addSprite(14325, 5, dir);
		addButton(14326, 6, dir, "Attack", 29, 39);
		addSprite(14327, 8, dir);
		addText(14328, "", 2, ORANGE_TEXT, true, false, 0);
		addHoverButton(14329, dir, 9, 38, 38, "Withdraw BoB", -1, 14330, 1, -1);
		addHoveredButton(14330, dir, 10, 38, 38, 14331);
		addHoverButton(14332, dir, 11, 38, 38, "Renew familiar", -1, 14333, 1,
				-1);
		addHoveredButton(14333, dir, 12, 38, 38, 14334);
		addHoverButton(14335, dir, 13, 38, 38, "Call follower", -1, 18036, 1,
				-1);
		addHoveredButton(14336, dir, 14, 38, 38, 14337);
		addHoverButton(14338, dir, 15, 38, 38, "Dismiss familiar", -1, 14339,
				1, -1);
		addHoveredButton(14339, dir, 16, 38, 38, 14340);
		addSprite(14341, 17, dir);
		addSprite(14342, 18, dir);
		addText(14343, "00:00", 0, GREY_TEXT, false);
		addSprite(14344, 19, dir);
		addText(14345, "0/99", 0, GREY_TEXT, false);
		addSprite(14346, 3, dir);
		setChildren(25, rsi);
		setBounds(14318, 23, 10, 0, rsi);
		setBounds(14319, 30, 12, 1, rsi);
		setBounds(14320, 10, 32, 2, rsi);
		setBounds(14345, 145, 241, 3, rsi);
		setBounds(14322, 11, 32, 4, rsi);
		setBounds(14323, 14, 35, 5, rsi);
		setBounds(14324, 25, 69, 6, rsi);
		setBounds(14325, 138, 32, 7, rsi);
		setBounds(14326, 143, 36, 8, rsi);
		setBounds(14327, 12, 144, 9, rsi);
		setBounds(14328, 93, 146, 10, rsi);
		setBounds(14329, 23, 168, 11, rsi);
		setBounds(14330, 23, 168, 12, rsi);
		setBounds(14332, 75, 168, 13, rsi);
		setBounds(14333, 75, 168, 14, rsi);
		setBounds(14335, 23, 213, 15, rsi);
		setBounds(14336, 23, 213, 16, rsi);
		setBounds(14338, 75, 213, 17, rsi);
		setBounds(14339, 75, 213, 18, rsi);
		setBounds(14341, 130, 168, 19, rsi);
		setBounds(14342, 153, 170, 20, rsi);
		setBounds(14343, 148, 198, 21, rsi);
		setBounds(14344, 149, 213, 22, rsi);
		setBounds(14321, 63, 60, 23, rsi);
		setBounds(14346, 14, 35, 24, rsi);
	}

	public static void addBobStorage(int index) {
		RSInterface rsi = interfaceCache[index] = new RSInterface();
		rsi.actions = new String[10];
		rsi.invStackSizes = new int[30];
		rsi.inv = new int[30];

		rsi.children = new int[0];
		rsi.childX = new int[0];
		rsi.childY = new int[0];

		rsi.actions[0] = "Take 1";
		rsi.actions[1] = "Take 5";
		rsi.actions[2] = "Take 10";
		rsi.actions[3] = "Take All";
		rsi.actions[4] = "Take X";
		rsi.centerText = true;
		rsi.filled = false;
		rsi.itemsDraggable = false;
		rsi.usableItemInterface = false;
		rsi.isInventoryInterface = false;
		rsi.aBoolean259 = true;
		rsi.textShadow = false;
		rsi.invSpritePadX = 22;
		rsi.invSpritePadY = 21;
		rsi.height = 5;
		rsi.width = 6;
		rsi.parentID = 2702;
		rsi.id = 2700;
		rsi.type = 2;
	}

	private static void bobInterface() {
		RSInterface rsi = addInterface(2700, 512, 334);
		addSprite(2701, 1, "/Interfaces/Summoning/BOB/store");

		addButton(2736, 2, "/Interfaces/Summoning/BOB/store", 21, 21, "Take BoB", 1);
		addHoverButton(2735, "/Interfaces/Summoning/BOB/store", 3, 16, 16, "Close", 250, 2734, 3);
		// addHoveredButton2(2734, "/Interfaces/Summoning/BOB/store", 4, 21, 21,  2735);

		rsi.totalChildren(33);
		/**
		 * Bob storage, starting with first row.
		 */
		for (int i = 2702; i < 2710; i++)
			addBobStorage(i);
		/**
		 * Second row
		 */
		for (int i = 2710; i < 2716; i++)
			addBobStorage(i);
		/**
		 * Third row
		 */
		for (int i = 2716; i < 2722; i++)
			addBobStorage(i);

		/**
		 * Fourth row
		 */
		for (int i = 2722; i < 2728; i++)
			addBobStorage(i);

		/**
		 * Fifth row
		 */
		for (int i = 2728; i < 2734; i++)
			addBobStorage(i);

		rsi.child(0, 2701, 90, 14);
		rsi.child(1, 2735, 431, 23);
		rsi.child(2, 2736, 427, 285);
		/**
		 * Bob storage first row
		 */
		rsi.child(3, 2702, 105, 56);
		rsi.child(4, 2705, 160, 56);
		rsi.child(5, 2706, 215, 56);
		rsi.child(6, 2707, 270, 56);
		rsi.child(7, 2708, 320, 56);
		rsi.child(8, 2709, 375, 56);

		/**
		 * Bob storage second row
		 */
		rsi.child(9, 2710, 105, 110);
		rsi.child(10, 2711, 160, 110);
		rsi.child(11, 2712, 215, 110);
		rsi.child(12, 2713, 270, 110);
		rsi.child(13, 2714, 320, 110);
		rsi.child(14, 2715, 375, 110);

		/**
		 * Bob storage third row
		 */
		rsi.child(15, 2716, 105, 163);
		rsi.child(16, 2717, 160, 163);
		rsi.child(17, 2718, 215, 163);
		rsi.child(18, 2719, 270, 163);
		rsi.child(19, 2720, 320, 163);
		rsi.child(20, 2721, 375, 163);

		/**
		 * Bob storage fourth row
		 */
		rsi.child(21, 2722, 105, 216);
		rsi.child(22, 2723, 160, 216);
		rsi.child(23, 2724, 215, 216);
		rsi.child(24, 2725, 270, 216);
		rsi.child(25, 2726, 320, 216);
		rsi.child(26, 2727, 375, 216);

		/**
		 * Bob storage fifth row
		 */
		rsi.child(27, 2728, 105, 269);
		rsi.child(28, 2729, 160, 269);
		rsi.child(29, 2730, 215, 269);
		rsi.child(30, 2731, 270, 269);
		rsi.child(31, 2732, 320, 269);
		rsi.child(32, 2733, 375, 269);

		// Close
		//rsi.child(33, 2734, 429, 22);
	}

	public static void addMobHead(int id, int w, int h, int zoom) {
		RSInterface rsinterface = addInterface(id, 512, 334);
		rsinterface.type = 6;
		rsinterface.mediaType = 2;
		/*
		 * rsinterface.disabledAnimation = 9847; rsinterface.enabledAnimation = 9847;
		 */
		rsinterface.mediaID = 4000;//
		rsinterface.modelZoom = zoom;
		rsinterface.rotationX = 40;
		rsinterface.rotationY = 1900;
		rsinterface.height = h;
		rsinterface.width = w;
	}

	private static void shop() {
		RSInterface widget = addInterface(3824, 512, 334);
		setChildren(8 + 36, widget);
		addSprite(3825, 0, "/Interfaces/Shop/SHOP");
		addHover(3902, 3, 0, 3826, 1, "/Interfaces/Shop/CLOSE", 17, 17,
				"Close Window");
		addHovered(3826, 2, "/Interfaces/Shop/CLOSE", 17, 17, 3827);
		addText(19679, "", 0xff981f, false, true, 52, 1);
		addText(19680, "", 0xbf751d, false, true, 52, 1);
		addButton(19681, 2, "/Interfaces/Shop/SHOP", 0, 0, "", 1);
		addSprite(19687, 1, "/Interfaces/Shop/ITEMBG");
		for (int i = 0; i < 36; i++)
			addText(28000 + i, "", 0xffff00, false, false, 52, 0);
		setBounds(3825, 6, 8, 0, widget);
		setBounds(3902, 478, 10, 1, widget);
		setBounds(3826, 478, 10, 2, widget);
		setBounds(3900, 32, 50, 3, widget);
		setBounds(3901, 240, 11, 4, widget);
		setBounds(19679, 42, 54, 5, widget);
		setBounds(19680, 150, 54, 6, widget);
		setBounds(19681, 129, 50, 7, widget);
		for (int i = 0; i < 36; i++) {
			int x = i % 9;
			int y = i / 9;
			x = 52 * x + 32;
			y = 65 * y + 84;
			setBounds(28000 + i, x, y, 8 + i, widget);
		}
		widget = interfaceCache[3900];
		setChildren(1, widget);
		setBounds(19687, 6, 15, 0, widget);
		widget.invSpritePadX = 20;
		widget.width = 9;
		widget.height = 4;
		widget.invSpritePadY = 33;
		widget.invStackSizes = new int[36];
		widget.inv = new int[36];
		widget = addInterface(19682, 512, 334);
		addSprite(19683, 1, "/Interfaces/Shop/SHOP");
		addText(19684, "Main Stock", 0xbf751d, false, true, 52, 1);
		addText(19685, "Store Info", 0xff981f, false, true, 52, 1);
		addButton(19686, 2, "/Interfaces/Shop/SHOP", 95, 19, "Main Stock", 1);
		setChildren(7, widget);
		setBounds(19683, 12, 12, 0, widget);
		setBounds(3901, 240, 21, 1, widget);
		setBounds(19684, 42, 54, 2, widget);
		setBounds(19685, 150, 54, 3, widget);
		setBounds(19686, 23, 50, 4, widget);
		setBounds(3902, 471, 22, 5, widget);
		setBounds(3826, 60, 85, 6, widget);
	}

	public static void equipmentTab() {
		RSInterface tab = interfaceCache[1644];
		addHoverButton(15201, "Interfaces/Equipment/CUSTOM", 1, 40, 40,
				"Show Equipment Stats", 0, 15202, 1, -1);
		addHoveredButton(15202, "Interfaces/Equipment/CUSTOM", 5, 40, 40, 15203);
		addHoverButton(15204, "Interfaces/Equipment/CUSTOM", 2, 40, 40,
				"Show Items Kept on Death", 0, 15205, 1, -1);
		addHoveredButton(15205, "Interfaces/Equipment/CUSTOM", 4, 40, 40, 15206);
		addHoverButton(15207, "Interfaces/Equipment/CUSTOM", 3, 40, 40,
				"Show Price-checker", 0, 15208, 1, -1);
		addHoveredButton(15208, "Interfaces/Equipment/CUSTOM", 6, 40, 40, 15209);
		addText(15226, "", 0, 0xff981f, true, true, 0);

		removeSomething(15106);

		tab.child(23, 15201, 21, 210);
		tab.child(1, 15226, 95, 250);
		tab.child(24, 15202, 21, 210);
		tab.child(25, 15204, 129, 210);
		tab.child(26, 15205, 129, 210);
		tab.child(0, 15207, 75, 210);
		tab.child(2, 15208, 75, 210);
		/* tab.child(3, 15109, 41+39+30, 212); */
	}

	public static void questInterface() {
		RSInterface Interface = addInterface(8134, 512, 334);
		Interface.centerText = true;
		addSprite(8135, 0, "Interfaces/Quest/QUESTBG");
		addSprite(8136, 1, "Interfaces/Quest/QUESTBG");
		addText(8144, "Quest Name", 0x8A0000, true, false, 52, 3);// 249 18
		addHover(8137, 3, 0, 8138, 0, "Interfaces/Quest/CLOSE", 26, 23, "Close");
		addHovered(8138, 1, "Interfaces/Quest/CLOSE", 26, 23, 8139);
		setChildren(6, Interface);
		setBounds(8136, 18, 4, 0, Interface);
		setBounds(8135, 18, 62, 1, Interface);
		setBounds(8144, 260, 15, 2, Interface);
		setBounds(8140, 50, 86, 3, Interface);
		setBounds(8137, 452, 63, 4, Interface);
		setBounds(8138, 452, 63, 5, Interface);
		Interface = addInterface(8140, 404, 217);
		Interface.newScroller = true;
		Interface.scrollMax = 1300;
		setChildren(167, Interface);
		int Ypos = 18;
		int frameID = 0;
		for (int iD = 18837; iD < 19004; iD++) {
			addText(iD, "", 0x000080, true, false, 52, 1);
			setBounds(iD, 202, Ypos, frameID, Interface);
			frameID++;
			Ypos += 19;
			Ypos++;
		}
	}

	public static void achievementTab() {
		RSInterface Interface = addTabInterface(53335);
		setChildren(6, Interface);

		addText(53336, "Achievements", 0xFF981F, false, true, 52, 2);
		addSprite(53337, 4, "Interfaces/Quest/QUEST");
		addSprite(53338, 0, "Interfaces/Quest/QUEST");
		addText(53339, "Achievement Points: 0", 0xFF981F, false, true, 52, 0);

		setBounds(53336, 4, 4, 0, Interface);
		setBounds(53337, 0, 24, 1, Interface);
		setBounds(53338, 0, 22, 2, Interface);
		setBounds(53338, 0, 242, 3, Interface);
		setBounds(53339, 4, 246, 4, Interface);
		setBounds(53340, 0, 24, 5, Interface);

		Interface = addTabInterface(53340);
		Interface.width = 175;
		Interface.height = 218;
		setChildren(EASY_TASKS.length + MEDIUM_TASKS.length + HARD_TASKS.length + ELITE_TASKS.length + 4, Interface);

		int index = 0;

		addText(53341, "Easy", 2, 0xFF9900, false, true, 0);
		setBounds(53341, 4, 4, index++, Interface);

		int height = 23;
		for (int i = 0; i < EASY_TASKS.length; i++) {
			int parentId = ((Integer) EASY_TASKS[i][0]).intValue();
			addText(parentId, (String) EASY_TASKS[i][1], 0xFF0000, false, true, 52, 0, 0xFFFFFF);
			setBounds(parentId, 6, height, index++, Interface);
			height += 12;
		}

		addText(53362, "Medium", 2, 0xFF9900, false, true, 0);
		setBounds(53362, 4, height, index++, Interface);
		height += 19;

		for (int i = 0; i < MEDIUM_TASKS.length; i++) {
			int parentId = ((Integer) MEDIUM_TASKS[i][0]).intValue();
			addText(parentId, (String) MEDIUM_TASKS[i][1], 0xFF0000, false, true, 52, 0, 0xFFFFFF);
			setBounds(parentId, 6, height, index++, Interface);
			height += 12;
		}

		addText(53379, "Hard", 2, 0xFF9900, false, true, 0);
		setBounds(53379, 4, height, index++, Interface);
		height += 19;

		for (int i = 0; i < HARD_TASKS.length; i++) {
			int parentId = ((Integer) HARD_TASKS[i][0]).intValue();
			addText(parentId, (String) HARD_TASKS[i][1], 0xFF0000, false, true, 52, 0, 0xFFFFFF);
			setBounds(parentId, 6, height, index++, Interface);
			height += 12;
		}

		addText(54356, "Elite", 2, 0xFF9900, false, true, 0);
		setBounds(54356, 4, height, index++, Interface);
		height += 19;

		for (int i = 0; i < ELITE_TASKS.length; i++) {
			int parentId = ((Integer) ELITE_TASKS[i][0]).intValue();
			addText(parentId, (String) ELITE_TASKS[i][1], 0xFF0000, false, true, 52, 0, 0xFFFFFF);
			setBounds(parentId, 6, height, index++, Interface);
			height += 12;
		}

		Interface.scrollMax = height + 10;
	}

	public static void questTab() {
		RSInterface Interface = addTabInterface(638);
		setChildren(6, Interface);

		addText(19155, "Quests", 0xFF981F, false, true, 52, 2);

		//addSprite(19156, 1, "Interfaces/Quest/QUEST");
		addSprite(19157, 4, "Interfaces/Quest/QUEST");
		addSprite(19158, 0, "Interfaces/Quest/QUEST");
		addText(19159, "Quest Points:", 0xFF981F, false, true, 52, 0);

		setBounds(19155, 4, 4, 0, Interface);
		//setBounds(19156, 150, 5, 1, Interface);
		setBounds(19157, 0, 24, 1, Interface);
		setBounds(19158, 0, 22, 2, Interface);
		setBounds(19158, 0, 242, 3, Interface);
		setBounds(19159, 4, 246, 4, Interface);
		setBounds(19160, 0, 24, 5, Interface);

		Interface = addTabInterface(19160);
		setChildren(8, Interface);

		for (int i = 0; i < 8; i++)
			addText(19161 + i, i > quests.length - 1 ? "" : quests[i], 0xFF0000, false, true, 52, 0, 0xFFFFFF);

		setBounds(19161, 4, 4, 0, Interface);
		setBounds(19162, 4, 16, 1, Interface);
		setBounds(19163, 4, 28, 2, Interface);
		setBounds(19164, 4, 40, 3, Interface);
		setBounds(19165, 4, 52, 4, Interface);
		setBounds(19166, 4, 64, 5, Interface);
		setBounds(19167, 4, 76, 6, Interface);
		setBounds(19168, 4, 88, 7, Interface);
	}

	public static void attackStyle() {
		Sidebar0a(1698, 1701, 7499, "Chop", "Hack", "Smash", "Block", 42, 75,
				127, 75, 39, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103,
				"Accurate\nSlash\nAttack XP", "Aggressive\nSlash\nStrength XP",
				"Aggressive\nCrush\nStrength XP",
				"Defensive\nSlash\nDefence XP",
				40132, 40136, 40140, 40144); // OK

		Sidebar0a(2276, 2279, 7574, "Stab", "Lunge", "Slash", "Block", 43, 75,
				124, 75, 41, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103,
				"Accurate\nStab\nAttack XP", "Aggressive\nStab\nStrength XP",
				"Aggressive\nSlash\nStrength XP",
				"Defensive\nStab\nDefence XP",
				40020, 40024, 40028, 40032); // OK

		Sidebar0a(2423, 2426, 7599, "Chop", "Slash", "Lunge", "Block", 42, 75,
				125, 75, 40, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103,
				"Accurate\nSlash\nAttack XP", "Aggressive\nSlash\nStrength XP",
				"Controlled\nStab\nShared XP",
				"Defensive\nSlash\nDefence XP", 40036,
				40040, 40044, 40048); // OK

		Sidebar0a(3796, 3799, 7624, "Pound", "Pummel", "Spike", "Block", 39,
				75, 121, 75, 41, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40,
				103, "Accurate\nPound\nAttack XP", "Aggressive\nPummel\nStrength XP",
				"Aggressive\nSpike\nStrength XP",
				"Defensive\nBlock\nDefence XP",
				40052, 40057, 40060, 40064); // WTF IS
		// THIS?!
		//this shit = dragon mace

		Sidebar0a(4679, 4682, 7674, "Lunge", "Swipe", "Pound", "Block", 40, 75,
				124, 75, 39, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103,
				"Controlled\nStab\nShared XP", "Controlled\nSlash\nShared XP",
				"Controlled\nCrush\nShared XP", "Defensive\nStab\nDefence XP",
				40068, 40072, 40076, 40080); // OK

		Sidebar0a(4705, 4708, 7699, "Chop", "Slash", "Smash", "Block", 42, 75,
				125, 75, 39, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103,
				"Accurate\nSlash\nAttack XP", "Aggressive\nSlash\nStrength XP",
				"Aggressive\nCrush\nStrength XP",
				"Defensive\nSlash\nDefence XP",
				40084, 40088, 40092, 40096); // ???

		Sidebar0a(5570, 5573, 7724, "Spike", "Impale", "Smash", "Block", 41,
				75, 123, 75, 39, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40,
				103, "Accurate\nStab\nAttack XP", "Aggressive\nStab\nStrength XP",
				"Aggressive\nCrush\nStrength XP",
				"Defensive\nStab\nDefence XP",
				40010, 40104, 40108, 40112); // OK

		Sidebar0a(7762, 7765, 7800, "Chop", "Slash", "Lunge", "Block", 42, 75,
				125, 75, 40, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103,
				"Accurate\nSlash\nAttack XP", "Aggressive\nSlash\nStrength XP",
				"Controlled\nStab\nShared XP",
				"Defensive\nSlash\nDefence XP", 40116,
				40120, 40124, 40128); // OK

		Sidebar0b(776, 779, "Reap", "Chop", "Jab", "Block", 42, 75, 126, 75,
				46, 128, 125, 128, 122, 103, 122, 50, 40, 103, 40, 50, "", "",
				"", "", 40132, 40136, 40140, 40144); // ???

		Sidebar0c(425, 428, 7474, "Pound", "Pummel", "Block", 39, 75, 121, 75,
				42, 128, 40, 103, 40, 50, 122, 50, "Accurate\nCrush\nAttack XP",
				"Aggressive\nCrush\nDefence XP", "Defensive\nCrush\nDefence XP",
				40148, 40152, 40156); // OK

		Sidebar0c(1749, 1752, 7524, "Accurate", "Rapid", "Longrange", 33, 75,
				125, 75, 29, 128, 40, 103, 40, 50, 122, 50, "Accurate\nRanged XP",
				"Rapid\nRanged XP", "Long range\nRanged XP\nDefence XP",
				40160, 40164, 40168); // OK

		Sidebar0c(1764, 1767, 7549, "Accurate", "Rapid", "Longrange", 33, 75,
				125, 75, 29, 128, 40, 103, 40, 50, 122, 50, "Accurate\nRanged XP",
				"Rapid\nRanged XP", "Long range\nRanged XP\nDefence XP",
				40172, 40176, 40180); // OK

		Sidebar0c(4446, 4449, 7649, "Accurate", "Rapid", "Longrange", 33, 75,
				125, 75, 29, 128, 40, 103, 40, 50, 122, 50, "Accurate\nRanged XP",
				"Rapid\nRanged XP", "Long range\nRanged XP\nDefence XP",
				40184, 40188, 40192); // OK

		Sidebar0c(5855, 5857, 7749, "Punch", "Kick", "Block", 40, 75, 129, 75,
				42, 128, 40, 50, 122, 50, 40, 103, "Accurate\nCrush\nAttack XP",
				"Aggressive\nCrush\nStrength XP", "Defensive\nCrush\nDefence XP",
				40196, 40200, 40204); // OK

		Sidebar0c(6103, 6132, 6117, "Bash", "Pound", "Block", 43, 75, 124, 75,
				42, 128, 40, 103, 40, 50, 122, 50, "Accurate\nCrush\nAttack XP",
				"Aggressive\nCrush\nStrength XP", "Defensive\nCrush\nDefence XP",
				40208, 40212, 40216); // ???

		Sidebar0c(8460, 8463, 8493, "Jab", "Swipe", "Fend", 46, 75, 124, 75,
				43, 128, 40, 103, 40, 50, 122, 50, "Controlled\nStabbed\nShared XP",
				"Aggressive\nSlash\nStrength XP",
				"Defensive\nStab\nDefence XP",
				40224, 40228, 40232); // OK

		Sidebar0c(12290, 12293, 12323, "Flick", "Lash", "Deflect", 44, 75, 127,
				75, 36, 128, 40, 50, 40, 103, 122, 50, "Accurate\nSlash\nAttack XP",
				"Controlled\nSlash\nShared XP", "Defensive\nSlash\nDefence XP",
				40236, 40240, 40244); // OK

		Sidebar0d(328, 331, "Bash", "Pound", "Focus", 42, 66, 39, 101, 41, 136,
				40, 120, 40, 50, 40, 85);

		RSInterface rsi = addTabInterface(19300);
		textSize(3983, 0);
		addAttackStyleButton2(
				150,
				150,
				172,
				150,
				44,
				"Auto Retaliate",
				40055,
				154,
				42,
				"When active, you will\nautomatically fight back if\nattacked.");

		rsi.totalChildren(3);
		rsi.child(0, 3983, 52, 25); // combat level
		rsi.child(1, 150, 21, 153); // auto retaliate
		rsi.child(2, 40055, 26, 200);

		rsi = interfaceCache[3983];
		rsi.centerText = true;
		rsi.textColor = 0xff981f;
	}

	public static void Sidebar0a(int id, int id2, int id3, String text1,
								 String text2, String text3, String text4, int str1x, int str1y,
								 int str2x, int str2y, int str3x, int str3y, int str4x, int str4y,
								 int img1x, int img1y, int img2x, int img2y, int img3x, int img3y,
								 int img4x, int img4y, String popupString1, String popupString2,
								 String popupString3, String popupString4, int hoverID1,
								 int hoverID2, int hoverID3, int hoverID4) // 4button
	// spec
	{
		RSInterface rsi = addTabInterface(id); // 2423
		addAttackText(id2, "-2", 3, 0xff981f, true); // 2426
		addAttackText(id2 + 11, text1, 0, 0xff981f, false);
		addAttackText(id2 + 12, text2, 0, 0xff981f, false);
		addAttackText(id2 + 13, text3, 0, 0xff981f, false);
		addAttackText(id2 + 14, text4, 0, 0xff981f, false);

		rsi.specialBar(id3); // 7599

		addAttackHover(id2 + 3, hoverID1, popupString1);
		addAttackHover(id2 + 6, hoverID2, popupString2);
		addAttackHover(id2 + 5, hoverID3, popupString3);
		addAttackHover(id2 + 4, hoverID4, popupString4);

		rsi.width = 190;
		rsi.height = 261;

		int frame = 0;
		rsi.totalChildren(20);

		rsi.child(frame, id2 + 3, 21, 46);
		frame++; // 2429
		rsi.child(frame, id2 + 4, 104, 99);
		frame++; // 2430
		rsi.child(frame, id2 + 5, 21, 99);
		frame++; // 2431
		rsi.child(frame, id2 + 6, 105, 46);
		frame++; // 2432

		rsi.child(frame, id2 + 7, img1x, img1y);
		frame++; // bottomright 2433
		rsi.child(frame, id2 + 8, img2x, img2y);
		frame++; // topleft 2434
		rsi.child(frame, id2 + 9, img3x, img3y);
		frame++; // bottomleft 2435
		rsi.child(frame, id2 + 10, img4x, img4y);
		frame++; // topright 2436

		rsi.child(frame, id2 + 11, str1x, str1y);
		frame++; // chop 2437
		rsi.child(frame, id2 + 12, str2x, str2y);
		frame++; // slash 2438
		rsi.child(frame, id2 + 13, str3x, str3y);
		frame++; // lunge 2439
		rsi.child(frame, id2 + 14, str4x, str4y);
		frame++; // block 2440

		rsi.child(frame, id3, 21, 205);
		frame++; // special attack 7599
		rsi.child(frame, 19300, 0, 0);
		frame++; // stuffs
		rsi.child(frame, id2, 94, 4);
		frame++; // weapon 2426
		rsi.child(frame, hoverID1, 25, 96);
		frame++;
		rsi.child(frame, hoverID2, 108, 96);
		frame++;
		rsi.child(frame, hoverID3, 25, 149);
		frame++;
		rsi.child(frame, hoverID4, 108, 149);
		frame++;
		rsi.child(frame, 40005, 28, 149);
		frame++; // special bar tooltip

		for (int i = id2 + 3; i < id2 + 7; i++) { // 2429 - 2433
			rsi = interfaceCache[i];
			rsi.sprite1 = CustomSpriteLoader(19301, "");
			rsi.sprite2 = CustomSpriteLoader(19301, "a");
			rsi.width = 68;
			rsi.height = 44;
		}
	}

	public static void Sidebar0b(int id, int id2, String text1, String text2,
								 String text3, String text4, int str1x, int str1y, int str2x,
								 int str2y, int str3x, int str3y, int str4x, int str4y, int img1x,
								 int img1y, int img2x, int img2y, int img3x, int img3y, int img4x,
								 int img4y, String popupString1, String popupString2,
								 String popupString3, String popupString4, int hoverID1,
								 int hoverID2, int hoverID3, int hoverID4) // 4button
	// nospec
	{
		RSInterface rsi = addTabInterface(id); // 2423
		addAttackText(id2, "-2", 3, 0xff981f, true); // 2426
		addAttackText(id2 + 11, text1, 0, 0xff981f, false);
		addAttackText(id2 + 12, text2, 0, 0xff981f, false);
		addAttackText(id2 + 13, text3, 0, 0xff981f, false);
		addAttackText(id2 + 14, text4, 0, 0xff981f, false);

		addAttackHover(id2 + 3, hoverID1, popupString1);
		addAttackHover(id2 + 6, hoverID2, popupString2);
		addAttackHover(id2 + 5, hoverID3, popupString3);
		addAttackHover(id2 + 4, hoverID4, popupString4);

		rsi.width = 190;
		rsi.height = 261;

		int frame = 0;
		rsi.totalChildren(18);

		rsi.child(frame, id2 + 3, 21, 46);
		frame++; // 2429
		rsi.child(frame, id2 + 4, 104, 99);
		frame++; // 2430
		rsi.child(frame, id2 + 5, 21, 99);
		frame++; // 2431
		rsi.child(frame, id2 + 6, 105, 46);
		frame++; // 2432

		rsi.child(frame, id2 + 7, img1x, img1y);
		frame++; // bottomright 2433
		rsi.child(frame, id2 + 8, img2x, img2y);
		frame++; // topleft 2434
		rsi.child(frame, id2 + 9, img3x, img3y);
		frame++; // bottomleft 2435
		rsi.child(frame, id2 + 10, img4x, img4y);
		frame++; // topright 2436

		rsi.child(frame, id2 + 11, str1x, str1y);
		frame++; // chop 2437
		rsi.child(frame, id2 + 12, str2x, str2y);
		frame++; // slash 2438
		rsi.child(frame, id2 + 13, str3x, str3y);
		frame++; // lunge 2439
		rsi.child(frame, id2 + 14, str4x, str4y);
		frame++; // block 2440

		rsi.child(frame, 19300, 0, 0);
		frame++; // stuffs
		rsi.child(frame, id2, 94, 4);
		frame++; // weapon 2426
		rsi.child(frame, hoverID1, 25, 96);
		frame++;
		rsi.child(frame, hoverID2, 108, 96);
		frame++;
		rsi.child(frame, hoverID3, 25, 149);
		frame++;
		rsi.child(frame, hoverID4, 108, 149);
		frame++;

		for (int i = id2 + 3; i < id2 + 7; i++) { // 2429 - 2433
			rsi = interfaceCache[i];
			rsi.sprite1 = CustomSpriteLoader(19301, "");
			rsi.sprite2 = CustomSpriteLoader(19301, "a");
			rsi.width = 68;
			rsi.height = 44;
		}
	}

	public static void Sidebar0c(int id, int id2, int id3, String text1,
								 String text2, String text3, int str1x, int str1y, int str2x,
								 int str2y, int str3x, int str3y, int img1x, int img1y, int img2x,
								 int img2y, int img3x, int img3y, String popupString1,
								 String popupString2, String popupString3, int hoverID1,
								 int hoverID2, int hoverID3) // 3button spec
	{
		RSInterface rsi = addTabInterface(id); // 2423
		addAttackText(id2, "-2", 3, 0xff981f, true); // 2426
		addAttackText(id2 + 9, text1, 0, 0xff981f, false);
		addAttackText(id2 + 10, text2, 0, 0xff981f, false);
		addAttackText(id2 + 11, text3, 0, 0xff981f, false);

		rsi.specialBar(id3); // 7599

		addAttackHover(id2 + 5, hoverID1, popupString1);
		addAttackHover(id2 + 4, hoverID2, popupString2);
		addAttackHover(id2 + 3, hoverID3, popupString3);

		rsi.width = 190;
		rsi.height = 261;

		int frame = 0;
		rsi.totalChildren(16);

		rsi.child(frame, id2 + 3, 21, 99);
		frame++;
		rsi.child(frame, id2 + 4, 105, 46);
		frame++;
		rsi.child(frame, id2 + 5, 21, 46);
		frame++;

		rsi.child(frame, id2 + 6, img1x, img1y);
		frame++; // topleft
		rsi.child(frame, id2 + 7, img2x, img2y);
		frame++; // bottomleft
		rsi.child(frame, id2 + 8, img3x, img3y);
		frame++; // topright

		rsi.child(frame, id2 + 9, str1x, str1y);
		frame++; // chop
		rsi.child(frame, id2 + 10, str2x, str2y);
		frame++; // slash
		rsi.child(frame, id2 + 11, str3x, str3y);
		frame++; // lunge

		rsi.child(frame, id3, 21, 205);
		frame++; // special attack 7599
		rsi.child(frame, 19300, 0, 0);
		frame++; // stuffs
		rsi.child(frame, id2, 94, 4);
		frame++; // weapon

		rsi.child(frame, hoverID1, 25, 96);
		frame++;
		rsi.child(frame, hoverID2, 108, 96);
		frame++;
		rsi.child(frame, hoverID3, 25, 149);
		frame++;
		rsi.child(frame, 40005, 28, 149);
		frame++; // special bar tooltip

		for (int i = id2 + 3; i < id2 + 6; i++) {
			rsi = interfaceCache[i];
			rsi.sprite1 = CustomSpriteLoader(19301, "");
			rsi.sprite2 = CustomSpriteLoader(19301, "a");
			rsi.width = 68;
			rsi.height = 44;
		}
	}

	public static void Sidebar0d(int id, int id2, String text1, String text2,
								 String text3, int str1x, int str1y, int str2x, int str2y,
								 int str3x, int str3y, int img1x, int img1y, int img2x, int img2y,
								 int img3x, int img3y) // 3button nospec
	// (magic intf)
	{
		RSInterface rsi = addTabInterface(id); // 2423
		addAttackText(id2, "-2", 3, 0xff981f, true); // 2426
		addAttackText(id2 + 9, text1, 0, 0xff981f, false);
		addAttackText(id2 + 10, text2, 0, 0xff981f, false);
		addAttackText(id2 + 11, text3, 0, 0xff981f, false);

		addAttackText(353, "Spell", 0, 0xff981f, false);
		addAttackText(354, "Spell", 0, 0xff981f, false);

		addCacheSprite(337, 19, 0, "combaticons");
		addCacheSprite(338, 13, 0, "combaticons2");
		addCacheSprite(339, 14, 0, "combaticons2");

		/* addToggleButton(id, sprite, config, width, height, tooltip); */
		// addToggleButton(349, 349, 108, 68, 44, "Select");
		removeSomething(349);
		addToggleButton(350, 350, 108, 68, 44, "Select");

		rsi.width = 190;
		rsi.height = 261;

		int last = 15;
		int frame = 0;
		rsi.totalChildren(last);

		rsi.child(frame, id2 + 3, 20, 115);
		frame++;
		rsi.child(frame, id2 + 4, 20, 80);
		frame++;
		rsi.child(frame, id2 + 5, 20, 45);
		frame++;

		rsi.child(frame, id2 + 6, img1x, img1y);
		frame++; // topleft
		rsi.child(frame, id2 + 7, img2x, img2y);
		frame++; // bottomleft
		rsi.child(frame, id2 + 8, img3x, img3y);
		frame++; // topright

		rsi.child(frame, id2 + 9, str1x, str1y);
		frame++; // bash
		rsi.child(frame, id2 + 10, str2x, str2y);
		frame++; // pound
		rsi.child(frame, id2 + 11, str3x, str3y);
		frame++; // focus

		rsi.child(frame, 349, 105, 46);
		frame++; // spell1
		rsi.child(frame, 350, 104, 106);
		frame++; // spell2

		rsi.child(frame, 353, 125, 74);
		frame++; // spell
		rsi.child(frame, 354, 125, 134);
		frame++; // spell

		rsi.child(frame, 19300, 0, 0);
		frame++; // stuffs
		rsi.child(frame, id2, 94, 4);
		frame++; // weapon
	}

	public static void addToggleButton(int id, int sprite, int setconfig,
									   int width, int height, String s) {
		RSInterface rsi = addInterface(id, 512, 334);
		rsi.sprite1 = CustomSpriteLoader(sprite, "");
		rsi.sprite2 = CustomSpriteLoader(sprite, "a");
		rsi.requiredValues = new int[1];
		rsi.requiredValues[0] = 1;
		rsi.valueCompareType = new int[1];
		rsi.valueCompareType[0] = 1;
		rsi.valueIndexArray = new int[1][3];
		rsi.valueIndexArray[0][0] = 5;
		rsi.valueIndexArray[0][1] = setconfig;
		rsi.valueIndexArray[0][2] = 0;
		rsi.atActionType = 4;
		rsi.width = width;
		rsi.mOverInterToTrigger = -1;
		rsi.parentID = id;
		rsi.id = id;
		rsi.type = 5;
		rsi.height = height;
		rsi.tooltip = s;
	}

	/*
     * protected static Sprite CustomSpriteLoader(int id, String s) { long l =
	 * (TextClass.method585(s) << 8) + id; Sprite sprite = null; String
	 * spriteFile = id+s; String spriteName = ""; int cacheSprite = -1; int
	 * actual = -1; for (int i = 0; i < fixedSprites.length; i++) { spriteName =
	 * fixedSprites[i][0]; cacheSprite = Integer.parseInt(fixedSprites[i][1]);
	 * if (spriteFile.equalsIgnoreCase(spriteName)) { actual = cacheSprite;
	 * continue; } } if (actual == -1) try { sprite = new Sprite(s+" "+id);
	 * aMRUNodes_238.removeFromCache(sprite, l); return sprite; }
	 * catch(Exception exception) { return null; } sprite =
	 * client.instance.cacheSprite[actual]; if (sprite != null) return sprite;
	 * return null; }
	 */

	@SuppressWarnings("unused")
	public static void removeSomething(int id) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
	}

	public static void addAttackHover(int id, int hoverID, String hoverText) {
		RSInterface rsi = interfaceCache[id];
		rsi.mOverInterToTrigger = hoverID;

		rsi = addInterface(hoverID, 512, 334);
		rsi.isMouseoverTriggered = true;
		rsi.type = 0;
		rsi.atActionType = 0;
		rsi.mOverInterToTrigger = -1;
		rsi.parentID = hoverID;
		rsi.id = hoverID;
		addBox(hoverID + 1, 0, false, 0x000000, hoverText);
		setChildren(1, rsi);
		setBounds(hoverID + 1, 0, 0, 0, rsi);
	}

	public static void addAttackText(int id, String text,
									 int idx, int color, boolean centered) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		if (centered)
			rsi.centerText = true;
		rsi.textShadow = true;
		rsi.textDrawingAreas = idx;
		rsi.message = text;
		rsi.textColor = color;
		rsi.id = id;
		rsi.type = 4;
	}

	public static void addAttackStyleButton2(int id, int sprite, int setconfig, int width, int height, String s, int hoverID, int hW, int hH, String hoverText) {
		RSInterface rsi = addInterface(id, 512, 334);
		rsi.sprite1 = CustomSpriteLoader(sprite, "");
		rsi.sprite2 = CustomSpriteLoader(sprite, "a");
		rsi.valueCompareType = new int[1];
		rsi.valueCompareType[0] = 1;
		rsi.requiredValues = new int[1];
		rsi.requiredValues[0] = 1;
		rsi.valueIndexArray = new int[1][3];
		rsi.valueIndexArray[0][0] = 5;
		rsi.valueIndexArray[0][1] = setconfig;
		rsi.valueIndexArray[0][2] = 0;
		rsi.atActionType = 4;
		rsi.width = width;
		rsi.mOverInterToTrigger = hoverID;
		rsi.parentID = id;
		rsi.id = id;
		rsi.type = 5;
		rsi.height = height;
		rsi.tooltip = s;
		rsi = addInterface(hoverID, 512, 334);
		rsi.isMouseoverTriggered = true;
		rsi.type = 0;
		rsi.atActionType = 0;
		rsi.mOverInterToTrigger = -1;
		rsi.parentID = hoverID;
		rsi.id = hoverID;
		addBox(hoverID + 1, 0, false, 0x000000, hoverText);
		setChildren(1, rsi);
		setBounds(hoverID + 1, 0, 0, 0, rsi);
	}

	public static void addBox(int id, int byte1, boolean filled, int color, String text) {
		RSInterface Interface = addInterface(id, 512, 334);
		Interface.id = id;
		Interface.parentID = id;
		Interface.type = 9;
		Interface.opacity = (byte) byte1;
		Interface.filled = filled;
		Interface.mOverInterToTrigger = -1;
		Interface.atActionType = 0;
		Interface.contentType = 0;
		Interface.textColor = color;
		Interface.message = text;
	}

	public static void addHoverBox(int id, int ParentID, String text,
								   String text2, int configId, int configFrame) {
		RSInterface rsi = addTabInterface(id);
		rsi.id = id;
		rsi.parentID = ParentID;
		rsi.type = 8;
		rsi.enabledText = text;
		rsi.message = text2;
		rsi.valueCompareType = new int[1];
		rsi.requiredValues = new int[1];
		rsi.valueCompareType[0] = 1;
		rsi.requiredValues[0] = configId;
		rsi.valueIndexArray = new int[1][3];
		rsi.valueIndexArray[0][0] = 5;
		rsi.valueIndexArray[0][1] = configFrame;
		rsi.valueIndexArray[0][2] = 0;
	}

	public static void addHover(int i, int aT, int cT, int hoverid, int sId,
								String NAME, int W, int H, String tip) {
		RSInterface hover = addTabInterface(i);
		hover.id = i;
		hover.parentID = i;
		hover.type = 5;
		hover.atActionType = aT;
		hover.contentType = cT;
		hover.mOverInterToTrigger = hoverid;
		hover.sprite1 = imageLoader(sId, NAME);
		hover.width = W;
		hover.height = H;
		hover.tooltip = tip;
	}

	public static void addSummonHovered(int i, int j, String imageName, int w, int h, int IMAGEID, int index) {
		RSInterface hover = addTabInterface(i);
		hover.parentID = i;
		hover.id = i;
		hover.type = 0;
		hover.atActionType = 0;
		hover.width = w;
		hover.height = h;
		hover.isMouseoverTriggered = true;
		hover.mOverInterToTrigger = -1;
		hover.summonReq = summoningLevelRequirements[index];
		addSprite(IMAGEID, j, imageName);
		setChildren(1, hover);
		setBounds(IMAGEID, 0, 0, 0, hover);
	}

	public static void addHovered(int i, int j, String imageName, int w, int h,
								  int IMAGEID) {
		RSInterface hover = addTabInterface(i);
		hover.parentID = i;
		hover.id = i;
		hover.type = 0;
		hover.atActionType = 0;
		hover.width = w;
		hover.height = h;
		hover.isMouseoverTriggered = true;
		hover.mOverInterToTrigger = -1;
		addSprite(IMAGEID, j, imageName);
		setChildren(1, hover);
		setBounds(IMAGEID, 0, 0, 0, hover);
	}

	public static void addButton(int i, int j, String name, int W, int H,
								 String S, int AT) {
		RSInterface RSInterface = addInterface(i, 512, 334);
		RSInterface.id = i;
		RSInterface.parentID = i;
		RSInterface.type = 5;
		RSInterface.atActionType = AT;
		RSInterface.contentType = 0;
		RSInterface.opacity = 0;
		RSInterface.mOverInterToTrigger = 52;
		RSInterface.sprite1 = imageLoader(j, name);
		RSInterface.width = W;
		RSInterface.height = H;
		RSInterface.tooltip = S;
	}

	public static void Trade() {
		RSInterface Interface = addTabInterface(3323);
		setChildren(15, Interface);
		addSprite(3324, 6, "Interfaces/Bank/TRADE");
		addHover(3442, 3, 0, 3325, 1, "Interfaces/Bank/BANK", 17, 17,
				"Close Window");
		addHovered(3325, 2, "Interfaces/Bank/BANK", 17, 17, 3326);
		addText(3417, "Trading With:", 0xFF9933, true, true, 52, 2);
		addText(3418, "Trader's Offer", 0xFF9933, false, true, 52, 1);
		addText(3419, "Your Offer", 0xFF9933, false, true, 52, 1);
		addText(3421, "Accept", 0x00C000, true, true, 52, 1);
		addText(3423, "Decline", 0xC00000, true, true, 52, 1);
		addText(3431, "Waiting For Other Player", 0xFFFFFF, true, true, 52,
				1);
		addHover(3420, 1, 0, 3327, 5, "Interfaces/Bank/TRADE", 65, 32, "Accept");
		addHovered(3327, 2, "Interfaces/Bank/TRADE", 65, 32, 3328);
		addHover(3422, 3, 0, 3329, 5, "Interfaces/Bank/TRADE", 65, 32,
				"Close Window");
		addHovered(3329, 2, "Interfaces/Bank/TRADE", 65, 32, 3330);
		setBounds(3324, 0, 16, 0, Interface);
		setBounds(3442, 485, 24, 1, Interface);
		setBounds(3325, 485, 24, 2, Interface);
		setBounds(3417, 258, 25, 3, Interface);
		setBounds(3418, 355, 51, 4, Interface);
		setBounds(3419, 68, 51, 5, Interface);
		setBounds(3420, 223, 120, 6, Interface);
		setBounds(3327, 223, 120, 7, Interface);
		setBounds(3422, 223, 160, 8, Interface);
		setBounds(3329, 223, 160, 9, Interface);
		setBounds(3421, 256, 127, 10, Interface);
		setBounds(3423, 256, 167, 11, Interface);
		setBounds(3431, 256, 272, 12, Interface);
		setBounds(3415, 12, 64, 13, Interface);
		setBounds(3416, 321, 64, 14, Interface);
		Interface = addTabInterface(3443);
		setChildren(15, Interface);
		addSprite(3444, 3, "Interfaces/Bank/TRADE");
		addButton(3546, 2, "Interfaces/Bank/SHOP", 63, 24, "Accept", 1);
		addButton(3548, 2, "Interfaces/Bank/SHOP", 63, 24, "Decline", 3);
		addText(3547, "Accept", 0x00C000, true, true, 52, 1);
		addText(3549, "Decline", 0xC00000, true, true, 52, 1);
		addText(3450, "Trading With:", 0x00FFFF, true, true, 52, 2);
		addText(3451, "Yourself", 0x00FFFF, true, true, 52, 2);
		setBounds(3444, 12, 20, 0, Interface);
		setBounds(3442, 470, 32, 1, Interface);
		setBounds(3325, 470, 32, 2, Interface);
		setBounds(3535, 130, 28, 3, Interface);
		setBounds(3536, 105, 47, 4, Interface);
		setBounds(3546, 189, 295, 5, Interface);
		setBounds(3548, 258, 295, 6, Interface);
		setBounds(3547, 220, 299, 7, Interface);
		setBounds(3549, 288, 299, 8, Interface);
		setBounds(3557, 71, 87, 9, Interface);
		setBounds(3558, 315, 87, 10, Interface);
		setBounds(3533, 64, 70, 11, Interface);
		setBounds(3534, 297, 70, 12, Interface);
		setBounds(3450, 95, 289, 13, Interface);
		setBounds(3451, 95, 304, 14, Interface);
	}

	public static void addConfigButton(int ID, int pID, int bID, int bID2, String bName, int width, int height, String tooltip, int requiredValue, int actionType, int configFrame) {
		RSInterface Tab = addTabInterface(ID);
		Tab.parentID = pID;
		Tab.id = ID;
		Tab.type = 5;
		Tab.atActionType = actionType;
		Tab.contentType = 0;
		Tab.width = width;
		Tab.height = height;
		Tab.opacity = 0;

		Tab.valueCompareType = new int[1];
		Tab.valueCompareType[0] = 1;

		Tab.requiredValues = new int[1];
		Tab.requiredValues[0] = requiredValue;

		Tab.valueIndexArray = new int[1][3];
		Tab.valueIndexArray[0][0] = 5;
		Tab.valueIndexArray[0][1] = configFrame;
		Tab.valueIndexArray[0][2] = 0;

		Tab.sprite1 = imageLoader(bID, bName);
		Tab.sprite2 = imageLoader(bID2, bName);
		Tab.tooltip = tooltip;
	}

	public static void addTransparentSprite(int id, int spriteId, String spriteName, int transparency) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.transparency = (byte) transparency;
		tab.sprite1 = imageLoader(spriteId, spriteName);
		tab.width = 512;
		tab.height = 334;
	}

	public static void addButton(int id, int sid, String spriteName,
								 String tooltip) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 1;
		tab.contentType = 0;
		tab.opacity = (byte) 0;
		tab.mOverInterToTrigger = 52;
		tab.sprite1 = imageLoader(sid, spriteName);
		tab.width = tab.sprite1.myWidth;
		tab.height = tab.sprite1.myHeight;
		tab.tooltip = tooltip;
	}

	public static void textColor(int id, int color) {
		RSInterface rsi = interfaceCache[id];
		rsi.textColor = color;
	}

	public static void textSize(int id, int idx) {
		RSInterface rsi = interfaceCache[id];
		rsi.textDrawingAreas = idx;
	}

	public static void addCacheSprite(int id, int sprite1, int sprite2,
									  String sprites) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		rsi.sprite1 = method207(sprite1, aClass44, sprites);
		rsi.parentID = id;
		rsi.id = id;
		rsi.type = 5;
	}

	public static void sprite1(int id, int sprite) {
		RSInterface class9 = interfaceCache[id];
		class9.sprite1 = CustomSpriteLoader(sprite, "");
	}

	public static void addActionButton(int id, int sprite, int sprite2,
									   int width, int height, String s) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		rsi.sprite1 = CustomSpriteLoader(sprite, "");
		rsi.tooltip = s;
		rsi.contentType = 0;
		rsi.atActionType = 1;
		rsi.width = width;
		rsi.mOverInterToTrigger = 52;
		rsi.parentID = id;
		rsi.id = id;
		rsi.type = 5;
		rsi.height = height;
	}

	protected static Sprite CustomSpriteLoader(int id, String s) {
		String spriteFile = id + s;
		String spriteName = "";
		int cacheSprite = -1;
		int actual = -1;
		for (int i = 0; i < fixedSprites2.length; i++) {
			spriteName = fixedSprites2[i][0];
			cacheSprite = Integer.parseInt(fixedSprites2[i][1]);
			if (spriteFile.equalsIgnoreCase(spriteName)) {
				actual = cacheSprite;
				continue;
			}
		}
		if (actual == -1)
			return null;
		Sprite sprite = Client.instance.cacheSprite[actual];
		if (sprite != null)
			return sprite;
		return null;
	}//Client.instance.cacheSprite

	public static void addCacheSprite(int i, int id) {
		RSInterface rsinterface = interfaceCache[i] = new RSInterface();
		rsinterface.id = i;
		rsinterface.parentID = i;
		rsinterface.type = 5;
		rsinterface.atActionType = 1;
		rsinterface.contentType = 0;
		rsinterface.width = 20;
		rsinterface.height = 20;
		rsinterface.opacity = 0;
		rsinterface.mOverInterToTrigger = 52;
		rsinterface.sprite1 = Client.instance.cacheSprite[id];
	}

	public static void priceChecker() {
		RSInterface rsi = addTabInterface(43933);
		addSprite(18245, 1, "Interfaces/Checker/CHECK");
		addPriceChecker(18246);
		addHoverButton(18247, "/Interfaces/Shop/CLOSE", 2, 21, 21, "Close",
				250, 18247, 3);
		addHoveredButton(18248, "/Interfaces/Shop/CLOSE", 1, 21, 21, 18248);
		rsi.totalChildren(67);
		rsi.child(0, 18245, 10, 20);// was 10 so + 10
		rsi.child(1, 18246, 100, 56);
		rsi.child(2, 18247, 472, 23);
		rsi.child(3, 18248, 472, 23);
		addText(18350, "Total value:", 0, 0xFFFFFF, false, true, 0);
		rsi.child(4, 18350, 225, 295); // Open Text
		addText(18351, "0", 0, 0xFFFFFF, true, true, 0);
		rsi.child(5, 18351, 251, 306);
		addText(18352, "", 0, 0xFFFFFF, false, true, 0);
		rsi.child(6, 18352, 120, 150);
		addText(18353, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(7, 18353, 120, 85);
		addText(18354, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(8, 18354, 120, 95);
		addText(18355, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(9, 18355, 120, 105);
		addText(18356, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(10, 18356, 190, 85);
		addText(18357, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(11, 18357, 190, 95);
		addText(18358, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(12, 18358, 190, 105);
		addText(18359, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(13, 18359, 260, 85);
		addText(18360, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(14, 18360, 260, 95);
		addText(18361, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(15, 18361, 260, 105);
		addText(18362, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(16, 18362, 330, 85);
		addText(18363, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(17, 18363, 330, 95);
		addText(18364, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(18, 18364, 330, 105);
		addText(18365, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(19, 18365, 400, 85);
		addText(18366, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(20, 18366, 400, 95);
		addText(18367, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(21, 18367, 400, 105);
		addText(18368, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(22, 18368, 120, 145);
		addText(18369, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(23, 18369, 120, 155);
		addText(18370, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(24, 18370, 120, 165);
		addText(18371, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(25, 18371, 190, 145);
		addText(18372, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(26, 18372, 190, 155);
		addText(18373, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(27, 18373, 190, 165);
		addText(18374, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(28, 18374, 260, 145);
		addText(18375, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(29, 18375, 260, 155);
		addText(18376, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(30, 18376, 260, 165);
		addText(18377, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(31, 18377, 330, 145);
		addText(18378, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(32, 18378, 330, 155);
		addText(18379, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(33, 18379, 330, 165);
		addText(18380, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(34, 18380, 400, 145);
		addText(18381, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(35, 18381, 400, 155);
		addText(18382, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(36, 18382, 400, 165);
		addText(18383, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(37, 18383, 120, 205);
		addText(18384, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(38, 18384, 120, 215);
		addText(18385, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(39, 18385, 120, 225);
		addText(18386, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(40, 18386, 190, 205);
		addText(18387, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(41, 18387, 190, 215);
		addText(18388, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(42, 18388, 190, 225);
		addText(18389, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(43, 18389, 260, 205);
		addText(18390, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(44, 18390, 260, 215);
		addText(18391, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(45, 18391, 260, 225);
		addText(18392, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(46, 18392, 330, 205);
		addText(18393, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(47, 18393, 330, 215);
		addText(18394, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(48, 18394, 330, 225);
		addText(18395, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(49, 18395, 400, 205);
		addText(18396, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(50, 18396, 400, 215);
		addText(18397, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(51, 18397, 400, 225);
		addText(18398, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(52, 18398, 120, 260);
		addText(18399, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(53, 18399, 120, 270);
		addText(18400, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(54, 18400, 120, 280);
		addText(18401, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(55, 18401, 190, 260);
		addText(18402, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(56, 18402, 190, 270);
		addText(18403, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(57, 18403, 190, 280);
		addText(18404, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(58, 18404, 260, 260);
		addText(18405, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(59, 18405, 260, 270);
		addText(18406, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(60, 18406, 260, 280);
		addText(18407, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(61, 18407, 330, 260);
		addText(18408, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(62, 18408, 330, 270);
		addText(18409, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(63, 18409, 330, 280);
		addText(18410, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(64, 18410, 400, 260);
		addText(18411, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(65, 18411, 400, 270);
		addText(18412, "", 0, 0xFFFFFF, true, true, 0);
		rsi.child(66, 18412, 400, 280);
	}

	public static void addPriceChecker(int index) {
		RSInterface rsi = interfaceCache[index] = new RSInterface();
		rsi.actions = new String[10];
		rsi.invStackSizes = new int[30];
		rsi.inv = new int[30];
		rsi.children = new int[0];
		rsi.childX = new int[0];
		rsi.childY = new int[0];
		rsi.actions[0] = "Take 1";
		rsi.actions[1] = "Take 5";
		rsi.actions[2] = "Take 10";
		rsi.actions[3] = "Take All";
		rsi.actions[4] = "Take X";
		rsi.centerText = true;
		rsi.filled = false;
		rsi.itemsDraggable = false;
		rsi.usableItemInterface = false;
		rsi.isInventoryInterface = false;
		rsi.aBoolean259 = true;
		rsi.textShadow = false;
		rsi.invSpritePadX = 40;
		rsi.invSpritePadY = 28;
		rsi.height = 5;
		rsi.width = 5;
		rsi.parentID = 18246;
		rsi.id = 4393;
		rsi.type = 2;
	}

	public static void addChar(int ID, int zoom, int clientCode) {
		RSInterface t = interfaceCache[ID] = new RSInterface();
		t.id = ID;
		t.parentID = ID;
		t.type = 6;
		t.atActionType = 0;
		t.contentType = clientCode;
		t.width = 165;
		t.height = 168;
		t.opacity = 0;
		t.mOverInterToTrigger = 0;
		t.modelZoom = zoom;
		t.rotationX = 150;
		t.rotationY = 150;
		t.disabledAnimation = -1;
		t.enabledAnimation = -1;
	}
	
	public static void addRecoloredModel(int ID, int zoom) {
		RSInterface t = interfaceCache[ID] = new RSInterface();
		t.id = ID;
		t.parentID = ID;
		t.type = 6;
		t.atActionType = 0;
		t.width = 165;
		t.height = 168;
		t.opacity = 0;
		t.mOverInterToTrigger = 0;
		t.modelZoom = zoom;
		t.rotationX = 0;
		t.rotationY = 1024;
		t.mediaType = 6;
		t.mediaID = 20767;
		t.disabledAnimation = -1;
		t.enabledAnimation = -1;
	}

	public static void addLunarSprite(int i, int j, String name) {
		RSInterface RSInterface = addInterface(i, 512, 334);
		RSInterface.id = i;
		RSInterface.parentID = i;
		RSInterface.type = 5;
		RSInterface.atActionType = 0;
		RSInterface.contentType = 0;
		RSInterface.opacity = 0;
		RSInterface.mOverInterToTrigger = 52;
		RSInterface.sprite1 = imageLoader(j, name);
		RSInterface.width = 500;
		RSInterface.height = 500;
		RSInterface.tooltip = "";
	}

	public static void drawRune(int i, int id) {
		RSInterface RSInterface = addInterface(i, 512, 334);
		RSInterface.type = 5;
		RSInterface.atActionType = 0;
		RSInterface.contentType = 0;
		RSInterface.opacity = 0;
		RSInterface.mOverInterToTrigger = 52;
		RSInterface.sprite1 = imageLoader(id, "Lunar/RUNE");
		RSInterface.width = 500;
		RSInterface.height = 500;
	}

	public static void addRuneText(int ID, int runeAmount, int RuneID) {
		RSInterface rsInterface = addInterface(ID, 512, 334);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 4;
		rsInterface.atActionType = 0;
		rsInterface.contentType = 0;
		rsInterface.width = 0;
		rsInterface.height = 14;
		rsInterface.opacity = 0;
		rsInterface.mOverInterToTrigger = -1;
		rsInterface.valueCompareType = new int[1];
		rsInterface.requiredValues = new int[1];
		rsInterface.valueCompareType[0] = 3;
		rsInterface.requiredValues[0] = runeAmount == 1 ? 0 : runeAmount;
		rsInterface.valueIndexArray = new int[1][4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = RuneID;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.centerText = true;
		rsInterface.textDrawingAreas = 0;
		rsInterface.textShadow = true;
		rsInterface.message = "%1/" + runeAmount + "";
		rsInterface.enabledText = "";
		rsInterface.textColor = 12582912;
		rsInterface.anInt219 = 49152;
	}

	public static void warriorsGuild() {
		RSInterface RSinterface = addInterface(21400, 512, 334);
		// addSprite(21401, 0, "PvP/INCOUNT1");
		addText(21401, "", 1, 0xff9040, true, true, 0);
		//addText(21403, "", tda, 1, 0xffffff, true, true);
		int last = 1;
		RSinterface.children = new int[last];
		RSinterface.childX = new int[last];
		RSinterface.childY = new int[last];
		//setBounds(21401, 400, 285, 0, RSinterface);
		setBounds(21401, 444, 318, 0, RSinterface);
		//setBounds(21403, 412, 290, 2, RSinterface);
	}

	public static void homeTeleport() {
		RSInterface RSInterface = addTabInterface(30000);
		RSInterface.tooltip = "Cast <col=00ff00>Lunar Home Teleport";
		RSInterface.id = 30000;
		RSInterface.parentID = 30000;
		RSInterface.type = 5;
		RSInterface.atActionType = 5;
		RSInterface.contentType = 0;
		RSInterface.opacity = 0;
		RSInterface.mOverInterToTrigger = 30001;
		RSInterface.sprite1 = imageLoader(1, "Lunar/SPRITE");
		RSInterface.width = 20;
		RSInterface.height = 20;
		RSInterface hover = addTabInterface(30001);
		hover.isMouseoverTriggered = true;
		setChildren(1, hover);
		addLunarSprite(30002, 0, "Lunar/SPRITE");
		setBounds(30002, 0, 0, 0, hover);
	}

	public static void addLunar2RunesSmallBox(int ID, int r1, int r2, int ra1,
											  int ra2, int rune1, int lvl, String name, String descr,
											  int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID, 512, 334);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mOverInterToTrigger = ID + 1;
		rsInterface.targetBitMask = suo;
		rsInterface.targetVerb = "Cast On";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast <col=00ff00>" + name;
		rsInterface.targetName = name;
		rsInterface.valueCompareType = new int[3];
		rsInterface.requiredValues = new int[3];
		rsInterface.valueCompareType[0] = 3;
		rsInterface.requiredValues[0] = ra1;
		rsInterface.valueCompareType[1] = 3;
		rsInterface.requiredValues[1] = ra2;
		rsInterface.valueCompareType[2] = 3;
		rsInterface.requiredValues[2] = lvl;
		rsInterface.valueIndexArray = new int[3][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[3];
		rsInterface.valueIndexArray[2][0] = 1;
		rsInterface.valueIndexArray[2][1] = 6;
		rsInterface.valueIndexArray[2][2] = 0;
		rsInterface.sprite2 = imageLoader(sid, "Lunar/LUNARON");
		rsInterface.sprite1 = imageLoader(sid, "Lunar/LUNAROFF");
		RSInterface INT = addInterface(ID + 1, 512, 334);
		INT.isMouseoverTriggered = true;
		INT.mOverInterToTrigger = -1;
		setChildren(7, INT);
		addLunarSprite(ID + 2, 0, "Lunar/BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true,
				true, 52, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, 0);
		setBounds(ID + 4, 90, 19, 2, INT);
		setBounds(30016, 37, 35, 3, INT);// Rune
		setBounds(rune1, 112, 35, 4, INT);// Rune
		addRuneText(ID + 5, ra1 + 1, r1);
		setBounds(ID + 5, 50, 66, 5, INT);
		addRuneText(ID + 6, ra2 + 1, r2);
		setBounds(ID + 6, 123, 66, 6, INT);

	}

	public static void addText(int id, String text, int idx,
							   int color) {
		RSInterface rsinterface = addTabInterface(id);
		rsinterface.id = id;
		rsinterface.parentID = id;
		rsinterface.type = 4;
		rsinterface.atActionType = 0;
		rsinterface.width = 174;
		rsinterface.height = 11;
		rsinterface.contentType = 0;
		rsinterface.opacity = 0;
		rsinterface.mOverInterToTrigger = -1;
		rsinterface.centerText = false;
		rsinterface.textShadow = true;
		rsinterface.textDrawingAreas = idx;
		rsinterface.message = text;
		rsinterface.textColor = color;
		rsinterface.anInt219 = 0;
		rsinterface.textHoverColor = 0;
		rsinterface.anInt239 = 0;
	}

	public static RSInterface addText(int i, String s, int k, boolean l, boolean m,
							   int a, int j) {
		RSInterface RSInterface = addInterface(i, 512, 334);
		RSInterface.parentID = i;
		RSInterface.id = i;
		RSInterface.type = 4;
		RSInterface.atActionType = 0;
		RSInterface.width = 0;
		RSInterface.height = 0;
		RSInterface.contentType = 0;
		RSInterface.opacity = 0;
		RSInterface.mOverInterToTrigger = a;
		RSInterface.centerText = l;
		RSInterface.textShadow = m;
		RSInterface.textDrawingAreas = j;
		RSInterface.message = s;
		RSInterface.textColor = k;
		return RSInterface;
	}

	public static void addText(int i, String s, int k, boolean l, boolean m, int a, int j, int dsc) {
		RSInterface rsinterface = addTabInterface(i);
		rsinterface.parentID = i;
		rsinterface.id = i;
		rsinterface.type = 4;
		rsinterface.atActionType = 1;
		rsinterface.width = 174;
		rsinterface.height = 11;
		rsinterface.contentType = 0;
		rsinterface.opacity = 0;
		rsinterface.mOverInterToTrigger = a;
		rsinterface.centerText = l;
		rsinterface.textShadow = m;
		rsinterface.textDrawingAreas = j;
		rsinterface.message = s;
		rsinterface.anInt219 = 0;
		rsinterface.textColor = k;
		rsinterface.textHoverColor = dsc;
		rsinterface.tooltip = s;
	}

	public static void addText(int id, String text, int idx,
							   int color, boolean centered) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		if (centered)
			rsi.centerText = true;
		rsi.textShadow = true;
		rsi.textDrawingAreas = idx;
		rsi.message = text;
		rsi.textColor = color;
		rsi.id = id;
		rsi.type = 4;
	}

	public static void addLunar3RunesSmallBox(int ID, int r1, int r2, int r3,
											  int ra1, int ra2, int ra3, int rune1, int rune2, int lvl,
											  String name, String descr, int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID, 512, 334);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mOverInterToTrigger = ID + 1;
		rsInterface.targetBitMask = suo;
		rsInterface.targetVerb = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast <col=00ff00>" + name;
		rsInterface.targetName = name;
		rsInterface.valueCompareType = new int[4];
		rsInterface.requiredValues = new int[4];
		rsInterface.valueCompareType[0] = 3;
		rsInterface.requiredValues[0] = ra1;
		rsInterface.valueCompareType[1] = 3;
		rsInterface.requiredValues[1] = ra2;
		rsInterface.valueCompareType[2] = 3;
		rsInterface.requiredValues[2] = ra3;
		rsInterface.valueCompareType[3] = 3;
		rsInterface.requiredValues[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 6;
		rsInterface.valueIndexArray[3][2] = 0;

		rsInterface.sprite2 = imageLoader(sid, "Lunar/LUNARON");
		rsInterface.sprite1 = imageLoader(sid, "Lunar/LUNAROFF");

		RSInterface INT = addInterface(ID + 1, 512, 334);
		INT.isMouseoverTriggered = true;
		INT.mOverInterToTrigger = -1;
		setChildren(9, INT);
		addLunarSprite(ID + 2, 0, "Lunar/BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true,
				true, 52, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, 0);
		setBounds(ID + 4, 90, 19, 2, INT);
		setBounds(30016, 14, 35, 3, INT);
		setBounds(rune1, 74, 35, 4, INT);
		setBounds(rune2, 130, 35, 5, INT);
		addRuneText(ID + 5, ra1 + 1, r1);
		setBounds(ID + 5, 26, 66, 6, INT);
		addRuneText(ID + 6, ra2 + 1, r2);
		setBounds(ID + 6, 87, 66, 7, INT);
		addRuneText(ID + 7, ra3 + 1, r3);
		setBounds(ID + 7, 142, 66, 8, INT);
	}

	public static void addLunar3RunesBigBox(int ID, int r1, int r2, int r3,
											int ra1, int ra2, int ra3, int rune1, int rune2, int lvl,
											String name, String descr, int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID, 512, 334);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mOverInterToTrigger = ID + 1;
		rsInterface.targetBitMask = suo;
		rsInterface.targetVerb = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast <col=00ff00>" + name;
		rsInterface.targetName = name;
		rsInterface.valueCompareType = new int[4];
		rsInterface.requiredValues = new int[4];
		rsInterface.valueCompareType[0] = 3;
		rsInterface.requiredValues[0] = ra1;
		rsInterface.valueCompareType[1] = 3;
		rsInterface.requiredValues[1] = ra2;
		rsInterface.valueCompareType[2] = 3;
		rsInterface.requiredValues[2] = ra3;
		rsInterface.valueCompareType[3] = 3;
		rsInterface.requiredValues[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 6;
		rsInterface.valueIndexArray[3][2] = 0;
		rsInterface.sprite2 = imageLoader(sid, "Lunar/LUNARON");
		rsInterface.sprite1 = imageLoader(sid, "Lunar/LUNAROFF");
		RSInterface INT = addInterface(ID + 1, 512, 334);
		INT.isMouseoverTriggered = true;
		INT.mOverInterToTrigger = -1;
		setChildren(9, INT);
		addLunarSprite(ID + 2, 1, "Lunar/BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true,
				true, 52, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, 0);
		setBounds(ID + 4, 90, 21, 2, INT);
		setBounds(30016, 14, 48, 3, INT);
		setBounds(rune1, 74, 48, 4, INT);
		setBounds(rune2, 130, 48, 5, INT);
		addRuneText(ID + 5, ra1 + 1, r1);
		setBounds(ID + 5, 26, 79, 6, INT);
		addRuneText(ID + 6, ra2 + 1, r2);
		setBounds(ID + 6, 87, 79, 7, INT);
		addRuneText(ID + 7, ra3 + 1, r3);
		setBounds(ID + 7, 142, 79, 8, INT);
	}

	public static void addLunar3RunesLargeBox(int ID, int r1, int r2, int r3,
											  int ra1, int ra2, int ra3, int rune1, int rune2, int lvl,
											  String name, String descr, int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID, 512, 334);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mOverInterToTrigger = ID + 1;
		rsInterface.targetBitMask = suo;
		rsInterface.targetVerb = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast <col=00ff00>" + name;
		rsInterface.targetName = name;
		rsInterface.valueCompareType = new int[4];
		rsInterface.requiredValues = new int[4];
		rsInterface.valueCompareType[0] = 3;
		rsInterface.requiredValues[0] = ra1;
		rsInterface.valueCompareType[1] = 3;
		rsInterface.requiredValues[1] = ra2;
		rsInterface.valueCompareType[2] = 3;
		rsInterface.requiredValues[2] = ra3;
		rsInterface.valueCompareType[3] = 3;
		rsInterface.requiredValues[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 6;
		rsInterface.valueIndexArray[3][2] = 0;
		rsInterface.sprite2 = imageLoader(sid, "Lunar/LUNARON");
		rsInterface.sprite1 = imageLoader(sid, "Lunar/LUNAROFF");
		RSInterface INT = addInterface(ID + 1, 512, 334);
		INT.isMouseoverTriggered = true;
		INT.mOverInterToTrigger = -1;
		setChildren(9, INT);
		addLunarSprite(ID + 2, 2, "Lunar/BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true,
				true, 52, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, 0);
		setBounds(ID + 4, 90, 34, 2, INT);
		setBounds(30016, 14, 61, 3, INT);
		setBounds(rune1, 74, 61, 4, INT);
		setBounds(rune2, 130, 61, 5, INT);
		addRuneText(ID + 5, ra1 + 1, r1);
		setBounds(ID + 5, 26, 92, 6, INT);
		addRuneText(ID + 6, ra2 + 1, r2);
		setBounds(ID + 6, 87, 92, 7, INT);
		addRuneText(ID + 7, ra3 + 1, r3);
		setBounds(ID + 7, 142, 92, 8, INT);
	}

	public static void configureLunar() {
		homeTeleport();

		drawItemModel(30003, 554, 900); // fire
		drawItemModel(30004, 555, 900); // water
		drawItemModel(30005, 556, 900); // air
		drawItemModel(30006, 557, 900); // earth
		drawItemModel(30007, 558, 900); // mind
		drawItemModel(30008, 559, 900); // body
		drawItemModel(30009, 560, 900); // death
		drawItemModel(30010, 561, 900); // nature
		drawItemModel(30011, 562, 900); // chaos
		drawItemModel(30012, 563, 900); // law
		drawItemModel(30013, 564, 900); // cosmic
		drawItemModel(30014, 565, 900); // blood
		drawItemModel(30015, 566, 900); // soul
		drawItemModel(30016, 9075, 900); // astral

		addLunar3RunesSmallBox(30017, 9075, 554, 555, 0, 4, 3, 30003, 30004, 64, "Bake Pie", "Bake pies without a stove", 0, 16, 2);

		addLunar2RunesSmallBox(30025, 9075, 557, 0, 7, 30006, 65, "Cure Plant", "Cure disease on farming patch", 1, 4, 2);
		addLunar3RunesBigBox(30032, 9075, 564, 558, 0, 0, 0, 30013, 30007, 65,
				"Monster Examine",
				"Detect the combat statistics of a\\nmonster", 2, 2, 2);
		addLunar3RunesSmallBox(30040, 9075, 564, 556, 0, 0, 1, 30013, 30005,
				66, "NPC Contact", "Speak with varied NPCs", 3, 0, 2);
		addLunar3RunesSmallBox(30048, 9075, 563, 557, 0, 0, 9, 30012, 30006,
				67, "Cure Other", "Cure poisoned players", 4, 8, 2);
		addLunar3RunesSmallBox(30056, 9075, 555, 554, 0, 2, 0, 30004, 30003,
				67, "Humidify", "Fills certain vessels with water", 5, 0, 5);
		addLunar3RunesSmallBox(30064, 9075, 563, 557, 1, 0, 1, 30012, 30006,
				68, "Moonclan Teleport", "Teleports you to moonclan island",
				6, 0, 5);
		addLunar3RunesBigBox(30075, 9075, 563, 557, 1, 0, 3, 30012, 30006, 69,
				"Tele Group Moonclan",
				"Teleports players to Moonclan\\nisland", 7, 0, 5);
		addLunar3RunesSmallBox(30083, 9075, 563, 557, 1, 0, 5, 30012, 30006,
				70, "Ourania Teleport", "Teleports you to ourania rune altar",
				8, 0, 5);
		addLunar3RunesSmallBox(30091, 9075, 564, 563, 1, 1, 0, 30013, 30012,
				70, "Cure Me", "Cures Poison", 9, 0, 5);
		addLunar2RunesSmallBox(30099, 9075, 557, 1, 1, 30006, 70, "Hunter Kit",
				"Get a kit of hunting gear", 10, 0, 5);
		addLunar3RunesSmallBox(30106, 9075, 563, 555, 1, 0, 0, 30012, 30004,
				71, "Waterbirth Teleport",
				"Teleports you to Waterbirth island", 11, 0, 5);
		addLunar3RunesBigBox(30114, 9075, 563, 555, 1, 0, 4, 30012, 30004, 72,
				"Tele Group Waterbirth",
				"Teleports players to Waterbirth\\nisland", 12, 0, 5);
		addLunar3RunesSmallBox(30122, 9075, 564, 563, 1, 1, 1, 30013, 30012,
				73, "Cure Group", "Cures Poison on players", 13, 0, 5);
		addLunar3RunesBigBox(30130, 9075, 564, 559, 1, 1, 4, 30013, 30008, 74,
				"Stat Spy",
				"Cast on another player to see their\\nskill levels", 14, 8,
				2);
		addLunar3RunesBigBox(30138, 9075, 563, 554, 1, 1, 2, 30012, 30003, 74,
				"Barbarian Teleport",
				"Teleports you to the Barbarian\\noutpost", 15, 0, 5);
		addLunar3RunesBigBox(30146, 9075, 563, 554, 1, 1, 5, 30012, 30003, 75,
				"Tele Group Barbarian",
				"Teleports players to the Barbarian\\noutpost", 16, 0, 5);
		addLunar3RunesSmallBox(30154, 9075, 554, 556, 1, 5, 9, 30003, 30005,
				76, "Superglass Make", "Make glass without a furnace", 17, 16,
				2);
		addLunar3RunesSmallBox(30162, 9075, 563, 555, 1, 1, 3, 30012, 30004,
				77, "Khazard Teleport", "Teleports you to Port khazard", 18,
				0, 5);
		addLunar3RunesSmallBox(30170, 9075, 563, 555, 1, 1, 7, 30012, 30004,
				78, "Tele Group Khazard", "Teleports players to Port khazard",
				19, 0, 5);
		addLunar3RunesBigBox(30178, 9075, 564, 559, 1, 0, 4, 30013, 30008, 78,
				"Dream", "Take a rest and restore hitpoints 3\\n times faster",
				20, 0, 5);
		addLunar3RunesSmallBox(30186, 9075, 557, 555, 1, 9, 4, 30006, 30004,
				79, "String Jewellery", "String amulets without wool", 21, 0,
				5);
		addLunar3RunesLargeBox(30194, 9075, 557, 555, 1, 9, 9, 30006, 30004,
				80, "Stat Restore Pot\\nShare",
				"Share a potion with up to 4 nearby\\nplayers", 22, 0, 5);
		addLunar3RunesSmallBox(30202, 9075, 554, 555, 1, 6, 6, 30003, 30004,
				81, "Magic Imbue", "Combine runes without a talisman", 23, 0,
				5);
		addLunar3RunesBigBox(30210, 9075, 561, 557, 2, 1, 14, 30010, 30006, 82,
				"Fertile Soil",
				"Fertilise a farming patch with super\\ncompost", 24, 4, 2);
		addLunar3RunesBigBox(30218, 9075, 557, 555, 2, 11, 9, 30006, 30004, 83,
				"Boost Potion Share",
				"Shares a potion with up to 4 nearby\\nplayers", 25, 0, 5);
		addLunar3RunesSmallBox(30226, 9075, 563, 555, 2, 2, 9, 30012, 30004,
				84, "Fishing Guild Teleport",
				"Teleports you to the fishing guild", 26, 0, 5);
		addLunar3RunesLargeBox(30234, 9075, 563, 555, 1, 2, 13, 30012, 30004,
				85, "Tele Group Fishing\\nGuild",
				"Teleports players to the Fishing\\nGuild", 27, 0, 5);
		addLunar3RunesSmallBox(30242, 9075, 557, 561, 2, 14, 0, 30006, 30010,
				85, "Plank Make", "Turn Logs into planks", 28, 16, 5);
		/******** Cut Off Limit **********/
		addLunar3RunesSmallBox(30250, 9075, 563, 555, 2, 2, 9, 30012, 30004,
				86, "Catherby Teleport", "Teleports you to Catherby", 29, 0,
				5);
		addLunar3RunesSmallBox(30258, 9075, 563, 555, 2, 2, 14, 30012, 30004,
				87, "Tele Group Catherby", "Teleports players to Catherby",
				30, 0, 5);
		addLunar3RunesSmallBox(30266, 9075, 563, 555, 2, 2, 7, 30012, 30004,
				88, "Ice Plateau Teleport", "Teleports you to Ice Plateau",
				31, 0, 5);
		addLunar3RunesBigBox(30274, 9075, 563, 555, 2, 2, 15, 30012, 30004, 89,
				"Tele Group Ice\\n Plateau",
				"\\nTeleports players to Ice Plateau", 32, 0, 5);
		addLunar3RunesBigBox(30282, 9075, 563, 561, 2, 1, 0, 30012, 30010, 90,
				"Energy Transfer",
				"Spend HP and SA energy to\\n give another SA and run energy",
				33, 8, 2);
		addLunar3RunesBigBox(30290, 9075, 563, 565, 2, 2, 0, 30012, 30014, 91,
				"Heal Other",
				"Transfer up to 75% of hitpoints\\n to another player", 34,
				8, 2);
		addLunar3RunesBigBox(30298, 9075, 560, 557, 2, 1, 9, 30009, 30006, 92,
				"Vengeance Other",
				"Allows another player to rebound\\ndamage to an opponent",
				35, 8, 2);
		addLunar3RunesSmallBox(30306, 9075, 560, 557, 3, 1, 9, 30009, 30006, 93, "Vengeance", "Rebound damage to an opponent", 36, 0, 5);

		addLunar3RunesBigBox(30314, 9075, 565, 563, 3, 2, 5, 30014, 30012, 94,
				"Heal Group", "Transfer up to 75% of hitpoints\\n to a group",
				37, 0, 5);
		addLunar3RunesBigBox(30322, 9075, 564, 563, 2, 1, 0, 30013, 30012, 95,
				"Spellbook Swap",
				"Change to another spellbook for 1\\nspell cast", 38, 0, 5);
	}

	public static void setBounds(int ID, int X, int Y, int frame,
								 RSInterface RSinterface) {
		RSinterface.children[frame] = ID;
		RSinterface.childX[frame] = X;
		RSinterface.childY[frame] = Y;
	}

	public static void setChildren(int total, RSInterface rsinterface) {
		rsinterface.children = new int[total];
		rsinterface.childX = new int[total];
		rsinterface.childY = new int[total];
	}

	public static void puzzle() {
		RSInterface rsi = interfaceCache[6976];

		int[] oldChildren = rsi.children;
		int[] oldChildrenX = rsi.childX;
		int[] oldChildrenY = rsi.childY;

		rsi.totalChildren(oldChildren.length + 1);
		for (int i = 0; i < oldChildren.length; i++) {
			rsi.children[i] = oldChildren[i];
			rsi.childX[i] = oldChildrenX[i];
			rsi.childY[i] = oldChildrenY[i];
		}

		addText(296, "Click here to pay 5M to complete this puzzle.", 1, 0xfad782, false, true, 0);

		interfaceCache[296].type = 4;
		interfaceCache[296].atActionType = 1;
		interfaceCache[296].width = 250;
		interfaceCache[296].height = 13;
		interfaceCache[296].tooltip = "Click here to pay 5M to complete this puzzle.";
		interfaceCache[296].contentType = 0;

		rsi.child(oldChildren.length, 296, 140, 300);
	}

	public static void friendsTab() {
		RSInterface tab = addTabInterface(5065);
		tab.width = 192;
		tab.height = 263;
		addText(5067, "Friends List", 1, 0xff9933, true, true, 0);
		addText(5068, "" + Constants.CLIENT_NAME + "", 1, 0xff9933, true, true, 0);
		addSprite(5069, 0, "Interfaces/Friends/SPRITE");
		addSprite(5070, 1, "Interfaces/Friends/SPRITE");
		addSprite(5071, 1, "Interfaces/Friends/SPRITE");
		addSprite(5072, 2, "Interfaces/Friends/SPRITE");
		addHoverButton(5073, "Interfaces/Friends/SPRITE", 3, 17, 17, "Add friend", 201, 5074, 1, -1);
		addHoveredButton(5074, "Interfaces/Friends/SPRITE", 4, 17, 17, 5075);
		addHoverButton(5076, "Interfaces/Friends/SPRITE", 5, 17, 17, "Delete friend", 202, 5077, 1, -1);
		addHoveredButton(5077, "Interfaces/Friends/SPRITE", 6, 17, 17, 5078);
		//addText(5079, "0 / 200", 0xff9933, 0, false, true, 901, 0);
		addText123(5079, "0 / 200", 0, 0xff9933, false, true, 901, 0);
		RSInterface friendsList = interfaceCache[5066];
		friendsList.height = 197;
		friendsList.width = 174;
		for (int id = 5092; id <= 5191; id++) {
			int i = id - 5092;
			friendsList.children[i] = id;
			friendsList.childX[i] = 3;
			friendsList.childY[i] = friendsList.childY[i] - 3;
		}
		for (int id = 5192; id <= 5291; id++) {
			int i = id - 5092;
			friendsList.children[i] = id;
			friendsList.childX[i] = 113;
			friendsList.childY[i] = friendsList.childY[i] - 3;
		}

		tab.totalChildren(12);
		tab.child(0, 5067, 92, 2);
		tab.child(1, 5068, 92, 17);
		tab.child(2, 5069, 0, 39);
		tab.child(3, 5070, 0, 36);
		tab.child(4, 5071, 0, 237);
		tab.child(5, 5072, 107, 39);
		tab.child(6, 5073, 5, 242);
		tab.child(7, 5074, 5, 242);
		tab.child(8, 5076, 23, 242);
		tab.child(9, 5077, 23, 242);
		tab.child(10, 5079, 46, 245);
		// tab.child(11, 5080, 170, 242);
		// tab.child(12, 5081, 151, 242);
		tab.child(11, 5066, 0, 39);

	}

	public static void addText(int i, String s, int k, int n, boolean l,
							   boolean m, int a, int j) {
		RSInterface rsinterface = addTabInterface(i);
		rsinterface.parentID = i;
		rsinterface.id = i;
		rsinterface.type = 4;
		rsinterface.atActionType = 0;
		rsinterface.width = 0;
		rsinterface.height = 0;
		rsinterface.contentType = 0;
		rsinterface.mOverInterToTrigger = a;
		rsinterface.centerText = l;
		rsinterface.textShadow = m;
		rsinterface.textDrawingAreas = j;
		rsinterface.message = s;
		rsinterface.textColor = k;
	}

	public static void addText123(int id, String text, int idx, int color, boolean center, boolean shadow, int contentType, int actionType) {
		RSInterface tab = addTabInterface(id);
		tab.parentID = id;
		tab.id = id;
		tab.type = 4;
		tab.atActionType = actionType;
		tab.width = 0;
		tab.height = 11;
		tab.contentType = contentType;
		tab.opacity = 0;
		tab.centerText = center;
		tab.textShadow = shadow;
		tab.textDrawingAreas = idx;
		tab.message = text;
		tab.textColor = color;
		tab.anInt219 = 0;
		tab.textHoverColor = 0;
		tab.anInt239 = 0;
	}

	public static void ignoreTab() {
		RSInterface tab = addTabInterface(5715);
		tab.width = 192;
		tab.height = 263;
		addText(5717, "Ignore List", 1, 0xff9933, true, true, 0);
		addSprite(5718, 0, "Interfaces/Friends/SPRITE");
		addSprite(5719, 1, "Interfaces/Friends/SPRITE");
		addSprite(5720, 1, "Interfaces/Friends/SPRITE");
		addHoverButton(5721, "Interfaces/Friends/SPRITE", 11, 17, 17,
				"Add name", 501, 5722, 1, -1);
		addHoveredButton(5722, "Interfaces/Friends/SPRITE", 12, 17, 17, 5723);
		addHoverButton(5724, "Interfaces/Friends/SPRITE", 13, 17, 17,
				"Delete name", 502, 5725, 1, -1);
		addHoveredButton(5725, "Interfaces/Friends/SPRITE", 14, 17, 17, 5726);
		addText123(5727, "0 / 100", 0, 0xff9933, false, true, 902, 0);

		RSInterface ignoresList = interfaceCache[5716];
		ignoresList.height = 197;
		ignoresList.width = 174;
		for (int id = 5742; id <= 5841; id++) {
			int i = id - 5742;
			ignoresList.children[i] = id;
			ignoresList.childX[i] = 3;
			ignoresList.childY[i] = ignoresList.childY[i] - 3;
		}

		tab.totalChildren(10);
		tab.child(0, 5717, 95, 9);
		tab.child(1, 5718, 0, 39);
		tab.child(2, 5719, 0, 36);
		tab.child(3, 5720, 0, 237);
		tab.child(4, 5721, 5, 242);
		tab.child(5, 5722, 5, 242);
		tab.child(6, 5724, 23, 242);
		tab.child(7, 5725, 23, 242);
		tab.child(8, 5727, 46, 245);
		tab.child(9, 5716, 0, 39);

	}

	public static void constructLunar() {
		RSInterface Interface = addInterface(25555, 512, 334);
		Interface.totalChildren(80);
		setBounds(30000, 11, 10, 0, Interface);
		setBounds(30017, 40, 11, 1, Interface);
		setBounds(30025, 71, 12, 2, Interface);
		setBounds(30032, 103, 10, 3, Interface);
		setBounds(30040, 135, 12, 4, Interface);
		setBounds(30048, 165, 10, 5, Interface);
		setBounds(30056, 8, 38, 6, Interface);
		setBounds(30064, 39, 39, 7, Interface);
		setBounds(30075, 71, 39, 8, Interface);
		setBounds(30083, 103, 39, 9, Interface);
		setBounds(30091, 135, 39, 10, Interface);
		setBounds(30099, 165, 37, 11, Interface);
		setBounds(30106, 12, 68, 12, Interface);
		setBounds(30114, 42, 68, 13, Interface);
		setBounds(30122, 71, 68, 14, Interface);
		setBounds(30130, 103, 68, 15, Interface);
		setBounds(30138, 135, 68, 16, Interface);
		setBounds(30146, 165, 68, 17, Interface);
		setBounds(30154, 14, 97, 18, Interface);
		setBounds(30162, 42, 97, 19, Interface);
		setBounds(30170, 71, 97, 20, Interface);
		setBounds(30178, 101, 97, 21, Interface);
		setBounds(30186, 135, 98, 22, Interface);
		setBounds(30194, 168, 98, 23, Interface);
		setBounds(30202, 11, 125, 24, Interface);
		setBounds(30210, 42, 124, 25, Interface);
		setBounds(30218, 74, 125, 26, Interface);
		setBounds(30226, 103, 125, 27, Interface);
		setBounds(30234, 135, 125, 28, Interface);
		setBounds(30242, 164, 126, 29, Interface);
		setBounds(30250, 10, 155, 30, Interface);
		setBounds(30258, 42, 155, 31, Interface);
		setBounds(30266, 71, 155, 32, Interface);
		setBounds(30274, 103, 155, 33, Interface);
		setBounds(30282, 136, 155, 34, Interface);
		setBounds(30290, 165, 155, 35, Interface);
		setBounds(30298, 13, 185, 36, Interface);
		setBounds(30306, 42, 185, 37, Interface);
		setBounds(30314, 71, 184, 38, Interface);
		setBounds(30322, 104, 184, 39, Interface);
		setBounds(30001, 6, 184, 40, Interface);// hover
		setBounds(30018, 5, 176, 41, Interface);// hover
		setBounds(30026, 5, 176, 42, Interface);// hover
		setBounds(30033, 5, 163, 43, Interface);// hover
		setBounds(30041, 5, 176, 44, Interface);// hover
		setBounds(30049, 5, 176, 45, Interface);// hover
		setBounds(30057, 5, 176, 46, Interface);// hover
		setBounds(30065, 5, 176, 47, Interface);// hover
		setBounds(30076, 5, 163, 48, Interface);// hover
		setBounds(30084, 5, 176, 49, Interface);// hover
		setBounds(30092, 5, 176, 50, Interface);// hover
		setBounds(30100, 5, 176, 51, Interface);// hover
		setBounds(30107, 5, 176, 52, Interface);// hover
		setBounds(30115, 5, 163, 53, Interface);// hover
		setBounds(30123, 5, 176, 54, Interface);// hover
		setBounds(30131, 5, 163, 55, Interface);// hover
		setBounds(30139, 5, 163, 56, Interface);// hover
		setBounds(30147, 5, 163, 57, Interface);// hover
		setBounds(30155, 5, 176, 58, Interface);// hover
		setBounds(30163, 5, 176, 59, Interface);// hover
		setBounds(30171, 5, 176, 60, Interface);// hover
		setBounds(30179, 5, 163, 61, Interface);// hover
		setBounds(30187, 5, 176, 62, Interface);// hover
		setBounds(30195, 5, 149, 63, Interface);// hover
		setBounds(30203, 5, 176, 64, Interface);// hover
		setBounds(30211, 5, 163, 65, Interface);// hover
		setBounds(30219, 5, 163, 66, Interface);// hover
		setBounds(30227, 5, 176, 67, Interface);// hover
		setBounds(30235, 5, 149, 68, Interface);// hover
		setBounds(30243, 5, 176, 69, Interface);// hover

		setBounds(30251, 5, 5, 70, Interface);// hover
		setBounds(30259, 5, 5, 71, Interface);// hover
		setBounds(30267, 5, 5, 72, Interface);// hover
		setBounds(30275, 5, 5, 73, Interface);// hover
		setBounds(30283, 5, 5, 74, Interface);// hover
		setBounds(30291, 5, 5, 75, Interface);// hover
		setBounds(30299, 5, 5, 76, Interface);// hover
		setBounds(30307, 5, 5, 77, Interface);// hover
		setBounds(30323, 5, 5, 78, Interface);// hover
		setBounds(30315, 5, 5, 79, Interface);// hover
	}

	public static RSInterface addText(int id, String text,
									  int idx, int color, boolean center, boolean shadow, int width) {
		RSInterface tab = addTabInterface(id);
		tab.parentID = id;
		tab.id = id;
		tab.type = 4;
		tab.atActionType = 0;
		tab.width = width;
		tab.height = 11;
		tab.contentType = 0;
		tab.opacity = 0;
		tab.mOverInterToTrigger = -1;
		tab.centerText = center;
		tab.textShadow = shadow;
		tab.textDrawingAreas = idx;
		tab.message = text;
		tab.textColor = color;
		tab.anInt219 = 0;
		tab.textHoverColor = 0;
		tab.anInt239 = 0;
		return tab;
	}

	public static void addButton(int id, int sid, String spriteName,
								 String tooltip, int w, int h) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 1;
		tab.contentType = 0;
		tab.opacity = (byte) 0;
		tab.mOverInterToTrigger = 52;
		tab.sprite1 = imageLoader(sid, spriteName);
		tab.width = w;
		tab.height = h;
		tab.tooltip = tooltip;
	}

	public static RSInterface addSprite(int id, int spriteId, String spriteName) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.opacity = (byte) 0;
		tab.mOverInterToTrigger = 52;
		tab.sprite1 = imageLoader(spriteId, spriteName);
		tab.width = 512;
		tab.height = 334;
		return tab;
	}

	public static RSInterface addSprite(int id, int cacheSpriteID) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.opacity = (byte) 0;
		tab.mOverInterToTrigger = 52;
		tab.sprite1 = Client.instance.cacheSprite[cacheSpriteID];
		tab.width = 512;
		tab.height = 334;
		return tab;
	}

	public static RSInterface addPouches(int id, int spriteId,
										 String spriteName, int index) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 5;
		tab.contentType = 0;
		tab.opacity = (byte) 0;
		tab.mOverInterToTrigger = 52;
		tab.itemSpriteId1 = pouchItems[index];
		tab.itemSpriteIndex = index;
		tab.tooltip = (new StringBuilder()).append("Transform <col=ffb000>").append(ItemDefinition.forID(pouchItems[index]).name).toString();
		tab.summonReq = summoningLevelRequirements[index];
		tab.sprite1 = imageLoader(spriteId, spriteName);
		tab.width = 46;
		tab.height = 50;
		return tab;
	}

	public static RSInterface addScrolls(int id, int spriteId, String spriteName, int index) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.tooltip = (new StringBuilder()).append("Transform <col=ffb000>").append(ItemDefinition.forID(scrollItems[index]).name).toString();
		tab.atActionType = 5;
		tab.contentType = 0;
		tab.opacity = (byte) 0;
		tab.mOverInterToTrigger = 52;
		tab.itemSpriteId1 = scrollItems[index];
		tab.itemSpriteIndex = index;
		tab.summonReq = summoningLevelRequirements[index];
		tab.sprite1 = imageLoader(spriteId, spriteName);
		tab.width = 46;
		tab.height = 50;
		return tab;
	}

	public static RSInterface addSprite(int id, int spriteId,
										String spriteName, boolean hidden) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 0;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.opacity = (byte) 0;
		tab.isMouseoverTriggered = hidden;
		tab.mOverInterToTrigger = -1;
		tab.sprite1 = imageLoader(spriteId, spriteName);
		tab.width = 512;
		tab.height = 334;
		return tab;
	}

	public static void addHoverButton(int i, String imageName, int spriteId, int width, int height, String text, int contentType, int hoverOver, int aT) {// hoverable button
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = i;
		tab.type = 5;
		tab.atActionType = aT;
		tab.contentType = contentType;
		tab.opacity = 0;
		tab.mOverInterToTrigger = hoverOver;
		tab.sprite1 = imageLoader(spriteId, imageName);
		tab.width = width;
		tab.height = height;
		tab.tooltip = text;
	}

	public static void addHoverButtonCacheSprite(int i, int cacheSpriteID,
												 int width, int height, String text, int contentType, int hoverOver,
												 int aT) {// hoverable button
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = i;
		tab.type = 5;
		tab.atActionType = aT;
		tab.contentType = contentType;
		tab.opacity = 0;
		tab.mOverInterToTrigger = hoverOver;
		tab.sprite1 = Client.instance.cacheSprite[cacheSpriteID];
		tab.width = width;
		tab.height = height;
		tab.tooltip = text;
	}

	public static void addHoveredButton2(int i, String imageName, int j, int w,
										 int h, int IMAGEID) {// hoverable button
		RSInterface tab = addTabInterface(i);
		tab.parentID = i;
		tab.id = i;
		tab.type = 0;
		tab.atActionType = 0;
		tab.width = w;
		tab.height = h;
		tab.isMouseoverTriggered = true;
		tab.opacity = 0;
		tab.mOverInterToTrigger = -1;
		tab.scrollMax = 0;
		addHoverImage(IMAGEID, j, j, imageName);
		tab.totalChildren(1);
		tab.child(0, IMAGEID, 0, 0);
	}

	public static void addHoverButton(int i, String imageName, int j,
									  int width, int height, String text, int contentType, int hoverOver,
									  int aT, int configId) {// hoverable button
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = i;
		tab.type = 5;
		tab.atActionType = aT;
		tab.contentType = contentType;
		tab.opacity = 0;
		tab.mOverInterToTrigger = hoverOver;
		tab.sprite1 = imageLoader(j, imageName);
		tab.sprite2 = imageLoader((j + 1), imageName);
		tab.width = width;
		tab.height = height;
		tab.tooltip = text;
		if (configId != -1) {
			tab.valueCompareType = new int[1];
			tab.requiredValues = new int[1];
			tab.valueCompareType[0] = 1;
			tab.requiredValues[0] = configId;
			tab.valueIndexArray = new int[1][3];
			tab.valueIndexArray[0][0] = 5;
			tab.valueIndexArray[0][1] = configId;
			tab.valueIndexArray[0][2] = 0;
		}
	}

	public static void addHoveredButton(int i, String imageName, int j, int w,
										int h, int IMAGEID) {// hoverable button
		RSInterface tab = addTabInterface(i);
		tab.parentID = i;
		tab.id = i;
		tab.type = 0;
		tab.atActionType = 0;
		tab.width = w;
		tab.height = h;
		tab.isMouseoverTriggered = true;
		tab.opacity = 0;
		tab.mOverInterToTrigger = -1;
		tab.scrollMax = 0;
		addHoverImage(IMAGEID, j, j, imageName);
		tab.totalChildren(1);
		tab.child(0, IMAGEID, 0, 0);
		/*
		 * if (configId != -1) { tab.valueCompareType = new int[1];
		 * tab.requiredValues = new int[1]; tab.valueCompareType[0] = 1;
		 * tab.requiredValues[0] = configId; tab.valueIndexArray = new
		 * int[1][3]; tab.valueIndexArray[0][0] = 5; tab.valueIndexArray[0][1] =
		 * configId; tab.valueIndexArray[0][2] = 0; }
		 */
	}

	public static void addHoveredButtonCacheSprite(int i, int cacheSpriteID, int w,
												   int h, int IMAGEID) {// hoverable button lolwut
		RSInterface tab = addTabInterface(i);
		tab.parentID = i;
		tab.id = i;
		tab.type = 0;
		tab.atActionType = 0;
		tab.width = w;
		tab.height = h;
		tab.isMouseoverTriggered = true;
		tab.opacity = 0;
		tab.mOverInterToTrigger = -1;
		tab.scrollMax = 0;
		addHoverImageCacheSprite(IMAGEID, cacheSpriteID);
		tab.totalChildren(1);
		tab.child(0, IMAGEID, 0, 0);
		/*
		 * if (configId != -1) { tab.valueCompareType = new int[1];
		 * tab.requiredValues = new int[1]; tab.valueCompareType[0] = 1;
		 * tab.requiredValues[0] = configId; tab.valueIndexArray = new
		 * int[1][3]; tab.valueIndexArray[0][0] = 5; tab.valueIndexArray[0][1] =
		 * configId; tab.valueIndexArray[0][2] = 0; }
		 */
	}

	public static void addHoverImage(int i, int j, int k, String name) {
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = i;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.width = 512;
		tab.height = 334;
		tab.opacity = 0;
		tab.mOverInterToTrigger = 52;
		tab.sprite1 = imageLoader(j, name);
	}

	public static void addHoverImageCacheSprite(int id, int cacheSpriteID) {
		RSInterface tab = addTabInterface(id);
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.width = 512;
		tab.height = 334;
		tab.opacity = 0;
		tab.mOverInterToTrigger = 52;
		tab.sprite1 = Client.instance.cacheSprite[cacheSpriteID];
	}

	public static void addTransparentSprite(int id, int spriteId,
											String spriteName) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.opacity = (byte) 0;
		tab.mOverInterToTrigger = 52;
		tab.sprite1 = imageLoader(spriteId, spriteName);
		tab.width = 512;
		tab.height = 334;
	}

	public static RSInterface addScreenInterface(int id) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 0;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.width = 512;
		tab.height = 334;
		tab.opacity = (byte) 0;
		tab.mOverInterToTrigger = 0;
		return tab;
	}

	public static RSInterface addTabInterface(int id) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;// 250
		tab.parentID = id;// 236
		tab.type = 0;// 262
		tab.atActionType = 0;// 217
		tab.contentType = 0;
		tab.width = 512;// 220
		tab.height = 700;// 267
		tab.opacity = (byte) 0;
		tab.mOverInterToTrigger = -1;// Int 230
		return tab;
	}

	public static RSInterface addContainer(int id, int width, int height, int clientCode) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;// 250
		tab.parentID = id;// 236
		tab.type = 0;// 262
		tab.atActionType = 0;// 217
		tab.contentType = clientCode;
		tab.width = width;// 220
		tab.height = height;// 267
		tab.opacity = (byte) 0;
		tab.mOverInterToTrigger = -1;// Int 230
		return tab;
	}

	public static Sprite imageLoader(int id, String s) {
		String spriteFile = s;

		if (id != -1) {
			spriteFile = s + " " + id;
		}

		if (s.equalsIgnoreCase("ge")) {
			spriteFile = s + "/" + id;
		}

		// System.out.println(spriteFile + ".png");

		byte[] buff = Client.spriteCache.getFile(spriteFile + ".png");
		if (buff != null) {
			return new Sprite(buff);
		}

		if (spriteIndex.containsKey(spriteFile.toLowerCase())) {
			return Client.instance.cacheSprite[spriteIndex.get(spriteFile.toLowerCase())];
		}

		return null;
	}

	public static void clanChatTab() {
		RSInterface tab = addInterface(18128, 512, 334);
		//addHoverButton(18129, "sprite", "clan", 6, 72, 32, "Join Chat", 550, 18130, 1);
		addHoverButton(18129, "/Clan Chat/sprite", 6, 72, 32, "Join Chat", -1, 18130, 5);
		addHoveredButton(18130, "/Clan Chat/sprite", 7, 72, 32, 18131);
		addHoverButton(18132, "/Clan Chat/sprite", 6, 72, 32, "Clan Chat Setup", -1, 18133, 5);
		addHoveredButton(18133, "/Clan Chat/sprite", 7, 72, 32, 18134);
		addText(18135, "Join Chat", 0, 0xff9b00, true, true, 0);
		addText(18136, "Clan Setup", 0, 0xff9b00, true, true, 0);
		addSprite(18137, 37, "/Clan Chat/sprite");
		addText(18138, "Clan Chat", 1, 0xff9b00, true, true, 0);
		addText(18139, "Talking in: Not in chat", 0, 0xff9b00, false, true, 0);
		addText(18140, "Owner: None", 0, 0xff9b00, false, true, 0);
		addSprite(16126, 4, "/Clan Chat/sprite");
		tab.totalChildren(13);
		tab.child(0, 16126, 0, 59);
		tab.child(1, 16126, 0, 221);
		tab.child(2, 18137, 0, 62);
		tab.child(3, 18143, 0, 62);
		tab.child(4, 18129, 15, 226);
		tab.child(5, 18130, 15, 226);
		tab.child(6, 18132, 103, 226);
		tab.child(7, 18133, 103, 226);
		tab.child(8, 18135, 51, 237);
		tab.child(9, 18136, 139, 237);
		tab.child(10, 18138, 95, 1);
		tab.child(11, 18139, 10, 23);
		tab.child(12, 18140, 25, 38);
		/* Text area */
		RSInterface list = addInterface(18143, 512, 334);
		list.totalChildren(100);
		for (int i = 0; i < 100; i++) {
			addText(i + 18144, "", 0, 0xffffff, false, true, 100);
		}
		for (int id = 18144, i = 0; id < 18244 && i < 100; id++, i++) {
			RSInterface text = interfaceCache[id];
			text.actions = new String[]{"Edit Rank", "Kick", "Ban"};
			list.children[i] = id;
			list.childX[i] = 10;
			for (int id2 = 18144, i2 = 1; id2 < 18244 && i2 < 100; id2++, i2++) {
				list.childY[0] = 2;
				list.childY[i2] = list.childY[i2 - 1] + 14;
			}
		}
		list.height = 158;
		list.width = 174;
		list.scrollMax = 1405;
	}

	public static void clanChatSetup() {
		RSInterface rsi = addInterface(18300, 512, 334);
		rsi.totalChildren(12 + 15);
		int count = 0;
		/* Background */
		addSprite(18301, 1, "/Clan Chat/sprite");
		rsi.child(count++, 18301, 14, 18);
		/* Close button */
		addButton2(18302, 0, "/Clan Chat/close", "Close");
		interfaceCache[18302].atActionType = 3;
		rsi.child(count++, 18302, 475, 26);
		/* Clan Setup title */
		addText(18303, "Clan Setup", 2, 0xFF981F, true, true, 0);
		rsi.child(count++, 18303, 256, 26);
		/* Setup buttons */
		for (int index = 0, id = 18304, y = 50; index < titles.length; index++, id += 3, y += 40) {
			addButton2(id, 2, "/Clan Chat/sprite", "");
			interfaceCache[id].atActionType = 0;
			if (index > 0) {
				interfaceCache[id].actions = whoCan;
			} else {
				interfaceCache[id].actions = new String[]{"Change title", "Delete clan"};
			}
			addText(id + 1, titles[index], 0, 0xFF981F, true, true, 0);
			addText(id + 2, defaults[index], 1, 0xFFFFFF, true, true, 0);
			rsi.child(count++, id, 25, y);
			rsi.child(count++, id + 1, 100, y + 4);
			rsi.child(count++, id + 2, 100, y + 17);
		}
		/* Table */
		addSprite(18319, 5, "/Clan Chat/sprite");
		rsi.child(count++, 18319, 197, 70);
		/* Labels */
		int id = 18320;
		int y = 74;
		addText(id, "Ranked Members", 2, 0xFF981F, false, true, 0);
		rsi.child(count++, id++, 202, y);
		addText(id, "Banned Members", 2, 0xFF981F, false, true, 0);
		rsi.child(count++, id++, 339, y);
		/* Ranked members list */
		RSInterface list = addInterface(id++, 119, 210);
		int lines = 100;
		list.totalChildren(lines);
		String[] ranks = {"Demote", "Recruit", "Corporal", "Sergeant", "Lieutenant", "Captain", "General", "Owner"};
		list.childY[0] = 2;
		//System.out.println(id);
		for (int index = id; index < id + lines; index++) {
			addText(index, "", 1, 0xffffff, false, true, 100);
			interfaceCache[index].actions = ranks;
			list.children[index - id] = index;
			list.childX[index - id] = 2;
			list.childY[index - id] = (index - id > 0 ? list.childY[index - id - 1] + 14 : 0);
		}
		id += lines;
		list.scrollMax = (lines * 14) + 2;
		rsi.child(count++, list.id, 199, 92);
		/* Banned members list */
		list = addInterface(id++, 119, 210);
		list.totalChildren(lines);
		list.childY[0] = 2;
		//System.out.println(id);
		for (int index = id; index < id + lines; index++) {
			addText(index, "", 1, 0xffffff, false, true, 100);
			interfaceCache[index].actions = new String[]{"Unban"};
			list.children[index - id] = index;
			list.childX[index - id] = 0;
			list.childY[index - id] = (index - id > 0 ? list.childY[index - id - 1] + 14 : 0);
		}
		id += lines;
		list.scrollMax = (lines * 14) + 2;
		rsi.child(count++, list.id, 339, 92);
		/* Table info text */
		y = 47;
		addText(id, "You can manage both ranked and banned members here.", 0, 0xFF981F, true, true, 0);
		rsi.child(count++, id++, 337, y);
		addText(id, "Right click on a name to edit the member.", 0, 0xFF981F, true, true, 0);
		rsi.child(count++, id++, 337, y + 11);
		/* Add ranked member button */
		y = 75;
		addButton2(id, 0, "/Clan Chat/plus", "Add ranked member");
		rsi.child(count++, id++, 319, y);
		/* Add banned member button */
		addButton2(id, 0, "/Clan Chat/plus", "Add banned member");
		rsi.child(count++, id++, 459, y);
	}

	public static void addButton2(int id, int sid, String spriteName, String tooltip) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 5;
		tab.contentType = 0;
		tab.opacity = (byte) 0;
		tab.mOverInterToTrigger = 52;
		tab.sprite1 = imageLoader(sid, spriteName);
		tab.width = tab.sprite1.myWidth;
		tab.height = tab.sprite1.myHeight;
		tab.tooltip = tooltip;
	}

	public static void addText2(int id, String text, int idx,
								int color, boolean center, boolean shadow) {
		RSInterface tab = addTabInterface(id);
		tab.parentID = id;
		tab.id = id;
		tab.type = 4;
		tab.atActionType = 0;
		tab.width = 0;
		tab.height = 11;
		tab.contentType = 0;
		tab.opacity = 0;
		tab.centerText = center;
		tab.textShadow = shadow;
		tab.textDrawingAreas = idx;
		tab.message = text;
		tab.textColor = color;
		tab.anInt219 = 0;
		tab.textHoverColor = 0;
		tab.anInt239 = 0;
	}

	public static void addHoverText2(int id, String text, String[] tooltips,
									 int idx, int color, boolean center, boolean textShadowed,
									 int width) {
		RSInterface rsinterface = addInterface(id, 512, 334);
		rsinterface.id = id;
		rsinterface.parentID = id;
		rsinterface.type = 4;
		rsinterface.atActionType = 1;
		rsinterface.width = width;
		rsinterface.height = 11;
		rsinterface.contentType = 0;
		rsinterface.opacity = 0;
		rsinterface.centerText = center;
		rsinterface.textShadow = textShadowed;
		rsinterface.textDrawingAreas = idx;
		rsinterface.message = text;
		rsinterface.textColor = color;
		rsinterface.anInt219 = 0;
		rsinterface.textHoverColor = 0xffffff;
		rsinterface.anInt239 = 0;
	}

	public static void quickPrayers() {
		int frame = 0;
		RSInterface tab = addTabInterface(17200);
		addSprite(17201, 3, "/Interfaces/QuickPrayer/Sprite");
		addText(17230, "Select your quick prayers:", 0, 0xFF981F, false, true, 0);
		addTransparentSprite(17229, 0, "/Interfaces/QuickPrayer/Sprite", 50);
		for (int i = 17202, j = 630; i <= 17228 || j <= 656; i++, j++) {
			addConfigButton(i, 17200, 2, 1, "/Interfaces/QuickPrayer/Sprite", 14, 15, "Select", 0, 1, j);
		}
		addConfigButton(15891, 17200, 2, 1, "/Interfaces/QuickPrayer/Sprite", 14, 15, "Select", 0, 1, 657);//Augury
		addHoverButton(17231, "/Interfaces/QuickPrayer/Sprite", 4, 190, 24, "Confirm Selection", -1, 17232, 1);
		addHoveredButton(17232, "/Interfaces/QuickPrayer/Sprite", 5, 190, 24, 17233);

		setChildren(62, tab);
		/*
		addPrayer(22503, 0, 83, 49, 7, "Protect Item", 22582);
		setBounds(22503, 2, 5, index, Interface);index++;//Glow
		setBounds(22504, 8, 8, index, Interface);index++;//Icon
		 */
		setBounds(25001, 5, 8 + 20, frame++, tab);
		setBounds(25003, 44, 8 + 20, frame++, tab);
		setBounds(25005, 79, 11 + 20, frame++, tab);
		setBounds(25007, 116, 10 + 20, frame++, tab);
		setBounds(25009, 153, 9 + 20, frame++, tab);
		setBounds(25011, 5, 48 + 20, frame++, tab);
		setBounds(25013, 44, 47 + 20, frame++, tab);
		setBounds(25015, 79, 49 + 20, frame++, tab);
		setBounds(25017, 116, 50 + 20, frame++, tab);
		setBounds(25019, 154, 50 + 20, frame++, tab);
		setBounds(25021, 4, 84 + 20, frame++, tab);
		setBounds(25023, 44, 87 + 20, frame++, tab);
		setBounds(25025, 81, 85 + 20, frame++, tab);
		setBounds(25027, 117, 85 + 20, frame++, tab);
		setBounds(25029, 156, 87 + 20, frame++, tab);
		setBounds(25031, 5, 125 + 20, frame++, tab);
		setBounds(25033, 43, 124 + 20, frame++, tab);
		setBounds(25035, 83, 124 + 20, frame++, tab);
		setBounds(25037, 115, 121 + 20, frame++, tab);
		setBounds(25039, 154, 124 + 20, frame++, tab);
		setBounds(25041, 5, 160 + 20, frame++, tab);
		setBounds(25043, 41, 158 + 20, frame++, tab);
		setBounds(25045, 79, 163 + 20, frame++, tab);
		setBounds(25047, 116, 158 + 20, frame++, tab);
		setBounds(25049, 161, 160 + 20, frame++, tab);
		setBounds(25051, 4, 207 + 12, frame++, tab);
		setBounds(25113, 44, 207 + 4, frame++, tab);
		setBounds(25107, 79, 207 + 4, frame++, tab);

		setBounds(17229, 0, 25, frame++, tab);//Faded backing
		setBounds(17201, 0, 22, frame++, tab);//Split
		setBounds(17201, 0, 237, frame++, tab);//Split

		setBounds(17202, 5 - 3, 8 + 17, frame++, tab);
		setBounds(17203, 44 - 3, 8 + 17, frame++, tab);
		setBounds(17204, 79 - 3, 8 + 17, frame++, tab);
		setBounds(17205, 116 - 3, 8 + 17, frame++, tab);
		setBounds(17206, 153 - 3, 8 + 17, frame++, tab);
		setBounds(17207, 5 - 3, 48 + 17, frame++, tab);
		setBounds(17208, 44 - 3, 48 + 17, frame++, tab);
		setBounds(17209, 79 - 3, 48 + 17, frame++, tab);
		setBounds(17210, 116 - 3, 48 + 17, frame++, tab);
		setBounds(17211, 153 - 3, 48 + 17, frame++, tab);
		setBounds(17212, 5 - 3, 85 + 17, frame++, tab);
		setBounds(17213, 44 - 3, 85 + 17, frame++, tab);
		setBounds(17214, 79 - 3, 85 + 17, frame++, tab);
		setBounds(17215, 116 - 3, 85 + 17, frame++, tab);
		setBounds(17216, 153 - 3, 85 + 17, frame++, tab);
		setBounds(17217, 5 - 3, 124 + 17, frame++, tab);
		setBounds(17218, 44 - 3, 124 + 17, frame++, tab);
		setBounds(17219, 79 - 3, 124 + 17, frame++, tab);
		setBounds(17220, 116 - 3, 124 + 17, frame++, tab);
		setBounds(17221, 153 - 3, 124 + 17, frame++, tab);
		setBounds(17222, 5 - 3, 160 + 17, frame++, tab);
		setBounds(17223, 44 - 3, 160 + 17, frame++, tab);
		setBounds(17224, 79 - 3, 160 + 17, frame++, tab);
		setBounds(17225, 116 - 3, 160 + 17, frame++, tab);
		setBounds(17226, 153 - 3, 160 + 17, frame++, tab);
		setBounds(17227, 4 - 3, 207 + 4, frame++, tab);
		setBounds(17228, 44 - 3, 207 + 4, frame++, tab);
		setBounds(15891, 79 - 3, 207 + 4, frame++, tab);

		setBounds(17230, 5, 5, frame++, tab);//text
		setBounds(17231, 0, 237, frame++, tab);//confirm
		setBounds(17232, 0, 237, frame++, tab);//Confirm hover
	}

	public static void skillTab602() {
		RSInterface skill = addInterface(3917, 512, 334);

		for (int i : logoutID) {
			RSInterface logout = interfaceCache[i];
			logout.textColor = 0xFF981F;
			logout.contentType = 0;
		}
		for (int i : logoutID2) {
			RSInterface Logout = interfaceCache[i];
			Logout.contentType = 0;
		}
		for (int i = 0; i < hovers.length; i++) {
			createSkillHover(hovers[i], 205 + i);
			addHoverButton(79924 + i, 274, 60, 27, "Set level goal", 1321, -1, 1);
			addHoverButton(79949 + i, 274, 60, 27, "Set exp goal", 1322, -1, 1);
			addHoverButton(79974 + i, 274, 60, 27, "Clear goal", 1323, -1, 1);
			addSkillButton(buttons[i], Client.skillNames[i]);
			addImage(icons[i], "/Interfaces/Skill/" + spriteNames[i]);
		}
		for (int i = 0; i < 3; i++) {
			addSkillText(newText[0][i], false, i + 21);
			addSkillText(newText[1][i], true, i + 21);
		}
		skill.children(icons.length + (text.length * 2) + hovers.length + buttons.length * 4 + 3);
		int frame = 0;
		RSInterface totalLevel = interfaceCache[3984];
		totalLevel.message = "<col=ffff00>Total level: %1";
		totalLevel.textDrawingAreas = 1;
		skill.child(frame, 27207, 37, 230);
		frame++;
		addSprite(27207, 0, "/Interfaces/Skill/Total");
		skill.child(frame, 3984, 52, 237);
		frame++;
		for (int i = 0; i < buttons.length; i++) {
			skill.child(frame++, 79974 + i, buttonCoords[i][0], buttonCoords[i][1]);
			skill.child(frame++, 79949 + i, buttonCoords[i][0], buttonCoords[i][1]);
			skill.child(frame++, 79924 + i, buttonCoords[i][0], buttonCoords[i][1]);
			skill.child(frame, buttons[i], buttonCoords[i][0], buttonCoords[i][1] + 0);
			frame++;
		}
		for (int i = 0; i < icons.length; i++) {
			skill.child(frame, icons[i], iconCoords[i][0], iconCoords[i][1]);
			frame++;
		}
		for (int i = 0; i < text.length; i++) {
			skill.child(frame, text[i][0], textCoords[i][0], textCoords[i][1] + 0);
			frame++;
		}
		for (int i = 0; i < text.length; i++) {
			skill.child(frame, text[i][1], textCoords[i][2], textCoords[i][3] + 0);
			frame++;
		}
		for (int i = 0; i < hovers.length; i++) {
			skill.child(frame, hovers[i], buttonCoords[i][0], buttonCoords[i][1]);
			frame++;
		}
		createTotalExp(19009, 231);
		skill.child(frame, 19009, 37, 230);
		frame++;
	}

	private static void addHoverButton(int id, int j, int width, int height, String text, int anInt214, int hoverOver,
									   int aT) {
		RSInterface component = addTabInterface(id);
		component.id = id;
		component.parentID = id;
		component.type = 5;
		component.atActionType = aT;
		component.contentType = anInt214;
		component.opacity = 0;

		if (j >= 0) {
			component.sprite1 = Client.instance.cacheSprite[j];
		}

		component.width = width;
		component.height = height;
		component.tooltip = text;
	}

	public static void addImage(int id, String s) {
		RSInterface image = addInterface(id, 512, 334);
		image.type = 5;
		image.atActionType = 0;
		image.contentType = 0;
		image.width = 100;
		image.height = 100;
		image.sprite1 = getSprite(s);
	}

	public static void addSkillText(int id, boolean max, int skill) {
		RSInterface text = addInterface(id, 512, 334);
		text.id = id;
		text.parentID = id;
		text.type = 4;
		text.atActionType = 0;
		text.width = 15;
		text.height = 12;
		text.centerText = true;
		text.textShadow = true;
		text.textColor = 16776960;
		text.textDrawingAreas = 0;

		text.valueIndexArray = new int[1][];
		text.valueIndexArray[0] = new int[3];
		text.valueIndexArray[0][0] = max ? 2 : 1;
		text.valueIndexArray[0][1] = skill;
		text.valueIndexArray[0][2] = 0;
		//text.valueIndexArray[0][3] = skill;
		//text.valueIndexArray[0][4] = 0;
		/*if (!max) {
            text.valueIndexArray = new int[1][];
            text.valueIndexArray[0] = new int[3];
            text.valueIndexArray[0][0] = 1;
            text.valueIndexArray[0][1] = skill;
            text.valueIndexArray[0][2] = 0;
        } else {
            text.valueIndexArray = new int[2][];
            text.valueIndexArray[0] = new int[3];
            text.valueIndexArray[0][0] = 1;
            text.valueIndexArray[0][1] = skill;
            text.valueIndexArray[0][2] = 0;
            text.valueIndexArray[1] = new int[1];
            text.valueIndexArray[1][0] = 0;
        }*/


		text.message = "%1";
	}

	public static void addSkillButton(int id, String skillGuide) {
		RSInterface button = addInterface(id, 512, 334);
		button.type = 5;
		button.atActionType = 5;
		button.contentType = 0;
		button.width = 60;
		button.height = 27;
		button.sprite1 = CustomSpriteLoader(33225, "");
		button.sprite1 = getSprite("/Interfaces/Skill/Button");
		button.tooltip = "<col=ffffff>View <col=ffb000>" + skillGuide + " <col=ffffff>Guide";
	}

	public static Sprite getSprite(String s) {
		Sprite sprite = null;
		String spriteName = s;
		int actual = -1;
		for (int i = 0; i < fixedSprites3.length; i++) {
			if (fixedSprites3[i][0].equalsIgnoreCase(spriteName)) {
				actual = Integer.parseInt(fixedSprites3[i][1]);
			}
		}
		if (actual == -1)
			return null;
		sprite = Client.instance.cacheSprite[actual];
		if (sprite != null)
			return sprite;
		return null;
	}

	public static void createSkillHover(int id, int x) {
		RSInterface hover = addInterface(id, 512, 334);
		hover.type = 8;
		hover.message = "AFG-";
		hover.contentType = x;
		hover.width = 60;
		hover.height = 28;
	}

	public static void createTotalExp(int id, int x) {
		RSInterface hover = addInterface(id, 512, 334);
		hover.type = 8;
		hover.message = "TESTING!";
		hover.contentType = x;
		hover.width = 122;
		hover.height = 27;
	}

	private static Sprite method207(int i, StreamLoader streamLoader, String s) {
		long l = (TextClass.method585(s) << 8) + i;
		Sprite sprite = (Sprite) aMRUNodes_238.get(l);
		if (sprite != null)
			return sprite;
		try {
			sprite = new Sprite(streamLoader, s, i);
			aMRUNodes_238.put(sprite, l);
		} catch (Exception _ex) {
			return null;
		}
		return sprite;
	}

	public static RSInterface addInterface(int id, int width, int height) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		rsi.id = id;
		rsi.parentID = id;
		rsi.width = width;
		rsi.height = height;
		return rsi;
	}

	public static void bankContainer(int id) {
		RSInterface rsInterface = addTabInterface(id);
		rsInterface.actions = new String[]{"Withdraw-1", "Withdraw-5", "Withdraw-10", "Withdraw-100", "Withdraw-X", "Withdraw-All", "Withdraw-All but one"};
		rsInterface.contentType = 206;
		rsInterface.type = 2;
		rsInterface.width = 10;
		rsInterface.height = 50;
		rsInterface.invSpritePadX = 12;
		rsInterface.invSpritePadY = 6;
		rsInterface.invStackSizes = new int[1500];
		rsInterface.inv = new int[1500];
		rsInterface.aBoolean259 = true;
		rsInterface.parentID = 5292;
		rsInterface.id = id;
		//rsInterface.isInventoryInterface = true;
		//rsInterface.usableItemInterface = true;
		//rsInterface.itemsDraggable = true;
	}

	public static void bank() {
		RSInterface Interface = addTabInterface(5292);
		setChildren(38, Interface);
		addSprite(5293, 0, "Bank/BANK");
		setBounds(5293, 13, 13, 0, Interface);
		addHover(5384, 3, 0, 5380, 1, "Bank/BANK", 17, 17, "Close Window");
		addHovered(5380, 2, "Bank/BANK", 17, 17, 5379);
		setBounds(5384, 476, 16, 3, Interface);
		setBounds(5380, 476, 16, 4, Interface);
		addHover(5294, 4, 0, 5295, 3, "Bank/BANK", 114, 25, "Set a Bank PIN");
		addHovered(5295, 4, "Bank/BANK", 114, 25, 5296);
		setBounds(5294, 110, 285, 5, Interface);
		setBounds(5295, 110, 285, 6, Interface);

		addBankHover(22000, 4, 22001, 5, 8, "Bank/BANK", 35, 25, 304, 1, "Swap withdraw mode", 22002, 7, 6, "Bank/BANK", 22003, "Switch to insert items \nmode", "Switch to swap items \nmode.", 12, 20);
		setBounds(22000, 25, 285, 7, Interface);
		setBounds(22001, 10, 225, 8, Interface);

		addBankHover(22004, 4, 22005, 13, 15, "Bank/BANK", 35, 25, 0, 1, "Search", 22006, 14, 16, "Bank/BANK", 22007, "Click here to search your \nbank", "Click here to search your \nbank", 12, 20);
		setBounds(22004, 65, 285, 9, Interface);
		setBounds(22005, 50, 225, 10, Interface);

		addBankHover(22008, 4, 22009, 9, 11, "Bank/BANK", 35, 25, 115, 1, "Withdraw as note", 22010, 10, 12, "Bank/BANK", 22011, "Switch to note withdrawal \nmode", "Switch to item withdrawal \nmode", 12, 20);
		setBounds(22008, 240, 285, 11, Interface);
		setBounds(22009, 225, 225, 12, Interface);

		addBankHover1(22012, 5, 22013, 17, "Bank/BANK", 35, 25, "Deposit carried items", 22014, 18, "Bank/BANK", 22015, "Empty your backpack into\nyour bank", 0, 20);
		setBounds(22012, 375, 285, 13, Interface);
		setBounds(22013, 360, 225, 14, Interface);

		addBankHover1(22016, 5, 22017, 19, "Bank/BANK", 35, 25, "Deposit worn items", 22018, 20, "Bank/BANK", 22019, "Empty the items your are\nwearing into your bank", 0, 20);
		setBounds(22016, 415, 285, 15, Interface);
		setBounds(22017, 400, 225, 16, Interface);

		addBankHover1(22020, 5, 22021, 21, "Bank/BANK", 35, 25, "Deposit beast of burden inventory", 22022, 22, "Bank/BANK", 22023, "Empty your BoB's inventory\ninto your bank", 0, 20);
		setBounds(22020, 455, 285, 17, Interface);
		setBounds(22021, 440, 225, 18, Interface);

		setBounds(5383, 170, 15, 1, Interface);
		setBounds(5385, -4, 74, 2, Interface);

		addButton(22024, 0, "BANK/TAB", "View all items");
		setBounds(22024, 22, 36, 19, Interface);

		addButton(22025, 4, "BANK/TAB", null);
		setBounds(22025, 70, 36, 20, Interface);

		addButton(22026, 4, "BANK/TAB", null);
		setBounds(22026, 118, 36, 21, Interface);

		addButton(22027, 4, "BANK/TAB", null);
		setBounds(22027, 166, 36, 22, Interface);

		addButton(22028, 4, "BANK/TAB", null);
		setBounds(22028, 214, 36, 23, Interface);
		addButton(22029, 4, "BANK/TAB", null);
		setBounds(22029, 262, 36, 24, Interface);

		addButton(22030, 4, "BANK/TAB", null);
		setBounds(22030, 310, 36, 25, Interface);

		addButton(22031, 4, "BANK/TAB", null);
		setBounds(22031, 358, 36, 26, Interface);

		addButton(22032, 4, "BANK/TAB", null);
		setBounds(22032, 406, 36, 27, Interface);

		addText(22033, "134", 0, 0xB4965A, true, false, 0);
		setBounds(22033, 473, 42, 28, Interface);

		addText(22034, "496", 0, 0xB4965A, true, false, 0);
		setBounds(22034, 473, 57, 29, Interface);

		addBankItem(22035, false);
		setBounds(22035, 77, 39, 30, Interface);

		addBankItem(22036, false);
		setBounds(22036, 125, 39, 31, Interface);

		addBankItem(22037, false);
		setBounds(22037, 173, 39, 32, Interface);

		addBankItem(22038, false);
		setBounds(22038, 221, 39, 33, Interface);

		addBankItem(22039, false);
		setBounds(22039, 269, 39, 34, Interface);

		addBankItem(22040, false);
		setBounds(22040, 317, 39, 35, Interface);

		addBankItem(22041, false);
		setBounds(22041, 365, 39, 36, Interface);

		addBankItem(22042, false);
		setBounds(22042, 413, 39, 37, Interface);

		addSprite(15276, 0, "bank/separator");
		addSprite(15277, 0, "bank/highlight");

		setChildren(37, interfaceCache[5385]);
		for (int i = 0; i < 9; i++) {

			addText(15292 + i + 18, "Tab " + (i), 0xFF981F, false, true, 52, 1);
			bankContainer(15292 + i);

			// highlight
			setBounds(15277, 36, 3, i, interfaceCache[5385]);

			// item container
			setBounds(15292 + i, 38, 3, i + 9, interfaceCache[5385]);

			// text
			setBounds(15292 + i + 18, 38, 3, i + 18, interfaceCache[5385]);

			// seperator
			setBounds(15276, 38, 3, i + 27, interfaceCache[5385]);

			// set the first text, seperator and highlighter off the client
			if (i == 0) {
				interfaceCache[5385].childY[i] = 1000;
				interfaceCache[5385].childY[i + 18] = 1000;
				interfaceCache[5385].childY[i + 27] = 1000;
			}
		}

		// used for searching
		bankContainer(15302);
		interfaceCache[5385].children[36] = 15302;
		interfaceCache[5385].childY[36] = 1000;
		interfaceCache[5385].childX[36] = 36;

		addText(27000, "0", 0xFF981F, false, true, 52, 1);
		addText(27001, "0", 0xFF981F, false, true, 52, 1);
		addText(27002, "0", 0xFF981F, false, true, 52, 1);

		Interface = interfaceCache[5385];
		Interface.height = 206;
		Interface.width = 480;

		Interface = interfaceCache[5382];
		Interface.width = 10;
		Interface.invSpritePadX = 12;
		Interface.height = 35;

	}

	public static void addBankItem(int index, Boolean hasOption) {
		RSInterface rsi = interfaceCache[index] = new RSInterface();
		rsi.actions = new String[5];
		rsi.invStackSizes = new int[1];
		rsi.inv = new int[1];

		rsi.children = new int[0];
		rsi.childX = new int[0];
		rsi.childY = new int[0];
		rsi.drawItemCount = false;

		rsi.invSpritePadX = 24;
		rsi.invSpritePadY = 24;
		rsi.height = 1;
		rsi.width = 1;
		rsi.parentID = 5292;
		rsi.id = index;
		rsi.type = 2;
	}

	public static void addBankHover(int interfaceID, int actionType, int hoverid, int spriteId, int spriteId2, String NAME, int Width, int Height, int configFrame, int configId, String Tooltip, int hoverId2, int hoverSpriteId, int hoverSpriteId2, String hoverSpriteName, int hoverId3, String hoverDisabledText, String hoverEnabledText, int X, int Y) {
		RSInterface hover = addTabInterface(interfaceID);
		hover.id = interfaceID;
		hover.parentID = interfaceID;
		hover.type = 5;
		hover.atActionType = actionType;
		hover.contentType = 0;
		hover.opacity = 0;
		hover.mOverInterToTrigger = hoverid;
		hover.sprite1 = imageLoader(spriteId, NAME);
		hover.sprite2 = imageLoader(spriteId2, NAME);
		hover.width = Width;
		hover.tooltip = Tooltip;
		hover.height = Height;
		hover.valueCompareType = new int[1];
		hover.requiredValues = new int[1];
		hover.valueCompareType[0] = 1;
		hover.requiredValues[0] = configId;
		hover.valueIndexArray = new int[1][3];
		hover.valueIndexArray[0][0] = 5;
		hover.valueIndexArray[0][1] = configFrame;
		hover.valueIndexArray[0][2] = 0;
		hover = addTabInterface(hoverid);
		hover.parentID = hoverid;
		hover.id = hoverid;
		hover.type = 0;
		hover.atActionType = 0;
		hover.width = 550;
		hover.height = 334;
		hover.isMouseoverTriggered = true;
		hover.mOverInterToTrigger = -1;
		//addSprite(hoverId2, hoverSpriteId, hoverSpriteId2, hoverSpriteName, configId, configFrame);
		addHoverBox(hoverId3, interfaceID, hoverDisabledText, hoverEnabledText, configId, configFrame);
		setChildren(2, hover);
		setBounds(hoverId2, 15, 60, 0, hover);
		setBounds(hoverId3, X, Y, 1, hover);
	}

	public static void addBankHover1(int interfaceID, int actionType, int hoverid, int spriteId, String NAME, int Width, int Height, String Tooltip, int hoverId2, int hoverSpriteId, String hoverSpriteName, int hoverId3, String hoverDisabledText, int X, int Y) {
		RSInterface hover = addTabInterface(interfaceID);
		hover.id = interfaceID;
		hover.parentID = interfaceID;
		hover.type = 5;
		hover.atActionType = actionType;
		hover.contentType = 0;
		hover.opacity = 0;
		hover.mOverInterToTrigger = hoverid;
		hover.sprite1 = imageLoader(spriteId, NAME);
		hover.width = Width;
		hover.tooltip = Tooltip;
		hover.height = Height;
		hover = addTabInterface(hoverid);
		hover.parentID = hoverid;
		hover.id = hoverid;
		hover.type = 0;
		hover.atActionType = 0;
		hover.width = 550;
		hover.height = 334;
		hover.isMouseoverTriggered = true;
		hover.mOverInterToTrigger = -1;
		// addSprite(hoverId2, hoverSpriteId, hoverSpriteId, hoverSpriteName, 0, 0);
		addHoverBox(hoverId3, interfaceID, hoverDisabledText, hoverDisabledText, 0, 0);
		setChildren(2, hover);
		setBounds(hoverId2, 15, 60, 0, hover);
		setBounds(hoverId3, X, Y, 1, hover);
	}

	public static void infoTab() {
		RSInterface tab = addTabInterface(6000);
		RSInterface list = addInterface(6100, 512, 334);
		addSprite(54414, 4, "interfaces/quest/SPRITE");
		addSprite(6102, 5, "interfaces/quest/SPRITE");
		addText(54415, "" + Constants.CLIENT_NAME + " Information", 2, 0xeb981f, false, true, 0);

		tab.totalChildren(4);
		tab.child(0, 54414, 0, 22);
		tab.child(1, 54414, 0, 249);
		tab.child(2, 6100, 6, 24);
		tab.child(3, 54415, 5, 5);

		int start = 54416;

		for (int i = start; i <= start + 50; i++) {
			// addText(19161 + i, "", 0xFF0000, false, true, 52, TDA, 0, 0xFFFFFF);
			//public static void addText(int i, String s, int k, boolean l, boolean m, int a, RSFont[] TDA, int j, int dsc) {

			//addText(i, "", 0xff0000, false, true, 150, tda, 0, 0xFFFFFF);

			addClickableText(i, "", "", 0, 0xff0000, false, true, 150);
			//interfaceCache[i].message = "";
		}

		list.totalChildren(51);
		list.child(0, start, 0, 3);

		for (int i = (start + 1); i <= start + 50; i++) {
			int id = i - start;

			int yAddition = 0;
			if (id == 12) {
				yAddition = 14;
			}

			list.child(id, i, 5, list.childY[id - 1] + 14 + yAddition);
		}

		interfaceCache[start].message = "<col=ff7000>Uptime: 0 hours";
		interfaceCache[start].textDrawingAreas = 1;
		interfaceCache[start].centerText = true;

		//6004

		interfaceCache[start + 2].message = "<col=ffff00>Teleports";
		interfaceCache[start + 2].textDrawingAreas = 1;
		interfaceCache[start + 2].centerText = true;

        /*interfaceCache[6107].message = "<col=ffff00>Donator Teleports";
        interfaceCache[6107].textDrawingAreas = tda[1];
        interfaceCache[6107].centerText = true;*/

		interfaceCache[start + 4].message = "<col=ffffff>-> <col=00ff00>Online Staff Members";
		interfaceCache[start + 5].message = "<col=ffffff>-> <col=00ff00>Donate";
		interfaceCache[start + 6].message = "<col=ffffff>-> <col=00ff00>Forums";
		interfaceCache[start + 7].message = "<col=ffffff>-> <col=00ff00>Vote";
		interfaceCache[start + 8].message = "<col=ffffff>-> <col=00ff00>Download Client";
		interfaceCache[start + 9].message = "<col=ffffff>-> <col=00ff00>Hiscores";
		interfaceCache[start + 10].message = "<col=ffffff>-> <col=00ff00>Guides";
		interfaceCache[start + 11].message = "<col=ffffff>-> <col=00ff00>Boss Kill Counts";

		interfaceCache[start + 12].message = "<col=ffb000>[<col=ffffff>0<col=ffb000>] Vote Points";
		interfaceCache[start + 13].message = "<col=ffb000>[<col=ffffff>0<col=ffb000>] Slayer Points";
		interfaceCache[start + 14].message = "<col=ffb000>[<col=ffffff>0<col=ffb000>] Pest Control Points";
		interfaceCache[start + 15].message = "<col=ffb000>[<col=ffffff>0<col=ffb000>] Pk Points";
		interfaceCache[start + 16].message = "<col=ffb000>[<col=ffffff>0<col=ffb000>] Kills";
		interfaceCache[start + 17].message = "<col=ffb000>[<col=ffffff>0<col=ffb000>] Deaths";
		interfaceCache[start + 18].message = "<col=ffb000>Is Muted:<col=ffffff> No";
		interfaceCache[start + 19].message = "<col=ffb000>Is Donator:<col=ffffff> No";
		interfaceCache[start + 20].message = "<col=ffb000>EXP Status:<col=ffffff> Locked";
		interfaceCache[start + 21].message = "<col=ffb000>Time Online:<col=ffffff>";
		interfaceCache[start + 22].message = "<col=ffb000>Current Perk:<col=ffffff>";

		System.out.println("START: " + start + 22);

		list.width = 168;
		list.height = 225;
		list.scrollMax = 350;
	}

	public static void addClickableText(int id, String text, String tooltip, int idx, int color, boolean center, boolean shadow, int width) {
		RSInterface tab = addTabInterface(id);
		tab.parentID = id;
		tab.id = id;
		tab.type = 4;
		tab.atActionType = 1;
		tab.width = width;
		tab.height = 13;
		tab.contentType = 0;
		tab.opacity = 0;
		tab.mOverInterToTrigger = -1;
		tab.centerText = center;
		tab.textShadow = shadow;
		tab.textDrawingAreas = idx;
		tab.message = text;
		tab.message = "";
		tab.textColor = color;
		tab.anInt219 = 0;
		tab.textHoverColor = 0xffffff;
		tab.anInt239 = 0;
		tab.tooltip = " ";
	}

	/*
 * @author Cobalt_rx@inet.ua
 * @date 26 June 2013
 */
	public static void zombiesLobbyDisplay() {
		RSInterface rsinterface = addInterface(27325, 512, 334);
		addSprite(27326, 4, "Interfaces/Minigame/ZOMBIE");
		addText(27327, "0:00", 0, 0xFFFFFF, false, true, 0);
		addText(27328, "0", 0, 0xFFFFFF, false, true, 0);
		addText(27329, "0", 0, 0xFFFFFF, false, true, 0);
		addText(27330, "0", 0, 0xFFFFFF, false, true, 0);
		rsinterface.totalChildren(5); //-316, 250
		rsinterface.child(0, 27326, 10, 10);
		rsinterface.child(1, 27327, 164, 23);
		rsinterface.child(2, 27328, 164, 35);
		rsinterface.child(3, 27329, 164, 47);
		rsinterface.child(4, 27330, 164, 59);
	}

	/*
	 * @author Cobalt_rx@inet.ua
	 * @date 26 June 2013
	 */
	public static void zombiesDisplay() {
		RSInterface rsinterface = addInterface(27310, 512, 334);
		addSprite(27311, 3, "Interfaces/Minigame/ZOMBIE");
		addText(27312, "0", 0, 0xFFFFFF, false, true, 0);
		addText(27313, "0:00", 0, 0xFFFFFF, false, true, 0);
		addText(27314, "0", 0, 0xFFFFFF, false, true, 0);
		addText(27315, "0", 0, 0xFFFFFF, false, true, 0);
		rsinterface.totalChildren(5); //-316, 250
		rsinterface.child(0, 27311, 10, 10);
		rsinterface.child(1, 27312, 164, 23);
		rsinterface.child(2, 27313, 164, 35);
		rsinterface.child(3, 27314, 164, 47);
		rsinterface.child(4, 27315, 164, 59);
	}

	/*
 * @author Cobalt_rx@inet.ua
 * @date 26 June 2013
 */
	public static void fightpitsDisplay() {
		RSInterface rsinterface = addInterface(27316, 512, 334);
		addSprite(27317, 0, "Interfaces/Minigame/PITS");
		addText(27318, "Current Champion: <col=ffffff>PlayerName13", 0, 0xff9b00, true, true, 0);
		addText(27319, "Time Remaining: <col=ffffff>7 Minutes", 0, 0xff9b00, true, true, 0);
		rsinterface.totalChildren(3);
		rsinterface.child(0, 27317, 130, 3);
		rsinterface.child(1, 27318, 260, 19);
		rsinterface.child(2, 27319, 260, 41);
	}

	public static void clanBattleRequest() {
		int id = 9000;

		RSInterface rsi = addInterface(id, 512, 334);

		addSprite(id + 1, 0, "/Interfaces/ClanWars/CLAN");

		addText(id + 2, "PlayerName is inviting your clan to a battle!", 3, 0xff9b00, true, true, 0);
		interfaceCache[id + 2].width = 300;

		addHoverButton(id + 3, "/Interfaces/ClanWars/CLAN", 6, 72, 32, "Accept", -1, id + 4, 1);
		addHoveredButton(id + 4, "/Interfaces/ClanWars/CLAN", 7, 72, 32, id + 5);
		addText(id + 6, "Accept", 1, 0xff9b00, false, true, 0);
		addHoverButton(id + 7, "/Interfaces/ClanWars/CLAN", 6, 72, 32, "Decline", -1, id + 8, 1);
		addHoveredButton(id + 8, "/Interfaces/ClanWars/CLAN", 7, 72, 32, id + 9);
		addText(id + 10, "Decline", 1, 0xff9b00, false, true, 0);

		int[][] children = {{id + 1, 16, 254}, {id + 2, 90, 254},
				{id + 3, 145, 283}, {id + 4, 145, 283},
				{id + 6, 164, 290}, {id + 7, 250, 283},
				{id + 8, 250, 283}, {id + 10, 269, 290}};

		rsi.totalChildren(children.length);
		for (int i = 0; i < children.length; i++) {
			rsi.child(i, children[i][0], children[i][1], children[i][2]);
		}
		
		children = null;
	}

	public static final void buildClanwarsInterface() {
		RSInterface rsi = addInterface(29000, 512, 334);

		addSprite(29001, 122, "Interfaces/Clanwars/cw");
		addHoverButton(29002, "Interfaces/Clanwars/cw", 135, 24, 24, "Close Window", -1, 29003, 3);
		addHoveredButton(29003, "Interfaces/Clanwars/cw", 136, 24, 24, 29004);
		addHDSprite(29005, 128, "Interfaces/Clanwars/cw");
		addHDSprite(29006, 127, "Interfaces/Clanwars/cw");
		addHDSprite(29007, 129, "Interfaces/Clanwars/cw");
		addHDSprite(29008, 126, "Interfaces/Clanwars/cw");
		addHDSprite(29009, 125, "Interfaces/Clanwars/cw");
		addHDSprite(29010, 124, "Interfaces/Clanwars/cw");
		addHDSprite(29011, 123, "Interfaces/Clanwars/cw");

		for (int i = 0; i < 7; i++)
			addConfigButton(29012 + i, 29000, 133, 134, "Interfaces/Clanwars/cw", 15, 15, "Toggle", 1, 1, 700 + i);

		addConfigButton(29019, 29000, 134, 133, "Interfaces/Clanwars/cw", 15, 15, "Toggle", 1, 1, 721);
		addText(29020, "Knock out", 1, 0xff9b00, false, true, 0);
		addText(29021, "(No run-ins)", 1, 0xff9b00, false, true, 0);
		addText(29022, "First team to...", 1, 0xff9b00, false, true, 0);

		addConfigButton(29023, 29000, 132, 133, "Interfaces/Clanwars/cw", 15, 15, "Toggle", 1, 1, 720);
		addText(29024, "No limit", 1, 0xff9b00, false, true, 0);

		addConfigButton(29025, 29000, 132, 133, "Interfaces/Clanwars/cw", 15, 15, "Toggle", 1, 1, 728);
		addText(29026, "Most kills at end", 1, 0xff9b00, false, true, 0);

		addConfigButton(29027, 29000, 132, 133, "Interfaces/Clanwars/cw", 15, 15, "Toggle", 1, 1, 729);
		addText(29028, "Kill 'em all", 1, 0xff9b00, false, true, 0);

		addConfigButton(29029, 29000, 132, 133, "Interfaces/Clanwars/cw", 15, 15, "Toggle", 1, 1, 730);
		addText(29030, "Ignore 5", 1, 0xff9b00, false, true, 0);

		addConfigButton(29031, 29000, 131, 131, "Interfaces/Clanwars/cw", 22, 22, "Toggle", 1, 1, 731);
		addText(29032, "... You keep", 1, 0xff9b00, false, true, 0);
		addText(29033, "your items.", 1, 0xff9b00, false, true, 0);
		addText(29034, "Clan Wars Options:", 2, 0xff9b00, false, true, 0);
		addButton(29035, 137, "Interfaces/Clanwars/cw", "Accept");
		addText(29036, "Accept", 1, 0x61a31c, false, true, 0);
		addText(29037, "", 1, 0x61a31c, true, true, 0);

		setChildren(39, rsi);
		setBounds(29001, 12, 14, 0, rsi);
		setBounds(29002, 472, 23, 1, rsi);
		setBounds(29003, 472, 23, 2, rsi);
		setBounds(29005, 260, 90, 3, rsi);
		setBounds(29006, 310, 90, 4, rsi);
		setBounds(29007, 370, 90, 5, rsi);
		setBounds(29008, 428, 87, 6, rsi);
		setBounds(29009, 285, 142, 7, rsi);
		setBounds(29010, 345, 142, 8, rsi);
		setBounds(29011, 405, 140, 9, rsi);
		setBounds(29012, 290, 120, 10, rsi);
		setBounds(29013, 345, 120, 11, rsi);
		setBounds(29014, 400, 120, 12, rsi);
		setBounds(29015, 460, 120, 13, rsi);
		setBounds(29016, 320, 175, 14, rsi);
		setBounds(29017, 380, 175, 15, rsi);
		setBounds(29018, 440, 175, 16, rsi);
		setBounds(29019, 27, 92, 17, rsi);
		setBounds(29020, 45, 84, 18, rsi);
		setBounds(29021, 45, 100, 19, rsi);
		setBounds(29022, 27, 120, 20, rsi);
		setBounds(29100, 27, 135, 21, rsi);
		setBounds(29023, 155, 141, 22, rsi);
		setBounds(29024, 175, 141, 23, rsi);
		setBounds(29130, 155, 84, 24, rsi);
		setBounds(29025, 27, 209, 25, rsi);
		setBounds(29026, 47, 209, 26, rsi);
		setBounds(29027, 27, 270, 27, rsi);
		setBounds(29028, 47, 270, 28, rsi);
		setBounds(29029, 27, 290, 29, rsi);
		setBounds(29030, 47, 290, 30, rsi);
		setBounds(29170, 258, 232, 31, rsi);
		setBounds(29031, 185, 199, 32, rsi);
		setBounds(29032, 165, 225, 33, rsi);
		setBounds(29033, 165, 240, 34, rsi);
		setBounds(29034, 25, 24, 35, rsi);
		setBounds(29035, 150, 272, 36, rsi);
		setBounds(29036, 183, 275, 37, rsi);
		setBounds(29037, 200, 290, 38, rsi);

		RSInterface scroll1 = addInterface(29100, 101, 67);
		scroll1.scrollMax = 105;
		setChildren(12, scroll1);
		int y = 0;
		int index = 0;
		for (int i = 0; i < 12; i += 2) {
			addConfigButton(29101 + i, 29100, 132, 133, "Interfaces/Clanwars/cw", 15, 15, "Toggle", 1, 1, 722 + index);
			addText(29101 + i + 1, kills[index] + " Kills", 1, 0xff9b00, false, true, 0);
			setBounds(29101 + i, 0, y, i, scroll1);
			setBounds(29101 + i + 1, 20, y, i + 1, scroll1);
			y += 17;
			index++;
		}

		RSInterface scroll2 = addInterface(29130, 77, 56);
		scroll2.scrollMax = 140;
		setChildren(16, scroll2);
		y = 0;
		index = 0;
		for (int i = 0; i < 16; i += 2) {
			addConfigButton(29131 + i, 29130, 132, 133, "Interfaces/Clanwars/cw", 15, 15, "Toggle", 1, 1, 712 + index);
			addText(29131 + i + 1, times[index], 1, 0xff9b00, false, true, 0);
			setBounds(29131 + i, 0, y, i, scroll2);
			setBounds(29131 + i + 1, 20, y, i + 1, scroll2);
			y += 17;
			index++;
		}

		RSInterface scroll3 = addInterface(29170, 212, 75);
		scroll3.scrollMax = 230;
		setChildren(20, scroll3);
		index = 0;
		y = 0;
		for (int i = 0; i < 20; i += 4) {
			addConfigButton(29171 + i, 29170, 132, 133, "Interfaces/Clanwars/cw", 15, 15, "Toggle", 1, 1, 707 + index);
			addText(29171 + i + 1, mapNames[index], 1, 0xff9b00, false, true, 0);
			addText(29171 + i + 2, mapDescriptions[index][0], 0, 0xff9b00, false, true, 0);
			addText(29171 + i + 3, mapDescriptions[index][1], 0, 0xff9b00, false, true, 0);
			setBounds(29171 + i, 0, y, i, scroll3);
			setBounds(29171 + i + 1, 20, y, i + 1, scroll3);
			setBounds(29171 + i + 2, 0, y + 17, i + 2, scroll3);
			setBounds(29171 + i + 3, 0, y + 29, i + 3, scroll3);
			y += 45;
			index++;
		}
	}

	public static void addHDSprite(int id, int spriteId, String spriteName) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		rsi.id = id;
		rsi.parentID = id;
		rsi.type = 5;
		rsi.atActionType = 0;
		rsi.contentType = 0;
		rsi.opacity = (byte) 0;
		rsi.sprite1 = imageLoader(spriteId, spriteName);
	}

	public static void clanswarsGame() {
		RSInterface tab = addTabInterface(28540);
		addRectangle(28541, 128, 0x595144, true, 152, 78);
		addRectangle(28542, 0, 0x372F22, false, 152, 78);
		addRectangle(28543, 0, 0x595144, false, 150, 76);
		addRectangle(28544, 0, 0x000000, true, 2, 31);
		addRectangle(28545, 0, 0x372F22, true, 148, 1);
		addRectangle(28546, 0, 0x595144, true, 148, 1);

		addText(28547, "My clan:", 0, 0xffb000, true, true, 0);
		addText(28548, "Opposing clan:", 0, 0xffb000, true, true, 0);
		addText(28549, "Players:", 0, 0xffb000, true, true, 0);
		addText(28550, "1", 0, 0xffb000, true, true, 0);
		addText(28551, "1", 0, 0xffb000, true, true, 0);
		addText(28552, "Countdown to battle:", 0, 0xffb000, true, true, 0);
		addText(28553, "0m 10s", 0, 0xffffff, true, true, 0);

		//public static void addRectangle(int id, int opacity, int color, boolean filled, int width, int height) {

		addRectangle(28554, 128, 0x595144, true, 64, 68);
		addRectangle(28555, 0, 0x372F22, false, 64, 68);
		addRectangle(28556, 0, 0x595144, false, 62, 66);
		addText(28557, "ON DEATH", 0, 0xffb000, true, true, 0);
		addSprite(28558, 131, "Interfaces/Clanwars/cw");
		addText(28559, "Items are", 0, 0xffb000, true, true, 0);
		addText(28560, "Kept", 0, 0x00ff00, true, true, 0);

		final int BASE_X = 355, BASE_Y = 3;
		tab.totalChildren(21);
		tab.child(0, 28541, 0 + BASE_X, 0 + BASE_Y);
		tab.child(1, 28542, 0 + BASE_X, 0 + BASE_Y);
		tab.child(2, 28543, 1 + BASE_X, 1 + BASE_Y);
		tab.child(3, 28544, 70 + BASE_X, 4 + BASE_Y);
		tab.child(4, 28545, 3 + BASE_X, 40 + BASE_Y);
		tab.child(5, 28546, 3 + BASE_X, 41 + BASE_Y);
		tab.child(6, 28547, 35 + BASE_X, 5 + BASE_Y);
		tab.child(7, 28548, 111 + BASE_X, 5 + BASE_Y);
		tab.child(8, 28549, 25 + BASE_X, 20 + BASE_Y);
		tab.child(9, 28549, 97 + BASE_X, 20 + BASE_Y);
		tab.child(10, 28550, 64 + BASE_X, 21 + BASE_Y);
		tab.child(11, 28551, 146 + BASE_X, 21 + BASE_Y);
		tab.child(12, 28552, 80 + BASE_X, 45 + BASE_Y);
		tab.child(13, 28553, 76 + BASE_X, 61 + BASE_Y);
		tab.child(14, 28554, 88 + BASE_X, 100 + BASE_Y);
		tab.child(15, 28555, 87 + BASE_X, 99 + BASE_Y);
		tab.child(16, 28556, 88 + BASE_X, 100 + BASE_Y);
		tab.child(17, 28557, 120 + BASE_X, 105 + BASE_Y);
		tab.child(18, 28558, 110 + BASE_X, 117 + BASE_Y);
		tab.child(19, 28559, 123 + BASE_X, 140 + BASE_Y);
		tab.child(20, 28560, 123 + BASE_X, 152 + BASE_Y);
	}

	public static void addRectangle(int id, int opacity, int color, boolean filled, int width, int height) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.textColor = color;
		tab.filled = filled;
		tab.id = id;
		tab.parentID = id;
		tab.type = 3;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.opacity = (byte) opacity;
		tab.width = width;
		tab.height = height;
	}

	public static void slayerAssignmentTab() {
		RSInterface rsInterface = addInterface(19748, 512, 334);
		addSprite(19749, 2, "Interfaces/SlayerSprites/Background");
		// Close Buttons
		addHoverButton(19750, "Interfaces/SlayerSprites/Close", 0, 16, 16, "Close window", 0, 19751, 3);
		addHoveredButton(19751, "Interfaces/SlayerSprites/Close", 1, 16, 16, 19752);
		// Buy Options
		addHoverButton(19765, "Interfaces/SlayerSprites/Buy", 0, 70, 18, "Purchase", 0, 19766, 1);
		addHoveredButton(19766, "Interfaces/SlayerSprites/Buy", 1, 70, 18, 19767);
		addHoverButton(19768, "Interfaces/SlayerSprites/Buy", 0, 70, 18, "Purchase", 0, 19769, 1);
		addHoveredButton(19769, "Interfaces/SlayerSprites/Buy", 1, 70, 18, 19770);
		// Remove Options
		addHoverButton(19796, "Interfaces/SlayerSprites/Exit", 4, 16, 16, "Delete from removed list", 0, 19797, 1);
		addHoveredButton(19797, "Interfaces/SlayerSprites/Exit", 5, 16, 16, 19798);
		addHoverButton(19799, "Interfaces/SlayerSprites/Exit", 4, 16, 16, "Delete from removed list", 0, 19800, 1);
		addHoveredButton(19800, "Interfaces/SlayerSprites/Exit", 5, 16, 16, 19801);
		addHoverButton(19802, "Interfaces/SlayerSprites/Exit", 4, 16, 16, "Delete from removed list", 0, 19803, 1);
		addHoveredButton(19803, "Interfaces/SlayerSprites/Exit", 5, 16, 16, 19804);
		addHoverButton(19805, "Interfaces/SlayerSprites/Exit", 4, 16, 16, "Delete from removed list", 0, 19806, 1);
		addHoveredButton(19806, "Interfaces/SlayerSprites/Exit", 5, 16, 16, 19807);
		addHoverButton(19808, "Interfaces/SlayerSprites/Exit", 4, 16, 16, "Delete from removed list", 0, 19809, 1);
		addHoveredButton(19809, "Interfaces/SlayerSprites/Exit", 5, 16, 16, 19810);
		addHoverButton(19811, "Interfaces/SlayerSprites/Exit", 4, 16, 16, "Delete from removed list", 0, 19812, 1);
		addHoveredButton(19812, "Interfaces/SlayerSprites/Exit", 5, 16, 16, 19813);
		// Text
		addText(19771, "Edit Assignments", 2, 0xFF6000);
		addText(19777, "Purchase", 0, 0xFF6000);
		addText(19778, "Currently removed:", 2, 0xFFFFFF);
		addText(19779, "Available geSlots:  1", 2, 0xFF6000);
		addText(19780, "Cancel current task:", 1, 0xFFFFFF);
		addText(19781, "Never assign task again:", 1, 0xFFFFFF);
		addText(19782, "30 Points", 1, 0x00FF00);
		addText(19783, "100 Points", 1, 0x00FF00);
		addText(19795, "Slayer Points: 1500", 2, 0xFFFFFF);
		addText(19784, "Nothing", 1, 0x848484);
		addText(19785, "Nothing", 1, 0x848484);
		addText(19786, "Nothing", 1, 0x848484);
		addText(19787, "Nothing", 1, 0x848484);
		addText(19788, "Nothing", 1, 0x848484);
		addText(19789, "Nothing", 1, 0x848484);
		addText(19790, "Reassign previous task:", 1, 0xFFFFFF);
		addText(19791, "30 Points", 1, 0x00FF00);
		addHoverButton(19792, "Interfaces/SlayerSprites/Buy", 0, 70, 18, "Purchase", 0, 19793, 1);
		addHoveredButton(19793, "Interfaces/SlayerSprites/Buy", 1, 70, 18, 19794);

		int index = 0;

		setChildren(39, rsInterface); // 0x848484
		rsInterface.child(index++, 19749, 0, 0); // Background
		rsInterface.child(index++, 19750, 483, 14); // Close Button
		rsInterface.child(index++, 19751, 483, 14); // Close Button
		rsInterface.child(index++, 19765, 405, 34); // Buy Option
		rsInterface.child(index++, 19766, 405, 34); // Buy Option
		rsInterface.child(index++, 19768, 405, 62); // Buy Option
		rsInterface.child(index++, 19769, 405, 62); // Buy Option
		rsInterface.child(index++, 19771, 185, 13); // Title
		rsInterface.child(index++, 19777, 416, 36); // Purchase Text
		rsInterface.child(index++, 19777, 416, 65); // Purchase Text
		rsInterface.child(index++, 19778, 20, 120); // Current Removed
		rsInterface.child(index++, 19780, 30, 35); // Remove Current Task
		rsInterface.child(index++, 19781, 30, 66); // Never assign task
		rsInterface.child(index++, 19782, 325, 35); // 30 Points
		rsInterface.child(index++, 19783, 325, 65); // 100 Points
		rsInterface.child(index++, 19784, 40, 142); // Nothing
		rsInterface.child(index++, 19785, 40, 162); // Nothing
		rsInterface.child(index++, 19786, 40, 183); // Nothing
		rsInterface.child(index++, 19787, 40, 205); // Nothing
		rsInterface.child(index++, 19788, 40, 226); // Nothing
		rsInterface.child(index++, 19789, 40, 246); // Nothing
		rsInterface.child(index++, 19796, 468, 143); // Remove
		rsInterface.child(index++, 19797, 468, 143); // Remove
		rsInterface.child(index++, 19799, 468, 164); // Remove
		rsInterface.child(index++, 19800, 468, 164); // Remove
		rsInterface.child(index++, 19802, 468, 184); // Remove
		rsInterface.child(index++, 19803, 468, 184); // Remove
		rsInterface.child(index++, 19805, 468, 204); // Remove
		rsInterface.child(index++, 19806, 468, 204); // Remove
		rsInterface.child(index++, 19808, 468, 225); // Remove
		rsInterface.child(index++, 19809, 468, 225); // Remove
		rsInterface.child(index++, 19811, 468, 246); // Remove
		rsInterface.child(index++, 19812, 468, 246); // Remove
		rsInterface.child(index++, 19795, 24, 271); // Points
		rsInterface.child(index++, 19790, 30, 93); // Reassign current task
		rsInterface.child(index++, 19791, 325, 93); // Points
		rsInterface.child(index++, 19792, 405, 90); // Purchase
		rsInterface.child(index++, 19793, 405, 90); // Purchase
		rsInterface.child(index++, 19777, 416, 93); // Purchase Text
	}

	public static void teleportInterface() {
		RSInterface rsInterface = addInterface(53399, 512, 334);

		setChildren(18, rsInterface);
		addSprite(53400, 0, "teleport/background");
		addHoverButton(53401, "teleport/close", 0, 16, 16, "Close window", 0, 53402, 3);
		addHoveredButton(53402, "Interfaces/SlayerSprites/Close", 1, 16, 16, 53403);

		addHoverButton(53404, "teleport/cat", 1, 136, 34, "Training Teleports", 0, 53405, 1);
		addHoveredButton(53405, "teleport/cat hover", 1, 136, 34, 53406);

		addHoverButton(53407, "teleport/cat", 2, 136, 34, "Slayer Teleports", 0, 53408, 1);
		addHoveredButton(53408, "teleport/cat hover", 2, 136, 34, 53409);

		addHoverButton(53410, "teleport/cat", 3, 136, 34, "Skilling Teleports", 0, 53411, 1);
		addHoveredButton(53411, "teleport/cat hover", 3, 136, 34, 53412);

		addHoverButton(53413, "teleport/cat", 4, 136, 34, "Pking Teleports", 0, 53414, 1);
		addHoveredButton(53414, "teleport/cat hover", 4, 136, 34, 53415);

		addHoverButton(53416, "teleport/cat", 5, 136, 34, "Minigame Teleports", 0, 53417, 1);
		addHoveredButton(53417, "teleport/cat hover", 5, 136, 34, 53418);

		addHoverButton(53419, "teleport/cat", 6, 136, 34, "Bossing Teleports", 0, 53420, 1);
		addHoveredButton(53420, "teleport/cat hover", 6, 136, 34, 53421);

		addHoverButton(53422, "teleport/cat", 7, 136, 34, "Donator Teleports", 0, 53423, 1);
		addHoveredButton(53423, "teleport/cat hover", 7, 136, 34, 53424);

		rsInterface.child(0, 53400, 5, 5); // Background
		rsInterface.child(1, 53401, 483, 8); // Close Button
		rsInterface.child(2, 53402, 483, 8); // Close Button hover

		rsInterface.child(3, 53404, 349, 41); // training
		rsInterface.child(4, 53405, 349, 41); // training

		rsInterface.child(5, 53407, 349, 79); // slayer
		rsInterface.child(6, 53408, 349, 79); // slayer

		rsInterface.child(7, 53410, 349, 117); // skilling
		rsInterface.child(8, 53411, 349, 117); // skilling

		rsInterface.child(9, 53413, 349, 156); // pking
		rsInterface.child(10, 53414, 349, 156); // pking

		rsInterface.child(11, 53416, 349, 195); // minigames
		rsInterface.child(12, 53417, 349, 195); // minigames

		rsInterface.child(13, 53419, 349, 234); // bossing
		rsInterface.child(14, 53420, 349, 234); // bossing

		rsInterface.child(15, 53422, 349, 272); // donator
		rsInterface.child(16, 53423, 349, 272); // donator

		rsInterface.child(17, 53425, 37, 39); // teles

		generateTeleports(TRAINING_NAMES, 53425);
		generateTeleports(SLAYER_NAMES, 53585);
		generateTeleports(SKILLING_NAMES, 53706);
		generateTeleports(PKING_NAMES, 53842);
		generateTeleports(MINIGAME_NAMES, 53959);
		generateTeleports(BOSSING_NAMES, 54095);
		generateTeleports(DONATOR_NAMES, 19023);
		//54231 next set
	}

	private static int generateTeleports(String[] teleports, int index) {
		int totalPages = teleports.length / 8;

		List<Integer> pages = new ArrayList<>();

		for (int i = 0; i <= totalPages; i++) {
			RSInterface rsInterface = addInterface(index++, 512, 334);
			final int startingIndex = index;

			pages.add(index);

			int totalChildren = 0;
			for (int a = i * 8; a < (i * 8) + 8; a++) {

				if (a > teleports.length - 1) {
					break;
				}

				addHoverButton(index++, "teleport/button", 0, 136, 34, teleports[a].replace("\n", " ") + " teleport", 0, index, 1);
				addHoveredButton(index++, "teleport/button", 2, 136, 34, index++);

				totalChildren += 2;

				if (teleports[a].contains("\n")) {
					String[] split = teleports[a].split("\n");
					addText(index++, split[0], 2, 0xa0845e, true, true, 0);//Title
					addText(index++, split[1], 2, 0xa0845e, true, true, 0);//Title
					totalChildren += 2;
				} else {
					addText(index++, teleports[a], 2, 0xa0845e, true, true, 0);//Title
					totalChildren += 1;
				}
			}

			setChildren(totalChildren + (totalPages > 0 && teleports.length != 8 ? 1 : 0), rsInterface);
			int buttonIndex = 0;
			int child = 0;
			int childIndex = startingIndex;

			for (int a = i * 8; a < (i * 8) + 8; a++) {

				if (a > teleports.length - 1) {
					break;
				}

				rsInterface.child(child++, childIndex++, BUTTON_POSITIONS[buttonIndex][0], BUTTON_POSITIONS[buttonIndex][1]); // button
				rsInterface.child(child++, childIndex++, BUTTON_POSITIONS[buttonIndex][0], BUTTON_POSITIONS[buttonIndex][1]); // button hover
				childIndex++;

				if (teleports[a].contains("\n")) {
					rsInterface.child(child++, childIndex++, BUTTON_POSITIONS[buttonIndex][0] + 60, BUTTON_POSITIONS[buttonIndex][1] + 3); // text
					rsInterface.child(child++, childIndex++, BUTTON_POSITIONS[buttonIndex][0] + 60, BUTTON_POSITIONS[buttonIndex][1] + 18); // text
				} else {
					rsInterface.child(child++, childIndex++, BUTTON_POSITIONS[buttonIndex][0] + 60, BUTTON_POSITIONS[buttonIndex][1] + 10); // text
				}

				buttonIndex++;
				if (buttonIndex >= 8) {
					buttonIndex = 0;
				}
			}

			if (totalPages > 0 && teleports.length != 8) {
				if (i == (teleports.length / 8)) {
					addButton(index++, 0, "teleport/arrow", 61, 27, "Previous Page", 1);// previous

					teleportPages.put(index - 1, pages.get(i - 1) - 1);
					System.out.println("index: " + (index - 1));
				} else {
					addButton(index++, 1, "teleport/arrow", 61, 27, "Next Page", 1);// Next

					teleportPages.put(index - 1, index);
				}
				rsInterface.child(child++, childIndex++, 104, 240);
			}
		}

		return index;
	}

	private static void loyaltyInterface() {
		RSInterface rsInterface = addInterface(19882, 512, 334);

		// background
		addSprite(19883, 0, "loyalty/background");

		// close button
		addHoverButton(19884, "Interfaces/SlayerSprites/Close", 0, 16, 16, "Close window", 0, 19885, 3);
		addHoveredButton(19885, "Interfaces/SlayerSprites/Close", 1, 16, 16, 19886);

		// title
		addText(250, Constants.CLIENT_NAME + " Loyalty Titles", 2, 0xFF8000);

		// tab buttons
		addButton(19887, 2, "loyalty/tab", "Skilling");
		addButton(19888, 1, "loyalty/tab", "PK'in");
		addButton(19889, 1, "loyalty/tab", "Misc");

		addText(19890, "Skilling", 2, 0xff991f);
		interfaceCache[19890].textShadow = true;
		addText(19891, "PK'in", 2, 0x8c5000);
		interfaceCache[19891].textShadow = true;
		addText(19892, "Misc", 2, 0x8c5000);
		interfaceCache[19892].textShadow = true;

		setChildren(11, rsInterface);
		rsInterface.child(0, 19883, 5, 17);
		rsInterface.child(1, 19884, 481, 20);
		rsInterface.child(2, 19885, 481, 20);
		rsInterface.child(3, 250, 180, 20);
		rsInterface.child(4, 19887, 20, 47);
		rsInterface.child(5, 19888, 127, 47);
		rsInterface.child(6, 19889, 234, 47);
		rsInterface.child(7, 19890, 47, 52);
		rsInterface.child(8, 19891, 164, 52);
		rsInterface.child(9, 19892, 270, 52);
		rsInterface.child(10, 19893, 32, 80);

		skillingInterfaceId = 19893;
		pkInterfaceId = createLoyaltyTab(skillingInterfaceId, skilling);
		miscInterfaceId = createLoyaltyTab(pkInterfaceId, pkin);
		createLoyaltyTab(miscInterfaceId, misc);
	}

	private static int createLoyaltyTab(int startId, String[][] titles) {
		RSInterface rsInterface = addInterface(startId, 439, 210);
		rsInterface.parentID = 19882;
		rsInterface.scrollMax = (int) Math.ceil((titles.length / 3.0) * 63.0) + 25;
		rsInterface.newScroller = false;
		setChildren(titles.length * 4, rsInterface);

		startId++;

		int x = 0;
		int y = 2;
		int id = 0;
		int index = 0;

		for (int i = 0; i < titles.length; i++) {
			addButton(startId + id, 1, "loyalty/button", titles[i][0]);
			interfaceCache[startId + id].mOverInterToTrigger = (startId + (titles.length * 3) + i + (i * 1));

			addText(startId + id + 1, titles[i][0], 2, 0xFF8000);
			interfaceCache[startId + id + 1].centerText = true;

			addSprite(startId + id + 2, 1, "loyalty/tick");

			// used for generating server config
			// System.out.println("put(\"" + titles[i][0] + "\", " + (startId + id + 1) + ").");

			RSInterface tick = interfaceCache[startId + id + 2];
			tick.sprite2 = imageLoader(2, "loyalty/tick");
			tick.requiredValues = new int[1];
			tick.requiredValues[0] = startId + id + 2;
			tick.valueCompareType = new int[1];
			tick.valueCompareType[0] = 5;
			tick.valueIndexArray = new int[1][3];
			tick.valueIndexArray[0][0] = 5;
			tick.valueIndexArray[0][1] = 5001;
			tick.valueIndexArray[0][2] = 0;

			rsInterface.child(index++, (startId + id), x, y);
			rsInterface.child(index++, (startId + id + 1), x - 15, y + 15);
			rsInterface.child(index++, (startId + id + 2), x + 123, y + 31);

			x += 145;
			if (x >= 435) {
				y += 60;
				x = 0;
			}
			id += 3;
		}

		for (int i = 0; i < titles.length; i++) {
			addTooltip(startId + id, "Requirements:\n" + titles[i][1] + ".");
			rsInterface.child(index++, startId + id, 0, 0);
			id += 2;
		}

		return startId + id;
	}

	public static void BuyandSell() {
		RSInterface Interface = addTabInterface(24500);
		setChildren(51, Interface);

		addHoverButton(24541, "b", 3, 138, 108, "Abort offer", 0, 24542, 1);
		addHoverButton(24543, "b", 3, 138, 108, "View offer", 0, 24544, 1);
		addHoverButton(24545, "b", 3, 138, 108, "Abort offer", 0, 24546, 1);
		addHoverButton(24547, "b", 3, 138, 108, "View offer", 0, 24548, 1);
		addHoverButton(24549, "b", 3, 138, 108, "Abort offer", 0, 24550, 1);
		addHoverButton(24551, "b", 3, 138, 108, "View offer", 0, 24552, 1);
		addHoverButton(24553, "b", 3, 138, 108, "Abort offer", 0, 24554, 1);
		addHoverButton(24555, "b", 3, 138, 108, "View offer", 0, 24556, 1);
		addHoverButton(24557, "b", 3, 138, 108, "Abort offer", 0, 24558, 1);
		addHoverButton(24559, "b", 3, 138, 108, "View offer", 0, 24560, 1);
		addHoverButton(24561, "b", 3, 138, 108, "Abort offer", 0, 24562, 1);
		addHoverButton(24563, "b", 3, 138, 108, "View offer", 0, 24564, 1);

		addSprite(1, 24579, 1, "b", false);
		addSprite(1, 24580, 1, "b", false);
		addSprite(1, 24581, 1, "b", false);
		addSprite(1, 24582, 1, "b", false);
		addSprite(1, 24583, 1, "b", false);
		addSprite(1, 24584, 1, "b", false);

		addHDSprite(24501, "ge", 658, 658); // fixed

		addHoverButton(24502, "ge", CLOSE_BUTTON, 21, 21, "Close", 250, 24503, 3); // fixed
		addHoveredButton(24503, "ge", CLOSE_BUTTON_HOVER, 21, 21, 24504); // fixed

		addHoverButton(24505, "ge", 659, 50, 50, "Buy", 0, 24506, 1); // fixed
		addHoveredButton(24506, "ge", 661, 50, 50, 24507); // fixed

		addHoverButton(24508, "ge", 659, 50, 50, "Buy", 0, 24509, 1); // fixed
		addHoveredButton(24509, "ge", 661, 50, 50, 24510); // fixed


		addHoverButton(24514, "ge", 659, 50, 50, "Buy", 0, 24515, 1); // fixed
		addHoveredButton(24515, "ge", 661, 50, 50, 24516); // fixed
		addHoverButton(24517, "ge", 659, 50, 50, "Buy", 0, 24518, 1); // fixed
		addHoveredButton(24518, "ge", 661, 50, 50, 24519); // fixed
		addHoverButton(24520, "ge", 659, 50, 50, "Buy", 0, 24521, 1); // fixed
		addHoveredButton(24521, "ge", 661, 50, 50, 24522); // fixed
		addHoverButton(24523, "ge", 659, 50, 50, "Buy", 0, 24524, 1); // fixed
		addHoveredButton(24524, "ge", 661, 50, 50, 24525); // fixed
		addHoverButton(24511, "ge", 660, 50, 50, "Sell", 0, 24512, 1); // fixed
		addHoveredButton(24512, "ge", 662, 50, 50, 24513); // fixed
		addHoverButton(24526, "ge", 660, 50, 50, "Sell", 0, 24527, 1); // fixed
		addHoveredButton(24527, "ge", 662, 50, 50, 24528); // fixed
		addHoverButton(24529, "ge", 660, 50, 50, "Sell", 0, 24530, 1); // fixed
		addHoveredButton(24530, "ge", 662, 50, 50, 24531); // fixed
		addHoverButton(24532, "ge", 660, 50, 50, "Sell", 0, 24533, 1); // fixed
		addHoveredButton(24533, "ge", 662, 50, 50, 24534); // fixed
		addHoverButton(24535, "ge", 660, 50, 50, "Sell", 0, 24536, 1); // fixed
		addHoveredButton(24536, "ge", 662, 50, 50, 24537); // fixed
		addHoverButton(24538, "ge", 660, 50, 50, "Sell", 0, 24539, 1); // fixed
		addHoveredButton(24539, "ge", 662, 50, 50, 24540); // fixed

		RSInterface add = addInterface(24567, 512, 334);
		addToItemGroup(add, 1, 1, 24, 24, true, "[GE]", "[GE]", "[GE]");
		add = addInterface(24569, 512, 334);
		addToItemGroup(add, 1, 1, 24, 24, true, "[GE]", "[GE]", "[GE]");
		add = addInterface(24571, 512, 334);
		addToItemGroup(add, 1, 1, 24, 24, true, "[GE]", "[GE]", "[GE]");
		add = addInterface(24573, 512, 334);
		addToItemGroup(add, 1, 1, 24, 24, true, "[GE]", "[GE]", "[GE]");
		add = addInterface(24575, 512, 334);
		addToItemGroup(add, 1, 1, 24, 24, true, "[GE]", "[GE]", "[GE]");
		add = addInterface(24577, 512, 334);
		addToItemGroup(add, 1, 1, 24, 24, true, "[GE]", "[GE]", "[GE]");

		setBounds(24541, 30, 74, 0, Interface);
		setBounds(24543, 30, 74, 1, Interface);
		setBounds(24545, 186, 74, 2, Interface);
		setBounds(24547, 186, 74, 3, Interface);
		setBounds(24549, 342, 74, 4, Interface);
		setBounds(24551, 342, 74, 5, Interface);
		setBounds(24553, 30, 194, 6, Interface);
		setBounds(24555, 30, 194, 7, Interface);
		setBounds(24557, 186, 194, 8, Interface);
		setBounds(24559, 186, 194, 9, Interface);
		setBounds(24561, 339, 194, 10, Interface);
		setBounds(24563, 339, 194, 11, Interface);
		setBounds(24501, 4, 23, 12, Interface);
		setBounds(24579, 30 + 6, 74 + 30, 13, Interface);
		setBounds(24580, 186 + 6, 74 + 30, 14, Interface);
		setBounds(24581, 342 + 6, 74 + 30, 15, Interface);
		setBounds(24582, 30 + 6, 194 + 30, 16, Interface);
		setBounds(24583, 186 + 6, 194 + 30, 17, Interface);
		setBounds(24584, 342 + 6, 194 + 30, 18, Interface);
		setBounds(24502, 480, 32, 19, Interface);
		setBounds(24503, 480, 32, 20, Interface);
		setBounds(24505, 45, 115, 21, Interface);
		setBounds(24506, 45, 106, 22, Interface);
		setBounds(24508, 45, 234, 23, Interface);
		setBounds(24509, 45, 225, 24, Interface);
		setBounds(24511, 105, 115, 25, Interface);
		setBounds(24512, 105, 106, 26, Interface);
		setBounds(24514, 358, 115, 27, Interface);
		setBounds(24515, 358, 106, 28, Interface);
		setBounds(24517, 202, 234, 29, Interface);
		setBounds(24518, 202, 225, 30, Interface);
		setBounds(24520, 358, 234, 31, Interface);
		setBounds(24521, 358, 225, 32, Interface);
		setBounds(24523, 202, 115, 33, Interface);
		setBounds(24524, 202, 106, 34, Interface);
		setBounds(24526, 261, 115, 35, Interface);
		setBounds(24527, 261, 106, 36, Interface);
		setBounds(24529, 417, 115, 37, Interface);
		setBounds(24530, 417, 106, 38, Interface);
		setBounds(24532, 105, 234, 39, Interface);
		setBounds(24533, 105, 225, 40, Interface);
		setBounds(24535, 261, 234, 41, Interface);
		setBounds(24536, 261, 225, 42, Interface);
		setBounds(24538, 417, 234, 43, Interface);
		setBounds(24539, 417, 225, 44, Interface);

		setBounds(24567, 39, 106, 45, Interface);
		setBounds(24569, 46 + 156 - 7, 114 - 7, 46, Interface);
		setBounds(24571, 46 + 156 + 156 - 7, 114 - 7, 47, Interface);
		setBounds(24573, 39, 234 - 7, 48, Interface);
		setBounds(24575, 46 + 156 - 7, 234 - 7, 49, Interface);
		setBounds(24577, 46 + 156 + 156 - 7, 234 - 7, 50, Interface);

	}

	public static void collectSell() {
		RSInterface rsinterface = addTabInterface(54700);
		int x = 9;
		addHDSprite(54701, "ge", 639, 639);

		addHoverButton(54702, "ge", CLOSE_BUTTON, 16, 16, "Close", 0, 54703, 3);
		addHoveredButton(54703, "ge", CLOSE_BUTTON_HOVER, 16, 16, 54704);
		addHoverButton(54758, "ge", 607, 29, 23, "Back", 0, 54759, 1);
		addHoveredButton(54759, "ge", 608, 29, 23, 54760);

		addText(54769, "Choose an item to exchange", 0, 0x96731A, false, true, 0);
		addText(54770, "Select an item from your inventory to sell.", 0, 0x958E60, false, true, 0);

		addText(54771, "0", 0, 0xB58338, true, true, 0);
		addText(54772, "1 gp", 0, 0xB58338, true, true, 0);
		addText(54773, "0 gp", 0, 0xB58338, true, true, 0);

		addHoverButton(54793, "ge", 653, 40, 36, "[GE]", 0, 54794, 1);
		addHoveredButton(54794, "ge", 652, 40, 36, 54795);
		addHoverButton(54796, "ge", 653, 40, 36, "[GE]", 0, 54797, 1);
		addHoveredButton(54797, "ge", 652, 40, 36, 54798);

		addItemOnInterface(54780, 2903, new String[]{null});
		addItemOnInterface(54781, 2903, new String[]{"Collect"});
		addItemOnInterface(54782, 2904, new String[]{"Collect"});
		addText(54784, "", 0, 0xFFFF00, false, true, 0);
		addText(54785, "", 0, 0xFFFF00, false, true, 0);
		addText(54787, "N/A", 0, 0xB58338, false, true, 0);
		addText(54788, "", 0, 0xFFFF00, true, true, 0);
		addText(54789, "", 0, 0xFFFF00, true, true, 0);

		addHoverButton(54800, "ge", 640, 20, 20, "Abort offer", 0, 54801, 1);
		addHoveredButton(54801, "ge", 641, 20, 20, 54802);

		rsinterface.totalChildren(24);
		rsinterface.child(0, 54701, 4 + x, 23);// 385, 260
		rsinterface.child(1, 54702, 464 + x, 33);// 435, 260
		rsinterface.child(2, 54703, 464 + x, 33);
		rsinterface.child(3, 54758, 19 + x, 284);
		rsinterface.child(4, 54759, 19 + x, 284);
		rsinterface.child(5, 54769, 202 + x, 71);
		rsinterface.child(6, 54770, 202 + x, 98);
		rsinterface.child(7, 54771, 142 + x, 185);
		rsinterface.child(8, 54772, 354 + x, 185);
		rsinterface.child(9, 54773, 252 + x, 246);
		rsinterface.child(10, 54793, 386 + x, 256 + 23);
		rsinterface.child(11, 54794, 386 + x, 256 + 23);
		rsinterface.child(12, 54796, 435 + x, 256 + 23);
		rsinterface.child(13, 54797, 435 + x, 256 + 23);
		rsinterface.child(14, 54780, 97 + x, 97);
		rsinterface.child(15, 54781, 385 + 4 + x, 260 + 23);
		rsinterface.child(16, 54782, 435 + 4 + x, 260 + 23);
		rsinterface.child(17, 54784, 385 + 4 + x, 260 + 23);
		rsinterface.child(18, 54785, 435 + 4 + x, 260 + 23);
		rsinterface.child(19, 54787, 108, 136);
		rsinterface.child(20, 54788, 214 + x, 249 + 23);
		rsinterface.child(21, 54789, 214 + x, 263 + 23);
		rsinterface.child(22, 54800, 345 + x, 250 + 23);
		rsinterface.child(23, 54801, 345 + x, 250 + 23);
	}

	public static void collectBuy() {
		RSInterface rsinterface = addTabInterface(53700);
		int x = 9;

		addHDSprite(53701, "ge", 642, 642);
		addHoverButton(53702, "ge", CLOSE_BUTTON, 16, 16, "Close", 0, 53703, 3);
		addHoveredButton(53703, "ge", CLOSE_BUTTON_HOVER, 16, 16, 53704);

		addHoverButton(53758, "ge", 607, 29, 23, "Back", 0, 53759, 1);
		addHoveredButton(53759, "ge", 608, 29, 23, 53760);

		addText(53769, "Choose an item to exchange", 0, 0x96731A, false, true, 0);
		addText(53770, "Select an item from your inventory to sell.", 0, 0x958E60, false, true, 0);
		addText(53771, "0", 0, 0xB58338, true, true, 0);
		addText(53772, "1 gp", 0, 0xB58338, true, true, 0);
		addText(53773, "0 gp", 0, 0xB58338, true, true, 0);

		addHoverButton(53793, "ge", 653, 40, 36, "[GE]", 0, 53794, 1);
		addHoveredButton(53794, "ge", 652, 40, 36, 53795);
		addHoverButton(53796, "ge", 653, 40, 36, "[GE]", 0, 53797, 1);
		addHoveredButton(53797, "ge", 652, 40, 36, 53798);

		addItemOnInterface(53780, 2901, new String[]{null});
		addItemOnInterface(53781, 2901, new String[]{"Collect"});
		addItemOnInterface(53782, 2902, new String[]{"Collect"});

		addText(53784, "", 0, 0xFFFF00, false, true, 0);
		addText(53785, "", 0, 0xFFFF00, false, true, 0);
		addText(53787, "N/A", 0, 0xB58338, false, true, 0);
		addText(53788, "", 0, 0xFFFF00, true, true, 0);
		addText(53789, "", 0, 0xFFFF00, true, true, 0);

		addHoverButton(53800, "ge", 640, 20, 20, "Abort offer", 0, 53801, 1);
		addHoveredButton(53801, "ge", 641, 20, 20, 53802);

		rsinterface.totalChildren(24);
		rsinterface.child(0, 53701, 4 + x, 23);// 385, 260
		rsinterface.child(1, 53702, 464 + x, 33);// 435, 260
		rsinterface.child(2, 53703, 464 + x, 33);
		rsinterface.child(3, 53758, 19 + x, 284);
		rsinterface.child(4, 53759, 19 + x, 284);
		rsinterface.child(5, 53769, 202 + x, 71);
		rsinterface.child(6, 53770, 202 + x, 98);
		rsinterface.child(7, 53771, 142 + x, 185);
		rsinterface.child(8, 53772, 354 + x, 185);
		rsinterface.child(9, 53773, 252 + x, 246);
		rsinterface.child(10, 53793, 386 + x, 256 + 23);
		rsinterface.child(11, 53794, 386 + x, 256 + 23);
		rsinterface.child(12, 53796, 435 + x, 256 + 23);
		rsinterface.child(13, 53797, 435 + x, 256 + 23);
		rsinterface.child(14, 53780, 97 + x, 97);
		rsinterface.child(15, 53781, 385 + 4 + x, 260 + 23);
		rsinterface.child(16, 53782, 435 + 4 + x, 260 + 23);
		rsinterface.child(17, 53784, 385 + 4 + x, 260 + 23);
		rsinterface.child(18, 53785, 435 + 4 + x, 260 + 23);
		rsinterface.child(19, 53787, 108, 136);
		rsinterface.child(20, 53788, 214 + x, 249 + 23);
		rsinterface.child(21, 53789, 214 + x, 263 + 23);
		rsinterface.child(22, 53800, 345 + x, 250 + 23);
		rsinterface.child(23, 53801, 345 + x, 250 + 23);
	}

	public static void Buy() {
		RSInterface rsinterface = addTabInterface(24600);
		int x = 9;
		addHDSprite(24601, "ge", 609, 609);
		addHoverButton(24602, "ge", CLOSE_BUTTON, 16, 16, "Close", 0, 24603, 3);
		addHoveredButton(24603, "ge", CLOSE_BUTTON_HOVER, 16, 16, 24604);
		addHoverButton(24606, "ge", 610, 13, 13, "Decrease Quantity", 0, 24607, 1);
		addHoveredButton(24607, "ge", 611, 13, 13, 24608);
		addHoverButton(24610, "ge", 612, 13, 13, "Increase Quantity", 0, 24611, 1);
		addHoveredButton(24611, "ge", 613, 13, 13, 24612);
		addHoverButton(24614, "ge", 614, 35, 25, "Add 1", 0, 24615, 1);
		addHoveredButton(24615, "ge", 615, 35, 25, 24616);
		addHoverButton(24618, "ge", 616, 35, 25, "Add 10", 0, 24619, 1);
		addHoveredButton(24619, "ge", 617, 35, 25, 24620);
		addHoverButton(24622, "ge", 618, 35, 25, "Add 100", 0, 24623, 1);
		addHoveredButton(24623, "ge", 619, 35, 25, 24624);
		addHoverButton(24626, "ge", 622, 35, 25, "Add 1000", 0, 24627, 1);
		addHoveredButton(24627, "ge", 623, 35, 25, 24628);
		addHoverButton(24630, "ge", 624, 35, 25, "Edit Quantity", -1, 24631, 1);
		addHoveredButton(24631, "ge", 625, 35, 25, 24632);
		addHoverButton(24634, "ge", 626, 35, 25, "Decrease Price", 0, 24635, 1);
		addHoveredButton(24635, "ge", 627, 35, 25, 24636);
		addHoverButton(24638, "ge", 628, 35, 25, "Offer Guild Price", 0, 24639, 1);
		addHoveredButton(24639, "ge", 629, 35, 25, 24640);
		addHoverButton(24642, "ge", 624, 35, 25, "Edit Price", -1, 24643, 1);
		addHoveredButton(24643, "ge", 625, 35, 25, 24644);
		addHoverButton(24646, "ge", 630, 35, 25, "Increase Price", 0, 24647, 1);
		addHoveredButton(24647, "ge", 631, 35, 25, 24648);
		addHoverButton(24650, "ge", 632, 120, 43, "Confirm Offer", 0, 24651, 1);
		addHoveredButton(24651, "ge", 633, 120, 43, 24652);
		addHoverButton(24654, "ge", 634, 40, 36, "Search", 0, 24655, 1);
		addHoveredButton(24655, "ge", 635, 40, 36, 24656);
		addHoverButton(24658, "ge", 607, 29, 23, "Back", 0, 24659, 1);
		addHoveredButton(24659, "ge", 608, 29, 23, 24660);
		addHoverButton(24662, "ge", 610, 13, 13, "Decrease Price", 0, 24663, 1);
		addHoveredButton(24663, "ge", 611, 13, 13, 24664);
		addHoverButton(24665, "ge", 612, 13, 13, "Increase Price", 0, 24666, 1);
		addHoveredButton(24666, "ge", 613, 13, 13, 24667);

		addText(24669, "Choose an item to exchange", 0, 0x96731A, false, true, 0);
		addText(24670, "Click the icon to the left to search for items.", 0, 0x958E60, false, true, 0);
		addText(24671, "0", 0, 0xB58338, true, true, 0);
		addText(24672, "1 gp", 0, 0xB58338, true, true, 0);
		addText(24673, "0 gp", 0, 0xB58338, true, true, 0);
		//RSInterface add = addInterface(24680);

		addItemOnInterface(24680, 3323, new String[]{null});
		//addToItemGroup(add, 1, 1, 24, 24, true, "[GE]", "[GE]", "[GE]");

		addText(24682, "N/A", 0, 0xB58338, false, true, 0);
		rsinterface.totalChildren(42);
		rsinterface.child(0, 24601, 4 + x, 23);
		rsinterface.child(1, 24602, 464 + x, 33);
		rsinterface.child(2, 24603, 464 + x, 33);
		rsinterface.child(3, 24606, 46 + x, 184);
		rsinterface.child(4, 24607, 46 + x, 184);
		rsinterface.child(5, 24610, 226 + x, 184);
		rsinterface.child(6, 24611, 226 + x, 184);
		rsinterface.child(7, 24614, 43 + x, 208);
		rsinterface.child(8, 24615, 43 + x, 208);
		rsinterface.child(9, 24618, 84 + x, 208);
		rsinterface.child(10, 24619, 84 + x, 208);
		rsinterface.child(11, 24622, 125 + x, 208);
		rsinterface.child(12, 24623, 125 + x, 208);
		rsinterface.child(13, 24626, 166 + x, 208);
		rsinterface.child(14, 24627, 166 + x, 208);
		rsinterface.child(15, 24630, 207 + x, 208);
		rsinterface.child(16, 24631, 207 + x, 208);
		rsinterface.child(17, 24634, 260 + x, 208);
		rsinterface.child(18, 24635, 260 + x, 208);
		rsinterface.child(19, 24638, 316 + x, 208);
		rsinterface.child(20, 24639, 316 + x, 208);
		rsinterface.child(21, 24642, 357 + x, 208);
		rsinterface.child(22, 24643, 357 + x, 208);
		rsinterface.child(23, 24646, 413 + x, 208);
		rsinterface.child(24, 24647, 413 + x, 208);
		rsinterface.child(25, 24650, 191 + x, 273);
		rsinterface.child(26, 24651, 191 + x, 273);
		rsinterface.child(27, 24654, 93 + x, 95);
		rsinterface.child(28, 24655, 93 + x, 95);
		rsinterface.child(29, 24658, 19 + x, 284);
		rsinterface.child(30, 24659, 19 + x, 284);
		rsinterface.child(31, 24662, 260 + x, 184);
		rsinterface.child(32, 24663, 260 + x, 184);
		rsinterface.child(33, 24665, 435 + x, 184);
		rsinterface.child(34, 24666, 435 + x, 184);
		rsinterface.child(35, 24669, 202 + x, 71);
		rsinterface.child(36, 24670, 202 + x, 98);
		rsinterface.child(37, 24671, 142 + x, 185);
		rsinterface.child(38, 24672, 354 + x, 185);
		rsinterface.child(39, 24673, 252 + x, 246);
		rsinterface.child(40, 24680, 97 + x, 97);
		rsinterface.child(41, 24682, 121, 136);
	}

	public static void Sell() {
		RSInterface rsinterface = addTabInterface(24700);
		int x = 9;
		addHDSprite(24701, "ge", 636, 636);
		addHoverButton(24702, "ge", CLOSE_BUTTON, 16, 16, "Close", 0, 24703, 3);
		addHoveredButton(24703, "ge", CLOSE_BUTTON_HOVER, 16, 16, 24704);
		addHoverButton(24706, "ge", 610, 13, 13, "Decrease Quantity", 0, 24707, 1);
		addHoveredButton(24707, "ge", 611, 13, 13, 24708);
		addHoverButton(24710, "ge", 612, 13, 13, "Increase Quantity", 0, 24711, 1);
		addHoveredButton(24711, "ge", 613, 13, 13, 24712);
		addHoverButton(24714, "ge", 614, 35, 25, "Sell 1", 0, 24715, 1);
		addHoveredButton(24715, "ge", 615, 35, 25, 24716);
		addHoverButton(24718, "ge", 616, 35, 25, "Sell 10", 0, 24719, 1);
		addHoveredButton(24719, "ge", 617, 35, 25, 24720);
		addHoverButton(24722, "ge", 618, 35, 25, "Sell 100", 0, 24723, 1);
		addHoveredButton(24723, "ge", 619, 35, 25, 24724);
		addHoverButton(24726, "ge", 620, 35, 25, "Sell All", 0, 24727, 1);
		addHoveredButton(24727, "ge", 621, 35, 25, 24728);
		addHoverButton(24730, "ge", 624, 35, 25, "Edit Quantity", -1, 24731, 1);
		addHoveredButton(24731, "ge", 625, 35, 25, 24732);
		addHoverButton(24734, "ge", 626, 35, 25, "Decrease Price", 0, 24735, 1);
		addHoveredButton(24735, "ge", 627, 35, 25, 24736);
		addHoverButton(24738, "ge", 628, 35, 25, "Offer Guild Price", 0, 24739, 1);
		addHoveredButton(24739, "ge", 629, 35, 25, 24740);
		addHoverButton(24742, "ge", 624, 35, 25, "Edit Price", -1, 24743, 1);
		addHoveredButton(24743, "ge", 625, 35, 25, 24744);
		addHoverButton(24746, "ge", 630, 35, 25, "Increase Price", 0, 24747, 1);
		addHoveredButton(24747, "ge", 631, 35, 25, 24748);
		addHoverButton(24750, "ge", 632, 120, 43, "Confirm Offer", 0, 24751, 1);
		addHoveredButton(24751, "ge", 633, 120, 43, 24752);
		addHoverButton(24758, "ge", 607, 29, 23, "Back", 0, 24759, 1);
		addHoveredButton(24759, "ge", 608, 29, 23, 24760);
		addHoverButton(24762, "ge", 610, 13, 13, "Decrease Price", 0, 24763, 1);
		addHoveredButton(24763, "ge", 611, 13, 13, 24764);
		addHoverButton(24765, "ge", 612, 13, 13, "Increase Price", 0, 24766, 1);
		addHoveredButton(24766, "ge", 613, 13, 13, 24767);

		addText(24769, "Choose an item to exchange", 0, 0x96731A, false, true, 0);
		addText(24770, "Select an item from your inventory to sell.", 0, 0x958E60, false, true, 0);
		addText(24771, "0", 0, 0xB58338, true, true, 0);
		addText(24772, "1 gp", 0, 0xB58338, true, true, 0);
		addText(24773, "0 gp", 0, 0xB58338, true, true, 0);
		addItemOnInterface(24780, 3323, new String[]{null});
		addText(24782, "N/A", 0, 0xB58338, false, true, 0);

		rsinterface.totalChildren(40);
		rsinterface.child(0, 24701, 4 + x, 23);
		rsinterface.child(1, 24702, 464 + x, 33);
		rsinterface.child(2, 24703, 464 + x, 33);
		rsinterface.child(3, 24706, 46 + x, 184);
		rsinterface.child(4, 24707, 46 + x, 184);
		rsinterface.child(5, 24710, 226 + x, 184);
		rsinterface.child(6, 24711, 226 + x, 184);
		rsinterface.child(7, 24714, 43 + x, 208);
		rsinterface.child(8, 24715, 43 + x, 208);
		rsinterface.child(9, 24718, 84 + x, 208);
		rsinterface.child(10, 24719, 84 + x, 208);
		rsinterface.child(11, 24722, 125 + x, 208);
		rsinterface.child(12, 24723, 125 + x, 208);
		rsinterface.child(13, 24726, 166 + x, 208);
		rsinterface.child(14, 24727, 166 + x, 208);
		rsinterface.child(15, 24730, 207 + x, 208);
		rsinterface.child(16, 24731, 207 + x, 208);
		rsinterface.child(17, 24734, 260 + x, 208);
		rsinterface.child(18, 24735, 260 + x, 208);
		rsinterface.child(19, 24738, 316 + x, 208);
		rsinterface.child(20, 24739, 316 + x, 208);
		rsinterface.child(21, 24742, 357 + x, 208);
		rsinterface.child(22, 24743, 357 + x, 208);
		rsinterface.child(23, 24746, 413 + x, 208);
		rsinterface.child(24, 24747, 413 + x, 208);
		rsinterface.child(25, 24750, 191 + x, 273);
		rsinterface.child(26, 24751, 191 + x, 273);
		rsinterface.child(27, 24758, 19 + x, 284);
		rsinterface.child(28, 24759, 19 + x, 284);
		rsinterface.child(29, 24762, 260 + x, 184);
		rsinterface.child(30, 24763, 260 + x, 184);
		rsinterface.child(31, 24765, 435 + x, 184);
		rsinterface.child(32, 24766, 435 + x, 184);
		rsinterface.child(33, 24769, 202 + x, 71);
		rsinterface.child(34, 24770, 202 + x, 98);
		rsinterface.child(35, 24771, 142 + x, 185);
		rsinterface.child(36, 24772, 354 + x, 185);
		rsinterface.child(37, 24773, 252 + x, 246);
		rsinterface.child(38, 24780, 97 + x, 97);
		rsinterface.child(39, 24782, 121, 136);
	}

	public static void addToItemGroup(RSInterface rsi, int w, int h, int x, int y, boolean actions, String action1, String action2, String action3) {
		rsi.width = w;
		rsi.height = h;
		rsi.inv = new int[w * h];
		rsi.invStackSizes = new int[w * h];
		rsi.usableItemInterface = false;
		rsi.isInventoryInterface = false;
		rsi.invSpritePadX = x;
		rsi.invSpritePadY = y;
		rsi.actions = new String[5];
		if (actions) {
			rsi.actions[0] = action1;
			rsi.actions[1] = action2;
			rsi.actions[2] = action3;
		}
		rsi.type = 2;
	}

	public static void addItemOnInterface(int childId, int interfaceId, String[] options) {
		RSInterface rsi = interfaceCache[childId] = new RSInterface();
		rsi.actions = new String[10];
		rsi.inv = new int[30];
		rsi.invStackSizes = new int[30];
		rsi.children = new int[0];
		rsi.childX = new int[0];
		rsi.childY = new int[0];
		for (int i = 0; i < rsi.actions.length; i++) {
			if (i < options.length) {
				if (options[i] != null) {
					rsi.actions[i] = options[i];
				}
			}
		}
		rsi.centerText = true;
		rsi.filled = false;
		//  rsi.dragDeletes = false;
		rsi.usableItemInterface = false;
		rsi.isInventoryInterface = false;
		// rsi.deleteOnDrag2 = true;
		rsi.invSpritePadX = 23;
		rsi.invSpritePadY = 22;
		rsi.height = 5;
		rsi.width = 6;
		rsi.parentID = interfaceId;
		rsi.id = childId;
		rsi.type = 2;
	}

	public static void addSprite(int a, int id, int spriteId, String spriteName, boolean l) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.opacity = (byte) 0;
		//  tab.disabledSprite = imageLoader(spriteId, spriteName);
		//  tab.enabledSprite = imageLoader(spriteId, spriteName);
		tab.width = 512;
		tab.height = 334;
	}

	public static void addHDSprite(int id, String spriteName, int spriteId, int sprite2) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		//  tab.advancedSprite = true;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.opacity = (byte) 0;
		tab.sprite1 = imageLoader(spriteId, spriteName);

		tab.width = 512;
		tab.height = 1024;
	}

	public void swapInventoryItems(int i, int j) {
		int k = inv[i];
		inv[i] = inv[j];
		inv[j] = k;
		k = invStackSizes[i];
		invStackSizes[i] = invStackSizes[j];
		invStackSizes[j] = k;
	}

	/**
	 * Append a child into specific index (between for example)
	 *
	 * @param frame   the index where you want place to
	 * @param childId child id
	 * @param childX  child x
	 * @param childY  child y
	 */
	public void appendChild(int frame, int childId, int childX, int childY) {

		int[] newChildren = new int[this.children.length + 1], newXs = new int[this.childX.length + 1], newYs = new int[this.childY.length + 1];

		for (int k = 0; k < frame; k++) {
			newChildren[k] = this.children[k];
			newXs[k] = this.childX[k];
			newYs[k] = this.childY[k];
		}

		newChildren[frame] = childId;
		newXs[frame] = childX;
		newYs[frame] = childY;

		for (int k = frame + 1; k < this.children.length + 1; k++) {
			newChildren[k] = (this.children[k - 1]);
			newXs[k] = (this.childX[k - 1]);
			newYs[k] = (this.childY[k - 1]);
		}

		this.children = newChildren;
		this.childX = newXs;
		this.childY = newYs;
	}

	public void removeChild(int frame) {
		int[] newChildren = new int[this.children.length - 1], newXs = new int[this.childX.length - 1], newYs = new int[this.childY.length - 1];

		if (frame == 0) {

			for (int k = 0; k < this.children.length - 1; k++) {
				newChildren[k] = this.children[k + 1];
				newXs[k] = this.childX[k + 1];
				newYs[k] = this.childY[k + 1];
			}

		} else {

			for (int k = 0; k < frame; k++) {
				newChildren[k] = this.children[k];
				newXs[k] = this.childX[k];
				newYs[k] = this.childY[k];
			}

			for (int k = frame; k < this.children.length - 1; k++) {
				newChildren[k] = this.children[k + 1];
				newXs[k] = this.childX[k + 1];
				newYs[k] = this.childY[k + 1];
			}

		}

		this.children = newChildren;
		this.childX = newXs;
		this.childY = newYs;
	}

	private void appendChildTop(int frame, int childId, int childX, int childY,
								RSInterface rsi) {
		int[] newChildren = new int[rsi.children.length + 1], newXs = new int[rsi.childX.length + 1], newYs = new int[rsi.childY.length + 1];
		int[] newChild = {childId}, newX = {childX}, newY = {childY};
		System.arraycopy(newChild, 0, newChildren, frame, newChild.length);
		System.arraycopy(rsi.children, 0, newChildren, 1, rsi.children.length);
		System.arraycopy(newX, 0, newXs, frame, newX.length);
		System.arraycopy(rsi.childX, 0, newXs, 1, rsi.childX.length);
		System.arraycopy(newY, 0, newYs, frame, newY.length);
		System.arraycopy(rsi.childY, 0, newYs, 1, rsi.childY.length);
		rsi.children = newChildren;
		rsi.childX = newXs;
		rsi.childY = newYs;
	}

	public void setSprite(Sprite sprite) {
		sprite1 = sprite;
	}

	public void specialBar(int id) // 7599
	{
		addActionButton(id - 12, 7587, -1, 150, 26, "Use <col=00ff00>Special Attack");
		for (int i = id - 11; i < id; i++)
			removeSomething(i);

		RSInterface rsi = interfaceCache[id - 12];
		rsi.width = 150;
		rsi.height = 26;
		rsi.mOverInterToTrigger = 40005;

		rsi = interfaceCache[id];
		rsi.width = 150;
		rsi.height = 26;

		rsi.child(0, id - 12, 0, 0);

		rsi.child(12, id + 1, 3, 7);

		rsi.child(23, id + 12, 16, 8);

		for (int i = 13; i < 23; i++) {
			rsi.childY[i] -= 1;
		}

		rsi = interfaceCache[id + 1];
		rsi.type = 5;
		rsi.sprite1 = CustomSpriteLoader(7600, "");

		for (int i = id + 2; i < id + 12; i++) {
			rsi = interfaceCache[i];
			rsi.type = 5;
		}

		sprite1(id + 2, 7601);
		sprite1(id + 3, 7602);
		sprite1(id + 4, 7603);
		sprite1(id + 5, 7604);
		sprite1(id + 6, 7605);
		sprite1(id + 7, 7606);
		sprite1(id + 8, 7607);
		sprite1(id + 9, 7608);
		sprite1(id + 10, 7609);
		sprite1(id + 11, 7610);

		rsi = addInterface(40005, 512, 334);
		rsi.isMouseoverTriggered = true;
		rsi.type = 0;
		rsi.atActionType = 0;
		rsi.mOverInterToTrigger = -1;
		rsi.parentID = 40005;
		rsi.id = 40005;
		addBox(40006, 0, false, 0x000000,
				"Select to perform a special\nattack.");
		setChildren(1, rsi);
		setBounds(40006, 0, 0, 0, rsi);
	}

	public void children(int total) {
		children = new int[total];
		childX = new int[total];
		childY = new int[total];
	}

	public void child(int id, int interID, int x, int y) {
		children[id] = interID;
		childX[id] = x;
		childY[id] = y;
	}

	public void totalChildren(int t) {
		children = new int[t];
		childX = new int[t];
		childY = new int[t];
	}

	public Model method209(int j, int k, boolean flag) {
		int type;
		int id;
		if (flag) {
			type = enabledModelType;
			id = enabledModelId;
		} else {
			type = mediaType;
			id = mediaID;
		}
		if (type == 0) {
			return null;
		}
		Model model = (Model) aMRUNodes_264.get((long) (type << 16) + id);
		if (model == null) {
			if (type == 1) {
				model = Model.getModel(id);
				if (model == null) {
					return null;
				}
				model.createBones();
				model.setLighting(64, 850, -30, -50, -30, true, false, true);
			}
			if (type == 2) {
				model = NpcDefinition.forID(id).getHeadModel();
				if (model == null) {
					return null;
				}
				model.createBones();
				model.setLighting(64, 850, -30, -50, -30, true, false, true);
			}
			if (type == 3) {
				model = Client.myPlayer.getHeadModel();
				if (model == null) {
					return null;
				}
				model.createBones();
				model.setLighting(64, 850, -30, -50, -30, true, false, true);
			}
			if (type == 4) {
				ItemDefinition itemDef = ItemDefinition.forID(id);
				model = itemDef.getUnshadedModel(50);
				if (model == null) {
					return null;
				}
				model.createBones();
				model.setLighting(64 + itemDef.lightIntensity, 850 + itemDef.lightMagnitude, -30, -50, -30, true, false, true);
			}
			if (type == 6) {
				ItemDefinition itemDef = ItemDefinition.forID(id);
				model = itemDef.getEquippedModel(Client.myPlayer.gender, Client.customizedItem);
				if (model == null) {
					return null;
				}
				model.createBones();
				model.setLighting(64 + itemDef.lightIntensity, 850 + itemDef.lightMagnitude, -30, -50, -30, true, false, true);
			}
			aMRUNodes_264.put(model, (long) (type << 16) + id);
		}
		if (k == -1 && j == -1)
			return model;
		Model model_1;
		if (j == -1) {
			model_1 = model.copy(Model.interfaceModel, !FrameReader.hasAlpha(k));
			model_1.applyTransform(k);
			return model_1;
		}
		model_1 = model.copy(Model.interfaceModel, !FrameReader.hasAlpha(k) & !FrameReader.hasAlpha(j));
		model_1.applyTransform(k);
		model_1.applyTransform(j);
		return model_1;
	}

}
