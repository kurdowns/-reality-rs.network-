package com.zionscape.server.util.pathfinding;

import com.zionscape.server.cache.Collision;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.players.Player;

import java.util.LinkedList;

public class PlayerPathFinder {

	private static final PlayerPathFinder PLAYER_PATH_FINDER = new PlayerPathFinder();

	public PlayerPathFinder() {
	}

	public static PlayerPathFinder getPlayerPathFinder() {
		return PLAYER_PATH_FINDER;
	}

	public static boolean canMove(int x, int y, int height, int moveTypeX, int moveTypeY) {
		if (height > 3) {
			height = 0;
		}
		// Region r = region;//West
		// East
		if (moveTypeX == 1 && moveTypeY == 0)// moveTypeX == -1 && moveTypeY == 0)
		{
			return (Collision.getClipping(x, y, height) & 0x1280108) == 0
					&& (Collision.getClipping(x + 1, y, height) & 0x1280180) == 0;
		} else
			// West
			if (moveTypeX == -1 && moveTypeY == 0)// moveTypeX == 1 && moveTypeY == 0)
			{
				return (Collision.getClipping(x, y, height) & 0x1280180) == 0
						&& (Collision.getClipping(x - 1, y, height) & 0x1280108) == 0;
			} else
				// North
				if (moveTypeX == 0 && moveTypeY == 1)// moveTypeX == 0 && moveTypeY == -1)
				{
					return (Collision.getClipping(x, y, height) & 0x1280102) == 0
							&& (Collision.getClipping(x, y + 1, height) & 0x1280120) == 0;
				} else
					// South
					if (moveTypeX == 0 && moveTypeY == -1)// moveTypeX == 0 && moveTypeY == 1)
					{
						return (Collision.getClipping(x, y, height) & 0x1280120) == 0
								&& (Collision.getClipping(x, y - 1, height) & 0x1280102) == 0;
					} else
						// NorthEast
						if (moveTypeX == 1 && moveTypeY == 1) {
							return (Collision.getClipping(x, y, height) & 0x1280102) == 0// Check if can go north.
									&& (Collision.getClipping(x, y + 1, height) & 0x1280120) == 0
									&& (Collision.getClipping(x, y, height) & 0x1280108) == 0// Check if can go East
									&& (Collision.getClipping(x + 1, y, height) & 0x1280180) == 0
									&& (Collision.getClipping(x, y + 1, height) & 0x1280108) == 0// Check if can go East from North
									&& (Collision.getClipping(x + 1, y + 1, height) & 0x1280180) == 0
									&& (Collision.getClipping(x + 1, y, height) & 0x1280102) == 0// Check if can go North from East
									&& (Collision.getClipping(x + 1, y + 1, height) & 0x1280120) == 0;
						} else
							// NorthWest
							if (moveTypeX == -1 && moveTypeY == 1) {
								return (Collision.getClipping(x, y, height) & 0x1280102) == 0// Going North
										&& (Collision.getClipping(x, y + 1, height) & 0x1280120) == 0
										&& (Collision.getClipping(x, y, height) & 0x1280180) == 0// Going West
										&& (Collision.getClipping(x - 1, y, height) & 0x1280108) == 0
										&& (Collision.getClipping(x, y + 1, height) & 0x1280180) == 0// Going West from North
										&& (Collision.getClipping(x - 1, y + 1, height) & 0x1280108) == 0
										&& (Collision.getClipping(x - 1, y, height) & 0x1280102) == 0// Going West
										&& (Collision.getClipping(x - 1, y + 1, height) & 0x1280120) == 0;
							} else
								// SouthEast
								if (moveTypeX == 1 && moveTypeY == -1) {
									return (Collision.getClipping(x, y, height) & 0x1280120) == 0// Going South
											&& (Collision.getClipping(x, y - 1, height) & 0x1280102) == 0
											&& (Collision.getClipping(x, y, height) & 0x1280108) == 0// Going East
											&& (Collision.getClipping(x + 1, y, height) & 0x1280180) == 0
											&& (Collision.getClipping(x, y - 1, height) & 0x1280108) == 0// Going East from South
											&& (Collision.getClipping(x + 1, y - 1, height) & 0x1280180) == 0
											&& (Collision.getClipping(x + 1, y, height) & 0x1280120) == 0// Going East
											&& (Collision.getClipping(x + 1, y - 1, height) & 0x1280102) == 0;
								} else
									// SouthWest
									if (moveTypeX == -1 && moveTypeY == -1) {
										return (Collision.getClipping(x, y, height) & 0x1280120) == 0// Going North
												&& (Collision.getClipping(x, y - 1, height) & 0x1280102) == 0
												&& (Collision.getClipping(x, y, height) & 0x1280180) == 0// Going West
												&& (Collision.getClipping(x - 1, y, height) & 0x1280108) == 0
												&& (Collision.getClipping(x, y - 1, height) & 0x1280180) == 0// Going West from North
												&& (Collision.getClipping(x - 1, y - 1, height) & 0x1280108) == 0
												&& (Collision.getClipping(x - 1, y, height) & 0x1280120) == 0// Going West
												&& (Collision.getClipping(x - 1, y - 1, height) & 0x1280102) == 0;
									}
		return false;
	}

	public void findRoute(Player c, int destX, int destY, boolean moveNear, int xLength, int yLength) {
		if (destX == c.getLocation().getLocalX() && destY == c.getLocation().getLocalY() && !moveNear) {
			c.sendMessage("ERROR!");
			return;
		}
		destX = destX - 8 * c.getLocation().getRegionX();
		destY = destY - 8 * c.getLocation().getRegionY();
		int[][] via = new int[104][104];
		int[][] cost = new int[104][104];
		LinkedList<Integer> tileQueueX = new LinkedList<Integer>();
		LinkedList<Integer> tileQueueY = new LinkedList<Integer>();
		for (int xx = 0; xx < 104; xx++) {
			for (int yy = 0; yy < 104; yy++) {
				cost[xx][yy] = 99999999;
			}
		}
		int curX = c.getLocation().getLocalX();
		int curY = c.getLocation().getLocalY();
		via[curX][curY] = 99;
		cost[curX][curY] = 0;
		int tail = 0;
		tileQueueX.add(curX);
		tileQueueY.add(curY);
		boolean foundPath = false;
		int pathLength = 4000;
		while (tail != tileQueueX.size() && tileQueueX.size() < pathLength) {
			curX = tileQueueX.get(tail);
			curY = tileQueueY.get(tail);
			int curAbsX = c.getLocation().getRegionX() * 8 + curX;
			int curAbsY = c.getLocation().getRegionY() * 8 + curY;
			if (curX == destX && curY == destY) {
				foundPath = true;
				break;
			}
			tail = (tail + 1) % pathLength;
			int thisCost = cost[curX][curY] + 1;
			if (curY > 0
					&& via[curX][curY - 1] == 0
					&& (Collision.getClipping(curAbsX, curAbsY - 1, c.heightLevel) & 0x1280102) == 0) {
				tileQueueX.add(curX);
				tileQueueY.add(curY - 1);
				via[curX][curY - 1] = 1;
				cost[curX][curY - 1] = thisCost;
			}
			if (curX > 0
					&& via[curX - 1][curY] == 0
					&& (Collision.getClipping(curAbsX - 1, curAbsY, c.heightLevel) & 0x1280108) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY);
				via[curX - 1][curY] = 2;
				cost[curX - 1][curY] = thisCost;
			}
			if (curY < 104 - 1
					&& via[curX][curY + 1] == 0
					&& (Collision.getClipping(curAbsX, curAbsY + 1, c.heightLevel) & 0x1280120) == 0) {
				tileQueueX.add(curX);
				tileQueueY.add(curY + 1);
				via[curX][curY + 1] = 4;
				cost[curX][curY + 1] = thisCost;
			}
			if (curX < 104 - 1
					&& via[curX + 1][curY] == 0
					&& (Collision.getClipping(curAbsX + 1, curAbsY, c.heightLevel) & 0x1280180) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY);
				via[curX + 1][curY] = 8;
				cost[curX + 1][curY] = thisCost;
			}
			if (curX > 0
					&& curY > 0
					&& via[curX - 1][curY - 1] == 0
					&& (Collision.getClipping(curAbsX - 1, curAbsY - 1,
					c.heightLevel) & 0x128010e) == 0
					&& (Collision.getClipping(curAbsX - 1, curAbsY, c.heightLevel) & 0x1280108) == 0
					&& (Collision.getClipping(curAbsX, curAbsY - 1, c.heightLevel) & 0x1280102) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY - 1);
				via[curX - 1][curY - 1] = 3;
				cost[curX - 1][curY - 1] = thisCost;
			}
			if (curX > 0
					&& curY < 104 - 1
					&& via[curX - 1][curY + 1] == 0
					&& (Collision.getClipping(curAbsX - 1, curAbsY + 1,
					c.heightLevel) & 0x1280138) == 0
					&& (Collision.getClipping(curAbsX - 1, curAbsY, c.heightLevel) & 0x1280108) == 0
					&& (Collision.getClipping(curAbsX, curAbsY + 1, c.heightLevel) & 0x1280120) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY + 1);
				via[curX - 1][curY + 1] = 6;
				cost[curX - 1][curY + 1] = thisCost;
			}
			if (curX < 104 - 1
					&& curY > 0
					&& via[curX + 1][curY - 1] == 0
					&& (Collision.getClipping(curAbsX + 1, curAbsY - 1,
					c.heightLevel) & 0x1280183) == 0
					&& (Collision.getClipping(curAbsX + 1, curAbsY, c.heightLevel) & 0x1280180) == 0
					&& (Collision.getClipping(curAbsX, curAbsY - 1, c.heightLevel) & 0x1280102) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY - 1);
				via[curX + 1][curY - 1] = 9;
				cost[curX + 1][curY - 1] = thisCost;
			}
			if (curX < 104 - 1
					&& curY < 104 - 1
					&& via[curX + 1][curY + 1] == 0
					&& (Collision.getClipping(curAbsX + 1, curAbsY + 1,
					c.heightLevel) & 0x12801e0) == 0
					&& (Collision.getClipping(curAbsX + 1, curAbsY, c.heightLevel) & 0x1280180) == 0
					&& (Collision.getClipping(curAbsX, curAbsY + 1, c.heightLevel) & 0x1280120) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY + 1);
				via[curX + 1][curY + 1] = 12;
				cost[curX + 1][curY + 1] = thisCost;
			}
		}
		if (!foundPath) {
			if (moveNear) {
				int i_223_ = 1000;
				int thisCost = 100;
				int i_225_ = 10;
				for (int x = destX - i_225_; x <= destX + i_225_; x++) {
					for (int y = destY - i_225_; y <= destY + i_225_; y++) {
						if (x >= 0 && y >= 0 && x < 104 && y < 104
								&& cost[x][y] < 100) {
							int i_228_ = 0;
							if (x < destX) {
								i_228_ = destX - x;
							} else if (x > destX + xLength - 1) {
								i_228_ = x - (destX + xLength - 1);
							}
							int i_229_ = 0;
							if (y < destY) {
								i_229_ = destY - y;
							} else if (y > destY + yLength - 1) {
								i_229_ = y - (destY + yLength - 1);
							}
							int i_230_ = i_228_ * i_228_ + i_229_ * i_229_;
							if (i_230_ < i_223_ || i_230_ == i_223_
									&& cost[x][y] < thisCost) {
								i_223_ = i_230_;
								thisCost = cost[x][y];
								curX = x;
								curY = y;
							}
						}
					}
				}
				if (i_223_ == 1000) {
					return;
				}
			} else {
				return;
			}
		}
		tail = 0;
		tileQueueX.set(tail, curX);
		tileQueueY.set(tail++, curY);
		int l5;
		for (int j5 = l5 = via[curX][curY]; curX != c.getLocation().getLocalX()
				|| curY != c.getLocation().getLocalY(); j5 = via[curX][curY]) {
			if (j5 != l5) {
				l5 = j5;
				tileQueueX.set(tail, curX);
				tileQueueY.set(tail++, curY);
			}
			if ((j5 & 2) != 0) {
				curX++;
			} else if ((j5 & 8) != 0) {
				curX--;
			}
			if ((j5 & 1) != 0) {
				curY++;
			} else if ((j5 & 4) != 0) {
				curY--;
			}
		}

		c.stopMovement();

		int size = tail--;
		int pathX = c.getLocation().getRegionX() * 8 + tileQueueX.get(tail);
		int pathY = c.getLocation().getRegionY() * 8 + tileQueueY.get(tail);

		c.getWalkingQueue().addStep(Location.create(pathX, pathY), false);

		for (int i = 1; i < size; i++) {
			tail--;
			pathX = c.getLocation().getRegionX() * 8 + tileQueueX.get(tail);
			pathY = c.getLocation().getRegionY() * 8 + tileQueueY.get(tail);
			c.getWalkingQueue().addStep(Location.create(pathX, pathY), false);
		}
	}

	public int localize(int x, int mapRegion) {
		return x - 8 * mapRegion;
	}
}