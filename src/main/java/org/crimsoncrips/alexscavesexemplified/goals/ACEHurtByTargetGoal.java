package org.crimsoncrips.alexscavesexemplified.goals;

import com.github.alexmodguy.alexscaves.server.entity.living.GingerbreadManEntity;
import net.minecraft.network.PacketListener;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;

public class ACEHurtByTargetGoal extends HurtByTargetGoal {

    public ACEHurtByTargetGoal(GingerbreadManEntity gingerbreadManEntity) {
        super(gingerbreadManEntity);
        this.setAlertOthers(GingerbreadManEntity.class);
    }

    protected void alertOther(Mob mob, LivingEntity target) {
        if (mob instanceof GingerbreadManEntity gingerbreadMan && this.mob.hasLineOfSight(target)) {
            if (gingerbreadMan.isOvenSpawned())
                return;
            mob.setTarget(target);
        }

    }
}
