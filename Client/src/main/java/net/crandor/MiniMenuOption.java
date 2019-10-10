package net.crandor;

public class MiniMenuOption extends Node {
    int cmd2;
    int cmd3;
    int uid;
    long cmd1;
    String name;//Option name: Add friend, Wield, Drop, Message
    String base;//Base is the Owner of the option: Abyssal whip

    public MiniMenuOption(String name, String base, int uid, long cmd1, int cmd2, int cmd3) {
    	this.name = name;
    	this.base = base;
    	this.uid = uid;
    	this.cmd1 = cmd1;
    	this.cmd2 = cmd2;
    	this.cmd3 = cmd3;
    }

}
