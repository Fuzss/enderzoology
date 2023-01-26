package fuzs.enderzoology.core;

import fuzs.puzzleslib.util.PuzzlesUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public interface CommonAbstractions {
    CommonAbstractions INSTANCE = PuzzlesUtil.loadServiceProvider(CommonAbstractions.class);

    boolean onExplosionStart(Level level, Explosion explosion);

    GoalSelector getGoalSelector(Mob mob);

    boolean canLivingConvert(LivingEntity entity, EntityType<? extends LivingEntity> outcome, Consumer<Integer> timer);

    void onLivingConvert(LivingEntity entity, LivingEntity outcome);
}
