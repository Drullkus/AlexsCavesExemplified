package org.crimsoncrips.alexscavesexemplified;

import com.github.alexmodguy.alexscaves.client.particle.*;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.crimsoncrips.alexscavesexemplified.client.particle.ACEGammaTremorExplosion;
import org.crimsoncrips.alexscavesexemplified.client.particle.ACEGammaTremorLightning;
import org.crimsoncrips.alexscavesexemplified.client.particle.ACEGammaTremorProton;
import org.crimsoncrips.alexscavesexemplified.client.particle.ACEParticleRegistry;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = AlexsCavesExemplified.MODID, value = Dist.CLIENT)
public class ACEClientProxy extends ACECommonProxy {

    public void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setupParticles);
    }
    public void clientInit() {
    }


    public void setupParticles(RegisterParticleProvidersEvent registry) {
        registry.registerSpriteSet(ACEParticleRegistry.TREMORZILLA_GAMMA_EXPLOSION.get(), ACEGammaTremorExplosion.TremorzillaFactory::new);
        registry.registerSpecial(ACEParticleRegistry.TREMORZILLA_GAMMA_PROTON.get(), new ACEGammaTremorProton.Factory());
        registry.registerSpriteSet(ACEParticleRegistry.TREMORZILLA_GAMMA_LIGHTNING.get(), ACEGammaTremorLightning.Factory::new);

    }


}
