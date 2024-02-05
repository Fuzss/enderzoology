package fuzs.enderzoology.mixin.accessor;

import net.minecraft.world.entity.ai.goal.target.NearestAttackableWitchTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestHealableRaiderTargetGoal;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Witch.class)
public interface WitchAccessor {

    @Accessor("healRaidersGoal")
    NearestHealableRaiderTargetGoal<Raider> enderzoology$getHealRaidersGoal();

    @Accessor("attackPlayersGoal")
    NearestAttackableWitchTargetGoal<Player> enderzoology$getAttackPlayersGoal();
}
