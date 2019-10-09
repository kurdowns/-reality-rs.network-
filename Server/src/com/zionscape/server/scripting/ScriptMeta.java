package com.zionscape.server.scripting;

/**
 * @author Stuart
 */
public class ScriptMeta {

	private final String name;
	private final String author;
	private final boolean enabled;
	private final double version;
	private final String[] scripts;
	private final String[] dependencies;

	public ScriptMeta(String name, String author, boolean enabled, double version, String[] scripts, String[] dependencies) {
		this.name = name;
		this.author = author;
		this.enabled = enabled;
		this.version = version;
		this.scripts = scripts;
		this.dependencies = dependencies;
	}

	public String getName() {
		return name;
	}

	public String getAuthor() {
		return author;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public double getVersion() {
		return version;
	}

	public String[] getScripts() {
		return scripts;
	}

	public String[] getDependencies() {
		return dependencies;
	}

}