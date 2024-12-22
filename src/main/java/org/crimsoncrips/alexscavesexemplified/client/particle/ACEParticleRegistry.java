package org.crimsoncrips.alexscavesexemplified.client.particle;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.crimsoncrips.alexscavesexemplified.AlexsCavesExemplified;

public class ACEParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> DEF_REG = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, AlexsCavesExemplified.MODID);

    public static final RegistryObject<SimpleParticleType> TREMORZILLA_GAMMA_EXPLOSION = DEF_REG.register("tremorzilla_gamma_tectonic_explosion", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> TREMORZILLA_GAMMA_PROTON = DEF_REG.register("tremorzilla_gamma_proton", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> TREMORZILLA_GAMMA_LIGHTNING = DEF_REG.register("tremorzilla_gamma_lightning", () -> new SimpleParticleType(false));

}
