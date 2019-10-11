package com.spikespaz.spigot.enchantablebottles;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class EnchantableBottles extends JavaPlugin {
    private PlayerInteractEventListener listener;

    @Override
    public void onEnable() {
        // Tell the console that the plugin is loaded.
        tellConsole("Enabled Enchantable Bottles.");

        listener = new PlayerInteractEventListener();
        getServer().getPluginManager().registerEvents(listener, this);

    }

    @Override
    public void onDisable() {
        // Inform on unload.
        tellConsole("Disabled Enchantable Bottles.");

        // Unregister the PlayerInteractEventListener
        HandlerList.unregisterAll(listener);
    }

    // Utility function to send a message to the console.
    public void tellConsole(String message){
        Bukkit.getConsoleSender().sendMessage(message);
    }
}
