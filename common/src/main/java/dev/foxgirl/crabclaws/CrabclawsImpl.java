package dev.foxgirl.crabclaws;

import dev.architectury.event.events.common.LootEvent;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class CrabclawsImpl {

    public static final DeferredRegister<Item> ITEMS;
    public static final RegistrySupplier<Item> CRAB_CLAW;;

    static {
        ITEMS = DeferredRegister.create("crabclaws", Registries.ITEM);
        CRAB_CLAW = ITEMS.register("crab_claw", CrabClawItem::new);
    }

    public CrabclawsImpl() {
        LootEvent.MODIFY_LOOT_TABLE.register(this::onModifyLootTable);
        ITEMS.register();
    }

    private static final ResourceLocation UNDERWATER_RUIN_SMALL = ResourceLocation.tryParse("minecraft:chests/underwater_ruin_small");
    private static final ResourceLocation UNDERWATER_RUIN_BIG = ResourceLocation.tryParse("minecraft:chests/underwater_ruin_big");

    private void onModifyLootTable(LootDataManager manager, ResourceLocation id, LootEvent.LootTableModificationContext context, boolean isBuiltin) {
        if (id.equals(UNDERWATER_RUIN_SMALL) || id.equals(UNDERWATER_RUIN_BIG)) {
            context.addPool(
                LootPool
                    .lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(EmptyLootItem.emptyItem().setWeight(7))
                    .add(LootItem.lootTableItem(CRAB_CLAW.get()).setWeight(1))
            );
        }
    }

}
