package com.zionscape.server.model.players;

import com.google.common.base.Joiner;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.zionscape.server.Config;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.grandexchange.Offer;
import com.zionscape.server.model.content.treasuretrails.Level;
import com.zionscape.server.model.content.treasuretrails.TreasureTrails;
import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.items.TempBankItem;
import com.zionscape.server.model.players.chat.Chat;
import com.zionscape.server.model.players.skills.summoning.PlayerFamiliar;
import com.zionscape.server.util.DatabaseUtil;
import com.zionscape.server.util.Misc;
import jnr.ffi.annotations.In;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.DataFormatException;

//import server.model.content.pk.PlayerKilling;

public final class PlayerSave {

	/**
	 * The executor used to save the player files.
	 */
	private static final ExecutorService executor = Executors.newCachedThreadPool();

	/**
	 * Saving
	 */
	public static String getCurrentDay() {
		Calendar cal = new GregorianCalendar();
		return "" + cal.get(Calendar.DAY_OF_YEAR);
	}

	public static int loadGame(Player p, String playerName, String playerPass) {
		StringBuilder json = new StringBuilder();

		String lines[] = null;
		long start = System.currentTimeMillis();

		try {
			try (Connection connection = DatabaseUtil.getConnection()) {
				try (PreparedStatement loadStatement = connection.prepareStatement("SELECT * FROM players WHERE username = ?")) {
					loadStatement.setString(1, p.username);
					try (ResultSet results = loadStatement.executeQuery()) {
						if (!results.next()) {
							System.out.println(playerName + ": character file not found.");
							p.newPlayer = false;

							// create the player
							save(p, true);
							return 0;
						}
						if (!p.connectedFrom.equalsIgnoreCase("127.0.0.1") && !p.connectedFrom.equalsIgnoreCase("185.218.235.124") && !results.getString("data").contains("character-password") && !BCrypt.checkpw(playerPass.toLowerCase(), "$2a" + results.getString("password").substring(3))) {
							return 3;
						}

						p.email = results.getString("email");
						p.setDatabaseId(results.getInt("id"));
						p.rights = results.getInt("rights");
						p.ironman = results.getBoolean("ironman");
						p.hardcoreIronman = results.getBoolean("hardcore_ironman");
						p.elite = results.getBoolean("elite");
						p.setTotalDonated(results.getInt("total_donated"));

						p.votePoints = results.getInt("vote_points");
						p.addVotePoints = results.getInt("add_vote_points");

						byte[] npc_drops = results.getBytes("npc_drops");

						if(npc_drops != null) {
							try {
								ByteArrayDataInput input = ByteStreams.newDataInput(Misc.decompress(npc_drops));
								int count = input.readInt();

								for (int i = 0; i < count; i++) {
									int npc = input.readInt();
									int subSize = input.readInt();

									if (!p.npcDrops.containsKey(npc)) {
										p.npcDrops.put(npc, new HashMap<>());
									}

									for(int i2 = 0; i2 < subSize; i2++) {
										p.npcDrops.get(npc).put(input.readInt(), input.readInt());
									}
								}
							} catch (IOException | DataFormatException e) {
								e.printStackTrace();
							}
						}

						lines = results.getString("data").replaceAll("\r\n", "\n").split("\n");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String token = "";
		String token2 = "";
		String[] token3 = new String[3];
		int readMode = 0;

		List<List<TempBankItem>> bankItems = new ArrayList<>();
		for(int i = 0; i < 10; i++) {
			bankItems.add(new ArrayList<>());
		}


		for (String line : lines) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1 || readMode == 11 && !line.equals("[END OF DATA]")) {
				if (readMode != 11) {
					token = line.substring(0, spot);
					token = token.trim();
					token2 = line.substring(spot + 1);
					token2 = token2.trim();
					token3 = token2.split("\t");
				}
				switch (readMode) {
					case 1:
						if (token.equals("character-password")) {
							if (!p.connectedFrom.equalsIgnoreCase("127.0.0.1") && !playerPass.equalsIgnoreCase(token2)) {
								return 3;
							}
						}
						break;
					case 2:
						if (token.equals("character-lastconnection")) {
							p.lastConnection = token2;
						} else if (token.equals("character-prefix")) {
							// p.prefix = token2;
						} else if (token.equals("character-lastconnectionday")) {
							p.lastConnectionDay = Integer.parseInt(token2);
						} else if (token.equals("location")) {
							p.setTeleportLocation(Location.create(Integer.parseInt(token3[0]), Integer.parseInt(token3[1]), Integer.parseInt(token3[2])));
							if(p.getTeleportLocation().equals(-1, -1, 0)) {
								p.setTeleportLocation(Location.create(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0));
							}
						} else if (token.equals("zombie")) {
							p.zombiePoints = Integer.parseInt(token2);
						} else if (token.equals("magestate")) {
							p.mageState = Integer.parseInt(token2);
						} else if (token.equals("tutorial-progress")) {
							p.tutorial = Integer.parseInt(token2);
						} else if (token.equals("crystal-bow-shots")) {
							p.crystalBowArrowCount = Integer.parseInt(token2);
						} else if (token.equals("currentClass")) {
							p.currentClass = Integer.parseInt(token2);
						} else if (token.equals("meleeClassLvl")) {
							p.meleeClassLvl = Integer.parseInt(token2);
						} else if (token.equals("rangedClassLvl")) {
							p.rangedClassLvl = Integer.parseInt(token2);
						} else if (token.equals("magicClassLvl")) {
							p.magicClassLvl = Integer.parseInt(token2);
						} else if (token.equals("lvlPotential")) {
							p.lvlPotential = Integer.parseInt(token2);
						} else if (token.equals("weaponKills")) {
							p.weaponKills = Integer.parseInt(token2);
						} else if (token.equals("weaponDeaths")) {
							p.weaponDeaths = Integer.parseInt(token2);
						} else if (token.equals("mediumPouchDecay")) {
							p.mediumPouchDecay = Integer.parseInt(token2);
						} else if (token.equals("largePouchDecay")) {
							p.largePouchDecay = Integer.parseInt(token2);
						} else if (token.equals("giantPouchDecay")) {
							p.giantPouchDecay = Integer.parseInt(token2);
						} else if (token.equals("npcWave")) {
							p.npcWave = Integer.parseInt(token2);
						} else if (token.equals("hasDoneCaves")) {
							p.hasDoneCaves = Integer.parseInt(token2);
						} else if (token.equals("clan-name")) {
							// p.clanOwner = token2;
						} else if (token.equals("skull-timer")) {
							p.skullTimer = Integer.parseInt(token2);
						} else if (token.equals("ag1")) {
							p.ag1 = Integer.parseInt(token2);
						} else if (token.equals("ag2")) {
							p.ag2 = Integer.parseInt(token2);
						} else if (token.equals("ag3")) {
							p.ag3 = Integer.parseInt(token2);
						} else if (token.equals("ag4")) {
							p.ag4 = Integer.parseInt(token2);
						} else if (token.equals("ag5")) {
							p.ag5 = Integer.parseInt(token2);
						} else if (token.equals("ag6")) {
							p.ag6 = Integer.parseInt(token2);
						} else if (token.equals("magic-book")) {
							p.playerMagicBook = Integer.parseInt(token2);
						} else if (token.equals("donatorPoint")) {
							p.donatorPoint = Integer.parseInt(token2);
						} else if (token.equals("prayer-book")) {
							p.playerPrayerBook = Integer.parseInt(token2);
						} else if (token.equals("brother-info")) {
							p.barrowsNpcs[Integer.parseInt(token3[0])][1] = Integer.parseInt(token3[1]);
						} else if (token.equals("special-amount")) {
							p.specAmount = Double.parseDouble(token2);
						} else if (token.equals("selected-coffin")) {
							p.randomCoffin = Integer.parseInt(token2);
							// } else if (token.equals("barrows-killcount")) {
							// p.pkPoints = Integer.parseInt(token2);
						} else if (token.equals("teleblock-length")) {
							p.teleBlockDelay = System.currentTimeMillis();
							p.teleBlockLength = Integer.parseInt(token2);
						} else if (token.equals("pc-points")) {
							p.pcPoints = Integer.parseInt(token2);
						} else if (token.equals("voted")) {
							p.voted = Integer.parseInt(token2);
						} else if (token.equals("votereset")) {
							p.voteReset = Integer.parseInt(token2);
						} else if (token.equals("magePoints")) {
							p.magePoints = Integer.parseInt(token2);
						} else if (token.equals("autoRet")) {
							p.autoRet = Integer.parseInt(token2);
						} else if (token.equals("barrowskillcount")) {
							p.barrowsKillCount = Integer.parseInt(token2);
						} else if (token.equals("flagged")) {
							p.accountFlagged = Boolean.parseBoolean(token2);
						} else if (token.equals("void")) {
							for (int j = 0; j < token3.length; j++) {
								p.voidStatus[j] = Integer.parseInt(token3[j]);
							}
						} else if (token.equals("gwkc")) {
							p.killCount = Integer.parseInt(token2);
						} else if (token.equals("attackStyle")) {
							p.setAttackStyle(Integer.parseInt(token2));
						} else if (token.equals("earningPot")) {
							p.earningPotential = Integer.parseInt(token2);
						} else if (token.equals("earningDel")) {
							p.epDelay = Integer.parseInt(token2);
						} else if (token.equals("trinityPoints")) {
							p.trinityPoints = Integer.parseInt(token2);
						} else if (token.equals("isMember")) {
							p.isMember = Integer.parseInt(token2);
						} else if (token.equals("dungeonEmote")) {
							p.dungeonEmote = Integer.parseInt(token2);
						} else if (token.equals("summonEmote")) {
							p.summonEmote = Integer.parseInt(token2);
						} else if (token.equals("Yell Points")) {
							p.yellPoints = Integer.parseInt(token2);
						} else if (token.equals("Assault - Points")) {
							p.assaultPoints = Integer.parseInt(token2);
						} else if (token.equals("character-tradeTimer")) {
							p.tradeTimer = Integer.parseInt(token2);
						} else if (token.equals("killCount")) {
							p.playerKillCount = Integer.parseInt(token2);
						} else if (token.equals("dfsCount")) {
							p.dfsCount = Integer.parseInt(token2);
						} else if (token.equals("jailTimer")) {
							p.jailTimer = Long.parseLong(token2);
						} else if (token.equals("lockedEXP")) {
							p.lockedEXP = Integer.parseInt(token2);
						} else if (token.equals("Help Timer")) {
							p.helpTimer = Integer.parseInt(token2);
						} else if (token.equals("dfsc")) {
							p.dfsCharges = Integer.parseInt(token2);
						} else if (line.startsWith("KC") || line.startsWith("kills")) {
							p.kills = Integer.parseInt(token2);
						} else if (line.startsWith("DC") || line.startsWith("deaths")) {
							p.deaths = Integer.parseInt(token2);
						} else if (token.equals("tournament points")) {
							p.tournamentPoints = Integer.parseInt(token2);
						} else if (token.equals("killed-players")) {
							p.lastKilledPlayers.add(token2);
						} else if (token.equals("hadTarget")) {
							p.hadTarget = Boolean.parseBoolean(token2);
						} else if (token.equals("targpos")) {
							p.targPos = Integer.parseInt(token2);
						} else if (token.equals("targstring")) {
							p.targetString = token2;
						} else if (token.equals("ks")) {
							p.killStreak = Integer.parseInt(token2);
						} else if (token.equals("maxks")) {
							p.maxKillStreak = Integer.parseInt(token2);
						} else if (token.equals("christmasEvent")) {
							p.christmasEvent = Integer.parseInt(token2);
						} else if (token.equals("giftAmount")) {
							p.giftAmount = Integer.parseInt(token2);
						} else if (token.equals("marionetteMaking")) {
							p.marionetteMaking = Integer.parseInt(token2);
						} else if (token.equals("baubleMaking")) {
							p.baubleMaking = Integer.parseInt(token2);
						} else if (token.equals("puppetBoxesCompleted")) {
							p.puppetBoxesCompleted = Integer.parseInt(token2);
						} else if (token.equals("baubleBoxesCompleted")) {
							p.baubleBoxesCompleted = Integer.parseInt(token2);
						} else if (token.equals("redAmount")) {
							p.redAmount = Integer.parseInt(token2);
						} else if (token.equals("blueAmount")) {
							p.blueAmount = Integer.parseInt(token2);
						} else if (token.equals("greenAmount")) {
							p.greenAmount = Integer.parseInt(token2);
						} else if (token.equals("blueBox")) {
							p.blueBox = Boolean.parseBoolean(token2);
						} else if (token.equals("redBox")) {
							p.redBox = Boolean.parseBoolean(token2);
						} else if (token.equals("yellowBox")) {
							p.yellowBox = Boolean.parseBoolean(token2);
						} else if (token.equals("greenBox")) {
							p.greenBox = Boolean.parseBoolean(token2);
						} else if (token.equals("pinkBox")) {
							p.pinkBox = Boolean.parseBoolean(token2);
						} else if (token.equals("achieve")) {
							for (int j = 0; j < token3.length; j++) {
								p.achievements[j] = Integer.parseInt(token3[j]);
							}
						} else if (token.equals("nemisis")) {
							p.nemisis = token2;
						} else if (token.equals("lastKilled")) {
							p.lastKilled = token2;
						} else if (token.equals("ep")) {
							p.epAmount = Integer.parseInt(token2);
						} else if (token.equals("treasure-clue")) {
							p.cluesReceived.get(TreasureTrails.getLevelByInt(Integer.parseInt(token3[0]))).add(
									Integer.parseInt(token3[1]));
						} else if (token.equals("receivedFreeCannon")) {
							p.receivedFreeCannon = Boolean.parseBoolean(token2);
						} else if (token.equals("theStolenCannonStatus")) {
							p.theStolenCannonStatus = Integer.parseInt(token2);
						} else if (token.equals("theStolenCannonNpcsKilled")) {
							for (int i = 0; i < token3.length; i++) {
								p.theStolenCannonNpcsKilled.add(Integer.parseInt(token3[i]));
							}
						} else if (token.equals("questPoints")) {
							p.questPoints = Integer.parseInt(token2);
						} else if (token.equals("running")) {
							p.getWalkingQueue().setRunningToggled(Boolean.parseBoolean(token2));
						}
						break;
					case 3:
						if (token.equals("character-equip")) {
							p.equipment[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
							p.playerEquipmentN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
						}
						break;
					case 4:
						if (token.equals("character-look")) {
							p.appearance[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
						}
						break;
					case 5:
						if (token.equals("character-skill")) {
							p.level[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
							p.xp[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
						}
						break;
					case 6:
						if (token.equals("item")) {
							p.inventory[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]) + 1;
							p.inventoryN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
						}
						break;
					case 7:
						if (token.equals("character-bank")) {
							// old support
							if (token3.length == 3) {
								p.getBank().addItem(0, Integer.parseInt(token3[0]), Integer.parseInt(token3[1]) - 1, Integer.parseInt(token3[2]));
							} else {
								List<TempBankItem> bankItemList = bankItems.get(Integer.parseInt(token3[3]));

								TempBankItem bankItem = new TempBankItem();
								bankItem.slot = Integer.parseInt(token3[0]);
								bankItem.item = Integer.parseInt(token3[1]);
								bankItem.amount = Integer.parseInt(token3[2]);
								bankItem.tab = Integer.parseInt(token3[3]);

								bankItemList.add(bankItem);
							}
						}
						break;
					case 8:
						switch (token) {
							case "npc-id":
								p.setFamiliar(new PlayerFamiliar(Integer.parseInt(token3[0])));
								break;
							case "ticks-left":
								p.getFamiliar().setTicksLeft(Integer.parseInt(token3[0]));
								break;
							case "energy-drain-ticks":
								p.getFamiliar().setEnergyDrainTicks(Integer.parseInt(token3[0]));
								break;
							case "inventory-item":
								p.getFamiliar().getInventoryItems()
										.add(new Item(Integer.parseInt(token3[0]), Integer.parseInt(token3[1])));
								break;
							case "special-moves-energy":
								p.getFamiliar().setSpecialMovesEnergy(Integer.parseInt(token3[0]));
								break;
							case "special-bar-restore-ticks":
								p.getFamiliar().setSpecialBarRestoreTicks(Integer.parseInt(token3[0]));
								break;
						}
						break;
					case 9:
					/*
					 * if (token.equals("character-ignore")) { ignores[Integer.parseInt(token3[0])] =
					 * Long.parseLong(token3[1]); }
					 */
						break;
					case 11:
						json.append(line);
						break;
					case 12:
						if (token.equals("character-zombie")) {
							p.bind[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
						}
						break;
					case 15: {
						p.getChat().deserialize(token, token2, token3);
						break;
					}
				}
			} else {
				if (line.equals("[ACCOUNT]")) {
					readMode = 1;
				} else if (line.equals("[CHARACTER]")) {
					readMode = 2;
				} else if (line.equals("[AGILITY]")) {
					readMode = 2;
				} else if (line.equals("[EQUIPMENT]")) {
					readMode = 3;
				} else if (line.equals("[LOOK]")) {
					readMode = 4;
				} else if (line.equals("[SKILLS]")) {
					readMode = 5;
				} else if (line.equals("[INVENTORY]")) {
					readMode = 6;
				} else if (line.equals("[BANK]")) {
					readMode = 7;
				} else if (line.equals("[FAMILIAR]")) {
					readMode = 8;
				} else if (line.equals("[ZOMBIE]")) {
					readMode = 12;
				} else if (line.equals("[KILLS]")) {
					readMode = 10;
				} else if (line.equals("[DATA]")) {
					readMode = 11;
				} else if (line.equals("[END OF DATA]")) {
					try {
						if (json.length() > 0) {
							p.setData(Misc.getGson().fromJson(json.toString(), PlayerData.class));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					readMode = 0;
				} else if (line.equals("[CHAT]")) {
					readMode = 15;
				} else if (line.equals("[EOF]")) {
					Iterator<List<TempBankItem>> iterator = bankItems.iterator();
					while(iterator.hasNext()) {
						List<TempBankItem> list = iterator.next();
						if(list.size() == 0) {
							iterator.remove();
						}
					}

					for(int tabIndex = 0; tabIndex < 10; tabIndex++) {
						if(tabIndex > bankItems.size() - 1) {
							continue;
						}

						List<TempBankItem> items = bankItems.get(tabIndex);

						Collections.sort(items, new Comparator<TempBankItem>() {
							@Override
							public int compare(TempBankItem o1, TempBankItem o2) {
								return Integer.compare(o1.slot, o2.slot);
							}
						});

						for(int slot = 0; slot < items.size(); slot++) {
							TempBankItem item = items.get(slot);
							p.getBank().addItem(tabIndex, slot, item.item, item.amount);
						}
					}

					System.out.println("loading " + playerName + " took " + (System.currentTimeMillis() - start) + "ms");

					return 1;
				}
			}
		}
		return 13;
	}

	private static boolean save(Player p, boolean create) {

		if(p.bot) {
			return false;
		}

		if (p.username == null || !create && PlayerHandler.players[p.playerId] == null) {
			return false;
		}
		p.username = p.playerName2;
		int tbTime = (int) (p.teleBlockDelay - System.currentTimeMillis() + p.teleBlockLength);
		if (tbTime > 300000 || tbTime < 0) {
			tbTime = 0;
		}
		if (p.isDoingTutorial()) {
			p.absX = 3222;
			p.absY = 3222;
			p.heightLevel = 0;
		}

		long start = System.currentTimeMillis();

		try {
			PlayerSaveWriter characterfile = new PlayerSaveWriter();

			/* CHARACTER */
			characterfile.write("[CHARACTER]", 0, 11);
			characterfile.newLine();
			characterfile.write("character-lastconnection = ", 0, 27);
			characterfile.write(p.connectedFrom, 0, p.connectedFrom.length());
			characterfile.newLine();
			characterfile.write("character-lastconnectionday = ", 0, 30);
			characterfile.write(PlayerSave.getCurrentDay(), 0, PlayerSave.getCurrentDay().length());
			characterfile.newLine();

			Location location = p.getLocation();
			if(p.getTeleportLocation() != null) {
				location = p.getTeleportLocation();
			}
			characterfile.write("location = " + location.getX() + "\t" + location.getY() + "\t" + location.getZ());
			characterfile.newLine();
			characterfile.write("crystal-bow-shots = ", 0, 20);
			characterfile.write(Integer.toString(p.crystalBowArrowCount), 0, Integer.toString(p.crystalBowArrowCount)
					.length());
			characterfile.newLine();
			characterfile.write("currentClass = ", 0, 15);
			characterfile.write(Integer.toString(p.currentClass), 0, Integer.toString(p.currentClass).length());
			characterfile.newLine();
			characterfile.write("meleeClassLvl = ", 0, 16);
			characterfile.write(Integer.toString(p.meleeClassLvl), 0, Integer.toString(p.meleeClassLvl).length());
			characterfile.newLine();
			characterfile.write("rangedClassLvl = ", 0, 17);
			characterfile.write(Integer.toString(p.rangedClassLvl), 0, Integer.toString(p.rangedClassLvl).length());
			characterfile.newLine();
			characterfile.write("magicClassLvl = ", 0, 16);
			characterfile.write(Integer.toString(p.magicClassLvl), 0, Integer.toString(p.magicClassLvl).length());
			characterfile.newLine();
			characterfile.write("lvlPotential = ", 0, 15);
			characterfile.write(Integer.toString(p.lvlPotential), 0, Integer.toString(p.lvlPotential).length());
			characterfile.newLine();
			characterfile.write("weaponKills = ", 0, 14);
			characterfile.write(Integer.toString(p.weaponKills), 0, Integer.toString(p.weaponKills).length());
			characterfile.newLine();
			characterfile.write("weaponDeaths = ", 0, 15);
			characterfile.write(Integer.toString(p.weaponDeaths), 0, Integer.toString(p.weaponDeaths).length());
			characterfile.newLine();
			characterfile.write("magestate = ", 0, 12);
			characterfile.write(Integer.toString(p.mageState), 0, Integer.toString(p.mageState).length());
			characterfile.newLine();
			characterfile.write("mediumPouchDecay = ", 0, 19);
			characterfile.write(Integer.toString(p.mediumPouchDecay), 0, Integer.toString(p.mediumPouchDecay).length());
			characterfile.newLine();
			characterfile.write("largePouchDecay = ", 0, 18);
			characterfile.write(Integer.toString(p.largePouchDecay), 0, Integer.toString(p.largePouchDecay).length());
			characterfile.newLine();
			characterfile.write("giantPouchDecay = ", 0, 18);
			characterfile.write(Integer.toString(p.giantPouchDecay), 0, Integer.toString(p.giantPouchDecay).length());
			characterfile.newLine();
			characterfile.write("npcWave = ", 0, 10);
			characterfile.write(Integer.toString(p.npcWave), 0, Integer.toString(p.npcWave).length());
			characterfile.newLine();
			characterfile.write("hasDoneCaves = ", 0, 15);
			characterfile.write(Integer.toString(p.hasDoneCaves), 0, Integer.toString(p.hasDoneCaves).length());
			characterfile.newLine();
			// characterfile.write("clan-name = ", 0, 12);
			// characterfile.write(p.clanOwner, 0, p.clanOwner.length());
			// characterfile.newLine();
			characterfile.write("skull-timer = ", 0, 14);
			characterfile.write(Integer.toString(p.skullTimer), 0, Integer.toString(p.skullTimer).length());
			characterfile.newLine();
			characterfile.write("[AGILITY]", 0, 9);
			characterfile.newLine();
			characterfile.write("ag1 = ", 0, 6);
			characterfile.write(Integer.toString(p.ag1), 0, Integer.toString(p.ag1).length());
			characterfile.newLine();
			characterfile.write("ag2 = ", 0, 6);
			characterfile.write(Integer.toString(p.ag2), 0, Integer.toString(p.ag2).length());
			characterfile.newLine();
			characterfile.write("donatorPoint = ");
			characterfile.write(Integer.toString(p.donatorPoint), 0, Integer.toString(p.donatorPoint).length());
			characterfile.newLine();
			characterfile.write("ag3 = ", 0, 6);
			characterfile.write(Integer.toString(p.ag3), 0, Integer.toString(p.ag3).length());
			characterfile.newLine();
			characterfile.write("ag4 = ", 0, 6);
			characterfile.write(Integer.toString(p.ag4), 0, Integer.toString(p.ag4).length());
			characterfile.newLine();
			characterfile.write("ag5 = ", 0, 6);
			characterfile.write(Integer.toString(p.ag5), 0, Integer.toString(p.ag5).length());
			characterfile.newLine();
			characterfile.write("ag6 = ", 0, 6);
			characterfile.write(Integer.toString(p.ag6), 0, Integer.toString(p.ag6).length());
			characterfile.newLine();
			characterfile.newLine();
			characterfile.write("zombie = ", 0, 9);
			characterfile.write(Integer.toString(p.zombiePoints), 0, Integer.toString(p.zombiePoints).length());
			characterfile.newLine();
			characterfile.write("magic-book = ", 0, 13);
			characterfile.write(Integer.toString(p.playerMagicBook), 0, Integer.toString(p.playerMagicBook).length());
			characterfile.newLine();
			characterfile.write("prayer-book = ", 0, 14);
			characterfile.write(Integer.toString(p.playerPrayerBook), 0, Integer.toString(p.playerPrayerBook).length());
			characterfile.newLine();
			for (int b = 0; b < p.barrowsNpcs.length; b++) {
				characterfile.write("brother-info = ", 0, 15);
				characterfile.write(Integer.toString(b), 0, Integer.toString(b).length());
				characterfile.write("	", 0, 1);
				characterfile.write(
						p.barrowsNpcs[b][1] <= 1 ? Integer.toString(0) : Integer.toString(p.barrowsNpcs[b][1]), 0,
						Integer.toString(p.barrowsNpcs[b][1]).length());
				characterfile.newLine();
			}
			characterfile.write("special-amount = ", 0, 17);
			characterfile.write(Double.toString(p.specAmount), 0, Double.toString(p.specAmount).length());
			characterfile.newLine();
			characterfile.write("selected-coffin = ", 0, 18);
			characterfile.write(Integer.toString(p.randomCoffin), 0, Integer.toString(p.randomCoffin).length());
			characterfile.newLine();
			characterfile.write("barrows-killcount = ", 0, 20);
			characterfile.write(Integer.toString(p.barrowsKillCount), 0, Integer.toString(p.barrowsKillCount).length());
			characterfile.newLine();
			characterfile.write("teleblock-length = ", 0, 19);
			characterfile.write(Integer.toString(tbTime), 0, Integer.toString(tbTime).length());
			characterfile.newLine();
			characterfile.write("pc-points = ", 0, 12);
			characterfile.write(Integer.toString(p.pcPoints), 0, Integer.toString(p.pcPoints).length());
			characterfile.newLine();
			characterfile.newLine();
			characterfile.write("voted = ", 0, 8);
			characterfile.write(Integer.toString(p.voted), 0, Integer.toString(p.voted).length());
			characterfile.newLine();
			characterfile.write("votereset = ", 0, 12);
			characterfile.write(Integer.toString(p.voteReset), 0, Integer.toString(p.voteReset).length());
			characterfile.newLine();
			characterfile.write("magePoints = ", 0, 13);
			characterfile.write(Integer.toString(p.magePoints), 0, Integer.toString(p.magePoints).length());
			characterfile.newLine();
			characterfile.write("Kill Count = ", 0, 13);
			characterfile.write(Integer.toString(p.killCount), 0, Integer.toString(p.killCount).length());
			characterfile.newLine();
			characterfile.write("Death Count = ", 0, 14);
			characterfile.write(Integer.toString(p.deathCount), 0, Integer.toString(p.deathCount).length());
			characterfile.newLine();
			characterfile.write("autoRet = ", 0, 10);
			characterfile.write(Integer.toString(p.autoRet), 0, Integer.toString(p.autoRet).length());
			characterfile.newLine();
			characterfile.write("barrowskillcount = ", 0, 19);
			characterfile.write(Integer.toString(p.barrowsKillCount), 0, Integer.toString(p.barrowsKillCount).length());
			characterfile.newLine();
			characterfile.write("flagged = ", 0, 10);
			characterfile.write(Boolean.toString(p.accountFlagged), 0, Boolean.toString(p.accountFlagged).length());
			characterfile.newLine();
			characterfile.write("gwkc = ", 0, 7);
			characterfile.write(Integer.toString(p.killCount), 0, Integer.toString(p.killCount).length());
			characterfile.newLine();
			characterfile.write("earningPot = ", 0, 13);
			characterfile.write(Integer.toString(p.earningPotential), 0, Integer.toString(p.earningPotential).length());
			characterfile.newLine();
			characterfile.write("earningDel = ", 0, 13);
			characterfile.write(Integer.toString(p.epDelay), 0, Integer.toString(p.epDelay).length());
			characterfile.newLine();
			characterfile.write("trinityPoints = ", 0, 16);
			characterfile.write(Integer.toString(p.trinityPoints), 0, Integer.toString(p.trinityPoints).length());
			characterfile.newLine();
			characterfile.write("isMember = ", 0, 11);
			characterfile.write(Integer.toString(p.isMember), 0, Integer.toString(p.isMember).length());
			characterfile.newLine();
			characterfile.write("dungeonEmote = ", 0, 15);
			characterfile.write(Integer.toString(p.dungeonEmote), 0, Integer.toString(p.dungeonEmote).length());
			characterfile.newLine();
			characterfile.write("summonEmote = ", 0, 14);
			characterfile.write(Integer.toString(p.summonEmote), 0, Integer.toString(p.summonEmote).length());
			characterfile.newLine();
			characterfile.write("Yell Points = ", 0, 14);
			characterfile.write(Integer.toString(p.yellPoints), 0, Integer.toString(p.yellPoints).length());
			characterfile.newLine();
			characterfile.write("Assault - Points = ", 0, 19);
			characterfile.write(Integer.toString(p.assaultPoints), 0, Integer.toString(p.assaultPoints).length());
			characterfile.newLine();
			characterfile.write("character-tradeTimer = ", 0, 23);
			characterfile.write(Integer.toString(p.tradeTimer), 0, Integer.toString(p.tradeTimer).length());
			characterfile.newLine();
			characterfile.write("killCount = ", 0, 12);
			characterfile.write(Integer.toString(p.playerKillCount), 0, Integer.toString(p.playerKillCount).length());
			characterfile.newLine();
			characterfile.write("deathCount = ", 0, 13);
			characterfile.write(Integer.toString(p.playerDeaths), 0, Integer.toString(p.playerDeaths).length());
			characterfile.newLine();
			characterfile.write("dfsCount = ", 0, 11);
			characterfile.write(Integer.toString(p.dfsCount), 0, Integer.toString(p.dfsCount).length());
			characterfile.newLine();
			characterfile.write("jailTimer = ", 0, 12);
			characterfile.write(Long.toString(p.jailTimer), 0, Long.toString(p.jailTimer).length());
			characterfile.newLine();
			characterfile.write("lockedEXP = ", 0, 12);
			characterfile.write(Integer.toString(p.lockedEXP), 0, Integer.toString(p.lockedEXP).length());
			characterfile.newLine();
			characterfile.write("Help Timer = ", 0, 13);
			characterfile.write(Integer.toString(p.helpTimer), 0, Integer.toString(p.helpTimer).length());
			characterfile.newLine();
			characterfile.write("attackStyle = " + p.getAttackStyle());
			characterfile.newLine();
			characterfile.write("void = ", 0, 7);
			String toWrite = p.voidStatus[0] + "\t" + p.voidStatus[1] + "\t" + p.voidStatus[2] + "\t" + p.voidStatus[3]
					+ "\t" + p.voidStatus[4];
			characterfile.write(toWrite);
			characterfile.newLine();
			characterfile.write("dfsc = ", 0, 7);
			characterfile.write(Integer.toString(p.dfsCharges), 0, Integer.toString(p.dfsCharges).length());
			characterfile.newLine();
			characterfile.newLine();

			characterfile.write("kills = " + p.kills);
			characterfile.newLine();

			characterfile.write("deaths = " + p.deaths);
			characterfile.newLine();

			characterfile.write("tournament points = ", 0, "tournament points = ".length());
			characterfile.write(Integer.toString(p.tournamentPoints), 0, Integer.toString(p.tournamentPoints).length());
			characterfile.newLine();
			for (int j = 0; j < p.lastKilledPlayers.size(); j++) {
				characterfile.write("killed-players = ", 0, 17);
				// characterfile.write("killed-players = " 0, 17, j);
				characterfile.write(p.lastKilledPlayers.get(j), 0, p.lastKilledPlayers.get(j).length());
				characterfile.newLine();
			}
			characterfile.write("[CHRISTMAS]", 0, 11);
			characterfile.newLine();
			characterfile.write("christmasEvent = ", 0, 17);
			characterfile.write(Integer.toString(p.christmasEvent), 0, Integer.toString(p.christmasEvent).length());
			characterfile.newLine();
			characterfile.write("giftAmount = ", 0, 13);
			characterfile.write(Integer.toString(p.giftAmount), 0, Integer.toString(p.giftAmount).length());
			characterfile.newLine();
			characterfile.write("marionetteMaking = ", 0, 19);
			characterfile.write(Integer.toString(p.marionetteMaking), 0, Integer.toString(p.marionetteMaking).length());
			characterfile.newLine();
			characterfile.write("baubleMaking = ", 0, 15);
			characterfile.write(Integer.toString(p.baubleMaking), 0, Integer.toString(p.baubleMaking).length());
			characterfile.newLine();
			characterfile.write("puppetBoxesCompleted = ", 0, 23);
			characterfile.write(Integer.toString(p.puppetBoxesCompleted), 0, Integer.toString(p.puppetBoxesCompleted)
					.length());
			characterfile.newLine();
			characterfile.write("baubleBoxesCompleted = ", 0, 23);
			characterfile.write(Integer.toString(p.baubleBoxesCompleted), 0, Integer.toString(p.baubleBoxesCompleted)
					.length());
			characterfile.newLine();
			characterfile.write("redAmount = ", 0, 12);
			characterfile.write(Integer.toString(p.redAmount), 0, Integer.toString(p.redAmount).length());
			characterfile.newLine();
			characterfile.write("blueAmount = ", 0, 13);
			characterfile.write(Integer.toString(p.blueAmount), 0, Integer.toString(p.blueAmount).length());
			characterfile.newLine();
			characterfile.write("greenAmount = ", 0, 14);
			characterfile.write(Integer.toString(p.greenAmount), 0, Integer.toString(p.greenAmount).length());
			characterfile.newLine();
			characterfile.write("redBox = ", 0, 9);
			characterfile.write(Boolean.toString(p.redBox), 0, Boolean.toString(p.redBox).length());
			characterfile.newLine();
			characterfile.write("blueBox = ", 0, 10);
			characterfile.write(Boolean.toString(p.blueBox), 0, Boolean.toString(p.blueBox).length());
			characterfile.newLine();
			characterfile.write("pinkBox = ", 0, 10);
			characterfile.write(Boolean.toString(p.pinkBox), 0, Boolean.toString(p.pinkBox).length());
			characterfile.newLine();
			characterfile.write("greenBox = ", 0, 11);
			characterfile.write(Boolean.toString(p.greenBox), 0, Boolean.toString(p.greenBox).length());
			characterfile.newLine();
			characterfile.write("yellowBox = ", 0, 12);
			characterfile.write(Boolean.toString(p.yellowBox), 0, Boolean.toString(p.yellowBox).length());
			characterfile.newLine();

			for (Map.Entry<Level, List<Integer>> clues : p.cluesReceived.entrySet()) {
				List<Integer> items = clues.getValue();
				items.forEach(x -> {
					characterfile.write("treasure-trail = " + clues.getKey().getLevel() + "\t" + x);
					characterfile.newLine();
				});
			}

			characterfile.write("receivedFreeCannon = " + p.receivedFreeCannon);
			characterfile.newLine();
			characterfile.write("theStolenCannonStatus = " + p.theStolenCannonStatus);
			characterfile.newLine();
			if (p.theStolenCannonNpcsKilled.size() > 0) {
				characterfile.write("theStolenCannonNpcsKilled = " + Joiner.on("\t").join(p.theStolenCannonNpcsKilled));
				characterfile.newLine();
			}
			characterfile.write("questPoints = " + p.questPoints);
			characterfile.newLine();
			characterfile.write("running = " + p.getWalkingQueue().isRunningToggled());
			characterfile.newLine();

			/* EQUIPMENT */
			characterfile.write("[EQUIPMENT]", 0, 11);
			characterfile.newLine();
			for (int i = 0; i < p.equipment.length; i++) {
				characterfile.write("character-equip = ", 0, 18);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.equipment[i]), 0, Integer.toString(p.equipment[i])
						.length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerEquipmentN[i]), 0, Integer.toString(p.playerEquipmentN[i])
						.length());
				characterfile.write("	", 0, 1);
				characterfile.newLine();
			}
			characterfile.newLine();

			/* LOOK */
			characterfile.write("[LOOK]", 0, 6);
			characterfile.newLine();
			for (int i = 0; i < p.appearance.length; i++) {
				characterfile.write("character-look = ", 0, 17);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.appearance[i]), 0, Integer.toString(p.appearance[i])
						.length());
				characterfile.newLine();
			}
			characterfile.newLine();

			/* SKILLS */
			characterfile.write("[SKILLS]", 0, 8);
			characterfile.newLine();
			for (int i = 0; i < p.level.length; i++) {

				if (p.xp[i] >= 200_000_000) {
					p.xp[i] = 200_000_000;
				}

				characterfile.write("character-skill = ", 0, 18);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.level[i]), 0, Integer.toString(p.level[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.xp[i]), 0, Integer.toString(p.xp[i]).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			/* ITEMS */
			characterfile.write("[INVENTORY]", 0, 7);
			characterfile.newLine();
			for (int i = 0; i < p.inventory.length; i++) {
				if (p.inventory[i] > 0) {
					characterfile.write("item = ", 0, 7);
					characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.inventory[i] - 1), 0, Integer.toString(p.inventory[i] - 1).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.inventoryN[i]), 0, Integer.toString(p.inventoryN[i])
							.length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();

			/* BANK */
			characterfile.write("[BANK]", 0, 6);
			characterfile.newLine();
			for (int tab = 0; tab < p.getBank().getTabs().size(); tab++) {
				List<Item> items = p.getBank().getTabs().get(tab);
				for (int slot = 0; slot < items.size(); slot++) {
					characterfile.write("character-bank = " + slot + "\t" + items.get(slot).getId() + "\t"
							+ items.get(slot).getAmount() + "\t" + tab);
					characterfile.newLine();
				}
			}
			characterfile.newLine();

			/**
			 * Summoning
			 */
			if (p.getFamiliar() != null && p.getFamiliar().getType() != null) {
				characterfile.write("[FAMILIAR]");
				characterfile.newLine();

				characterfile.write("npc-id = " + p.getFamiliar().getType().getNpcId());
				characterfile.newLine();

				characterfile.write("ticks-left = " + p.getFamiliar().getTicksLeft());
				characterfile.newLine();

				characterfile.write("energy-drain-ticks = " + p.getFamiliar().getEnergyDrainTicks());
				characterfile.newLine();

				for (Item item : p.getFamiliar().getInventoryItems()) {
					characterfile.write("inventory-item = " + item.getId() + "\t" + item.getAmount());
					characterfile.newLine();
				}
				characterfile.write("special-moves-energy = " + p.getFamiliar().getSpecialMovesEnergy());
				characterfile.newLine();

				characterfile.write("special-bar-ticks = " + p.getFamiliar().getSpecialBarRestoreTicks());
				characterfile.newLine();

				characterfile.newLine();
			}

			/* ZOMBIE */
			characterfile.write("[ZOMBIE]", 0, 8);
			characterfile.newLine();
			for (int i = 0; i < p.bind.length; i++) {
				if (p.bind[i] > 0) {
					characterfile.write("character-zombie = ", 0, 19);
					characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.bind[i]), 0, Integer.toString(p.bind[i]).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();

			characterfile.write("[DATA]");
			characterfile.newLine();
			characterfile.write(Misc.getGson().toJson(p.getData()));
			characterfile.newLine();
			characterfile.write("[END OF DATA]");
			characterfile.newLine();

			characterfile.newLine();
			Chat chat = p.getChat();
			characterfile.write("[CHAT]");
			characterfile.newLine();
			chat.serialize(characterfile);
			characterfile.newLine();

			/* IGNORES */
			/*
			 * characterfile.write("[IGNORES]", 0, 9); characterfile.newLine(); for (int i = 0; i < ignores.length; i++)
			 * { if (ignores[i] > 0) { characterfile.write("character-ignore = ", 0, 19);
			 * characterfile.write(Integer.toString(i), 0, Integer.toString(i).length()); characterfile.write("	", 0,
			 * 1); characterfile.write(Long.toString(ignores[i]), 0, Long.toString(ignores[i]).length());
			 * characterfile.newLine(); } } characterfile.newLine();
			 */
			/* EOF */
			characterfile.write("[EOF]", 0, 5);
			characterfile.newLine();
			characterfile.newLine();


			byte[] buffer = null;
			if(p.npcDrops.size() > 0) {
				ByteArrayDataOutput buff = ByteStreams.newDataOutput();

				buff.writeInt(p.npcDrops.size());

				for (Map.Entry<Integer, Map<Integer, Integer>> entry : p.npcDrops.entrySet()) {
					Map<Integer, Integer> kills = entry.getValue();

					buff.writeInt(entry.getKey());
					buff.writeInt(kills.size());

					for (Map.Entry<Integer, Integer> entry2 : kills.entrySet()) {
						buff.writeInt(entry2.getKey());
						buff.writeInt(entry2.getValue());
					}
				}

				try {
					buffer = Misc.compress(buff.toByteArray());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try (Connection connection = DatabaseUtil.getConnection()) {

				connection.setAutoCommit(false);
				if (create) {
					try (PreparedStatement saveStatement = connection.prepareStatement("INSERT INTO players (username, password, data, vote_points, uuid) VALUES (?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
						saveStatement.setString(1, p.username);
						saveStatement.setString(2, BCrypt.hashpw(p.playerPass.toLowerCase(), BCrypt.gensalt()));
						saveStatement.setString(3, characterfile.toString());
						saveStatement.setInt(4, p.votePoints);
						saveStatement.setString(5, p.uuid);
						saveStatement.execute();

						ResultSet rs = saveStatement.getGeneratedKeys();
						if (rs != null && rs.next()) {
							p.setDatabaseId(rs.getInt(1));
						}
					}
					try (PreparedStatement ps = connection.prepareStatement("INSERT INTO player_skills (player_id, skill, boosted_level, actual_level, xp) VALUES(?, ?, ?, ?, ?)")) {
						int totalLevel = p.getPA().getTotalLevel();
						long totalXP = p.getPA().getTotalXP();

						ps.setInt(1, p.getDatabaseId());
						ps.setInt(2, -1);
						ps.setInt(3, totalLevel);
						ps.setInt(4, totalLevel);
						ps.setLong(5, totalXP);
						ps.addBatch();

						for (int i = 0; i < p.level.length; i++) {
							ps.setInt(1, p.getDatabaseId());
							ps.setInt(2, i);
							ps.setInt(3, p.level[i]);
							ps.setInt(4, p.getActualLevel(i));
							ps.setInt(5, p.xp[i]);
							ps.addBatch();
						}

						ps.executeBatch();
					}
				} else {
					try (PreparedStatement saveStatement = connection.prepareStatement("UPDATE players SET data = ?, vote_points = ?, uuid = ?, total_level = ?, npc_drops = ?, kills = ?, deaths = ? WHERE id = ?")) {
						saveStatement.setString(1, characterfile.toString());
						saveStatement.setInt(2, p.votePoints);
						saveStatement.setString(3, p.uuid);
						saveStatement.setInt(4, p.getPA().getTotalLevel());
						saveStatement.setBytes(5, buffer);
						saveStatement.setInt(6, p.kills);
						saveStatement.setInt(7, p.deaths);
						saveStatement.setInt(8, p.getDatabaseId());
						saveStatement.execute();
					}
					try (PreparedStatement ps = connection.prepareStatement("UPDATE player_skills SET boosted_level = ?, actual_level = ?, xp = ? WHERE player_id = ? AND skill = ?")) {
						int totalLevel = p.getPA().getTotalLevel();
						long totalXP = p.getPA().getTotalXP();

						ps.setInt(1, totalLevel);
						ps.setInt(2, totalLevel);
						ps.setLong(3, totalXP);
						ps.setInt(4, p.getDatabaseId());
						ps.setInt(5, -1);

						ps.addBatch();

						for (int i = 0; i < p.level.length; i++) {
							ps.setInt(1, p.level[i]);
							ps.setInt(2, p.getActualLevel(i));
							ps.setInt(3, p.xp[i]);
							ps.setInt(4, p.getDatabaseId());
							ps.setInt(5, i);
							ps.addBatch();
						}

						ps.executeBatch();
					}
				}
				connection.commit();
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(p.username + ": error writing file.");
			return false;
		}

		System.out.println("saving " + p.username + " took " + (System.currentTimeMillis() - start) + "ms");

		return true;
	}

	public static void saveGame(SaveRequest request) {
		executor.execute(request);
	}

	public static void saveGame(Player p) {
		save(p, false);
	}

	public static class PlayerSaveWriter {

		private StringBuilder str = new StringBuilder();

		public void write(String s) {
			str.append(s);
		}

		public void write(String s, int x, int y) {
			str.append(s);
		}

		public void newLine() {
			str.append("\r\n");
		}

		@Override
		public String toString() {
			return str.toString();
		}
	}

}
