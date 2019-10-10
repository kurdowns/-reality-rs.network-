package net.crandor;

public class ChatMessage extends NodeSub {
	
	public static final int GAME_MESSAGE = 0;
	public static final int PUBLIC_MESSAGE_STAFF = 1;
	public static final int PUBLIC_MESSAGE_PLAYER = 2;
	public static final int PRIVATE_MESSAGE_RECEIVED_PLAYER = 3;
	public static final int TRADE_REQUEST = 4;
	public static final int PRIVATE_MESSAGE_RECEIVED = 5;
	public static final int PRIVATE_MESSAGE_SENT = 6;
	public static final int PRIVATE_MESSAGE_RECEIVED_STAFF = 7;
	public static final int CHALLENGE_REQUEST = 8;
	public static final int CLAN_MESSAGE = 9;

	public int uid;
	public int type;
	public String message;
	public String title;
	public String name;

	public ChatMessage(int type, String message, String title, String name) {
		set(type, message, title, name);
	}

	public void set(int type, String message, String title, String name) {
		this.uid = ChatMessageManager.messageCount++;
		this.type = type;
		this.message = message;
		this.title = title;
		this.name = name;
	}

}
