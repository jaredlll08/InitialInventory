package com.blamejared.initialinventory;

import com.blamejared.initialinventory.events.CommonEventHandler;
import com.blamejared.initialinventory.handlers.InvHandler;
import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.mc1102.brackets.ItemBracketHandler;
import minetweaker.runtime.providers.ScriptProviderDirectory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

import static com.blamejared.initialinventory.reference.Reference.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION)
public class InitialInventory {

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        scriptFile = new File(e.getModConfigurationDirectory(), MOD_NAME + File.separator);
        if(!scriptFile.exists()) {
            scriptFile.mkdirs();
        }
        MineTweakerAPI.registerBracketHandler(new ItemBracketHandler());
        ItemBracketHandler.rebuildItemRegistry();
        MineTweakerAPI.registerClass(InvHandler.class);
        MineTweakerAPI.tweaker.setScriptProvider(new ScriptProviderDirectory(scriptFile.getParentFile()));
        MineTweakerImplementationAPI.reload();
        MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

    }
}
