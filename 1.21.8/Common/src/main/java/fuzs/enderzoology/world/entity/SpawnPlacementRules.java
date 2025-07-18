package fuzs.enderzoology.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;

public class SpawnPlacementRules {

    public static boolean checkDireWolfSpawnRules(EntityType<? extends Mob> entity, ServerLevelAccessor level, EntitySpawnReason entitySpawnReason, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(BlockTags.WOLVES_SPAWNABLE_ON) &&
                checkMonsterSpawnRules(entity, level, entitySpawnReason, pos, random);
    }

    public static boolean checkMonsterSpawnRules(EntityType<? extends Mob> entity, ServerLevelAccessor level, EntitySpawnReason entitySpawnReason, BlockPos pos, RandomSource random) {
        return level.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(level, pos, random) &&
                Mob.checkMobSpawnRules(entity, level, entitySpawnReason, pos, random);
    }

    public static boolean checkSurfaceSpawnRules(EntityType<? extends Monster> entity, ServerLevelAccessor level, EntitySpawnReason entitySpawnReason, BlockPos pos, RandomSource random) {
        return checkMonsterSpawnRules(entity, level, entitySpawnReason, pos, random) &&
                (entitySpawnReason == EntitySpawnReason.SPAWNER || level.canSeeSky(pos));
    }

    public static boolean checkOwlSpawnRules(EntityType<? extends Animal> entity, LevelAccessor level, EntitySpawnReason entitySpawnReason, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(BlockTags.PARROTS_SPAWNABLE_ON) &&
                level.getRawBrightness(pos, 0) > 8;
    }
}
