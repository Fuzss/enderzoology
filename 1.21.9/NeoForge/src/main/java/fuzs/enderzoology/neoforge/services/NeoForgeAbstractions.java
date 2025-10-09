package fuzs.enderzoology.neoforge.services;

import fuzs.enderzoology.services.CommonAbstractions;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.neoforged.neoforge.event.EventHooks;
import org.apache.commons.lang3.function.Consumers;

public class NeoForgeAbstractions implements CommonAbstractions {

    @Override
    public boolean canLivingConvert(LivingEntity entity, EntityType<? extends LivingEntity> outcome) {
        return EventHooks.canLivingConvert(entity, outcome, Consumers.nop());
    }

    @Override
    public void onLivingConvert(Mob entity, Mob outcome, ConversionParams conversionParams) {
        EventHooks.onLivingConvert(entity, outcome);
    }
}
