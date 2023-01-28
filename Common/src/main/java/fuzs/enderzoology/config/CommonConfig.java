package fuzs.enderzoology.config;

import fuzs.puzzleslib.config.ConfigCore;
import fuzs.puzzleslib.config.annotation.Config;

public class CommonConfig implements ConfigCore {
    @Config
    public final EnderMobConfig concussionCreeper = new EnderMobConfig(20.0, 16.0, 2.0, 0.25);
    @Config
    public final EnderMobConfig infestedZombie = new EnderMobConfig(20.0, 35.0, 5.0, 0.23);
    @Config
    public final EnderMobConfig enderminy = new EnderMobConfig(40.0, 35.0, 5.0, 0.3);
    @Config
    public final EnderMobConfig direWolf = new EnderMobConfig(36.0, 35.0, 7.0, 0.35);
    @Config
    public final EnderMobConfig fallenMount = new EnderMobConfig(36.0, 35.0, 5.0, 0.3);
    @Config
    public final EnderMobConfig witherCat = new EnderMobConfig(10.0, 16.0, 7.0, 0.25);
    @Config
    public final EnderMobConfig witherWitch = new EnderMobConfig(36.0, 16.0, 7.0, 0.25);
    @Config
    public final EnderMobConfig fallenKnight = new EnderMobConfig(36.0, 35.0, 5.0, 0.3);
    @Config
    public final EnderMobConfig owl = new EnderMobConfig(6.0, 16.0, 3.0, 0.2);

    public static class EnderMobConfig implements ConfigCore {
        @Config(description = "Should this mob spawn in the world.")
        public boolean spawning = true;
        @Config(description = "Max health of this mob.")
        @Config.DoubleRange(min = 0.0, max = 1000.0)
        public double maxHealth;
        @Config(description = "Range in blocks where this mob will search for targets.")
        @Config.DoubleRange(min = 4.0, max = 64.0)
        public double followRange;
        @Config(description = "Damage dealt by this mob's melee attacks.")
        @Config.DoubleRange(min = 0.0, max = 100.0)
        public double attackDamage;
        @Config(description = "Movement speed multiplier for this mob.")
        @Config.DoubleRange(min = 0.05, max = 0.5)
        public double movementSpeed;

        public EnderMobConfig(double maxHealth, double followRange, double attackDamage, double movementSpeed) {
            this.maxHealth = maxHealth;
            this.followRange = followRange;
            this.attackDamage = attackDamage;
            this.movementSpeed = movementSpeed;
        }
    }
}
