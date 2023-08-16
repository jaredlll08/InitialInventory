package com.blamejared.initialinventory;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.blamejared.initialinventory.actions.ActionAddRespawnItem;
import com.blamejared.initialinventory.actions.ActionAddStartingItem;
import com.blamejared.initialinventory.items.RespawnItem;
import com.blamejared.initialinventory.items.StartingItem;
import net.minecraft.world.entity.player.Player;
import org.openzen.zencode.java.ZenCodeType;

import java.util.function.BiFunction;

@ZenRegister
@ZenCodeType.Name("mods.initialinventory.InvHandler")
@Document("mods/initialinventory/InvHandler")
public class InvHandler {
    
    /**
     * Adds an item to the player's inventory when they join a world.
     *
     * The key is used as a flag that tracks if the player has previously received the item. This flag is used to prevent players from receiving duplicate items by joining the game multiple times.
     * Multiple items may share the same key however it is recommended to give each item a unique key or to group keys based on the pack version.
     *
     * Following these recommendations will ensure that existing players from a previous version of the pack will still receive new items when updating the pack.
     *
     * For example if version 1.0.0 of your pack gives the player a stick and an apple you could give both items the key "1.0.0".
     * Then if you later add a new item like a stone pickaxe in version 1.0.1 of your pack you would give it the key "1.0.1".
     * This will ensure that players who have already played 1.0.0 will receive the stone pickaxe when updating to 1.0.8.
     *
     * The slotIndex is where in the inventory the item will go.
     * By default, the slotIndex will be `-1`, which means that it will try and fill an existing slot with the item, but if the player doesn't have the item, or if there is an overflow of items (if the player has 63 Diamonds and then receives 4 diamonds),
     * then the item is put into the first empty slot in the player's inventory, or spawned in the world around the player.
     *
     * For example, if slot 0 was empty, and slot 1 had a Diamond, and the player then received a Diamond with an index of `-1`, it will go into slot 1, filling the already existing stack, however, if slot 1 already had 64 diamonds, then it will go into slot 0.
     *
     * The onGiven function is used to alter an item before it is given to a player, it can be used to add a level of randomization to the given items, you can change any aspect of the item (like count or Data) and you will have access to a random source through the world you can get from the player.
     *
     * @param key       The key used to determine if the player has already received this item.
     * @param item      The item to give to the player on initial join
     * @param slotIndex The slot to put the item into, or `-1`
     * @param onGiven   The function to modify the item before it is given to the player.
     *
     * @docParam key "one"
     * @docParam item <item:minecraft:diamond_sword>
     * @docParam slotIndex 2
     * @docParam onGiven (stack as crafttweaker.api.item.IItemStack, player as crafttweaker.api.entity.type.player.Player) as crafttweaker.api.item.IItemStack => {
     *      return stack * (player.level.random.nextInt(6) + 1);
     * }
     */
    @ZenCodeType.Method
    public static void addStartingItem(String key, IItemStack item, @ZenCodeType.OptionalInt(-1) int slotIndex, @ZenCodeType.Optional("(stack as crafttweaker.api.item.IItemStack, player as crafttweaker.api.entity.type.player.Player) as crafttweaker.api.item.IItemStack => stack") BiFunction<IItemStack, Player, IItemStack> onGiven) {
        
        CraftTweakerAPI.apply(new ActionAddStartingItem(new StartingItem(key, item, (iItemStack, player) -> slotIndex, onGiven)));
    }
    
    //TODO move to a builder pattern or something
    /**
     * Adds an item to the player's inventory when they join a world.
     *
     * The key is used as a flag that tracks if the player has previously received the item. This flag is used to prevent players from receiving duplicate items by joining the game multiple times.
     * Multiple items may share the same key however it is recommended to give each item a unique key or to group keys based on the pack version.
     *
     * Following these recommendations will ensure that existing players from a previous version of the pack will still receive new items when updating the pack.
     *
     * For example if version 1.0.0 of your pack gives the player a stick and an apple you could give both items the key "1.0.0".
     * Then if you later add a new item like a stone pickaxe in version 1.0.1 of your pack you would give it the key "1.0.1".
     * This will ensure that players who have already played 1.0.0 will receive the stone pickaxe when updating to 1.0.8.
     *
     * The slotIndex is a function that can be used to randomize where in the inventory the item will go.
     * By default, the slotIndex will be `-1`, which means that it will try and fill an existing slot with the item, but if the player doesn't have the item, or if there is an overflow of items (if the player has 63 Diamonds and then receives 4 diamonds),
     * then the item is put into the first empty slot in the player's inventory, or spawned in the world around the player.
     *
     * For example, if slot 0 was empty, and slot 1 had a Diamond, and the player then received a Diamond with an index of `-1`, it will go into slot 1, filling the already existing stack, however, if slot 1 already had 64 diamonds, then it will go into slot 0.
     *
     * The onGiven function is used to alter an item before it is given to a player, it can be used to add a level of randomization to the given items, you can change any aspect of the item (like count or Data) and you will have access to a random source through the world you can get from the player.
     *
     * @param key       The key used to determine if the player has already received this item.
     * @param item      The item to give to the player on initial join
     * @param slotIndex The slot to put the item into, or `-1`
     * @param onGiven   The function to modify the item before it is given to the player.
     *
     * @docParam key "one"
     * @docParam item <item:minecraft:diamond_sword>
     * @docParam slotIndex (stack as IItemStack, player as Player) as int => 20
     * @docParam onGiven (stack as crafttweaker.api.item.IItemStack, player as crafttweaker.api.entity.type.player.Player) as crafttweaker.api.item.IItemStack => {
     *      return stack * (player.level.random.nextInt(6) + 1);
     * }
     */
    @ZenCodeType.Method
    public static void addStartingItem(String key,
                                       IItemStack item,
                                       BiFunction<IItemStack, Player, Integer> slotIndex,
                                       @ZenCodeType.Optional("(stack as crafttweaker.api.item.IItemStack, player as crafttweaker.api.entity.type.player.Player) as crafttweaker.api.item.IItemStack => stack") BiFunction<IItemStack, Player, IItemStack> onGiven) {
        
        CraftTweakerAPI.apply(new ActionAddStartingItem(new StartingItem(key, item, slotIndex, onGiven)));
    }
    
    /**
     * Adds an item to the player's inventory when they respawn after dying.
     *
     * The slotIndex is where in the inventory the item will go.
     * By default, the slotIndex will be `-1`, which means that it will try and fill an existing slot with the item, but if the player doesn't have the item, or if there is an overflow of items (if the player has 63 Diamonds and then receives 4 diamonds),
     * then the item is put into the first empty slot in the player's inventory, or spawned in the world around the player.
     *
     * For example, if slot 0 was empty, and slot 1 had a Diamond, and the player then received a Diamond with an index of `-1`, it will go into slot 1, filling the already existing stack, however, if slot 1 already had 64 diamonds, then it will go into slot 0.
     *
     * The onGiven function is used to alter an item before it is given to a player, it can be used to add a level of randomization to the given items, you can change any aspect of the item (like count or Data) and you will have access to a random source through the world you can get from the player.
     *
     * @param item      The item to give to the player on initial join
     * @param slotIndex The slot to put the item into, or `-1`
     * @param onGiven   The function to modify the item before it is given to the player.
     *
     * @docParam item <item:minecraft:diamond_sword>
     * @docParam slotIndex 2
     * @docParam onGiven (stack as IItemStack, player as Player) as IItemStack => {
     *     return stack.withDisplayName(new crafttweaker.api.text.TranslatableComponent("%s's sword", [player.name]));
     * }
     */
    @ZenCodeType.Method
    public static void addRespawnItem(IItemStack item, @ZenCodeType.OptionalInt(-1) int slotIndex, @ZenCodeType.Optional("(stack as crafttweaker.api.item.IItemStack, player as crafttweaker.api.entity.type.player.Player) as crafttweaker.api.item.IItemStack => stack") BiFunction<IItemStack, Player, IItemStack> onGiven) {
        
        CraftTweakerAPI.apply(new ActionAddRespawnItem(new RespawnItem(item, (iItemStack, player) -> slotIndex, onGiven)));
    }
    
    /**
     * Adds an item to the player's inventory when they respawn after dying.
     *
     * The slotIndex is a function that can be used to randomize where in the inventory the item will go.
     * By default, the slotIndex will be `-1`, which means that it will try and fill an existing slot with the item, but if the player doesn't have the item, or if there is an overflow of items (if the player has 63 Diamonds and then receives 4 diamonds),
     * then the item is put into the first empty slot in the player's inventory, or spawned in the world around the player.
     *
     * For example, if slot 0 was empty, and slot 1 had a Diamond, and the player then received a Diamond with an index of `-1`, it will go into slot 1, filling the already existing stack, however, if slot 1 already had 64 diamonds, then it will go into slot 0.
     *
     * The onGiven function is used to alter an item before it is given to a player, it can be used to add a level of randomization to the given items, you can change any aspect of the item (like count or Data) and you will have access to a random source through the world you can get from the player.
     *
     * @param item      The item to give to the player on initial join
     * @param slotIndex The slot to put the item into, or `-1`
     * @param onGiven   The function to modify the item before it is given to the player.
     *
     * @docParam item <item:minecraft:diamond_sword>
     * @docParam slotIndex (stack as IItemStack, player as Player) as int => 20
     * @docParam onGiven (stack as IItemStack, player as Player) as IItemStack => {
     *     return stack.withDisplayName(new crafttweaker.api.text.TranslatableComponent("%s's sword", [player.name]));
     * }
     */
    @ZenCodeType.Method
    public static void addRespawnItem(IItemStack item,
                                      BiFunction<IItemStack, Player, Integer> slotIndex,
                                      @ZenCodeType.Optional("(stack as crafttweaker.api.item.IItemStack, player as crafttweaker.api.entity.type.player.Player) as crafttweaker.api.item.IItemStack => stack") BiFunction<IItemStack, Player, IItemStack> onGiven) {
        
        CraftTweakerAPI.apply(new ActionAddRespawnItem(new RespawnItem(item, slotIndex, onGiven)));
    }
    
}
