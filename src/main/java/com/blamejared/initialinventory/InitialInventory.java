package com.blamejared.initialinventory;


import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

@Mod("initialinventory")
public class InitialInventory {
    
    public static final Map<String, List<Pair<ItemStack, Integer>>> STACK_MAP = new HashMap<>();
    
    public InitialInventory() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void playerLogin(final PlayerEvent.PlayerLoggedInEvent e) {
        CompoundNBT tag = e.getPlayer().getPersistentData();
        STACK_MAP.forEach((s, pairs) -> {
            if(tag.getBoolean("initialinventory_" + s)) {
                return;
            }
            pairs.forEach(pair -> {
                if(pair.getRight() <= -1) {
                    ItemHandlerHelper.giveItemToPlayer(e.getPlayer(), pair.getLeft().copy());
                } else {
                    if(e.getPlayer().inventory.getStackInSlot(pair.getRight()) != ItemStack.EMPTY) {
                        ItemHandlerHelper.giveItemToPlayer(e.getPlayer(), pair.getLeft().copy());
                    } else {
                        e.getPlayer().inventory.setInventorySlotContents(pair.getRight(), pair.getLeft().copy());
                    }
                }
            });
            tag.putBoolean("initialinventory_" + s, true);
        });
    }
    
}
