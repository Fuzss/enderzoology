package fuzs.enderzoology.world.entity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;

public class EntityAttributeProviders {

    public static AttributeSupplier.Builder createConcussionCreeperAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    public static AttributeSupplier.Builder createEnderInfestedZombieAttributes() {
        return Zombie.createAttributes().add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.0D).add(Attributes.MOVEMENT_SPEED, (double)0.23F).add(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    public static AttributeSupplier.Builder createEnderminyAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 40.0).add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.ATTACK_DAMAGE, 5.0).add(Attributes.FOLLOW_RANGE, 48.0);
    }

    public static AttributeSupplier.Builder createDireWolfAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.32).add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    public static AttributeSupplier.Builder createFallenMountAttributes() {
        return AbstractHorse.createBaseHorseAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.MOVEMENT_SPEED, 0.26).add(Attributes.ATTACK_DAMAGE, 5.0).add(Attributes.FOLLOW_RANGE, 32.0);
    }

    public static AttributeSupplier.Builder createWitherCatAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_DAMAGE, 7.0);
    }

    public static AttributeSupplier.Builder createWitherWitchAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 36.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    public static AttributeSupplier.Builder createOwlAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6.0).add(Attributes.FLYING_SPEED, 0.4).add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    public static AttributeSupplier.Builder createFallenKnightAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D);
    }
}
