package org.crimsoncrips.alexscavesexemplified.client.event;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.ClientProxy;
import com.github.alexmodguy.alexscaves.client.gui.ACAdvancementTabs;
import com.github.alexmodguy.alexscaves.server.entity.item.BeholderEyeEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.CaniacEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.CorrodentEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.WatcherEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.PossessesCamera;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.level.biome.BiomeSampler;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.crimsoncrips.alexscavesexemplified.config.ACExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.server.effect.ACEEffects;

@OnlyIn(Dist.CLIENT)
public class ACEClientEvents {

    double vibrate = 0;



    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void preRender(RenderLivingEvent.Pre preEvent) {
        if (preEvent.getEntity().hasEffect(ACEEffects.RABIAL.get()) && ACExemplifiedConfig.RABIES_ENABLED) {
            preEvent.getPoseStack().pushPose();
            vibrate = (preEvent.getEntity().getRandom().nextFloat() - 0.5F) * (Math.sin((double) preEvent.getEntity().tickCount / 50) * 0.5 + 0.5) * 0.1;
            if (vibrate >= 0) {
                preEvent.getPoseStack().translate(vibrate,  vibrate, vibrate);
            }
        }
        if (preEvent.getEntity() instanceof WatcherEntity watcherEntity && watcherEntity.tickCount > 10000) {
            preEvent.getPoseStack().pushPose();
            vibrate = (preEvent.getEntity().getRandom().nextFloat() - 0.5F) * (Math.sin((double) preEvent.getEntity().tickCount / 50) * 0.5 + 0.5) * 0.1;
        }
        if (preEvent.getEntity() instanceof CaniacEntity) {
            preEvent.getPoseStack().pushPose();
            vibrate = (preEvent.getEntity().getRandom().nextFloat() - 0.5F) * (Math.sin((double) preEvent.getEntity().tickCount / 50) * 0.5 + 0.5) * 0.1;
            if (vibrate >= 0) {
                preEvent.getPoseStack().translate(vibrate,  vibrate, vibrate);
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void postRender(RenderLivingEvent.Post postEvent) {
        if (postEvent.getEntity().hasEffect(ACEEffects.RABIAL.get())) {
            postEvent.getPoseStack().popPose();
        }
        if (postEvent.getEntity() instanceof WatcherEntity watcherEntity && watcherEntity.tickCount > 10000) {
            postEvent.getPoseStack().popPose();
        }
        if (postEvent.getEntity() instanceof CaniacEntity) {
            postEvent.getPoseStack().popPose();
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {


    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void layerAdd(EntityRenderersEvent.AddLayers addLayers) {
//        LivingEntityRenderer renderer = addLayers.getRenderer(EntityType.ALLAY);
//        if (renderer != null){
//            renderer.addLayer()
//        }
    }

}
