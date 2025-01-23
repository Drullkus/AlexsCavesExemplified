package org.crimsoncrips.alexscavesexemplified.mixins.mobs.base_deep_entity.deep_knight;

import com.github.alexmodguy.alexscaves.client.model.DeepOneKnightModel;
import com.github.alexmodguy.alexscaves.client.render.entity.DeepOneKnightRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneKnightEntity;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DeepOneKnightRenderer.class)
public abstract class ACEDeepKnightRendererMixin extends MobRenderer<DeepOneKnightEntity, DeepOneKnightModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/deep_one/deep_one_knight.png");
    private static final ResourceLocation TEXTURE_NOON = new ResourceLocation("alexscaves:textures/entity/deep_one/deep_one_knight_noon.png");
    private static final ResourceLocation TEXTURE_WILTED = new ResourceLocation("alexscavesexemplified:textures/entity/base_deep_one/wilted_deep_knight.png");
    private static final ResourceLocation TEXTURE_NOON_WILTED = new ResourceLocation("alexscavesexemplified:textures/entity/base_deep_one/wilted_noon_knight.png");


    public ACEDeepKnightRendererMixin(EntityRendererProvider.Context pContext, DeepOneKnightModel pModel, float pShadowRadius) {
        super(pContext, pModel, pShadowRadius);
    }

    public ResourceLocation getTextureLocation(DeepOneKnightEntity entity) {
        boolean wilted = !entity.hasEffect(MobEffects.WATER_BREATHING) && (!entity.level().getBiome(entity.blockPosition()).is(ACBiomeRegistry.ABYSSAL_CHASM) || !entity.isInWaterRainOrBubble());
        return wilted ? (entity.isNoon() ? TEXTURE_NOON_WILTED : TEXTURE_WILTED) : (entity.isNoon() ? TEXTURE_NOON : TEXTURE);
    }


}
