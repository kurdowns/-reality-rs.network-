package com.zionscape.server.net.codec;

import com.zionscape.server.Config;
import com.zionscape.server.Server;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.PlayerSave;
import com.zionscape.server.model.players.commands.PunishmentCommands;
import com.zionscape.server.net.LoginLimitFilter;
import com.zionscape.server.net.PacketBuilder;
import com.zionscape.server.util.DatabaseUtil;
import com.zionscape.server.util.ISAACRandomGen;
import com.zionscape.server.util.Misc;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RS2LoginProtocol
 *
 * @author Lmctruck30
 * @version 1.0
 */
public class RS2LoginProtocol extends FrameDecoder {

	private static final int CONNECTED = 0;
	private static final int LOGGING_IN = 1;
	private int state = CONNECTED;

	public static boolean blockCheckup(String s, int checkUP) {
		char[] UIDFletcherInput = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
				'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
				'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
				'4', '5', '6', '7', '8', '9'};
		int[] UIDFletcherOutput = {10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
				31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56,
				57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71};
		int UID = 0;
		for (int i1 = 0; i1 < s.length(); i1++) {
			for (int i2 = 0; i2 < UIDFletcherInput.length; i2++) {
				if (UIDFletcherInput[i2] == s.charAt(i1)) {
					UID += UIDFletcherOutput[i2];
				}
			}
		}
		if (UID == checkUP) {
			return false;
		} else {
			return true;
		}
	}

	private static boolean hasMaxConnections(String ip, String uuid) {
		int ipCount = 0;
		int guidCount = 0;
		for (Player player : PlayerHandler.players) {
			if(player == null) {
				continue;
			}
			if(player.connectedFrom.equalsIgnoreCase(ip)) {
				ipCount++;
			}
			if(player.uuid.equalsIgnoreCase(uuid)) {
				guidCount++;
			}
		}

		if(ipCount >= Config.ACCOUNTS_PER_IP_ALLOWED) {
			return true;
		}

		if(guidCount >= Config.ACCOUNTS_PER_GUID_ALLOWED) {
			return true;
		}

		return false;
	}

	public static void sendReturnCode(final Channel channel, final int code) {
		channel.write(new PacketBuilder().put((byte) code).put((byte) 0).put((byte) 0).toPacket()).addListener(
				new ChannelFutureListener() {

					@Override
					public void operationComplete(final ChannelFuture arg0) throws Exception {
						arg0.getChannel().close();
					}
				});
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer in) throws Exception {
		if (!channel.isConnected()) {
			return null;
		}

		switch (state) {
			case CONNECTED:
				if (in.readableBytes() < 2) {
					return null;
				}

				int request = in.readUnsignedByte();
				if (request != 14) {
					System.out.println("Invalid login request: " + request);
					channel.close();
					LoginLimitFilter.getInstance().addAttempt(channel);
					return null;
				}
				in.readUnsignedByte();
				channel.write(new PacketBuilder().putLong(0).put((byte) 0).putLong(new SecureRandom().nextLong())
						.toPacket());
				state = LOGGING_IN;
				return null;
			case LOGGING_IN:
				if (in.readableBytes() < 2) {
					return null;
				}

				int loginType = in.readByte();
				if (loginType != 16 && loginType != 18) {
					System.out.println("Invalid login type: " + loginType);
					channel.close();
					LoginLimitFilter.getInstance().addAttempt(channel);
					return null;
				}

				if (Config.antibot) {
					String connectedFrom = ((InetSocketAddress) channel.getRemoteAddress()).getAddress().getHostAddress();
					try (Connection connection = DatabaseUtil.getConnection()) {
						try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM `anti-bot` WHERE ip = ?")) {
							ps.setString(1, connectedFrom);
							try (ResultSet resultSet = ps.executeQuery()) {
								if (!resultSet.next()) {
									RS2LoginProtocol.sendReturnCode(channel, 17);
									channel.close();
									LoginLimitFilter.getInstance().addAttempt(channel);
									return null;
								}
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}

				// System.out.println("Login type = "+loginType);
				int blockLength = in.readByte() & 0xff;
				if (in.readableBytes() < blockLength) {
					return null;
				}
				int checkUP = in.readShort();
				String version = Misc.getRS2String(in);
			/*
			 * if (clientVersion != 317) { System.out.println("Invalid client version: " + clientVersion);
			 * channel.close(); return null; }
			 */
				@SuppressWarnings("unused")
				boolean lowMem = in.readByte() == 1;// memory
				for (int i = 0; i < 9; i++) {
					if (Config.EXPECTED_CRCS[i] != in.readInt()) {
						//  sendReturnCode(channel, 6);
						//   return null;
					}
				}
				in.readByte();
				int rsaOpcode = in.readByte();
				if (rsaOpcode != 10) {
					System.out.println("Unable to decode RSA block properly!");
					channel.close();
					LoginLimitFilter.getInstance().addAttempt(channel);
					return null;
				}
				final long clientHalf = in.readLong();
				final long serverHalf = in.readLong();

				if (clientHalf == 0 || serverHalf == 0) {
					channel.close();
					LoginLimitFilter.getInstance().addAttempt(channel);
					return null;
				}

				final int[] isaacSeed = {(int) (clientHalf >> 32), (int) clientHalf, (int) (serverHalf >> 32),
						(int) serverHalf};
				final ISAACRandomGen inCipher = new ISAACRandomGen(isaacSeed);
				for (int i = 0; i < isaacSeed.length; i++) {
					isaacSeed[i] += 50;
				}
				final ISAACRandomGen outCipher = new ISAACRandomGen(isaacSeed);
				@SuppressWarnings("unused")
				final int uid = in.readInt();
				final String name = Misc.getRS2String(in);
				final String pass = Misc.getRS2String(in);
				final String uuid = Misc.getRS2String(in);

				channel.getPipeline().replace("decoder", "decoder", new RS2ProtocolDecoder(inCipher));
				return this.login(channel, inCipher, outCipher, version, name, pass, uuid);
		}
		return null;
	}

	private Player login(Channel channel, ISAACRandomGen inCipher, ISAACRandomGen outCipher, String version, String name, String pass, String uuid) {
		int returnCode = 2;
		name = name.trim();
		if (!name.matches("[A-Za-z0-9 ]+")) {
			returnCode = 3;
		}
		if (name.length() > 12) {
			returnCode = 3;
		}

		String connectedFrom = ((InetSocketAddress) channel.getRemoteAddress()).getAddress().getHostAddress();


		// check if more than 2 ips are being used from one guid
		if (hasMaxConnections(connectedFrom, uuid)) {
			returnCode = 9; // login limit exceeded
		}

		/*
		 * if (RS2LoginProtocol.blockCheckup(name, checkUP)) { if (!Config.ALLOW_BOTS) { System.out.println(name + " " +
		 * checkUP); returnCode = 3; } }
		 */
		if (!version.equalsIgnoreCase(Config.CLIENT_VERSION)) {
			returnCode = 6;
		}
		if (PlayerHandler.getPlayer(name) != null) {
			returnCode = 5;
		}
		if (PlayerHandler.getPlayerCount() >= Config.MAX_PLAYERS) {
			returnCode = 7;
		}
		if (Server.UpdateServer) {
			returnCode = 14;
		}

		if (uuid == null || uuid.length() == 0) {
			uuid = "NO_GUID_" + connectedFrom;
		}

		// check for any existing punishments
		boolean jail = false;
		boolean mute = false;
		int punishment = PunishmentCommands.getPunishment(name, connectedFrom, uuid);
		if (punishment != -1) {
			switch (punishment) {
				case PunishmentCommands.MUTE:
				case PunishmentCommands.IPMUTE:
					mute = true;
					break;
				case PunishmentCommands.BAN:
					returnCode = 4;
					break;
				case PunishmentCommands.UIDBAN: // guid
				case PunishmentCommands.IPBAN: // mac
					returnCode = 27;
					break;
				case PunishmentCommands.IPJAIL: // jail
					jail = true;
					break;
			}
		}
		System.out.println("1 return code: " + returnCode);
		if (returnCode != 2) {
			LoginLimitFilter.getInstance().addAttempt(channel);
			RS2LoginProtocol.sendReturnCode(channel, returnCode);
			return null;
		}
		System.out.println("2 return code: " + returnCode + "- " + connectedFrom+" - "+uuid);
		name = Misc.formatPlayerName(name);
		System.out.println("~~1 " + returnCode);
		Player cl = new Player(channel, -1, false);
		System.out.println("###12 " + returnCode);
		cl.username = name;
		System.out.println("##13" + returnCode);
		cl.connectedFrom = connectedFrom;
		System.out.println("##14 " + returnCode);
		cl.encodedName = Misc.playerNameToInt64(name);
		System.out.println("##15 " + returnCode);
		cl.playerName2 = cl.username;
		System.out.println("##16 " + returnCode);
		cl.playerPass = pass;
		System.out.println("##17 " + returnCode);
		cl.outStream.packetEncryption = outCipher;
		System.out.println("##18 " + returnCode);
		cl.isActive = true;
		System.out.println("##19 " + returnCode);
		cl.uuid = uuid;
		System.out.println("1 222" + returnCode);
		cl.muted = mute;
		System.out.println("1234 " + returnCode);
		System.out.println("3 return code: " + returnCode);
		for (int i = 0; i < cl.equipment.length; i++) {
			if (cl.equipment[i] == 0) {
				cl.equipment[i] = -1;
				cl.playerEquipmentN[i] = 0;
			}
		}

		if (returnCode == 2) {
			int load = PlayerSave.loadGame(cl, cl.username, cl.playerPass);
			if (load == 0) {
				cl.addStarter = true;
			}
			if (load == 3) {
				returnCode = 3;
			} else {
				if (!PlayerHandler.newPlayerClient(cl)) {
					returnCode = 7;
				}
			}
		}
		if (returnCode == 2) {
			if(jail) {
				cl.setTeleportLocation(Location.create(2602, 4775, 0));
				cl.jailTimer = Long.MAX_VALUE;
			}

			cl.combatLevel = cl.calculateCombatLevel();

			final PacketBuilder bldr = new PacketBuilder();
			bldr.put((byte) 2);

			bldr.put((byte) Misc.getChatIcon(cl));

			bldr.put((byte) 0);
			channel.write(bldr.toPacket());
		} else {
			System.out.println(name + " returncode: " + returnCode);
			RS2LoginProtocol.sendReturnCode(channel, returnCode);
			return null;
		}
		return cl;
	}
}
