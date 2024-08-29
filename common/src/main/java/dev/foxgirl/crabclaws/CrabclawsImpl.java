package dev.foxgirl.crabclaws;

import dev.architectury.event.events.common.LootEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.UUID;

public class CrabclawsImpl {

    private static CrabclawsImpl INSTANCE = null;

    public static CrabclawsImpl getInstance() {
        return INSTANCE;
    }

    public final DeferredRegister<Item> itemRegister;
    public final RegistrySupplier<CrabClawItem> crabClawItem;

    public final AttributeModifier reachAttributeModifier;

    public CrabclawsImpl() {
        INSTANCE = this;

        itemRegister = DeferredRegister.create("crabclaws", Registries.ITEM);
        crabClawItem = itemRegister.register("crab_claw", CrabClawItem::new);

        reachAttributeModifier = new AttributeModifier(
            UUID.fromString("61df419d-4f62-4fee-a151-909344b439e7"), "crabclaws_extra_reach",
            CrabclawsConfig.getConfig().clawExtraReachAmount, AttributeModifier.Operation.ADDITION
        );

        TickEvent.PLAYER_POST.register(this::onPlayerPost);
        LootEvent.MODIFY_LOOT_TABLE.register(this::onModifyLootTable);

        itemRegister.register();
    }

    public CrabClawItem getCrabClawItem() {
        return crabClawItem.get();
    }

    protected void onPlayerPost(Player player) {
        /*
        var attribute = player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);
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
        */
    }

    protected static final ResourceLocation UNDERWATER_RUIN_SMALL = ResourceLocation.tryParse("minecraft:chests/underwater_ruin_small");
    protected static final ResourceLocation UNDERWATER_RUIN_BIG = ResourceLocation.tryParse("minecraft:chests/underwater_ruin_big");

    protected void onModifyLootTable(LootDataManager manager, ResourceLocation id, LootEvent.LootTableModificationContext context, boolean isBuiltin) {
        if (CrabclawsConfig.getConfig().shouldSpawnClawsInRuins) {
            if (id.equals(UNDERWATER_RUIN_SMALL) || id.equals(UNDERWATER_RUIN_BIG)) {
                context.addPool(
                    LootPool
                        .lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(EmptyLootItem.emptyItem().setWeight(CrabclawsConfig.getConfig().probabilityOfClawsInRuins))
                        .add(LootItem.lootTableItem(getCrabClawItem()).setWeight(1))
                );
            }
        }
    }

}
