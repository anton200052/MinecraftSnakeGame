package me.chiddori.chiddorisnake.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class Utils
{
    public static ItemStack getItem(ItemStack item, String name, List<String> lore)
    {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        List<String> lores = new ArrayList<>();
        for (String s : lore)
        {
            lores.add(ChatColor.translateAlternateColorCodes('&', s));
        }

        meta.setLore(lores);

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getHeadItem(String playerName, String name, List<String> lore)
    {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        skullMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        List<String> lores = new ArrayList<>();

        for (String s : lore)
        {
            lores.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        skullMeta.setLore(lores);

        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }
}
