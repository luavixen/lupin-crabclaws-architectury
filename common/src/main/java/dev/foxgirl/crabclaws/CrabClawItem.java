package dev.foxgirl.crabclaws;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class CrabClawItem extends Item {

    public CrabClawItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON));
    }

}
