package fuzs.enderzoology.world.entity.monster;

import java.util.stream.Stream;

public interface PackMob {
    int DONT_FOLLOW_IF_CLOSER_THAN = 3;

    int getMaxSchoolSize();

    boolean isFollower();

    void startFollowing(PackMob leader);

    void stopFollowing();

    void addFollower();

    void removeFollower();

    boolean canBeFollowed();

    boolean hasFollowers();

    boolean inRangeOfLeader();

    boolean tooCloseToLeader();

    void pathToLeader();

    void addFollowers(Stream<? extends PackMob> followers);
}
