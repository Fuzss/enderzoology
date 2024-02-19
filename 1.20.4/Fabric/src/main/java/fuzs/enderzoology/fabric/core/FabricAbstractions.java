package fuzs.enderzoology.fabric.core;

import fuzs.enderzoology.core.CommonAbstractions;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.function.Consumer;

public class FabricAbstractions implements CommonAbstractions {

    @Override
    public boolean canLivingConvert(LivingEntity entity, EntityType<? extends LivingEntity> outcome, Consumer<Integer> timer) {
        return true;
    }

    @Override
    public void onLivingConvert(Mob entity, Mob outcome) {
        ServerLivingEntityEvents.MOB_CONVERSION.invoker().onConversion(entity, outcome, false);
    }
}
