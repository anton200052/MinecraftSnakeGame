package me.chiddori.chiddorisnake;

import me.chiddori.chiddorisnake.Commands.CommandZmeyka;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChiddoriSnake extends JavaPlugin
{

    private static ChiddoriSnake instance;
    public ChiddoriSnake()
    {
        instance = this;
    }

    public static ChiddoriSnake getInstance()
    {
        return instance;
    }

    @Override
    public void onEnable()
    {
        saveDefaultConfig();
        getCommand("zmeyka".toLowerCase()).setExecutor(new CommandZmeyka());
    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
    }
}
