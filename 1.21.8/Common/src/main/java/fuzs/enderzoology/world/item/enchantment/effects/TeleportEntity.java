package fuzs.enderzoology.world.item.enchantment.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.enderzoology.world.level.EnderTeleportHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record TeleportEntity(LevelBasedValue minRange, LevelBasedValue maxRange) implements EnchantmentEntityEffect {
    public static final MapCodec<TeleportEntity> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LevelBasedValue.CODEC.fieldOf("min_range").forGetter(TeleportEntity::minRange),
            LevelBasedValue.CODEC.fieldOf("max_range").forGetter(TeleportEntity::maxRange)
    ).apply(instance, TeleportEntity::new));

    public TeleportEntity(LevelBasedValue teleportRange) {
        this(teleportRange, teleportRange);
    }

    @Override
    public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 origin) {
        if (entity instanceof LivingEntity livingEntity) {
            float teleportRange = Mth.randomBetween(entity.getRandom(),
                    this.minRange.calculate(enchantmentLevel),
                    this.maxRange.calculate(enchantmentLevel)
            );
            EnderTeleportHelper.teleportEntity(level, livingEntity, (int) teleportRange, false);
        }
    }

    @Override
    public MapCodec<TeleportEntity> codec() {
        return CODEC;
    }
}
