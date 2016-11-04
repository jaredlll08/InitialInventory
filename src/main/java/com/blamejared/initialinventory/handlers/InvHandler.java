package com.blamejared.initialinventory.handlers;

import com.blamejared.initialinventory.api.Registry;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseMapAddition;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jared.
 */
@ZenClass("mods.initialinventory.InvHandler")
public class InvHandler {

    @ZenMethod
    public static void addStartingItem(IItemStack item) {
        Map<ItemStack, Integer> map = new HashMap<>();
        map.put(InputHelper.toStack(item), -1);
        MineTweakerAPI.apply(new Add(map));
        System.out.println("adding inv item");
    }

    @ZenMethod
    public static void addStartingItem(IItemStack item, int slotID) {
        Map<ItemStack, Integer> map = new HashMap<>();
        map.put(InputHelper.toStack(item), slotID);
        MineTweakerAPI.apply(new Add(map));
        System.out.println("adding inv item slot");
    }

    private static class Add extends BaseMapAddition<ItemStack, Integer> {

        protected Add(Map<ItemStack, Integer> recipes) {
            super("Initial Inventory", Registry.stacks, recipes);
        }

        @Override
        public void undo() {

        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public String describeUndo() {
            return "";
        }

        @Override
        protected String getRecipeInfo(Map.Entry<ItemStack, Integer> recipe) {
            return LogHelper.getStackDescription(recipe.getKey()) + ":" + recipe.getValue();
        }
    }
}
