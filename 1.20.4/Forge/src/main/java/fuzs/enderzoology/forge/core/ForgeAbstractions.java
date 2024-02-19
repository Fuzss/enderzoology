package fuzs.enderzoology.forge.core;

import fuzs.enderzoology.core.CommonAbstractions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.function.Consumer;

public class ForgeAbstractions implements CommonAbstractions {

    @Override
    public boolean canLivingConvert(LivingEntity entity, EntityType<? extends LivingEntity> outcome, Consumer<Integer> timer) {
        return ForgeEventFactory.canLivingConvert(entity, outcome, timer);
    }

    @Override
    public void onLivingConvert(Mob entity, Mob outcome) {
        ForgeEventFactory.onLivingConvert(entity, outcome);
    }
}
