package net.crandor;

final class MRUNodes {

    MRUNodes(int i) {
        emptyNodeSub = new NodeSub();
        nodeSubList = new NodeSubList();
        initialCount = i;
        spaceLeft = i;
        nodeCache = new NodeCache();
    }

    NodeSub get(long l) {
        NodeSub nodeSub = (NodeSub) nodeCache.findNodeByID(l);
        if (nodeSub != null) {
            nodeSubList.insertHead(nodeSub);
        }
        return nodeSub;
    }

	void put(NodeSub nodeSub, long l) {
		if (spaceLeft == 0) {
			NodeSub nodeSub_1 = nodeSubList.popTail();
			nodeSub_1.unlink();
			nodeSub_1.unlinkSub();
			if (nodeSub_1 == emptyNodeSub) {
				NodeSub nodeSub_2 = nodeSubList.popTail();
				nodeSub_2.unlink();
				nodeSub_2.unlinkSub();
			}
		} else {
			spaceLeft--;
		}
		nodeCache.removeFromCache(nodeSub, l);
		nodeSubList.insertHead(nodeSub);
	}

    void unlinkAll() {
        do {
            NodeSub nodeSub = nodeSubList.popTail();
            if (nodeSub != null) {
                nodeSub.unlink();
                nodeSub.unlinkSub();
            } else {
                spaceLeft = initialCount;
                return;
            }
        } while (true);
    }

    private final NodeSub emptyNodeSub;
    private final int initialCount;
    private int spaceLeft;
    private final NodeCache nodeCache;
    private final NodeSubList nodeSubList;
}
