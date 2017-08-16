package com.blamejared.initialinventory;

import com.blamejared.initialinventory.events.CommonEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


import static com.blamejared.initialinventory.reference.Reference.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, dependencies = DEPENDENCIES)
public class InitialInventory {
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
    }
}
