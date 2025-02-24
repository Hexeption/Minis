package uk.co.hexeption.minis.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ArmorSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.hexeption.minis.entity.MiniEntity;
import uk.co.hexeption.minis.init.MenuInit;

import java.util.Map;
import java.util.Objects;

public class MiniInventoryMenu extends AbstractContainerMenu {

    private int entityId;
    private MiniEntity miniEntity;
    private SimpleContainer inventory;

    public static final ResourceLocation EMPTY_ARMOR_SLOT_HELMET = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_helmet");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_CHESTPLATE = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_chestplate");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_LEGGINGS = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_leggings");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_BOOTS = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_boots");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_SHIELD = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_shield");
    private static final Map<EquipmentSlot, ResourceLocation> TEXTURE_EMPTY_SLOTS = Map.of(
            EquipmentSlot.FEET,
            EMPTY_ARMOR_SLOT_BOOTS,
            EquipmentSlot.LEGS,
            EMPTY_ARMOR_SLOT_LEGGINGS,
            EquipmentSlot.CHEST,
            EMPTY_ARMOR_SLOT_CHESTPLATE,
            EquipmentSlot.HEAD,
            EMPTY_ARMOR_SLOT_HELMET
    );
    private static final EquipmentSlot[] SLOT_IDS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public MiniInventoryMenu(int syncId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(syncId, playerInventory, (MiniEntity) Minecraft.getInstance().level.getEntity(buf.readVarInt()));

        this.entityId = buf.readVarInt();

        this.inventory = new SimpleContainer(5);
        this.miniEntity = (MiniEntity) playerInventory.player.level().getEntity(entityId);
        this.miniEntity.syncArmor(inventory);
    }

    public MiniInventoryMenu(int id, Inventory playerInventory, MiniEntity miniEntity) {
        super(MenuInit.MINI_INVENTORY.get(), id);
        this.miniEntity = miniEntity;

        SimpleContainer entityInventory = miniEntity.getInventory();
        checkContainerSize(entityInventory, miniEntity.getInventorySize());
        this.inventory = entityInventory;
        this.miniEntity.syncArmor(inventory);

        this.addPlayerSlots(playerInventory, 8, 84);
        this.addArmorSlots(this.inventory, 8, 8);
    }

    protected void addPlayerSlots(@NotNull Inventory playerInventory, int baseX, int baseY) {
        int i;
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, baseX + j * 18, baseY + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, baseX + i * 18, baseY + 58));
        }
    }



    protected void addArmorSlots(@NotNull Container container, int baseX, int baseY) {
        for (int k = 0; k < 4; k++) {
            EquipmentSlot equipmentslot = SLOT_IDS[k];
            ResourceLocation resourcelocation = TEXTURE_EMPTY_SLOTS.get(equipmentslot);
            this.addSlot(new ArmorSlot(container, miniEntity, equipmentslot, 4 - k, baseX, baseY + k * 18, resourcelocation));
        }
    }


    public MiniEntity getMiniEntity() {
        return miniEntity;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            int i = this.inventory.getContainerSize();
            if (index < i) {
                if (!this.moveItemStackTo(itemstack1, i, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).mayPlace(itemstack1) && !this.getSlot(1).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(0).mayPlace(itemstack1)) {
                if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.miniEntity.isAlive();
    }
}
