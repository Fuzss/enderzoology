package fuzs.enderzoology.world.entity.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;

public class FlyNodeProcessor extends WalkNodeEvaluator {

    public Node getStart() {
        return this.getNode(Mth.floor(this.mob.getBoundingBox().minX), Mth.floor(this.mob.getBoundingBox().minY + 0.5), Mth.floor(this.mob.getBoundingBox().minZ));
    }

    public Node func_186325_a(double x, double y, double z) {
        return this.getNode(Mth.floor(x - (double)(this.mob.getBbWidth() / 2.0F)), Mth.floor(y), Mth.floor(z - (double)(this.mob.getBbWidth() / 2.0F)));
    }

    public int func_186320_a(Node[] pathOptions, Node currentPoint, Node targetPoint, float maxDistance) {
        Mob entityIn = this.mob;
        int i = 0;
        for (Direction direction : Direction.values()) {
            Node pathpoint = this.getSafePoint(entityIn, currentPoint.x + direction.getStepX(), currentPoint.y + direction.getStepY(), currentPoint.z + direction.getStepZ());
            if (pathpoint != null && !pathpoint.closed && pathpoint.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathpoint;
            }
        }

        return i;
    }

    private Node getSafePoint(Entity entityIn, int x, int y, int z) {
        boolean i = this.entityFits(entityIn, x, y, z);
        return i ? this.getNode(x, y, z) : null;
    }

    private boolean entityFits(Entity entityIn, int x, int y, int z) {
        BlockPos.MutableBlockPos mutableblockpos = new BlockPos.MutableBlockPos();

        for(int i = x; i < x + this.entityWidth; ++i) {
            for(int j = y; j < y + this.entityHeight; ++j) {
                for(int k = z; k < z + this.entityDepth; ++k) {
                    BlockState state = this.level.getBlockState(mutableblockpos.set(i, j, k));
                    if (state.getMaterial() != Material.AIR) {
//                        AABB bb = state.func_185890_d(entityIn.level, mutableblockpos);
//                        if (bb != null) {
//                            return false;
//                        }
                    }
                }
            }
        }

        return true;
    }
}
