package org.apollo.jagcached.fs;

/**
 * A class which points to a file in the cache.
 *
 * @author Graham
 */
public final class FileDescriptor {

	/**
	 * The file type.
	 */
	private final int type;

	/**
	 * The file id.
	 */
	private final int file;

	/**
	 * Creates the file descriptor.
	 *
	 * @param type The file type.
	 * @param file The file id.
	 */
	public FileDescriptor(int type, int file) {
		this.type = type;
		this.file = file;
	}

	/**
	 * Gets the file type.
	 *
	 * @return The file type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * Gets the file id.
	 *
	 * @return The file id.
	 */
	public int getFile() {
		return file;
	}

	@Override
	public int hashCode() {
		int hash = 23;
		hash = hash * 31 + type;
		hash = hash * 3 * file;
		return hash;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof FileDescriptor) {
			FileDescriptor fd = (FileDescriptor) o;
			return type == fd.getType() && file == fd.getFile();
		}
		return false;
	}

}