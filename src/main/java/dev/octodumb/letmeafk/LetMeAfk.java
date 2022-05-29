package dev.octodumb.letmeafk;

import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.octodumb.letmeafk.commands.AfkCommand;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LetMeAfk implements DedicatedServerModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("letmeafk");

	@Override
	public void onInitializeServer() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			LiteralCommandNode<ServerCommandSource> afkNode = CommandManager.
				literal("afk")
				.executes(new AfkCommand())
				.build();

			dispatcher.getRoot().addChild(afkNode);
		});

		LOGGER.info("Let Me AFK initialized!");
	}
}
