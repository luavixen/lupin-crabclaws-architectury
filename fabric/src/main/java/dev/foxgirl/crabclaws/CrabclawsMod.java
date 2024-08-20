package dev.foxgirl.crabclaws;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;

public class CrabclawsMod implements ModInitializer {

    @Override
    public void onInitialize() {
        new CrabclawsImpl();
        ItemGroupEvents
            .modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
            .register(entries -> entries.prepend(CrabclawsImpl.getInstance().getCrabClawItem()));
    }

}
