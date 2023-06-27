package com.blamejared.initialinventory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;

public class InitialInventoryFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            if(alive || newPlayer.level().isClientSide()) {
                return;
            }
            InitialInventoryCommon.playerRespawn(newPlayer);
        });
    }
    
}
