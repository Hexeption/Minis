package uk.co.hexeption.minis.client.screen;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import uk.co.hexeption.minis.client.render.entity.MiniRenderer;
import uk.co.hexeption.minis.command.MiniCommand;
import uk.co.hexeption.minis.entity.MiniMode;
import uk.co.hexeption.minis.menu.MiniMenu;

//? if >=1.21.9
import net.minecraft.client.input.KeyEvent;
//? if >=26.1 {
import net.minecraft.client.gui.GuiGraphicsExtractor;
//?} else {
/*import net.minecraft.client.gui.GuiGraphics;
*///?}

/**
 * MiniScreen
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public class MiniScreen extends AbstractContainerScreen<MiniMenu> {

	private static final int PANEL = 0xFFC6C6C6;
	private static final int LIGHT = 0xFFFFFFFF;
	private static final int DARK = 0xFF555555;
	private static final int SLOT = 0xFF8B8B8B;
	private static final int SLOT_SHADOW = 0xFF373737;
	// The preview centres on the Mini's half-size hitbox, so a full-size model is moved up by the missing half
	private static final float MODEL_OFFSET = 0.5125F;

	private final List<Button> modeButtons = new ArrayList<>();
	private EditBox nameBox;
	private String sentName;

	public MiniScreen(MiniMenu menu, Inventory playerInventory, Component title) {
		//? if >=26.1 {
		super(menu, playerInventory, title, 176, 216);
		//?} else {
		/*super(menu, playerInventory, title);
		this.imageWidth = 176;
		this.imageHeight = 216;
		*///?}
		// The name box takes the title's place
		this.titleLabelY = -1000;
		this.inventoryLabelY = 123;
	}

	@Override
	protected void init() {
		super.init();
		this.sentName = this.nameBox == null ? this.title.getString() : this.nameBox.getValue();
		this.nameBox = new EditBox(this.font, this.leftPos + 8, this.topPos + 5, 160, 12, Component.translatable("minis.name"));
		this.nameBox.setMaxLength(MiniCommand.MAX_NAME_LENGTH);
		this.nameBox.setValue(this.sentName);
		this.addRenderableWidget(this.nameBox);

		this.modeButtons.clear();
		for (MiniMode mode : MiniMode.values()) {
			Button button = Button.builder(Component.translatable(mode.translationKey()), pressed -> this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, mode.ordinal()))
					.bounds(this.leftPos + 98, this.topPos + 24 + mode.ordinal() * 20, 72, 18)
					.build();
			this.modeButtons.add(this.addRenderableWidget(button));
		}
	}

	private void sendName() {
		String name = this.nameBox.getValue().trim();
		if (!name.equals(this.sentName)) {
			this.sentName = name;
			this.minecraft.player.connection.sendCommand(name.isEmpty() ? "minis name" : "minis name " + name);
		}
	}

	// Sent before the screen closes, while the server still has the Mini's menu open
	@Override
	public void onClose() {
		this.sendName();
		super.onClose();
	}

	// While typing a name, keys go to the box instead of closing the screen. Enter saves it.
	//? if >=1.21.9 {
	@Override
	public boolean keyPressed(KeyEvent event) {
		int key = event.key();
		if (this.nameBox.isFocused() && key != 256) {
			if (key == 257 || key == 335) {
				this.sendName();
				this.nameBox.setFocused(false);
				return true;
			}
			return this.nameBox.keyPressed(event) || this.nameBox.canConsumeInput();
		}
		return super.keyPressed(event);
	}
	//?} else {
	/*@Override
	public boolean keyPressed(int key, int scanCode, int modifiers) {
		if (this.nameBox.isFocused() && key != 256) {
			if (key == 257 || key == 335) {
				this.sendName();
				this.nameBox.setFocused(false);
				return true;
			}
			return this.nameBox.keyPressed(key, scanCode, modifiers) || this.nameBox.canConsumeInput();
		}
		return super.keyPressed(key, scanCode, modifiers);
	}
	*///?}

	@Override
	protected void containerTick() {
		super.containerTick();
		MiniMode current = this.menu.getMode();
		for (MiniMode mode : MiniMode.values()) {
			this.modeButtons.get(mode.ordinal()).active = mode != current;
		}
	}

	//? if >=26.1 {
	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		super.extractBackground(graphics, mouseX, mouseY, partialTick);
		this.drawPanel(graphics::fill);
		LivingEntity mini = this.mini();
		if (mini != null) {
			MiniRenderer.renderingInGui = true;
			InventoryScreen.extractEntityInInventoryFollowsMouse(graphics, this.leftPos + 26, this.topPos + 24, this.leftPos + 76, this.topPos + 94, 30, MODEL_OFFSET, mouseX, mouseY, mini);
			MiniRenderer.renderingInGui = false;
		}
	}
	//?} else {
	/*//? if <1.20.2 {
	/^@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		this.renderBackground(graphics);
		super.render(graphics, mouseX, mouseY, partialTick);
		this.renderTooltip(graphics, mouseX, mouseY);
	}
	^///?} else {
	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		super.render(graphics, mouseX, mouseY, partialTick);
		this.renderTooltip(graphics, mouseX, mouseY);
	}
	//?}

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		this.drawPanel(graphics::fill);
		LivingEntity mini = this.mini();
		if (mini != null) {
			MiniRenderer.renderingInGui = true;
			//? if >=1.20.2 {
			InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, this.leftPos + 26, this.topPos + 24, this.leftPos + 76, this.topPos + 94, 30, MODEL_OFFSET, mouseX, mouseY, mini);
			//?} else
			/^InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, this.leftPos + 51, this.topPos + 91, 30, (float) (this.leftPos + 51 - mouseX), (float) (this.topPos + 61 - mouseY), mini);^/
			MiniRenderer.renderingInGui = false;
		}
	}
	*///?}

	private LivingEntity mini() {
		Entity entity = this.minecraft.level == null ? null : this.minecraft.level.getEntity(this.menu.getMiniId());
		return entity instanceof LivingEntity living ? living : null;
	}

	private void drawPanel(Filler fill) {
		int x = this.leftPos;
		int y = this.topPos;
		fill.fill(x, y, x + this.imageWidth, y + this.imageHeight, PANEL);
		fill.fill(x, y, x + this.imageWidth - 1, y + 1, LIGHT);
		fill.fill(x, y, x + 1, y + this.imageHeight - 1, LIGHT);
		fill.fill(x + 1, y + this.imageHeight - 1, x + this.imageWidth, y + this.imageHeight, DARK);
		fill.fill(x + this.imageWidth - 1, y + 1, x + this.imageWidth, y + this.imageHeight, DARK);

		// The Mini's preview box
		fill.fill(x + 25, y + 23, x + 77, y + 95, SLOT_SHADOW);
		fill.fill(x + 26, y + 24, x + 77, y + 95, LIGHT);
		fill.fill(x + 26, y + 24, x + 76, y + 94, 0xFF000000);

		for (Slot slot : this.menu.slots) {
			int slotX = x + slot.x - 1;
			int slotY = y + slot.y - 1;
			fill.fill(slotX, slotY, slotX + 18, slotY + 18, SLOT_SHADOW);
			fill.fill(slotX + 1, slotY + 1, slotX + 18, slotY + 18, LIGHT);
			fill.fill(slotX + 1, slotY + 1, slotX + 17, slotY + 17, SLOT);
		}
	}

	@FunctionalInterface
	private interface Filler {
		void fill(int x1, int y1, int x2, int y2, int color);
	}
}
