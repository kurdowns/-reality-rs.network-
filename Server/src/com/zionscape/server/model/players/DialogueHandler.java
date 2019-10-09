package com.zionscape.server.model.players;

import com.zionscape.server.model.content.minigames.christmas.ChristmasEvent;

public class DialogueHandler {

	private Player c;

	public DialogueHandler(Player client) {
		this.c = client;
	}

	@SuppressWarnings("unused")
	public static void sendDialogues(Player c, int dialogue, int npcId) {
		c.talkingNpc = npcId;
		int next = c.nextChat;
		switch (dialogue) {
		/*
		 * case 30 : sendOption(c, "Invite", "Kick", "Promote/Demote", "Ban/Unban"); c.dialogueAction = 30; break; case
		 * 31 : sendOption(c, "Invite", "Kick", "Ban/Unban"); c.dialogueAction = 31; break; case 32 : sendOption(c,
		 * "Change Name", (Server.clanChat.clans[c.clanId].chat ? "Lock" : "Unlock") + " Chat",
		 * (Server.clanChat.clans[c.clanId].locked ? "Unlock" : "Lock") + " Clan", "Leave Clan"); c.dialogueAction = 32;
		 * break;
		 *
		 * case 301 : sendOption(c, "Invite", "Kick", "Promote/Demote", "Ban/Unban"); c.dialogueAction = 301; break;
		 * case 302 : sendOption(c, "Invite", "Kick", "Ban/Unban"); c.dialogueAction = 302; break; case 303:
		 * sendOption(c, "Change Name", (Server.clanChat.clans[c.clanId].chat ? "Lock" : "Unlock") + " Chat",
		 * (Server.clanChat.clans[c.clanId].locked ? "Unlock" : "Lock") + " Clan", "Leave Clan"); c.dialogueAction =
		 * 303; break; case 304: sendOption(c, "Accept", "Decline"); c.dialogueAction = 304; break;
		 */
		}
	}

	public static void sendOption(Player c, String s, String s1) {
		c.getPA().sendFrame126("Select an Option", 2460);
		c.getPA().sendFrame126(s, 2461);
		c.getPA().sendFrame126(s1, 2462);
		c.getPA().sendFrame164(2459);
	}

	/*
	 * Options
	 */
	public static void sendOption(Player c, String s, String s1, String s2) {
		c.getPA().sendFrame126("Select an Option", 2470);
		c.getPA().sendFrame126(s, 2471);
		c.getPA().sendFrame126(s1, 2472);
		c.getPA().sendFrame126(s2, 2473);
		c.getPA().sendFrame164(2469);
	}

	public static void sendOption(Player c, String s, String s1, String s2, String s3) {
		c.getPA().sendFrame126("Select an Option", 2481);
		c.getPA().sendFrame126(s, 2482);
		c.getPA().sendFrame126(s1, 2483);
		c.getPA().sendFrame126(s2, 2484);
		c.getPA().sendFrame126(s3, 2485);
		c.getPA().sendFrame164(2480);
	}

	public static void sendStatement(Player c, String s) {
		c.getPA().sendFrame126(s, 357);
		c.getPA().sendFrame126("Click here to continue", 358);
		c.getPA().sendFrame164(356);
	}

	/*
	 * Information Box
	 */

	/**
	 * Handles all talking
	 *
	 * @param dialogue The dialogues you want to use
	 * @param npcId    The npc id that the chat will focus on during the chat
	 */
	@SuppressWarnings("unused")
	public void sendDialogues(int dialogue, int npcId) {
		c.talkingNpc = npcId;
		int next = c.nextChat;
		switch (dialogue) {
			/**
			 * end tutorial section
			 */
			case 5002:
				if (PvPArtifacts.hasArtifact(c)) {
					this.sendNpcChat3("Oh glorious warrior!", "I see you have found some ancient artifacts!",
							"Are you willing to sell them to me?", 8725, "Mandrith");
					c.nextChat = 5003;
				} else {
					this.sendNpcChat3("Hello, my name is Mandrith, I'm a collector.", "If you find any ancient artifacts,",
							"I am willing to buy them off you for a fine price!", c.talkingNpc, "Mandrith");
					c.nextChat = 0;
				}
				break;
			case 5003:
				this.sendOption2("Yes.", "No, thanks.");
				c.dialogueAction = 5003;
				break;
			case 5020:
				this.sendOption2("Pk Point Shop #1.", "Pk Point Shop #2.");
				c.dialogueAction = 5020;
				break;
			case 3627:
				this.sendOption2("Yes", "No");
				c.nextChat = 0;
				break;
			case 0:
				c.talkingNpc = -1;
				c.getPA().removeAllWindows();
				c.nextChat = 0;
				break;
			case 1:
				this.sendStatement("You found a hidden tunnel! Do you want to enter it?");
				c.dialogueAction = 1;
				c.nextChat = 2;
				break;
			case 2:
				this.sendOption2("Yea! I'm fearless!", "No way! That looks scary!");
				c.dialogueAction = 1;
				c.nextChat = 0;
				break;
			case 3:
				// sendNpcChat4("Hello!", "My name is Duradel and I am a master of the slayer skill.",
				// "I can assign you a slayer task suitable to your combat level.",
				this.sendNpcChat4("Hello, adventurer", "I am the daughter of Duradel",
						"I can assign you slayer tasks if you'd like", "Would you like a slayer task?", c.talkingNpc,
						"Kuradel");
				c.nextChat = 4;
				break;
			case 5:
				this.sendNpcChat4("Hello adventurer...", "My name is Kolodion, the master of this mage bank.",
						"Would you like to play a minigame in order ",
						"to earn points towards recieving magic related prizes?", c.talkingNpc, "Kolodion");
				c.nextChat = 6;
				break;
			case 6:
				this.sendNpcChat4("The way the game works is as follows...", "You will be teleported to the wilderness,",
						"You must kill mages to recieve points,", "redeem points with the chamber guardian.", c.talkingNpc,
						"Kolodion");
				c.nextChat = 15;
				break;
			case 11:
				this.sendNpcChat4("Hello, adventurer", "I am the daughter of Duradel",
						"I can assign you slayer tasks if you'd like", "Would you like a slayer task?", c.talkingNpc,
						"Kuradel");
				c.nextChat = 12;
				break;
			case 12:
				this.sendOption2("Yes I would like a slayer task.", "No I would not like a slayer task.");
				c.dialogueAction = 5;
				break;
			case 13:
				this.sendNpcChat4("Hello, adventurer", "I am the daughter of Duradel",
						"I can assign you slayer tasks if you'd like", "Would you like a slayer task?", c.talkingNpc,
						"Kuradel");
				c.nextChat = 14;
				break;
			case 14:
				this.sendOption2("Yes I would like an easier task.", "No I would like to keep my task.");
				c.dialogueAction = 6;
				break;
			case 15:
				this.sendOption2("Yes I would like to play", "No, sounds too dangerous for me.");
				c.dialogueAction = 7;
				break;
			case 16:
				this.sendOption2("I would like to reset my barrows brothers.", "I would like to fix all my barrows");
				c.dialogueAction = 8;
				break;
			case 20:
				this.sendNpcChat4("Hello " + c.username + ", I am the donation manager",
						"Thank you for donating to support us!", "Would you like to buy yell points, or open the shop?",
						"It is 1m per 25 yell points", c.talkingNpc, "Donation Manager");
				c.nextChat = 21;
				break;
			case 21:
				this.sendOption2("Buy yell points (1m for 25 points)", "Open the shop");
				c.dialogueAction = 21;
				break;
			case 22:
				this.sendNpcChat4("Hello, " + c.username, "I can provide you with colored whips",
						"Yellow, green, blue, or white?", "You need 10m and a regular whip", c.talkingNpc, "Barbarian");
				c.nextChat = 23;
				break;
			case 23:
				this.sendOption5("Blue", "White", "Green", "Yellow", "Nevermind");
				c.dialogueAction = 50;
				c.teleAction = -1;
				break;
			/**
			 *
			 * Christmas 2010
			 *
			 **/
			// BEFORE EVENT
			case 24:
				this.sendNpcChat4("Ughhhh....", "Umph.....", "Ohhhhhhh....", "Ahhhhhh....", c.talkingNpc, "Drunk Santa");
				c.nextChat = 100;
				break;
			case 100:
				this.sendStatement("Santa seems drunk...Let's come back later");
				c.nextChat = -1;
				break;
			// begginning
        /*
         * case 24: sendStatement("........."); c.nextChat = 25; break;
		 */
			case 25:
				this.sendStatement(".................");
				c.nextChat = 26;
				break;
			case 26:
				this.sendStatement("...........................");
				c.nextChat = 27;
				break;
			case 27:
				this.sendPlayerChat1("WHAT?");
				c.nextChat = 28;
				break;
			case 28:
				this.sendNpcChat2("Wait, what...?", "Oh ish you...", c.talkingNpc, "Drunk Santa");
				c.nextChat = 29;
				break;
			case 29:
				this.sendPlayerChat1("What's wrong with your voice?");
				c.nextChat = 30;
				break;
			case 30:
				this.sendNpcChat2("What...? Meh?", "Der is noshing wrong wit it!", c.talkingNpc, "Drunk Santa");
				c.nextChat = 31;
				break;
			case 31:
				this.sendPlayerChat1("Yes it is. Have you been drinking?");
				c.nextChat = 32;
				break;
			case 32:
				this.sendNpcChat2("Whhaattt?", "Of coursh not!", c.talkingNpc, "Drunk Santa");
				c.nextChat = 33;
				break;
			case 33:
				this.sendPlayerChat2("Okay then....", "Well, do you have any quests for me?");
				c.nextChat = 34;
				break;
			case 34:
				this.sendNpcChat2("Ash a matter of fact, yesh i do", "I need you to buy me *hic* more...*snore*",
						c.talkingNpc, "Drunk Santa");
				c.nextChat = 35;
				break;
			case 35:
				this.sendPlayerChat2("I should find some beer!", "That'll loosen his tongue");
				c.nextChat = 36;
				break;
			case 36:
				this.sendStatement("Go find some beer! Maybe the Falador pub?");
				c.nextChat = -1;
				break;
			case 38:
				this.sendStatement("Congratulations, you open the chest and got a reward!");
				c.nextChat = 0;
				break;
			case 37:
				this.sendStatement("You need atleast 3 free inventory spaces! and a crystal key!");
				c.nextChat = 0;
				break;
			case 39:
				this.sendNpcChat4("Hi there!", "Aether has asked me to help him with a favour",
						"He needs you to vote on a subject", "Please vote honestly, yes or no", c.talkingNpc, "Larxus");
				c.nextChat = 40;
				break;
			case 40:
				this.sendStatement("Should PvP Armour and weapons degrade? (Vesta statius etc");
				c.nextChat = 41;
				break;
			case 41:
				this.sendOption2("Yes, they should degrade", "No, they shouldn't degrade");
				c.dialogueAction = 41;
				c.nextChat = -1;
				break;
			case 42:
				this.sendNpcChat4("I can combine all 5 extreme potions", "into an Overload Potion!",
						"They have to be in their (4) form", "You need 96 Herblore!", c.talkingNpc, "Herblore Manager");
				c.nextChat = 43;
				break;
			case 43:
				this.sendOption2("Yes please!", "No thanks");
				c.dialogueAction = 1000;
				c.nextChat = -1;
				break;
			case 200:
				this.sendStatement("Would you like to fight the Sea Troll Queen?");
				c.nextChat = 201;
				break;
			case 201:
				this.sendOption2("Yes - I'm Braze", "No - I'm  a coward");
				c.dialogueAction = 201;
				c.nextChat = -1;
				break;
			case 71:
				this.sendNpcChat3("I am impressed, JalYt " + c.username + ".",
						"Unfortunately, I can only give you this many Tokkul",
						"You will receive more if you complete more waves.", 2617, "Tzhaar-Met-Jal");
				c.nextChat = 0;
				break;
			case 72:
				this.sendNpcChat2("You even defeated TzTok-Jad, I am most impressed!",
						"Please accept this gift as a reward", 2617, "Tzhaar-Mej-Jal");
				c.nextChat = 0;
				break;
			case 73:
				this.sendNpcChat2("Your on your own, JalYt " + c.username + ",", "prepare to fight for your life!", 2617,
						"Tzhaar-Met-Jal");
				c.nextChat = 0;
				break;
			case 131:
				this.sendOption4("Player Killing", "Monster Killing", "Skilling", "Minigames");
				c.dialogueAction = 131;
				break;
			case 132:// Player Killing
				this.sendOption5("Edgeville", "Revnants @red@(51 Wild)", "Green Drags @red@(21 Wild)", "SUGGEST", "SUGGEST");
				c.dialogueAction = 132;
				break;
			case 94:// Monster Killing
				this.sendOption5("Bork", "Jungle Demon", "Kalphite Queen", "Daggonth Kings", "KBD");
				c.dialogueAction = 94;
				break;
			case 95:// Skilling
				this.sendOption5("Lumbridge", "Varrock", "Camelot", "Falador", "Canifis");
				c.dialogueAction = 95;
				break;
			case 96:// Minigames
				this.sendOption5("Zombies Minigame", "Fight Pits", "Clan wars?", "Speed slayer?", "Weapons mini?");
				c.dialogueAction = 96;
				break;
			case 97:
				this.sendNpcChat4("Hello " + c.username + ", I'm in charge of Proelium-Pk's Lottery.",
						"Entering costs 5m million gp but you could win up to",
						"250 million gp! you can enter up to 5 times per draw.", "Would you like to enter the lottery?",
						c.talkingNpc, "Lottie");
				c.nextChat = 98;
				break;
			case 98:
				this.sendOption2("Yes i would like to enter!", "No, Id rather not.");
				c.dialogueAction = 200;
				break;
			case 99:
				this.sendPlayerChat2("Please enter the clan's owner name that you wish to war.", "");
				c.nextChat = 101;
				break;
			case 102:
				this.sendNpcChat2("Please help me sir!", "My town has been destroyed!", c.talkingNpc, "Villager");
				c.nextChat = 103;
				break;
			case 103:
				this.sendPlayerChat1("What or who has destroyed your town?");
				c.nextChat = 104;
				break;
			case 104:
				this.sendNpcChat2("This terrible Avatar of destruction", "He is residing in a cave near here",
						c.talkingNpc, "Villager");
				c.nextChat = 105;
				break;
			case 105:
				this.sendPlayerChat2("I was actually on my way to slay this hideous beast",
						"Where might I find this creature?");
				c.nextChat = 106;
				break;
			case 106:
				this.sendNpcChat2("Run south until you start seeing the cave turn into ice",
						"Follow that ice path until you can go no further", c.talkingNpc, "Villager");
				c.nextChat = 107;
				break;
			case 107:
				this.sendNpcChat2("Then open the chest at the end, and he will summon you",
						"Be careful sir, he has the power to drain all prayer!", c.talkingNpc, "Villager");
				c.nextChat = 108;
				break;
			case 108:
				this.sendStatement("I guess I should listen to the scared man's directions");
				c.nextChat = 0;
				break;
			case 5021:
				this.sendNpcChat2("Hello " + c.username + ", I have the ability to lock/unlock your EXP.",
						"What would you like to do?", c.talkingNpc, "Town Crier");
				c.nextChat = 5022;
				break;
			case 5022:
				this.sendOption2("Lock EXP", "Unlock EXP");
				c.dialogueAction = 5022;
				break;
			case 5023:
				if (c.lockedEXP == 0) {
					this.sendStatement("Your EXP has been locked!");
					c.lockedEXP = 1;
				} else {
					this.sendStatement("Your EXP is already locked!");
				}
				c.nextChat = 0;
				break;
			case 5024:
				if (c.lockedEXP == 1) {
					this.sendStatement("Your EXP has been unlocked!");
					c.lockedEXP = 0;
				} else {
					this.sendStatement("Your EXP is already unlocked!");
				}
				c.nextChat = 0;
				break;
		/*
		 * case 301 : sendOption(c, "Invite", "Kick", "Promote/Demote", "Ban/Unban"); c.dialogueAction = 301; break;
		 * case 302 : sendOption(c, "Invite", "Kick", "Ban/Unban"); c.dialogueAction = 302; break; case 303:
		 * sendOption(c, "Change Name", (Server.clanChat.clans[c.clanId].chat ? "Lock" : "Unlock") + " Chat",
		 * (Server.clanChat.clans[c.clanId].locked ? "Unlock" : "Lock") + " Clan", "Leave Clan"); c.dialogueAction =
		 * 303; break; case 304: sendOption(c, "Accept", "Decline"); c.dialogueAction = 304; break;
		 */
			/** Christmas Event */
			case 1550:
				sendNpcChat1("Oh dear what am I going to do? Christmas is ruined!", c.talkingNpc, "Diango");
				c.nextChat = 1551;
				break;
			case 1551:
				sendPlayerChat1("What do you mean Christmas is ruined?");
				c.nextChat = 1552;
				break;
			case 1552:
				sendNpcChat3("The pixies aren't working as fast as they should be.",
						"We're currently way behind schedule and I don't know",
						"how we're ever going to manage to get caught up!", c.talkingNpc, "Diango");
				c.nextChat = 1553;
				break;
			case 1553:
				sendPlayerChat2("Oh my! Since it's Christmas I'm feeling rather helpful.",
						"Is there anything I can do to help speed things up?");
				c.christmasEvent = 1;
				c.nextChat = 1554;
				break;
			case 1554:
				sendNpcChat1("Really? Thank you! Of course you can help.", c.talkingNpc, "Diango");
				c.nextChat = 1555;
				break;
			case 1555:
				sendNpcChat4("I will need you to head to the workshop which is in",
						"a house north of here, and go down the trapdoor.",
						"You can talk to the head pixie Rosie who will", "tell you what you can do to help.", c.talkingNpc,
						"Diango");
				c.nextChat = 1556;
				break;
			case 1556:
				sendPlayerChat1("Yeah sure! I'll go speak with her right away.");
				c.nextChat = 0;
				break;
			case 1557:
				sendNpcChat1("Hello, I'm Rosie. I'm the head pixie around here.", c.talkingNpc, "Rosie");
				c.nextChat = 1558;
				break;
			case 1558:
				sendPlayerChat1("Hi, I'm " + c.username + ". Diango sent me here.");
				c.christmasEvent = 2;
				c.nextChat = 1559;
				break;
			case 1559:
				sendNpcChat2("Oh goodie! We need all the help we can get around here.",
						"Otherwise we may not have enough toys for Christmas!", c.talkingNpc, "Rosie");
				c.nextChat = 1560;
				break;
			case 1560:
				sendPlayerChat1("So how can I help?");
				c.nextChat = 1561;
				break;
			case 1561:
				sendNpcChat3("Good to see some excitement! I need you to do a",
						"few things around here. The first thing is I need", "5 boxes full of completed marionettes.",
						c.talkingNpc, "Rosie");
				c.nextChat = 1562;
				break;
			case 1562:
				sendNpcChat4("After that I'll need you to do some painting on",
						"those tables on the west side. You'll need to collect",
						"baubles from the decoration boxes and paint them. There",
						"is 5 types of baubles and 5 colors. I need each box", c.talkingNpc, "Rosie");
				c.nextChat = 1563;
				break;
			case 1563:
				sendNpcChat3("filled each with a single color of every bauble. If you have",
						"any questions or if you need anything, you may", "speak to me again whenever you wish.",
						c.talkingNpc, "Rosie");
				c.nextChat = 1564;
				break;
			case 1564:
				sendPlayerChat1("Okay, thanks. I'll get on that!");
				c.nextChat = 0;
				break;
			case 1565:
				sendOption5("Tell me about toy making.", "Tell me about the pixies.", "Can I have a set of strings?",
						"I need a box for my completed marionettes.", "I need a box to put my painted baubles into.");
				c.dialogueAction = 1565;
				c.nextChat = 0;
				break;
			case 1566:
				sendNpcChat4("The pixies are the some of the hardest working I've",
						"ever known. They help keep this place in order and ensure",
						"everything is going as scheduled. Except this year we are",
						"running a far bit later then expected.", c.talkingNpc, "Rosie");
				c.nextChat = 1567;
				break;
			case 1567:
				sendNpcChat1("Do you have any other questions?", c.talkingNpc, "Rosie");
				c.nextChat = 1565;
				break;
			case 1568:
				sendNpcChat4("Toy-making is the most important part of all Christmas!",
						"To build marionettes go downstairs and collect different",
						"parts. Once you have all the parts of the same color, you",
						"can build them together by using any part on the", c.talkingNpc, "Rosie");
				c.nextChat = 1571;
				break;
			case 1571:
				sendNpcChat1("workbench. Be sure to use the right color of workbench!", c.talkingNpc, "Rosie");
				c.nextChat = 1567;
				break;
			case 1569:
				sendPlayerChat1("Tell me about toy making.");
				c.nextChat = 1568;
				break;
			case 1570:
				sendPlayerChat1("Tell me about the pixies.");
				c.nextChat = 1566;
				break;
			case 1572:
				sendNpcChat1("Why hello there, " + c.username + ". Merry Christmas!", c.talkingNpc, "Santa");
				c.nextChat = 1573;
				break;
			case 1573:
				sendPlayerChat1("How.. how do you know my..");
				c.nextChat = 1574;
				break;
			case 1574:
				sendNpcChat2("Not to worry. I think it's best you speak with Diango",
						"who can be found in Draynor village.", c.talkingNpc, "Santa");
				c.nextChat = 1575;
				break;
			case 1575:
				sendPlayerChat1("Okay, thanks!");
				c.nextChat = 0;
				break;
			case 1576:
				sendNpcChat1("Workity, workity, work, Hrmph!", c.talkingNpc, "Sorcha");
				c.nextChat = 0;
				break;
			case 1577:
				sendNpcChat1("Workity, workity, work, Hrmph!", c.talkingNpc, "Cait");
				c.nextChat = 0;
				break;
			case 1578:
				sendNpcChat1("Workity, workity, work, Hrmph!", c.talkingNpc, "Cormac");
				c.nextChat = 0;
				break;
			case 1579:
				sendNpcChat1("Workity, workity, work, Hrmph!", c.talkingNpc, "Fionn");
				c.nextChat = 0;
				break;
			case 1580:
				sendNpcChat1("Workity, workity, work, Hrmph!", c.talkingNpc, "Donnacha");
				c.nextChat = 0;
				break;
			case 1581:
				sendNpcChat1("Workity, workity, work, Hrmph!", c.talkingNpc, "Ronan");
				c.nextChat = 0;
				break;
			case 1582:
				sendNpcChat1("Ho, ho, ho! You helped my workshop finish on time!", c.talkingNpc, "Santa");
				c.nextChat = 1583;
				break;
			case 1583:
				sendPlayerChat1("Anything I can do to help out, Santa!");
				c.nextChat = 1584;
				break;
			case 1584:
				sendNpcChat3("Well I'm sure glad to hear that! As a way of showing",
						"my gratitude, please take this santa suit.",
						"I'm sure Diango will have a nice gift for you aswell!", c.talkingNpc, "Santa");
				c.nextChat = 1585;
				break;
			case 1585:
				if (c.getItems().freeInventorySlots() > 3) {
					sendStatement("Santa hands you one of his santa costumes.");
					ChristmasEvent.completeEvent(c);
					c.nextChat = 1586;
				} else {
					sendStatement("You need more inventory space in order to do this!");
					c.nextChat = 0;
				}
				break;
			case 1586:
				sendPlayerChat1("Wow thanks a lot Santa!");
				c.nextChat = 0;
				break;
			case 1587:
				sendNpcChat1("Ho, ho, ho!", c.talkingNpc, "Santa");
				c.nextChat = 0;
				break;
			case 1588:
				sendNpcChat4("I've just received word that Christmas is saved! I cannot",
						"thank you enough for this. If you go back over",
						"by the trapdoor, you'll see a box with marionettes inside.",
						"Feel free to grab them, as a token for helping me.", c.talkingNpc, "Diango");
				c.getPA().customObject(10672, 3090, 3276, 0, 1, 10);
				c.christmasEvent = 4;
				c.nextChat = 1589;
				break;
			case 1589:
				sendPlayerChat2("I'm glad I was able to help! I'll go grab those",
						"marionettes right away! Thanks again, Diango!");
				c.nextChat = 0;
				break;
			case 1590:
				sendNpcChat1("Thanks again for your help, " + c.username + ".", c.talkingNpc, "Diango");
				c.nextChat = 0;
				break;
			case 1591:
				sendNpcChat1("I appreciate all the help you did for us. Thanks!", c.talkingNpc, "Rosie");
				c.nextChat = 0;
				break;
			case 1592:

				c.getPA().sendNpcChat(9085, "Hello there, I am Kuradal - the master of Slayer. If you like, I can assign you a slayer task, or would you like to talk about something else instead?");

				//sendNpcChat3("Hello there, I am Kuradal - the master of Slayer.", "If you like, I can assign you a slayer task, or would you", "like to talk about something else instead?", c.talkingNpc, "Kuradal");
				c.nextChat = 1594;
				break;
			case 1594:
				sendOption4("Who are you?", "I would like a Slayer task.", "What's in your shop?", "Nothing, thanks.");
				c.dialogueAction = 1594;
				c.nextChat = 0;
				break;
			/**Who are you*/
			case 1595:
				c.getPA().sendNpcChat(9085, "Who am I? I'm only the finest Slayer master that has ever lived!");
				c.nextChat = 1594;
				break;
			/**Already has a slayer task*/
			case 1596:
				c.getPA().sendNpcChat(9085, "It appears you already have a Slayer task. If you want you're able to cancel your task with points in my shop.");
				c.nextChat = 1594;
				break;
			/**Mazchna*/
			case 1597:
				sendNpcChat3("Hello there, I am Sumona, a Slayer master.", "If you like, I can assign you a slayer task, or would", "you like to talk about something else instead?", c.talkingNpc, "Sumona");
				c.nextChat = 1598;
				break;
			case 1598:
				sendOption4("Who are you?", "I would like a Slayer task.", "What's in your shop?", "Nothing, thanks.");
				c.dialogueAction = 1595;
				c.nextChat = 0;
				break;
			/**Who are you*/
			case 1599:
				sendNpcChat2("I'm Sumona, and I've been long training in the art of", "Slaying. I'll be able to help you get started.", c.talkingNpc, "Sumona");
				c.nextChat = 1598;
				break;
			/**Already has a slayer task*/
			case 1600:
				sendNpcChat2("It appears you already have a Slayer task. If you want", "you're able to cancel your task with points in my shop.", c.talkingNpc, "Sumona");
				c.nextChat = 1598;
				break;
			//Effigy Chat...
			case 3250:
				break;
			case 3500:
				sendNpcChat2("If you wish to access this minigame, you must defeat me.", "Do you wish to fight me?", c.talkingNpc, "Kolodion");
				c.nextChat = 3501;
				break;
			case 3501:
				sendOption2("I'm ready!", "Not right now.");
				c.dialogueAction = 3501;
				c.nextChat = 0;
				break;
		}
	}

	/*
	 * private void sendNpcChat1(String s) { }
	 */
	private void sendNpcChat1(String s, int ChatNpc, String name) {
		c.getPA().sendFrame200(4883, 591);
		c.getPA().sendFrame126(name, 4884);
		c.getPA().sendFrame126(s, 4885);
		c.getPA().sendFrame75(ChatNpc, 4883);
		c.getPA().sendFrame164(4882);
	}

	/**
	 * public static void sendOption(Player c, String s, String s1, String s2) {
	 * c.getPA().sendFrame126("Select an Option", 2470); c.getPA().sendFrame126(s, 2471); c.getPA().sendFrame126(s1,
	 * 2472); c.getPA().sendFrame126(s2, 2473); c.getPA().sendFrame164(2469); }
	 * <p>
	 * public static void sendOption(Player c, String s, String s1, String s2, String s3) {
	 * c.getPA().sendFrame126("Select an Option", 2481); c.getPA().sendFrame126(s, 2482); c.getPA().sendFrame126(s1,
	 * 2483); c.getPA().sendFrame126(s2, 2484); c.getPA().sendFrame126(s3, 2485); c.getPA().sendFrame164(2480); }
	 */
	public void sendNpcChat2(String s, String s1, int ChatNpc, String name) {
		c.getPA().sendFrame200(4888, 591);
		c.getPA().sendFrame126(name, 4889);
		c.getPA().sendFrame126(s, 4890);
		c.getPA().sendFrame126(s1, 4891);
		c.getPA().sendFrame75(ChatNpc, 4888);
		c.getPA().sendFrame164(4887);
	}

	public void sendNpcChat3(String s, String s1, String s2, int ChatNpc, String name) {
		String s3 = "";
		c.getPA().sendFrame200(4901, 591);
		c.getPA().sendFrame126(name, 4902);
		c.getPA().sendFrame126(s, 4903);
		c.getPA().sendFrame126(s1, 4904);
		c.getPA().sendFrame126(s2, 4905);
		c.getPA().sendFrame126(s3, 4906);
		c.getPA().sendFrame75(ChatNpc, 4901);
		c.getPA().sendFrame164(4900);
	}

	public void sendNpcChat4(String s, String s1, String s2, String s3, int ChatNpc, String name) {
		c.getPA().sendFrame200(4901, 591);
		c.getPA().sendFrame126(name, 4902);
		c.getPA().sendFrame126(s, 4903);
		c.getPA().sendFrame126(s1, 4904);
		c.getPA().sendFrame126(s2, 4905);
		c.getPA().sendFrame126(s3, 4906);
		c.getPA().sendFrame75(ChatNpc, 4901);
		c.getPA().sendFrame164(4900);
	}

	@SuppressWarnings("unused")
	private void sendOption(String s, String s1) {
		c.getPA().sendFrame126("Select an Option", 2470);
		c.getPA().sendFrame126(s, 2471);
		c.getPA().sendFrame126(s1, 2472);
		c.getPA().sendFrame126("Click here to continue", 2473);
		c.getPA().sendFrame164(13758);
	}

	public void sendOption2(String s, String s1) {
		c.getPA().sendFrame126("Select an Option", 2460);
		c.getPA().sendFrame126(s, 2461);
		c.getPA().sendFrame126(s1, 2462);
		c.getPA().sendFrame164(2459);
	}

	public void sendOption3(String s, String s1, String s2) {
		c.getPA().sendFrame126("Select an Option", 2470);
		c.getPA().sendFrame126(s, 2471);
		c.getPA().sendFrame126(s1, 2472);
		c.getPA().sendFrame126(s2, 2473);
		c.getPA().sendFrame164(2469);
	}

	/*
	 * Statements
	 */

	public void sendOption4(String s, String s1, String s2, String s3) {
		c.getPA().sendFrame126("Select an Option", 2481);
		c.getPA().sendFrame126(s, 2482);
		c.getPA().sendFrame126(s1, 2483);
		c.getPA().sendFrame126(s2, 2484);
		c.getPA().sendFrame126(s3, 2485);
		c.getPA().sendFrame164(2480);
	}

	/*
	 * Npc Chatting
	 */

	public void sendOption5(String s, String s1, String s2, String s3, String s4) {
		c.getPA().sendFrame126("Select an Option", 2493);
		c.getPA().sendFrame126(s, 2494);
		c.getPA().sendFrame126(s1, 2495);
		c.getPA().sendFrame126(s2, 2496);
		c.getPA().sendFrame126(s3, 2497);
		c.getPA().sendFrame126(s4, 2498);
		c.getPA().sendFrame164(2492);
	}

	private void sendPlayerChat1(String s) {
		c.getPA().sendFrame200(969, 591);
		c.getPA().sendFrame126(c.username, 970);
		c.getPA().sendFrame126(s, 971);
		c.getPA().sendFrame185(969);
		c.getPA().sendFrame164(968);
	}

	private void sendPlayerChat2(String s, String s1) {
		c.getPA().sendFrame200(974, 591);
		c.getPA().sendFrame126(c.username, 975);
		c.getPA().sendFrame126(s, 976);
		c.getPA().sendFrame126(s1, 977);
		c.getPA().sendFrame185(974);
		c.getPA().sendFrame164(973);
	}

	/*
	 * Player Chating Back
	 */

	@SuppressWarnings("unused")
	private void sendPlayerChat3(String s, String s1, String s2) {
		c.getPA().sendFrame200(980, 591);
		c.getPA().sendFrame126(c.username, 981);
		c.getPA().sendFrame126(s, 982);
		c.getPA().sendFrame126(s1, 983);
		c.getPA().sendFrame126(s2, 984);
		c.getPA().sendFrame185(980);
		c.getPA().sendFrame164(979);
	}

	@SuppressWarnings("unused")
	private void sendPlayerChat4(String s, String s1, String s2, String s3) {
		c.getPA().sendFrame200(987, 591);
		c.getPA().sendFrame126(c.username, 988);
		c.getPA().sendFrame126(s, 989);
		c.getPA().sendFrame126(s1, 990);
		c.getPA().sendFrame126(s2, 991);
		c.getPA().sendFrame126(s3, 992);
		c.getPA().sendFrame185(987);
		c.getPA().sendFrame164(986);
	}

	public void sendStartInfo(String text, String text1, String text2, String text3, String title) {
		c.getPA().sendFrame126(title, 6180);
		c.getPA().sendFrame126(text, 6181);
		c.getPA().sendFrame126(text1, 6182);
		c.getPA().sendFrame126(text2, 6183);
		c.getPA().sendFrame126(text3, 6184);
		c.getPA().sendFrame164(6179);
	}

	public void sendStatement(String s) { // 1 line click here to continue chat box interface
		c.getPA().sendFrame126(s, 357);
		c.getPA().sendFrame126("Click here to continue", 358);
		c.getPA().sendFrame164(356);
	}
}
