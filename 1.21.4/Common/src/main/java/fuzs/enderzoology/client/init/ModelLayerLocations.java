package fuzs.enderzoology.client.init;

import fuzs.enderzoology.EnderZoology;
import fuzs.puzzleslib.api.client.init.v1.ModelLayerFactory;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class ModelLayerLocations {
    static final ModelLayerFactory REGISTRY = ModelLayerFactory.from(EnderZoology.MOD_ID);
    public static final ModelLayerLocation OWL = REGISTRY.register("owl");
    public static final ModelLayerLocation OWL_BABY = REGISTRY.register("owl_baby");
    public static final ModelLayerLocation FALLEN_KNIGHT = REGISTRY.register("fallen_knight");
    public static final ModelLayerLocation FALLEN_KNIGHT_INNER_ARMOR = REGISTRY.registerInnerArmor("fallen_knight");
    public static final ModelLayerLocation FALLEN_KNIGHT_OUTER_ARMOR = REGISTRY.registerOuterArmor("fallen_knight");
    public static final ModelLayerLocation ENDER_INFESTED_ZOMBIE = REGISTRY.register("ender_infested_zombie");
    public static final ModelLayerLocation ENDER_INFESTED_ZOMBIE_INNER_ARMOR = REGISTRY.registerInnerArmor("ender_infested_zombie");
    public static final ModelLayerLocation ENDER_INFESTED_ZOMBIE_OUTER_ARMOR = REGISTRY.registerOuterArmor("ender_infested_zombie");
    public static final ModelLayerLocation ENDER_INFESTED_ZOMBIE_BABY = REGISTRY.register("ender_infested_zombie_baby");
    public static final ModelLayerLocation ENDER_INFESTED_ZOMBIE_BABY_INNER_ARMOR = REGISTRY.registerInnerArmor("ender_infested_zombie_baby");
    public static final ModelLayerLocation ENDER_INFESTED_ZOMBIE_BABY_OUTER_ARMOR = REGISTRY.registerOuterArmor("ender_infested_zombie_baby");
    public static final ModelLayerLocation ENDERMINY = REGISTRY.register("enderminy");
    public static final ModelLayerLocation FALLEN_MOUNT = REGISTRY.register("fallen_mount");
    public static final ModelLayerLocation FALLEN_MOUNT_BABY = REGISTRY.register("fallen_mount_baby");
    public static final ModelLayerLocation WITHER_CAT = REGISTRY.register("wither_cat");
    public static final ModelLayerLocation WITHER_WITCH = REGISTRY.register("wither_witch");
    public static final ModelLayerLocation DIRE_WOLF = REGISTRY.register("dire_wolf");
    public static final ModelLayerLocation DIRE_WOLF_BABY = REGISTRY.register("dire_wolf_baby");
    public static final ModelLayerLocation CONCUSSION_CREEPER = REGISTRY.register("concussion_creeper");
    public static final ModelLayerLocation CONCUSSION_CREEPER_ARMOR = REGISTRY.register("concussion_creeper", "armor");
}
