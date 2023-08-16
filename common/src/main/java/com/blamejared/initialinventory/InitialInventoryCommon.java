package com.blamejared.initialinventory;


import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.natives.entity.type.player.ExpandPlayer;
import com.blamejared.initialinventory.items.RespawnItem;
import com.blamejared.initialinventory.items.StartingItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class InitialInventoryCommon {
    
    public static final Map<String, List<StartingItem>> STARTING_ITEMS = new HashMap<>();
    public static final List<RespawnItem> RESPAWN_ITEMS = new ArrayList<>();
    
    
    private static void giveItem(Player player, RespawnItem item) {
        
        IItemStack givenIStack = item.onGiven().apply(item.stack().copy(), player).copy();
        Integer index = item.index().apply(givenIStack, player);
        if(index <= -1) {
            ExpandPlayer.give(player, givenIStack, -1);
        } else {
            if(!player.getInventory().getItem(index).isEmpty()) {
                ExpandPlayer.give(player, givenIStack, -1);
            } else {
                ExpandPlayer.give(player, givenIStack, index);
            }
        }
    }
    
    public static void playerRespawn(Player player) {
        
        RESPAWN_ITEMS.forEach((item) -> giveItem(player, item));
    }
    
    
    public static void playerLogin(Player player, Supplier<CompoundTag> persistentData, Consumer<CompoundTag> setPersistentData) {
        
        CompoundTag tag = persistentData.get();
        
        STARTING_ITEMS.forEach((key, inventoryItems) -> {
            String nbtKey = Constants.MOD_ID + "_given_" + key;
            if(tag.getBoolean(nbtKey)) {
                return;
            }
            inventoryItems.forEach(item -> giveItem(player, item));
            tag.putBoolean(nbtKey, true);
        });
        setPersistentData.accept(tag);
    }
    
}