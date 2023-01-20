//package fuzs.enderzoology.world.entity.ai.navigation;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.util.Mth;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.Mob;
//import net.minecraft.world.level.PathNavigationRegion;
//import net.minecraft.world.level.pathfinder.*;
//import net.minecraft.world.phys.Vec3;
//
//import javax.annotation.Nonnull;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class FlyingPathFinder extends PathFinder {
//    @Nonnull
//    private final BinaryHeap path = new BinaryHeap();
//    @Nonnull
//    private Node[] pathOptions = new Node[32];
//    @Nonnull
//    private NodeEvaluator nodeProcessor;
//
//    public FlyingPathFinder(NodeEvaluator nodeEvaluator, int maxVisitedNodes) {
//        super(nodeEvaluator, maxVisitedNodes);
//        this.nodeProcessor = nodeEvaluator;
//    }
//
//    public Path findPath(@Nonnull PathNavigationRegion blockaccess, @Nonnull Mob entityFrom, @Nonnull Entity entityTo, float dist) {
//        return this.createEntityPathTo(blockaccess, entityFrom, entityTo.getX(), entityTo.getBoundingBox().minY, entityTo.getZ(), dist);
//    }
//
//    public Path findPath(@Nonnull PathNavigationRegion blockaccess, @Nonnull Mob entityIn, @Nonnull BlockPos targetPos, float dist) {
//        return this.createEntityPathTo(blockaccess, entityIn, (double)((float)targetPos.getX() + 0.5F), (double)((float)targetPos.getY() + 0.5F), (double)((float)targetPos.getZ() + 0.5F), dist);
//    }
//
//    private Path createEntityPathTo(@Nonnull PathNavigationRegion blockaccess, @Nonnull Entity ent, double x, double y, double z, float distance) {
//        this.path.clear();
//        if (!(ent instanceof Mob)) {
//            return null;
//        } else {
//            Mob entityIn = (Mob)ent;
//            this.nodeProcessor.func_186315_a(blockaccess, entityIn);
//            Node startPoint = this.nodeProcessor.getStart();
//            Node endPoint = this.nodeProcessor.func_186325_a(x, y, z);
//            Vec3 targ = new Vec3(x, y, z);
//            Vec3 ePos = entityIn.func_174791_d();
//            double yDelta = targ.y - ePos.y;
//            double horizDist = (new Vec3(x, 0.0, z)).distanceTo(new Vec3(ePos.x, 0.0, ePos.z));
//            int climbY = 0;
//            if (horizDist > 4.0 && entityIn.isOnGround()) {
//                climbY = 1 * Mth.clamp((int)(horizDist / 8.0), 1, 3);
//                if (yDelta >= 1.0) {
//                    climbY += (int)yDelta;
//                } else {
//                    ++climbY;
//                }
//            }
//
//            if (climbY == 0) {
//                return this.createDefault(blockaccess, entityIn, distance, x, y, z);
//            } else {
//                List<Node> resPoints = new ArrayList();
//                double climbDistance = Math.min(horizDist / 2.0, (double)climbY);
//                Vec3 horizDirVec = new Vec3(targ.x, 0.0, targ.z);
//                horizDirVec = horizDirVec.subtract(new Vec3(ePos.x, 0.0, ePos.z));
//                horizDirVec = horizDirVec.normalize();
//                Vec3 offset = new Vec3(horizDirVec.x * climbDistance, (double)climbY, horizDirVec.z * climbDistance);
//                Node climbPoint = new Node(this.rnd((double)startPoint.x + offset.x), this.rnd((double)startPoint.y + offset.y), this.rnd((double)startPoint.z + offset.z));
//                if (!SpawnUtil.isSpaceAvailableForSpawn(entityIn.level, entityIn, false)) {
//                    return this.createDefault(blockaccess, entityIn, distance, x, y, z);
//                } else {
//                    Node[] points = this.addToPath(entityIn, startPoint, climbPoint, distance);
//                    this.nodeProcessor.done();
//                    if (points == null) {
//                        return this.createDefault(blockaccess, entityIn, distance, x, y, z);
//                    } else {
//                        resPoints.addAll(Arrays.asList(points));
//                        this.path.clear();
//                        this.nodeProcessor.func_186315_a(blockaccess, entityIn);
//                        climbPoint = new Node(climbPoint.x, climbPoint.y, climbPoint.z);
//                        points = this.addToPath(entityIn, climbPoint, endPoint, distance);
//                        this.nodeProcessor.done();
//                        if (points == null) {
//                            return this.createDefault(blockaccess, entityIn, distance, x, y, z);
//                        } else {
//                            resPoints.addAll(Arrays.asList(points));
//                            return resPoints.isEmpty() ? null : new Path((Node[])resPoints.toArray(new Node[resPoints.size()]));
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private Node[] addToPath(@Nonnull Entity entityIn, @Nonnull Node pathpointStart, @Nonnull Node pathpointEnd, float maxDistance) {
//        PPUtil.setTotalPathDistance(pathpointStart, 0.0F);
//        float dist = pathpointStart.distanceToSqr(pathpointEnd);
//        PPUtil.setDistanceToNext(pathpointStart, dist);
//        PPUtil.setDistanceToTarget(pathpointStart, dist);
//        PPUtil.setIndex(pathpointStart, -1);
//        this.path.clear();
//        this.path.insert(pathpointStart);
//        Node curPoint = pathpointStart;
//
//        while(!this.path.isEmpty()) {
//            Node dequeued = this.path.pop();
//            if (dequeued.equals(pathpointEnd)) {
//                return createEntityPath(pathpointStart, pathpointEnd);
//            }
//
//            if (dequeued.distanceToSqr(pathpointEnd) < curPoint.distanceToSqr(pathpointEnd)) {
//                curPoint = dequeued;
//            }
//
//            dequeued.closed = true;
//            int numPathOptions = this.nodeProcessor.func_186320_a(this.pathOptions, dequeued, pathpointEnd, maxDistance);
//
//            for(int j = 0; j < numPathOptions; ++j) {
//                Node cadidatePoint = this.pathOptions[j];
//                if (cadidatePoint != null) {
//                    float newTotalDistance = PPUtil.getTotalPathDistance(dequeued) + dequeued.distanceToSqr(cadidatePoint);
//                    if (newTotalDistance < maxDistance * 2.0F && (!cadidatePoint.inOpenSet() || newTotalDistance < PPUtil.getTotalPathDistance(cadidatePoint))) {
//                        PPUtil.setPrevious(cadidatePoint, dequeued);
//                        PPUtil.setTotalPathDistance(cadidatePoint, newTotalDistance);
//                        PPUtil.setDistanceToNext(cadidatePoint, cadidatePoint.distanceToSqr(pathpointEnd));
//                        if (cadidatePoint.inOpenSet()) {
//                            this.path.changeCost(cadidatePoint, PPUtil.getTotalPathDistance(cadidatePoint) + PPUtil.getDistanceToNext(cadidatePoint));
//                        } else {
//                            PPUtil.setDistanceToTarget(cadidatePoint, PPUtil.getTotalPathDistance(cadidatePoint) + PPUtil.getDistanceToNext(cadidatePoint));
//                            this.path.insert(cadidatePoint);
//                        }
//                    }
//                }
//            }
//        }
//
//        if (curPoint == pathpointStart) {
//            return null;
//        } else {
//            return createEntityPath(pathpointStart, curPoint);
//        }
//    }
//
//    private int rnd(double d) {
//        return (int)Math.round(d);
//    }
//
//    private Path createDefault(@Nonnull IBlockAccess blockaccess, @Nonnull Mob entityIn, float distance, double x, double y, double z) {
//        this.path.clear();
//        this.nodeProcessor.func_186315_a(blockaccess, entityIn);
//        Node pathpoint = this.nodeProcessor.getStart();
//        Node pathpoint1 = this.nodeProcessor.func_186325_a(x, y, z);
//        Node[] p = this.addToPath(entityIn, pathpoint, pathpoint1, distance);
//        Path res;
//        if (p == null) {
//            res = null;
//        } else {
//            res = new Path(p);
//        }
//
//        this.nodeProcessor.done();
//        return res;
//    }
//
//    private static Node[] createEntityPath(Node start, Node end) {
//        int i = 1;
//
//        for(Node pathpoint = end; pathpoint != null && PPUtil.getPrevious(pathpoint) != null; pathpoint = PPUtil.getPrevious(pathpoint)) {
//            ++i;
//        }
//
//        Node[] apathpoint = new Node[i];
//        Node pathpoint1 = end;
//        --i;
//
//        for(apathpoint[i] = end; pathpoint1 != null && PPUtil.getPrevious(pathpoint1) != null; apathpoint[i] = pathpoint1) {
//            pathpoint1 = PPUtil.getPrevious(pathpoint1);
//            --i;
//        }
//
//        return apathpoint;
//    }
//}
