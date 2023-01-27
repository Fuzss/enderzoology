package fuzs.enderzoology.client.init;

import fuzs.enderzoology.EnderZoology;
import fuzs.puzzleslib.client.model.geom.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class ClientModRegistry {
    private static final ModelLayerRegistry REGISTRY = ModelLayerRegistry.of(EnderZoology.MOD_ID);
    public static final ModelLayerLocation OWL = REGISTRY.register("owl");
    public static final ModelLayerLocation FALLEN_KNIGHT = REGISTRY.register("fallen_knight");
    public static final ModelLayerLocation FALLEN_KNIGHT_INNER_ARMOR = REGISTRY.registerInnerArmor("fallen_knight");
    public static final ModelLayerLocation FALLEN_KNIGHT_OUTER_ARMOR = REGISTRY.registerOuterArmor("fallen_knight");
    public static final ModelLayerLocation ENDER_INFESTED_ZOMBIE = REGISTRY.register("ender_infested_zombie");
    public static final ModelLayerLocation ENDER_INFESTED_ZOMBIE_INNER_ARMOR = REGISTRY.registerInnerArmor("ender_infested_zombie");
    public static final ModelLayerLocation ENDER_INFESTED_ZOMBIE_OUTER_ARMOR = REGISTRY.registerOuterArmor("ender_infested_zombie");
    public static final ModelLayerLocation ENDERMINY = REGISTRY.register("enderminy");
    public static final ModelLayerLocation FALLEN_MOUNT = REGISTRY.register("fallen_mount");
    public static final ModelLayerLocation FALLEN_MOUNT_ARMOR = REGISTRY.register("fallen_mount", "armor");
    public static final ModelLayerLocation WITHER_CAT = REGISTRY.register("wither_cat");
    public static final ModelLayerLocation WITHER_WITCH = REGISTRY.register("wither_witch");
    public static final ModelLayerLocation DIRE_WOLF = REGISTRY.register("dire_wolf");
    public static final ModelLayerLocation CONCUSSION_CREEPER = REGISTRY.register("concussion_creeper");
    public static final ModelLayerLocation CONCUSSION_CREEPER_ARMOR = REGISTRY.register("concussion_creeper", "armor");
}
