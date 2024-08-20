package dev.foxgirl.crabclaws;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod("crabclaws")
public class CrabclawsMod {

    public CrabclawsMod(IEventBus modEventBus) {
        new CrabclawsImpl();
        modEventBus.addListener(this::onBuildCreativeModeTabContents);
    }

    private void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(CrabclawsImpl.getInstance().getCrabClawItem());
        }
    }

}
