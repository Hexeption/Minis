package uk.co.hexeption.minis.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import uk.co.hexeption.minis.client.render.entity.MiniRenderer;
import uk.co.hexeption.minis.entity.MiniEntity;
import uk.co.hexeption.minis.init.ModEntities;
import uk.co.hexeption.minis.item.MiniItem;

//? if >=1.21.9 {
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
//?} else {
/*import net.minecraft.client.renderer.MultiBufferSource;
*///?}
//? if >=26.1 {
import net.minecraft.client.renderer.state.level.CameraRenderState;
//?} else if >=1.21.9
/*import net.minecraft.client.renderer.state.CameraRenderState;*/
//? if >=1.21.2
import net.minecraft.world.entity.EntitySpawnReason;

/**
 * MiniItemRenderer
 *
 * Draws a picked up Mini as a little figure, using the Mini saved in the item.
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public final class MiniItemRenderer {

	private static final Map<CompoundTag, MiniEntity> MINIS = new HashMap<>();
	// Never added to the level, so give each one an id that can't clash with a real entity
	private static int nextId = -1;

	private MiniItemRenderer() {
	}

	@Nullable
	private static MiniEntity miniFor(ItemStack stack) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.level == null) {
			return null;
		}

		CompoundTag data = MiniItem.data(stack);
		CompoundTag key = data == null ? new CompoundTag() : data;
		MiniEntity mini = MINIS.get(key);
		if (mini == null || mini.level() != minecraft.level) {
			if (MINIS.size() > 64) {
				MINIS.clear();
			}
			//? if >=1.21.2 {
			mini = ModEntities.MINI_ENTITY.get().create(minecraft.level, EntitySpawnReason.LOAD);
			//?} else
			/*mini = ModEntities.MINI_ENTITY.get().create(minecraft.level);*/
			if (mini == null) {
				return null;
			}
			mini.setId(nextId--);
			if (data != null) {
				mini.loadFromItem(data);
			}
			MINIS.put(key, mini);
		}
		return mini;
	}

	// Feet at the bottom middle of the item's block, scaled so a full-size Mini fits inside it
	private static void place(PoseStack poseStack) {
		poseStack.translate(0.5F, 0.0F, 0.5F);
		poseStack.scale(0.5F, 0.5F, 0.5F);
	}

	//? if >=1.21.9 {
	public static void submit(ItemStack stack, PoseStack poseStack, SubmitNodeCollector collector, int light) {
		MiniEntity mini = miniFor(stack);
		if (mini == null) {
			return;
		}

		EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		MiniRenderer.renderingInGui = true;
		EntityRenderState state = dispatcher.extractEntity(mini, 0.0F);
		MiniRenderer.renderingInGui = false;
		state.lightCoords = light;
		state.shadowRadius = 0.0F;

		poseStack.pushPose();
		place(poseStack);
		dispatcher.submit(state, new CameraRenderState(), 0.0D, 0.0D, 0.0D, poseStack, collector);
		poseStack.popPose();
	}
	//?} else {
	/*public static void render(ItemStack stack, PoseStack poseStack, MultiBufferSource buffers, int light) {
		MiniEntity mini = miniFor(stack);
		if (mini == null) {
			return;
		}

		EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		poseStack.pushPose();
		place(poseStack);
		MiniRenderer.renderingInGui = true;
		dispatcher.setRenderShadow(false);
		//? if >=1.21.2 {
		dispatcher.render(mini, 0.0D, 0.0D, 0.0D, 0.0F, poseStack, buffers, light);
		//?} else
		/^dispatcher.render(mini, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F, poseStack, buffers, light);^/
		dispatcher.setRenderShadow(true);
		MiniRenderer.renderingInGui = false;
		poseStack.popPose();
	}
	*///?}
}
