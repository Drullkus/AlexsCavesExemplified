package org.crimsoncrips.alexscavesexemplified.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.common.Mod;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;
import org.crimsoncrips.alexscavesexemplified.datagen.loottables.ACELootGenerator;
import org.crimsoncrips.alexscavesexemplified.datagen.loottables.ACEManualLoot;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;



@Mod.EventBusSubscriber(modid = AlexsCavesExemplified.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ACEDatagen {
    //Props to Drull and TF for assistance//
    public static void generateData(GatherDataEvent event) {
        boolean isServer = event.includeServer();
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        generator.addProvider(isServer, new DatapackBuiltinEntriesProvider(output, provider, ACEFeatures.DATA_BUILDER, Collections.singleton(AlexsCavesExemplified.MODID)));
        generator.addProvider(event.includeServer(), new ACELootGenerator(output));
    }

}
