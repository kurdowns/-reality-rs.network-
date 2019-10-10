package net.crandor;

class Node {
	
    Node prev;
    Node next;
    long id;

    final void unlink() {
        if (next != null) {
            next.prev = prev;
            prev.next = next;
            prev = null;
            next = null;
        }
    }

}
