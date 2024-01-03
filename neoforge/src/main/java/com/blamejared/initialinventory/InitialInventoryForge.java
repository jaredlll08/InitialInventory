package com.blamejared.initialinventory;


import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod("initialinventory")
public class InitialInventoryForge {
    
    public InitialInventoryForge() {
        
        NeoForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void playerLogin(final PlayerEvent.PlayerLoggedInEvent e) {
        
        InitialInventoryCommon.playerLogin(
                e.getEntity(),
                () -> e.getEntity().getPersistentData().getCompound(Player.PERSISTED_NBT_TAG),
                compoundTag -> e.getEntity().getPersistentData().put(Player.PERSISTED_NBT_TAG, compoundTag));
    }
    
    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        
        if(event.isEndConquered() || event.getEntity().level().isClientSide()) {
            return;
        }
        InitialInventoryCommon.playerRespawn(event.getEntity());
    }
    
}
