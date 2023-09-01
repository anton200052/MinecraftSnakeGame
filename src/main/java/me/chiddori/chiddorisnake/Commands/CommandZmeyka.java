package me.chiddori.chiddorisnake.Commands;

import me.chiddori.chiddorisnake.ChiddoriSnake;
import me.chiddori.chiddorisnake.SnakeLogic.SnakeGame;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandZmeyka implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage("Только в игре!");
            return true;
        }

        Player player = (Player) sender;

        SnakeGame snakeGame = new SnakeGame();
        snakeGame.initialize(player);
        return true;
    }
}
