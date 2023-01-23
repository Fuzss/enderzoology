package fuzs.enderzoology.world.level;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnderExplosion extends Explosion {

    public EnderExplosion(Level level, @Nullable Entity entity, @Nullable DamageSource damageSource, @Nullable ExplosionDamageCalculator explosionDamageCalculator, double d, double e, double f, float g, boolean bl, BlockInteraction blockInteraction) {
        super(level, entity, damageSource, explosionDamageCalculator, d, e, f, g, bl, blockInteraction);
    }

    public static void onExplosionDetonate(Level level, Explosion explosion, List<Entity> entities) {
        if (!(explosion instanceof EnderExplosion enderExplosion)) return;
    }
}
