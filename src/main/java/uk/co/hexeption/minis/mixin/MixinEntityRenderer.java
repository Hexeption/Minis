package uk.co.hexeption.minis.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uk.co.hexeption.minis.entity.MiniEntity;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;

/**
 * MixinEntityRenderer
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 22/05/2021 - 04:38 am
 */
@Mixin(EntityRenderer.class)
public class MixinEntityRenderer<T extends Entity> {

	@Inject(method = "renderName", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/matrix/MatrixStack;scale(FFF)V"))
	private void renderName(T entityIn, ITextComponent displayNameIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, CallbackInfo ci) {
		if(entityIn instanceof MiniEntity){
			matrixStackIn.translate(0.0D, 0.5, 0.0D);
			matrixStackIn.scale(2.2F, 2.2F, 2.2F);
		}
	}

}
