package dev.octodumb.letmeafk;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public final class AfkManager {
    private static final Long AFK_COOLDOWN = 5000L;

    private static final List<PlayerEntity> afkPlayers = new ArrayList<>();

    private static final HashMap<PlayerEntity, Long> lastAfk = new HashMap<>();

    public static boolean isAfk(PlayerEntity player) {
        return afkPlayers.contains(player);
    }

    public static boolean toggleAfk(PlayerEntity player) {
        if(!isAfk(player)) {
            if(lastAfk.containsKey(player))
                if(lastAfk.get(player) + AFK_COOLDOWN > new Date().getTime())
                    return false;
            afkPlayers.add(player);
            player.sendMessage(new LiteralText("You are now AFK!"), false);
        }
        else {
            afkPlayers.remove(player);
            lastAfk.put(player, new Date().getTime());
            LetMeAfk.LOGGER.info("Turned off AFK for {}", player);
            player.sendMessage(new LiteralText("You are no longer AFK!"), false);
        }

        return true;
    }

    public static void remove(PlayerEntity player) {
        afkPlayers.remove(player);
    }
}
