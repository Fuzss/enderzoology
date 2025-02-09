package fuzs.enderzoology.init;

import fuzs.enderzoology.world.item.OwlEggItem;
import fuzs.enderzoology.world.level.EnderExplosionType;
import net.minecraft.core.Holder;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.TeleportRandomlyConsumeEffect;

public class ModItems {
    private static final FoodProperties ENDERIOS_FOOD_PROPERTIES = new FoodProperties.Builder().nutrition(6)
            .saturationModifier(0.6F)
            .alwaysEdible()
            .build();
    private static final Consumable ENDERIOS_CONSUMABLE = Consumables.defaultFood()
            .onConsume(new TeleportRandomlyConsumeEffect())
            .build();

    public static final Holder.Reference<Item> CONCUSSION_CHARGE_ITEM = ModRegistry.REGISTRIES.registerBlockItem(
            ModBlocks.CONCUSSION_CHARGE_BLOCK);
    public static final Holder.Reference<Item> CONFUSING_CHARGE_ITEM = ModRegistry.REGISTRIES.registerBlockItem(
            ModBlocks.CONFUSING_CHARGE_BLOCK);
    public static final Holder.Reference<Item> ENDER_CHARGE_ITEM = ModRegistry.REGISTRIES.registerBlockItem(ModBlocks.ENDER_CHARGE_BLOCK);
    public static final Holder.Reference<Item> CONFUSING_POWDER_ITEM = ModRegistry.REGISTRIES.registerItem(
            "confusing_powder");
    public static final Holder.Reference<Item> ENDER_FRAGMENT_ITEM = ModRegistry.REGISTRIES.registerItem(
            "ender_fragment");
    public static final Holder.Reference<Item> HUNTING_BOW_ITEM = ModRegistry.REGISTRIES.registerItem("hunting_bow",
            BowItem::new,
            () -> new Item.Properties().durability(546));
    public static final Holder.Reference<Item> OWL_EGG_ITEM = ModRegistry.REGISTRIES.registerItem("owl_egg",
            OwlEggItem::new,
            () -> new Item.Properties().stacksTo(16));
    public static final Holder.Reference<Item> WITHERING_DUST_ITEM = ModRegistry.REGISTRIES.registerItem(
            "withering_dust");
    public static final Holder.Reference<Item> ENDER_CHARGE_MINECART_ITEM = ModRegistry.REGISTRIES.registerItem(
            "ender_charge_minecart",
            (Item.Properties properties) -> new MinecartItem(EnderExplosionType.ENDER.getMinecartEntityType(),
                    properties),
            () -> new Item.Properties().stacksTo(1));
    public static final Holder.Reference<Item> CONFUSING_CHARGE_MINECART_ITEM = ModRegistry.REGISTRIES.registerItem(
            "confusing_charge_minecart",
            (Item.Properties properties) -> new MinecartItem(EnderExplosionType.CONFUSION.getMinecartEntityType(),
                    properties),
            () -> new Item.Properties().stacksTo(1));
    public static final Holder.Reference<Item> CONCUSSION_CHARGE_MINECART_ITEM = ModRegistry.REGISTRIES.registerItem(
            "concussion_charge_minecart",
            (Item.Properties properties) -> new MinecartItem(EnderExplosionType.CONCUSSION.getMinecartEntityType(),
                    properties),
            () -> new Item.Properties().stacksTo(1));
    public static final Holder.Reference<Item> ENDERIOS_ITEM = ModRegistry.REGISTRIES.registerItem("enderios",
            () -> new Item.Properties().stacksTo(1)
                    .food(ENDERIOS_FOOD_PROPERTIES, ENDERIOS_CONSUMABLE)
                    .usingConvertsTo(Items.BOWL));
    public static final Holder.Reference<Item> CONCUSSION_CREEPER_SPAWN_EGG_ITEM = ModRegistry.REGISTRIES.registerSpawnEggItem(
            ModEntityTypes.CONCUSSION_CREEPER_ENTITY_TYPE,
            0x56FF8E,
            0xFF0A22);
    public static final Holder.Reference<Item> INFESTED_ZOMBIE_SPAWN_EGG_ITEM = ModRegistry.REGISTRIES.registerSpawnEggItem(
            ModEntityTypes.INFESTED_ZOMBIE_ENTITY_TYPE,
            0x132F55,
            0x2B2D1C);
    public static final Holder.Reference<Item> ENDERMINY_SPAWN_EGG_ITEM = ModRegistry.REGISTRIES.registerSpawnEggItem(
            ModEntityTypes.ENDERMINY_ENTITY_TYPE,
            0x27624D,
            0x212121);
    public static final Holder.Reference<Item> DIRE_WOLF_SPAWN_EGG_ITEM = ModRegistry.REGISTRIES.registerSpawnEggItem(
            ModEntityTypes.DIRE_WOLF_ENTITY_TYPE,
            0x606060,
            0xA0A0A0);
    public static final Holder.Reference<Item> FALLEN_MOUNT_SPAWN_EGG_ITEM = ModRegistry.REGISTRIES.registerSpawnEggItem(
            ModEntityTypes.FALLEN_MOUNT_ENTITY_TYPE,
            0x365A25,
            0xA0A0A0);
    public static final Holder.Reference<Item> WITHER_CAT_SPAWN_EGG_ITEM = ModRegistry.REGISTRIES.registerSpawnEggItem(
            ModEntityTypes.WITHER_CAT_ENTITY_TYPE,
            0x303030,
            0xFFFFFF);
    public static final Holder.Reference<Item> WITHER_WITCH_SPAWN_EGG_ITEM = ModRegistry.REGISTRIES.registerSpawnEggItem(
            ModEntityTypes.WITHER_WITCH_ENTITY_TYPE,
            0x26520D,
            0x905E43);
    public static final Holder.Reference<Item> OWL_SPAWN_EGG_ITEM = ModRegistry.REGISTRIES.registerSpawnEggItem(
            ModEntityTypes.OWL_ENTITY_TYPE,
            0xC17949,
            0xFFDDC6);
    public static final Holder.Reference<Item> FALLEN_KNIGHT_SPAWN_EGG_ITEM = ModRegistry.REGISTRIES.registerSpawnEggItem(
            ModEntityTypes.FALLEN_KNIGHT_ENTITY_TYPE,
            0x365A25,
            0xA0A0A0);

    public static void bootstrap() {
        // NO-OP
    }
}
