package dev.foxgirl.crabclaws;

import dev.architectury.event.events.common.LootEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
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
        TickEvent.PLAYER_POST.register(this::onPlayerPost);
        LootEvent.MODIFY_LOOT_TABLE.register(this::onModifyLootTable);
        ITEMS.register();
    }

    private static final AttributeModifier REACH_MODIFIER =
        new AttributeModifier(ResourceLocation.parse("crabclaws:extra_reach"), 3.0, AttributeModifier.Operation.ADD_VALUE);

    private void onPlayerPost(Player player) {
        var attribute = player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);
        if (attribute == null) return;

        if (
            player.getMainHandItem().getItem() == CRAB_CLAW.get() ||
            player.getOffhandItem().getItem() == CRAB_CLAW.get()
        ) {
            if (!attribute.hasModifier(REACH_MODIFIER.id())) {
                attribute.addTransientModifier(REACH_MODIFIER);
            }
        } else {
            if (attribute.hasModifier(REACH_MODIFIER.id())) {
                attribute.removeModifier(REACH_MODIFIER.id());
            }
        }
    }

    private static final ResourceLocation UNDERWATER_RUIN_SMALL = ResourceLocation.parse("minecraft:chests/underwater_ruin_small");
    private static final ResourceLocation UNDERWATER_RUIN_BIG = ResourceLocation.parse("minecraft:chests/underwater_ruin_big");

    private void onModifyLootTable(ResourceKey<LootTable> key, LootEvent.LootTableModificationContext context, boolean isBuiltin) {
        if (key.location().equals(UNDERWATER_RUIN_SMALL) || key.location().equals(UNDERWATER_RUIN_BIG)) {
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
