package org.crimsoncrips.alexscavesexemplified.server.entity;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.*;
import com.github.alexmodguy.alexscaves.server.entity.living.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

@Mod.EventBusSubscriber(modid = AlexsCavesExemplified.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ACEEntityRegistry {


    public static final DeferredRegister<EntityType<?>> DEF_REG = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AlexsCavesExemplified.MODID);
   public static final RegistryObject<EntityType<GammaBlock>> GAMMA_BLOCK = DEF_REG.register("gamma_block", () -> (EntityType) EntityType.Builder.of(GammaBlock::new, MobCategory.MISC).sized(0.6F, 0.6F).setCustomClientFactory(GammaBlock::new).fireImmune().setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).build("gamma_block"));

}

