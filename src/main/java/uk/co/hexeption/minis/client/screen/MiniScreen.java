package uk.co.hexeption.minis.client.screen;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
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
	private String name;
	private boolean editing;
	private boolean focusNameBox;
	private EditBox nameBox;
	private Button editButton;
	private Button confirmButton;

	public MiniScreen(MiniMenu menu, Inventory playerInventory, Component title) {
		//? if >=26.1 {
		super(menu, playerInventory, title, 176, 216);
		//?} else {
		/*super(menu, playerInventory, title);
		this.imageWidth = 176;
		this.imageHeight = 216;
		*///?}
		// The name row draws the title itself
		this.titleLabelY = -1000;
		this.inventoryLabelY = 123;
		this.name = title.getString();
	}

	@Override
	protected void init() {
		super.init();
		this.nameBox = new EditBox(this.font, this.leftPos + 8, this.topPos + 5, 144, 12, Component.translatable("minis.name"));
		this.nameBox.setMaxLength(MiniCommand.MAX_NAME_LENGTH);
		this.nameBox.setValue(this.name);
		this.addRenderableWidget(this.nameBox);

		this.editButton = this.addRenderableWidget(Button.builder(Component.literal("\u270E"), pressed -> this.setEditing(true))
				.bounds(this.leftPos + 156, this.topPos + 4, 14, 14)
				.build());
		this.confirmButton = this.addRenderableWidget(Button.builder(Component.literal("\u2714"), pressed -> this.confirmName())
				.bounds(this.leftPos + 156, this.topPos + 4, 14, 14)
				.build());
		this.setEditing(this.editing);

		this.modeButtons.clear();
		for (MiniMode mode : MiniMode.values()) {
			Button button = Button.builder(Component.translatable(mode.translationKey()), pressed -> this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, mode.ordinal()))
					.bounds(this.leftPos + 98, this.topPos + 24 + mode.ordinal() * 20, 72, 18)
					.build();
			this.modeButtons.add(this.addRenderableWidget(button));
		}
	}

	private void setEditing(boolean editing) {
		this.editing = editing;
		this.nameBox.setVisible(editing);
		this.editButton.visible = !editing;
		this.confirmButton.visible = editing;
		if (editing) {
			this.nameBox.setValue(this.name);
			// The screen focuses the clicked button after this runs, so the box takes focus on the next tick
			this.focusNameBox = true;
		} else {
			this.nameBox.setFocused(false);
		}
	}

	// An empty name goes back to the owner's name and skin
	private void confirmName() {
		String name = this.nameBox.getValue().trim();
		if (!name.equals(this.name)) {
			this.minecraft.player.connection.sendCommand(name.isEmpty() ? "minis name" : "minis name " + name);
			this.name = name;
		}
		this.setEditing(false);
	}

	// While editing, keys go to the name box instead of closing the screen. Enter saves and Escape cancels.
	//? if >=1.21.9 {
	@Override
	public boolean keyPressed(KeyEvent event) {
		if (this.editing) {
			return this.handleEditingKey(event.key()) || this.nameBox.keyPressed(event) || this.nameBox.canConsumeInput();
		}
		return super.keyPressed(event);
	}
	//?} else {
	/*@Override
	public boolean keyPressed(int key, int scanCode, int modifiers) {
		if (this.editing) {
			return this.handleEditingKey(key) || this.nameBox.keyPressed(key, scanCode, modifiers) || this.nameBox.canConsumeInput();
		}
		return super.keyPressed(key, scanCode, modifiers);
	}
	*///?}

	private boolean handleEditingKey(int key) {
		if (key == 257 || key == 335) {
			this.confirmName();
			return true;
		}
		if (key == 256) {
			this.setEditing(false);
			return true;
		}
		return false;
	}

	private static final String HEART = "\u2764";

	// One heart per two points of max health, eight to a row
	private void drawHearts(TextDrawer text) {
		LivingEntity mini = this.mini();
		if (mini == null) {
			return;
		}

		int hearts = Mth.ceil(mini.getMaxHealth() / 2.0F);
		float health = mini.getHealth();
		for (int i = 0; i < hearts; i++) {
			float left = health - i * 2;
			int color = left >= 2 ? 0xFFE03030 : left >= 1 ? 0xFFF08080 : 0xFF555555;
			text.draw(HEART, 98 + (i % 8) * 9, 86 + (i / 8) * 9, color);
		}
	}

	@FunctionalInterface
	private interface TextDrawer {
		void draw(String text, int x, int y, int color);
	}

	private String shownName() {
		return this.font.plainSubstrByWidth(this.name, 144);
	}

	//? if >=26.1 {
	@Override
	protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
		super.extractLabels(graphics, mouseX, mouseY);
		if (!this.editing) {
			graphics.text(this.font, this.shownName(), 8, 7, 0xFF404040, false);
		}
		this.drawHearts((text, x, y, color) -> graphics.text(this.font, text, x, y, color, false));
	}
	//?} else {
	/*@Override
	protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
		super.renderLabels(graphics, mouseX, mouseY);
		if (!this.editing) {
			graphics.drawString(this.font, this.shownName(), 8, 7, 0xFF404040, false);
		}
		this.drawHearts((text, x, y, color) -> graphics.drawString(this.font, text, x, y, color, false));
	}
	*///?}

	@Override
	protected void containerTick() {
		super.containerTick();
		if (this.focusNameBox) {
			this.focusNameBox = false;
			this.setFocused(this.nameBox);
			this.nameBox.setFocused(true);
		}
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
