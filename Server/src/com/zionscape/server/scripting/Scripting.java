package com.zionscape.server.scripting;

import com.zionscape.server.Server;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.scripting.messages.Message;
import com.zionscape.server.scripting.messages.handler.MessageHandler;
import com.zionscape.server.scripting.messages.handler.MessageHandlerChain;
import com.zionscape.server.scripting.messages.handler.MessageHandlerChainGroup;
import com.zionscape.server.util.Misc;
import org.apache.commons.io.FileUtils;
import org.jruby.embed.ScriptingContainer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This scripting system has been adapted and taken from the Apollo project found at @see
 * <a href="https://github.com/apollo-rsps/apollo">https://github.com/apollo-rsps/apollo</a>
 * <p>
 * All credits to Graham Edgecombe
 *
 * @author Stuart
 */
public final class Scripting {

	/**
	 *
	 */
	public static final Map<String, Object> dialogues = new HashMap<>();
	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(Scripting.class.getName());
	/**
	 *
	 */
	private static final String SCRIPT_DIRECTORY = "./scripts/";
	/**
	 *
	 */
	private static final ScriptingContainer container = new ScriptingContainer();

	/**
	 *
	 */
	public static void load() {
		parse(SCRIPT_DIRECTORY + "bootstrap.rb");
		try {
			Files.walk(Paths.get(SCRIPT_DIRECTORY)).filter(p -> p.endsWith("script.json")).forEach(f -> {
				try {
					ScriptMeta meta = Misc.getGson().fromJson(FileUtils.readFileToString(f.toFile()), ScriptMeta.class);
					if (meta.isEnabled()) {
						if (meta.getDependencies() != null) {
							for (String s : meta.getScripts()) {

							}
						}
						if (meta.getScripts() != null) {
							for (String s : meta.getScripts()) {
								parse(f.toFile().getParent() + "/" + s);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("Error loading ruby scripts");
				}

			});
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error loading ruby scripts");
		}
	}

	/**
	 * @param file
	 */
	private static void parse(String file) {
		try {
			container.runScriptlet(FileUtils.readFileToString(new File(file)));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("error parsing scriptlet " + file + ".");
		}
	}

	/**
	 * @param player
	 * @param message
	 * @return if or not the chain was broken
	 */
	@SuppressWarnings("unchecked")
	public static boolean handleMessage(Player player, Message message) {
		MessageHandlerChainGroup chainGroup = Server.getMessageHandlerChains();

		Class<? extends Message> messageType = message.getClass();
		MessageHandlerChain<Message> chain = (MessageHandlerChain<Message>) chainGroup.getChain(messageType);

		while (chain == null && messageType != null) {
			messageType = (Class<? extends Message>) messageType.getSuperclass();
			if (messageType == Message.class) {
				messageType = null;
			} else {
				chain = (MessageHandlerChain<Message>) chainGroup.getChain(messageType);
			}
		}

		if (chain != null) {
			try {
				if (chain.handle(player, message)) {
					return true;
				}
			} catch (Exception ex) {
				logger.log(Level.SEVERE, "Error handling message: ", ex);
			}
		}

		return false;
	}

	/**
	 * @param message
	 * @param handler
	 * @param <M>
	 */
	public static <M extends Message> void addLastMessageHandler(Class<M> message, MessageHandler<M> handler) {
		MessageHandlerChainGroup chains = Server.getMessageHandlerChains();
		MessageHandlerChain<M> chain = chains.getChain(message);
		if (chain == null) {
			chain = new MessageHandlerChain<>(handler);
			chains.register(message, chain);
		} else {
			chain.addLast(handler);
		}
	}

}