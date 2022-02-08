package com.blamejared.initialinventory.items;


import com.blamejared.crafttweaker.api.item.IItemStack;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;
import java.util.function.BiFunction;

public class RespawnItem {
    
    protected final IItemStack stack;
    protected final int index;
    protected final BiFunction<IItemStack, Player, IItemStack> onGiven;
    
    public RespawnItem(IItemStack stack, int index, BiFunction<IItemStack, Player, IItemStack> onGiven) {
        
        this.stack = stack;
        this.index = index;
        this.onGiven = onGiven == null ? (originalStack, player) -> originalStack : onGiven;
    }
    
    public IItemStack stack() {
        
        return stack;
    }
    
    public int index() {
        
        return index;
    }
    
    public BiFunction<IItemStack, Player, IItemStack> onGiven() {
        
        return onGiven;
    }
    
    @Override
    public boolean equals(Object obj) {
        
        if(obj == this) {
            return true;
        }
        if(obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (RespawnItem) obj;
        return Objects.equals(this.stack, that.stack) &&
                this.index == that.index &&
                Objects.equals(this.onGiven, that.onGiven);
    }
    
    @Override
    public int hashCode() {
        
        return Objects.hash(stack, index, onGiven);
    }
    
    @Override
    public String toString() {
        
        return "RespawnItem[" +
                "stack=" + stack + ", " +
                "index=" + index + ", " +
                "onGiven=" + onGiven + ']';
    }
    
    
}
