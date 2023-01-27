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
}
