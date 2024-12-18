package org.crimsoncrips.alexscavesexemplified.client.event;

import com.github.alexmodguy.alexscaves.server.entity.living.WatcherEntity;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.mojang.math.Axis;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.crimsoncrips.alexscavesexemplified.effect.ACEEffects;

@OnlyIn(Dist.CLIENT)
public class ACEClientEvents {

    double vibrate = 0;



    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void preRender(RenderLivingEvent.Pre preEvent) {
        if (preEvent.getEntity().hasEffect(ACEEffects.RABIAL.get()) || (preEvent.getEntity() instanceof WatcherEntity watcherEntity && watcherEntity.tickCount > 10000)) {
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
    }

}
