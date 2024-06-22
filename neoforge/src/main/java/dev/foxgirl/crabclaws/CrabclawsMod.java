package dev.foxgirl.crabclaws;

import dev.architectury.event.events.common.TickEvent;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import java.util.UUID;

@Mod("crabclaws")
public class CrabclawsMod {

    public CrabclawsMod(IEventBus modEventBus) {
        new CrabclawsImpl();
        modEventBus.addListener(this::onBuildCreativeModeTabContents);
        TickEvent.PLAYER_POST.register(this::onPlayerTick);
    }

    private static final AttributeModifier REACH_MODIFIER = new AttributeModifier(
        UUID.fromString("61df419d-4f62-4fee-a151-909344b439e7"),
        "crabclaws_extra_reach", 3.0, AttributeModifier.Operation.ADDITION
    );

    public void onPlayerTick(Player player) {
        var attribute = player.getAttribute(NeoForgeMod.BLOCK_REACH.value());
        if (attribute == null) return;

        if (
            player.getMainHandItem().getItem() == CrabclawsImpl.CRAB_CLAW.get() ||
            player.getOffhandItem().getItem() == CrabclawsImpl.CRAB_CLAW.get()
        ) {
            if (!attribute.hasModifier(REACH_MODIFIER)) {
                attribute.addTransientModifier(REACH_MODIFIER);
            }
        } else {
            if (attribute.hasModifier(REACH_MODIFIER)) {
                attribute.removeModifier(REACH_MODIFIER.getId());
            }
        }
    }

    public void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(CrabclawsImpl.CRAB_CLAW.get());
        }
    }

}
