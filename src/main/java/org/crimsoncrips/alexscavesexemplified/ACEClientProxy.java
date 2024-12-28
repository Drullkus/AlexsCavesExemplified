package org.crimsoncrips.alexscavesexemplified;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.crimsoncrips.alexscavesexemplified.client.entity.GammaBlockRenderer;
import org.crimsoncrips.alexscavesexemplified.client.particle.*;
import org.crimsoncrips.alexscavesexemplified.server.entity.ACEEntityRegistry;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = AlexsCavesExemplified.MODID, value = Dist.CLIENT)
public class ACEClientProxy extends ACECommonProxy {

    public void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setupParticles);
    }
    public void clientInit() {
        EntityRenderers.register(ACEEntityRegistry.GAMMA_BLOCK.get(), GammaBlockRenderer::new);
    }


    public void setupParticles(RegisterParticleProvidersEvent registry) {
        registry.registerSpriteSet(ACEParticleRegistry.TREMORZILLA_GAMMA_EXPLOSION.get(), GammaTremorExplosion.TremorzillaFactory::new);
        registry.registerSpecial(ACEParticleRegistry.TREMORZILLA_GAMMA_PROTON.get(), new GammaTremorProton.Factory());
        registry.registerSpriteSet(ACEParticleRegistry.TREMORZILLA_GAMMA_LIGHTNING.get(), GammaTremorLightning.Factory::new);


    }


}
