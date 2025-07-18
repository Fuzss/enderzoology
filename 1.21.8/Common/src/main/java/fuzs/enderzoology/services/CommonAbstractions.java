package fuzs.enderzoology.services;

import fuzs.puzzleslib.api.core.v1.ServiceProviderHelper;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public interface CommonAbstractions {
    CommonAbstractions INSTANCE = ServiceProviderHelper.load(CommonAbstractions.class);

    boolean canLivingConvert(LivingEntity entity, EntityType<? extends LivingEntity> outcome);

    void onLivingConvert(Mob entity, Mob outcome, ConversionParams conversionParams);
}
