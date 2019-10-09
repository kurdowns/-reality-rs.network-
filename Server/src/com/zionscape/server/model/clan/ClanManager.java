package com.zionscape.server.model.clan;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;

public class ClanManager {

	/**
	 * Is the clan wars arena currently being used?
	 */
	public boolean arenaInUse = false;

	/**
	 * The clans.
	 */
	public LinkedList<Clan> clans;

	/**
	 * Initializes the clan manager.
	 */
	public ClanManager() {
		clans = new LinkedList<Clan>();
	}

	public void challenge(String challenger_, String challenged_) {
		Clan challenger = this.getClan(challenger_);
		Clan challenged = this.getClan(challenged_);
		if (challenged != null && challenger != null) {
			this.sendChallenge(challenged, challenger);
		}
	}

	/**
	 * Does the clan for the specified owner exist?
	 *
	 * @param owner
	 * @return
	 */
	public boolean clanExists(String owner) {
		return new File("./data/clan/" + owner.toLowerCase() + ".cla").exists();
	}

	public void sendClanInterface() {
		for(Clan clan : clans) {
			if(clan == null) {
				continue;
			}
			if(clan.updateMembers) {
				clan.sendUpdateInterface();
				clan.updateMembers = false;
			}
		}
	}

	/**
	 * Creates a new clan for the specified player.
	 *
	 * @param player
	 */
	public void create(Player player) {
		Clan clan = new Clan(player);
		clans.add(clan);
		clan.addMember(player);
		clan.save();
		player.getPA().setClanData();
		player.sendMessage("<col=FF0000>You may change your clan settings by clicking the 'Clan Setup' button.");
	}

	/**
	 * Deletes the specified clan.
	 *
	 * @param clan
	 */
	public void delete(Clan clan) {
		/*
		 * If the clan is null, stop the method.
		 */
		if (clan == null) {
			return;
		}
		File file = new File("./data/clan/" + clan.getFounder().toLowerCase() + ".cla");
		if (file.delete()) {
			Player player = PlayerHandler.getPlayer(clan.getFounder());
			if (player != null) {
				player.sendMessage("Your clan has been deleted.");
			}
			clans.remove(clan);
		}
	}

	/**
	 * Gets the active clan count.
	 *
	 * @return
	 */
	public int getActiveClans() {
		return clans.size();
	}

	/**
	 * Gets the clan for the specified founder.
	 *
	 * @param title
	 * @return
	 */
	public Clan getClan(String founder) {
		/*
         * Loop through the currently active clans and look for one with the same founder as the one specified. If we
		 * find it, return the clan instance.
		 */
		for (int index = 0; index < clans.size(); index++) {
			if (clans.get(index).getFounder().equalsIgnoreCase(founder)) {
				return clans.get(index);
			}
		}
        /*
		 * If the clan isn't found in the existing clans, we will try to read the file for the specified founder. If the
		 * read file isn't null, we'll add the read clan to the list of clans. If the read data is null, we'll return a
		 * null clan.
		 */
		Clan read = this.read(founder);
		if (read != null) {
			clans.add(read);
			return read;
		} else {
			return null;
		}
	}

	/**
	 * Gets the list of clans.
	 *
	 * @return
	 */
	public LinkedList<Clan> getClans() {
		return clans;
	}

	/**
	 * Gets the clan count.
	 *
	 * @return
	 */
	public int getTotalClans() {
		File directory = new File("./data/clan/");
		return directory.listFiles().length;
	}

	/**
	 * Reads the clan file.
	 *
	 * @param owner
	 * @return
	 */
	private Clan read(String owner) {
		File file = new File("./data/clan/" + owner.toLowerCase() + ".cla");
		if (!file.exists()) {
			/*
			 * Clan doesn't exist.
			 */
			return null;
		}
		try (RandomAccessFile in = new RandomAccessFile(file, "rwd")) {
			// DataInputStream in = new DataInputStream(new FileInputStream(file));
			/*
			 * Create a new clan out of the read data.
			 */
			Clan clan = new Clan(in.readUTF(), owner);
			clan.whoCanJoin = in.readByte();
			clan.whoCanTalk = in.readByte();
			clan.whoCanKick = in.readByte();
			clan.whoCanBan = in.readByte();

			int memberCount = in.readShort();
			for (int index = 0; index < memberCount; index++) {
				clan.rankedMembers.add(in.readUTF());
				clan.ranks.add((int) in.readShort());
			}

			int banCount = in.readShort();
			for (int i = 0; i < banCount; i++) {
				clan.bannedMembers.add(in.readUTF());
			}

			in.close();
			/*
			 * Data was read successfully.
			 */
			return clan;
		} catch (IOException e) {
			/*
			 * An error occurred while reading the data.
			 */
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Saves the clan data.
	 */
	public void save(Clan clan) {
		/*
		 * If the clan is null, stop the method.
		 */
		if (clan == null) {
			return;
		}

		File file = new File("./data/clan/" + clan.getFounder().toLowerCase() + ".cla");
		try (RandomAccessFile out = new RandomAccessFile(file, "rwd")) {
			// DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
			/*
			 * Write the clan information.
			 */
			out.writeUTF(clan.getTitle());
			out.writeByte(clan.whoCanJoin);
			out.writeByte(clan.whoCanTalk);
			out.writeByte(clan.whoCanKick);
			out.writeByte(clan.whoCanBan);

			out.writeShort(clan.rankedMembers.size());
			for (int index = 0; index < clan.rankedMembers.size(); index++) {
				out.writeUTF(clan.rankedMembers.get(index));
				out.writeShort(clan.ranks.get(index));
			}

			out.writeShort(clan.bannedMembers.size());
			for (int index = 0; index < clan.bannedMembers.size(); index++) {
				out.writeUTF(clan.bannedMembers.get(index));
			}

			/*
			 * Close the file.
			 */
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendChallenge(Clan challenger, Clan challenged) {
		PlayerHandler.getPlayer(challenged.founder).sendMessage(
				challenger.founder + " wishes to challenge your clan to a war!");
		// TODO: change this to send the interface w/ buttons and shi
	}
}