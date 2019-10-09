package com.zionscape.server.net.packets;

import com.google.common.base.MoreObjects;
import com.zionscape.server.cache.clientref.ObjectDef;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.MapObject;
import com.zionscape.server.model.content.Resting;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.region.RegionUtility;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Misc;
import com.zionscape.server.util.Stream;

import java.util.Optional;

/**
 * Click GameObject
 */
public class ClickObject implements PacketType {

    public static final int FIRST_CLICK = 132, SECOND_CLICK = 252, THIRD_CLICK = 70, FOURTH_CLICK = 228;

    @Override
    public void processPacket(Player c, int packetType, int packetSize, Stream stream) {
    	if (c.isDoingTutorial())
			return;
    	c.clickObjectType = c.objectX = c.objectId = c.objectY = 0;
        c.objectYOffset = c.objectXOffset = 0;
        Resting.stop(c);
        c.getPA().resetFollow();
        c.getCombat().resetPlayerAttack();
        c.getPA().requestUpdates();
        c.lampVfy = false;
        c.resetDialogue();

        if (c.isDoingTutorial()) {
            return;
        }
        if (c.onObstacle) {
            return;
        }

        switch (packetType) {
            case FIRST_CLICK:
                c.objectX = stream.readSignedWordBigEndianA();
                c.objectId = stream.readUnsignedWord();
                c.objectY = stream.readUnsignedWordA();

                // make sure the object exists
                int height = c.getLocation().getZ();
                if (height >= 4) {
                    height = height % 4;
                }

                if (c.rights >= 2) {
                    c.sendMessage(MoreObjects.toStringHelper(this).add("option", 1).add("object", c.objectId).add("x", c.objectX).add("y", c.objectY).toString());
                    // System.out.println("forthclick: objectId: " + c.objectId + "  ObjectX: " + c.objectX + "  objectY: " + c.objectY
                    //         + " Xoff: " + (c.getX() - c.objectX) + " Yoff: " + (c.getY() - c.objectY));
                }

                Optional<MapObject> obj = RegionUtility.getMapObject(c.objectId, Location.create(c.objectX, c.objectY, height));
                if (!obj.isPresent()) {
                    c.objectX = 0;
                    c.objectId = 0;
                    c.objectY = 0;
                    c.resetWalkingQueue();
                    return;
                }

                ObjectDef def = ObjectDef.forID(c.objectId);
                if (def == null) {
                    c.objectDistance = 1;
                } else {
                    c.objectDistance = def.width < def.height ? def.height : def.width;
                }

                if (Math.abs(c.getX() - c.objectX) > 25 || Math.abs(c.getY() - c.objectY) > 25) {
                    c.resetWalkingQueue();
                    c.sendMessage("Your destination is too far.");
                    break;
                }

                c.turnPlayerTo(c.objectX, c.objectY);

                switch (c.objectId) {
                    case 12568: //Stepping stone
                    case 12570: //Tropical tree
                    case 12573: //Monkeybars
                    case 12576: //Skull slope
                    case 12578: //Rope
                    case 12618: //Tropical tree
                    case 2295: //Log balance
                    case 2285: //Obstacle net
                    case 35970: //Tree branch
                    case 2312: //Balancing rope
                    case 2314: //Tree branch
                    case 2286: //Obstacle net
                    case 4058: //Obstacle pipe
                    case 154: //Obstacle pipe
                    //case 2282: //Ropeswing
                    case 2294: //Log balance
                    case 20211: //Obstacle net
                    case 2302: //Balancing ledge
                    case 3205: //Ladder
                    //case 20210: //Obstacle pipe
                    case 2288: //Obstacle pipe
                   // case 2283: //Ropeswing
                    case 37704: //Stepping stone
                    case 1: //Crate
                    case 2297: //Log balance
                    case 2328: //Rocks
                        c.objectDistance = 1;
                        break;

                   /* case 1948: // agility crumbling wall fix
                        if (c.objectX == 2542 && c.objectY == 3553 && c.absX > 2541) {
                            c.resetWalkingQueue();
                            return;
                        }
                        if (c.objectX == 2536 && c.objectY == 3553 && c.absX > 2535) {
                            c.resetWalkingQueue();
                            return;
                        }
                        if (c.objectX == 2539 && c.objectY == 3553 && c.absX > 2538) {
                            c.resetWalkingQueue();
                            return;
                        }
                        break;*/
                    case 25336:
                        c.objectDistance = 2;
                        break;
                    case 25339:
                        c.objectDistance = 2;
                        break;
                    case 23271:
                        c.objectDistance = 2;
                        break;
                    case 1733:
                    case 1738:
                        c.objectYOffset = 2;
                        break;
                    case 11666:
                        c.objectDistance = 2;
                        c.objectYOffset = 1;
                        c.objectXOffset = 1;
                        break;

                    case 5097:
                        c.objectDistance = 3;
                        break;
                    case 3044:
                        c.objectDistance = 3;
                        break;
                    case 245:
                        c.objectYOffset = -1;
                        c.objectDistance = 0;
                        break;
                    case 272:
                        c.objectYOffset = 1;
                        c.objectDistance = 0;
                        break;
                    case 273:
                        c.objectYOffset = 1;
                        c.objectDistance = 0;
                        break;
                    case 246:
                        c.objectYOffset = 1;
                        c.objectDistance = 0;
                        break;
                    case 4493:
                    case 4494:
                    case 4496:
                    case 4495:
                        c.objectDistance = 5;
                        break;
                    case 10229:
                    case 6522:
                        c.objectDistance = 2;
                        break;
                    case 8959:
                        c.objectYOffset = 1;
                        break;
                    case 2561:
                        c.getThieving().stealFromStall(1897, 10, 1);
                        break;
                    case 2560:
                        c.getThieving().stealFromStall(950, 30, 25);
                        break;
                    case 2562:
                        c.getThieving().stealFromStall(1635, 60, 50);
                        break;
                    case 2563:
                        c.getThieving().stealFromStall(7650, 100, 75);
                        break;
                    case 2564:
                        c.getThieving().stealFromStall(1613, 170, 90);
                        break;
                    case 4417:
                        if (c.objectX == 2425 && c.objectY == 3074) {
                            c.objectYOffset = 2;
                        }
                        break;
                    case 4420:
                        if (c.getX() >= 2383 && c.getX() <= 2385) {
                            c.objectYOffset = 1;
                        } else {
                            c.objectYOffset = -2;
                        }
                    case 6552:
                    case 409:
                    case 13641:
                        c.objectDistance = 2;
                        break;
                    case 2879:
                    case 2878:
                        c.objectDistance = 3;
                        break;
                    case 2558:
                        c.objectDistance = 0;
                        if (c.absX > c.objectX && c.objectX == 3044) {
                            c.objectXOffset = 1;
                        }
                        if (c.absY > c.objectY) {
                            c.objectYOffset = 1;
                        }
                        if (c.absX < c.objectX && c.objectX == 3038) {
                            c.objectXOffset = -1;
                        }
                        break;
                    case 9356:
                        c.objectDistance = 2;
                        break;
                    case 5959:
                    case 1815:
                    case 5960:
                    case 1816:
                        c.objectDistance = 0;
                        break;
                    case 9293:
                        c.objectDistance = 2;
                        break;
                    case 4418:
                        if (c.objectX == 2374 && c.objectY == 3131) {
                            c.objectYOffset = -2;
                        } else if (c.objectX == 2369 && c.objectY == 3126) {
                            c.objectXOffset = 2;
                        } else if (c.objectX == 2380 && c.objectY == 3127) {
                            c.objectYOffset = 2;
                        } else if (c.objectX == 2369 && c.objectY == 3126) {
                            c.objectXOffset = 2;
                        } else if (c.objectX == 2374 && c.objectY == 3131) {
                            c.objectYOffset = -2;
                        }
                        break;
                    case 4419:
                    case 6707: // verac
                        c.objectYOffset = 3;
                        break;
                    case 6823:
                        c.objectDistance = 2;
                        c.objectYOffset = 1;
                        break;
                    case 6706: // torag
                        c.objectXOffset = 2;
                        break;
                    case 6772:
                        c.objectDistance = 2;
                        c.objectYOffset = 1;
                        break;
                    case 6705: // karils
                        c.objectYOffset = -1;
                        break;
                    case 6822:
                        c.objectDistance = 2;
                        c.objectYOffset = 1;
                        break;
                    case 6704: // guthan stairs
                        c.objectYOffset = -1;
                        break;
                    case 6773:
                        c.objectDistance = 2;
                        c.objectXOffset = 1;
                        c.objectYOffset = 1;
                        break;
                    case 6703: // dharok stairs
                        c.objectXOffset = -1;
                        break;
                    case 6771:
                        c.objectDistance = 2;
                        c.objectXOffset = 1;
                        c.objectYOffset = 1;
                        break;
                    case 6702: // ahrim stairs
                        c.objectXOffset = -1;
                        break;
                    case 1276:
                    case 1278:// trees
                    case 1281: // oak
                    case 1308: // willow
                    case 1307: // maple
                    case 1309: // yew
                    case 1306: // yew
                        c.objectDistance = 3;
                        break;
                    case 26303:
                        c.objectYOffset = 8;
                        break;
            /*
			 * default: c.objectDistance = 1; c.objectXOffset = 0; c.objectYOffset = 0; break;
			 */
                }

                if (Math.floor(c.getLocation().getDistance(Location.create(c.objectX, c.objectY))) <= c.objectDistance) {
                    c.getActions().firstClickObject(c.objectId, c.objectX, c.objectY);
                } else {
                    c.clickObjectType = 1;
                }
                break;
            case SECOND_CLICK:

                c.objectId = stream.readUnsignedWordBigEndianA();
                c.objectY = stream.readSignedWordBigEndian();
                c.objectX = stream.readUnsignedWordA();

                height = c.getLocation().getZ();
                if (height >= 4) {
                    height = height % 4;
                }

                if (c.rights >= 3) {
                    c.sendMessage(MoreObjects.toStringHelper(this).add("option", 2).add("object", c.objectId).add("x", c.objectX).add("y", c.objectY).toString());
                    // System.out.println("forthclick: objectId: " + c.objectId + "  ObjectX: " + c.objectX + "  objectY: " + c.objectY
                    //         + " Xoff: " + (c.getX() - c.objectX) + " Yoff: " + (c.getY() - c.objectY));
                }

                // make sure the object exists
                obj = RegionUtility.getMapObject(c.objectId, Location.create(c.objectX, c.objectY, height));
                if (!obj.isPresent()) {
                    c.objectX = 0;
                    c.objectId = 0;
                    c.objectY = 0;
                    c.resetWalkingQueue();
                    return;
                }


                def = ObjectDef.forID(c.objectId);
                if (def == null) {
                    c.objectDistance = 1;
                } else {
                    c.objectDistance = def.width < def.height ? def.height : def.width;
                }

                c.turnPlayerTo(c.objectX, c.objectY);

			/*
			 * switch (c.objectId) { case 6163: case 6165: case 6166: case 6164: case 6162: case 15683: c.objectDistance
			 * = 2; break; default: c.objectDistance = 1; c.objectXOffset = 0; c.objectYOffset = 0; break; }
			 */

                if (Math.floor(c.getLocation().getDistance(Location.create(c.objectX, c.objectY))) <= c.objectDistance) {
                    c.getActions().secondClickObject(c.objectId, c.objectX, c.objectY);
                } else {
                    c.clickObjectType = 2;
                }
                break;
            case THIRD_CLICK:
                c.objectX = stream.readSignedWordBigEndian();
                c.objectY = stream.readUnsignedWord();
                c.objectId = stream.readUnsignedWordBigEndianA();

                // make sure the object exists
                height = c.getLocation().getZ();
                if (height >= 4) {
                    height = height % 4;
                }

                if (c.rights >= 3) {
                    c.sendMessage(MoreObjects.toStringHelper(this).add("option", 3).add("object", c.objectId).add("x", c.objectX).add("y", c.objectY).toString());
                    // System.out.println("forthclick: objectId: " + c.objectId + "  ObjectX: " + c.objectX + "  objectY: " + c.objectY
                    //         + " Xoff: " + (c.getX() - c.objectX) + " Yoff: " + (c.getY() - c.objectY));
                }

                obj = RegionUtility.getMapObject(c.objectId, Location.create(c.objectX, c.objectY, height));
                if (!obj.isPresent()) {
                    c.objectX = 0;
                    c.objectId = 0;
                    c.objectY = 0;
                    c.resetWalkingQueue();
                    return;
                }

                def = ObjectDef.forID(c.objectId);
                if (def == null) {
                    c.objectDistance = 1;
                } else {
                    c.objectDistance = def.width < def.height ? def.height : def.width;
                }

                int xDiff = Math.abs(c.absX - c.objectX);
                int yDiff = Math.abs(c.absY - c.objectY);
                if (c.objectId == 38012 && c.objectX == 2839 && c.objectY == 3537 && xDiff <= 2 && yDiff <= 2) {
                    c.getPA().movePlayer(c.absX, c.absY, c.heightLevel - 1);
                }

                c.turnPlayerTo(c.objectX, c.objectY);

                switch (c.objectId) {
                    default:
                        c.objectDistance = 1;
                        c.objectXOffset = 0;
                        c.objectYOffset = 0;
                        break;
                }
                if (Math.floor(c.getLocation().getDistance(Location.create(c.objectX, c.objectY))) <= c.objectDistance) {
                    c.getActions().thirdClickObject(c.objectId, c.objectX, c.objectY);
                } else {
                    c.clickObjectType = 3;
                }
                break;

            case FOURTH_CLICK:
                c.objectId = stream.readSignedWordA();
                c.objectY = stream.readSignedWordA();
                c.objectX = stream.readUnsignedWord();

                // make sure the object exists
                height = c.getLocation().getZ();
                if (height >= 4) {
                    height = height % 4;
                }

                if (c.rights >= 3) {
                    c.sendMessage(MoreObjects.toStringHelper(this).add("option", 4).add("object", c.objectId).add("x", c.objectX).add("y", c.objectY).toString());
                    // System.out.println("forthclick: objectId: " + c.objectId + "  ObjectX: " + c.objectX + "  objectY: " + c.objectY
                    //         + " Xoff: " + (c.getX() - c.objectX) + " Yoff: " + (c.getY() - c.objectY));
                }

                obj = RegionUtility.getMapObject(c.objectId, Location.create(c.objectX, c.objectY, height));
                if (!obj.isPresent()) {
                    c.objectX = 0;
                    c.objectId = 0;
                    c.objectY = 0;
                    c.resetWalkingQueue();
                    return;
                }

                def = ObjectDef.forID(c.objectId);
                if (def == null) {
                    c.objectDistance = 1;
                } else {
                    c.objectDistance = def.width < def.height ? def.height : def.width;
                }

                c.turnPlayerTo(c.objectX, c.objectY);

                switch (c.objectId) {
                    default:
                        c.objectDistance = 1;
                        c.objectXOffset = 0;
                        c.objectYOffset = 0;
                        break;
                }
                if (c.goodDistance(c.objectX + c.objectXOffset, c.objectY + c.objectYOffset, c.getX(), c.getY(), c.objectDistance)) {
                    c.getActions().secondClickObject(c.objectId, c.objectX, c.objectY);
                } else {
                    c.clickObjectType = 4;
                }

                break;
        }
    }

}