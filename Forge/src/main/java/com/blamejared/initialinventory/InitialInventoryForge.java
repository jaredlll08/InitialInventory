package com.blamejared.initialinventory;


import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("initialinventory")
public class InitialInventoryForge {
    
    public InitialInventoryForge() {
        
        MinecraftForge.EVENT_BUS.register(this);
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
        
        if(event.isEndConquered() || event.getEntity().level.isClientSide()) {
            return;
        }
        InitialInventoryCommon.playerRespawn(event.getEntity());
    }
    
}
