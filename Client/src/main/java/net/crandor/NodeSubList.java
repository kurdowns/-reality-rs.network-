package net.crandor;

final class NodeSubList {

    public NodeSubList() {
        head = new NodeSub();
        head.prevNodeSub = head;
        head.nextNodeSub = head;
    }

    public void insertHead(NodeSub nodeSub) {
        if (nodeSub.nextNodeSub != null)
            nodeSub.unlinkSub();
        nodeSub.nextNodeSub = head.nextNodeSub;
        nodeSub.prevNodeSub = head;
        nodeSub.nextNodeSub.prevNodeSub = nodeSub;
        nodeSub.prevNodeSub.nextNodeSub = nodeSub;
    }

    public NodeSub popTail() {
        NodeSub nodeSub = head.prevNodeSub;
        if (nodeSub == head) {
            return null;
        } else {
            nodeSub.unlinkSub();
            return nodeSub;
        }
    }

    public NodeSub reverseGetFirst() {
        NodeSub nodeSub = head.prevNodeSub;
        if (nodeSub == head) {
            current = null;
            return null;
        } else {
            current = nodeSub.prevNodeSub;
            return nodeSub;
        }
    }

    public NodeSub reverseGetNext() {
        NodeSub nodeSub = current;
        if (nodeSub == head) {
            current = null;
            return null;
        } else {
            current = nodeSub.prevNodeSub;
            return nodeSub;
        }
    }

    public int getNodeCount() {
        int i = 0;
        for (NodeSub nodeSub = head.prevNodeSub; nodeSub != head; nodeSub = nodeSub.prevNodeSub)
            i++;

        return i;
    }

    public void clear() {
		while (true) {
			NodeSub first = head.prevNodeSub;
			if (head == first) {
				current = null;
				return;
			}

			first.unlinkSub();
		}
	}

    public final NodeSub head;
    private NodeSub current;

}
