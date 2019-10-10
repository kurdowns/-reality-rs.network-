package net.crandor;

final class NodeList {

    public NodeList() {
        head = new Node();
        head.prev = head;
        head.next = head;
    }

    public void transfer(NodeList toNodeList) {
		Node start = head.prev;
		Node prev = head.next;
		head.next = start.next;
		start.next.prev = head;
		if (head == start) {
			return;
		}

		start.next = toNodeList.head.next;
		start.next.prev = start;
		prev.prev = toNodeList.head;
		toNodeList.head.next = prev;
    }

    public void insertHead(Node node) {
        if (node.next != null)
            node.unlink();
        node.next = head.next;
        node.prev = head;
        node.next.prev = node;
        node.prev.next = node;
    }

    public void insertTail(Node node) {
        if (node.next != null)
            node.unlink();
        node.next = head;
        node.prev = head.prev;
        node.next.prev = node;
        node.prev.next = node;
    }

    public Node popHead() {
        Node node = head.prev;
        if (node == head) {
            return null;
        } else {
            node.unlink();
            return node;
        }
    }

    public Node reverseGetFirst() {
        Node node = head.prev;
        if (node == head) {
            current = null;
            return null;
        }

        current = node.prev;
        return node;
    }

    public Node getFirst() {
        Node node = head.next;
        if (node == head) {
            current = null;
            return null;
        } else {
            current = node.next;
            return node;
        }
    }

    public Node reverseGetNext() {
        Node node = current;
        if (node == head) {
            current = null;
            return null;
        }

        current = node.prev;
        return node;
    }

    public Node getNext() {
        Node node = current;
        if (node == head) {
            current = null;
            return null;
        }
        current = node.next;
        return node;
    }

    public void clear() {
        for (;;) {
            Node node = head.prev;
            if (node == head) {
                break;
            }
            node.unlink();
        }
        current = null;
    }

    final Node head;
    private Node current;
}
