package com.zionscape.server.plugin.impl.quests;

import com.zionscape.server.events.Listener;
import com.zionscape.server.events.impl.*;
import com.zionscape.server.model.content.minigames.quests.QuestHandler;
import com.zionscape.server.model.content.minigames.quests.QuestInterfaceGenerator;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.dialogue.CloseDialogue;
import com.zionscape.server.plugin.Plugin;
import com.zionscape.server.world.shops.Shops;

public class GnomeGliderDown implements Listener, Plugin {

    private static final String QUEST_NAME = "Gnome Glider Down";
    private static final int GREEBO = 3815;
    private static final int BORIC = 350;
    private static final int KING_SHAREEN =  670;
    private static final int GNOME = 6611;
    private static final int LARRY = 13790;

    private static final int SIDEBAR_INTERFACE_ID = 19163;

    @Override
    public void onNpcClicked(NpcClickedEvent event) {
        Player player = event.getPlayer();
        if(event.getNpc().getType() == BORIC) {
            event.setHandled(true);

            if(player.getData().gnomeGliderDownStatus == 2) {
                player.getPA().sendNpcChat(BORIC, "How is the quest going?");
                player.setDialogueOwner(GnomeGliderDown.class);
                player.setCurrentDialogueId(18);
            } else if(player.getData().gnomeGliderDownStatus == 1) {
                player.getPA().sendNpcChat(BORIC, "Would you like to start the quest now?");
                player.setDialogueOwner(GnomeGliderDown.class);
                player.setCurrentDialogueId(14);
            } else if(player.getData().gnomeGliderDownStatus == 0) {
                if(!player.getData().getQuestProgress().containsKey("beginners-adventure") || player.getData().getQuestProgress().get("beginners-adventure") != 4) {
                    player.getPA().sendNpcChat(BORIC, "You must complete A Beginners Adventure before starting this quest");
                    player.setDialogueOwner(CloseDialogue.class);
                } else {
                    player.getPA().sendPlayerChat("Hello Boric, my name is " + player.username + ".");
                    player.setDialogueOwner(GnomeGliderDown.class);
                    player.setCurrentDialogueId(1);
                }
            } else {
                player.getPA().sendNpcChat(BORIC, "Hello");
                player.setDialogueOwner(CloseDialogue.class);
            }
        }

        if(event.getNpc().getType() == KING_SHAREEN) {
            event.setHandled(true);
            if(player.getData().gnomeGliderDownStatus == 12) {
                player.getPA().sendNpcChat(KING_SHAREEN, "You did it! He's back home safe and sound!");
                player.setDialogueOwner(GnomeGliderDown.class);
                player.setCurrentDialogueId(146);
            } else if(player.getData().gnomeGliderDownStatus == 3) {
                player.getPA().sendPlayerChat("Hope you can find him!");
                player.setDialogueOwner(CloseDialogue.class);
            } else if(player.getData().gnomeGliderDownStatus == 2) {
                player.getPA().sendPlayerChat("Hello King Narnode Shareen.");
                player.setDialogueOwner(GnomeGliderDown.class);
                player.setCurrentDialogueId(19);
            } else {
                player.getPA().sendPlayerChat("Hello");
                player.setDialogueOwner(CloseDialogue.class);
            }
        }

        if(event.getNpc().getType() == GREEBO) {
            if(player.getData().gnomeGliderDownStatus == 13) {
                player.getPA().sendPlayerChat("Hello");
                player.setDialogueOwner(GnomeGliderDown.class);
                player.setCurrentDialogueId(152);
            } else if(player.getData().gnomeGliderDownStatus == 11) {
                player.getPA().sendNpcChat(GREEBO, "Get that supplies to the pilot");
                player.setDialogueOwner(CloseDialogue.class);
            } else if(player.getData().gnomeGliderDownStatus == 10) {
                player.getPA().sendNpcChat(GREEBO, "I have got the supplies you need!");
                player.setDialogueOwner(GnomeGliderDown.class);
                player.setCurrentDialogueId(138);
            } else if(player.getData().gnomeGliderDownStatus == 8) {
                player.getPA().sendNpcChat(GREEBO, "Find larry in the lumberyard!");
                player.setDialogueOwner(CloseDialogue.class);
            } else if(player.getData().gnomeGliderDownStatus == 7) {
                player.getPA().sendNpcChat(GREEBO, "Have you found anything?");
                player.setDialogueOwner(GnomeGliderDown.class);
                player.setCurrentDialogueId(88);
            } else if(player.getData().gnomeGliderDownStatus == 5) {
                player.getPA().sendNpcChat(GREEBO, "Did you find him?");
                player.setDialogueOwner(GnomeGliderDown.class);
                player.setCurrentDialogueId(55);
            } else if(player.getData().gnomeGliderDownStatus == 4) {
                player.getPA().sendNpcChat(GREEBO, "Have you collected the data pages?");
                player.setDialogueOwner(GnomeGliderDown.class);
                player.setCurrentDialogueId(47);
            } else if(player.getData().gnomeGliderDownStatus == 3) {
                player.getPA().sendNpcChat(GREEBO, "Hello, and who might you be?");
                player.setDialogueOwner(GnomeGliderDown.class);
                player.setCurrentDialogueId(29);
            } else {
                player.getPA().sendPlayerChat("Hello");
                player.setDialogueOwner(CloseDialogue.class);
            }
        }

        if(event.getNpc().getType() == GNOME) {

            if(event.getOption() == 2) {
                if(player.getData().gnomeGliderDownStatus != 13) {
                    player.getPA().sendNpcChat(BORIC, "You must complete " + QUEST_NAME + " to use this shop.");
                    player.setDialogueOwner(CloseDialogue.class);
                    return;
                }
                Shops.open(player, 27);
                return;
            }

            if(player.getData().gnomeGliderDownStatus == 11) {
                player.getPA().sendPlayerChat("I have got the supplies you need!");
                player.setDialogueOwner(GnomeGliderDown.class);
                player.setCurrentDialogueId(141);
            } else if(player.getData().gnomeGliderDownStatus == 7) {
                player.getPA().sendNpcChat(GNOME, "Hurry and tell Greebo!");
                player.setDialogueOwner(CloseDialogue.class);
            } else if(player.getData().gnomeGliderDownStatus == 6) {
                if(player.getItems().playerHasItem(1929)) {
                    player.getPA().sendPlayerChat("I have got some water!");
                    player.setDialogueOwner(GnomeGliderDown.class);
                    player.setCurrentDialogueId(63);
                } else {
                    player.getPA().sendNpcChat(GNOME, "WATER!");
                    player.setDialogueOwner(CloseDialogue.class);
                }
            } else if(player.getData().gnomeGliderDownStatus == 5) {
                player.getPA().sendNpcChat(GNOME, "Water... need water.");
                player.setDialogueOwner(GnomeGliderDown.class);
                player.setCurrentDialogueId(56);
            } else {
                player.getPA().sendNpcChat(GNOME, "Hello");
                player.setDialogueOwner(CloseDialogue.class);
            }
        }

        if(event.getNpc().type == LARRY) {
            if(player.getData().gnomeGliderDownStatus == 10) {
                player.getPA().sendOptions("Hey Larry", "I have lost the crate");
                player.setDialogueOwner(GnomeGliderDown.class);
                player.setCurrentDialogueId(134);
            } else if(player.getData().gnomeGliderDownStatus == 9) {
                player.getPA().sendNpcChat(LARRY, "Do you have the tools?");
                player.setDialogueOwner(GnomeGliderDown.class);
                player.setCurrentDialogueId(125);
            } else if(player.getData().gnomeGliderDownStatus == 8) {
                player.getPA().sendPlayerChat("Hello, Larry is it?");
                player.setCurrentDialogueId(100);
                player.setDialogueOwner(GnomeGliderDown.class);
            } else {
                player.getPA().sendNpcChat(LARRY, "Hello");
                player.setDialogueOwner(CloseDialogue.class);
            }
        }


    }

    @Override
    public void onDialogueContinue(DialogueContinueEvent event) {
        Player player = event.getPlayer();
        if (player.getDialogueOwner() == null || !player.getDialogueOwner().equals(GnomeGliderDown.class)) {
            return;
        }

        event.setHandled(true);

        switch (player.getCurrentDialogueId()) {
            case 1:
                player.getPA().sendNpcChat(BORIC, "Nice to meet you, " + player.username + ", what  can I do for you?");
                player.setCurrentDialogueId(2);
                break;
            case 2:
                player.getPA().sendPlayerChat("I've heard you're the guy to talk to for an adventure? They say you know of it all!");
                player.setCurrentDialogueId(3);
                break;
            case 3:
                player.getPA().sendNpcChat(BORIC, "That would be correct, I travel far and wide, from men to gnomes to wizards, gathering all the news and information I can.");
                player.setCurrentDialogueId(4);
                break;
            case 4:
                player.getPA().sendPlayerChat("What's in it for you?");
                player.setCurrentDialogueId(5);
                break;
            case 5:
                player.getPA().sendNpcChat(BORIC, "I love to get out of the norm, find myself walking through an elegant forest, or down the slopes of a mountain, meeting different type of people and hearing their story. Its so... satisfying!");
                player.setCurrentDialogueId(6);
                break;
            case 6:
                player.getPA().sendPlayerChat("Sounds marvelous! That’s actually why I'm here, I'm looking for an adventure!");
                player.setCurrentDialogueId(7);
                break;
            case 7:
                player.getPA().sendNpcChat(BORIC, "An adventure you say? Hmmmm....");
                player.setCurrentDialogueId(8);
                break;
            case 8:
                player.getPA().sendPlayerChat("Yes, I want to feel the wind against my face, the sound of wolves at night, the taste of rain falling on my face, i want to LIVE!");
                player.setCurrentDialogueId(9);
                break;
            case 9:
                player.getPA().sendNpcChat(BORIC, "Woah woah settle down there youngster!");
                player.setCurrentDialogueId(10);
                break;
            case 10:
                player.getPA().sendNpcChat(BORIC, "I think I've got something for you!");
                player.setCurrentDialogueId(11);
                break;
            case 11:
                player.getPA().sendPlayerChat("What is it?");
                player.setCurrentDialogueId(12);
                break;
            case 12:
                player.getPA().sendNpcChat(BORIC, "A gnome was doing a fly over mission in a glider the other week collecting desert readings of the dust storms that have been occurring. Something went wrong and he went down and no one has been able to find him.");
                player.setCurrentDialogueId(13);
                break;
            case 13:
                player.getPA().sendNpcChat(BORIC, "The gnomes being the timid folk they are, haven't done the greatest of job trying to track him down. But that’s where you come in! Does that interest you? ");
                player.setCurrentDialogueId(14);
                break;
            case 14:
                player.getPA().sendOptions("Yes", "No");
                player.setCurrentDialogueId(15);
                break;
            case 16:
                player.getPA().sendNpcChat(BORIC, "Go to the Grand Tree in the Tree Gnome Stronghold and find King Narnode Shareen");
                player.setCurrentDialogueId(17);
                break;
            case 17:
                player.getPA().sendPlayerChat("Thanks!");
                player.setDialogueOwner(CloseDialogue.class);
                break;
            case 18:
                player.getPA().sendPlayerChat("Great!");
                player.setDialogueOwner(CloseDialogue.class);
                break;
            case 19:
                player.getPA().sendNpcChat(KING_SHAREEN, "Hello there, and who might you be?");
                player.setCurrentDialogueId(20);
                break;
            case 20:
                player.getPA().sendPlayerChat("My name is " + player.username + ".");
                player.setCurrentDialogueId(21);
                break;
            case 21:
                player.getPA().sendPlayerChat(" I was told you guys lost a gnome out in the desert?");
                player.setCurrentDialogueId(22);
                break;
            case 22:
                player.getPA().sendNpcChat(KING_SHAREEN, "That is correct, we had a pilot flying over the desert collecting data about the recent sand storms, he must of gotten caught up in a storm and had some sort of glider failure...");
                player.setCurrentDialogueId(23);
                break;
            case 23:
                player.getPA().sendNpcChat(KING_SHAREEN, "The last contact we had with him was a routine check in which was when he was flying over al kharid heading south.");
                player.setCurrentDialogueId(24);
                break;
            case 24:
                player.getPA().sendPlayerChat("Hmm, doesn't give me much to work with does it?");
                player.setCurrentDialogueId(25);
                break;
            case 25:
                player.getPA().sendNpcChat(KING_SHAREEN, "Unfortunately not, we have sent search parties out but have had no luck. It's very important we get him home, don't know how long he can last!");
                player.setCurrentDialogueId(26);
                break;
            case 26:
                player.getPA().sendPlayerChat("Is there anyone I could talk to too help me out in finding him?");
                player.setCurrentDialogueId(27);
                break;
            case 27:
                player.getPA().sendNpcChat(KING_SHAREEN, "Yes, go find the Greebo the Engineer, hes out walking around the community somewhere, he will give you some instructions on how to find the pilot easier.");
                player.setCurrentDialogueId(28);
                break;
            case 28:
                player.getPA().sendPlayerChat("Thanks");
                player.setDialogueOwner(CloseDialogue.class);
                player.getData().gnomeGliderDownStatus = 3;
                break;
            case 29:
                player.getPA().sendPlayerChat("My name is " + player.username + ".");
                player.setCurrentDialogueId(30);
                break;
            case 30:
                player.getPA().sendNpcChat(GREEBO, " Why hello " + player.username + ", my name is Greebo and I am the master engineer of the gnome community!");
                player.setCurrentDialogueId(31);
                break;
            case 31:
                player.getPA().sendNpcChat(GREEBO, "Some of this world's greatest creations have come from me, like for example, the dwarf cannon! They act like they built that on their own, those blue prints came from this brain.");
                player.setCurrentDialogueId(32);
                break;
            case 32:
                player.getPA().sendNpcChat(GREEBO, "The gnome glider? Guess who designed that? Me.");
                player.setCurrentDialogueId(33);
                break;
            case 33:
                player.getPA().sendPlayerChat("Speaking of the gnome glider, that’s actually why I am here.");
                player.setCurrentDialogueId(34);
                break;
            case 34:
                player.getPA().sendNpcChat(GREEBO, "Are you looking for an interview? I have already given the Varrock reporter a full interview.");
                player.setCurrentDialogueId(35);
                break;
            case 35:
                player.getPA().sendPlayerChat("Uh.. No.. I'm here for the gnome glider that went down in the desert.. You know, the missing pilot?");
                player.setCurrentDialogueId(36);
                break;
            case 36:
                player.getPA().sendNpcChat(GREEBO, "Ah yes, very tragic thing that happened.");
                player.setCurrentDialogueId(37);
                break;
            case 37:
                player.getPA().sendPlayerChat("I was planning on going out in the desert searching for him, King Shareen told me to consult you first.");
                player.setCurrentDialogueId(38);
                break;
            case 38:
                player.getPA().sendNpcChat(GREEBO, "Doesn’t surprise me, this place would be nothing without me!");
                player.setCurrentDialogueId(39);
                break;
            case 39:
                player.getPA().sendPlayerChat("So is there anything you can help me with? King Shareen told me that last check in you had with him was over the desert?");
                player.setCurrentDialogueId(40);
                break;
            case 40:
                player.getPA().sendNpcChat(GREEBO, "Correct, he had a routine check in with one of our tracking beacons that track our gliders throughout the world, the only problem is that we can't pinpoint remotely which one it came from...");
                player.setCurrentDialogueId(41);
                break;
            case 41:
                player.getPA().sendNpcChat(GREEBO, "We assume because of a big dust storm, which most likely is the same thing that brought down the glider.");
                player.setCurrentDialogueId(42);
                break;
            case 42:
                player.getPA().sendNpcChat(GREEBO, "If we wanted to find out which beacon it came from so we have a better idea of where he could be, you would have to go to each beacon and collect the data manually and bring it back to me to look at.");
                player.setCurrentDialogueId(43);
                break;
            case 43:
                player.getPA().sendNpcChat(GREEBO, "There are 3 in the desert. One near the al kharid mine, one south of shanty pass, and another way down by Nardah.");
                player.setCurrentDialogueId(44);
                break;
            case 44:
                player.getPA().sendPlayerChat("So what do I have to do exactly?");
                player.setCurrentDialogueId(45);
                break;
            case 45:
                player.getPA().sendNpcChat(GREEBO, "Look for the 3 beacons in the desert, one near alkharid mine, another near shanty pass, and the last one around the town of Nardah, and search each one and collect the data pages and bring them back to me!");
                player.setCurrentDialogueId(46);
                break;
            case 46:
                player.getPA().sendPlayerChat("Got it!");
                player.setDialogueOwner(CloseDialogue.class);
                player.getData().gnomeGliderDownStatus = 4;
                break;
            case 47:
                player.getPA().sendOptions("Yes", "No");
                break;
            case 48:
                if(!player.getItems().playerHasItem(13394) || !player.getItems().playerHasItem(13309) || !player.getItems().playerHasItem(1811)) {
                    player.getPA().sendNpcChat(GREEBO, "Woops looks like you don't have all the items. Keep looking!");
                    player.setDialogueOwner(CloseDialogue.class);
                    break;
                }

                player.getItems().deleteItem(13394, 1);
                player.getItems().deleteItem(13309, 1);
                player.getItems().deleteItem(1811, 1);
                player.getPA().sendStatement("You hand the data pages over.");
                player.setCurrentDialogueId(49);
                break;
            case 49:
                player.getPA().sendNpcChat(GREEBO, "Interesting...");
                player.setCurrentDialogueId(50);
                break;
            case 50:
                player.getPA().sendNpcChat(GREEBO, "Yes this definitely helps! It looks like the nearest check in came from the beacon right outside of the town of 'Nardah.' He seems to be a little north of the town.");
                player.setCurrentDialogueId(51);
                break;
            case 51:
                player.getPA().sendPlayerChat("Awesome, that really helps!");
                player.setCurrentDialogueId(52);
                break;
            case 52:
                player.getPA().sendPlayerChat("What should I be looking for?");
                player.setCurrentDialogueId(53);
                break;
            case 53:
                player.getPA().sendNpcChat(GREEBO, "Most likely a gnome walking around, he will be very timid as we are more on the shy side. He should be close to a crashed glider.");
                player.setCurrentDialogueId(54);
                break;
            case 54:
                player.getPA().sendPlayerChat("I will check out the area and see what I can find! ");
                player.setDialogueOwner(CloseDialogue.class);
                player.getData().gnomeGliderDownStatus = 5;
                break;
            case 55:
                player.getPA().sendPlayerChat("No not yet!");
                player.setDialogueOwner(CloseDialogue.class);
                break;
            case 56:
                player.getPA().sendPlayerChat("There you are! We've been looking all over for you!");
                player.setCurrentDialogueId(57);
                break;
            case 57:
                player.getPA().sendNpcChat(GNOME, "WATER!");
                player.setCurrentDialogueId(58);
                break;
            case 58:
                player.getPA().sendPlayerChat("You need water?");
                player.setCurrentDialogueId(59);
                break;
            case 59:
                player.getPA().sendNpcChat(GNOME, "There's a town to the south with a fountain, fill a bucket up with some water and bring it to me NOW!");
                player.setCurrentDialogueId(60);
                break;
            case 60:
                player.getPA().sendPlayerChat("Okay!");
                player.setCurrentDialogueId(61);
                break;
            case 61:
                player.getPA().sendNpcChat(GNOME, "Watch out for the bandits!!! ");
                player.setCurrentDialogueId(62);
                break;
            case 62:
                player.getPA().sendPlayerChat("Okay!");
                player.setDialogueOwner(CloseDialogue.class);
                player.getData().gnomeGliderDownStatus = 6;
                break;
            case 63:
                player.getPA().sendNpcChat(GNOME, "Thank you!");
                player.setCurrentDialogueId(64);
                break;
            case 64:
                player.getPA().sendStatement("You hand the water to the gnome...");
                player.getItems().deleteItem(1929, 1);
                player.setCurrentDialogueId(65);
                break;
            case 65:
                player.getPA().sendStatement("The gnome violently drinks the water and starts yelling in satisfaction.");
                player.setCurrentDialogueId(66);
                break;
            case 66:
                player.getPA().sendNpcChat(GNOME, "That’s what I'm talking about, that’s what I'm ******* talking about!");
                player.setCurrentDialogueId(67);
                break;
            case 67:
                player.getPA().sendNpcChat(GNOME, "Excuse my language, dehydration can do some things to your mental state.");
                player.setCurrentDialogueId(68);
                break;
            case 68:
                player.getPA().sendPlayerChat("No problem buddy!");
                player.setCurrentDialogueId(69);
                break;
            case 69:
                player.getPA().sendPlayerChat("I was sent by King Shareen to find you!");
                player.setCurrentDialogueId(70);
                break;
            case 70:
                player.getPA().sendNpcChat(GNOME, "They sent you?");
                player.setCurrentDialogueId(71);
                break;
            case 71:
                player.getPA().sendPlayerChat("Uh yes");
                player.setCurrentDialogueId(72);
                break;
            case 72:
                player.getPA().sendNpcChat(GNOME, "Leave it to the gnomes to be too timid to come save one of their own!");
                player.setCurrentDialogueId(73);
                break;
            case 73:
                player.getPA().sendNpcChat(GNOME, "Shareen should of come out here himself on his hands and knees crawling around this forsaken desert looking for me!");
                player.setCurrentDialogueId(74);
                break;
            case 74:
                player.getPA().sendNpcChat(GNOME, "I've been stuck out eating dirt, bugs, and my own spit pissing all over myself and hes up cool in his tree! He is going to HEAR IT FROM ME!");
                player.setCurrentDialogueId(75);
                break;
            case 75:
                player.getPA().sendPlayerChat("Woah woah lets just settle down there! I know you've been through a lot these past few days, but let's focus on the positive. You are found and going home!");
                player.setCurrentDialogueId(76);
                break;
            case 76:
                player.getPA().sendNpcChat(GNOME, "Not so fast, look at my glider! It got wrecked in this huge dust storm, never seen anything like it!");
                player.setCurrentDialogueId(77);
                break;
            case 77:
                player.getPA().sendPlayerChat("What happened?");
                player.setCurrentDialogueId(78);
                break;
            case 78:
                player.getPA().sendNpcChat(GNOME, "I was doing a routine fly over to checking out the landscape and seeing the effects of the recent storms, and I look out to the east and it looks like a wall of darkness. It was a massive dust storm coming...");
                player.setCurrentDialogueId(79);
                break;
            case 79:
                player.getPA().sendNpcChat(GNOME, "I tried to hit maximum altitude to try and get some rough measurements of the height of the storm, it had to of been a couple hundred feet high, it engulfed everything in its path...");
                player.setCurrentDialogueId(80);
                break;
            case 80:
                player.getPA().sendNpcChat(GNOME, "I misjudged the speed of the storm and by the time I hit maximum altitude it was already upon me, it turned to blackness and my engine started spewing and I dropped...");
                player.setCurrentDialogueId(81);
                break;
            case 81:
                player.getPA().sendNpcChat(GNOME, "Altitude and speeds to as low as I could go but the engine already failed and I took a dive into the sand. Took me about a day to un cover the glider, I still got sand in me in places I don’t care to share.");
                player.setCurrentDialogueId(82);
                break;
            case 82:
                player.getPA().sendPlayerChat("Oh my goodness! Are you ok? Sounds like quite an ordeal!");
                player.setCurrentDialogueId(83);
                break;
            case 83:
                player.getPA().sendNpcChat(GNOME, "Yeah I'm fine for the most part, some cuts and bruises but I'll live");
                player.setCurrentDialogueId(84);
                break;
            case 84:
                player.getPA().sendNpcChat(GNOME, "But the glider, not so sure. The left wing is badly damaged and un flyable until its replaced, I think I could get the engine back up and running just need some tools as a lot of them flew out in the crash.");
                player.setCurrentDialogueId(85);
                break;
            case 85:
                player.getPA().sendPlayerChat("What should I do to help?");
                player.setCurrentDialogueId(86);
                break;
            case 86:
                player.getPA().sendNpcChat(GNOME, "Tell Greebo I'm going to need a new left wing, wrench, turnscrew, and a toolbox.");
                player.setCurrentDialogueId(87);
                break;
            case 87:
                player.getPA().sendPlayerChat("I'm on it!");
                player.setDialogueOwner(CloseDialogue.class);
                player.getData().gnomeGliderDownStatus = 7;
                break;
            case 88:
                player.getPA().sendPlayerChat("Yes, I was able to locate the pilot!");
                player.setCurrentDialogueId(89);
                break;
            case 89:
                player.getPA().sendNpcChat(GREEBO, "That’s great! Where was he? Is he alright? How's the glider?");
                player.setCurrentDialogueId(90);
                break;
            case 90:
                player.getPA().sendPlayerChat("He was a few clicks north of Nardah. Yeah he seems alright for the most part, not sure about his mental stability right now, but now that there's hope for him returning I'm sure he'll settle down");
                player.setCurrentDialogueId(91);
                break;
            case 91:
                player.getPA().sendNpcChat(GREEBO, "That’s a relief! So what happened? How did he crash?");
                player.setCurrentDialogueId(92);
                break;
            case 92:
                player.getPA().sendPlayerChat("He says he got caught in a massive dust storm, the biggest he's ever seen and it caused engine failure and he crash landed.");
                player.setCurrentDialogueId(93);
                break;
            case 93:
                player.getPA().sendNpcChat(GREEBO, "Oh my! What shape is the glider in?");
                player.setCurrentDialogueId(94);
                break;
            case 94:
                player.getPA().sendPlayerChat("He said he's gonna need a left wing replaced, a wrench, a turnscrew, and a toolbox");
                player.setCurrentDialogueId(95);
                break;
            case 95:
                player.getPA().sendNpcChat(GREEBO, "That’s not good.. Hm let me think");
                player.setCurrentDialogueId(96);
                break;
            case 96:
                player.getPA().sendNpcChat(GREEBO, "Head on over to the lumberyard east of Varrock, and find Larry the lumberjack, he can get you the supplies you need. Then report back to me");
                player.setCurrentDialogueId(97);
                break;
            case 97:
                player.getPA().sendPlayerChat("Larry the lumberjack? What a name");
                player.setCurrentDialogueId(98);
                break;
            case 98:
                player.getPA().sendNpcChat(GREEBO, "He eats, sleeps, and lives for wood");
                player.setCurrentDialogueId(99);
                break;
            case 99:
                player.getPA().sendPlayerChat("I sure hope not!");
                player.setDialogueOwner(CloseDialogue.class);
                player.getData().gnomeGliderDownStatus = 8;
                break;
            case 100:
                player.getPA().sendNpcChat(LARRY, "No my name is Linda");
                player.setCurrentDialogueId(101);
                break;
            case 101:
                player.getPA().sendPlayerChat("Uh.. I must have the wrong person.");
                player.setCurrentDialogueId(102);
                break;
            case 102:
                player.getPA().sendNpcChat(LARRY, "Not at all my boy! Just having a good ole fashion joke!");
                player.setCurrentDialogueId(103);
                break;
            case 103:
                player.getPA().sendNpcChat(LARRY, "Did you laugh?");
                player.setCurrentDialogueId(104);
                break;
            case 104:
                player.getPA().sendPlayerChat("Uh somewhat");
                player.setCurrentDialogueId(105);
                break;
            case 105:
                player.getPA().sendNpcChat(LARRY, "Seems I need to practice more, it gets very lonely up here surrounded by all this wood");
                player.setCurrentDialogueId(106);
                break;
            case 106:
                player.getPA().sendNpcChat(LARRY, "Is it too hard to ask for a woman who shares the same passion for wood as myself?");
                player.setCurrentDialogueId(107);
                break;
            case 107:
                player.getPA().sendPlayerChat("I'm sure there's a lot of women who have a passion for wood");
                player.setCurrentDialogueId(108);
                break;
            case 108:
                player.getPA().sendNpcChat(LARRY, "HAHAHA now that’s a good one! I gotta write that one down!");
                player.setCurrentDialogueId(109);
                break;
            case 109:
                player.getPA().sendPlayerChat("That is going to cost cost you!");
                player.setCurrentDialogueId(110);
                break;
            case 110:
                player.getPA().sendNpcChat(LARRY, "I like you....");
                player.setCurrentDialogueId(111);
                break;
            case 111:
                player.getPA().sendNpcChat(LARRY, "Whats your name?");
                player.setCurrentDialogueId(112);
                break;
            case 112:
                player.getPA().sendPlayerChat("My name is " + player.username);
                player.setCurrentDialogueId(113);
                break;
            case 113:
                player.getPA().sendNpcChat(LARRY, "Well " + player.username + ", what can I do for you?");
                player.setCurrentDialogueId(114);
                break;
            case 114:
                player.getPA().sendPlayerChat("Greebo sent me actually");
                player.setCurrentDialogueId(115);
                break;
            case 115:
                player.getPA().sendNpcChat(LARRY, "Oh greebo the engineer? The gnome?");
                player.setCurrentDialogueId(116);
                break;
            case 116:
                player.getPA().sendPlayerChat("Yes");
                player.setCurrentDialogueId(117);
                break;
            case 117:
                player.getPA().sendNpcChat(LARRY, "I'm very fond of him, great guy");
                player.setCurrentDialogueId(118);
                break;
            case 118:
                player.getPA().sendPlayerChat("Indeed");
                player.setCurrentDialogueId(119);
                break;
            case 119:
                player.getPA().sendPlayerChat("But anyways, a gnome glider went down in the desert and crash landed and needs some repairs to get it and the pilot home");
                player.setCurrentDialogueId(120);
                break;
            case 120:
                player.getPA().sendNpcChat(LARRY, "Those gnomes, they push those gliders to far, they aren't built to handle the rough terrain and elements of the desert");
                player.setCurrentDialogueId(121);
                break;
            case 121:
                player.getPA().sendPlayerChat("They need a wrench, a turnscrew, a toolbox, and a replacement left wing");
                player.setCurrentDialogueId(122);
                break;
            case 122:
                player.getPA().sendNpcChat(LARRY, "I see, well you came to the right place!");
                player.setCurrentDialogueId(123);
                break;
            case 123:
                player.getPA().sendNpcChat(LARRY, "First, to get the tools, I got a buddy who runs a tool shop in Varrock, his name is Doug, he can get you the tools you need And for the wing replacement, get me 50 yew logs, and 15 steel bars, and I'll get you the wing");
                player.setCurrentDialogueId(124);
                break;
            case 124:
                player.getPA().sendPlayerChat("I'm on it.");
                player.setDialogueOwner(CloseDialogue.class);
                player.getData().gnomeGliderDownStatus = 9;
                break;
            case 125:
                if(!player.getItems().playerHasItem(6713, 1) || !player.getItems().playerHasItem(15253, 1) || !player.getItems().playerHasItem(14821, 1) || !player.getItems().playerHasItem(1516, 50) || !player.getItems().playerHasItem(2354, 15)) {
                    player.getPA().sendPlayerChat("Not yet.");
                    player.setDialogueOwner(CloseDialogue.class);
                } else {
                    player.getPA().sendPlayerChat("Yes I do!");
                    player.setCurrentDialogueId(126);
                }
                break;
            case 126:
                player.getPA().sendNpcChat(LARRY, "Hand them over!");
                player.setCurrentDialogueId(127);
                break;
            case 127:
                player.getPA().sendStatement("You hand the items over to Larry");
                player.setCurrentDialogueId(128);

                player.getItems().deleteItem(6713, 1);
                player.getItems().deleteItem(15253, 1);
                player.getItems().deleteItem(14821, 1);
                player.getItems().deleteItem(1516, 50);
                player.getItems().deleteItem(2354, 15);
                break;
            case 128:
                player.getPA().sendNpcChat(LARRY, "I will get you your items");
                player.setCurrentDialogueId(129);

                player.getItems().addItem(7959, 1);
                break;
            case 129:
                player.getPA().sendPlayerChat("So is this everything?");
                player.setCurrentDialogueId(130);
                break;
            case 130:
                player.getPA().sendNpcChat(LARRY, "Yes it is!");
                player.setCurrentDialogueId(131);
                break;
            case 131:
                player.getPA().sendPlayerChat("Awesome, I will take this back to greebo!");
                player.setCurrentDialogueId(132);
                break;
            case 132:
                player.getPA().sendPlayerChat("Thanks a lot! See you around Larry!");
                player.setCurrentDialogueId(133);
                break;
            case 133:
                player.getPA().sendNpcChat(LARRY, "You too " + player.username);
                player.setDialogueOwner(CloseDialogue.class);
                player.getData().gnomeGliderDownStatus = 10;
                break;
            case 135:
                player.getPA().sendNpcChat(LARRY, "First, to get the tools, I got a buddy who runs a toolshop in Varrock, his name is doug, he can get you the tools you need");
                player.setCurrentDialogueId(136);
                break;
            case 136:
                player.getPA().sendNpcChat(LARRY, "And for the wing replacement, get me 50 yew logs,  and 15 steel bars, and Ill get you the wing");
                player.setCurrentDialogueId(137);
                break;
            case 137:
                player.getPA().sendPlayerChat("I'm on it!");
                player.setDialogueOwner(CloseDialogue.class);
                break;
            case 138:
                player.getPA().sendNpcChat(GREEBO, "Very good");
                player.setCurrentDialogueId(139);
                break;
            case 139:
                player.getPA().sendNpcChat(GREEBO, "Get this to the gnome as soon as possible!");
                player.setCurrentDialogueId(140);
                break;
            case 140:
                player.getPA().sendPlayerChat("On it!");
                player.setDialogueOwner(CloseDialogue.class);
                player.getData().gnomeGliderDownStatus = 11;
                break;
            case 141:
                player.getPA().sendNpcChat(GNOME, "Hooray! Hand it over");
                player.setCurrentDialogueId(142);
                break;
            case 142:
                if(player.getItems().playerHasItem(7959)) {
                    player.getPA().sendStatement("You hand the supplies to the gnome pilot");
                    player.getItems().deleteItem(7959, 1);
                    player.setCurrentDialogueId(143);
                } else {
                    player.getPA().sendNpcChat(GNOME, "You do not have the supplies");
                    player.setDialogueOwner(CloseDialogue.class);
                }
                break;
            case 143:
                player.getPA().sendNpcChat(GNOME, "Excellent, it seems like it's all here!");
                player.setCurrentDialogueId(144);
                break;
            case 144:
                player.getPA().sendNpcChat(GNOME, "I'll get this baby up and running! Tell the King I'm back!");
                player.setCurrentDialogueId(145);
                break;
            case 145:
                player.getPA().sendPlayerChat("Wooohooo!");
                player.setDialogueOwner(CloseDialogue.class);
                player.getData().gnomeGliderDownStatus = 12;
                break;
            case 146:
                player.getPA().sendPlayerChat("So glad I could help out");
                player.setCurrentDialogueId(147);
                break;
            case 147:
                player.getPA().sendNpcChat(KING_SHAREEN, "You are forever in our favor, in return you have access to our engineer whenever you want, he can make you a dragon fire shield, or a fury whenever you like!");
                player.setCurrentDialogueId(148);
                break;
            case 148:
                player.getPA().sendNpcChat(KING_SHAREEN, "Also you can trade our engineer for some fancy gnome clothing!");
                player.setCurrentDialogueId(149);
                break;
            case 149:
                player.getPA().sendPlayerChat("Awesome!");
                player.setCurrentDialogueId(150);
                break;
            case 150:
                player.getPA().sendNpcChat(KING_SHAREEN, "Thank you so much!");
                player.setCurrentDialogueId(151);
                break;
            case 151:
                if(player.getData().gnomeGliderDownStatus == 13) {
                    break;
                }
                player.getData().gnomeGliderDownStatus = 13;
                player.questPoints += 3;
                QuestHandler.sendQuestRewardInterface(player, QUEST_NAME, "Awarded 3 quest point", "Access to the gnomes engineer", "Access to the gnome clothing store");

                sendQuestColor(player);
                QuestHandler.sendQuestPoints(player);
                break;
            case 152:
                player.getPA().sendNpcChat(GREEBO, "What can I do for you?");
                player.setCurrentDialogueId(153);
                break;
            case 153:
                player.getPA().sendOptions("Make me a dfs", "Make me a fury", "Nothing");
                break;
        }
    }

    @Override
    public void onDialogueOption(DialogueOptionEvent event) {
        Player player = event.getPlayer();
        if (player.getDialogueOwner() == null || !player.getDialogueOwner().equals(GnomeGliderDown.class)) {
            return;
        }

        event.setHandled(true);

        switch (player.getCurrentDialogueId()) {
            case 15:
                switch (event.getOption()) {
                    case 1: // yes
                        player.getPA().sendPlayerChat("Very much so! Where do I start?");
                        player.getData().gnomeGliderDownStatus = 2;
                        player.setCurrentDialogueId(16);
                        sendQuestColor(player);
                        break;
                    case 2: // no
                        player.getPA().sendNpcChat(BORIC, "Maybe another time!");
                        player.setDialogueOwner(CloseDialogue.class);
                        player.getData().gnomeGliderDownStatus = 1;
                        break;
                }
                break;
            case 47:
                switch (event.getOption()) {
                    case 1:
                        if(!player.getItems().playerHasItem(13394) || !player.getItems().playerHasItem(13309) || !player.getItems().playerHasItem(1811)) {
                            player.getPA().sendNpcChat(GREEBO, "Woops looks like you don't have all the items. Keep looking!");
                            player.setDialogueOwner(CloseDialogue.class);
                        } else {
                            player.getPA().sendNpcChat(GREEBO, "Great! Hand them over!");
                            player.setCurrentDialogueId(48);
                        }
                        break;
                    case 2:
                        player.getPA().sendNpcChat(GREEBO, "Keep looking!");
                        player.setDialogueOwner(CloseDialogue.class);
                        break;
                }
                break;
            case 134:
                switch (event.getOption()) {
                    case 1:
                        player.getPA().sendNpcChat(LARRY, "Hey " + player.username + ".");
                        player.setDialogueOwner(CloseDialogue.class);
                        break;
                    case 2:
                        if(player.getItems().playerHasItem(6713, 1) && player.getItems().playerHasItem(15253, 1) && player.getItems().playerHasItem(14821, 1) && player.getItems().playerHasItem(1516, 50) && player.getItems().playerHasItem(2354, 15)) {
                            player.getPA().sendNpcChat(LARRY, "Try not to loose this one.");
                            player.setDialogueOwner(CloseDialogue.class);

                            player.getItems().deleteItem(6713, 1);
                            player.getItems().deleteItem(15253, 1);
                            player.getItems().deleteItem(14821, 1);
                            player.getItems().deleteItem(1516, 50);
                            player.getItems().deleteItem(2354, 15);

                            player.getItems().addItem(7959, 1);
                        } else {
                            player.getPA().sendNpcChat(LARRY, "Oh dear!");
                            player.setCurrentDialogueId(135);
                        }
                        break;
                }
                break;
            case 153:
                switch (event.getOption()) {
                    case 1:
                        if (!player.getItems().playerHasItem(11286)) {
                            player.getPA().sendNpcChat(GREEBO, "You do not have a Draconic visage.");
                            player.setDialogueOwner(CloseDialogue.class);
                            break;
                        }
                        player.getItems().deleteItem(11286, 1);
                        player.getItems().addItem(11283, 1);
                        player.getPA().sendNpcChat(GREEBO, "All done for you " + player.username + ".");
                        player.setDialogueOwner(CloseDialogue.class);
                        break;
                    case 2:
                        if (!player.getItems().playerHasItem(6571)) {
                            player.getPA().sendNpcChat(GREEBO, "You do not have an uncut onyx.");
                            player.setDialogueOwner(CloseDialogue.class);
                            break;
                        }
                        player.getItems().deleteItem(6571, 1);
                        player.getItems().addItem(6585, 1);
                        player.getPA().sendNpcChat(GREEBO, "All done for you " + player.username + ".");
                        player.setDialogueOwner(CloseDialogue.class);
                        break;
                    case 3:
                        player.resetDialogue();
                        break;
                }
                break;
        }
    }

    @Override
    public void onClickingButton(ClickingButtonEvent event) {
        Player player = event.getPlayer();
        event.setHandled(true);

        switch (event.getButton()) {
            case 74219:
                QuestInterfaceGenerator qig = new QuestInterfaceGenerator(QUEST_NAME, player.getData().gnomeGliderDownStatus);
                qig.add(0, "I should find Boric in falador and speak to him");
                qig.add(2, "I should head over to the Tree Gnome Stronghold and talk to King Narnode Shareen.");
                qig.add(3, "I should find Greebo the Engineer who is somewhere in the gnome community.");
                qig.add(4, "I should head over to the desert and look for these beacons.");
                qig.add(5, "I should look for the gnome north of Nardah.");
                qig.add(6, "The Gnome Pilot needs water, he said you can find some south of his location.");
                qig.add(7, "I should return to Greebo with the new information.");
                qig.add(8, "I should head on over to the Varrock Lumberyard and find Larry.");
                qig.add(9, "I should get the tools from doug in varrock, and 50 yew logs and 15 steel bars");
                qig.add(11, "I should get this supplies to the gnome pilot");
                qig.add(12, "I should head on back and talk with the King ");
                qig.writeQuest(player);
                return;
        }

        event.setHandled(false);
    }

    @Override
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        sendQuestColor(event.getPlayer());
    }

    private static void sendQuestColor(Player player) {
        String color = "@red@";
        if(player.getData().gnomeGliderDownStatus > 1) {
            color = "@yel@";
        }
        if(player.getData().gnomeGliderDownStatus == 13) {
            color = "@gre@";
        }
        player.getPA().sendFrame126(color + QUEST_NAME, SIDEBAR_INTERFACE_ID);
    }

    @Override
    public void onObjectClicked(ClickObjectEvent event) {
        Player player = event.getPlayer();
        if(event.getObjectId() == 31149) {
            if(player.getX() <= 3295) {
                player.getPA().movePlayer(3296, 3498);
            } else {
                player.getPA().movePlayer(3295, 3498);
            }
        }
        if(event.getObjectId() == 25200) {
            event.setHandled(true);
            player.getItems().addItem(13394, 1);
            player.sendMessage("You find a bit of paper.");
        }
        if(event.getObjectId() == 28847) {
            event.setHandled(true);

            player.getItems().addItem(13309, 1);
            player.sendMessage("You find a bit of paper.");
        }
        if(event.getObjectId() == 38266) {
            event.setHandled(true);

            player.getItems().addItem(1811, 1);
            player.sendMessage("You find a bit of paper.");
        }
    }
}