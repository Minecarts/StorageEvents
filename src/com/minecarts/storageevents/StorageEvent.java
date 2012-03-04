package com.minecarts.storageevents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

public class StorageEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    
    protected final Player player;
    protected final Block block;
    protected final Inventory inventory;
    
    public StorageEvent(Player player, Block block, Inventory inventory) {
        this.player = player;
        this.block = block;
        this.inventory = inventory;
    }
    
    public Player getPlayer() {
        return player;
    }
    public Block getBlock() {
        return block;
    }
    public Inventory getInventory() {
        return inventory;
    }
    
    
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
