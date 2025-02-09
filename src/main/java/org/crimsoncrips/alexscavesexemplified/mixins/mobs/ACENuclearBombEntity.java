package org.crimsoncrips.alexscavesexemplified.mixins.mobs;

import com.github.alexmodguy.alexscaves.server.entity.item.NuclearBombEntity;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(NuclearBombEntity.class)
public abstract class ACENuclearBombEntity extends Entity {

    protected ACENuclearBombEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexmodguy/alexscaves/server/entity/item/NuclearBombEntity;setTime(I)V"))
    private boolean nearestAttack(NuclearBombEntity instance, int time) {
        if (AlexsCavesExemplified.COMMON_CONFIG.GROUNDED_NUKE_ENABLED.get()){
            return this.onGround();
        }
        return true;
    }

}
