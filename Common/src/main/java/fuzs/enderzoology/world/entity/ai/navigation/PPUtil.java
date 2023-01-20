package fuzs.enderzoology.world.entity.ai.navigation;

import net.minecraft.world.level.pathfinder.Node;

import javax.annotation.Nonnull;

public class PPUtil {

    public static Node getPrevious(@Nonnull Node pp) {
        return pp.cameFrom;
    }

    public static void setPrevious(@Nonnull Node pp, @Nonnull Node prev) {
        pp.cameFrom = prev;
    }

    public static float getTotalPathDistance(@Nonnull Node pp) {
        return pp.g;
    }

    public static void setTotalPathDistance(@Nonnull Node pp, float dist) {
        pp.g = dist;
    }

    public static float getDistanceToNext(@Nonnull Node pp) {
        return pp.h;
    }

    public static void setDistanceToNext(@Nonnull Node pp, float dist) {
        pp.h = dist;
    }

    public static void setDistanceToTarget(@Nonnull Node pp, float dist) {
        pp.f = dist;
    }

    public static void setIndex(@Nonnull Node pp, int i) {
        pp.heapIdx = i;
    }
}
