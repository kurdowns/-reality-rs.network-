package com.zionscape.server.events;

public class Event {

    private boolean handled = false;

    public boolean isHandled() {
        return handled;
    }

    public void setHandled(boolean handled) {
        this.handled = handled;
    }

}