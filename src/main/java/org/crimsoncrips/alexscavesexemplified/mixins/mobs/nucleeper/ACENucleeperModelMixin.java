package org.crimsoncrips.alexscavesexemplified.mixins.mobs.nucleeper;

import com.github.alexmodguy.alexscaves.client.model.NucleeperModel;
import com.github.alexmodguy.alexscaves.server.entity.living.NucleeperEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.NucleeperXtra;
import org.crimsoncrips.alexscavesexemplified.server.goals.ACENucleeperMelee;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(NucleeperModel.class)
public abstract class ACENucleeperModelMixin extends AdvancedEntityModel<NucleeperEntity> {

    @Shadow @Final private AdvancedModelBox lpupil;

    @Shadow @Final private AdvancedModelBox rpupil;

    @Inject(method = "setupAnim(Lcom/github/alexmodguy/alexscaves/server/entity/living/NucleeperEntity;FFFFF)V", at = @At(value = "TAIL"),remap = false)
    private void alexsCavesExemplified$add(NucleeperEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (((NucleeperXtra)entity).alexsCavesExemplified$isDefused()){
            this.lpupil.setPos(lpupil.defaultPositionX,lpupil.defaultPositionY,lpupil.defaultPositionZ + 2);
            this.rpupil.setPos(rpupil.defaultPositionX,rpupil.defaultPositionY,rpupil.defaultPositionZ + 2);
        }
    }
}
