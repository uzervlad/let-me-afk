package dev.octodumb.letmeafk.mixin;

import dev.octodumb.letmeafk.AfkManager;
import dev.octodumb.letmeafk.LetMeAfk;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    // None of this has worked
    @Inject(method = "isPushable()B", at = @At("HEAD"), cancellable = true)
    private void isPushable(CallbackInfoReturnable<Boolean> info) {
        if(!((LivingEntity)(Object)this instanceof PlayerEntity)) return;

        if(AfkManager.isAfk((ServerPlayerEntity)(Object)this)) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "pushAway(Lnet/minecraft/entity/Entity;)B", at = @At("HEAD"), cancellable = true)
    private void pushAway(Entity entity, CallbackInfo info) {
        if(!((LivingEntity)(Object)this instanceof PlayerEntity)) return;

        LetMeAfk.LOGGER.info("{} pushed {}", this, entity);

        LetMeAfk.LOGGER.info("AFKs: {} | {}", AfkManager.isAfk((PlayerEntity)(Object)this), AfkManager.isAfk((PlayerEntity)entity));

        if(AfkManager.isAfk((PlayerEntity)(Object)this) || AfkManager.isAfk((PlayerEntity)entity)) {
            LetMeAfk.LOGGER.info("Cancelled pushing!");
            info.cancel();
        }
    }
}
