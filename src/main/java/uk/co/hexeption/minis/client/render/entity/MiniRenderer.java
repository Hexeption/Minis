package uk.co.hexeption.minis.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import uk.co.hexeption.minis.entity.MiniEntity;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.util.ResourceLocation;

/**
 * MiniRenderer
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 13/04/2021 - 04:45 pm
 */
public class MiniRenderer extends BipedRenderer<MiniEntity, PlayerModel<MiniEntity>> {

	public MiniRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new PlayerModel<>(0, false), 0.5f);

	}

	@Override
	public void render(MiniEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.scale(0.5f, 0.5f, 0.5f);
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getEntityTexture(MiniEntity entity) {
		return entity.getSkinLocation();
	}
}
