//? if forgeLike && <1.21.4 {
/*package uk.co.hexeption.minis.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/^*
 * MiniItemBlockEntityRenderer
 *
 * How Forge and NeoForge draw the Mini item before 1.21.4.
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 ^/
public class MiniItemBlockEntityRenderer extends BlockEntityWithoutLevelRenderer {

	public MiniItemBlockEntityRenderer() {
		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffers, int light, int overlay) {
		MiniItemRenderer.render(stack, poseStack, buffers, light);
	}
}
*///?}
