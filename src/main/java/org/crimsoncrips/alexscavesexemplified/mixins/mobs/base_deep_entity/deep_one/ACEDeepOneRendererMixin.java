package org.crimsoncrips.alexscavesexemplified.mixins.mobs.base_deep_entity.deep_one;

import com.github.alexmodguy.alexscaves.client.model.DeepOneModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.client.render.entity.CustomBookEntityRenderer;
import com.github.alexmodguy.alexscaves.client.render.entity.DeepOneRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneEntity;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DeepOneRenderer.class)
public abstract class ACEDeepOneRendererMixin extends MobRenderer<DeepOneEntity, DeepOneModel> implements CustomBookEntityRenderer {

    @Shadow private boolean sepia;

    public ACEDeepOneRendererMixin(EntityRendererProvider.Context pContext, DeepOneModel pModel, float pShadowRadius) {
        super(pContext, pModel, pShadowRadius);
    }

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/deep_one/deep_one.png");
    private static final ResourceLocation TEXTURE_WILTED = new ResourceLocation("alexscavesexemplified:textures/entity/base_deep_one/wilted_deep_one.png");


    protected RenderType getRenderType(DeepOneEntity entity, boolean normal, boolean translucent, boolean outline) {
        boolean wilted = !entity.hasEffect(MobEffects.WATER_BREATHING) && (!entity.level().getBiome(entity.blockPosition()).is(ACBiomeRegistry.ABYSSAL_CHASM) || !entity.isInWaterRainOrBubble());
        return this.sepia ? ACRenderTypes.getBookWidget(wilted ? TEXTURE_WILTED : TEXTURE, true) : super.getRenderType(entity, normal, translucent, outline);
    }

    public ResourceLocation getTextureLocation(DeepOneEntity entity) {
        boolean wilted = !entity.hasEffect(MobEffects.WATER_BREATHING) && (!entity.level().getBiome(entity.blockPosition()).is(ACBiomeRegistry.ABYSSAL_CHASM) || !entity.isInWaterRainOrBubble());
        return wilted ? TEXTURE_WILTED : TEXTURE;
    }


}
