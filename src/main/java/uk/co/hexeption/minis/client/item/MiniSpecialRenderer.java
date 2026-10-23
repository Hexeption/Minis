//? if >=1.21.4 {
package uk.co.hexeption.minis.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemStack;

//? if >=1.21.9 {
import java.util.function.Consumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import org.joml.Vector3fc;
//?} else {
/*import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
*///?}
//? if <26.1
/*import net.minecraft.world.item.ItemDisplayContext;*/
//? if >=1.21.6
import org.joml.Vector3f;
//? if >=1.21.6 && <1.21.9
/*import java.util.Set;*/

/**
 * MiniSpecialRenderer
 *
 * The item model type (minis:mini) that draws a picked up Mini from 1.21.4 on.
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public class MiniSpecialRenderer implements SpecialModelRenderer<ItemStack> {

    @Override
    public ItemStack extractArgument(ItemStack stack) {
        return stack;
    }

    //? if >=26.1 {
    @Override
    public void submit(ItemStack stack, PoseStack poseStack, SubmitNodeCollector collector, int light, int overlay, boolean foil, int outline) {
        MiniItemRenderer.submit(stack, poseStack, collector, light);
    }
    //?} else if >=1.21.9 {
    /*@Override
    public void submit(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, SubmitNodeCollector collector, int light, int overlay, boolean foil, int outline) {
        MiniItemRenderer.submit(stack, poseStack, collector, light);
    }
    *///?} else {
    /*@Override
    public void render(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffers, int light, int overlay, boolean foil) {
        MiniItemRenderer.render(stack, poseStack, buffers, light);
    }
    *///?}

    // The space the figure takes up, used to place the item in hands and frames
    //? if >=1.21.9 {
    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        output.accept(new Vector3f(0.25F, 0.0F, 0.25F));
        output.accept(new Vector3f(0.75F, 0.9F, 0.75F));
    }
    //?} else if >=1.21.6 {
    /*@Override
    public void getExtents(Set<Vector3f> output) {
        output.add(new Vector3f(0.25F, 0.0F, 0.25F));
        output.add(new Vector3f(0.75F, 0.9F, 0.75F));
    }
    *///?}

    //? if >=26.1 {
    public record Unbaked() implements SpecialModelRenderer.Unbaked<ItemStack> {
        public static final MapCodec<Unbaked> CODEC = MapCodec.unit(new Unbaked());

        @Override
        public SpecialModelRenderer<ItemStack> bake(SpecialModelRenderer.BakingContext context) {
            return new MiniSpecialRenderer();
        }

        @Override
        public MapCodec<Unbaked> type() {
            return CODEC;
        }
    }
    //?} else if >=1.21.9 {
    /*public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> CODEC = MapCodec.unit(new Unbaked());

        @Override
        public SpecialModelRenderer<?> bake(SpecialModelRenderer.BakingContext context) {
            return new MiniSpecialRenderer();
        }

        @Override
        public MapCodec<Unbaked> type() {
            return CODEC;
        }
    }
    *///?} else {
    /*public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> CODEC = MapCodec.unit(new Unbaked());

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet models) {
            return new MiniSpecialRenderer();
        }

        @Override
        public MapCodec<Unbaked> type() {
            return CODEC;
        }
    }
    *///?}
}
//?}
