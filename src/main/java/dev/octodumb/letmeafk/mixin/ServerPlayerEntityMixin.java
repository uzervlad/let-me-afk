package dev.octodumb.letmeafk.mixin;

import dev.octodumb.letmeafk.AfkManager;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
	@Inject(at = @At("HEAD"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)B", cancellable = true)
	private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		if(AfkManager.isAfk((PlayerEntity)(Object)this))
			info.setReturnValue(false);
	}
}
