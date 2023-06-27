package com.blamejared.initialinventory.actions;

import com.blamejared.crafttweaker.api.action.base.IUndoableAction;
import com.blamejared.initialinventory.Constants;
import com.blamejared.initialinventory.InitialInventoryCommon;
import com.blamejared.initialinventory.items.StartingItem;

import java.util.ArrayList;
import java.util.List;

public record ActionAddStartingItem(StartingItem item) implements IUndoableAction {
    
    @Override
    public void apply() {
        
        List<StartingItem> list = InitialInventoryCommon.STARTING_ITEMS.computeIfAbsent(item.key(), s -> new ArrayList<>());
        list.add(item);
    }
    
    @Override
    public String describe() {
        
        return String.format("Adding starting item: %s in slot: %s in key: %s", item.stack()
                .getCommandString(), item.index(), item.key());
    }
    
    @Override
    public String systemName() {
        
        return Constants.MOD_NAME;
    }
    
    @Override
    public void undo() {
        
        List<StartingItem> list = InitialInventoryCommon.STARTING_ITEMS.computeIfAbsent(item.key(), s -> new ArrayList<>());
        list.removeIf(itemStackIntegerPair -> itemStackIntegerPair.stack().matches(item.stack()));
    }
    
    @Override
    public String describeUndo() {
        
        return String.format("Removing starting item: %s in slot: %s in key: %s", item.stack()
                .getCommandString(), item.index(), item.key());
    }
    
}