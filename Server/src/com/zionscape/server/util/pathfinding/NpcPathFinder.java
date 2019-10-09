package com.zionscape.server.util.pathfinding;

import com.google.common.collect.Lists;
import com.zionscape.server.cache.Collision;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCHandler;

import java.util.*;

public class NpcPathFinder {

	/**
	 * The cost of moving in a straight line.
	 */
	private static final int COST_STRAIGHT = 10;

	public static boolean canMove(Location location, Move move, int size) {

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {

				//  if(npcAtLocation(location.getX() + x, location.getY() + y, location.getZ())) {
				//     return false;
				//  }

				switch (move) {

					case NORTH_EAST:
						if (Collision.blockedNorthEast(location.getX() + x, location.getY() + y, location.getZ())) {
							return false;
						}
						break;
					case SOUTH_EAST:
						if (Collision.blockedSouthEast(location.getX() + x, location.getY() + y, location.getZ())) {
							return false;
						}
						break;
					case SOUTH_WEST:
						if (Collision.blockedNorthWest(location.getX() + x, location.getY() + y, location.getZ())) {
							return false;
						}
						break;
					case NORTH_WEST:
						if (Collision.blockedNorthWest(location.getX() + x, location.getY() + y, location.getZ())) {
							return false;
						}
						break;
					case NORTH:
						if (Collision.blockedNorth(location.getX() + x, location.getY() + y, location.getZ())) {
							return false;
						}
						break;
					case SOUTH:
						if (Collision.blockedSouth(location.getX() + x, location.getY() + y, location.getZ())) {
							return false;
						}
						break;
					case EAST:
						if (Collision.blockedEast(location.getX() + x, location.getY() + y, location.getZ())) {
							return false;
						}
						break;
					case WEST:
						if (Collision.blockedWest(location.getX() + x, location.getY() + y, location.getZ())) {
							return false;
						}
						break;
				}
			}
		}
		return true;
	}

	public static Deque<Location> findPath(Location src, Location dst, CollisionMap map) {

		SortedList open = new SortedList();
		List<Node> closed = new ArrayList<>();

		open.add(new Node(src));

		Node current = null;
		int maxDepth = 0;
		while (maxDepth < 104 && open.size() != 0) {
			current = (Node) open.first();

			if (current.getLocation().equals(dst)) {
				Deque<Location> shortest = new ArrayDeque<>();
				while (current != null && current.parent != null) {
					Location location = current.getLocation();

					while (!src.equals(location)) {
						shortest.addFirst(location);
						current = current.parent;
						location = current.getLocation();
					}
				}
				return shortest;
			}

			open.remove(current);
			closed.add(current);


			List<Node> adjacentNodes = Lists.newArrayList();
			int x = current.getLocation().getX(), y = current.getLocation().getY();
			for (int nextX = x - 1; nextX <= x + 1; nextX++) {
				for (int nextY = y - 1; nextY <= y + 1; nextY++) {
					if (nextX == x && nextY == y) {
						continue;
					}

					Node node = new Node(Location.create(nextX, nextY, src.getZ()));

					boolean walkable = true;
					if (nextX > x && nextY > y && !map.blockedSouthWest(nextX, nextY)) { // north east
						walkable = false;
					}
					if (nextX > x && nextY < y && !map.blockedNorthWest(nextX, nextY)) { // south east
						walkable = false;
					}
					if (nextX < x && nextY < y && !map.blockedNorthEast(nextX, nextY)) { // south west
						walkable = false;
					}
					if (nextX < x && nextY > y && !map.blockedSouthEast(nextX, nextY)) { // north west
						walkable = false;
					}
					if (nextX > x && map.blockedWest(nextX, nextY)) {
						walkable = false;
					}
					if (nextX < x && map.blockedEast(nextX, nextY)) {
						walkable = false;
					}
					if (nextY > y && map.blockedSouth(nextX, nextY)) {
						walkable = false;
					}
					if (nextY < y && map.blockedNorth(nextX, nextY)) {
						walkable = false;
					}

					// npc tile clipping
					for (NPC npc : NPCHandler.npcs) {
						// evil magic numbers
						if (npc == null || npc.type != 14205) {
							continue;
						}
						if (npc.absX == nextX && npc.absY == nextY) {
							walkable = false;
						}
					}

					if (walkable) {
						adjacentNodes.add(node);
					}
				}
			}

			for (int i = 0; i < adjacentNodes.size(); i++) {
				Node adjacentNode = adjacentNodes.get(i);

				int nextStepCost = current.getCost() + estimateHeuristic(current.getLocation(), adjacentNode.getLocation());

				if (nextStepCost < adjacentNode.getCost()) {
					if (open.contains(adjacentNode)) {
						open.remove(adjacentNode);
					}
					if (closed.contains(adjacentNode)) {
						closed.remove(adjacentNode);
					}
				}

				if (!open.contains(adjacentNode) && !closed.contains(adjacentNode)) {
					adjacentNode.setCost(nextStepCost);
					adjacentNode.setHeuristic(estimateHeuristic(adjacentNode.getLocation(), dst));
					maxDepth = Math.max(maxDepth, adjacentNode.setParent(current));
					open.add(adjacentNode);
				}
			}
		}

		return null;
	}

	private static int estimateHeuristic(Location current, Location goal) {
		int dx = Math.abs(current.getX() - goal.getX());
		int dy = Math.abs(current.getY() - goal.getY());
		return (dx + dy) * COST_STRAIGHT;
	}

	public static boolean npcAtLocation(int x, int y, int z) {
		// npc tile clipping
		for (NPC npc : NPCHandler.npcs) {
			// evil magic numbers
			if (npc == null) {
				continue;
			}
			if (npc.absX == x && npc.absY == y && npc.heightLevel == z) {
				return true;
			}
		}
		return false;
	}

	public enum Move {
		NORTH,
		SOUTH,
		EAST,
		WEST,
		NORTH_EAST,
		SOUTH_EAST,
		SOUTH_WEST,
		NORTH_WEST
	}

	public static class Node implements Comparable {

		private Location location;
		private Node parent;
		private int cost;
		private int heuristic;
		private int depth;

		public Node(Location location) {
			this.location = location;
		}

		public Location getLocation() {
			return location;
		}

		@Override
		public boolean equals(Object obj) {

			if (obj instanceof Location) {
				return location.equals(obj);
			}

			if (obj instanceof Node) {
				return ((Node) obj).location.equals(location);
			}

			return super.equals(obj);
		}

		@Override
		public int hashCode() {
			return location.hashCode();
		}

		public Node getParent() {
			return parent;
		}

		public int setParent(Node parent) {
			depth = parent.depth + 1;
			this.parent = parent;

			return depth;
		}

		public int getCost() {
			return cost;
		}

		public void setCost(int cost) {
			this.cost = cost;
		}

		public int getHeuristic() {
			return heuristic;
		}

		public void setHeuristic(int heuristic) {
			this.heuristic = heuristic;
		}

		@Override
		public int compareTo(Object other) {
			Node o = (Node) other;

			float f = heuristic + cost;
			float of = o.heuristic + o.cost;

			if (f < of) {
				return -1;
			} else if (f > of) {
				return 1;
			} else {
				return 0;
			}
		}

	}

	private static class SortedList {
		private ArrayList list = new ArrayList<>();

		public Object first() {
			return list.get(0);
		}

		public void clear() {
			list.clear();
		}

		public void add(Object o) {
			list.add(o);
			Collections.sort(list);
		}

		public void remove(Object o) {
			list.remove(o);
		}

		public int size() {
			return list.size();
		}

		public boolean contains(Object o) {
			return list.contains(o);
		}
	}


}
