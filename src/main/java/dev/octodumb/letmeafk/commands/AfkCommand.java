package dev.octodumb.letmeafk.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.octodumb.letmeafk.AfkManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

public class AfkCommand implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        final ServerCommandSource source = context.getSource();
        final PlayerEntity player = source.getPlayer();

        boolean success = AfkManager.toggleAfk(player);

        if(!success)
            throw new SimpleCommandExceptionType(new LiteralText("You need to wait 5 seconds to go AFK again!")).create();

        return Command.SINGLE_SUCCESS;
    }
}
