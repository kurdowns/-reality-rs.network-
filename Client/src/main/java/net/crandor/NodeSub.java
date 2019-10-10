package net.crandor;

class NodeSub extends Node {

    NodeSub prevNodeSub;
    NodeSub nextNodeSub;
	
    final void unlinkSub() {
        if (nextNodeSub != null) {
            nextNodeSub.prevNodeSub = prevNodeSub;
            prevNodeSub.nextNodeSub = nextNodeSub;
            prevNodeSub = null;
            nextNodeSub = null;
        }
    }

}
