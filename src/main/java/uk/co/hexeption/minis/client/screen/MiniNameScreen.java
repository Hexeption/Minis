package uk.co.hexeption.minis.client.screen;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import uk.co.hexeption.minis.command.MiniCommand;
import uk.co.hexeption.minis.menu.MiniNameMenu;

//? if >=1.21.9
import net.minecraft.client.input.KeyEvent;
//? if >=26.1 {
import net.minecraft.client.gui.GuiGraphicsExtractor;
//?} else {
/*import net.minecraft.client.gui.GuiGraphics;
*///?}

/**
 * MiniNameScreen
 *
 * Asks for a newly hatched Mini's name. Once it's sent, the server opens the Mini's main screen.
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public class MiniNameScreen extends AbstractContainerScreen<MiniNameMenu> {

	private static final int PANEL = 0xFFC6C6C6;
	private static final int LIGHT = 0xFFFFFFFF;
	private static final int DARK = 0xFF555555;

	private EditBox nameBox;
	private Button doneButton;

	public MiniNameScreen(MiniNameMenu menu, Inventory playerInventory, Component title) {
		//? if >=26.1 {
		super(menu, playerInventory, Component.translatable("minis.name.title"), 176, 46);
		//?} else {
		/*super(menu, playerInventory, Component.translatable("minis.name.title"));
		this.imageWidth = 176;
		this.imageHeight = 46;
		*///?}
		this.inventoryLabelY = -1000;
	}

	@Override
	protected void init() {
		super.init();
		this.nameBox = new EditBox(this.font, this.leftPos + 8, this.topPos + 20, 124, 18, Component.translatable("minis.name"));
		this.nameBox.setMaxLength(MiniCommand.MAX_NAME_LENGTH);
		this.addRenderableWidget(this.nameBox);
		this.doneButton = this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), pressed -> this.confirm())
				.bounds(this.leftPos + 136, this.topPos + 19, 32, 20)
				.build());
		this.doneButton.active = false;
		this.setInitialFocus(this.nameBox);
	}

	private void confirm() {
		String name = this.nameBox.getValue().trim();
		if (!name.isEmpty()) {
			this.minecraft.player.connection.sendCommand("minis name " + name);
		}
	}

	@Override
	protected void containerTick() {
		super.containerTick();
		this.doneButton.active = !this.nameBox.getValue().trim().isEmpty();
	}

	// Keys go to the name box instead of closing the screen. Enter sends the name and Escape closes it.
	//? if >=1.21.9 {
	@Override
	public boolean keyPressed(KeyEvent event) {
		if (event.key() == 257 || event.key() == 335) {
			this.confirm();
			return true;
		}
		if (event.key() == 256) {
			return super.keyPressed(event);
		}
		return this.nameBox.keyPressed(event) || this.nameBox.canConsumeInput();
	}
	//?} else {
	/*@Override
	public boolean keyPressed(int key, int scanCode, int modifiers) {
		if (key == 257 || key == 335) {
			this.confirm();
			return true;
		}
		if (key == 256) {
			return super.keyPressed(key, scanCode, modifiers);
		}
		return this.nameBox.keyPressed(key, scanCode, modifiers) || this.nameBox.canConsumeInput();
	}
	*///?}

	//? if >=26.1 {
	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		super.extractBackground(graphics, mouseX, mouseY, partialTick);
		this.drawPanel(graphics::fill);
	}
	//?} else {
	/*//? if <1.20.2 {
	/^@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		this.renderBackground(graphics);
		super.render(graphics, mouseX, mouseY, partialTick);
	}
	^///?}

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		this.drawPanel(graphics::fill);
	}
	*///?}

	private void drawPanel(Filler fill) {
		int x = this.leftPos;
		int y = this.topPos;
		fill.fill(x, y, x + this.imageWidth, y + this.imageHeight, PANEL);
		fill.fill(x, y, x + this.imageWidth - 1, y + 1, LIGHT);
		fill.fill(x, y, x + 1, y + this.imageHeight - 1, LIGHT);
		fill.fill(x + 1, y + this.imageHeight - 1, x + this.imageWidth, y + this.imageHeight, DARK);
		fill.fill(x + this.imageWidth - 1, y + 1, x + this.imageWidth, y + this.imageHeight, DARK);
	}

	@FunctionalInterface
	private interface Filler {
		void fill(int x1, int y1, int x2, int y2, int color);
	}
}
