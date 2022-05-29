package dev.octodumb.letmeafk.mixin;

import dev.octodumb.letmeafk.AfkManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.SleepManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(SleepManager.class)
public class SleepManagerMixin {
    @ModifyVariable(
        method = "update(Ljava/util/List;)B",
        at = @At("HEAD"),
        ordinal = 0
    )
    private List<ServerPlayerEntity> update(List<ServerPlayerEntity> players) {
        return players.stream().filter(p -> !AfkManager.isAfk(p)).toList();
    }
}
