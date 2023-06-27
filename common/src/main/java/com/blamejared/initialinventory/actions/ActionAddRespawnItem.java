package com.blamejared.initialinventory.actions;

import com.blamejared.crafttweaker.api.action.base.IUndoableAction;
import com.blamejared.initialinventory.Constants;
import com.blamejared.initialinventory.InitialInventoryCommon;
import com.blamejared.initialinventory.items.RespawnItem;

public record ActionAddRespawnItem(RespawnItem item) implements IUndoableAction {
    
    @Override
    public void apply() {
        
        InitialInventoryCommon.RESPAWN_ITEMS.add(item);
    }
    
    @Override
    public String describe() {
        
        return String.format("Adding starting item: %s in slot: %s", item.stack()
                .getCommandString(), item.index());
    }
    
    @Override
    public String systemName() {
        
        return Constants.MOD_NAME;
    }
    
    @Override
    public void undo() {
        
        InitialInventoryCommon.RESPAWN_ITEMS.removeIf(itemStackIntegerPair -> itemStackIntegerPair.stack()
                .matches(item.stack()));
    }
    
    @Override
    public String describeUndo() {
        
        return String.format("Removing starting item: %s in slot: %s", item.stack()
                .getCommandString(), item.index());
    }
    
}