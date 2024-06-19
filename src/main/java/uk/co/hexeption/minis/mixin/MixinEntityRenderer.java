package uk.co.hexeption.minis.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uk.co.hexeption.minis.entity.MiniEntity;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

/**
 * MixinEntityRenderer
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 22/05/2021 - 04:38 am
 */
@Mixin(value = EntityRenderer.class, remap = false)
public class MixinEntityRenderer<T extends Entity> {

	@Inject(method = "renderNameTag", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"))
	private void renderNameTag(T pEntity, Component pDisplayName, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, float pPartialTick, CallbackInfo ci) {
		if(pEntity instanceof MiniEntity){
			pPoseStack.translate(0.0D, 0.5, 0.0D);
			pPoseStack.scale(2.2F, 2.2F, 2.2F);
		}
	}

}
