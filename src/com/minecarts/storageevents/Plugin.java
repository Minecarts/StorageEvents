package com.minecarts.storageevents;

import java.util.logging.Level;
import java.text.MessageFormat;

import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.configuration.file.FileConfiguration;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;

import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.Cancellable;

import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.block.*;

public class Plugin extends JavaPlugin implements Listener {
    
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        log("Version {0} enabled.", getDescription().getVersion());
    }
    
    
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        debug("InventoryOpenEvent by {0}", event.getPlayer().getName());
        handleEvent(event);
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        debug("InventoryCloseEvent by {0}", event.getPlayer().getName());
        handleEvent(event);
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        debug("InventoryClickEvent by {0}", event.getView().getPlayer().getName());
        handleEvent(event);
    }
    
    
    private void handleEvent(InventoryEvent event) {
        if(event.getInventory() instanceof DoubleChestInventory) {
            debug("Handling inventory event for each side of double chest");
            handleInventory(event, ((DoubleChestInventory) event.getInventory()).getLeftSide());
            handleInventory(event, ((DoubleChestInventory) event.getInventory()).getRightSide());
        }
        else {
            debug("Handling inventory event");
            handleInventory(event, event.getInventory());
        }
    }
    
    private void handleInventory(InventoryEvent originalEvent, Inventory inventory) {
        InventoryHolder holder = inventory.getHolder();
        debug("Got holder {0}", holder);
        
        if(!(holder instanceof Chest
                || holder instanceof Dispenser
                || holder instanceof Furnace
                || holder instanceof BrewingStand)) return;
        
        Block block = ((BlockState) holder).getBlock();
        debug("Got block {0}", block);
        
        
        if(originalEvent instanceof InventoryOpenEvent) {
            InventoryOpenEvent openEvent = (InventoryOpenEvent) originalEvent;
            if(!(openEvent.getPlayer() instanceof Player)) return;
            
            callEvent(originalEvent, new StorageOpen((Player) openEvent.getPlayer(), block, inventory));
        }
        else if(originalEvent instanceof InventoryCloseEvent) {
            InventoryCloseEvent closeEvent = (InventoryCloseEvent) originalEvent;
            if(!(closeEvent.getPlayer() instanceof Player)) return;
            
            callEvent(originalEvent, new StorageClose((Player) closeEvent.getPlayer(), block, inventory));
        }
        else if(originalEvent instanceof InventoryClickEvent) {
            InventoryClickEvent clickEvent = (InventoryClickEvent) originalEvent;
            
            if(!(clickEvent.getWhoClicked() instanceof Player)) return;
            Player player = (Player) clickEvent.getWhoClicked();
            
            if(clickEvent.getRawSlot() == InventoryView.OUTSIDE) return;
            boolean clickWithinStorage = clickEvent.getRawSlot() < clickEvent.getInventory().getSize();
            
            if(clickEvent.isShiftClick()) { // full item stack/slot movement
                ItemStack slot = clickEvent.getCurrentItem();
                if(slot.getAmount() == 0) return; // empty slot, nothing happens on shift click
                
                if(clickWithinStorage) { // storage slot widthdraws into inventory on shift click
                    callEvent(originalEvent, new StorageWithdraw(player, block, inventory, slot));
                }
                else { // inventory slot deposits into storage on shift click
                    callEvent(originalEvent, new StorageDeposit(player, block, inventory, slot));
                }
            }
            else { // regular inventory interactions
                if(!clickWithinStorage) return; // clicked slot not within storage container
                
                ItemStack cursor = clickEvent.getCursor();
                ItemStack slot = clickEvent.getCurrentItem();
                
                if(cursor.getAmount() == 0) { // nothing on cursor, pick up item from slot
                    if(slot.getAmount() == 0) return; // picking up an empty slot does nothing
                    callEvent(originalEvent, new StorageWithdraw(player, block, inventory, slot));
                }
                else { // cursor has item, drop item into slot
                    if(slot.getAmount() == 0) { // empty slot
                        callEvent(originalEvent, new StorageDeposit(player, block, inventory, cursor));
                    }
                    else if(slot.getType().equals(cursor.getType())) { // slot item matches cursor item, add to stack
                        if(slot.getAmount() >= slot.getMaxStackSize()) return; // full stack in slot
                        callEvent(originalEvent, new StorageDeposit(player, block, inventory, cursor));
                    }
                    else { // non-matching item types, clicking swaps the two
                        callEvent(originalEvent, new StorageDeposit(player, block, inventory, cursor));
                        callEvent(originalEvent, new StorageWithdraw(player, block, inventory, slot));
                    }
                }
            }
        }
    }
    
    private void callEvent(InventoryEvent originalEvent, StorageEvent event) {
        if(originalEvent instanceof Cancellable && event instanceof Cancellable) {
            ((Cancellable) event).setCancelled(((Cancellable) originalEvent).isCancelled());
        }
        getServer().getPluginManager().callEvent(event);
        if(originalEvent instanceof Cancellable && event instanceof Cancellable && ((Cancellable) event).isCancelled()) {
            ((Cancellable) originalEvent).setCancelled(true);
        }
    }
    
    
    
    public void log(String message) {
        log(Level.INFO, message);
    }
    public void log(Level level, String message) {
        getLogger().log(level, message);
    }
    public void log(String message, Object... args) {
        log(MessageFormat.format(message, args));
    }
    public void log(Level level, String message, Object... args) {
        log(level, MessageFormat.format(message, args));
    }
    
    public void debug(String message) {
        log(Level.FINE, message);
    }
    public void debug(String message, Object... args) {
        debug(MessageFormat.format(message, args));
    }
}