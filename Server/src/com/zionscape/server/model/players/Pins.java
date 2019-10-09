package com.zionscape.server.model.players;

import com.zionscape.server.model.content.grandexchange.GrandExchange;
import com.zionscape.server.model.players.dialogue.CloseDialogue;
import com.zionscape.server.net.Packet;
import com.zionscape.server.net.PacketBuilder;

import java.util.Optional;

public class Pins {

    public static void open(Player player, Type type) {
        if(player.getData().pin > 0 && !player.attributeExists("entered_pin")) {
            player.setAttribute("pin_type", type);

            player.setDialogueOwner(Pins.class);

            Packet packet = new PacketBuilder(28, Packet.Type.VARIABLE_SHORT).putRS2String("Please enter your pin:").putRS2String("\\d").putRS2String("pin").toPacket();
            player.writePacket(packet);
        } else {
            type.open(player);
        }
    }

    public static boolean checkPin(Player player) {
        if(player.getData().pin > 0 && !player.attributeExists("entered_pin")) {
            return true;
        }
        return false;
    }

    public static void process(Player player) {
        if(player.getData().pin == 0 && player.getData().minutesOnline > 10 * 60 && Areas.inHomeArea(player.getLocation()) && !player.attributeExists("set_pin")) {
            player.setAttribute("set_pin", true);

            player.getPA().sendNpcChat(945, "To continue playing you must have an account pin of 5 digits, this stops unauthorised players using your account.");
            player.setDialogueOwner(Pins.class);
            player.setCurrentDialogueId(1);

            player.setAttribute("pin_block_packets", true);
        }
    }

    public static void onDialogueInput(Player player, String input, String type) {
        if(player.getDialogueOwner() == null || !player.getDialogueOwner().equals(Pins.class)) {
            return;
        }

        if(type.equalsIgnoreCase("pin") && player.attributeExists("pin_type")) {
            try {
                long pin = Long.parseLong(input);
                if(pin != player.getData().pin) {
                    player.getPA().sendStatement("Incorrect pin, try again.");
                    return;
                }
            } catch (NumberFormatException e) {
                return;
            }

            Type t = (Type)player.getAttribute("pin_type");
            t.open(player);

            player.removeAttribute("pin_block_packets");
            player.setAttribute("entered_pin", true);
            player.removeAttribute("pin_type");
            player.setDialogueOwner(CloseDialogue.class);
            return;
        }

        if(!player.attributeExists("set_pin")) {
            return;
        }
        if(!type.equalsIgnoreCase("new_pin")) {
            return;
        }

        if(input.length() < 5) {
            player.getPA().sendNpcChat(945, "Pin must be at least 5 digits in length.");
            player.setCurrentDialogueId(1);
            return;
        }

        long pin;
        try {
            pin = Long.parseLong(input);
        } catch (NumberFormatException e) {
            player.getPA().sendNpcChat(945, "Pin must be digits only.");
            player.setCurrentDialogueId(1);
            return;
        }

        player.setAttribute("set_pin", pin);
        player.getPA().sendNpcChat(945, "Your pin will be set to " + pin + ", is that correct?");
        player.setCurrentDialogueId(2);

    }

    public static boolean onDialogueContinue(Player player) {
        if(player.getDialogueOwner() == null || !player.getDialogueOwner().equals(Pins.class)) {
            return false;
        }

        if(player.attributeExists("pin_type")) {
            Packet packet = new PacketBuilder(28, Packet.Type.VARIABLE_SHORT).putRS2String("Please enter your pin:").putRS2String("\\d").putRS2String("pin").toPacket();
            player.writePacket(packet);
            return true;
        }

        switch (player.getCurrentDialogueId()) {
            case 1:
                Packet packet = new PacketBuilder(28, Packet.Type.VARIABLE_SHORT).putRS2String("Please enter a pin:").putRS2String("\\d").putRS2String("new_pin").toPacket();
                player.writePacket(packet);
                break;
            case 2:
                player.getPA().sendOptions(Optional.of("Pin will be set to " + player.getAttribute("set_pin")), "Yes", "No");
                player.setCurrentDialogueId(3);
                break;
        }

        return true;
    }

    public static boolean onDialogueOption(Player player, int option) {
        if(player.getDialogueOwner() == null || !player.getDialogueOwner().equals(Pins.class)) {
            return false;
        }

        if(!player.attributeExists("set_pin")) {
            return false;
        }

        if(player.getCurrentDialogueId() == 3) {
            if (option == 1) {
                player.removeAttribute("pin_block_packets");
                player.getData().pin = (long)player.getAttribute("set_pin");
                player.getPA().sendNpcChat(945, "Your pin has been set to " + player.getData().pin + ", each time you login on a new ip address you will need to enter this pin. DO NOT FORGET IT.");
                player.setDialogueOwner(CloseDialogue.class);
                player.removeAttribute("set_pin");

                player.getData().trustedIps.add(player.connectedFrom);
            }

            if (option == 2) {
                player.setCurrentDialogueId(1);
                onDialogueContinue(player);
            }
        }

        return true;
    }

    public static void onPlayerLoggedIn(Player player) {
        if(player.getData().pin > 0 && !player.getData().trustedIps.contains(player.connectedFrom)) {

            player.setDialogueOwner(Pins.class);
            player.setAttribute("pin_type", Type.ACCOUNT_PIN);
            Packet packet = new PacketBuilder(28, Packet.Type.VARIABLE_SHORT).putRS2String("Please enter your pin to continue playing:").putRS2String("\\d").putRS2String("pin").toPacket();
            player.writePacket(packet);

            player.setAttribute("pin_block_packets", true);
        }
    }

    public enum Type {
        ACCOUNT_PIN((Player player) -> {
            player.getData().trustedIps.add(player.connectedFrom);
            player.sendMessage("Your ip has been added to the trusted ip list on your account.");
        }),
        BANK((Player player) -> {
            player.getBank().openBank();
        }),
        MONEY_POUCH((Player player) -> {
            player.sendMessage("You may now withdraw from your pouch.");
        }),
        GRAND_EXCHANGE((Player player) -> {
            GrandExchange.openGrandExchange(player);
        });

        private Open open;

        Type(Open open) {
            this.open = open;
        }

        public void open(Player player) {
            open.open(player);
        }
    }

    private interface Open {
        void open(Player player);
    }

}