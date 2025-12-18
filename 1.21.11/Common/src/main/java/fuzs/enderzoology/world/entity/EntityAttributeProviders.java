package fuzs.enderzoology.world.entity;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.zombie.Zombie;

public class EntityAttributeProviders {

    public static AttributeSupplier.Builder createConcussionCreeperAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    public static AttributeSupplier.Builder createEnderInfestedZombieAttributes() {
        return Zombie.createAttributes()
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.0)
                .add(Attributes.ATTACK_DAMAGE, 0.5);
    }

    public static AttributeSupplier.Builder createEnderminyAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.FOLLOW_RANGE, 35.0)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.STEP_HEIGHT, 1.0);
    }

    public static AttributeSupplier.Builder createDireWolfAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 36.0)
                .add(Attributes.FOLLOW_RANGE, 35.0)
                .add(Attributes.ATTACK_DAMAGE, 7.0)
                .add(Attributes.MOVEMENT_SPEED, 0.35);
    }

    public static AttributeSupplier.Builder createFallenMountAttributes() {
        return AbstractHorse.createBaseHorseAttributes()
                .add(Attributes.MAX_HEALTH, 36.0)
                .add(Attributes.FOLLOW_RANGE, 35.0)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    public static AttributeSupplier.Builder createWitherCatAttributes() {
        // they get stuck on things like snow layers without an increased step height (maybe pathfinding breaks for tall / wide mobs?)
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.STEP_HEIGHT, 1.0);
    }

    public static AttributeSupplier.Builder createWitherWitchAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 36.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    public static AttributeSupplier.Builder createOwlAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 6.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.FLYING_SPEED, 0.4);
    }

    public static AttributeSupplier.Builder createFallenKnightAttributes() {
        return AbstractHorse.createBaseHorseAttributes()
                .add(Attributes.MAX_HEALTH, 36.0)
                .add(Attributes.FOLLOW_RANGE, 35.0)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }
}
