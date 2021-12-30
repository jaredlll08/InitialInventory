package com.blamejared.initialinventory;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IUndoableAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import net.minecraft.entity.player.PlayerEntity;
import org.openzen.zencode.java.ZenCodeType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

@ZenCodeType.Name("mods.initialinventory.InvHandler")
@ZenRegister
public class InvHandler {
    
    @ZenCodeType.Method
    public static void addStartingItem(String key, IItemStack item, @ZenCodeType.OptionalInt(-1) int slotId, @ZenCodeType.Optional("(stack as crafttweaker.api.item.IItemStack, player as crafttweaker.api.player.MCPlayerEntity) as crafttweaker.api.item.IItemStack => stack") BiFunction<IItemStack, PlayerEntity, IItemStack> onGiven) {
        
        CraftTweakerAPI.apply(new AddItem(new InventoryItem(key, item, slotId, onGiven)));
    }
    
    private static class AddItem implements IUndoableAction {
        
        private final InventoryItem item;
        
        public AddItem(InventoryItem item) {
            
            this.item = item;
        }
        
        @Override
        public void apply() {
            
            List<InventoryItem> list = InitialInventory.STACK_MAP.computeIfAbsent(item.getKey(), s -> new ArrayList<>());
            list.add(item);
        }
        
        @Override
        public String describe() {
            
            return String.format("Adding starting item: %s in slot: %s in key: %s", item.getStack()
                    .getCommandString(), item.getIndex(), item.getKey());
        }
        
        @Override
        public void undo() {
            
            List<InventoryItem> list = InitialInventory.STACK_MAP.computeIfAbsent(item.getKey(), s -> new ArrayList<>());
            list.removeIf(itemStackIntegerPair -> itemStackIntegerPair.getStack().matches(item.getStack()));
        }
        
        @Override
        public String describeUndo() {
            
            return String.format("Removing starting item: %s in slot: %s in key: %s", item.getStack()
                    .getCommandString(), item.getIndex(), item.getKey());
        }
        
    }
    
}
