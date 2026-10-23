package uk.co.hexeption.minis.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
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

    public MiniRenderer(EntityRendererProvider.Context context) {
        //? if >=1.21.2 {
        super(context, new PlayerModel(context.bakeLayer(ModelLayers.PLAYER_SLIM), true), 0.25f);
        this.addLayer(new CapeLayer(this, context.getModelSet(), context.getEquipmentAssets()));
        //?} else {
        /*super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM), true), 0.25f);
        this.addLayer(new CustomCapeLayer(this));
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
        state.scale *= 0.5f;
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
    *///?}
    //?} else {
    /*@Override
    protected void scale(MiniEntity mini, PoseStack poseStack, float partialTick) {
        poseStack.scale(0.5f, 0.5f, 0.5f);
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
