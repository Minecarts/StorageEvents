package com.minecarts.storageevents;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;

import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

public class StorageOpen extends StorageEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    
    protected boolean cancelled;
    
    public StorageOpen(Player player, Block block, Inventory inventory) {
        super(player, block, inventory);
    }
    
    
    
    public boolean isCancelled() {
        return cancelled;
    }
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
    
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
