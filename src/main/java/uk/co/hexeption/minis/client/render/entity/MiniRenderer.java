package uk.co.hexeption.minis.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import uk.co.hexeption.minis.entity.MiniEntity;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.resources.ResourceLocation;

/**
 * MiniRenderer
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 13/04/2021 - 04:45 pm
 */
public class MiniRenderer extends HumanoidMobRenderer<MiniEntity, PlayerModel<MiniEntity>> {

    public MiniRenderer(EntityRendererProvider.Context p_174557_, boolean normal) {
        super(p_174557_, new PlayerModel<>(p_174557_.bakeLayer(normal ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), false), 0.5f);
    }

    @Override
    public void render(MiniEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.scale(0.5f, 0.5f, 0.5f);
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(MiniEntity entity) {
        return entity.getSkinLocation();
    }
}
