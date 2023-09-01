package me.chiddori.chiddorisnake.SnakeLogic;

import me.chiddori.chiddorisnake.ChiddoriSnake;
import me.chiddori.chiddorisnake.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.security.auth.login.Configuration;
import javax.swing.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SnakeGame implements Listener
{
    private final String invName = ChiddoriSnake.getInstance().getConfig().getConfigurationSection("sceneSettings").getString("invName");
    private final FileConfiguration config = ChiddoriSnake.getInstance().getConfig();

    public final static int HEIGH = 6;
    public final static int WIDTH = 9;

    private Apple apple;
    private Snake snake;
    private Inventory inventory;
    private BukkitTask task;
    private boolean isGameStopped;
    private static final Map<Player, List<ItemStack>> playersInv = new HashMap<>();

    public SnakeGame()
    {
        Bukkit.getPluginManager().registerEvents(this, ChiddoriSnake.getInstance());
    }

    public void initialize(Player player)
    {
        inventory = Bukkit.createInventory(player, WIDTH * HEIGH, invName);
        player.openInventory(inventory);
        createGame(player);
    }

    private void createGame(Player player)
    {
        snake = new Snake(2, 4);
        createNewApple();
        isGameStopped = false;
        savePlayerInv(player);
        clearPlayerInv(player);
        drawControlPanel(player);
        updateScene();
    }

    private void reCreateGame()
    {
        snake = new Snake(2, 4);
        createNewApple();
        isGameStopped = false;
        updateScene();
    }

    public void gameOver()
    {
        task.cancel();
        isGameStopped = true;
    }

    private void updateScene()
    {
        task = ChiddoriSnake.getInstance().getServer().getScheduler().runTaskTimer(ChiddoriSnake.getInstance(), () ->
        {
            snake.move(apple);
            if (!apple.isAlive)
            {
                createNewApple();
            }
            if (!snake.isAlive)
            {
                gameOver();
            }
            drawScene();
        }, 0L, 10L);
    }

    private void drawScene()
    {
        inventory.clear();
        long delay = config.getConfigurationSection("sceneSettings").getBoolean("isDeprecatedVersion") ? 1 : 0;
        ChiddoriSnake.getInstance().getServer().getScheduler().runTaskLater(ChiddoriSnake.getInstance(), () ->
        {
            snake.draw(inventory);
            apple.draw(inventory);
        }, delay);
    }

    private void savePlayerInv(Player player)
    {
        PlayerInventory inv = player.getInventory();
        List<ItemStack> savedInventory = new ArrayList<>();
        for (int i = 0; i < inv.getSize(); i++)
        {
            savedInventory.add(inv.getItem(i));
        }
        playersInv.put(player, savedInventory);
    }

    private void clearPlayerInv(Player player)
    {
        player.getInventory().clear();
    }

    private void createNewApple()
    {
        Apple newApple;
        do
        {
            SecureRandom random = new SecureRandom();
            int row = random.nextInt(HEIGH);
            int col = random.nextInt(WIDTH);
            newApple = new Apple(row, col);
        } while (snake.checkCollision(newApple));
        apple = newApple;
    }

    private void drawControlPanel(Player player)
    {
        PlayerInventory playerInventory = player.getInventory();
        ConfigurationSection buttonsSection = config.getConfigurationSection("controlPanelSettings");
        String materialName = config.getConfigurationSection("controlPanelSettings").getConfigurationSection("itemToFillEmptySlots").getString("itemName");
        Material material = Material.matchMaterial(materialName);


        if (material != null)
        {
            ItemStack item = new ItemStack(Utils.getItem(new ItemStack(material), config.getConfigurationSection("controlPanelSettings").getConfigurationSection("itemToFillEmptySlots").getString("name"), config.getConfigurationSection("controlPanelSettings").getConfigurationSection("itemToFillEmptySlots").getStringList("lore")));

            for (int i = 0; i < playerInventory.getSize() - 5; i++)
            {
                ItemStack currentItem = playerInventory.getItem(i);
                if (currentItem == null)
                {
                    playerInventory.setItem(i, item);
                }
            }
        }

        playerInventory.setItem(19, Utils.getHeadItem(buttonsSection.getConfigurationSection("buttonRestart").getString("headOwnerName"), buttonsSection.getConfigurationSection("buttonRestart").getString("name"), buttonsSection.getConfigurationSection("buttonRestart").getStringList("lore")));
        playerInventory.setItem(25, Utils.getHeadItem(buttonsSection.getConfigurationSection("buttonInfo").getString("headOwnerName"), buttonsSection.getConfigurationSection("buttonInfo").getString("name"), buttonsSection.getConfigurationSection("buttonInfo").getStringList("lore")));
        playerInventory.setItem(13, Utils.getHeadItem(buttonsSection.getConfigurationSection("buttonUp").getString("headOwnerName"), buttonsSection.getConfigurationSection("buttonUp").getString("name"), buttonsSection.getConfigurationSection("buttonUp").getStringList("lore")));
        playerInventory.setItem(21, Utils.getHeadItem(buttonsSection.getConfigurationSection("buttonLeft").getString("headOwnerName"), buttonsSection.getConfigurationSection("buttonLeft").getString("name"), buttonsSection.getConfigurationSection("buttonLeft").getStringList("lore")));
        playerInventory.setItem(23, Utils.getHeadItem(buttonsSection.getConfigurationSection("buttonRight").getString("headOwnerName"), buttonsSection.getConfigurationSection("buttonRight").getString("name"), buttonsSection.getConfigurationSection("buttonRight").getStringList("lore")));
        playerInventory.setItem(31, Utils.getHeadItem(buttonsSection.getConfigurationSection("buttonDown").getString("headOwnerName"), buttonsSection.getConfigurationSection("buttonDown").getString("name"), buttonsSection.getConfigurationSection("buttonDown").getStringList("lore")));
    }

    private void removeControlPanel(Player player)
    {
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.clear();
    }

    private void restorePlayerInv(Player player)
    {
        for (int i = 0; i < player.getInventory().getSize(); i++)
        {
            player.getInventory().setItem(i, playersInv.get(player).get(i));
        }
        playersInv.remove(player);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        Player player = (Player) event.getPlayer();
        Inventory inv = event.getInventory();

        if (inv.equals(inventory) && event.getPlayer().getHealth() > 0)
        {
            gameOver();
            removeControlPanel(player);
            restorePlayerInv(player);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if (!event.getInventory().equals(inventory))
        {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        if (event.getClickedInventory() != null && event.getClickedInventory().equals(player.getInventory()))
        {
            if (slot == 19 && isGameStopped)
            {
                reCreateGame();
            }
            else if (slot == 13)
            {
                snake.setDirection(Direction.DOWN);
            }
            else if (slot == 21)
            {
                snake.setDirection(Direction.LEFT);
            }
            else if (slot == 23)
            {
                snake.setDirection(Direction.RIGHT);
            }
            else if (slot == 31)
            {
                snake.setDirection(Direction.UP);
            }
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        Player player = event.getEntity().getPlayer();

        if (playersInv.containsKey(player))
        {
            event.getDrops().clear();

            for (ItemStack item : playersInv.get(player))
            {
                if (item != null)
                {
                    player.getWorld().dropItemNaturally(player.getLocation(), item);
                }
            }
        }
        playersInv.remove(player);
    }
}
