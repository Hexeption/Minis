package uk.co.hexeption.minis.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.Identifier;
import uk.co.hexeption.minis.client.MiniSkins;
import uk.co.hexeption.minis.entity.MiniEntity;

//? if >=1.21.9 {
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
//?} else if >=1.21.2 {
/*import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
*///?} else {
/*import net.minecraft.client.model.PlayerModel;
import uk.co.hexeption.minis.client.render.entity.layers.CustomCapeLayer;
*///?}
//? if >=1.21.2 {
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.util.Mth;
//?}

/**
 * MiniRenderer
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 13/04/2021 - 04:45 pm
 */
//? if >=1.21.9 {
public class MiniRenderer extends HumanoidMobRenderer<MiniEntity, AvatarRenderState, PlayerModel> {
//?} else if >=1.21.2 {
/*public class MiniRenderer extends HumanoidMobRenderer<MiniEntity, PlayerRenderState, PlayerModel> {
*///?} else {
/*public class MiniRenderer extends HumanoidMobRenderer<MiniEntity, PlayerModel<MiniEntity>> {
*///?}

    // How far a sitting Mini drops so it sits on the ground instead of on thin air, in blocks
    private static final float SITTING_DROP = 0.3125F;

    // Set while the Mini's screen draws it, so it shows full size and without a name tag
    public static boolean renderingInGui;

    public MiniRenderer(EntityRendererProvider.Context context) {
        //? if >=1.21.2 {
        super(context, new PlayerModel(context.bakeLayer(ModelLayers.PLAYER_SLIM), true), 0.25f);
        this.addLayer(new CapeLayer(this, context.getModelSet(), context.getEquipmentAssets()));
        //? if <1.21.9
        /*this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM_INNER_ARMOR)), new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM_OUTER_ARMOR)), context.getEquipmentRenderer()));*/
        //?} else {
        /*super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM), true) {
            @Override
            public void setupAnim(MiniEntity mini, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
                this.riding = mini.isSitting();
                super.setupAnim(mini, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            }
        }, 0.25f);
        this.addLayer(new CustomCapeLayer(this));
        this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM_INNER_ARMOR)), new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM_OUTER_ARMOR)), context.getModelManager()));
        *///?}
    }

    //? if >=1.21.2 {
    //? if >=1.21.9 {
    @Override
    public AvatarRenderState createRenderState() {
        return new AvatarRenderState();
    }

    @Override
    public void extractRenderState(MiniEntity mini, AvatarRenderState state, float partialTick) {
    //?} else {
    /*@Override
    public PlayerRenderState createRenderState() {
        return new PlayerRenderState();
    }

    @Override
    public void extractRenderState(MiniEntity mini, PlayerRenderState state, float partialTick) {
    *///?}
        super.extractRenderState(mini, state, partialTick);
        if (renderingInGui) {
            state.nameTag = null;
        } else {
            state.scale *= 0.5f;
        }
        state.isPassenger = mini.isSitting();
        //? if >=1.21.9 {
        if (mini.isSitting()) {
            state.y -= SITTING_DROP;
        }
        //?}
        state.skin = MiniSkins.skin(mini);
        state.showHat = true;
        state.showJacket = true;
        state.showLeftSleeve = true;
        state.showRightSleeve = true;
        state.showLeftPants = true;
        state.showRightPants = true;
        state.showCape = true;

        double dx = Mth.lerp(partialTick, mini.xCloakO, mini.xCloak) - Mth.lerp(partialTick, mini.xo, mini.getX());
        double dy = Mth.lerp(partialTick, mini.yCloakO, mini.yCloak) - Mth.lerp(partialTick, mini.yo, mini.getY());
        double dz = Mth.lerp(partialTick, mini.zCloakO, mini.zCloak) - Mth.lerp(partialTick, mini.zo, mini.getZ());
        float bodyRot = Mth.rotLerp(partialTick, mini.yBodyRotO, mini.yBodyRot);
        double sin = Mth.sin(bodyRot * (float) (Math.PI / 180.0));
        double cos = -Mth.cos(bodyRot * (float) (Math.PI / 180.0));
        state.capeFlap = Mth.clamp((float) dy * 10.0F, -6.0F, 32.0F) + (mini.isCrouching() ? 25.0F : 0.0F);
        state.capeLean = Mth.clamp((float) (dx * sin + dz * cos) * 100.0F, 0.0F, 150.0F);
        state.capeLean2 = Mth.clamp((float) (dx * cos - dz * sin) * 100.0F, -20.0F, 20.0F);
    }

    //? if >=1.21.9 {
    @Override
    public Identifier getTextureLocation(AvatarRenderState state) {
        return state.skin.body().texturePath();
    }
    //?} else {
    /*@Override
    public Identifier getTextureLocation(PlayerRenderState state) {
        return state.skin.texture();
    }

    @Override
    protected void scale(PlayerRenderState state, PoseStack poseStack) {
        if (state.isPassenger) {
            poseStack.translate(0.0F, SITTING_DROP * 2.0F, 0.0F);
        }
    }
    *///?}
    //?} else {
    /*@Override
    protected void scale(MiniEntity mini, PoseStack poseStack, float partialTick) {
        if (!renderingInGui) {
            poseStack.scale(0.5f, 0.5f, 0.5f);
        }
        if (mini.isSitting()) {
            poseStack.translate(0.0F, SITTING_DROP * 2.0F, 0.0F);
        }
    }

    @Override
    protected boolean shouldShowName(MiniEntity mini) {
        return !renderingInGui && super.shouldShowName(mini);
    }

    @Override
    public Identifier getTextureLocation(MiniEntity mini) {
        //? if >=1.20.2 {
        return MiniSkins.skin(mini).texture();
        //?} else
        /^return MiniSkins.texture(mini);^/
    }
    *///?}
}
