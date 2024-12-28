package fuzs.enderzoology.neoforge.core;

import fuzs.enderzoology.core.CommonAbstractions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.neoforged.neoforge.event.EventHooks;

import java.util.function.Consumer;

public class NeoForgeAbstractions implements CommonAbstractions {

    @Override
    public boolean canLivingConvert(LivingEntity entity, EntityType<? extends LivingEntity> outcome, Consumer<Integer> timer) {
        return EventHooks.canLivingConvert(entity, outcome, timer);
    }

    @Override
    public void onLivingConvert(Mob entity, Mob outcome) {
        EventHooks.onLivingConvert(entity, outcome);
    }
}
