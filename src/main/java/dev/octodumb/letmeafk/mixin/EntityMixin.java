package dev.octodumb.letmeafk.mixin;

import dev.octodumb.letmeafk.AfkManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    // None of this has worked
    @Inject(method = "collidesWith(Lnet/minecraft/entity/Entity;)B", at = @At("HEAD"), cancellable = true)
    private void collidesWith(Entity other, CallbackInfoReturnable<Boolean> info) {
        if(!((Entity)(Object)this instanceof PlayerEntity)) return;

        if(AfkManager.isAfk((PlayerEntity)(Object)this)) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "isPushable()B", at = @At("HEAD"), cancellable = true)
    private void isPushable(CallbackInfoReturnable<Boolean> info) {
        if(!((Entity)(Object)this instanceof PlayerEntity)) return;

        if(AfkManager.isAfk((PlayerEntity)(Object)this)) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "pushAwayFrom(Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
    private void pushAwayFrom(Entity entity, CallbackInfo info) {
        if(!((Entity)(Object)this instanceof PlayerEntity)) return;

        if(AfkManager.isAfk((PlayerEntity)(Object)this) || AfkManager.isAfk((PlayerEntity)entity)) {
            info.cancel();
        }
    }

    @Inject(method = "updateMovementInFluid(Lnet/minecraft/tag/TagKey;D)B", at = @At("HEAD"), cancellable = true)
    private void updateMovementInFluid(TagKey<Fluid> tag, double speed, CallbackInfoReturnable<Boolean> info) {
        if(!((Entity)(Object)this instanceof PlayerEntity)) return;

        if(AfkManager.isAfk((PlayerEntity)(Object)this)) {
            info.setReturnValue(false);
        }
    }
}
