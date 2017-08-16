package com.blamejared.initialinventory.handlers;

import com.blamejared.initialinventory.api.Registry;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseMapAddition;
import crafttweaker.*;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashMap;
import java.util.Map;

@ZenClass("mods.initialinventory.InvHandler")
@ZenRegister
public class InvHandler {
    
    @ZenMethod
    public static void addStartingItem(IItemStack item) {
        Map<ItemStack, Integer> map = new HashMap<>();
        map.put(InputHelper.toStack(item), -1);
        CraftTweakerAPI.apply(new Add(map));
    }
    
    @ZenMethod
    public static void addStartingItem(IItemStack item, int slotID) {
        Map<ItemStack, Integer> map = new HashMap<>();
        map.put(InputHelper.toStack(item), slotID);
        CraftTweakerAPI.apply(new Add(map));
    }
    
    private static class Add extends BaseMapAddition<ItemStack, Integer> {
        
        protected Add(Map<ItemStack, Integer> recipes) {
            super("Initial Inventory", Registry.stacks, recipes);
        }
        
        @Override
        protected String getRecipeInfo(Map.Entry<ItemStack, Integer> recipe) {
            return LogHelper.getStackDescription(recipe.getKey()) + ":" + recipe.getValue();
        }
    }
}
