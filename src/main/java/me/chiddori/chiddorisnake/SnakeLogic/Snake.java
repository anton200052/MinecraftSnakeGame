package me.chiddori.chiddorisnake.SnakeLogic;

import me.chiddori.chiddorisnake.ChiddoriSnake;
import me.chiddori.chiddorisnake.Utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.security.auth.login.CredentialException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Snake
{
    public boolean isAlive = true;
    private List<GameObject> snakeParts = new ArrayList<>();
    private static final FileConfiguration config = ChiddoriSnake.getInstance().getConfig();
    private Direction direction = Direction.LEFT;

    private static final ItemStack HEAD_SIGN_ALIVE = Utils.getHeadItem(config.getConfigurationSection("sceneSettings").getConfigurationSection("snakeHeadAlive").getString("headOwnerName"), config.getConfigurationSection("sceneSettings").getConfigurationSection("snakeHeadAlive").getString("name"), config.getConfigurationSection("sceneSettings").getConfigurationSection("snakeHeadAlive").getStringList("lore"));
    private static final ItemStack BODY_SIGN_ALIVE = Utils.getHeadItem(config.getConfigurationSection("sceneSettings").getConfigurationSection("snakeBodyAlive").getString("headOwnerName"), config.getConfigurationSection("sceneSettings").getConfigurationSection("snakeBodyAlive").getString("name"), config.getConfigurationSection("sceneSettings").getConfigurationSection("snakeBodyAlive").getStringList("lore"));
    private static final ItemStack HEAD_SIGN_DEAD = Utils.getHeadItem(config.getConfigurationSection("sceneSettings").getConfigurationSection("snakeHeadDead").getString("headOwnerName"), config.getConfigurationSection("sceneSettings").getConfigurationSection("snakeHeadDead").getString("name"), config.getConfigurationSection("sceneSettings").getConfigurationSection("snakeHeadDead").getStringList("lore"));
    private static final ItemStack BODY_SIGN_DEAD = Utils.getHeadItem(config.getConfigurationSection("sceneSettings").getConfigurationSection("snakeBodyDead").getString("headOwnerName"), config.getConfigurationSection("sceneSettings").getConfigurationSection("snakeBodyDead").getString("name"), config.getConfigurationSection("sceneSettings").getConfigurationSection("snakeBodyDead").getStringList("lore"));

    public Snake(int row, int col)
    {
        Collections.addAll(snakeParts, new GameObject(row, col), new GameObject(row, col + 1), new GameObject(row, col + 2));
    }

    public void draw(Inventory inventory)
    {
        for (int i = 0; i < snakeParts.size(); i++)
        {
            int index = snakeParts.get(i).row * SnakeGame.WIDTH + snakeParts.get(i).col;
            if (isAlive)
            {
                if (i == 0)
                {
                    inventory.setItem(index, HEAD_SIGN_ALIVE);
                    continue;
                }

                inventory.setItem(index, BODY_SIGN_ALIVE);
            }
            else
            {
                if (i == 0)
                {
                    inventory.setItem(index, HEAD_SIGN_DEAD);
                    continue;
                }

                inventory.setItem(index, BODY_SIGN_DEAD);
            }
        }
    }

    public void move(Apple apple)
    {
        GameObject newHead = createNewHead();
        if (newHead.row >= SnakeGame.HEIGH || newHead.row < 0 || newHead.col >= SnakeGame.WIDTH || newHead.col < 0)
        {
            isAlive = false;
            return;
        }

        if (checkCollision(newHead))
        {
            isAlive = false;
            return;
        }

        snakeParts.add(0, newHead);

        if (newHead.row == apple.row && newHead.col == apple.col)
        {
            apple.isAlive = false;
            return;
        }

        removeTail();
    }

    public GameObject createNewHead()
    {
        int headRow = snakeParts.get(0).row;
        int headCol = snakeParts.get(0).col;

        if (direction == Direction.UP)
        {
            return new GameObject(headRow + 1, headCol);
        }
        else if (direction == Direction.DOWN)
        {
            return new GameObject(headRow - 1, headCol);
        }
        else if (direction == Direction.RIGHT)
        {
            return new GameObject(headRow, headCol + 1);
        }
        else
        {
            return new GameObject(headRow, headCol - 1);
        }
    }

    public void removeTail()
    {
        snakeParts.remove(snakeParts.size() - 1);
    }

    public void setDirection(Direction direction)
    {
        if (this.direction == Direction.UP && direction != Direction.DOWN && !(snakeParts.get(0).row == snakeParts.get(1).row) || this.direction == Direction.DOWN && direction != Direction.UP && !(snakeParts.get(0).row == snakeParts.get(1).row) || this.direction == Direction.RIGHT && direction != Direction.LEFT && !(snakeParts.get(0).col == snakeParts.get(1).col) || this.direction == Direction.LEFT && direction != Direction.RIGHT && !(snakeParts.get(0).col == snakeParts.get(1).col))
        {
            this.direction = direction;
        }
    }

    public boolean checkCollision(GameObject gameObject)
    {
        for (GameObject snPart : snakeParts)
        {
            if (gameObject.row == snPart.row && gameObject.col == snPart.col)
            {
                return true;
            }
        }
        return false;
    }
}
