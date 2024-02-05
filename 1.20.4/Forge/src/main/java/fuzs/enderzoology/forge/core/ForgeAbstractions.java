package fuzs.enderzoology.forge.core;

import fuzs.enderzoology.core.CommonAbstractions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ForgeAbstractions implements CommonAbstractions {

    @Override
    public boolean onExplosionStart(Level level, Explosion explosion) {
        return ForgeEventFactory.onExplosionStart(level, explosion);
    }

    @Override
    public boolean canLivingConvert(LivingEntity entity, EntityType<? extends LivingEntity> outcome, Consumer<Integer> timer) {
        return ForgeEventFactory.canLivingConvert(entity, outcome, timer);
    }

    @Override
    public void onLivingConvert(LivingEntity entity, LivingEntity outcome) {
        ForgeEventFactory.onLivingConvert(entity, outcome);
    }

    @Override
    public boolean getMobGriefingEvent(Level level, @Nullable Entity entity) {
        return ForgeEventFactory.getMobGriefingEvent(level, entity);
    }
}
