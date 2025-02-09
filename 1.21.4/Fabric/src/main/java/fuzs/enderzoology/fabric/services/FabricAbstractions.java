package fuzs.enderzoology.fabric.services;

import fuzs.enderzoology.services.CommonAbstractions;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class FabricAbstractions implements CommonAbstractions {

    @Override
    public boolean canLivingConvert(LivingEntity entity, EntityType<? extends LivingEntity> outcome) {
        return true;
    }

    @Override
    public void onLivingConvert(Mob entity, Mob outcome, ConversionParams conversionParams) {
        ServerLivingEntityEvents.MOB_CONVERSION.invoker().onConversion(entity, outcome, conversionParams);
    }
}
