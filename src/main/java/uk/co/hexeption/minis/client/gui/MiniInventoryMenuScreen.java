package uk.co.hexeption.minis.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import uk.co.hexeption.minis.Minis;
import uk.co.hexeption.minis.entity.MiniEntity;

public class MiniInventoryMenuScreen extends AbstractContainerScreen<MiniInventoryMenu> {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(Minis.MODID, "textures/gui/container/mini_inventory.png");

    private MiniEntity miniEntity;
    private Player player;
    private boolean isEditingName;
    private EditBox editBox;
    private Button saveButton;

    public MiniInventoryMenuScreen(MiniInventoryMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.miniEntity = menu.getMiniEntity();
        this.player = playerInventory.player;
    }

    @Override
    protected void init() {
        super.init();

        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        this.addRenderableWidget(Button.builder(Component.literal("Edit"), button -> {
            this.isEditingName = !this.isEditingName;
            if (this.isEditingName) {
                this.editBox.setVisible(true);
                this.editBox.setFocused(true);
                this.saveButton.visible = true;
            } else {
                this.editBox.setVisible(false);
                this.saveButton.visible = false;
            }
        }).bounds(i + 80, j + 40, 40, 20).build());

        this.saveButton = Button.builder(Component.literal("Done"), button -> {
            if (this.isEditingName && !this.editBox.getValue().isEmpty()) {
                this.miniEntity.updateSkinAndName(this.editBox.getValue());
                this.isEditingName = false;
            }
            this.editBox.setVisible(false);
            this.saveButton.visible = false;
        }).bounds(i + 80 + 45, j + 40, 40, 20).build();
        this.saveButton.visible = false;

        this.addRenderableWidget(this.saveButton);

        this.editBox = new EditBox(this.font, i + 80, j + 5, 90, 20, Component.literal("Edit Name"));
        this.editBox.setMaxLength(32);
        this.editBox.setVisible(false);
        this.editBox.setValue(this.miniEntity.getDisplayName().getString());

        this.addRenderableWidget(this.editBox);
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (!this.isEditingName){
            guiGraphics.drawString(this.font, this.menu.getMiniEntity().getDisplayName(), 85, 10, 0x404040, false);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(BACKGROUND, i, j, 0, 0, this.imageWidth, this.imageHeight);

        InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, i + 26, j + 8, i + 75, j + 78, 60, -0.5F, mouseX, mouseY, this.menu.getMiniEntity());

        //guiGraphics.fill(i + 26 , j + 8, i + 75 , j + 78 , 0x8000FF00);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.isEditingName) {
            if (keyCode == 256) {
                this.isEditingName = false;
                this.editBox.setVisible(false);
                return true;
            }
            this.editBox.keyPressed(keyCode, scanCode, modifiers);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
