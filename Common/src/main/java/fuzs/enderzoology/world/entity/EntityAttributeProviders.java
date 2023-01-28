package fuzs.enderzoology.world.entity;

import fuzs.enderzoology.EnderZoology;
import fuzs.enderzoology.config.CommonConfig;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;

public class EntityAttributeProviders {

    public static AttributeSupplier.Builder createConcussionCreeperAttributes() {
        CommonConfig.EnderMobConfig mobConfig = EnderZoology.CONFIG.get(CommonConfig.class).concussionCreeper;
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, mobConfig.maxHealth).add(Attributes.FOLLOW_RANGE, mobConfig.followRange).add(Attributes.ATTACK_DAMAGE, mobConfig.attackDamage).add(Attributes.MOVEMENT_SPEED, mobConfig.movementSpeed);
    }

    public static AttributeSupplier.Builder createEnderInfestedZombieAttributes() {
        CommonConfig.EnderMobConfig mobConfig = EnderZoology.CONFIG.get(CommonConfig.class).infestedZombie;
        return Zombie.createAttributes().add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.0).add(Attributes.MAX_HEALTH, mobConfig.maxHealth).add(Attributes.FOLLOW_RANGE, mobConfig.followRange).add(Attributes.ATTACK_DAMAGE, mobConfig.attackDamage).add(Attributes.MOVEMENT_SPEED, mobConfig.movementSpeed);
    }

    public static AttributeSupplier.Builder createEnderminyAttributes() {
        CommonConfig.EnderMobConfig mobConfig = EnderZoology.CONFIG.get(CommonConfig.class).enderminy;
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, mobConfig.maxHealth).add(Attributes.FOLLOW_RANGE, mobConfig.followRange).add(Attributes.ATTACK_DAMAGE, mobConfig.attackDamage).add(Attributes.MOVEMENT_SPEED, mobConfig.movementSpeed);
    }

    public static AttributeSupplier.Builder createDireWolfAttributes() {
        CommonConfig.EnderMobConfig mobConfig = EnderZoology.CONFIG.get(CommonConfig.class).direWolf;
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, mobConfig.maxHealth).add(Attributes.FOLLOW_RANGE, mobConfig.followRange).add(Attributes.ATTACK_DAMAGE, mobConfig.attackDamage).add(Attributes.MOVEMENT_SPEED, mobConfig.movementSpeed);
    }

    public static AttributeSupplier.Builder createFallenMountAttributes() {
        CommonConfig.EnderMobConfig mobConfig = EnderZoology.CONFIG.get(CommonConfig.class).fallenMount;
        return AbstractHorse.createBaseHorseAttributes().add(Attributes.MAX_HEALTH, mobConfig.maxHealth).add(Attributes.FOLLOW_RANGE, mobConfig.followRange).add(Attributes.ATTACK_DAMAGE, mobConfig.attackDamage).add(Attributes.MOVEMENT_SPEED, mobConfig.movementSpeed);
    }

    public static AttributeSupplier.Builder createWitherCatAttributes() {
        CommonConfig.EnderMobConfig mobConfig = EnderZoology.CONFIG.get(CommonConfig.class).witherCat;
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, mobConfig.maxHealth).add(Attributes.FOLLOW_RANGE, mobConfig.followRange).add(Attributes.ATTACK_DAMAGE, mobConfig.attackDamage).add(Attributes.MOVEMENT_SPEED, mobConfig.movementSpeed);
    }

    public static AttributeSupplier.Builder createWitherWitchAttributes() {
        CommonConfig.EnderMobConfig mobConfig = EnderZoology.CONFIG.get(CommonConfig.class).witherWitch;
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, mobConfig.maxHealth).add(Attributes.FOLLOW_RANGE, mobConfig.followRange).add(Attributes.ATTACK_DAMAGE, mobConfig.attackDamage).add(Attributes.MOVEMENT_SPEED, mobConfig.movementSpeed);
    }

    public static AttributeSupplier.Builder createOwlAttributes() {
        CommonConfig.EnderMobConfig mobConfig = EnderZoology.CONFIG.get(CommonConfig.class).owl;
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, mobConfig.maxHealth).add(Attributes.FOLLOW_RANGE, mobConfig.followRange).add(Attributes.ATTACK_DAMAGE, mobConfig.attackDamage).add(Attributes.MOVEMENT_SPEED, mobConfig.movementSpeed).add(Attributes.FLYING_SPEED, 0.4);
    }

    public static AttributeSupplier.Builder createFallenKnightAttributes() {
        CommonConfig.EnderMobConfig mobConfig = EnderZoology.CONFIG.get(CommonConfig.class).fallenKnight;
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, mobConfig.maxHealth).add(Attributes.FOLLOW_RANGE, mobConfig.followRange).add(Attributes.ATTACK_DAMAGE, mobConfig.attackDamage).add(Attributes.MOVEMENT_SPEED, mobConfig.movementSpeed);
    }
}
