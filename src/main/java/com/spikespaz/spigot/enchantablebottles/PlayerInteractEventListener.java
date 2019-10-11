package com.spikespaz.spigot.enchantablebottles;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.Random;

public class PlayerInteractEventListener implements Listener {
    private static Random random = new Random();

    private boolean isWaterBottle(ItemStack item) {
        // Make sure the item is a potion and that the metadata isn't null.
        if (item.getType().equals(Material.POTION) && item.getItemMeta() != null) {
            PotionMeta meta = (PotionMeta) item.getItemMeta();

            // Return true if the potion type is water.
            return meta.getBasePotionData().getType().equals(PotionType.WATER);
        }

        return false;
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        // Null checks and make sure that the item is a water bottle and the block is enchanting table.
        if (event.getItem() == null || event.getClickedBlock() == null || !isWaterBottle(event.getItem()) || !event.getClickedBlock().getType().equals(Material.ENCHANTING_TABLE))
            return;

        // Cancel before checking for player XP levels because it would seem un-immersive to open the enchantment GUI while holding a bottle.
        event.setCancelled(true);

        // Skip the rest of the code if the player doesn't have enough levels, minimum is 5.
        if (event.getPlayer().getLevel() < 5)
            return;

        // Create a water bottle "potion" to compare to and remove from the player's inventory. It should already exist according to previous conditionals.
        ItemStack waterBottleItem = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) waterBottleItem.getItemMeta();
        assert meta != null;
        meta.setBasePotionData(new PotionData(PotionType.WATER));
        waterBottleItem.setItemMeta(meta);

        event.getPlayer().getInventory().removeItem(waterBottleItem);
        // Spawn a Bottle o' Enchanting above the enchantment table.
        event.getPlayer().getWorld().dropItem(event.getClickedBlock().getLocation().add(0.5, 1, 0.5), new ItemStack(Material.EXPERIENCE_BOTTLE));

        // Experience bottles give between 3 and 11 experience each, below will subtract between 3 and 12 experience. This operates at a bit of a loss.
        event.getPlayer().giveExp(-random.nextInt(10) - 3);
        // Play an XP orb sound between 0.5 and 1 octaves.
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, random.nextFloat() / 2 + 0.5F);
    }
}
