package com.blamejared.initialinventory;


import com.blamejared.crafttweaker.api.item.IItemStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod("initialinventory")
public class InitialInventory {
    
    public static final Map<String, List<InventoryItem>> STACK_MAP = new HashMap<>();
    
    public InitialInventory() {
        
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void playerLogin(final PlayerEvent.PlayerLoggedInEvent e) {
        
        CompoundNBT oldTag = e.getPlayer().getPersistentData();
        CompoundNBT tag = e.getPlayer().getPersistentData().getCompound(PlayerEntity.PERSISTED_NBT_TAG);
        for(String name : oldTag.keySet()) {
            if(name.startsWith("initialinventory_") && !tag.contains(name)) {
                tag.putBoolean(name, oldTag.getBoolean(name));
            }
        }
        
        STACK_MAP.forEach((s, inventoryItems) -> {
            if(tag.getBoolean("initialinventory_" + s)) {
                return;
            }
            inventoryItems.forEach(item -> {
                IItemStack givenIStack = item.getOnGiven().apply(item.getStack().copy(), e.getPlayer());
                ItemStack givenStack = givenIStack.getInternal().copy();
                if(item.getIndex() <= -1) {
                    ItemHandlerHelper.giveItemToPlayer(e.getPlayer(), givenStack);
                } else {
                    if(e.getPlayer().inventory.getStackInSlot(item.getIndex()) != ItemStack.EMPTY) {
                        ItemHandlerHelper.giveItemToPlayer(e.getPlayer(), givenStack);
                    } else {
                        e.getPlayer().inventory.setInventorySlotContents(item.getIndex(), givenStack);
                    }
                }
            });
            tag.putBoolean("initialinventory_" + s, true);
        });
        e.getPlayer().getPersistentData().put(PlayerEntity.PERSISTED_NBT_TAG, tag);
    }
    
}
