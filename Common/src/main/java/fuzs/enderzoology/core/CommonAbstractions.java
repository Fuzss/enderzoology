package fuzs.enderzoology.core;

import fuzs.puzzleslib.util.PuzzlesUtil;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

public interface CommonAbstractions {
    CommonAbstractions INSTANCE = PuzzlesUtil.loadServiceProvider(CommonAbstractions.class);

    boolean onExplosionStart(Level level, Explosion explosion);
}
