package fuzs.enderzoology.client.init;

import fuzs.enderzoology.EnderZoology;
import fuzs.puzzleslib.api.client.init.v1.ModelLayerFactory;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class ModelLayerLocations {
    static final ModelLayerFactory MODEL_LAYERS = ModelLayerFactory.from(EnderZoology.MOD_ID);
    public static final ModelLayerLocation OWL = MODEL_LAYERS.registerModelLayer("owl");
    public static final ModelLayerLocation OWL_BABY = MODEL_LAYERS.registerModelLayer("owl_baby");
    public static final ModelLayerLocation FALLEN_KNIGHT = MODEL_LAYERS.registerModelLayer("fallen_knight");
    public static final ModelLayerLocation FALLEN_KNIGHT_INNER_ARMOR = MODEL_LAYERS.registerInnerArmorModelLayer(
            "fallen_knight");
    public static final ModelLayerLocation FALLEN_KNIGHT_OUTER_ARMOR = MODEL_LAYERS.registerOuterArmorModelLayer(
            "fallen_knight");
    public static final ModelLayerLocation ENDER_INFESTED_ZOMBIE = MODEL_LAYERS.registerModelLayer(
            "ender_infested_zombie");
    public static final ModelLayerLocation ENDER_INFESTED_ZOMBIE_INNER_ARMOR = MODEL_LAYERS.registerInnerArmorModelLayer(
            "ender_infested_zombie");
    public static final ModelLayerLocation ENDER_INFESTED_ZOMBIE_OUTER_ARMOR = MODEL_LAYERS.registerOuterArmorModelLayer(
            "ender_infested_zombie");
    public static final ModelLayerLocation ENDER_INFESTED_ZOMBIE_BABY = MODEL_LAYERS.registerModelLayer(
            "ender_infested_zombie_baby");
    public static final ModelLayerLocation ENDER_INFESTED_ZOMBIE_BABY_INNER_ARMOR = MODEL_LAYERS.registerInnerArmorModelLayer(
            "ender_infested_zombie_baby");
    public static final ModelLayerLocation ENDER_INFESTED_ZOMBIE_BABY_OUTER_ARMOR = MODEL_LAYERS.registerOuterArmorModelLayer(
            "ender_infested_zombie_baby");
    public static final ModelLayerLocation ENDERMINY = MODEL_LAYERS.registerModelLayer("enderminy");
    public static final ModelLayerLocation FALLEN_MOUNT = MODEL_LAYERS.registerModelLayer("fallen_mount");
    public static final ModelLayerLocation FALLEN_MOUNT_BABY = MODEL_LAYERS.registerModelLayer("fallen_mount_baby");
    public static final ModelLayerLocation WITHER_CAT = MODEL_LAYERS.registerModelLayer("wither_cat");
    public static final ModelLayerLocation WITHER_WITCH = MODEL_LAYERS.registerModelLayer("wither_witch");
    public static final ModelLayerLocation DIRE_WOLF = MODEL_LAYERS.registerModelLayer("dire_wolf");
    public static final ModelLayerLocation DIRE_WOLF_BABY = MODEL_LAYERS.registerModelLayer("dire_wolf_baby");
    public static final ModelLayerLocation CONCUSSION_CREEPER = MODEL_LAYERS.registerModelLayer("concussion_creeper");
    public static final ModelLayerLocation CONCUSSION_CREEPER_ARMOR = MODEL_LAYERS.registerModelLayer(
            "concussion_creeper",
            "armor");
}
