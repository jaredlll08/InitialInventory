package com.blamejared.initialinventory.events;

import com.blamejared.initialinventory.api.Registry;
import com.blamejared.initialinventory.reference.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.items.ItemHandlerHelper;

public class CommonEventHandler {
	
	public static String givenItems = Reference.MOD_ID + "_given_items";
	
	@SubscribeEvent
	public void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		NBTTagCompound tag = event.player.getEntityData();
		NBTTagCompound data;
		if(tag == null || !tag.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
			data = new NBTTagCompound();
		} else {
			data = tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		}
		if(!data.getBoolean(givenItems)) {
			Registry.stacks.forEach((stack, slot) -> {
				if(slot <= -1) {
					ItemHandlerHelper.giveItemToPlayer(event.player, stack.copy());
				} else {
					if(event.player.inventory.getStackInSlot(slot) != null) {
						ItemHandlerHelper.giveItemToPlayer(event.player, stack.copy());
					} else {
						event.player.inventory.setInventorySlotContents(slot, stack.copy());
					}
				}
				
			});
			data.setBoolean(givenItems, true);
			tag.setTag(EntityPlayer.PERSISTED_NBT_TAG, data);
		}
		
		
	}
	
}
