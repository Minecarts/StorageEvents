package com.minecarts.storageevents;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;

import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class StorageWithdraw extends StorageEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    
    protected boolean cancelled;
    protected final ItemStack item;
    
    public StorageWithdraw(Player player, Block block, Inventory inventory, ItemStack item) {
        super(player, block, inventory);
        this.item = item;
    }
    
    public ItemStack getItem() {
        return item;
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
