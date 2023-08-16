package com.blamejared.initialinventory.items;


import com.blamejared.crafttweaker.api.item.IItemStack;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;
import java.util.function.BiFunction;

public class StartingItem extends RespawnItem {
    
    private final String key;
    
    public StartingItem(String key, IItemStack stack, BiFunction<IItemStack, Player, Integer> index, BiFunction<IItemStack, Player, IItemStack> onGiven) {
        
        super(stack, index, onGiven);
        this.key = key;
    }
    
    public String key() {
        
        return key;
    }
    
    @Override
    public boolean equals(Object obj) {
        
        if(obj == this) {
            return true;
        }
        if(obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (StartingItem) obj;
        return Objects.equals(this.key, that.key) &&
                Objects.equals(this.stack, that.stack) &&
                this.index == that.index &&
                Objects.equals(this.onGiven, that.onGiven);
    }
    
    @Override
    public int hashCode() {
        
        return Objects.hash(key, stack, index, onGiven);
    }
    
    @Override
    public String toString() {
        
        return "StartingItem[" +
                "key=" + key + ", " +
                "stack=" + stack + ", " +
                "index=" + index + ", " +
                "onGiven=" + onGiven + ']';
    }
    
    
}
