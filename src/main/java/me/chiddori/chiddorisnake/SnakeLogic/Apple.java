package me.chiddori.chiddorisnake.SnakeLogic;

import me.chiddori.chiddorisnake.ChiddoriSnake;
import me.chiddori.chiddorisnake.Utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Apple extends GameObject
{
    public boolean isAlive = true;
    private static final FileConfiguration config = ChiddoriSnake.getInstance().getConfig();
    private static final ItemStack APPLE_SIGN = Utils.getHeadItem(config.getConfigurationSection("sceneSettings").getConfigurationSection("eatingObj").getString("headOwnerName"), config.getConfigurationSection("sceneSettings").getConfigurationSection("eatingObj").getString("name"), config.getConfigurationSection("sceneSettings").getConfigurationSection("eatingObj").getStringList("lore"));
    private int index = row * SnakeGame.WIDTH + col;
    public Apple(int row, int col)
    {
        super(row, col);
    }

    public void draw (Inventory inventory)
    {
        inventory.setItem(index, APPLE_SIGN);
    }
}
