package fuzs.enderzoology.client.init;

import fuzs.enderzoology.EnderZoology;
import fuzs.puzzleslib.api.client.init.v1.ModelLayerFactory;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.ArmorModelSet;

public class ModModelLayers {
    static final ModelLayerFactory MODEL_LAYERS = ModelLayerFactory.from(EnderZoology.MOD_ID);
    public static final ModelLayerLocation OWL = MODEL_LAYERS.registerModelLayer("owl");
    public static final ModelLayerLocation OWL_EYES = MODEL_LAYERS.registerModelLayer("owl", "eyes");
    public static final ModelLayerLocation OWL_BABY = MODEL_LAYERS.registerModelLayer("owl_baby");
    public static final ModelLayerLocation OWL_BABY_EYES = MODEL_LAYERS.registerModelLayer("owl_baby", "eyes");
    public static final ModelLayerLocation FALLEN_KNIGHT = MODEL_LAYERS.registerModelLayer("fallen_knight");
    public static final ArmorModelSet<ModelLayerLocation> FALLEN_KNIGHT_ARMOR = MODEL_LAYERS.registerArmorSet(
            "fallen_knight");
    public static final ModelLayerLocation ENDER_INFESTED_ZOMBIE = MODEL_LAYERS.registerModelLayer(
            "ender_infested_zombie");
    public static final ArmorModelSet<ModelLayerLocation> ENDER_INFESTED_ZOMBIE_ARMOR = MODEL_LAYERS.registerArmorSet(
            "ender_infested_zombie");
    public static final ModelLayerLocation ENDER_INFESTED_ZOMBIE_BABY = MODEL_LAYERS.registerModelLayer(
            "ender_infested_zombie_baby");
    public static final ArmorModelSet<ModelLayerLocation> ENDER_INFESTED_ZOMBIE_BABY_ARMOR = MODEL_LAYERS.registerArmorSet(
            "ender_infested_zombie_baby");
    public static final ModelLayerLocation ENDERMINY = MODEL_LAYERS.registerModelLayer("enderminy");
    public static final ModelLayerLocation FALLEN_MOUNT = MODEL_LAYERS.registerModelLayer("fallen_mount");
    public static final ModelLayerLocation FALLEN_MOUNT_ARMOR = MODEL_LAYERS.registerModelLayer("fallen_mount",
            "armor");
    public static final ModelLayerLocation FALLEN_MOUNT_BABY = MODEL_LAYERS.registerModelLayer("fallen_mount_baby");
    public static final ModelLayerLocation FALLEN_MOUNT_BABY_ARMOR = MODEL_LAYERS.registerModelLayer("fallen_mount_baby",
            "armor");
    public static final ModelLayerLocation WITHER_CAT = MODEL_LAYERS.registerModelLayer("wither_cat");
    public static final ModelLayerLocation WITHER_WITCH = MODEL_LAYERS.registerModelLayer("wither_witch");
    public static final ModelLayerLocation DIRE_WOLF = MODEL_LAYERS.registerModelLayer("dire_wolf");
    public static final ModelLayerLocation DIRE_WOLF_BABY = MODEL_LAYERS.registerModelLayer("dire_wolf_baby");
    public static final ModelLayerLocation CONCUSSION_CREEPER = MODEL_LAYERS.registerModelLayer("concussion_creeper");
    public static final ModelLayerLocation CONCUSSION_CREEPER_ARMOR = MODEL_LAYERS.registerModelLayer(
            "concussion_creeper",
            "armor");
}
