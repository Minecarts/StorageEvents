package com.minecarts.storageevents;

import org.bukkit.event.HandlerList;

import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

public class StorageClose extends StorageEvent {
    private static final HandlerList handlers = new HandlerList();
    
    public StorageClose(Player player, Block block, Inventory inventory) {
        super(player, block, inventory);
    }
    
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
