package dev.octodumb.letmeafk.mixin;

import dev.octodumb.letmeafk.AfkManager;
import dev.octodumb.letmeafk.LetMeAfk;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    private ServerPlayerEntity getThisPlayer() {
        return ((ServerPlayNetworkHandler)(Object)this).player;
    }

    private boolean checkAfk() {
        return AfkManager.isAfk(getThisPlayer());
    }

    private void offAfk(String method) {
        if(checkAfk()) {
            var player = getThisPlayer();
            AfkManager.toggleAfk(player);
            LetMeAfk.LOGGER.debug("AFK turned off by {} for player {}", method, player.getDisplayName().asString());
        }
    }

    @Inject(method = "tick()V", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo info) {
        if(checkAfk())
            info.cancel();
    }

    @Inject(method = "onPlayerMove(Lnet/minecraft/network/packet/c2s/play/PlayerMoveC2SPacket;)V", at = @At("HEAD"))
    private void onPlayerMove(PlayerMoveC2SPacket packet, CallbackInfo info) {
        if(packet.changesPosition()) {
            var player = getThisPlayer();
            var x1 = player.getX();
            var y1 = player.getY();
            var z1 = player.getZ();

            var x2 = packet.getX(x1);
            var y2 = packet.getY(y1);
            var z2 = packet.getZ(z1);

            if(x1 != x2 || y1 != y2 || z1 != z2)
                offAfk("onPlayerMove");
        }
    }

    @Inject(method = "onPlayerAction(Lnet/minecraft/network/packet/c2s/play/PlayerActionC2SPacket;)V", at = @At("HEAD"))
    private void onPlayerAction(PlayerActionC2SPacket packet, CallbackInfo info) {
        offAfk("onPlayerAction");
    }

    @Inject(method = "onPlayerInteractBlock(Lnet/minecraft/network/packet/c2s/play/PlayerInteractBlockC2SPacket;)V", at = @At("HEAD"))
    private void onPlayerInteractBlock(PlayerInteractBlockC2SPacket packet, CallbackInfo info) {
        offAfk("onPlayerInteractBlock");
    }

    @Inject(method = "onPlayerInteractItem(Lnet/minecraft/network/packet/c2s/play/PlayerInteractItemC2SPacket;)V", at = @At("HEAD"))
    private void onPlayerInteractItem(PlayerInteractItemC2SPacket packet, CallbackInfo info) {
        offAfk("onPlayerInteractItem");
    }

    @Inject(method = "onPlayerInteractEntity(Lnet/minecraft/network/packet/c2s/play/PlayerInteractEntityC2SPacket;)V", at = @At("HEAD"))
    private void onPlayerInteractEntity(PlayerInteractEntityC2SPacket packet, CallbackInfo info) {
        offAfk("onPlayerInteractEntity");
    }

    @Inject(method = "disconnect(Lnet/minecraft/text/Text;)V", at = @At("HEAD"))
    private void disconnect(Text text, CallbackInfo info) {
        AfkManager.remove(getThisPlayer());
    }
}
