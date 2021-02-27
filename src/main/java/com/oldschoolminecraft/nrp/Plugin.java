package com.oldschoolminecraft.nrp;

import com.oldschoolminecraft.nrp.handlers.CommandHandler;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.commands.CommandsManager;

public class Plugin extends JavaPlugin
{
    public static Plugin instance;

    private CommandsManager commandsManager;

    public void onEnable()
    {
        instance = this;

        commandsManager = new CommandsManager(this);
        commandsManager.register(new CommandHandler());

        if (!loadDriver()) getServer().getPluginManager().disablePlugin(this);

        getConfiguration().load();

        System.out.println("NRP enabled");
    }

    public void onDisable()
    {
        getConfiguration().save();

        System.out.println("NRP disabled");
    }

    private boolean loadDriver()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return true;
        } catch (Exception ex) {
            System.out.println("Failed to load JDBC driver");
            ex.printStackTrace();
            return false;
        }
    }

    public String generateRandomString(int length)
    {
        String AlphaNumericString = "0123456789abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++)
        {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }
}
