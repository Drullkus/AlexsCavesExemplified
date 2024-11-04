package org.crimsoncrips.alexscavesexemplified;


import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.crimsoncrips.alexscavesexemplified.config.AMEConfigHolder;
import org.crimsoncrips.alexscavesexemplified.config.AMExemplifiedConfig;
import org.crimsoncrips.alexscavesexemplified.event.AMExemplifiedEvents;

import java.util.Locale;

@Mod(AlexsCavesExemplified.MODID)
public class AlexsCavesExemplified {

    public static final String MODID = "alexscavesexemplified";

    public AlexsCavesExemplified() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onModConfigEvent);
        MinecraftForge.EVENT_BUS.register(new AMExemplifiedEvents());
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::setup);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AMEConfigHolder.EXEMPLIFIED_SPEC, "alexscavesexemplified.toml");

    }



    @SubscribeEvent
    public void onModConfigEvent(final ModConfigEvent event) {
        final ModConfig config = event.getConfig();
        // Rebake the configs when they change
        if (config.getSpec() == AMEConfigHolder.EXEMPLIFIED_SPEC) {
            AMExemplifiedConfig.bake();
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
    }


    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MODID, name.toLowerCase(Locale.ROOT));
    }
}
