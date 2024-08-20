package dev.foxgirl.crabclaws;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForgeMod;

public class CrabclawsNeoForgeImpl extends CrabclawsImpl {

    @Override
    protected void onPlayerPost(Player player) {
        var attribute = player.getAttribute(NeoForgeMod.BLOCK_REACH.value());
        if (attribute == null) return;

        if (
            player.getMainHandItem().getItem() == getCrabClawItem() ||
            player.getOffhandItem().getItem() == getCrabClawItem()
        ) {
            if (!attribute.hasModifier(reachAttributeModifier)) {
                attribute.addTransientModifier(reachAttributeModifier);
            }
        } else {
            if (attribute.hasModifier(reachAttributeModifier)) {
                attribute.removeModifier(reachAttributeModifier.getId());
            }
        }
    }

}
