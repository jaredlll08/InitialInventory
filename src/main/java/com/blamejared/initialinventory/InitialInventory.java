package com.blamejared.initialinventory;

import com.blamejared.initialinventory.events.CommonEventHandler;
import com.blamejared.initialinventory.handlers.InvHandler;
import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.mc1112.brackets.ItemBracketHandler;
import minetweaker.runtime.providers.ScriptProviderDirectory;
import net.minecraft.item.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.io.IOException;

import static com.blamejared.initialinventory.reference.Reference.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, dependencies = DEPENDENCIES)
public class InitialInventory {

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        scriptFile = new File(e.getModConfigurationDirectory(), MOD_NAME + File.separator);
        scriptFile = new File(scriptFile, "inventory.zs");
        if(!scriptFile.exists()) {
            scriptFile.getParentFile().mkdirs();
            try {
                scriptFile.createNewFile();
            } catch(IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        MineTweakerAPI.registerBracketHandler(new ItemBracketHandler());
        ItemBracketHandler.rebuildItemRegistry();
        MineTweakerAPI.registerClass(InvHandler.class);
        MineTweakerAPI.tweaker.setScriptProvider(new ScriptProviderDirectory(scriptFile.getParentFile()));
        MineTweakerImplementationAPI.reload();
        MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
    }
}
