package com.blamejared.initialinventory;

import com.blamejared.crafttweaker.api.item.IItemStack;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.BiFunction;

public class InventoryItem {
    
    private final String key;
    private final IItemStack stack;
    private final int index;
    private final BiFunction<IItemStack, PlayerEntity, IItemStack> onGiven;
    
    public InventoryItem(String key, IItemStack stack, int index, BiFunction<IItemStack, PlayerEntity, IItemStack> onGiven) {
        
        this.key = key;
        this.stack = stack;
        this.index = index;
        this.onGiven = onGiven == null ? (originalStack, player) -> originalStack : onGiven;
    }
    
    public String getKey() {
        
        return key;
    }
    
    public IItemStack getStack() {
        
        return stack;
    }
    
    public int getIndex() {
        
        return index;
    }
    
    public BiFunction<IItemStack, PlayerEntity, IItemStack> getOnGiven() {
        
        return onGiven;
    }
    
}
