package net.crandor;

public final class OnDemandData extends NodeSub {

	int dataType;
	byte buffer[];
	int ID;
	boolean incomplete = true;
	int loopCycle;
}
