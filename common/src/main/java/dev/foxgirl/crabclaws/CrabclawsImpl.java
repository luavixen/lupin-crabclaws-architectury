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
            ResourceLocation.parse("crabclaws:extra_reach"),
            CrabclawsConfig.getConfig().clawExtraReachAmount, AttributeModifier.Operation.ADD_VALUE
        );

        TickEvent.PLAYER_POST.register(this::onPlayerPost);
        LootEvent.MODIFY_LOOT_TABLE.register(this::onModifyLootTable);

        itemRegister.register();
    }

    public CrabClawItem getCrabClawItem() {
        return crabClawItem.get();
    }

    protected void onPlayerPost(Player player) {
        var attribute = player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);
        if (attribute == null) return;

        if (
            player.getMainHandItem().getItem() == getCrabClawItem() ||
            player.getOffhandItem().getItem() == getCrabClawItem()
        ) {
            if (!attribute.hasModifier(reachAttributeModifier.id())) {
                attribute.addTransientModifier(reachAttributeModifier);
            }
        } else {
            if (attribute.hasModifier(reachAttributeModifier.id())) {
                attribute.removeModifier(reachAttributeModifier.id());
            }
        }
    }

    protected static final ResourceLocation UNDERWATER_RUIN_SMALL = ResourceLocation.parse("minecraft:chests/underwater_ruin_small");
    protected static final ResourceLocation UNDERWATER_RUIN_BIG = ResourceLocation.parse("minecraft:chests/underwater_ruin_big");

    protected void onModifyLootTable(ResourceKey<LootTable> key, LootEvent.LootTableModificationContext context, boolean isBuiltin) {
        if (CrabclawsConfig.getConfig().shouldSpawnClawsInRuins) {
            if (key.location().equals(UNDERWATER_RUIN_SMALL) || key.location().equals(UNDERWATER_RUIN_BIG)) {
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
