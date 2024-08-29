package dev.foxgirl.crabclaws.mixin;

import dev.foxgirl.crabclaws.CrabclawsConfig;
import dev.foxgirl.crabclaws.CrabclawsImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public abstract class MixinMultiPlayerGameMode {

    @Unique
    private ClientPacketListener connection;

    @Inject(method = "getPickRange()F", at = @At("RETURN"), cancellable = true)
    private void injected$getPickRange(CallbackInfoReturnable<Float> info) {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            if (
                player.getMainHandItem().getItem() == CrabclawsImpl.getInstance().getCrabClawItem() ||
                player.getOffhandItem().getItem() == CrabclawsImpl.getInstance().getCrabClawItem()
            ) {
                info.setReturnValue(info.getReturnValueF() + (float) CrabclawsConfig.getConfig().clawExtraReachAmount);
            }
        }
    }

}
