package dev.foxgirl.crabclaws.mixin;

import dev.foxgirl.crabclaws.CrabclawsConfig;
import dev.foxgirl.crabclaws.CrabclawsImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MixinServerGamePacketListenerImpl {

    @Shadow
    public static double MAX_INTERACTION_DISTANCE;

    static {
        MAX_INTERACTION_DISTANCE = Math.max(
            MAX_INTERACTION_DISTANCE,
            Mth.square(7.0 + CrabclawsConfig.getConfig().clawExtraReachAmount)
        );
    }

}
