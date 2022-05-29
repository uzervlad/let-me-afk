package dev.octodumb.letmeafk.mixin;

import com.google.common.base.Predicates;
import dev.octodumb.letmeafk.AfkManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(EntityPredicates.class)
public class EntityPredicatesMixin {
    // Didn't work
    @Inject(method = "canBePushedBy(Lnet/minecraft/entity/Entity;)Ljava/util/function/Predicate;", at = @At("HEAD"))
    private static void canBePushedBy(Entity entity, CallbackInfoReturnable<Predicate<Entity>> info) {
        if(!(entity instanceof PlayerEntity)) return;

        if(AfkManager.isAfk((PlayerEntity)entity)) {
            info.setReturnValue(Predicates.alwaysFalse());
        }
    }
}
