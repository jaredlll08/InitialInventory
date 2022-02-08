package com.blamejared.initialinventory.mixin;

import com.blamejared.initialinventory.InitialInventoryCommon;
import com.faux.customentitydata.api.CustomDataHelper;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class MixinPlayerList {
    
    @Inject(method = "placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;initInventoryMenu()V"))
    public void onLogin(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci) {
    
        InitialInventoryCommon.playerLogin(
                serverPlayer,
                () -> CustomDataHelper.getPersistentData(serverPlayer),
                compoundTag -> CustomDataHelper.setPersistentData(serverPlayer, compoundTag));
    }
    
}
