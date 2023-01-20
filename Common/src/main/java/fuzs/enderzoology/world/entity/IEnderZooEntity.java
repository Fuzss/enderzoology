package fuzs.enderzoology.world.entity;

import fuzs.enderzoology.world.entity.ai.navigation.FlyingPathNavigate;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.monster.Enemy;

public interface IEnderZooEntity {

    enum MobID implements IMobID {
        CCREEPER, DLIME, DWOLF, EMINIY, FKNIGHT, FMOUNT, OWL, WCAT, WWITCH, LCHILD, ESQUID, VSLIME;

        public int getID() {
            return this.ordinal();
        }
    }

    interface IMobID {
        int getID();
    }

    interface Flying extends IEnderZooEntity {
        float getMaxTurnRate();

        float getMaxClimbRate();

        FlyingPathNavigate getFlyingNavigator();

        AmbientCreature asEntityCreature();
    }

    interface Aggressive extends IEnderZooEntity, Enemy {

    }
}
