package com.blamejared.initialinventory;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IUndoableAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.impl.item.MCItemStackMutable;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import org.openzen.zencode.java.ZenCodeType;

import java.util.ArrayList;
import java.util.List;

@ZenCodeType.Name("mods.initialinventory.InvHandler")
@ZenRegister
public class InvHandler {
    
    @ZenCodeType.Method
    public static void addStartingItem(String key, IItemStack item, @ZenCodeType.OptionalInt(-1) int slotId) {
        
        CraftTweakerAPI.apply(new AddItem(key, item.getInternal(), slotId));
    }
    
    private static class AddItem implements IUndoableAction {
        
        private final String key;
        private final ItemStack stack;
        private final int index;
        
        public AddItem(String key, ItemStack stack, int index) {
            
            this.key = key;
            this.stack = stack;
            this.index = index;
        }
        
        @Override
        public void apply() {
            
            List<Pair<ItemStack, Integer>> list = InitialInventory.STACK_MAP.computeIfAbsent(key, s -> new ArrayList<>());
            list.add(Pair.of(stack, index));
        }
        
        @Override
        public String describe() {
            
            return String.format("Adding starting item: %s in slot: %s in key: %s", new MCItemStackMutable(stack).getCommandString(), index, key);
        }
        
        @Override
        public void undo() {
            
            List<Pair<ItemStack, Integer>> list = InitialInventory.STACK_MAP.computeIfAbsent(key, s -> new ArrayList<>());
            list.removeIf(itemStackIntegerPair -> new MCItemStackMutable(itemStackIntegerPair.getKey()).matches(new MCItemStackMutable(stack)));
        }
        
        @Override
        public String describeUndo() {
            
            return String.format("Removing starting item: %s in slot: %s in key: %s", new MCItemStackMutable(stack).getCommandString(), index, key);
        }
        
    }
    
}
