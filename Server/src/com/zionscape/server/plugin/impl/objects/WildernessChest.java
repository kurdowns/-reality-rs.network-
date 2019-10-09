package com.zionscape.server.plugin.impl.objects;

import com.zionscape.server.events.Listener;
import com.zionscape.server.events.impl.ClickObjectEvent;
import com.zionscape.server.events.impl.DialogueOptionEvent;
import com.zionscape.server.model.items.ItemDefinition;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.dialogue.CloseDialogue;
import com.zionscape.server.plugin.Plugin;
import com.zionscape.server.util.CollectionUtil;
import com.zionscape.server.util.Misc;

public class WildernessChest implements Listener, Plugin {


    private static final int[][] EASY_ITEMS = new int[][]{
            {5298, 1, 3}, //seeds
            {5299, 1, 3},
            {5300, 1, 3},
            {5301, 1, 3},
            {5302, 1, 3},
            {5303, 1, 3},
            {5304, 1, 3},
            {445, 30, 35}, //coal
            {1437, 100, 350},   //rune ess
            {384, 50, 75}, //raw sharks
            {533, 50, 60}, //Big bones
            {12158, 12, 18},  //Charms
            {12159, 12, 18},
            {12160, 12, 18},
            {9193, 50}, //dragon bolt tips
            {12163, 12, 18}, // Charms
            {11212, 30}, //dragon arrows
            {1604, 15, 20}, //rubys
            {13727, 100, 200}, //stardust
            {2683, 1}, //clue scroll easy
    };
    private static final int[][] MEDIUM_ITEMS = new int[][]{
            {537, 30, 50}, //dragon bones
            {3140}, //d chain
            {4087}, //d legs
            {6739}, // d axe
            {7158},//d 2h
            {4716}, //dharok hem
            {4718}, //dhrok axe
            {4720}, //dharok plate
            {4722}, //dharok legs
            {4753}, //verac helm
            {4755}, // verac flail
            {4757}, //verac bassy
            {4759}, //v skirt
            {4708}, //ahrim hood
            {4710}, //ahrim staff
            {4712}, //ahrim robe top
            {4717}, // ahrim skirt
            {4724}, //guthan helm
            {4726}, //guthan spear
            {4728}, //guth plate
            {4730}, // guth skirt
            {4745}, //torags helm
            {4747}, //torag hammer
            {4749}, //torag plat
            {4751}, //torag legs
            {4732}, //karils coif
            {4734}, //karils crossbow
            {4736}, //karils top
            {4738}, //karil skirt
            {4151}, // whip
            {11235}, // dark bow
            {6585}, //fury
            {15486}, //sol
            {11732}, //d boots
            {21787}, //steads
            {21790}, //glav
            {21793}, //ragefire
            {10551}, //torso
            {452, 50}, //rune ore
            {1514, 80, 100}, //magic logs
            {2801}, //clue scroll
            {25106} //armadyl c bow
    };

    private static final int[] HARD_ITEMS = new int[]{
            2735,  //clue scroll
            11694,  //ags
            11696,  //bgs
            11698,  //sgs
            11700,  //zgs
            21371,  //vine whip
            19780,  //korasi
            20173,  //z bow
            14484,  //d claws
            18349,  //chatoic rapier
            18351,  //cls
            18353, //c maul
            18355, //c staff
            18357, // c bow
            13899, //vls
            8465, //kiln cape
            13902,//stat hammer
            25053, //d warhammer
            11730,  // sara sword
            21371,  //blowpipe
            11726, //bandos top
            11724, //bandos bottom
            15220, // b ring i
            13740, // divine
            13744, //spectral
            13738, // acrane
            13742, //ely
            526, //bones
            15332,//overloads


    };

    @Override
    public void onObjectClicked(ClickObjectEvent event) {
        if (event.getObjectId() == 4116) {
            event.setHandled(true);

            Player player = event.getPlayer();

            if (player.ironman) {
                player.sendMessage("You cannot do this on an ironman account.");
                return;
            }

            if(!player.getItems().playerHasItem(9722)) {
                player.getPA().sendStatement("Obtain wilderness keys from killing players in the wild for big rewards!");
                player.setDialogueOwner(CloseDialogue.class);
                return;
            }

            player.getItems().deleteItem(9722, 1);


            //easy 50% chance of getting, medium 35 % chance, hard 15% change?

            int itemId = 0;
            int amount = 1;
            int random = Misc.random(99);
            if(random <= 60) { // easy
                int arr[] = CollectionUtil.getRandomElement(EASY_ITEMS);

                itemId = arr[0];
                if(arr.length == 2) {
                    amount = arr[1];
                } else if(arr.length == 3) {
                    amount = Misc.random(arr[1], arr[2]);
                }
            } else if(random >= 61 && random <= 86) { // mediuam
                int arr[] = CollectionUtil.getRandomElement(MEDIUM_ITEMS);

                itemId = arr[0];
                if(arr.length == 2) {
                    amount = arr[1];
                } else if(arr.length == 3) {
                    amount = Misc.random(arr[1], arr[2]);
                }
            } else { // hard
                itemId = CollectionUtil.getRandomElement(HARD_ITEMS);
                PlayerHandler.yell("[Server]\"" + player.username + "\" has received " + player.getItems().getItemName(itemId) + " from the wilderness chest!");
            }

            player.getData().wildernessChestsOpened++;

            player.getItems().addItem(itemId, amount);
            player.sendMessage("You receive " + amount + " x " + ItemUtility.getName(itemId) + ".");
        }
    }

}
