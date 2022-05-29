package dev.octodumb.letmeafk.mixin;

import dev.octodumb.letmeafk.AfkManager;
import dev.octodumb.letmeafk.LetMeAfk;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    // None of this has worked
    @Inject(method = "isPushedByFluids()B", at = @At("HEAD"), cancellable = true)
    private void isPushedByFluids(CallbackInfoReturnable<Boolean> info) {
        if(AfkManager.isAfk((PlayerEntity)(Object)this)) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "collideWithEntity(Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
    private void collideWithEntity(Entity entity, CallbackInfo info) {
        if(AfkManager.isAfk((PlayerEntity)(Object)this))
            info.cancel();
    }
}
