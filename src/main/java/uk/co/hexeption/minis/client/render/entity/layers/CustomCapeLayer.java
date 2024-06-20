package uk.co.hexeption.minis.client.render.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import uk.co.hexeption.minis.Minis;
import uk.co.hexeption.minis.entity.MiniEntity;

public class CustomCapeLayer extends RenderLayer<MiniEntity, PlayerModel<MiniEntity>> {

    public CustomCapeLayer(RenderLayerParent pRenderer) {

        super(pRenderer);
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, MiniEntity pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {


        PlayerSkin playerSkin =  pLivingEntity.getSkin();
        if (playerSkin.capeTexture() != null) {
            pPoseStack.pushPose();
            pPoseStack.translate(0.0F, 0.0F, 0.125F);
            double d0 = Mth.lerp((double)pPartialTick, pLivingEntity.xCloakO, pLivingEntity.xCloak) - Mth.lerp((double)pPartialTick, pLivingEntity.xo, pLivingEntity.getX());
            double d1 = Mth.lerp((double)pPartialTick, pLivingEntity.yCloakO, pLivingEntity.yCloak) - Mth.lerp((double)pPartialTick, pLivingEntity.yo, pLivingEntity.getY());
            double d2 = Mth.lerp((double)pPartialTick, pLivingEntity.zCloakO, pLivingEntity.zCloak) - Mth.lerp((double)pPartialTick, pLivingEntity.zo, pLivingEntity.getZ());
            float f = Mth.rotLerp(pPartialTick, pLivingEntity.yBodyRotO, pLivingEntity.yBodyRot);
            double d3 = (double)Mth.sin(f * (float) (Math.PI / 180.0));
            double d4 = (double)(-Mth.cos(f * (float) (Math.PI / 180.0)));
            float f1 = (float)d1 * 10.0F;
            f1 = Mth.clamp(f1, -6.0F, 32.0F);
            float f2 = (float)(d0 * d3 + d2 * d4) * 100.0F;
            f2 = Mth.clamp(f2, 0.0F, 150.0F);
            float f3 = (float)(d0 * d4 - d2 * d3) * 100.0F;
            f3 = Mth.clamp(f3, -20.0F, 20.0F);
            if (f2 < 0.0F) {
                f2 = 0.0F;
            }

            float f4 = Mth.lerp(pPartialTick, pLivingEntity.oBob, pLivingEntity.bob);
            f1 += Mth.sin(Mth.lerp(pPartialTick, pLivingEntity.walkDistO, pLivingEntity.walkDist) * 6.0F) * 32.0F * f4;
            if (pLivingEntity.isCrouching()) {
                f1 += 25.0F;
            }

            pPoseStack.mulPose(Axis.XP.rotationDegrees(6.0F + f2 / 2.0F + f1));
            pPoseStack.mulPose(Axis.ZP.rotationDegrees(f3 / 2.0F));
            pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F - f3 / 2.0F));
            VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entitySolid(playerSkin.capeTexture()));
            this.getParentModel().renderCloak(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY);
            pPoseStack.popPose();
        }

    }

}
